package com.lalocal.lalocal.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lalocal.lalocal.help.TargetPage;
import com.lalocal.lalocal.util.AppLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiaojw on 2016/12/14.
 * Common WebViewClient for WebView
 */

public class CommonWebClient extends WebViewClient {
    Context mContext;

    public CommonWebClient(Context context) {
        mContext = context;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        AppLog.print("onPageStarted____url___" + url);
        super.onPageStarted(view, url, favicon);
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        AppLog.print("shouldOverrideUrlLoading  url____" + url);
//            webviewclient url____lalocal://app?{"targetType": "19","targetId": "","targetUrl": ""}
        boolean is=false;
        if (url.startsWith("lalocal:")) {
            int startIndex = url.indexOf("?") + 1;
            int code = 0;
            try {
                String jsonData = url.substring(startIndex, url.length());
                JSONObject jsonObject = new JSONObject(jsonData);
                String targetType = jsonObject.optString("targetType");
                String targetId = jsonObject.optString("targetId");
                String targetUrl = jsonObject.optString("targetUrl");
                String targetName = jsonObject.optString("targetName");
                code = TargetPage.gotoTargetPage(mContext, targetType, targetId, targetUrl, targetName, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            view.loadUrl(url);
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

}
