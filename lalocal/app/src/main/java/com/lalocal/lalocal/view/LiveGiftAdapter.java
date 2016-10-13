package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.adapter.BaseRecyclerAdapter;

/**
 * Created by xiaojw on 2016/10/12.
 */

public class LiveGiftAdapter extends BaseRecyclerAdapter {
    Context mContext;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view= LayoutInflater.from(mContext).inflate(R.layout.list_item_gift,parent,false);
        return new LiveGiftHolder(view);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class LiveGiftHolder extends RecyclerView.ViewHolder{


        public LiveGiftHolder(View itemView) {
            super(itemView);
        }
    }




}
