package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.view.adapter.CurrencyInstruAdapter;

/**
 * Created by xiaojw on 2016/10/26.
 */

public class CurrencyInstructionActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        WalletContent content = getIntent().getParcelableExtra(KeyParams.WALLET_CONTENT);
        RecyclerView scoreRlv = (RecyclerView) findViewById(R.id.score_instructions_rlv);
        RecyclerView goldRlv = (RecyclerView) findViewById(R.id.gold_instructions_rlv);
        scoreRlv.setLayoutManager(new LinearLayoutManager(this));
        goldRlv.setLayoutManager(new LinearLayoutManager(this));
        if (content != null) {
            scoreRlv.setAdapter(new CurrencyInstruAdapter(content.getScoreRules()));
            goldRlv.setAdapter(new CurrencyInstruAdapter(content.getGoldRules()));
        }


    }

    public static void start(Context context, WalletContent walletContent) {
        Intent intent = new Intent(context, CurrencyInstructionActivity.class);
        intent.putExtra(KeyParams.WALLET_CONTENT, walletContent);
        context.startActivity(intent);
    }


}
