package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.exchage_score_num_tv)
    TextView exchageScoreNumTv;
    @BindView(R.id.exchage_gold_num_tv)
    TextView exchageGoldNumTv;
    @BindView(R.id.exchage_score_edit)
    EditText exchageScoreEdit;
    @BindView(R.id.exchage_btn)
    Button exchageBtn;
    int scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_layout);
        ButterKnife.bind(this);
        scale = getScale();
        exchageScoreNumTv.setText(getScoreNum());
        exchageScoreEdit.addTextChangedListener(this);
        setLoaderCallBack(new ExchangeCallBack());
    }

    @OnClick(R.id.exchage_btn)
    public void onClick() {
        String socreText = exchageScoreEdit.getText().toString();
        if (!TextUtils.isEmpty(socreText)) {
            long socre = Long.parseLong(socreText);
            socre *= 100;
            mContentloader.exchargeGold(socre);
        }
    }

    public String getScoreNum() {
        return getIntent().getStringExtra(KeyParams.SOCRE_NUM);
    }

    public int getScale() {
        return getIntent().getIntExtra(KeyParams.SCALE, 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            exchageBtn.setEnabled(true);
            long scoreNum = Long.parseLong(s.toString());
            exchageGoldNumTv.setText(CommonUtil.formartNum(scoreNum * scale));
        } else {
            exchageBtn.setEnabled(false);
            exchageGoldNumTv.setText("0");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {


    }

    class ExchangeCallBack extends ICallBack {
        @Override
        public void onExchargeGoldSuccess() {
            Toast.makeText(ExchangeActivity.this, "兑换成功", Toast.LENGTH_SHORT).show();
        }
    }
}
