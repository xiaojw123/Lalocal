package com.lalocal.lalocal.util;

import android.content.Context;
import android.content.Intent;

import com.lalocal.lalocal.help.KeyParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaojw on 2016/6/13.
 */
public class FileUploadUtil {
    public static void uploadFile(final Context context, final Intent backIntent, final byte[] bodyData, final int userid, final String token) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String end = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "__X_PAW_BOUNDARY__";
                    URL url = new URL(AppConfig.UPLOAD_HEDARE_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "multipart/form-data; charset=utf-8; boundary=__X_PAW_BOUNDARY__");
                    urlConnection.setRequestProperty("APP_VERSION", AppConfig.getVersionName(context));
                    urlConnection.setRequestProperty("DEVICE", "android");
                    urlConnection.setRequestProperty("DEVICE_ID", CommonUtil.getUUID(context));
                    urlConnection.setRequestProperty("USER_ID", String.valueOf(userid));
                    urlConnection.setRequestProperty("TOKEN", token);
                    urlConnection.connect();
                    DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + end);
                    dos.writeBytes("Content-Disposition: form-data; " + "name=avatar;filename=alipay_m@3x.png; mimeType:image/png" + end);
                    dos.writeBytes(end);
                    dos.write(bodyData);
                    dos.writeBytes(end);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    dos.flush();
                    dos.close();
                    AppLog.print("rescode__" + urlConnection.getResponseCode());
                    InputStream is = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuffer sb = new StringBuffer();
                    String line = null;
                    if ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    is.close();
                    backIntent.putExtra(KeyParams.AVATAR, getAvatar(sb.toString()));
                    AppLog.print("res message___" + sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public static String getAvatar(String result) throws JSONException {
        JSONObject jsonObj = new JSONObject(result);
        JSONObject resultJson = jsonObj.optJSONObject("result");
        return resultJson.optString("avatar", null);

    }

}
