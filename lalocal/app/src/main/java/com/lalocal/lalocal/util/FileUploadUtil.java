package com.lalocal.lalocal.util;

import android.content.Context;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaojw on 2016/6/13.
 */
public class FileUploadUtil {
    public static void uploadFile(final  Context context, final byte[] bodyBytes, final int userid, final String token) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(APPcofig.UPLOAD_HEDARE_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type","multipart/form-data; charset=utf-8; boundary=__X_PAW_BOUNDARY__");
                    urlConnection.setRequestProperty("APP_VERSION", APPcofig.getVersionName(context));
                    urlConnection.setRequestProperty("DEVICE", "android");
                    urlConnection.setRequestProperty("DEVICE_ID", CommonUtil.getUUID(context));
                    urlConnection.setRequestProperty("USER_ID",String.valueOf(userid));
                    urlConnection.setRequestProperty("TOKEN",token);
                    urlConnection.connect();
                    DataOutputStream dos=new DataOutputStream(urlConnection.getOutputStream());
                    dos.writeBytes("avatar=");
                    dos.write(bodyBytes);
                    dos.flush();
                    AppLog.print("response__code_"+urlConnection.getResponseCode());
                    AppLog.print("response__msg_"+urlConnection.getResponseMessage());
                    dos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

}
