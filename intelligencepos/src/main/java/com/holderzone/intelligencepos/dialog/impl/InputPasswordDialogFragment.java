package com.holderzone.intelligencepos.dialog.impl;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.jakewharton.rxbinding2.view.RxView;
import com.jungly.gridpasswordview.GridPasswordView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 付款时输入6位密码对话框，特定场景，较难复用
 * Created by tcw on 2017/3/21.
 */

public class InputPasswordDialogFragment extends BaseDialogFragment<InputPasswordDialogFragment.ConfirmPasswordDialogListener> {
    private static final String KEY_IS_HES_MEMBER = "KEY_IS_HES_MEMBER";

    @BindView(R.id.tv_forget_psd)
    TextView mTvForgetPsd;
    @BindView(R.id.gridPasswordView)
    GridPasswordView mGridPasswordView;
    @BindView(R.id.btn_number_dot)
    Button mBtnNumberDot;

    private boolean isHesMember = false;

    private ConfirmPasswordDialogListener mConfirmPasswordDialogListener;

    public interface ConfirmPasswordDialogListener {
        void onForgetPsdClick();

        void onInputDone(String password);
    }

    public static InputPasswordDialogFragment newInstance() {
        Bundle args = new Bundle();
        InputPasswordDialogFragment fragment = new InputPasswordDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static InputPasswordDialogFragment newInstance(boolean isHesMember) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_IS_HES_MEMBER, isHesMember);
        InputPasswordDialogFragment fragment = new InputPasswordDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        isHesMember = args.getBoolean(KEY_IS_HES_MEMBER, false);
    }

    @Override
    protected void setAttributesOnceCreate() {
        // 设置底部弹出
        setStyle(STYLE_NORMAL, R.style.Dialog_BottomSheet);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_input_password;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // 动画
//        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        slide.setDuration(400);
//        slide.setFillAfter(true);
//        slide.setFillEnabled(true);
//        view.startAnimation(slide);
        return view;
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        // 处理取消事件的回调
        getDialog().setCanceledOnTouchOutside(false);
        // 设置宽度为全屏，高度为3/4，居下显示
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.dialog_bg);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = getResources().getDisplayMetrics().heightPixels * 14 / 25;
        window.setAttributes(wlp);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        // 默认不弹出键盘
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

        if (isHesMember) {
            mTvForgetPsd.setVisibility(View.GONE);
        } else {
            mTvForgetPsd.setVisibility(View.VISIBLE);
        }
        // "忘记密码" 防抖、监听
        RxView.clicks(mTvForgetPsd).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            if (mConfirmPasswordDialogListener != null) {
                mConfirmPasswordDialogListener.onForgetPsdClick();
            }
        });
        // "." 按钮重定义
        mBtnNumberDot.setText("清空");
        mBtnNumberDot.setTextSize(18.7f);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(ConfirmPasswordDialogListener confirmPasswordDialogListener) {
        mConfirmPasswordDialogListener = confirmPasswordDialogListener;
    }

    @OnClick({R.id.iv_close, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3,
            R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6,
            R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9,
            R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
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
                keyBoardSetting("clear");
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

    /**
     * 自定义键盘操作
     *
     * @param number
     */
    private void keyBoardSetting(CharSequence number) {
        String passWord = mGridPasswordView.getPassWord();
        if ("clear".equals(number)) {
            mGridPasswordView.clearPassword();
        } else {
            if ("del".equals(number)) {
                int length = passWord.length();
                if (length > 0) {
                    mGridPasswordView.setPassword(passWord.substring(0, length - 1));
                }
            } else {
                mGridPasswordView.setPassword(passWord + number);
                String psw = mGridPasswordView.getPassWord();
                if (psw.length() == 6) {
                    if (mConfirmPasswordDialogListener != null) {
                        mConfirmPasswordDialogListener.onInputDone(psw);
                        dismiss();
                    }
                }
            }
        }
    }
}
