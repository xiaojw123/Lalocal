package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.view.SharePopupWindow;

/**
 * Created by android on 2016/7/7.
 */
public class CarouselFigureActivity extends  BaseActivity implements View.OnClickListener {

    private WebView carousFigure;
    private ImageView figgure;
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
        figureTv = (TextView) findViewById(R.id.carous_figure_tv);
        figgure.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.carous_figure_share:
                showShare(shareVO);
                break;
        }

    }

    private void showShare(SpecialShareVOBean shareVO) {
        SharePopupWindow shareActivity = new SharePopupWindow(CarouselFigureActivity.this, shareVO);
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
    protected void onStop() {
        super.onStop();
        finish();
    }
}
