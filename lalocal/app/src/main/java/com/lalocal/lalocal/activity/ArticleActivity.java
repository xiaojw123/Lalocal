package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;
/**
 * Created by lenovo on 2016/6/21.
 */
public class ArticleActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);
        WebView articleWebview= (WebView) findViewById(R.id.article_webview);
        Intent intent = getIntent();
        String articl = intent.getStringExtra("articl");
        articleWebview.getSettings().setJavaScriptEnabled(true);
        articleWebview.loadUrl("https://dev.lalocal.cn/wechat/app_article?id="+articl);
        articleWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(ArticleActivity.this,"Article:"+ url, Toast.LENGTH_SHORT).show();
                //lalocal://codeImageClick?{"name":"Lalocal","wechatNo":"%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB","imageUrl":"http://7xpid3.com1.z0.glb.clouddn.com/20160615142707308988042388"}
              /*  String[] split = url.split("codeImageClick\\?");
                String json = split[1];*/

                AppLog.i("000000000000000000000",url);
                return  true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
    }
}
