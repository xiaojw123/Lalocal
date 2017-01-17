package com.lalocal.lalocal.live;

import android.content.Context;

import com.lalocal.lalocal.live.im.session.image.ImageLoaderKit;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * Created by wcj
 */
public class LiveCache {

    private static Context context;

    private static String account;

    private static NimUserInfo userInfo;

    private static boolean isLogin;

    private static boolean isLoginChatRoom;
    // 图片加载、缓存与管理组件
    private static ImageLoaderKit imageLoaderKit;

    public static void  clear() {
        account = null;
        userInfo = null;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        LiveCache.account = account;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        LiveCache.context = context.getApplicationContext();
    }

    public static boolean getLoginChatRoomStatus(){
        return  isLoginChatRoom;
    }
    public static void setLoginChatRoomStatus(boolean isLoginChatRoom){
        LiveCache.isLoginChatRoom=isLoginChatRoom;
    }
    public  static  boolean getLoginStatus(){
        return isLogin;
    }
    public static void setLoginStatus(boolean isLogin){
        LiveCache.isLogin=isLogin;
    }
    public static NimUserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = NIMClient.getService(UserService.class).getUserInfo(account);
        }

        return userInfo;
    }

    public static NimUserInfo getRegUserInfo(){
        userInfo= NIMClient.getService(UserService.class).getUserInfo(account);
        return  userInfo;
    }
    public static ImageLoaderKit getImageLoaderKit() {
        return imageLoaderKit;
    }

    public static void initImageLoaderKit() {
        imageLoaderKit = new ImageLoaderKit(context, DrawableUtils.getConfiguration(context));
    }
}
