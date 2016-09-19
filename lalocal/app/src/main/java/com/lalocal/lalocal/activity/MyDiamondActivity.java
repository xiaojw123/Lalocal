package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
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
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDiamondActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {
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
    @BindView(R.id.my_diamond_ctv)
    CustomTitleView myDiamondCtv;
    WalletContent mWalletContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_diamond_layout);
        unbinder = ButterKnife.bind(this);
        mWalletContent = getWallConent();
        double goldNum = mWalletContent.getGold();
        myDiamondNumTv.setText(CommonUtil.formartNum(goldNum));
        consumeDoubtTv.setText(Html.fromHtml("<u>" + consumeDoubt + "</u>"));
        myDiamondCtv.setOnBackClickListener(this);
        DiamondCallBack callBack = new DiamondCallBack();
        setLoaderCallBack(callBack);
        if (goldNum > 0) {
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
                Intent rechargeIntent = new Intent(this, RechargeActivity.class);
                rechargeIntent.putExtra(KeyParams.WALLET_CONTENT, mWalletContent);
                startActivityForResult(rechargeIntent, KeyParams.REQUEST_CODE);
                break;
            case R.id.consume_doubt_tv:
                Intent doubtIntent = new Intent(this, ChatActivity.class);
                startActivity(doubtIntent);
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
            AppLog.print("onGetScoreLog toalPages___"+toalPages);
            if (toalPages > 1) {
                myDiamondCosumeXlv.setPullLoadEnable(true);
                myDiamondCosumeXlv.setXListViewListener(this);
            } else {
                myDiamondCosumeXlv.setPullLoadEnable(false);
            }
            if (isLoadMore) {
                isLoadMore = false;
                myDiamondCosumeXlv.stopLoadMore();
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
            }else{
                Toast.makeText(MyDiamondActivity.this,"没有更多",Toast.LENGTH_SHORT).show();
                myDiamondCosumeXlv.stopLoadMore();
            }


        }

        @Override
        public void onGetMyWallet(WalletContent content) {
            mWalletContent = content;
            myDiamondNumTv.setText(CommonUtil.formartNum(content.getGold()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppLog.print("MyDiamondActivtiy onActivityResult___resultcode__" + resultCode);
        if (resultCode == KeyParams.RESULT_ChARGE_SUCCESS) {
            mContentloader.getMyWallet();
            mContentloader.getGoldLogs(1);
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
