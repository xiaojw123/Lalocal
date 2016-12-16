package com.android.tedcoder.wkvideoplayer.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.tedcoder.wkvideoplayer.R;
import com.android.tedcoder.wkvideoplayer.util.VideoPlayCallbackImpl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by android on 2016/10/12.
 */
public class PlayBackPlayer extends RelativeLayout {
    private final int MSG_HIDE_CONTROLLER = 10;//隐藏控制器
    private final int MSG_UPDATE_PLAY_TIME = 11;//更新播放时间
    private PlayBackMediaController.PageType mCurrPageType = PlayBackMediaController.PageType.SHRINK;//当前是横屏还是竖屏

    private Context mContext;
    private CustomVideoView mVideoView;//播放器
    private PlayBackMediaController mMediaController;//控制器
    private Timer mUpdateTimer;
    private VideoPlayCallbackImpl mVideoPlayCallback;//回调函数
    private boolean isPlayerStatus;
    private View mProgressBarView;//加载中按钮
    // private View mCloseBtnView;//关闭按钮
    private Uri mUri;//网络视频路径

    //是否自动隐藏控制栏
    private boolean mAutoHideController = true;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_PLAY_TIME) {
                updatePlayTime();
                updatePlayProgress();
            } else if (msg.what == MSG_HIDE_CONTROLLER) {
                showOrHideController();
            }
            return false;
        }
    });


    private float touchLastX;
    private int position;
    private int touchStep = 1000*10;//快进的时间，1秒
    private int touchPosition = -111111111;
    private View.OnTouchListener mOnTouchVideoListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    showOrHideController();

                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    break;

            }
            return true;

        }
    };
    private TextView loadingTv;
    private LinearLayout videoLayout;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    // 播放器控制条的回调函数
    private PlayBackMediaController.MediaControlImpl mMediaControl = new PlayBackMediaController.MediaControlImpl() {

        @Override
        public void onPlayTurn() {
            if (mVideoView.isPlaying()) {
                pausePlay(true);
                isPlayerStatus=false;
            } else {
                goOnPlay();
                isPlayerStatus=true;
            }
            mVideoPlayCallback.onPlayStatus(isPlayerStatus);
        }

        @Override
        public void onPageTurn() {
            mVideoPlayCallback.onSwitchPageType();
        }

        @Override
        public void onProgressTurn(PlayBackMediaController.ProgressState state, int progress) {
            if (state.equals(MediaController.ProgressState.START)) {
                Log.i("TAG","查看进度条onProgressTurn1");
                mHandler.removeMessages(MSG_HIDE_CONTROLLER);
            } else if (state.equals(PlayBackMediaController.ProgressState.STOP)) {
                resetHideTimer();
            } else {
                int time = progress * mVideoView.getDuration() / 100;
                mVideoView.seekTo(time);
                Log.i("TAG","查看进度条onProgressTurn2");
                updatePlayTime();
            }
        }



        @Override
        public void onClickCollect(ImageView iv) {
            if(mVideoPlayCallback!=null){
                mVideoPlayCallback.onClickCollect(iv);
            }

        }

        @Override
        public void onClickBefore(ImageView imageView) {
            if(mVideoPlayCallback!=null){
                mVideoPlayCallback.onClickBefore(imageView);
            }

        }

        @Override
        public void onClickNext(ImageView imageView) {
            if(mVideoPlayCallback!=null) {
                mVideoPlayCallback.onClickNext(imageView);
            }
        }
    };

    // 当MediaPlayer准备好后触发该回调
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                           //  mProgressBarView.setVisibility(View.GONE);
                        mVideoPlayCallback.showLoadingPage(false);
                        setCloseButton(true);
                        return true;
                    }
                    return false;
                }
            });

        }

    };


    // 当MediaPlayer播放完成后触发该回调
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            stopUpdateTimer();
            stopHideTimer(true);
            mMediaController.playFinish(mVideoView.getDuration());
            mVideoPlayCallback.onPlayFinish();

        }
    };
    private int duration;

    //set回调方法，实现回调在本类中的实例化
    public void setVideoPlayCallback(VideoPlayCallbackImpl videoPlayCallback) {
        mVideoPlayCallback = videoPlayCallback;
    }

    /**
     * 设置页面状态（横屏or竖屏）
     */
    public void setPageType(PlayBackMediaController.PageType pageType) {

        mCurrPageType = pageType;
    }

    /***
     * 强制横屏模式
     */
    @SuppressWarnings("unused")
    public void forceLandscapeMode() {
        mMediaController.forceLandscapeMode();
    }

    /**
     * 暂停播放
     *
     * @param isShowController 是否显示控制条
     */
    public boolean pausePlay(boolean isShowController) {
        mVideoView.pause();
        mMediaController.setPlayState(PlayBackMediaController.PlayState.PAUSE);
        stopHideTimer(isShowController);
        return isShowController;
    }
    public int  pause(){
        if(mVideoView!=null){
            Log.i("TAF","播放器player pause");
            mVideoView.pause();
            return getSeek();
        }else {
            return 0;
        }


    }
    public  int  getSeek(){
        return mVideoView.getCurrentPosition();
    }

    /***
     * 继续播放
     */
    public void goOnPlay() {
        mVideoView.start();
        mMediaController.setPlayState(PlayBackMediaController.PlayState.PLAY);
        resetHideTimer();
        resetUpdateTimer();
    }

    /**
     * 关闭视频
     */
    public void close() {
        mMediaController.setPlayState(PlayBackMediaController.PlayState.PAUSE);
        stopHideTimer(true);
        stopUpdateTimer();
        mVideoView.pause();
        mVideoView.stopPlayback();
        mVideoView.setVisibility(GONE);
    }

    /**
     * 判断是否自动隐藏控制器
     */
    public boolean isAutoHideController() {
        return mAutoHideController;
    }

    public void setAutoHideController(boolean autoHideController) {
        mAutoHideController = autoHideController;
    }

    public PlayBackPlayer(Context context) {
        super(context);
        initView(context);
    }

    public PlayBackPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PlayBackPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化View
     *
     * @param context
     */

    private LinearLayout touchStatusView;
    private ImageView touchStatusImg;
    private TextView touchStatusTime;
    private String formatTotalTime;
    private void initView(Context context) {
        mContext = context;
        View inflate = View.inflate(context, R.layout.live_playback_player_layout, this);//TODO 假如只是将java和Layout结合起来，可以直接这么写。
        mVideoView = (CustomVideoView) findViewById(R.id.video_view);
        videoLayout = (LinearLayout) findViewById(R.id.video_view_layout);
        mMediaController = (PlayBackMediaController) findViewById(R.id.controller);
        touchStatusView = (LinearLayout) inflate.findViewById(R.id.touch_view);
        touchStatusImg = (ImageView) inflate.findViewById(R.id.touchStatusImg);
        touchStatusTime = (TextView) inflate.findViewById(R.id.touch_time);
       // mProgressBarView= findViewById(R.id.progressbar);
       /* loadingTv = (TextView) inflate.findViewById(R.id.loading_tv);
        loadingTv.setVisibility(View.GONE);*/
        mMediaController.setMediaControl(mMediaControl);
        mVideoView.setOnTouchListener(mOnTouchVideoListener);

        setCloseButton(false);
   //     showProgressView(false);

    }

    /**
     * 显示关闭视频的按钮
     *
     * @param isShow isShow
     */
    private void setCloseButton(boolean isShow) {
        ///  mCloseBtnView.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }
    public  void setBefore(float alpha,boolean clickAble){
        mMediaController.setBefore(alpha,clickAble);
    }
    public  void setNext(float alpha,boolean clickAble){
        mMediaController.setNext(alpha,clickAble);
    }
    public  void setCollect(boolean isCollect){
        mMediaController.setCollect(isCollect);
    }

    /**
     * 加载并开始播放视频
     */
    public void loadAndPlay(Uri uri, int seekTime) {
        mUri = uri;
      //  showProgressView(seekTime > 0);
        setCloseButton(true);
        mVideoView.setOnPreparedListener(mOnPreparedListener);
        mVideoView.setVideoURI(uri);
        mVideoView.setVisibility(VISIBLE);
        startPlayVideo(seekTime);
    }


    //视频缓冲
    int old_duration=-1999;
    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            int duration = mVideoView.getCurrentPosition();
            if (old_duration == duration && mVideoView.isPlaying()) {
            //    loadingTv.setVisibility(View.VISIBLE);
            } else {
              //  loadingTv.setVisibility(View.GONE);
            }
            old_duration = duration;
            handler.postDelayed(runnable, 500);
        }
    };
 //



    /**
     * 播放视频
     * should called after setVideoPath()
     */
    private void startPlayVideo(int seekTime) {
        if (null == mUpdateTimer) resetUpdateTimer();
        resetHideTimer();
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.start();
        if (seekTime > 0) {
            mVideoView.seekTo(seekTime);
        }
        mMediaController.setPlayState(PlayBackMediaController.PlayState.PLAY);
        handler.postDelayed(runnable, 500);
    }

    /**
     * 更新播放的进度时间
     */
    private void updatePlayTime() {
        int allTime = mVideoView.getDuration();
        int playTime = mVideoView.getCurrentPosition();
        duration = mVideoView.getDuration();
        int[] time = getMinuteAndSecond(duration);
        formatTotalTime = String.format("%02d:%02d", time[0], time[1]);
        mMediaController.setPlayProgressTxt(playTime, allTime);
        mVideoPlayCallback.getprogressDuration(playTime);

    }

    /**
     * 更新播放进度条
     */
    private void updatePlayProgress() {
        int allTime = mVideoView.getDuration();
        int playTime = mVideoView.getCurrentPosition();
        int loadProgress = mVideoView.getBufferPercentage();
        int progress = playTime * 100 / allTime;
        mMediaController.setProgressBar(progress, loadProgress);
    }

    /**
     * 显示loading圈
     *
     * @param isTransparentBg isTransparentBg
     */
   /* private void showProgressView(Boolean isTransparentBg) {
          mProgressBarView.setVisibility(VISIBLE);
        if (!isTransparentBg) {
               mProgressBarView.setBackgroundResource(android.R.color.black);
        } else {
             mProgressBarView.setBackgroundResource(android.R.color.transparent);
        }
    }*/

    /**
     * 控制器的显示与隐藏
     */
    private void showOrHideController() {
        if (mMediaController.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.anim_exit_from_bottom);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mMediaController.setVisibility(View.GONE);
                }
            });
            mMediaController.startAnimation(animation);
        } else {
            mMediaController.setVisibility(View.VISIBLE);
            mMediaController.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.anim_enter_from_bottom);
            mMediaController.startAnimation(animation);
            resetHideTimer();
        }
    }

    /**
     * 始终显示控制器
     */
    public void alwaysShowController() {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaController.setVisibility(View.VISIBLE);
    }

    private void resetHideTimer() {
        if (!isAutoHideController()) return;
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        int TIME_SHOW_CONTROLLER = 4000;
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, TIME_SHOW_CONTROLLER);
    }

    private void stopHideTimer(boolean isShowController) {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaController.clearAnimation();
        mMediaController.setVisibility(isShowController ? View.VISIBLE : View.GONE);
    }

    private void resetUpdateTimer() {
        mUpdateTimer = new Timer();
        int TIME_UPDATE_PLAY_TIME = 1000;
        mUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_UPDATE_PLAY_TIME);
            }
        }, 0, TIME_UPDATE_PLAY_TIME);
    }

    private void stopUpdateTimer() {
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }
    }



    private int[] getMinuteAndSecond(int mils) {
        mils /= 1000;
        int[] time = new int[2];
        time[0] = mils / 60;
        time[1] = mils % 60;
        return time;
    }


    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    }

   /* public interface VideoPlayCallbackImpl {
        void onCloseVideo();

        void onSwitchPageType();

        void onPlayFinish();

        void onPlayStatus(boolean isPlay);

        void onClickQuit();
        void onClickShare();
        void  onClickBefore(ImageView view);
        void onClickNext(ImageView view);
    }*/
}
