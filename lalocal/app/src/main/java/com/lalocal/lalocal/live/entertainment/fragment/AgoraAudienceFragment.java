package com.lalocal.lalocal.live.entertainment.fragment;

import android.view.SurfaceView;
import android.view.View;

import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.util.AppLog;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by ${WCJ} on 2017/1/6.
 */
public class AgoraAudienceFragment extends AgoraFragment {
    @Override
    protected void initUIandEvent() {
        event().addEventHandler(this);
        if (cname != null) {
            int cRole = Constants.CLIENT_ROLE_AUDIENCE;
            doConfigEngine(cRole);
            AppLog.d("TAG","config："+config().mUid+"   cname:"+cname);
            worker().joinChannel(cname, config().mUid);
        }
    }

    /**
     * @param cRole
     */
    private void doConfigEngine(int cRole) {
        int vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_480P;
        switch (LiveConstant.LIVE_DEFINITION) {
            case 1:
                vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_720P;
                AppLog.i("TAG", "用戶端視頻分辨率為720p");
                break;
            case 2:
                vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_480P;
                break;
            case 3:
                break;
        }
        worker().configEngine(cRole, vProfile);
    }

    @Override
    protected void deInitUIandEvent() {
        doLeaveChannel();
        event().removeEventHandler(this);
    }

    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
        if (isBroadcaster()) {
            worker().preview(false, null, 0);
        }
    }

    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_AUDIENCE;
    }

    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }

    int uid;

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        this.uid = uid;
        AppLog.d("TAG","接收远端第一针视频");
        doRenderRemoteUi(uid);
    }

    private void doRenderRemoteUi(final int uid) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SurfaceView surfaceV = RtcEngine.CreateRendererView(getActivity());
                int childCount = playerLayout.getChildCount();
                if (childCount > 0) {
                    playerLayout.removeAllViews();
                }
                playerLayout.addView(surfaceV);
                //监听视频布局变化，防止软键盘挤压视频窗口
                surfaceV.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (playerLayout != null && playerLayout.getChildAt(0) != null) {
                            if (bottom > oldBottom) {
                                playerLayout.getChildAt(0).layout(left, top, right, bottom);
                            } else {
                                playerLayout.getChildAt(0).layout(oldLeft, oldTop, oldRight, oldBottom);
                            }
                        }
                    }
                });
                surfaceV.setZOrderOnTop(true);
                surfaceV.setZOrderMediaOverlay(true);
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));//设置远端视频属性
                rtcEngine().setRemoteVideoStreamType(uid, 0);
                if (agoraCallBackListener != null) {
                    agoraCallBackListener.onFirstRemoteVideoDecoded();
                }

            }
        });
    }


    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        AudienceActivity.masterComeBack = false;
        AppLog.d("TAG","用户端：onJoinChannelSuccess");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rtcEngine().setEnableSpeakerphone(true);
                rtcEngine().setPreferHeadset(true);
            }
        });
        if(agoraCallBackListener!=null){
            agoraCallBackListener.onJoinChannelSuccess();
        }

    }


    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onUserJoined(int uid, int elapsed) {

    }

    @Override
    public void onConnectionInterrupted() {
        AppLog.i("TAG", "視頻連接中斷连接中断回调");
    }

    @Override
    public void onConnectionLost() {
        AppLog.i("TAG", "視頻連接中斷连接丟失");
    }

    @Override
    public void onError(int err) {
        AppLog.i("TAG", "用户端视频播放错误码:" + err);
    }

    @Override
    public void onVideoStopped() {

    }

    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {

    }

    @Override
    public void onUserEnableVideo(int uid, boolean enabled) {

    }

    @Override
    public void onLastmileQuality(int quality) {
    }

    AgoraAudienceCallBackListener agoraCallBackListener;

    public void setAgoraAudienceCallBackListener(AgoraAudienceCallBackListener agoraCallBackListener) {
        this.agoraCallBackListener = agoraCallBackListener;
    }

    public interface AgoraAudienceCallBackListener {
        void onFirstRemoteVideoDecoded();
        void onJoinChannelSuccess();

    }

}
