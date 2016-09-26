package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;

/**
 * Created by xiaojw on 2016/9/12.
 */
public class CustomTextView extends TextView {
    //drawable宽高
    int drawableWidth, drawableHeight;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        drawableWidth = (int) a.getDimension(R.styleable.CustomTextView_drawable_width, 0);
        drawableHeight = (int) a.getDimension(R.styleable.CustomTextView_drawable_height, 0);
        if (drawableWidth > 0 && drawableHeight > 0) {
            Drawable[] drawables = getCompoundDrawables();
            for (Drawable drawable : drawables) {
                if (drawable != null) {
                    AppLog.print("drawable___"+drawable);
                    drawable.setBounds(0, 0, drawableWidth, drawableHeight);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
