package com.lalocal.lalocal.live.entertainment.agora.openlive;

import io.agora.rtc.IRtcEngineEventHandler;

public interface AGEventHandler {
    void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed);

    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);//其他用户离开当前频道回调
    void onUserJoined(int uid, int elapsed);//其他用户加入当前频道
    void onConnectionInterrupted();//连接中断回调
    void onConnectionLost();//连接丢失回调
    void onError(int err);//错误监听
    void onVideoStopped();//停止视频功能
   void  onLeaveChannel(IRtcEngineEventHandler.RtcStats stats);//离开频道的回调
    void onUserEnableVideo(int uid, boolean enabled);
}
