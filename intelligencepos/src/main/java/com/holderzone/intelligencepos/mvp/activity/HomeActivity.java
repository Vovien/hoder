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
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.dialog.DialogFactory;
import com.holderzone.intelligencepos.msg.NoticeBroadcastAction;
import com.holderzone.intelligencepos.mvp.contract.HomeContract;
import com.holderzone.intelligencepos.mvp.model.bean.HomeItemBean;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.presenter.HomePresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.StatusBarUtil;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.widget.Title;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 * 获取营业日、设置营业日
 * 包含堂食收银、快餐收银、会员管理、预订、排队、菜品估清、收银记录、后台管理、功能设置、退出系统等模块的跳转
 * Created by tcw on 2017/5/26.
 */
public class HomeActivity extends BaseActivity<HomeContract.Presenter> implements HomeContract.View {
    private static final int MSG_WHAT_HOME = 1;
//    private static final int HOME_DELAY_MILLIS = 5000;

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.tv_count_of_dine_check_out_unfinished)
    TextView mTvCountOfDineCheckoutUnfinished;
    @BindView(R.id.tv_fast_food_register_cashier)
    TextView mTvFastFoodRegisterCashier;
    @BindView(R.id.tv_take_out_cashier)
    TextView mTvTakeOutCashier;
    @BindView(R.id.tv_queue)
    TextView mTvQueue;
    @BindView(R.id.ll_setting_right)
    LinearLayout llSetRig;
    @BindView(R.id.ll_bill_record)
    LinearLayout llBillRecord;

    /**
     * 轮询Handler
     */
    private Handler mHandler;
    /**
     * 是否是轮询
     */
    private boolean mPolling;

    private AtomicBoolean needNextRequest = new AtomicBoolean(false);
    private boolean isVisible = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            needNextRequest.set(true);
            requestChangedOrderData();
        }


    };

    private void requestChangedOrderData() {
        if (isVisible == true && isFinishing() == false && needNextRequest.getAndSet(false)) {
            mPresenter.requestMainGUIRefreshInfo();
        }
    }

    private void registBroadcast() {
        IntentFilter intentFilter = new IntentFilter(NoticeBroadcastAction.ORDER_CHANGED);
        registerReceiver(receiver, intentFilter);
    }


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected HomeContract.Presenter initPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        registBroadcast();
        StatusBarUtil.setStatusBarColorM(this, R.color.common_text_color_f8f8f8);
        mTvCountOfDineCheckoutUnfinished.setVisibility(View.GONE);
        mTvFastFoodRegisterCashier.setVisibility(View.GONE);
        mTvTakeOutCashier.setVisibility(View.GONE);
        mTvQueue.setVisibility(View.GONE);
        mTitle.setViewVisibility(false);
        mTitle.setBackGroundColor(R.color.common_text_color_f8f8f8);
        initHandler();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //获取是否是何师版
        mPresenter.getHesVersion();
        // 请求营业日
        mPresenter.getAccountRecord();
        // 请求本地门店数据
        mPresenter.getLocalStore();
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
                    case MSG_WHAT_HOME:
                        mPresenter.requestMainGUIRefreshInfo();
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        //请求未结账数据
        mPolling = false;
        isVisible = true;
        mHandler.sendEmptyMessage(MSG_WHAT_HOME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Activity不可见时 取消订阅 停止轮询
        mPolling = false;
        mPresenter.disposeMainGUIRefreshInfo();
        mHandler.removeMessages(MSG_WHAT_HOME);
    }

    @Override
    public void showLoading() {
        if (!mPolling) {
            super.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
        mPresenter.requestMainGUIRefreshInfo();
    }

    @Override
    public void onAccountRecordGetSuccess(AccountRecord accountRecord) {
        // 往数据库中写accountRecordE
        mPresenter.saveAccountRecord(accountRecord);
    }

    @Override
    public void onAccountRecordGetFailed() {
        DialogFactory.showForceLogoutDialogResultDataClear(this, "未检测到营业日，请退出软件！");
    }

    @Override
    public void onAccountRecordSaveSuccess(AccountRecord accountRecord) {
        // 设置title
        mTitle.setTitleText("营业日：" + accountRecord.getBusinessDay());
        mTitle.setTitleTextColor(R.color.common_text_color_686868);
        mTitle.setTitleTextSize(16);
        // 开启营业日服务
        BaseApplication.getApplication().startMqttService();
    }

    @Override
    public void onLocalStoreGetSucceed(Store store) {
        // 设置门店名
        mTitle.setMenuText(store.getName());
        mTitle.setMenuTextColor(R.color.common_text_color_686868);
    }


    @Override
    public void refreshMainGui(List<HomeItemBean> itemList) {
        //开启轮询
        mPolling = true;
        requestChangedOrderData();
        for (int i = 0; i < itemList.size(); i++) {
            switch (itemList.get(i).getItemGUID()) {
                case HomePresenter.ITEM_TABLE_GUID:
                    setViewVisiblity(mTvCountOfDineCheckoutUnfinished, true, false, itemList.get(i).getOrderCount());
                    break;
                case HomePresenter.ITEM_SNACK_DISHES_GUID:
                    setViewVisiblity(mTvFastFoodRegisterCashier, true, false, itemList.get(i).getOrderCount());
                    break;
                case HomePresenter.ITEM_TAKE_OUT_GUID:
                    setViewVisiblity(mTvTakeOutCashier, false, false, itemList.get(i).getOrderCount());
                case HomePresenter.ITEM_QUEUE_GUID:
                    setViewVisiblity(mTvQueue, false, true, itemList.get(i).getOrderCount());
                    break;
            }
        }
    }

    private void setViewVisiblity(TextView textView, boolean isBill, boolean isQueue, int count) {
        if (count == 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(new SpanUtils()
                    .append("  ")
                    .append(count + "")
                    .append(isBill ? "单未结  " : (isQueue ? "桌等待  " : "新订单  "))
                    .create());
        }
    }

    @Override
    public void onMainGUIRefreshInfoDisposed() {

    }

    @Override
    public void onGetHesVersion(Boolean hesVersion) {
        if (hesVersion) {
            llSetRig.setVisibility(View.VISIBLE);
            llBillRecord.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDispose() {

    }

    @OnClick({R.id.ll_dine_register_cashier, R.id.ll_fast_food_register_cashier, R.id.ll_take_out_cashier,
            R.id.ll_queue, R.id.ll_bill_record, R.id.ll_setting})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_dine_register_cashier:
                intent = TableActivity.newIntent(HomeActivity.this);
                break;
            case R.id.ll_fast_food_register_cashier:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_OPEN_TABLE,
                        () -> startActivity(SnackDishesActivity.newIntent(HomeActivity.this)));
                break;
            case R.id.ll_bill_record:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_YINYE_DATA,
                        () -> {
                            Intent intent1 = BillRecordActivity.newIntent(HomeActivity.this);
                            Bundle bundle = new Bundle();
                            bundle.putInt("showProgress", 1);
                            intent1.putExtras(bundle);
                            startActivity(intent1);
                        });
                break;
            case R.id.ll_queue:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_QUEUE, () -> {
                    startActivity(QueueActivity.newIntent(this));
                });
                break;
            case R.id.ll_take_out_cashier:
                //外卖订单
                intent = TakeOutActivity.newIntent(HomeActivity.this);
                break;
            case R.id.ll_setting:
                intent = SettingActivity.newIntent(HomeActivity.this);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                showMessage("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                // 退出程序(数据清理)
                AppManager.getInstance().AppExitWithServiceShutDownAndDataClear(HomeActivity.this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
