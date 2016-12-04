package com.lalocal.lalocal.net;

import com.lalocal.lalocal.util.AppLog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaojw on 2016/11/15.
 */

public class TestMananger {


    public static void testSearch(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.connect();
            int code = urlConnection.getResponseCode();
            AppLog.print("testSearch  CODE____" + code);
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream is = urlConnection.getInputStream();
                int b=-1;
                byte[] buffer=new byte[1024];
                while ((b=is.read(buffer))!=-1){
                    bos.write(buffer,0,buffer.length);
                }
                AppLog.print("testSearch 结果输出——————"+bos.toString());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
