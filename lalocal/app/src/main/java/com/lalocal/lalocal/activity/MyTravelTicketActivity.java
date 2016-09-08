package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.ConsumeRecord;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.ConsumeRecordAdapter;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTravelTicketActivity extends BaseActivity {
    @BindView(R.id.my_ticket_titleview)
    FrameLayout myTicketTitleview;
    @BindView(R.id.my_score_num_tv)
    TextView myScoreNumTv;
    @BindView(R.id.my_ticket_cur_fl)
    FrameLayout myTicketCurFl;
    @BindView(R.id.my_ticket_cosume_xlv)
    XListView myTicketCosumeXlv;
    @BindView(R.id.my_ticket_exchargegold_btn)
    Button myTicketRechargegoldBtn;
    @BindView(R.id.my_ticket_withdrawcash_btn)
    Button myTicketWithdrawcashBtn;
    @BindView(R.id.my_ticket_no_score)
    TextView myTicketNoScore;
    int pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_travel_ticket_layout);
        ButterKnife.bind(this);
        String scoreNum = getScoreNum();
        myScoreNumTv.setText(scoreNum);
        long num = Long.parseLong(scoreNum);
        if (num > 0) {
            myTicketCosumeXlv.setVisibility(View.VISIBLE);
            myTicketCosumeXlv.setPullRefreshEnable(false);
            myTicketCosumeXlv.setRefreshEnable(false);
        } else {
            myTicketNoScore.setVisibility(View.VISIBLE);
        }

        MyScoreCallBack callBack = new MyScoreCallBack();
        setLoaderCallBack(callBack);
        if (myTicketCosumeXlv.getVisibility() == View.VISIBLE) {
            myTicketCosumeXlv.setXListViewListener(callBack);
        }
        mContentloader.getScoreLogs(pageNum);
    }


    public String getScoreNum() {
        return getIntent().getStringExtra(KeyParams.SOCRE_NUM);
    }

    @OnClick({R.id.my_ticket_exchargegold_btn, R.id.my_ticket_withdrawcash_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_ticket_exchargegold_btn://兑换乐钻
                Intent exchargeIntent=getIntent();
                exchargeIntent.setClass(this,ExchangeActivity.class);
                startActivity(exchargeIntent);
                break;
            case R.id.my_ticket_withdrawcash_btn://提现
                // TODO: 2016/9/7 add 提现
                break;
        }
    }


    class MyScoreCallBack extends ICallBack implements XListView.IXListViewListener {
        int toalPages;
        boolean isLoadMore;
        List<ConsumeRecord.RowsBean> mRows = new ArrayList<>();
        ConsumeRecordAdapter adapter;

        @Override
        public void onGetScoreLog(ConsumeRecord log) {
            pageNum = log.getPageSize();
            toalPages = log.getTotalPages();
            if (toalPages>1){
                myTicketCosumeXlv.setPullLoadEnable(true);
            }else{
                myTicketCosumeXlv.setPullLoadEnable(false);
            }
            if (isLoadMore) {
                isLoadMore = false;
            } else {
                mRows.clear();
            }
            mRows.addAll(log.getRows());
            if (adapter == null) {
                adapter = new ConsumeRecordAdapter(mRows);
                myTicketCosumeXlv.setAdapter(adapter);
            } else {
                adapter.updateItems(mRows);
            }

        }

        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {
            if (pageNum < toalPages) {
                isLoadMore = true;
                ++pageNum;
                mContentloader.getScoreLogs(pageNum);
            }
        }
    }
}
