package com.lalocal.lalocal.me;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.util.AppLog;
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
        titleView.setTitle(getTargetTitle());
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setUseWideViewPort(false);
        webView.loadUrl(getTargeUrl());
        webView.setWebViewClient(new TargetWebviewClient());
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

    class TargetWebviewClient extends WebViewClient {
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

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            AppLog.print("url____" + url);
            view.loadUrl(url);
            return true;
        }
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
