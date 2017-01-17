package com.lalocal.lalocal.live.im.business;

import android.app.Activity;
import android.content.Context;

import com.lalocal.lalocal.live.LiveCache;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.inject.FlavorDependent;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout(Context context, boolean isKickOut) {
        AuthPreferences.saveUserToken("");
        // 清理缓存&注销监听&清除状态
        LiveCache.getImageLoaderKit().clear();
        // flavor do logout
        FlavorDependent.getInstance().onLogout();
        LiveCache.clear();

        NIMClient.getService(AuthService.class).logout();
        LiveCache.setLoginStatus(false);

        // 启动登录
   //     LoginActivity.start(context, isKickOut);
        ((Activity)context).finish();
    }
}
