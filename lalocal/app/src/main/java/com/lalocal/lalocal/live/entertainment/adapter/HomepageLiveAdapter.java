package com.lalocal.lalocal.live.entertainment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.ScaleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/10/12.
 */
public class HomepageLiveAdapter extends RecyclerView.Adapter {

    // 不同类型布局标记
    private static final int TYPE_LIVE_ING = 0x01;
    private static final int TYPE_LIVE_PLAYBACK = 0x02;

    // 上下文
    private Context mContext;
    // 正在直播
    private LiveRowsBean mLiving;
    // 历史直播
    private List<LiveRowsBean> mUserLiveList;

    // 当前主页是否为自己的标记
    private boolean mIsSelf = false;

    public HomepageLiveAdapter(Context context, boolean isSelf, LiveRowsBean living, List<LiveRowsBean> userLiveList) {
        AppLog.i("ttt", "LIVE_init HomepageLiveAdapter");
        this.mContext = context;
        this.mIsSelf = isSelf;
        this.mLiving = living;
        this.mUserLiveList = userLiveList;
        this.mIsSelf = isSelf;

        if (mUserLiveList == null) {
            mUserLiveList = new ArrayList<>();
        }
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
        switch (getItemViewType(position)) {
            case TYPE_LIVE_ING:
                if (mLiving == null && mUserLiveList.size() > 0){
                    ((LivingViewHolder) holder).initView(false, null, true);
                } else if (mLiving == null && mUserLiveList.size() == 0) {
                    ((LivingViewHolder) holder).initView(false, null, false);
                } else if (mLiving != null && mUserLiveList.size() == 0) {
                    ((LivingViewHolder) holder).initView(true, mLiving, false);
                } else if (mLiving != null && mUserLiveList.size() > 0){
                    ((LivingViewHolder) holder).initView(true, mLiving, true);
                }
                break;
            case TYPE_LIVE_PLAYBACK:
                LiveRowsBean bean = mUserLiveList.get(position - 1);
                if (position == mUserLiveList.size()) {
                    ((LivePlaybackViewHolder) holder).initView(bean, true);
                } else {
                    ((LivePlaybackViewHolder) holder).initView(bean, false);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mUserLiveList.size() > 0) {
            return mUserLiveList.size() + 1;
        }
        return mLiving == null ? 0 : 1;
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
        // 直播标题
        TextView tvLivingTitle;
        // 直播定位
        TextView tvLivingLocation;
        // 回放标题
        TextView tvLivePlayBack;
        // 直播图片
        ScaleImageView imgLiving;
        // 上一次信息
        TextView tvLastMsg;
        // 当前直播视图容器
        LinearLayout liveClick;

        public LivingViewHolder(View itemView) {
            super(itemView);

            // -关联控件
            tvLivingTitle = (TextView) itemView.findViewById(R.id.tv_live_ing_title);
            tvLivePlayBack = (TextView) itemView.findViewById(R.id.tv_live_playback);
            tvLivingLocation = (TextView) itemView.findViewById(R.id.tv_live_ing_location);
            imgLiving = (ScaleImageView) itemView.findViewById(R.id.img_live_ing);
            tvLastMsg = (TextView) itemView.findViewById(R.id.tv_last_msg);
            liveClick = (LinearLayout) itemView.findViewById(R.id.layout_click);
        }

        /**
         * 初始化视图，判断是否显示当前直播
         * @param isShowLiving
         * @param liveBean
         * @param isShowTitle
         */
        public void initView(boolean isShowLiving, final LiveRowsBean liveBean, boolean isShowTitle) {
            if (isShowLiving) {
                // 设置当前直播视图可见
                liveClick.setVisibility(View.VISIBLE);

                String title = null;
                String location = null;
                String picUrl = null;
                String lastMsg = null;
                if (liveBean != null) {
                    // 获取当前直播标题
                    title = liveBean.getTitle();
                    // 获取定位信息
                    location = liveBean.getAddress();
                    // 获取直播图片
                    picUrl = liveBean.getPhoto();
                    // 获取上一次信息
                    lastMsg = liveBean.getLastMsg();
                }

                // -空数据处理
                // 标题
                if (TextUtils.isEmpty(title)) {
                    title = "获取标题失败";
                }
                // 定位
                if (TextUtils.isEmpty(location)) {
                    location = "定位失败";
                }
                // 直播图片
                if (TextUtils.isEmpty(picUrl) && liveBean != null && liveBean.getUser() != null) {
                    picUrl = liveBean.getUser().getAvatarOrigin();
                }

                // -视图数据填充
                // 标题
                tvLivingTitle.setText(title);
                // 定位
                tvLivingLocation.setText(location);
                // 图片
                if (!TextUtils.isEmpty(picUrl)) {
                    DrawableUtils.displayRadiusImg(mContext, imgLiving, picUrl,
                            DensityUtil.dip2px(mContext, 3), R.drawable.androidloading);
                }
                // 上一次信息
                if (!TextUtils.isEmpty(lastMsg)) {
                    tvLastMsg.setText(lastMsg);
                    tvLastMsg.setVisibility(View.VISIBLE);
                } else {
                    tvLastMsg.setVisibility(View.GONE);
                }

            } else {
                liveClick.setVisibility(View.GONE);
            }

            // 判断是否显示直播的提示
            if (isShowTitle) {
                // 显示回放直播的提示
                tvLivePlayBack.setVisibility(View.VISIBLE);
            } else {
                // 隐藏回放直播的提示
                tvLivePlayBack.setVisibility(View.GONE);
            }

            // 点击事件回调
            liveClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如果是自己在直播
                    if (mIsSelf) {
                        ((Activity)mContext).finish();
                        return;
                    }

                    Intent intent=new Intent(mContext, AudienceActivity.class);
                    intent.putExtra("id",String.valueOf(liveBean.getId()));
                    mContext.startActivity(intent);

                }
            });
        }
    }

    /**
     * 直播回放ViewHolder
     */
    private class LivePlaybackViewHolder extends RecyclerView.ViewHolder {

        // 最外层layout
        LinearLayout itemContainer;
        // 直播回放图片
        ImageView imgLivePlayback;
        // 直播回放标题
        TextView tvLivePlaybackTitle;
        // 直播回放定位
        TextView tvLivePlaybackLocation;
        // 直播回放浏览人数
        TextView tvLivePlaybackPeople;
        // 直播回放时长
        TextView tvLivePlaybackTime;
        // 分割线
        View divider;

        public LivePlaybackViewHolder(View itemView) {
            super(itemView);

            // 关联控件
            this.itemContainer = (LinearLayout) itemView.findViewById(R.id.container);
            this.imgLivePlayback = (ImageView) itemView.findViewById(R.id.img_live_playback);
            this.tvLivePlaybackTitle = (TextView) itemView.findViewById(R.id.tv_live_playback_title);
            this.tvLivePlaybackLocation = (TextView) itemView.findViewById(R.id.tv_live_playback_location);
            this.tvLivePlaybackPeople = (TextView) itemView.findViewById(R.id.tv_live_playback_people);
            this.tvLivePlaybackTime = (TextView) itemView.findViewById(R.id.tv_live_playback_time);
            this.divider = itemView.findViewById(R.id.divider);
        }

        public void initView(final LiveRowsBean bean, boolean isBottom) {

            String photo = null;
            String title = null;
            String location = null;
            String people = null;
            String startAt = null;

            if (bean != null) {
                // 获取直播预览图
                photo = bean.getPhoto();
                // 获取直播标题
                title = bean.getTitle();
                // 获取直播定位
                location = bean.getAddress();
                // 获取在线人数
                people = String.valueOf(bean.getOnlineNumber());
                // 获取开始时间
                startAt = bean.getStartAt();
            }

            // -空数据处理
            if (TextUtils.isEmpty(photo)) {
                photo = bean.getUser().getAvatarOrigin();
            }
            if (TextUtils.isEmpty(title)) {
                title = "主播没有添加标题哦";
            }
            if (TextUtils.isEmpty(location)) {
                location = "获取不到定位信息";
            }
            if (TextUtils.isEmpty(people)) {
                people = "0";
            }
            if (TextUtils.isEmpty(startAt)) {
                startAt = "获取播放时间失败";
            } else if (startAt.contains(":") && startAt.substring(startAt.indexOf(":")).contains(":")) { // 如果开始时间包含两个“:”
                // 去掉秒数
                startAt = startAt.substring(0, startAt.lastIndexOf(":"));
            }

            // -数据填充
            DrawableUtils.displayImg(mContext, imgLivePlayback, photo);
            tvLivePlaybackTitle.setText(title);
            tvLivePlaybackLocation.setText(location);
            tvLivePlaybackPeople.setText(people);
            tvLivePlaybackTime.setText(startAt);
            if (isBottom) {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemContainer.getLayoutParams();
                lp.setMargins(0, 0, 0, DensityUtil.dip2px(mContext, 15));
                itemContainer.setLayoutParams(lp);
                divider.setVisibility(View.GONE);
            } else {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemContainer.getLayoutParams();
                lp.setMargins(0, 0, 0, 0);
                itemContainer.setLayoutParams(lp);
                divider.setVisibility(View.VISIBLE);
            }

            // 点击事件回调
            itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContentLoader loader = new ContentLoader(mContext);
                    loader.setCallBack(new MyCallBack());
                    int id = bean.getId();
                    loader.getPlayBackLiveDetails(id);
//                    AppLog.i("ttttt", "LiveRowsBean:[id:" + bean.getId() + ";title:" + bean.getTitle()
//                        +";photo:"+bean.getPhoto()+";userId:"+bean.getUser().getId()+";nickName:"+bean.getUser().getNickName());
//                    PlayBackActivity.start(mContext, bean);
                }
            });
        }
    }

    private class MyCallBack extends ICallBack {

        @Override
        public void onPlayBackDetails(LiveRowsBean liveRowsBean) {
            super.onPlayBackDetails(liveRowsBean);
            if (liveRowsBean != null) {
                Intent intent=new Intent(mContext,PlayBackActivity.class);
                intent.putExtra("id",String.valueOf(liveRowsBean.getId()));
                mContext.startActivity(intent);
            }
        }
    }
}
