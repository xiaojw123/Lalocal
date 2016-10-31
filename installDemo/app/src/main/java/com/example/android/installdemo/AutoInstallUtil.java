package com.example.android.installdemo;

import android.content.Context;

import top.wuhaojie.installerlibrary.AutoInstaller;

/**
 * Created by android on 2016/10/29.
 */
public class AutoInstallUtil {
  public static final   String APK_FILE_PATH=null;//文件地址；
    public static void installApk(Context context) {
        // 文件下载到 指定文件地址下  ， 通过逻辑获取本地文件路径下色apk 名字 ，在做拼接 完成最后的完整地址
        AutoInstaller.getDefault(context).install(APK_FILE_PATH);
    }

}
