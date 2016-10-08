package com.lalocal.lalocal.live.entertainment.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.RechargeActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.entertainment.constant.CustomDialogStyle;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.model.OnLineUser;
import com.lalocal.lalocal.live.entertainment.model.SendGiftResp;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomLiveUserInfoDialog;
import com.lalocal.lalocal.live.entertainment.ui.GiftStorePopuWindow;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.live.thirdparty.video.NEVideoView;
import com.lalocal.lalocal.live.thirdparty.video.VideoPlayer;
import com.lalocal.lalocal.live.thirdparty.video.constant.VideoConstant;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * 观众端
 * Created by hzxuwen on 2016/3/18.
 */
public class AudienceActivity extends LivePlayerBaseActivity implements VideoPlayer.VideoPlayerProxy, View.OnLayoutChangeListener {
    public static final String LIVE_SEARCH_ITEM = "live_search_item";
    private static final String TAG = AudienceActivity.class.getSimpleName();
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    public static final String AVATAR_AUDIENCE = "AVATAR";
    public static final String NICK_NAME_AUDIENCE = "NICK_NAME";
    public static final String USER = "user";
    public static final String LIVE_USER_ID = "LIVE_USER_ID";
    public static final String PLAYER_TYPE = "PLAYER_TYPE";
    public static final String ANNOUCEMENT = "ANNOUCEMENT";
    public static final String CHANNELID = "CHANNELID";
    public static final String CNAME="CNAME";
    public static final String STATUS="STATUS";
    // view


    private Button sendGiftBtn;

    // 播放器
    private VideoPlayer videoPlayer;
    // 发送礼物频率控制使用
    private long lastClickTime = 0;
    // 选中的礼物
    private int giftPosition = -1;

    // state
    private boolean isStartLive = false; // 推流是否开始
    private ImageView clickPraise;
    private ImageView quit;


    private View liveSettingLayout;
    private LinearLayout keyboardLayout;

    protected View viewById;
    private int screenHeight;
    private int keyHeight;

    private String nickname;
    private String playType;
    protected View loadingPage;

    protected LinearLayout loadingPageLayout;
    protected TextView andiuence;
    public String annoucement;//公告
    private SpecialShareVOBean shareVO;
    private ImageView liveQuit;
    private String style;
    private View audienceOver;
    private BlurImageView blurImageView;
    private LinearLayout backHome;
    private LinearLayout masterInfoBack;
    private int infoId;
    private LinearLayout goMasterHome;
    private CustomChatDialog dialogNet;
    private CustomChatDialog dialogConnect;

    private LinearLayout giftPageVp;
    private String nickNameAudience;
    private AnimationDrawable rocketAnimation;
    protected String channelId;
    private GiftStorePopuWindow giftStorePopuWindow;

    private NEVideoView videoView;
    private String cname;
    private String liveStatus;
    protected LiveRowsBean liveRowsBean;
    private int myGold;
    private CustomLiveUserInfoDialog customLiveUserInfoDialog;
    private Timer timerOnLine;
    private OnLineCallBack onLineCallBack;
    private ContentLoader contentLoader1;


    public static void start(Context context,LiveRowsBean liveRowsBean,String annoucement){
        Intent intent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("LiveRowsBean", liveRowsBean);
        intent.putExtras(mBundle);
        intent.setClass(context, AudienceActivity.class);
        intent.putExtra(ANNOUCEMENT, annoucement);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[] locations = new int[2];
        if (clickPraise != null && periscopeLayout != null) {//计算点赞动画的位置
            clickPraise.getLocationOnScreen(locations);
            int x = locations[0];
            int y = locations[1];
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) periscopeLayout.getLayoutParams();
            int i = DensityUtil.dip2px(AudienceActivity.this, 70);
            layoutParams.leftMargin = x - (i / 4);
            periscopeLayout.setLayoutParams(layoutParams);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAudienceParam();
        parseIntent();
        initView();
        registerObservers(true);
        loginIm();
        initData();
        postOnLineUser();
        if(onLineCallBack==null){
            onLineCallBack = new OnLineCallBack();
        }

        contentLoader1 = new ContentLoader(this);
        contentLoader1.setCallBack(onLineCallBack);
        if("0".equals(liveStatus)){
            showFinishLayout(true,2);
        }
    }

