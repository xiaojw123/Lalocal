package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RecommendCommodityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/9/14.
 */
public class RecommendComodityAdapter extends BaseAdapter {

    private Context mContext;
    private List<RecommendCommodityBean> mList = new ArrayList<>();

    public RecommendComodityAdapter(Context context, List<RecommendCommodityBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_recommend_product_gridview_item, null);

        }
        return convertView;
    }


    private class ViewHolder {

    }
}
