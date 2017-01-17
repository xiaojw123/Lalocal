package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ChannelRecord;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.LiveCallengeAdapter;
import com.lalocal.lalocal.view.LiveGiftAdapter;
import com.lalocal.lalocal.view.decoration.LinearItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lalocal.lalocal.view.adapter.MyLiveAdapter.FOMRAT_TIME;

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
    @BindView(R.id.live_detail_startat)
    TextView liveDetailLiveStartAt;
    @BindView(R.id.live_detail_toalscore_tv)
    TextView liveDetailToalScoreTv;
    @BindView(R.id.livelen_text)
    TextView livelenText;
    @BindView(R.id.live_detail_livelen)
    TextView liveDetailLivelen;
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
        LiveRowsBean item = getUserLiveItem();
        intiView(item);
        setLoaderCallBack(new LiveDetailBack());
        AppLog.print("item___" + item);
        if (item != null) {
            AppLog.print("id__channelsRecords_____" + item.getId());
            mContentloader.getChannelRecords(item.getId());
        }
    }

    private void intiView(LiveRowsBean item) {
        liveDetailLocation.getCompoundDrawables()[0].setAlpha(20);
        liveDetailOnlinenum.getCompoundDrawables()[0].setAlpha(20);
        liveDetailLiveStartAt.getCompoundDrawables()[0].setAlpha(20);
        liveDetailGiftRlv.setNestedScrollingEnabled(false);
        liveDetailGiftRlv.setLayoutManager(new GridLayoutManager(this, 4));
        liveDetailChallengeList.setNestedScrollingEnabled(false);
        liveDetailChallengeList.setLayoutManager(new LinearLayoutManager(this));
        liveDetailChallengeList.addItemDecoration(new LinearItemDecoration(this));
        if (item != null) {
            DrawableUtils.displayImg(this, liveDetailImg, item.getPhoto(), R.drawable.home_me_personheadnormal);
            liveDetailTitle.setText(item.getTitle());
            String addres = item.getAddress();
            String onlineNum = String.valueOf(item.getOnlineNumber());
            String startAt = item.getStartAt();
            String endAt = item.getEndAt();
            String liveLen = getLiveLen(startAt, endAt);
            liveDetailLocation.setText(addres);
            liveDetailOnlinenum.setText(onlineNum);
            liveDetailLiveStartAt.setText(startAt);
            liveDetailLivelen.setText(liveLen);
        }
    }

    class LiveDetailBack extends ICallBack {
        @Override
        public void onGetChannelRecord(ChannelRecord record) {
            AppLog.print("onGetChannelRecord____");
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


    public LiveRowsBean getUserLiveItem() {

        return getIntent().getParcelableExtra(LIVE_ITEM);
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


}
