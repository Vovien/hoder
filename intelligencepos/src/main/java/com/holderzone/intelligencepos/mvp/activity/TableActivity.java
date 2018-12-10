package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.holderzone.intelligencepos.msg.NoticeBroadcastAction;
import com.holderzone.intelligencepos.mvp.contract.TableContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableAreaE;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.TablePresenter;
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
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;

/**
 * 桌台界面
 * Created by tcw on 2017/9/4.
 */
public class TableActivity extends BaseActivity<TableContract.Presenter> implements TableContract.View {

    public static final String FAKE_DINING_TABLE_AREA_GUID = "00000000-0000-0000-0000-000000000000";

    private static final String KEY_SAVE_DINING_TABLE_AREA_GUID = "SAVE_DINING_TABLE_AREA_GUID";

    private static final int MSG_WHAT_TABLE = 0;

//    private static final int TABLE_DELAY_MILLIS = 5000;

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

    // origin data source
    private List<DiningTableAreaE> mDiningTableAreaEsReceived = new ArrayList<>();// 接收到的桌台区域集合
    private List<DiningTableE> mDiningTableEsReceived = new ArrayList<>();// 接收到的桌台集合

    // data helper
    private Map<String, List<DiningTableE>> mArrayOfDiningTableHashMap = new HashMap<>();// 记录GUID和桌台实体集合的对应关系
    private Map<String, DiningTableE> mDiningTableHashMap = new HashMap<>();// 记录GUID和桌台实体的对应关系
    private String mDiningTableAreaGUIDSelected = FAKE_DINING_TABLE_AREA_GUID;// 当前选中的桌台区域GUID

    // rv && adapter
    private CommonAdapter<DiningTableAreaE> mDiningTableAreaEAdapter;
    private CommonAdapter<DiningTableE> mDiningTableEAdapter;
    private List<DiningTableAreaE> mDiningTableAreaEs = new ArrayList<>();
    private List<DiningTableE> mDiningTableEs = new ArrayList<>();

    // init flag
    private boolean mInitFinished;

    // logic helper
    private boolean mRefreshing;// 是否正在刷新
    private boolean mRefreshManual;// 是否手动刷新

    // 轮询Handler
    private Handler mHandler;

