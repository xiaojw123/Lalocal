package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/21.
 */
public class SearchTagAdapter extends BaseRecyclerAdapter {
    Context context;
    List<String> datas;

    Resources res;

    public SearchTagAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        res = context.getResources();

    }


    public void updateItems(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppLog.print("onCreateViewHolder__");
            TextView tag = new TextView(context);
            tag.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, (int) res.getDimension(R.dimen.dimen_size_50_dp)));
            tag.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_16_sp));
            tag.setTextColor(res.getColor(R.color.color_8c));
            tag.setGravity(Gravity.CENTER_VERTICAL);
            return new ViewHolder(tag);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (datas == null || datas.size() < 1) {
            return;
        }
        AppLog.print("onBindViewHolder_____" + position);
        final String text = datas.get(position);
        ((TextView) holder.itemView).setText(text);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    v.setTag(text);
                    listener.onItemClickListener(holder.itemView, position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
