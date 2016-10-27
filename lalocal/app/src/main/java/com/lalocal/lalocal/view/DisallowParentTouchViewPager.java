package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wangjie on 2016/9/23.
 */
public class DisallowParentTouchViewPager extends ViewPager {

    private ViewGroup parent;

    public DisallowParentTouchViewPager(Context context) {
        super(context);
    }

    public DisallowParentTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNestParent(ViewGroup parent) {
        this.parent = parent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                AppLog.i("move-", "down : x = " + mX + ", y = " + mY);
//                mX = ev.getX();
//                mY = ev.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//                float curX = ev.getX();
//                float curY = ev.getY();
//                float deltaX = Math.abs(curX - mX);
//                float deltaY = Math.abs(curY - mY);
//                AppLog.i("move-", "("+curX+", "+curY+")x = " + deltaX + ", y = " + deltaY);
//                if (deltaX > deltaY) {
//                    AppLog.i("move-", "true");
//                    return true;
//                } else {
//                    AppLog.i("move-", "false");
//                    return false;
//                }
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        AppLog.i("move-", "onTouch--("+ev.getX()+","+ev.getY()+")");
//        boolean result = super.onTouchEvent(ev);
//        AppLog.i("move-", "onTouch--" + result);
//        return result;
//    }
}
