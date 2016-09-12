package com.lalocal.lalocal.live.entertainment.helper;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by android on 2016/9/11.
 */
public class GiftPlaneAnimation {
    private ImageView giftPlaneUp;

    private Context mContext;

    private Queue<ChatRoomMessage> cache = new LinkedList<>();
    private AnimationDrawable rocketAnimation;

    public GiftPlaneAnimation(ImageView giftPlaneUp,Context mContext){
        this.giftPlaneUp=giftPlaneUp;
        this.mContext=mContext;
    }
    public void showPlaneAnimation(ChatRoomMessage message){
        cache.add(message);
        startAnimation(giftPlaneUp);
    }

    private void startAnimation(ImageView target) {
        ChatRoomMessage message = cache.poll();
        if(message == null) {
            return;
        }
        updateView(message,target);


    }

    private void updateView(final ChatRoomMessage message, final ImageView target) {
        target.setVisibility(View.VISIBLE);
        target.setBackgroundResource(R.drawable.plane_rocket);
        rocketAnimation = (AnimationDrawable) target.getBackground();
        rocketAnimation.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message msg=new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }
        }, 4400);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1||msg.what==2) {
                rocketAnimation.stop();
                if(msg.what==1){
                    giftPlaneUp.setVisibility(View.GONE);

                }
            }
        }
    };


}
