package com.lalocal.lalocal.thread;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaojw on 2016/9/20.
 */
public class UpdateTask extends AsyncTask<String, Integer, Void> {
    private static final String APK_NAME = "lalocal.apk";
    ProgressDialog pBar;
    Context mContext;

    public UpdateTask(Context context) {
        mContext = context;
    }


    @Override
    protected void onPreExecute() {
        pBar = new ProgressDialog(mContext);    //进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setCancelable(false);
        pBar.setMessage("正在更新, 请稍候...");
        pBar.setProgress(0);
        pBar.setMax(0);
        pBar.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_update_bg));
        pBar.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(70000);
            urlConnection.setReadTimeout(70000);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            int resCode = urlConnection.getResponseCode();
            AppLog.print("url___"+params[0]+", resCode:"+resCode);
            if (resCode == HttpURLConnection.HTTP_OK) {
                int length = urlConnection.getContentLength();
                pBar.setMax((int) (length / Math.pow(1024, 2)));
                InputStream is = urlConnection.getInputStream();
                //设置进度条的总长度
                if (is != null) {
                    File file = new File(
                            Environment.getExternalStorageDirectory(),
                            APK_NAME);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024];   //这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一 下就下载完了，看不出progressbar的效果。
                    int b;
                    int process = 0;
                    AppLog.print("开始请求更新数据——————");
                    while ((b = is.read(buf)) != -1) {
                        fos.write(buf, 0, b);
                        process += b;
                        AppLog.print("progress__" + process);
                        pBar.setProgress((int) (process / Math.pow(1024, 2)));       //这里就是关键的实时更新进度了！
                    }
                    AppLog.print("完成请求更新数据——————");
                    fos.flush();
                    fos.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            AppLog.print("update IOException ”\n“  class:" + e.getClass() + "\n" + "message:" + e.getMessage() + "\n" + "cause:" + e.getCause());
//            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        pBar.cancel();
        File apkFile = new File(Environment.getExternalStorageDirectory(), APK_NAME);
        if (apkFile.exists()) {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkFile.getPath(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                AppLog.print(String.format("待更新app版本信息：name=%1$s, uid=%2$s, vname=%3$s, code=%4$s", packageInfo.packageName, packageInfo.sharedUserId, packageInfo.versionName, packageInfo.versionCode));
                String pkgName = packageInfo.packageName;
                int verCode = packageInfo.versionCode;
                if (pkgName.equals(AppConfig.getPackageName(mContext))) {
                    if (verCode > AppConfig.getVersionCode(mContext)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setDataAndType(Uri.fromFile(apkFile),
                                "application/vnd.android.package-archive");
                        mContext.startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        return;
                    }
                }
            }
        }
        Toast.makeText(mContext, "更新失败", Toast.LENGTH_SHORT).show();
    }


}
