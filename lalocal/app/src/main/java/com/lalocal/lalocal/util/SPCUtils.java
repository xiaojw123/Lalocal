package com.lalocal.lalocal.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lenovo on 2016/4/25.
 */
public class SPCUtils {
    private static final String CONFIG_NAME = "config";

    public static String getString(Context ctx, String key){
        SharedPreferences sp  = ctx.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
    public static Boolean getBoolean(Context ctx, String key){
        SharedPreferences sp = ctx.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }



    public static int getInt(Context ctx, String key){
        SharedPreferences sp = ctx.getSharedPreferences(CONFIG_NAME , Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }


    public static void put(Context ctx, String key , Object value){
        SharedPreferences sp = ctx.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if(value instanceof String){
            edit.putString(key, (String) value);
        }else if(value instanceof Integer){
            edit.putInt(key, (Integer) value);
        }else if(value instanceof Boolean){
            edit.putBoolean(key, (Boolean) value);
        }
        edit.commit();
    }

}
