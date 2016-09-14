package com.lalocal.lalocal.live.entertainment.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
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
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private ImageView liveGiftImg;
    private LinearLayout giftPageVp;
    private String nickNameAudience;
    private AnimationDrawable rocketAnimation;
    protected String channelId;
    private GiftStorePopuWindow giftStorePopuWindow;
    private ContentLoader contentLoader1;


    public static void start(Context context, String roomId, String url, String avatar,
                             String nickName, String userId, SpecialShareVOBean shareVO, String type, String annoucement, String channelId) {
        Intent intent = new Intent();
        intent.setClass(context, AudienceActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(AVATAR_AUDIENCE, avatar);
        intent.putExtra(NICK_NAME_AUDIENCE, nickName);
        intent.putExtra(ANNOUCEMENT, annoucement);
        intent.putExtra(CHANNELID, channelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(PLAYER_TYPE, type);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("shareVO", shareVO);
        intent.putExtras(mBundle);
        intent.putExtra(LIVE_USER_ID, userId);
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
        nickname = getIntent().getStringExtra(NICK_NAME_AUDIENCE);
        avatar = getIntent().getStringExtra(AVATAR_AUDIENCE);
        playType = getIntent().getStringExtra(PLAYER_TYPE);
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
        nickNameAudience = getIntent().getStringExtra(NICK_NAME_AUDIENCE);
        shareVO = getIntent().getParcelableExtra("shareVO");
        channelId = getIntent().getStringExtra(CHANNELID);
    }

    private void initView() {
        viewById = findViewById(R.id.live_layout);
        loadingPage = findViewById(R.id.live_loading_page);
        audienceOver.setVisibility(View.GONE);
        andiuence = (TextView) loadingPage.findViewById(R.id.audience_over_layout);
        loadingPageLayout = (LinearLayout) loadingPage.findViewById(R.id.xlistview_header_anim);
        BlurImageView imageView = (BlurImageView) findViewById(R.id.loading_page_bg);
        imageView.setBlurImageURL(avatar);
        imageView.setBlurRadius(1);
        imageView.setScaleRatio(20);

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;

    }

    protected void enterRoom() {
        super.enterRoom();
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
                isFirstCheckNet = false;
                dialogNet = new CustomChatDialog(AudienceActivity.this);
                dialogNet.setTitle("提示");
                dialogNet.setContent("当前网络为移动网络，是否继续观看直播？");
                dialogNet.setCancelable(false);
                dialogNet.setCancelBtn("继续观看", new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {

                    }
                });
                dialogNet.setSurceBtn("结束直播", new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
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
                enterRoom();
            }
        }
    };
    boolean masterFirstEnter = true;
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

            if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();
                switch (notificationAttachment.getType()) {
                    case ChatRoomMemberIn:
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
                    case ChatRoomMemberExit:
                        String fromAccountExit = message.getFromAccount();
                        if (creatorAccount.equals(fromAccountExit)) {
                            showFinishLayout(true, 2);
                        }
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
        NEVideoView videoView = findView(R.id.video_view);
        LinearLayout videoLayout = (LinearLayout) findViewById(R.id.video_layout);
        if ("1".equals(playType)) {
            bufferStrategy = 1;
            videoView.setBufferStrategy(bufferStrategy);
            mediaType = "videoondemand";
            videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                    bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FIT, mediaType);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(videoView.getLayoutParams());
            lp.gravity = Gravity.CENTER;
            videoLayout.setBackgroundColor(Color.BLACK);
            videoView.setLayoutParams(lp);
        } else {
            bufferStrategy = 0;
            mediaType = "livestream";
            videoView.setBufferStrategy(bufferStrategy);
            videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                    bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FILL_SCALE, mediaType);
        }
        videoPlayer.openVideo();

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
        liveQuit = (ImageView) findViewById(R.id.live_quit);
        clickPraise = (ImageView) findViewById(R.id.live_telecast_like);
        quit = (ImageView) findViewById(R.id.live_telecast_quit);

        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.VISIBLE);
        liveSettingLayout.setClickable(true);
        ImageView settingBtn = (ImageView) findViewById(R.id.live_telecast_setting);
        settingBtn.setVisibility(View.GONE);
        liveGiftImg = (ImageView) findViewById(R.id.live_gift_img);
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


    protected void updateUI(String nick) {
        super.updateUI(nick);
        DrawableUtils.displayImg(this, maseterHead, avatar);
        masterName.setText(nickname);
    }

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
                    boolean logineds = UserHelper.isLogined(AudienceActivity.this);
                    if (!logineds) {
                        showLoginViewDialog();
                    } else{
                        keyboardLayout.setAlpha(1.0f);
                        keyboardLayout.setClickable(true);
                        liveSettingLayout.setVisibility(View.GONE);
                        inputPanel.switchToTextLayout(true);
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
                    boolean loginedq = UserHelper.isLogined(AudienceActivity.this);
                    if (!loginedq) {
                        showLoginViewDialog();
                    }else if(!DemoCache.getLoginChatRoomStatus()) {
                       Toast.makeText(AudienceActivity.this,"未登录聊天室",Toast.LENGTH_SHORT).show();
                    }else {
                        showGiftPage();
                    }

                    break;

            }
        }
    };

    boolean isFirstShowPlane = true;

    private void showGiftPage() {
        giftStorePopuWindow = new GiftStorePopuWindow(this, giftSresult);
        giftStorePopuWindow.showGiftStorePopuWindow();
        giftStorePopuWindow.showAtLocation(this.findViewById(R.id.live_layout),
                Gravity.BOTTOM, 0, 0);
        giftStorePopuWindow.setOnSendClickListener(new GiftStorePopuWindow.OnSendClickListener() {
            @Override
            public void sendGiftMessage(final int itemPosition, final int sendTotal, final int payBalance) {
                final String code = giftSresult.get(itemPosition).getCode();
                startSendGiftsAnimation(itemPosition, sendTotal, payBalance);
           //    contentLoader.getMyWallet();
                contentLoader.setCallBack(new ICallBack() {
                    @Override
                    public void onGetMyWallet(final WalletContent content) {
                        super.onGetMyWallet(content);
                        int gold = (int) content.getGold();
                        if (payBalance > gold) {
                            CustomChatDialog rechargeDialog = new CustomChatDialog(AudienceActivity.this);
                            rechargeDialog.setTitle("提示");
                            rechargeDialog.setContent("乐钻不足,请充值!");
                            rechargeDialog.setCancelable(false);
                            rechargeDialog.setCancelBtn("取消", null);
                            rechargeDialog.setSurceBtn("充值", new CustomChatDialog.CustomDialogListener() {
                                @Override
                                public void onDialogClickListener() {
                                    //TODO 去充值界面
                                    Intent intent=new Intent(AudienceActivity.this, RechargeActivity.class);
                                    intent.putExtra(KeyParams.WALLET_CONTENT,content);
                                    startActivity(intent);

                                }
                            });
                            rechargeDialog.show();
                        } else if (payBalance == 0) {
                            Toast.makeText(AudienceActivity.this, "您还未选中礼物!", Toast.LENGTH_SHORT).show();
                        } else {
                            contentLoader.liveSendGifts(channelId, userId, nickname, code, String.valueOf(sendTotal));
                        }
                    }

                    @Override
                    public void onSendGiftsBack(String result) {
                        super.onSendGiftsBack(result);

                        SendGiftResp sendGiftResp = new Gson().fromJson(result, SendGiftResp.class);
                        if(sendGiftResp.getReturnCode()==0){
                            startSendGiftsAnimation(itemPosition, sendTotal, payBalance);
                        }else{
                            Toast.makeText(AudienceActivity.this,"赠送失败",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


    }

    private void startSendGiftsAnimation(int itemPosition, int sendTotal, int payBalance) {
        final String code = giftSresult.get(itemPosition).getCode();

        IMMessage giftMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "给主播送了" + ("001".equals(code) ? "鲜花" : ("002".equals(code) ? "行李箱" : "飞机")));
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, AuthPreferences.getUserAccount());
        Map<String, Object> ext = new HashMap<>();
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            Map<String, Object> ext1 = new HashMap<>();
            ext.put("type", chatRoomMember.getMemberType().getValue());
            ext1.put("headImage", UserHelper.getUserAvatar(AudienceActivity.this));
            ext1.put("giftImage", giftSresult.get(itemPosition).getPhoto());
            ext1.put("giftName", giftSresult.get(itemPosition).getName());
            ext1.put("giftCount", String.valueOf(sendTotal));
            ext1.put("userId", UserHelper.getUserId(AudienceActivity.this));
            ext1.put("code", giftSresult.get(itemPosition).getCode());
            ext1.put("userName", UserHelper.getUserName(AudienceActivity.this));
            ext.put("style", "10");
            ext.put("giftModel", ext1);
            giftMessage.setRemoteExtension(ext);
        }
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
        final CustomLiveUserInfoDialog customLiveUserInfoDialog = new CustomLiveUserInfoDialog(AudienceActivity.this, result, isManager, isMuted);
        customLiveUserInfoDialog.setCancelable(false);

        Object statusa = result.getAttentionVO().getStatus();
        if (statusa != null) {
            double parseDouble = Double.parseDouble(String.valueOf(statusa));
            status = (int) parseDouble;

        }

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
            if(managerList!=null&&managerList.size()>0){
            for (LiveManagerListBean bean:managerList){

                if (bean.getId()==UserHelper.getUserId(AudienceActivity.this)){
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

                                IMMessage banMessage= ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "被管理员解除了禁言");
                                ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId,meberAccount);
                                Map<String, Object> ext = new HashMap<>();
                                if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                                    ext.put("style","7");
                                    ext.put("type", chatRoomMember.getMemberType().getValue());
                                    ext.put("disableSendMsgUserId",meberAccount);
                                    ext.put("disableSendMsgNickName",result.getNickName());
                                    banMessage.setRemoteExtension(ext);
                                }
                                if(banListLive.size()>0){
                                    for(int i=0;i<banListLive.size();i++){
                                        if(meberAccount.equals(banListLive.get(i))){
                                            banListLive.remove(i);
                                        }
                                    }
                                }
                                sendMessage(banMessage, MessageType.relieveBan);
                                textView.setText("禁言");
                                isMuteds=false;
                            } else {
                                IMMessage banMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "被管理员禁言了");
                                ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, meberAccount);
                                Map<String, Object> ext = new HashMap<>();
                                if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                                    ext.put("style", "6");
                                    ext.put("type", chatRoomMember.getMemberType().getValue());
                                    ext.put("disableSendMsgUserId", meberAccount);
                                    ext.put("disableSendMsgNickName", result.getNickName());
                                    banMessage.setRemoteExtension(ext);
                                }
                                banListLive.add(meberAccount);
                                sendMessage(banMessage, MessageType.ban);
                                textView.setText("解除禁言");
                                isMuteds = true;
                            }
                        }
                    });
                    break;
                }else{
                    CustomDialogStyle.IDENTITY = CustomDialogStyle.ME_CHECK_OTHER;
                }
            }
            }else {
                CustomDialogStyle.IDENTITY = CustomDialogStyle.ME_CHECK_OTHER;
            }
                    customLiveUserInfoDialog.setAttention(status == 0 ? "关注" : "正在关注", new CustomLiveUserInfoDialog.CustomLiveFansOrAttentionListener() {
                        int fansCounts = -2;
                        @Override
                        public void onCustomLiveFansOrAttentionListener(String id, TextView fansView, TextView attentionView, int fansCount, int attentionCount, TextView attentionStatus) {

                            if (fansCounts == -2) {
                                fansCounts = fansCount;
                            }
                            if (status == 0) {
                                attentionStatus.setText("正在关注");
                                attentionStatus.setAlpha(0.4f);
                                ++fansCounts;
                                fansView.setText(String.valueOf(fansCounts));
                                contentLoader.getAddAttention(id);
                                status = 1;
                            } else {
                                attentionStatus.setText("关注");
                                attentionStatus.setAlpha(1);
                                --fansCounts;
                                fansView.setText(String.valueOf(fansCounts));
                                contentLoader.getCancelAttention(id);
                                status = 0;
                            }
                        }
                    });
        }
        customLiveUserInfoDialog.show();
    }


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
    public void onBasicPermissionSuccess() {

        initAudienceParam();
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
            IMMessage likeMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "给主播点了个赞");
            ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, AuthPreferences.getUserAccount());
            Map<String, Object> ext = new HashMap<>();
            if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                ext.put("style", "2");
                ext.put("type", chatRoomMember.getMemberType().getValue());
                likeMessage.setRemoteExtension(ext);
            }

            sendMessage(likeMessage, MessageType.like);
        }
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
                    showMasterInfoLayout(result);
                }
            });
        }
        if (!liveEnd && !isAudienceOver) {
            isAudienceOver = true;
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
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            if (keyboardLayout != null) {
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
