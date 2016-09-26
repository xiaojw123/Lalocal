package com.lalocal.lalocal.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.lalocal.lalocal.R;

/**
 * Created by wangjie on 2016/9/13.
 */
public class Dots extends LinearLayout {

    // 点个数
    private int mAmount = -1;
    // 选中的下表
    private int mSelected = -1;

    private Context mContext;

    public Dots(Context context, int amount, int selected) {
        this(context, null, amount, selected);
    }

    public Dots(Context context, AttributeSet attrs, int amount, int selected) {
        this(context, attrs, 0, amount, selected);
    }

    public Dots(Context context, AttributeSet attrs, int defStyleAttr, int amount, int selected) {
        super(context, attrs, defStyleAttr);

        this.mAmount = amount;
        this.mSelected = selected;

        // 初始化视图
        initView();
    }

    /**
     * 初始化试图
     */
    private void initView() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_dot_normal);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        // 水平居中
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        for (int i = 0; i < mAmount; i++) {
            View btn = new View(mContext);
            this.setOrientation(HORIZONTAL);
            LayoutParams params = new LayoutParams(bitmap.getWidth(), bitmap.getHeight());
            params.setMargins(4, 15, 4, 15);
            btn.setLayoutParams(params);
            if (i == mSelected) {
                btn.setBackgroundResource(R.drawable.icon_dot_normal);
            } else {
                btn.setBackgroundResource(R.drawable.icon_dot_selected);
            }
            this.addView(btn);
        }
        this.setLayoutParams(layoutParams);
    }

    /**
     * 设置点的个数
     * @param amount
     */
    public void setDotAmountAndSelected(int amount, int selected) {
        this.mAmount = amount;
        this.mSelected = selected;
        // 更新控件
        initView();
    }


}
