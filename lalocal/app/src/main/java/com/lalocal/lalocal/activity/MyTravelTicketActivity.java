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
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.adapter.ConsumeRecordAdapter;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTravelTicketActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {
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
    @BindView(R.id.my_travelticket_ctv)
    CustomTitleView myTicketCtv;


    int pageNum = 1;
    WalletContent mWalletContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_travel_ticket_layout);
        ButterKnife.bind(this);
        mWalletContent = getWalletContent();
        long scoreNum = mWalletContent.getScore();
        myScoreNumTv.setText(CommonUtil.formartNum(scoreNum));
        myTicketCtv.setOnBackClickListener(this);
        if (scoreNum > 0) {
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


    public WalletContent getWalletContent() {
        return getIntent().getParcelableExtra(KeyParams.WALLET_CONTENT);

    }


    @OnClick({R.id.my_ticket_exchargegold_btn, R.id.my_ticket_withdrawcash_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_ticket_exchargegold_btn://兑换乐钻
                Intent exchargeIntent = new Intent(this, ExchangeActivity.class);
                exchargeIntent.putExtra(KeyParams.WALLET_CONTENT, mWalletContent);
                startActivityForResult(exchargeIntent, KeyParams.REQUEST_CODE);
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
            if (toalPages > 1) {
                myTicketCosumeXlv.setPullLoadEnable(true);
            } else {
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

        @Override
        public void onGetMyWallet(WalletContent content) {
            mWalletContent = content;
            myScoreNumTv.setText(CommonUtil.formartNum(content.getScore()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == KeyParams.RESULT_EXCHARGE_SUCCESS) {
            mContentloader.getMyWallet();
            mContentloader.getScoreLogs(1);
        }

    }


    @Override
    public void onBackClick() {
        setResultForUpdateWallet();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResultForUpdateWallet();

    }

    public void setResultForUpdateWallet() {
        if (mWalletContent != getWalletContent()) {
            setResult(KeyParams.RESULT_UPDATE_WALLET, null);
        }
    }

}
