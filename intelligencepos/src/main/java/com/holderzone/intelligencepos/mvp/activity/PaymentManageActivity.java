package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allinpay.usdk.core.data.BaseData;
import com.allinpay.usdk.core.data.RequestData;
import com.allinpay.usdk.core.data.ResponseData;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.decoration.SolidLineItemDecoration;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.PaymentManageContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentManagePresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.IntentExistUtils;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;
import com.trello.rxlifecycle2.internal.Preconditions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 付款项列表界面
 * Created by tcw on 2017/6/1.
 */

public class PaymentManageActivity extends BaseActivity<PaymentManageContract.Presenter> implements PaymentManageContract.View {

    public static final String EXTRA_SALES_ORDER_GUID = "EXTRA_SALES_ORDER_GUID";
    public static final String EXTRA_LAUNCH_FROM_PAYMENT_THEN_REFUND = "EXTRA_LAUNCH_FROM_PAYMENT_THEN_REFUND";
    public static final String RESULT_REFUND_HAPPENED = "RESULT_REFUND_HAPPENED";

    private static final int REQUEST_CODE_CHECK_OUT = 0x00000015;
    private static final int REQUEST_CODE_MTC_REFUND = 0x00000100;
    private static final int REQUEST_CODE_RETUEN_PAY = 0x00000016;
    private static final String REQUEST_RETURN_CODE = "200100001";//通联支付撤销
    private static final int PAY_RETURN_CODE = 1;
    private static final String PAYMENT_CANCEL_CODE = "FFFE";//通联支付取消
    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.rv_sales_order_payment)
    RecyclerView mRvSalesOrderPayment;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;
    @BindView(R.id.btn_check_out)
    Button mBtnCheckOut;
    @BindView(R.id.tv_check_out_total)
    TextView mTvCheckOutTotal;
    @BindView(R.id.tv_payable_total)
    TextView mTvPayableTotal;

    /**
     * 待支付订单GUID
     */
    private String mSaleOrderGUID;

    /**
     * 从Payment跳转过来修正超额款项
     */
    private boolean mLaunchFromPaymentActivityThenRefundMoney;

    /**
     * 付款项adapter
     */
    private CommonAdapter<SalesOrderPaymentE> mCommonAdapter;

    /**
     * 付款项adapter数据源
     */
    private List<SalesOrderPaymentE> mSalesOrderPaymentEList = new ArrayList<>();

    /**
     * 待支付金额
     */
    private double mUnpaidTotal;

    /**
     * result flag
     */
    private boolean mRefundHappened;

    /**
     * 流水号
     */
    private String mSerialNumber;

    /**
     * 应付总额
     */
    private double mCheckTotal;

    /**
     * 餐桌名字
     */
    private String mDiningTableName;
    /**
     * 点击的 item的收款记录标志
     */
    private String mCurrentSalesOrderPaymentGUID;
    /**
     * 原始流水号  消费撤销时用
     */
    private String mOrigTraceNo = null;
    /**
     * 交易撤销参考号
     */
    private String mRefNumber;
    /**
     * 通联支付异常 处理
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAY_RETURN_CODE:
                    //通联支付撤销失败处理
                    getDialogFactory().showPaymentAllinReturnDialog(refNumber -> {
                        mRefNumber = refNumber;
                        mPresenter.submitRefund(mSaleOrderGUID, mCurrentSalesOrderPaymentGUID
                                , PaymentActivity.PaymentItemCode_SYB01, mRefNumber);
                    });
                    break;
                default:
                    break;
            }
        }
    };

    public static Intent newIntent(Context context, String salesOrderGUID) {
        Intent intent = new Intent(context, PaymentManageActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent newIntent(Context context, String salesOrderGUID, boolean launchFromPaymentActivityThenRefundMoney) {
        Intent intent = new Intent(context, PaymentManageActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        extras.putBoolean(EXTRA_LAUNCH_FROM_PAYMENT_THEN_REFUND, launchFromPaymentActivityThenRefundMoney);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSaleOrderGUID = extras.getString(EXTRA_SALES_ORDER_GUID);
        mLaunchFromPaymentActivityThenRefundMoney = extras.getBoolean(EXTRA_LAUNCH_FROM_PAYMENT_THEN_REFUND);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_payment_manage;
    }

    @Override
    protected PaymentManageContract.Presenter initPresenter() {
        return new PaymentManagePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // title
        mTitle.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putBoolean(RESULT_REFUND_HAPPENED, mRefundHappened);
                intent.putExtras(extras);
                setResult(Activity.RESULT_CANCELED, intent);
                finishActivity();
            }
        });
        // MultiStateView retry when error
        Button btnRetry = (Button) mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(btnRetry).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> mPresenter.requestSalesOrder(mSaleOrderGUID));
        // adapter && rv
        mCommonAdapter = new CommonAdapter<SalesOrderPaymentE>(PaymentManageActivity.this, R.layout.item_sales_order_payment, mSalesOrderPaymentEList) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderPaymentE salesOrderPaymentE, int position) {
                holder.setText(R.id.tv_sales_order_payment_name, salesOrderPaymentE.getPaymentItemName());
                holder.setText(R.id.tv_sales_order_payment_actually_amount, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.add(salesOrderPaymentE.getActuallyAmount(), salesOrderPaymentE.getDiscountAmount()))));
                holder.setOnClickListener(R.id.iv_retreat_sales_order_payment, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PermissionManager.checkPermission(PermissionManager.PERMISSION_REFUND_MONEY,
                                () -> {
                                    if (PaymentActivity.PaymentItemCode_MTC01.equals(salesOrderPaymentE.getPaymentItemCode())) {
                                        Intent intent = PaymentMtcRefundActivity.newIntent(PaymentManageActivity.this, mSaleOrderGUID, salesOrderPaymentE.getSalesOrderPaymentGUID());
                                        startActivityForResult(intent, REQUEST_CODE_MTC_REFUND);
                                    } else {
                                        mDialogFactory.showConfirmDialog("确认退还该笔支付吗？", "取消", "确认退款", new ConfirmDialogFragment.ConfirmDialogListener() {
                                            @Override
                                            public void onNegClick() {

                                            }

                                            @Override
                                            public void onPosClick() {
                                                if ("SYB01".equals(salesOrderPaymentE.getPaymentItemCode())) {
                                                    //通联支付方式
                                                    mCurrentSalesOrderPaymentGUID = salesOrderPaymentE.getSalesOrderPaymentGUID();
                                                    mOrigTraceNo = salesOrderPaymentE.getTransactionNumber();
                                                    Preconditions.checkNotNull(mOrigTraceNo, "原始流水号为空!");
                                                    double amount = ArithUtil.add(salesOrderPaymentE.getActuallyAmount(), salesOrderPaymentE.getDiscountAmount());
                                                    String amountStr = String.valueOf(amount);
                                                    returnL3Pay(amountStr);
                                                } else {
                                                    //其他支付方式
                                                    mPresenter.submitRefund(mSaleOrderGUID, salesOrderPaymentE.getSalesOrderPaymentGUID(), "other", null);
                                                }
                                            }
                                        });
                                    }
                                });
                    }
                });
                double discountAmount = salesOrderPaymentE.getDiscountAmount();
                if (discountAmount > 0.001) {
                    holder.setVisible(R.id.ll_discount, true);
                    holder.setText(R.id.tv_sales_order_payment_discount, "支付折扣");
                    holder.setText(R.id.tv_sales_order_payment_discount_amount, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(discountAmount)));
                } else {
                    holder.setVisible(R.id.ll_discount, false);
                }
            }
        };
        mRvSalesOrderPayment.setLayoutManager(new LinearLayoutManager(PaymentManageActivity.this));
        mRvSalesOrderPayment.setAdapter(mCommonAdapter);
        mRvSalesOrderPayment.addItemDecoration(new SolidLineItemDecoration(PaymentManageActivity.this, LinearLayoutManager.VERTICAL));
        // checkout button
        RxView.clicks(mBtnCheckOut).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            mDialogFactory.showConfirmDialog("实收" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCheckTotal)) + "，确认结账吗？",
                    "取消", "确认结账", new ConfirmDialogFragment.ConfirmDialogListener() {
                        @Override
                        public void onNegClick() {

                        }

                        @Override
                        public void onPosClick() {
                            startActivityForResult(PaymentCheckoutActivity.newIntent(PaymentManageActivity.this, mSaleOrderGUID, mDiningTableName, mCheckTotal, mSerialNumber), REQUEST_CODE_CHECK_OUT);
                        }
                    });
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestSalesOrder(mSaleOrderGUID);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putBoolean(RESULT_REFUND_HAPPENED, mRefundHappened);
        intent.putExtras(extras);
        setResult(Activity.RESULT_CANCELED, intent);
        finishActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHECK_OUT) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK);
                finishActivity();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (data != null) {
                    boolean needCorrect = data.getExtras().getBoolean(PaymentCheckoutActivity.RESULT_CODE_NEED_CORRECT);
                    if (needCorrect) {
                        mRefundHappened = true;
                        mPresenter.requestSalesOrder(mSaleOrderGUID);
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_MTC_REFUND) {
            boolean payHappened = false;
            if (resultCode == Activity.RESULT_CANCELED) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        payHappened = extras.getBoolean(PaymentMtcRefundActivity.RESULT_CODE_PAYMENT_HAPPENED_INNER_REFUND);
                    }
                }
            }
            mRefundHappened = payHappened;
            if (mRefundHappened) {
                mPresenter.requestSalesOrder(mSaleOrderGUID);
            }
        } else if (requestCode == REQUEST_CODE_RETUEN_PAY) {
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
                        //交易参考号
                        mRefNumber = response.getValue("REF_NO");
                        mPresenter.submitRefund(mSaleOrderGUID, mCurrentSalesOrderPaymentGUID, PaymentActivity.PaymentItemCode_SYB01, mRefNumber);
                    }
                }
            } else {
                //撤销失败
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        showMessage("No extras provided");
                        return;
                    }
                    ResponseData response = (ResponseData) extras.getSerializable(ResponseData.KEY_ERTRAS);
                    showMessage("交易结果：" + response.getValue(BaseData.REJCODE_CN) + "," + response.getValue(BaseData.REJCODE));
                    if (!response.getValue(BaseData.REJCODE).equals(PAYMENT_CANCEL_CODE)) {
                        Message message = new Message();
                        message.what = PAY_RETURN_CODE;
                        mHandler.sendMessage(message);
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /********************view callback begin*********************/

    @Override
    public void onSalesOrderObtainSucceed(SalesOrderE salesOrderE) {
        // 结账界面需要 流水号 和 实收金额
        mSerialNumber = salesOrderE.getSerialNumber();
        mDiningTableName = salesOrderE.getDiningTableE().getName();
        mCheckTotal = salesOrderE.getCheckTotal();
        // 刷新订单基本数据
        refreshBasicInfo(salesOrderE);
        // 刷新付款项
        refreshSalesOrderPayment(salesOrderE);
    }

    @Override
    public void onSalesOrderObtainFailed(String msg) {
//        showMessage(msg);
    }

    @Override
    public void onRefundSucceed(SalesOrderE salesOrderE) {
        mCheckTotal = salesOrderE.getCheckTotal();
        // 刷新订单基本数据
        refreshBasicInfo(salesOrderE);
        // 刷新付款项
        refreshSalesOrderPayment(salesOrderE);
        // 更新是否发生退付款frag
        updateResultFlag();
        //清空原始流水号
        if (mOrigTraceNo != null) {
            mOrigTraceNo = null;
        }
        //退款成功 弱提示
        showMessage("退款成功");
    }

    @Override
    public void onRefundFailed(String msg) {
        if (mOrigTraceNo != null) {
            getDialogFactory().showConfirmDialog("无法连接到服务器，", "取消", "再次尝试", new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {

                }

                @Override
                public void onPosClick() {
                    mPresenter.submitRefund(mSaleOrderGUID, mCurrentSalesOrderPaymentGUID, PaymentActivity.PaymentItemCode_SYB01, mRefNumber);
                }
            });
        } else {
            showMessage(msg);
        }
    }

    @Override
    public void showNetworkErrorLayout() {
        mBtnCheckOut.setVisibility(View.GONE);
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void showIntervalDialog() {
        mDialogFactory.showIntervalDialog("未完成退款，请勿关闭窗口...", "关闭并重新操作"
                , () -> mPresenter.disposeWxPay())
        ;
    }

    @Override
    public void hideIntervalDialog() {
        mDialogFactory.dismissIntervalDialog();
    }

    @Override
    public void onDispose() {

    }

    /********************view callback end*********************/

    /**
     * 刷新订单基本数据
     *
     * @param salesOrderE
     */
    private void refreshBasicInfo(SalesOrderE salesOrderE) {
        mTvCheckOutTotal.setText(getString(R.string.amount_str,ArithUtil.stripTrailingZeros( salesOrderE.getPayTotal())));
        mTvPayableTotal.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCheckTotal)));
        Double unpaidTotal = salesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            mUnpaidTotal = unpaidTotal.doubleValue();
            if (mUnpaidTotal < 0.01) {
                Double payTotal = salesOrderE.getPayTotal();
                if (payTotal != null) {
                    if (payTotal == mCheckTotal) {
                        mBtnCheckOut.setEnabled(true);
                        mBtnCheckOut.setText("结账");
                        mDialogFactory.showConfirmDialog("实收" + getString(R.string.amount_str,ArithUtil.stripTrailingZeros( salesOrderE.getCheckTotal().doubleValue())) + "，确认结账吗？",
                                "取消", "确认结账", new ConfirmDialogFragment.ConfirmDialogListener() {
                                    @Override
                                    public void onNegClick() {

                                    }

                                    @Override
                                    public void onPosClick() {
                                        startActivityForResult(PaymentCheckoutActivity.newIntent(PaymentManageActivity.this, mSaleOrderGUID, mDiningTableName, mCheckTotal, mSerialNumber), REQUEST_CODE_CHECK_OUT);
                                    }
                                });
                    } else if (payTotal > mCheckTotal) {
                        mBtnCheckOut.setEnabled(false);
                        mBtnCheckOut.setText("溢收：" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(ArithUtil.sub(payTotal, mCheckTotal))));
                        if (mLaunchFromPaymentActivityThenRefundMoney) {
                            mLaunchFromPaymentActivityThenRefundMoney = false;
                        } else {
                            mDialogFactory.showConfirmDialog("价格已调整为应收" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCheckTotal)) +
                                    "，超额实收" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(payTotal)) +
                                    "。请退还超额款项后结账。", false, null, 0, true, "确定", 0, new ConfirmDialogFragment.ConfirmDialogListener() {
                                @Override
                                public void onNegClick() {

                                }

                                @Override
                                public void onPosClick() {

                                }
                            });
                        }
                    }
                } else {
                    showMessage("数据非法，PayTotal不得为null！");
                }
            } else {
                mBtnCheckOut.setEnabled(false);
                mBtnCheckOut.setText("待收:" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mUnpaidTotal)));
            }
        }
    }

    /**
     * 刷新付款项
     *
     * @param salesOrderE
     */
    private void refreshSalesOrderPayment(SalesOrderE salesOrderE) {
        mSalesOrderPaymentEList.clear();
        List<SalesOrderPaymentE> arrayOfSalesOrderPaymentE = salesOrderE.getArrayOfSalesOrderPaymentE();
        if (arrayOfSalesOrderPaymentE.size() > 0) {
            for (SalesOrderPaymentE salesOrderPaymentE : arrayOfSalesOrderPaymentE) {
                double sub = ArithUtil.sub(salesOrderPaymentE.getActuallyAmount(), salesOrderPaymentE.getRefundAmount());
                if (sub < 0.01) {
                    continue;
                }
                mSalesOrderPaymentEList.add(salesOrderPaymentE);
            }
            if (mSalesOrderPaymentEList.size() > 0) {
                mBtnCheckOut.setVisibility(View.VISIBLE);
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            } else {
                mBtnCheckOut.setVisibility(View.VISIBLE);
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
            mCommonAdapter.notifyDataSetChanged();
        } else {
            mBtnCheckOut.setVisibility(View.VISIBLE);
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    /**
     * 是否发生了支付或退款
     */
    private void updateResultFlag() {
        if (!mRefundHappened) {
            mRefundHappened = true;
        }
    }

    /**
     * 通联支付撤销
     *
     * @param amount 交易金额
     */
    private void returnL3Pay(String amount) {
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
        RequestData payReturnData = new RequestData();
        payReturnData.putValue("BUSINESS_ID", REQUEST_RETURN_CODE);
        payReturnData.putValue("AMOUNT", newAmount);
        payReturnData.putValue("ORIG_TRACE_NO", mOrigTraceNo);
        doTrans(REQUEST_CODE_RETUEN_PAY, payReturnData);
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
        if (IntentExistUtils.isIntentExisting(intent, this)) {
            startActivityForResult(intent, requestCode);
        } else {
            showMessage("此机器上没有安装智收银应用");
        }
    }
}
