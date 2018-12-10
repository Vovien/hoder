package com.holderzone.intelligencepos.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 透明分割块儿，只支持GridLayoutManager，根据布局自动设置等间距decoration
 * Created by tcw on 2017/7/10.
 */

public class SmartGridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private boolean mIncludeEdge;
    private int mOrientation;
    private int mSpanCount;
    private float mDecoWidth;
    private float mDecoHeight;
    private float mDecoWidthOrHeight;

    public SmartGridSpacingItemDecoration() {
        this(true);
    }

    public SmartGridSpacingItemDecoration(float decoWidthOrHeight) {
        this(true, decoWidthOrHeight);
    }

    public SmartGridSpacingItemDecoration(boolean includeEdge) {
        this(includeEdge, 0);
    }

    public SmartGridSpacingItemDecoration(boolean includeEdge, float decoWidthOrHeight) {
        mIncludeEdge = includeEdge;
        mDecoWidthOrHeight = decoWidthOrHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mSpanCount == 0) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            mSpanCount = layoutManager.getSpanCount();
            mOrientation = layoutManager.getOrientation();
        }

        if (mOrientation == GridLayoutManager.VERTICAL) {
            if (mDecoWidth == 0) {
                int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                view.measure(widthSpec, heightSpec);
                if (mIncludeEdge) {
                    mDecoWidth = (parent.getWidth() - mSpanCount * view.getMeasuredWidth()) * 1.0f / (mSpanCount + 1);
                } else {
                    mDecoWidth = (parent.getWidth() - mSpanCount * view.getMeasuredWidth()) * 1.0f / (mSpanCount - 1);
                }
            }
            if (mDecoHeight == 0) {
                mDecoHeight = mDecoWidthOrHeight > 0 ? mDecoWidthOrHeight : mDecoWidth;
            }
            int position = parent.getChildAdapterPosition(view); // item position
            if (mIncludeEdge) {
                outRect.left = (int) (mDecoWidth * (mSpanCount - position % mSpanCount) * 1.0f / mSpanCount);
                if (position < mSpanCount) {
                    outRect.top = (int) mDecoHeight;
                }
                outRect.bottom = (int) mDecoHeight;
            } else {
                outRect.left = (int) (mDecoWidth * (position % mSpanCount * 1.0f / (mSpanCount - 1)));
                if (position >= mSpanCount) {
                    outRect.top = (int) mDecoHeight;
                }
            }
        } else {
            int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(widthSpec, heightSpec);
            if (mDecoHeight == 0) {
                if (mIncludeEdge) {
                    mDecoHeight = (parent.getHeight() - mSpanCount * view.getMeasuredHeight()) * 1.0f / (mSpanCount + 1);
                } else {
                    mDecoHeight = (parent.getHeight() - mSpanCount * view.getMeasuredHeight()) * 1.0f / (mSpanCount - 1);
                }
            }
            int position = parent.getChildAdapterPosition(view); // item position
            if (mDecoWidth == 0) {
                mDecoWidth = mDecoWidthOrHeight > 0 ? mDecoWidthOrHeight : mDecoHeight;
            }
            if (mIncludeEdge) {
                outRect.top = (int) (mDecoHeight * (mSpanCount - position % mSpanCount) * 1.0f / mSpanCount);
                if (position < mSpanCount) {
                    outRect.left = (int) mDecoWidth;
                }
                outRect.right = (int) mDecoWidth;
            } else {
                outRect.top = (int) (mDecoHeight * (position % mSpanCount * 1.0f / (mSpanCount - 1)));
                if (position >= mSpanCount) {
                    outRect.left = (int) mDecoWidth;
                }
            }
        }
    }
}
