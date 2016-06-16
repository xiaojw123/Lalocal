package com.lalocal.lalocal.util;

import android.content.Context;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaojw on 2016/6/13.
 */
public class FileUploadUtil {
    private static String delimiter = "--";
    private static  String boundary = "SwA" + Long.toString(System.currentTimeMillis()) + "SwA";
    public static  void uploadFile(Context context,byte[] data) throws Exception {
        AppLog.print("uploadFile____data:"+data);
        String paramName="file";
        String fileName="header.jpg";
        HttpURLConnection con = (HttpURLConnection) (new URL(APPcofig.UPLOAD_HEDARE_URL)).openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setReadTimeout(5000);
        con.setConnectTimeout(5000);
        AppLog.print("1__");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.setRequestProperty("APP_VERSION",APPcofig.getVersionName(context));
        con.setRequestProperty("DEVICE","android");
        con.setRequestProperty("DEVICE_ID",CommonUtil.getUUID(context));
        AppLog.print("2__");
        OutputStream os = con.getOutputStream();
        AppLog.print("3__");
        os.write((delimiter + boundary + "\r\n").getBytes());
        AppLog.print("4__");
        os.write(("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
        os.write(("Content-Type: application/octet-stream\r\n").getBytes());
        os.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
        os.write("\r\n".getBytes());
        os.write(data);
        AppLog.print("5__");
        os.write("\r\n".getBytes());
        con.connect();
        AppLog.print("6__");
        int code=con.getResponseCode();
        String msg= con.getResponseMessage();
        AppLog.print("code___"+code+", message__"+msg);

    }

    public  void uploadImg(){


    }
//    headers.put("APP_VERSION", APPcofig.getVersionName(context));
//    headers.put("DEVICE", "android");
//    headers.put("DEVICE_ID", CommonUtil.getUUID(context));

}
