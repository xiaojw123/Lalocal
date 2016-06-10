package com.lalocal.lalocal.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomDialog;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaojw on 2016/6/6.
 */
public class CommonUtil{

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
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();

    }


    public static void showPromptDialog(Context context, String message, CustomDialog.CustomDialogListener listener) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle(context.getResources().getString(R.string.prompt));
        dialog.setMessage(message);
        dialog.setNeturalBtn(context.getResources().getString(R.string.sure), listener);
        dialog.show();
    }

}
