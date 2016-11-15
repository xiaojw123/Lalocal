package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * Created by wangjie on 2016/11/8.
 */
public class HomeLiveAdapter extends RecyclerView.Adapter {

    private static final int MY_CONCERN = 0x01;
    private static final int LIVE_ITEM = 0x02;

    private Context mContext;

    public HomeLiveAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case MY_CONCERN:
                view = LayoutInflater.from(mContext).inflate(R.layout.live_my_attention_item, parent, false);
                holder = new ViewHolder(view);
                break;
            case LIVE_ITEM:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_item, parent, false);
                holder = new ViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MY_CONCERN;
        }
        return LIVE_ITEM;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        // 主标题
        TextView tvTitle;
        // 定位
        TextView tvAddress;
        // 主播ID
        TextView tvUserId;
        // 主播头像
        ImageView imgAvatar;
        // 直播图片
        ImageView imgLive;
        // 用户进入直播间情况
        TextView tvEnter;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvUserId = (TextView) itemView.findViewById(R.id.tv_userid);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            imgLive = (ImageView) itemView.findViewById(R.id.img_live);
            tvEnter = (TextView) itemView.findViewById(R.id.tv_enter);
        }
    }
}
