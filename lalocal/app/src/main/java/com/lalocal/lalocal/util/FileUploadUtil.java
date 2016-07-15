package com.lalocal.lalocal.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

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
    public static void uploadFile(Context context, Intent backIntent, byte[] bodyData, int userid, String token) {
        UploadTask task = new UploadTask(context, backIntent);
        task.setHeadParams(String.valueOf(userid), token);
        task.setBodyBytes(bodyData);
        task.execute(AppConfig.getHeaderUpdateUrl());
    }

    static class UploadTask extends AsyncTask<String, Void, String> {
        Context context;
        String userid, token, uuid;
        byte[] bodyBytes;
        Intent intent;

        public UploadTask(Context context, Intent intent) {
            this.context = context;
            this.intent = intent;
        }

        public void setHeadParams(String userid, String token) {
            this.userid = userid;
            this.token = token;
            uuid = CommonUtil.getUUID(context);
        }

        public void setBodyBytes(byte[] bodyBytes) {
            this.bodyBytes = bodyBytes;
        }


        @Override
        protected String doInBackground(String... params) {
            StringBuffer sb = new StringBuffer();
            try {
                String end = "\r\n";
                String twoHyphens = "--";
                String boundary = "__X_PAW_BOUNDARY__";
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "multipart/form-data; charset=utf-8; boundary=__X_PAW_BOUNDARY__");
                urlConnection.setRequestProperty("APP_VERSION", AppConfig.getVersionName(context));
                urlConnection.setRequestProperty("DEVICE", "android");
                urlConnection.setRequestProperty("DEVICE_ID", uuid);
                urlConnection.setRequestProperty("USER_ID", String.valueOf(userid));
                urlConnection.setRequestProperty("TOKEN", token);
                DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; " + "name=avatar;filename=alipay_m@3x.png; mimeType:image/*" + end);
                dos.writeBytes(end);
                dos.write(bodyBytes);
                dos.writeBytes(end);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                dos.flush();
                InputStream is = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                if ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                is.close();
                dos.close();
            } catch (Exception e) {
//                e.printStackTrace();
                return null;
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (TextUtils.isEmpty(result)) {
                CommonUtil.showToast(context, "上传头像失败", Toast.LENGTH_SHORT);
            } else {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject resultJson = jsonObj.optJSONObject("result");
                    String avatar = resultJson.optString("avatar", null);
                    intent.putExtra(KeyParams.AVATAR, avatar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
