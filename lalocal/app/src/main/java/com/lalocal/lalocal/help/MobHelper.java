package com.lalocal.lalocal.help;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.lalocal.lalocal.MyApplication;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CheckWeixinAndWeibo;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.dplus.UMADplus;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaojw on 2016/10/19.
 */

public class MobHelper {
    //定义用户画像属性
    public interface UMADParamas {
        String NAME="name";
        String SEX="sex";
        String USER_ID="userid";
    }


    private static MobHelper helper = new MobHelper();
    private static Map<String, Object> sUserAttr = new HashMap<>();


    private MobHelper() {
    }

    public static MobHelper getInstance() {
        return helper;
    }

    public static void registerSuperProperty(Context context, Map<String, Object> userAttr) {
        sUserAttr = userAttr;
        for (Map.Entry<String, Object> entry : userAttr.entrySet()) {
            UMADplus.registerSuperProperty(context, entry.getKey(), entry.getValue());
        }
    }

    public static void unRegsiterUserProperty(Context context) {
        if (sUserAttr.size() > 0) {
            for (Map.Entry<String, Object> entry : sUserAttr.entrySet()) {
                UMADplus.unregisterSuperProperty(context, entry.getKey());
            }
        }
    }


    public static void sendEevent(Context context, String event) {
        if (!MyApplication.isDebug) {
            MobclickAgent.onEvent(context, event);
            if (sUserAttr.size() > 0) {
                UMADplus.track(context, event, sUserAttr);
            }
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
        if (share_media == SHARE_MEDIA.SINA) {
            boolean isInstallWeibo = CheckWeixinAndWeibo.checkAPPInstall(activity, "com.sina.weibo");
            if (!isInstallWeibo) {
                Toast.makeText(activity, "没有安装微博客户端", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mActivity = activity;
        mLoader = loader;
        mUmShareAPI = UMShareAPI.get(activity);
        //三方授权登录
        mUmShareAPI.doOauthVerify(activity, share_media, authListener);

    }

    private UMAuthListener authListener = new UMAuthListener() {
        //授权成功
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Toast.makeText(mActivity, "授权成功", Toast.LENGTH_SHORT).show();
            if (mUmShareAPI != null) {
                mUmShareAPI.getPlatformInfo(mActivity, share_media, infoGetListener);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(mActivity, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(mActivity, "取消授权", Toast.LENGTH_SHORT).show();
        }
    };


    private UMAuthListener infoGetListener = new UMAuthListener() {
        //三方授权信息获取成功
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            AppLog.print("获取三方信息成功——————");
            if (mLoader != null) {
                mLoader.loginBySocial(map, share_media);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            AppLog.print("获取三方信息错误——————");

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            AppLog.print("获取三方信息失败——————");

        }
    };
    UMShareAPI mUmShareAPI;
    Activity mActivity;
    ContentLoader mLoader;

}
