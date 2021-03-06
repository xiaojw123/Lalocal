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
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.RechargeItem;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.adapter.RechargeListAdapter;
import com.lalocal.lalocal.view.decoration.LinearItemDecoration;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {
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
    @BindView(R.id.recharge_doubt_container)
    FrameLayout rechargeDoubtContainer;
    @BindView(R.id.recharge_titleview)
    CustomTitleView rechargeCtv;
    @BindString(R.string.recharge_doubt_customer)
    String rechargeDoubtText;
    WalletContent mWalletCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_layout);
        ButterKnife.bind(this);
        showLoadingAnimation();
        setLoaderCallBack(new RechargeCallBack());
        rechargeCtv.setOnBackClickListener(this);
        rechargeDoubtTv.setText(Html.fromHtml("<u>" + rechargeDoubtText + "</u>"));
        mWalletCont = getWallConent();
        if (mWalletCont != null) {
            updateView();
        } else {
            mContentloader.getMyWallet();
        }
        rechargePackageRlv.setLayoutManager(new LinearLayoutManager(this));
        rechargePackageRlv.addItemDecoration(new LinearItemDecoration(this));
        mContentloader.getRechargeProducts();
    }

    private void updateView() {
        if (mWalletCont != null) {
            String firstMsg = mWalletCont.getFirstMsg();
            if (!TextUtils.isEmpty(firstMsg)) {
                fistMsgTv.setText(firstMsg);
            }
            rechargeDiamondNum.setText(CommonUtil.formartNum(mWalletCont.getGold()));
        }
    }


    @OnClick({R.id.rechage_ticket_exchage, R.id.recharge_doubt_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rechage_ticket_exchage://兑换乐钻
                Intent intent = new Intent(this, ExchangeActivity.class);
                intent.putExtra(KeyParams.WALLET_CONTENT, mWalletCont);
                startActivityForResult(intent, KeyParams.REQUEST_CODE);
                break;
            case R.id.recharge_doubt_container://充值问题联系客服
                CommonUtil.startCustomService(this);
                break;

        }

    }


    class RechargeCallBack extends ICallBack implements OnItemClickListener {
        @Override
        public void onGetMyWallet(WalletContent content) {
            hidenLoadingAnimation();
            mWalletCont = content;
            updateView();
        }

        @Override
        public void onGetRechargeProducts(List<RechargeItem> items) {
            hidenLoadingAnimation();
            RechargeListAdapter adapter = new RechargeListAdapter(items);
            adapter.setOnItemClickListener(this);
            rechargePackageRlv.setAdapter(adapter);
        }
        //选择一种规格的乐钻产品进行充值
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
        setResult(resultCode, data);
        switch (resultCode) {
            case KeyParams.RESULT_EXCHARGE_SUCCESS:
            case KeyParams.RESULT_ChARGE_SUCCESS:
                mContentloader.getMyWallet();
                break;

        }

    }

    @Override
    public void onBackClick() {
        setResult(KeyParams.RESULT_EXCHARGE_SUCCESS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(KeyParams.RESULT_EXCHARGE_SUCCESS);
    }
}
