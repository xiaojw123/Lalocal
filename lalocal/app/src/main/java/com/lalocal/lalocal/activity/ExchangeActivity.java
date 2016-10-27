package com.lalocal.lalocal.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.KeyboardUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lalocal.lalocal.R.id.exchage_btn;

public class ExchangeActivity extends BaseActivity implements TextWatcher, CustomTitleView.onBackBtnClickListener, ViewTreeObserver.OnGlobalLayoutListener {
    private static String FORMART_EXCHARGE_PROMPT = "%1$d游票马上要兑换成%2$s乐钻啦";
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
    @BindView(R.id.exchange_title_ctv)
    CustomTitleView exchangeCtv;
    @BindView(R.id.exchange_root_view)
    RelativeLayout parentLayout;
    RelativeLayout.LayoutParams exchargeBtnParams;
    int scale;
    WalletContent mWalletCont;
    int lastHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_layout);
        ButterKnife.bind(this);
        setLoaderCallBack(new ExchangeCallBack());
        exchargeBtnParams = (RelativeLayout.LayoutParams) exchageBtn.getLayoutParams();
        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        KeyboardUtil.showSoftKey(exchageScoreEdit);
        exchangeCtv.setOnBackClickListener(this);
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
                    final long finalSocre = socre;
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.setTitle("兑换");
                    dialog.setMessage(String.format(FORMART_EXCHARGE_PROMPT, socre, exchageGoldNumTv.getText().toString()));
                    dialog.setCancelBtn("否", null);
                    dialog.setSurceBtn("是", new CustomDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            mContentloader.exchargeGold(finalSocre);
                        }
                    });
                    dialog.show();
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

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        // r will be populated with the coordinates of your view that area still visible.
        parentLayout.getWindowVisibleDisplayFrame(r);
        int screenHeight = parentLayout.getRootView().getHeight();
        int height = screenHeight - r.bottom;
        if (height == lastHeight) {
            return;
        }
        if (height > 0) {
            exchargeBtnParams.bottomMargin = height;
        } else {
            exchargeBtnParams.bottomMargin = 0;
        }
        exchageBtn.setLayoutParams(exchargeBtnParams);
        lastHeight = height;
    }


    class ExchangeCallBack extends ICallBack {
        @Override
        public void onExchargeGoldSuccess() {
            Toast.makeText(ExchangeActivity.this, "兑换成功", Toast.LENGTH_SHORT).show();
            mContentloader.getMyWallet();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            AppLog.print("keySoft inputmode status___" + params.softInputMode);
            if (KeyboardUtil.isShowSoftKey()) {
                KeyboardUtil.hidenSoftKey(exchageScoreEdit);
            } else {
                KeyboardUtil.showSoftKey(exchageScoreEdit);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackClick() {
        setExchangeResult();
    }

    @Override
    public void onBackPressed() {
        setExchangeResult();
        super.onBackPressed();
    }

    public void setExchangeResult() {
        setResult(KeyParams.RESULT_EXCHARGE_SUCCESS, null);
    }
}