    private void postOnLineUser() {
        if (timerOnLine == null) {
            timerOnLine = new Timer(true);
        }
        timerOnLine.schedule(new TimerTask() {

            @Override
            public void run() {

                        AppLog.i("TAG", "用户获取在线人数：" + onlineCounts);
                        if (onlineCounts > 0&&contentLoader1!=null) {
                            contentLoader1.getAudienceUserOnLine(onlineCounts);
                            contentLoader1.setCallBack(onLineCallBack);
                        }

            }
        }, 1000, 2 * 1000);

    }


    private class OnLineCallBack extends ICallBack{
        @Override
        public void onGetAudienceOnLineUserCount(String json) {
            super.onGetAudienceOnLineUserCount(json);

            OnLineUser onLineUser = new Gson().fromJson(json, OnLineUser.class);
            if(onLineUser!=null&&onLineUser.getResult()>0){
                onlineCountText.setText(String.valueOf(onLineUser.getResult())+"人");
            }

        }
    }

    @Override
    protected void initUIandEvent() {
        super.initUIandEvent();
        int cRole = Constants.CLIENT_ROLE_DUAL_STREAM_AUDIENCE;
        doConfigEngine(cRole);
        if(!"1".equals(playType)){
            worker().joinChannel(cname, config().mUid);
        }
    }

