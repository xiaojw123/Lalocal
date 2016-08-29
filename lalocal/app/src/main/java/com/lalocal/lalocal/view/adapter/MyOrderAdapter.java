package com.lalocal.lalocal.view.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.lalocal.lalocal.activity.PayActivity;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by xiaojw on 2016/6/21.
 */
public class MyOrderAdapter extends BaseAdapter implements View.OnClickListener{
    Context context;
    List<OrderItem> items;
    Resources mRes;
    ContentLoader loader;
    MeFragment fragment;

    public MyOrderAdapter(Context context, MeFragment fragment, List<OrderItem> items, ContentLoader loader) {
        this.context = context;
        this.items = items;
        this.loader=loader;
        this.fragment=fragment;
        mRes=context.getResources();
    }

    public void setLoader(ContentLoader loader){
        this.loader=loader;
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
            AppLog.print("getView____position___"+position);
            ViewHolder holder =null;
            if (convertView == null) {
                holder= new ViewHolder();
                convertView = inflater.inflate(R.layout.home_me_my_oder_item, null);
                parent.setEnabled(true);
                holder.itemDetail = (LinearLayout) convertView.findViewById(R.id.my_order_item_detail);

                holder.orderNum = (TextView) convertView.findViewById(R.id.my_oder_ordernum_text);
                holder.orderStatus = (TextView) convertView.findViewById(R.id.my_order_status_tv);
                holder.orderName = (TextView) convertView.findViewById(R.id.my_order_name_tv);
                holder.orderPhoto = (ImageView) convertView.findViewById(R.id.my_oder_img);
                holder.orderToalPrice = (TextView) convertView.findViewById(R.id.my_order_toal_price);
                holder.cancleBtn = (Button) convertView.findViewById(R.id.my_order_cancel_btn);
                holder.payBtn = (Button) convertView.findViewById(R.id.my_order_pay_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            OrderItem item = items.get(position);
            AppLog.print("getView____position___"+position+", numb__"+item.getOrderNumb());
            if (item != null) {
                holder.orderNum.setText(item.getOrderNumb());
                int status = item.getStatus();//: 0已取消/1已预订(未⽀付)/2已⽀付/3已消费/4已评价
                Resources res = context.getResources();
                int color = res.getColor(R.color.color_d9);
                String orderSatus = "";
                switch (status) {
                    case 0:
                        orderSatus = "已取消";
                        holder.payBtn.setVisibility(View.GONE);
                        holder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 1:
                        orderSatus = "待支付";
                        color = res.getColor(R.color.color_ffaa2a);
                        holder.payBtn.setActivated(true);
                        holder.payBtn.setText(mRes.getString(R.string.pay_amount));
                        holder.payBtn.setVisibility(View.VISIBLE);
                        holder.cancleBtn.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        orderSatus = "已支付";
                        color = res.getColor(R.color.color_fac1a0);
                        holder.payBtn.setVisibility(View.GONE);
                        holder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 3:
                        orderSatus = "已出票";
                        color = res.getColor(R.color.color_79c4d9);
                        holder.payBtn.setSelected(true);
                        holder.payBtn.setText(mRes.getString(R.string.evaluate));
                        holder.payBtn.setVisibility(View.VISIBLE);
                        holder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 4:
                        orderSatus = "已评价";
                        holder.payBtn.setVisibility(View.GONE);
                        holder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 5:
                        orderSatus="申请退款";
                        holder.payBtn.setVisibility(View.GONE);
                        holder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 6:
                        orderSatus="退款中";
                        holder.payBtn.setVisibility(View.GONE);
                        holder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 7:
                        orderSatus="已退款";
                        holder.payBtn.setVisibility(View.GONE);
                        holder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 8:
                        orderSatus="退款失败";
                        holder.payBtn.setVisibility(View.GONE);
                        holder.cancleBtn.setVisibility(View.GONE);
                        break;

                }
                holder.orderStatus.setTextColor(color);
                holder.orderStatus.setText(orderSatus);
                holder.orderName.setText(item.getName());
                holder.orderToalPrice.setText(CommonUtil.formartOrderPrice(item.getFee()));
                holder.payBtn.setTag(item.getId());
                holder.cancleBtn.setTag(item.getId());
                DrawableUtils.displayImg(context, holder.orderPhoto, item.getPhoto(),(int)context.getResources().getDimension(R.dimen.dimen_size_2_dp),-1);
                holder.payBtn.setOnClickListener(this);
                holder.cancleBtn.setOnClickListener(this);
                convertView.setTag(R.id.orderDetailId, item);
                convertView.setOnClickListener(this);
            }
        }
        return convertView;
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_order_item_detail:
                gotoOrderDetail(v);
                break;
            case R.id.my_order_pay_btn:
                String attr= ((Button) v).getText().toString();
                final int orderId= (int) v.getTag();
                if (mRes.getString(R.string.pay_amount).equals(attr)){
                    Intent intent=new Intent(context,PayActivity.class);
                    intent.putExtra(PayActivity.ORDER_ID,orderId);
                    intent.putExtra(KeyParams.ACTION_TYPE, KeyParams.ACTION_UPDATE_ORDER);
                    ((Activity)context).startActivityForResult(intent,100);
                }else if (mRes.getString(R.string.evaluate).equals(attr)){

                }
                break;
            case R.id.my_order_cancel_btn:
                final int cancelOrderId= (int) v.getTag();
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("确认要取消订单");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppLog.print("否——————loader__"+loader);
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppLog.print("是——————loader__"+loader);
                        dialog.dismiss();
                        if (loader!=null){
                        loader.cancelOrder(cancelOrderId);
                        }
                    }
                });
                builder.create();
                builder.show();
                break;
            default:
                gotoOrderDetail(v);
                break;

        }


    }

    private void gotoOrderDetail(View v) {
        if (v.getTag(R.id.orderDetailId) != null) {
            OrderItem item = (OrderItem) v.getTag(R.id.orderDetailId);
            Intent intent = new Intent(context, OrderActivity.class);
            intent.putExtra(KeyParams.ORDER_ID, item.getId());
            intent.putExtra(KeyParams.STATUS, item.getStatus());
            intent.putExtra(KeyParams.ACTION_TYPE, KeyParams.ACTION_UPDATE_ORDER);
            fragment.startActivityForResult(intent,100);
        }
    }


    class ViewHolder {
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
