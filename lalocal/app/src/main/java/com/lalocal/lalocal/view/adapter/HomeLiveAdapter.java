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
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.CategoryBean;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.viewholder.live.ADViewHolder;
import com.lalocal.lalocal.view.viewholder.live.AttentionViewHolder;
import com.lalocal.lalocal.view.viewholder.live.CategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/11/8.
 */
public class HomeLiveAdapter extends RecyclerView.Adapter {

    private static final int ADVERTISEMENT = 0x01;
    private static final int MY_ATTENTION = 0x02;
    private static final int CATEGORY = 0x03;
    private static final int LIVING_ITEM = 0x04;
    private static final int PLAYBACK_ITEM = 0x05;

    public static final String CREATE_ROOMID = "createRoomId";

    private Context mContext;

    private LiveUserBean mAttentionUser;
    private List<RecommendAdResultBean> mAdList = new ArrayList<>();
    private List<CategoryBean> mCategoryList = new ArrayList<>();
    private List<LiveRowsBean> mLivingList = new ArrayList<>();
    private List<LiveRowsBean> mPlaybackList = new ArrayList<>();

    // 分类项点击事件
    private CategoryAdapter.MyOnItemClickListener mCategoryItemListener;

    private RecyclerView mRvTopCategory;

    // 分类栏选中状态
    private int mCateSelected = 0;

    // 分类栏Viewholder
    private RecyclerView.ViewHolder mCategoryHolder;

    public HomeLiveAdapter(Context context, List<RecommendAdResultBean> adList, LiveUserBean attention,
                           List<CategoryBean> categoryList, int cateSelected, List<LiveRowsBean> livingList, List<LiveRowsBean> playbackList,
                           CategoryAdapter.MyOnItemClickListener categoryItemListener, RecyclerView rvTopCategory) {
        AppLog.i("lsck", "listener is " + (categoryItemListener == null ? "null" : "not null"));
        this.mContext = context;
        this.mAttentionUser = attention;
        this.mRvTopCategory = rvTopCategory;
        this.mCateSelected = cateSelected;

        if (adList != null && adList.size() > 0) {
            this.mAdList = adList;
        }
        if (categoryList != null && categoryList.size() > 0) {
            this.mCategoryList = categoryList;
        }
        if (livingList != null && livingList.size() > 0) {
            this.mLivingList = livingList;
        }
        if (playbackList != null && playbackList.size() > 0) {
            this.mPlaybackList = playbackList;
        }
        this.mCategoryItemListener = categoryItemListener;
    }

    /**
     * 刷新我的关注
     *
     * @param attention
     */
    public void refreshMyAttention(LiveUserBean attention) {
        this.mAttentionUser = attention;
        this.notifyDataSetChanged();
    }

    /**
     * 刷新正在直播的列表
     *
     * @param livingList
     */
    public void refreshLivingList(List<LiveRowsBean> livingList) {
        this.mLivingList = livingList;
        this.notifyDataSetChanged();
    }

    /**
     * 刷新直播回放列表
     *
     * @param playbackList
     */
    public void refreshPlaybackList(List<LiveRowsBean> playbackList) {
        this.mPlaybackList = playbackList;
        this.notifyDataSetChanged();
    }

    /**
     * 刷新直播和回放
     * @param livingList
     * @param playbackList
     */
    public void refreshLive(List<LiveRowsBean> livingList, List<LiveRowsBean> playbackList) {
        AppLog.i("sls", "refreshLive");
        this.mLivingList = livingList;
        this.mPlaybackList = playbackList;
        this.notifyDataSetChanged();
    }

