package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.lalocal.lalocal.R;

/**
 * Created by lenovo on 2016/6/23.
 */
public class ProductCheckDetailActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_check_detail_layout);
        String checkdetail = getIntent().getStringExtra("checkdetail");
        ImageView back= (ImageView) findViewById(R.id.common_back_btn);
        back.setOnClickListener(this);
        WebView checkWebview= (WebView) findViewById(R.id.product_check_detail_webview);
        checkWebview.getSettings().setJavaScriptEnabled(true);
        checkWebview.setBackgroundColor(0);
        checkWebview.loadUrl(checkdetail);
        checkWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.common_back_btn){
            finish();
        }

    }
}
