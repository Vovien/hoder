package com.holderzone.intelligencepos.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 透明分割块，只支持GridLayout
 * Created by tcw on 2017/5/12.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int dividerWidth;
    private int dividerHeight;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int dividerWidth, int dividerHeight, boolean includeEdge) {
        this.spanCount = spanCount;
        this.dividerWidth = dividerWidth;
        this.dividerHeight = dividerHeight;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = dividerWidth - column * dividerWidth / spanCount; // dividerWidth - column * ((1f / spanCount) * dividerWidth)
            outRect.right = (column + 1) * dividerWidth / spanCount; // (column + 1) * ((1f / spanCount) * dividerWidth)

            if (position < spanCount) { // top edge
                outRect.top = dividerHeight;
            }
            outRect.bottom = dividerHeight; // item bottom
        } else {
            outRect.left = column * dividerWidth / spanCount; // column * ((1f / spanCount) * dividerWidth)
            outRect.right = dividerWidth - (column + 1) * dividerWidth / spanCount; // dividerWidth - (column + 1) * ((1f /    spanCount) * dividerWidth)
            if (position >= spanCount) {
                outRect.top = dividerHeight; // item top
            }
        }
    }
}