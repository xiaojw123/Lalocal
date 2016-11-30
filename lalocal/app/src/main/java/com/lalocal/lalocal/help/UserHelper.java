package com.lalocal.lalocal.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.model.MessageItem;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.util.AppLog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaojw on 2016/7/7.
 */
public class UserHelper {
    //保存收藏 ，订单，优惠券使用记录
    public static int NOTICE_VALUE=0;
    static SharedPreferences sp;
    static String CUSTOM_ALIAS_TPYE = "USER_ID";

    private static void initSPref(Context context) {
        if (sp == null && context != null) {
            sp = context.getSharedPreferences("userparams", Context.MODE_PRIVATE);
        }
    }

    public static void setLoginSuccessResult(Activity activity, User user) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MeFragment.USER, user);
        activity.setResult(MeFragment.LOGIN_OK, resultIntent);
        activity.finish();
    }

    public static void updateSignOutInfo(Context context) {
        removeUserAlias(context);
        MobHelper.singOff();
        AuthPreferences.clearUserInfo();
        DemoCache.clear();
        NIMClient.getService(AuthService.class).logout();
        DemoCache.setLoginStatus(false);
        DataSupport.deleteAll(MessageItem.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KeyParams.IS_LOGIN, false);
        UserHelper.saveLoginInfo(context, bundle);
    }

    private static void removeUserAlias(Context context) {
        PushAgent.getInstance(context).removeAlias(String.valueOf(UserHelper.getUserId(context)), CUSTOM_ALIAS_TPYE, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
                AppLog.print("移除Alias___" + b);
            }
        });
    }

    private static void addUserAlias(Context context, int userid) {
        PushAgent.getInstance(context).addAlias(String.valueOf(userid), CUSTOM_ALIAS_TPYE, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
                AppLog.print("添加Alias___" + b);
            }
        });
    }


    public static void saveLoginInfo(Context context, Bundle bundle) {
        int userid = bundle.getInt(KeyParams.USERID, -1);
        boolean isLogin = bundle.getBoolean(KeyParams.IS_LOGIN);
        addUserAlias(context, userid);
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        if (!isLogin) {
            editor.putString(KeyParams.DATE_TIME, "");
        }
        editor.putBoolean(KeyParams.IS_LOGIN, isLogin);
        editor.putInt(KeyParams.LOGIN_CHANNEL, bundle.getInt(KeyParams.LOGIN_CHANNEL));
        editor.putString(KeyParams.EMAIL, bundle.getString(KeyParams.EMAIL));
        editor.putInt(KeyParams.USERID, userid);
        editor.putString(KeyParams.TOKEN, bundle.getString(KeyParams.TOKEN));
        editor.putString(KeyParams.AVATAR, bundle.getString(KeyParams.AVATAR));
        editor.putString(KeyParams.IM_TOKEN, bundle.getString(KeyParams.IM_TOKEN));
        editor.putString(KeyParams.IM_CCID, bundle.getString(KeyParams.IM_CCID));
        editor.putString(KeyParams.NICKNAME, bundle.getString(KeyParams.NICKNAME));
        editor.putInt(KeyParams.SORTVALUE,bundle.getInt(KeyParams.SORTVALUE));
        editor.commit();
    }

    public static void updateDateTime(Context context, String dateTime) {
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KeyParams.DATE_TIME, dateTime);
        editor.commit();

    }

    public static String getDateTime(Context context) {
        initSPref(context);
        return sp.getString(KeyParams.DATE_TIME, "");
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

    public static int getStatus(Context context) {
        initSPref(context);
        return sp.getInt(KeyParams.STATUS, -1);
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

    public static int getLoginChannel(Context context) {
        initSPref(context);
        return sp.getInt(KeyParams.LOGIN_CHANNEL, -1);

    }

}
