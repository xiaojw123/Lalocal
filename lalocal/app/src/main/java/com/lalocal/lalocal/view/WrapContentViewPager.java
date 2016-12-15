package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wangjie on 2016/9/13.
 */
public class WrapContentViewPager extends ViewPager {

    public WrapContentViewPager(Context context) {
        super(context);
    }

    public WrapContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static final String TAG = "WrapContentViewPager";


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
            Log.d(TAG, "item "  + i + ": the size is " + getChildCount() + "---the height is " + h);
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED);
        Log.d(TAG, "the height is " + heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setPageMargin(int marginPixels) {
        super.setPageMargin(marginPixels);
    }
}
