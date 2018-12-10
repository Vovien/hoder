package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;

import butterknife.BindView;


/**
 * Created by tcw on 2017/5/25.
 */

public class ProgressDialogFragment extends BaseDialogFragment {
    public static final String ARGUMENT_EXTRA_MSG_KEY = "ARGUMENT_EXTRA_MSG_KEY";

    @BindView(R.id.tv_loading_msg)
    TextView mTvLoadingMsg;

    private String mMsg;

    public static ProgressDialogFragment newInstance(String msg) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_EXTRA_MSG_KEY, msg);
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        mMsg = args.getString(ARGUMENT_EXTRA_MSG_KEY);
    }

    @Override
    protected void setAttributesOnceCreate() {
        // 设置无标题
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_progress;
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
        wlp.height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
        window.setAttributes(wlp);
    }

    @Override
    protected void initView() {
        mTvLoadingMsg.setText(mMsg);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(Object o) {

    }
}
