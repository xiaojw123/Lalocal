package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.MeItem;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.ShapeTextView;

import java.util.List;

/**
 * Created by xiaojw on 2016/11/28.
 */

public class MeItemAdapter extends BaseRecyclerAdapter {
    public static final int ITEM_MY_LIVE = 0x01;
    public static final int ITEM_MY_MESSAGE = 0x02;
    public static final int ITEM_MY_FAVOR = 0x03;
    public static final int ITEM_MY_WALLET = 0x04;
    public static final int ITEM_MY_ORDER = 0x05;
    public static final int ITEM_MY_FRIEND = 0x06;
    public static final int ITEM_MY_ARTICLE = 0x07;
    public static final int ITEM_MY_SETTING = 0x08;
    List<MeItem> mItems;
    Context mContext;

    public MeItemAdapter(List<MeItem> items) {
        mItems = items;
    }

    public void updateItems(List<MeItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public List<MeItem> getItems() {
        return mItems;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        AppLog.print("onBindViewHolder____" + position);
        if (mItems.size() > 0) {
            MeItem item = mItems.get(position);
            if (item != null) {
                MeItemHolder itemHolder = (MeItemHolder) holder;
                itemHolder.meTv.setText(item.getName());
                Drawable drawable = mContext.getResources().getDrawable(item.getDrawableId());
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                itemHolder.meTv.setCompoundDrawables(null, drawable, null, null);
                if (item.getId() == ITEM_MY_MESSAGE) {
                    int msgCount = item.getMsgCount();
                    if (msgCount > 0) {
                        itemHolder.msgCountTv.setVisibility(View.VISIBLE);
                        String fomartCount = msgCount > 99 ? msgCount + "+" : String.valueOf(msgCount);
                        itemHolder.msgCountTv.setText(fomartCount);
                    } else {
                        itemHolder.msgCountTv.setVisibility(View.GONE);
                    }
                } else {
                    itemHolder.msgCountTv.setVisibility(View.GONE);
                }
                itemHolder.itemView.setTag(item);
            }

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_me_grid, parent, false);
        return new MeItemHolder(view);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MeItemHolder extends RecyclerView.ViewHolder {
        TextView meTv;
        ShapeTextView msgCountTv;

        public MeItemHolder(View itemView) {
            super(itemView);
            meTv = (TextView) itemView.findViewById(R.id.item_me_tv);
            msgCountTv = (ShapeTextView) itemView.findViewById(R.id.item_me_msgcount);
        }
    }
}
