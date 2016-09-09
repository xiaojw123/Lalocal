package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyWalletActivity extends BaseActivity {

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
    WalletContent mWalletContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wallet_layout);
        unbinder = ButterKnife.bind(this);
        setLoaderCallBack(new WalletCallBack());
        mContentloader.getMyWallet();


    }

    @OnClick({R.id.my_diamond_llt, R.id.my_travelticket_llt, R.id.my_coupon_llt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_diamond_llt:
                Intent diamondIntent = new Intent(this, MyDiamondActivity.class);
                diamondIntent.putExtra(KeyParams.WALLET_CONTENT,mWalletContent);
                startActivityForResult(diamondIntent,KeyParams.REQUEST_CODE);
                break;

            case R.id.my_travelticket_llt:
                Intent scoreIntent = new Intent(this, MyTravelTicketActivity.class);
                scoreIntent.putExtra(KeyParams.WALLET_CONTENT,mWalletContent);
                startActivityForResult(scoreIntent,KeyParams.REQUEST_CODE);
                break;
            case R.id.my_coupon_llt:
                break;
        }
    }

    class WalletCallBack extends ICallBack {
        @Override
        public void onGetMyWallet(WalletContent content) {
            mWalletContent = content;
            if (content != null) {
                String goldText = CommonUtil.formartNum(content.getGold());
                String scoreText = CommonUtil.formartNum(content.getScore());
                String couponText = CommonUtil.formartNum(content.getCouponNumb());
                myDiamondNum.setText(goldText);
                myTravelticketNum.setText(scoreText);
                myCouponNum.setText(couponText);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==KeyParams.RESULT_UPDATE_WALLET){
            mContentloader.getMyWallet();
        }

    }
}

