package com.lalocal.lalocal.view.liveroomview.entertainment.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.liveroomview.DemoCache;
import com.lalocal.lalocal.view.liveroomview.entertainment.constant.GiftType;
import com.lalocal.lalocal.view.liveroomview.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.view.liveroomview.entertainment.module.GiftAttachment;
import com.lalocal.lalocal.view.liveroomview.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.view.liveroomview.im.config.AuthPreferences;
import com.lalocal.lalocal.view.liveroomview.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.view.liveroomview.permission.MPermission;
import com.lalocal.lalocal.view.liveroomview.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.view.liveroomview.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.view.liveroomview.thirdparty.video.NEVideoView;
import com.lalocal.lalocal.view.liveroomview.thirdparty.video.VideoPlayer;
import com.lalocal.lalocal.view.liveroomview.thirdparty.video.constant.VideoConstant;
import com.lalocal.lalocal.view.xlistview.PowerImageView;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 观众端
 * Created by hzxuwen on 2016/3/18.
 */
public class AudienceActivity extends LivePlayerBaseActivity implements VideoPlayer.VideoPlayerProxy ,View.OnLayoutChangeListener{
    private static final String TAG = AudienceActivity.class.getSimpleName();
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    public static final String  AVATAR_AUDIENCE ="AVATAR";
    public static final String NICK_NAME_AUDIENCE="NICK_NAME";
    public static final String USER = "user";
    public static final String LIVE_USER_ID="LIVE_USER_ID";
    public static final String PLAYER_TYPE="PLAYER_TYPE";
    public static final String ANNOUCEMENT="ANNOUCEMENT";


    // view

    private ImageButton shareBtn;
    private ImageButton likeBtn;

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
    private PowerImageView loadingImag;


