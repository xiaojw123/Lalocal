package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * Created by xiaojw on 2016/10/17.
 */

public class LCustomLayout extends FrameLayout {
    public LCustomLayout(Context context) {
        this(context, null);
    }

    public LCustomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LCustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LCustomLayout);
        String title = a.getString(R.styleable.LCustomLayout_l_title);
        String des = a.getString(R.styleable.LCustomLayout_l_des);
        boolean titleVisible = a.getBoolean(R.styleable.LCustomLayout_l_titleVisible, false);
        a.recycle();
        LayoutInflater.from(context).inflate(R.layout.common_login_bg, this);
        TextView titleTv = (TextView) findViewById(R.id.common_title_tv);
        TextView desTv = (TextView) findViewById(R.id.common_des_tv);
        Button backBtn = (Button) findViewById(R.id.common_back_btn);
        titleTv.setText(title);
        desTv.setText(des);
        if (titleVisible) {
            backBtn.setVisibility(VISIBLE);
        } else {
            backBtn.setVisibility(GONE);
        }


    }

}
