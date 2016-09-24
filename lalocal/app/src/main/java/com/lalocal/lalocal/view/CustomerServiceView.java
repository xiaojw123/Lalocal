package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.easemob.Constant;
import com.lalocal.lalocal.easemob.ui.ChatActivity;

/**
 * Created by xiaojw on 2016/9/7.
 * 客服
 */
public class CustomerServiceView extends TextView implements View.OnClickListener {
    private String mPhotoUrl, mTitle;


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
        setPadding(0,(int) resources.getDimension(R.dimen.dimen_size_8_dp),0,0);
        setOnClickListener(this);
    }

    public void setData(String photoUrl, String title) {
        mPhotoUrl = photoUrl;
        mTitle = title;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        if (!TextUtils.isEmpty(mPhotoUrl)) {
            intent.putExtra(Constant.ITEM_POST_URL, mPhotoUrl);
        }
        if (!TextUtils.isEmpty(mTitle)) {
            intent.putExtra(Constant.ITEM_TITLE, mTitle);
        }
        getContext().startActivity(intent);
    }
}
