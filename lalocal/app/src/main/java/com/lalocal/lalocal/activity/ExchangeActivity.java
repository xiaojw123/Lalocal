package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.KeyboardUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lalocal.lalocal.R.id.exchage_btn;

public class ExchangeActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.exchage_score_num_tv)
    TextView exchageScoreNumTv;
    @BindView(R.id.exchage_gold_num_tv)
    TextView exchageGoldNumTv;
    @BindView(R.id.exchage_score_edit)
    EditText exchageScoreEdit;
    @BindView(exchage_btn)
    Button exchageBtn;
    @BindView(R.id.excharge_score_unit)
    TextView unitTv;
    int scale;
    WalletContent mWalletCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_layout);
        ButterKnife.bind(this);
        setLoaderCallBack(new ExchangeCallBack());
        KeyboardUtil.showSoftKey(exchageScoreEdit);
        mWalletCont = getWalletContent();
        if (mWalletCont != null) {
            updateView();
        } else {
            mContentloader.getMyWallet();
        }
    }

    private void updateView() {
        if (mWalletCont != null) {
            scale = mWalletCont.getScale();
            exchageScoreNumTv.setText(CommonUtil.formartNum(mWalletCont.getScore()));
            exchageScoreEdit.addTextChangedListener(this);
        }
    }

    @OnClick({exchage_btn, R.id.excharge_score_unit})
    public void onClick(View view) {
        switch (view.getId()) {
            case exchage_btn:
                String socreText = exchageScoreEdit.getText().toString();
                if (!TextUtils.isEmpty(socreText)) {
                    long socre = Long.parseLong(socreText);
                    socre *= 100;
                    exchageBtn.setEnabled(false);
                    mContentloader.exchargeGold(socre);
                }
                break;
            case R.id.excharge_score_unit:
                KeyboardUtil.showSoftKey(exchageScoreEdit);
                break;
        }

    }


    public WalletContent getWalletContent() {
        return getIntent().getParcelableExtra(KeyParams.WALLET_CONTENT);

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
            exchageBtn.setEnabled(true);
            Toast.makeText(ExchangeActivity.this, "兑换成功", Toast.LENGTH_SHORT).show();
            setResult(KeyParams.RESULT_EXCHARGE_SUCCESS, null);
            finish();
        }

        @Override
        public void onError(VolleyError volleyError) {
            exchageBtn.setEnabled(true);
        }

        @Override
        public void onResponseFailed() {
            exchageBtn.setEnabled(true);
        }

        @Override
        public void onGetMyWallet(WalletContent content) {
            mWalletCont = content;
            updateView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtil.hidenSoftKey(exchageScoreEdit);
    }


}
