package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lalocal.lalocal.R;

/**
 * Created by xiaojw on 2016/10/11.
 */

public class ShapeEditText extends EditText{

    GradientDrawable drawable;
    public ShapeEditText(Context context) {
        this(context,null);
    }

    public ShapeEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
