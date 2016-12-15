package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ConsumeRecord;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/9/3.
 */
public class ConsumeRecordAdapter extends BaseRecyclerAdapter {

    List<ConsumeRecord.RowsBean> mRows;
    Context context;

    public ConsumeRecordAdapter(List<ConsumeRecord.RowsBean> rows) {
        mRows = rows;
    }

    public void updateItems(List<ConsumeRecord.RowsBean> rows) {
        mRows = rows;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.consume_record_item, parent, false);
        return new ConsumeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConsumeHolder itemHolder = (ConsumeHolder) holder;
        if (mRows != null && mRows.size() > 0) {
            if (position == 0) {
                itemHolder.lin1.setVisibility(View.VISIBLE);
                if (position == mRows.size() - 1) {
                    itemHolder.lin2.setVisibility(View.VISIBLE);
                    itemHolder.lin3.setVisibility(View.INVISIBLE);
                } else {
                    if (itemHolder.lin2.getVisibility() == View.VISIBLE) {
                        itemHolder.lin2.setVisibility(View.INVISIBLE);
                    }
                    itemHolder.lin3.setVisibility(View.VISIBLE);
                }
            } else if (position == mRows.size() - 1) {
                itemHolder.lin2.setVisibility(View.VISIBLE);
                if (itemHolder.lin1.getVisibility() == View.VISIBLE) {
                    itemHolder.lin1.setVisibility(View.INVISIBLE);
                }
                if (itemHolder.lin3.getVisibility() == View.VISIBLE) {
                    itemHolder.lin3.setVisibility(View.INVISIBLE);
                }

            } else {
                itemHolder.lin3.setVisibility(View.VISIBLE);
                if (itemHolder.lin1.getVisibility() == View.VISIBLE) {
                    itemHolder.lin1.setVisibility(View.INVISIBLE);
                }
                if (itemHolder.lin2.getVisibility() == View.VISIBLE) {
                    itemHolder.lin2.setVisibility(View.INVISIBLE);
                }
            }


            ConsumeRecord.RowsBean rowsBean = mRows.get(position);
            itemHolder.channel_tv.setText(rowsBean.getChannel());
            itemHolder.date_tv.setText(rowsBean.getDate());
            boolean incomFlag = rowsBean.isIncomeFlag();
            int value = rowsBean.getValue();
            String numText = "";
            if (incomFlag) {
                itemHolder.num_tv.setSelected(false);
                numText = "+" + value;
            } else {
                itemHolder.num_tv.setSelected(true);
                numText = "-" + value;
            }
            itemHolder.num_tv.setText(numText);
        }

    }

    @Override
    public int getItemCount() {
        return mRows.size();
    }

    class ConsumeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cri_channel_tv)
        TextView channel_tv;
        @BindView(R.id.cri_date_tv)
        TextView date_tv;
        @BindView(R.id.cri_num_tv)
        TextView num_tv;
        @BindView(R.id.consume_record_item_line1)
        View lin1;
        @BindView(R.id.consume_record_item_line2)
        View lin2;
        @BindView(R.id.consume_record_item_line3)
        View lin3;


        public ConsumeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }




}
