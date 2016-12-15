package com.lalocal.lalocal.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lalocal.lalocal.view.listener.OnItemClickListener;

/**
 * Created by xiaojw on 2016/7/21.
 */
public  abstract  class BaseRecyclerAdapter extends RecyclerView.Adapter{
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (listener!=null){
                      listener.onItemClickListener(v,position);
                  }

              }
          });

    }
}
