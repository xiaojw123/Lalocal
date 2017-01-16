package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.JsInterface;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.dialog.CustomDialog;

public class BookActivity extends BaseActivity {
    public static final String BOOK_URL = "pre_order_url";
    public static final String PAGE_BACK_PRODUCT_DETAIL = "backProdutionDetail";
    public static final String PAGE_TO_PAY = "orderIdCallBack";
    public static final String PAGET_TO_COUPON = "couponCallBack";
    public static final String PAGET_TO_COUPON_PAY_DONE = "couponsJumpToPayDone";
    public static final int RESULT_COUPON_SELECTED = 0x12;
    WebView mPreOrderWv;
    View loadPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);
        mPreOrderWv = (WebView) findViewById(R.id.pre_order_wv);
        loadPage = findViewById(R.id.page_base_loading);
        CommonUtil.setWebView(mPreOrderWv,false);
        String url = getIntent().getStringExtra(BOOK_URL);

        AppLog.print("H5___URL__" + url);
        mPreOrderWv.setWebChromeClient(new PreWebChromeClient());
        mPreOrderWv.setWebViewClient(new PreWebClient());
        mPreOrderWv.addJavascriptInterface(new JsInterface(this), "webkit");
        mPreOrderWv.loadUrl(AppConfig.getH5Url(this, url));
    }

    class PreWebChromeClient extends WebChromeClient {
              @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AppLog.print("alert__" + message);
            if (!TextUtils.isEmpty(message)) {
                CustomDialog dialog = new CustomDialog(BookActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage(message);
                dialog.setNeturalBtn("确定", new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        result.confirm();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
                return true;
            }
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AppLog.print("onJsConfirm____");
            if (!TextUtils.isEmpty(message)) {
                CustomDialog dialog = new CustomDialog(BookActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage(message);
                dialog.setSurceBtn("确定", new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        result.confirm();
                    }
                });
                dialog.setCancelBtn("取消", new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        result.cancel();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
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
            return super.shouldOverrideUrlLoading(view,url);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AppLog.print("keyDown__keycode___" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK && mPreOrderWv.canGoBack()) {
            AppLog.print("keyBack___webview goback");
            mPreOrderWv.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_COUPON_SELECTED) {
            String params = "";
            if (data != null) {
                params = data.getStringExtra("selectedCoupons");
            } else {
                params = "[]";
            }
            String js = String.format("javascript:backOrderWeb(%1$s)", params);
            AppLog.print("book___js_____" + js);
            mPreOrderWv.loadUrl(js);
        } else if (resultCode == PayActivity.RESULT_BACK_PRODUCT) {
            finish();
        }
    }
}
