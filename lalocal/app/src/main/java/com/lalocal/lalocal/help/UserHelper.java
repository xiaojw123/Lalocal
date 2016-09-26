package com.lalocal.lalocal.help;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2016/7/7.
 */
public class UserHelper {
    //保存收藏 ，订单，优惠券使用记录
    public static List<String> favorites = new ArrayList<>();
    public static List<String> orders = new ArrayList<>();
    public static List<String> coupons = new ArrayList<>();
    static SharedPreferences sp;

    private static void initSPref(Context context) {
        if (sp == null && context != null) {
            sp = context.getSharedPreferences("userparams", Context.MODE_PRIVATE);
        }
    }

    public static void updateSignOutInfo(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KeyParams.IS_LOGIN, false);
        UserHelper.saveLoginInfo(context, bundle);
        AuthPreferences.clearUserInfo();
        DemoCache.clear();
        NIMClient.getService(AuthService.class).logout();
        DemoCache.setLoginStatus(false);
    }


    public static void saveLoginInfo(Context context, Bundle bundle) {
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KeyParams.IS_LOGIN, bundle.getBoolean(KeyParams.IS_LOGIN));
        editor.putString(KeyParams.EMAIL, bundle.getString(KeyParams.EMAIL));
        editor.putString(KeyParams.PASSWORD, bundle.getString(KeyParams.PASSWORD));
        editor.putInt(KeyParams.USERID, bundle.getInt(KeyParams.USERID, -1));
        editor.putString(KeyParams.TOKEN, bundle.getString(KeyParams.TOKEN));
        editor.putString(KeyParams.AVATAR, bundle.getString(KeyParams.AVATAR));
        editor.putString(KeyParams.IM_TOKEN, bundle.getString(KeyParams.IM_TOKEN));
        editor.putString(KeyParams.IM_CCID, bundle.getString(KeyParams.IM_CCID));
        editor.putString(KeyParams.NICKNAME, bundle.getString(KeyParams.NICKNAME));
        editor.commit();
    }
    public static void updatePassword(Context context, String psw){
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KeyParams.PASSWORD, psw);
        editor.commit();
    }

    public static void updateNickName(Context context, String nickName) {
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KeyParams.NICKNAME, nickName);
        editor.commit();
    }

    public static void updateAvatar(Context context, String avatar) {
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KeyParams.AVATAR, avatar);
        editor.commit();
    }

    public static void updateEmail(Context context, String email) {
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KeyParams.EMAIL, email);
        editor.commit();
    }


    public static boolean isLogined(Context context) {
        initSPref(context);
        return sp.getBoolean(KeyParams.IS_LOGIN, false);
    }

    public static String getUserEmail(Context context) {
        initSPref(context);
        return sp.getString(KeyParams.EMAIL, null);
    }

    public static String getPassword(Context context) {
        initSPref(context);
        return sp.getString(KeyParams.PASSWORD, null);
    }

    public static int getUserId(Context context) {
        initSPref(context);
        return sp.getInt(KeyParams.USERID, -1);

    }

    public static String getToken(Context context) {
        initSPref(context);
        return sp.getString(KeyParams.TOKEN, null);

    }

    public static String getUserAvatar(Context context) {
        initSPref(context);
        return sp.getString(KeyParams.AVATAR, null);
    }

    public static String getUserName(Context context) {
        initSPref(context);
        return sp.getString(KeyParams.NICKNAME, null);
    }

    public static String getImccId(Context context) {
        initSPref(context);
        return sp.getString(KeyParams.IM_CCID, null);
    }

    public static String getImToken(Context context) {
        initSPref(context);
        return sp.getString(KeyParams.IM_TOKEN, null);
    }

    public static void clearUserInfo(Context context) {
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KeyParams.IM_TOKEN, null);
        editor.putString(KeyParams.IM_CCID, null);
        editor.commit();
    }
}
