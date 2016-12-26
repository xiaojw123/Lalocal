package openlive.agora.io.pldroidplayer.utils.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pili.pldroid.player.IMediaController;

import java.util.Locale;

import openlive.agora.io.pldroidplayer.R;

/**
 * You can write a custom MediaController instead of this class
 * A MediaController widget must implement all the interface defined by com.pili.pldroid.player.IMediaController
 */
public class MediaController extends FrameLayout implements IMediaController,View.OnClickListener{

    private static final String TAG = "PLMediaController";
    private IMediaController.MediaPlayerControl mPlayer;
    private Context mContext;
    private PopupWindow mWindow;
    private int mAnimStyle;
    private View mAnchor;
    private View mRoot;
    private SeekBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private long mDuration;
    private boolean mShowing;
    private boolean mDragging;
    private boolean mInstantSeeking = true;
    private static int sDefaultTimeout = 300000000;
    private static final int SEEK_TO_POST_DELAY_MILLIS = 200;

    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private boolean mFromXml = false;
    private ImageView mPauseButton;
    private ImageView mFfwdButton;//快进
    private ImageView mRewButton;//快退
    private ImageView mNextButton;
    private ImageView mPrevButton;

    private MediaControlImpl mMediaControl;
    private boolean mUseFastForward;
    private AudioManager mAM;
    private Runnable mLastSeekBarRunnable;
    private boolean mDisableProgress = false;

    private ImageView collect;
    private TextView thinkCollect;

