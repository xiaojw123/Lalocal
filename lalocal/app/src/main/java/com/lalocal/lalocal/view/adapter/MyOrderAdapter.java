package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.OrderActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by xiaojw on 2016/6/21.
 */
public class MyOrderAdapter extends BaseAdapter implements View.OnClickListener {
    Context context;
    List<OrderItem> items;

    public MyOrderAdapter(Context context, List<OrderItem> items) {
        this.context = context;
        this.items = items;
    }

    public void updateListView(List<OrderItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items == null || items.size() < 1 ? 1 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items == null || items.size() < 1 ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (position == 0 && (items == null || items.size() < 1)) {
            convertView = inflater.inflate(R.layout.home_me_my_oder_nothing, null);
            parent.setEnabled(false);
        } else {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.home_me_my_oder_item, null);
                parent.setEnabled(true);
                holder.adultItemCotainer = (LinearLayout) convertView.findViewById(R.id.my_order_adult_container);
                holder.itemDetail = (LinearLayout) convertView.findViewById(R.id.my_order_item_detail);

                holder.orderNum = (TextView) convertView.findViewById(R.id.my_oder_ordernum_text);
                holder.orderStatus = (TextView) convertView.findViewById(R.id.my_order_status_tv);
                holder.orderName = (TextView) convertView.findViewById(R.id.my_order_name_tv);
                holder.orderPhoto = (ImageView) convertView.findViewById(R.id.my_oder_img);
                holder.orderToalPrice = (TextView) convertView.findViewById(R.id.my_order_toal_price);
                holder.cancleBtn = (Button) convertView.findViewById(R.id.my_order_cancel_btn);
                holder.payBtn = (Button) convertView.findViewById(R.id.my_order_pay_btn);
                holder.itemDetail.setOnClickListener(this);

                OrderItem item = items.get(position);
                if (item != null) {
                    List<OrderItem.OrderPay> orderPays = item.getOrderPayList();
                    for (OrderItem.OrderPay orderPay : orderPays) {
                        if (orderPay != null) {
                            View adultItemView = inflater.inflate(R.layout.my_order_adult_item, holder.adultItemCotainer, false);
                            TextView name = (TextView) adultItemView.findViewById(R.id.my_order_adult_item_name);
                            TextView discount = (TextView) adultItemView.findViewById(R.id.my_order_adult_item_discount);
                            name.setText(orderPay.getName());
                            discount.setText("￥ " + (int) orderPay.getUnit() + "x" + orderPay.getAmount());
                            holder.adultItemCotainer.addView(adultItemView);
                        }
                    }
                    holder.orderNum.setText(item.getOrderNumb());
                    int status = item.getStatus();//: 0已取消/1已预订(未⽀付)/2已⽀付/3已消费/4已评价
                    Resources res = context.getResources();
                    int color = res.getColor(R.color.color_d9);
                    String orderSatus = "";
                    switch (status) {
                        case 0:
                            orderSatus = "已取消";
                            break;
                        case 1:
                            orderSatus = "未支付";
                            color = res.getColor(R.color.color_ffaa2a);
                            break;
                        case 2:
                            orderSatus = "交易成功";
                            color = res.getColor(R.color.color_fac1a0);
                            break;
                        case 3:
                            orderSatus = "已出票";
                            color = res.getColor(R.color.color_79c4d9);
                            break;
                        case 4:
                            orderSatus = "已评价";
                            break;

                    }
                    holder.orderStatus.setTextColor(color);
                    holder.orderStatus.setText(orderSatus);
                    holder.orderName.setText(item.getName());
                    holder.orderToalPrice.setText("¥ " + String.valueOf(item.getFee()));
                    if (status == 1) {
                        holder.payBtn.setActivated(true);
                        holder.payBtn.setText("支付");
                        holder.payBtn.setVisibility(View.VISIBLE);
                        holder.cancleBtn.setVisibility(View.VISIBLE);
                    } else {
                        holder.payBtn.setSelected(true);
                        holder.payBtn.setText("评价");
                        holder.payBtn.setVisibility(View.VISIBLE);
                        holder.cancleBtn.setVisibility(View.GONE);
                    }
                    DrawableUtils.displayImg(context, holder.orderPhoto, item.getPhoto(),R.drawable.my_oder_img_drawable);
                    holder.itemDetail.setTag(R.id.orderDetailId, item);
                }
                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }


        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_order_item_detail:
                if (v.getTag(R.id.orderDetailId) != null) {
                    OrderItem item = (OrderItem) v.getTag(R.id.orderDetailId);
                    Intent intent = new Intent(context, OrderActivity.class);
                    intent.putExtra(KeyParams.ORDER_ID, item.getId());
                    intent.putExtra(KeyParams.STATUS, item.getStatus());
                    context.startActivity(intent);
                }
                break;

        }


    }

    class ViewHolder {
        LinearLayout adultItemCotainer;
        LinearLayout itemDetail;
        TextView orderNum;
        TextView orderStatus;
        ImageView orderPhoto;
        TextView orderName;
        TextView orderToalPrice;
        Button cancleBtn;
        Button payBtn;
    }

}
