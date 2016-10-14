package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;

/**
 * Created by wangjie on 2016/10/14.
 */
public class ScaleRelativeLayout extends RelativeLayout {

    // 常量标记：固定宽度
    public static final int SOLID_WIDTH = -1;
    // 常量标记：固定高度
    public static final int SOLID_HEIGHT = -2;

    // 常量标记：未设置比例
    private static final float NO_SCALE = -1;

    // 宽高比
    private float mScale = NO_SCALE;
    // 固定标记
    private int mSolid = SOLID_WIDTH;

    public ScaleRelativeLayout(Context context) {
        this(context, null);
    }

    public ScaleRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScaleView);
        // 获取宽高比
        mScale = ta.getFloat(R.styleable.ScaleView_scale, NO_SCALE);
        // 获取固定标记
        mSolid = ta.getInteger(R.styleable.ScaleView_solid, SOLID_WIDTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mScale < 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        // 计算ImageView的宽度
        int width = 0;
        // 根据自定义的宽高比例，高度适当比例改变
        int height = 0;
        if (mSolid == SOLID_WIDTH) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = (int) (width / mScale);
        } else if (mSolid == SOLID_HEIGHT) {
            height = MeasureSpec.getSize(heightMeasureSpec);
            width = (int) (height * mScale);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        // 将重新定义后的宽度和高度设置为图片显示的大小
        setMeasuredDimension(width, height);

    }
}
