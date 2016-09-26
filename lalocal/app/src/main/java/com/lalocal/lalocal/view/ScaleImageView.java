package com.lalocal.lalocal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wangjie on 2016/9/13.
 */
public class ScaleImageView extends ImageView {

    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getDrawable() == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        // 计算出ImageView的宽度
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        // 根据图片长宽比例算出ImageView的高度
        int viewHeight = viewWidth * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
        // 将计算出的宽度和高度设置为图片显示的大小
        setMeasuredDimension(viewWidth, viewHeight);
    }
}
