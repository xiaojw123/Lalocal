package com.lalocal.lalocal.view;

import android.content.Context;
import android.util.AttributeSet;

import android.view.View;
import android.widget.ScrollView;

/**
 * Created by lenovo on 2016/6/22.
 */
public class MyScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {

        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {

            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }


    //是否要其弹性滑动
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (scrollByListener != null) {
            scrollByListener.onScrollBy(deltaX, deltaY, scrollX, scrollY);
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, 0, 0, isTouchEvent);
    }

    //接口
    public interface ScrollViewListener {

        void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy);

    }

    public void setScrollViewListener(ScrollViewListener listener) {
        scrollViewListener = listener;
    }

    private ScrollByListener scrollByListener;

    private void setScrollByListener(ScrollByListener scrollByListener) {
        this.scrollByListener = scrollByListener;
    }

    public interface ScrollByListener {
        void onScrollBy(int deltaX, int deltaY, int scrollX, int scrollY);
    }
}
