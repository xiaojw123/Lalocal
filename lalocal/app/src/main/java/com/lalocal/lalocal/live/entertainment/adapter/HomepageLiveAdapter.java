package com.lalocal.lalocal.live.entertainment.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.ScaleImageView;

/**
 * Created by wangjie on 2016/10/12.
 */
public class HomepageLiveAdapter extends RecyclerView.Adapter {

    // 不同类型布局标记
    private static final int TYPE_LIVE_ING = 0x01;
    private static final int TYPE_LIVE_PLAYBACK = 0x02;

    private Context mContext;

    public HomepageLiveAdapter(Context context) {
        AppLog.i("ttt", "LIVE_init HomepageLiveAdapter");
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppLog.i("ttt", "LIVE_onCreateViewHolder");
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_LIVE_ING:
                AppLog.i("ttt", "TYPE_LIVE_ING__");
                // 加载正在直播布局
                view = LayoutInflater.from(mContext).inflate(R.layout.homepage_live_ing_item, parent, false);
                // 初始化正在直播ViewHolder
                holder = new LivingViewHolder(view);
                break;
            case TYPE_LIVE_PLAYBACK:
                AppLog.i("ttt", "TYPE_LIVE_PLAYBACK__");
                // 加载直播回放布局
                view = LayoutInflater.from(mContext).inflate(R.layout.homepage_live_playback_item, parent, false);
                // 初始化直播回放ViewHolder
                holder = new LivePlaybackViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppLog.i("ttt", "LIVE_IS__" + position);
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    /**
     * 返回子项类型，与布局标记对应
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_LIVE_ING;
        }
        return TYPE_LIVE_PLAYBACK;
    }

    /**
     * 直播ViewHolder
     */
    private class LivingViewHolder extends RecyclerView.ViewHolder {
        TextView tvLivingTitle;
        TextView tvLivingLocation;
        ScaleImageView imgLiving;
        CardView cardView;
        ImageView imgLiveLocation;

        public LivingViewHolder(View itemView) {
            super(itemView);

            // -关联控件
            tvLivingTitle = (TextView) itemView.findViewById(R.id.tv_live_ing_title);
            tvLivingLocation = (TextView) itemView.findViewById(R.id.tv_live_ing_location);
            imgLiving = (ScaleImageView) itemView.findViewById(R.id.img_live_ing);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imgLiveLocation = (ImageView) itemView.findViewById(R.id.icon_live_location);
            // 图标透明度设置为
            imgLiveLocation.getDrawable().setAlpha(20);
        }
    }

    /**
     * 直播回放ViewHolder
     */
    private class LivePlaybackViewHolder extends RecyclerView.ViewHolder {

        ImageView imgLivePlayback;
        TextView tvLivePlaybackTitle;
        TextView tvLivePlaybackLocation;
        TextView tvLivePlaybackPeople;
        TextView tvLivePlaybackDuration;
        ImageView imgPlaybackLocation;
        ImageView imgPlaybackPeople;
        ImageView imgPlaybackTime;

        public LivePlaybackViewHolder(View itemView) {
            super(itemView);

            this.imgLivePlayback = (ImageView) itemView.findViewById(R.id.img_live_playback);
            this.tvLivePlaybackTitle = (TextView) itemView.findViewById(R.id.tv_live_playback_title);
            this.tvLivePlaybackLocation = (TextView) itemView.findViewById(R.id.tv_live_playback_location);
            this.tvLivePlaybackPeople = (TextView) itemView.findViewById(R.id.tv_live_playback_people);
            this.tvLivePlaybackDuration = (TextView) itemView.findViewById(R.id.tv_live_playback_duration);


            imgPlaybackLocation = (ImageView) itemView.findViewById(R.id.icon_playback_location);
            imgPlaybackPeople = (ImageView) itemView.findViewById(R.id.icon_playback_people);
            imgPlaybackTime = (ImageView) itemView.findViewById(R.id.icon_playback_time);
            imgPlaybackLocation.getDrawable().setAlpha(20);
            imgPlaybackPeople.getDrawable().setAlpha(20);
            imgPlaybackTime.getDrawable().setAlpha(20);
        }
    }
}
