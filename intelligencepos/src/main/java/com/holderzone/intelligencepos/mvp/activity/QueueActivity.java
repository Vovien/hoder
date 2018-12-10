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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.decoration.SolidLineItemDecoration;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.DiningNumberDialogFragment;
import com.holderzone.intelligencepos.msg.NoticeBroadcastAction;
import com.holderzone.intelligencepos.mvp.contract.QueueContract;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpTypeE;
import com.holderzone.intelligencepos.mvp.presenter.QueuePresenter;
import com.holderzone.intelligencepos.utils.BaiduTTS;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.QueuePopup;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 排队 Activity
 */
public class QueueActivity extends BaseActivity<QueueContract.Presenter> implements QueueContract.View, QueuePopup.QueuePopupListener, DiningNumberDialogFragment.DiningNumberDialogListener, ConfirmDialogFragment.ConfirmDialogListener {
    private static final String KEY_QUEUE_UP_TYPE_GUID_SELECTED = "QUEUE_UP_TYPE_GUID_SELECTED";
    private static final String KEY_QUEUE_UP_RECORD_GUID_SELECTED = "QUEUE_UP_RECORD_GUID_SELECTED";
    private static final String KEY_PHONE_NUMBER_SELECTED = "PHONE_NUMBER_SELECTED";
    private static final String KEY_SB_PHONE_NUMBER = "SB_PHONE_NUMBER";
    private static final String KEY_SB_DINING_NUMBER = "SB_DINING_NUMBER";

    private static final String FAKE_QUEUE_UP_TYPE_ALL = "00000000-0000-0000-0000-000000000000";
    private static final String FAKE_QUEUE_UP_TYPE_HISTORY = "ffffffff-ffff-ffff-ffff-ffffffffffff";

    private static final int MSG_WHAT_QUEUE_UP_RECORD = 0;
//    private static final int QUEUE_UP_RECORD_DELAY_MILLIS = 5000;

