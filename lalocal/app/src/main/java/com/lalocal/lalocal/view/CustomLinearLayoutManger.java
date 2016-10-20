package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by xiaojw on 2016/10/20.
 */

public class CustomLinearLayoutManger extends LinearLayoutManager {
    public CustomLinearLayoutManger(Context context) {
        super(context);
    }

    public CustomLinearLayoutManger(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLinearLayoutManger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