    @Override
    protected void deInitUIandEvent() {
        doLeaveChannel();
        event().removeEventHandler(this);
    }


    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_DUAL_STREAM_BROADCASTER;
    }
    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }
    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
        if (isBroadcaster()) {
            worker().preview(false, null, 0);
        }
    }

    private void doConfigEngine(int cRole) {
        int vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_480P;
        switch (CustomDialogStyle.LIVE_DEFINITION) {
            case 1:
                vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_720P;
                break;
            case 2:
                vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_480P;
                break;
            case 3:
                break;
        }
        worker().configEngine(cRole, vProfile);

    }

    private void initData() {
        contentLoader.liveGiftStore();


    }





    private void loginIm() {
        if (!DemoCache.getLoginStatus()) {
            String userAccount = AuthPreferences.getUserAccount();
            String userToken = AuthPreferences.getUserToken();
            if (userAccount != null && userToken != null) {
                loginIMServer(userAccount, userToken);
            } else {
                contentLoader.getTouristInfo();
            }
        }
    }

    protected void parseIntent() {
        super.parseIntent();
        liveRowsBean = getIntent().getParcelableExtra("LiveRowsBean");
        nickname= liveRowsBean.getUser().getNickName();
        avatar= liveRowsBean.getUser().getAvatar();
        playType=String.valueOf(liveRowsBean.getType());
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
        nickNameAudience= liveRowsBean.getUser().getNickName();
        channelId=String.valueOf(liveRowsBean.getId());
        cname= liveRowsBean.getCname();
        liveStatus=String.valueOf(liveRowsBean.getStatus());
         shareVO = liveRowsBean.getShareVO();
    }



    private void initView() {
        viewById = findViewById(R.id.live_layout);
        loadingPage = findViewById(R.id.live_loading_page);
        audienceOver.setVisibility(View.GONE);
        andiuence = (TextView) loadingPage.findViewById(R.id.audience_over_layout);
        loadingPageLayout = (LinearLayout) loadingPage.findViewById(R.id.xlistview_header_anim);


        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;


    }

    protected void registerObservers(boolean register) {
        super.registerObservers(register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    boolean isShowNetDialog = true;//监测网络的dialog显示标记
    int reminder = -1;//0:网络切换，1：连接error ，2：主播离开
    boolean isFirstCheckNet = true;//弹一次提示网络的dialog

    @Override
    protected void checkNetInfo(String netType, int reminder) {

        if ("rests".equals(netType)) {
            if (reminder == 1) {
                dialogConnect.dismiss();
            }
            if (reminder == 0 && isFirstCheckNet && isAudienceOver) {
                CustomDialogStyle.NET_CHECK=1;
                isFirstCheckNet = false;
                dialogNet = new CustomChatDialog(AudienceActivity.this);
                dialogNet.setTitle("提示");
                dialogNet.setContent("当前网络为移动网络，是否继续观看直播？");
                dialogNet.setCancelable(false);
                dialogNet.setCancelBtn("继续观看", new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        CustomDialogStyle.NET_CHECK=0;
                    }
                });
                dialogNet.setSurceBtn("结束直播", new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        CustomDialogStyle.NET_CHECK=0;
                        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                        clearChatRoom();
                    }
                });
                dialogNet.show();
            }

        }
    }


    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            if (statusCode == StatusCode.LOGINED) {
              // enterRoom();
            }
        }
    };
    boolean masterFirstEnter = true;
    boolean audienceOnLineCountsChange=true;
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {

        private String style;

        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            IMMessage message = messages.get(0);

            Map<String, Object> remoteExtension = message.getRemoteExtension();

            if (remoteExtension != null) {
                Iterator<Map.Entry<String, Object>> iterator = remoteExtension.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> next = iterator.next();
                    String key = next.getKey();
                    Object value = next.getValue();
                    if ("style".equals(key)) {
                        style = value.toString();
                    }
                }
            }

            if("11".equals(style)){
                AppLog.i("TAG","用户端接受到主播结束的消息");
                showFinishLayout(true, 2);
            }

            if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();
                switch (notificationAttachment.getType()) {
                    case ChatRoomMemberIn:
                        audienceOnLineCountsChange=true;

                        String fromAccountIn = message.getFromAccount();
                        MsgStatusEnum status = message.getStatus();
                        if (creatorAccount.equals(fromAccountIn)) {
                            showFinishLayout(false, 2);
                            AppLog.i("TAG", "主播回来了。。。。。。。。");
                        }
                        break;
                    case ChatRoomClose:
                        //直播间被关闭；
                        showFinishLayout(true, 2);
                        AppLog.i("TAG", "直播间被关闭");
                        break;
                    case ChatRoomMemberExit://主播退出房间监听
                        audienceOnLineCountsChange=true;

                     /*   String fromAccountExit = message.getFromAccount();
                        if (creatorAccount.equals(fromAccountExit)) {
                            showFinishLayout(true, 2);
                        }*/
                        break;
                    case ChatRoomManagerRemove:

                        break;
                    case ChatRoomManagerAdd:

                        break;
                }
            }
        }
    };

    @Override
    protected int getActivityLayout() {
        return R.layout.audience_activity;
    }

    @Override
    protected int getLayoutId() {
        return R.id.live_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewById.addOnLayoutChangeListener(this);
        // 恢复播放
        if (videoPlayer != null) {
            videoPlayer.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停播放
        if (videoPlayer != null) {
            videoPlayer.onActivityPause();
        }
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        if (videoPlayer != null) {

            videoPlayer.resetVideo();
        }
        deInitUIandEvent();
        registerObservers(false);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishLive();
    }

    // 离开聊天室
    private void logoutChatRoom() {
        CustomChatDialog customDialog = new CustomChatDialog(this);
        customDialog.setContent(getString(R.string.finish_confirm));
        customDialog.setCancelable(false);
        customDialog.setCancelBtn(getString(R.string.cancel), null);
        customDialog.setSurceBtn(getString(R.string.confirm), new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                clearChatRoom();
            }
        });
        customDialog.show();

    }

    private void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }

    @Override
    protected void onConnected() {

    }

    @Override
    protected void onDisconnected() {

    }

    /********************************
     * 初始化
     *******************************/

    // 初始化播放器参数
    protected void initParam() {
        requestBasicPermission(); // 申请APP基本权限
    }


    int bufferStrategy = -1;
    String mediaType;
    boolean isFirstLink = true;//标记第一次重新连接，再次连接就显示主播休息了的界面

    private void initAudienceParam() {

        if ("1".equals(playType)) {
            videoView = new NEVideoView(this);
            // 防止软键盘挤压屏幕
            videoView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if(palyerLayout!=null&&palyerLayout.getChildAt(0)!=null){
                        if(bottom>oldBottom){
                            palyerLayout.getChildAt(0).layout(left,top,right,bottom);
                        }else {
                            palyerLayout.getChildAt(0).layout(oldLeft,oldTop,oldRight,oldBottom);
                        }
                    }
                }
            });
            palyerLayout.addView(videoView);
            bufferStrategy = 1;
            videoView.setBufferStrategy(bufferStrategy);
            mediaType = "videoondemand";
            videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                    bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FIT, mediaType);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(videoView.getLayoutParams());
            palyerLayout.setBackgroundColor(Color.BLACK);
            videoView.setLayoutParams(lp);
            videoPlayer.openVideo();
            errorListener();
        }/* else if("0".equals(playType)){

            bufferStrategy = 0;
            mediaType = "livestream";
            videoView.setBufferStrategy(bufferStrategy);
            videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                    bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FILL_SCALE, mediaType);
        }*/



    }

    private void errorListener() {
        if(videoView==null){
            return;
        }
/*
        videoView.setOnErrorListener(new NELivePlayer.OnErrorListener() {
            @Override
            public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
                if (reminder == 0) {
                    dialogNet.dismiss();
                }
                if (isFirstLink && isAudienceOver) {
                    isFirstLink = false;
                    reminder = 1;
                    loadingPageLayout.setVisibility(View.GONE);
                    dialogConnect = new CustomChatDialog(AudienceActivity.this);
                    dialogConnect.setContent("视频连接失败!");
                    dialogConnect.setCancelable(false);
                    dialogConnect.setCancelBtn("退出直播间", new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                            clearChatRoom();
                        }
                    });
                    dialogConnect.setSurceBtn("重新连接", new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            loadingPageLayout.setVisibility(View.VISIBLE);
                            if (videoPlayer != null) {
                                videoPlayer.resetVideo();
                            }
                            initAudienceParam();
                        }
                    });
                    dialogConnect.show();
                } else {
                    showFinishLayout(true, 2);
                }
                return false;
            }
        });*/

        videoView.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer neLivePlayer) {
                loadingPage.setVisibility(View.GONE);
            }
        });



    }

    protected void findViews() {
        super.findViews();

        liveQuit = (ImageView) findViewById(R.id.live_quit);
        clickPraise = (ImageView) findViewById(R.id.live_telecast_like);
        quit = (ImageView) findViewById(R.id.live_telecast_quit);
        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.VISIBLE);
        liveSettingLayout.setClickable(true);
        ImageView settingBtn = (ImageView) findViewById(R.id.live_telecast_setting);
        settingBtn.setVisibility(View.GONE);

        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);
        audienceOver = findViewById(R.id.audience_over);
        backHome = (LinearLayout) findViewById(R.id.master_info_back_home);
        blurImageView = (BlurImageView) audienceOver.findViewById(R.id.audience_over_bg);

        backHome.setOnClickListener(buttonClickListener);
        keyboardLayout.setAlpha(0);
        keyboardLayout.setFocusable(false);
        keyboardLayout.setClickable(false);
        liveGiftImg.setOnClickListener(buttonClickListener);


        clickPraise.setOnClickListener(buttonClickListener);
        quit.setOnClickListener(buttonClickListener);
        inputChar.setOnClickListener(buttonClickListener);
        liveQuit.setOnClickListener(buttonClickListener);





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


    boolean giftStoreFirstClick=true;
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.gift_btn:
                    showGiftLayout();
                    break;

                case R.id.live_telecast_like:

                    boolean logined = UserHelper.isLogined(AudienceActivity.this);
                    if (!logined) {
                        showLoginViewDialog();
                    } else {
                        periscopeLayout.addHeart();
                        sendLike();
                    }

                    break;
                case R.id.live_telecast_quit:
                    finishLive();
                    break;
                case R.id.live_quit:
                    finishLive();
                    break;
                case R.id.live_telecast_input_text:
