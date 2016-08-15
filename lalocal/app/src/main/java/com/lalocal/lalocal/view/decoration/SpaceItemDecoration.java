package com.lalocal.lalocal.view.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lalocal.lalocal.util.AppLog;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildLayoutPosition(view);
        int count = parent.getAdapter().getItemCount();
        AppLog.print("pos___" + pos + "___childsize__" + count);
        if (pos != 0) {
            outRect.top = space;
            if (pos == count - 1) {
                outRect.bottom = space;
            }
        }
    }
}