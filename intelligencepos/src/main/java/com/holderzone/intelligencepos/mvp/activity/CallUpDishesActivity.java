package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.CallUpDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.presenter.CallUpDishesPresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DateUtils;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.internal.Preconditions;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 叫起页面
 * Created by tcw on 2017/7/6.
 */
public class CallUpDishesActivity extends BaseActivity<CallUpDishesContract.Presenter> implements CallUpDishesContract.View {

    private static final String KEY_DINING_TABLE = "DINING_TABLE";

    private static final String KEY_SAVE_ARRAY_OF_DISH_GUID = "SAVE_ARRAY_OF_DISH_GUID";

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.rv_dishes)
    RecyclerView mRvDishes;
    @BindView(R.id.ll_select_all)
    LinearLayout mLlSelectAll;
    @BindView(R.id.iv_select_all)
    ImageView mIvSelectAll;
    @BindView(R.id.tv_select_all)
    TextView mTvSelectAll;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    // 需要进行菜品操作的桌台
    private DiningTableE mDiningTableE;

    // rv && adapter
    private CommonAdapter<SalesOrderBatchDishesE> mSalesOrderBatchDishesAdapter;// adapter
    private List<SalesOrderBatchDishesE> mSalesOrderBatchDishesEs = new ArrayList<>();// adapter数据源
    private Map<String, SalesOrderBatchDishesE> mSalesOrderBatchDishesHashMap = new HashMap<>();// guid和实体对应关系的HashMap
    private List<String> mArrayOfSalesOrderBatchDishesGUIDSelected = new ArrayList<>();// 选中的菜品guid集合

    // logic helper
    private boolean mRefreshing;

    public static Intent newIntent(Context context, DiningTableE diningTableE) {
        Intent intent = new Intent(context, CallUpDishesActivity.class);
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
        mArrayOfSalesOrderBatchDishesGUIDSelected = savedInstanceState.getStringArrayList(KEY_SAVE_ARRAY_OF_DISH_GUID);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_call_up_dishes;
    }

    @Override
    protected CallUpDishesContract.Presenter initPresenter() {
        return new CallUpDishesPresenter(this);
    }


    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

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
            requestHangedUpDishes();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_SAVE_ARRAY_OF_DISH_GUID, (ArrayList<String>) mArrayOfSalesOrderBatchDishesGUIDSelected);
    }

    /*************************view callback begin*************************/

    @Override
    public void onRequestHangedUpDishesSucceed(List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE) {
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
                // 修正已选中的guid
                correctArrayOfSalesOrderBatchDishesGUIDSelected();
                // 刷新adapter
                refreshRvDishes();
                // 修改“全选”“确认”按钮状态
                modifyButtonStatus();
            }
        }
    }

    @Override
    public void onRequestHangedUpDishesFailed() {
        // do nothing
    }

    @Override
    public void onCallUpSucceed() {
        // 返回桌台界面
        AppManager.getInstance().finishUntil(TableActivity.class);
    }

    @Override
    public void onCallUpFailed() {
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
                requestHangedUpDishes();
            }
        });
    }

    @Override
    public void onDispose() {
        // 刷新中 复位
        setRefreshing(false);
    }

    /**************************view callback end**************************/

    /*************************child callback begin*************************/
    /**************************child callback end**************************/

    /*************************dialog callback begin*************************/
    /**************************dialog callback end**************************/

    /*************************private method begin*************************/

    /**
     * 初始化标题
     */
    private void initTitle() {
        mTitle.setTitleText(mDiningTableE.getName() + " 叫起");
        mTitle.setOnReturnClickListener(this::finish);
        mTitle.setOnMenuClickListener(this::requestHangedUpDishes);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mSalesOrderBatchDishesAdapter = new CommonAdapter<SalesOrderBatchDishesE>(CallUpDishesActivity.this, R.layout.item_call_up, mSalesOrderBatchDishesEs) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderBatchDishesE salesOrderBatchDishesE, int position) {
                // content
                if (mArrayOfSalesOrderBatchDishesGUIDSelected.contains(salesOrderBatchDishesE.getSalesOrderBatchDishesGUID())) {
                    holder.setImageResource(R.id.iv_circle_selected_or_not, R.drawable.circle_state_selected);
                } else {
                    holder.setImageResource(R.id.iv_circle_selected_or_not, R.drawable.circle_state_normal);
                }
                holder.setVisible(R.id.tv_gift, salesOrderBatchDishesE.getGift() == 1);
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
                holder.setText(R.id.tv_dish_count,
                        getString(R.string.dishes_count_str, ArithUtil.stripTrailingZeros(salesOrderBatchDishesE.getCheckCount())));
                holder.setText(R.id.tv_order_created_time, DateUtils.anewFormat(salesOrderBatchDishesE.getBatchTime(), "HH:mm") + " 下单");
                holder.setVisible(R.id.divider_dash_line, position != mSalesOrderBatchDishesEs.size() - 1);
            }
        };
        mSalesOrderBatchDishesAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                // 修改选中
                SalesOrderBatchDishesE salesOrderBatchDishesE = mSalesOrderBatchDishesEs.get(position);
                String salesOrderBatchDishesGUID = salesOrderBatchDishesE.getSalesOrderBatchDishesGUID();
                if (mArrayOfSalesOrderBatchDishesGUIDSelected.contains(salesOrderBatchDishesGUID)) {
                    mArrayOfSalesOrderBatchDishesGUIDSelected.remove(salesOrderBatchDishesGUID);
                } else {
                    mArrayOfSalesOrderBatchDishesGUIDSelected.add(salesOrderBatchDishesGUID);
                }
                // 刷新adapter
                mSalesOrderBatchDishesAdapter.notifyDataSetChanged();
                // 修改按钮状态
                modifyButtonStatus();
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
        mLlSelectAll.setOnClickListener(v -> {
            if (mArrayOfSalesOrderBatchDishesGUIDSelected.size() < mSalesOrderBatchDishesEs.size()) {
                mArrayOfSalesOrderBatchDishesGUIDSelected.clear();
                for (SalesOrderBatchDishesE salesOrderBatchDishesE : mSalesOrderBatchDishesEs) {
                    mArrayOfSalesOrderBatchDishesGUIDSelected.add(salesOrderBatchDishesE.getSalesOrderBatchDishesGUID());
                }
            } else {
                mArrayOfSalesOrderBatchDishesGUIDSelected.clear();
            }
            // 刷新adapter
            refreshRvDishes();
            // 修改“全选”“确认”按钮状态
            modifyButtonStatus();
        });
        mBtnConfirm.setText("选择叫起菜品");
        mBtnConfirm.setEnabled(false);
        RxView.clicks(mBtnConfirm).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            List<SalesOrderBatchDishesE> temp = new ArrayList<>();
            for (String salesOrderBatchDishesGUID : mArrayOfSalesOrderBatchDishesGUIDSelected) {
                temp.add(mSalesOrderBatchDishesHashMap.get(salesOrderBatchDishesGUID));
            }
            mPresenter.submitCallUpDishes(temp);
        });
    }

    /**
     * 请求菜品数据
     */
    private void requestHangedUpDishes() {
        // 刷新中 置位
        setRefreshing(true);
        // 请求新的网络数据
        mPresenter.requestHangedUpDishes(mDiningTableE.getSalesOrderE().getSalesOrderGUID(), 0);
    }

    /**
     * 生成菜品数据
     *
     * @param arrayOfSalesOrderBatchDishesE
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
        mSalesOrderBatchDishesEs.clear();
        mSalesOrderBatchDishesEs.addAll(dataFilterRetreatDone);
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
     * 修正已选中的guid
     */
    private void correctArrayOfSalesOrderBatchDishesGUIDSelected() {
        List<String> mSalesOrderBatchDishesGuidDisappear = new ArrayList<>();
        for (String salesOrderBatchDishesGUID : mArrayOfSalesOrderBatchDishesGUIDSelected) {
            SalesOrderBatchDishesE salesOrderBatchDishesE = mSalesOrderBatchDishesHashMap.get(salesOrderBatchDishesGUID);
            if (salesOrderBatchDishesE == null) {
                mSalesOrderBatchDishesGuidDisappear.add(salesOrderBatchDishesGUID);
            }
        }
        if (mSalesOrderBatchDishesGuidDisappear.size() > 0) {
            mArrayOfSalesOrderBatchDishesGUIDSelected.removeAll(mSalesOrderBatchDishesGuidDisappear);
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
        if (mArrayOfSalesOrderBatchDishesGUIDSelected.size() < mSalesOrderBatchDishesEs.size()) {
            mIvSelectAll.setImageResource(R.drawable.circle_state_normal);
//            mTvSelectAll.setText("全选");
        } else {
            mIvSelectAll.setImageResource(R.drawable.circle_state_selected);
//            mTvSelectAll.setText("取消全选");
        }
        if (mArrayOfSalesOrderBatchDishesGUIDSelected.size() == 0) {
            mBtnConfirm.setEnabled(false);
            mBtnConfirm.setText("选择叫起菜品");
        } else {
            mBtnConfirm.setEnabled(true);
//            double amount = 0d;
//            for (String salesOrderBatchDishesGUIDSelected : mArrayOfSalesOrderBatchDishesGUIDSelected) {
//                SalesOrderBatchDishesE salesOrderBatchDishesE = mSalesOrderBatchDishesHashMap.get(salesOrderBatchDishesGUIDSelected);
//                if (salesOrderBatchDishesE.getGift() == 0) {
//                    amount = ArithUtil.add(amount, ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), ArithUtil.add(salesOrderBatchDishesE.getPrice(), salesOrderBatchDishesE.getPracticeSubTotal())));
//                }
//            }
            mBtnConfirm.setText(mArrayOfSalesOrderBatchDishesGUIDSelected.size() + "菜品 确认叫起");
        }
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