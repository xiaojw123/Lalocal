package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.RouteDetailActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.util.CommonUtil;

/**
 * Created by xiaojw on 2016/9/7.
 * 客服
 */
public class CustomerServiceView extends TextView implements View.OnClickListener {
    public CustomerServiceView(Context context) {
        this(context, null);
    }

    public CustomerServiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerServiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        Resources resources = getContext().getResources();
        setText(resources.getString(R.string.service));
        Drawable drawable = resources.getDrawable(R.drawable.serviceicon);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(null, drawable, null, null);
        setPadding(0, (int) resources.getDimension(R.dimen.dimen_size_8_dp), (int) resources.getDimension(R.dimen.dimen_size_15_dp), 0);
        setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        final Context context = v.getContext();
        if (context instanceof RouteDetailActivity) {
            // 进入主页面
            MobHelper.sendEevent(context, MobEvent.DESTINATION_ROUTE_SERVICE);
        }
        CommonUtil.startCustomService(context);
    }
}
