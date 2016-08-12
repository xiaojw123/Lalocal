package com.lalocal.lalocal.view.liveroomview.inject;

import android.app.Activity;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

/**
 * Created by huangjun on 2016/3/15.
 */
public interface IFlavorDependent {

    String getFlavorName();

    Class<? extends Activity> getMainClass();

    MsgAttachmentParser getMsgAttachmentParser();

    void onLogout();

    void onApplicationCreate();
}
