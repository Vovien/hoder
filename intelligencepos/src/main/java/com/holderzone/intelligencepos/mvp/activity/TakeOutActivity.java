package com.holderzone.intelligencepos.mvp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.decoration.LinearSpacingItemDecoration;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.msg.NoticeBroadcastAction;
import com.holderzone.intelligencepos.mvp.contract.TakeOutContract;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderCategory;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderE;
import com.holderzone.intelligencepos.mvp.presenter.TakeOutPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DateUtils;
import com.holderzone.intelligencepos.utils.DensityUtils;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ItemViewDelegate;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.holderzone.intelligencepos.widget.recyclerview.wrapper.EmptyWrapper;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;

/**
 * Created by LT on 2018-04-02.
 * 外卖订单列表页面
 */

public class TakeOutActivity extends BaseActivity<TakeOutContract.Presenter> implements TakeOutContract.View {
    public static final int ORDER_TYPE_FAKE = -1;// 全部
    public static final int ORDER_TYPE_TAKEOUT = 0;// 外卖
    public static final int ORDER_TYPE_WEIXIN = 1;// 微信
    public static final int ORDER_TYPE_OTHERS = 2;// 其他

    public static final String MSG_TYPE_NEW = "1010";// 新订单
    public static final String MSG_TYPE_REMIND = "1020";// 催单
    public static final String MSG_TYPE_RETREAT = "1030";// 取消订单

    public static final int ORDER_SUB_TYPE_FAKE = -1;// 子分类中全部
    public static final int ORDER_SUB_TYPE_MEITUAN_WHEN_TAKEOUT = 0;// 美团
    public static final int ORDER_SUB_TYPE_ELEME_WHEN_TAKEOUT = 1;// 饿了么
    public static final int ORDER_SUB_TYPE_BAIDU_WHEN_TAKEOUT = 2;// 百度
    public static final int ORDER_SUB_TYPE_JINGDONG_WHEN_TAKEOUT = 3;// 京东
    public static final int ORDER_SUB_TYPE_SCAN_WHEN_WEIXIN = 0;// 微信扫码
    public static final int ORDER_SUB_TYPE_PREDICTE_WHEN_WEIXIN = 1;// 微信预订单

    public static final int ORDER_STATUS_NEW = 0;// 待处理订单
    public static final int ORDER_STATUS_CANCELED = -1;// 订单已取消
    public static final int ORDER_STATUS_RECEIVED = 10;// 订单已接单
    public static final int ORDER_STATUS_ON_THE_ROAD = 20;// 订单配送
    public static final int ORDER_STATUS_FINISHED = 100;// 订单完成
    public static final int ORDER_STATUS_RETREATED = -200;// 订单已退单

