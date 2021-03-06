package com.lalocal.lalocal.live.entertainment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.PostVideoActivity;
import com.lalocal.lalocal.activity.RePlyActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.LiveHomePageActivity;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.model.PlayBackResultBean;
import com.lalocal.lalocal.live.entertainment.model.PlayBackReviewRowsBean;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/12/14.
 */
public class PlayBackReviewAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private PlayBackResultBean liveRowsBean;
    private List<PlayBackReviewRowsBean> reviewRows;
    public static final int VIDEO_INFO = 0;
    public static final int REVIEW_TITLE = 1;
    public static final int REVIEW_LIST = 2;
    private boolean isAttention = false;

    public PlayBackReviewAdapter(Context context) {
        this.mContext = context;
    }

    public void setRefreshVideoInfo(PlayBackResultBean liveRowsBean) {
        this.liveRowsBean = liveRowsBean;
        notifyDataSetChanged();
    }

    public void setRefreshAttention(boolean isAttention) {
        this.isAttention = isAttention;
        notifyItemChanged(1);
    }

    public void setRefreshReviewList(List<PlayBackReviewRowsBean> reviewRows) {
        this.reviewRows = reviewRows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIDEO_INFO;
        } else if (position == 1) {
            return REVIEW_TITLE;
        } else {
            return REVIEW_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIDEO_INFO) {
            View infoLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_back_info_item_layout, null);
            return new VideoInfoHolder(infoLayout);
        } else if (viewType == REVIEW_TITLE) {
            View reviewTitle = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_back_review_layout, null);
            return new ReviewTitle(reviewTitle);
        } else if (viewType == REVIEW_LIST) {
            if (reviewRows.size() == 0) {
                View replyNull = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_list_null_layout, null);
                return new ReplyNull(replyNull);
            } else {
                View reviewListLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_back_item_review_layout, null);
                return new ReviewHolder(reviewListLayout);
            }

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIDEO_INFO:
                final VideoInfoHolder videoInfoHolder = (VideoInfoHolder) holder;
                if(liveRowsBean!=null){
                    if (liveRowsBean.getRecommendTitle() != null) {
                        videoInfoHolder.playBackTitle.setText(liveRowsBean.getRecommendTitle());
                        videoInfoHolder.playBackOldTitle.setText("(原标题:" + liveRowsBean.getTitle() + ")");
                    } else {
                        videoInfoHolder.playBackTitle.setText(liveRowsBean.getTitle());
                    }
                    videoInfoHolder.playBackAttentionMaster.setText(isAttention == true ? "已关注" : "关注");
                    if (isAttention) {
                        videoInfoHolder.playBackAttentionMaster.setBackgroundResource(R.drawable.play_back_info);
                        videoInfoHolder.playBackAttentionMaster.setCompoundDrawables(null, null, null, null);
                    } else {
                        videoInfoHolder.playBackAttentionMaster.setBackgroundResource(R.drawable.play_back_info_un_attention_bg);
                        Drawable drawable = mContext.getResources().getDrawable(R.drawable.add);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        videoInfoHolder.playBackAttentionMaster.setCompoundDrawables(drawable, null, null, null);
                    }
                    videoInfoHolder.playBackAttentionMaster.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(UserHelper.isLogined(mContext)){
                                if (onReviewItemClickListener != null && !isAttention) {
                                    isAttention = true;
                                    onReviewItemClickListener.attentionBtnClick(isAttention, videoInfoHolder.playBackAttentionMaster);
                                }
                            }else{
                                if(LiveConstant.USER_INFO_FIRST_CLICK){
                                    LiveConstant.USER_INFO_FIRST_CLICK = false;
                                    showLoginViewDialog();
                                }
                            }

                        }
                    });

                    videoInfoHolder.playBackLocation.setText(liveRowsBean.getAddress());
                    videoInfoHolder.playNum.setText(liveRowsBean.getReadNum() + "次播放");
                    if (liveRowsBean.getDescription() != null) {
                        videoInfoHolder.playBackTitleContent.setText(liveRowsBean.getDescription());
                    }
                    if (liveRowsBean.getUser().getId() == UserHelper.getUserId(mContext)) {//我自己的视频回放
                        videoInfoHolder.playBackAttentionLayout.setVisibility(View.GONE);
                        videoInfoHolder.editLayout.setVisibility(View.VISIBLE);
                        videoInfoHolder.playBackMyName.setText(liveRowsBean.getUser().getNickName());
                        DrawableUtils.loadingImg(mContext, videoInfoHolder.palyBackMyHeadIv, liveRowsBean.getUser().getAvatar());
                        videoInfoHolder.editContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 去编辑标题PostVideoActivity
                                Intent intent = new Intent(mContext, PostVideoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(KeyParams.POST_LOCATION, liveRowsBean.getAddress());
                                bundle.putString(KeyParams.POST_PHOTO, liveRowsBean.getPhoto());
                                bundle.putString(KeyParams.POST_TITLE, liveRowsBean.getTitle());
                                bundle.putInt(KeyParams.POST_HISTORY_ID, liveRowsBean.getId());
                                bundle.putString(KeyParams.POST_VIDEO_INFO, liveRowsBean.getDescription());
                                intent.putExtras(bundle);
                                ((Activity) mContext).startActivityForResult(intent, KeyParams.POST_REQUESTCODE);
                            }
                        });
                    } else {//他人视频
                        videoInfoHolder.playBackAttentionLayout.setVisibility(View.VISIBLE);
                        videoInfoHolder.editLayout.setVisibility(View.GONE);
                        videoInfoHolder.playBackIsMeLayout.setVisibility(View.GONE);
                        DrawableUtils.loadingImg(mContext, videoInfoHolder.palyBackByHeadIv, liveRowsBean.getUser().getAvatar());
                        videoInfoHolder.playBackByName.setText(liveRowsBean.getUser().getNickName());
                        videoInfoHolder.playBackByName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 去个人页面
                                Intent intent = new Intent(mContext, LiveHomePageActivity.class);
                                intent.putExtra("userId", String.valueOf(liveRowsBean.getUser().getId()));
                                mContext.startActivity(intent);
                            }
                        });
                        videoInfoHolder.palyBackByHeadIv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 去个人页面
                                Intent intent = new Intent(mContext, LiveHomePageActivity.class);
                                intent.putExtra("userId", String.valueOf(liveRowsBean.getUser().getId()));
                                mContext.startActivity(intent);
                            }
                        });

                    }
                }

                break;
            case REVIEW_TITLE:
                ReviewTitle reviewTitle = (ReviewTitle) holder;
                reviewTitle.replyTitleLayout.setVisibility(View.VISIBLE);
                reviewTitle.replyWriteIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 去编辑评论页面
                        if(UserHelper.isLogined(mContext)){
                            Intent intent = new Intent(mContext, RePlyActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(KeyParams.REPLY_TITLE, "发起评论");
                            bundle.putInt(KeyParams.REPLY_TYPE, KeyParams.REPLY_TYPE_NEW);
                            bundle.putInt(KeyParams.TARGET_ID, liveRowsBean.getId());
                            bundle.putInt(KeyParams.TARGET_TYPE, Constants.PLAY_BACK_TYPE_URL);
                            intent.putExtras(bundle);
                            ((Activity) mContext).startActivityForResult(intent, KeyParams.REPLY_REQUESTCODE);
                        }else{
                            if(LiveConstant.USER_INFO_FIRST_CLICK){
                                LiveConstant.USER_INFO_FIRST_CLICK=false;
                                showLoginViewDialog();
                            }
                        }

                    }
                });
                break;
            case REVIEW_LIST:
                if(reviewRows.size()==0){
                    try {
                        ReplyNull replyNull=(ReplyNull)holder;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    ReviewHolder reviewHolder = (ReviewHolder) holder;
                    final PlayBackReviewRowsBean playBackReviewRowsBean = reviewRows.get(getPosition(position));

                    reviewHolder.palyBackReviewHeadIv.setTag(playBackReviewRowsBean.getUser().getAvatar());
                    if(reviewHolder.palyBackReviewHeadIv.getTag()!=null&&reviewHolder.palyBackReviewHeadIv.getTag().equals(playBackReviewRowsBean.getUser().getAvatar())){
                       // DrawableUtils.loadingImg(mContext, reviewHolder.palyBackReviewHeadIv, playBackReviewRowsBean.getUser().getAvatar());
                        DrawableUtils.displayImg(mContext, reviewHolder.palyBackReviewHeadIv, playBackReviewRowsBean.getUser().getAvatar());
                    }
                    reviewHolder.reviewName.setText(playBackReviewRowsBean.getUser().getNickName());
                    reviewHolder.reviewTime.setText(playBackReviewRowsBean.getDateTime());
                    reviewHolder.reviewContent.setText(playBackReviewRowsBean.getContent());

                    final LiveUserBean targetUser = playBackReviewRowsBean.getTargetUser();
                    if (targetUser != null) {
                        reviewHolder.reviewReplyTv.setVisibility(View.VISIBLE);
                        PlayBackReviewRowsBean partentComment = playBackReviewRowsBean.getPartentComment();
                        reviewHolder.reviewReplyTv.setText(textviewSetContent(targetUser.getNickName(), partentComment.getContent()));
                    } else {
                        reviewHolder.reviewReplyTv.setVisibility(View.GONE);
                    }
                    reviewHolder.reviewItemLyout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(UserHelper.isLogined(mContext)){
                                if (onReviewItemClickListener != null) {
                                    onReviewItemClickListener.itemClick(playBackReviewRowsBean.getId(), playBackReviewRowsBean.getUser().getId(), playBackReviewRowsBean.getUser().getNickName());
                                }
                            }else{
                                if(LiveConstant.USER_INFO_FIRST_CLICK){
                                    LiveConstant.USER_INFO_FIRST_CLICK=false;
                                    showLoginViewDialog();
                                }

                            }

                        }
                    });
                    reviewHolder.palyBackReviewHeadIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LiveHomePageActivity.class);
                            intent.putExtra("userId",String.valueOf(playBackReviewRowsBean.getUser().getId()));
                            mContext.startActivity(intent);
                        }
                    });
                }

                break;
        }
    }

    private SpannableStringBuilder textviewSetContent(String nickName, String content) {
        int nameLength = nickName.length() + 1;
        int contentLength = content.length();
        SpannableStringBuilder style = new SpannableStringBuilder(nickName + ":" + content);
        style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_0065b2)), 0, nameLength - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#70190f00")), nameLength - 1, nameLength + contentLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    class ReviewTitle extends RecyclerView.ViewHolder {
        @BindView(R.id.review_title)
        TextView reviewTitle;
        @BindView(R.id.reply_write_iv)
        ImageView replyWriteIv;
        @BindView(R.id.reply_title_layout)
        RelativeLayout replyTitleLayout;

        public ReviewTitle(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ReplyNull extends RecyclerView.ViewHolder {
        @BindView(R.id.reply_null_tv)
        TextView replyNullTv;
        public ReplyNull(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class VideoInfoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.play_back_title)
        TextView playBackTitle;
        @BindView(R.id.play_back_old_title)
        TextView playBackOldTitle;
        @BindView(R.id.play_back_title_content)
        TextView playBackTitleContent;
        @BindView(R.id.paly_back_my_head_iv)
        CircleImageView palyBackMyHeadIv;
        @BindView(R.id.play_back_my_name)
        TextView playBackMyName;
        @BindView(R.id.play_back_is_me_layout)
        LinearLayout playBackIsMeLayout;
        @BindView(R.id.edit_content)
        TextView editContent;
        @BindView(R.id.paly_back_by_head_iv)
        CircleImageView palyBackByHeadIv;
        @BindView(R.id.play_back_by_name)
        TextView playBackByName;
        @BindView(R.id.play_back_attention_master)
        TextView playBackAttentionMaster;
        @BindView(R.id.play_back_attention_layout)
        LinearLayout playBackAttentionLayout;
        @BindView(R.id.play_back_location)
        TextView playBackLocation;
        @BindView(R.id.play_num)
        TextView playNum;
        @BindView(R.id.edit_layout)
        RelativeLayout editLayout;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void showLoginViewDialog() {
            final CustomChatDialog customDialog = new CustomChatDialog(mContext);
            customDialog.setContent(mContext.getString(R.string.live_login_hint));
            customDialog.setCancelable(false);
            customDialog.setCancelBtn(mContext.getString(R.string.live_canncel), null);
            customDialog.setSurceBtn(mContext.getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    LLoginActivity.startForResult(mContext, 701);
                }
            });
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    LiveConstant.USER_INFO_FIRST_CLICK = true;
                }
            });
            customDialog.show();
        }


    class ReviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.paly_back_review_head_iv)
        CircleImageView palyBackReviewHeadIv;
        @BindView(R.id.review_name)
        TextView reviewName;
        @BindView(R.id.review_time)
        TextView reviewTime;
        @BindView(R.id.review_like)
        TextView reviewLike;
        @BindView(R.id.review_content)
        TextView reviewContent;
        @BindView(R.id.review_reply_tv)
        TextView reviewReplyTv;
        @BindView(R.id.review_item_layout)
        LinearLayout reviewItemLyout;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public int getItemCount() {
        if (liveRowsBean == null && reviewRows == null) {
            return 0;
        } else if (liveRowsBean == null && reviewRows != null) {
            return reviewRows.size() + 1;
        } else if (liveRowsBean != null && reviewRows == null) {
            return 2;
        } else if (liveRowsBean != null && reviewRows != null && reviewRows.size() == 0) {
            return 3;
        } else {
            return reviewRows.size() + 2;
        }
    }

    private int getPosition(int pos) {
        if (liveRowsBean != null && reviewRows != null) {
            return pos - 2;
        }
        return 0;
    }


    //recyclerview条目点击事件
    private OnReviewItemClickListener onReviewItemClickListener;

    public interface OnReviewItemClickListener {
        void itemClick(int targetId, int userId, String userName);

        void attentionBtnClick(boolean isAttention, TextView textView);
    }

    public void setOnReviewItemClickListener(OnReviewItemClickListener onReviewItemClickListener) {
        this.onReviewItemClickListener = onReviewItemClickListener;
    }

}
