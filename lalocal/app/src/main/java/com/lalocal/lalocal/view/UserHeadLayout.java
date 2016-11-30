package com.lalocal.lalocal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by xiaojw on 2016/11/14.
 */

public class UserHeadLayout extends FrameLayout {
    Paint paint;
    public UserHeadLayout(Context context) {
        this(context,null);
    }

    public UserHeadLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UserHeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);                       //设置画笔为无锯齿
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth((float) 3.0);              //线宽
        paint.setStyle(Paint.Style.STROKE);//设置画笔颜色
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable=getBackground();
        canvas.drawColor(Color.WHITE);                  //白色背景
        RectF oval=new RectF();                     //RectF对象
        oval.left=0;                              //左边
        oval.top=200;                                   //上边
        oval.right=getMeasuredWidth();                             //右边
        oval.bottom=300;                                //下边
        canvas.drawArc(oval, 225, 90, false, paint);    //绘制圆弧
        drawable.draw(canvas);
        super.onDraw(canvas);
    }
}
