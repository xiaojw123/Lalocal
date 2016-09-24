package com.lalocal.lalocal.live.entertainment.helper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.MessageToGiftBean;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by android on 2016/9/7.
 */
public class GiftAnimations {
    private final int LITTLE_DURATIN = 500;
    private final int MSG_UPDATE_VALUE = 0x10;
    private final int SHOW_HIDE_ANIMATOR_DURATION = 1000;
    private Context mContext;
    private boolean upFree = true;
    private boolean downFree = true;
    private ImageView giftPlane;
    private ViewGroup upView;
    private ViewGroup downView;
    private AnimatorSet upAnimatorSet;
    private AnimatorSet downAnimatorSet;
    private AnimatorSet hidenUpAnimatorSet;
    private AnimatorSet hidenDownAniamtorSet;

    private Queue<GiftBean> cache = new LinkedList<>();
    private AnimationDrawable rocketAnimation;

    GiftHandler mHandler;
    GiftHandler upHandler, dowHandler;


    public GiftAnimations(ImageView giftPlane, ViewGroup downView, ViewGroup upView, Context mContext) {
        this.mContext = mContext;
        this.upView = upView;
        this.downView = downView;
        this.giftPlane = giftPlane;
        this.hidenUpAnimatorSet = buildHidenAnimationSet(upView);
        this.hidenDownAniamtorSet = buildHidenAnimationSet(downView);
        this.upAnimatorSet = buildAnimationSet(upView);
        this.downAnimatorSet = buildAnimationSet(downView);
    }

    // 收到礼物，等待显示动画
    public void showGiftAnimation(final ChatRoomMessage message) {
        GiftBean giftBean = MessageToGiftBean.getMessageToGiftBean(message);
        cache.add(giftBean);
        checkAndStart();
    }

    private void checkAndStartForHiden(ViewGroup targetView, GiftHandler handler) {
        if (targetView == downView) {
            dowHandler = handler;
            hidenDownAniamtorSet.start();
        } else {
            upHandler = handler;
            hidenUpAnimatorSet.start();
        }
    }


    private void checkAndStart() {
        if (!upFree && !downFree) {
            return;
        }
        GiftBean message = cache.poll();
        if (message == null) {
            return;
        }
        if (continueGiftAnim(mHandler, message)) return;
        if (downFree) {
            startAnimation(downView, downAnimatorSet, message);
        } else {
            startAnimation(upView, upAnimatorSet, message);
        }
    }

    private boolean continueGiftAnim(GiftHandler handler, GiftBean giftBean) {

        if (handler != null && handler.isRuning()) {
            AppLog.print("continueAnim isRuning___");
            String userid = handler.getUserId();
            String code = handler.getCode();
            if (!TextUtils.isEmpty(userid) && userid.equals(giftBean.getUserId())) {
                if (!TextUtils.isEmpty(code) && code.equals(giftBean.getCode())) {
                    handler.addGiftCout(giftBean.getGiftCount());
                    return true;
                }
            }
        }
        return false;
    }


    // 开始礼物动画
    private void startAnimation(ViewGroup target, AnimatorSet set, GiftBean message) {
//        GiftBean message = cache.poll();
        AppLog.print("startAnimation message___" + message);
        if (message == null) {
            return;
        }

        // 更新状态
        onAnimationStart(target);
        // 更新礼物视图
        updateView(message, target);

        // 执行动画组
        target.setAlpha(1f);
        target.setVisibility(View.VISIBLE);
        set.start();
    }

    private void onAnimationStart(final ViewGroup target) {
        if (target == upView) {
            upFree = false;
        } else if (target == downView) {
            downFree = false;
        }
    }

    private void onAnimationCompleted(final ViewGroup target) {
        if (target == upView) {
            upFree = true;
        } else if (target == downView) {
            downFree = true;
        }

        checkAndStart();
    }

    /**
     * ********************* 属性动画 *********************
     */

    private AnimatorSet buildHidenAnimationSet(final ViewGroup target) {
        final AnimatorSet set = new AnimatorSet();
        set.setTarget(target);
        ObjectAnimator hide = buildHideAnimator(target, 1);
        set.setStartDelay(1200);
        set.play(hide);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (target == upView) {
                    upFree = true;
                    if (upHandler.isRuning()) {
                        upHandler.setRuning(false);
                    }

                } else if (target == downView) {
                    downFree = true;
                    if (dowHandler.isRuning()) {
                        dowHandler.setRuning(false);
                    }
                }
                checkAndStart();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return set;
    }


    private AnimatorSet buildAnimationSet(final ViewGroup target) {
        final AnimatorSet set = new AnimatorSet();
        set.setTarget(target);
        ObjectAnimator show = buildShowAnimator(target, SHOW_HIDE_ANIMATOR_DURATION);
        set.play(show);
        return set;
    }


