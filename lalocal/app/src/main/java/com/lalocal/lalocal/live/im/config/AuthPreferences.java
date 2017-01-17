package com.lalocal.lalocal.live.im.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.lalocal.lalocal.live.LiveCache;


/**
 * Created by hzxuwen on 2015/4/13.
 */
public class AuthPreferences {
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_TOKEN = "token";
    private static final String KEY_USER_AVATAR="avatar";

    public static void saveUserAvatar(String avatar){
        saveString(KEY_USER_AVATAR,avatar);
    }

    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }
    public static  String getKeyUserAvatar(){
        return getString(KEY_USER_AVATAR);
    }
    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static  void clearUserInfo(){
        saveString(KEY_USER_ACCOUNT,null);
        saveString(KEY_USER_TOKEN,null);
    }
    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    static SharedPreferences getSharedPreferences() {
        return LiveCache.getContext().getSharedPreferences("Demo", Context.MODE_PRIVATE);
    }
}
