package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by wangjie on 2016/9/23.
 */
public class MyRecyclerView extends RecyclerView {

    private int downX;
    private int downY;

    private float interX;
    private float interY;

    private float dispatchX;
    private float dispatchY;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //触摸事件
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX=(int) ev.getX();
//                downY = (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int moveX=(int) ev.getX();
//                int dX=moveX-downX;
//                int moveY=(int) ev.getY();
//                int dY=moveY-downY;
//                if(Math.abs(dX)>Math.abs(dY)){
//                    return true;
//                }
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent e) {
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                dispatchX = e.getX();
//                dispatchY = e.getY();
//                AppLog.i("slidd", "recycler-down:" + dispatchX + "," + dispatchY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float curX = e.getX();
//                float curY = e.getY();
//                float deltaX = Math.abs(curX - dispatchX);
//                float deltaY = Math.abs(curY - dispatchY);
//                AppLog.i("slidd", "recycler-move:" + deltaX + "," + deltaY);
//                if (deltaX > 5 || deltaX > deltaY) {
//                    return false;
//                }
//                dispatchX = curX;
//                dispatchY = curY;
//                break;
//        }
//        return super.dispatchTouchEvent(e);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                interX = e.getX();
//                interY = e.getY();
//                AppLog.i("slidd", "recycler-inter-down:" + interX + "," + interY);
//                return false;
////                break;
//            case MotionEvent.ACTION_MOVE:
//                float curX = e.getX();
//                float curY = e.getY();
//                float deltaX = Math.abs(curX - interX);
//                float deltaY = Math.abs(curY - interY);
//                AppLog.i("slidd", "recycler-inter-move:" + deltaX + "," + deltaY);
//                if (deltaX > 5 || deltaX > deltaY) {
//                    AppLog.i("slidd", "enter");
//                    return false;
//                }
//                interX = curX;
//                interY = curY;
//                break;
//        }
//        return super.onInterceptTouchEvent(e);
//    }
}
