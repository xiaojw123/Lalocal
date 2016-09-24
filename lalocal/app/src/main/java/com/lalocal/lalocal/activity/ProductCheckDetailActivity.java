package com.lalocal.lalocal.activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lalocal.lalocal.R;
/**
 * Created by lenovo on 2016/6/23.
 */
public class ProductCheckDetailActivity extends BaseActivity{
    private boolean isLoading;
    private Context mContext=ProductCheckDetailActivity.this;
    private WebView checkWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_check_detail_layout);
        String checkdetail = getIntent().getStringExtra("checkdetail");
        checkWebview = (WebView) findViewById(R.id.product_check_detail_webview);
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
        if(Build.VERSION.SDK_INT >= 21){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && checkWebview.canGoBack()) {
            checkWebview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
