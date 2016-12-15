package com.lalocal.lalocal.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.me.LPEmailBound2Activity;
import com.lalocal.lalocal.me.LPEmailBoundActivity;
import com.lalocal.lalocal.me.LPasswordForget1Activity;
import com.lalocal.lalocal.me.LPasswordForget2Activity;

/**
 * Created by xiaojw on 2016/7/4.
 * APP通用标题栏
 * lineVisible  type; boolen 设置标题分界线是否显示
 * title_name   type:string 设置标题名称
 */
public class CustomTitleView extends FrameLayout implements View.OnClickListener {
    onBackBtnClickListener listener, cListener;
    Context context;
    TextView title_tv;
    boolean isFinishEanble = true;

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleView);
        String name = a.getString(R.styleable.CustomTitleView_title_name);
        boolean lineVisible = a.getBoolean(R.styleable.CustomTitleView_lineVisible, true);
        boolean backVisible = a.getBoolean(R.styleable.CustomTitleView_backVisible, true);
        a.recycle();
        LayoutInflater.from(context).inflate(R.layout.custom_title_layout, this);
        ImageView backImg = (ImageView) findViewById(R.id.titleview_back_img);
        title_tv = (TextView) findViewById(R.id.titleview_title_tv);
        View line = findViewById(R.id.titleview_line);
        backImg.setOnClickListener(this);
        if (!lineVisible) {
            line.setVisibility(GONE);
        }
        if (!backVisible) {
            backImg.setVisibility(GONE);
        }
        title_tv.setText(name);
    }

    public void setFisishEanble(boolean isFinishEanble) {
        this.isFinishEanble = isFinishEanble;
    }

    public void setOnBackClickListener(onBackBtnClickListener listener) {
        this.listener = listener;

    }

    public void setOnCustomClickLister(onBackBtnClickListener listener) {
        cListener = listener;
    }

    public void setTitle(String titleName) {
        title_tv.setText(titleName);
    }

    @Override
    public void onClick(View v) {
        if (cListener != null) {
            cListener.onBackClick();
            return;
        }
        try {
            if (listener != null) {
                listener.onBackClick();
            }
            if (isFinishEanble) {
                Activity a = (Activity) context;
                if (a instanceof LPEmailBoundActivity) {
                    MobHelper.sendEevent(context, MobEvent.BINDING_BACK_01);
                } else if (a instanceof LPEmailBound2Activity) {
                    MobHelper.sendEevent(context, MobEvent.BINDING_BACK_02);
                } else if (a instanceof LPasswordForget1Activity) {
                    MobHelper.sendEevent(context, MobEvent.LOGIN_FORGET_BACK_01);
                } else if (a instanceof LPasswordForget2Activity) {
                    MobHelper.sendEevent(context, MobEvent.LOGIN_FORGET_BACK_02);
                }
                a.finish();
            }
        } catch (Exception e) {

        }
    }

    public interface onBackBtnClickListener {
        void onBackClick();
    }

}
