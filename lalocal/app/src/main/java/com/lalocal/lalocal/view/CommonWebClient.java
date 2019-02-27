package com.lalocal.lalocal.view;

import android.content.Context;
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
    private final String THEME_APP = "lalocal://app";
    private final String THEME_CALENDAR = "lalocal://productpricecalendarclick";
    private final String THEME_IMGE_CLICK = "myweb:imageClick";
    Context mContext;


    public CommonWebClient(Context context) {
        mContext = context;
    }

<<<<<<< HEAD
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        AppLog.print("onPageStarted____url___" + url);
        super.onPageStarted(view, url, favicon);
    }
=======
//    @Override
//    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//        super.onPageStarted(view, url, favicon);
//        if (mContext instanceof BaseActivity){
//            ((BaseActivity)mContext).showLoadingAnimation();
//        }
//    }
//
//    @Override
//    public void onPageFinished(WebView view, String url) {
//        super.onPageFinished(view, url);
//        if (mContext instanceof BaseActivity){
//            ((BaseActivity)mContext).hidenLoadingAnimation();
//        }
//    }
>>>>>>> dev


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        AppLog.print("loadding url____" + url);
//            webviewclient url____lalocal://app?{"targetType": "19","targetId": "","targetUrl": ""}
<<<<<<< HEAD
        boolean is=false;
        if (url.startsWith("lalocal:")) {
=======
        if (url.startsWith(THEME_APP)) {
            boolean isHandled = false;
>>>>>>> dev
            int startIndex = url.indexOf("?") + 1;
            int code = 0;
            try {
                String jsonData = url.substring(startIndex, url.length());
                JSONObject jsonObject = new JSONObject(jsonData);
                String targetType = jsonObject.optString("targetType");
                String targetId = jsonObject.optString("targetId");
                String targetUrl = jsonObject.optString("targetUrl");
                String targetName = jsonObject.optString("targetName");
<<<<<<< HEAD
                code = TargetPage.gotoTargetPage(mContext, targetType, targetId, targetUrl, targetName, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
=======
                isHandled = TargetPage.gotoTargetPage(mContext, targetType, targetId, targetUrl, targetName, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return isHandled;
        } else if (url.startsWith(THEME_CALENDAR)) {
            String json = url.substring(url.indexOf("{"));
            try {
                JSONObject resultJobj = new JSONObject(json);
                String h5Url = resultJobj.optString("url");
                TargetPage.gotoWebDetail(mContext, h5Url, null, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else if (url.startsWith(THEME_IMGE_CLICK)) {
//            String imgUrl = url.substring(url.indexOf(":")+1);
//            TargetPage.gotoWebDetail(mContext, imgUrl, null, false);
            return true;
>>>>>>> dev
        } else {
            view.loadUrl(url);
        }
        return false;
    }

}