    public static final int MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM = 0;// 轮询what字段

//    private static final long UNORDER_ZIP_WITH_PLATFORM_DELAY_MILLIS = 5000;// 轮询时间间隔
    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.rv_unorder_category)
    RecyclerView mRvUnorderCategory;
    @BindView(R.id.rv_take_out)
    RecyclerView mRvTakeOut;
    @BindView(R.id.multi_state_view_take_out)
    MultiStateView multiStateViewTakeOut;
    @BindView(R.id.multi_state_view)
    MultiStateView multiStateView;
    /**
     * 消息类型
     */
    private List<UnOrderCategory> mArrayOfUnOrderCategory = new ArrayList<>();
    /**
     * 订单类别adapter
     */
    private CommonAdapter<UnOrderCategory> mUnOrderCategoryAdapter;
    /**
     * 选中的消息类型 新订单 催单 取消订单
     */
    private String mMsgTypeSelected = MSG_TYPE_NEW;
    /**
     * 外卖列表数据adapter
     */
    private EmptyWrapper<UnOrderE> mUnOrderAdapter;
    /**
     * 外卖订单数据
     */
    private List<UnOrderE> mArrayOfUnOrder = new ArrayList<>();
    /**
     * 收到的外卖订单集合，未经过滤
     */
    private List<UnOrderE> mArrayOfUnOrderReceived = new ArrayList<>();
    /**
     * 轮询handler
     */
    private Handler mHandler;
    /**
     * 是否正在刷新
     */
    private boolean mRefreshing;
    /**
     * 是否是手动刷新
     */
    private boolean mRefreshManual;
    /**
     * 业务使用订单实体，用于记录即将进行业务操作的订单实体
     */
    private UnOrderE mUnOrderE4Buzz;
    private AtomicBoolean needNextRequest = new AtomicBoolean(false);
    private boolean isVisible = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            needNextRequest.set(true);
            requestChangedData();
        }
    };

    private void requestChangedData() {
        if (isVisible == true && isFinishing() == false && needNextRequest.getAndSet(false)) {
            mPresenter.requestUnOrderAndPlatformStatistic();
        }

    }

    private void registBroadcast() {
        IntentFilter intentFilter = new IntentFilter(NoticeBroadcastAction.TAKEOUT_CHANGED);
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
        if (!isRefreshing()) {
            // 需要加载对话框
            setRefreshManual(true);
            // 请求新的网络数据
            requestUnOrderAndPlatformStatistic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, TakeOutActivity.class);
    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_take_out;
    }

    @Nullable
    @Override
    protected TakeOutContract.Presenter initPresenter() {
        return new TakeOutPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        registBroadcast();
        //title点击返回
        mTitle.setOnReturnClickListener(this::finishActivity);
        //initFakeData
        initFakeData();
        //初始化订单类别adapter
        initUnOrderCategoryAdapter();
        //初始化订单数据
        initUnOrderAdapter();
        //初始化轮询Handler
        initHandler();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }


    @Override
    protected void onStop() {
        super.onStop();
        // 取消订阅
        mPresenter.disposeUnOrderAndPlatformStatistic();
        // 移除消息
        mHandler.removeMessages(MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM);
    }

    private void initUnOrderCategoryAdapter() {
        mUnOrderCategoryAdapter = new CommonAdapter<UnOrderCategory>(this, R.layout.item_unorder_category, mArrayOfUnOrderCategory) {
            @Override
            protected void convert(ViewHolder holder, UnOrderCategory unOrderCategory, int position) {
                if (unOrderCategory.getUnOrderCategoryCount() == 0) {
                    holder.setText(R.id.tv_unorder_type_count, unOrderCategory.getUnOrderCategoryName());
                } else {
                    holder.setText(R.id.tv_unorder_type_count, unOrderCategory.getUnOrderCategoryName()
                            + "（" + unOrderCategory.getUnOrderCategoryCount() + "）");
                }
                if (mMsgTypeSelected.equals(unOrderCategory.getMsgType())) {// 选中
                    holder.setTextColor(R.id.tv_unorder_type_count, Color.parseColor("#2495ee"));
                    holder.setBackgroundRes(R.id.view_indicator, R.color.btn_text_blue_2495ee);
                } else {// 未选中
                    holder.setTextColor(R.id.tv_unorder_type_count, Color.parseColor("#555555"));
                    holder.setBackgroundRes(R.id.view_indicator, R.color.btn_text_white_ffffff);
                }
            }
        };
        mUnOrderCategoryAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String msgTypeSelected = mArrayOfUnOrderCategory.get(position).getMsgType();
                if (!mMsgTypeSelected.equals(msgTypeSelected)) {
                    // 更新选中
                    mMsgTypeSelected = msgTypeSelected;
                    // 过滤订单数据
                    filterUnorderThenRefresh();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRvUnorderCategory.setAdapter(mUnOrderCategoryAdapter);
        mRvUnorderCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initUnOrderAdapter() {
        mRvTakeOut.setLayoutManager(new LinearLayoutManager(this));
        UnOrderAdapter unOrderAdapter = new UnOrderAdapter(TakeOutActivity.this, mArrayOfUnOrder);
        mUnOrderAdapter = new EmptyWrapper<>(unOrderAdapter);
        mUnOrderAdapter.setEmptyView(R.layout.empty_unorder);
        mRvTakeOut.setAdapter(mUnOrderAdapter);
        mRvTakeOut.addItemDecoration(new LinearSpacingItemDecoration(DensityUtils.dp2px(this, 6.3f), true));
    }

    /**
     * 初始化fake数据
     */
    private void initFakeData() {
        mArrayOfUnOrderCategory.add(new UnOrderCategory(MSG_TYPE_NEW, "新订单", 0));
        mArrayOfUnOrderCategory.add(new UnOrderCategory(MSG_TYPE_REMIND, "催单", 0));
        mArrayOfUnOrderCategory.add(new UnOrderCategory(MSG_TYPE_RETREAT, "取消订单", 0));
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM:
                        requestUnOrderAndPlatformStatistic();
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };
    }

    /**
     * 请求待处理外卖订单列表
     */
    private void requestUnOrderAndPlatformStatistic() {
        // 刷新中 置位
        setRefreshing(true);
        // 请求新的网络数据
        mPresenter.requestUnOrderAndPlatformStatistic();
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
     * 获取刷新标志位
     *
     * @return
     */
    private boolean isRefreshing() {
        return mRefreshing;
    }

    /**
     * 获取手动刷新标志位
     *
     * @return
     */
    private boolean isRefreshManual() {
        return mRefreshManual;
    }

    /**
     * 每次手动业务前准备工作
     */
    private void preparedForManualBusiness() {
        // 轮询正在刷新，dispose本次请求
        if (isRefreshing()) {
            mPresenter.disposeUnOrderAndPlatformStatistic();
        }
        // 移除消息，取消轮询
        mHandler.removeMessages(MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM);
        // 手动操作标志为设置为true
        setRefreshManual(true);
    }

    /**
     * 过滤订单
     */
    private void filterUnorderThenRefresh() {
        // 过滤选中订单类型
        List<UnOrderE> tempOfSameOrderType = new ArrayList<>();
//        for (UnOrderE unOrderE : mArrayOfUnOrderReceived) {
//            tempOfSameOrderType.add(unOrderE);
//        }
        tempOfSameOrderType.addAll(mArrayOfUnOrderReceived);
        // 刷新选中订单类型下各消息类型的数量
        Map<String, Integer> map = new HashMap<>();
        for (UnOrderE unOrderE : tempOfSameOrderType) {
            String msgType = unOrderE.getMsgType();
            Integer integer = map.get(msgType);
            if (integer == null) {
                map.put(msgType, 1);
            } else {
                map.put(msgType, integer + 1);
            }
        }
        for (UnOrderCategory unOrderCategory : mArrayOfUnOrderCategory) {
            Integer integer = map.get(unOrderCategory.getMsgType());
            unOrderCategory.setUnOrderCategoryCount(integer != null ? integer : 0);
        }
        //更新消息类型adapter
        mUnOrderCategoryAdapter.notifyDataSetChanged();
        //刷新订单列表
        List<UnOrderE> tempOfBothSameOrderTypeAndMsgType = new ArrayList<>();
        for (UnOrderE unOrderE : tempOfSameOrderType) {
            if (unOrderE.getMsgType().equals(mMsgTypeSelected)) {
                tempOfBothSameOrderTypeAndMsgType.add(unOrderE);
            }
        }
        mArrayOfUnOrder.clear();
        mArrayOfUnOrder.addAll(tempOfBothSameOrderTypeAndMsgType);
        if (mArrayOfUnOrder.size() == 0) {
            multiStateViewTakeOut.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else if (mArrayOfUnOrder.size() > 0) {
            multiStateViewTakeOut.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
        mUnOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        if (isRefreshManual()) {
            super.showLoading();
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onRequestUnOrderAndPlatformStatisticSucceed(List<UnOrderE> arrayOfUnOrderE) {
        // 更新所有订单数据
        mArrayOfUnOrderReceived = arrayOfUnOrderE;
        // 过滤并刷新
        filterUnorderThenRefresh();
        // 刷新中 复位
        setRefreshing(false);

        // 手动刷新中 复位
        setRefreshManual(false);

        requestChangedData();
    }

    @Override
    public void onRequestUnOrderAndPlatformStatisticFailed() {
        // 刷新中 复位
        setRefreshing(false);

        // 手动刷新中 复位
        setRefreshManual(false);

        requestChangedData();
    }

    @Override
    public void onRequestUnOrderAndPlatformStatisticDisposed() {
        // 如果正在刷新，说明正在显示的加载对话框需要隐藏
        if (isRefreshing()) {
            // 隐藏对话框
            hideLoading();
        }

        // 刷新中 复位
        setRefreshing(false);

        // 手动刷新中 复位
        setRefreshManual(false);

        requestChangedData();
    }

    @Override
    public void onSubmitConfirmOrderSucceed() {
        // 手动操作复位
        setRefreshManual(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM);
    }

    @Override
    public void onSubmitConfirmOrderFailed() {
        // 手动操作复位
        setRefreshManual(false);
        requestChangedData();
    }

    @Override
    public void onSubmitConfirmOrderThenEnterDetailsSucceed() {
        //接单成功后 请求下单
        preparedForManualBusiness();
        mPresenter.confirmOrder(mUnOrderE4Buzz);
    }

    @Override
    public void onSubmitConfirmOrderThenEnterDetailsFailed() {
        // 手动操作复位
        setRefreshManual(false);
        requestChangedData();
    }

    @Override
    public void onSubmitRejectOrderSucceed() {
        // 手动操作复位
        setRefreshManual(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM);
    }

    @Override
    public void onSubmitRejectOrderFailed() {
        // 手动操作复位
        setRefreshManual(false);
        requestChangedData();
    }

    @Override
    public void onSubmitConfirmChargeBackSucceed() {
        // 手动操作复位
        setRefreshManual(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM);
    }

    @Override
    public void onSubmitConfirmChargeBackFailed() {
        // 手动操作复位
        setRefreshManual(false);
        requestChangedData();
    }

    @Override
    public void onSubmitRefuseChargeBackSucceed() {
        // 手动操作复位
        setRefreshManual(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM);
    }

    @Override
    public void onSubmitRefuseChargeBackFailed() {
        // 手动操作复位
        setRefreshManual(false);
        requestChangedData();
    }

    @Override
    public void onConfirmOrderSuccess() {
        // 手动操作复位
        setRefreshManual(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM);
    }

    @Override
    public void addDishesEstimateFailed() {
        // 手动操作复位
        setRefreshManual(false);
        requestChangedData();
        //菜品超过估清
        getDialogFactory().showConfirmDialog("部分菜品已售罄，请更换成其他菜品。", "取消", "确定", new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {
            }

            @Override
            public void onPosClick() {
                //菜品详情页面
                startActivity(TakeawayBillDetailActivity.newIntent(TakeOutActivity.this, mUnOrderE4Buzz.getUnOrderGUID()
                        , mUnOrderE4Buzz.getUnOrderReceiveMsgGUID(), mUnOrderE4Buzz.getOrderType()
                        , mUnOrderE4Buzz.getOrderSubType(), mUnOrderE4Buzz.getDaySn(), true, false));
            }
        });
    }

    @Override
    public void onConfirmOrderFailedInnerNoDish() {
        // 手动操作复位
        setRefreshManual(false);
        requestChangedData();
        //没有菜品
        getDialogFactory().showConfirmDialog("部分菜品门店无货，请更换成其他菜品。", "取消", "确定", new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {

            }

            @Override
            public void onPosClick() {
                //菜品详情页面
                startActivity(TakeawayBillDetailActivity.newIntent(TakeOutActivity.this, mUnOrderE4Buzz.getUnOrderGUID()
                        , mUnOrderE4Buzz.getUnOrderReceiveMsgGUID(), mUnOrderE4Buzz.getOrderType()
                        , mUnOrderE4Buzz.getOrderSubType(), mUnOrderE4Buzz.getDaySn(), true, false));
            }
        });
    }

    @Override
    public void onConfirmOrderFailed() {
        // 手动操作复位
        setRefreshManual(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_UNORDER_ZIP_WITH_PLATFORM);
    }

    @Override
    public void onConfirmReturnDishes() {
        mDialogFactory.showConfirmDialog("该订单已经开始制作,同意顾客取消订单吗?"
                , "暂不取消", "确定同意", new ConfirmDialogFragment.ConfirmDialogListener() {
                    @Override
                    public void onNegClick() {

                    }

                    @Override
                    public void onPosClick() {
                        //同意退单
                        preparedForManualBusiness();
                        mPresenter.submitConfirmChargeBack(mUnOrderE4Buzz.getUnOrderGUID(), mUnOrderE4Buzz.getUnOrderReceiveMsgGUID(), false);
                    }
                });
    }

    /*************************inner class begin*************************/
    private class UnOrderAdapter extends MultiItemTypeAdapter<UnOrderE> {

        public UnOrderAdapter(Context context, List<UnOrderE> datas) {
            super(context, datas);
            addItemViewDelegate(new NewTakeOutDelegate());
            addItemViewDelegate(new ReceivedTakeOutDelegate());
            addItemViewDelegate(new RemindTakeOutDelegate());
            addItemViewDelegate(new RetreatTakeOutDelegate());
            addItemViewDelegate(new NewWeiXinDelegate());
        }
    }

    /**
     * 美团 饿了么的新订单
     */
    private class NewTakeOutDelegate implements ItemViewDelegate<UnOrderE> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_unorder_new_takeout;
        }

        @Override
        public boolean isForViewType(UnOrderE item, int position) {
            return item.getMsgType().equals(MSG_TYPE_NEW)
                    && item.getOrderType() == ORDER_TYPE_TAKEOUT
                    && item.getOrderStatus() == ORDER_STATUS_NEW;
        }

        @Override
        public void convert(ViewHolder holder, UnOrderE unOrderE, int position) {
            if (unOrderE.getOrderSubType() == ORDER_SUB_TYPE_ELEME_WHEN_TAKEOUT) {
                holder.setImageResource(R.id.iv_unorder_type, R.drawable.ele_small);
            } else if (unOrderE.getOrderSubType() == ORDER_SUB_TYPE_MEITUAN_WHEN_TAKEOUT) {
                holder.setImageResource(R.id.iv_unorder_type, R.drawable.meituan_small);
            }
            String daySn = unOrderE.getDaySn();
            holder.setText(R.id.tv_sort_number, "#" + (TextUtils.isEmpty(daySn) ? "#" : daySn));
            Long deliveryTimeStamp = unOrderE.getDeliveryTimeStamp();
            String timeLine = (deliveryTimeStamp == null || deliveryTimeStamp == 0)
                    ? "立即配送" : (DateUtils.format(new Date(deliveryTimeStamp), "HH:mm") + "前出餐");
            holder.setText(R.id.tv_time_line, timeLine);
            holder.setText(R.id.tv_customer_name, unOrderE.getCustomerName());
            holder.setText(R.id.tv_customer_phone, unOrderE.getCustomerPhone());
            holder.setText(R.id.tv_customer_address, unOrderE.getCustomerAddress());
            String remark = unOrderE.getRemark();
            if (!TextUtils.isEmpty(remark)) {
                holder.setVisible(R.id.rl_remark, true);
                holder.setText(R.id.tv_remark, remark);
            } else {
                holder.setVisible(R.id.rl_remark, false);
            }
            //订单信息
            holder.setText(R.id.tv_order_count, String.valueOf(unOrderE.getDishesCount()));
            holder.setText(R.id.order_money, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(unOrderE.getTotal())));
            holder.setOnClickListener(R.id.rl_order_detail, v -> {
                //跳转到订单详情页面
                startActivity(TakeawayBillDetailActivity.newIntent(TakeOutActivity.this, unOrderE.getUnOrderGUID()
                        , unOrderE.getUnOrderReceiveMsgGUID(), unOrderE.getOrderType()
                        , unOrderE.getOrderSubType(), unOrderE.getDaySn(), true, true));
            });
            holder.setOnClickListener(R.id.btn_refuse, v -> mDialogFactory.showConfirmDialog("确定要拒单吗？"
                    , "取消", "确定拒单", new ConfirmDialogFragment.ConfirmDialogListener() {
                        @Override
                        public void onNegClick() {

                        }

                        @Override
                        public void onPosClick() {
                            //拒单
                            preparedForManualBusiness();
                            mPresenter.submitRejectOrder(unOrderE.getUnOrderGUID(), unOrderE.getUnOrderReceiveMsgGUID());
                        }
                    }));
            holder.setText(R.id.btn_recv, "接单 ");
            holder.setOnClickListener(R.id.btn_recv, v -> mDialogFactory.showConfirmDialog("确定要接单吗？"
                    , "取消", "确定接单", new ConfirmDialogFragment.ConfirmDialogListener() {
                        @Override
                        public void onNegClick() {
                        }

                        @Override
                        public void onPosClick() {
                            //接单
                            preparedForManualBusiness();
                            mPresenter.submitConfirmOrder(unOrderE.getUnOrderGUID(), unOrderE.getUnOrderReceiveMsgGUID(), 0);
                        }
                    }));
            holder.setOnClickListener(R.id.btn_recv_then_confirm, v -> mDialogFactory.showConfirmDialog("确定要接单吗？"
                    , "取消", "确定接单", new ConfirmDialogFragment.ConfirmDialogListener() {
                        @Override
                        public void onNegClick() {
                        }

                        @Override
                        public void onPosClick() {
                            //接单并下单
                            mUnOrderE4Buzz = unOrderE;
                            preparedForManualBusiness();
                            mPresenter.submitConfirmOrderThenEnterDetails(unOrderE.getUnOrderGUID(), unOrderE.getUnOrderReceiveMsgGUID(), 0);
                        }
                    }));
        }
    }

    /**
     * 微信的新订单
     */
    private class NewWeiXinDelegate implements ItemViewDelegate<UnOrderE> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_unorder_new_weixin;
        }

        @Override
        public boolean isForViewType(UnOrderE item, int position) {
            return item.getMsgType().equals(MSG_TYPE_NEW)
                    && item.getOrderType().equals(ORDER_TYPE_WEIXIN);
        }

        @Override
        public void convert(ViewHolder holder, UnOrderE unOrderE, int position) {
            holder.setImageResource(R.id.iv_unorder_type, R.drawable.wechat_takeaway_small);
            String daySn = unOrderE.getDaySn();
            holder.setText(R.id.tv_sort_number, "#" + (TextUtils.isEmpty(daySn) ? "#" : daySn));
            holder.setText(R.id.tv_sub_order_type, unOrderE.getOrderSubType() == 0 ? "扫码点餐" : "微信预订单");
            holder.setText(R.id.tv_table_name, unOrderE.getDiningTableName());
            holder.setText(R.id.tv_customer_count, unOrderE.getCustomerNumber() + "人");
            holder.setText(R.id.tv_order_created_time, DateUtils.format(new Date(unOrderE.getCreateOrderTimeStamp()), DateUtils.getDatePattern()));
            //订单信息
            holder.setText(R.id.tv_order_count, String.valueOf(unOrderE.getDishesCount()));
            holder.setText(R.id.order_money, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(unOrderE.getTotal())));
            holder.setOnClickListener(R.id.rl_order_detail, v -> {
                //跳转到订单详情页面
                startActivity(TakeawayBillDetailActivity.newIntent(TakeOutActivity.this, unOrderE.getUnOrderGUID()
                        , unOrderE.getUnOrderReceiveMsgGUID(), unOrderE.getOrderType()
                        , unOrderE.getOrderSubType(), unOrderE.getDaySn(), true, false));
            });
            holder.setOnClickListener(R.id.btn_invalid, v -> mDialogFactory.showConfirmDialog("确定要作废吗？"
                    , "取消", "确定", new ConfirmDialogFragment.ConfirmDialogListener() {
                        @Override
                        public void onNegClick() {

                        }

                        @Override
                        public void onPosClick() {
                            //微信作废账单
                            preparedForManualBusiness();
                            mPresenter.submitRejectOrder(unOrderE.getUnOrderGUID(), unOrderE.getUnOrderReceiveMsgGUID());
                        }
                    }));
            holder.setText(R.id.btn_confirm, "下单");
            holder.setOnClickListener(R.id.btn_confirm, v ->
                    mDialogFactory.showConfirmDialog("确认要下单吗？"
                            , "取消", "确认下单", new ConfirmDialogFragment.ConfirmDialogListener() {
                                @Override
                                public void onNegClick() {

                                }

                                @Override
                                public void onPosClick() {
                                    //微信下单
                                    mUnOrderE4Buzz = unOrderE;
                                    preparedForManualBusiness();
                                    mPresenter.confirmOrder(mUnOrderE4Buzz);
                                }
                            }));
        }
    }

    private class ReceivedTakeOutDelegate implements ItemViewDelegate<UnOrderE> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_unorder_received_takeout;
        }

        @Override
        public boolean isForViewType(UnOrderE item, int position) {
            return item.getMsgType().equals(MSG_TYPE_NEW)
                    && item.getOrderType() == ORDER_TYPE_TAKEOUT
                    && item.getOrderStatus() != ORDER_STATUS_NEW;
        }

        @Override
        public void convert(ViewHolder holder, UnOrderE unOrderE, int position) {
            if (unOrderE.getOrderSubType() == ORDER_SUB_TYPE_ELEME_WHEN_TAKEOUT) {
                holder.setImageResource(R.id.iv_unorder_type, R.drawable.ele_small);
            } else if (unOrderE.getOrderSubType() == ORDER_SUB_TYPE_MEITUAN_WHEN_TAKEOUT) {
                holder.setImageResource(R.id.iv_unorder_type, R.drawable.meituan_small);
            }
            String daySn = unOrderE.getDaySn();
            holder.setText(R.id.tv_sort_number, "#" + (TextUtils.isEmpty(daySn) ? "#" : daySn));
            Long deliveryTimeStamp = unOrderE.getDeliveryTimeStamp();
            String timeLine = (deliveryTimeStamp == null || deliveryTimeStamp == 0)
                    ? "立即配送" : (DateUtils.format(new Date(deliveryTimeStamp), "HH:mm") + "前出餐");
            holder.setText(R.id.tv_time_line, timeLine);
            holder.setText(R.id.tv_customer_name, unOrderE.getCustomerName());
            holder.setText(R.id.tv_customer_phone, unOrderE.getCustomerPhone());
            holder.setText(R.id.tv_customer_address, unOrderE.getCustomerAddress());
            holder.setText(R.id.tv_order_status, obtainOrderStatus(unOrderE.getOrderStatus()));
            String remark = unOrderE.getRemark();
            if (!TextUtils.isEmpty(remark)) {
                holder.setVisible(R.id.rl_remark, true);
                holder.setText(R.id.tv_remark, remark);
            } else {
                holder.setVisible(R.id.rl_remark, false);
            }
            //订单信息
            holder.setText(R.id.tv_order_count, String.valueOf(unOrderE.getDishesCount()));
            holder.setText(R.id.order_money, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(unOrderE.getTotal())));
            holder.setOnClickListener(R.id.rl_order_detail, v -> {
                //跳转到订单详情页面
                startActivity(TakeawayBillDetailActivity.newIntent(TakeOutActivity.this, unOrderE.getUnOrderGUID()
                        , unOrderE.getUnOrderReceiveMsgGUID(), unOrderE.getOrderType()
                        , unOrderE.getOrderSubType(), unOrderE.getDaySn(), true, false));
            });
            holder.setOnClickListener(R.id.btn_confirm, v ->
                    mDialogFactory.showConfirmDialog("确认要下单吗？"
                            , "取消", "确认下单", new ConfirmDialogFragment.ConfirmDialogListener() {
                                @Override
                                public void onNegClick() {

                                }

                                @Override
                                public void onPosClick() {
                                    mUnOrderE4Buzz = unOrderE;
                                    preparedForManualBusiness();
                                    mPresenter.confirmOrder(mUnOrderE4Buzz);
                                }
                            })
            );
        }
    }

    /**
     * 催单
     */
    private class RemindTakeOutDelegate implements ItemViewDelegate<UnOrderE> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_unorder_remind_takeout;
        }

        @Override
        public boolean isForViewType(UnOrderE item, int position) {
            return item.getMsgType().equals(MSG_TYPE_REMIND)
                    && item.getOrderType() == ORDER_TYPE_TAKEOUT;
        }

        @Override
        public void convert(ViewHolder holder, UnOrderE unOrderE, int position) {
            if (unOrderE.getOrderSubType() == ORDER_SUB_TYPE_ELEME_WHEN_TAKEOUT) {
                holder.setImageResource(R.id.iv_unorder_type, R.drawable.ele_small);
            } else if (unOrderE.getOrderSubType() == ORDER_SUB_TYPE_MEITUAN_WHEN_TAKEOUT) {
                holder.setImageResource(R.id.iv_unorder_type, R.drawable.meituan_small);
            }
            String daySn = unOrderE.getDaySn();
            holder.setText(R.id.tv_sort_number, "#" + (TextUtils.isEmpty(daySn) ? "#" : daySn));
            Long deliveryTimeStamp = unOrderE.getDeliveryTimeStamp();
            String timeLine = (deliveryTimeStamp == null || deliveryTimeStamp == 0)
                    ? "立即配送" : (DateUtils.format(new Date(deliveryTimeStamp), "HH:mm") + "前出餐");
            holder.setText(R.id.tv_time_line, timeLine);
            holder.setText(R.id.tv_customer_name, unOrderE.getCustomerName());
            holder.setText(R.id.tv_customer_phone, unOrderE.getCustomerPhone());
            holder.setText(R.id.tv_customer_address, unOrderE.getCustomerAddress());
            holder.setText(R.id.tv_order_status, obtainOrderStatus(unOrderE.getOrderStatus()));
            String remark = unOrderE.getRemark();
            if (!TextUtils.isEmpty(remark)) {
                holder.setVisible(R.id.rl_remark, true);
                holder.setText(R.id.tv_remark, remark);
            } else {
                holder.setVisible(R.id.rl_remark, false);
            }
            //订单信息
            holder.setText(R.id.tv_order_count, String.valueOf(unOrderE.getDishesCount()));
            holder.setText(R.id.order_money, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(unOrderE.getTotal())));
            holder.setOnClickListener(R.id.rl_order_detail, v -> {
                //跳转到订单详情页面
                startActivity(TakeawayBillDetailActivity.newIntent(TakeOutActivity.this, unOrderE.getUnOrderGUID()
                        , unOrderE.getUnOrderReceiveMsgGUID(), unOrderE.getOrderType()
                        , unOrderE.getOrderSubType(), unOrderE.getDaySn(), false, false));
            });
            holder.setOnClickListener(R.id.btn_reply, v -> {
                //催单回复
                mUnOrderE4Buzz = unOrderE;
                //跳转回复页面
                startActivity(ReplyReminderActivity.newIntent(TakeOutActivity.this, unOrderE.getUnOrderGUID(), unOrderE.getUnOrderReceiveMsgGUID()));
            });
        }
    }

    /**
     * 取消订单
     */
    private class RetreatTakeOutDelegate implements ItemViewDelegate<UnOrderE> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_unorder_retreat_takeout;
        }

        @Override
        public boolean isForViewType(UnOrderE item, int position) {
            return item.getMsgType().equals(MSG_TYPE_RETREAT)
                    && item.getOrderType() == ORDER_TYPE_TAKEOUT;
        }

        @Override
        public void convert(ViewHolder holder, UnOrderE unOrderE, int position) {
            if (unOrderE.getOrderSubType() == ORDER_SUB_TYPE_ELEME_WHEN_TAKEOUT) {
                holder.setImageResource(R.id.iv_unorder_type, R.drawable.ele_small);
            } else if (unOrderE.getOrderSubType() == ORDER_SUB_TYPE_MEITUAN_WHEN_TAKEOUT) {
                holder.setImageResource(R.id.iv_unorder_type, R.drawable.meituan_small);
            }
            String daySn = unOrderE.getDaySn();
            holder.setText(R.id.tv_sort_number, "#" + (TextUtils.isEmpty(daySn) ? "#" : daySn));
            Long deliveryTimeStamp = unOrderE.getDeliveryTimeStamp();
            String timeLine = (deliveryTimeStamp == null || deliveryTimeStamp == 0)
                    ? "立即配送" : (DateUtils.format(new Date(deliveryTimeStamp), "HH:mm") + "前出餐");
            holder.setText(R.id.tv_time_line, timeLine);
            holder.setText(R.id.tv_customer_name, unOrderE.getCustomerName());
            holder.setText(R.id.tv_customer_phone, unOrderE.getCustomerPhone());
            holder.setText(R.id.tv_customer_address, unOrderE.getCustomerAddress());
            String remark = unOrderE.getRemark();
            if (!TextUtils.isEmpty(remark)) {
                holder.setVisible(R.id.rl_remark, true);
                holder.setText(R.id.tv_remark, remark);
            } else {
                holder.setVisible(R.id.rl_remark, false);
            }
            //订单信息
            holder.setText(R.id.tv_order_count, String.valueOf(unOrderE.getDishesCount()));
            holder.setText(R.id.order_money, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(unOrderE.getTotal())));
            holder.setOnClickListener(R.id.rl_order_detail, v -> {
                //跳转到订单详情页面
                startActivity(TakeawayBillDetailActivity.newIntent(TakeOutActivity.this, unOrderE.getUnOrderGUID()
                        , unOrderE.getUnOrderReceiveMsgGUID(), unOrderE.getOrderType()
                        , unOrderE.getOrderSubType(), unOrderE.getDaySn(), false, false));
            });
            holder.setOnClickListener(R.id.btn_disagree, v -> mDialogFactory.showConfirmDialog("拒绝顾客取消订单吗？"
                    , "取消", "确定拒绝", new ConfirmDialogFragment.ConfirmDialogListener() {
                        @Override
                        public void onNegClick() {
                        }

                        @Override
                        public void onPosClick() {
                            //拒绝退单
                            preparedForManualBusiness();
                            mPresenter.submitRefuseChargeBack(unOrderE.getUnOrderGUID(), unOrderE.getUnOrderReceiveMsgGUID());
                        }
                    }));
            holder.setOnClickListener(R.id.btn_agree, v -> mDialogFactory.showConfirmDialog("同意顾客取消订单吗？"
                    , "取消", "确定同意", new ConfirmDialogFragment.ConfirmDialogListener() {
                        @Override
                        public void onNegClick() {

                        }

                        @Override
                        public void onPosClick() {
                            //同意退单
                            mUnOrderE4Buzz = unOrderE;
                            preparedForManualBusiness();
                            mPresenter.submitConfirmChargeBack(unOrderE.getUnOrderGUID(), unOrderE.getUnOrderReceiveMsgGUID(), true);
                        }
                    }));
        }
    }
    /*************************inner class end*************************/
    /**
     * 根据订单状态code获得订单状态描述
     *
     * @param status
     * @return
     */
    private String obtainOrderStatus(int status) {
        switch (status) {
            case ORDER_STATUS_NEW:
                return "新订单";
            case ORDER_STATUS_CANCELED:
                return "已取消";
            case ORDER_STATUS_RECEIVED:
                return "已接单";
            case ORDER_STATUS_ON_THE_ROAD:
                return "配送中";
            case ORDER_STATUS_FINISHED:
                return "已完成";
            case ORDER_STATUS_RETREATED:
                return "已退单";
            default:
                return "未知订单状态";
        }
    }
}
