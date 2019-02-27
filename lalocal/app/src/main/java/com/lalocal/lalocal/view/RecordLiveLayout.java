package com.lalocal.lalocal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangjie on 2017/1/5.
 */

public class RecordLiveLayout extends RelativeLayout {

    @BindView(R.id.img_record)
    RoundedImageView imgRecord;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.img_live)
    RoundedImageView imgLive;
    @BindView(R.id.tv_live)
    TextView tvLive;
    @BindView(R.id.container)
    RelativeLayout container;

    // 布局视图
    private View mRootView;


    public RecordLiveLayout(Context context) {
        this(context, null);
    }

    public RecordLiveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordLiveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 视图
        mRootView = LayoutInflater.from(context).inflate(R.layout.record_live_layout, null);
        // ButterKnife关联控件
        ButterKnife.bind(mRootView);
    }

    /**
     * 渐变进入
     */
    public void fadeIn() {

    }

    /**
     * 渐变出去
     */
    public void fadeOut() {

    }

    /**
     * 消失
     */
    public void dismiss() {

    }
}

