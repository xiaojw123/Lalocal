package com.lalocal.lalocal.activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsBean;

/**
 * Created by android on 2016/7/5.
 */
public class ArticleTestAct extends BaseActivity {
    private ArticleDetailsBean articleDetailsBean;
    private WebSettings settings;
    private WebView webView;
    private boolean isLoading;
    private LinearLayout reLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_test);
        webView = (WebView) findViewById(R.id.webview);
        reLayout = (LinearLayout) findViewById(R.id.article_relayout);
        Intent intent = getIntent();
        articleDetailsBean = intent.getParcelableExtra("articleDetailsBean");
        if(Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
      /*  Drawable btnDrawable = getResources().getDrawable(R.drawable.aritic_mo_bg);
       webView.setBackgroundResource(R.drawable.aritic_mo_bg);
       webView.setBackgroundDrawable(btnDrawable);*/
    //  webView.setBackgroundResource(R.drawable.aritic_mo_bg);
        webView.setBackgroundColor(0);
      //  webView.getBackground().setAlpha(110);
        if(Build.VERSION.SDK_INT >= 21){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.parseColor("#000000"));
        webView.loadUrl("https://dev.lalocal.cn/wechat/app_article?id=" + articleDetailsBean.getTargetId());
        webView.setWebViewClient(new MyWebViewClient());

    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ConnectivityManager manager = (ConnectivityManager) ArticleTestAct.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            String typeName = networkInfo.getTypeName();
            if(typeName.equalsIgnoreCase("WIFI")&&isLoading){
                isLoading=false;
                webView.reload();
            }
            if(!webView.getSettings().getLoadsImagesAutomatically()) {
                webView.getSettings().setLoadsImagesAutomatically(true);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
