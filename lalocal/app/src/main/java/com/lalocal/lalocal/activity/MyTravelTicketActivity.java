package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.model.ConsumeRecord;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.adapter.ConsumeRecordAdapter;

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
    @BindView(R.id.my_ticket_cosume_xrv)
    XRecyclerView myTicketCosumeXrv;
    @BindView(R.id.my_ticket_exchargegold_tv)
    TextView myTicketRechargegoldTv;
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
        showLoadingAnimation();
        setLoaderCallBack(new MyScoreCallBack());
        mWalletContent = getWalletContent();
        if (mWalletContent != null) {
            updateView();
        } else {
            mContentloader.getMyWallet();
        }
    }

    private void updateView() {
        long scoreNum = mWalletContent.getScore();
        myScoreNumTv.setText(CommonUtil.formartNum(scoreNum));
        myTicketCtv.setOnBackClickListener(this);
        if (scoreNum > 0) {
            myTicketCosumeXrv.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
            myTicketCosumeXrv.setLayoutManager(new LinearLayoutManager(this));
            myTicketCosumeXrv.setVisibility(View.VISIBLE);
            myTicketCosumeXrv.setPullRefreshEnabled(false);
        } else {
            myTicketNoScore.setVisibility(View.VISIBLE);
        }
        mContentloader.getScoreLogs(pageNum);
    }


    public WalletContent getWalletContent() {
        return getIntent().getParcelableExtra(KeyParams.WALLET_CONTENT);

    }


    @OnClick(R.id.my_ticket_exchargegold_tv)
    public void onClick() {
        MobHelper.sendEevent(this, MobEvent.MY_WALLET_TICKET_EXCHANGE);
        Intent exchargeIntent = new Intent(this, ExchangeActivity.class);
        exchargeIntent.putExtra(KeyParams.WALLET_CONTENT, mWalletContent);
        startActivityForResult(exchargeIntent, KeyParams.REQUEST_CODE);
    }


    class MyScoreCallBack extends ICallBack implements XRecyclerView.LoadingListener {
        int toalPages;
        boolean isLoadMore;
        List<ConsumeRecord.RowsBean> mRows = new ArrayList<>();
        ConsumeRecordAdapter adapter;

        @Override
        public void onGetScoreLog(ConsumeRecord log) {
            hidenLoadingAnimation();
            pageNum = log.getPageNumber();
            toalPages = log.getTotalPages();
            if (toalPages > 1) {
                myTicketCosumeXrv.setLoadingMoreEnabled(true);
                myTicketCosumeXrv.setLoadingListener(this);
            } else {
                myTicketCosumeXrv.setLoadingMoreEnabled(false);
            }
            if (isLoadMore) {
                loadMoreComplete();
            } else {
                mRows.clear();
            }
            mRows.addAll(log.getRows());
            if (adapter == null) {
                adapter = new ConsumeRecordAdapter(mRows);
                myTicketCosumeXrv.setAdapter(adapter);
            } else {
                adapter.updateItems(mRows);
            }

        }


        @Override
        public void onGetMyWallet(WalletContent content) {
            hidenLoadingAnimation();
            mWalletContent = content;
            updateView();
        }

        @Override
        public void onRefresh() {

        }

        @Override
        public void onResponseFailed() {
            super.onResponseFailed();
            loadMoreComplete();
        }

        private void loadMoreComplete() {
            isLoadMore = false;
            myTicketCosumeXrv.setNoMore(true);
        }

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);
            loadMoreComplete();
        }

        @Override
        public void onLoadMore() {
            if (pageNum < toalPages) {
                isLoadMore = true;
                ++pageNum;
                mContentloader.getScoreLogs(pageNum);
            } else {
                isLoadMore=false;
                myTicketCosumeXrv.setNoMore(true);
            }

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
        setResultForUpdateWallet();
        super.onBackPressed();

    }

    public void setResultForUpdateWallet() {
        if (mWalletContent != getWalletContent()) {
            setResult(KeyParams.RESULT_UPDATE_WALLET, null);
        }
    }

}
