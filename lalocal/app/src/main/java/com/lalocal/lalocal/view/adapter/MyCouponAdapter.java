package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lalocal.lalocal.R;


/**
 * Created by xiaojw on 2016/6/21.
 * 优惠券适配器
 */
public class MyCouponAdapter extends BaseAdapter {
    Context context;

    public MyCouponAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.home_me_my_coupon_item,null);
        return convertView;
    }
}
