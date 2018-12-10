package com.holderzone.intelligencepos.widget.popupwindow.impl;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TableMergedPopup extends RelativePopupWindow {

    public static final int ITEM_TYPE_MERGE_TABLE = 0;
    public static final int ITEM_TYPE_SPLIT_TABLE = 1;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(int itemType);
    }

    public TableMergedPopup(Context context) {
        View view = null;
        setContentView(view = LayoutInflater.from(context).inflate(R.layout.popwindow_table_merge, null));
        ButterKnife.bind(this, view);

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Disable default animation for circular reveal
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setAnimationStyle(0);
//        }
        if (context instanceof OnItemClickListener) {
            mOnItemClickListener = (OnItemClickListener) context;
        }
    }

    @Override
    public void showOnAnchor(@NonNull View anchor, int vertPos, int horizPos, int x, int y) {
        super.showOnAnchor(anchor, vertPos, horizPos, x, y);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            circularReveal(anchor);
//        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal(@NonNull final View anchor) {
        final View contentView = getContentView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                final int[] myLocation = new int[2];
                final int[] anchorLocation = new int[2];
                contentView.getLocationOnScreen(myLocation);
                anchor.getLocationOnScreen(anchorLocation);
                final int cx = anchorLocation[0] - myLocation[0] + anchor.getWidth() / 2;
                final int cy = anchorLocation[1] - myLocation[1] + anchor.getHeight() / 2;

                contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                final int dx = Math.max(cx, contentView.getMeasuredWidth() - cx);
                final int dy = Math.max(cy, contentView.getMeasuredHeight() - cy);
                final float finalRadius = (float) Math.hypot(dx, dy);
                Animator animator = ViewAnimationUtils.createCircularReveal(contentView, cx, cy, 0f, finalRadius);
                animator.setDuration(200);
                animator.start();
            }
        });
    }

    @OnClick({R.id.ll_popup_window_merge, R.id.ll_popup_window_split})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_popup_window_merge:
                if (mOnItemClickListener != null) {
                    dismiss();
                    mOnItemClickListener.onItemClick(ITEM_TYPE_MERGE_TABLE);
                }
                break;
            case R.id.ll_popup_window_split:
                if (mOnItemClickListener != null) {
                    dismiss();
                    mOnItemClickListener.onItemClick(ITEM_TYPE_SPLIT_TABLE);
                }
                break;
            default:
                break;
        }
    }
}