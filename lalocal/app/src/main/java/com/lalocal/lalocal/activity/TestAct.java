package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsBean;

/**
 * Created by lenovo on 2016/7/4.
 */
public class TestAct extends AppCompatActivity{
    private ArticleDetailsBean articleDetailsBean;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        articleDetailsBean = intent.getParcelableExtra("articleDetailsBean");
        WebView wb= (WebView) findViewById(R.id.article_webview_wb);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl("https://dev.lalocal.cn/wechat/app_article?id=" + articleDetailsBean.getTargetId());
    }


}
