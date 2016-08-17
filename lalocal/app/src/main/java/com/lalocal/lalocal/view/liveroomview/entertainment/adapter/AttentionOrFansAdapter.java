package com.lalocal.lalocal.view.liveroomview.entertainment.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.LiveFansOrAttentionRowsBean;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by android on 2016/8/7.
 */
public class AttentionOrFansAdapter extends BaseAdapter {
    private List<LiveFansOrAttentionRowsBean> allRows;
    private Context mContext;

    public AttentionOrFansAdapter(Context context, List<LiveFansOrAttentionRowsBean> allRows) {
        this.allRows = allRows;
        this.mContext = context;

    }

    public void refresh(List<LiveFansOrAttentionRowsBean> allRows) {
        this.allRows = allRows;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return allRows == null ? 0 : allRows.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.live_attention_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.attentionHead = (ImageView) convertView.findViewById(R.id.attention_list_item_head);
            viewHolder.attentionName = (TextView) convertView.findViewById(R.id.attention_list_item_name);
            viewHolder.attentionSingture = (TextView) convertView.findViewById(R.id.attention_list_item_singture);
            viewHolder.attentionAttention = (TextView) convertView.findViewById(R.id.attention_list_item_attention);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LiveFansOrAttentionRowsBean liveFansOrAttentionRowsBean = allRows.get(position);
        String description = liveFansOrAttentionRowsBean.getDescription();
        DrawableUtils.displayImg(mContext, viewHolder.attentionHead, liveFansOrAttentionRowsBean.getAvatar());
        viewHolder.attentionName.setText(liveFansOrAttentionRowsBean.getNickName());
        int status = liveFansOrAttentionRowsBean.getAttentionVO().getStatus();
        if(status==2){
            viewHolder.attentionAttention.setText("已相互关注");
            viewHolder.attentionAttention.setTextColor(Color.parseColor("#666666"));
        }
        if(description!=null&&description.length()>0){
            viewHolder.attentionSingture.setText(description);
        }else {
            viewHolder.attentionSingture.setText("这家伙很懒啥也没写");
        }

        return convertView;
    }

    class ViewHolder {
        ImageView attentionHead;
        TextView attentionName;
        TextView attentionSingture;
        TextView attentionAttention;
    }



}
