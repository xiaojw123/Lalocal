package com.lalocal.lalocal.live.thirdparty.video;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.lalocal.lalocal.util.AppLog;
import com.netease.neliveplayer.NELivePlayer;


/**
 * Created by huangjun on 2016/3/28.
 */
public class VideoPlayer {
    public interface VideoPlayerProxy {
        boolean isDisconnected();
        void onError();
        void onCompletion();
        void onPrepared();
    }

    private final String TAG = "NEVideoPlayer";

    private final int VIDEO_ERROR_REOPEN_TIMEOUT = 10 * 1000;
    private final int VIDEO_COMPLETED_REOPEN_TIMEOUT = 30 * 1000;

    private VideoPlayerProxy proxy;
    private NEVideoView videoView;
    private Handler handler;

    private String videoPath; // 拉流地址
    private boolean pauseInBackgroud = true;
    private boolean isHardWare = false;

    public VideoPlayer(Context context, NEVideoView videoView, NEVideoController mMediaController, String videoPath,
                       int bufferStrategy, VideoPlayerProxy proxy, int videoScaleMode,String mediaType) {
        this.handler = new Handler(context.getMainLooper());
        this.videoView = videoView;
        this.videoPath = videoPath;
        this.proxy = proxy;

        videoView.setBufferStrategy(bufferStrategy); //直播低延时/抗抖动
        videoView.setMediaType(mediaType);
        videoView.setMediaController(mMediaController, videoScaleMode);
        videoView.setHardwareDecoder(isHardWare);// 硬件解码还是软件解码
        videoView.setPauseInBackground(pauseInBackgroud);
        videoView.setOnErrorListener(onVideoErrorListener);
        videoView.setOnPreparedListener(onVideoPreparedListener);
        videoView.setOnCompletionListener(onCompletionListener);
        videoView.setVisibility(View.VISIBLE);
    }

    public void onActivityResume() {
        if (pauseInBackgroud && videoView != null && !videoView.isPaused()) {
            videoView.start(); //锁屏打开后恢复播放
        }
    }

    public void onActivityPause() {
        if (pauseInBackgroud && videoView != null) {
            videoView.pause(); //锁屏时暂停
        }
    }

    public void resetVideo() {
        clearReopenVideoTask();

        if (videoView != null) {
            videoView.release_resource();
        }
    }

    // start
    public void openVideo() {
        clearReopenVideoTask();
        videoView.requestFocus();
        videoView.setVideoPath(videoPath).start();

        AppLog.i("TAG", "播放器open video, path=" + videoPath);
    }

    // onPrepared
    private NELivePlayer.OnPreparedListener onVideoPreparedListener = new NELivePlayer.OnPreparedListener() {
        @Override
        public void onPrepared(NELivePlayer neLivePlayer) {
            AppLog.i("TAG", "播放器video on prepared");

            proxy.onPrepared(); // 视频已经准备好了
        }
    };

    // onError
    private NELivePlayer.OnErrorListener onVideoErrorListener = new NELivePlayer.OnErrorListener() {
        @Override
        public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
            AppLog.i("TAG", "播放器video on error, post delay reopen task, delay " + VIDEO_ERROR_REOPEN_TIMEOUT);

            proxy.onError();
            handler.postDelayed(reopenVideoRunnable, VIDEO_ERROR_REOPEN_TIMEOUT); // 开启reopen定时器
            return true;
        }
    };
    // onCompletion
    private NELivePlayer.OnCompletionListener onCompletionListener = new NELivePlayer.OnCompletionListener() {
        @Override
        public void onCompletion(NELivePlayer neLivePlayer) {
            AppLog.i("TAG", "播放器video on completed, post delay reopen task, delay " + VIDEO_COMPLETED_REOPEN_TIMEOUT);
            proxy.onCompletion();
            handler.postDelayed(reopenVideoRunnable, VIDEO_COMPLETED_REOPEN_TIMEOUT); // 开启reopen定时器
        }
    };

    private Runnable reopenVideoRunnable = new Runnable() {
        @Override
        public void run() {
            if (proxy.isDisconnected()) {
                AppLog.i("TAG", "播放器reopen video task run but disconnected");
                return;
            }

            AppLog.i("TAG", "播放器reopen video task run");
            openVideo();
        }
    };

    private void clearReopenVideoTask() {
        handler.removeCallbacks(reopenVideoRunnable);
    }
}
