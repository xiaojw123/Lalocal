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
import com.lalocal.lalocal.model.UserLiveItem;
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
    List<UserLiveItem.RowsBean> mItems;

    public MyLiveAdapter(List<UserLiveItem.RowsBean> items) {
        mItems = items;
    }

    public void updateItems(List<UserLiveItem.RowsBean> items) {
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
            UserLiveItem.RowsBean item = mItems.get(position);
            if (item != null) {
                ItemLiveHolder itemHolder = (ItemLiveHolder) holder;
                DrawableUtils.displayImg(mContext, itemHolder.postImg, item.getPhoto());
                itemHolder.titleTv.setText(item.getTitle());
                String adddres = item.getAddress();
                String onlienNum = item.getOnlineNumber();
                String startAt = item.getStartAt();
                String endAt = item.getEndAt();
                String toalScore = item.getTotalScore();
                if (!TextUtils.isEmpty(adddres)) {
                    itemHolder.locationTv.setVisibility(View.VISIBLE);
                    itemHolder.locationTv.setText(adddres);
                }
                if (!TextUtils.isEmpty(onlienNum)) {
                    itemHolder.onlineNumTv.setText(onlienNum);
                }
                String liveLen = "00:00:00";
                if (!TextUtils.isEmpty(startAt) && !TextUtils.isEmpty(endAt)) {
                    liveLen = getLiveLen(startAt, endAt);
                    itemHolder.liveLenTv.setText(liveLen);
                }
                if (!TextUtils.isEmpty(toalScore)) {
                    itemHolder.scoreNumTv.setText(toalScore);

                }
                item.setLiveLen(liveLen);
                if (!TextUtils.isEmpty(endAt)) {
                    item.setDate(endAt.substring(0, 10));
                }
                itemHolder.itemView.setTag(item);
            }
        }


    }

    //年和月上有可能存在误差
    public String getLiveLen(String startAt, String endAt) {
//        "startAt": "2016-10-10 18:29:28",
//                "endAt": "2016-10-10 18:30:51",
        //   18:39:25  18:40:01  00:1:-24   60-24  36
        try {
            String[] startDate = startAt.substring(0, 10).split("-");
            String[] endDate = endAt.substring(0, 10).split("-");
            String[] startTime = startAt.substring(startAt.length() - 8, startAt.length()).split(":");
            String[] endTime = endAt.substring(startAt.length() - 8, startAt.length()).split(":");
            int reduceH = reducedArrayValue(endTime, startTime, 0);
            int reduceM = reducedArrayValue(endTime, startTime, 1);
            int reduceS = reducedArrayValue(endTime, startTime, 2);
            int reduceYear = reducedArrayValue(endDate, startDate, 0);
            int reduceMonth = reducedArrayValue(endDate, startDate, 1);
            int reduceDay = reducedArrayValue(endDate, startDate, 2);
            int len = reduceYear * 365 * 24 * 3600 + reduceMonth * 30 * 24 * 3600 + reduceDay * 24 * 3600 + reduceH * 3600 + reduceM * 60 + reduceS;
            int h = len / 3600;
            int m = (len - h * 3600) / 60;
            int s = len - h * 3600 - m * 60;
            h = Math.max(h, 0);
            m = Math.max(m, 0);
            s = Math.max(s, 0);
            String fH = h < 10 ? "0" + h : String.valueOf(h);
            String fM = m < 10 ? "0" + m : String.valueOf(m);
            String fS = s < 10 ? "0" + s : String.valueOf(s);
            return String.format(FOMRAT_TIME, fH, fM, fS);
        } catch (Exception e) {
            return "00:00:00";
        }
    }


    public int reducedArrayValue(String[] array1, String[] array2, int index) {
        return Integer.parseInt(array1[index]) - Integer.parseInt(array2[index]);

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
