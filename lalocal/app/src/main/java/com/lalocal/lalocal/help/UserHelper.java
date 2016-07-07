package com.lalocal.lalocal.help;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by xiaojw on 2016/7/7.
 */
public class UserHelper {
    static SharedPreferences sp;

    private static void initSPref(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("userparams", Context.MODE_PRIVATE);
        }
    }

    public static void saveLoginInfo(Context context, Bundle bundle) {
        initSPref(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KeyParams.IS_LOGIN, bundle.getBoolean(KeyParams.IS_LOGIN));
        editor.putString(KeyParams.EMAIL, bundle.getString(KeyParams.EMAIL));
        editor.putString(KeyParams.PASSWORD, bundle.getString(KeyParams.PASSWORD));
        editor.putInt(KeyParams.USERID, bundle.getInt(KeyParams.USERID));
        editor.putString(KeyParams.TOKEN, bundle.getString(KeyParams.TOKEN));
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
        return sp.getString(KeyParams.TOKEN,null);

    }
}
