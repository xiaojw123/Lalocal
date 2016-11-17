package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.AttentionActivity;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/11/8.
 */
public class HomeLiveAdapter extends RecyclerView.Adapter {

    private static final int MY_CONCERN = 0x01;
    private static final int LIVING_ITEM = 0x02;
    private static final int PLAYBACK_ITEM = 0x03;

    private Context mContext;

    private LiveRowsBean mMyAttention;
    private List<LiveRowsBean> mLivingList = new ArrayList<>();
    private List<LiveRowsBean> mPlaybackList = new ArrayList<>();

    public HomeLiveAdapter(Context context, LiveRowsBean attention, List<LiveRowsBean> livingList, List<LiveRowsBean> playbackList) {
        this.mContext = context;
        this.mMyAttention = attention;
        this.mLivingList = livingList;
        this.mPlaybackList = playbackList;

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
            case LIVING_ITEM:
            case PLAYBACK_ITEM:
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
        if (mMyAttention == null) {
            return mLivingList.size() + mPlaybackList.size();
        }
        return mLivingList.size() + mPlaybackList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMyAttention == null) {
            if (position < mLivingList.size()) {
                return LIVING_ITEM;
            } else {
                return PLAYBACK_ITEM;
            }
        } else {
            if (position == 0) {
                return MY_CONCERN;
            } else if (position <= mLivingList.size()) {
                return LIVING_ITEM;
            } else {
                return PLAYBACK_ITEM;
            }
        }
    }

    /**
     * 我的关注、回放、正在直播，三者共用同一个ViewHolder
     */
    private class ViewHolder extends RecyclerView.ViewHolder {

        // 主标题
        TextView tvTitle;
        // 定位
        TextView tvAddress;
        // 主播ID
        TextView tvNickname;
        // 主播头像
        ImageView imgAvatar;
        // 直播图片
        ImageView imgPhoto;
        // 情况描述
        TextView tvLastMsg;
        // 查看更多
        LinearLayout layoutSeeAll;
        // Item点击范围
        LinearLayout layoutClick;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            imgPhoto = (ImageView) itemView.findViewById(R.id.img_photo);
            tvLastMsg = (TextView) itemView.findViewById(R.id.tv_last_msg);
            layoutClick = (LinearLayout) itemView.findViewById(R.id.layout_click);

            // -我的关注布局
            // 关联“查看全部”控件
            layoutSeeAll = (LinearLayout) itemView.findViewById(R.id.layout_see_all);
            // 如果控件存在
            if (layoutSeeAll != null) {
                // 关键点击事件监听
                layoutSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转我的关注页面查看更多关注
                        mContext.startActivity(new Intent(mContext, AttentionActivity.class));
                    }
                });
            }
        }

        /**
         * 初始化数据
         * @param bean
         */
        public void initData(LiveRowsBean bean) {
            String title = bean.getTitle();
            String address = bean.getAddress();
            String photo = bean.getPhoto();
            String lastMsg = bean.getLastMsg();
            int targetType = bean.getTargetType();
            final int id = bean.getId();
            LiveUserBean user = bean.getUser();
            String avatar = user.getAvatar();
            String nickname = user.getNickName();

            if (TextUtils.isEmpty(title)) {
                title = "没有标题哦~";
            }
            tvTitle.setText(title);

            if (TextUtils.isEmpty(address)) {
                address = "地点找不到啦";
            }
            tvAddress.setText(address);

            if (!TextUtils.isEmpty(photo)) {
                Glide.with(mContext).load(photo).into(imgPhoto);
            }

            if (!TextUtils.isEmpty(lastMsg)) {
                tvLastMsg.setText(lastMsg);
            }

            if (!TextUtils.isEmpty(avatar)) {
                Glide.with(mContext).load(avatar).into(imgAvatar);
            }

            if (TextUtils.isEmpty(nickname)) {
                nickname = "一位不愿意透露...";
            }
            tvNickname.setText(nickname);

            layoutClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent1 = new Intent(mContext, AudienceActivity.class);
                    intent1.putExtra("id", String.valueOf(id));
                    mContext.startActivity(intent1);
                }
            });

        }
    }
}
