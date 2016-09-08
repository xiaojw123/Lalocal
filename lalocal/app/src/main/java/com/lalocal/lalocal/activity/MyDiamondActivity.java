package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ConsumeRecord;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.ConsumeRecordAdapter;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDiamondActivity extends BaseActivity{
    public static final String GOLD_PARAM = "gold";
    @BindView(R.id.my_diamond_recharge_tv)
    TextView myDiamondRechargeTv;
    @BindView(R.id.my_diamond_num_tv)
    TextView myDiamondNumTv;
    @BindView(R.id.my_diamond_cosume_xlv)
    XListView myDiamondCosumeXlv;
    @BindView(R.id.consume_doubt_tv)
    TextView consumeDoubtTv;
    @BindString(R.string.consume_doubt)
    String consumeDoubt;
    @BindView(R.id.my_diamond_no_recharge)
    TextView myDiamondNoRecharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_diamond_layout);
        unbinder = ButterKnife.bind(this);
        String goldText = getIntent().getStringExtra(GOLD_PARAM);
        myDiamondNumTv.setText(goldText);
        consumeDoubtTv.setText(Html.fromHtml("<u>" + consumeDoubt + "</u>"));
        DiamondCallBack callBack = new DiamondCallBack();
        setLoaderCallBack(callBack);
        long goldAccout = 0;
        if (goldText != null) {
            goldAccout = Long.parseLong(goldText);
        }
        if (goldAccout > 0) {
            myDiamondCosumeXlv.setVisibility(View.VISIBLE);
            myDiamondCosumeXlv.setPullRefreshEnable(false);
            myDiamondCosumeXlv.setRefreshEnable(false);
            mContentloader.getGoldLogs(1);
        } else {
            myDiamondNoRecharge.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.my_diamond_recharge_tv, R.id.consume_doubt_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_diamond_recharge_tv:
                Intent rechargeIntent = getIntent();
                rechargeIntent.setClass(this, RechargeActivity.class);
                startActivity(rechargeIntent);
                break;
            case R.id.consume_doubt_tv:
                break;
        }
    }



    class DiamondCallBack extends ICallBack implements XListView.IXListViewListener {
        ConsumeRecordAdapter adapter;
        int pageNum, toalPages;
        boolean isLoadMore;
        List<ConsumeRecord.RowsBean> mRows = new ArrayList<>();

        @Override
        public void onGetScoreLog(ConsumeRecord log) {
            pageNum = log.getPageSize();
            toalPages = log.getTotalPages();
            if (toalPages > 1) {
                myDiamondCosumeXlv.setPullLoadEnable(true);
                myDiamondCosumeXlv.setXListViewListener(this);
            } else {
                myDiamondCosumeXlv.setPullLoadEnable(false);
            }
            if (isLoadMore) {
                isLoadMore = false;
            } else {
                mRows.clear();
            }
            mRows.addAll(log.getRows());
            if (adapter == null) {
                adapter = new ConsumeRecordAdapter(mRows);
                myDiamondCosumeXlv.setAdapter(adapter);
            } else {
                adapter.updateItems(mRows);
            }
        }

        @Override
        public void onRequestFailed(VolleyError volleyError) {
            isLoadMore = false;
        }

        @Override
        public void onResponseFailed() {
            isLoadMore = false;
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
            }


        }
    }
}
