package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by wangjie on 2016/9/21.
 */
public class WrapContentRecyclerView extends RecyclerView {
    public WrapContentRecyclerView(Context context) {
        super(context);
    }

    public WrapContentRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        widthSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthSpec), MeasureSpec.EXACTLY);
        heightSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightSpec), MeasureSpec.EXACTLY);

        super.onMeasure(widthSpec, heightSpec);
    }
}
