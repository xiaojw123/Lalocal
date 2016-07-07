package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.MeFragment;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.dialog.ConvertDialog;

import java.util.List;


/**
 * Created by xiaojw on 2016/6/21.
 * 优惠券适配器
 */
public class MyCouponAdapter extends BaseAdapter implements View.OnClickListener {
    Context context;
    List<Coupon> items;
    MeFragment fragment;

    public MyCouponAdapter(Context context, List<Coupon> items, MeFragment fragment) {
        this.context = context;
        this.items = items;
        this.fragment = fragment;
    }

    public void updateItems(List<Coupon> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return (items == null || items.size() < 1 ? 1 : items.size() + 1);
    }

    @Override
    public Object getItem(int position) {
        return position == 0 ? null : items.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppLog.print("coupon position___" + position);
        LayoutInflater inflater = LayoutInflater.from(context);
        if (position == 0) {
            convertView = inflater.inflate(R.layout.home_me_my_coupon_item1, null);
            ImageView nothingImg = (ImageView) convertView.findViewById(R.id.my_coupon_nothing_img);
            TextView converRuleTv = (TextView) convertView.findViewById(R.id.my_coupon_convert_rule);
            TextView warnText = (TextView) convertView.findViewById(R.id.my_coupon_nothing_warn);
            //        Button convertBtn = (Button) convertView.findViewById(my_coupon_convert_btn);
//        convertBtn.setOnClickListener(this);
            if (items == null || items.size() < 1) {
                if (fragment.isLogined) {
                    warnText.setText("没有优惠券");
                } else {
                    warnText.setText(context.getResources().getString(R.string.unlogin_warn));
                }
                nothingImg.setVisibility(View.VISIBLE);
                converRuleTv.setVisibility(View.GONE);
                warnText.setVisibility(View.VISIBLE);
            } else {
                nothingImg.setVisibility(View.GONE);
                converRuleTv.setVisibility(View.VISIBLE);
                warnText.setVisibility(View.GONE);
            }
        } else {
            if (items == null || items.size() < 1) {
                return null;
            }
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.home_me_my_coupon_item, null);
                Resources res = context.getResources();
                int left = (int) res.getDimension(R.dimen.home_me_my_favorite_item_left);
                int top = 0;
                int bottom = (int) res.getDimension(R.dimen.dimen_size_5_dp);
                int height = (int) res.getDimension(R.dimen.my_coupon_item_height);
                if (position == 1) {
                    top = (int) res.getDimension(R.dimen.dimen_size_25_dp);
                    height += (int) res.getDimension(R.dimen.dimen_size_20_dp);
                } else {
                    top = (int) res.getDimension(R.dimen.dimen_size_5_dp);
                }
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                convertView.setPadding(left, top, left, bottom);
                holder.myCouponContainer = (RelativeLayout) convertView.findViewById(R.id.my_coupon_card);
                holder.cornerImg = (ImageView) convertView.findViewById(R.id.my_coupon_corner_img);
                holder.discount_tv = (TextView) convertView.findViewById(R.id.my_coupon_denomination_tv);
                holder.discount_units_tv = (TextView) convertView.findViewById(R.id.my_coupon_discount_units);
                holder.rule1_tv = (TextView) convertView.findViewById(R.id.my_coupon_use_rule_tv);
                holder.time_tv = (TextView) convertView.findViewById(R.id.my_coupon_time_tv);
                Coupon item = items.get(position - 1);
                if (item != null) {
                    if (item.getType() == 1 && item.getStatus() == 0) {
                        holder.cornerImg.setBackground(res.getDrawable(R.drawable.writer_mark_icon));
                    } else if (item.getStatus() == 2) {
                        holder.cornerImg.setBackground(res.getDrawable(R.drawable.losttime_mark_icon));
                    }
                    if (item.getType() == 1) {
                        holder.myCouponContainer.setActivated(false);
                        holder.discount_tv.setActivated(false);
                        holder.discount_units_tv.setActivated(false);
                        holder.time_tv.setActivated(false);
                        holder.rule1_tv.setActivated(false);
                        holder.rule1_tv.setText("此优惠券可与同色优惠券叠加使用");
                    } else {
                        holder.myCouponContainer.setActivated(true);
                        holder.discount_tv.setActivated(true);
                        holder.discount_units_tv.setActivated(true);
                        holder.time_tv.setActivated(true);
                        holder.rule1_tv.setActivated(true);
                        holder.rule1_tv.setText("不可叠加使用");
                    }

                    holder.discount_tv.setText(String.valueOf((int) item.getDiscount()));
                    holder.time_tv.setText("有效期至：" + item.getExpiredDateStr().replace("_", "."));
                    convertView.setTag(holder);
                }

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


        }
        return convertView;
    }

    class ViewHolder {
        RelativeLayout myCouponContainer;
        ImageView cornerImg;
        TextView discount_tv;
        TextView discount_units_tv;
        TextView rule1_tv;
        TextView time_tv;


    }

    @Override
    public void onClick(View v) {
        ConvertDialog dialog = new ConvertDialog(context);
        dialog.show();
    }
}
