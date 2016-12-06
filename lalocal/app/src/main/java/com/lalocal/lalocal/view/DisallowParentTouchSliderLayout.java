package com.lalocal.lalocal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;


/**
 * Created by wangjie on 2016/9/23.
 */
public class DisallowParentTouchSliderLayout extends SliderLayout {

    private ViewGroup parent;

    public DisallowParentTouchSliderLayout(Context context) {
        super(context);
    }

    public DisallowParentTouchSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisallowParentTouchSliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setNestParent(ViewGroup parent) {
        this.parent = parent;
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
}
