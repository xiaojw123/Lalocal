package com.lalocal.lalocal.live.entertainment.helper;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.MessageToBean;
import com.lalocal.lalocal.live.base.util.ScreenUtil;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.live.entertainment.ui.MyAnimationDrawable;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by android on 2016/9/11.
 */
public class GiftPlaneAnimation {
    private ImageView giftPlaneUp;
    private RelativeLayout giftPlaneBg;
    private View gfitPlaneIndex;
    private TextView giftPlaneText;
    private Context mContext;
    private Queue<GiftBean> cache = new LinkedList<>();//队列数据结构管理类,先进先出
    private AnimationDrawable rocketAnimation;
    private Animation messageBgAnimation;
    private Animation messageSliderAnimtion;
    private boolean isStartAnim=true;
    ImageView mAnchorHeadImg,mUserHeadImg;
    private String avatar;


    public GiftPlaneAnimation(ImageView anchorHeadImg,ImageView userHeadImg,ImageView giftPlaneUp, RelativeLayout giftPlanceBg, Context mContext,String avatar) {
        mAnchorHeadImg=anchorHeadImg;
        mUserHeadImg=userHeadImg;
        this.giftPlaneUp = giftPlaneUp;
        this.mContext = mContext;
        this.giftPlaneBg = giftPlanceBg;
        this.avatar=avatar;

        gfitPlaneIndex = giftPlanceBg.getChildAt(0);
        giftPlaneText = (TextView) giftPlanceBg.getChildAt(1);
        AppLog.i("TAG","飞机动画1:avatar"+avatar);
        DrawableUtils.displayImg(mContext,mAnchorHeadImg,avatar);
    }

    public void showPlaneAnimation(ChatRoomMessage message) {
        GiftBean messageToGiftBean = MessageToBean.getMessageToGiftBean(message);
        cache.add(messageToGiftBean);
        startAnimation(giftPlaneUp,giftPlaneBg);
    }

    private void startAnimation(ImageView target,RelativeLayout giftPlanceBg) {
        if(isStartAnim){
            isStartAnim=false;
            GiftBean giftBean = cache.poll();
            if (giftBean == null) {
                isStartAnim=true;
                return;
            }
            updateView(giftBean, target,giftPlanceBg);
        }
    }

    private void updateView(final  GiftBean giftBean, final ImageView target,final RelativeLayout giftPlanceBg) {
        mAnchorHeadImg.setVisibility(View.VISIBLE);
        mUserHeadImg.setVisibility(View.VISIBLE);
        AppLog.i("TAG","飞机动画2："+giftBean.getHeadImage());

        DrawableUtils.displayImg(mContext,mUserHeadImg,giftBean.getHeadImage());
        target.setVisibility(View.VISIBLE);

        giftPlaneBg.setVisibility(View.VISIBLE);
        String s = giftBean.getUserName() + "  与主播同乘飞机旅行";
        TextView sendName= (TextView) giftPlanceBg.findViewById(R.id.audience_gift_send_plane);
        sendName.setText(s);
        giftPlaneBg.startAnimation(getTranslateAnim());
        MyAnimationDrawable.animateRawManuallyFromXML(R.drawable.plane_rocket,target,null,null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 4400);
    }

    @NonNull
    private Animation getTranslateAnim() {
        if (messageBgAnimation == null) {
            messageBgAnimation = new TranslateAnimation(-ScreenUtil.screenWidth, 0, 0, 0);
            messageBgAnimation.setDuration(2000);
            messageBgAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (messageSliderAnimtion == null) {
                        messageSliderAnimtion = new TranslateAnimation(0, ScreenUtil.screenWidth, 0, 0);
                        messageSliderAnimtion.setDuration(1200);
                    }
                    gfitPlaneIndex.startAnimation(messageSliderAnimtion);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
        return messageBgAnimation;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 || msg.what == 2) {
                isStartAnim=true;
                if (msg.what == 1) {
                    giftPlaneUp.setVisibility(View.GONE);
                    giftPlaneBg.setVisibility(View.GONE);
                    mAnchorHeadImg.setVisibility(View.GONE);
                    mUserHeadImg.setVisibility(View.GONE);
                    startAnimation(giftPlaneUp,giftPlaneBg);

                }
            }
        }
    };


}
