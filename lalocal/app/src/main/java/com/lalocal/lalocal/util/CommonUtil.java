package com.lalocal.lalocal.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaojw on 2016/6/6.
 */
public class CommonUtil {
    private static double EARTH_RADIUS = 6378.137;
    private  static  final  int READ_PHONE_STATE_CODE=112;

    public static  int RESULT_DIALOG=0;


    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    //textview设置不同前景色
    public static  SpannableString getSpannelStyle(Context context,String text,int appearance,int start ,int end){
        SpannableString spannableStr=new SpannableString(text);
        spannableStr.setSpan(new TextAppearanceSpan(context, appearance), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return  spannableStr;
    }


    /**
     * 根据两个位置的经纬度，来计算两地的距离（单位为KM）
     * 参数为String类型
     * @param lng1 loc1经度
     * @param lat1 loc1纬度
     * @param lng2 loc2经度
     * @param lat2 loc2纬度
     * @return
     */
    public static String getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance =(double) (Math.round(distance * 10))/ 10;
        return String.valueOf(distance);
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static String formartOrderPrice(double price) {
        NumberFormat nf = new DecimalFormat("¥ ###,###,###,###,###,###,###,###.##");
        return nf.format(price);
    }
    public static String fomartStartOrderPrice(double price){
        return  formartOrderPrice(price)+" 起";
    }

    //验证邮箱格式
    public static boolean checkEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    //验证密码长度
    public static boolean checkPassword(String psw) {
        int len = psw.length();
        if (psw != null && len >= 6 && len <= 20) {
            return true;
        }
        return false;
    }

    //验证昵称长度
    public static boolean checkNickname(String nickname) {
        if (!TextUtils.isEmpty(nickname) && nickname.length() < 20) {
            return true;
        }
        return false;
    }

    public static String getUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
//        String tmDevice, tmSerial, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        return deviceUuid.toString();
    }


    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() < 1) {
            return true;
        } else {
            return false;
        }

    }

    public static void showPromptDialog(Context context, String message, CustomDialog.CustomDialogListener listener) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setCancelable(false);
        dialog.setTitle(context.getResources().getString(R.string.prompt));
        dialog.setMessage(message);
        dialog.setNeturalBtn(context.getResources().getString(R.string.sure), listener);
        dialog.show();
    }

    public static void showToast(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }





}
