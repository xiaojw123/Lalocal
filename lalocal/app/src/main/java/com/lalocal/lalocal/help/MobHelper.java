package com.lalocal.lalocal.help;

import android.content.Context;

import com.lalocal.lalocal.MyApplication;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by xiaojw on 2016/10/19.
 */

public class MobHelper {

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


}