    private ObjectAnimator buildShowAnimator(final View target, long duration) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -300.0F, 0.0F).setDuration(duration);
        translationX.setInterpolator(new OvershootInterpolator());

        return translationX;
    }

    private ObjectAnimator buildHideAnimator(final View target, long duration) {
        return ObjectAnimator.ofFloat(target, View.ALPHA, 1f, 0.0f).setDuration(duration);
    }

    /**
     * ********************* 更新礼物信息 *********************
     */

    private void updateView(final GiftBean message, ViewGroup root) {
        TextView audienceNameText = (TextView) root.findViewById(R.id.send_gift_username);
        ImageView sendGiftAvatar = (ImageView) root.findViewById(R.id.send_gift_avatar);
        TextView sendGiftName = (TextView) root.findViewById(R.id.send_gift_name);
        ImageView sendGiftImg = (ImageView) root.findViewById(R.id.send_gift_img);
        TextView sendGiftTotal = (TextView) root.findViewById(R.id.send_gift_total);
        audienceNameText.setText(message.getUserName());
        sendGiftName.setText("送了一个"+message.getGiftName());
        int count = message.getGiftCount();
        int n = count / 10; // 2
        int m = n * 10; // 20
        String userId = message.getUserId();
        DrawableUtils.displayImg(mContext, sendGiftAvatar, message.getHeadImage());
        sendGiftImg.setImageResource(0);
        String code = message.getCode();
        mHandler = new GiftHandler(sendGiftTotal);
        sendGiftMessage(mHandler, root, count, m, userId, code);
        switch (code) {
            case "001":
                sendGiftImg.setBackgroundResource(R.drawable.rose_rocket);
                break;
            case "002":
                sendGiftImg.setBackgroundResource(R.drawable.boot_rocket);
                break;
            case "003":
                sendGiftImg.setBackgroundResource(R.drawable.plane_rocket);
                break;
            default:
                sendGiftImg.setBackgroundResource(0);
                DrawableUtils.displayImg(mContext, sendGiftImg, message.getGiftImage());
                return;
        }
        rocketAnimation = (AnimationDrawable) sendGiftImg.getBackground();
        rocketAnimation.start();
    }

    private void sendGiftMessage(GiftHandler handler, ViewGroup root, int count, int m, String userid, String code) {
        AppLog.print("send gift message___");
        handler.setUserId(userid);
        handler.setRefNum(m);
        handler.setGiftCout(count);
        handler.setGitCode(code);
        handler.setFolowValue(0);
        handler.setTargeView(root);
        handler.sendEmptyMessage(MSG_UPDATE_VALUE);
    }


    class GiftHandler extends Handler {
        int mGifCount;
        int mFlowingValue;
        TextView mSendGiftTotal;
        int m;
        ViewGroup mTargeView;
        private boolean isRuning;
        private String mUserId;
        String mCode;

        public GiftHandler(TextView sendGiftTotal) {
            mSendGiftTotal = sendGiftTotal;
        }

        public void setRefNum(int m) {
            this.m = m;
        }

        public void setGiftCout(int giftCout) {
            mGifCount = giftCout;

        }

        public void setGitCode(String code) {
            mCode = code;
        }

        public String getCode() {
            return mCode;
        }

        public void setUserId(String userId) {
            mUserId = userId;
        }

        public String getUserId() {
            return mUserId;
        }


        public void setFolowValue(int followValue) {
            mFlowingValue = followValue;
        }


        public void setTargeView(ViewGroup targeView) {
            mTargeView = targeView;
        }

        public void addGiftCout(int attachValue) {
            mGifCount += attachValue;
            if (hasMessages(MSG_UPDATE_VALUE)) {
                removeMessages(MSG_UPDATE_VALUE);
            }
            sendEmptyMessage(MSG_UPDATE_VALUE);
        }

        public boolean isRuning() {
            return isRuning;
        }

        public void setRuning(boolean isRuning) {
            this.isRuning = isRuning;

        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_VALUE:
                    if (!isRuning) {
                        isRuning = true;
                    }
                    if (mGifCount > 99 && mFlowingValue % 10 == 0 && mFlowingValue <= (m - 10)
                            && mFlowingValue >= 10) {
                        mFlowingValue += 10;
                        sendEmptyMessageDelayed(MSG_UPDATE_VALUE, LITTLE_DURATIN);
                    } else if (mFlowingValue == mGifCount) {
                        checkAndStartForHiden(mTargeView, this);
                    } else {
                        ++mFlowingValue;
                        sendEmptyMessageDelayed(MSG_UPDATE_VALUE, LITTLE_DURATIN);
                    }
                    mSendGiftTotal.setText("x" + mFlowingValue);
                    Animation textAnimation = AnimationUtils.loadAnimation(mContext, R.anim.text_animation);
                    mSendGiftTotal.startAnimation(textAnimation);
                    break;

            }
        }
    }


}
