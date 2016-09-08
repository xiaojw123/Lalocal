package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RechargeItem;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.RechargeListAdapter;
import com.lalocal.lalocal.view.decoration.LinearItemDecoration;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {
    public static  final  int RESULT_CODE_UPDATE_COTENT=0x12;

    @BindView(R.id.recharge_diamond_num)
    TextView rechargeDiamondNum;
    @BindView(R.id.recharge_doubt_tv)
    TextView rechargeDoubtTv;
    @BindView(R.id.recharge_package_rlv)
    RecyclerView rechargePackageRlv;
    @BindView(R.id.rechage_ticket_exchage)
    FrameLayout rechageTicketExchage;
    @BindString(R.string.recharge_doubt_customer)
    String rechargeDoubtText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_layout);
        unbinder = ButterKnife.bind(this);
        rechargeDoubtTv.setText(Html.fromHtml("<u>" + rechargeDoubtText + "</u>"));
        rechargeDiamondNum.setText(getGoldNum());
        rechargePackageRlv.setLayoutManager(new LinearLayoutManager(this));
        rechargePackageRlv.addItemDecoration(new LinearItemDecoration());
        setLoaderCallBack(new RechargeCallBack());
        mContentloader.getRechargeProducts();
    }


    @OnClick(R.id.rechage_ticket_exchage)
    public void onClick() {
        Intent intent=getIntent();
        intent.setClass(this,ExchangeActivity.class);
        startActivity(intent);

    }

    class RechargeCallBack extends ICallBack implements OnItemClickListener {

        @Override
        public void onGetRechargeProducts(List<RechargeItem> items) {
            RechargeListAdapter adapter = new RechargeListAdapter(items);
            adapter.setOnItemClickListener(this);
            rechargePackageRlv.setAdapter(adapter);
        }

        @Override
        public void onItemClickListener(View view, int position) {
            Object tagObj=view.getTag();
            if (tagObj != null) {
                Intent intent = new Intent(RechargeActivity.this, ChargePayActivity.class);
                intent.putExtra(ChargePayActivity.RECHARGE_ITEM, (RechargeItem) tagObj);
                startActivityForResult(intent,100);
            }

        }
    }

    public String getGoldNum() {
        return getIntent().getStringExtra(MyDiamondActivity.GOLD_PARAM);
    }
}
