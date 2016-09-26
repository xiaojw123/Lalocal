package com.lalocal.lalocal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lalocal.lalocal.R;

/**
 * Created by xiaojw on 2016/7/14.
 */
public class CustomTabLayout extends LinearLayout {
    int selectedColor;
    int unSelectedColor;
    int drawableWidth;
    int drawableHeight;
    Drawable mDrawable;


    public CustomTabLayout(Context context) {
        this(context, null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        selectedColor = getResources().getColor(R.color.color_66);
        unSelectedColor = getResources().getColor(R.color.color_006);
        mDrawable = getResources().getDrawable(R.drawable.home_me_triangle);
        drawableWidth = (int) getResources().getDimension(R.dimen.triangle_drawable_width);
        drawableHeight = (int) getResources().getDimension(R.dimen.triangle_drawable_height);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isSelected()) {
            mDrawable.setBounds((getMeasuredWidth() - drawableWidth) / 2, getMeasuredHeight() - drawableHeight, (getMeasuredWidth() + drawableWidth) / 2, getMeasuredHeight());
            mDrawable.draw(canvas);
            setBackgroundColor(selectedColor);
        } else {
            setBackgroundColor(unSelectedColor);
        }
    }


}
