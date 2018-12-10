package com.holderzone.intelligencepos.mvp.activity;

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
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.InputPasswordDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ResetPasswordDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.PaymentVipCardCollectionContract;
import com.holderzone.intelligencepos.mvp.model.bean.CardsE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.VipCardModel;
import com.holderzone.intelligencepos.mvp.presenter.PaymentVipCardCollectionPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.widget.Title;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会员卡收款（输入金额页）
 * Created by tcw on 2017/5/31.
 */

public class PaymentVipCardCollectionActivity extends BaseActivity<PaymentVipCardCollectionContract.Presenter> implements PaymentVipCardCollectionContract.View {

    private static final String EXTRA_SALES_ORDER_GUID = "cn.holdzone.intelligencepos.SalesOrderGUID";
    private static final String EXTRA_REG_TEL = "com.holderzone.intelligencepos.RegTel";
    private static final String EXTRA_UNPAID_TOTAL = "cn.holdzone.intelligencepos.UnpaidTotal";
    private static final String EXTRA_CRADSE = "cn.holdzone.intelligencepos.CardsE";
    private static final String KEY_IS_HES = "KEY_IS_HES";
    private static final String KEY_IS_USE_PASSWORD = "KEY_IS_USE_PASSWORD";
    private static final String KEY_HES_MEMBER_CARD = "KEY_HES_MEMBER_CARD";

    public static final String RESULT_CHECK_OUT_OR_NOT = "cn.holdzone.intelligencepos.CheckoutOrNot";

    private static final int REQUEST_CODE_CHECK_OUT = 0x00000000;

