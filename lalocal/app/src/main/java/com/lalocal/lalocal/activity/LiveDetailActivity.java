package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ChannelRecord;
import com.lalocal.lalocal.model.UserLiveItem;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.LiveCallengeAdapter;
import com.lalocal.lalocal.view.LiveGiftAdapter;
import com.lalocal.lalocal.view.decoration.LinearItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/10/11.
 */

public class LiveDetailActivity extends BaseActivity {
    public static final String LIVE_ITEM = "record_id";
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
    @BindView(R.id.live_detail_toalscore_tv)
    TextView liveDetailToalScoreTv;
    @BindView(R.id.livelen_text)
    TextView livelenText;
    @BindView(R.id.live_detail_livelen2)
    TextView liveDetailLivelen2;
    @BindView(R.id.live_detail_live_score)
    TextView liveDetailLiveScore;
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
        UserLiveItem.RowsBean item = getUserLiveItem();
        intiView(item);
        setLoaderCallBack(new LiveDetailBack());
        mContentloader.getChannelRecords(item.getId());
    }

    private void intiView(UserLiveItem.RowsBean item) {
        liveDetailGiftRlv.setNestedScrollingEnabled(false);
        liveDetailGiftRlv.setLayoutManager(new GridLayoutManager(this, 4));
        liveDetailChallengeList.setNestedScrollingEnabled(false);
        liveDetailChallengeList.setLayoutManager(new LinearLayoutManager(this));
        liveDetailChallengeList.addItemDecoration(new LinearItemDecoration(this));
        if (item != null) {
            DrawableUtils.displayImg(this, liveDetailImg, item.getPhoto());
            liveDetailTitle.setText(item.getTitle());
            String addres = item.getAddress();
            String onlineNum = item.getOnlineNumber();
            String liveLen = item.getLiveLen();
            if (!TextUtils.isEmpty(addres)) {
                liveDetailLocation.setVisibility(View.VISIBLE);
                liveDetailLocation.setText(addres);
            }
            if (!TextUtils.isEmpty(onlineNum)) {
                liveDetailOnlinenum.setVisibility(View.VISIBLE);
                liveDetailOnlinenum.setText(onlineNum);
            }
            if (!TextUtils.isEmpty(liveLen)) {
                liveDetailLivelen2.setText(liveLen);
                liveDetailLivelen.setText(liveLen);
                liveDetailDate.setText(item.getDate());
            }
        }
    }

    class LiveDetailBack extends ICallBack {
        @Override
        public void onGetChannelRecord(ChannelRecord record) {
            updateDetailView(record);
        }
    }

    public void updateDetailView(ChannelRecord record) {
        if (record != null) {
            liveDetailToalScoreTv.setText(CommonUtil.formartNum(record.getTotalScore()));
            liveDetailLiveScore.setText("+" + CommonUtil.formartNum(record.getTimeScore()));
            liveDetailGiftScore.setText("+" + CommonUtil.formartNum(record.getGiftScore()));
            liveDetialChallengeScore.setText("+" + CommonUtil.formartNum(record.getChallengeScore()));
            liveDetailGiftRlv.setAdapter(new LiveGiftAdapter(record.getGiftRecords()));
            liveDetailChallengeList.setAdapter(new LiveCallengeAdapter(record.getChallengeRecords()));
        }


    }


    public UserLiveItem.RowsBean getUserLiveItem() {
        return getIntent().getParcelableExtra(LIVE_ITEM);
    }


}