    private static final int KEY_ADD_QUEUE = 1001;
    private static final int KEY_STATISTIC_QUEUE = 1101;

    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_more_image)
    ImageView titleMoreImage;
    @BindView(R.id.rv_title)
    RecyclerView rvTitle;
    @BindView(R.id.rv_context)
    RecyclerView rvContext;
    @BindView(R.id.msv_context)
    MultiStateView msvContext;
    @BindView(R.id.btn_fetch_number)
    Button btnFetchNumber;
    @BindView(R.id.btn_retry)
    Button btnRetry;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    /**
     * 排号类型相关数据
     */
    private CommonAdapter<QueueUpTypeE> mQueueUpTypeEAdapter;
    private List<QueueUpTypeE> mQueueUpTypeEList = new ArrayList<>();
    private String mQueueUpTypeGUIDSelected;

    /**
     * 排号记录相关数据
     */
    private CommonAdapter<QueueUpRecordE> mQueueUpRecordEAdapter;
    private List<QueueUpRecordE> mAllQueueUpRecordEList = new ArrayList<>();
    private List<QueueUpRecordE> mQueueUpRecordEList = new ArrayList<>();

    /**
     * 取号相关
     */
    private boolean mPhoneNumberSelected;
    private StringBuilder mSbPhoneNumber = new StringBuilder();
    private StringBuilder mSbDiningNumber = new StringBuilder();

    /**
     * 轮询Handler
     */
    private Handler mHandler;
    /**
     * 是否正在刷新标志位(请求排队类型、排队记录，不包含叫号、就餐等操作)
     */
    private boolean mRefreshing;

    /**
     * 是否手动操作标志位(手动操作才显示loading对话框)
     */
    private boolean mOperateManually;

    /**
     * 当前点击需要修改人数的QueueUpRecordGUID;
     */
    private String mQueueUpRecordGUID;

    private AtomicBoolean needNextRequest = new AtomicBoolean(false);
    private boolean isVisible = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            needNextRequest.set(true);
            requestQueueData();
        }
    };

    private void requestQueueData() {
        if (isVisible == true && isFinishing() == false && needNextRequest.getAndSet(false)) {
            mPresenter.getQueueUpInfo();
        }

    }

    private void registBroadcast() {
        IntentFilter intentFilter = new IntentFilter(NoticeBroadcastAction.QUEUE_CHANGED);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, QueueActivity.class);
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
        mQueueUpTypeGUIDSelected = savedInstanceState.getString(KEY_QUEUE_UP_TYPE_GUID_SELECTED);
        mQueueUpRecordGUID = savedInstanceState.getString(KEY_QUEUE_UP_RECORD_GUID_SELECTED);
        mPhoneNumberSelected = savedInstanceState.getBoolean(KEY_PHONE_NUMBER_SELECTED);
        mSbPhoneNumber.append(savedInstanceState.getString(KEY_SB_PHONE_NUMBER));
        mSbDiningNumber.append(savedInstanceState.getString(KEY_SB_DINING_NUMBER));
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_queue;
    }

    @Nullable
    @Override
    protected QueueContract.Presenter initPresenter() {
        return new QueuePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        registBroadcast();
        msvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        initTitleRecycler();
        initContextRecycler();
        initClickEvent();
        // 初始化Handler
        initHandler();
        ivEmpty.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.no_queue));
        tvEmpty.setText("暂无排队");
        tvEmpty.setTextSize(21);
        tvEmpty.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.btn_text_gray_8b8b8b));
    }

    private void initTitleRecycler() {
        mQueueUpTypeEAdapter = new CommonAdapter<QueueUpTypeE>(this, R.layout.queue_title_item, mQueueUpTypeEList) {
            @Override
            protected void convert(ViewHolder holder, QueueUpTypeE queueUpTypeE, int position) {
                holder.setText(R.id.tv_type_name, queueUpTypeE.getName());
                Integer waitingCount = queueUpTypeE.getWaitingCount();
                if (waitingCount != null) {
                    holder.setVisible(R.id.tv_type_waiting_count, true);
                    holder.setText(R.id.tv_type_waiting_count, waitingCount >= 10 ? "  " + waitingCount + "  " : waitingCount + "");
                } else {
                    holder.setVisible(R.id.tv_type_waiting_count, false);
                }

                if (queueUpTypeE.getQueueUpTypeGUID().equalsIgnoreCase(mQueueUpTypeGUIDSelected)) {
                    holder.setBackgroundRes(R.id.tv_type_waiting_count, R.drawable.shape_wating_number_selected);
                    holder.setVisible(R.id.view_bottom_line, true);
                } else {
                    holder.setBackgroundRes(R.id.tv_type_waiting_count, R.drawable.shape_wating_number);
                    holder.setVisible(R.id.view_bottom_line, false);
                }
            }
        };

        mQueueUpTypeEAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mQueueUpTypeGUIDSelected = mQueueUpTypeEList.get(position).getQueueUpTypeGUID();
                mQueueUpTypeEAdapter.notifyDataSetChanged();
                refreshRecordWithFilter();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        rvTitle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTitle.setAdapter(mQueueUpTypeEAdapter);
    }

    private void initContextRecycler() {
        mQueueUpRecordEAdapter = new CommonAdapter<QueueUpRecordE>(QueueActivity.this, R.layout.queue_context_item, mQueueUpRecordEList) {

            @Override
            protected void convert(ViewHolder holder, QueueUpRecordE queueUpRecordE, int position) {
                // 排号编码
                holder.setText(R.id.tv_queue_number, queueUpRecordE.getCode() + "");
                // 客人数
                holder.setText(R.id.tv_dining_number, queueUpRecordE.getCustomerCount() + "人");
                // 客人电话
                String customerTel = queueUpRecordE.getCustomerTel();

                Integer isDelete = queueUpRecordE.getIsDelete();
                if (null != isDelete) {
                    if (1 == isDelete.intValue()) {// 已删除
                        holder.setTextColorRes(R.id.tv_queue_number, R.color.tv_text_gray_de555555);
                        holder.setEnable(R.id.ll_modify_dining_number, false);
                        holder.setImageResource(R.id.iv_modify_dining_number, R.drawable.people_select_down_gray);
                        // 等待时间
                        holder.setText(R.id.tv_waiting_time, "已放弃");
                        // ctrl 1
                        holder.setInVisible(R.id.rl_call_or_revoke, false);
                        // ctrl 2
                        holder.setInVisible(R.id.ll_dining, false);
                        // ctrl 3
                        holder.setInVisible(R.id.ll_pass_number_or_delete, false);
                    } else {
                        holder.setTextColorRes(R.id.tv_queue_number, R.color.tv_text_blue_de2495ee);
                        holder.setEnable(R.id.ll_modify_dining_number, true);
                        holder.setImageResource(R.id.iv_modify_dining_number, R.drawable.people_select_down);
                        Integer queueUpStatus = queueUpRecordE.getQueueUpStatus();
                        if (queueUpStatus != null) {
                            int status = queueUpStatus.intValue();
                            if (0 == status) {// 排号中
                                // 等待时间
                                holder.setText(R.id.tv_waiting_time, getString(R.string.queue_wait_time, String.valueOf(queueUpRecordE.getWaitingTime())));
                                // ctrl 1
                                holder.setImageResource(R.id.iv_call_or_revoke, R.drawable.call_number);
                                holder.setEnable(R.id.rl_call_or_revoke, true);
                                holder.setTextColorRes(R.id.tv_call_or_revoke, R.color.tv_text_gray_555555);
                                holder.setOnClickListener(R.id.rl_call_or_revoke, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startTTTS("请" + queueUpRecordE.getCode() + "号顾客就餐");
                                        // 取消轮询、dispose请求、手动操作标志置true
                                        preparedForManualBusiness();
                                        mPresenter.submitCall(queueUpRecordE.getQueueUpRecordGUID());
                                    }
                                });
                                holder.setText(R.id.tv_call_or_revoke, "叫号");
                                holder.setInVisible(R.id.rl_call_or_revoke, true);
                                if (queueUpRecordE.getCallTimes() == 0) {
                                    holder.setVisible(R.id.tv_call_or_revoke_times, false);
                                } else {
                                    holder.setVisible(R.id.tv_call_or_revoke_times, true);
                                    holder.setText(R.id.tv_call_or_revoke_times, queueUpRecordE.getCallTimes() < 10 ? "" + queueUpRecordE.getCallTimes() : "  " + (queueUpRecordE.getCallTimes() < 100 ? queueUpRecordE.getCallTimes() : "99+") + "  ");// 叫号次数
                                }
                                // ctrl 2
                                holder.setInVisible(R.id.ll_dining, true);
                                holder.setOnClickListener(R.id.ll_dining, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mQueueUpRecordGUID = queueUpRecordE.getQueueUpRecordGUID();
                                        Integer index = queueUpRecordE.getIndex();
                                        if (index != null) {
                                            if (index.intValue() == 1) {
                                                // 取消轮询、dispose请求、手动操作标志置true
                                                preparedForManualBusiness();
                                                mPresenter.submitConfirm(queueUpRecordE.getQueueUpRecordGUID());
                                            } else {
                                                mDialogFactory.showConfirmDialog("此操作为跳号操作，确定选择？", "取消", "确定", QueueActivity.this);
                                            }
                                        }
                                    }
                                });
                                // ctrl 3
                                holder.setImageResource(R.id.iv_pass_number_or_delete, R.drawable.skip_number);
                                holder.setText(R.id.tv_pass_number_or_delete, "过号");
                                holder.setInVisible(R.id.ll_pass_number_or_delete, true);
                                holder.setOnClickListener(R.id.ll_pass_number_or_delete, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // 取消轮询、dispose请求、手动操作标志置true
                                        preparedForManualBusiness();
                                        mPresenter.submitSkip(queueUpRecordE.getQueueUpRecordGUID());
                                    }
                                });
                            } else {// 排号完成
                                // 等待时间
                                holder.setText(R.id.tv_waiting_time, queueUpRecordE.getWaitingTime() + "分钟");
                                if (1 == status) {// 已就餐
                                    // ctrl 1
                                    holder.setImageResource(R.id.iv_call_or_revoke, R.drawable.restore);
                                    holder.setEnable(R.id.rl_call_or_revoke, true);
                                    holder.setTextColorRes(R.id.tv_call_or_revoke, R.color.tv_text_gray_555555);
                                    holder.setOnClickListener(R.id.rl_call_or_revoke, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // 取消轮询、dispose请求、手动操作标志置true
                                            preparedForManualBusiness();
                                            mPresenter.submitRecover(queueUpRecordE.getQueueUpRecordGUID());
                                        }
                                    });
                                    holder.setText(R.id.tv_call_or_revoke, "撤销");
                                    holder.setInVisible(R.id.rl_call_or_revoke, true);
                                    if (queueUpRecordE.getCallTimes() == 0) {
                                        holder.setVisible(R.id.tv_call_or_revoke_times, false);
                                    } else {
                                        holder.setVisible(R.id.tv_call_or_revoke_times, true);
                                        holder.setText(R.id.tv_call_or_revoke_times, queueUpRecordE.getCallTimes() < 10 ? "" + queueUpRecordE.getCallTimes() : "  " + (queueUpRecordE.getCallTimes() < 100 ? queueUpRecordE.getCallTimes() : "99+") + "  ");// 叫号次数
                                    }
                                    // ctrl 2
                                    holder.setInVisible(R.id.ll_dining, false);
                                    // ctrl 3
                                    holder.setInVisible(R.id.ll_pass_number_or_delete, false);
                                } else {// 已过号
                                    // ctrl 1
                                    holder.setImageResource(R.id.iv_call_or_revoke, R.drawable.restore);
                                    holder.setEnable(R.id.rl_call_or_revoke, true);
                                    holder.setTextColorRes(R.id.tv_call_or_revoke, R.color.tv_text_gray_555555);
                                    holder.setOnClickListener(R.id.rl_call_or_revoke, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // 取消轮询、dispose请求、手动操作标志置true
                                            preparedForManualBusiness();
                                            mPresenter.submitRecover(queueUpRecordE.getQueueUpRecordGUID());
                                        }
                                    });
                                    holder.setText(R.id.tv_call_or_revoke, "撤销");
                                    holder.setInVisible(R.id.rl_call_or_revoke, true);
                                    if (queueUpRecordE.getCallTimes() == 0) {
                                        holder.setVisible(R.id.tv_call_or_revoke_times, false);
                                    } else {
                                        holder.setVisible(R.id.tv_call_or_revoke_times, true);
                                        holder.setText(R.id.tv_call_or_revoke_times, queueUpRecordE.getCallTimes() < 10 ? "" + queueUpRecordE.getCallTimes() : "  " + (queueUpRecordE.getCallTimes() < 100 ? queueUpRecordE.getCallTimes() : "99+") + "  ");// 叫号次数
                                    }
                                    // ctrl 2
                                    holder.setInVisible(R.id.ll_dining, true);
                                    holder.setOnClickListener(R.id.ll_dining, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // 取消轮询、dispose请求、手动操作标志置true
                                            preparedForManualBusiness();
                                            mPresenter.submitConfirm(queueUpRecordE.getQueueUpRecordGUID());
                                        }
                                    });
                                    // ctrl 3
                                    holder.setImageResource(R.id.iv_pass_number_or_delete, R.drawable.delete);
                                    holder.setText(R.id.tv_pass_number_or_delete, "删除");
                                    holder.setInVisible(R.id.ll_pass_number_or_delete, true);
                                    holder.setOnClickListener(R.id.ll_pass_number_or_delete, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // 取消轮询、dispose请求、手动操作标志置true
                                            preparedForManualBusiness();
                                            mPresenter.submitDelete(queueUpRecordE.getQueueUpRecordGUID());
                                        }
                                    });
                                }

                            }
                        }
                    }
                }

                // 点击事件
                holder.setOnClickListener(R.id.ll_modify_dining_number, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mQueueUpRecordGUID = queueUpRecordE.getQueueUpRecordGUID();
                        for (QueueUpTypeE queueUpTypeE : mQueueUpTypeEList) {
                            if (queueUpRecordE.getQueueUpTypeGUID().equalsIgnoreCase(queueUpTypeE.getQueueUpTypeGUID())) {
                                int minCustomerCount = queueUpTypeE.getMinCustomerCount();
                                int maxCustomerCount = queueUpTypeE.getMaxCustomerCount();
                                mDialogFactory.showDiningNumberDialog("修改人数", queueUpRecordE.getCustomerCount(), minCustomerCount, maxCustomerCount, 1, "取消", "确定", QueueActivity.this);
                                break;
                            }
                        }
                    }
                });
            }
        };
        mQueueUpRecordEAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        rvContext.setLayoutManager(new LinearLayoutManager(this));
        rvContext.setAdapter(mQueueUpRecordEAdapter);
        rvContext.addItemDecoration(new SolidLineItemDecoration(QueueActivity.this, LinearLayoutManager.VERTICAL));
    }

    /**
     * 初始化轮询Handler
     */
    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_WHAT_QUEUE_UP_RECORD:
                        // 正在刷新 置位
                        setRefreshing(true);
                        // 请求排队数据
                        mPresenter.getQueueUpInfo();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /***
     * 开始语音播报
     */
    private void startTTTS(String text) {
        BaiduTTS.getInstance().speak(text);
    }

    /**
     * 每次手动业务前准备工作
     */
    private void preparedForManualBusiness() {
        // 轮询正在刷新，dispose本次请求
        if (isRefreshing()) {
            mPresenter.disposeQueueUpInfo();
        }
        // 移除消息，取消轮询
        mHandler.removeMessages(MSG_WHAT_QUEUE_UP_RECORD);
        // 手动操作标志为设置为true
        setOperateManually(true);
    }

    /**
     * 是否正在刷新
     *
     * @return
     */
    private boolean isRefreshing() {
        return mRefreshing;
    }

    /**
     * 是否手动操作中
     *
     * @return
     */
    private boolean isOperateManually() {
        return mOperateManually;
    }

    /**
     * 设置是否手动操作标志位
     *
     * @param enter
     */
    private void setOperateManually(boolean enter) {
        if (enter) {
            if (!mOperateManually) {
                mOperateManually = true;
            }
        } else {
            if (mOperateManually) {
                mOperateManually = false;
            }
        }
    }

    /**
     * 设置是否正在刷新标志位
     *
     * @param enter
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
     * 根据QueueType过滤QueueRecord
     */
    private void refreshRecordWithFilter() {
        List<QueueUpRecordE> temp = new ArrayList<>();
        if (mQueueUpTypeGUIDSelected.equalsIgnoreCase(FAKE_QUEUE_UP_TYPE_ALL)) {
            for (QueueUpRecordE queueUpRecordE : mAllQueueUpRecordEList) {
                if (queueUpRecordE.getQueueUpStatus() == 0) {// 排号中
                    temp.add(queueUpRecordE);
                }
            }
        } else if (mQueueUpTypeGUIDSelected.equalsIgnoreCase(FAKE_QUEUE_UP_TYPE_HISTORY)) {
            for (QueueUpRecordE queueUpRecordE : mAllQueueUpRecordEList) {
                if (queueUpRecordE.getQueueUpStatus() != 0) {// 排号完成
                    temp.add(queueUpRecordE);
                }
            }
        } else {
            for (QueueUpRecordE queueUpRecordE : mAllQueueUpRecordEList) {
                if (queueUpRecordE.getQueueUpStatus() == 0 && queueUpRecordE.getQueueUpTypeGUID().equalsIgnoreCase(mQueueUpTypeGUIDSelected)) {
                    temp.add(queueUpRecordE);
                }
            }
        }
        if (temp.size() > 0) {
            mQueueUpRecordEList.clear();
            mQueueUpRecordEList.addAll(temp);
            mQueueUpRecordEAdapter.notifyDataSetChanged();
            msvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }


    @SuppressLint("CheckResult")
    private void initClickEvent() {
        RxView.clicks(btnFetchNumber).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(o -> {
            startActivityForResult(AddQueueActivity.newInstance(this), KEY_ADD_QUEUE);
        });

        RxView.clicks(titleMoreImage).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            QueuePopup dishesOrderedPopup = new QueuePopup(QueueActivity.this);
            dishesOrderedPopup.showOnAnchor(titleMoreImage,
                    RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
        });

        RxView.clicks(btnRetry).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(o -> {
            onRefreshClick();
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (!isRefreshing()) {
            // 手动刷新 置位 才可显示加载动画
            setOperateManually(true);
            // 请求新的网络数据
            mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 取消订阅
        mPresenter.disposeQueueUpInfo();
        // 移除消息，停止轮询
        mHandler.removeMessages(MSG_WHAT_QUEUE_UP_RECORD);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putString(KEY_QUEUE_UP_TYPE_GUID_SELECTED, mQueueUpTypeGUIDSelected);
        outState.putString(KEY_QUEUE_UP_RECORD_GUID_SELECTED, mQueueUpRecordGUID);
        outState.putBoolean(KEY_PHONE_NUMBER_SELECTED, mPhoneNumberSelected);
        outState.putString(KEY_SB_PHONE_NUMBER, mSbPhoneNumber.toString());
        outState.putString(KEY_SB_DINING_NUMBER, mSbDiningNumber.toString());
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    /*******************************view callback begin*******************************/
    @Override
    public void showLoading() {
        if (isOperateManually()) {
            super.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void onDispose() {
        // 正在刷新复位
        setRefreshing(false);
    }

    @Override
    public void onGetQueueUpInfoSucceed(List<QueueUpTypeE> queueUpTypeEList, List<QueueUpRecordE> queueUpRecordEList) {
        rvTitle.setVisibility(View.VISIBLE);
        // 排队类型数据处理
        if (queueUpTypeEList != null) {
            mQueueUpTypeEList.clear();
            mQueueUpTypeEList.addAll(queueUpTypeEList);

            int waitingCount = 0;
            for (QueueUpTypeE queueUpTypeE : mQueueUpTypeEList) {
                waitingCount += queueUpTypeE.getWaitingCount();
            }
            // 添加 "所有"item
            QueueUpTypeE queueUpTypeAll = new QueueUpTypeE();
            queueUpTypeAll.setName("所有");
            queueUpTypeAll.setQueueUpTypeGUID(FAKE_QUEUE_UP_TYPE_ALL);
            queueUpTypeAll.setWaitingCount(waitingCount);
            mQueueUpTypeEList.add(0, queueUpTypeAll);

            // 添加 "历史"item
            QueueUpTypeE queueUpTypeHistory = new QueueUpTypeE();
            queueUpTypeHistory.setName("历史");
            queueUpTypeHistory.setQueueUpTypeGUID(FAKE_QUEUE_UP_TYPE_HISTORY);
            mQueueUpTypeEList.add(queueUpTypeHistory);

            // 修正当前选中GUID
            boolean queueUpTypeGuidFound = false;
            for (QueueUpTypeE queueUpTypeE : mQueueUpTypeEList) {
                if (!queueUpTypeGuidFound && queueUpTypeE.getQueueUpTypeGUID().equalsIgnoreCase(mQueueUpTypeGUIDSelected)) {
                    queueUpTypeGuidFound = true;
                }
            }
            if (!queueUpTypeGuidFound) {
                mQueueUpTypeGUIDSelected = FAKE_QUEUE_UP_TYPE_ALL;
            }

            // 刷新adapter
            mQueueUpTypeEAdapter.notifyDataSetChanged();
        }
        // 排队记录数据处理
        if (queueUpRecordEList != null) {
            if (queueUpRecordEList.size() > 0) {
                mAllQueueUpRecordEList.clear();
                mAllQueueUpRecordEList.addAll(queueUpRecordEList);
                refreshRecordWithFilter();
            } else {
                msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        }
        // 刷新当前统计数据
        refreshCurrentStatistic();
        // 手动刷新复位
        setOperateManually(false);
        requestQueueData();
    }

    /**
     * 刷新当前统计信息
     */
    private void refreshCurrentStatistic() {
//        List<QueueUpRecordE> arrayOfQueueUpRecordUnfinished = new ArrayList<>();
//        for (QueueUpRecordE queueUpRecordE : mAllQueueUpRecordEList) {
//            if (queueUpRecordE.getQueueUpStatus() == 0) {
//                arrayOfQueueUpRecordUnfinished.add(queueUpRecordE);
//            }
//        }
//        int waitingCount = arrayOfQueueUpRecordUnfinished.size();
//        mTvWaitingCount.setText(new SpanUtils()
//                .append("等待桌数  ").setFontSize(22, true).setForegroundColor(ContextCompat.getColor(this, R.color.btn_text_gray_555555))
//                .append(waitingCount + "桌").setFontSize(28, true).setForegroundColor(ContextCompat.getColor(this, R.color.btn_text_red_f56766))
//                .create()
//        );
//        String curWaitingCode = waitingCount > 0 ? arrayOfQueueUpRecordUnfinished.get(0).getCode() : "- -";
//        mTvCurrentQueueNumber.setText(new SpanUtils()
//                .append("当前  ").setFontSize(22, true).setForegroundColor(ContextCompat.getColor(this, R.color.btn_text_gray_555555))
//                .append(curWaitingCode).setFontSize(28, true).setForegroundColor(ContextCompat.getColor(this, R.color.btn_text_red_f56766))
//                .create()
//        );
    }

    @Override
    public void onGetQueueUpInfoFailed() {
        // 手动刷新复位
        setOperateManually(false);
        requestQueueData();
    }

    @Override
    public void onQueueUpInfoDisposed() {
        // 手动刷新复位
        setOperateManually(false);
    }

    @Override
    public void onCallSuccess() {
        // 手动操作复位
        setOperateManually(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
    }

    @Override
    public void onCallFailed() {
        // 手动操作复位
        setOperateManually(false);
        requestQueueData();
    }

    @Override
    public void onUpdateCustomerCountSuccess() {
        // 手动操作复位
        setOperateManually(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
    }

    @Override
    public void onUpdateCustomerCountFailed() {
        // 手动操作复位
        setOperateManually(false);
        requestQueueData();
    }

    @Override
    public void onConfirmSuccess() {
        // 手动操作复位
        setOperateManually(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
    }

    @Override
    public void onConfirmFailed() {
        // 手动操作复位
        setOperateManually(false);
        requestQueueData();
    }

    @Override
    public void onSkipSuccess() {
        // 手动操作复位
        setOperateManually(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
    }

    @Override
    public void onSkipFailed() {
        // 手动操作复位
        setOperateManually(false);
        requestQueueData();
    }

    @Override
    public void onRecoverSuccess() {
        // 手动操作复位
        setOperateManually(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
    }

    @Override
    public void onRecoverFailed() {
        // 手动操作复位
        setOperateManually(false);
        requestQueueData();
    }

    @Override
    public void onDeleteSuccess() {
        // 手动操作复位
        setOperateManually(false);
        // 开始轮询
        mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
    }

    @Override
    public void onDeleteFailed() {
        // 手动操作复位
        setOperateManually(false);
        requestQueueData();
    }

    @Override
    public void onNetWorkError() {
        // 移除消息，停止轮询
        mHandler.removeMessages(MSG_WHAT_QUEUE_UP_RECORD);
        // 取消订阅
        mPresenter.disposeQueueUpInfo();
        rvTitle.setVisibility(View.GONE);
        msvContext.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == KEY_ADD_QUEUE && resultCode == RESULT_OK) {
            // 手动操作复位
            setOperateManually(false);
            // 开始轮询
            mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
        } else if (requestCode == KEY_STATISTIC_QUEUE && resultCode == RESULT_OK) {
            // 手动操作复位
            setOperateManually(false);
            // 开始轮询
            mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onQueuePopupClick() {
        startActivityForResult(QueueStatisticActivity.newInstance(this), KEY_STATISTIC_QUEUE);
    }

    @Override
    public void onRefreshClick() {
        if (!isRefreshing()) {
            // 移除消息，停止轮询
            mHandler.removeMessages(MSG_WHAT_QUEUE_UP_RECORD);
            // 手动刷新 置位 才可显示加载动画
            setOperateManually(true);
            // 请求新的网络数据
            mHandler.sendEmptyMessage(MSG_WHAT_QUEUE_UP_RECORD);
        }
    }

    @OnClick({R.id.back_image, R.id.title_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_image:
            case R.id.title_text:
                finishActivity();
                break;
        }
    }

    @Override
    public void onDiningNumberDialogLeftBtnClick(int number) {

    }

    @Override
    public void onDiningNumberDialogRightBtnClick(int number) {
        // 取消轮询、dispose请求、手动操作标志置true
        preparedForManualBusiness();
        mPresenter.submitUpdateCustomerCount(mQueueUpRecordGUID, number);
    }

    @Override
    public void onNegClick() {

    }

    @Override
    public void onPosClick() {
        // 取消轮询、dispose请求、手动操作标志置true
        preparedForManualBusiness();
        mPresenter.submitConfirm(mQueueUpRecordGUID);
    }
}
