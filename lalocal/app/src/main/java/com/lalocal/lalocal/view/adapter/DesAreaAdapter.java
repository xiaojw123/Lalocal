package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.AreaItem;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/18.
 */
public class DesAreaAdapter extends BaseRecyclerAdapter {
    List<AreaItem> items;
    Context context;


    public DesAreaAdapter(Context context, List<AreaItem> items) {
        this.items = items;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_des_area_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (items != null && items.size() > 0) {
            final AreaItem item = items.get(position);
            if (item != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (listener!=null){
                            v.setTag(item);
                            listener.onItemClickListener(v,position);
                        }
                    }
                });
                DrawableUtils.displayImg(context, ((DesAreaAdapter.ViewHolder) holder).itemImg, item.getPhoto());
                ((DesAreaAdapter.ViewHolder) holder).itemName.setText(item.getName());
            }
        }


    }

    @Override
    public int getItemCount() {
        return items != null && items.size() > 0 ? items.size() : 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImg;
        TextView itemName;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemImg = (ImageView) itemView.findViewById(R.id.des_area_img);
            itemName = (TextView) itemView.findViewById(R.id.des_area_name);
        }
    }

}
