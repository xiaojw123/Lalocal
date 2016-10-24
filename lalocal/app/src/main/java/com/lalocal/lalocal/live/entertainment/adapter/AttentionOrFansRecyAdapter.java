package com.lalocal.lalocal.live.entertainment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.LiveFansOrAttentionRowsBean;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 2016/10/10.
 */
public class AttentionOrFansRecyAdapter extends RecyclerView.Adapter {


    private List<LiveFansOrAttentionRowsBean> allRows;
    private Context mContext;
    private LayoutInflater inflater;

    public AttentionOrFansRecyAdapter(Context context, List<LiveFansOrAttentionRowsBean> allRows) {
        this.allRows = allRows;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public  void refresh(List<LiveFansOrAttentionRowsBean> allRows){
        this.allRows=allRows;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.live_attention_list_item, parent, false);
        AttentionToFansViewHodler holder = new AttentionToFansViewHodler(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AttentionToFansViewHodler viewHodler= (AttentionToFansViewHodler)holder;
        final LiveFansOrAttentionRowsBean liveFansOrAttentionRowsBean = allRows.get(position);
        int id = liveFansOrAttentionRowsBean.getId();
        int userId = UserHelper.getUserId(mContext);
        if(id==userId){
            viewHodler.attentionListItemAttention.setVisibility(View.INVISIBLE);
        }else{
            viewHodler.attentionListItemAttention.setVisibility(View.VISIBLE);
        }
        String description = liveFansOrAttentionRowsBean.getDescription();
        DrawableUtils.displayImg(mContext, viewHodler.attentionListItemHead, liveFansOrAttentionRowsBean.getAvatar());
        viewHodler.attentionListItemName.setText(liveFansOrAttentionRowsBean.getNickName());
        int status = liveFansOrAttentionRowsBean.getAttentionVO().getStatus();
        if(status==2){
            viewHodler.attentionListItemAttention.setText("已相互关注");
            viewHodler.attentionListItemAttention.setTextColor(Color.parseColor("#666666"));
        }else if(status==1){
            viewHodler.attentionListItemAttention.setText("已关注");
            viewHodler.attentionListItemAttention.setTextColor(Color.parseColor("#666666"));
        }else {
            viewHodler.attentionListItemAttention.setText("关注");
            viewHodler.attentionListItemAttention.setTextColor(Color.parseColor("#ffaa2a"));
        }
        if(description!=null&&description.length()>0){
            viewHodler.attentionListItemSingture.setText(description);
        }else {
            viewHodler.attentionListItemSingture.setText("这家伙很懒啥也没写");
        }
        viewHodler.itemView.setTag(liveFansOrAttentionRowsBean.getId());
        viewHodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAttentionToFansItemClickListener!=null){
                    onAttentionToFansItemClickListener.onItemClick(liveFansOrAttentionRowsBean.getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return allRows == null ? 0 : allRows.size();
    }

    public class AttentionToFansViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.attention_list_item_head)
        ImageView attentionListItemHead;
        @BindView(R.id.attention_list_item_name)
        TextView attentionListItemName;
        @BindView(R.id.attention_list_item_singture)
        TextView attentionListItemSingture;
        @BindView(R.id.attention_list_item_attention)
        TextView attentionListItemAttention;
        public AttentionToFansViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //recyclerview条目点击事件
    public OnAttentionToFansItemClickListener onAttentionToFansItemClickListener;

    public interface OnAttentionToFansItemClickListener {
        void onItemClick(int id);
    }

    public void setOnAttentionToFansItemClickListener(OnAttentionToFansItemClickListener onAttentionToFansItemClickListener) {
        this.onAttentionToFansItemClickListener = onAttentionToFansItemClickListener;
    }
}