    // 服务器数据的哈希值
    private String mMd5Hash16 = null;
    private AtomicBoolean needNextRequest = new AtomicBoolean(false);
    private boolean isVisible = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            needNextRequest.set(true);
            requestChangedTableData();
        }
    };

    private void requestChangedTableData() {
        // 若未操作，开启延迟轮询
        if (isVisible == true && isFinishing() == false && needNextRequest.getAndSet(false)) {
            requestMainGuiData();
        }

    }

    private void registBroadcast() {
        IntentFilter intentFilter = new IntentFilter(NoticeBroadcastAction.TABLE_CHANGED);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        requestMainGuiData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, TableActivity.class);
        Bundle extras = new Bundle();
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
        mDiningTableAreaGUIDSelected = savedInstanceState.getString(KEY_SAVE_DINING_TABLE_AREA_GUID);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_table;
    }

    @Override
    protected TableContract.Presenter initPresenter() {
        return new TablePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        registBroadcast();
        // 初始化标题
        initTitle();
        // 初始化桌台区域
        initTableArea();
        // 初始化桌台
        initTable();
        // 初始化轮询Handler
        initHandler();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
        // 此处逻辑与手动刷新逻辑一致
        if (!isRefreshing()) {
            // 手动刷新 置位 才可显示加载动画
            setRefreshManual(true);
            // 请求新的网络数据
            mHandler.sendEmptyMessage(MSG_WHAT_TABLE);
        }
    }

    @Override
    public void onDispose() {
        // 刷新中 复位
        setRefreshing(false);

        // 手动刷新中 复位
        setRefreshManual(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SAVE_DINING_TABLE_AREA_GUID, mDiningTableAreaGUIDSelected);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRefreshing()) {
            mPresenter.disposeMainGUIRefreshInfo();

        }
        mHandler.removeMessages(MSG_WHAT_TABLE);
    }

    /*************************view callback begin*************************/

    /*************************view callback begin***************************/
    @Override
    public void showLoading() {
        // TODO: 2017/5/9  release用第一段代码，debug用第二段代码，以便测试轮询的正确性

        if (isRefreshManual()) {
            super.showLoading();
        }

//        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void refreshMainGui(List<DiningTableAreaE> arrayOfDiningTableAreaE, List<DiningTableE> arrayOfDiningTableE, String md5Hash16) {
        // 处理hash没变时返回数据为空数组的情况
        if (!mInitFinished || arrayOfDiningTableAreaE.size() > 0 || arrayOfDiningTableE.size() > 0) {

            // 初始化置位
            if (!mInitFinished) {
                mInitFinished = true;
            }

            // 生成桌台区域
            generateTableArea(arrayOfDiningTableAreaE);

            // 桌台区域数量不为0时
            if (mMultiStateViewTableArea.getViewState() == MultiStateView.VIEW_STATE_CONTENT) {

                // 修正已选择桌台区域的guid和position
                correctDiningTableAreaGUIDSelected();

                // 刷新桌台区域
                refreshTableAreaRv();

                // 生成桌台
                generateTable(arrayOfDiningTableE);

                // 生成新的hashMap中的SalesOrderGUID(或DiningTableGUID)和List<DininTableE>的关系
                mArrayOfDiningTableHashMap = generateArrayOfTableHashMap();

                // 生成新的hashMap中DiningTableGUID和DininTableE的关系
                mDiningTableHashMap = generateTableHashMap();

                // 生成过滤桌台
                generateTableWithFilter();

                // 过滤桌台数量不为0时
                if (mMultiStateViewTable.getViewState() == MultiStateView.VIEW_STATE_CONTENT) {

                    // 刷新桌台
                    refreshTableRv();
                }
            }
        }
        requestChangedTableData();
        // 手动刷新中 复位
        setRefreshManual(false);

        // 哈希值 赋值
        mMd5Hash16 = md5Hash16;
    }

    @Override
    public void onRequestMainGUIRefreshInfoFailed() {
        requestChangedTableData();

        // 手动刷新中 复位
        setRefreshManual(false);

        // 哈希值 赋值
        mMd5Hash16 = null;
    }

    @Override
    public void onNetworkError() {
        // 切换到网络错误布局
        mMultiStateViewTableArea.setViewState(MultiStateView.VIEW_STATE_ERROR);
        // 哈希值 赋值
        mMd5Hash16 = null;
        // 注册点击事件
        Button btnRetry = (Button) mMultiStateViewTableArea.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            if (!isRefreshing()) {
                requestMainGuiData();
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
        mTitle.setOnReturnClickListener(this::finish);
        mTitle.setOnMenuClickListener(() -> {
            if (!isRefreshing()) {
                // 手动刷新 置位 才可显示加载动画
                setRefreshManual(true);
                // 请求新的网络数据
                requestMainGuiData();
            }
        });
    }

    /**
     * 初始化桌台区域
     */
    private void initTableArea() {
        // 桌台区域 适配器配置
        mDiningTableAreaEAdapter = new CommonAdapter<DiningTableAreaE>(TableActivity.this, R.layout.item_table_area, mDiningTableAreaEs) {
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
                holder.setText(R.id.tv_table_used, "(" + diningTableAreaE.getUseCount());
                holder.setText(R.id.tv_table_total, "/" + diningTableAreaE.getTableCount() + ")");
            }
        };
        mDiningTableAreaEAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String diningTableAreaGUID = mDiningTableAreaEs.get(position).getDiningTableAreaGUID();
                if (diningTableAreaGUID.equalsIgnoreCase(mDiningTableAreaGUIDSelected)) {
                    return;
                }
                mDiningTableAreaGUIDSelected = diningTableAreaGUID;

                // 刷新桌台区域Rv
                refreshTableAreaRv();

                // 按照桌台区域进行过滤
                generateTableWithFilter();

                // 刷新桌台Rv
                refreshTableRv();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRvTableArea.setAdapter(mDiningTableAreaEAdapter);
        mRvTableArea.setLayoutManager(new LinearLayoutManager(TableActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * 初始化桌台
     */
    private void initTable() {
        // 桌台 适配器配置
        mDiningTableEAdapter = new CommonAdapter<DiningTableE>(TableActivity.this, R.layout.item_table, mDiningTableEs) {
            @SuppressLint("RestrictedApi")
            @Override
            protected void convert(ViewHolder holder, DiningTableE diningTableE, int position) {
                // 设置选择效果
                holder.setText(R.id.tv_table_name, diningTableE.getName());// 桌台名
                Integer tableStatus = diningTableE.getTableStatus();
                Preconditions.checkNotNull(tableStatus, "tableStatus==null");
                if (tableStatus == TableStatusConstans.STATUS_FREE) {// 空闲
                    holder.setBackgroundRes(R.id.rl_table_title, R.drawable.shape_table_activity_item_table_title_bg_free);// 标题背景颜色
                    holder.setTextColorRes(R.id.tv_table_status_or_consume, R.color.tv_text_green_01b6ad);// 消费合计字体颜色
                    holder.setText(R.id.tv_table_status_or_consume, diningTableE.getTableStatusCN());// 消费合计
                    holder.setVisible(R.id.iv_table_calc, false);// 算账标识
                    Integer seatsCount = diningTableE.getSeatsCount();
                    Preconditions.checkNotNull(seatsCount, "seatsCount==null");
                    holder.setText(R.id.tv_table_persons, "0/" + seatsCount);// 桌台使用人数
                    holder.setVisible(R.id.tv_table_use_hours, false);// 桌台使用时间
                    holder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg);
                    holder.setVisible(R.id.iv_merge_identify, false);
                    Integer isOrder = diningTableE.getIsOrder();
                    Preconditions.checkNotNull(isOrder, "isOrder==null");
                    holder.setVisible(R.id.iv_order_identify, 1 == isOrder);
                } else {// 占用
                    SalesOrderE salesOrderE = diningTableE.getSalesOrderE();
                    Preconditions.checkNotNull(salesOrderE, "salesOrderE==null");
                    Integer dishesHangCount = salesOrderE.getDishesHangCount();
                    Preconditions.checkNotNull(dishesHangCount, "dishesHangCount==null");
                    if (0 == dishesHangCount) {// 占用 未挂起
                        holder.setBackgroundRes(R.id.rl_table_title, R.drawable.shape_table_activity_item_table_title_bg_none_free);// 标题背景颜色
                        holder.setTextColorRes(R.id.tv_table_status_or_consume, R.color.tv_text_red_f56766);// 消费合计字体颜色
                    } else {// 占用 已挂起
                        holder.setBackgroundRes(R.id.rl_table_title, R.drawable.shape_table_activity_item_table_title_bg_hang);// 标题背景颜色
                        holder.setTextColorRes(R.id.tv_table_status_or_consume, R.color.tv_text_orange_f4a902);// 消费合计字体颜色
                    }
                    Integer upperState = salesOrderE.getUpperState();
                    Preconditions.checkNotNull(upperState, "upperState==null");
                    if (0 == upperState.intValue()) {// 占用 无并单
                        holder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg);
                        Double consumeTotal = salesOrderE.getConsumeTotal();
                        Preconditions.checkNotNull(consumeTotal, "consumeTotal==null");
                        holder.setText(R.id.tv_table_status_or_consume, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(consumeTotal)));
                        holder.setVisible(R.id.iv_merge_identify, false);// 无并单标识
                    } else {//  占用 有并单
                        if (1 == upperState.intValue()) {// 占用 主单
                            holder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg);
                            Double consumeTotal = salesOrderE.getConsumeTotal();
                            Preconditions.checkNotNull(consumeTotal, "consumeTotal==null");
                            holder.setText(R.id.tv_table_status_or_consume, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(consumeTotal)));
                        } else {// 占用 子单
                            holder.setBackgroundRes(R.id.ll_table_list_item, R.drawable.shape_table_activity_item_table_bg);
                            holder.setText(R.id.tv_table_status_or_consume, "已并桌");
                        }
                        holder.setVisible(R.id.iv_merge_identify, true);// 有并单标识
                    }
                    Integer calculationPrintCount = salesOrderE.getCalculationPrintCount();
                    Preconditions.checkNotNull(calculationPrintCount, "calculationPrintCount==null");
                    holder.setVisible(R.id.iv_table_calc, calculationPrintCount > 0);// 算账标识
                    Integer guestCount = salesOrderE.getGuestCount();
                    Preconditions.checkNotNull(guestCount, "guestCount==null");
                    Integer seatsCount = diningTableE.getSeatsCount();
                    Preconditions.checkNotNull(seatsCount, "seatsCount==null");
                    holder.setText(R.id.tv_table_persons, guestCount + "/" + seatsCount);// 桌台使用人数
                    holder.setVisible(R.id.tv_table_use_hours, true);
                    holder.setText(R.id.tv_table_use_hours, salesOrderE.getUseHour());// 桌台使用时间
                    holder.setVisible(R.id.iv_order_identify, false);// 不显示预订
                }
            }
        };

        mDiningTableEAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                DiningTableE diningTableE = mDiningTableEs.get(position);
                Integer tableStatus = diningTableE.getTableStatus();
                Preconditions.checkNotNull(tableStatus, "tableStatus==null");
                if (TableStatusConstans.STATUS_FREE == tableStatus.intValue()) {// 空闲
                    startActivity(OpenTableActivity.newIntent(TableActivity.this, diningTableE));
                } else {
                    // 占用
                    SalesOrderE salesOrderE = diningTableE.getSalesOrderE();
                    Preconditions.checkNotNull(salesOrderE, "salesOrderE==null");
                    Integer upperState = salesOrderE.getUpperState();
                    if (0 == upperState) {// 无并单
                        String salesOrderGUID = salesOrderE.getSalesOrderGUID();
                        Integer orderBatchCount = salesOrderE.getOrderBatchCount();
                        Preconditions.checkNotNull(orderBatchCount, "orderBatchCount==null");
                        if (0 == orderBatchCount) {// 未点餐
                            startActivity(TableOpenedActivity.newIntent(TableActivity.this, diningTableE));
                        } else {// 已点餐
                            startActivity(DishesOrderedActivity.newIntent(TableActivity.this, diningTableE));
                        }
                    } else {// 有并单
                        String mainSalesOrderGUID;
                        if (1 == upperState) {// 主单
                            mainSalesOrderGUID = salesOrderE.getSalesOrderGUID();
                        } else {// 子单
                            mainSalesOrderGUID = salesOrderE.getUpperGUID();
                        }
                        startActivity(TableMergedActivity.newIntent(TableActivity.this, mArrayOfDiningTableHashMap.get(mainSalesOrderGUID)));
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //设置页面滚动监听mDiningTableEAdapter
        mRvTable.setLayoutManager(new GridLayoutManager(TableActivity.this, 3));
        mRvTable.setAdapter(mDiningTableEAdapter);
        mRvTable.addItemDecoration(new SmartGridSpacingItemDecoration(DensityUtils.dp2px(this, 10)));
        ((SimpleItemAnimator) mRvTable.getItemAnimator()).setSupportsChangeAnimations(false);
//        mRvTable.getItemAnimator().setChangeDuration(0);
    }

    /**
     * 初始化轮询Handler
     */
    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_WHAT_TABLE:// table interval
                        requestMainGuiData();
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };
    }

    /**
     * 请求堂食主界面数据
     */
    private void requestMainGuiData() {
        // 刷新中 置位
        setRefreshing(true);
        // 请求新的网络数据
        mPresenter.requestMainGUIRefreshInfo(mMd5Hash16);
    }

    /**
     * 生成桌台区域
     */
    @SuppressLint("RestrictedApi")
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
     * 修正已选择桌台区域的guid和position
     */
    private void correctDiningTableAreaGUIDSelected() {
        for (DiningTableAreaE diningTableAreaE : mDiningTableAreaEsReceived) {
            if (diningTableAreaE.getDiningTableAreaGUID().equalsIgnoreCase(mDiningTableAreaGUIDSelected)) {
                return;
            }
        }
        mDiningTableAreaGUIDSelected = FAKE_DINING_TABLE_AREA_GUID;// 默认选中"全部"
    }

    /**
     * 刷新桌台区域RV 不清除数据
     */
    private void refreshTableAreaRv() {
        mDiningTableAreaEAdapter.notifyDataSetChanged();
    }

    /**
     * 生成桌台
     *
     * @param arrayOfDiningTableE
     */
    @SuppressLint("RestrictedApi")
    private void generateTable(List<DiningTableE> arrayOfDiningTableE) {
        Preconditions.checkNotNull(arrayOfDiningTableE, "arrayOfDiningTableE==null");
        mDiningTableEsReceived = arrayOfDiningTableE;
    }

    /**
     * 构造hashMap SalesOrderGUID(或DiningTableGUID)和List<DininTableE>的关系
     */
    @SuppressLint("RestrictedApi")
    private Map<String, List<DiningTableE>> generateArrayOfTableHashMap() {
        Map<String, List<DiningTableE>> arrayOfDiningTableHashMap = new HashMap<>();// 记录guid和实体的对应关系
        for (DiningTableE tableE : mDiningTableEsReceived) {
            Integer tableStatus = tableE.getTableStatus();
            Preconditions.checkNotNull(tableStatus, "tableStatus==null");
            if (TableStatusConstans.STATUS_FREE == tableStatus.intValue()) {
                List<DiningTableE> diningTableEs = new ArrayList<>();
                diningTableEs.add(tableE);
                arrayOfDiningTableHashMap.put(tableE.getDiningTableGUID(), diningTableEs);
            } else if (TableStatusConstans.STATUS_NONE_FREE == tableStatus.intValue()) {
                SalesOrderE salesOrderE = tableE.getSalesOrderE();
                Preconditions.checkNotNull(salesOrderE, "salesOrderE==null");
                Integer upperState = salesOrderE.getUpperState();
                Preconditions.checkNotNull(upperState, "upperState==null");
                if (2 == upperState.intValue()) {// 子单
                    List<DiningTableE> diningTableEs = arrayOfDiningTableHashMap.get(salesOrderE.getUpperGUID());
                    if (diningTableEs == null) {
                        diningTableEs = new ArrayList<>();
                        arrayOfDiningTableHashMap.put(salesOrderE.getUpperGUID(), diningTableEs);
                    }
                    diningTableEs.add(tableE);
                } else {// 主单 或 无并单
                    List<DiningTableE> diningTableEs = arrayOfDiningTableHashMap.get(salesOrderE.getSalesOrderGUID());
                    if (diningTableEs == null) {
                        diningTableEs = new ArrayList<>();
                        arrayOfDiningTableHashMap.put(salesOrderE.getSalesOrderGUID(), diningTableEs);
                    }
                    diningTableEs.add(0, tableE);
                }
            }
        }

        return arrayOfDiningTableHashMap;
    }

    /**
     * 构造hashMap DiningTableGUID和DininTableE的关系
     */
    private Map<String, DiningTableE> generateTableHashMap() {
        Map<String, DiningTableE> diningTableHashMap = new HashMap<>();// 记录guid和实体的对应关系
        for (DiningTableE diningTableE : mDiningTableEsReceived) {
            diningTableHashMap.put(diningTableE.getDiningTableGUID(), diningTableE);
        }
        return diningTableHashMap;
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
     * 刷新桌台RV 清除数据并重新构造
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
     * 设置手动刷新标志位
     * 以方法的形式进行调用，当切换逻辑需要改变时只需要修改该方法内部代码，外部的调用保持不变
     *
     * @param enter 进入手动刷新中
     */
    private void setRefreshManual(boolean enter) {
        if (enter) {
            if (!mRefreshManual) {
                mRefreshManual = true;
            }
        } else {
            if (mRefreshManual) {
                mRefreshManual = false;
            }
        }
    }

    /**
     * 获取手动刷新标志位
     *
     * @return
     */
    private boolean isRefreshManual() {
        return mRefreshManual;
    }

    /**************************private method end**************************/

    /*************************inner class begin***************************/

    /**
     * 桌台呈现出来的所有状态
     * DiningTableE实体只返回STATUS_FREE和STATUS_NONE_FREE两种
     * TableStatus实体可返回以下所有状态
     */
    public static class TableStatusConstans {
        public static final int STATUS_ALL = -1;
        public static final int STATUS_FREE = 0;
        public static final int STATUS_NONE_FREE = 1;
        public static final int STATUS_HANG = 51;
        public static final int STATUS_MERGED = 52;
        public static final int STATUS_CALC = 53;
        public static final int STATUS_ORDER = 60;
    }

    /**************************inner class end****************************/
}