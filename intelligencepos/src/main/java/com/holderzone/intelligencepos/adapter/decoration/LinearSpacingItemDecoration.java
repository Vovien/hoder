package com.holderzone.intelligencepos.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 透明分割块儿，只支持LinearLayoutManager.VERTICAL
 * Created by tcw on 2017/5/12.
 */

public class LinearSpacingItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 间隔距离，单位px
     */
    private int mSpacing;

    /**
     * 边界是否有间隔
     */
    private boolean mIncludeEdge;

    /**
     * 列表的方向
     * LinearLayoutManager.VERTICAL
     * LinearLayoutManager.HORIZONTAL
     */
    private int mOrientation;


    public LinearSpacingItemDecoration(int spacing, boolean includeEdge) {
        this(spacing, includeEdge, LinearLayoutManager.VERTICAL);
    }

    public LinearSpacingItemDecoration(int spacing, boolean includeEdge, int orientation) {
        mSpacing = spacing;
        mIncludeEdge = includeEdge;
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            if (mIncludeEdge) {
                if (position == 0) {
                    outRect.top = mSpacing;
                }
                outRect.bottom = mSpacing;
            } else {
                if (position > 0) {
                    outRect.set(0, mSpacing, 0, 0);
                }
            }
        } else {
            if (mIncludeEdge) {
                if (position == 0) {
                    outRect.left = mSpacing;
                }
                outRect.right = mSpacing;
            } else {
                if (position > 0) {
                    outRect.set(mSpacing, 0, 0, 0);
                }
            }
        }
    }
}
