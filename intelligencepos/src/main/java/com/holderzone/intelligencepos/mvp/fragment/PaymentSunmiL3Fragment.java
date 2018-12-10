package com.holderzone.intelligencepos.mvp.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.allinpay.usdk.core.data.BaseData;
import com.allinpay.usdk.core.data.RequestData;
import com.allinpay.usdk.core.data.ResponseData;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.activity.PaymentCheckoutActivity;
import com.holderzone.intelligencepos.mvp.contract.PaymentSunmiL3Contract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentSunmiL3Presenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.IntentExistUtils;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by LiTao on 2017-10-30.
 * 通联支付
 */

public class PaymentSunmiL3Fragment extends BaseFragment<PaymentSunmiL3Contract.Presenter> implements PaymentSunmiL3Contract.View {
    private static final String EXTRA_SALES_ORDER_GUID = "cn.holdzone.intelligencepos.SalesOrderGUID";
    private static final String EXTRA_UNPAID_TOTAL = "cn.holdzone.intelligencepos.Unpaid.Total";
    private static final int REQUEST_CODE_CHECK_OUT = 0x00000010;
    private static final int REQUEST_CODE_ZSY_PAY = 0x00000011;
    public static final String PaymentItemCode_SYB01 = "SYB01";// 通联支付
    public static final int PAYMENT_ERROR_MESSAGE = 1;
    private static final String USDK_PAYMENT_CODE = "100100001";//通联支付消费
    private static final String PAYMENT_CANCEL_CODE = "FFFE";//通联支付取消
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
    private OnL3PayListener mL3PayListener;
    /**
     * 原始流水号 消费撤销时用
     */
    private String mOrigTraceNo;
    /**
     * 交易参考号
     */
    private String mRefNumber;
    /**
     * 智收银 唯一ID  用于获取本笔交易数据
     */
    private String mTransID;
    /**
     * 通联支付异常 处理
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAYMENT_ERROR_MESSAGE:
                    getDialogFactory().showPaymentAllinDialog((refNumber, traceNumber) -> {
                        mRefNumber = refNumber;
                        mOrigTraceNo = traceNumber;
                        submitData();
                    });
                    break;
                default:
                    break;
            }
        }
    };

    public static PaymentSunmiL3Fragment newInstence(String salesOrderGUID, double unpaidTotal) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        args.putDouble(EXTRA_UNPAID_TOTAL, unpaidTotal);
        PaymentSunmiL3Fragment fragment = new PaymentSunmiL3Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnL3PayListener) {
            mL3PayListener = (OnL3PayListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mL3PayListener != null) {
            mL3PayListener = null;
        }
    }

    @Override
    public void onDispose() {

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
    protected PaymentSunmiL3Contract.Presenter initPresenter() {
        return new PaymentSunmiL3Presenter(this);
    }


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
                    BigDecimal amountActually = new BigDecimal(cur);
                    BigDecimal amountUnpaidTotal = new BigDecimal(mUnpaidTotal);
                    if (cur > 0) {
                        mBtnConfirm.setEnabled(true);
                    } else {
                        mBtnConfirm.setEnabled(false);
                    }
                    if (amountActually.compareTo(amountUnpaidTotal) == 1) {
                        editable.delete(length - 1, length);
                        return;
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
            //L3支付
            l3Payment();
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onSunmiL3Succeed(SalesOrderE salesOrderE) {
        mSalesOrderE = salesOrderE;
        Double unpaidTotal = mSalesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            mUnpaidTotal = unpaidTotal.doubleValue();
            if (mUnpaidTotal < 0.01) {
                // 跳转到CheckOutActivity
                startActivityForResult(PaymentCheckoutActivity.newIntent(mBaseActivity, mSalesOrderGUID, mSalesOrderE.getDiningTableE().getName(), mSalesOrderE.getCheckTotal(), mSalesOrderE.getSerialNumber()), REQUEST_CODE_CHECK_OUT);
            } else {
                // 通知PaymentActivity刷新已收金额
                if (mL3PayListener != null) {
                    mL3PayListener.onPaymentHappenedAndCheckoutUnfinishedInnerL3(mSalesOrderE);
                }
            }
        } else {
            showMessage("数据非法，UnpaidTotal不得为null！");
        }
    }

    @Override
    public void onSunmiL3Failed(int code) {
        switch (code) {
            case 0:
                //账单已经结账 返回桌台页面
                mDialogFactory.showConfirmDialog("该账单已结账。", false, "", R.color.additional_frees_count_select
                        , true, "确定", R.color.btn_bg_light_blue_normal, new ConfirmDialogFragment.ConfirmDialogListener() {
                            @Override
                            public void onNegClick() {

                            }

                            @Override
                            public void onPosClick() {
                                mL3PayListener.onCheckoutFinishedInnerL3();
                            }
                        });
                break;
            case 1:
                //超额支付
                break;
            case 2:
                //其他异常
                getDialogFactory().showConfirmDialog(getString(R.string.usdk_pay_error), getString(R.string.cancel), getString(R.string.submit_try), new ConfirmDialogFragment.ConfirmDialogListener() {
                    @Override
                    public void onNegClick() {

                    }

                    @Override
                    public void onPosClick() {
                        submitData();
                    }
                });
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_CHECK_OUT == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                if (mL3PayListener != null) {
                    mL3PayListener.onCheckoutFinishedInnerL3();
                }
            } else if (Activity.RESULT_CANCELED == resultCode) {
                if (data != null) {
                    boolean aBoolean = data.getExtras().getBoolean(PaymentCheckoutActivity.RESULT_CODE_NEED_CORRECT);
                    if (aBoolean) {
                        if (mL3PayListener != null) {
                            mL3PayListener.onPaymentHappenedAndCheckoutUnfinishedInnerL3(mSalesOrderE);
                        }
                    } else {// 即时失败，也要刷新，既然能进CheckOut，说明有付款过程
                        if (mL3PayListener != null) {
                            mL3PayListener.onPaymentHappenedAndCheckoutUnfinishedInnerL3(mSalesOrderE);
                        }
                    }
                }
            }
        } else if (REQUEST_CODE_ZSY_PAY == requestCode) {
            if (resultCode == 0) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        showMessage("No extras provided");
                        return;
                    }
                    ResponseData response = (ResponseData) extras.getSerializable(ResponseData.KEY_ERTRAS);
                    showMessage("交易结果：" + response.getValue(BaseData.REJCODE_CN));
                    if ("00".equals(response.getValue(BaseData.REJCODE))) {
                        //原始流水号，用于交易撤销
                        mOrigTraceNo = response.getValue("TRACE_NO");
                        //交易参考号
                        mRefNumber = response.getValue("REF_NO");
                        submitData();
                    }
                }
            } else {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        showMessage("No extras provided");
                        return;
                    }
                    ResponseData response = (ResponseData) extras.getSerializable(ResponseData.KEY_ERTRAS);
                    showMessage("交易结果：" + response.getValue(BaseData.REJCODE_CN) + "," + response.getValue(BaseData.REJCODE));
                    //交易异常
                    if (!response.getValue(BaseData.REJCODE).equals(PAYMENT_CANCEL_CODE)) {
                        Message message = new Message();
                        message.what = PAYMENT_ERROR_MESSAGE;
                        mHandler.sendMessage(message);
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick({R.id.iv_clear, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete})
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

    public interface OnL3PayListener {
        void onPaymentHappenedAndCheckoutUnfinishedInnerL3(SalesOrderE salesOrderE);

        void onCheckoutFinishedInnerL3();
    }

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

    /**
     * 通联（智收银）支付设置
     */
    private void l3Payment() {
        //结算金额
        String amountStr = mEtActuallyPayMoney.getText().toString();
        String amount = amountStr.substring(1, amountStr.length());
        double dAmount = Double.parseDouble(amount);
        double dAmount100 = ArithUtil.mul(dAmount, 100);
        //不使用科学计数发
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        String dAmount100Str = numberFormat.format(dAmount100);
        String newAmount;
        //如果金额含有小数  去掉小数
        if (dAmount100Str.contains(".")) {
            newAmount = dAmount100Str.substring(0, dAmount100Str.indexOf("."));
        } else {
            newAmount = dAmount100Str;
        }
        //金额如果不足12位 左边添加0补足12位
        for (int i = 0; i < 12; i++) {
            if (newAmount.length() < 12) {
                newAmount = "0" + newAmount;
            } else {
                break;
            }
        }
        //标记ID
        mTransID = System.currentTimeMillis() + "";
        RequestData payData = new RequestData();
        payData.putValue("BUSINESS_ID", USDK_PAYMENT_CODE);
        payData.putValue("TRANS_CHECK", mTransID);
        payData.putValue("AMOUNT", newAmount);
        doTrans(REQUEST_CODE_ZSY_PAY, payData);
    }

    /**
     * 往服务器提交数据
     */
    private void submitData() {
        String amountStr = mEtActuallyPayMoney.getText().toString();
        //向服务器发起支付请求
        mPresenter.sunmiL3Pay(mSalesOrderGUID, PaymentItemCode_SYB01, mUnpaidTotal, Double.valueOf(amountStr.substring(1, amountStr.length()))
                , mOrigTraceNo, mRefNumber);
    }

    /**
     * 调起智收银界面
     *
     * @param requestCode 请求码
     * @param requestData 请求参数
     */
    private void doTrans(int requestCode, RequestData requestData) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));
        Bundle bundle = new Bundle();
        bundle.putSerializable(RequestData.KEY_ERTRAS, requestData);
        intent.putExtras(bundle);
        if (IntentExistUtils.isIntentExisting(intent, getActivity())) {
            startActivityForResult(intent, requestCode);
        } else {
            showMessage("此机器上没有安装智收银应用");
        }
    }
}
