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
import android.widget.RelativeLayout;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.mvp.activity.PaymentCheckoutActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentBankCardContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentBankCardPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.holderzone.intelligencepos.R.id.iv_clear_serial_number;

/**
 * 银行卡支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentBankCardFragment extends BaseFragment<PaymentBankCardContract.Presenter> implements PaymentBankCardContract.View {

    private static final String EXTRA_SALES_ORDER_GUID = "EXTRA_SALES_ORDER_GUID";
    private static final String EXTRA_PAYABLE_AMOUNT = "EXTRA_PAYABLE_AMOUNT";
    private static final String EXTRA_PAYMENT_ITEM_CODE = "EXTRA_PAYMENT_ITEM_CODE";

    private static final int REQUEST_CODE_CHECK_OUT = 0x00000011;

    public static final String PaymentItemCode_BC01 = "BC01";// 银行卡

    @BindView(R.id.et_actually_pay_money)
    EditText mEtActuallyPayMoney;
    @BindView(R.id.iv_clear_actually_pay_money)
    ImageView mIvClearActuallyPayMoney;
    @BindView(R.id.rl_actually_pay_money)
    RelativeLayout mRlActuallyPayMoney;
    @BindView(R.id.et_serial_number)
    EditText mEtSerialNumber;
    @BindView(iv_clear_serial_number)
    ImageView mIvClearSerialNumber;
    @BindView(R.id.rl_serial_number)
    RelativeLayout mRlSerialNumber;
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
     * 支付方式
     */
    private String mPaymentItemCode;

    /**
     * 清空"金额"
     */
    private boolean mClearActuallyPayMoney;

    /**
     * 清空"流水号"
     */
    private boolean mClearSerialNumber;

    /**
     * 订单实体
     */
    private SalesOrderE mSalesOrderE;

    /**
     * 回调
     */
    private OnPaymentListener mOnPaymentListener;
    /**
     * 是否有输入大于零的金额
     */
    private boolean isInputPayMoney = false;
    /**
     * 是否有输入流水号
     */
    private boolean isInputSerialNumber = false;
    /**
     * 是否是何师版本
     */
    private boolean mIsHes = false;

    /**
     * 回调接口定义
     */
    public interface OnPaymentListener {
        void onPaymentHappenedAndCheckoutUnfinishedInnerBankCard(SalesOrderE salesOrderE);

        void onCheckoutFinishedInnerBankCard();
    }


    public static PaymentBankCardFragment newInstance(String salesOrderGUID, double payableAmount) {
        return newInstance(salesOrderGUID, payableAmount, null);
    }


    public static PaymentBankCardFragment newInstance(String salesOrderGUID, double payableAmount, String paymentItemCode) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        args.putDouble(EXTRA_PAYABLE_AMOUNT, payableAmount);
        args.putString(EXTRA_PAYMENT_ITEM_CODE, paymentItemCode);
        PaymentBankCardFragment fragment = new PaymentBankCardFragment();
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
        mUnpaidTotal = extras.getDouble(EXTRA_PAYABLE_AMOUNT);
        mPaymentItemCode = extras.getString(EXTRA_PAYMENT_ITEM_CODE);
        if (mPaymentItemCode == null) {
            mPaymentItemCode = PaymentItemCode_BC01;
        }
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_payment_bank_card;
    }

    @Override
    protected PaymentBankCardContract.Presenter initPresenter() {
        return new PaymentBankCardPresenter(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mPresenter.requestIsHes();
        // 金额
        mEtActuallyPayMoney.setOnFocusChangeListener((v, hasFocus) -> {
            if (mRlActuallyPayMoney != null) {
                if (hasFocus) {
                    mRlActuallyPayMoney.setBackgroundResource(R.drawable.shape_edit_corner_bg_focused);
                } else {
                    mRlActuallyPayMoney.setBackgroundResource(R.drawable.shape_edit_corner_bg);
                }
            }
        });
        mEtActuallyPayMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
                        isInputPayMoney = true;
                    } else {
                        isInputPayMoney = false;
                    }
                } else {
                    isInputPayMoney = false;
                }
                setBtnConfirmEnable();
                if (length > 0) {
                    mIvClearActuallyPayMoney.setVisibility(View.VISIBLE);
                } else {
                    mIvClearActuallyPayMoney.setVisibility(View.GONE);
                }
            }
        });
        mEtActuallyPayMoney.setInputType(InputType.TYPE_NULL);
        mEtActuallyPayMoney.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mUnpaidTotal)));
        mEtActuallyPayMoney.clearFocus();
        // 流水号
        mEtSerialNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mRlSerialNumber != null) {
                    if (hasFocus) {
                        mRlSerialNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg_focused);
                    } else {
                        mRlSerialNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg);
                    }
                }
            }
        });
        mEtSerialNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if (!RegexExUtils.isSerialNumber(editable)) {
                    editable.delete(length - 1, length);
                    return;
                } else {
                    mEtSerialNumber.setSelection(length);
                }
                if (length > 0) {
                    isInputSerialNumber = true;
                    mIvClearSerialNumber.setVisibility(View.VISIBLE);
                } else {
                    isInputSerialNumber = false;
                    mIvClearSerialNumber.setVisibility(View.GONE);
                }
                setBtnConfirmEnable();
            }
        });
        mEtSerialNumber.setInputType(InputType.TYPE_NULL);
        mEtSerialNumber.requestFocus();
        // 收款按钮 防抖、监听
        RxView.clicks(mBtnConfirm).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            String amountStr = mEtActuallyPayMoney.getText().toString();
            String serialNumStr = mEtSerialNumber.getText().toString();
            mPresenter.submitBankCardPay(mSalesOrderGUID, mPaymentItemCode, mUnpaidTotal, Double.valueOf(amountStr.substring(1, amountStr.length())), serialNumStr);
        });
    }

    /**
     * 修改确定按钮的状态
     */
    private void setBtnConfirmEnable() {
        if (mIsHes) {
            if (isInputPayMoney && isInputSerialNumber) {
                mBtnConfirm.setEnabled(true);
            } else {
                mBtnConfirm.setEnabled(false);
            }
        } else {
            if (isInputPayMoney) {
                mBtnConfirm.setEnabled(true);
            } else {
                mBtnConfirm.setEnabled(false);
            }
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
        if (REQUEST_CODE_CHECK_OUT == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                if (mOnPaymentListener != null) {
                    mOnPaymentListener.onCheckoutFinishedInnerBankCard();
                }
            } else if (Activity.RESULT_CANCELED == resultCode) {
                if (data != null) {
                    boolean aBoolean = data.getExtras().getBoolean(PaymentCheckoutActivity.RESULT_CODE_NEED_CORRECT);
                    if (aBoolean) {
                        if (mOnPaymentListener != null) {
                            mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerBankCard(mSalesOrderE);
                        }
                    } else {// 即时失败，也要刷新，既然能进CheckOut，说明有付款过程
                        if (mOnPaymentListener != null) {
                            mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerBankCard(mSalesOrderE);
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
    public void onBankCardPaySucceed(SalesOrderE salesOrderE) {
        mSalesOrderE = salesOrderE;
        Double unpaidTotal = mSalesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            mUnpaidTotal = unpaidTotal.doubleValue();
            if (mUnpaidTotal < 0.01) {
                // 跳转到CheckOutActivity
                startActivityForResult(PaymentCheckoutActivity.newIntent(mBaseActivity, mSalesOrderGUID, mSalesOrderE.getDiningTableE().getName(), salesOrderE.getCheckTotal(), salesOrderE.getSerialNumber()), REQUEST_CODE_CHECK_OUT);
            } else {
                // 重置输入框
                mEtActuallyPayMoney.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mUnpaidTotal)));
                mEtSerialNumber.setText("");
                mEtSerialNumber.requestFocus();
                // 通知PaymentActivity刷新已收金额
                if (mOnPaymentListener != null) {
                    mOnPaymentListener.onPaymentHappenedAndCheckoutUnfinishedInnerBankCard(mSalesOrderE);
                }
            }
        } else {
            showMessage("数据非法，UnpaidTotal不得为null！");
        }
    }

    @Override
    public void onBankCardPayFailed() {
//        showMessage("支付失败");
    }

    @Override
    public void getIsHes(boolean isHes) {
        mIsHes = isHes;
        setBtnConfirmEnable();
    }

    @Override
    public void onDispose() {

    }

    /**********************view callback end**********************/

    @OnClick({R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete, R.id.iv_clear_actually_pay_money, R.id.iv_clear_serial_number})
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
            case R.id.iv_clear_actually_pay_money:
                mClearActuallyPayMoney = true;
                keyBoardSetting("clear");
                mEtActuallyPayMoney.requestFocus();
                break;
            case R.id.iv_clear_serial_number:
                mClearSerialNumber = true;
                keyBoardSetting("clear");
                mEtSerialNumber.requestFocus();
                break;
            default:
                break;
        }
    }

    /**
     * 自定义键盘操作
     *
     * @param number
     */
    private void keyBoardSetting(CharSequence number) {
        if ("clear".equals(number)) {
            if (mClearActuallyPayMoney) {
                mClearActuallyPayMoney = false;
                mEtActuallyPayMoney.getText().clear();
            } else if (mClearSerialNumber) {
                mClearSerialNumber = false;
                mEtSerialNumber.getText().clear();
            }
        } else {
            if (mEtActuallyPayMoney.isFocused()) {// 刷卡金额 选中
                Editable editable = mEtActuallyPayMoney.getText();
                if ("del".equals(number)) {
                    int length = editable.length();
                    if (length > 0) {
                        editable.delete(length - 1, length);
                    }
                } else {
                    editable.append(number);
                }
            }
            if (mEtSerialNumber.isFocused()) {// 流水号 选中
                Editable editable = mEtSerialNumber.getText();
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
    }

    /*********************public  method begin*************************/
    /*********************public method end*************************/
}
