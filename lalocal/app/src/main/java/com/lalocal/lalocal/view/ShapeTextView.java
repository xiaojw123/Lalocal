package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * 动态设置shape的TextView
 * Created by xiaojw on 2016/10/10.
 */

public class ShapeTextView extends TextView {
    GradientDrawable drawable;

    public ShapeTextView(Context context) {
        this(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView);
        int radius = (int) a.getDimension(R.styleable.ShapeTextView_corner_radius, 0);
        int solidColor = a.getColor(R.styleable.ShapeTextView_solid_color, 0);
        int strokeColor = a.getColor(R.styleable.ShapeTextView_stroke_color, 0);
        int strokeWidth = (int) a.getDimension(R.styleable.ShapeTextView_stroke_width, 0);
        drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setColor(solidColor);
        setBackgroundDrawable(drawable);
    }

    public void setSolidColor(int color) {
        drawable.setColor(color);
    }


}
