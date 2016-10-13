package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.LiveCallengeAdapter;
import com.lalocal.lalocal.view.LiveGiftAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/10/11.
 */

public class LiveDetailActivity extends BaseActivity {
    @BindView(R.id.live_detail_ctv)
    CustomTitleView liveDetailCtv;
    @BindView(R.id.live_detail_img)
    ImageView liveDetailImg;
    @BindView(R.id.live_detail_title)
    TextView liveDetailTitle;
    @BindView(R.id.live_detail_location)
    TextView liveDetailLocation;
    @BindView(R.id.live_detail_onlinenum)
    TextView liveDetailOnlinenum;
    @BindView(R.id.live_detail_livelen)
    TextView liveDetailLivelen;
    @BindView(R.id.live_detail_date)
    TextView liveDetailDate;
    @BindView(R.id.live_detail_scorenum_tv)
    TextView liveDetailScorenumTv;
    @BindView(R.id.livelen_text)
    TextView livelenText;
    @BindView(R.id.live_detail_livelen2)
    TextView liveDetailLivelen2;
    @BindView(R.id.live_detail_toal_score)
    TextView liveDetailToalScore;
    @BindView(R.id.live_detail_gift_score)
    TextView liveDetailGiftScore;
    @BindView(R.id.live_detail_gift_rlv)
    RecyclerView liveDetailGiftRlv;
    @BindView(R.id.live_detial_challenge_score)
    TextView liveDetialChallengeScore;
    @BindView(R.id.live_detail_challenge_list)
    RecyclerView liveDetailChallengeList;
    @BindView(R.id.live_detail_scrollview)
ScrollView reboundScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detail);
        ButterKnife.bind(this);
        intiView();

    }

    private void intiView() {
        liveDetailGiftRlv.setNestedScrollingEnabled(false);
        liveDetailGiftRlv.setLayoutManager(new GridLayoutManager(this,4));
        liveDetailChallengeList.setNestedScrollingEnabled(false);
        liveDetailChallengeList.setLayoutManager(new LinearLayoutManager(this));
        mHandler.sendEmptyMessageDelayed(0,200);
    }

    Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            liveDetailGiftRlv.setAdapter(new LiveGiftAdapter());
        liveDetailChallengeList.setAdapter(new LiveCallengeAdapter());
        }
    };

}