    public MediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = this;
        mFromXml = true;
        initController(context);

    }

    public MediaController(Context context) {
        super(context);
        if (!mFromXml && initController(context))
            initFloatingWindow();

    }

    public MediaController(Context context, boolean useFastForward, boolean disableProgressBar) {
        this(context);
        mUseFastForward = useFastForward;
        mDisableProgress = disableProgressBar;
    }

    public MediaController(Context context, boolean useFastForward) {
        this(context);
        mUseFastForward = useFastForward;
    }



    private boolean initController(Context context) {
        mUseFastForward = true;
        mContext = context.getApplicationContext();
        mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        return true;
    }
    @Override
    public void onFinishInflate() {
        if (mRoot != null)
            initControllerView(mRoot);
        super.onFinishInflate();
    }

    private void initFloatingWindow() {
        mWindow = new PopupWindow(mContext);
        mWindow.setFocusable(false);
        mWindow.setBackgroundDrawable(null);
     //   mWindow.setOutsideTouchable(true);
        mAnimStyle = android.R.style.Animation;
    }

    /**
     * Create the view that holds the widgets that control playback. Derived
     * classes can override this to create their own.
     *
     * @return The controller view.
     */
    protected View makeControllerView() {
        return ((LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.media_controller, this);
    }

    public void setMediaControl(MediaControlImpl mediaControl) {
        mMediaControl = mediaControl;
    }

    private void initControllerView(View v) {

        mPrevButton = (ImageView) v.findViewById(R.id.prev);
        if (mPrevButton != null) {
            mPrevButton.setOnClickListener(this);

        }
        mNextButton = (ImageView) v.findViewById(R.id.next);
        if (mNextButton != null) {
            mNextButton.setOnClickListener(this);
        }
        mPauseButton = (ImageView) v.findViewById(R.id.ic_media_pause);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(this);
        }

        mProgress = (SeekBar) v.findViewById(R.id.media_controller_progress);
        if (mProgress != null) {
            mProgress.setOnSeekBarChangeListener(mSeekListener);
            mProgress.setThumbOffset(1);
            mProgress.setMax(1000);
            mProgress.setEnabled(true);
        }

        mEndTime = (TextView) v.findViewById(R.id.time);
        mCurrentTime = (TextView) v.findViewById(R.id.time_current);

        collect = (ImageView) v.findViewById(R.id.live_telecast_collect);
        if(collect!=null){
            collect.setOnClickListener(this);
        }
        thinkCollect = (TextView) v.findViewById(R.id.think_collect);

        TextView inputText= (TextView) v.findViewById(R.id.input_text);
        inputText.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ic_media_pause) {
            doPauseResume();
            show(sDefaultTimeout);

        } else if (i == R.id.prev) {
            Log.d("TAG", "上一集");
            mMediaControl.onClickBefore((ImageView) view);

        } else if (i == R.id.next) {
            Log.d("TAG", "下一集");
            mMediaControl.onClickNext((ImageView) view);

        } else if (i == R.id.live_telecast_collect) {
            mMediaControl.onClickCollect(collect);

        }else if(i==R.id.input_text){
            mMediaControl.inputPrivate();
        }
    }
    //是否收藏
    public void setCollect(boolean isCollect){
        if(isCollect){
            collect.setImageResource(R.drawable.collect_light_sel);
            thinkCollect.setVisibility(VISIBLE);
        }else {
            collect.setImageResource(R.drawable.collect_light_unsel);
            thinkCollect.setVisibility(GONE);
        }
    }

    public  void setBefore(float alpha,boolean clickAble){
        mPrevButton.setAlpha(alpha);
        mPrevButton.setClickable(clickAble);
    }
    public void setNext(float alpha,boolean clickAble){
        mNextButton.setAlpha(alpha);
        mNextButton.setClickable(clickAble);
    }

    public  void setPlayerCountStatus(boolean show){
        if(show){
            mPrevButton.setVisibility(VISIBLE);
            mNextButton.setVisibility(VISIBLE);
        }else{
            mPrevButton.setVisibility(GONE);
            mNextButton.setVisibility(GONE);
        }
    }


    public void setSeekTo(long mDuration){
        mPlayer.seekTo(mDuration);
    }


    /**
     * Control the action when the seekbar dragged by user
     *
     * @param seekWhenDragging
     * True the media will seek periodically
     */
    public void setInstantSeeking(boolean seekWhenDragging) {
        mInstantSeeking = seekWhenDragging;
    }

    private void disableUnsupportedButtons() {
        try {
            if (mPauseButton != null && !mPlayer.canPause())
                mPauseButton.setEnabled(false);
        } catch (IncompatibleClassChangeError ex) {
        }
    }

    /**
     * <p>
     * Change the animation style resource for this controller.
     * </p>
     *
     * <p>
     * If the controller is showing, calling this method will take effect only
     * the next time the controller is shown.
     * </p>
     *
     * @param animationStyle
     * animation style to use when the controller appears and disappears.
     * Set to -1 for the default animation, 0 for no animation,
     * or a resource identifier for an explicit animation.
     *
     */
    public void setAnimationStyle(int animationStyle) {
        mAnimStyle = animationStyle;
    }



    public interface OnShownListener {
        public void onShown();
    }

    private OnShownListener mShownListener;

    public void setOnShownListener(OnShownListener l) {
        mShownListener = l;
    }

    public interface OnHiddenListener {
        public void onHidden();
    }

    private OnHiddenListener mHiddenListener;

    public void setOnHiddenListener(OnHiddenListener l) {
        mHiddenListener = l;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        updatePausePlay();
                    }
                    break;
            }
        }
    };

    private long setProgress() {
        if (mPlayer == null || mDragging)
            return 0;

        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        mDuration = duration;

        if (mEndTime != null)
            mEndTime.setText("/"+generateTime(mDuration));
        if (mCurrentTime != null)
            mCurrentTime.setText(generateTime(position));
        mMediaControl.getprogressDuration(position);
        return position;
    }

    private static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(sDefaultTimeout);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0
                && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show(sDefaultTimeout);
            if (mPauseButton != null)
                mPauseButton.requestFocus();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
            return true;
        } else {
            show(sDefaultTimeout);
        }
        return super.dispatchKeyEvent(event);
    }





    private void updatePausePlay() {
        if (mRoot == null || mPauseButton == null)
            return;

        if (mPlayer.isPlaying())
            mPauseButton.setImageResource(R.drawable.livingreplay_unplay);
        else
            mPauseButton.setImageResource(R.drawable.livingreplay_play);
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying())
            mPlayer.pause();
        else
            mPlayer.start();
        updatePausePlay();
    }

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {

        public void onStartTrackingTouch(SeekBar bar) {
            mDragging = true;
            show(3600000);
            mHandler.removeMessages(SHOW_PROGRESS);
            if (mInstantSeeking)
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser)
                return;

            final long newposition = (long) (mDuration * progress) / 1000;
            String time = generateTime(newposition);
            Log.d("TAG","时间:"+newposition);
            if (mInstantSeeking) {
                mHandler.removeCallbacks(mLastSeekBarRunnable);
                mLastSeekBarRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG","反倒是客服很快收到货福克斯的福克斯的家伙");
                        mPlayer.seekTo(newposition);
                    }
                };
                mHandler.postDelayed(mLastSeekBarRunnable, SEEK_TO_POST_DELAY_MILLIS);
            }
            if (mCurrentTime != null)
                mCurrentTime.setText(time);
        }

        public void onStopTrackingTouch(SeekBar bar) {
            if (!mInstantSeeking)
                mPlayer.seekTo(mDuration * bar.getProgress() / 1000);

            show(sDefaultTimeout);
            mHandler.removeMessages(SHOW_PROGRESS);
            mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mDragging = false;
            mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
        }
    };

    private OnClickListener mRewListener = new OnClickListener() {
        public void onClick(View v) {
            long pos = mPlayer.getCurrentPosition();
            pos -= 5000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();
            show(sDefaultTimeout);
        }
    };

    private OnClickListener mFfwdListener = new OnClickListener() {
        public void onClick(View v) {
            long pos = mPlayer.getCurrentPosition();
            pos += 15000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };



    /**
     * Set the view that acts as the anchor for the control view.
     *
     * - This can for example be a VideoView, or your Activity's main view.
     * - AudioPlayer has no anchor view, so the view parameter will be null.
     *
     * @param view
     * The view to which to anchor the controller when it is visible.
     */
    @Override
    public void setAnchorView(View view) {
        mAnchor = view;
        if (mAnchor == null) {
            sDefaultTimeout = 0; // show forever
        }
        if (!mFromXml) {
            removeAllViews();
            mRoot = makeControllerView();
            mWindow.setContentView(mRoot);
            mWindow.setWidth(LayoutParams.MATCH_PARENT);
            mWindow.setHeight(LayoutParams.WRAP_CONTENT);
        }
        initControllerView(mRoot);
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    @Override
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     *
     * @param timeout
     * The timeout in milliseconds. Use 0 to show the controller until hide() is called.
     */
    @Override
    public void show(int timeout) {
        if (!mShowing) {
            if (mAnchor != null && mAnchor.getWindowToken() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
            if (mPauseButton != null)
                mPauseButton.requestFocus();
            disableUnsupportedButtons();

            if (mFromXml) {
                setVisibility(View.VISIBLE);
            } else {
                try {
                    int[] location = new int[2];

                    if (mAnchor != null) {
                        mAnchor.getLocationOnScreen(location);
                        Rect anchorRect = new Rect(location[0], location[1],
                                location[0] + mAnchor.getWidth(), location[1]
                                + mAnchor.getHeight());

                        mWindow.setAnimationStyle(mAnimStyle);
                        mWindow.showAtLocation(mAnchor, Gravity.BOTTOM,
                                anchorRect.left, 0);
                    } else {
                        Rect anchorRect = new Rect(location[0], location[1],
                                location[0] + mRoot.getWidth(), location[1]
                                + mRoot.getHeight());

                        mWindow.setAnimationStyle(mAnimStyle);
                        mWindow.showAtLocation(mRoot, Gravity.BOTTOM,
                                anchorRect.left, 0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            mShowing = true;
            if (mShownListener != null)
                mShownListener.onShown();
        }
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT),
                    timeout);
        }
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    @Override
    public void hide() {
        if (mShowing) {
            if (mAnchor != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    //mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                }
            }
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                if (mFromXml)
                    setVisibility(View.GONE);
                else{
                //    mWindow.dismiss();
                }

            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "MediaController already removed");
            }
            mShowing = false;
            if (mHiddenListener != null)
                mHiddenListener.onHidden();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
      /*  if (mFfwdButton != null) {
            mFfwdButton.setEnabled(enabled);
        }
        if (mRewButton != null) {
            mRewButton.setEnabled(enabled);
        }*/
        if (mProgress != null && !mDisableProgress)
            mProgress.setEnabled(enabled);
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    public  interface MediaControlImpl{
        void onClickCollect(ImageView imageView);
        void onClickBefore(ImageView imageView);
        void onClickNext(ImageView imageView);
        void getprogressDuration(long duration);

        void  inputPrivate();

    }



}
