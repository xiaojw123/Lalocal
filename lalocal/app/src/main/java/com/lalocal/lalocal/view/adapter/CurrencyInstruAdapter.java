package com.lalocal.lalocal.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;

import java.util.List;

/**
 * Created by xiaojw on 2016/10/26.
 */

public class CurrencyInstruAdapter extends RecyclerView.Adapter<CurrencyInstruAdapter.CurrencyHolder>{
    List<String> mItems;
    public CurrencyInstruAdapter(List<String> items){
        mItems=items;
    }
    @Override
    public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrencyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instruction,parent,false));
    }

    @Override
    public void onBindViewHolder(CurrencyHolder holder, int position) {
        if (mItems!=null&&mItems.size()>0){
            String description=mItems.get(position);
            holder.descripTv.setText(description);
        }
    }

    @Override
    public int getItemCount() {
        return mItems!=null&&mItems.size()>0?mItems.size():0;
    }

    class CurrencyHolder extends  RecyclerView.ViewHolder{
        TextView descripTv;

        public CurrencyHolder(View itemView) {
            super(itemView);
            descripTv=(TextView) itemView.findViewById(R.id.item_instrucion_description);
        }
    }

}
