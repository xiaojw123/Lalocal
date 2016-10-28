package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by android on 2016/10/24.
 * 修复recyclerview bug:java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position 5(offset:5).state:7
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
       // LinearLayoutManager.scrollHorizontallyBy(LinearLayoutManager.java:1031)

    }

  

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        try {
            super.onLayoutChildren(recycler, state);

        } catch (Exception e) {
            e.printStackTrace();
        }

    } }
