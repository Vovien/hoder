package com.holderzone.intelligencepos.dialog.impl;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialog;

import butterknife.BindView;

/**
 * Created by tcw on 2017/8/29.
 */

public class ForceLogOutDialog extends BaseDialog {

    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.btn_negative)
    Button mBtnNegative;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;

    // 提示消息内容
    private String mMsg;

    // 监听回调
    private OnClickListener mOnClickListener;

    // 监听interface
    public interface OnClickListener {

        void onClick();
    }

    public ForceLogOutDialog(Context context, String msg) {
        super(context);
        mMsg = msg;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_confirm;
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
        // 设置系统级Dialog
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    @Override
    protected void initView() {
        mTvMsg.setText(mMsg);
        mBtnNegative.setVisibility(View.GONE);
        mBtnPositive.setText("确认退出");
        mBtnPositive.setBackgroundResource(R.drawable.selector_button_red);
        mBtnPositive.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                dismiss();
                mOnClickListener.onClick();
            }
        });
    }

    @Override
    protected void initData() {

    }

    /**
     * 设置按钮监听
     *
     * @param onClickListener
     */
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
