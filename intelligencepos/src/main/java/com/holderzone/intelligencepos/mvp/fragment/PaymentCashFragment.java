package com.holderzone.intelligencepos.mvp.fragment;

import android.annotation.SuppressLint;
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

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.mvp.activity.PaymentCheckoutActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentCashContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentCashPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 现金支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentCashFragment extends BaseFragment<PaymentCashContract.Presenter> implements PaymentCashContract.View {

    private static final String EXTRA_SALES_ORDER_GUID = "cn.holdzone.intelligencepos.SalesOrderGUID";
    private static final String EXTRA_UNPAID_TOTAL = "cn.holdzone.intelligencepos.Unpaid.Total";

    private static final int REQUEST_CODE_CHECK_OUT = 0x00000010;

    public static final String PaymentItemCode_CH01 = "CH01";// 人民币

    @BindView(R.id.et_actually_pay_money)
    EditText mEtActuallyPayMoney;
    @BindView(R.id.iv_clear)
    ImageView mIvClear;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

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
        void onPaymentHappenedAndCheckoutUnfinishedInnerCash(SalesOrderE salesOrderE);

        void onCheckoutFinishedInnerCash();
    }

    public static PaymentCashFragment newInstance(String salesOrderGUID, double unpaidTotal) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        args.putDouble(EXTRA_UNPAID_TOTAL, unpaidTotal);
        PaymentCashFragment fragment = new PaymentCashFragment();
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
        return R.layout.fragment_payment_cash;
    }

    @Override
    protected PaymentCashContract.Presenter initPresenter() {
        return new PaymentCashPresenter(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 输入框添加监听、设置输入type、请求焦点、设置初始值
        mEtActuallyPayMoney.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if ("￥".equals(editable.toString())) {
                    editable.clear();
                    return;
                }
                if (length > 0 && !"￥".equals(String.valueOf(editable.charAt(0)))) {
                    editable.insert(0, "￥");
                    return;
                }
                CharSequence money = length > 0 ? editable.subSequence(1, length) : "";
                if (!RegexExUtils.isMoneyInputLegal(money)) {
                    editable.delete(length - 1, length);
                    return;
                } else {
                    mEtActuallyPayMoney.setSelection(length);
                }
                if (RegexExUtils.isMoney(money) && mUnpaidTotal > 0) {
                    Double cur = Double.valueOf(money.toString());
                    if (cur > 0) {
                        mBtnConfirm.setEnabled(true);
                    } else {
                        mBtnConfirm.setEnabled(false);
                    }
                } else {
                    mBtnConfirm.setEnabled(false);
                }
                if (length > 0) {
                    mIvClear.setVisibility(View.VISIBLE);
                } else {
                    mIvClear.setVisibility(View.GONE);
                }
            }
        });
        mEtActuallyPayMoney.setInputType(InputType.TYPE_NULL);
        mEtActuallyPayMoney.requestFocus();
        mEtActuallyPayMoney.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mUnpaidTotal)));
        // 收款按钮 防抖、监听、初始化
        RxView.clicks(mBtnConfirm).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            String amountStr = mEtActuallyPayMoney.getText().toString();
            double receivedMoney = Double.valueOf(amountStr.substring(1, amountStr.length()));
            double subMoney = ArithUtil.sub(receivedMoney, mUnpaidTotal);
            double repayMoney = subMoney > 0 ? subMoney : 0.00;
            double actuallyMoney = subMoney > 0 ? mUnpaidTotal : receivedMoney;
            mPresenter.submitCashPay(mSalesOrderGUID, PaymentItemCode_CH01, mUnpaidTotal, receivedMoney, repayMoney, actuallyMoney);
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
        if (REQUEST_CODE_CHECK_OUT == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                if (mOnPaymentListener != null) {
                    mOnPaymentListener.onCheckoutFinishedInnerCash();
                }
            } else if (Activity.RESULT_CANCELED == resultCode) {
                if (data != null) {
                    boolean aBoolean = data.getExtras().getBoolean(PaymentCheckoutActivity.RESULT_CODE_NEED_CORRECT);
                    if (aBoolean) {
                        if (mOnPaymentListener != null) {
                            mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerCash(mSalesOrderE);
                        }
                    } else {// 即时失败，也要刷新，既然能进CheckOut，说明有付款过程
                        if (mOnPaymentListener != null) {
                            mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerCash(mSalesOrderE);
                        }
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**********************view callback begin**********************/

    @Override
    public void onCashPaySucceed(SalesOrderE salesOrderE) {
        mSalesOrderE = salesOrderE;
        Double unpaidTotal = mSalesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            mUnpaidTotal = unpaidTotal.doubleValue();
            if (mUnpaidTotal < 0.01) {
                // 跳转到CheckOutActivity
                startActivityForResult(PaymentCheckoutActivity.newIntent(mBaseActivity, mSalesOrderGUID, mSalesOrderE.getDiningTableE().getName(), mSalesOrderE.getCheckTotal(), mSalesOrderE.getSerialNumber()), REQUEST_CODE_CHECK_OUT);
            } else {
                // 通知PaymentActivity刷新已收金额
                if (mOnPaymentListener != null) {
                    mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerCash(mSalesOrderE);
                }
            }
        } else {
            showMessage("数据非法，UnpaidTotal不得为null！");
        }
    }

    @Override
    public void onCashPayFailed() {
//        showMessage("支付失败");
    }

    @Override
    public void onDispose() {

    }

    /**********************view callback end**********************/

    @OnClick({R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete, R.id.iv_clear})
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
            case R.id.btn_number_dot:
                keyBoardSetting(".");
                break;
            case R.id.btn_number_0:
                keyBoardSetting("0");
                break;
            case R.id.iv_delete:
                keyBoardSetting("del");
                break;
            case R.id.iv_clear:
                keyBoardSetting("clear");
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
        Editable editable = mEtActuallyPayMoney.getText();
        if ("clear".equals(number)) {
            editable.clear();
            mEtActuallyPayMoney.requestFocus();
        } else {
            if ("del".equals(number)) {
                int length = editable.length();
                if (length == 0) {
                    mEtActuallyPayMoney.requestFocus();
                } else {
                    editable.delete(length - 1, length);
                    mEtActuallyPayMoney.requestFocus();
                }
            } else {
                editable.append(number);
                mEtActuallyPayMoney.requestFocus();
            }
        }
    }

    /*********************private method end*************************/

    /*********************public  method begin*************************/
    /*********************public method end*************************/
}
