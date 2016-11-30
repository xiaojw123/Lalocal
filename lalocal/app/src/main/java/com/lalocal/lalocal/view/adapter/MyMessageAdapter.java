package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.MessageItem;
import com.lalocal.lalocal.util.AppLog;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/11/21.
 */

public class MyMessageAdapter extends BaseRecyclerAdapter {
    List<MessageItem> mItems;
    Context mContext;

    public MyMessageAdapter(List<MessageItem> items) {
        mItems = items;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mybg = LayoutInflater.from(mContext).inflate(R.layout.item_delete, null);
        mybg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_mymessage,null);
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new MessageHolder(mContext, mybg, itemView, RecyclerViewDragHolder.EDGE_RIGHT, (int) mContext.getResources().getDimension(R.dimen.item_my_message_layout_height)).getDragViewHolder();
    }

    public void remove(int position) {
        AppLog.print("remove pos__"+position);
        MessageItem item = mItems.get(position);
        DataSupport.delete(MessageItem.class,item.getId());
        mItems.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AppLog.print("onBindViewHolder____pos__"+position);
        if (mItems != null && mItems.size() > 0) {
            MessageItem item = mItems.get(position);
            if (item != null) {
                MessageHolder msgHolder = (MessageHolder) RecyclerViewDragHolder.getHolder(holder);
                msgHolder.contentTv.setText(item.getContent());
                msgHolder.dateTv.setText(item.getCreateTime());
                msgHolder.itemLayout.setTag(item);
                msgHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null){
                            listener.onItemClickListener(v,position);
                        }
                    }
                });
                msgHolder.deleteFl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null && mItems.size() > 0 ? mItems.size() : 0;
    }

     class MessageHolder extends RecyclerViewDragHolder {
         @BindView(R.id.list_item_msg_layout)
         RelativeLayout itemLayout;
        @BindView(R.id.list_item_msg_content)
        TextView contentTv;
        @BindView(R.id.list_item_msg_date)
        TextView dateTv;
        @BindView(R.id.item_delete)
        FrameLayout deleteFl;

        public MessageHolder(Context context, View bgView, View topView, int mTrackingEdges, int height) {
            super(context, bgView, topView, mTrackingEdges, height);
        }

        @Override
        public void initView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }


}
