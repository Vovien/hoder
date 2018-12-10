package com.holderzone.intelligencepos.mvp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.dialog.impl.IntervalDialogFragment;
import com.holderzone.intelligencepos.mvp.activity.CaptureActivity;
import com.holderzone.intelligencepos.mvp.activity.PaymentCheckoutActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentWxPayContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentWxPayPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 微信支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentWxPayFragment extends BaseFragment<PaymentWxPayContract.Presenter> implements PaymentWxPayContract.View {

    private static final String EXTRA_SALES_ORDER_GUID = "EXTRA_SALES_ORDER_GUID";
    private static final String EXTRA_UNPAID_TOTAL = "EXTRA_UNPAID_TOTAL";

    private static final int REQUEST_CODE_CHECK_OUT = 0x00000013;

    public static final String PaymentItemCode_WX01 = "WX01";// 微信

    @BindView(R.id.tv_unpaid_total)
    TextView mTvUnpaidTotalMoney;
    @BindView(R.id.btn_confirm_pay)
    Button mBtnConfirmPay;

    /**
     * 订单GUID
     */
    private String mSalesOrderGUID;

    /**
     * 待收金额
     */
    private double mUnpaidTotal;

    /**
     * 订单实体
     */
    private SalesOrderE mSalesOrderE;

    /**
     * 回调
     */
    private OnPaymentListener mOnPaymentListener;

    /**
     * 回调接口定义
     */
    public interface OnPaymentListener {
        /**
         * 发生了支付操作，但未进行结账操作
         */
        void onPaymentHappenedAndCheckoutUnfinishedInnerWxPay(SalesOrderE salesOrderE);

        /**
         * 结账成功
         */
        void onCheckoutFinishedInnerWxPay();
    }

    public static PaymentWxPayFragment newInstance(String salesOrderGUID, double unpaidTotal) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        args.putDouble(EXTRA_UNPAID_TOTAL, unpaidTotal);
        PaymentWxPayFragment fragment = new PaymentWxPayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaymentListener) {
            mOnPaymentListener = (OnPaymentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnPaymentListener != null) {
            mOnPaymentListener = null;
        }
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGUID = extras.getString(EXTRA_SALES_ORDER_GUID);
        mUnpaidTotal = extras.getDouble(EXTRA_UNPAID_TOTAL);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_payment_wxpay;
    }

    @Override
    protected PaymentWxPayContract.Presenter initPresenter() {
        return new PaymentWxPayPresenter(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 显示收款金额
        mTvUnpaidTotalMoney.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mUnpaidTotal)));
        // 扫码收款按钮 防抖、监听
        if (mUnpaidTotal > 0) {
            mBtnConfirmPay.setEnabled(true);
            RxView.clicks(mBtnConfirmPay).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
                Intent scanIntent = new IntentIntegrator(mBaseActivity)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setPrompt("")//写那句提示的话
                        .setOrientationLocked(true)//扫描方向固定
                        .setCaptureActivity(CaptureActivity.class) // 设置自定义的activity是CustomActivity
                        .addExtra(CaptureActivity.EXTRA_TITLE, "微信扫码收款")
                        .addExtra(CaptureActivity.EXTRA_MESSAGE, "请扫描顾客的支付二维码")
                        .createScanIntent();// 初始化扫描
                startActivityForResult(scanIntent, IntentIntegrator.REQUEST_CODE);
            });
        } else {
            mBtnConfirmPay.setEnabled(false);
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
//                showMessage("内容为空");
            } else {
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
//                showMessage(ScanResult);
                mPresenter.submitWxPay(mSalesOrderGUID, PaymentItemCode_WX01, mUnpaidTotal, ScanResult);
            }
        } else {
            if (requestCode == REQUEST_CODE_CHECK_OUT) {
                if (resultCode == Activity.RESULT_OK) {
                    if (mOnPaymentListener != null) {
                        mOnPaymentListener.onCheckoutFinishedInnerWxPay();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    if (data != null) {
                        boolean needCorrect = data.getExtras().getBoolean(PaymentCheckoutActivity.RESULT_CODE_NEED_CORRECT);
                        if (needCorrect) {
                            if (mOnPaymentListener != null) {
                                mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerWxPay(mSalesOrderE);
                            }
                        } else {// 即时失败，也要刷新，既然能进CheckOut，说明有付款过程
                            if (mOnPaymentListener != null) {
                                mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerWxPay(mSalesOrderE);
                            }
                        }
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**************************view callback begin**************************/

    @Override
    public void onWxPaySucceed(SalesOrderE salesOrderE) {
        mSalesOrderE = salesOrderE;
        Double unpaidTotal = mSalesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            mUnpaidTotal = unpaidTotal.doubleValue();
            if (mUnpaidTotal < 0.01) {
                // 跳转到CheckOutActivity
                startActivityForResult(PaymentCheckoutActivity.newIntent(mBaseActivity, mSalesOrderGUID, mSalesOrderE.getDiningTableE().getName(), salesOrderE.getCheckTotal(), salesOrderE.getSerialNumber()), REQUEST_CODE_CHECK_OUT);
            } else {
                // 通知PaymentActivity刷新已收金额
                if (mOnPaymentListener != null) {
                    mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerWxPay(mSalesOrderE);
                }
            }
        } else {
            showMessage("数据非法，UnpaidTotal不得为null！");
        }
    }

    @Override
    public void onWxPayFailed(String msg) {
        showMessage(msg);
    }

    @Override
    public void showIntervalDialog() {
        mDialogFactory.showIntervalDialog("未完成付款，请勿关闭窗口...", "关闭并重新操作", new IntervalDialogFragment.IntervalDialogListener() {
            @Override
            public void onClick() {
                mPresenter.disposeWxPay();
            }
        });
    }

    @Override
    public void hideIntervalDialog() {
        mDialogFactory.dismissIntervalDialog();
    }

    @Override
    public void onDispose() {

    }

    /**************************view callback end**************************/

    /*********************public  method begin*************************/
    /*********************public method end*************************/
}
