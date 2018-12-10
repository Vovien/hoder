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
import com.holderzone.intelligencepos.mvp.contract.ChangeTableContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableAreaE;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.presenter.ChangeTablePresenter;
import com.holderzone.intelligencepos.utils.AppManager;
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
 * 换桌界面
 * Created by tcw on 2017/7/4.
 */
public class ChangeTableActivity extends BaseActivity<ChangeTableContract.Presenter> implements ChangeTableContract.View {

    private static final String KEY_INTENT_DINING_TABLE = "INTENT_DINING_TABLE";

    private static final String KEY_SAVE_DINING_TABLE_AREA_GUID = "SAVE_DINING_TABLE_AREA_GUID";

    private static final String KEY_SAVE_DINING_TABLE_GUID = "SAVE_DINING_TABLE_GUID";

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

    // intent data namely src table
    private DiningTableE mSrcDiningTableE;

    // origin data source
    private List<DiningTableAreaE> mDiningTableAreaEsReceived = new ArrayList<>();// 接收到的桌台区域集合
    private List<DiningTableE> mDiningTableEsReceived = new ArrayList<>();// 接收到的桌台集合

    // data helper
    private Map<String, DiningTableE> mDiningTableHashMap = new HashMap<>();// 记录GUID和DiningTable实体的对应关系
    private Map<String, Integer> mPositionHashMap = new HashMap<>();// 记录GUID和Adapter中位置的对应关系
    private String mDiningTableAreaGUIDSelected;// 当前选中的桌台区域GUID
    private String mDiningTableGUIDSelected;// 当前选中的桌台GUID

    // rv && adapter
    private CommonAdapter<DiningTableAreaE> mDiningTableAreaEAdapter;
    private CommonAdapter<DiningTableE> mDiningTableEAdapter;
    private List<DiningTableAreaE> mDiningTableAreaEs = new ArrayList<>();
    private List<DiningTableE> mDiningTableEs = new ArrayList<>();

    // logic helper
    private boolean mRefreshing;

