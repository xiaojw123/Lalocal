package com.lalocal.lalocal.live.entertainment.agora.openlive;

import android.content.Context;
import android.util.Log;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.IRtcEngineEventHandler;

public class MyEngineEventHandler {
    public MyEngineEventHandler(Context ctx, EngineConfig config) {
        this.mContext = ctx;
        this.mConfig = config;
    }

    private final EngineConfig mConfig;

    private final Context mContext;

    private final ConcurrentHashMap<AGEventHandler, Integer> mEventHandlerList = new ConcurrentHashMap<>();

    public void addEventHandler(AGEventHandler handler) {
        this.mEventHandlerList.put(handler, 0);
    }

    public void removeEventHandler(AGEventHandler handler) {
        this.mEventHandlerList.remove(handler);
    }

    final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        @Override
        public void onUserEnableVideo(int uid, boolean enabled) {
            super.onUserEnableVideo(uid, enabled);
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onUserEnableVideo(uid,enabled);
            }

        }

        //远端视频接收解码回调
        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
            }
        }
        //本地视频显示回调
        @Override
        public void onFirstLocalVideoFrame(int width, int height, int elapsed) {

        }

        //其他用户进入当前频道回调
        @Override
        public void onUserJoined(int uid, int elapsed) {
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onUserJoined(uid, elapsed);
            }

        }


        //其他用户离开当前频道回调
        @Override
        public void onUserOffline(int uid, int reason) {
            // FIXME this callback may return times
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onUserOffline(uid, reason);
            }
        }
        //其他用户停止或重启视频回调
        @Override
        public void onUserMuteVideo(int uid, boolean muted) {
        }
        //统计数据回调
        @Override
        public void onRtcStats(RtcStats stats) {
        }


        //离开频道
        @Override
        public void onLeaveChannel(RtcStats stats) {
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onLeaveChannel(stats);
            }
        }

        @Override
        public void onConnectionInterrupted() {//连接中断回调
            super.onConnectionInterrupted();
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onConnectionInterrupted();
            }
        }

        @Override
        public void onConnectionLost() {//连接丢失回调
            super.onConnectionLost();
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onConnectionLost();
            }
        }


        @Override
        public void onLastmileQuality(int quality) {

        }

        @Override
        public void onError(int err) {
            super.onError(err);

            Log.i("TAG","检测错误onError："+err);
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onError(err);
            }
        }

        //加入频道的回调
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {


            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }
        //重新加入频道的回调
        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
          /*  Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onJoinChannelSuccess(channel, uid, elapsed);
            }*/
        }

        @Override
        public void onVideoStopped() {
            super.onVideoStopped();
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onVideoStopped();
            }
        }

        public void onWarning(int warn) {
          //  log.debug("onWarning " + warn);
            Log.d("TAG","检测警告onWarning"+warn);
        }

    };

}
