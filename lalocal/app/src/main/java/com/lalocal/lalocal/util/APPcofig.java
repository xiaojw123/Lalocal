package com.lalocal.lalocal.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by xiaojw on 2016/6/6.
 */
public class APPcofig {
    public static final String BASE_URL = "http://dev.lalocal.cn:8080/api/";
    //注册接口
    public static final String REGISTER_URL = BASE_URL + "users/register";
    //登录接口
    public static final String LOGIN_URL = BASE_URL + "users/login";
    public static  final String CHECK_EMAIL_URL=BASE_URL+"users/checkEmail";
    public static final  String SEND_VERIFICATION_URL=BASE_URL+"system/sendEmail";
    public static final String RESET_PASSWORD_URL=BASE_URL+"users/forgetPassword";
    public static final String USER_PROTOCOL_URL = "http://h5.lalocal.cn/static/userRole.html";


    public static String getVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0.0.0";
    }


}
