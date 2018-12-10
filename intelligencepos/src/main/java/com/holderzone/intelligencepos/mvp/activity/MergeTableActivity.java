package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.decoration.SmartGridSpacingItemDecoration;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.MergeTableContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableAreaE;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SubSalesOrder;
import com.holderzone.intelligencepos.mvp.presenter.MergeTablePresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DensityUtils;
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
 * 并单界面
 * Created by tcw on 2017/7/4.
 */
public class MergeTableActivity extends BaseActivity<MergeTableContract.Presenter> implements MergeTableContract.View {

    private static final String KEY_INTENT_DINING_TABLE = "INTENT_DINING_TABLE";

    private static final String KEY_SAVE_DINING_TABLE_AREA_GUID = "SAVE_DINING_TABLE_AREA_GUID";

    private static final String KEY_SAVE_ARRAY_OF_SALES_ORDER_GUID = "SAVE_ARRAY_OF_SALES_ORDER_GUID";

    public static final String FAKE_DINING_TABLE_AREA_GUID = "00000000-0000-0000-0000-000000000000";

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.multi_state_view_table_area)
    MultiStateView mMultiStateViewTableArea;
    @BindView(R.id.rv_table_area)
    RecyclerView mRvTableArea;
    @BindView(R.id.multi_state_view_table)
    MultiStateView mMultiStateViewTable;
    @BindView(R.id.rv_table)
    RecyclerView mRvTable;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    // intent data namely src SalesOrderGUID
    private DiningTableE mMainDiningTableE;

    // origin data source
    private List<DiningTableAreaE> mDiningTableAreaEsReceived = new ArrayList<>();// 接收到的桌台区域集合
    private List<DiningTableE> mDiningTableEsReceived = new ArrayList<>();// 接收到的桌台集合

    // data helper
    private Map<String, DiningTableE> mDiningTableHashMap = new HashMap<>();// 记录SalesOrderGUID和桌台实体的对应关系
    private Map<String, Integer> mPositionHashMap = new HashMap<>();// 记录SalesOrderGUID和Adapter中位置的对应关系
    private String mDiningTableAreaGUIDSelected;// 当前选中的桌台区域GUID
    private List<String> mArrayOfSalesOrderGUIDSelected = new ArrayList<>();// 当前选中的桌台GUID集合

    // rv && adapter
    private CommonAdapter<DiningTableAreaE> mDiningTableAreaEAdapter;
    private CommonAdapter<DiningTableE> mDiningTableEAdapter;
    private List<DiningTableAreaE> mDiningTableAreaEs = new ArrayList<>();
    private List<DiningTableE> mDiningTableEs = new ArrayList<>();

    // logic helper
    private boolean mRefreshing;

    public static Intent newIntent(Context context, DiningTableE diningTableE) {
        Intent intent = new Intent(context, MergeTableActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mMainDiningTableE = extras.getParcelable(KEY_INTENT_DINING_TABLE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
        mDiningTableAreaGUIDSelected = savedInstanceState.getString(KEY_SAVE_DINING_TABLE_AREA_GUID);
        mArrayOfSalesOrderGUIDSelected = savedInstanceState.getStringArrayList(KEY_SAVE_ARRAY_OF_SALES_ORDER_GUID);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_merge_table;
    }

    @Override
    protected MergeTableContract.Presenter initPresenter() {
        return new MergeTablePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化标题
        initTitle();
        // 初始化桌台区域
        initTableArea();
        // 初始化桌台
        initTable();
        // 初始化确认按钮
        initConfirmButton();

    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (!isRefreshing()) {
            requestMainGuiData();
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SAVE_DINING_TABLE_AREA_GUID, mDiningTableAreaGUIDSelected);
        outState.putStringArrayList(KEY_SAVE_ARRAY_OF_SALES_ORDER_GUID, (ArrayList<String>) mArrayOfSalesOrderGUIDSelected);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /*************************view callback begin*************************/

    @Override
    public void onDispose() {
        // 刷新中 复位
        setRefreshing(false);
    }

    @Override
    public void refreshMainGui(List<DiningTableAreaE> arrayOfDiningTableAreaE, List<DiningTableE> arrayOfDiningTableE) {
        // 生成桌台区域
        generateTableArea(arrayOfDiningTableAreaE);

        // 桌台区域数量不为0时
        if (mMultiStateViewTableArea.getViewState() == MultiStateView.VIEW_STATE_CONTENT) {

            // 修正当前选中桌台区域GUID
            correctDiningTableAreaGUIDSelected();

            // 刷新TableAreaRV
            refreshTableAreaRv();

            // 生成桌台
            generateTable(arrayOfDiningTableE);

            // 生成value为DiningTableE的HashMap
            generateDiningTableHashMap();

            // 修正当前选中桌台订单GUID和桌台GUID
            correctSalesOrderGUIDSelected();

            // 生成过滤桌台
            generateTableWithFilter();

            // 过滤桌台数量不为0时
            if (mMultiStateViewTable.getViewState() == MultiStateView.VIEW_STATE_CONTENT) {

                // 生成value为position的HashMap
                generatePositionHashMap();

                // 刷新TableRV
                refreshTableRv();
            }

            // 修改确认按钮的状态
            modifyConfirmButtonStatus();
        }
    }

    @Override
    public void onRequestTableCouldBeMergeFailed() {
        // do nothing
    }

    @Override
    public void onNetworkError() {
        // 切换到网络错误布局
        mMultiStateViewTableArea.setViewState(MultiStateView.VIEW_STATE_ERROR);
        // 注册点击事件
        Button btnRetry = (Button) mMultiStateViewTableArea.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            if (!isRefreshing()) {
                requestMainGuiData();
            }
        });
    }

    @Override
    public void onMergeTableSucceed() {
        AppManager.getInstance().finishUntil(TableActivity.class);
    }

    /**************************view callback end**************************/

    /*************************child callback begin*************************/
    /**************************child callback end**************************/

    /*************************private method begin*************************/

    /**
     * 初始化标题
     */
    private void initTitle() {
        mTitle.setTitleText(mMainDiningTableE.getName() + " 并单");
        mTitle.setOnReturnClickListener(this::finish);
        mTitle.setOnMenuClickListener(() -> {
            if (!isRefreshing()) {
                requestMainGuiData();
            }
        });
    }

    /**
     * 初始化桌台区域
     */
    private void initTableArea() {
// 桌台区域 适配器配置
        mDiningTableAreaEAdapter = new CommonAdapter<DiningTableAreaE>(MergeTableActivity.this, R.layout.item_table_area, mDiningTableAreaEs) {
            @Override
            protected void convert(ViewHolder holder, DiningTableAreaE diningTableAreaE, int position) {
                // 设置桌台区域名称
                holder.setText(R.id.tv_area_name, diningTableAreaE.getName());
                if (diningTableAreaE.getDiningTableAreaGUID().equalsIgnoreCase(mDiningTableAreaGUIDSelected)) {
                    holder.setBackgroundRes(R.id.view_indicator, R.color.tv_text_blue_2495ee);
                    holder.setTextColorRes(R.id.tv_area_name, R.color.tv_text_blue_2495ee);
                    holder.setTextColorRes(R.id.tv_table_total, R.color.tv_text_blue_2495ee);
                    holder.setTextColorRes(R.id.tv_table_used, R.color.tv_text_blue_2495ee);
                } else {
                    holder.setBackgroundRes(R.id.view_indicator, R.color.tv_text_white_ffffff);
                    holder.setTextColorRes(R.id.tv_area_name, R.color.tv_text_gray_555555);
                    holder.setTextColorRes(R.id.tv_table_total, R.color.tv_text_gray_555555);
                    holder.setTextColorRes(R.id.tv_table_used, R.color.tv_text_gray_555555);
                }
                holder.setText(R.id.tv_table_used, "");
                holder.setText(R.id.tv_table_total, "(" + diningTableAreaE.getTableCount() + ")");
            }
        };
        mDiningTableAreaEAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                // 设置选中桌台区域GUID
                String diningTableAreaGUID = mDiningTableAreaEs.get(position).getDiningTableAreaGUID();
                if (diningTableAreaGUID.equalsIgnoreCase(mDiningTableAreaGUIDSelected)) {
                    return;
                }
                mDiningTableAreaGUIDSelected = diningTableAreaGUID;

                // 刷新桌台区域Rv
                refreshTableAreaRv();

                // 生成过滤桌台
                generateTableWithFilter();

                // 生成guid和position的对应关系
                generatePositionHashMap();

                // 刷新桌台Rv
                refreshTableRv();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRvTableArea.setAdapter(mDiningTableAreaEAdapter);
        mRvTableArea.setLayoutManager(new LinearLayoutManager(MergeTableActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * 初始化桌台
     */
    private void initTable() {
        // 桌台 适配器配置
        mDiningTableEAdapter = new CommonAdapter<DiningTableE>(MergeTableActivity.this, R.layout.item_table, mDiningTableEs) {
            @Override
            protected void convert(ViewHolder holder, DiningTableE diningTableE, int position) {
                if (mArrayOfSalesOrderGUIDSelected.contains(diningTableE.getSalesOrderE().getSalesOrderGUID())) {
                    holder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg_selected);
                } else {
                    holder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg);
                }
                holder.setText(R.id.tv_table_name, diningTableE.getName());// 桌台名
                SalesOrderE salesOrderE = diningTableE.getSalesOrderE();
                Preconditions.checkNotNull(salesOrderE, "salesOrderE==null");
                Integer dishesHangCount = salesOrderE.getDishesHangCount();
                Preconditions.checkNotNull(dishesHangCount, "dishesHangCount==null");
                if (0 == dishesHangCount) {// 占用未挂起
                    holder.setBackgroundRes(R.id.rl_table_title, R.drawable.shape_table_activity_item_table_title_bg_none_free);// 标题背景颜色
                    holder.setTextColorRes(R.id.tv_table_status_or_consume, R.color.tv_text_red_f56766);// 消费合计字体颜色
                } else {// 占用挂起
                    holder.setBackgroundRes(R.id.rl_table_title, R.drawable.shape_table_activity_item_table_title_bg_hang);// 标题背景颜色
                    holder.setTextColorRes(R.id.tv_table_status_or_consume, R.color.btn_text_orange_f4a902);// 消费合计字体颜色
                }
                Double consumeTotal = salesOrderE.getConsumeTotal();
                Preconditions.checkNotNull(consumeTotal, "consumeTotal==null");
                holder.setText(R.id.tv_table_status_or_consume, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(consumeTotal)));// 消费合计
                Integer calculationPrintCount = salesOrderE.getCalculationPrintCount();
                Preconditions.checkNotNull(calculationPrintCount, "calculationPrintCount==null");
                holder.setVisible(R.id.iv_table_calc, calculationPrintCount != 0);// 算账标识
                Integer guestCount = salesOrderE.getGuestCount();
                Preconditions.checkNotNull(guestCount, "guestCount==null");
                Integer seatsCount = diningTableE.getSeatsCount();
                Preconditions.checkNotNull(seatsCount, "seatsCount==null");
                holder.setText(R.id.tv_table_persons, guestCount + "/" + seatsCount);// 桌台使用人数
                holder.setVisible(R.id.tv_table_use_hours, true);
                holder.setText(R.id.tv_table_use_hours, salesOrderE.getUseHour());// 桌台使用时间
                holder.setVisible(R.id.iv_merge_identify, false);// 无并单
                holder.setVisible(R.id.iv_order_identify, false);// 无预订
            }
        };

        mDiningTableEAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                DiningTableE diningTableE = mDiningTableEs.get(position);
                String salesOrderGUID = diningTableE.getSalesOrderE().getSalesOrderGUID();
                if (!mArrayOfSalesOrderGUIDSelected.contains(salesOrderGUID)) {
                    // 添加当前选中桌台GUID到集合里
                    mArrayOfSalesOrderGUIDSelected.add(salesOrderGUID);

                    // 设置当前选中效果 (刷新adapter)
                    if (holder instanceof ViewHolder) {
                        ViewHolder viewHolder = (ViewHolder) holder;
                        viewHolder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg_selected);
                    }
//                        // 方法二：设置当前选中效果 (刷新adapter)
//                        mDiningTableEAdapter.notifyItemChanged(position);
                } else {
                    // 从集合里移除当前选中的桌台GUID
                    mArrayOfSalesOrderGUIDSelected.remove(salesOrderGUID);

                    // 设置当前未选中效果 (刷新adapter)
                    if (holder instanceof ViewHolder) {
                        ViewHolder viewHolder = (ViewHolder) holder;
                        viewHolder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg);
                    }
//                        // 方法二：设置当前选中效果 (刷新adapter)
//                        mDiningTableEAdapter.notifyItemChanged(position);
                }
                modifyConfirmButtonStatus();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //设置页面滚动监听mDiningTableEAdapter
        mRvTable.setLayoutManager(new GridLayoutManager(MergeTableActivity.this, 3));
        mRvTable.setAdapter(mDiningTableEAdapter);
        mRvTable.addItemDecoration(new SmartGridSpacingItemDecoration(DensityUtils.dp2px(this, 10)));
        ((SimpleItemAnimator) mRvTable.getItemAnimator()).setSupportsChangeAnimations(false);
