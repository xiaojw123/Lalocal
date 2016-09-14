package com.lalocal.lalocal.live.entertainment.helper;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by android on 2016/9/7.
 */
public class GiftAnimations {
    private final int LITTLE_DURATIN = 200;
    private final int MSG_UPDATE_VALUE = 0x10;
    private final int MSG_UPDATE_VALUE1 = 0x11;
    private final int MSG_UPDATE_VALUE2 = 0x12;
    private final int SHOW_HIDE_ANIMATOR_DURATION = 1000;
    private final int ANIMATION_STAY_DURATION = 2000;
    private Context mContext;
    private boolean upFree = true;
    private boolean downFree = true;
    private boolean isUping,isDowning;

    private ImageView giftPlane;
    private ViewGroup upView;
    private ViewGroup downView;
    private AnimatorSet upAnimatorSet;
    private AnimatorSet downAnimatorSet;
    private AnimatorSet hidenUpAnimatorSet;
    private AnimatorSet hidenDownAniamtorSet;

    private Queue<GiftBean> cache = new LinkedList<>();
    private AnimationDrawable rocketAnimation;
    Animation textAnimation;


//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            int code = msg.what;
//            switch (code) {
//                case 110:
//                    rocketAnimation.stop();
//                    giftPlane.setVisibility(View.GONE);
//                    break;
//                case MSG_UPDATE_VALUE:
//                    if (mGifCount > 99 && mFlowingValue % 10 == 0 && mFlowingValue <= (m - 10)
//                            && mFlowingValue >= 10) {
//                        mFlowingValue += 10;
//                        sendEmptyMessageDelayed(MSG_UPDATE_VALUE, LITTLE_DURATIN);
//                    } else if (mFlowingValue == mGifCount) {
//                        hidenAnimation();
//                    } else {
//                        ++mFlowingValue;
//                        sendEmptyMessageDelayed(MSG_UPDATE_VALUE, LITTLE_DURATIN);
//                    }
//                    sendGiftTotal.setText("x" + mFlowingValue);
//                    sendGiftTotal.startAnimation(textAnimation);
//                    break;
//
//
//            }
//        }
//
//    };

    public GiftAnimations(ImageView giftPlane, ViewGroup downView, ViewGroup upView, Context mContext) {
        this.mContext = mContext;
        this.upView = upView;
        this.downView = downView;
        this.giftPlane = giftPlane;
        this.hidenUpAnimatorSet = buildHidenAnimationSet(upView);
        this.hidenDownAniamtorSet = buildHidenAnimationSet(downView);
        this.upAnimatorSet = buildAnimationSet(upView);
        this.downAnimatorSet = buildAnimationSet(downView);
        textAnimation = AnimationUtils.loadAnimation(mContext, R.anim.text_animation);
    }