    public static void start(Context context, String roomId, String url, String avatar, String nickName, String userId, SpecialShareVOBean shareVO, String type,String annoucement) {
        Intent intent = new Intent();
        intent.setClass(context, AudienceActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(AVATAR_AUDIENCE,avatar);
        intent.putExtra(NICK_NAME_AUDIENCE,nickName);
        intent.putExtra(ANNOUCEMENT,annoucement);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(PLAYER_TYPE,type);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("shareVO",shareVO);
        intent.putExtras(mBundle);
        intent.putExtra(LIVE_USER_ID,userId);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAudienceParam();
        parseIntent();
        initView();
        registerObservers(true);
        loginIm();
    }

    private void loginIm() {
        if(!DemoCache.getLoginStatus()){
            String userAccount = AuthPreferences.getUserAccount();
            String userToken = AuthPreferences.getUserToken();
            if(userAccount!=null&&userToken!=null){
               loginIMServer(userAccount,userToken);
            }else {
                contentLoader.getTouristInfo();
            }
        }
    }

    protected  void parseIntent(){
        super.parseIntent();
        nickname = getIntent().getStringExtra(NICK_NAME_AUDIENCE);
        avatar = getIntent().getStringExtra(AVATAR_AUDIENCE);
        playType = getIntent().getStringExtra(PLAYER_TYPE);
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
        shareVO = getIntent().getParcelableExtra("shareVO");
    }

    private void initView() {
        viewById = findViewById(R.id.live_layout);
        loadingPage = findViewById(R.id.live_loading_page);

        audienceOver.setVisibility(View.GONE);

        andiuence = (TextView) loadingPage.findViewById(R.id.audience_over_layout);
        loadingPageLayout = (LinearLayout) loadingPage.findViewById(R.id.xlistview_header_anim);
        BlurImageView imageView= (BlurImageView) findViewById(R.id.loading_page_bg);
        imageView.setBlurImageURL(avatar);
        imageView.setBlurRadius(1);
        imageView.setScaleRatio(20);

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight /3;

    }
    protected  void enterRoom(){
        super.enterRoom();
    }
    protected  void registerObservers(boolean register){
        super.registerObservers(register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }


    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode statusCode) {
           if(statusCode==StatusCode.LOGINED){
               enterRoom();
           }
        }
    };
    boolean masterFirstEnter=true;
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
                    AppLog.i("TAG", "key:" + key + "////value:" + value);
                    if ("style".equals(key)) {
                        style = value.toString();
                    }
                }
            }
            if("100".equals(style)){
                showFinishLayout(true);
            }else if("101".equals(style)){
                 showFinishLayout(false);
               // Toast.makeText(AudienceActivity.this,"主播又进来了",Toast.LENGTH_SHORT).show();
            }
            if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();

                switch (notificationAttachment.getType()) {
                    case ChatRoomMemberIn:
                        //发送进入直播间的通知
                      /*  String fromAccount1 = message.getFromAccount();
                        if(!masterFirstEnter&&creatorAccount.equals(fromAccount1)){
                            showFinishLayout(false);
                            Toast.makeText(AudienceActivity.this,"主播回来了",Toast.LENGTH_SHORT).show();
                            masterFirstEnter=true;
                        }
                        if(creatorAccount.equals(fromAccount1)&&masterFirstEnter){
                            masterFirstEnter=false;
                            Toast.makeText(AudienceActivity.this,"主播第一次进来了",Toast.LENGTH_SHORT).show();
                        }*/
                        break;
                    case ChatRoomClose:
                        //直播间被关闭；
                        showFinishLayout(true);
                        AppLog.i("TAG","直播间被关闭");
                        break;
                    case ChatRoomMemberExit:

                      /*  String fromAccount = message.getFromAccount();
                        if(creatorAccount.equals(fromAccount)){
                            showFinishLayout(true);
                            Toast.makeText(AudienceActivity.this,"主播离开了",Toast.LENGTH_SHORT).show();
                        }*/
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
        if(videoPlayer != null) {
            videoPlayer.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 暂停播放
        if(videoPlayer != null) {
            videoPlayer.onActivityPause();
        }
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        if (videoPlayer != null) {
            videoPlayer.resetVideo();

        }
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
        customDialog.setTitle( getString(R.string.finish_confirm));
        customDialog.setCancelable(false);
        customDialog.setCancelBtn(getString(R.string.cancel),null);
        customDialog.setSurceBtn( getString(R.string.confirm),new CustomChatDialog.CustomDialogListener() {
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

    /******************************** 初始化 *******************************/

    // 初始化播放器参数
    protected void initParam() {
        requestBasicPermission(); // 申请APP基本权限
    }


    int bufferStrategy=-1;
    String mediaType;
    private void initAudienceParam() {
        NEVideoView videoView = findView(R.id.video_view);
        LinearLayout videoLayout= (LinearLayout) findViewById(R.id.video_layout);
        if("1".equals(playType)){
            bufferStrategy=1;
            videoView.setBufferStrategy(bufferStrategy);
            mediaType="videoondemand";
            videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                    bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FIT,mediaType);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(videoView.getLayoutParams());
            lp.gravity= Gravity.CENTER;
            videoLayout.setBackgroundColor(Color.BLACK);
            videoView.setLayoutParams(lp);
        }else {
            bufferStrategy=0;
            mediaType="livestream";
            videoView.setBufferStrategy(bufferStrategy);
            videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                    bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FILL_SCALE,mediaType);
        }
        videoPlayer.openVideo();

        videoView.setOnErrorListener(new NELivePlayer.OnErrorListener() {
            @Override
            public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
                loadingPageLayout.setVisibility(View.GONE);
                CustomChatDialog customDialog = new CustomChatDialog(AudienceActivity.this);
                customDialog.setTitle("视频连接失败!");
                customDialog.setCancelable(false);
                customDialog.setCancelBtn("退出直播间", new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                        clearChatRoom();
                    }
                });
                customDialog.setSurceBtn( "重新连接",new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        if (videoPlayer != null) {
                            videoPlayer.resetVideo();
                        }
                        initAudienceParam();
                    }
                });
                customDialog.show();
                return false;
            }
        });
        videoView.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer neLivePlayer) {
                loadingPage.setVisibility(View.GONE);
            }
        });

    }
    protected void findViews() {
        super.findViews();
        controlLayout = findView(R.id.control_layout);
        liveQuit = (ImageView) findViewById(R.id.live_quit);
        shareBtn = findView(R.id.share_btn);
        shareBtn.setVisibility(View.GONE);
        likeBtn = findView(R.id.like_btn);
        clickPraise = (ImageView) findViewById(R.id.live_telecast_like);
        quit = (ImageView) findViewById(R.id.live_telecast_quit);
        likeBtn.setVisibility(View.GONE);
        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.VISIBLE);
        liveSettingLayout.setClickable(true);
        ImageView settingBtn= (ImageView) findViewById(R.id.live_telecast_setting);
        settingBtn.setVisibility(View.GONE);
        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);
        audienceOver = findViewById(R.id.audience_over);
        blurImageView = (BlurImageView) audienceOver.findViewById(R.id.audience_over_bg);


        keyboardLayout.setAlpha(0);
        keyboardLayout.setFocusable(false);
        keyboardLayout.setClickable(false);
        shareBtn.setOnClickListener(buttonClickListener);
        giftBtn.setOnClickListener(buttonClickListener);
        likeBtn.setOnClickListener(buttonClickListener);
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

  /*  // 初始化礼物布局
    protected void findGiftLayout() {
        super.findGiftLayout();
        sendGiftBtn = findView(R.id.send_gift_btn);
        sendGiftBtn.setOnClickListener(buttonClickListener);

        adapter = new GiftAdapter(this);
        giftView.setAdapter(adapter);

        giftLayout.setOnClickListener(buttonClickListener);
        giftView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                giftPosition = position;
            }
        });

    }
*/
    protected  void updateUI(String nick){
        super.updateUI( nick);
        DrawableUtils.displayImg(this, maseterHead,avatar);
        masterName.setText(nickname);
    }

    private View.OnClickListener buttonClickListener =  new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.share_btn:
                    break;
                case R.id.gift_btn:
                    showGiftLayout();
                    break;
                case R.id.like_btn:

                    break;
                case R.id.gift_layout:
                    giftLayout.setVisibility(View.GONE);
                    giftPosition = -1;
                    break;
                case R.id.send_gift_btn:
                    sendGift();
                    break;
                case R.id.live_telecast_like:

                    boolean logined = UserHelper.isLogined(AudienceActivity.this);
                    if(!logined){
                        showLoginViewDialog();
                    }else {
                        int[] locations = new int[2];
                        v.getLocationOnScreen(locations);
                        int x = locations[0];//获取组件当前位置的横坐标
                        int y = locations[1];
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) periscopeLayout.getLayoutParams();
                        int i = DensityUtil.dip2px(AudienceActivity.this, 70);

                        layoutParams.leftMargin=x-(i/4);
                        periscopeLayout.setLayoutParams(layoutParams);
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
                    boolean logineds = UserHelper.isLogined(AudienceActivity.this);
                    if(!logineds){
                        showLoginViewDialog();
                    }else {
                        keyboardLayout.setAlpha(1.0f);
                        keyboardLayout.setClickable(true);
                        liveSettingLayout.setVisibility(View.GONE);
                        inputPanel.switchToTextLayout(true);
                    }

                    break;

            }
        }
    };

    private void finishLive() {
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
    public void onBasicPermissionSuccess(){
        //    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        initAudienceParam();

    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed(){
        //    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
        finish();

    }

    /************************* 点赞爱心 ********************************/

    // 发送点赞爱心
    public void sendLike() {
        if (!isFastClick()) {

            IMMessage likeMessage= ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "给主播点了个赞");
            sendMessage(likeMessage,"2");

        }
    }


    // 发送爱心频率控制
    private boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        long time = currentTime - lastClickTime;
        if (time > 0 && time < 10000) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }

    /*********************** 收发礼物 ******************************/

    // 显示礼物列表
    private void showGiftLayout() {
        giftLayout.setVisibility(View.VISIBLE);
        inputPanel.collapse(true);
    }

    // 发送礼物
    private void sendGift() {
        if (giftPosition == -1) {
            //  Toast.makeText(AudienceActivity.this, "请选择礼物", Toast.LENGTH_SHORT).show();
            return;
        }
        giftLayout.setVisibility(View.GONE);
        GiftAttachment attachment = new GiftAttachment(GiftType.typeOfValue(giftPosition), 1);
        ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(roomId, attachment);
        // setMemberType(message,);
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false);

        giftAnimation.showGiftAnimation(message);
        giftPosition = -1; // 发送完毕，置空
    }

    private void setMemberType(ChatRoomMessage message, String type) {
        Map<String, Object> ext = new HashMap<>();
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, AuthPreferences.getUserAccount());
        if (chatRoomMember != null) {
            // ext.put("type", type);
            ext.put("type", chatRoomMember.getMemberType().getValue());
            message.setRemoteExtension(ext);
        }
    }

    @Override
    public boolean isDisconnected() {
        AppLog.i("TAG","直播结束监听到了。。。。。。。。。。。。。");
        return false;
    }

    /**************************** 播放器状态回调 *****************************/

    @Override
    public void onError() {
        showFinishLayout(true);
    }
    @Override
    public void onCompletion() {
        isStartLive = false;
        showFinishLayout(true);
    }
    @Override
    public void onPrepared() {
        isStartLive = true;

    }

    // 显示和隐藏直播已结束布局
    boolean isAudienceOver=true;
    protected void showFinishLayout(boolean liveEnd) {
        if(isAudienceOver&&liveEnd){
            isAudienceOver=false;
            audienceOver.setVisibility(View.VISIBLE);
            blurImageView.setBlurImageURL(avatar);
            blurImageView.setScaleRatio(20);
            blurImageView.setBlurRadius(1);
            inputPanel.collapse(true);
            contentLoader.getLiveUserInfo(userId);
             contentLoader.setCallBack(new ICallBack() {
              @Override
              public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
                  super.onLiveUserInfo(liveUserInfosDataResp);
                  LiveUserInfoResultBean result = liveUserInfosDataResp.getResult();
                  showMasterInfoPopuwindow(result,true);
              }
          });
        }
        if(!liveEnd&&!isAudienceOver){
            isAudienceOver=true;
            audienceOver.setVisibility(View.GONE);
            liveUserInfoPopuwindow.dismiss();
        }
    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            if(keyboardLayout!=null){
                keyboardLayout.setAlpha(0);
                keyboardLayout.setClickable(false);
                liveSettingLayout.setVisibility(View.VISIBLE);
                liveSettingLayout.setClickable(true);
            }

        }
    }

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }
}