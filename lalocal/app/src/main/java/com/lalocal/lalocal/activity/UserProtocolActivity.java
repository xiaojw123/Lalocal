package com.lalocal.lalocal.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppConfig;

public class UserProtocolActivity extends BaseActivity {
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_clauses_layout);
        webView = (WebView) findViewById(R.id.user_clauses_wv);
        webView.setWebViewClient(new MWebviewClient());
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setDisplayZoomControls(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl(AppConfig.getUserRuleUrl());
    }

    class MWebviewClient extends WebViewClient {
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

    public static  void start(Context context){
        Intent intent = new Intent(context, UserProtocolActivity.class);
        context.startActivity(intent);

    }
}
