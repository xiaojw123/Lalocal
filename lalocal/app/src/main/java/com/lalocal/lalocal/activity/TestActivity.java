package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsBean;

/**
 * Created by lenovo on 2016/6/20.
 */
public class TestActivity extends BaseActivity {
    private ArticleDetailsBean articleDetailsBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.test_activity_layout);
      /*  WebView wb = (WebView) findViewById(R.id.wb);
        wb.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        String h5url = intent.getStringExtra("h5url");
        wb.loadUrl(h5url);
        MyWebviewClient myWebviewClient = new MyWebviewClient();
        wb.setWebViewClient(myWebviewClient);*/
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        articleDetailsBean = intent.getParcelableExtra("articleDetailsBean");
        WebView wb= (WebView) findViewById(R.id.article_webview_wb);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl("https://dev.lalocal.cn/wechat/app_article?id=" + articleDetailsBean.getTargetId());
    }
/*
int i=0;
    class MyWebviewClient extends WebViewClient{
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
              */
/*  if(i==0){
                    view.loadUrl("https://dev.lalocal.cn/wechat/app_theme?id=34");
                    i++;
                }*//*



            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        }
*/



    }