    public static final String PaymentItemCode_VC01 = "VC01";// 会员卡
    private static final String PaymentItemCode_HES01 = "Hes01";// He`s会员支付

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.tv_unpaid_total)
    TextView mTvUnpaidTotal;
    @BindView(R.id.et_actually_pay_money)
    EditText mEtActuallyPayMoney;
    @BindView(R.id.iv_actually_pay_money_clear)
    ImageView mIvActuallyPayMoneyClear;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    /**
     * 订单GUID
     */
    private String mSalesOrderGUID;

    /**
     * 待支付金额
     */
    private double mUnpaidTotal;

    /**
     * 注册手机号
     */
    private String mRegTel;

    /**
     * 使用的会员卡实体
     */
    private CardsE mCardsE;
    /**
     * 合适会员卡
     */
    private VipCardModel mVipCardModel;
    private boolean isHesMember = false;
    /**
     * 是否启用密码支付
     */
    private boolean isUsePassword = true;

    /**
     * 静态方法，对外暴露需要的参数
     *
     * @param context        上下文
     * @param salesOrderGUID 订单GUID
     * @param unpaidTotal    待支付金额
     * @param cardsE         会员卡实体
     * @return Intent意图
     */
    public static Intent newIntent(Context context, String salesOrderGUID, double unpaidTotal, String regTel, CardsE cardsE) {
        Intent intent = new Intent(context, PaymentVipCardCollectionActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        extras.putDouble(EXTRA_UNPAID_TOTAL, unpaidTotal);
        extras.putString(EXTRA_REG_TEL, regTel);
        extras.putParcelable(EXTRA_CRADSE, cardsE);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent newIntent(Context context, boolean isHesMember, boolean isUsePassword, String salesOrderGUID, double unpaidTotal, VipCardModel vipCardModel) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        bundle.putDouble(EXTRA_UNPAID_TOTAL, unpaidTotal);
        bundle.putBoolean(KEY_IS_HES, isHesMember);
        bundle.putBoolean(KEY_IS_USE_PASSWORD, isUsePassword);
        bundle.putParcelable(KEY_HES_MEMBER_CARD, vipCardModel);
        Intent intent = new Intent(context, PaymentVipCardCollectionActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGUID = extras.getString(EXTRA_SALES_ORDER_GUID);
        mUnpaidTotal = extras.getDouble(EXTRA_UNPAID_TOTAL);
        mRegTel = extras.getString(EXTRA_REG_TEL);
        mCardsE = extras.getParcelable(EXTRA_CRADSE);
        isHesMember = extras.getBoolean(KEY_IS_HES, false);
        isUsePassword = extras.getBoolean(KEY_IS_USE_PASSWORD, true);
        mVipCardModel = extras.getParcelable(KEY_HES_MEMBER_CARD);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_payment_vip_card_collection;
    }

    @Override
    protected PaymentVipCardCollectionContract.Presenter initPresenter() {
        return new PaymentVipCardCollectionPresenter(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 返回按钮  监听
        mTitle.setOnReturnClickListener(this::setResultCancelAndFinish);
        // 显示收款金额
        mTvUnpaidTotal.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mUnpaidTotal)));
        // 设置光标默认位置 请求焦点
        mEtActuallyPayMoney.setInputType(InputType.TYPE_NULL);
        mEtActuallyPayMoney.requestFocus();
        // 添加textChange监听
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
                if (RegexExUtils.isMoney(money)) {
                    Double cur = Double.valueOf(money.toString());
                    boolean balanceEnough;
                    if (isHesMember) {
                        balanceEnough = ArithUtil.sub(mVipCardModel.getBalance(), cur) >= 0;
                    } else {
                        balanceEnough = ArithUtil.sub(mCardsE.getBalance(), cur) >= 0;
                    }
                    if (cur > 0 && ArithUtil.sub(mUnpaidTotal, cur) >= 0 && balanceEnough) {
                        mBtnConfirm.setEnabled(true);
                    } else {
                        mBtnConfirm.setEnabled(false);
                    }
                } else {
                    mBtnConfirm.setEnabled(false);
                }
                if (length > 0) {
                    mIvActuallyPayMoneyClear.setVisibility(View.VISIBLE);
                } else {
                    mIvActuallyPayMoneyClear.setVisibility(View.GONE);
                }
            }
        });
        mEtActuallyPayMoney.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(Math.min(mUnpaidTotal, isHesMember ? mVipCardModel.getBalance() : mCardsE.getBalance()))));
        // 确认按钮 防抖监听
        RxView.clicks(mBtnConfirm)
                .throttleFirst(1L, TimeUnit.SECONDS)
                .subscribe(o -> {
                            if (isHesMember && !isUsePassword) {
                                String amountStr = mEtActuallyPayMoney.getText().toString();
                                mPresenter.hesMemberPay(mSalesOrderGUID, PaymentItemCode_HES01, Double.valueOf(amountStr.substring(1, amountStr.length())), mVipCardModel.isMember() ? 1 : 0, mVipCardModel.getCardNum(), null);
                            } else {
                                showInputPasswordDialog();
                            }
                        }

                );
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.iv_actually_pay_money_clear, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_actually_pay_money_clear:
                keyBoardSetting("clear");
                break;
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
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResultCancelAndFinish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_CHECK_OUT == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                setResultOkAndFinish(true);
            } else if (Activity.RESULT_CANCELED == resultCode) {
                if (data != null) {
                    boolean needCorrect = data.getExtras().getBoolean(PaymentCheckoutActivity.RESULT_CODE_NEED_CORRECT);
                    if (needCorrect) {
                        setResultOkAndFinish(false);
                    } else {
                        setResultOkAndFinish(false);
                    }
                }
            }
        }
    }

    /**************************view callback begin************************/

    @Override
    public void onPaySuccess(SalesOrderE salesOrderE) {
        // 弱提示
        showMessage("付款成功");
        // 业务跳转：结账界面 || 收款界面
        Double unpaidTotal = salesOrderE.getUnpaidTotal();
        if (unpaidTotal != null) {
            mUnpaidTotal = unpaidTotal.doubleValue();
            if (mUnpaidTotal < 0.01) {// 跳转到结账界面
                startActivityForResult(PaymentCheckoutActivity.newIntent(PaymentVipCardCollectionActivity.this, mSalesOrderGUID, salesOrderE.getDiningTableE().getName(), salesOrderE.getCheckTotal(), salesOrderE.getSerialNumber()), REQUEST_CODE_CHECK_OUT);
            } else {// 跳转回先前界面
                setResultOkAndFinish(false);
            }
        } else {
            showMessage("数据非法，UnpaidTotal不得为null！");
        }
    }

    @Override
    public void onPayFailedOutOfWrongPsd() {
        mDialogFactory.showConfirmDialog("支付密码错误。", "重新输入", "忘记密码", new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {
                showInputPasswordDialog();
            }

            @Override
            public void onPosClick() {
                // 忘记密码 so 显示重置密码对话框
                showResetPasswordDialog();
            }
        });
    }

    @Override
    public void onGetVerCodeSuccess(String verCode) {
        mDialogFactory.updateResetPasswordDialog(verCode);
    }

    /**
     * 业务成功，返回数据非法时 提示
     *
     * @param msg
     */
    @Override
    public void onGetVerCodeFailed(String msg) {
        showMessage(msg);
    }

    @Override
    public void onResetPasswordSuccess() {
        showMessage("重置密码成功");
    }

    @Override
    public void onResetPasswordFailed() {
        // do nothing
    }

    @Override
    public void onDispose() {

    }

    /**************************view callback end************************/

    /***************************private method begin*****************************/

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
     * 设置result cancel，并退出当前activity
     */
    private void setResultCancelAndFinish() {
        setResult(Activity.RESULT_CANCELED);
        finishActivity();
    }

    /**
     * 设置result ok，并退出当前activity
     *
     * @param checkout 是否成功结账
     */
    private void setResultOkAndFinish(boolean checkout) {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putBoolean(RESULT_CHECK_OUT_OR_NOT, checkout);
        intent.putExtras(extras);
        setResult(Activity.RESULT_OK, intent);
        finishActivity();
    }

    /**
     * 显示输入密码对话框
     */
    private void showInputPasswordDialog() {
        mDialogFactory.showInputPasswordDialog(isHesMember, new InputPasswordDialogFragment.ConfirmPasswordDialogListener() {
            @Override
            public void onForgetPsdClick() {
                showResetPasswordDialog();
            }

            @Override
            public void onInputDone(String password) {
                String amountStr = mEtActuallyPayMoney.getText().toString();
                if (isHesMember) {
                    mPresenter.hesMemberPay(mSalesOrderGUID, PaymentItemCode_HES01, Double.valueOf(amountStr.substring(1, amountStr.length())), mVipCardModel.isMember() ? 1 : 0, mVipCardModel.getCardNum(), password);
                } else {
                    mPresenter.submitVipCardPay(mSalesOrderGUID, PaymentItemCode_VC01, Double.valueOf(amountStr.substring(1, amountStr.length())), mCardsE.getCardsChipNo(), password);
                }
            }
        });
    }

    /**
     * 显示充值密码对话框
     */
    private void showResetPasswordDialog() {
        PermissionManager.checkPermission(PermissionManager.PERMISSION_RESET_PSD, () ->
                mDialogFactory.showResetPasswordDialog(new ResetPasswordDialogFragment.ResetPasswordDialogListener() {
                    @Override
                    public void onRequestVerCodeClick() {
                        mPresenter.requestVerCode(mRegTel);
                    }

                    @Override
                    public void onNegClick() {

                    }

                    @Override
                    public void onPosClick(String verCodeUID, String verCode) {
                        mPresenter.submitResetPassword(mRegTel, verCode, verCodeUID);
                    }
                }));

    }

    /***************************private method end*****************************/
}
