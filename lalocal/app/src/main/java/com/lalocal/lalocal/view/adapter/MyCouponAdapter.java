package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.dialog.ConvertDialog;

import static com.lalocal.lalocal.R.id.my_coupon_convert_btn;


/**
 * Created by xiaojw on 2016/6/21.
 * 优惠券适配器
 */
public class MyCouponAdapter extends BaseAdapter implements View.OnClickListener {
    Context context;

    public MyCouponAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
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
        AppLog.print("coupon position___" + position);
        convertView = LayoutInflater.from(context).inflate(R.layout.home_me_my_coupon_item1, null);
        Button convertBtn = (Button) convertView.findViewById(my_coupon_convert_btn);
        convertBtn.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        ConvertDialog dialog = new ConvertDialog(context);
        dialog.show();
    }
}
