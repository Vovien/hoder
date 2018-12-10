package com.holderzone.intelligencepos.widget.recyclerview.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017-3-28.
 * 设置RecyclerView的item之间的高度
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildPosition(view) != 0) {
            outRect.top = space;
        }
    }
}
