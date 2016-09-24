package com.lalocal.lalocal.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RechargeItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/9/5.
 */
public class RechargeListAdapter extends BaseRecyclerAdapter {
    private final String FORMART_FEE = "￥ %1$s购买";
    List<RechargeItem> mItems;

    public RechargeListAdapter(List<RechargeItem> items) {
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recharge_list_item, parent, false);
        RechargeListHolder holder = new RechargeListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RechargeItem item = mItems.get(position);
        if (item != null) {
            RechargeListHolder itemHolder = (RechargeListHolder) holder;
            if (position == mItems.size() - 1) {
                itemHolder.lin2.setVisibility(View.VISIBLE);
            } else {
                if (itemHolder.lin2.getVisibility() == View.VISIBLE) {
                    itemHolder.lin2.setVisibility(View.GONE);
                }
            }
            itemHolder.fee_tv.setText(String.format(FORMART_FEE, item.getFee()));
            itemHolder.value_tv.setText(String.valueOf(item.getValue()));
            if (listener!=null){
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        listener.onItemClickListener(v,position);
                    }
                });
            }
            itemHolder.itemView.setTag(item);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class RechargeListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recharge_list_item_value)
        TextView value_tv;
        @BindView(R.id.recharge_list_item_fee)
        TextView fee_tv;
        @BindView(R.id.recharge_list_item_lin2)
        View lin2;

        public RechargeListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
