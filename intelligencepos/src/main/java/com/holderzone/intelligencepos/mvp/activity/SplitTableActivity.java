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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.SplitTableContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SubSalesOrder;
import com.holderzone.intelligencepos.mvp.presenter.SplitTablePresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 拆单界面
 * Created by tcw on 2017/7/4.
 */
public class SplitTableActivity extends BaseActivity<SplitTableContract.Presenter> implements SplitTableContract.View {

    private static final String KEY_INTENT_DINING_TABLE = "INTENT_DINING_TABLE";

    private static final String KEY_SAVE_ARRAY_OF_SALES_ORDER_GUID = "SAVE_ARRAY_OF_SALES_ORDER_GUID";

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;
    @BindView(R.id.ll_select_all)
    LinearLayout mLlSelectAll;
    @BindView(R.id.iv_select_all)
    ImageView mIvSelectAll;
    @BindView(R.id.tv_select_all)
    TextView mTvSelectAll;
    @BindView(R.id.rv_table)
    RecyclerView mRvTable;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    // intent data namely src SalesOrderGUID
    private DiningTableE mMainDiningTableE;

    // rv && adapter
    private CommonAdapter<DiningTableE> mDiningTableAdapter;
    private List<DiningTableE> mArraOfSubDiningTableE = new ArrayList<>();
    private Map<String, DiningTableE> mDiningTableHashMap = new HashMap<>();
    private List<String> mArrayOfSalesOrderGUIDSelected = new ArrayList<>();

    // logic helper
    private boolean mRefreshing;

    public static Intent newIntent(Context context, DiningTableE diningTableE) {
        Intent intent = new Intent(context, SplitTableActivity.class);
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
        mArrayOfSalesOrderGUIDSelected = savedInstanceState.getStringArrayList(KEY_SAVE_ARRAY_OF_SALES_ORDER_GUID);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_split_table;
    }

    @Override
    protected SplitTableContract.Presenter initPresenter() {
        return new SplitTablePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化标题
        initTitle();
        // 初始化桌台
        initTable();
        // 初始化确认按钮
        initButton();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (!isRefreshing()) {
            requestTable();
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
    public void onRequestTableSucceed(List<DiningTableE> arrayOfDiningTableE) {
        // 生成该主单已并单的桌台
        generateTableMerged(arrayOfDiningTableE);

        // 桌台区域数量不为0时
        if (mMultiStateView.getViewState() == MultiStateView.VIEW_STATE_CONTENT) {
            // 生成value为DiningTable的HashMap
            generateDiningTableHashMap();

            // 修正当前选中SalesOrderGUID
            correctSalesOrderGUIDSelected();

            // 刷新adpater
            refreshTableRv();

            // 修改按钮的状态
            modifyButtonStatus();
        }
    }

    @Override
    public void onRequestTableFailed() {
        // do nothing
    }

    @Override
    public void onSplitSucceed() {
        AppManager.getInstance().finishUntil(TableActivity.class);
    }

    @Override
    public void onNetworkError() {
        // 切换到网络错误布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        // 注册点击事件
        Button btnRetry = (Button) mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            if (!isRefreshing()) {
                requestTable();
            }
        });
    }
    /**************************view callback end**************************/

    /*************************child callback begin*************************/
    /**************************child callback end**************************/

    /*************************private method begin*************************/

    /**
     * 初始化标题
     */
    private void initTitle() {
        mTitle.setTitleText(mMainDiningTableE.getName() + " 拆单");
        mTitle.setOnReturnClickListener(this::finish);
    }

