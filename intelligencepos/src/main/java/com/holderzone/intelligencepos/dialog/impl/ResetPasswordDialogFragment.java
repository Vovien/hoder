package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by tcw on 2017/5/25.
 */

public class ResetPasswordDialogFragment extends BaseDialogFragment<ResetPasswordDialogFragment.ResetPasswordDialogListener> {

    @BindView(R.id.et_ver_code)
    EditText mEtVerCode;
    @BindView(R.id.iv_clear)
    ImageView mIvClear;
    @BindView(R.id.btn_request_ver_code)
    Button mBtnRequestVerCode;
    @BindView(R.id.btn_negative)
    Button mBtnNegative;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;

    /**
     * 验证码UID
     */
    private String mVerCodeUID;

    /**
     * 验证码
     */
    private String mVerCode;


    /**
     * 倒计时dispose
     */
    private Disposable mDisposable;

    /**
     * 回调
     */
    private ResetPasswordDialogListener mResetPasswordDialogListener;

    /**
     * 回调接口定义
     */
    public interface ResetPasswordDialogListener {
        /**
         * 发送短信
         */
        void onRequestVerCodeClick();

        /**
         * 取消
         */
        void onNegClick();

        /**
         * 确认
         *
         * @param verCodeUID
         * @param verCode
         */
        void onPosClick(String verCodeUID, String verCode);
    }

    public static ResetPasswordDialogFragment newInstance() {
        Bundle args = new Bundle();
        ResetPasswordDialogFragment fragment = new ResetPasswordDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {

    }

    @Override
    protected void setAttributesOnceCreate() {
        // 设置无标题
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_reset_password;
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        // 设置不可取消
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        // 设置宽度为dialog_width*dialog_height，居中显示
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.dialog_bg);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        wlp.height = getResources().getDimensionPixelSize(R.dimen.dialog_height_reset_psd);
        window.setAttributes(wlp);
    }

    @Override
    protected void initView() {
        // 验证码editText 监听
        mEtVerCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mVerCode = s.toString();
                if (mVerCode.length() > 0) {
                    mIvClear.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(mVerCodeUID)) {
                        mBtnPositive.setEnabled(true);
                    } else {
                        mBtnPositive.setEnabled(false);
                    }
                } else {
                    mIvClear.setVisibility(View.GONE);
                    mBtnPositive.setEnabled(false);
                }
            }
        });
        // 清空按钮 防抖、监听
        RxView.clicks(mIvClear).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> mEtVerCode.setText(""));
        // 发送短信按钮 防抖、监听，初始化请求
        RxView.clicks(mBtnRequestVerCode).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            requestVerCode();
        });
        requestVerCode();
        // 取消按钮 防抖、监听
        RxView.clicks(mBtnNegative).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            if (mResetPasswordDialogListener != null) {
                mResetPasswordDialogListener.onNegClick();
            }
            dismiss();
        });
        // 确定按钮 防抖、监听，初始化
        RxView.clicks(mBtnPositive).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            if (mResetPasswordDialogListener != null) {
                mResetPasswordDialogListener.onPosClick(mVerCodeUID, mVerCode);
                dismiss();
            }
        });
        mBtnPositive.setEnabled(false);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(ResetPasswordDialogListener resetPasswordDialogListener) {
        mResetPasswordDialogListener = resetPasswordDialogListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private void requestVerCode() {
        Observable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (aLong < 60) {
                            mBtnRequestVerCode.setText(String.valueOf(60 - aLong));
                        } else {
                            mBtnRequestVerCode.setEnabled(true);
                            mBtnRequestVerCode.setText("发送短信");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDisposable != null) {
                            mDisposable = null;
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mDisposable != null) {
                            mDisposable = null;
                        }
                    }
                });
        mBtnRequestVerCode.setEnabled(false);
        if (mResetPasswordDialogListener != null) {
            mResetPasswordDialogListener.onRequestVerCodeClick();
        }
    }

    /**
     * 对外提供方法，用以更新该dialogFragment中mVerCodeUID值
     *
     * @param verCodeUID
     */
    public void update(String verCodeUID) {
        mVerCodeUID = verCodeUID;
    }
}
