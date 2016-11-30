package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomTitleView;

import cmb.pb.util.CMBKeyboardFunc;

import static com.lalocal.lalocal.help.KeyParams.PAY_NO;


public class CmbPayActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {
    public static final String CMB_PAY_URL = "cmb_pay_url";
    WebView webView;
    CustomTitleView cmbCtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmb_pay);
        showLoadingAnimation();
        setLoaderCallBack(new CmbPayCallBack());
        webView = (WebView) findViewById(R.id.cmb_pay_wv);
        cmbCtv = (CustomTitleView) findViewById(R.id.cmb_pay_ctv);
        cmbCtv.setOnCustomClickLister(this);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setSaveFormData(false);
        ws.setSavePassword(false);
        ws.setSupportZoom(false);
        try {
            CookieSyncManager.createInstance(this.getApplicationContext());
            CookieManager.getInstance().removeAllCookie();
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {

        }
        webView.loadUrl(getCmbPayUrl());
        webView.setWebViewClient(new CMBWebClient());
    }

    @Override
    public void onBackClick() {
        if (PageType.PAGE_CHARGE == getPageType()) {
            mContentloader.getPayStatus(getPayNo());
        } else {
            mContentloader.getOrderStatus(getOrderId());
        }
    }


    class CMBWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            hidenLoadingAnimation();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            myweb:lalocalcmbpaysuccessful
            AppLog.print("CMB PAY loadUrl__"+url);
            if ("myweb:lalocalcmbpaysuccessful".equals(url)) {
                Toast.makeText(CmbPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                setResult(PayActivity.RESULT_CMB_PAY_SUCCESS);
                finish();
                return true;
            } else {
                CMBKeyboardFunc kbFunc = new CMBKeyboardFunc(CmbPayActivity.this);
                if (kbFunc.HandleUrlCall(view, url) == false) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    return true;
                }
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (PageType.PAGE_CHARGE == getPageType()) {
                mContentloader.getPayStatus(getPayNo());
            } else {
                mContentloader.getOrderStatus(getOrderId());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class CmbPayCallBack extends ICallBack {
        @Override
        public void onError(VolleyError volleyError) {
            setFailedResut();
        }

        private void setFailedResut() {
            Toast.makeText(CmbPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            setResult(PayActivity.RESULT_CMB_PAY_FAILED);
            finish();
        }

        @Override
        public void onResponseFailed(int returnCode, String message) {
            setFailedResut();
        }

        @Override
        public void onGetPayStatus(int status) {
            if (status == 0) {
                setResult(PayActivity.RESULT_CMB_PAY_SUCCESS);
            } else {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return;
                }
                Toast.makeText(CmbPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                setResult(PayActivity.RESULT_CMB_PAY_FAILED);

            }
            finish();
        }

        @Override
        public void onGetOrderStatus(int status) {
            if (status == 2) {
                setResult(PayActivity.RESULT_CMB_PAY_SUCCESS);
            } else {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return;
                }
                setResult(PayActivity.RESULT_CMB_PAY_FAILED);
            }
            finish();
        }
    }

    public String getCmbPayUrl() {
        return getIntent().getStringExtra(CMB_PAY_URL);
    }

    public int getOrderId() {
        return getIntent().getIntExtra(KeyParams.ORDER_ID, -1);
    }

    public String getPayNo() {
        return getIntent().getStringExtra(PAY_NO);

    }


}