    /**
     * 初始化桌台
     */
    private void initTable() {
        mDiningTableAdapter = new CommonAdapter<DiningTableE>(SplitTableActivity.this, R.layout.item_split_table, mArraOfSubDiningTableE) {
            @Override
            protected void convert(ViewHolder holder, DiningTableE diningTableE, int position) {
                SalesOrderE salesOrderE = diningTableE.getSalesOrderE();
                holder.setImageResource(R.id.iv_circle_selected_or_not,
                        mArrayOfSalesOrderGUIDSelected.contains(salesOrderE.getSalesOrderGUID())
                                ? R.drawable.circle_state_selected
                                : R.drawable.circle_state_normal);
                holder.setText(R.id.tv_table_name, diningTableE.getName());
                holder.setText(R.id.tv_consume_total, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(salesOrderE.getConsumeTotal())));
                holder.setVisible(R.id.include_dash_line, position != mArraOfSubDiningTableE.size() - 1);
            }
        };
        mDiningTableAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                DiningTableE diningTableE = mArraOfSubDiningTableE.get(position);
                SalesOrderE salesOrderE = diningTableE.getSalesOrderE();
                String salesOrderGUID = salesOrderE.getSalesOrderGUID();
                if (mArrayOfSalesOrderGUIDSelected.contains(salesOrderGUID)) {
                    mArrayOfSalesOrderGUIDSelected.remove(salesOrderGUID);
                } else {
                    mArrayOfSalesOrderGUIDSelected.add(salesOrderGUID);
                }
                // 刷新桌台列表
                refreshTableRv();
                // 修改按钮的状态
                modifyButtonStatus();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRvTable.setAdapter(mDiningTableAdapter);
        mRvTable.setLayoutManager(new LinearLayoutManager(SplitTableActivity.this));
    }

    /**
     * 初始化确认按钮
     */
    private void initButton() {
        mLlSelectAll.setOnClickListener(v -> {
            // 修改选中桌台SalesOrderGUID
            if (mArrayOfSalesOrderGUIDSelected.size() < mArraOfSubDiningTableE.size()) {
                mArrayOfSalesOrderGUIDSelected.clear();
                for (DiningTableE diningTableE : mArraOfSubDiningTableE) {
                    mArrayOfSalesOrderGUIDSelected.add(diningTableE.getSalesOrderE().getSalesOrderGUID());
                }
            } else {
                mArrayOfSalesOrderGUIDSelected.clear();
            }
            // 刷新桌台列表
            refreshTableRv();
            // 修改按钮的状态
            modifyButtonStatus();
        });
        mBtnConfirm.setEnabled(false);
        mBtnConfirm.setText("选择拆单桌台");
        RxView.clicks(mBtnConfirm).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            mDialogFactory.showConfirmDialog("确认将所选桌台拆分回原账单吗？", "取消", "确认拆单", new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {

                }

                @Override
                public void onPosClick() {
                    List<SubSalesOrder> subSalesOrderList = new ArrayList<>();
                    for (String salesOrderGUID : mArrayOfSalesOrderGUIDSelected) {
                        DiningTableE diningTableE = mDiningTableHashMap.get(salesOrderGUID);
                        SubSalesOrder subSalesOrder = new SubSalesOrder();
                        subSalesOrder.setSalesOrderGUID(diningTableE.getSalesOrderE().getSalesOrderGUID());
                        subSalesOrderList.add(subSalesOrder);
                    }
                    mPresenter.submitSplitTable(subSalesOrderList);
                }
            });
        });
    }

    /**
     * 请求已并桌台列表
     */
    private void requestTable() {
        // 刷新中 置位
        setRefreshing(true);
        // 请求新的网络数据
        mPresenter.requestTable();
    }

    /**
     * 修正当前选中SalesOrderGUID
     */
    private void correctSalesOrderGUIDSelected() {
        List<String> arrayOfSalesOrderGUIDWhichDisappear = new ArrayList<>();
        for (String salesOrderGUID : mArrayOfSalesOrderGUIDSelected) {
            DiningTableE diningTableE = mDiningTableHashMap.get(salesOrderGUID);
            if (diningTableE == null) {
                arrayOfSalesOrderGUIDWhichDisappear.add(salesOrderGUID);
            }
        }
        mArrayOfSalesOrderGUIDSelected.removeAll(arrayOfSalesOrderGUIDWhichDisappear);
    }

    /**
     * 生成value为DiningTable的HashMap
     */
    private void generateDiningTableHashMap() {
        mDiningTableHashMap.clear();
        for (DiningTableE diningTableE : mArraOfSubDiningTableE) {
            mDiningTableHashMap.put(diningTableE.getSalesOrderE().getSalesOrderGUID(), diningTableE);
        }
    }

    /**
     * 生成该主单已并单的桌台
     */
    private void generateTableMerged(List<DiningTableE> arrayOfDiningTableE) {
        mArraOfSubDiningTableE.clear();
        String mainSalesOrderGUID = mMainDiningTableE.getSalesOrderE().getSalesOrderGUID();
        for (DiningTableE tableE : arrayOfDiningTableE) {
            Integer tableStatus = tableE.getTableStatus();
            if (tableStatus != null) {
                if (1 == tableStatus) {// 占用
                    SalesOrderE salesOrderE = tableE.getSalesOrderE();
                    Integer upperState = salesOrderE.getUpperState();
                    if (2 == upperState) {// 子
                        if (salesOrderE.getUpperGUID().equalsIgnoreCase(mainSalesOrderGUID)) {
                            mArraOfSubDiningTableE.add(tableE);
                        }
                    }
                }
            }
        }
        if (mArraOfSubDiningTableE.size() == 0) {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    /**
     * 刷新adpater
     */
    private void refreshTableRv() {
        mDiningTableAdapter.notifyDataSetChanged();
    }

    /**
     * 修改确认按钮的状态
     */
    private void modifyButtonStatus() {
        int size = mArrayOfSalesOrderGUIDSelected.size();
        if (size < mArraOfSubDiningTableE.size()) {
            mIvSelectAll.setImageResource(R.drawable.circle_state_normal);
            mTvSelectAll.setText("全选");
        } else {
            mIvSelectAll.setImageResource(R.drawable.circle_state_selected);
            mTvSelectAll.setText("取消全选");
        }
        if (size == 0) {
            mBtnConfirm.setText("选择拆单桌台");
            mBtnConfirm.setEnabled(false);
        } else {
            mBtnConfirm.setText(size + "桌台 确认拆单");
            mBtnConfirm.setEnabled(true);
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