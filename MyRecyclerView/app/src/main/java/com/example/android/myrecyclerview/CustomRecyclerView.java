package com.example.android.myrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by android on 2016/9/23.
 */
public class CustomRecyclerView extends RecyclerView {
    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //触摸事件
    int downX;
    int downY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
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
                if(Math.abs(dX)>Math.abs(dY)){
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

}
