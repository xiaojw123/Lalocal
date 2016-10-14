package com.lalocal.lalocal.view.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.OrderActivity;
import com.lalocal.lalocal.activity.PayActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/10/8.
 */

public class MyOrderRecyclerAdapter extends BaseRecyclerAdapter implements View.OnClickListener {
    Context context;
    List<OrderItem> items;
    Resources mRes;
    ContentLoader loader;

    public MyOrderRecyclerAdapter(List<OrderItem> items, ContentLoader loader) {
        this.items = items;
        this.loader = loader;
    }
    public void updateItems(List<OrderItem> items){
        this.items=items;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        mRes = context.getResources();
        View itemView = LayoutInflater.from(context).inflate(R.layout.home_me_my_oder_item, parent, false);
        return new OrderHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return items != null && items.size() > 0 ? items.size() : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (items != null && items.size() > 0) {
            OrderItem item = items.get(position);
            if (item != null) {
                OrderHolder itemHolder= (OrderHolder) holder;
                itemHolder.orderNum.setText(item.getOrderNumb());
                int status = item.getStatus();//: 0已取消/1已预订(未⽀付)/2已⽀付/3已消费/4已评价
                Resources res = context.getResources();
                int color = res.getColor(R.color.color_d9);
                String orderSatus = "";
                switch (status) {
                    case 0:
                        orderSatus = "已取消";
                        itemHolder.payBtn.setVisibility(View.GONE);
                        itemHolder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 1:
                        orderSatus = "待支付";
                        color = res.getColor(R.color.color_ffaa2a);
                        itemHolder.payBtn.setActivated(true);
                        itemHolder.payBtn.setText(mRes.getString(R.string.pay_amount));
                        itemHolder.payBtn.setVisibility(View.VISIBLE);
                        itemHolder.cancleBtn.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        orderSatus = "已支付";
                        color = res.getColor(R.color.color_fac1a0);
                        itemHolder.payBtn.setVisibility(View.GONE);
                        itemHolder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 3:
                        orderSatus = "已出票";
                        color = res.getColor(R.color.color_79c4d9);
                        itemHolder.payBtn.setSelected(true);
                        itemHolder.payBtn.setText(mRes.getString(R.string.evaluate));
                        itemHolder.payBtn.setVisibility(View.VISIBLE);
                        itemHolder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 4:
                        orderSatus = "已评价";
                        itemHolder.payBtn.setVisibility(View.GONE);
                        itemHolder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 5:
                        orderSatus = "申请退款";
                        itemHolder.payBtn.setVisibility(View.GONE);
                        itemHolder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 6:
                        orderSatus = "退款中";
                        itemHolder.payBtn.setVisibility(View.GONE);
                        itemHolder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 7:
                        orderSatus = "已退款";
                        itemHolder.payBtn.setVisibility(View.GONE);
                        itemHolder.cancleBtn.setVisibility(View.GONE);
                        break;
                    case 8:
                        orderSatus = "退款失败";
                        itemHolder.payBtn.setVisibility(View.GONE);
                        itemHolder.cancleBtn.setVisibility(View.GONE);
                        break;

                }
                itemHolder.orderStatus.setTextColor(color);
                itemHolder.orderStatus.setText(orderSatus);
                itemHolder.orderName.setText(item.getName());
                itemHolder.orderToalPrice.setText(CommonUtil.formartOrderPrice(item.getFee()));
                itemHolder.payBtn.setTag(item.getId());
                itemHolder.cancleBtn.setTag(item.getId());
                DrawableUtils.displayImg(context, itemHolder.orderPhoto, item.getPhoto(), (int) context.getResources().getDimension(R.dimen.dimen_size_2_dp), -1);
                itemHolder.payBtn.setOnClickListener(this);
                itemHolder.cancleBtn.setOnClickListener(this);
                itemHolder.itemView.setOnClickListener(this);
                itemHolder.itemView.setTag(item);
        }
        }


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
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
        if (v.getTag() != null) {
            OrderItem item = (OrderItem) v.getTag();
            Intent intent = new Intent(context, OrderActivity.class);
            intent.putExtra(KeyParams.ORDER_ID, item.getId());
            intent.putExtra(KeyParams.STATUS, item.getStatus());
            intent.putExtra(KeyParams.ACTION_TYPE, KeyParams.ACTION_UPDATE_ORDER);
            ((Activity)context).startActivityForResult(intent,100);
        }
    }

        class OrderHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.my_order_item_detail)
            LinearLayout itemDetail;
            @BindView(R.id.my_oder_ordernum_text)
            TextView orderNum;
            @BindView(R.id.my_order_status_tv)
            TextView orderStatus;
            @BindView(R.id.my_order_name_tv)
            TextView orderName;
            @BindView(R.id.my_oder_img)
            ImageView orderPhoto;
            @BindView(R.id.my_order_toal_price)
            TextView orderToalPrice;
            @BindView(R.id.my_order_cancel_btn)
            Button cancleBtn;
            @BindView(R.id.my_order_pay_btn)
            Button payBtn;

            public OrderHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }
        }
    }
