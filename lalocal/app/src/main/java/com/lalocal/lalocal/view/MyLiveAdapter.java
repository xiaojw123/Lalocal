package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.LiveDetailActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.adapter.BaseRecyclerAdapter;
import com.lalocal.lalocal.view.adapter.RecyclerViewDragHolder;

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
    Resources res;
    ContentLoader mContentLoader;
    XRecyclerView mRecyclerView;

    public MyLiveAdapter(XRecyclerView recyclerView, ContentLoader contentLoader, List<LiveRowsBean> items) {
        mItems = items;
        mContentLoader = contentLoader;
        mRecyclerView = recyclerView;
    }

    public void updateItems(List<LiveRowsBean> items) {
        mItems = items;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        res = mContext.getResources();
        View mybg = LayoutInflater.from(mContext).inflate(R.layout.list_item_delete, null);
        mybg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //获取item布局
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_my_live, null);
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //生成返回RecyclerView.ViewHolder
        return new ItemLiveHolder(mContext, mybg, itemView, RecyclerViewDragHolder.EDGE_RIGHT, (int) res.getDimension(R.dimen.item_my_live_layout_height)).getDragViewHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mItems != null) {
            final LiveRowsBean item = mItems.get(position);
            if (item != null) {
                ItemLiveHolder itemHolder = (ItemLiveHolder) RecyclerViewDragHolder.getHolder(holder);
                DrawableUtils.displayImg(mContext, itemHolder.postImg, item.getPhoto());
                itemHolder.titleTv.setText(item.getTitle());
                String adddres = item.getAddress();
                String onlienNum = String.valueOf(item.getOnlineNumber());
                String startAt = item.getStartAt();
                String endAt = item.getEndAt();
                String toalScore = String.valueOf(item.getTotalScore());
                if (!TextUtils.isEmpty(adddres)) {
                    itemHolder.locationTv.setVisibility(View.VISIBLE);
                    itemHolder.locationTv.setText(adddres);
                }
                if (!TextUtils.isEmpty(onlienNum)) {
                    itemHolder.onlineNumTv.setText(onlienNum);
                }
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
                        PlayBackActivity.start(mContext, item);
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


    }


    @Override
    public int getItemCount() {

        return mItems != null && mItems.size() > 0 ? mItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void remove(int position) {
        LiveRowsBean item = mItems.get(position);
        AppLog.print("liveAdapter remove___position___" + position+",id__"+item.getId());
        mContentLoader.deleteLiveHistory(item.getId());
        mItems.remove(position);
        notifyDataSetChanged();
//        notifyItemRangeChanged(position, getItemCount());
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
