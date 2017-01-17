package com.lalocal.lalocal.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.LiveDetailActivity;
import com.lalocal.lalocal.activity.MyLiveActivity;
import com.lalocal.lalocal.activity.VideoContinueUploadActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackDetailActivity;
import com.lalocal.lalocal.live.entertainment.model.PostShortVideoParameterBean;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/10/11.
 */

public class MyLiveAdapter extends BaseRecyclerAdapter {

    public static String FOMRAT_TIME = "%1$s:%2$s:%3$s";

    Context mContext;
    List<LiveRowsBean> mItems;
    List<PostShortVideoParameterBean> all;
    Resources res;
    ContentLoader mContentLoader;
    XRecyclerView mRecyclerView;

    public static final int WAIT_POST_VIDEO = 1;
    public static final int HISTORY_VIDEO = 2;



    public MyLiveAdapter(XRecyclerView recyclerView, ContentLoader contentLoader, List<LiveRowsBean> items, List<PostShortVideoParameterBean> all) {
        mItems = items;
        mContentLoader = contentLoader;
        mRecyclerView = recyclerView;
        this.all = all;

    }

    public void updateItems(List<LiveRowsBean> items) {
        mItems = items;
        notifyDataSetChanged();
    }
    public  void updateDataBaseItmes(List<PostShortVideoParameterBean> alls){
        all=alls;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        res = mContext.getResources();
        if (viewType == WAIT_POST_VIDEO) {
            View mybg = LayoutInflater.from(mContext).inflate(R.layout.item_delete, null);
            mybg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //获取item布局
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.wait_post_video_item, null);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //生成返回RecyclerView.ViewHolder
            return new ItemWaitPostHolder(mContext, mybg, itemView, RecyclerViewDragHolder.EDGE_RIGHT, (int) res.getDimension(R.dimen.item_my_live_layout_height)).getDragViewHolder();
        } else {
            View mybg = LayoutInflater.from(mContext).inflate(R.layout.item_delete, null);
            mybg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //获取item布局
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_my_live, null);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //生成返回RecyclerView.ViewHolder
            return new ItemLiveHolder(mContext, mybg, itemView, RecyclerViewDragHolder.EDGE_RIGHT, (int) res.getDimension(R.dimen.item_my_live_layout_height)).getDragViewHolder();

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AppLog.d("TAG", "po是:" + position + "   getItemViewType(position):" + getItemViewType(position));
        if (getItemViewType(position) == HISTORY_VIDEO) {
            if (mItems != null) {
                final LiveRowsBean item = mItems.get(getPositon(position));
                if (item != null) {
                    ItemLiveHolder itemHolder = (ItemLiveHolder) RecyclerViewDragHolder.getHolder(holder);
                    itemHolder.locationTv.getCompoundDrawables()[0].setAlpha(20);
                    itemHolder.onlineNumTv.getCompoundDrawables()[0].setAlpha(20);
                    itemHolder.liveLenTv.getCompoundDrawables()[0].setAlpha(20);
                    DrawableUtils.displayImg(mContext, itemHolder.postImg, item.getPhoto());
                    itemHolder.titleTv.setText(item.getTitle());
                    String adddres = item.getAddress();
                    String onlienNum = String.valueOf(item.getOnlineNumber());
                    String startAt = item.getStartAt();
                    String endAt = item.getEndAt();
                    String toalScore = String.valueOf(item.getTotalScore());
                    itemHolder.locationTv.setText(adddres);
                    itemHolder.onlineNumTv.setText(onlienNum);
                    itemHolder.liveLenTv.setText(startAt);
//                String liveLen = "00:00:00";
//                if (!TextUtils.isEmpty(startAt) && !TextUtils.isEmpty(endAt)) {
//                    liveLen = getLiveLen(startAt, endAt);
//                    itemHolder.liveLenTv.setText(liveLen);
//                }
                    if (!TextUtils.isEmpty(toalScore)) {
                        itemHolder.scoreNumTv.setText(toalScore);

                    }
                    if (!TextUtils.isEmpty(endAt)) {
                        item.setDate(endAt.substring(0, 10));
                    }
                    itemHolder.viewdetailsTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MobHelper.sendEevent(mContext, MobEvent.MY_LIVE_DETAIL);
                            if (item != null) {
                                Intent intent = new Intent(mContext, LiveDetailActivity.class);
                                intent.putExtra(LiveDetailActivity.LIVE_ITEM, item);
                                mContext.startActivity(intent);
                            }
                        }
                    });
                    itemHolder.myLiveCotainer.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PlayBackDetailActivity.class);
                            intent.putExtra("id", String.valueOf(item.getId()));
                            mContext.startActivity(intent);

                        }
                    });

                    itemHolder.deleteFl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            remove(position);
                        }
                    });
                }
            }

        } else {
            ItemWaitPostHolder waitPostHolder = (ItemWaitPostHolder) RecyclerViewDragHolder.getHolder(holder);
            final PostShortVideoParameterBean bean = all.get(position);
            waitPostHolder.uploadProgress.setProgress(bean.getProgress());
            waitPostHolder.uploadContent.setText(bean.getDescription());
            waitPostHolder.uploadHintTitle.setText(bean.getTitle());
            waitPostHolder.uploadTime.setText(bean.getDuration());
            waitPostHolder.uploadLocation.setText(bean.getAddress());
            waitPostHolder.itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeWait(bean.getFilename(), position);
                }
            });
            waitPostHolder.videoContinueCover.setImageBitmap(BitmapFactory.decodeByteArray(bean.getBytesImg(),0,bean.getBytesImg().length));
            waitPostHolder.postStatusIv.setVisibility(View.VISIBLE);
            waitPostHolder.waitPostContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, VideoContinueUploadActivity.class);
                    intent.putExtra(KeyParams.SHORT_VIDEO_PARAMETER, bean);
                    ((Activity) mContext).startActivityForResult(intent, MyLiveActivity.VIDEO_PREVIEW_REQUESTCODE);
                }
            });
        }

    }

    private int getPositon(int pos) {
        if (all != null && all.size() > 0) {
            return pos - all.size();
        }
        return pos;

    }

    private void removeWait(String filename, int pos) {
        AppLog.d("TAG", "vvvvvvvv pos:" + pos);
        DataSupport.deleteAll(PostShortVideoParameterBean.class, "filename=?", filename);
        all.remove(pos);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (all == null && mItems == null) {
            return 0;
        } else if (all == null && mItems != null) {
            return mItems.size();
        } else if (mItems == null && all != null) {
            return all.size();
        } else {
            return all.size() + mItems.size();
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (all != null) {
            if (position < all.size()) {
                return WAIT_POST_VIDEO;
            } else {
                return HISTORY_VIDEO;
            }
        } else {
            return HISTORY_VIDEO;
        }
    }

    public void remove(int position) {
        LiveRowsBean item = mItems.get(getPositon(position));
        AppLog.print("liveAdapter remove___position___" + position + ",id__" + item.getId());
        mContentLoader.deleteLiveHistory(item.getId());
        mItems.remove(position);
        notifyDataSetChanged();
//        notifyItemRangeChanged(position, getItemCount());
    }

    class ItemWaitPostHolder extends RecyclerViewDragHolder {
        @BindView(R.id.video_continue_cover)
        ImageView videoContinueCover;
        @BindView(R.id.upload_progress)
        ProgressBar uploadProgress;
        @BindView(R.id.upload_cover_layout)
        RelativeLayout uploadCoverLayout;
        @BindView(R.id.upload_hint_title)
        TextView uploadHintTitle;
        @BindView(R.id.upload_location)
        TextView uploadLocation;
        @BindView(R.id.upload_time)
        TextView uploadTime;
        @BindView(R.id.upload_content)
        TextView uploadContent;
        @BindView(R.id.post_status_iv)
        ImageView postStatusIv;
        @BindView(R.id.wait_post_container)
        FrameLayout waitPostContainer;
        @BindView(R.id.item_delete)
        FrameLayout itemDelete;

        public ItemWaitPostHolder(Context context, View bgView, View topView, int mTrackingEdges, int height) {
            super(context, bgView, topView, mTrackingEdges, height);
        }

        @Override
        public void initView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemLiveHolder extends RecyclerViewDragHolder {
        @BindView(R.id.item_my_live_img)
        ImageView postImg;
        @BindView(R.id.item_my_live_title)
        TextView titleTv;
        @BindView(R.id.item_my_live_location)
        TextView locationTv;
        @BindView(R.id.item_my_live_onlinenum)
        TextView onlineNumTv;
        @BindView(R.id.item_my_live_scorenum)
        TextView scoreNumTv;
        @BindView(R.id.item_my_live_livelen)
        TextView liveLenTv;
        @BindView(R.id.item_my_live_viewdetails)
        TextView viewdetailsTv;
        @BindView(R.id.item_delete)
        FrameLayout deleteFl;
        @BindView(R.id.item_my_live_container)
        ViewGroup myLiveCotainer;
        @BindString(R.string.view_details)
        String viewDetailsStr;


        public ItemLiveHolder(Context context, View bgView, View topView, int mTrackingEdges, int height) {
            super(context, bgView, topView, mTrackingEdges, height);
        }

        @Override
        public void initView(View itemView) {
            ButterKnife.bind(this, itemView);
            viewdetailsTv.setText(Html.fromHtml("<u>" + viewDetailsStr + "</u>"));
        }
    }


}
