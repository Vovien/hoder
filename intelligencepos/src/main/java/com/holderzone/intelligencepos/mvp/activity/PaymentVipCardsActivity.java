package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.PaymentVipCardsContract;
import com.holderzone.intelligencepos.mvp.model.bean.CardsE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.PaymentVipCardsPresebter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.Title;
import com.jakewharton.rxbinding2.view.RxView;
import com.jungly.gridpasswordview.GridPasswordView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class PaymentVipCardsActivity extends BaseActivity<PaymentVipCardsContract.Presenter> implements PaymentVipCardsContract.View {

    private static final String EXTRA_SALES_ORDER_GUID = "cn.holdzone.intelligencepos.SalesOrderGUID";
    private static final String EXTRA_CRADSE = "cn.holdzone.intelligencepos.CardsE";
    private static final int REQUEST_CODE_CHECK_OUT = 0x00000000;
    public static final String RESULT_CHECK_OUT_OR_NOT = "cn.holdzone.intelligencepos.CheckoutOrNot";
    public static final int RESULT_PAYMENT_BY_OTHER_WAY = 110;


    @BindView(R.id.gridPasswordView)
    GridPasswordView mGridPasswordView;
    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.tv_payment_discount)
    TextView tvPaymentDiscount;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_unpaid_total)
    TextView tv_unpaid_total;
    @BindView(R.id.layout_balance_insufficient)
    LinearLayout layoutBalanceInsufficient;
    @BindView(R.id.layout_balance_sufficient)
    LinearLayout layoutBalanceSufficient;
    @BindView(R.id.money_insufficient)
    TextView moneyInsufficient;
    @BindView(R.id.tv_forget_password)
    TextView forgetPassword;

    /**
     * 订单GUID
     */
    private String mSalesOrderGUID;

    /**
     * 使用的会员卡实体
     */
    private CardsE mCardsE;

    /**
     * 静态方法，对外暴露需要的参数
     *
     * @param context        上下文
     * @param salesOrderGUID 订单GUID
     * @param cardsE         会员卡实体
     * @return Intent意图
     */
    public static Intent newIntent(Context context, String salesOrderGUID, CardsE cardsE) {
        Intent intent = new Intent(context, PaymentVipCardsActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        extras.putParcelable(EXTRA_CRADSE, cardsE);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGUID = extras.getString(EXTRA_SALES_ORDER_GUID);
        mCardsE = extras.getParcelable(EXTRA_CRADSE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_payment_vip_cards;
    }

    @Nullable
    @Override
    protected PaymentVipCardsContract.Presenter initPresenter() {
        return new PaymentVipCardsPresebter(this);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化密码框
        initPasswordEditText();

        // 卡内余额    =0不足     =1足够
        boolean IsEnough = mCardsE.getIsEnough() == 1;
        moneyInsufficient.setVisibility(IsEnough ? View.GONE : View.VISIBLE);
        layoutBalanceInsufficient.setVisibility(IsEnough ? View.GONE : View.VISIBLE);
        layoutBalanceSufficient.setVisibility(IsEnough ? View.VISIBLE : View.GONE);
        forgetPassword.setVisibility(IsEnough ? View.VISIBLE : View.GONE);
        mGridPasswordView.setVisibility(IsEnough ? View.VISIBLE : View.GONE);


        mTitle.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });
        //设置支付折扣
        tvPaymentDiscount.setText("-" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCardsE.getDiscountAmount())));
        //设置实际应付竟然
        tv_unpaid_total.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCardsE.getActualyAmount())));
        mTvBalance.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mCardsE.getBalance())));


        //忘记密码
        RxView.clicks(forgetPassword).throttleFirst(1, TimeUnit.SECONDS).subscribe(o ->
                mPresenter.requestVerCode(mCardsE.getRegTel()));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHECK_OUT) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putBoolean(RESULT_CHECK_OUT_OR_NOT, true);
                intent.putExtras(extras);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }


    /**
     * 验证码获取失败
     *
     * @param msg 失败内容
     */
    @Override
    public void onGetVerCodeFailed(String msg) {
        showMessage(msg);
    }

    /**
     * 验证码获取成功
     *
     * @param verCode 验证码
     */
    @Override
    public void onGetVerCodeSuccess(String verCode) {
        startActivity(VercodeUidActiivty.newIntent(this, verCode, mCardsE.getRegTel()));
    }


    /**
     * 付款成功
     *
     * @param salesOrderE
     */
    @Override
    public void onPaySuccess(SalesOrderE salesOrderE) {

        showMessage("付款成功");
        // 业务跳转：结账界面 || 收款界面
        Double unpaidTotal = salesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            double mUnpaidTotal = unpaidTotal.doubleValue();
            if (mUnpaidTotal < 0.01) {// 跳转到结账界面
                startActivityForResult(
                        PaymentCheckoutActivity.newIntent(PaymentVipCardsActivity.this
                                , mSalesOrderGUID
                                , salesOrderE.getDiningTableE().getName()
                                , salesOrderE.getActuallyPayTotal()
                                , salesOrderE.getSerialNumber())
                        , REQUEST_CODE_CHECK_OUT);
            }
        } else {
            showMessage("数据非法，UnpaidTotal不得为null！");
        }
    }

    /**
     * 付款失败
     */

    @Override
    public void onPayFailedOutOfWrongPsd() {
        mDialogFactory.showConfirmDialog("支付密码错误", "取消支付", "重新输入", new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {
                setResult(RESULT_PAYMENT_BY_OTHER_WAY);
                finishActivity();
            }

            @Override
            public void onPosClick() {
            }
        });
    }

    @OnClick({R.id.other_pay_way, R.id.bt_number_1, R.id.bt_number_2, R.id.bt_number_3, R.id.bt_number_4, R.id.bt_number_5, R.id.bt_number_6, R.id.bt_number_7, R.id.bt_number_8, R.id.bt_number_9, R.id.bt_number_clear, R.id.bt_number_0, R.id.payment_keyboard_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.other_pay_way:
                setResult(RESULT_PAYMENT_BY_OTHER_WAY);
                finishActivity();
                break;
            case R.id.bt_number_1:
                writePassword("1");
                break;
            case R.id.bt_number_2:
                writePassword("2");
                break;
            case R.id.bt_number_3:
                writePassword("3");
                break;
            case R.id.bt_number_4:
                writePassword("4");
                break;
            case R.id.bt_number_5:
                writePassword("5");
                break;
            case R.id.bt_number_6:
                writePassword("6");
                break;
            case R.id.bt_number_7:
                writePassword("7");
                break;
            case R.id.bt_number_8:
                writePassword("8");
                break;
            case R.id.bt_number_9:
                writePassword("9");
                break;
            case R.id.bt_number_clear:
//                mGridPasswordView.clearPassword();
                break;
            case R.id.bt_number_0:
                writePassword("0");
                break;
            case R.id.payment_keyboard_delete:
                writePassword("delete");
                break;
            default:
                break;
        }
    }

    private void writePassword(String s) {
        String mPassWord = mGridPasswordView.getPassWord();
        if (s.equals("delete")) {
            int length = mPassWord.length();
            if (length == 0) {
                return;
            }
            mPassWord = mPassWord.substring(0, length - 1);
            mGridPasswordView.clearPassword();
            mGridPasswordView.setPassword(mPassWord);
        } else {
            mPassWord += s;
            mGridPasswordView.setPassword(mPassWord);
            String passWord = mGridPasswordView.getPassWord();
            if (passWord.length() == 6) {

                mPresenter.submitVipCardPay(mSalesOrderGUID, "VC01", mCardsE.getPayableAmount(), mCardsE.getCardsChipNo(), passWord);
                mGridPasswordView.clearPassword();
            }
        }
    }

    private void initPasswordEditText() {

        mGridPasswordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
    }


}
