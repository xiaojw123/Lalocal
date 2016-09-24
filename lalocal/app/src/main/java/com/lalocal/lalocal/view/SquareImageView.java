package com.lalocal.lalocal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wangjie on 2016/9/19.
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        // 长宽一致
        int viewHeight = viewWidth;
        // 将计算出的宽度和高度设置为图片显示的大小
        setMeasuredDimension(viewWidth, viewHeight);
    }
}
