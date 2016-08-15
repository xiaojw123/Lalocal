package com.lalocal.lalocal.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lalocal.lalocal.view.listener.OnItemClickListener;

/**
 * Created by xiaojw on 2016/7/21.
 */
public  abstract  class BaseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_NULL=0x11;
    OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    class EmptViewHolder extends RecyclerView.ViewHolder{
        public EmptViewHolder(View itemView) {
            super(itemView);
        }
    }

}
