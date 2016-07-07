package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.SharePopupWindow;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by android on 2016/7/7.
 */
public class CarouselFigureActivity extends  BaseActivity implements View.OnClickListener, PlatformActionListener, Handler.Callback {

    private WebView carousFigure;
    private ImageView figgure;
    private ImageView back;
    private TextView figureTv;
    private RecommendAdResultBean recommendAdResultBean;
    private SpecialShareVOBean shareVO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.carous_figure_layout);
        init();
        Intent intent = getIntent();
        recommendAdResultBean = intent.getParcelableExtra("carousefigure");
        String url = recommendAdResultBean.url;
        shareVO = recommendAdResultBean.shareVO;
        carousFigure.getSettings().setJavaScriptEnabled(true);
        carousFigure.loadUrl(url);
        figureTv.setText(recommendAdResultBean.title);
    }

    private void init() {
        carousFigure = (WebView) findViewById(R.id.carous_figure_webview);
        figgure = (ImageView) findViewById(R.id.carous_figure_share);
        back = (ImageView) findViewById(R.id.common_back_btn);
        figureTv = (TextView) findViewById(R.id.carous_figure_tv);
        back.setOnClickListener(this);
        figgure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.carous_figure_share:
                showShare(shareVO);
                break;
            case R.id.common_back_btn:
                finish();
                break;
        }

    }

    private void showShare(SpecialShareVOBean shareVO) {
        SharePopupWindow shareActivity = new SharePopupWindow(CarouselFigureActivity.this, shareVO);
        shareActivity.setPlatformActionListener(this);
        shareActivity.showShareWindow();
        shareActivity.showAtLocation(CarouselFigureActivity.this.findViewById(R.id.carous),
                Gravity.CENTER, 0, 0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && carousFigure.canGoBack()) {
            carousFigure.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