//                    boolean logineds = UserHelper.isLogined(AudienceActivity.this);
//                    if (!logineds) {
//                        showLoginViewDialog();
//                    } else{
//                        keyboardLayout.setAlpha(1.0f);
//                        keyboardLayout.setClickable(true);
//                        liveSettingLayout.setVisibility(View.GONE);
//                        inputPanel.switchToTextLayout(true);
//                    }
                    boolean logineds = UserHelper.isLogined(AudienceActivity.this);
                    if (!logineds) {
                        showLoginViewDialog();
                    } else if(DemoCache.getLoginChatRoomStatus()){
                        keyboardLayout.setAlpha(1.0f);
                        keyboardLayout.setClickable(true);
                        liveSettingLayout.setVisibility(View.GONE);

                        inputPanel.switchToTextLayout(true);
                    }else {
                        Toast.makeText(AudienceActivity.this,"没有登录聊天室，请退出重进!",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.master_info_back_home:
                    finishLive();
                    break;
                case R.id.go_master_home:
                    Intent intent = new Intent(AudienceActivity.this, LiveHomePageActivity.class);
                    intent.putExtra("userId", String.valueOf(infoId));
                    startActivity(intent);
                    break;
                case R.id.live_gift_img:

                    if(giftStoreFirstClick){
                        giftStoreFirstClick=false;

                    boolean loginedq = UserHelper.isLogined(AudienceActivity.this);
                    if (!loginedq) {
                        showLoginViewDialog();
                        giftStoreFirstClick=true;
                    }else if(!DemoCache.getLoginChatRoomStatus()) {
                       Toast.makeText(AudienceActivity.this,"未登录聊天室",Toast.LENGTH_SHORT).show();
                    } else if(!"1".equals(playType)) {

                        if(CustomDialogStyle.IS_FIRST_CLICK_PAGE){
                            CustomDialogStyle.IS_FIRST_CLICK_PAGE=false;
                            contentLoader.getMyWallet();
                            contentLoader.setCallBack(new ICallBack(){
                                @Override
                                public void onGetMyWallet(WalletContent content) {
                                    super.onGetMyWallet(content);
                                    myGold = (int)content.getGold();
                                    AppLog.i("TAG","我的乐钻石："+myGold);
                                    showGiftPage(myGold);
                                }
                            } );
                        }else {
                            showGiftPage(myGold);
                        }
                    }
                    }
                    break;

            }
        }
    };

    boolean isFirstShowPlane = true;

    private void showGiftPage(int gold) {
        liveSettingLayout.setVisibility(View.GONE);
        giftStorePopuWindow = new GiftStorePopuWindow(this, giftSresult);
        giftStorePopuWindow.showGiftStorePopuWindow(gold);
        giftStorePopuWindow.showAtLocation(this.findViewById(R.id.live_layout),
                Gravity.BOTTOM, 0, 0);
        giftStorePopuWindow.setOnSendClickListener(new GiftStorePopuWindow.OnSendClickListener() {
            @Override
            public void sendGiftMessage(final int itemPosition, final int sendTotal, final int payBalance) {

                final  int id = giftSresult.get(itemPosition).getId();
                contentLoader.getMyWallet();
//                startSendGiftsAnimation(itemPosition, sendTotal, payBalance);
                contentLoader.setCallBack(new ICallBack() {
                    @Override
                    public void onGetMyWallet(final WalletContent content) {
                        super.onGetMyWallet(content);
                        int gold = (int) content.getGold();
                        if (payBalance > gold) {
                            final CustomChatDialog rechargeDialog = new CustomChatDialog(AudienceActivity.this);
                            rechargeDialog.setTitle("提示");
                            rechargeDialog.setContent("乐钻不足,请充值!");
                            rechargeDialog.setCancelable(false);
                            rechargeDialog.setCancelBtn("取消", null);

                            rechargeDialog.setSurceBtn("充值", new CustomChatDialog.CustomDialogListener() {
                                @Override
                                public void onDialogClickListener() {
                                  CustomDialogStyle.IS_FIRST_CLICK_PAGE=true;
                                    Intent intent = new Intent(AudienceActivity.this, RechargeActivity.class);
                                    intent.putExtra(KeyParams.WALLET_CONTENT, content);
                                    startActivity(intent);
                                    giftStorePopuWindow.dismiss();
                                }
                            });
                            rechargeDialog.show();
                        }  else {
                            contentLoader.liveSendGifts(channelId, userId, nickname, id, String.valueOf(sendTotal));
                        }
                    }

                    @Override
                    public void onSendGiftsBack(String result) {
                        super.onSendGiftsBack(result);

                        SendGiftResp sendGiftResp = new Gson().fromJson(result, SendGiftResp.class);
                        if (sendGiftResp.getReturnCode() == 0) {
                            startSendGiftsAnimation(itemPosition, sendTotal, payBalance);
                            myGold=myGold-payBalance;
                        } else {
                            Toast.makeText(AudienceActivity.this, "赠送失败", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });


        giftStorePopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                liveSettingLayout.setVisibility(View.VISIBLE);
                giftStoreFirstClick=true;
            }
        });

    }

    private void startSendGiftsAnimation(int itemPosition, int sendTotal, int payBalance) {
        final String code = giftSresult.get(itemPosition).getCode();
        AppLog.i("TAG", "startSendGiftsAnimation:" + code);
        String messageContent= "给主播送了" + ("001".equals(code) ? "鲜花" : ("002".equals(code) ? "行李箱" : ("003".equals(code)?"飞机":"神秘礼物")));
        LiveMessage liveMessage=new LiveMessage();
        GiftBean giftBean=new GiftBean();
        giftBean.setUserName(UserHelper.getUserName(AudienceActivity.this));
        giftBean.setUserId(String.valueOf(UserHelper.getUserId(AudienceActivity.this)));
        giftBean.setCode(giftSresult.get(itemPosition).getCode());
        giftBean.setGiftCount(sendTotal);
        giftBean.setGiftImage(giftSresult.get(itemPosition).getPhoto());
        giftBean.setGiftName(giftSresult.get(itemPosition).getName());
       giftBean.setHeadImage(UserHelper.getUserAvatar(AudienceActivity.this));
        liveMessage.setGiftModel(giftBean);
        liveMessage.setStyle("10");
        liveMessage.setChannelId(channelId);
        IMMessage giftMessage = SendMessageUtil.sendMessage(container.account, messageContent, roomId, AuthPreferences.getUserAccount(), liveMessage);

        if ("003".equals(code)) {

            giftPlaneAnimation.showPlaneAnimation((ChatRoomMessage) giftMessage);

        } else {

            giftAnimation.showGiftAnimation((ChatRoomMessage) giftMessage);
        }
        sendMessage(giftMessage, MessageType.gift);
        giftStorePopuWindow.dismiss();

    }

    int status = -1;
    boolean isManager = false;
    boolean isMuteds = false;

    protected void showMasterInfoPopuwindow(final LiveUserInfoResultBean result, boolean isMuted, final String meberAccount, int id, int managerId, List<LiveManagerListBean> managerList) {

        isMuteds = isMuted;
        if (managerId != 0) {
            isManager = true;

        } else {
            isManager = false;
        }
        customLiveUserInfoDialog = new CustomLiveUserInfoDialog(AudienceActivity.this, result, isManager, isMuted);
        customLiveUserInfoDialog.setCancelable(false);

        Object statusa = result.getAttentionVO().getStatus();
        if (statusa != null) {
            double parseDouble = Double.parseDouble(String.valueOf(statusa));
            status = (int) parseDouble;

        }

        customLiveUserInfoDialog.setUserHomeBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
            @Override
            public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                Intent intent = new Intent(AudienceActivity.this, LiveHomePageActivity.class);
                intent.putExtra("userId", String.valueOf(id));
                startActivity(intent);
            }
        });

        if (CustomDialogStyle.IDENTITY == CustomDialogStyle.IS_ONESELF || CustomDialogStyle.IDENTITY == CustomDialogStyle.IS_LIVEER) {

            customLiveUserInfoDialog.setSurceBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                    Intent intent = new Intent(AudienceActivity.this, LiveHomePageActivity.class);
                    intent.putExtra("userId", String.valueOf(id));
                    startActivity(intent);
                }
            });


        } else {
            if (managerList != null && managerList.size() > 0) {
                for (LiveManagerListBean bean : managerList) {

                    if (bean.getId() == UserHelper.getUserId(AudienceActivity.this)) {
                        customLiveUserInfoDialog.setReport(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                            @Override
                            public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {

                            }
                        });

                        CustomDialogStyle.IDENTITY = CustomDialogStyle.MANAGER_IS_ME;
                        customLiveUserInfoDialog.setBanBtn(isMuteds == true ? "解除禁言" : "禁言", new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                            @Override
                            public void onCustomLiveUserInfoDialogListener(String id, final TextView textView, ImageView managerMark) {
                                if (isMuteds) {
                                   String messageContent="解除了"+result.getNickName()+"的禁言";
                                    LiveMessage liveMessage=new LiveMessage();
                                    liveMessage.setStyle("7");
                                    liveMessage.setDisableSendMsgNickName(result.getNickName());
                                    liveMessage.setDisableSendMsgUserId(String.valueOf(result.getId()));
                                    liveMessage.setUserId(userId);
                                    liveMessage.setCreatorAccount(creatorAccount);
                                    liveMessage.setChannelId(channelId);
                                    IMMessage imMessage = SendMessageUtil.sendMessage(container.account, messageContent, roomId, meberAccount, liveMessage);

                                    if (banListLive.size() > 0) {
                                        for (int i = 0; i < banListLive.size(); i++) {
                                            if (meberAccount.equals(banListLive.get(i))) {
                                                banListLive.remove(i);
                                            }
                                        }
                                    }
                                    sendMessage(imMessage, MessageType.relieveBan);
                                    textView.setText("禁言");
                                    isMuteds = false;
                                } else {

                                    String messageContent="禁言了"+result.getNickName();
                                    LiveMessage liveMessage=new LiveMessage();
                                    liveMessage.setStyle("6");
                                    liveMessage.setDisableSendMsgNickName(result.getNickName());
                                    liveMessage.setDisableSendMsgUserId(String.valueOf(result.getId()));
                                    liveMessage.setUserId(userId);
                                    liveMessage.setCreatorAccount(creatorAccount);
                                    liveMessage.setChannelId(channelId);
                                    IMMessage imMessage = SendMessageUtil.sendMessage(container.account, messageContent, roomId, meberAccount, liveMessage);

                                    banListLive.add(meberAccount);
                                    sendMessage(imMessage, MessageType.ban);
                                    textView.setText("解除禁言");
                                    isMuteds = true;
                                }
                            }
                        });
                        break;
                    } else {
                        CustomDialogStyle.IDENTITY = CustomDialogStyle.ME_CHECK_OTHER;
                    }
                }
            } else {
                CustomDialogStyle.IDENTITY = CustomDialogStyle.ME_CHECK_OTHER;
            }

        }
        customLiveUserInfoDialog.setAttention(status == 0 ? getString(R.string.live_attention):getString(R.string.live_attention_ok), new CustomLiveUserInfoDialog.CustomLiveFansOrAttentionListener() {
            int fansCounts = -2;

            @Override
            public void onCustomLiveFansOrAttentionListener(String id, TextView fansView, TextView attentionView, int fansCount, int attentionCount, TextView attentionStatus) {

                if (fansCounts == -2) {
                    fansCounts = fansCount;
                }
                if (status == 0) {
                    attentionStatus.setText(getString(R.string.live_attention_ok));
                    attentionStatus.setAlpha(0.4f);
                    ++fansCounts;
                    fansView.setText(String.valueOf(fansCounts));
                    contentLoader.getAddAttention(id);
                    status = 1;
                } else {
                    attentionStatus.setText(getString(R.string.live_attention));
                    attentionStatus.setAlpha(1);
                    --fansCounts;
                    fansView.setText(String.valueOf(fansCounts));
                    contentLoader.getCancelAttention(id);
                    status = 0;
                }
            }
        });
        customLiveUserInfoDialog.show();

        customLiveUserInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                CustomDialogStyle.USER_INFO_FIRST_CLICK=true;
            }
        });
    }


    private void finishLive() {

        if (videoPlayer != null) {
            videoPlayer.resetVideo();
        }

        if (isStartLive) {
            logoutChatRoom();
        } else {

            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }

    }

    /**
     * 基本权限管理
     */
    private void requestBasicPermission() {
        MPermission.with(AudienceActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.READ_PHONE_STATE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {

//        initAudienceParam();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        finish();
    }

    /*************************
     * 点赞爱心
     ********************************/

    // 发送点赞爱心
    public void sendLike() {
        if (!isFastClick()) {
            LiveMessage liveMessage=new LiveMessage();
            liveMessage.setStyle("2");
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "给主播点了个赞", roomId, AuthPreferences.getUserAccount(), liveMessage);

            sendMessage(imMessage, MessageType.like);
        }
    }


    // 发送爱心频率控制
    private boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        long time = currentTime - lastClickTime;
        if (time > 0 && time < 2000) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }

    /***********************
     * 收发礼物
     ******************************/

    // 显示礼物列表
    private void showGiftLayout() {
        giftLayout.setVisibility(View.VISIBLE);
        inputPanel.collapse(true);
    }

    @Override
    public boolean isDisconnected() {
        return false;
    }

    /****************************
     * 播放器状态回调
     *****************************/

    @Override
    public void onError() {
        showFinishLayout(true, 2);
    }

    @Override
    public void onCompletion() {
        isStartLive = false;
        showFinishLayout(true, 2);
    }

    @Override
    public void onPrepared() {
        isStartLive = true;

    }

    // 显示和隐藏直播已结束布局
    boolean isAudienceOver = true;

    protected void showFinishLayout(boolean liveEnd, int reminder) {

        if (reminder == 0) {
            dialogNet.dismiss();
        } else if (reminder == 1) {
            dialogConnect.dismiss();
        }
        if (isAudienceOver && liveEnd) {
            isAudienceOver = false;
            palyerLayout.removeAllViews();
            audienceOver.setVisibility(View.VISIBLE);
            blurImageView.setBlurImageURL(avatar);
            blurImageView.setScaleRatio(20);
            blurImageView.setBlurRadius(1);
            if(inputPanel!=null){
                inputPanel.collapse(true);
            }
            if(giftStorePopuWindow!=null){
                giftStorePopuWindow.dismiss();
            }
            if(giftsRankPopuWindow!=null){
                giftsRankPopuWindow.dismiss();
            }
            if(customLiveUserInfoDialog!=null){
                customLiveUserInfoDialog.dismiss();
            }
            contentLoader.getLiveUserInfo(userId);
            contentLoader.setCallBack(new ICallBack() {
                @Override
                public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
                    super.onLiveUserInfo(liveUserInfosDataResp);
                    LiveUserInfoResultBean result = liveUserInfosDataResp.getResult();
                    showMasterInfoLayout(result);
                }
            });
        }
        if (!liveEnd && !isAudienceOver) {
            isAudienceOver = true;
            loadingPage.setVisibility(View.GONE);
            audienceOver.setVisibility(View.GONE);

        }
    }

    private void showMasterInfoLayout(LiveUserInfoResultBean result) {
        infoId = result.getId();
        masterInfoCloseIv = (ImageView) audienceOver.findViewById(R.id.custom_dialog_close_iv);
        masterInfoCloseIv.setVisibility(View.INVISIBLE);
        masterInfoHeadIv = (CircleImageView) audienceOver.findViewById(R.id.master_info_head_iv);
        masterInfoNickTv = (TextView) audienceOver.findViewById(R.id.master_info_nick_tv);
        masterInfoSignature = (TextView) audienceOver.findViewById(R.id.master_info_signature);
        liveAttention = (TextView) audienceOver.findViewById(R.id.live_attention);
        masterInfoBack = (LinearLayout) audienceOver.findViewById(R.id.master_info_back_home);
        liveFans = (TextView) audienceOver.findViewById(R.id.live_fans);
        liveContribute = (TextView) audienceOver.findViewById(R.id.live_contribute);
        goMasterHome = (LinearLayout) audienceOver.findViewById(R.id.go_master_home);
        liveMasterHome = (TextView) audienceOver.findViewById(R.id.live_master_home);
        masterInfoCloseIv.setOnClickListener(buttonClickListener);
        liveMasterHome.setOnClickListener(buttonClickListener);
        goMasterHome.setOnClickListener(buttonClickListener);
        masterInfoBack.setOnClickListener(buttonClickListener);
        String avatar = result.getAvatar();
        String nickName = result.getNickName();
        int fansNum = result.getFansNum();
        int attentionNum = result.getAttentionNum();
        String description = result.getDescription();
        liveFans.setText(String.valueOf(fansNum));
        liveAttention.setText(String.valueOf(attentionNum));
        if (!TextUtils.isEmpty(description)) {
            masterInfoSignature.setText(description);
        }
        DrawableUtils.displayImg(AudienceActivity.this, masterInfoHeadIv, avatar);
        masterInfoNickTv.setText(nickName);
        liveMasterHome.setOnClickListener(buttonClickListener);
    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {


        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {//显示
            if(liveSettingLayout!=null&&keyboardLayout!=null){
                keyboardLayout.setAlpha(1);
                keyboardLayout.setClickable(true);
                keyboardLayout.setVisibility(View.VISIBLE);
                liveSettingLayout.setVisibility(View.INVISIBLE);
                liveSettingLayout.setClickable(false);
            }
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//隐藏
            if (keyboardLayout != null) {
                keyboardLayout.setAlpha(0);
                keyboardLayout.setClickable(false);
                keyboardLayout.setVisibility(View.INVISIBLE);
                liveSettingLayout.setVisibility(View.VISIBLE);
                liveSettingLayout.setClickable(true);
            }
        }
    }

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

        doRenderRemoteUi(uid);


    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                int childCount = palyerLayout.getChildCount();

                if(childCount>0){
                    palyerLayout.removeAllViews();
                }
                palyerLayout.addView(surfaceV);


                surfaceV.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                        if(palyerLayout!=null&&palyerLayout.getChildAt(0)!=null){
                            if(bottom>oldBottom){
                                palyerLayout.getChildAt(0).layout(left,top,right,bottom);
                            }else {
                                palyerLayout.getChildAt(0).layout(oldLeft,oldTop,oldRight,oldBottom);
                            }
                        }
                    }
                });

                surfaceV.setZOrderOnTop(true);
                surfaceV.setZOrderMediaOverlay(true);
                Log.i("TAG","远端视频接收解码回调："+config().mUid+"   uid:"+uid);
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));//设置远端视频属性

              /*  if (config().mUid == uid) {
                        rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));//设置本地视频属性
                } else {
                    rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));//设置远端视频属性
                }*/
                loadingPage.setVisibility(View.GONE);
                if(audienceOver!=null){
                    isAudienceOver = true;
                    audienceOver.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

        CustomDialogStyle.IS_FIRST_CLICK_PAGE=true;
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onUserJoined(int uid, int elapsed) {

    }

    @Override
    public void onConnectionInterrupted() {
        AppLog.i("TAG","視頻連接中斷连接中断回调");


    }

    @Override
    public void onConnectionLost() {
        AppLog.i("TAG","視頻連接中斷连接丟失");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showFinishLayout(true,2);
            }
        });


    }

    @Override
    public void onError(int err) {
        AppLog.i("TAG","用户端视频播放错误码:"+err);

    }

    @Override
    public void onVideoStopped() {

    }

    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        AppLog.i("TAG","用户离开直播间回调:"+stats.toString());

    }

}
