package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CommonWebClient;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.SharePopupWindow;

import static com.lalocal.lalocal.R.id.carous_figure_webview;

/**
 * Created by android on 2016/7/7.
 */
public class CarouselFigureActivity extends BaseActivity implements View.OnClickListener, CustomTitleView.onBackBtnClickListener {

    private WebView carousFigure;
    private ImageView figgure;
    private TextView figureTv;
    private RecommendAdResultBean recommendAdResultBean;
    private SpecialShareVOBean shareVO;
    private CustomTitleView carousFigureCtv;
    private int targetId;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carous_figure_layout);
        AppLog.print("CarouselFigureActivity___oncreate___");
        init();
        Intent intent = getIntent();
        recommendAdResultBean = intent.getParcelableExtra("carousefigure");
        url = recommendAdResultBean.url;
        shareVO = recommendAdResultBean.shareVO;
        targetId = recommendAdResultBean.targetId;
        WebSettings ws = carousFigure.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setUseWideViewPort(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccess(true); // 允许访问文件
        ws.setLoadWithOverviewMode(true);
        ws.setDisplayZoomControls(false);
        ws.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        carousFigure.setWebViewClient(new CommonWebClient(this));
        figureTv.setText(recommendAdResultBean.title);
    }

    private void init() {
        carousFigureCtv = (CustomTitleView) findViewById(R.id.carous_figure_ctv);
        carousFigure = (WebView) findViewById(carous_figure_webview);
        figgure = (ImageView) findViewById(R.id.carous_figure_share);
        figureTv = (TextView) findViewById(R.id.carous_figure_tv);
        figgure.setOnClickListener(this);
        carousFigureCtv.setFisishEanble(false);
        carousFigureCtv.setOnBackClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        carousFigure.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carous_figure_share:
                showShare(shareVO);
                break;
        }

    }

    private void showShare(SpecialShareVOBean shareVO) {
        SharePopupWindow shareActivity = new SharePopupWindow(CarouselFigureActivity.this, shareVO);
        shareActivity.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && carousFigure.canGoBack()) {
            AppLog.print("onKeyDown__back__goback____");
            carousFigure.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cloaseAudio();
    }

    private void cloaseAudio() {
        String js = "var _audio = document.getElementsByTagName('audio')[0];\nif(_audio.played){\n_audio.pause();}";
        carousFigure.loadUrl("javascript:" + js);//video
    }

    @Override
    public void onBackClick() {
        if (carousFigure.canGoBack()) {
            carousFigure.goBack();
        } else {
            finish();
        }
    }


}
