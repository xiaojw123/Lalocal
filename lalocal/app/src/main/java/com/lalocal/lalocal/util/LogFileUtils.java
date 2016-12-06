package com.lalocal.lalocal.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by android on 2016/12/5.
 */
public class LogFileUtils {
    public static final String  fileNimPath="logs/nim/android/";
    public static final String fileAgoraPath="logs/ago/android/";


    public static File makeNimFilePath(String fileName) {
        File file = null;
        createNimLogMkdir();
        try {
            file = new File(Environment.getExternalStorageDirectory(),fileNimPath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File makeAgoraFilePath(String fileName) {
        File file = null;
        createAroraLogMkdir();
        try {
            file = new File(Environment.getExternalStorageDirectory(),fileAgoraPath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    public  static void createNimLogMkdir(){
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory(),fileNimPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            AppLog.i("TAG","创建云信文件夹失败");
        }

    }
    public  static void createAroraLogMkdir(){
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory(),fileAgoraPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            AppLog.i("TAG","创建声网文件夹失败");
        }

    }

}