    // 收到礼物，等待显示动画
    public void showGiftAnimation(final ChatRoomMessage message) {
        GiftBean giftBean = new GiftBean();
        giftBean.setFromAccount(message.getFromAccount());
        Map<String, Object> remoteExtension = message.getRemoteExtension();
        if (remoteExtension != null) {
            Iterator<Map.Entry<String, Object>> iterator = remoteExtension.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next1 = iterator.next();
                String key1 = next1.getKey();
                Object value1 = next1.getValue();
                if (key1.equals("giftModel")) {
                    Map<String, Object> map = (Map<String, Object>) value1;
                    Iterator<Map.Entry<String, Object>> mapItem = map.entrySet().iterator();
                    while (mapItem.hasNext()) {
                        Map.Entry<String, Object> next = mapItem.next();
                        String key = next.getKey();
                        Object value = next.getValue();
                        switch (key) {
                            case "headImage":
                                giftBean.setHeadImage(value.toString());
                                break;
                            case "giftImage":
                                giftBean.setGiftImage(value.toString());
                                break;
                            case "giftName":
                                giftBean.setGiftName(value.toString());
                                break;
                            case "giftCount":
                                giftBean.setGiftCount(Integer.parseInt(value.toString()));
                                break;
                            case "code":
                                giftBean.setCode(value.toString());
                                break;
                            case "userName":
                                giftBean.setUserName(value.toString());
                                break;
                            case "userId":
                                giftBean.setUserId(value.toString());
                                break;
                        }
                    }
                }

            }


            cache.add(giftBean);
            checkAndStart();
        }
    }

    private void checkAndStartForHiden() {
        AppLog.print("checkAndStartForHiden___upFree__" + upFree + ", downFree__" + downFree);
        if (!upFree && !downFree) {
            return;
        }
        if (downFree) {
            hidenDownAniamtorSet.start();
//            downFree = false;
        } else {
//            upFree = false;
            hidenUpAnimatorSet.start();
        }
    }


    private void checkAndStart() {
        AppLog.print("checkAndStart____upfree_" + upFree + ", downFree" + downFree);
        if (!upFree && !downFree) {
            return;
        }
        if (downFree) {
            startAnimation(downView, downAnimatorSet);
        } else {
            startAnimation(upView, upAnimatorSet);
        }
    }


    // 开始礼物动画
    private void startAnimation(ViewGroup target, AnimatorSet set) {
        GiftBean message = cache.poll();
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
        ObjectAnimator hide = buildHideAnimator(target, SHOW_HIDE_ANIMATOR_DURATION);
        set.setStartDelay(LITTLE_DURATIN);
        set.play(hide);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkAndStartForHiden();
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
//        set.playSequentially(show, hide);
//        set.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
////                if (!isShow) {
////                    onAnimationCompleted(target, isShow);
////                }
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });

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
        sendGiftName.setText(message.getGiftName());
        int count = message.getGiftCount();
        int n = count / 10; // 2
        int m = n * 10; // 20
        GiftHandler handler = new GiftHandler(m, count, 0, sendGiftTotal);
        handler.sendEmptyMessage(MSG_UPDATE_VALUE);
        DrawableUtils.displayImg(mContext, sendGiftAvatar, message.getHeadImage());
        switch (message.getCode()) {
            case "001":
                sendGiftImg.setBackgroundResource(R.drawable.rose_rocket);
                break;
            case "002":
                sendGiftImg.setBackgroundResource(R.drawable.boot_rocket);
                break;
            case "003":
                sendGiftImg.setBackgroundResource(R.drawable.plane_rocket);
                return;

        }
        rocketAnimation = (AnimationDrawable) sendGiftImg.getBackground();
        rocketAnimation.start();
    }


    class GiftHandler extends Handler {
        int mGifCount;
        int mFlowingValue;
        TextView mSendGiftTotal;
        int m;

        public GiftHandler(int m, int giftCout, int followValue, TextView sendGiftTotal) {
            this.m = m;
            mGifCount = giftCout;
            mFlowingValue = followValue;
            mSendGiftTotal = sendGiftTotal;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_VALUE:
                    if (mGifCount > 99 && mFlowingValue % 10 == 0 && mFlowingValue <= (m - 10)
                            && mFlowingValue >= 10) {
                        mFlowingValue += 10;
                        sendEmptyMessageDelayed(MSG_UPDATE_VALUE, LITTLE_DURATIN);
                    } else if (mFlowingValue == mGifCount) {
                        checkAndStartForHiden();
                    } else {
                        ++mFlowingValue;
                        sendEmptyMessageDelayed(MSG_UPDATE_VALUE, LITTLE_DURATIN);
                    }
                    mSendGiftTotal.setText("x" + mFlowingValue);
                    mSendGiftTotal.startAnimation(textAnimation);
                    break;

            }
        }
    }

    public void animtion(){
        //boolean  isDownAnimtion=true  isUpAniamtion=true
        //
        // isDown --->isDown=false  startDownAnimtion --->  endAnimtion  isDown=true





    }

}
