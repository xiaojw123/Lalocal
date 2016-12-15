package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ChannelRecord;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.adapter.BaseRecyclerAdapter;

import java.util.List;

/**
 * Created by xiaojw on 2016/10/12.
 */

public class LiveGiftAdapter extends BaseRecyclerAdapter {


    Context mContext;
    List<ChannelRecord.GiftRecordsBean> mItems;

    public LiveGiftAdapter(List<ChannelRecord.GiftRecordsBean> items){
        mItems=items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_gift,parent,false);
        return new LiveGiftHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mItems!=null&&mItems.size()>0){
            ChannelRecord.GiftRecordsBean record=mItems.get(position);
            if (record!=null){
                LiveGiftHolder itemHoler= (LiveGiftHolder) holder;
                DrawableUtils.displayImg(mContext,itemHoler.gfitImg,record.getPhoto());
                itemHoler.giftNum.setText(record.getNumb());
            }


        }

    }

    @Override
    public int getItemCount() {
        return mItems!=null&&mItems.size()>0?mItems.size():0;
    }

    class LiveGiftHolder extends RecyclerView.ViewHolder{
        ImageView gfitImg;
        TextView giftNum;


        public LiveGiftHolder(View itemView) {
            super(itemView);
            gfitImg= (ImageView) itemView.findViewById(R.id.item_gift_img);
            giftNum= (TextView) itemView.findViewById(R.id.item_gift_num);

        }
    }




}
