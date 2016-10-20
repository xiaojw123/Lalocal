package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.LiveDetailActivity;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.adapter.BaseRecyclerAdapter;

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

    public MyLiveAdapter(List<LiveRowsBean> items) {
        mItems = items;
    }

    public void updateItems(List<LiveRowsBean> items) {
        mItems = items;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_my_live, parent, false);
        return new ItemLiveHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mItems != null) {
            LiveRowsBean item = mItems.get(position);
            if (item != null) {
                ItemLiveHolder itemHolder = (ItemLiveHolder) holder;
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
                itemHolder.itemView.setTag(item);
            }
        }


    }


    @Override
    public int getItemCount() {
        return mItems != null && mItems.size() > 0 ? mItems.size() : 0;
    }

    class ItemLiveHolder extends RecyclerView.ViewHolder {
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
        @BindString(R.string.view_details)
        String viewDetailsStr;

        public ItemLiveHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            viewdetailsTv.setText(Html.fromHtml("<u>" + viewDetailsStr + "</u>"));
            viewdetailsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LiveDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }


}
