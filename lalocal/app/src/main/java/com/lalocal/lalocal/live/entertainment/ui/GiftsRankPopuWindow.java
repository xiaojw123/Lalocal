package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.model.LiveGiftRanksResp;
import com.lalocal.lalocal.live.entertainment.model.RankUserBean;
import com.lalocal.lalocal.live.entertainment.model.TotalRanksBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.AppLog;

import java.util.List;

/**
 * Created by android on 2016/9/11.
 */
public class GiftsRankPopuWindow extends PopupWindow {
    private Context mContext;
    private TextView thisRoomTv;

    private LinearLayout allRoomRank;
    private TextView allRoomTv;

    private ListView rankListView;
    private TextView shareBtn;
    private LiveGiftRanksResp liveGiftRanksResp;
    private List<TotalRanksBean> currentRanks;
    private TextView noGift;

    public GiftsRankPopuWindow(Context mContext,LiveGiftRanksResp liveGiftRanksResp){
        this.mContext=mContext;
        this.liveGiftRanksResp=liveGiftRanksResp;

    }
    public  void showGiftsRankPopuWindow(){
        View view = View.inflate(mContext, R.layout.live_gifts_ranking_pop_layout, null);
        ImageView closePopu= (ImageView) view.findViewById(R.id.live_gift_ranking_close_iv);
        LinearLayout thisRoomRank= (LinearLayout) view.findViewById(R.id.live_gifts_this_room_ranking);
        thisRoomTv = (TextView) view.findViewById(R.id.live_gifts_this_room_ranking_tv);

        allRoomRank = (LinearLayout) view.findViewById(R.id.live_gifts_all_room_ranking);
        allRoomTv = (TextView) view.findViewById(R.id.live_gifts_all_room_ranking_tv);
        noGift = (TextView) view.findViewById(R.id.live_no_gift_ranking);

        rankListView = (ListView) view.findViewById(R.id.live_gift_ranking_listview);

        shareBtn = (TextView) view.findViewById(R.id.live_gift_ranking_share);
        shareBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        shareBtn.getPaint().setAntiAlias(true);

        closePopu.setOnClickListener(buttonClickListener);
        thisRoomRank.setOnClickListener(buttonClickListener);
        allRoomRank.setOnClickListener(buttonClickListener);
        shareBtn.setOnClickListener(buttonClickListener);


        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);
        currentRanks = liveGiftRanksResp.getResult().getCurrentRanks();
        TotalRanksBean mycurrentRank = liveGiftRanksResp.getResult().getMycurrentRank();

        if(mycurrentRank!=null&&mycurrentRank.getRank()>10){
            currentRanks.add(mycurrentRank);
        }
        giftRankingStatus(currentRanks);

    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.live_gift_ranking_close_iv:
                    dismiss();
                   if(onGiftRanksListener!=null){
                       onGiftRanksListener.closeRankPopuBtn();
                   }
                    break;
                case R.id.live_gifts_this_room_ranking:
                    thisRoomTv.setTextColor(Color.parseColor("#ffaa2a"));
                    allRoomTv.setTextColor(Color.WHITE);
                    currentRanks = liveGiftRanksResp.getResult().getCurrentRanks();
                    giftRankingStatus(currentRanks);
                    break;
                case R.id.live_gifts_all_room_ranking:
                    allRoomTv.setTextColor(Color.parseColor("#ffaa2a"));
                    thisRoomTv.setTextColor(Color.WHITE);
                    currentRanks = liveGiftRanksResp.getResult().getTotalRanks();
                    TotalRanksBean myTotalRank = liveGiftRanksResp.getResult().getMyTotalRank();
                    if(myTotalRank!=null&&myTotalRank.getRank()>10){
                        currentRanks.add(myTotalRank);
                    }
                    giftRankingStatus(currentRanks);
                    break;
                case R.id.live_gift_ranking_share:
                    SpecialShareVOBean shareVO = liveGiftRanksResp.getResult().getShareVO();
                    if (shareVO != null&&onGiftRanksListener!=null) {
                        onGiftRanksListener.shareBtn(shareVO);
                    }
                    break;
            }
        }
    };

    private  void giftRankingStatus(List<TotalRanksBean> currentRanks){
        AppLog.i("TAG","礼物榜单："+currentRanks.size());
        if(currentRanks.size()==0){
            noGift.setVisibility(View.VISIBLE);
            rankListView.setVisibility(View.GONE);
        }else {
            noGift.setVisibility(View.GONE);
            rankListView.setVisibility(View.VISIBLE);
            rankListView.setAdapter(new GiftRankViewAdapter(mContext,currentRanks));
            rankListView.setOnItemClickListener(onItemClickListener);
        }
    }



    private OnGiftRanksListener onGiftRanksListener;

    public interface OnGiftRanksListener {
        void closeRankPopuBtn();
        void shareBtn(SpecialShareVOBean shareVO);
        void ranListItemClick(RankUserBean rankUser);
    }

    public void setOnSendClickListener(OnGiftRanksListener onGiftRanksListener) {
        this.onGiftRanksListener = onGiftRanksListener;
    }
    AdapterView.OnItemClickListener onItemClickListener=  new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TotalRanksBean totalRanksBean = currentRanks.get(position);
            RankUserBean rankUser = totalRanksBean.getUser();
            if(onGiftRanksListener!=null){
                onGiftRanksListener.ranListItemClick(rankUser);
            }
        }
    };

    }
