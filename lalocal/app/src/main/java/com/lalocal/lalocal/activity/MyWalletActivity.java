package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomTitleView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class MyWalletActivity extends BaseActivity{

    @BindView(R.id.my_diamond_num)
    TextView myDiamondNum;
    @BindView(R.id.my_diamond_llt)
    LinearLayout myDiamondLlt;
    @BindView(R.id.my_travelticket_num)
    TextView myTravelticketNum;
    @BindView(R.id.my_travelticket_llt)
    LinearLayout myTravelticketLlt;
    @BindView(R.id.my_coupon_num)
    TextView myCouponNum;
    @BindView(R.id.my_coupon_llt)
    LinearLayout myCouponLlt;
    @BindView(R.id.my_wallet_ctv)
    CustomTitleView myWalletCtv;
    @BindView(R.id.my_wallet_instructions)
    TextView instructions_tv;
    @BindString(R.string.gold_instructions)
    String goldInstructions;
    WalletContent mWalletContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wallet_layout);
        ButterKnife.bind(this);
        instructions_tv.setText(Html.fromHtml("<u>" + goldInstructions + "</u>"));
        setLoaderCallBack(new WalletCallBack());
        mWalletContent = getWalletContent();
        if (mWalletContent == null) {
            showLoadingAnimation();
            mContentloader.getMyWallet();
        } else {
            updateView(mWalletContent);
        }

    }

    @OnClick({R.id.my_diamond_llt, R.id.my_travelticket_llt, R.id.my_coupon_llt,R.id.my_wallet_instructions})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_diamond_llt://乐钻
                MobHelper.sendEevent(this, MobEvent.MY_WALLET_DIAMOND);
                Intent diamondIntent = new Intent(this, MyDiamondActivity.class);
                diamondIntent.putExtra(KeyParams.WALLET_CONTENT, mWalletContent);
                startActivityForResult(diamondIntent, KeyParams.REQUEST_CODE);
                break;

            case R.id.my_travelticket_llt://游票
                MobHelper.sendEevent(this,MobEvent.MY_WALLET_TICKET);
                Intent scoreIntent = new Intent(this, MyTravelTicketActivity.class);
                scoreIntent.putExtra(KeyParams.WALLET_CONTENT, mWalletContent);
                startActivityForResult(scoreIntent, KeyParams.REQUEST_CODE);
                break;
            case R.id.my_coupon_llt://优惠券
                Intent couponIntent = new Intent(this, MyCouponActivity.class);
                couponIntent.putExtra(KeyParams.PAGE_TYPE, KeyParams.PAGE_TYPE_WALLET);
                couponIntent.putExtra(KeyParams.WALLET_CONTENT, mWalletContent);
                startActivityForResult(couponIntent, KeyParams.REQUEST_CODE);
                break;
            case R.id.my_wallet_instructions://乐钻使用说明
                CurrencyInstructionActivity.start(this,mWalletContent);
                break;
        }
    }


    class WalletCallBack extends ICallBack {
        @Override
        public void onGetMyWallet(WalletContent content) {
            hidenLoadingAnimation();
            mWalletContent = content;
            if (content != null) {
                updateView(content);
            }
        }

    }

    private void updateView(WalletContent content) {
        String goldText = CommonUtil.formartNum(content.getGold());
        String scoreText = CommonUtil.formartNum(content.getScore());
        String couponText = CommonUtil.formartNum(content.getCouponNumb());
        myDiamondNum.setText(goldText);
        myTravelticketNum.setText(scoreText);
        myCouponNum.setText(couponText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == KeyParams.RESULT_UPDATE_WALLET) {
            mContentloader.getMyWallet();
        }

    }

    public WalletContent getWalletContent() {
        return getIntent().getParcelableExtra(KeyParams.WALLET_CONTENT);

    }
}

