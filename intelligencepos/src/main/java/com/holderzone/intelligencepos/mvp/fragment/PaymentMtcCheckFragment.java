package com.holderzone.intelligencepos.mvp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.mvp.activity.CaptureActivity;
import com.holderzone.intelligencepos.mvp.activity.PaymentMtcPayActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentMtcCheckContract;
import com.holderzone.intelligencepos.mvp.model.bean.MeituanCoupon;
import com.holderzone.intelligencepos.mvp.presenter.PaymentMtcCheckPresenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 现金支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentMtcCheckFragment extends BaseFragment<PaymentMtcCheckContract.Presenter> implements PaymentMtcCheckContract.View {

    private static final String KEY_SALES_ORDER_GUID = "SALES_ORDER_GUID";

    private static final int REQUEST_CODE_MTC_PAY = 0;

    @BindView(R.id.et_coupon_code)
    EditText mEtCouponCode;
    @BindView(R.id.iv_clear_coupon_code)
    ImageView mIvClearCouponCode;
    @BindView(R.id.btn_check_coupon_code)
    Button mBtnCheckCouponCode;
    @BindView(R.id.btn_check_scan)
    Button mBtnCheckScan;

    private String mSalesOrderGUID;

    /**
     * 回调
     */
    private OnPaymentListener mOnPaymentListener;

    /**
     * 回调接口定义
     */
    public interface OnPaymentListener {

        void onPaymentHappenedAndCheckoutUnfinishedInnerMtc();

        void onCheckoutFinishedInnerMtc();
    }

    public static PaymentMtcCheckFragment newInstance(String salesOrderGUID) {
        Bundle args = new Bundle();
        args.putString(KEY_SALES_ORDER_GUID, salesOrderGUID);
        PaymentMtcCheckFragment fragment = new PaymentMtcCheckFragment();
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
        mSalesOrderGUID = extras.getString(KEY_SALES_ORDER_GUID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_payment_mtc;
    }

    @Override
    protected PaymentMtcCheckContract.Presenter initPresenter() {
        return new PaymentMtcCheckPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mEtCouponCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    mIvClearCouponCode.setVisibility(View.VISIBLE);
                    mBtnCheckCouponCode.setEnabled(true);
                } else {
                    mIvClearCouponCode.setVisibility(View.GONE);
                    mBtnCheckCouponCode.setEnabled(false);
                }
            }
        });
        mEtCouponCode.setInputType(InputType.TYPE_NULL);
        mIvClearCouponCode.setVisibility(View.GONE);
        mBtnCheckCouponCode.setEnabled(false);
        RxView.clicks(mBtnCheckCouponCode).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(o ->
                mPresenter.MeituanCheck(mEtCouponCode.getText().toString()));
        //美团验券
        RxView.clicks(mBtnCheckScan).throttleFirst(1000, TimeUnit.MICROSECONDS).subscribe(o -> {
            Intent scanIntent = new IntentIntegrator(mBaseActivity)
                    .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                    .setPrompt("")
                    .setOrientationLocked(true)
                    .setCaptureActivity(CaptureActivity.class)
                    .addExtra(CaptureActivity.EXTRA_TITLE, "扫码验券")
                    .addExtra(CaptureActivity.EXTRA_MESSAGE, "请扫描顾客的团购二维码")
                    .createScanIntent();
            startActivityForResult(scanIntent, IntentIntegrator.REQUEST_CODE);

        });

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

            } else {
                String scanResult = intentResult.getContents();
                mEtCouponCode.setText(scanResult);
            }
        } else {
            if (REQUEST_CODE_MTC_PAY == requestCode) {
                if (Activity.RESULT_OK == resultCode) {
                    if (mOnPaymentListener != null) {
                        mOnPaymentListener.onCheckoutFinishedInnerMtc();
                    }
                } else if (Activity.RESULT_CANCELED == resultCode) {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            boolean payHappened = extras.getBoolean(PaymentMtcPayActivity.RESULT_CODE_PAYMENT_HAPPENED_INNER_PAY);
                            if (payHappened && mOnPaymentListener != null) {
                                mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerMtc();
                            }
                        }
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**********************view callback begin**********************/


    @Override
    public void MeituanCheckSuccess(MeituanCoupon meituanCoupon) {
        mEtCouponCode.getText().clear();
        Intent intent = PaymentMtcPayActivity.newIntent(mBaseActivity, mSalesOrderGUID, meituanCoupon);
        startActivityForResult(intent, REQUEST_CODE_MTC_PAY);
    }

    @Override
    public void MeituanCheckFailed() {
        mEtCouponCode.setText("");
        mEtCouponCode.requestFocus();
        mDialogFactory.showMsgDialog(mBaseActivity, "团购验证码无效。");
    }

    @Override
    public void onDispose() {

    }

    /**********************view callback end**********************/

    @OnClick({R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_clear, R.id.btn_number_0, R.id.iv_delete, R.id.iv_clear_coupon_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_number_1:
                keyBoardSetting("1");
                break;
            case R.id.btn_number_2:
                keyBoardSetting("2");
                break;
            case R.id.btn_number_3:
                keyBoardSetting("3");
                break;
            case R.id.btn_number_4:
                keyBoardSetting("4");
                break;
            case R.id.btn_number_5:
                keyBoardSetting("5");
                break;
            case R.id.btn_number_6:
                keyBoardSetting("6");
                break;
            case R.id.btn_number_7:
                keyBoardSetting("7");
                break;
            case R.id.btn_number_8:
                keyBoardSetting("8");
                break;
            case R.id.btn_number_9:
                keyBoardSetting("9");
                break;
            case R.id.btn_number_clear:
                keyBoardSetting("clear");
                break;
            case R.id.btn_number_0:
                keyBoardSetting("0");
                break;
            case R.id.iv_delete:
                keyBoardSetting("del");
                break;
            case R.id.iv_clear_coupon_code:
                mEtCouponCode.setText("");
                break;
            default:
                break;
        }
    }

    /*********************private method begin*************************/

    /**
     * 自定义键盘操作
     *
     * @param number
     */
    private void keyBoardSetting(CharSequence number) {
        Editable editable = mEtCouponCode.getText();
        if ("clear".equals(number)) {
            editable.clear();
        } else {
            if ("del".equals(number)) {
                int length = editable.length();
                if (length > 0) {
                    editable.delete(length - 1, length);
                }
            } else {
                editable.append(number);
            }
        }
    }

    /*********************private method end*************************/

    /*********************public  method begin*************************/
    /*********************public method end*************************/
}
