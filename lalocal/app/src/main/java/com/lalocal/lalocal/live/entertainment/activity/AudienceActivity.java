package com.lalocal.lalocal.live.entertainment.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
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
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.util.ActivityManager;
import com.lalocal.lalocal.live.base.util.DialogUtil;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.ChallengeDetailsResp;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResp;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResultBean;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.model.SendGiftResp;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomUserInfoDialog;
import com.lalocal.lalocal.live.entertainment.ui.GiftStorePopuWindow;
import com.lalocal.lalocal.live.entertainment.ui.LiveAttentionPopuwindow;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.live.thirdparty.video.NEVideoView;
import com.lalocal.lalocal.live.thirdparty.video.VideoPlayer;
import com.lalocal.lalocal.live.thirdparty.video.constant.VideoConstant;
import com.lalocal.lalocal.model.LiveAttentionStatusBean;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveDetailsDataResp;
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
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * 观众端
 * Created by hzxuwen on 2016/3/18.
 * 日志聚合系统kids
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class
AudienceActivity extends LivePlayerBaseActivity implements VideoPlayer.VideoPlayerProxy, View.OnLayoutChangeListener, GiftStorePopuWindow.OnSendClickListener {
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    // 播放器
    private VideoPlayer videoPlayer;
    // 发送礼物频率控制使用
    private long lastClickTime = 0;
    // 选中的礼物
    private int giftPosition = -1;

    // state
    private boolean isStartLive = false; // 推流是否开始
    private View liveSettingLayout;
    private LinearLayout keyboardLayout;
    protected View viewById;
    private int screenHeight;
    private int keyHeight;
    private String nickname;
    private String playType;
    protected View loadingPage;
    protected LinearLayout loadingPageLayout;
    private SpecialShareVOBean shareVO;
    private ImageView liveQuit;
    private View audienceOver;
    private CustomChatDialog dialogNet;
    private CustomChatDialog dialogConnect;
    protected String channelId;
    private GiftStorePopuWindow giftStorePopuWindow;
    private NEVideoView videoView;
    private String cname;
    private String liveStatus;
    private int myGold;
    private AudienceCallBack audienceCallBack;
    private ContentLoader contentLoaderAudience;
    protected List<GiftDataResultBean> giftSresult;
    private SurfaceView surfaceV;
    protected String roomId;

    private BlurImageView blurView;
    private LiveRowsBean liveRowsBean;
    private AudioManager audio;
    private int role;
    private ImageView audienceOverClose;
    private TextView overAttention;
    private CircleImageView headIv;
    private TextView masterName;
    private boolean isLocation = false;
    private String liveTitle;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[] locations = new int[2];
        if (quit != null && periscopeLayout != null && !isLocation) {//计算点赞动画的位置
            isLocation = true;
            quit.getLocationOnScreen(locations);
            int x = locations[0];
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) periscopeLayout.getLayoutParams();
            int i = DensityUtil.dip2px(this, 70);
            layoutParams.leftMargin = x - (i / 4);
            periscopeLayout.setLayoutParams(layoutParams);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        ActivityManager.removeAudienceCurrent();
        ActivityManager.audienceActivityStack(this);
        audienceCallBack = new AudienceCallBack();
        contentLoaderAudience = new ContentLoader(this);
        contentLoaderAudience.setCallBack(audienceCallBack);
        String id = getIntent().getStringExtra("id");
        contentLoaderAudience.liveDetails(id);
        contentLoaderAudience.liveGiftStore();
        LiveConstant.ROLE=0;
        AppLog.i("TAG", "用户端获取token:" + UserHelper.getToken(this));

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    boolean firstWarning = true;
    private int overAttentionStatus;
    private class AudienceCallBack extends ICallBack {

        @Override
        public void onLiveAttentionStatus(LiveAttentionStatusBean liveAttentionStatusBean) {
            super.onLiveAttentionStatus(liveAttentionStatusBean);
            if(liveAttentionStatusBean.getReturnCode()==0){
                LiveAttentionStatusBean.ResultBean result = liveAttentionStatusBean.getResult();
              int  userStatus = result.getStatus();
                overAttentionStatus=userStatus;
                if(userStatus==0){
                    overAttention.setText(getString(R.string.live_master_attention));
                }else if(userStatus==1){
                    overAttention.setText(getString(R.string.live_attention_ok));

                }else if(userStatus==2){
                    overAttention.setText(getString(R.string.live_attention_mutual));
                }
            }
        }

        @Override
        public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
            super.onLiveCancelAttention(liveCancelAttention);
            if(liveCancelAttention.getReturnCode()==0){
                overAttentionStatus=0;
                overAttention.setText(getString(R.string.live_master_attention));
            }
        }

        @Override
        public void onLiveDetails(LiveDetailsDataResp liveDetailsDataResp) {
            super.onLiveDetails(liveDetailsDataResp);
            if (liveDetailsDataResp.getReturnCode() == 0) {
                liveRowsBean = liveDetailsDataResp.getResult();
                nickname = liveRowsBean.getUser().getNickName();
                avatar = liveRowsBean.getUser().getAvatar();
                playType = String.valueOf(liveRowsBean.getType());
                Object ann = liveRowsBean.getAnnoucement();
                String annoucement = null;
                if (ann != null) {
                    annoucement = ann.toString();
                } else {
                    annoucement = "这是公告哈";
                }
                liveTitle = liveRowsBean.getTitle();
                channelId = String.valueOf(liveRowsBean.getId());
                cname = liveRowsBean.getCname();
                liveStatus = String.valueOf(liveRowsBean.getStatus());
                shareVO = liveRowsBean.getShareVO();
                roomId = String.valueOf(liveRowsBean.getRoomId());
                LiveConstant.ROOM_ID = roomId;
                LiveConstant.liveTitle=liveRowsBean.getTitle();
                ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
                int onlineUser = liveRowsBean.getOnlineUser();
                url = liveRowsBean.getPullUrl();
               LiveConstant.creatorAccid=liveRowsBean.getCreaterAccId();
                userId = String.valueOf(liveRowsBean.getUser().getId());
                if (avatar != null) {
                    blurView.setBlurImageURL(avatar);
                    blurView.setScaleRatio(20);
                    blurView.setBlurRadius(1);
                }
                contentLoaderAudience.getUploadLogs(new Gson().toJson(liveRowsBean));

                getParameter(liveRowsBean);
                registerObservers(true);
                initParam();
                initUIandEvent();
                if ("0".equals(liveStatus)) {
                    showFinishLayout(true, 2);
                }

                if ("1".equals(playType)) {
                    hideBtn(onlineUser);
                }
            }
        }


        @Override
        public void onGiftsStore(GiftDataResp giftDataResp) {
            if (giftDataResp.getReturnCode() == 0) {
                AppLog.i("TAG", "请求成功");
                giftSresult = giftDataResp.getResult();
            }
        }

        @Override
        public void onGetMyWallet(WalletContent content) {
            super.onGetMyWallet(content);
            if (isNeedRequestServerShowGiftStorePage) {
                myGold = (int) content.getGold();
                AppLog.i("TAG", "我的乐钻石：" + myGold);
                showGiftPage(myGold);
            }

        }

        @Override
        public void onSendGiftsBack(String result) {
            super.onSendGiftsBack(result);
            SendGiftResp sendGiftResp = new Gson().fromJson(result, SendGiftResp.class);
            SendGiftResp.ResultBean sendGiftResult = sendGiftResp.getResult();
            if (sendGiftResp.getReturnCode() == 0 && sendTotal > 0) {
                startSendGiftsAnimation(giftDataResultBean, sendTotal);
                myGold = myGold - payBalance;
            } else {
                Toast.makeText(AudienceActivity.this, getString(R.string.live_sendgift_fail), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onChallengeInitiate(ChallengeDetailsResp.ResultBean resultBean) {
            super.onChallengeInitiate(resultBean);
            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setStyle(MessageType.challenge);
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChallengeModel(resultBean);
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "发起挑战", roomId, AuthPreferences.getUserAccount(), liveMessage);
            sendMessage(imMessage, MessageType.challenge);
            if (LiveConstant.isUnDestory) {
                final CustomChatDialog customDialog = new CustomChatDialog(AudienceActivity.this);
                customDialog.setContent(getString(R.string.chanllage_initiate_hint));
                customDialog.setCancelable(false);
                customDialog.setOkBtn(getString(R.string.chanllage_initiate_ok), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        customDialog.dismiss();
                    }
                });
                customDialog.show();
            }
        }

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            if (liveUserInfosDataResp.getReturnCode() == 0) {

                LiveUserInfoResultBean result = liveUserInfosDataResp.getResult();
                Object status = result.getAttentionVO().getStatus();
                int id = result.getId();
                if (userId.equals(String.valueOf(id))) {
                    if (status != null) {
                        double parseDouble = Double.parseDouble(String.valueOf(status));
                        overAttentionStatus = (int) parseDouble;
                        if(overAttentionStatus==0){
                            overAttention.setText("关注");
                        }else{
                            overAttention.setText("已关注");
                        }
                    }
                } else if (id == UserHelper.getUserId(AudienceActivity.this)) {
                    role = result.getRole();
                    LiveConstant.ROLE = role;
                    if (role != 2) {
                        LiveConstant.ROLE = 0;
                    }

                }

            }
        }
    }

    private void hideBtn(int onlineUser) {
        liveGiftImg.setVisibility(View.GONE);
        onlineCountText.setText(String.valueOf(onlineUser));
    }

    @Override
    protected void initUIandEvent() {
        super.initUIandEvent();
        AppLog.i("TAG", "initUIandEvent 用户端");
        if (cname != null) {
            int cRole = Constants.CLIENT_ROLE_AUDIENCE;
            doConfigEngine(cRole);
            if (!"1".equals(playType)) {
                worker().joinChannel(cname, config().mUid);
            }
        }
    }

    @Override
    protected void deInitUIandEvent() {
        doLeaveChannel();
        event().removeEventHandler(this);
    }


    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_AUDIENCE;
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
        switch (LiveConstant.LIVE_DEFINITION) {
            case 1:
                vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_720P;
                AppLog.i("TAG", "用戶端視頻分辨率為720p");
                break;
            case 2:
                vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_480P;
                break;
            case 3:
                break;
        }
        worker().configEngine(cRole, vProfile);
    }

    private void loginIm() {
        if (!DemoCache.getLoginStatus()) {
            String userAccount = AuthPreferences.getUserAccount();
            String userToken = AuthPreferences.getUserToken();
            if (userAccount != null && userToken != null) {
                loginIMServer(userAccount, userToken);
            } else {
                contentLoaderAudience.getTouristInfo();
            }
        }
    }

    private void initView() {
        viewById = findViewById(R.id.live_layout);
        viewById.setOnClickListener(buttonClickListener);
        loadingPage = findViewById(R.id.live_loading_page);
        blurView = (BlurImageView) loadingPage.findViewById(R.id.loading_page_bg);
        audienceOver.setVisibility(View.GONE);

        loadingPageLayout = (LinearLayout) loadingPage.findViewById(R.id.xlistview_header_anim);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
    }

    boolean isShowNetDialog = true;//监测网络的dialog显示标记
    int reminder = -1;//0:网络切换，1：连接error ，2：主播离开
    boolean isFirstCheckNet = true;//弹一次提示网络的dialog

    @Override
    protected void checkNetInfo(String netType, int reminder) {

        if (NET_TYPE_RESTS.equals(netType)) {
            if (reminder == 1) {
                dialogConnect.dismiss();
            }
            if (reminder == 0 && isFirstCheckNet && isAudienceOver) {
                LiveConstant.NET_CHECK = 1;
                isFirstCheckNet = false;
                if (LiveConstant.isUnDestory) {
                    dialogNet = new CustomChatDialog(AudienceActivity.this);
                    dialogNet.setTitle(getString(R.string.live_hint));
                    dialogNet.setContent(getString(R.string.live_net_type_cmcc));
                    dialogNet.setCancelable(false);
                    dialogNet.setCancelBtn(getString(R.string.live_continue_look), new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            LiveConstant.NET_CHECK = 0;
                        }
                    });
                    dialogNet.setSurceBtn(getString(R.string.live_over), new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            LiveConstant.NET_CHECK = 0;
                            if(DemoCache.getLoginChatRoomStatus()){
                                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                            }

                            clearChatRoom();
                        }
                    });
                    dialogNet.show();
                }

            }

        }
    }

    boolean masterFirstEnter = true;
    boolean masterComeBack = true;
    //计算主播离开时间，若果超过30秒还未回来，就显示主播离开界面，masterComeBack：标记主播离开回来状态

    public void masterLeaveTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!masterComeBack) {

                    showFinishLayout(true, 2);
                }
            }
        },30000);

    }

    //视频连接超时
    public void playerConnectFail() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!masterComeBack&&LiveConstant.isUnDestory) {
                        CustomChatDialog dialogConnect = new CustomChatDialog(AudienceActivity.this);
                        dialogConnect.setContent("视频连接失败!");
                        dialogConnect.setCancelable(false);
                        dialogConnect.setCancelBtn("退出直播间", new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                if(DemoCache.getLoginChatRoomStatus()){
                                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                                }
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
                                isAudienceOver = true;
                                loadingPage.setVisibility(View.GONE);
                                audienceOver.setVisibility(View.GONE);
                                if ("1".equals(playType)) {
                                    palyerLayout.addView(videoView);
                                } else {
                                    doRenderRemoteUi(uid);
                                }
                            }
                        });
                        dialogConnect.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },30000);


    }

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
        if (inputPanel != null) {
            inputPanel.hideInputMethod();
            inputPanel.collapse(false);
        }
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
        if (LiveConstant.isUnDestory) {
            CustomChatDialog customDialog = new CustomChatDialog(this);
            customDialog.setContent(getString(R.string.finish_confirm));
            customDialog.setCancelable(false);
            customDialog.setCancelBtn(getString(R.string.cancel), null);
            customDialog.setSurceBtn(getString(R.string.confirm), new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    if(DemoCache.getLoginChatRoomStatus()){
                        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    }
                    clearChatRoom();
                }
            });
            customDialog.show();
        }

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

            palyerLayout.addView(videoView);
            bufferStrategy = 1;
            videoView.setBufferStrategy(bufferStrategy);
            mediaType = "videoondemand";
            videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                    bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FILL_SCALE, mediaType);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(videoView.getLayoutParams());
            palyerLayout.setBackgroundColor(Color.BLACK);
            videoView.setLayoutParams(lp);
            videoPlayer.openVideo();
            errorListener();
        }
    }

    private void errorListener() {
        if (videoView == null) {
            return;
        }
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
                    if (LiveConstant.isUnDestory) {
                        dialogConnect = new CustomChatDialog(AudienceActivity.this);
                        dialogConnect.setContent(getString(R.string.live_video_conncet_fail));
                        dialogConnect.setCancelable(false);
                        dialogConnect.setCancelBtn(getString(R.string.live_quit_room), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                if(DemoCache.getLoginChatRoomStatus()){
                                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                                }
                                clearChatRoom();
                            }
                        });
                        dialogConnect.setSurceBtn(getString(R.string.live_again_conncet), new CustomChatDialog.CustomDialogListener() {
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
                    }

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
        videoView.setOnCompletionListener(new NELivePlayer.OnCompletionListener() {
            @Override
            public void onCompletion(NELivePlayer neLivePlayer) {
                Toast.makeText(AudienceActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
                showFinishLayout(true, 2);
                videoPlayer.resetVideo();
            }
        });

    }

    protected void findViews() {
        super.findViews();
        drawerLayout.openDrawer(Gravity.RIGHT);
        liveQuit = (ImageView) findViewById(R.id.live_quit);
        liveQuit.setVisibility(View.GONE);
        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.VISIBLE);
        liveSettingLayout.setClickable(true);
        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);


        //结束页面
        audienceOver = findViewById(R.id.audience_over);
        audienceOverClose = (ImageView) findViewById(R.id.audience_live_over_close);
        overAttention = (TextView) findViewById(R.id.audience_live_over_attention);
        headIv = (CircleImageView) findViewById(R.id.live_over_audience_head_iv);
        masterName = (TextView) findViewById(R.id.live_over_master_name);

        keyboardLayout.setAlpha(0);
        keyboardLayout.setFocusable(false);
        keyboardLayout.setClickable(false);
        liveGiftImg.setOnClickListener(buttonClickListener);
        inputChar.setOnClickListener(buttonClickListener);
        overAttention.setOnClickListener(buttonClickListener);
        quit.setOnClickListener(buttonClickListener);
        inputChar.setOnClickListener(buttonClickListener);
        liveQuit.setOnClickListener(buttonClickListener);
        audienceOverClose.setOnClickListener(buttonClickListener);

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

    @Override
    protected void showStatusUnUsual() {
        try {
            if (LiveConstant.isUnDestory && firstWarning) {
                firstWarning = false;
                final CustomChatDialog customDialog = new CustomChatDialog(AudienceActivity.this);
                customDialog.setContent(getString(R.string.live_status_inusual));
                customDialog.setCancelable(false);
                customDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        finishLive();
                        firstWarning = true;
                    }
                });
                customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                customDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void showUserInfoDialog(final String userIds, final String channelIds, boolean isMaster) {

        CustomUserInfoDialog dialog = new CustomUserInfoDialog(this, container, userIds, channelIds, LiveConstant.ROLE, isMaster, creatorAccount, roomId);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LiveConstant.USER_INFO_FIRST_CLICK = true;
            }
        });
        dialog.setOnSuperManagerCloseLiveRoomListener(new CustomUserInfoDialog.OnSuperManagerCloseLiveRoomListener() {
            @Override
            public void closeLiveRoom() {
                LiveMessage liveMessage = new LiveMessage();
                liveMessage.setStyle(MessageType.closeLive);
                liveMessage.setUserId(userId);
                liveMessage.setCreatorAccount(creatorAccount);
                liveMessage.setChannelId(channelId);
                IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "关闭直播间", roomId, AuthPreferences.getUserAccount(), liveMessage);
                sendMessage(imMessage, MessageType.closeLive);
            }
        });
        dialog.setOnLiveRoomContentListener(new CustomUserInfoDialog.OnLiveRoomContentListener() {
            @Override
            public void getClickContent(String content) {
                marqueeView.start(content);
            }
        });
        dialog.show();
    }


    protected void showInputTextView() {
        AppLog.i("TAG","用户端显示软键盘showInputTextView");
        boolean logineds = UserHelper.isLogined(AudienceActivity.this);
        if (!logineds) {
            showLoginViewDialog();
        } else if (DemoCache.getLoginChatRoomStatus()) {
            keyboardLayout.setAlpha(1.0f);
            keyboardLayout.setClickable(true);
            liveSettingLayout.setVisibility(View.GONE);
            if (inputPanel != null) {
                inputPanel.switchToTextLayout(true);
            }
        } else {
            Toast.makeText(AudienceActivity.this, "没有登录聊天室，请退出重进!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void liveCommonSetting() {
        MobHelper.sendEevent(this, MobEvent.LIVE_USER_SHARE);
        showSharePopuwindow(shareVO,"分享了直播");
    }

    @Override
    protected void superManagerKickOutUser() {
        Toast.makeText(this,"你被管理员踢出聊天室",Toast.LENGTH_SHORT).show();
        finishLive();
    }
    //超级管理员关闭直播间
    @Override
    protected void closeLiveNotifi() {
    }
    @Override
    protected void masterOnLineStatus(boolean b) {
        if (b) {
            masterComeBack = true;
            //主播回来了
            showFinishLayout(false, 2);
        } else {
            masterComeBack = false;
            masterLeaveTime();
            //主播离开了
        }

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    boolean isClickSettingLayout=false;
    boolean giftStoreFirstClick = true;
    boolean isNeedRequestServerShowGiftStorePage = false;
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.live_telecast_quit:
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_CLOSE);
                    finishLive();
                    break;
                case R.id.live_quit:
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_CLOSE);
                    DialogUtil.clear();
                    finishLive();

                    break;
                case R.id.live_gift_img:
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_GIFT);
                    if (giftStoreFirstClick) {
                        giftStoreFirstClick = false;
                        boolean loginedq = UserHelper.isLogined(AudienceActivity.this);
                        if (!loginedq) {
                            showLoginViewDialog();
                            giftStoreFirstClick = true;
                        } else if (!DemoCache.getLoginChatRoomStatus() || !DemoCache.getLoginStatus()) {
                            Toast.makeText(AudienceActivity.this, getString(R.string.live_unlogin_channel_room), Toast.LENGTH_SHORT).show();
                            giftStoreFirstClick = true;
                        } else {
                            if (!"1".equals(playType)) {
                                if (LiveConstant.IS_FIRST_CLICK_PAGE) {
                                    LiveConstant.IS_FIRST_CLICK_PAGE = false;
                                    isNeedRequestServerShowGiftStorePage = true;
                                    contentLoaderAudience.getMyWallet();
                                } else {
                                    isNeedRequestServerShowGiftStorePage = false;
                                    if (hasWindowFocus()) {
                                        showGiftPage(myGold);
                                    }
                                }

                            }
                        }


                    }
                    break;


                case R.id.live_layout:
                    if (UserHelper.isLogined(AudienceActivity.this) && DemoCache.getLoginStatus()) {
                        periscopeLayout.addHeart();
                        sendLike();
                    }
                    break;
                case R.id.master_info_head_iv:
                    Intent intent3 = new Intent(AudienceActivity.this, LiveHomePageActivity.class);
                    intent3.putExtra("userId", userId);
                    startActivity(intent3);
                    break;

                case R.id.live_telecast_input_text:
                    isClickSettingLayout=true;
                    showInputTextView();
                    break;
                case R.id.audience_live_over_close:
                    finishLive();
                    break;
                case R.id.audience_live_over_attention:
                    if (UserHelper.isLogined(AudienceActivity.this)) {
                        if (overAttentionStatus == 0) {
                            contentLoaderAudience.getAddAttention(userId);
                        } else {
                            contentLoaderAudience.getCancelAttention(userId);
                        }
                    } else {
                        showLoginViewDialog();
                    }

                    break;


            }
        }
    };

    boolean isFirstShowPlane = true;
    private WalletContent content;

    //显示礼物布局
    private void showGiftPage(int gold) {
        liveSettingLayout.setVisibility(View.GONE);
        if (LiveConstant.isUnDestory) {
            giftStorePopuWindow = new GiftStorePopuWindow(this, giftSresult);
            giftStorePopuWindow.showGiftStorePopuWindow(gold);
            giftStorePopuWindow.showAtLocation(this.findViewById(R.id.live_layout),
                    Gravity.BOTTOM, 0, 0);
            giftStorePopuWindow.setOnSendClickListener(this);
            giftStorePopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    liveSettingLayout.setVisibility(View.VISIBLE);
                    giftStoreFirstClick = true;
                }
            });
        }

    }

    private int sendTotal;
    private int payBalance;
    private GiftDataResultBean giftDataResultBean;

    @Override
    public void sendGiftMessage(GiftDataResultBean giftDataResultBean, int sendTotal, int payBalance) {
        final int id = giftDataResultBean.getId();
        this.giftDataResultBean = giftDataResultBean;
        this.sendTotal = sendTotal;
        this.payBalance = payBalance;
        if (payBalance > myGold) {
            //余额不足，充值dialog
            showRechargeDialog(content);
        } else {
            if (sendTotal > 0) {
                contentLoaderAudience.liveSendGifts(channelId, userId, nickname, id, String.valueOf(sendTotal));
            }

        }
    }

    private void startSendGiftsAnimation(GiftDataResultBean giftDataResultBean, int sendTotal) {
        final String code = giftDataResultBean.getCode();
        AppLog.i("TAG", "startSendGiftsAnimation:" + code);
        String messageContent = "给主播送了" + ("001".equals(code) ? "鲜花" : ("002".equals(code) ? "行李箱" : ("003".equals(code) ? "飞机" : "神秘礼物")));
        LiveMessage liveMessage = new LiveMessage();
        GiftBean giftBean = new GiftBean();
        giftBean.setUserName(UserHelper.getUserName(AudienceActivity.this));
        giftBean.setUserId(String.valueOf(UserHelper.getUserId(AudienceActivity.this)));
        giftBean.setCode(giftDataResultBean.getCode());
        giftBean.setGiftCount(sendTotal);
        giftBean.setGiftImage(giftDataResultBean.getPhoto());
        giftBean.setGiftName(giftDataResultBean.getName());
        giftBean.setHeadImage(UserHelper.getUserAvatar(AudienceActivity.this));
        liveMessage.setGiftModel(giftBean);
        liveMessage.setChannelId(channelId);
        liveMessage.setStyle(MessageType.gift);
        IMMessage giftMessage = SendMessageUtil.sendMessage(container.account, messageContent, roomId, AuthPreferences.getUserAccount(), liveMessage);
        if (giftDataResultBean == null || code == null || giftDataResultBean.getName() == null || sendTotal == 0) {
            return;
        }
        if ("003".equals(code)) {
            giftPlaneAnimation.showPlaneAnimation((ChatRoomMessage) giftMessage);

        } else if (code != null) {
            giftAnimation.showGiftAnimation((ChatRoomMessage) giftMessage);
        }
        sendMessage(giftMessage, MessageType.gift);
        giftStorePopuWindow.dismiss();

    }

    private void finishLive() {
        if (videoPlayer != null) {
            videoPlayer.resetVideo();
        }
        if (isStartLive) {
            logoutChatRoom();
        } else {
            if(DemoCache.getLoginChatRoomStatus()){
                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            }

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
    boolean isSendLike=true;
    public void sendLike() {
     //   marqueeView.start("我给主播点了个赞");
        if (!isFastClick() && container != null && container.account != null && creatorAccount != null) {
            IMMessage imMessage=null;
            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setStyle(MessageType.like);
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            if(isSendLike){
                isSendLike=false;
                imMessage = SendMessageUtil.sendMessage(container.account, "给主播点了个赞", roomId, AuthPreferences.getUserAccount(), liveMessage);
            }else{
                imMessage = SendMessageUtil.sendMessage(container.account, "给主播点了个赞1", roomId, AuthPreferences.getUserAccount(), liveMessage);
            }
            sendMessage(imMessage, MessageType.like);

        }
    }
    //发送主播离开童子

    // 发送爱心频率控制
    private boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        long time = currentTime - lastClickTime;
        if (time > 0 && time < 1000) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }

    /***********************
     * 收发礼物
     ******************************/


    @Override
    public boolean isDisconnected() {
        return false;
    }

    /****************************
     * 播放器状态回调
     *****************************/

    @Override
    public void onError() {
        AppLog.i("TAG", "用户端播放错误弹框");
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
            showOverInfo();
            if (inputPanel != null) {
                inputPanel.collapse(true);
            }
            if (giftStorePopuWindow != null) {
                giftStorePopuWindow.dismiss();
            }
            if (giftsRankPopuWindow != null) {
                giftsRankPopuWindow.dismiss();
            }

            contentLoaderAudience.getLiveUserInfo(String.valueOf(userId));//获取用户基本信息

    }
        if (!liveEnd && !isAudienceOver) {
            AppLog.i("TAG", "主播回来了隐藏主播信息界面");
            isAudienceOver = true;
            loadingPage.setVisibility(View.GONE);
            audienceOver.setVisibility(View.GONE);
            if ("1".equals(playType)) {
                palyerLayout.addView(videoView);
            } else {
                doRenderRemoteUi(uid);
            }
        }
    }

    private void showOverInfo() {
        DrawableUtils.displayImg(this,headIv,avatar);
        masterName.setText(nickname);
    }

    //显示充值dialog
    private void showRechargeDialog(final WalletContent content) {
        if (LiveConstant.isUnDestory) {
            final CustomChatDialog rechargeDialog = new CustomChatDialog(AudienceActivity.this);
            rechargeDialog.setTitle(getString(R.string.live_hint));
            rechargeDialog.setContent(getString(R.string.live_recharge_hint));
            rechargeDialog.setCancelable(false);
            rechargeDialog.setCancelBtn(getString(R.string.live_canncel), null);
            rechargeDialog.setSurceBtn("充值", new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    LiveConstant.IS_FIRST_CLICK_PAGE = true;
                    Intent intent = new Intent(AudienceActivity.this, RechargeActivity.class);
                    intent.putExtra(KeyParams.WALLET_CONTENT, content);
                    startActivity(intent);
                    giftStorePopuWindow.dismiss();
                }
            });
            rechargeDialog.show();
        }


    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(isClickSettingLayout){
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {//显示
                if (liveSettingLayout != null && keyboardLayout != null) {
                    keyboardLayout.setAlpha(1);
                    keyboardLayout.setClickable(true);
                    keyboardLayout.setVisibility(View.VISIBLE);
                    liveSettingLayout.setVisibility(View.INVISIBLE);
                    liveSettingLayout.setClickable(false);
                    topView.setVisibility(View.GONE);
                    AppLog.i("TAG","用户端软键盘显示");
                }
            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//隐藏
                if (keyboardLayout != null) {
                    keyboardLayout.setAlpha(0);
                    keyboardLayout.setClickable(false);
                    keyboardLayout.setVisibility(View.INVISIBLE);
                    liveSettingLayout.setVisibility(View.VISIBLE);
                    liveSettingLayout.setClickable(true);
                    topView.setVisibility(View.VISIBLE);
                    AppLog.i("TAG","用户端软键盘隐藏");
                }
            }
        }

    }

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        AppLog.i("TAG", "用户端onFirstRemoteVideoDecoded");
        this.uid = uid;
        doRenderRemoteUi(uid);
    }

    int uid;

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                int childCount = palyerLayout.getChildCount();

                if (childCount > 0) {
                    palyerLayout.removeAllViews();
                }
                palyerLayout.addView(surfaceV);
                surfaceV.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        AppLog.i("TAG","视频布局变化监听");
                        if (palyerLayout != null && palyerLayout.getChildAt(0) != null) {
                            if (bottom > oldBottom) {
                                palyerLayout.getChildAt(0).layout(left, top, right, bottom);
                                AppLog.i("TAG","视频布局变化监听1");
                            } else {
                                palyerLayout.getChildAt(0).layout(oldLeft, oldTop, oldRight, oldBottom);
                                AppLog.i("TAG","视频布局变化监听2");
                            }
                        }
                    }
                });
                surfaceV.setZOrderOnTop(true);
                surfaceV.setZOrderMediaOverlay(true);
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));//设置远端视频属性
                rtcEngine().setRemoteVideoStreamType(uid, 0);
                if (messageListPanel != null) {
                    messageListPanel.setHeaderViewVisible();
                }
                masterComeBack = true;
                loadingPage.setVisibility(View.GONE);
                contentLoaderAudience.getLiveUserInfo(userId);
                if(showAttentionRunnable==null){
                    showAttentionRunnable=new ShowAttentionRunnable();
                }
                handler.postDelayed(showAttentionRunnable,60000);
                if (audienceOver != null) {
                    isAudienceOver = true;
                    audienceOver.setVisibility(View.GONE);
                }
            }
        });
    }

    ShowAttentionRunnable showAttentionRunnable=null;
    private class ShowAttentionRunnable implements Runnable {//直播1分钟显示关注主播dialog
        @Override
        public void run() {
            try{
                if (overAttentionStatus == 0 && LiveConstant.isUnDestory&&audienceOver.getVisibility()!=View.VISIBLE) {
                    final LiveAttentionPopuwindow popuwindow = new LiveAttentionPopuwindow(AudienceActivity.this);
                    popuwindow.showAttentionPopu(liveRowsBean);
                    popuwindow.setOnUserAttentionListener(new LiveAttentionPopuwindow.OnUserAttentionListener() {
                        @Override
                        public void getAttention() {
                            if (container != null && container.account != null && creatorAccount != null) {
                                LiveMessage liveMessage = new LiveMessage();
                                liveMessage.setStyle(MessageType.text);
                                liveMessage.setUserId(userId);
                                liveMessage.setCreatorAccount(creatorAccount);
                                liveMessage.setChannelId(channelId);
                                IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "关注了主播!", roomId, AuthPreferences.getUserAccount(), liveMessage);
                                AudienceActivity.this.sendMessage(imMessage, MessageType.text);
                            }
                            popuwindow.dismiss();
                        }

                        @Override
                        public void goLoginActivity() {
                            showLoginViewDialog();
                        }
                    });
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        DialogUtil.clear();
        if (giftStorePopuWindow != null) {
            giftStorePopuWindow.dismiss();
        }
        if (giftsRankPopuWindow != null) {
            giftsRankPopuWindow.dismiss();
        }
        LiveConstant.IS_FIRST_CLICK_PAGE = true;
        AppLog.i("TAG", "用户端activity走了onStop方法");

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        AppLog.i("TAG", "用户端走了:onJoinChannelSuccess");
        masterComeBack = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rtcEngine().setEnableSpeakerphone(true);
                rtcEngine().setPreferHeadset(true);
                playerConnectFail();
            }
        });

    }

    @Override
    public void onUserOffline(int uid, int reason) {

        AppLog.i("TAG", "用户端：onUserOffline" + uid + "  reason:" + reason);
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        AppLog.i("TAG", "用户端：onUserJoined" + uid + "  reason:" + elapsed);
    }

    @Override
    public void onConnectionInterrupted() {
        AppLog.i("TAG", "視頻連接中斷连接中断回调");
    }

    @Override
    public void onConnectionLost() {
        AppLog.i("TAG", "視頻連接中斷连接丟失");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showFinishLayout(true, 2);
            }
        });

    }

    @Override
    public void onError(int err) {
        AppLog.i("TAG", "用户端视频播放错误码:" + err);

    }

    @Override
    public void onVideoStopped() {
        AppLog.i("TAG", "用户端视频播停止回调:");
    }

    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        AppLog.i("TAG", "用户离开直播间回调:" + stats.toString());

    }

    @Override
    public void onUserEnableVideo(int uid, boolean enabled) {
        AppLog.i("TAG", "其他用户启用/关闭视频 :" + uid + "         " + enabled);
    }

    @Override
    public void onLastmileQuality(int quality) {//网络质量检测

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
