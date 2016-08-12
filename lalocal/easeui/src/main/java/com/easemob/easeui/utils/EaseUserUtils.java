package com.easemob.easeui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easemob.easeui.R;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.easemob.easeui.domain.EaseUser;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * 根据username获取相应user
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }



    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView, int rule) {
//        EaseUser user = getUserInfo(username);
//        if (user != null && user.getAvatar() != null) {
            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(avatarResId).into(imageView);
                SharedPreferences sp = context.getSharedPreferences("userparams", Context.MODE_PRIVATE);
                boolean isLogin = sp.getBoolean("isLogin", false);
                if (isLogin) {
                    String avatar = sp.getString("avatar", null);
                    Log.d("lalocal", "setUserAvatar(Context   avatar url___" + avatar);
                    if (!TextUtils.isEmpty(avatar)) {
                        Glide.with(context).load(avatar).into(imageView);
                    }
                }
            } catch (Exception e) {
                //正常的string路径
//                showDefaultAvatar(context, imageView, rule, user);
            }
//        } else {
////            showDefaultAvatar(context, imageView, rule, user);
//        }
    }



    private static void showDefaultAvatar(Context context, ImageView imageView, int rule, EaseUser user) {
        switch (rule) {
            case 1:
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.vistor_header_default).into(imageView);
                break;
            case 2:
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.service_header_default).into(imageView);
                break;
        }
    }

    /**
     * 设置用户昵称
     */

    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            username = "小LA";
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {
                textView.setText(username);
            }
        }
    }

}
