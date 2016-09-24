package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by android on 2016/9/7.
 */
public class CreateLiveRoomPopuwindow extends PopupWindow {
    private Context mContext;
    private EditText liveRoomName;
    private TextView textCount;
    private ImageView shareFriends;
    private ImageView shareWeibo;
    private ImageView shareWeixin;
    private ImageView cancelCreateRoom;
    private TextView inputLiveRoom;
    private ScrollView startLiveScrollview;
    private LinearLayout startLiveLayout1;
    private String roomName;//直播室名字
    private BlurImageView roomBg;

    public CreateLiveRoomPopuwindow(Context mContext){
        this.mContext=mContext;
    }
    public  void showCreateLiveRoomPopuwindow(){
        View view = View.inflate(mContext, R.layout.live_create_room_pop_layout, null);
        cancelCreateRoom = (ImageView) view.findViewById(R.id.live_create_room_close_iv);
        inputLiveRoom = (TextView) view.findViewById(R.id.input_start_live);
        startLiveScrollview = (ScrollView) view.findViewById(R.id.start_live_bottom_layout);
        startLiveLayout1 = (LinearLayout) view.findViewById(R.id.start_live_layout);
        shareFriends = (ImageView) view.findViewById(R.id.live_create_share_friends);
        shareWeibo = (ImageView) view.findViewById(R.id.live_create_share_weibo);
        shareWeixin = (ImageView) view.findViewById(R.id.live_create_share_weixin);
        roomBg = (BlurImageView) view.findViewById(R.id.live_create_room_bg);
        String userAvatar = UserHelper.getUserAvatar(mContext);
        if(userAvatar!=null){
            roomBg.setBlurImageURL(userAvatar);
        }

        roomBg.setScaleRatio(20);
        roomBg.setBlurRadius(1);


        shareFriends.setSelected(true);
        isShareSelector = 0;
        shareFriends.setOnClickListener(buttonClickListener);
        shareWeibo.setOnClickListener(buttonClickListener);
        shareWeixin.setOnClickListener(buttonClickListener);
        startLiveLayout1.setOnClickListener(buttonClickListener);
        cancelCreateRoom.setOnClickListener(buttonClickListener);
        //自动调用软键盘
        liveRoomName = (EditText) view.findViewById(R.id.live_room_name);
        textCount = (TextView) view.findViewById(R.id.live_text_title_count);
        liveRoomName.setFocusable(true);
        liveRoomName.setFocusableInTouchMode(true);
        liveRoomName.requestFocus();
        liveRoomName.addTextChangedListener(watcher);//edittext字数改变监听
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) liveRoomName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(liveRoomName, 0);
                           }
                       },
                300);


        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);

    }


    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String liveRoomNameCount = liveRoomName.getText().toString();
            int length = liveRoomNameCount.length();
            textCount.setText(length + "/20");
        }
    };

    boolean isFirstFirendsClick=true;
    int isShareSelector = -1;//创建直播分享
    View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.live_create_share_friends:
                    if(isFirstFirendsClick){
                        isFirstFirendsClick=false;
                        shareFriends.setSelected(false);
                        isShareSelector=-1;
                    }else {
                        shareWeibo.setSelected(false);
                        shareFriends.setSelected(true);
                        shareWeixin.setSelected(false);
                        isShareSelector=0;
                    }
                    break;
                case R.id.live_create_share_weibo:
                    shareWeibo.setSelected(true);
                    shareFriends.setSelected(false);
                    shareWeixin.setSelected(false);
                    isShareSelector = 1;
                    break;
                case R.id.live_create_share_weixin:
                    shareFriends.setSelected(false);
                    shareWeibo.setSelected(false);
                    shareWeixin.setSelected(true);
                    isShareSelector = 2;
                    break;
                case R.id.start_live_layout:
                    roomName = liveRoomName.getText().toString().trim();
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(startLiveScrollview.getWindowToken(), 0);
                    if(onCreateLiveListener!=null){
                        onCreateLiveListener.startLiveBtn(roomName,isShareSelector);
                    }
                    break;
                case R.id.live_create_room_close_iv:
                    inputLiveRoom.setVisibility(View.GONE);
                    if(onCreateLiveListener!=null){
                        onCreateLiveListener.closeLiveBtn();
                    }
                    break;
            }
        }
    };


    private OnCreateLiveListener onCreateLiveListener;

    public interface OnCreateLiveListener {
        void startLiveBtn(String roomName,int isShareSelector);
        void closeLiveBtn();
    }

    public void setOnSendClickListener(OnCreateLiveListener onCreateLiveListener) {
        this.onCreateLiveListener = onCreateLiveListener;
    }
}
