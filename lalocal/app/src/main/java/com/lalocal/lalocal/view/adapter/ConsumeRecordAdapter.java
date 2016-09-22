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
        context=parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.consume_record_item, parent, false);
        return new ConsumeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConsumeHolder itemHolder = (ConsumeHolder) holder;
        if (mRows != null && mRows.size() > 0) {
            if (position > 0) {
                itemHolder.lin1.setPadding((int) context.getResources().getDimension(R.dimen.dimen_size_15_dp), 0, (int) context.getResources().getDimension(R.dimen.dimen_size_15_dp), 0);
            }
            if (position == mRows.size() - 1) {
                itemHolder.lin2.setVisibility(View.VISIBLE);
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


        public ConsumeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


//
//    @Override
//    public int getCount() {
//        return mRows != null ? mRows.size() : 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mRows != null ? mRows.get(position) : null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        Context context=parent.getContext();
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = LayoutInflater.from(context).inflate(R.layout.consume_record_item, parent, false);
//            holder.channel_tv = (TextView) convertView.findViewById(R.id.cri_channel_tv);
//            holder.date_tv = (TextView) convertView.findViewById(R.id.cri_date_tv);
//            holder.num_tv = (TextView) convertView.findViewById(R.id.cri_num_tv);
//            holder.lin1=convertView.findViewById(R.id.consume_record_item_line1);
//            holder.lin2=convertView.findViewById(R.id.consume_record_item_line2);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        if (mRows != null && mRows.size() > 0) {
//            if (position>0){
//                holder.lin1.setPadding((int)context.getResources().getDimension(R.dimen.dimen_size_15_dp),0,(int)context.getResources().getDimension(R.dimen.dimen_size_15_dp),0);
//            }
//            if (position==mRows.size()-1){
//                holder.lin2.setVisibility(View.VISIBLE);
//            }
//            ConsumeRecord.RowsBean rowsBean = mRows.get(position);
//            holder.channel_tv.setText(rowsBean.getChannel());
//            holder.date_tv.setText(rowsBean.getDate());
//            boolean incomFlag = rowsBean.isIncomeFlag();
//            int value = rowsBean.getValue();
//            String numText = "";
//            if (incomFlag) {
//                holder.num_tv.setSelected(false);
//                numText = "+" + value;
//            } else {
//                holder.num_tv.setSelected(true);
//                numText = "-" + value;
//            }
//            holder.num_tv.setText(numText);
//        }
//
//        return convertView;
//    }
//
//    class ViewHolder {
//        TextView channel_tv;
//        TextView date_tv;
//        TextView num_tv;
//        View lin1,lin2;
//    }


}