    /**
     * 刷新所有
     * @param adList
     * @param attention
     * @param categoryList
     * @param livingList
     * @param playbackList
     */
    public void refreshAll(List<RecommendAdResultBean> adList, LiveUserBean attention, List<CategoryBean> categoryList,
                           int cateSelected, List<LiveRowsBean> livingList, List<LiveRowsBean> playbackList) {
        this.mAdList = adList;
        this.mAttentionUser = attention;
        this.mCategoryList = categoryList;
        this.mLivingList = livingList;
        this.mPlaybackList = playbackList;
        this.mCateSelected = cateSelected;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case ADVERTISEMENT:
                view = LayoutInflater.from(mContext).inflate(R.layout.live_advertisement_slider_item, parent, false);
                holder = new ADViewHolder(mContext, view, (XRecyclerView)parent);
                break;
            case MY_ATTENTION:
                view = LayoutInflater.from(mContext).inflate(R.layout.live_my_attention_item, parent, false);
                holder = new AttentionViewHolder(mContext, view);
                break;
            case CATEGORY:
                AppLog.i("sls", "CATEGORY ONcREATE");
                // 保存分类栏，防止其中的RecyclerView的滑动距离未保存
                if (mCategoryHolder == null) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.live_categories, parent, false);
                    mCategoryHolder = new CategoryViewHolder(mContext, view, mRvTopCategory);
                    mCategoryHolder.setIsRecyclable(false);
                }
                holder = mCategoryHolder;
                break;
            case LIVING_ITEM:
            case PLAYBACK_ITEM:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_item, parent, false);
                holder = new LiveViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ADVERTISEMENT:
                ((ADViewHolder)holder).initData(mAdList);
                break;
            case MY_ATTENTION:
                ((AttentionViewHolder) holder).initData(mAttentionUser);
                break;
            case CATEGORY:
                AppLog.i("sls", "CAETGORY onBIndV");
                ((CategoryViewHolder) holder).initData(mCategoryList, mCateSelected, mCategoryItemListener);
                break;
            case LIVING_ITEM:
                int livingIndex = getIndex(position);
                ((LiveViewHolder) holder).initData(mLivingList.get(livingIndex));
                break;
            case PLAYBACK_ITEM:
                int playbackIndex = getIndex(position);
                ((LiveViewHolder) holder).initData(mPlaybackList.get(playbackIndex));
                break;
        }
    }

    @Override
    public int getItemCount() {
        int preCount = getPreCount();
        return preCount + mLivingList.size() + mPlaybackList.size();
    }

    public int getIndex(int position) {
        int index = position;
        int preCount = getPreCount();
        if (preCount <= index) {
            index -= preCount;
            if (index >= mLivingList.size()) {
                index -= mLivingList.size();
                // 以重复加载最后一项的缺陷弥补崩溃
                if (index >= mPlaybackList.size()) {
                    return mPlaybackList.size() - 1;
                }
            }
            return index;
        }
        return 0;
    }

    /**
     * 获取直播首页非直播、回放的item数量（即：广告、我的关注、分类栏）
     * @return
     */
    private int getPreCount() {
        int preCount = 1;
        if (mAdList.size() > 0) {
            preCount++;
        }
        if (mAttentionUser != null) {
            preCount++;
        }
        return preCount;
    }

    /**
     * 根据对应的bean和list是否有数据对item的类型进行判断
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        AppLog.i("pss", "pos is " + position);
        if (position < 0) {
            return -1;
        }

        List<Integer> mTypes = new ArrayList<>();
        if (mAdList.size() > 0) {
            mTypes.add(ADVERTISEMENT);
        }
        if (UserHelper.isLogined(mContext)) {
            mTypes.add(MY_ATTENTION);
        }
        mTypes.add(CATEGORY);
        if (mLivingList.size() > 0) {
            mTypes.add(LIVING_ITEM);
        }

        // 防止崩溃信息的发生
        if (position >= mTypes.size() + mPlaybackList.size()) {
            return -1;
        }

        if (position >= mTypes.size()) {
            return PLAYBACK_ITEM;
        } else {
            return mTypes.get(position);
        }

    }

    /**
     * 回放、正在直播共用同一个ViewHolder
     */
    private class LiveViewHolder extends RecyclerView.ViewHolder {

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
        // 播放类型
        TextView tvType;
        // Item点击范围
        LinearLayout layoutClick;

        public LiveViewHolder(View itemView) {
            super(itemView);

            itemView.setFocusable(false);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            imgPhoto = (ImageView) itemView.findViewById(R.id.img_photo);
            tvLastMsg = (TextView) itemView.findViewById(R.id.tv_last_msg);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            layoutClick = (LinearLayout) itemView.findViewById(R.id.layout_click);

        }

        /**
         * 初始化数据
         *
         * @param bean
         */
        public void initData(final LiveRowsBean bean) {
            if(bean==null){
                return;
            }

            String title = bean.getTitle();
            String address = bean.getAddress();
            String photo = bean.getPhoto();
            String lastMsg = bean.getLastMsg();
            final int targetType = bean.getTargetType();
            final int id = bean.getId();

            LiveUserBean user = bean.getUser();
            String avatar =null;
            String nickname=null;
            if(user!=null){
                avatar = user.getAvatar();
                nickname = user.getNickName();
            }

            if (TextUtils.isEmpty(title)) {
                title = "木有标题哦~";
            }
            tvTitle.setText(title);

            if (TextUtils.isEmpty(address)) {
                address = "地点找不到啦";
            }
            tvAddress.setText(address);

            if (!TextUtils.isEmpty(photo)) {
                Glide.with(mContext)
                        .load(photo)
                        .placeholder(R.drawable.androidloading)
                        .into(imgPhoto);
            }

            if (!TextUtils.isEmpty(lastMsg)) {
                tvLastMsg.setVisibility(View.VISIBLE);
                tvLastMsg.setText(lastMsg);
            } else {
                tvLastMsg.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(avatar)) {
                Glide.with(mContext)
                        .load(avatar)
                        .placeholder(R.drawable.androidloading)
                        .into(imgAvatar);
            }

            if (TextUtils.isEmpty(nickname)) {
                nickname = "一位不愿意透露...";
            }
            tvNickname.setText(nickname);

            // 直播回放标签设置
            if (targetType == Constants.TARGET_TYPE_CHANNEL) {
                if (bean.isChallengeStatus()) {
                    tvType.setBackgroundResource(R.drawable.bg_tag_challenge);
                    tvType.setText("任务挑战中");
                } else {
                    tvType.setBackgroundResource(R.drawable.bg_tag_living);
                    tvType.setText("直播中");
                }
            } else if (targetType == Constants.PLAY_BACK_TYPE_URL) {
                tvType.setBackgroundResource(R.drawable.bg_tag_playback);
                tvType.setText("精彩回放");
            } else if (targetType == 0) {

                if (bean.getEndAt() != null && bean.getStartAt() != null) {
                    tvType.setBackgroundResource(R.drawable.bg_tag_playback);
                    tvType.setText("精彩回放");
                } else if (bean.isChallengeStatus()) {
                    tvType.setBackgroundResource(R.drawable.bg_tag_challenge);
                    tvType.setText("任务挑战中");
                } else {
                    int roomId = bean.getRoomId();
                    tvType.setBackgroundResource(R.drawable.bg_tag_living);
                    tvType.setText("直播中");
                }
            }

            layoutClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            mContext.startActivity(new Intent(mContext, LiveActivity.class));
        } else {
            showLoginDialog();
        }
    }

    /**
     * 显示登录对话框
     */
    private void showLoginDialog() {
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