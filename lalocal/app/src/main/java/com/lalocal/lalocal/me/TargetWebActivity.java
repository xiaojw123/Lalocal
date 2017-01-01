package com.lalocal.lalocal.me;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.view.CommonWebClient;
import com.lalocal.lalocal.view.CustomTitleView;

/**
 * Created by xiaojw on 2016/11/21.
 * 用于加载基础H5页面
 */

public class TargetWebActivity extends BaseActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_web);
        CustomTitleView titleView = (CustomTitleView) findViewById(R.id.target_web_title);
        webView = (WebView) findViewById(R.id.target_web_wv);
        String title = getTargetTitle();
        if (!TextUtils.isEmpty(title)) {
            titleView.setTitle(title);
        }else{
            titleView.setVisibility(View.GONE);
        }
        WebSettings ws = webView.getSettings();
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
        webView.loadUrl(getTargeUrl());
        webView.setWebViewClient(new TargetWebviewClient(this));
    }

    private void cloaseVideo() {
        String js = "var _video = document.getElementsByTagName('video')[0];\nif(_video.played){\n_video.pause();}";
        webView.loadUrl("javascript:" + js);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cloaseVideo();
    }

    class TargetWebviewClient extends CommonWebClient {
        public TargetWebviewClient(Context context) {
            super(context);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoadingAnimation();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hidenLoadingAnimation();
        }

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            AppLog.print("url____" + url);
//            view.loadUrl(url);
//            return true;
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public String getTargeUrl() {
        return getIntent().getStringExtra(KeyParams.TARGE_URL);
    }

    public String getTargetTitle() {
        return getIntent().getStringExtra(KeyParams.TARGE_TITLE);
    }
}
