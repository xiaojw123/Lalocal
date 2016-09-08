package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by android on 2016/9/1.
 */
public class BannerPagerAdapter extends PagerAdapter {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图像列表
     */
    private List<GridView> list;


    public BannerPagerAdapter(Context context, List<GridView> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = list.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}