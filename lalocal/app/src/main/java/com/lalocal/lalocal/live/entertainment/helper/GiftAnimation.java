package com.lalocal.lalocal.live.entertainment.helper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
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

    private ImageView giftPlane;
    private RelativeLayout upView;
    private RelativeLayout downView;


    private Queue<GiftBean> cache = new LinkedList<>();
    private AnimationDrawable rocketAnimation;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==110){
                rocketAnimation.stop();
                giftPlane.setVisibility(View.GONE);

            }
        }
    };
    private TranslateAnimation inAnim;//礼物View出现的动画
    private TranslateAnimation outAnim;//礼物View消失的动画
    private ScaleAnimation giftNumAnim;//修改礼物数量的动画
    private TextView sendGiftTotal;


    public GiftAnimation(ImageView giftPlane, RelativeLayout downView, RelativeLayout upView, Context mContext) {
        this.mContext=mContext;
        this.upView = upView;
        this.downView = downView;
        this.giftPlane=giftPlane;

        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.gift_out);
        giftNumAnim = (ScaleAnimation) AnimationUtils.loadAnimation(mContext, R.anim.gift_num);
    }

    // 收到礼物，等待显示动画
    public void showGiftAnimation(final ChatRoomMessage message) {
        GiftBean giftBean = new GiftBean();
            giftBean.setFromAccount(message.getFromAccount());
        Map<String, Object> remoteExtension = message.getRemoteExtension();
        if (remoteExtension != null) {
            Iterator<Map.Entry<String, Object>> iterator = remoteExtension.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
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


            cache.add(giftBean);
            checkAndStart();
        }
    }
    private void checkAndStart() {
        if(!upFree && !downFree) {
            return;
        }

        if(upFree) {
            startAnimation(upView, inAnim);
        } else {
            startAnimation(downView, inAnim);
        }
    }



    // 开始礼物动画
  String upFromAccount;
    String upCode;

    int upGiftCount=0;
    private void startAnimation(final RelativeLayout target, TranslateAnimation set) {
        final GiftBean giftBean = cache.poll();
        if(giftBean == null) {
            return;
        }
        // 更新礼物视图
        updateView(giftBean, target);
        String fromAccount = giftBean.getFromAccount();
        String code = giftBean.getCode();
         int  giftCount = giftBean.getGiftCount();
        if(fromAccount.equals(upFromAccount)&&code.equals(upCode)&&giftCount<66){
            onAnimationStart(target,true);
            upGiftCount=upGiftCount+giftCount;
            startGiftAnim(target,giftBean,set);
            return;
        }else {
           // onAnimationStart(target,false);
            target.setAlpha(1f);
            target.setVisibility(View.VISIBLE);
            target.startAnimation(set);
           set.setAnimationListener(new Animation.AnimationListener() {
               @Override
               public void onAnimationStart(Animation animation) {

               }

               @Override
               public void onAnimationEnd(Animation animation) {
                   startAnim(target,sendGiftTotal, 1);
               }

               @Override
               public void onAnimationRepeat(Animation animation) {

               }
           });
        }
       upGiftCount=giftCount;
        upCode=code;
        upFromAccount=fromAccount;
    }

    private void startGiftAnim(final RelativeLayout target, final GiftBean giftBean,TranslateAnimation set) {

        target.setAlpha(1f);
        target.setVisibility(View.VISIBLE);
        target.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                startAnim(target,sendGiftTotal, 1);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void onAnimationStart(final RelativeLayout target,boolean liansong) {
        if(liansong){
            if(target == upView) {
                upFree = true;
            } else if(target == downView) {
                downFree = true;
            }
        }else {
            if(target == upView) {
                upFree = false;
            } else if(target == downView) {
                downFree = false;
            }
        }

    }

    private void onAnimationCompleted(final RelativeLayout target) {
        if(target == upView) {
            upFree = true;
        } else if(target == downView) {
            downFree = true;
        }

        checkAndStart();
    }



    /**
     * ********************* 更新礼物信息 *********************
     */

    private void updateView(final GiftBean message, RelativeLayout root) {

        TextView audienceNameText = (TextView) root.findViewById(R.id.send_gift_username);
        ImageView sendGiftAvatar= (ImageView) root.findViewById(R.id.send_gift_avatar);
        TextView sendGiftName= (TextView) root.findViewById(R.id.send_gift_name);
        ImageView sendGiftImg= (ImageView) root.findViewById(R.id.send_gift_img);
        sendGiftTotal = (TextView) root.findViewById(R.id.send_gift_total);
        audienceNameText.setText(message.getUserName());
        sendGiftName.setText(message.getGiftName());

        DrawableUtils.displayImg(mContext,sendGiftAvatar,message.getHeadImage());
        switch (message.getCode()){
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

    private void startAnim(final RelativeLayout target,final TextView tv, final int giftCoun) {
        tv.setText("x"+giftCoun);
        final AnimatorSet set=new AnimatorSet();
        ObjectAnimator sa1=ObjectAnimator.ofFloat(tv,"scaleY",2.0f,1.5f,1.0f,0.5f,0.2f,0.5f,1.0f);
        sa1.setDuration(400);
        ObjectAnimator sa2=ObjectAnimator.ofFloat(tv,"scaleX",2.0f,1.5f,1.0f,0.5f,0.2f,0.5f,1.0f);
        sa2.setDuration(400);
        set.setDuration(400);
        set.setTarget(tv);
        set.playTogether(sa1, sa2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(giftCoun<upGiftCount){
                    startAnim(target,tv,(giftCoun+1));
                }else{
                    target.setAnimation(outAnim);
                    outAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            target.setVisibility(View.GONE);
                            onAnimationCompleted(target);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
