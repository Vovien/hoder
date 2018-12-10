package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tcw on 2017/5/25.
 */

public class ConfirmDialogFragment extends BaseDialogFragment<ConfirmDialogFragment.ConfirmDialogListener> {
    public static final String ARGUMENT_EXTRA_MSG_KEY = "ARGUMENT_EXTRA_MSG_KEY";

    public static final String ARGUMENT_EXTRA_NEG_VISIBILITY_KEY = "ARGUMENT_EXTRA_NEG_VISIBILITY_KEY";
    public static final String ARGUMENT_EXTRA_NEG_TEXT_KEY = "ARGUMENT_EXTRA_NEG_TEXT_KEY";
    public static final String ARGUMENT_EXTRA_NEG_RES_KEY = "ARGUMENT_EXTRA_NEG_RES_KEY";
    public static final String ARGUMENT_EXTRA_IS_MORE_COLOR = "ARGUMENT_EXTRA_IS_MORE_COLOR";

    public static final String ARGUMENT_EXTRA_POS_VISIBILITY_KEY = "ARGUMENT_EXTRA_POS_VISIBILITY_KEY";
    public static final String ARGUMENT_EXTRA_POS_TEXT_KEY = "ARGUMENT_EXTRA_POS_TEXT_KEY";
    public static final String ARGUMENT_EXTRA_POS_RES_KEY = "ARGUMENT_EXTRA_POS_RES_KEY";

    public static final int DEFAULT_NEG_RES = R.drawable.selector_button_green;
    public static final int DEFAULT_POS_RES = R.drawable.selector_button_blue;


    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.btn_negative)
    Button mBtnNegative;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;

    // 内容
    private String mMsg;
    // 左下侧按钮是否可见
    private boolean mNegVisibility;
    private String mNegText;
    @DrawableRes
    private int mNegDrawableRes;
    // 右下侧按钮是否可见
    private boolean mPosVisibility;
    private String mPosText;
    //是否显示多种颜色
    private boolean isMoreColor;

    @DrawableRes
    private int mPosDrawableRes;

    private ConfirmDialogListener mConfirmDialogListener;

    public interface ConfirmDialogListener {
        void onNegClick();

        void onPosClick();
    }

    public static ConfirmDialogFragment newInstance(String msg, String negaText, String posiText) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_EXTRA_MSG_KEY, msg);
        args.putString(ARGUMENT_EXTRA_NEG_TEXT_KEY, negaText);
        args.putString(ARGUMENT_EXTRA_POS_TEXT_KEY, posiText);
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ConfirmDialogFragment newInstance(String msg, boolean isMoreColor, String negaText, String posiText) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_EXTRA_MSG_KEY, msg);
        args.putString(ARGUMENT_EXTRA_NEG_TEXT_KEY, negaText);
        args.putString(ARGUMENT_EXTRA_POS_TEXT_KEY, posiText);
        args.putBoolean(ARGUMENT_EXTRA_IS_MORE_COLOR, isMoreColor);
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ConfirmDialogFragment newInstance(String msg,
                                                    boolean negaVisibility, String negaText, @DrawableRes int negDrawableRes,
                                                    boolean posiVisibility, String posiText, @DrawableRes int posDrawableRes) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_EXTRA_MSG_KEY, msg);
        args.putBoolean(ARGUMENT_EXTRA_NEG_VISIBILITY_KEY, negaVisibility);
        args.putString(ARGUMENT_EXTRA_NEG_TEXT_KEY, negaText);
        args.putInt(ARGUMENT_EXTRA_NEG_RES_KEY, negDrawableRes);
        args.putBoolean(ARGUMENT_EXTRA_POS_VISIBILITY_KEY, posiVisibility);
        args.putString(ARGUMENT_EXTRA_POS_TEXT_KEY, posiText);
        args.putInt(ARGUMENT_EXTRA_POS_RES_KEY, posDrawableRes);
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        mMsg = args.getString(ARGUMENT_EXTRA_MSG_KEY);
        isMoreColor = args.getBoolean(ARGUMENT_EXTRA_IS_MORE_COLOR, false);
        mNegVisibility = args.getBoolean(ARGUMENT_EXTRA_NEG_VISIBILITY_KEY, true);
        if (mNegVisibility) {
            mNegText = args.getString(ARGUMENT_EXTRA_NEG_TEXT_KEY);
            mNegDrawableRes = args.getInt(ARGUMENT_EXTRA_NEG_RES_KEY, 0);
        }
        mPosVisibility = args.getBoolean(ARGUMENT_EXTRA_POS_VISIBILITY_KEY, true);
        if (mPosVisibility) {
            mPosText = args.getString(ARGUMENT_EXTRA_POS_TEXT_KEY);
            mPosDrawableRes = args.getInt(ARGUMENT_EXTRA_POS_RES_KEY, 0);
        }
    }

    @Override
    protected void setAttributesOnceCreate() {
        // 设置无标题
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_confirm;
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
        // 内容
        if (isMoreColor) {
            CharSequence charSequence = new SpanUtils()
                    .append("确定")
                    .setForegroundColor(ContextCompat.getColor(getContext(), R.color.common_text_color_000000))
                    .append(mMsg)
                    .setForegroundColor(ContextCompat.getColor(getContext(), R.color.common_text_color_2495ee))
                    .append("菜品已全上齐吗？")
                    .setForegroundColor(ContextCompat.getColor(getContext(), R.color.common_text_color_000000))
                    .create();
            mTvMsg.setText(charSequence);
        } else {
            mTvMsg.setText(mMsg);
        }
        // 左下侧按钮
        mBtnNegative.setVisibility(mNegVisibility ? View.VISIBLE : View.GONE);
        if (mNegVisibility) {
            if (0 != mNegDrawableRes) {
                mBtnNegative.setBackgroundResource(mNegDrawableRes);
            }
            mBtnNegative.setText(mNegText);
        }
        // 右下侧按钮
        mBtnPositive.setVisibility(mPosVisibility ? View.VISIBLE : View.GONE);
        if (mPosVisibility) {
            if (0 != mPosDrawableRes) {
                mBtnPositive.setBackgroundResource(mPosDrawableRes);
            }
            mBtnPositive.setText(mPosText);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(ConfirmDialogListener confirmDialogListener) {
        mConfirmDialogListener = confirmDialogListener;
    }

    @OnClick({R.id.btn_negative, R.id.btn_positive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_negative:
                if (mConfirmDialogListener != null) {
                    mConfirmDialogListener.onNegClick();
                }
                dismiss();
                break;
            case R.id.btn_positive:
                if (mConfirmDialogListener != null) {
                    mConfirmDialogListener.onPosClick();
                }
                dismiss();
                break;
            default:
                break;
        }
    }
}
