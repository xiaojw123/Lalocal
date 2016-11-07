package com.lalocal.lalocal.help;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.lalocal.lalocal.MyApplication;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.util.AppLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by xiaojw on 2016/10/19.
 */

public class MobHelper {
    private static MobHelper helper = new MobHelper();


    private MobHelper() {

    }

    public static MobHelper getInstance() {
        return helper;
    }


    public static void sendEevent(Context context, String event) {
        if (!MyApplication.isDebug) {
            MobclickAgent.onEvent(context, event);
        }
    }

    public static void singIn(int id) {
        if (!MyApplication.isDebug) {
            MobclickAgent.onProfileSignIn(String.valueOf(id));
        }
    }

    public static void singOff() {
        if (!MyApplication.isDebug) {
            MobclickAgent.onProfileSignOff();
        }
    }

    public void socialLogin(Activity activity, ContentLoader loader, SHARE_MEDIA share_media) {
        AppLog.print("socialLogin__");
        mActivity = activity;
        mLoader = loader;
        mUmShareAPI = UMShareAPI.get(activity);
        mUmShareAPI.doOauthVerify(activity, share_media, authListener);

    }

    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Toast.makeText(mActivity, "授权成功", Toast.LENGTH_LONG).show();
            if (mUmShareAPI != null) {
                mUmShareAPI.getPlatformInfo(mActivity, share_media, infoGetListener);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(mActivity, "授权失败", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(mActivity, "取消授权", Toast.LENGTH_LONG).show();

        }
    };


    private UMAuthListener infoGetListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            AppLog.print("onComplete  loginBySocial__");
            if (mLoader != null) {
                mLoader.loginBySocial(map, share_media);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };
    UMShareAPI mUmShareAPI;
    Activity mActivity;
    ContentLoader mLoader;

}
