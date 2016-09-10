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
    private final int SHOW_HIDE_ANIMATOR_DURATION = 1000;
    private final int ANIMATION_STAY_DURATION = 2000;
    private Context mContext;
    private boolean upFree = true;
    private boolean downFree = true;

    private ImageView giftPlane;
    private ViewGroup upView;
    private ViewGroup downView;
    private AnimatorSet upAnimatorSet;
    private AnimatorSet downAnimatorSet;

    private Queue<GiftBean> cache = new LinkedList<>();
    private AnimationDrawable rocketAnimation;
    TextView sendGiftTotal;
    int mFlowingValue = 1;
    int mGifCount;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 110) {
                rocketAnimation.stop();
                giftPlane.setVisibility(View.GONE);

            } else if (msg.what == MSG_UPDATE_VALUE) {
//                sendGiftTotal.setText("x" + count);


//                sendTotal = 1;
//                sendTotal = 9;
//                sendTotal = 10;
//                sendTotal = 66;
//                sendTotal = 99;
//                sendTotal = 666;
//                sendTotal = 999;


                switch (mFlowingValue) {
                    case 0:
                        sendGifCout(1);
                        sendGiftTotal.setText("x" + 0);
                        break;

                    case 1:
                        sendGifCout(9);
                        sendGiftTotal.setText("x" + 1);
                        break;
                    case 9:
                        sendGifCout(10);
                        sendGiftTotal.setText("x" + 9);
                        break;
                    case 10:
                        sendGifCout(66);
                        sendGiftTotal.setText("x" + 10);
                        break;
                    case 66:
                        sendGifCout(99);
                        sendGiftTotal.setText("x" + 66);
                        break;
                    case 99:
                        sendGifCout(666);
                        sendGiftTotal.setText("x" + 99);
                        break;
                    case 666:
                        sendGifCout(999);
                        sendGiftTotal.setText("x" + 666);
                        break;
                    case 999:
                        sendGiftTotal.setText("x" + 999);
                        break;

                }


            }
        }

        private void sendGifCout(int nextValue) {
            if (mGifCount >= nextValue) {
                mFlowingValue = nextValue;
                sendEmptyMessageDelayed(MSG_UPDATE_VALUE, LITTLE_DURATIN);
            }
        }
    };

    public GiftAnimations(ImageView giftPlane, ViewGroup downView, ViewGroup upView, Context mContext) {
        this.mContext = mContext;
        this.upView = upView;
        this.downView = downView;
        this.giftPlane = giftPlane;
        this.upAnimatorSet = buildAnimationSet(upView);
        this.downAnimatorSet = buildAnimationSet(downView);
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
                        }
                    }
                }

            }


            cache.add(giftBean);
            checkAndStart();
        }
    }

    private void checkAndStart() {
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

    private AnimatorSet buildAnimationSet(final ViewGroup target) {
        ObjectAnimator show = buildShowAnimator(target, SHOW_HIDE_ANIMATOR_DURATION);
        ObjectAnimator scaleY = buildScaleyAnimator(target, "scaleY", SHOW_HIDE_ANIMATOR_DURATION, 1.0f, 2.5f);
        ObjectAnimator scaleX = buildScaleyAnimator(target, "scaleX", SHOW_HIDE_ANIMATOR_DURATION, 1.0f, 2.5f);
        ObjectAnimator scaleY1 = buildScaleyAnimator(target, "scaleY", SHOW_HIDE_ANIMATOR_DURATION, 2.5f, 1.0f);
        ObjectAnimator scalex1 = buildScaleyAnimator(target, "scaleX", SHOW_HIDE_ANIMATOR_DURATION, 2.5f, 1.0f);
        ObjectAnimator hide = buildHideAnimator(target, SHOW_HIDE_ANIMATOR_DURATION);
        hide.setStartDelay(ANIMATION_STAY_DURATION);
        final AnimatorSet set = new AnimatorSet();
        set.setTarget(target);
        set.playTogether(scaleX, scaleY, show);
        set.playTogether(scalex1, scaleY1, hide);
//        set.playSequentially(scale, show, hide);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationCompleted(target);
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


    private ObjectAnimator buildScaleyAnimator(final View target, String propertyName, long duration, float... values) {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, propertyName, values)
                .setDuration(duration);
        scaleY.setInterpolator(new OvershootInterpolator());
        return scaleY;
    }

    private ObjectAnimator buildShowAnimator(final View target, long duration) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -300.0F, 0.0F)
                .setDuration(duration);
        translationX.setInterpolator(new OvershootInterpolator());

        return translationX;
    }

    private ObjectAnimator buildHideAnimator(final View target, long duration) {
        return ObjectAnimator.ofFloat(target, View.ALPHA, 1f, 0.0f)
                .setDuration(duration);
    }

    /**
     * ********************* 更新礼物信息 *********************
     */

    private void updateView(final GiftBean message, ViewGroup root) {


        TextView audienceNameText = (TextView) root.findViewById(R.id.send_gift_username);
        ImageView sendGiftAvatar = (ImageView) root.findViewById(R.id.send_gift_avatar);
        TextView sendGiftName = (TextView) root.findViewById(R.id.send_gift_name);
        ImageView sendGiftImg = (ImageView) root.findViewById(R.id.send_gift_img);
        sendGiftTotal = (TextView) root.findViewById(R.id.send_gift_total);
        audienceNameText.setText(message.getUserName());
        sendGiftName.setText(message.getGiftName());
        mFlowingValue=0;
        mGifCount = message.getGiftCount();
        AppLog.print("updateView____gifCount__" + mGifCount);
        if (handler.hasMessages(MSG_UPDATE_VALUE)) {
            handler.removeMessages(MSG_UPDATE_VALUE);
        }
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


}
