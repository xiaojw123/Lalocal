package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.easemob.ui.ChatActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.ConsumeRecord;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.adapter.ConsumeRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDiamondActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {
    @BindView(R.id.my_diamond_recharge_tv)
    TextView myDiamondRechargeTv;
    @BindView(R.id.my_diamond_num_tv)
    TextView myDiamondNumTv;
    @BindView(R.id.my_diamond_cosume_xrv)
    XRecyclerView myDiamondCosumeXrv;
    @BindView(R.id.consume_doubt_tv)
    TextView consumeDoubtTv;
    @BindString(R.string.consume_doubt)
    String consumeDoubt;
    @BindView(R.id.my_diamond_no_recharge)
    TextView myDiamondNoRecharge;
    @BindView(R.id.my_diamond_ctv)
    CustomTitleView myDiamondCtv;
    @BindView(R.id.consume_doubt_container)
    FrameLayout consumeDoubtContainer;

    WalletContent mWalletContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_diamond_layout);
        unbinder = ButterKnife.bind(this);
        showLoadingAnimation();
        setLoaderCallBack(new DiamondCallBack());
        mWalletContent = getWallConent();
        if (mWalletContent != null) {
            updateView();
        } else {
            mContentloader.getMyWallet();
        }
    }

    private void updateView() {
        double goldNum = mWalletContent.getGold();
        myDiamondNumTv.setText(CommonUtil.formartNum(goldNum));
        consumeDoubtTv.setText(Html.fromHtml("<u>" + consumeDoubt + "</u>"));
        myDiamondCtv.setOnBackClickListener(this);
        if (goldNum > 0) {
            myDiamondCosumeXrv.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
            myDiamondCosumeXrv.setLayoutManager(new LinearLayoutManager(this));
            myDiamondCosumeXrv.setVisibility(View.VISIBLE);
            myDiamondCosumeXrv.setPullRefreshEnabled(false);
            mContentloader.getGoldLogs(1);
        } else {
            myDiamondNoRecharge.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.my_diamond_recharge_tv, R.id.consume_doubt_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_diamond_recharge_tv:
                Intent rechargeIntent = new Intent(this, RechargeActivity.class);
                rechargeIntent.putExtra(KeyParams.WALLET_CONTENT, mWalletContent);
                startActivityForResult(rechargeIntent, KeyParams.REQUEST_CODE);
                break;
            case R.id.consume_doubt_container:
                Intent doubtIntent = new Intent(this, ChatActivity.class);
                startActivity(doubtIntent);
                break;
        }
    }


    class DiamondCallBack extends ICallBack implements XRecyclerView.LoadingListener {
        ConsumeRecordAdapter adapter;
        int pageNum, toalPages;
        boolean isLoadMore;
        List<ConsumeRecord.RowsBean> mRows = new ArrayList<>();

        @Override
        public void onGetScoreLog(ConsumeRecord log) {
            hidenLoadingAnimation();
            pageNum = log.getPageNumber();
            toalPages = log.getTotalPages();
            if (toalPages > 1) {
                myDiamondCosumeXrv.setLoadingMoreEnabled(true);
                myDiamondCosumeXrv.setLoadingListener(this);
            } else {
                myDiamondCosumeXrv.setLoadingMoreEnabled(false);
            }
            if (isLoadMore) {
                isLoadMore = false;
                myDiamondCosumeXrv.loadMoreComplete();
            } else {
                mRows.clear();
            }
            mRows.addAll(log.getRows());
            if (adapter == null) {
                adapter = new ConsumeRecordAdapter(mRows);
                myDiamondCosumeXrv.setAdapter(adapter);
            } else {
                adapter.updateItems(mRows);
            }
        }

        @Override
        public void onError(VolleyError volleyError) {
            isLoadMore = false;
        }

        @Override
        public void onResponseFailed() {
            isLoadMore = false;
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
        public void onLoadMore() {
            if (pageNum < toalPages) {
                isLoadMore = true;
                ++pageNum;
                mContentloader.getGoldLogs(pageNum);
            } else {
                myDiamondCosumeXrv.loadMoreComplete();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppLog.print("MyDiamondActivtiy onActivityResult___resultcode__" + resultCode);
        switch (resultCode) {
            case KeyParams.RESULT_ChARGE_SUCCESS:
            case KeyParams.RESULT_EXCHARGE_SUCCESS:
                mContentloader.getMyWallet();
                mContentloader.getGoldLogs(1);
                break;

        }


    }

    private WalletContent getWallConent() {
        return getIntent().getParcelableExtra(KeyParams.WALLET_CONTENT);
    }

    @Override
    public void onBackClick() {
        setUpdateWalletResult();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setUpdateWalletResult();
    }

    //更新我的钱包
    public void setUpdateWalletResult() {
        if (mWalletContent != getWallConent()) {
            setResult(KeyParams.RESULT_UPDATE_WALLET);
        }
    }


}
