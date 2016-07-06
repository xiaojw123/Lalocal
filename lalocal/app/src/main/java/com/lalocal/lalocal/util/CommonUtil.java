package com.lalocal.lalocal.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

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
    private static int mUserid = -1;
    private static String mToken = null;

    //人民币
    public static String formartPrice(String price) {
        NumberFormat nf = new DecimalFormat("¥ ###,###,###,###,###,###,###,###");
        long d = Long.parseLong(price);
        return nf.format(d);
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

    public static void setUserParams(int userid, String token) {
        mUserid = userid;
        mToken = token;
    }

    public static int getUserId() {
        return mUserid;
    }

    public static String getUserToken() {
        return mToken;
    }


}
