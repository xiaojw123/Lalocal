package com.lalocal.lalocal.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lalocal.lalocal.model.ProductDetailsResultBean;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by android on 2016/11/24.
 */
public class CustomGridLayout extends GridLayout {
    public CustomGridLayout(Context context) {
        this(context, null);
    }
    public CustomGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGridLayout(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }
    private void init() {
        setColumnCount(2);
        // 开启布局动画效果
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(100);
        setLayoutTransition(transition);
    }

    public  void getData(List<ProductDetailsResultBean> list){
        for (ProductDetailsResultBean str:list){
            LinearLayout layout=new LinearLayout(getContext());
            layout.setGravity(Gravity.CENTER);
            ImageView tv=new ImageView(getContext());
            DrawableUtils.displayImg(getContext(),tv,str.orderUrl);
            layout.addView(tv);
            setListener(layout);
            addView(layout);
        }



    }

    private void setListener(LinearLayout layout) {
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick();
                }
            }
        });

    }


    public static interface OnItemClickListener {
        public void onItemClick();
    }
    OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
