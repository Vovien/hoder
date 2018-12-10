package com.holderzone.intelligencepos.mvp.fragment;

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
import com.holderzone.intelligencepos.mvp.contract.PaymentJhPayContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentJhPayPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 支付宝支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentJhPayFragment extends BaseFragment<PaymentJhPayContract.Presenter> implements PaymentJhPayContract.View {

    private static final String EXTRA_SALES_ORDER_GUID = "EXTRA_SALES_ORDER_GUID";
    private static final String EXTRA_UNPAID_TOTAL = "EXTRA_UNPAID_TOTAL";

    private static final int REQUEST_CODE_CHECK_OUT = 0x00000015;

    public static final String PaymentItemCode_JHZF01 = "JHZF01";// 聚合支付

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
        void onPaymentHappenedAndCheckoutUnfinishedInnerJhPay(SalesOrderE salesOrderE);

        void onCheckoutFinishedInnerJhPay();
    }

    public static PaymentJhPayFragment newInstance(String salesOrderGUID, double unpaidTotal) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        args.putDouble(EXTRA_UNPAID_TOTAL, unpaidTotal);
        PaymentJhPayFragment fragment = new PaymentJhPayFragment();
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
        return R.layout.fragment_payment_jhzf;
    }

    @Override
    protected PaymentJhPayContract.Presenter initPresenter() {
        return new PaymentJhPayPresenter(this);
    }

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
                        .addExtra(CaptureActivity.EXTRA_TITLE, "扫码收款")
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
                mPresenter.submitJhPay(mSalesOrderGUID, PaymentItemCode_JHZF01, mUnpaidTotal, ScanResult);
            }
        } else {
            if (requestCode == REQUEST_CODE_CHECK_OUT) {
                if (resultCode == Activity.RESULT_OK) {
                    if (mOnPaymentListener != null) {
                        mOnPaymentListener.onCheckoutFinishedInnerJhPay();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    if (data != null) {
                        boolean aBoolean = data.getExtras().getBoolean(PaymentCheckoutActivity.RESULT_CODE_NEED_CORRECT);
                        if (aBoolean) {
                            if (mOnPaymentListener != null) {
                                mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerJhPay(mSalesOrderE);
                            }
                        } else {// 即时失败，也要刷新，既然能进CheckOut，说明有付款过程
                            if (mOnPaymentListener != null) {
                                mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerJhPay(mSalesOrderE);
                            }
                        }
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onDispose() {

    }

    /**************************view callback begin**************************/

    @Override
    public void onJhPaySucceed(SalesOrderE salesOrderE) {
        mSalesOrderE = salesOrderE;
        Double unpaidTotal = mSalesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            mUnpaidTotal = unpaidTotal;
            if (mUnpaidTotal < 0.01) {
                // 跳转到CheckOutActivity
                startActivityForResult(PaymentCheckoutActivity.newIntent(mBaseActivity, mSalesOrderGUID, mSalesOrderE.getDiningTableE().getName(), mSalesOrderE.getCheckTotal(), mSalesOrderE.getSerialNumber()), REQUEST_CODE_CHECK_OUT);
            } else {
                // 通知PaymentActivity刷新已收金额
                if (mOnPaymentListener != null) {
                    mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerJhPay(mSalesOrderE);
                }
            }
        } else {
            showMessage("数据非法，UnpaidTotal不得为null！");
        }
    }

    @Override
    public void onJhPayFailed(String msg) {
        showMessage(msg);
    }

    @Override
    public void showIntervalDialog() {
        mDialogFactory.showIntervalDialog("未完成付款，请勿关闭窗口...", "关闭并重新操作", new IntervalDialogFragment.IntervalDialogListener() {
            @Override
            public void onClick() {
                mPresenter.disposeAliPay();
            }
        });
    }

    @Override
    public void hideIntervalDialog() {
        mDialogFactory.dismissIntervalDialog();
    }

    /**************************view callback end**************************/

    /*********************public  method begin*************************/
    /*********************public method end*************************/
}
