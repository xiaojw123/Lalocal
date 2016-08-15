package com.lalocal.lalocal.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.JsModeul;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.dialog.CustomDialog;

public class PreOrderActivity extends BaseActivity {
    public static final String PRE_ORDER_URL = "pre_order_url";
    public static final String PAGE_BACK_PRODUCT_DETAIL = "backProdutionDetail";
    public static final String PAGE_TO_PAY = "orderIdCallBack";
    WebView mPreOrderWv;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_order_layout);
        mPreOrderWv = (WebView) findViewById(R.id.pre_order_wv);
        String url = getIntent().getStringExtra(PRE_ORDER_URL);
        AppLog.print("H5___URL__" + url);
        mPreOrderWv.loadUrl(url);
        mPreOrderWv.setWebChromeClient(new PreWebChromeClient());
        mPreOrderWv.setWebViewClient(new PreWebClient());
        mPreOrderWv.addJavascriptInterface(new JsModeul(this), "webkit");
        WebSettings ws = mPreOrderWv.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setDisplayZoomControls(false);
    }

    class PreWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            if (!TextUtils.isEmpty(message)) {
                CustomDialog dialog = new CustomDialog(PreOrderActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage(message);
                dialog.setNeturalBtn("确定", null);
                dialog.setCancelable(false);
                dialog.show();
            }
            result.cancel();
            return true;
        }


    }


    class PreWebClient extends WebViewClient {
        //
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            AppLog.print("url____" + url);
            if (url.contains("/")) {
                int index = url.lastIndexOf("/");
                if (index < url.length() - 1) {
                    String key = url.substring(index + 1, url.length());
                    AppLog.print("key____" + key);
                    switch (key) {
                        case PAGE_BACK_PRODUCT_DETAIL:
                            finish();
                            break;
                        default:
                            view.loadUrl(url);
                            break;
                    }

                } else {
                    view.loadUrl(url);
                }


            }
            return true;
        }

    }


}
