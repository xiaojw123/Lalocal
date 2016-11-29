package com.lalocal.lalocal.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.model.User;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

/**
 * Created by xiaojw on 2016/7/7.
 */
public class UserHelper {
    //保存收藏 ，订单，优惠券使用记录
    static SharedPreferences sp;

    private static void initSPref(Context context) {
        if (sp == null && context != null) {
            sp = context.getSharedPreferences("userparams", Context.MODE_PRIVATE);
        }
    }
    public static void setLoginSuccessResult(Activity activity, User user){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MeFragment.USER, user);
        activity.setResult(MeFragment.LOGIN_OK, resultIntent);
        activity.finish();
    }

    public static void updateSignOutInfo(Context context) {
        MobHelper.singOff();
        AuthPreferences.clearUserInfo();
        DemoCache.clear();
        NIMClient.getService(AuthService.class).logout();
        DemoCache.setLoginStatus(false);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KeyParams.IS_LOGIN, false);
        UserHelper.saveLoginInfo(context, bundle);
    }


    public static void saveLoginInfo(Context context, Bundle bundle) {
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KeyParams.IS_LOGIN, bundle.getBoolean(KeyParams.IS_LOGIN));
        editor.putString(KeyParams.EMAIL, bundle.getString(KeyParams.EMAIL));
        editor.putInt(KeyParams.USERID, bundle.getInt(KeyParams.USERID, -1));
        editor.putString(KeyParams.TOKEN, bundle.getString(KeyParams.TOKEN));
        editor.putString(KeyParams.AVATAR, bundle.getString(KeyParams.AVATAR));
        editor.putString(KeyParams.IM_TOKEN, bundle.getString(KeyParams.IM_TOKEN));
        editor.putString(KeyParams.IM_CCID, bundle.getString(KeyParams.IM_CCID));
        editor.putString(KeyParams.NICKNAME, bundle.getString(KeyParams.NICKNAME));
        editor.putInt(KeyParams.SORTVALUE,bundle.getInt(KeyParams.SORTVALUE));
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
    public  static int getStatus(Context context){
        initSPref(context);
        return sp.getInt(KeyParams.STATUS,-1);
    }


    public static int getUserId(Context context) {
        initSPref(context);
        return sp.getInt(KeyParams.USERID, -1);

    }

    public static int getSortValue(Context context){
        initSPref(context);
        return sp.getInt(KeyParams.SORTVALUE, -1);
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
