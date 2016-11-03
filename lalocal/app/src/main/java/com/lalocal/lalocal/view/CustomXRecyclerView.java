package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Created by android on 2016/10/22.
 */
public class CustomXRecyclerView extends XRecyclerView {
    public CustomXRecyclerView(Context context) {
        super(context);
    }
    public CustomXRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //截断事件
    private int downX;
    private int downY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX=(int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX=(int) ev.getX();
                int dX=moveX-downX;
                int moveY=(int) ev.getY();
                int dY=moveY-downY;
                if(Math.abs(dX)<Math.abs(dY)){
                    return true;
                }
                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    public int getDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        View firstVisibleItem = this.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        int recyclerViewHeight = this.getHeight();
        int itemHeight = firstVisibleItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
        return (itemCount - firstItemPosition - 1) * itemHeight - recyclerViewHeight + firstItemBottom;
    }

    public int getScrolledDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        View firstVisibleItem = this.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibleItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }
}
