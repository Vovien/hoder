package com.holderzone.intelligencepos.dialog.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialog;

import butterknife.BindView;

/**
 * 进度dialog
 * Created by tcw on 2017/9/6.
 */

public class ProgressDialog extends BaseDialog {

    @BindView(R.id.tv_loading_msg)
    TextView mTvLoadingMsg;

    // 提示消息内容
    private String mMsg;

    public ProgressDialog(@NonNull Context context, String msg) {
        super(context);
        mMsg = msg;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_progress;
    }

    @Override
    protected void setAttributesBefore() {
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void setAttributesAfter() {
        // 设置不可取消、不可触摸取消
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        // 设置宽高
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
        mTvLoadingMsg.setText(mMsg);
    }

    @Override
    protected void initData() {

    }
}
