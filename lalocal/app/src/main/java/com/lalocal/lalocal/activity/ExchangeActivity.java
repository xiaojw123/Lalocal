package com.lalocal.lalocal.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeActivity extends BaseActivity implements TextWatcher{

    @BindView(R.id.exchage_score_num_tv)
    TextView exchageScoreNumTv;
    @BindView(R.id.exchage_gold_num_tv)
    TextView exchageGoldNumTv;
    @BindView(R.id.exchage_score_edit)
    EditText exchageScoreEdit;
    @BindView(R.id.exchage_btn)
    Button exchageBtn;
    @BindView(R.id.excharge_score_unit)
    TextView unitTv;
    int scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_layout);
        ButterKnife.bind(this);
        showSoftKey();
        WalletContent content = getWalletContent();
        scale = content.getScale();
        exchageScoreNumTv.setText(CommonUtil.formartNum(content.getScore()));
        exchageScoreEdit.addTextChangedListener(this);
        setLoaderCallBack(new ExchangeCallBack());
    }

    @OnClick({R.id.exchage_btn, R.id.excharge_score_unit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exchage_btn:
                String socreText = exchageScoreEdit.getText().toString();
                if (!TextUtils.isEmpty(socreText)) {
                    long socre = Long.parseLong(socreText);
                    socre *= 100;
                    mContentloader.exchargeGold(socre);
                }
                break;
            case R.id.excharge_score_unit:
                showSoftKey();
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
            Toast.makeText(ExchangeActivity.this, "兑换成功", Toast.LENGTH_SHORT).show();
            setResult(KeyParams.RESULT_EXCHARGE_SUCCESS, null);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hidenSoftKey();
    }

    private void hidenSoftKey() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(exchageScoreEdit.getApplicationWindowToken(),
                    0);
        }
    }
    private void showSoftKey() {
        exchageScoreEdit.setCursorVisible(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(
                exchageScoreEdit, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


}
