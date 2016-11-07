package com.lalocal.lalocal.live.entertainment.agora;


import io.agora.rtc.RtcEngine;

/*
*美颜强度设置
*
* */
public class Constant {

    public static final String MEDIA_SDK_VERSION;

    static {
        String sdk = "undefined";
        try {
            sdk = RtcEngine.getSdkVersion();
        } catch (Throwable e) {
        }
        MEDIA_SDK_VERSION = sdk;
    }

    public static boolean PRP_ENABLED = true;
    public static float PRP_DEFAULT_LIGHTNESS = 0.65f;
    public static float PRP_DEFAULT_SMOOTHNESS = 1.0f;
    public static final float PRP_MAX_LIGHTNESS = 1.0f;
    public static final float PRP_MAX_SMOOTHNESS = 1.0f;

    public static boolean CAMERABACK=false;


}
