package com.lalocal.lalocal.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by xiaojw on 2016/6/8.
 */
public class DataCleanManager {
    public static String getTotalCacheSize(Context context) {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                AppLog.print("externalCacheDir___" + context.getExternalCacheDir());
                cacheSize += getFolderSize(context.getExternalCacheDir());
                cacheSize += getFolderSize(DrawableUtils.getFileDir());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getFormatSize(cacheSize);
    }

    public static void clearAllCache(Context context) {
        boolean s1 = deleteDir(context.getCacheDir());
        AppLog.print("s1___" + s1);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            boolean s2 = deleteDir(context.getExternalCacheDir());
            AppLog.print("s2___" + s2);
            boolean s3 = deleteDir(DrawableUtils.getFileDir());
            AppLog.print("s3___" + s3);
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        AppLog.print("getFormatSize_toal__" + size);
        double mByte = size / Math.pow(1024, 2);
//        if (mByte < 1) {
        BigDecimal result2 = new BigDecimal(Double.toString(mByte));
        return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB";
//        }

//        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
////            return size + "Byte";
//            return "0KB";
//        }
//
//        double megaByte = kiloByte / 1024;
//        if (megaByte < 1) {
//            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
//            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
//                    .toPlainString() + "KB";
//        }
//
//        double gigaByte = megaByte / 1024;
//        if (gigaByte < 1) {
//            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
//            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
//                    .toPlainString() + "MB";
//        }
//
//        double teraBytes = gigaByte / 1024;
//        if (teraBytes < 1) {
//            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
//            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
//                    .toPlainString() + "GB";
//        }
//        BigDecimal result4 = new BigDecimal(teraBytes);
//        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
//                + "TB";
    }


}
