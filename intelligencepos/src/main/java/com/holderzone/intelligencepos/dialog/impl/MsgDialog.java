package com.holderzone.intelligencepos.dialog.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialog;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by terry on 17-11-7.
 */

public class MsgDialog extends BaseDialog {

    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;
    private String mMsg;

    public MsgDialog(@NonNull Context context) {
        super(context);
        mMsg = "这是一条默认消息";
    }

    public MsgDialog(@NonNull Context context, @NonNull String msg) {
        super(context);
        mMsg = msg;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_msg;
    }

    @Override
    protected void setAttributesBefore() {
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void setAttributesAfter() {
        // 设置不可取消
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        // 设置宽度为dialog_width*dialog_height，居中显示
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.dialog_bg);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_width);
        wlp.height = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_height);
        window.setAttributes(wlp);
    }

    @Override
    protected void initView() {
        mTvMsg.setText(mMsg);
        RxView.clicks(mBtnPositive)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> dismiss());
    }

    @Override
    protected void initData() {

    }
}
