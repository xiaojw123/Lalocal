package com.lalocal.lalocal.live.entertainment.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.LogFileUtils;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by ${WCJ} on 2017/1/8.
 */
public class AgoraLiveFragment extends AgoraFragment {
    private CustomChatDialog customChatDialog;
    private String longTime;

    @Override
    protected void initUIandEvent() {
        event().addEventHandler(this);
        initTime();
        startLive();
    }

    private void initTime() {
        Bundle bundle = getArguments();
        if(bundle!=null){
            longTime = bundle.getString("longTime");
        }
    }

    /**
     * 开启直播
     */
    private void startLive() {
        int cRole = Constants.CLIENT_ROLE_BROADCASTER;
        doConfigEngine(cRole);
        if (isBroadcaster(cRole)) {
            final SurfaceView surfaceView = RtcEngine.CreateRendererView(getActivity());
            int childCount = playerLayout.getChildCount();
            if (childCount > 0) {
                playerLayout.removeAllViews();
            }
            playerLayout.addView(surfaceView);
            surfaceView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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

            rtcEngine().setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));//VideoCanvas:本地代码显示属性
            surfaceView.setZOrderOnTop(true);
            surfaceView.setZOrderMediaOverlay(true);
            rtcEngine().setLogFile(Environment.getExternalStorageDirectory() + "/" + LogFileUtils.fileAgoraPath + longTime);
            worker().preview(true, surfaceView, UserHelper.getUserId(getActivity()));
        }
        worker().joinChannel(cname, UserHelper.getUserId(getActivity()));
    }
    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_BROADCASTER;
    }

    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }
    /**
     * 推流分辨率设置
     * @param cRole
     */
    private void doConfigEngine(int cRole) {
        int vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_720P;
        switch (LiveConstant.LIVE_DEFINITION) {
            case 1:
                vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_720P;
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


    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
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
        if(agoraCallBackListener!=null){
            agoraCallBackListener.onConnectionInterrupted();
        }
    }


    @Override
    public void onConnectionLost() {

    }

    @Override
    public void onError(final int err) {

       getActivity(). runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (err == 1003) {
                    if (onDestory) {
                        if(customChatDialog==null){
                            customChatDialog =  new CustomChatDialog(getActivity());
                        }
                        customChatDialog.setContent(getString(R.string.live_camera_start_failure));
                        customChatDialog.setCancelable(false);
                        customChatDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                getActivity().finish();
                            }
                        });

                        customChatDialog.show();
                    }


                } else if (err == 1018) {
                    CommonUtil.RESULT_DIALOG = 0;
                    if (LiveConstant.isUnDestory) {
                        if(customChatDialog==null){
                            customChatDialog = new CustomChatDialog(getActivity());
                        }
                        customChatDialog.setContent(getString(R.string.live_frequency_start_failure));
                        customChatDialog.setCancelable(false);
                        customChatDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                getActivity().finish();
                            }
                        });
                        customChatDialog.show();
                    }
                }
            }
        });

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

    boolean onDestory=false;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onDestory=true;
    }

    @Override
    public void onStart() {
        super.onStart();
        onDestory=false;
    }

    AgoraCallBackListener agoraCallBackListener;

    public void setAgoraCallBackListener(AgoraCallBackListener agoraCallBackListener) {
        this.agoraCallBackListener = agoraCallBackListener;
    }

    public interface AgoraCallBackListener {
        void onConnectionInterrupted();
        void onJoinChannelSuccess();
    }



}