    public static Intent newIntent(Context context, DiningTableE diningTableE) {
        Intent intent = new Intent(context, ChangeTableActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSrcDiningTableE = extras.getParcelable(KEY_INTENT_DINING_TABLE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
        mDiningTableAreaGUIDSelected = savedInstanceState.getString(KEY_SAVE_DINING_TABLE_AREA_GUID);
        mDiningTableGUIDSelected = savedInstanceState.getString(KEY_SAVE_DINING_TABLE_GUID);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_change_table;
    }

    @Override
    protected ChangeTableContract.Presenter initPresenter() {
        return new ChangeTablePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化标题
        initTitle();
        // 初始化桌台区域
        initTableArea();
        // 初始化桌台
        initTable();
        // 初始化
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
        outState.putString(KEY_SAVE_DINING_TABLE_GUID, mDiningTableGUIDSelected);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /*************************view callback begin*************************/

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

            // 生成value为table的HashMap
            generateDiningTableHashMap();

            // 修正当前选中桌台订单GUID和桌台GUID
            correctDiningTableGUIDSelected();

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
    public void onRequestTableCouldBeChangeFailed() {
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
    public void onChangeTableSucceed() {
        AppManager.getInstance().finishUntil(TableActivity.class);
    }

    @Override
    public void onDispose() {
        // 刷新中 复位
        setRefreshing(false);
    }

    /**************************view callback end**************************/

    /*************************child callback begin*************************/
    /**************************child callback end**************************/

    /*************************private method begin*************************/

    /**
     * 初始化标题
     */
    private void initTitle() {
        mTitle.setTitleText(mSrcDiningTableE.getName() + " 换台");
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
        mDiningTableAreaEAdapter = new CommonAdapter<DiningTableAreaE>(ChangeTableActivity.this, R.layout.item_table_area, mDiningTableAreaEs) {
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
        mRvTableArea.setLayoutManager(new LinearLayoutManager(ChangeTableActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * 初始化桌台
     */
    private void initTable() {
        // 桌台 适配器配置
        mDiningTableEAdapter = new CommonAdapter<DiningTableE>(ChangeTableActivity.this, R.layout.item_table, mDiningTableEs) {
            @Override
            protected void convert(ViewHolder holder, DiningTableE diningTableE, int position) {
                // 设置选择效果
                if (diningTableE.getDiningTableGUID().equalsIgnoreCase(mDiningTableGUIDSelected)) {
                    holder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg_selected);
                } else {
                    holder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg);
                }
                holder.setText(R.id.tv_table_name, diningTableE.getName());// 桌台名
                holder.setBackgroundRes(R.id.rl_table_title, R.drawable.shape_table_activity_item_table_title_bg_free);// 标题背景颜色
                holder.setTextColorRes(R.id.tv_table_status_or_consume, R.color.btn_text_green_01b6ad);// 消费合计字体颜色
                holder.setText(R.id.tv_table_status_or_consume, diningTableE.getTableStatusCN());// 消费合计
                holder.setVisible(R.id.iv_table_calc, false);// 算账标识
                holder.setText(R.id.tv_table_persons, "0/" + diningTableE.getSeatsCount());// 桌台使用人数
                holder.setVisible(R.id.tv_table_use_hours, false);// 桌台使用时间
                holder.setVisible(R.id.iv_merge_identify, false);// 无并单
                Integer isOrder = diningTableE.getIsOrder();
                Preconditions.checkNotNull(isOrder, "isOrder==null");
                holder.setVisible(R.id.iv_order_identify, 1 == isOrder);// 预订标识
            }
        };

        mDiningTableEAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                DiningTableE diningTableE = mDiningTableEs.get(position);
                String diningTableGUID = diningTableE.getDiningTableGUID();
                if (!diningTableGUID.equalsIgnoreCase(mDiningTableGUIDSelected)) {
                    // 获取上一个位置
                    Integer oldPosition = mPositionHashMap.get(mDiningTableGUIDSelected);

                    // 更新当前选中的桌台GUID
                    mDiningTableGUIDSelected = diningTableGUID;

                    // 设置上一个未选中效果 (刷新adapter)
                    if (oldPosition != null) {
                        RecyclerView.ViewHolder oldHolder = mRvTable.findViewHolderForAdapterPosition(oldPosition.intValue());
                        if (oldHolder instanceof ViewHolder) {
                            ViewHolder viewHolder = (ViewHolder) oldHolder;
                            viewHolder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg);
                        }
//                        // 方法二：设置上一个未选中效果 (刷新adapter，与GridLayoutManager配合使用效果更佳)
//                        mDiningTableEAdapter.notifyItemChanged(oldPosition);
                    }

                    // 设置当前选中效果 (刷新adapter)
                    if (holder instanceof ViewHolder) {
                        ViewHolder viewHolder = (ViewHolder) holder;
                        viewHolder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg_selected);
                    }
//                        // 方法二：设置当前选中效果 (刷新adapter，与GridLayoutManager配合使用效果更佳)
//                        mDiningTableEAdapter.notifyItemChanged(position);

                    // 确认按钮可点击
                    mBtnConfirm.setEnabled(true);
                    mBtnConfirm.setText("换至" + diningTableE.getName() + " 确认换台");
                } else {
                    // 更新当前选中的桌台GUID
                    mDiningTableGUIDSelected = null;

                    // 设置当前未选中效果 (刷新adapter)
                    if (holder instanceof ViewHolder) {
                        ViewHolder viewHolder = (ViewHolder) holder;
                        viewHolder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg);
                    }
//                        // 方法二：设置当前未选中效果 (刷新adapter，与GridLayoutManager配合使用效果更佳)
//                        mDiningTableEAdapter.notifyItemChanged(position);

                    // 确认按钮不可点击
                    mBtnConfirm.setEnabled(false);
                    mBtnConfirm.setText("选择换至桌台");
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //设置页面滚动监听mDiningTableEAdapter
        mRvTable.setLayoutManager(new GridLayoutManager(ChangeTableActivity.this, 3));
        mRvTable.setAdapter(mDiningTableEAdapter);
        mRvTable.addItemDecoration(new SmartGridSpacingItemDecoration(DensityUtils.dp2px(this, 10)));
        ((SimpleItemAnimator) mRvTable.getItemAnimator()).setSupportsChangeAnimations(false);
//        mRvTable.getItemAnimator().setChangeDuration(0);
    }

    private void initConfirmButton() {
        mBtnConfirm.setEnabled(false);
        mBtnConfirm.setText("选择换至桌台");
        RxView.clicks(mBtnConfirm).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            mDialogFactory.showConfirmDialog("确定将桌台更换至" + mDiningTableHashMap.get(mDiningTableGUIDSelected).getName() + "吗？", "取消", "确认换台", new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {

                }

                @Override
                public void onPosClick() {
                    mPresenter.submitChangeTable(mSrcDiningTableE.getSalesOrderE().getSalesOrderGUID(), mSrcDiningTableE.getDiningTableGUID(), mDiningTableGUIDSelected);
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
        mPresenter.requestTableCouldBeChange();
    }

    /**
     * 换桌模式下 生成桌台区域
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
     * 换桌模式下 修正当前选中桌台区域GUID
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
     * 换桌模式下 生成桌台
     *
     * @param arrayOfDiningTableE
     */
    private void generateTable(List<DiningTableE> arrayOfDiningTableE) {
        Preconditions.checkNotNull(arrayOfDiningTableE, "arrayOfDiningTableE==null");
        mDiningTableEsReceived = arrayOfDiningTableE;
    }

    /**
     * 生成value为table的HashMap
     */
    private void generateDiningTableHashMap() {
        mDiningTableHashMap.clear();
        for (DiningTableE diningTableE : mDiningTableEsReceived) {
            mDiningTableHashMap.put(diningTableE.getDiningTableGUID(), diningTableE);
        }
    }

    /**
     * 换桌模式下 修正当前选中桌台订单GUID和桌台GUID
     */
    private void correctDiningTableGUIDSelected() {
        if (mDiningTableGUIDSelected != null) {
            if (mDiningTableHashMap.get(mDiningTableGUIDSelected) == null) {
                mDiningTableGUIDSelected = null;
                showMessage("检测到桌台状态发生变化，已自动更正");
            }
        }
    }

    /**
     * 换桌模式下 生成过滤桌台
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
     * 换桌模式下 生成value为position的HashMap
     */
    private void generatePositionHashMap() {
        mPositionHashMap.clear();
        int size = mDiningTableEs.size();
        for (int i = 0; i < size; i++) {
            DiningTableE tableE = mDiningTableEs.get(i);
            mPositionHashMap.put(tableE.getDiningTableGUID(), i);
        }
    }

    /**
     * 换桌模式下 刷新TableAreaRv
     */
    private void refreshTableAreaRv() {
        mDiningTableAreaEAdapter.notifyDataSetChanged();
    }

    /**
     * 换桌模式下 重构数据、刷新TableRv
     */
    private void refreshTableRv() {
        mDiningTableEAdapter.notifyDataSetChanged();
    }

    /**
     * 修改确认按钮的状态
     */
    private void modifyConfirmButtonStatus() {
        DiningTableE diningTableE = mDiningTableHashMap.get(mDiningTableGUIDSelected);
        if (diningTableE != null) {
            mBtnConfirm.setEnabled(true);
            mBtnConfirm.setText("换至" + diningTableE.getName() + " 确认换台");
        } else {
            mBtnConfirm.setEnabled(false);
            mBtnConfirm.setText("选择换至桌台");
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