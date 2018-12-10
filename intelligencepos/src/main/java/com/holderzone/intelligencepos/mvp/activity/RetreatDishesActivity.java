package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.StringUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.RetreatDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.presenter.RetreatDishesPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.RetreatDishesPopup;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.internal.Preconditions;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 退菜界面
 * Created by tcw on 2017/7/6.
 */
public class RetreatDishesActivity extends BaseActivity<RetreatDishesContract.Presenter> implements
        RetreatDishesContract.View, RetreatDishesPopup.OnItemClickListener {

    private static final String KEY_DINING_TABLE = "DINING_TABLE";

    private static final String KEY_SAVE_CUR_NUMBER_HASH_MAP = "SAVE_CUR_NUMBER_HASH_MAP";

    private static final String KEY_SAVE_SALES_ORDER_BATCH_DISHES_GUID = "SAVE_SALES_ORDER_BATCH_DISHES_GUID";

    private static final int REQUEST_CODE_CHANGE_NUMBER = 101;

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.rv_dishes)
    RecyclerView mRvDishes;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    // 需要进行菜品操作的桌台
    private DiningTableE mDiningTableE;

    // rv && adapter
    private CommonAdapter<SalesOrderBatchDishesE> mSalesOrderBatchDishesAdapter;// adapter
    private List<SalesOrderBatchDishesE> mHangedUpSalesOrderBatchDishes = new ArrayList<>();// temp数据源(挂起中菜品)
    private List<SalesOrderBatchDishesE> mCalledUpSalesOrderBatchDishes = new ArrayList<>();// temp数据源(已叫起菜品)
    private List<SalesOrderBatchDishesE> mSalesOrderBatchDishesEs = new ArrayList<>();// adapter数据源
    private Map<String, SalesOrderBatchDishesE> mSalesOrderBatchDishesHashMap = new HashMap<>();// guid和实体对应关系的HashMap
    private Map<String, Double> mMaxNumberHashMap = new HashMap<>();// guid和最大退菜数量对应关系的HashMap
    private Map<String, Double> mCurNumberHashMap = new HashMap<>();// guid和当前退菜数量对应关系的HashMap

    // saveInstanceState when using decimal dialog
    private String mSalesOrderBatchDishesGUID;

    // logic helper
    private boolean mRefreshing;


    public static Intent newIntent(Context context, DiningTableE diningTableE) {
        Intent intent = new Intent(context, RetreatDishesActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_DINING_TABLE, diningTableE);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDiningTableE = extras.getParcelable(KEY_DINING_TABLE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
        mCurNumberHashMap = (Map<String, Double>) savedInstanceState.getSerializable(KEY_SAVE_CUR_NUMBER_HASH_MAP);
        mSalesOrderBatchDishesGUID = savedInstanceState.getString(KEY_SAVE_SALES_ORDER_BATCH_DISHES_GUID);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_retreat_dishes;
    }

    @Override
    protected RetreatDishesContract.Presenter initPresenter() {
        return new RetreatDishesPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化标题
        initTitle();
        // 初始化adapter
        initAdapter();
        // 初始化按钮
        initButton();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (!isRefreshing()) {
            requestDishes();
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SAVE_CUR_NUMBER_HASH_MAP, (Serializable) mCurNumberHashMap);
        outState.putString(KEY_SAVE_SALES_ORDER_BATCH_DISHES_GUID, mSalesOrderBatchDishesGUID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_CHANGE_NUMBER == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                Preconditions.checkNotNull(data, "data==null");
                Bundle extras = data.getExtras();
                double number = extras.getDouble(ChangeRetreatNumberActivity.RESULT_CODE_CHANGED_NUMBER);
                String salesOrderBatchDishesGUID = extras.getString(ChangeRetreatNumberActivity.RESULT_CODE_STORED_GUID);
                int minusStock = extras.getInt(ChangeRetreatNumberActivity.RESULT_CODE_MINUS_STOCK);
                mCurNumberHashMap.put(salesOrderBatchDishesGUID, number);
                mSalesOrderBatchDishesHashMap.get(salesOrderBatchDishesGUID).setIsMinusStock(minusStock);
                mSalesOrderBatchDishesHashMap.get(salesOrderBatchDishesGUID).setBackCount(number);
                // 刷新adapter
                refreshRvDishes();
                // 修改“全选”“确认”按钮状态
                modifyButtonStatus();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(int itemType) {
        switch (itemType) {
            case RetreatDishesPopup.ITEM_TYPE_REFERSH:
                if (!isRefreshing()) {
                    requestDishes();
                }
                break;
            case RetreatDishesPopup.ITEM_TYPE_RETREAT_ALL:
                // 更改选中数据
                if (allSelected()) {
                    mCurNumberHashMap.clear();
                } else {
                    mCurNumberHashMap.clear();
                    mCurNumberHashMap.putAll(mMaxNumberHashMap);
                }
                // 刷新adapter
                refreshRvDishes();
                // 修改“全选”“确认”按钮状态
                modifyButtonStatus();
                break;
            default:
                break;
        }
    }

    /*************************
     * view callback begin
     *************************/

    @Override
    public void onDispose() {
        // 刷新中 复位
        setRefreshing(false);
    }

    @Override
    public void onRequestDishesSucceed(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE) {
        if (arrayOfSalesOrderBatchDishesE.size() == 0) {
            // 切换到空布局
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            // 生成菜品数据
            generateSalesOrderBatchDishes(arrayOfSalesOrderBatchDishesE);
            // 再次判断是否为空
            if (mSalesOrderBatchDishesEs.size() == 0) {
                // 切换到空布局
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                // 切换到内容布局
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                // 生成value为SalesOrderBatchDishesE的HashMap
                generateSalesOrderBatchDishesHashMap();
                // 生成value为CheckCount(即最大退菜数量)的HashMap
                generateMaxNumberHashMap();
                // 修正value为CheckCount(即当前退菜数量)的HashMap
                correctCurNumberHashMap();
                // 刷新adapter
                refreshRvDishes();
                // 修改“全选”“确认”按钮状态
                modifyButtonStatus();
            }
        }
    }

    @Override
    public void onRequestDishesFailed() {
        // do nothing
    }

    @Override
    public void onNetworkError() {
        // 切换到网络错误布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        // 注册点击事件
        Button btnRetry = (Button) mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            if (!isRefreshing()) {
                requestDishes();
            }
        });
    }

    /**************************view callback end**************************/

    /*************************private method begin*************************/

    /**
     * 初始化标题
     */
    private void initTitle() {
        mTitle.setTitleText(mDiningTableE.getName() + " 退菜");
        mTitle.setOnReturnClickListener(this::finish);
        mTitle.setOnMenuClickListener(() -> {
            RetreatDishesPopup retreatDishesPopup = new RetreatDishesPopup(this);
            retreatDishesPopup.setRetreatAllText(allSelected() ? "全不退" : "全退");
            retreatDishesPopup.showOnAnchor(mTitle.findViewById(R.id.ll_menu),
                    RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
        });
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mSalesOrderBatchDishesAdapter = new CommonAdapter<SalesOrderBatchDishesE>(RetreatDishesActivity.this, R.layout.item_retreat, mSalesOrderBatchDishesEs) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderBatchDishesE salesOrderBatchDishesE, int position) {
                // title
                if (0 == position || !salesOrderBatchDishesE.getCheckStatus().equals(mSalesOrderBatchDishesEs.get(position - 1).getCheckStatus())) {
                    holder.setVisible(R.id.ll_statistic, true);
                    holder.setOnClickListener(R.id.ll_statistic, v -> {
                        // do nothing designed
                    });
                    if (salesOrderBatchDishesE.getCheckStatus() == 0) {
                        holder.setText(R.id.tv_statistic, "挂起中（" + mHangedUpSalesOrderBatchDishes.size() + "）");
                        holder.setTextColorRes(R.id.tv_statistic, R.color.tv_text_orange_f4a902);
                        holder.setImageResource(R.id.iv_statistic, R.drawable.dishes_hanged_up_title);
                    } else {
                        holder.setText(R.id.tv_statistic, "已叫起（" + mCalledUpSalesOrderBatchDishes.size() + "）");
                        holder.setTextColorRes(R.id.tv_statistic, R.color.tv_text_red_f56766);
                        holder.setImageResource(R.id.iv_statistic, R.drawable.dishes_called_up_title);
                    }
                } else {
                    holder.setVisible(R.id.ll_statistic, false);
                }
                //
                holder.setVisible(R.id.tv_gift, salesOrderBatchDishesE.getGift() == 1 ? true : false);
                holder.setText(R.id.tv_dish_name, salesOrderBatchDishesE.getDishesE().getSimpleName());
//                holder.setText(R.id.tv_dish_price, getString(R.string.two_decimals, salesOrderBatchDishesE.getPrice()));
                // 设置做法及做法金额
                List<DishesPracticeE> arrayOfDishesPracticeE = salesOrderBatchDishesE.getArrayOfDishesPracticeE();
                String subDishes = salesOrderBatchDishesE.getSubDishes();
                if ((arrayOfDishesPracticeE == null || arrayOfDishesPracticeE.size() == 0) && StringUtils.isEmpty(subDishes)) {
                    holder.setVisible(R.id.tv_practice, false);
//                    holder.setVisible(R.id.tv_practice_price, false);
                } else {
                    if (!StringUtils.isEmpty(subDishes)) {
                        holder.setText(R.id.tv_practice, subDishes);
                    } else {
                        StringBuilder sbMethod = new StringBuilder();
                        int size = arrayOfDishesPracticeE.size();
                        for (int i = 0; i < size; i++) {
                            DishesPracticeE dishesPracticeE = arrayOfDishesPracticeE.get(i);
                            if (i == size - 1) {
                                sbMethod.append(dishesPracticeE.getName());
                            } else {
                                sbMethod.append(dishesPracticeE.getName() + "，");
                            }
                        }
                        holder.setText(R.id.tv_practice, sbMethod.toString());
                    }
                    holder.setVisible(R.id.tv_practice, true);
//                    holder.setText(R.id.tv_practice_price, getString(R.string.two_decimals, salesOrderBatchDishesE.getPracticeSubTotal()));
//                    holder.setVisible(R.id.tv_practice_price, true);
                }
                holder.setText(R.id.tv_order_created_time, salesOrderBatchDishesE.getBatchTime().substring(11, 16) + "下单");
                holder.setText(R.id.tv_dish_count, getString(R.string.two_decimal_str, ArithUtil.stripTrailingZeros(salesOrderBatchDishesE.getCheckCount())));
                Double curNum = mCurNumberHashMap.get(salesOrderBatchDishesE.getSalesOrderBatchDishesGUID());
                holder.setText(R.id.tv_return_num, getString(R.string.two_decimal_str, ArithUtil.stripTrailingZeros(curNum != null ? curNum : 0)));
                holder.setTextColorRes(R.id.tv_return_num, (curNum == null || curNum == 0) ? R.color.btn_text_gray_707070 : R.color.tv_text_red_f56766);
                // 分割线
                if (position == mSalesOrderBatchDishesEs.size() - 1) {
                    holder.setVisible(R.id.divider_dash_line, false);
                    holder.setVisible(R.id.divider_solid_line, false);
                } else if (salesOrderBatchDishesE.getCheckStatus().equals(mSalesOrderBatchDishesEs.get(position + 1).getCheckStatus())) {
                    holder.setVisible(R.id.divider_dash_line, true);
                    holder.setVisible(R.id.divider_solid_line, false);
                } else {
                    holder.setVisible(R.id.divider_solid_line, true);
                    holder.setVisible(R.id.divider_dash_line, false);
                }
//                holder.setVisible(R.id.divider_space_line, position == mSalesOrderBatchDishesEs.size() - 1);
            }
        };
        mSalesOrderBatchDishesAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                SalesOrderBatchDishesE salesOrderBatchDishesE = mSalesOrderBatchDishesEs.get(position);
                mSalesOrderBatchDishesGUID = salesOrderBatchDishesE.getSalesOrderBatchDishesGUID();
                String salesOrderBatchDishesGUID = salesOrderBatchDishesE.getSalesOrderBatchDishesGUID();
                String dishesName = salesOrderBatchDishesE.getDishesE().getSimpleName();
                Double maxCount = salesOrderBatchDishesE.getCheckCount();
                int isHang = salesOrderBatchDishesE.getCheckStatus();
                int isGift = salesOrderBatchDishesE.getGift();
                int minusStock = salesOrderBatchDishesE.getIsMinusStock();
                Double retreatNumber = mCurNumberHashMap.get(salesOrderBatchDishesGUID);
                startActivityForResult(ChangeRetreatNumberActivity.newIntent(RetreatDishesActivity.this, salesOrderBatchDishesGUID, dishesName
                        , maxCount, isHang, isGift, maxCount, minusStock, retreatNumber != null ? retreatNumber : 0.00), REQUEST_CODE_CHANGE_NUMBER);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRvDishes.setAdapter(mSalesOrderBatchDishesAdapter);
        mRvDishes.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 初始化按钮
     */
    private void initButton() {
        // 确认按钮
        mBtnConfirm.setText("选择退单菜品");
        mBtnConfirm.setEnabled(false);
        RxView.clicks(mBtnConfirm).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            List<SalesOrderBatchDishesE> submitArray = new ArrayList<>();
            Set<String> keySet = mSalesOrderBatchDishesHashMap.keySet();
            for (String key : keySet) {
                Double curRetreatNumber = mCurNumberHashMap.get(key);
                if (curRetreatNumber != null && curRetreatNumber > 0.0) {
                    SalesOrderBatchDishesE salesOrderBatchDishesE = new SalesOrderBatchDishesE();
                    salesOrderBatchDishesE.setReturnSalesOrderBatchDishesGUID(key);
                    salesOrderBatchDishesE.setBackCount(curRetreatNumber);
                    salesOrderBatchDishesE.setIsMinusStock(mSalesOrderBatchDishesHashMap.get(key).getIsMinusStock());
                    submitArray.add(salesOrderBatchDishesE);
                }
            }
            startActivity(RetreatReasonActivity.newIntent(this,
                    mDiningTableE.getName(), mDiningTableE.getSalesOrderE().getSalesOrderGUID(),
                    mDiningTableE.getDiningTableGUID(), submitArray));
        });
    }

    /**
     * 请求挂起中&&已叫起菜品数据
     */
    private void requestDishes() {
        // 刷新中 置位
        setRefreshing(true);
        // 请求新的网络数据
        mPresenter.requestDishes(mDiningTableE.getSalesOrderE().getSalesOrderGUID(), null);
    }

    /**
     * 过滤已退完的菜品
     */
    private void generateSalesOrderBatchDishes(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE) {
        List<SalesOrderBatchDishesE> dataFilterRetreatDone = new ArrayList<>();
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : arrayOfSalesOrderBatchDishesE) {
            Double checkCount = salesOrderBatchDishesE.getCheckCount();
            Preconditions.checkNotNull(checkCount, "checkCount==null");
            if (checkCount > 0) {
                dataFilterRetreatDone.add(salesOrderBatchDishesE);
            }
        }
        // 挂起菜品数据构造、叫起菜品数据构造
        mHangedUpSalesOrderBatchDishes.clear();
        mCalledUpSalesOrderBatchDishes.clear();
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : dataFilterRetreatDone) {
            Integer checkStatus = salesOrderBatchDishesE.getCheckStatus();
            Preconditions.checkNotNull(checkStatus, "checkStatus==null");
            if (checkStatus == 0) {
                mHangedUpSalesOrderBatchDishes.add(salesOrderBatchDishesE);
            } else if (checkStatus == 1) {
                mCalledUpSalesOrderBatchDishes.add(salesOrderBatchDishesE);
            }
        }

        mSalesOrderBatchDishesEs.clear();
        mSalesOrderBatchDishesEs.addAll(mHangedUpSalesOrderBatchDishes);
        mSalesOrderBatchDishesEs.addAll(mCalledUpSalesOrderBatchDishes);
    }

    /**
     * 生成value为SalesOrderBatchDishesE的HashMap
     */
    private void generateSalesOrderBatchDishesHashMap() {
        mSalesOrderBatchDishesHashMap.clear();
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : mSalesOrderBatchDishesEs) {
            mSalesOrderBatchDishesHashMap.put(salesOrderBatchDishesE.getSalesOrderBatchDishesGUID(), salesOrderBatchDishesE);
        }
    }

    /**
     * 生成value为CheckCount(即最大退菜数量)的HashMap
     */
    private void generateMaxNumberHashMap() {
        mMaxNumberHashMap.clear();
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : mSalesOrderBatchDishesEs) {
            mMaxNumberHashMap.put(salesOrderBatchDishesE.getSalesOrderBatchDishesGUID(), salesOrderBatchDishesE.getCheckCount());
        }
    }

    /**
     * 修正value为CheckCount(即当前退菜数量)的HashMap
     */
    private void correctCurNumberHashMap() {
        Set<String> keySet = mCurNumberHashMap.keySet();
        for (String key : keySet) {
            SalesOrderBatchDishesE salesOrderBatchDishesE = mSalesOrderBatchDishesHashMap.get(key);
            if (salesOrderBatchDishesE == null) {
                mCurNumberHashMap.remove(key);
            } else {
                if (ArithUtil.sub(mCurNumberHashMap.get(key), mMaxNumberHashMap.get(key)) > 0) {
                    mCurNumberHashMap.put(key, mMaxNumberHashMap.get(key));
                }
            }
        }
    }

    /**
     * 刷新adapter
     */
    private void refreshRvDishes() {
        mSalesOrderBatchDishesAdapter.notifyDataSetChanged();
    }

    /**
     * 修改“全选”“确认”按钮状态
     */
    private void modifyButtonStatus() {
        // 确认按钮
        Set<String> keySet = mCurNumberHashMap.keySet();
        if (keySet.size() == 0) {
            mBtnConfirm.setEnabled(false);
            mBtnConfirm.setText("选择退单菜品");
        } else {
            mBtnConfirm.setEnabled(true);
            double amount = 0d;
            for (String key : keySet) {
                SalesOrderBatchDishesE salesOrderBatchDishesE = mSalesOrderBatchDishesHashMap.get(key);
                if (salesOrderBatchDishesE.getGift() == 0) {
                    amount = ArithUtil.add(amount, ArithUtil.mul(mCurNumberHashMap.get(key), ArithUtil.add(salesOrderBatchDishesE.getPrice(), salesOrderBatchDishesE.getPracticeSubTotal())));
                }
            }
            mBtnConfirm.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(amount)) + " 确认退菜");
        }
    }

    /**
     * 是否已全选
     *
     * @return
     */
    private boolean allSelected() {
        boolean allSelected = true;
        Set<String> keySet = mCurNumberHashMap.keySet();
        if (keySet.size() == mMaxNumberHashMap.size()) {
            for (String key : keySet) {
                if (allSelected && ArithUtil.sub(mMaxNumberHashMap.get(key), mCurNumberHashMap.get(key)) > 0) {
                    allSelected = false;
                }
            }
        } else {
            allSelected = false;
        }
        return allSelected;
    }

    /**
     * 设置刷新标志位
     * 以方法的形式进行调用，当切换逻辑需要改变时只需要修改该方法内部代码，外部的调用保持不变
     *
     * @param enter 进入刷新中
     */
    private void setRefreshing(boolean enter) {
        if (enter) {
            if (!mRefreshing) {
                mRefreshing = true;
            }
        } else {
            if (mRefreshing) {
                mRefreshing = false;
            }
        }
    }

    /**
     * 获取刷新标志位
     *
     * @return
     */
    private boolean isRefreshing() {
        return mRefreshing;
    }

    /**************************private method end**************************/
}