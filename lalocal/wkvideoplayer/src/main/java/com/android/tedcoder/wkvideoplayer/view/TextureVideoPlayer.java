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
 * Created by android on 2016/10/17.
 */
public class TextureVideoPlayer extends RelativeLayout {

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
    private Timer mUpdateTimer;
    private LinearLayout touchStatusView;
    private ImageView touchStatusImg;
    private TextView touchStatusTime;

    public void setAutoHideController(boolean autoHideController) {
        mAutoHideController = autoHideController;
    }
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
    private void resetHideTimer() {
        if (!isAutoHideController()) return;
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        int TIME_SHOW_CONTROLLER = 4000;
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, TIME_SHOW_CONTROLLER);
    }
    private boolean mAutoHideController = true;
    public boolean isAutoHideController() {
        return mAutoHideController;
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
    private int duration;
    private String formatTotalTime;
    private void updatePlayTime() {
        int allTime = mVideoView.getDuration();
        int playTime = mVideoView.getCurrentPosition();
        duration = mVideoView.getDuration();
        int[] time = getMinuteAndSecond(duration);
        formatTotalTime = String.format("%02d:%02d", time[0], time[1]);
        mMediaController.setPlayProgressTxt(playTime, allTime);
        mVideoPlayCallback.getprogressDuration(playTime);
    }
    private void updatePlayProgress() {
        int allTime = mVideoView.getDuration();
        int playTime = mVideoView.getCurrentPosition();
        int loadProgress = mVideoView.getBufferPercentage();
        int progress = playTime * 100 / allTime;

        mMediaController.setProgressBar(progress, loadProgress);
    }

    private int[] getMinuteAndSecond(int mils) {
        mils /= 1000;
        int[] time = new int[2];
        time[0] = mils / 60;
        time[1] = mils % 60;
        return time;
    }



    public TextureVideoPlayer(Context context) {
        super(context);
        initView(context);
    }



    public TextureVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TextureVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private Context mContext;
    private TextureVideoView mVideoView;//播放器
  //  private View mProgressBarView;//加载中按钮
    private void initView(Context context) {
        mContext = context;
        View inflate = View.inflate(context, R.layout.texture_view_layout, this);//TODO 假如只是将java和Layout结合起来，可以直接这么写。
        mVideoView = (TextureVideoView) findViewById(R.id.texture_videoview);
        LinearLayout videoLayout = (LinearLayout) findViewById(R.id.video_view_layout);
        mMediaController = (PlayBackMediaController) findViewById(R.id.controller);
        touchStatusView = (LinearLayout) inflate.findViewById(R.id.touch_view);
        touchStatusImg = (ImageView) inflate.findViewById(R.id.touchStatusImg);
        touchStatusTime = (TextView) inflate.findViewById(R.id.touch_time);
      //  mProgressBarView= findViewById(R.id.progressbar);

        mMediaController.setMediaControl(mMediaControl);
        mVideoView.setOnTouchListener(mOnTouchVideoListener);
      //  showProgressView(false);
    }


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
                /*    if (!mVideoView.isPlaying()){
                        return false;
                    }
                    float downX =  event.getRawX();
                    touchLastX = downX;
                    position = mVideoView.getCurrentPosition();
                    touchPosition=position;
                    Log.i("TAg", "ACTION_DOWN: "+touchPosition+"position:"+position);*/
                    break;
                case MotionEvent.ACTION_MOVE:
                 /*   Log.i("TAg", "ACTION_MOVE: "+touchPosition);
                    float currentX =  event.getRawX();
                    float currentY = event.getRawY();
                    float deltaX = currentX - touchLastX;
                    float deltaXAbs  =  Math.abs(deltaX);
                    if (!mVideoView.isPlaying()){
                        return false;
                    }
                    if (deltaXAbs>10){
                        if (touchStatusView.getVisibility()!= View.VISIBLE){
                            touchStatusView.setVisibility(View.VISIBLE);
                        }
                        touchLastX = currentX;
                        if (deltaX > 1) {
                            position += touchStep;
                            if (position > duration) {
                                position = duration;
                            }
                            touchPosition = position;
                            touchStatusImg.setImageResource(R.drawable.ic_fast_forward_white_24dp);
                            int[] time = getMinuteAndSecond(position);
                            touchStatusTime.setText(String.format("%02d:%02d/%s", time[0], time[1],formatTotalTime));
                        } else if (deltaX < -1) {
                            position -= touchStep;
                            if (position < 0) {
                                position = 0;
                            }
                            touchPosition = position;
                            touchStatusImg.setImageResource(R.drawable.ic_fast_rewind_white_24dp);
                            int[] time = getMinuteAndSecond(position);
                            touchStatusTime.setText(String.format("%02d:%02d/%s", time[0], time[1],formatTotalTime));
                            mVideoView.seekTo(position);
                        }
                    }*/
                    break;
                case MotionEvent.ACTION_UP:
                   /* if (!mVideoView.isPlaying()){
                        return false;
                    }
                    Log.i("TAg", "ACTION_UP: "+touchPosition);
                    touchStatusView.setVisibility(View.GONE);
                    if (touchPosition!=-1){
                        mVideoView.seekTo(touchPosition);
                        touchPosition = -1;
                        Log.i("TAg", "ACTION_UP: "+touchPosition+"哈哈哈哈哈哈哈哈");
                  *//*  if (videoControllerShow){
                        return true;
                    }*//*
                    }*/
                    break;
             /*   case MotionEvent.ACTION_CANCEL:
                    if (!mVideoView.isPlaying()){
                        return false;
                    }
                    Log.i("TAg", "ACTION_UP: "+touchPosition);
                    touchStatusView.setVisibility(View.GONE);
                    if (touchPosition!=-1){
                        mVideoView.seekTo(touchPosition);
                        touchPosition = -1;
                  *//*  if (videoControllerShow){
                        return true;
                    }*//*
                    }
                    break;*/
            }
            return true;


            //  return mCurrPageType == MediaController.PageType.EXPAND;
        }
    };






    private boolean isPlayerStatus;
    private VideoPlayCallbackImpl mVideoPlayCallback;//回调函数

    public  void setBefore(float alpha,boolean clickAble){
        mMediaController.setBefore(alpha,clickAble);
    }
    public  void setCollect(boolean isCollect){
        mMediaController.setCollect(isCollect);
    }
    public  void setNext(float alpha,boolean clickAble){
        mMediaController.setNext(alpha,clickAble);
    }
    //set回调方法，实现回调在本类中的实例化
    public void setVideoPlayCallback(VideoPlayCallbackImpl videoPlayCallback) {
        mVideoPlayCallback = videoPlayCallback;
    }

    public void close() {
        mMediaController.setPlayState(PlayBackMediaController.PlayState.PAUSE);
        stopHideTimer(true);
        stopUpdateTimer();
        mVideoView.pause();
        mVideoView.stopPlayback();
        mVideoView.setVisibility(GONE);
    }
    public int  pause(){
        if(mVideoView!=null){
            Log.i("TAF","播放器player pause");
            mVideoView.pause();
        return getSeek();
        }else {
            Log.i("TAF","播放器player pause22222");
            return 0;
        }


    }

    public  int  getSeek(){
       return mVideoView.getCurrentPosition();
    }
    public void onResume(){
        if(mVideoView!=null){
            Log.i("TAF","播放器player onResume");
            mVideoView.onResume();

        }
        Log.i("TAF","播放器player2  onResume");
    }

    private PlayBackMediaController.MediaControlImpl mMediaControl = new PlayBackMediaController.MediaControlImpl() {

        @Override
        public void onPlayTurn() {
            if(mVideoView.isPlaying()){
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
                Log.i("TAF","查看进度条onProgressTurn1");
                mHandler.removeMessages(MSG_HIDE_CONTROLLER);
            } else if (state.equals(PlayBackMediaController.ProgressState.STOP)) {
                resetHideTimer();
            } else {
                int time = progress * mVideoView.getDuration() / 100;
                mVideoView.seekTo(time);
                Log.i("TAF","查看进度条onProgressTurn2");
                updatePlayTime();
            }
        }


        @Override
        public void onClickCollect(ImageView iv) {
            mVideoPlayCallback.onClickCollect(iv);
        }

        @Override
        public void onClickBefore(ImageView view) {
            mVideoPlayCallback.onClickBefore(view);
        }

        @Override
        public void onClickNext(ImageView view) {
            mVideoPlayCallback.onClickNext(view);
        }


    };
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                           //  mProgressBarView.setVisibility(View.GONE);
                        if(mVideoPlayCallback!=null){
                            mVideoPlayCallback.showLoadingPage(false);
                        }

                        return true;
                    }
                    return false;
                }
            });

        }

    };
    private Uri mUri;//网络视频路径
    public void loadAndPlay(Uri uri, int seekTime) {
        mUri = uri;
        mVideoView.setOnPreparedListener(mOnPreparedListener);
      //  showProgressView(seekTime > 0);
        mVideoView.setVideoURI(uri);
        mVideoView.setVisibility(VISIBLE);
        startPlayVideo(seekTime);
    }

    public  void setRotation(float rotation){
        mVideoView.setRotation(rotation);
    }
    private PlayBackMediaController mMediaController;//控制器
    private void startPlayVideo(int seekTime) {
        if (null == mUpdateTimer) resetUpdateTimer();
        resetHideTimer();
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.start();
        if (seekTime > 0) {
            mVideoView.seekTo(seekTime);
        }
        mMediaController.setPlayState(PlayBackMediaController.PlayState.PLAY);

    }
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            stopUpdateTimer();
            stopHideTimer(true);
            mMediaController.playFinish(mVideoView.getDuration());
            mVideoPlayCallback.onPlayFinish();

        }
    };

    private void stopUpdateTimer() {
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }
    }

    public void goOnPlay() {
        mVideoView.start();
        mMediaController.setPlayState(PlayBackMediaController.PlayState.PLAY);
        resetHideTimer();
        resetUpdateTimer();
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
    private final int MSG_HIDE_CONTROLLER = 10;//隐藏控制器
    private final int MSG_UPDATE_PLAY_TIME = 11;//更新播放时间
    private PlayBackMediaController.PageType mCurrPageType = PlayBackMediaController.PageType.SHRINK;//当前是横屏还是竖屏
    private boolean pausePlay(boolean isShowController) {
        mVideoView.pause();
        mMediaController.setPlayState(PlayBackMediaController.PlayState.PAUSE);
        stopHideTimer(isShowController);
        return isShowController;
    }

    private void stopHideTimer(boolean isShowController) {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaController.clearAnimation();
        mMediaController.setVisibility(isShowController ? View.VISIBLE : View.GONE);
    }


}
