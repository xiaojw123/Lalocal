package com.lalocal.lalocal.live.entertainment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.log.LogUtil;
import com.lalocal.lalocal.live.entertainment.agora.openlive.ConstantApp;
import com.lalocal.lalocal.live.entertainment.constant.CustomDialogStyle;
import com.lalocal.lalocal.live.entertainment.constant.GiftConstant;
import com.lalocal.lalocal.live.entertainment.constant.GiftType;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.GiftCache;
import com.lalocal.lalocal.live.entertainment.model.Gift;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.module.CustomRemoteExtensionStyle;
import com.lalocal.lalocal.live.entertainment.ui.CreateLiveRoomPopuwindow;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomLiveUserInfoDialog;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.thirdparty.live.LivePlayer;
import com.lalocal.lalocal.model.CloseLiveBean;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.model.ImgTokenResult;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.MemberOption;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qiniu.android.storage.UploadManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class LiveActivity extends LivePlayerBaseActivity implements LivePlayer.ActivityProxy, View.OnLayoutChangeListener {
    private static final String TAG = "LiveActivity";
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    private final static String CHANNELID = "CHANNELID";
    private final static String AVATAR = "AVATAR";
    public static final String LIVE_USER_ID = "LIVE_USER_ID";
    public static final String ANNOUCEMENT = "ANNOUCEMENT";
    public static final String CREATE_NICK_NAME = "CREATE_NICK_NAME";
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;
    public static final String IS_SECOND = "IS_SECOND";
    public static final String CNAME = "CNAME";

    private int screenHeight;
    private int keyHeight;

    private long lastClickTime = 0;  // 发送礼物频率控制使用
    private String userOnLineCountParameter;
    protected String channelId;
    private String createNickName;
    public String annoucement;//公告
    private String roomName;//直播室名字


    boolean isshowPopu = false;
    boolean isEnterRoom = false;
    private boolean disconnected = false; // 是否断网（断网重连用）
    private boolean isStartLive = false; // 是否开始直播推流
    boolean isClickStartLiveBtn = false; //设置直播title的popuwindow
    boolean isCancelCreama = true;//监听屏幕焦点改变，控制软键盘显示与隐藏


    private View backBtn;


    private View liveSettingLayout;//直播间底部设置栏
    protected View viewById;

    private TextView onlineCount;//在线人数
    private TextView liveOverBack;
    private TextView aucienceCount;

    private TextView overTime;
    private TextView inputLiveRoom;

    private ImageView overLiveShareFriends;
    private ImageView overLiveShareWeibo;
    private ImageView liveQuit;
    private ImageView quit;
    private ImageView playLike;
    private ImageView overLiveShareWeixin;
    private ImageView switchBtn;
    private ImageView clickPraise;//点赞



    private LinearLayout modelLayout;//用户信息layout
    private LinearLayout keyboardLayout;//自定义键盘输入

    private ViewGroup liveFinishLayout;
    // data
    private List<Gift> giftList = new ArrayList<>(); // 礼物列表数据
    private ContentLoader contentLoader;
    private SpecialShareVOBean shareVO;
    private UploadManager uploadManager;
    private Timer timer;

    private String isSecond;

    boolean isShowPopuTo = false;
    private String roomId;
    private List<LiveManagerListBean> managerList;
    private CreateLiveRoomPopuwindow createLiveRoomPopuwindow;
    private String cname;
    private long startTime;
    private long endTime;


    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_DUAL_STREAM_BROADCASTER;
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.live_player_activity;
    }

    @Override
    protected int getLayoutId() {
        return R.id.live_layout;
    }

    @Override
    protected void initParam() {

    }

    @Override
    public Activity getActivity() {
        return LiveActivity.this;
    }


    @Override
    protected void initUIandEvent() {
        super.initUIandEvent();

        if (CommonUtil.REMIND_BACK == 1) {
            contentLoader.alterLive(createNickName, channelId, null, null, null, null);
            CommonUtil.REMIND_BACK = -1;
        }
        int cRole = Constants.CLIENT_ROLE_DUAL_STREAM_BROADCASTER;
        doConfigEngine(cRole);
        if (isBroadcaster(cRole)) {
          SurfaceView  surfaceView= RtcEngine.CreateRendererView(getApplicationContext());
           RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
           layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            surfaceView.setLayoutParams(layoutParams);
          //  palyerLayout.setLayoutParams(layoutParams);
            palyerLayout.addView(surfaceView);
            rtcEngine().setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN , 0));//VideoCanvas:本地代码显示属性
            surfaceView.setZOrderOnTop(true);

            surfaceView.setZOrderMediaOverlay(true);
            worker().preview(true, surfaceView, 0);
        }

    }

    private void doConfigEngine(int cRole) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int prefIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantApp.DEFAULT_PROFILE_IDX);
        if (prefIndex > ConstantApp.VIDEO_PROFILES.length - 1) {
            prefIndex = ConstantApp.DEFAULT_PROFILE_IDX;
        }
        int vProfile = ConstantApp.VIDEO_PROFILES[prefIndex];
        //  int vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_480P;
        worker().configEngine(cRole, vProfile);
    }

    @Override
    protected void deInitUIandEvent() {
        doLeaveChannel();
        event().removeEventHandler(this);
    }
    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }
    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
        if (isBroadcaster()) {
            AppLog.i("TAG","停止视频预览。。。。");
            worker().preview(false, null, 0);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && !isshowPopu && isCancelCreama && CommonUtil.REMIND_BACK != -1) {
            isshowPopu = true;
            showCreateLiveRoomPopuwindow();

        }
    }

    //判断软键盘显示与隐藏
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            if (inputLiveRoom != null && inputLiveRoom.getVisibility() == View.VISIBLE) {
                liveSettingLayout.setVisibility(View.GONE);
                liveSettingLayout.setClickable(false);
            }
            if (keyboardLayout != null && isEnterRoom) {
                keyboardLayout.setAlpha(0);
                keyboardLayout.setClickable(false);
                liveSettingLayout.setVisibility(View.VISIBLE);
                liveSettingLayout.setClickable(true);
            }
        }
    }

    public static void start(Context context, String roomId, String url,
                             String channelId, String avatag, String liveUserId,
                             SpecialShareVOBean shareVO, String annoucement, String createNickName,
                             String isSecond, String cname) {
        Intent intent = new Intent();
        intent.setClass(context, LiveActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(CHANNELID, channelId);
        intent.putExtra(AVATAR, avatag);
        intent.putExtra(ANNOUCEMENT, annoucement);
        intent.putExtra(IS_SECOND, isSecond);
        intent.putExtra(CREATE_NICK_NAME, createNickName);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("shareVO", shareVO);
        intent.putExtras(mBundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(LIVE_USER_ID, liveUserId);
        intent.putExtra(CNAME, cname);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        viewById = findViewById(R.id.live_layout);
        uploadManager = new UploadManager();//七牛云api
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        gainIntent();
        setListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewById.addOnLayoutChangeListener(this);
        AppLog.i("TAG", "LiveActivity:onResume");

    }

    protected void onPause() {
        super.onPause();
        AppLog.i("TAG", "LiveActivity:onPause");

    }

    @Override
    protected void onDestroy() {
        AppLog.i("TAG","直播端走了onDestroy");
        giftList.clear();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishLive();
    }

    private void gainIntent() {
        channelId = getIntent().getStringExtra(CHANNELID);
        createNickName = getIntent().getStringExtra(CREATE_NICK_NAME);
        shareVO = getIntent().getParcelableExtra("shareVO");
        String s = new Gson().toJson(shareVO);
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
        isSecond = getIntent().getStringExtra(IS_SECOND);
        roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);
        cname = getIntent().getStringExtra(CNAME);
    }

    protected void findViews() {
        super.findViews();

        backBtn = findView(R.id.BackBtn);

        modelLayout = (LinearLayout) findViewById(R.id.live_telecast_model_layout);
        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);
        switchBtn = (ImageView) findViewById(R.id.live_telecast_setting);
        liveGiftImg.setVisibility(View.GONE);

        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveQuit = (ImageView) findViewById(R.id.live_quit);
        onlineCount = (TextView) findViewById(R.id.live_online_count);
        backBtn.setVisibility(View.GONE);

        modelLayout.setVisibility(View.GONE);
        keyboardLayout.setAlpha(0);
        keyboardLayout.setClickable(false);
        liveSettingLayout.setClickable(true);

        // 直播结束
        liveFinishLayout = findView(R.id.live_finish_layout);
        aucienceCount = (TextView) findViewById(R.id.live_over_audience_count);
        overTime = (TextView) findViewById(R.id.live_over_time_tv);
        quit = (ImageView) findViewById(R.id.live_telecast_quit);
        playLike = (ImageView) findViewById(R.id.live_telecast_like);
        playLike.setVisibility(View.INVISIBLE);

        //结束直播
        liveOverBack = (TextView) findViewById(R.id.live_over_back_home);
        liveOverBack.setOnClickListener(buttonClickListener);
        overLiveShareFriends = (ImageView) findViewById(R.id.live_over_share_friends);
        overLiveShareWeibo = (ImageView) findViewById(R.id.live_over_share_weibo);
        overLiveShareWeixin = (ImageView) findViewById(R.id.live_over_share_weixin);
        overLiveShareFriends.setOnClickListener(buttonClickListener);
        overLiveShareWeibo.setOnClickListener(buttonClickListener);
        overLiveShareWeixin.setOnClickListener(buttonClickListener);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                liveQuit.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                liveQuit.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    private void setListener() {
        backBtn.setOnClickListener(buttonClickListener);
        switchBtn.setOnClickListener(buttonClickListener);
        //   giftLayout.setOnClickListener(buttonClickListener);
        inputChar.setOnClickListener(buttonClickListener);
        quit.setOnClickListener(buttonClickListener);
        playLike.setOnClickListener(buttonClickListener);
        liveQuit.setOnClickListener(buttonClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        AppLog.i("TAG", "LiveActivity:onStart");
    }

    @Override
    public void onStop() {
        super.onStop();

        AppLog.i("TAG", "LiveActivity:onStop");
    }

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }

    boolean isCloseLive = false;//结束直播状态

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        AppLog.i("TAG", "加入频道回调onJoinChannelSuccess;" + channel + "   uid:" + uid + " elapsed:" + elapsed + "     config().mUid:" + config().mUid);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isShowPopuTo = true;
                isStartLive = true;
                sendLiveStatusMessgae(CustomRemoteExtensionStyle.START_LIVE_STYLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(liveSettingLayout.getWindowToken(), 0);
                isEnterRoom = true;
                if (createLiveRoomPopuwindow != null) {
                    createLiveRoomPopuwindow.dismiss();
                }
                enterRoom();
                registerObservers(true);
                modelLayout.setVisibility(View.VISIBLE);
                scoreLayout.setVisibility(View.VISIBLE);
                liveSettingLayout.setVisibility(View.VISIBLE);
                liveSettingLayout.setClickable(true);
                userOnLineCountParameter = channelId + "/onlineUsers";
                //上传在线人数
                contentLoader.getUserOnLine(userOnLineCountParameter, onlineCounts);

                if (timer == null) {
                    timer = new Timer(true);
                }
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        if (!isCloseLive) {
                            AppLog.i("TAG", "上传在线人数：" + onlineCounts);
                            contentLoader.getUserOnLine(userOnLineCountParameter, onlineCounts);
                        }
                    }
                }, 1000, 5 * 1000);
            }
        });


    }

    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onUserJoined(int uid, int elapsed) {

    }

    @Override
    public void onConnectionInterrupted() {
        AppLog.i("TAG","直播連接中斷连接中断回调");

    }

    @Override
    public void onConnectionLost() {
        AppLog.i("TAG","直播連接中斷连接丟失");
    }

    /*   CommonUtil.RESULT_DIALOG = 2;//摄像头权限
   CommonUtil.RESULT_DIALOG = 3;//音频权限*/
    @Override
    public void onError(int err) {
        AppLog.i("TAG","直播发生错误:"+err);
        if(err==1003){
            CommonUtil.RESULT_DIALOG = 2;
            finish();
        }else if(err==1018){
            CommonUtil.RESULT_DIALOG = 3;
            finish();
        }
    }

    @Override
    public void onVideoStopped() {//停止视频功能

    }

    @Override
    public void onLeaveChannel(final IRtcEngineEventHandler.RtcStats stats) {//离开频道的回调

    }


    public class MyCallBack extends ICallBack {
        @Override
        public void onAlterLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {//修改直播间
            super.onAlterLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                avatar = createLiveRoomDataResp.getResult().getUser().getAvatar();
                LiveRowsBean result = createLiveRoomDataResp.getResult();
                worker().joinChannel(cname, config().mUid);
                CommonUtil.REMIND_BACK=0;
            }
        }

        @Override
        public void onCloseLive(CloseLiveBean closeLiveBean) {
            super.onCloseLive(closeLiveBean);
            if (closeLiveBean.getReturnCode() == 0) {
                isCloseLive = true;
                AppLog.i("TAG","直播持续时间:。。。。。。。。。");

            }
        }


        @Override
        public void onImgToken(ImgTokenBean imgTokenBean) {
            super.onImgToken(imgTokenBean);
            if (imgTokenBean.getReturnCode() == 0) {
                ImgTokenResult imgToken = imgTokenBean.getResult();
                final String filename = imgToken.getFilename();
                final String token = imgToken.getToken();

            }
        }

        @Override
        public void onAlterLiveCover(CreateLiveRoomDataResp createLiveRoomDataResp) {
            super.onAlterLiveCover(createLiveRoomDataResp);
        }

        @Override
        public void onLiveManager(LiveManagerBean liveManagerBean) {
            super.onLiveManager(liveManagerBean);
        }
    }


    private void showCreateLiveRoomPopuwindow() {
        createLiveRoomPopuwindow = new CreateLiveRoomPopuwindow(this);
        createLiveRoomPopuwindow.showCreateLiveRoomPopuwindow();
        createLiveRoomPopuwindow.showAtLocation(this.findViewById(R.id.live_layout),
                Gravity.CENTER, 0, 0);
        createLiveRoomPopuwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isShowPopuTo){
                    contentLoader.cancelLiveRoom(channelId);
                    finish();
                }
            }
        });

        createLiveRoomPopuwindow.setOnSendClickListener(new CreateLiveRoomPopuwindow.OnCreateLiveListener() {
            @Override
            public void startLiveBtn(String rooName, int isShareSelector) {
                liveSettingLayout.setVisibility(View.GONE);
                if (TextUtils.isEmpty(rooName)) {
                    roomName = createNickName;
                } else {
                    roomName = rooName;
                }
                //TODO 进入直播间
                isClickStartLiveBtn = true;
                //开始直播的时间
                startTime = System.currentTimeMillis();
                if (isShareSelector == 0) {
                    liveShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                } else if (isShareSelector == 1) {
                    liveShare(SHARE_MEDIA.SINA);
                } else if (isShareSelector == 2) {
                    liveShare(SHARE_MEDIA.WEIXIN);
                }
                if (channelId != null) {
                    contentLoader.alterLive(roomName, channelId, null, null, null, null);
                } else {
                    finish();
                }
            }

            @Override
            public void closeLiveBtn() {

                if (isStartLive) {

                    finish();
                } else {
                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    clearChatRoom();
                }
                contentLoader.cancelLiveRoom(channelId);
                createLiveRoomPopuwindow.dismiss();
            }
        });
    }

    ;

    // 退出聊天室
    private void logoutChatRoom() {
        CustomChatDialog customDialog = new CustomChatDialog(getActivity());
        customDialog.setContent(getString(R.string.finish_confirm));
        customDialog.setCancelable(false);
        customDialog.setCancelBtn("结束直播", new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                endLive();
                //结束直播的时间
                endTime = System.currentTimeMillis();
            }
        });
        customDialog.setSurceBtn("继续直播", null);
        customDialog.show();
    }

    private void endLive() {
        if (timer != null) {
            timer.cancel();
        }
        contentLoader.cancelLiveRoom(channelId);
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        if (isClickStartLiveBtn) {
            inputPanel.collapse(true);// 收起软键盘
            isStartLive = false;

            drawerLayout.closeDrawer(Gravity.RIGHT);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");//初始化Formatter的转换格式。
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            String hms = formatter.format(endTime-startTime);
            overTime.setText(hms);
            aucienceCount.setText(String.valueOf(maxOnLineCount));
            liveFinishLayout.setVisibility(View.VISIBLE);

        } else {
            finish();
        }
    }

    private void sendLiveStatusMessgae(String status) {

        ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(roomId, "结束或开始直播");
        Map<String, Object> ext = new HashMap<>();
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, AuthPreferences.getUserAccount());
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            ext.put("type", chatRoomMember.getMemberType().getValue());
            ext.put("style", status);
            ext.put("creatorAccount",creatorAccount);
            ext.put("userId",userId);
            message.setRemoteExtension(ext);
        }
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false);

    }


    // 清空聊天室缓存
    private void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }


    boolean isMuteds = false;//没有禁言
    int status = -1;
    boolean isManager = false;

    @Override
    protected void showMasterInfoPopuwindow(final LiveUserInfoResultBean result1, final boolean isMuted, final String meberAccount, int id, int managerId, final List<LiveManagerListBean> managerList) {

        isMuteds = isMuted;
        Object statusa = result1.getAttentionVO().getStatus();

        if (statusa != null) {
            double parseDouble = Double.parseDouble(String.valueOf(statusa));
            status = (int) parseDouble;

        }

        if (managerId != 0) {
            isManager = true;
        } else {
            isManager = false;
        }
        CustomLiveUserInfoDialog customLiveUserInfoDialog = new CustomLiveUserInfoDialog(LiveActivity.this, result1, isManager, isMuted);
        customLiveUserInfoDialog.setCancelable(false);
        customLiveUserInfoDialog.setCancelBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
            @Override
            public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {

            }
        });
        if (CustomDialogStyle.IDENTITY == CustomDialogStyle.IS_ONESELF) {
            CustomDialogStyle.IDENTITY = CustomDialogStyle.IS_ONESELF;
            customLiveUserInfoDialog.setSurceBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                    Intent intent = new Intent(LiveActivity.this, LiveHomePageActivity.class);
                    intent.putExtra("userId", String.valueOf(id));
                    startActivity(intent);
                }

            });
        } else {
            CustomDialogStyle.IDENTITY = CustomDialogStyle.LIVEER_CHECK_ADMIN;
            customLiveUserInfoDialog.setAttention(status == 0 ? "关注" : "正在关注", new CustomLiveUserInfoDialog.CustomLiveFansOrAttentionListener() {
                int fansCounts = -2;

                @Override
                public void onCustomLiveFansOrAttentionListener(String id, TextView fansView, TextView attentionView, int fansCount, int attentionCount, TextView textView) {
                    if (fansCounts == -2) {
                        fansCounts = fansCount;
                    }
                    if (status == 0) {
                        textView.setText("正在关注");
                        textView.setAlpha(0.4f);
                        ++fansCounts;
                        fansView.setText(String.valueOf(fansCounts));
                        contentLoader.getAddAttention(id);
                        status = 1;
                    } else {
                        textView.setText("关注");
                        textView.setAlpha(1);
                        --fansCounts;
                        fansView.setText(String.valueOf(fansCounts));
                        contentLoader.getCancelAttention(id);
                        status = 0;
                    }
                    //
                }
            });
            customLiveUserInfoDialog.setReport(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {

                }
            });
            customLiveUserInfoDialog.setBanBtn(isMuteds == true ? "解除禁言" : "禁言", new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, final TextView textView, ImageView managerMark) {
                    contentLoader.checkUserIdentity(channelId, String.valueOf(id));
                    contentLoader.setCallBack(new ICallBack() {
                        @Override
                        public void onCheckManager(LiveManagerBean liveManagerBean) {
                            super.onCheckManager(liveManagerBean);
                            if (liveManagerBean.getResult() != 0) {
                                Toast.makeText(LiveActivity.this, "对方为管理员!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (isMuteds) {
                                    IMMessage banMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "解除了"+result1.getNickName()+"的禁言");
                                    ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, meberAccount);
                                    Map<String, Object> ext = new HashMap<>();
                                    if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                                        ext.put("style", "7");
                                        ext.put("type", chatRoomMember.getMemberType().getValue());
                                        ext.put("disableSendMsgUserId", meberAccount);
                                        ext.put("disableSendMsgNickName", result1.getNickName());
                                        ext.put("creatorAccount",creatorAccount);
                                        ext.put("userId",userId);
                                        banMessage.setRemoteExtension(ext);
                                    }
                                    if (banListLive.size() > 0) {
                                        for (int i = 0; i < banListLive.size(); i++) {
                                            if (meberAccount.equals(banListLive.get(i))) {
                                                banListLive.remove(i);
                                            }
                                        }
                                    }
                                    sendMessage(banMessage, MessageType.relieveBan);
                                    textView.setText("禁言");
                                    textView.setAlpha(1f);
                                    isMuteds = false;
                                } else {
                                    IMMessage banMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "禁言了"+result1.getNickName());
                                    ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, meberAccount);
                                    Map<String, Object> ext = new HashMap<>();
                                    if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                                        ext.put("style", "6");
                                        ext.put("type", chatRoomMember.getMemberType().getValue());
                                        ext.put("disableSendMsgUserId", meberAccount);
                                        ext.put("disableSendMsgNickName", result1.getNickName());
                                        ext.put("creatorAccount",creatorAccount);
                                        ext.put("userId",userId);
                                        banMessage.setRemoteExtension(ext);
                                    }
                                    banListLive.add(meberAccount);
                                    sendMessage(banMessage, MessageType.ban);

                                    textView.setText("解除禁言");
                                    textView.setAlpha(0.4f);
                                    isMuteds = true;
                                }
                            }
                        }
                    });

                }
            });
            customLiveUserInfoDialog.setManagerBtn(isManager == true ? "取消管理员" : "设为管理员", new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(final String id, final TextView textView, final ImageView managerMark) {
                    if (managerList != null && managerList.size() >= 3 && !isManager) {
                        Toast.makeText(LiveActivity.this, "设置失败，每个直播间最多设置3个管理员", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!isManager) {
                            contentLoader.liveAccreditManager(channelId, String.valueOf(id));
                            NIMClient.getService(ChatRoomService.class).markChatRoomManager(true, new MemberOption(roomId, meberAccount));
                            IMMessage banMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "授权"+result1.getNickName()+"为管理员");
                            ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, meberAccount);
                            Map<String, Object> ext = new HashMap<>();
                            if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                                ext.put("style", "8");
                                ext.put("type", chatRoomMember.getMemberType().getValue());
                                ext.put("adminSendMsgUserId", meberAccount);
                                ext.put("adminSendMsgNickName", result1.getNickName());
                                ext.put("adminSendMsgImUserId", meberAccount);
                                ext.put("creatorAccount",creatorAccount);
                                ext.put("userId",userId);
                                banMessage.setRemoteExtension(ext);
                            }
                            sendMessage(banMessage, MessageType.managerLive);
                            textView.setText("取消管理员");
                            textView.setAlpha(0.4f);
                            managerMark.setVisibility(View.VISIBLE);
                            isManager = true;
                        } else {
                            contentLoader.checkUserIdentity(channelId, String.valueOf(id));

                            NIMClient.getService(ChatRoomService.class).markChatRoomManager(false, new MemberOption(roomId, meberAccount));
                            IMMessage banMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "取消了"+result1.getNickName()+"的管理员权限");
                            ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, meberAccount);
                            Map<String, Object> ext = new HashMap<>();
                            if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                                ext.put("style", "9");
                                ext.put("type", chatRoomMember.getMemberType().getValue());
                                ext.put("adminSendMsgUserId", meberAccount);
                                ext.put("adminSendMsgNickName", result1.getNickName());
                                ext.put("adminSendMsgImUserId", meberAccount);
                                ext.put("creatorAccount",creatorAccount);
                                ext.put("userId",userId);
                                banMessage.setRemoteExtension(ext);
                            }
                            sendMessage(banMessage, MessageType.cancel);
                            textView.setText("设为管理员");
                            textView.setAlpha(1);

                            managerMark.setVisibility(View.GONE);
                            isManager = false;
                        }
                    }


                    contentLoader.setCallBack(new ICallBack() {
                        @Override
                        public void onCheckManager(LiveManagerBean liveManagerBean) {
                            super.onCheckManager(liveManagerBean);
                            int result = liveManagerBean.getResult();
                            AppLog.i("TAG", "查看是否为管理员：" + result);
                            if (result != 0) {
                                contentLoader.cancelManagerAccredit(String.valueOf(result));
                            }
                        }


                    });
                }
            });
        }
        customLiveUserInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                CustomDialogStyle.IDENTITY = CustomDialogStyle.LIVEER_CHECK_ADMIN;
            }
        });


        customLiveUserInfoDialog.show();


    }


    boolean isFirstAudio = true;

    protected void enterRoom() {
        super.enterRoom();
    }

    protected void registerObservers(boolean register) {
        super.registerObservers(register);
    }

    @Override
    protected void checkNetInfo(String netType, int reminder) {
        if ("rests".equals(netType) && reminder == 0) {
            CustomChatDialog customChatDialog = new CustomChatDialog(LiveActivity.this);
            customChatDialog.setTitle("提示");
            customChatDialog.setContent("当前网络为移动网络，是否继续直播？");
            customChatDialog.setCancelable(false);
            customChatDialog.setCancelBtn("继续直播", null);
            customChatDialog.setSurceBtn("结束直播", new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    endLive();
                }
            });
            customChatDialog.show();
        }
    }



    boolean isFirstFirendsClick = true;
    OnClickListener buttonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BackBtn:
                    finishLive();
                    break;
                case R.id.live_telecast_setting:
                    worker().getRtcEngine().switchCamera();
                    break;
                case R.id.gift_btn:
                    // showGiftLayout();
                    break;
                case R.id.gift_layout:
                    //    giftLayout.setVisibility(View.GONE);
                    break;
                case R.id.live_telecast_input_text:

                    keyboardLayout.setAlpha(1.0f);
                    keyboardLayout.setClickable(true);
                    liveSettingLayout.setVisibility(View.GONE);
                    liveSettingLayout.setClickable(false);
                    inputPanel.switchToTextLayout(true);

                    break;

                case R.id.live_telecast_like:
                    // periscopeLayout.addHeart();
                    // sendLike();
                    break;
                case R.id.live_telecast_quit:
                    finishLive();
                    break;
                case R.id.live_quit:
                    finishLive();
                    break;
                case R.id.live_over_back_home:

                    finish();
                    break;
                case R.id.live_over_share_friends:
                    overLiveShareFriends.setSelected(true);
                    overLiveShareWeibo.setSelected(false);
                    overLiveShareWeixin.setSelected(false);
                    liveShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;
                case R.id.live_over_share_weibo:
                    overLiveShareFriends.setSelected(false);
                    overLiveShareWeibo.setSelected(true);
                    overLiveShareWeixin.setSelected(false);
                    liveShare(SHARE_MEDIA.SINA);
                    break;
                case R.id.live_over_share_weixin:
                    overLiveShareFriends.setSelected(false);
                    overLiveShareWeibo.setSelected(false);
                    overLiveShareWeixin.setSelected(true);
                    liveShare(SHARE_MEDIA.WEIXIN);
                    break;
            }
        }
    };

    //分享
    private void liveShare(SHARE_MEDIA share_media) {
        ShareAction sp = new ShareAction(this);
        sp.setPlatform(share_media);
        sp.withTitle(shareVO.getTitle());
        sp.withTargetUrl(shareVO.getUrl());
        sp.withText(shareVO.getTitle());
        UMImage image = new UMImage(this, shareVO.getImg());
        sp.withMedia(image);
        sp.share();
    }


    // 发送爱心频率控制
    private boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        long time = currentTime - lastClickTime;
        if (time > 0 && time < 500) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }

    private void finishLive() {
        if (isStartLive ) {
            logoutChatRoom();
        } else {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }
    }


    protected void updateGiftList(GiftType type) {
        if (!updateGiftCount(type)) {
            giftList.add(new Gift(type, GiftConstant.titles[type.getValue()], 1, GiftConstant.images[type.getValue()]));
        }
        GiftCache.getInstance().saveGift(roomId, type.getValue());
    }

    // 更新收到礼物的数量
    private boolean updateGiftCount(GiftType type) {
        for (Gift gift : giftList) {
            if (type == gift.getGiftType()) {
                gift.setCount(gift.getCount() + 1);
                return true;
            }
        }
        return false;
    }

    /**
     * ********************************** 断网重连处理 **********************************
     */

    // 网络连接成功
    protected void onConnected() {
        if (disconnected == false) {
            return;
        }
        disconnected = false;
    }

    // 网络断开
    protected void onDisconnected() {
        LogUtil.i(TAG, "live on disconnected");
        disconnected = true;
    }


}