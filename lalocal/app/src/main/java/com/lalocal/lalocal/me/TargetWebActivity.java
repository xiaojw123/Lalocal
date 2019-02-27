package com.lalocal.lalocal.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.util.AppConfig;
<<<<<<< HEAD
import com.lalocal.lalocal.view.CommonWebClient;
=======
import com.lalocal.lalocal.util.CommonUtil;
>>>>>>> dev
import com.lalocal.lalocal.view.CustomTitleView;

/**
 * Created by xiaojw on 2016/11/21.
 * 用于加载基础H5页面
 */

public class TargetWebActivity extends BaseActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_web);
        CustomTitleView titleView = (CustomTitleView) findViewById(R.id.target_web_title);
        webView = (WebView) findViewById(R.id.target_web_wv);
        CommonUtil.setWebView(webView, true);
        String title = getTargetTitle();
        if (!TextUtils.isEmpty(title)) {
            titleView.setTitle(title);
        } else {
            titleView.setVisibility(View.GONE);
        }
    }

    private void cloaseVideo() {
        String js = "var _video = document.getElementsByTagName('video')[0];\nif(_video.played){\n_video.pause();}";
        webView.loadUrl("javascript:" + js);
    }

    @Override
    protected void onStart() {
        super.onStart();
        webView.loadUrl(getTargeUrl());
    }

    @Override
    protected void onStop() {
        super.onStop();
        cloaseVideo();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public String getTargeUrl() {
<<<<<<< HEAD
        String url=getIntent().getStringExtra(KeyParams.TARGE_URL);
        return AppConfig.getH5Url(this,url);
=======
        String url = getIntent().getStringExtra(KeyParams.TARGE_URL);
        return AppConfig.getH5Url(this, url);
>>>>>>> dev
    }

    public String getTargetTitle() {
        return getIntent().getStringExtra(KeyParams.TARGE_TITLE);
    }
}
