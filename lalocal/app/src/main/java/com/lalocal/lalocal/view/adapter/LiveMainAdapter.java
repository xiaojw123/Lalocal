package com.lalocal.lalocal.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveHomePageActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.ScaleImageView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by android on 2016/7/18.
 */
public class LiveMainAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private static final int VIEW_TYPE_HIGHT = 0x23;
    private static final int VIEW_TYPE_HIGHT_TITILE = 0x24;
    private Context mContext;
    private LayoutInflater inflater;
    private List<LiveRowsBean> rowsBeen;
    private int attenIndex;
    private boolean isAtten;
    private XRecyclerView mRecyclerView;

    public LiveMainAdapter(Context context, List<LiveRowsBean> rowsBeen) {
        this.mContext = context;
        this.rowsBeen = rowsBeen;
        inflater = LayoutInflater.from(mContext);
    }

    public void setRecyclerView(XRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void setHightPostion(boolean isAtten, int postion) {
        this.attenIndex = postion;
        this.isAtten = isAtten;
    }


    public void refresh(List<LiveRowsBean> rowsBeen) {
        this.rowsBeen = rowsBeen;
        notifyDataSetChanged();
        AppLog.print("refresh____");
    }


    @Override
    public int getItemViewType(int position) {
        if (isAtten) {
            if (position == attenIndex) {
                return VIEW_TYPE_HIGHT_TITILE;
            } else if (position > attenIndex) {
                return VIEW_TYPE_HIGHT;
            }
        }
        return super.getItemViewType(position);
    }

    //  DrawableUtils.displayImg(mContext, liveViewHodler.liveCompereHeadPortrait, user.getAvatar());
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_HIGHT || viewType == VIEW_TYPE_HIGHT_TITILE) {
            View hightView = inflater.inflate(R.layout.item_highlights, parent, false);
            hightView.setOnClickListener(this);
            if (viewType == VIEW_TYPE_HIGHT_TITILE) {
                LinearLayout titleCotaner = new LinearLayout(mContext);
                titleCotaner.setOrientation(LinearLayout.VERTICAL);
                TextView titleView = getHightTitleView();
                titleCotaner.addView(titleView);
                titleCotaner.addView(hightView);
                holder = new LiveHightHolder(titleCotaner);
            } else {
                holder = new LiveHightHolder(hightView);
            }
        } else {
            View view = inflater.inflate(R.layout.fragment_live_item, parent, false);
            holder = new LiveViewHodler(view);
            view.setOnClickListener(this);
        }
        return holder;
    }

    @NonNull
    private TextView getHightTitleView() {
        TextView titleView = new TextView(mContext);
        titleView.setText("精彩回放");
        Resources resources = mContext.getResources();
        titleView.setTextColor(resources.getColor(R.color.color_1a));
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_size_12_sp));
        titleView.setPadding((int) resources.getDimension(R.dimen.dimen_size_15_dp), (int) resources.getDimension(R.dimen.dimen_size_15_dp), 0, (int) resources.getDimension(R.dimen.dimen_size_5_dp));
        return titleView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (rowsBeen != null && rowsBeen.size() > 0) {
            final LiveRowsBean liveRowsBean = rowsBeen.get(position);
            if (holder instanceof LiveHightHolder) {
                LiveHightHolder hightHolder = (LiveHightHolder) holder;
                if (liveRowsBean != null) {
                    final LiveUserBean user = liveRowsBean.getUser();
                    if (user != null) {
                        DrawableUtils.displayImg(mContext, hightHolder.avatarImg, user.getAvatar());
                        hightHolder.nicknameTv.setText(user.getNickName());
                        String userId = String.valueOf(user.getId());
                        hightHolder.avatarImg.setTag(userId);
                        hightHolder.nicknameTv.setTag(userId);
                        hightHolder.avatarImg.setOnClickListener(this);
                        hightHolder.nicknameTv.setOnClickListener(this);
                    }
                    String addres = liveRowsBean.getAddress();
                    hightHolder.locTv.setText(addres);
                    hightHolder.titleTv.setText(liveRowsBean.getTitle());
                    hightHolder.onlineNumTv.setText(String.valueOf(liveRowsBean.getOnlineNumber()));
                    hightHolder.timeTv.setText(liveRowsBean.getStartAt());
                    hightHolder.locTv.getCompoundDrawables()[0].setAlpha(60);
                    hightHolder.onlineNumTv.getCompoundDrawables()[0].setAlpha(20);
                    hightHolder.timeTv.getCompoundDrawables()[0].setAlpha(20);
                    DrawableUtils.displayImg(mContext, hightHolder.photoImg, liveRowsBean.getPhoto());
                    hightHolder.liveRoomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onLiveItemClickListener != null) {
                                onLiveItemClickListener.goLiveRoom(liveRowsBean);
                            }
                        }
                    });
                }
            } else {
                LiveViewHodler liveViewHodler = (LiveViewHodler) holder;

                if (liveRowsBean == null) {
                    return;
                }

                liveViewHodler.imgLiveType.setImageResource(R.drawable.homelist_living_ic);

                liveViewHodler.layoutLiveContainer.setFocusable(false);
                String title = liveRowsBean.getTitle();
                String address = liveRowsBean.getAddress();
                String photo = liveRowsBean.getPhoto();
                String lastMsg = liveRowsBean.getLastMsg();
                final int targetType = liveRowsBean.getTargetType();
                final int id = liveRowsBean.getId();

                LiveUserBean user = liveRowsBean.getUser();

                String avatar = null;
                String nickname = null;

                if (user != null) {
                    avatar = user.getAvatar();
                    nickname = user.getNickName();
                }

                if (TextUtils.isEmpty(title)) {
                    title = "木有标题哦~";
                }
                liveViewHodler.tvTitle.setText(title);

                if (TextUtils.isEmpty(address)) {
                    address = "地点找不到啦";
                }
                liveViewHodler.tvAddress.setText(address);

                if (!TextUtils.isEmpty(photo)) {
                    Glide.with(mContext)
                            .load(photo)
                            .placeholder(R.drawable.androidloading)
                            .into(liveViewHodler.imgPhoto);
                }

                if (!TextUtils.isEmpty(lastMsg)) {
                    liveViewHodler.tvLastMsg.setVisibility(View.VISIBLE);
                    liveViewHodler.tvLastMsg.setText(lastMsg);
                } else {
                    liveViewHodler.tvLastMsg.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(avatar)) {
                    DrawableUtils.displayImg(mContext, liveViewHodler.imgAvatar, avatar, R.drawable.androidloading);
                }

                if (TextUtils.isEmpty(nickname)) {
                    nickname = "一位不愿意透露...";
                }
                liveViewHodler.tvNickname.setText(nickname);

                // 直播回放标签设置
                if (targetType == Constants.TARGET_TYPE_CHANNEL) {
                    if (liveRowsBean.isChallengeStatus()) {
                        liveViewHodler.tvType.setBackgroundResource(R.drawable.bg_tag_challenge);
                        liveViewHodler.tvType.setText("任务挑战中");
                    } else {
                        liveViewHodler.tvType.setBackgroundResource(R.drawable.bg_tag_living);
                        liveViewHodler.tvType.setText("直播中");
                    }
                } else if (targetType == Constants.PLAY_BACK_TYPE_URL) {
                    liveViewHodler.tvType.setBackgroundResource(R.drawable.bg_tag_playback);
                    liveViewHodler.tvType.setText("回放");
                } else if (targetType == 0) {

                    if (liveRowsBean.getEndAt() != null && liveRowsBean.getStartAt() != null) {
                        liveViewHodler.tvType.setBackgroundResource(R.drawable.bg_tag_playback);
                        liveViewHodler.tvType.setText("回放");
                    } else if (liveRowsBean.isChallengeStatus()) {
                        liveViewHodler.tvType.setBackgroundResource(R.drawable.bg_tag_challenge);
                        liveViewHodler.tvType.setText("任务挑战中");
                    } else {
                        int roomId = liveRowsBean.getRoomId();
                        liveViewHodler.tvType.setBackgroundResource(R.drawable.bg_tag_living);
                        liveViewHodler.tvType.setText("直播中");
                    }
                }

                liveViewHodler.layoutClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 我的关注对回放还是直播进行判断
                        if (targetType == Constants.TARGET_TYPE_CHANNEL) {
                            AppLog.i("sfr", "1");
                            Intent intent1 = new Intent(mContext, AudienceActivity.class);
                            intent1.putExtra("id", String.valueOf(id));
                            ((Activity) mContext).startActivityForResult(intent1, 123);
                        } else if (targetType == Constants.PLAY_BACK_TYPE_URL) {
                            AppLog.i("sfr", "2");
                            Intent intent = new Intent(mContext, PlayBackActivity.class);
                            intent.putExtra("id", String.valueOf(id));
                            ((Activity) mContext).startActivityForResult(intent, 123);
                        }
                    }
                });
            }
        }
    }

    public void gotoLiveHomePage(String userid) {
        Intent intent = new Intent(mContext, LiveHomePageActivity.class);
        intent.putExtra("userId", userid);
        mContext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return rowsBeen == null ? 0 : rowsBeen.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_hightlists_avatar:
            case R.id.item_hightlists_nickname:
                Object tag = v.getTag();
                if (tag != null) {
                    gotoLiveHomePage((String) tag);
                }
                break;
        }


    }

    public class LiveViewHodler extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.img_live_type)
        ImageView imgLiveType;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.icon_my_concern_address)
        ImageView iconMyConcernAddress;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_nickname)
        TextView tvNickname;
        @BindView(R.id.img_avatar)
        RoundedImageView imgAvatar;
        @BindView(R.id.img_photo)
        ScaleImageView imgPhoto;
        @BindView(R.id.tv_last_msg)
        TextView tvLastMsg;
        @BindView(R.id.layout_click)
        LinearLayout layoutClick;
        @BindView(R.id.layout_live_container)
        LinearLayout layoutLiveContainer;

        public LiveViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class LiveHightHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_hightlists_avatar)
        CircleImageView avatarImg;
        @BindView(R.id.item_hightlists_nickname)
        TextView nicknameTv;
        @BindView(R.id.item_hightlists_location)
        TextView locTv;
        @BindView(R.id.item_hightlists_title)
        TextView titleTv;
        @BindView(R.id.item_hightlists_photo)
        ImageView photoImg;
        @BindView(R.id.item_hightlists_onlinenum)
        TextView onlineNumTv;
        @BindView(R.id.item_hightlists_starttime)
        TextView timeTv;
        @BindView(R.id.item_hightlists_liveroom)
        RelativeLayout liveRoomLayout;

        public LiveHightHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //recyclerview条目点击事件
    private OnLiveItemClickListener onLiveItemClickListener;

    public interface OnLiveItemClickListener {
        void goLiveRoom(LiveRowsBean liveRowsBean);
    }

    public void setOnLiveItemClickListener(OnLiveItemClickListener onLiveItemClickListener) {
        this.onLiveItemClickListener = onLiveItemClickListener;
    }
}
