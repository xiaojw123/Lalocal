package com.lalocal.lalocal.live.entertainment.helper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.MessageToBean;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 礼物动画
 *
 */
public class GiftAnimation {
    private final int SHOW_HIDE_ANIMATOR_DURATION = 1000;
    private final int ANIMATION_STAY_DURATION = 2000;
    private Context mContext;
    private boolean upFree = true;
    private boolean downFree = true;


    private ViewGroup upView;
    private ViewGroup downView;
    private AnimatorSet upAnimatorSet;
    private AnimatorSet downAnimatorSet;

    private Queue<GiftBean> cache = new LinkedList<>();
    private AnimationDrawable rocketAnimation;


    public GiftAnimation(ViewGroup downView, ViewGroup upView,Context mContext) {
        this.mContext=mContext;
        this.upView = upView;
        this.downView = downView;

        this.upAnimatorSet = buildAnimationSet(upView);
        this.downAnimatorSet = buildAnimationSet(downView);
    }

    // 收到礼物，等待显示动画
    public void showGiftAnimation(final ChatRoomMessage message) {
        GiftBean giftBean = MessageToBean.getMessageToGiftBean(message);
        cache.add(giftBean);
        checkAndStart();
    }

    private void checkAndStart() {
        if(!upFree && !downFree) {
            return;
        }

        if(downFree) {
            startAnimation(downView, downAnimatorSet);
        } else {
            startAnimation(upView, upAnimatorSet);
        }
    }

    // 开始礼物动画
    String fromAccount1;
    private void startAnimation(ViewGroup target, AnimatorSet set) {
        GiftBean message = cache.poll();
        if(message == null) {
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
        if(target == upView) {
            upFree = false;
        } else if(target == downView) {
            downFree = false;
        }
    }

    private void onAnimationCompleted(final ViewGroup target) {
        if(target == upView) {
            upFree = true;
        } else if(target == downView) {
            downFree = true;
        }

        checkAndStart();
    }

    /**
     * ********************* 属性动画 *********************
     */

    private AnimatorSet buildAnimationSet(final ViewGroup target){
        ObjectAnimator show = buildShowAnimator(target, SHOW_HIDE_ANIMATOR_DURATION);
        ObjectAnimator hide = buildHideAnimator(target, SHOW_HIDE_ANIMATOR_DURATION);
        hide.setStartDelay(ANIMATION_STAY_DURATION);

        AnimatorSet set = new AnimatorSet();
        set.setTarget(target);
        set.playSequentially(show, hide);
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
        TextView sendGiftTotal = (TextView) root.findViewById(R.id.send_gift_total);
        audienceNameText.setText(message.getUserName());
        sendGiftName.setText(message.getGiftName());
        sendGiftTotal.setText(String.valueOf(message.getGiftCount()));
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