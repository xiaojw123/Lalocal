package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lalocal.lalocal.activity.MyCouponActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.TargetPage;
import com.lalocal.lalocal.help.TargetType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.me.LLoginActivity;
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
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        AppLog.print("webviewclient url____" + url);
//            webviewclient url____lalocal://app?{"targetType": "19","targetId": "","targetUrl": ""}
        if (url.startsWith("lalocal:")) {
            int startIndex = url.indexOf("?") + 1;
            String jsonData = url.substring(startIndex, url.length());
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String targetType = jsonObject.optString("targetType");
                String targetId = jsonObject.optString("targetId");
                String targetUrl = jsonObject.optString("targetUrl");
                if (!TextUtils.isEmpty(targetType)) {
                    //优惠券跳转
                    if (TargetType.COUPON.equals(targetType)) {
                        if (UserHelper.isLogined(mContext)) {
                            Intent intent = new Intent(mContext, MyCouponActivity.class);
                            intent.putExtra(KeyParams.PAGE_TYPE, KeyParams.PAGE_TYPE_WALLET);
                            mContext.startActivity(intent);
                        } else {
                            LLoginActivity.start(mContext);
                        }
                        return true;
                    }
                    //直播视频跳转
                    if (TargetType.LIVE_VIDEO.equals(targetType)) {
                        Intent intent = new Intent(mContext, AudienceActivity.class);
                        intent.putExtra("id", targetId);
                        mContext.startActivity(intent);
                        return true;
                    }
                    //直播回放跳转
                    if (TargetType.LIVE_PALY_BACK.equals(targetType)) {
                        Intent intent = new Intent(mContext, PlayBackActivity.class);
                        intent.putExtra("id", targetId);
                        mContext.startActivity(intent);
                        return true;
                    }
                    if (TargetType.URL.equals(targetType)) {
                        TargetPage.gotoWebDetail(mContext, targetUrl, null, false);
                        return true;
                    }

                    if (TargetType.USER.equals(targetType) || TargetType.AUTHOR.equals(targetType)) {
                        TargetPage.gotoUser(mContext, targetId, false);
                        return true;
                    }
                    if (TargetType.ARTICLE.equals(targetType) || TargetType.INFORMATION.equals(targetType)) {
                        TargetPage.gotoArticleDetail(mContext, targetId, false);
                        return true;
                    }
                    if (TargetType.PRODUCT.equals(targetType)) {
                        TargetPage.gotoProductDetail(mContext, targetId, targetType, false);
                        return true;
                    }
                    if (TargetType.ROUTE.equals(targetType)) {
                        TargetPage.gotoRouteDetail(mContext, targetId, false);
                        return true;
                    }
                    if (TargetType.SPECIAL.equals(targetType)) {
                        TargetPage.gotoSpecialDetail(mContext, targetId, false);
                        return true;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return super.shouldOverrideUrlLoading(view, url);
    }

}
