package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by tcw on 2017/5/31.
 */

public class IntervalDialogFragment extends BaseDialogFragment<IntervalDialogFragment.IntervalDialogListener> {
    public static final String ARGUMENT_EXTRA_MSG_KEY = "ARGUMENT_EXTRA_MSG_KEY";
    public static final String ARGUMENT_EXTRA_BUTTON_TEXT_KEY = "ARGUMENT_EXTRA_BUTTON_TEXT_KEY";

    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;

    private String mMsg;
    private String mBtnText;

    public IntervalDialogListener mIntervalDialogListener;

    public interface IntervalDialogListener {
        void onClick();
    }

    public static IntervalDialogFragment newInstance(String msg, String btnText) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_EXTRA_MSG_KEY, msg);
        args.putString(ARGUMENT_EXTRA_BUTTON_TEXT_KEY, btnText);
        IntervalDialogFragment fragment = new IntervalDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_interval;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        mMsg = args.getString(ARGUMENT_EXTRA_MSG_KEY);
        mBtnText = args.getString(ARGUMENT_EXTRA_BUTTON_TEXT_KEY);
    }

    @Override
    protected void setAttributesOnceCreate() {
        // 设置无标题
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        // 处理取消事件的回调
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        // 设置宽度为dialog_width*dialog_height，居中显示
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.dialog_bg);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        wlp.height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
        window.setAttributes(wlp);
    }

    @Override
    protected void initView() {
        // title,content,btn 文字设置
        mTvMsg.setText(mMsg);
        mBtnPositive.setText(mBtnText);
        // 取消付款 防抖、监听
        RxView.clicks(mBtnPositive).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            if (mIntervalDialogListener != null) {
                mIntervalDialogListener.onClick();
                dismiss();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(IntervalDialogListener intervalDialogListener) {
        mIntervalDialogListener = intervalDialogListener;
    }
}
