package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentCheckoutContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentCheckoutPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 结账状态界面
 * Created by tcw on 2017/5/31.
 */

public class PaymentCheckoutActivity extends BaseActivity<PaymentCheckoutContract.Presenter> implements PaymentCheckoutContract.View {

    private static final String EXTRA_SALES_ORDER_GUID = "cn.holdzone.intelligencepos.SalesOrderGUID";
    private static final String EXTRA_DINING_TABLE_NAME = "cn.holdzone.intelligencepos.DiningTableName";
    private static final String EXTRA_SERIAL_NUMBER = "cn.holdzone.intelligencepos.SerialNumber";
    private static final String EXTRA_CHECK_TOTAL = "cn.holdzone.intelligencepos.CheckTotal";

    public static final String RESULT_CODE_NEED_CORRECT = "cn.holdzone.intelligencepos.NeedCorrect";
    /**
     * 会员积分变化导致结账失败
     */
    public static final int CALL_BACK_BY_CHANGE_MEMBERSHIP_INTEGRAL = 10010;

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.iv_check_out_state)
    ImageView mIvCheckOutState;
    @BindView(R.id.tv_check_out_state)
    TextView mTvCheckOutState;
    @BindView(R.id.tv_check_out_notice)
    TextView mTvCheckOutNotice;
    @BindView(R.id.tv_serial_number)
    TextView mTvSerialNumber;
    @BindView(R.id.tv_table_name)
    TextView mTvTableName;
    @BindView(R.id.tv_actually_pay_money)
    TextView mTvActuallyPayMoney;
    @BindView(R.id.tv_checkout_time)
    TextView mTvCheckoutTime;
    @BindView(R.id.btn_done)
    Button mBtnDone;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;
    @BindView(R.id.ll_serial_number)
    LinearLayout mLlSerialNumber;
    @BindView(R.id.ll_table_name)
    LinearLayout mLlTableName;
    @BindView(R.id.ll_actually_pay_money)
    LinearLayout mLlActuallyPayMoney;
    @BindView(R.id.ll_checkout_time)
    LinearLayout mLlCheckoutTime;

    /**
     * 订单唯一标识
     */
    private String mSalesOrderGUID;

    /**
     * 餐桌名
     */
    private String mDiningTableName;

    /**
     * 实收金额
     */
    private double mCheckTotal;

    /**
     * 订单流水号
     */
    private String mSerialNumber;

    /**
     * 是否结账成功
     */
    private boolean mCheckOutDone;

    /**
     * 需要校正
     */
    private boolean mNeedCorrect;

    public static Intent newIntent(Context context, String salesOrderGUID, String diningTableName, double checkTotalMoney, String serialNumber) {
        Intent intent = new Intent(context, PaymentCheckoutActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        extras.putString(EXTRA_DINING_TABLE_NAME, diningTableName);
        extras.putDouble(EXTRA_CHECK_TOTAL, checkTotalMoney);
        extras.putString(EXTRA_SERIAL_NUMBER, serialNumber);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGUID = extras.getString(EXTRA_SALES_ORDER_GUID);
        mDiningTableName = extras.getString(EXTRA_DINING_TABLE_NAME);
        mCheckTotal = extras.getDouble(EXTRA_CHECK_TOTAL);
        mSerialNumber = extras.getString(EXTRA_SERIAL_NUMBER);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_check_out;
    }

    @Override
    protected PaymentCheckoutContract.Presenter initPresenter() {
        return new PaymentCheckoutPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 返回按钮 防抖、坚挺
        mTitle.setOnReturnClickListener(this::setResultAndFinish);
        // 完成按钮 防抖、监听
        RxView.clicks(mBtnDone).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            setResultAndFinish();
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.submitCheckOut(mSalesOrderGUID);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        setResultAndFinish();
    }

    /********************
     * view callback begin
     ***********************/

    @Override
    public void onCheckOutSucceed(SalesOrderE salesOrderE) {
        // 切换到content布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        // 结账成功
        mCheckOutDone = true;

        mIvCheckOutState.setImageResource(R.drawable.check_out_succeed);
        mTvCheckOutState.setText("结账成功");
        mTvCheckOutNotice.setText("请将结账小票交于顾客");

        mLlSerialNumber.setVisibility(View.VISIBLE);
        mTvSerialNumber.setText(mSerialNumber);
        mLlTableName.setVisibility(View.GONE);
        mTvTableName.setText(mDiningTableName);
        mLlActuallyPayMoney.setVisibility(View.VISIBLE);
        mTvActuallyPayMoney.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCheckTotal)));
        mLlCheckoutTime.setVisibility(View.VISIBLE);
        mTvCheckoutTime.setText(salesOrderE != null ? salesOrderE.getChecOutkTime() : "未查询到");

        mBtnDone.setText("完成");
        mBtnDone.setEnabled(true);
    }

    @Override
    public void onCheckOutFailed() {
        // 结账失败
        mCheckOutDone = false;
        mNeedCorrect = false;
        onCheckOutFailView();
    }

    @Override
    public void onCheckOutFailedBecauseOfMoneyNotMatch() {
        // 结账失败
        mCheckOutDone = false;
        mNeedCorrect = true;
        onCheckOutFailView();
    }

    @Override
    public void onCheckOutFailedBecauseOfCheckoutAlready() {
        // 结账失败
        mCheckOutDone = true;
        onCheckOutFailView();
    }

    @Override
    public void onCheckOutFailedByIntegralChange() {
        // 结账失败
        mCheckOutDone = true;
        onCheckOutFailView();

//        mDialogFactory.showConfirmDialog("会员积分核算异常，请重新结算。", false, "取消", R.drawable.selector_button_red, true, "确定", R.drawable.selector_button_blue, new ConfirmDialogFragment.ConfirmDialogListener() {
//            @Override
//            public void onNegClick() {
//
//            }
//
//            @Override
//            public void onPosClick() {
//                AppManager.getInstance().finishUntil(BalanceAccountsActivity.class);
//            }
//        });
    }

    /***
     * 结账失败时 显示的View
     */
    private void onCheckOutFailView() {
        // 切换到content布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);

        mIvCheckOutState.setImageResource(R.drawable.check_out_failed);
        mTvCheckOutState.setText("操作异常");
        mTvCheckOutNotice.setText("请返回并重新尝试");

        mLlSerialNumber.setVisibility(View.GONE);
        mLlTableName.setVisibility(View.GONE);
        mLlActuallyPayMoney.setVisibility(View.GONE);
        mLlCheckoutTime.setVisibility(View.GONE);

        mBtnDone.setText("返回");
        mBtnDone.setEnabled(true);
    }

    @Override
    public void onNetworkError() {
        // 切换到error布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onDispose() {

    }

    /********************view callback end***********************/

    private void setResultAndFinish() {
        if (mCheckOutDone) {
            setResult(Activity.RESULT_OK);
        } else {
            Intent intent = new Intent();
            Bundle extras = new Bundle();
            extras.putBoolean(RESULT_CODE_NEED_CORRECT, mNeedCorrect);
            intent.putExtras(extras);
            setResult(Activity.RESULT_CANCELED, intent);
        }
        finishActivity();
    }
}