//        mRvTable.getItemAnimator().setChangeDuration(0);
    }

    /**
     * 初始化确认按钮
     */
    private void initConfirmButton() {
        mBtnConfirm.setEnabled(false);
        mBtnConfirm.setText("选择并单桌台");
        RxView.clicks(mBtnConfirm).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            mDialogFactory.showConfirmDialog("确认合并所选桌台的账单吗？", "取消", "确认并单", new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {

                }

                @Override
                public void onPosClick() {
                    List<SubSalesOrder> subSalesOrderList = new ArrayList<>();
                    for (String salesOrderGUIDSelected : mArrayOfSalesOrderGUIDSelected) {
                        SubSalesOrder subSalesOrder = new SubSalesOrder();
                        subSalesOrder.setSalesOrderGUID(salesOrderGUIDSelected);
                        subSalesOrderList.add(subSalesOrder);
                    }
                    mPresenter.submitMergeTable(mMainDiningTableE.getSalesOrderE().getSalesOrderGUID(), subSalesOrderList);
                }
            });
        });
    }

    /**
     * 请求堂食主界面数据
     */
    private void requestMainGuiData() {
        // 刷新中 置位
        setRefreshing(true);
        // 请求新的网络数据
        mPresenter.requestTableCouldBeMerge(mMainDiningTableE.getSalesOrderE().getSalesOrderGUID());
    }

    /**
     * 并单模式下 生成桌台区域
     *
     * @param arrayOfDiningTableAreaE
     */
    private void generateTableArea(List<DiningTableAreaE> arrayOfDiningTableAreaE) {
        Preconditions.checkNotNull(arrayOfDiningTableAreaE, "arrayOfDiningTableAreaE==null");
        mDiningTableAreaEsReceived = arrayOfDiningTableAreaE;
        if (mDiningTableAreaEsReceived.size() == 0) {
            mMultiStateViewTableArea.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            mMultiStateViewTableArea.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            int useCount = 0;
            int tableCount = 0;
            for (DiningTableAreaE diningTableAreaE : mDiningTableAreaEsReceived) {
                useCount += diningTableAreaE.getUseCount();
                tableCount += diningTableAreaE.getTableCount();
            }
            DiningTableAreaE diningTableAreaE = new DiningTableAreaE();
            diningTableAreaE.setName("全部");
            diningTableAreaE.setDiningTableAreaGUID(FAKE_DINING_TABLE_AREA_GUID);
            diningTableAreaE.setUseCount(useCount);
            diningTableAreaE.setTableCount(tableCount);
            mDiningTableAreaEs.clear();
            mDiningTableAreaEs.add(diningTableAreaE);
            mDiningTableAreaEs.addAll(mDiningTableAreaEsReceived);
        }
    }

    /**
     * 并单模式下 修正当前选中桌台区域GUID
     */
    private void correctDiningTableAreaGUIDSelected() {
        for (DiningTableAreaE diningTableAreaE : mDiningTableAreaEs) {
            if (diningTableAreaE.getDiningTableAreaGUID().equalsIgnoreCase(mDiningTableAreaGUIDSelected)) {
                return;
            }
        }
        mDiningTableAreaGUIDSelected = FAKE_DINING_TABLE_AREA_GUID;
    }

    /**
     * 并单模式下 生成桌台
     *
     * @param arrayOfDiningTableE
     */
    private void generateTable(List<DiningTableE> arrayOfDiningTableE) {
        Preconditions.checkNotNull(arrayOfDiningTableE, "arrayOfDiningTableE==null");
        mDiningTableEsReceived = arrayOfDiningTableE;
    }

    /**
     * 并单模式下 生成value为DiningTableE的HashMap
     */
    private void generateDiningTableHashMap() {
        mDiningTableHashMap.clear();
        for (DiningTableE diningTableE : mDiningTableEsReceived) {
            mDiningTableHashMap.put(diningTableE.getSalesOrderE().getSalesOrderGUID(), diningTableE);
        }
    }

    /**
     * 并单模式下 修正当前选中桌台订单GUID和桌台GUID
     */
    private void correctSalesOrderGUIDSelected() {
        // 并单时一般不会出现冲突，因为顾客一般只会通知1位营业员来完成并单操作，极少出现2个营业员同时并一个单
        if (0 == mDiningTableEsReceived.size() && 0 == mArrayOfSalesOrderGUIDSelected.size()) {
            return;
        }
        boolean correctHappened = false;
        List<String> selectedArrayWhichToBeDelete = new ArrayList<>();
        for (String salesOrderGUID : mArrayOfSalesOrderGUIDSelected) {
            DiningTableE diningTableE = mDiningTableHashMap.get(salesOrderGUID);
            if (diningTableE == null) {// 不存在了
                if (!correctHappened) {
                    correctHappened = true;
                }
                selectedArrayWhichToBeDelete.add(salesOrderGUID);
            }
        }
        mArrayOfSalesOrderGUIDSelected.removeAll(selectedArrayWhichToBeDelete);
        if (correctHappened) {
            showMessage("检测到桌台状态发生变化，已自动更正");
        }
    }

    /**
     * 并单模式下 生成过滤桌台
     */
    private void generateTableWithFilter() {
        mDiningTableEs.clear();
        for (DiningTableE diningTableE : mDiningTableEsReceived) {
            if (FAKE_DINING_TABLE_AREA_GUID.equalsIgnoreCase(mDiningTableAreaGUIDSelected)
                    || diningTableE.getDiningTableAreaGUID().equalsIgnoreCase(mDiningTableAreaGUIDSelected)) {
                mDiningTableEs.add(diningTableE);
            }
        }
        if (mDiningTableEs.size() == 0) {
            mMultiStateViewTable.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            mMultiStateViewTable.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    /**
     * 并单模式下 生成value为position的HashMap
     */
    private void generatePositionHashMap() {
        mPositionHashMap.clear();
        int size = mDiningTableEs.size();
        for (int i = 0; i < size; i++) {
            DiningTableE tableE = mDiningTableEs.get(i);
            mPositionHashMap.put(tableE.getSalesOrderE().getSalesOrderGUID(), i);
        }
    }


    /**
     * 并单模式下 刷新TableAreaRv
     */
    private void refreshTableAreaRv() {
        mDiningTableAreaEAdapter.notifyDataSetChanged();
    }

    /**
     * 并单模式下  刷新TableRv
     */
    private void refreshTableRv() {
        mDiningTableEAdapter.notifyDataSetChanged();
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

    /**
     * 修改确认按钮的状态
     */
    private void modifyConfirmButtonStatus() {
        int size = mArrayOfSalesOrderGUIDSelected.size();
        if (size > 0) {
            mBtnConfirm.setEnabled(true);
            mBtnConfirm.setText(size + "桌台" + " 确认并单");
        } else {
            mBtnConfirm.setEnabled(false);
            mBtnConfirm.setText("选择并单桌台");
        }
    }

    /**************************private method end**************************/
}