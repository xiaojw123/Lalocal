package com.jcodecraeer.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;

import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class ArrowRefreshHeader extends LinearLayout implements BaseRefreshHeader {
   private GifImageView mGifImageView;
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	private SimpleViewSwitcher mProgressBar;
	private TextView mStatusTextView;
	private int mState = STATE_NORMAL;

	private TextView mHeaderTimeView;
    private  RelativeLayout mHeadContent;
	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;

	private static final int ROTATE_ANIM_DURATION = 180;

	public int mMeasuredHeight;
    public boolean isSysResfresh;

	public ArrowRefreshHeader(Context context) {
		super(context);
		initView();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ArrowRefreshHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		// 初始情况，设置下拉刷新view高度为0
		mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(
				R.layout.listview_header, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
		this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

		addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
		setGravity(Gravity.BOTTOM);
        mHeadContent=(RelativeLayout) findViewById(R.id.listview_header_content);
        mGifImageView=(GifImageView) findViewById(R.id.listview_header_gifview);
        mArrowImageView = (ImageView)findViewById(R.id.listview_header_arrow);
		mStatusTextView = (TextView)findViewById(R.id.refresh_status_textview);

        //init the progress view
		mProgressBar = (SimpleViewSwitcher)findViewById(R.id.listview_header_progressbar);
        AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        mProgressBar.setView(progressView);


		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);

		mHeaderTimeView = (TextView)findViewById(R.id.last_refresh_time);
		measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		mMeasuredHeight = getMeasuredHeight();
	}

    public void setProgressStyle(int style) {
        if(style == ProgressStyle.SysProgress){
            isSysResfresh=true;
            mHeadContent.setVisibility(GONE);
            mGifImageView.setVisibility(VISIBLE);
            mGifImageView.setBackgroundResource(R.drawable.loading_down);
//            mProgressBar.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        }else{
            isSysResfresh=false;
            mHeadContent.setVisibility(VISIBLE);
            mGifImageView.setVisibility(GONE);
            AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            mProgressBar.setView(progressView);
        }
    }

    public void setArrowImageView(int resid){
        mArrowImageView.setImageResource(resid);
    }

	public void setState(int state) {
		if (state == mState) return ;

		if (state == STATE_REFRESHING) {	// 显示进度
            AppLog.print("STATE_REFRESHING____");
            if (isSysResfresh){
                mGifImageView.setVisibility(VISIBLE);
                 mGifImageView.setBackgroundResource(R.drawable.loading_down);
            }else{
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
            }
		} else if(state == STATE_DONE) {
            AppLog.print("STATE_DONE____");
            if (!isSysResfresh){
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            }
        } else {	// 显示箭头图片
            AppLog.print("setate else____");
            if (isSysResfresh){
                mGifImageView.setVisibility(VISIBLE);
            }else{
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
            }
		}

		switch(state){
            case STATE_NORMAL:
                AppLog.print("STATE_NORMAL————————————");
                if (!isSysResfresh){

                if (mState == STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mStatusTextView.setText(R.string.listview_header_hint_normal);
                }else{
                    mGifImageView.setBackgroundResource(R.drawable.loading_down);
                }
                break;
            case STATE_RELEASE_TO_REFRESH:
                AppLog.print("STATE_RELEASE_TO_REFRESH————————————");
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    if (!isSysResfresh){
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mStatusTextView.setText(R.string.listview_header_hint_release);
                    }
                }
                break;
            case     STATE_REFRESHING:
                AppLog.print("STATE_REFRESHING————————————");
                if (!isSysResfresh){
                mStatusTextView.setText(R.string.refreshing);
                }
                break;
            case    STATE_DONE:
                AppLog.print("STATE_DONE————————————");
                if (!isSysResfresh){
                mStatusTextView.setText(R.string.refresh_done);
                }else{
                    mGifImageView.setBackgroundResource(R.drawable.loading_up);
                }
                break;
            default:
		}

		mState = state;
	}

    public int getState() {
        return mState;
    }

    @Override
	public void refreshComplete(){
        AppLog.print("refreshComplete_____");
        if (!isSysResfresh){
        mHeaderTimeView.setText(friendlyTime(new Date()));
        }
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                reset();
            }
        }, 2000);
	}

	public void setVisibleHeight(int height) {
		if (height < 0) height = 0;
		LayoutParams lp = (LayoutParams) mContainer .getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
		return lp.height;
	}

    @Override
    public void onMove(float delta) {
        if(getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                }else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        if(getVisibleHeight() > mMeasuredHeight &&  mState < STATE_REFRESHING){
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <=  mMeasuredHeight) {
            //return;
        }
        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public static String friendlyTime(Date time) {
        //获取time距离当前的秒数
        int ct = (int)((System.currentTimeMillis() - time.getTime())/1000);

        if(ct == 0) {
            return "刚刚";
        }

        if(ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if(ct >= 60 && ct < 3600) {
            return Math.max(ct / 60,1) + "分钟前";
        }
        if(ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if(ct >= 86400 && ct < 2592000){ //86400 * 30
            int day = ct / 86400 ;
            return day + "天前";
        }
        if(ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }

}