package com.lalocal.lalocal.live.entertainment.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.BaseFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.util.AppLog;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${WCJ} on 2017/1/9.
 */
public class IJKPlayerFragment extends BaseFragment {
    @BindView(R.id.plv_videoview)
    PLVideoView mVideoView;
    @BindView(R.id.player_layout)
    RelativeLayout playerLayout;
    private String pullUrl;
    private boolean mIsActivityPaused = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.ijkplayer_layout, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }


    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pullUrl = bundle.getString(KeyParams.PULLURL);
        }
    }

    private void initView() {
        setOptions(AVOptions.MEDIA_CODEC_AUTO);
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnPreparedListener(mOnPreparedListener);
        mVideoView.setOnSeekCompleteListener(mOnSeekCompleteListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setVideoPath(pullUrl);
        AppLog.d("TAG","用户端URL地址:"+pullUrl);//rtmp://pili-live-rtmp.lalocal.cn/lalocal/11812-dev-16-732
        //防止软键盘挤压视频
        mVideoView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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
    }

    private void setOptions(int codecType) {
            AVOptions options = new AVOptions();
            options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 30 * 1000);
            options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
            options.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
           // options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
            options.setInteger(AVOptions.KEY_MEDIACODEC, codecType);
            options.setInteger(AVOptions.KEY_START_ON_PREPARED, 1);
            mVideoView.setAVOptions(options);
    }

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer plMediaPlayer) {
            if(ijkplayerCallBackListener!=null){
                ijkplayerCallBackListener.onPreparedListener();
            }
        }
    };

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
            AppLog.i("TAG", "onInfo: " + what + ", " + extra);
            return false;
        }
    };

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
            boolean isNeedReconnect = false;
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    AppLog.i("TAG","用戶端播放器 ERROR_CODE_INVALID_URI");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    AppLog.i("TAG","用戶端播放器 ERROR_CODE_404_NOT_FOUND");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    AppLog.i("TAG","用戶端播放器 ERROR_CODE_CONNECTION_REFUSED");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    AppLog.i("TAG","用戶端播放器 ERROR_CODE_CONNECTION_TIMEOUT");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    AppLog.i("TAG","用戶端播放器 ERROR_CODE_EMPTY_PLAYLIST");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    AppLog.i("TAG","用戶端播放器 ERROR_CODE_STREAM_DISCONNECTED");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    AppLog.i("TAG","用戶端播放器 ERROR_CODE_IO_ERROR");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    AppLog.i("TAG","用戶端播放器 ERROR_CODE_UNAUTHORIZED");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    AppLog.i("TAG","用戶端播放器 ERROR_CODE_PREPARE_TIMEOUT");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    AppLog.i("TAG","用戶端播放器ERROR_CODE_READ_FRAME_TIMEOUT");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_HW_DECODE_FAILURE:
                    setOptions(AVOptions.MEDIA_CODEC_SW_DECODE);
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                  AppLog.i("TAG","位置錯誤，播放器");
                    break;
            }

            if (isNeedReconnect) {
                sendReconnectMessage();
            } else {
                AppLog.d("TAG","分开快点恢复健康和2");
                getActivity().finish();
            }
            return true;
        }
    };

    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            AppLog.i("TAG", "Play Completed !");

        }
    };

    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int precent) {
            AppLog.i("TAG",  "onBufferingUpdate: " + precent);
        }
    };

    private PLMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new PLMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
            AppLog.i("TAG",  "onSeekComplete !");
        }
    };

    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
            AppLog.i("TAG",  "onVideoSizeChanged: " + width + "," + height);
        }
    };



    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING) {
                return;
            }
            AppLog.d("TAG","看积分卡接口接口即可");
            mVideoView.stopPlayback();
            mVideoView.setVideoPath(pullUrl);
            mVideoView.start();
        }
    };

    private static final int MESSAGE_ID_RECONNECTING = 0x01;
    private void sendReconnectMessage() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 500);
    }

    @Override
    public void onResume() {
        super.onResume();
        mVideoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsActivityPaused = true;
        mVideoView.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mVideoView.stopPlayback();
    }

    IJKPlayerCallBackListener ijkplayerCallBackListener;
    public void setAgoraAudienceCallBackListener(IJKPlayerCallBackListener ijkplayerCallBackListener) {
        this.ijkplayerCallBackListener = ijkplayerCallBackListener;
    }

    public interface IJKPlayerCallBackListener {
        void onPreparedListener();
    }

}
