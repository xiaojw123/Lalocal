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
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.live.entertainment.agora.Constant;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.SPCUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/11/8.
 */
public class HomeLiveAdapter extends RecyclerView.Adapter {

    private static final int MY_ATTENTION = 0x01;
    private static final int LIVING_ITEM = 0x02;
    private static final int PLAYBACK_ITEM = 0x03;

    public static final String CREATE_ROOMID = "createRoomId";

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

    /**
     * 刷新我的关注
     * @param attention
     */
    public void refreshMyAttention(LiveRowsBean attention) {
        this.mMyAttention = attention;
        this.notifyDataSetChanged();
    }

    /**
     * 刷新正在直播的列表
     * @param livingList
     */
    public void refreshLivingList(List<LiveRowsBean> livingList) {
        this.mLivingList = livingList;
        this.notifyDataSetChanged();
    }

    /**
     * 刷新直播回放列表
     * @param playbackList
     */
    public void refreshPlaybackList(List<LiveRowsBean> playbackList) {
        this.mPlaybackList = playbackList;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case MY_ATTENTION:
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
        switch (getItemViewType(position)) {
            case MY_ATTENTION:
                ((ViewHolder) holder).initData(mMyAttention);
                break;
            case LIVING_ITEM:
                int livingIndex = getIndex(position);
                ((ViewHolder) holder).initData(mLivingList.get(livingIndex));
                break;
            case PLAYBACK_ITEM:
                int playbackIndex = getIndex(position);
                ((ViewHolder) holder).initData(mPlaybackList.get(playbackIndex));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mMyAttention == null) {
            return mLivingList.size() + mPlaybackList.size();
        }
        return mLivingList.size() + mPlaybackList.size() + 1;
    }

    public int getIndex(int position) {
        int index = position;
        AppLog.i("dailyRec", "position = " + position);
        if (mMyAttention == null) {
            AppLog.i("dailyRec", "1");
            if (index >= mLivingList.size()) {
                AppLog.i("dailyRec", "2");
                index -= mLivingList.size();
                AppLog.i("dailyRec", "living sizeis " + mLivingList.size());
            }
        } else {
            AppLog.i("dailyRec", "3");
            if (index > 0) {
                AppLog.i("dailyRec", "4");
                index--;
            }
            if (index >= mLivingList.size()) {
                AppLog.i("dailyRec", "5");
                index -= mLivingList.size();
                AppLog.i("dailyRec", "playback sizeis " + mPlaybackList.size());
            }
        }
        return index;
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
                return MY_ATTENTION;
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
         *
         * @param bean
         */
        public void initData(final LiveRowsBean bean) {
            String title = bean.getTitle();
            String address = bean.getAddress();
            String photo = bean.getPhoto();
            String lastMsg = bean.getLastMsg();
            final int targetType = bean.getTargetType();
            final int id = bean.getId();
            LiveUserBean user = bean.getUser();
            String avatar = user.getAvatar();
            String nickname = user.getNickName();

            if (TextUtils.isEmpty(title)) {
                title = "木有标题哦~";
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
                tvLastMsg.setVisibility(View.VISIBLE);
                tvLastMsg.setText(lastMsg);
            } else {
                tvLastMsg.setVisibility(View.GONE);
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
                    AppLog.i("itemC", "targetType is " + targetType);
                    // 我的关注对回放还是直播进行判断
                    if (targetType == Constants.TARGET_TYPE_CHANNEL) {
                        Intent intent1 = new Intent(mContext, AudienceActivity.class);
                        intent1.putExtra("id", String.valueOf(id));
                        mContext.startActivity(intent1);
                    } else if (targetType == Constants.PLAY_BACK_TYPE_URL) {
                        Intent intent = new Intent(mContext, PlayBackActivity.class);
                        intent.putExtra("id", String.valueOf(id));
                        mContext.startActivity(intent);
                    } else if (targetType == 0) {

                        if (bean.getEndAt() != null && bean.getStartAt() != null) {
                            Intent intent = new Intent(mContext, PlayBackActivity.class);
                            intent.putExtra("id", String.valueOf(bean.getId()));
                            mContext.startActivity(intent);
                        } else {
                            int roomId = bean.getRoomId();
                            String createRoom = SPCUtils.getString(mContext, CREATE_ROOMID);
                            String s = String.valueOf(roomId);
                            if (createRoom != null && createRoom.equals(s)) {
                                CommonUtil.REMIND_BACK = 1;
                                prepareLive();
                                return;
                            }
                            Intent intent = new Intent(mContext, AudienceActivity.class);
                            intent.putExtra("id", String.valueOf(bean.getId()));
                            mContext.startActivity(intent);
                        }
                    }
                }
            });

        }
    }

    /**
     * 准备创建直播
     */
    private void prepareLive() {
        boolean isLogin = UserHelper.isLogined(mContext);
        if (isLogin) {
            AppLog.i("TAG","登录成功，去开启直播页面");
            mContext.startActivity(new Intent(mContext, LiveActivity.class));
        } else {
            showLoginDialog();}
    }

    /**
     * 显示登录对话框
     */
    private  void  showLoginDialog(){
        CustomChatDialog customDialog = new CustomChatDialog(mContext);
        customDialog.setContent(mContext.getString(R.string.live_login_hint));
        customDialog.setCancelable(false);
        customDialog.setCancelable(false);
        customDialog.setCancelBtn(mContext.getString(R.string.live_canncel), null);
        customDialog.setSurceBtn(mContext.getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                LLoginActivity.start(mContext);
            }
        });
        customDialog.show();
    }
}