package com.lalocal.lalocal.util;

import android.text.TextUtils;

/**
 * Created by wangjie on 2016/11/1.
 */
public class QiniuUtils {

    /**
     * 等比缩放后居中裁剪
     * @param url 图片链接
     * @param width 最小宽度
     * @param height 最小高度
     * @return
     */
    public static String centerCrop(String url, int width, int height) {

        // 如果url为空或url中使用了七牛云的imageView2图片处理，则直接返回传入的url
        if (TextUtils.isEmpty(url) || url.contains("imageView2")) {
            return url;
        }

        // 如果url不包含“？”，则在传入参数前添加“？”；否则反之
        if (url.contains("?")) {
            return url + "/imageView2/1/w/" + width + "/h/" + height;
        } else {
            return url + "?imageView2/1/w/" + width + "/h/" + height;
        }
    }
}
