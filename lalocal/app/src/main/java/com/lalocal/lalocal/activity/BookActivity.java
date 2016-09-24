package com.lalocal.lalocal.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.JsInterface;
import com.lalocal.lalocal.util.AppLog;

public class BookActivity extends BaseActivity {
    public static final String BOOK_URL = "pre_order_url";
    public static final String PAGE_BACK_PRODUCT_DETAIL = "backProdutionDetail";
    public static final String PAGE_TO_PAY = "orderIdCallBack";
    public static final String PAGET_TO_COUPON = "couponCallBack";
    public static final int RESULT_COUPON_SELECTED = 0x12;
    WebView mPreOrderWv;
    View loadPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);
        mPreOrderWv = (WebView) findViewById(R.id.pre_order_wv);
        loadPage = findViewById(R.id.page_base_loading);
        String url = getIntent().getStringExtra(BOOK_URL);

        AppLog.print("H5___URL__" + url);
        mPreOrderWv.setWebChromeClient(new PreWebChromeClient());
        mPreOrderWv.setWebViewClient(new PreWebClient());
        mPreOrderWv.addJavascriptInterface(new JsInterface(this), "webkit");
        WebSettings ws = mPreOrderWv.getSettings();
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setUseWideViewPort(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccess(true); // 允许访问文件
        ws.setLoadWithOverviewMode(true);
        ws.setDisplayZoomControls(false);
        mPreOrderWv.loadUrl(url);
//        url = "http://www.jianshu.com/p/8bc9a4af771f";
    }

    class PreWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AppLog.print("alert__" + message);
            if (!TextUtils.isEmpty(message)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
                builder.setTitle("提示");
                builder.setMessage(message);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                builder.setCancelable(false);
                builder.show();
                return true;
            }
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AppLog.print("onJsConfirm____");
            if (!TextUtils.isEmpty(message)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
                builder.setMessage(message);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        result.cancel();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        result.confirm();

                    }
                });
                builder.setCancelable(false);
                builder.show();
                return true;
            }
            return super.onJsConfirm(view, url, message, result);


        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            AppLog.print("onJsPrompt____");
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }


    class PreWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (loadPage.getVisibility() == View.VISIBLE) {
                loadPage.setVisibility(View.GONE);
            }
        }

        //
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            AppLog.print("shouldOverrideUrlLoading  url____" + url);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_COUPON_SELECTED) {
            String params="";
            if (data!=null){
                params=data.getStringExtra("selectedCoupons");
            }else{
                params="[]";
            }
            String js = String.format("javascript:backOrderWeb(%1$s)", params);
            mPreOrderWv.loadUrl(js);
        } else if (resultCode == PayActivity.RESULT_BACK_PRODUCT) {
            finish();
        }
    }
}
