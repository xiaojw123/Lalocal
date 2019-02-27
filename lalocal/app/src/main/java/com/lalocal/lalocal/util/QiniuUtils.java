package com.lalocal.lalocal.util;

import android.text.TextUtils;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

/**
 * Created by wangjie on 2016/11/1.
 *
 * 常用的七牛云有关封装
 */
public class QiniuUtils {

    public static final int NOT_SET = -999;

    /**
     * 等比缩放后居中裁剪
     * @param url 图片链接

     * @param width 最小宽度 单位：px
     * @param height 最小高度 单位：px

     * @param width 最小宽度
     * @param height 最小高度

     * @return
     */
    public static String centerCrop(String url, int width, int height) {

        // 如果url为空或url中使用了七牛云的imageView2图片处理，则直接返回传入的url
        if (TextUtils.isEmpty(url) || url.contains("imageView2")) {
            return url;
        }

        String widthUrl = "/w/" + width;
        String heightUrl = "/h/" + height;

        // 如果url不包含“？”，则在传入参数前添加“？”；否则反之
        if (!url.contains("?")) {
            url += "?";
        }

        url += "imageView2/1";

        if (width != NOT_SET) {
            url += widthUrl;
        }

        if (height != NOT_SET) {
            url += heightUrl;
        }

        return url;
    }

    /**
     * 上传简单文件
     * @param filePath 文件路径，手机中的图片路径
     * @param fileName 指定上传到七牛云上后图片的文件名，后台获取token的时候有fileName
     * @param token 从服务端获取的token
     * @return 图片是否上传成功
     */
    public static void uploadSimpleFile(String filePath, String fileName, String token, final OnUploadListener listener) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(filePath, fileName, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.statusCode == 200) {
                            listener.onUploadSuccess(info);
                        } else {
                            listener.onUploadFail(info);
                        }
                        listener.afterUpload(info);
                    }
                }, null);
    }

    /**
     * 无监听事件的单一文件上传封装
     * @param bytesImg
     * @param fileName
     * @param token
     * @return
     */
    public static boolean uploadSimpleFile(byte[] bytesImg, final String fileName, final String token) {
        final boolean[] isSuccess = {false};
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(bytesImg, fileName, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        AppLog.i("TAG","key:"+key+"     fileName:"+fileName+"    info:"+info.statusCode+(response==null?"     空":response.toString()));
                        if (info.statusCode == 200) {
                            isSuccess[0] = true;

                        }
                    }
                }, null);
        return isSuccess[0];
    }

    /**
     * 上传接口
     */
    public interface OnUploadListener {
        void onUploadSuccess(ResponseInfo info);
        void onUploadFail(ResponseInfo info);
        void afterUpload(ResponseInfo info);
    }

}
