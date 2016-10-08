package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.lalocal.lalocal.R;


/**
 * Created by Administrator on 2016/3/19.
 */
public class RoundProgress extends View {

    private int roundColor;
    private int roundProgressColor;
    private float roundWidth;
    private int percentTextColor;
    private float percentTextSize;

    private int progress = 0;

    public RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //加载自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgress);
        roundColor = typedArray.getColor(R.styleable.RoundProgress_roundColor, Color.GRAY);
        roundProgressColor = typedArray.getColor(R.styleable.RoundProgress_roundProgressColor, Color.RED);

        roundWidth = typedArray.getDimension(R.styleable.RoundProgress_roundWidth, 10);
        percentTextColor = typedArray.getColor(R.styleable.RoundProgress_percentTextColor, Color.BLUE);
        percentTextSize = typedArray.getDimension(R.styleable.RoundProgress_percentTextSize, 10);
        typedArray.recycle();
    }

    /**
     * 第一步：画圆
     * 第二步：画中间百分比文字
     * 第三步：画一个弧形进度圈
     * 第四步：让这个弧形圈动起来
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //1:画圆
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(roundColor);
        paint.setStrokeWidth(roundWidth);
        paint.setStyle(Paint.Style.STROKE);
        int center = getWidth() / 2;
        int radius = (int) (center - roundWidth / 2);
        canvas.drawCircle(center, center, radius, paint);


        //2:画中间百分比文字
        paint.setColor(percentTextColor);
        paint.setTextSize(percentTextSize);
        paint.setFakeBoldText(true);
        paint.setStrokeWidth(0);
        float textWidth = paint.measureText(progress + "%");
        canvas.drawText(progress + "%", center - textWidth / 2, center + percentTextSize / 2, paint);

        //3:画一个弧形进度圈
        paint.setColor(roundProgressColor);
        paint.setStrokeWidth(roundWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        SweepGradient sweepGradient = new SweepGradient(getWidth()/2 , getHeight()/2, new int[]{Color.YELLOW, Color.RED}, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(85,getWidth()/2, getHeight()/2);
        sweepGradient.setLocalMatrix(matrix);
        paint.setShader(sweepGradient);

        float sweepAngle = progress * 360 / 100;
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(oval, 90, sweepAngle, false, paint);
    }


    /**
     * 设置进度，进度是多少，就画多少
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress >= 100) {
            this.progress = 100;
        }
        this.progress = progress;
        postInvalidate();
    }
}
