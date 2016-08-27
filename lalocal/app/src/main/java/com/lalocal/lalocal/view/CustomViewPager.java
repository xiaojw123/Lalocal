package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class CustomViewPager extends ViewPager {

    private PagerCallBack mPagerCallBack;
    private int lastValue = -1;
    private boolean left, right;
    private boolean isScrolling;

    public CustomViewPager(Context context) {
        this(context, null);

    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public void init() {
//        setOnPageChangeListener(listener);
        addOnPageChangeListener(listener);
    }

    ;

    public void setCallBack(PagerCallBack pagerCallBack) {
        mPagerCallBack = pagerCallBack;
    }


    private OnPageChangeListener listener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            if (mPagerCallBack != null) {
                mPagerCallBack.getCurrentPosition(arg0);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            //arg0: 当前页面，点击滑动的页面  arg1:当前页面偏移百分比  arg2:当前页面偏移像素位置
            if (isScrolling) {
                if (lastValue < arg2) {
                    left = false;
                    right = true;
                } else if (lastValue > arg2) {
                    right = false;
                    left = true;
                } else {
                    left = right = false;
                }
            }
            lastValue = arg2;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            //arg0:1.滑动中   2.滑动完成   0.什么都没做
            if (arg0 == 1) {
                isScrolling = true;
            } else {
                isScrolling = false;
            }
            if (arg0 == 2) {
                //滑动完成
                if (mPagerCallBack != null) {
                    mPagerCallBack.changeView(left, right);
                }
                left = right = false;
            }
        }
    };


    public interface PagerCallBack {
        public void getCurrentPosition(int pos);

        public void changeView(boolean left, boolean right);
    }

}
