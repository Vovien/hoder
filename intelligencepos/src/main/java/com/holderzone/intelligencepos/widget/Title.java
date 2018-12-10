package com.holderzone.intelligencepos.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tcw on 2017/5/26.
 */

public class Title extends LinearLayout {

    @BindView(R.id.ll_return)
    LinearLayout mLlReturn;
    @BindView(R.id.iv_return)
    ImageView mIvReturn;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_menu)
    LinearLayout mLlMenu;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.tv_menu)
    TextView mTvMenu;
    @BindView(R.id.view_line)
    View mView;
    @BindView(R.id.rl_layout_title)
    RelativeLayout mRelativeLayout;

    private OnReturnClickListener mOnReturnClickListener;

    private OnMenuClickListener mOnMenuClickListener;

    public Title(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.include_title, this);

        ButterKnife.bind(this);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IncludeTitle);

            // return
            switch (typedArray.getInt(R.styleable.IncludeTitle_returnVisibility, 1)) {
                case -1:
                    mIvReturn.setVisibility(View.GONE);
                    break;
                case 1:
                    mIvReturn.setVisibility(View.VISIBLE);
                    Drawable drawable = typedArray.getDrawable(R.styleable.IncludeTitle_returnRes);
                    if (drawable != null) {
                        mIvReturn.setImageDrawable(drawable);
                    }
                    break;
                default:
                    mIvReturn.setVisibility(View.GONE);
                    break;
            }

            // title
            String titleText = typedArray.getString(R.styleable.IncludeTitle_title);
            if (titleText != null) {
                mTvTitle.setText(titleText);
            }

            // menu res
            switch (typedArray.getInt(R.styleable.IncludeTitle_menuResVisibility, 1)) {
                case -1:
                    mIvMenu.setVisibility(View.GONE);
                    break;
                case 1:
                    mIvMenu.setVisibility(View.VISIBLE);
                    Drawable drawable = typedArray.getDrawable(R.styleable.IncludeTitle_menuRes);
                    if (drawable != null) {
                        mIvMenu.setImageDrawable(drawable);
                    }
                    break;
                default:
                    mIvMenu.setVisibility(View.GONE);
                    break;
            }

            // menu text
            switch (typedArray.getInt(R.styleable.IncludeTitle_menuTextVisibility, 1)) {
                case -1:
                    mTvMenu.setVisibility(View.GONE);
                    break;
                case 1:
                    mTvMenu.setVisibility(View.VISIBLE);
                    String menuText = typedArray.getString(R.styleable.IncludeTitle_menuText);
                    if (menuText != null) {
                        mTvMenu.setText(menuText);
                    }
                    break;
                default:
                    mTvMenu.setVisibility(View.GONE);
                    break;
            }

            // release resource
            typedArray.recycle();
        }

        RxView.clicks(mLlReturn).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            if (mOnReturnClickListener != null) {
                mOnReturnClickListener.onReturnClick();
            }
        });
        RxView.clicks(mLlMenu).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick();
            }
        });
    }

    /**
     * 设置Title背景颜色
     *
     * @param color
     */
    public void setBackGroundColor(int color) {
        if (mRelativeLayout != null) {
            mRelativeLayout.setBackgroundResource(color);
        }
    }

    public void setReturnVisibility(boolean visibility) {
        mIvReturn.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    public void setViewVisibility(boolean viewVisibility) {
        mView.setVisibility(viewVisibility ? VISIBLE : GONE);
    }

    public void setReturnDrawableRes(@DrawableRes int drawableRes) {
        mIvReturn.setImageResource(drawableRes);
    }

    public void setOnReturnClickListener(OnReturnClickListener onReturnClickListener) {
        mOnReturnClickListener = onReturnClickListener;
    }

    public void setTitleText(CharSequence titleText) {
        mTvTitle.setText(titleText);
    }

    public void setTitleTextColor(int colorId) {
        mTvTitle.setTextColor(ContextCompat.getColor(getContext(), colorId));
    }

    public void setTitleTextSize(int textSize) {
        mTvTitle.setTextSize(textSize);
    }

    public void setMenuVisibility(boolean visibility) {
        mTvMenu.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    public void setMenuText(CharSequence menuText) {
        mTvMenu.setText(menuText);
    }

    public void setMenuTextColor(int colorId) {
        mTvMenu.setTextColor(ContextCompat.getColor(getContext(), colorId));
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }

    public interface OnReturnClickListener {
        void onReturnClick();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
    }
}
