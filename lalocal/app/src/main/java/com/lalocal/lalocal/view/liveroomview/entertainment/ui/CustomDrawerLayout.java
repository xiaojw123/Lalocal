package com.lalocal.lalocal.view.liveroomview.entertainment.ui;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.lalocal.lalocal.util.DensityUtil;

/**
 * Created by android on 2016/8/1.
 */
public class CustomDrawerLayout extends DrawerLayout {
    public CustomDrawerLayout(Context context) {
        this(context, null);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final ViewConfiguration configuration = ViewConfiguration
                .get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            final float x = ev.getX();
            final float y = ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_MOVE:
                    int i = DensityUtil.dip2px(getContext(), 52);
                    if(y<i){
                        return false;
                    }

                    break;
                default:

                    break;
            }
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }
}
