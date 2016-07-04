package com.lalocal.lalocal.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.lalocal.lalocal.R;

/**
 * Created by lenovo on 2016/6/23.
 */
public class ProductCheckDetailActivity extends BaseActivity implements View.OnClickListener{
    private boolean isLoading;
    private Context mContext=ProductCheckDetailActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_check_detail_layout);
        String checkdetail = getIntent().getStringExtra("checkdetail");
        ImageView back= (ImageView) findViewById(R.id.common_back_btn);
        back.setOnClickListener(this);
        final WebView checkWebview= (WebView) findViewById(R.id.product_check_detail_webview);
        if(Build.VERSION.SDK_INT >= 19) {
            checkWebview.getSettings().setLoadsImagesAutomatically(true);
        } else {
            checkWebview.getSettings().setLoadsImagesAutomatically(false);
        }
        WebSettings settings = checkWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        checkWebview.setBackgroundColor(0);
        checkWebview.loadUrl(checkdetail);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        checkWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                String typeName = networkInfo.getTypeName();
                if(typeName.equalsIgnoreCase("WIFI")&&isLoading){
                    isLoading=false;
                    checkWebview.reload();
                }

                if(!checkWebview.getSettings().getLoadsImagesAutomatically()) {
                    checkWebview.getSettings().setLoadsImagesAutomatically(true);
                }
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
