package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.easemob.ui.ChatActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.RechargeItem;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.adapter.RechargeListAdapter;
import com.lalocal.lalocal.view.decoration.LinearItemDecoration;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {
    public static final int RESULT_CODE_UPDATE_COTENT = 0x12;

    @BindView(R.id.recharge_diamond_num)
    TextView rechargeDiamondNum;
    @BindView(R.id.recharge_doubt_tv)
    TextView rechargeDoubtTv;
    @BindView(R.id.recharge_package_rlv)
    RecyclerView rechargePackageRlv;
    @BindView(R.id.rechage_ticket_exchage)
    FrameLayout rechageTicketExchage;
    @BindView(R.id.recharge_package_firstmsg)
    TextView fistMsgTv;
    @BindString(R.string.recharge_doubt_customer)
    String rechargeDoubtText;
    WalletContent content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_layout);
        unbinder = ButterKnife.bind(this);
        setLoaderCallBack(new RechargeCallBack());
        rechargeDoubtTv.setText(Html.fromHtml("<u>" + rechargeDoubtText + "</u>"));
        content = getWallConent();
        if (content != null) {
            updateView(content);
        }else{
            mContentloader.getMyWallet();
        }
        rechargePackageRlv.setLayoutManager(new LinearLayoutManager(this));
        rechargePackageRlv.addItemDecoration(new LinearItemDecoration());
        mContentloader.getRechargeProducts();
    }

    private void updateView(WalletContent content) {
        this.content=content;
        String firstMsg = content.getFirstMsg();
        if (!TextUtils.isEmpty(firstMsg)) {
            fistMsgTv.setText(firstMsg);
        }
        rechargeDiamondNum.setText(CommonUtil.formartNum(content.getGold()));
    }


    @OnClick({R.id.rechage_ticket_exchage, R.id.recharge_doubt_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rechage_ticket_exchage:
                Intent intent =new Intent(this, ExchangeActivity.class);
                intent.putExtra(KeyParams.WALLET_CONTENT,content);
                startActivity(intent);
                break;
            case R.id.recharge_doubt_tv:
                Intent doubtIntent = new Intent(this, ChatActivity.class);
                startActivity(doubtIntent);
                break;

        }

    }


    class RechargeCallBack extends ICallBack implements OnItemClickListener {
        @Override
        public void onGetMyWallet(WalletContent content) {
            updateView(content);
        }

        @Override
        public void onGetRechargeProducts(List<RechargeItem> items) {
            RechargeListAdapter adapter = new RechargeListAdapter(items);
            adapter.setOnItemClickListener(this);
            rechargePackageRlv.setAdapter(adapter);
        }

        @Override
        public void onItemClickListener(View view, int position) {
            Object tagObj = view.getTag();
            if (tagObj != null) {
                Intent intent = new Intent(RechargeActivity.this, ChargePayActivity.class);
                intent.putExtra(ChargePayActivity.RECHARGE_ITEM, (RechargeItem) tagObj);
                startActivityForResult(intent, KeyParams.REQUEST_CODE);
            }

        }
    }

    private WalletContent getWallConent() {
        return getIntent().getParcelableExtra(KeyParams.WALLET_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppLog.print("RechargeAct____onActivityResult resultcode__" + resultCode);
        if (resultCode == KeyParams.RESULT_ChARGE_SUCCESS) {
            setResult(resultCode, data);
            finish();
        }


    }
}
