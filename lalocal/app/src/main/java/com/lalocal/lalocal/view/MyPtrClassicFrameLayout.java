package com.lalocal.lalocal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.chanven.lib.cptr.PtrClassicFrameLayout;

/**
 * Created by wangjie on 2016/9/23.
 */
public class MyPtrClassicFrameLayout extends PtrClassicFrameLayout {

    private int downX;
    private int downY;

    private float interX;
    private float interY;

    private float dispatchX;
    private float dispatchY;

    public MyPtrClassicFrameLayout(Context context) {
        super(context);
    }

    public MyPtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
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
//                AppLog.i("slidd", "ptr-down:" + dispatchX + "," + dispatchY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float curX = e.getX();
//                float curY = e.getY();
//                float deltaX = Math.abs(curX - dispatchX);
//                float deltaY = Math.abs(curY - dispatchY);
//                AppLog.i("slidd", "ptr-move:" + deltaX + "," + deltaY);
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
//                AppLog.i("slidd", "ptr-inter-down:" + interX + "," + interY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float curX = e.getX();
//                float curY = e.getY();
//                float deltaX = Math.abs(curX - interX);
//                float deltaY = Math.abs(curY - interY);
//                AppLog.i("slidd", "ptr-inter-move:" + deltaX + "," + deltaY);
//                if (deltaX > 5 || deltaX > deltaY) {
//                    return false;
//                }
//                interX = curX;
//                interY = curY;
//                break;
//        }
//        return super.onInterceptTouchEvent(e);
//    }

    public static boolean disallowInterceptTouchEvent = false;


    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dispatchX = e.getX();
                dispatchY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = e.getX();
                float curY = e.getY();
                float deltaX = Math.abs(curX - dispatchX);
                float deltaY = curY - dispatchY;

                if (deltaY > deltaX) {
                    this.requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                this.requestDisallowInterceptTouchEvent(false);
                break;
        }

        if (disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }

}
