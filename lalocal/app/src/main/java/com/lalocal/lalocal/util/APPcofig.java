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
    public static final  String GET_MY_FARORITE_ITEMS=BASE_URL+"praises?pageNumber=1&pageSize=10";
    public static final  String UPLOAD_HEDARE_URL=BASE_URL+"users/avatar";
    public static  final  String MODIFY_USER_PROFILE_URL=BASE_URL+"users/profile";
    public static final  String GET_USER_PROFILE_URL=BASE_URL+"users/profile";
    public static  final  String BOUND_EMAIL_URL=BASE_URL+"users/bindEmail";
    public static final String USER_PROTOCOL_URL = "http://h5.lalocal.cn/static/userRole.html";

    //推荐接口
    public static final String RECOMMEND_URL=BASE_URL+"themes?";
    //推荐广告位接口
    public static final String RECOMMEND_AD=BASE_URL+"advertising?type=0";

    //专题详情接口
    public static final String SPECIAL_DETAILS_URL="http://dev.lalocal.cn:8080/api/themes/";
    public static  final String IN_LAND_PHONE="400-017-8056";
    public static  final String FOREIGEN_PHONE="(+86)0571-86808267";

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

    public static String  getInlandPhone(){
        return IN_LAND_PHONE;
    }
    public static String getForeigenPhone(){
        return FOREIGEN_PHONE;
    }


}
