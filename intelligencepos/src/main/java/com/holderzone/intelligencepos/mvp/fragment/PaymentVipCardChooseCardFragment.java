package com.holderzone.intelligencepos.mvp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.activity.CaptureActivity;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会员卡支付 选卡
 * Created by tcw on 2017/5/31.
 */

public class PaymentVipCardChooseCardFragment extends BaseFragment {

    @BindView(R.id.et_vip_phone_number)
    EditText mEtVipPhoneNumber;
    @BindView(R.id.iv_vip_phone_number_clear)
    ImageView mIvVipPhoneNumberClear;
    @BindView(R.id.ll_scan_code)
    LinearLayout mLlScanCode;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_number_dot)
    Button mBtnNumberDot;

    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        void onLoginClick(String regTel);
    }

    public static PaymentVipCardChooseCardFragment newInstance() {
        Bundle args = new Bundle();
        PaymentVipCardChooseCardFragment fragment = new PaymentVipCardChooseCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete, R.id.iv_vip_phone_number_clear})
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
                keyBoardSetting("clear");
                break;
            case R.id.btn_number_0:
                keyBoardSetting("0");
                break;
            case R.id.iv_delete:
                keyBoardSetting("del");
                break;
            case R.id.iv_vip_phone_number_clear:
                keyBoardSetting("clear");
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnClickListener) {
            mOnClickListener = (OnClickListener) parentFragment;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnClickListener != null) {
            mOnClickListener = null;
        }
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_payment_vip_card_choose_card;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mBtnNumberDot.setText("清空");
        mBtnNumberDot.setTextSize(18.7f);
        // 设置光标默认位置 请求焦点
        mEtVipPhoneNumber.setInputType(InputType.TYPE_NULL);
        mEtVipPhoneNumber.requestFocus();
        // 添加textChange监听
        mEtVipPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (!RegexExUtils.isMobileInputLegal(s)) {
                    s.delete(length - 1, length);
                    return;
                } else {
                    mEtVipPhoneNumber.setSelection(length);
                }
                if (RegexExUtils.isMobile(s)) {
                    mBtnLogin.setEnabled(true);
                } else {
                    mBtnLogin.setEnabled(false);
                }
                if (s.length() > 0) {
                    mIvVipPhoneNumberClear.setVisibility(View.VISIBLE);
                } else {
                    mIvVipPhoneNumberClear.setVisibility(View.GONE);
                }
            }
        });
        // 扫码按钮 防抖监听
        RxView.clicks(mLlScanCode).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            Intent scanIntent = new IntentIntegrator(mBaseActivity)
                    .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                    .setPrompt("")//写那句提示的话
                    .setOrientationLocked(true)//扫描方向固定
                    .setCaptureActivity(CaptureActivity.class) // 设置自定义的activity是CustomActivity
                    .addExtra(CaptureActivity.EXTRA_TITLE, "会员扫码登录")
                    .addExtra(CaptureActivity.EXTRA_MESSAGE, "请扫描顾客的会员二维码")
                    .createScanIntent();// 初始化扫描
            startActivityForResult(scanIntent, IntentIntegrator.REQUEST_CODE);
        });
        // 登录按钮 初始化、防抖监听
        mBtnLogin.setEnabled(false);
        RxView.clicks(mBtnLogin).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            if (mOnClickListener != null) {
                mOnClickListener.onLoginClick(mEtVipPhoneNumber.getText().toString());
            }
        });
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
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
//                showMessage("内容为空");
            } else {
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                showMessage(ScanResult);
                mEtVipPhoneNumber.setText(ScanResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 自定义键盘操作
     *
     * @param number
     */
    private void keyBoardSetting(CharSequence number) {
        Editable editable = mEtVipPhoneNumber.getText();
        if ("clear".equals(number)) {
            editable.clear();
            mEtVipPhoneNumber.requestFocus();
        } else {
            if ("del".equals(number)) {
                int length = editable.length();
                if (length == 0) {
                    mEtVipPhoneNumber.requestFocus();
                } else {
                    editable.delete(length - 1, length);
                    mEtVipPhoneNumber.requestFocus();
                }
            } else {
                editable.append(number);
                mEtVipPhoneNumber.requestFocus();
            }
        }
    }
}
