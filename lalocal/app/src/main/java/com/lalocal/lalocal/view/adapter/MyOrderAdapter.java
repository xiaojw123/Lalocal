package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * Created by xiaojw on 2016/6/21.
 */
public class MyOrderAdapter extends BaseAdapter {
    Context context;

    public MyOrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
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
        TextView textView = new TextView(context);
        textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText("订单" + position);
        textView.setTextColor(context.getResources().getColor(R.color.color_7ed321));
        textView.setTextSize(context.getResources().getDimension(R.dimen.text_size_14_sp));
        return textView;
    }
}
