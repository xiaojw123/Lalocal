package com.lalocal.lalocal.live.entertainment.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
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

import com.android.volley.VolleyError;
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
import com.lalocal.lalocal.live.base.util.MessageToBean;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.ChallengeDetailsResp;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResp;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResultBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.model.OnLineUser;
import com.lalocal.lalocal.live.entertainment.model.SendGiftResp;
import com.lalocal.lalocal.live.entertainment.ui.CustomChallengeRaiseDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomChanllengeDialog;
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
import com.lalocal.lalocal.model.LiveDetailsDataResp;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;
import java.util.Timer;

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
AudienceActivity extends LivePlayerBaseActivity implements VideoPlayer.VideoPlayerProxy, View.OnLayoutChangeListener,GiftStorePopuWindow.OnSendClickListener {
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
    private Button sendGiftBtn;


    // 播放器
    private VideoPlayer videoPlayer;
    // 发送礼物频率控制使用
    private long lastClickTime = 0;
    // 选中的礼物
    private int giftPosition = -1;

    // state
    private boolean isStartLive = false; // 推流是否开始
 //   protected ImageView clickPraise;
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
    private String  liveStatus;

    private int myGold;
    private CustomLiveUserInfoDialog customLiveUserInfoDialog;
    private Timer timerOnLine;
    private AudienceCallBack audienceCallBack;
    private ContentLoader contentLoaderAudience;
    protected List<GiftDataResultBean> giftSresult;
    private TextView challengeHint;
    private LinearLayout challengeRaiseLayout;
    private SurfaceView surfaceV;
    protected String roomId;
    private CountDownTimer countDownTimer;
    private ImageView headIv;
    private TextView overNick;
    private TextView overSignature;
    private TextView overAttention;
    private TextView overFans;
    private BlurImageView blurView;
    private MyRunnable myRunnable;
    private TextView masterAttentino;
    private int fansNumMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        ActivityManager.removeAudienceCurrent();
        ActivityManager.audienceActivityStack(this);
        audienceCallBack = new AudienceCallBack();
        contentLoaderAudience = new ContentLoader(this);
        contentLoaderAudience.setCallBack(audienceCallBack);
        String id = getIntent().getStringExtra("id");
        contentLoaderAudience.liveDetails(id);
        contentLoaderAudience.liveGiftStore();


        AppLog.i("TAG","用户端获取token:"+UserHelper.getToken(this));

    }

    @Override
    protected void accountKicout() {

    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            handler.removeCallbacks(this);
            if (onlineCounts > 0&&contentLoaderAudience!=null&&channelId!=null) {
                contentLoaderAudience.getAudienceUserOnLine(onlineCounts,channelId);
            }
            handler.postDelayed(this, 2000);
        }
    }
    boolean firstWarning=true;
    private int overAttentionStatus;
    private class AudienceCallBack extends ICallBack {

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);
            if (volleyError != null) {
                String errorMsg = volleyError.toString();
                if (volleyError.networkResponse != null) {
                    int code = volleyError.networkResponse.statusCode;
                    if (code == 401) {
                        if(handler!=null){
                            handler.removeCallbacks(myRunnable);
                        }
                    }
                }
            }
        }

        @Override
        public void onLiveDetails(LiveDetailsDataResp liveDetailsDataResp) {
            super.onLiveDetails(liveDetailsDataResp);
            if(liveDetailsDataResp.getReturnCode()==0){
                LiveRowsBean liveRowsBean = liveDetailsDataResp.getResult();
                nickname= liveRowsBean.getUser().getNickName();
                avatar= liveRowsBean.getUser().getAvatar();
                playType=String.valueOf(liveRowsBean.getType());
                Object ann = liveRowsBean.getAnnoucement();
                String annoucement = null;
                if (ann != null) {
                    annoucement = ann.toString();
                } else {
                    annoucement = "这是公告哈";
                }
                nickNameAudience= liveRowsBean.getUser().getNickName();
                channelId=String.valueOf(liveRowsBean.getId());
                cname= liveRowsBean.getCname();
                liveStatus=String.valueOf(liveRowsBean.getStatus());
                shareVO = liveRowsBean.getShareVO();
                roomId = String.valueOf(liveRowsBean.getRoomId());
                ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
                int onlineUser = liveRowsBean.getOnlineUser();
                url = liveRowsBean.getPullUrl();
                userId = String.valueOf(liveRowsBean.getUser().getId());
                if(avatar!=null){
                    blurView.setBlurImageURL(avatar);
                    blurView.setScaleRatio(20);
                    blurView.setBlurRadius(1);
                }
                DrawableUtils.displayImg(AudienceActivity.this,headIv,liveRowsBean.getUser().getAvatar());
                getParameter(liveRowsBean);
                registerObservers(true);
                initParam();
                initUIandEvent();
                if("0".equals(liveStatus)){
                    showFinishLayout(true,2);
                }
                myRunnable = new MyRunnable();
                handler.postDelayed(myRunnable,2000);
                if("1".equals(playType)){
                    hideBtn(onlineUser);
                }
            }
        }

        @Override
        public void onGetAudienceOnLineUserCount(String json) {
            super.onGetAudienceOnLineUserCount(json);
            OnLineUser onLineUser = new Gson().fromJson(json, OnLineUser.class);
            if(onLineUser!=null&&onLineUser.getResult()>0){
                onlineCountText.setText(String.valueOf(onLineUser.getResult())+"人");
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
        public void onResponseFailed(String message ,int code) {
            super.onResponseFailed(message,code);
            if(code==230&&firstWarning){
                firstWarning = false;
                try{
                    if(isUnDestory){
                        CustomChatDialog customDialog= new CustomChatDialog(AudienceActivity.this);
                        customDialog.setContent(getString(R.string.audience_status_unsual));
                        customDialog.setCancelable(false);
                        customDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
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

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        @Override
        public void onGetMyWallet(WalletContent content) {
            super.onGetMyWallet(content);
            if(isNeedRequestServerShowGiftStorePage){
                myGold = (int)content.getGold();
                AppLog.i("TAG","我的乐钻石："+myGold);
                showGiftPage(myGold);
            }

        }
        @Override
        public void onSendGiftsBack(String result) {
            super.onSendGiftsBack(result);
            SendGiftResp sendGiftResp = new Gson().fromJson(result, SendGiftResp.class);
            SendGiftResp.ResultBean sendGiftResult = sendGiftResp.getResult();
            if (sendGiftResp.getReturnCode() == 0&&sendTotal>0) {
                startSendGiftsAnimation(giftDataResultBean, sendTotal);
                myGold=myGold-payBalance;
            } else {
                Toast.makeText(AudienceActivity.this, getString(R.string.live_sendgift_fail), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onChallengeInitiate( ChallengeDetailsResp.ResultBean resultBean) {
            super.onChallengeInitiate(resultBean);
                LiveMessage liveMessage = new LiveMessage();
                liveMessage.setStyle(MessageType.challenge);
                liveMessage.setUserId(userId);
                liveMessage.setCreatorAccount(creatorAccount);
                liveMessage.setChallengeModel(resultBean);
                IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "发起挑战", roomId, AuthPreferences.getUserAccount(), liveMessage);
                sendMessage(imMessage, MessageType.challenge);
            if(isUnDestory){
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
                overNick.setText(result.getNickName());
                fansNumMaster = result.getFansNum();
                if(result.getDescription()==null||result.getDescription().toString().length()<1){
                    overSignature.setText(getString(R.string.live_default_signture));
                }else{
                    overSignature.setText(result.getDescription());
                }

                Object status = result.getAttentionVO().getStatus();
                if(status!=null){
                    double parseDouble = Double.parseDouble(String.valueOf(status));
                    overAttentionStatus = (int) parseDouble;
                    if(overAttentionStatus==0){
                        masterAttentino.setText("关注");
                    }else{
                        masterAttentino.setText("已关注");
                    }
                }
                AudienceActivity.this.overAttention.setText(String.valueOf(result.getAttentionNum()));
                overFans.setText(String.valueOf(result.getFansNum()));
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
        AppLog.i("TAG","initUIandEvent 用户端");
        if(cname!=null){
            int cRole = Constants.CLIENT_ROLE_AUDIENCE;
            doConfigEngine(cRole);
            if(!"1".equals(playType)){
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
                AppLog.i("TAG","用戶端視頻分辨率為720p");
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
        andiuence = (TextView) loadingPage.findViewById(R.id.audience_over_layout);
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
                LiveConstant.NET_CHECK=1;
                isFirstCheckNet = false;
                if(isUnDestory){
                    dialogNet = new CustomChatDialog(AudienceActivity.this);
                    dialogNet.setTitle(getString(R.string.live_hint));
                    dialogNet.setContent(getString(R.string.live_net_type_cmcc));
                    dialogNet.setCancelable(false);
                    dialogNet.setCancelBtn(getString(R.string.live_continue_look), new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            LiveConstant.NET_CHECK=0;
                        }
                    });
                    dialogNet.setSurceBtn(getString(R.string.live_over), new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            LiveConstant.NET_CHECK=0;
                            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                            clearChatRoom();
                        }
                    });
                    dialogNet.show();
                }

            }

        }
    }

    boolean masterFirstEnter = true;
    boolean masterComeBack=true;
    //计算主播离开时间，若果超过25秒还未回来，就显示主播离开界面，masterComeBack：标记主播离开回来状态
    public  void masterLeaveTime(){
        countDownTimer = new CountDownTimer(30000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                if(!masterComeBack){
                    AppLog.i("TAG","主播还没有回来弹框");
                    showFinishLayout(true, 2);
                }
            }
        }.start();
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
        if(countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
        if(handler!=null){
            handler.removeCallbacks(myRunnable);
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
        if(isUnDestory){
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

    @Override
    protected void clickChallengeBtn() {
        if(isUnDestory){
            CustomChanllengeDialog customChanllengeDialog=new CustomChanllengeDialog(this);
            customChanllengeDialog.startChanllageClikListener(new CustomChanllengeDialog.ChanllengeInitiateDialogListener() {
                @Override
                public void onChanllengeInitiateDialogListener(int gold,String content) {
                    if(gold>=10){

                        contentLoaderAudience.getChallenge(content,gold,channelId);
                    }else{
                        Toast.makeText(AudienceActivity.this,"挑战金额不得少于10个乐钻!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            customChanllengeDialog.show();
        }

    }


    int bufferStrategy = -1;
    String mediaType;
    boolean isFirstLink = true;//标记第一次重新连接，再次连接就显示主播休息了的界面

    private void initAudienceParam() {

        if ("1".equals(playType)) {
            videoView = new NEVideoView(this);
            // 防止软键盘挤压屏幕
           /* videoView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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
            });*/
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
                    if(isUnDestory){
                        dialogConnect = new CustomChatDialog(AudienceActivity.this);
                        dialogConnect.setContent(getString(R.string.live_video_conncet_fail));
                        dialogConnect.setCancelable(false);
                        dialogConnect.setCancelBtn(getString(R.string.live_quit_room), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
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
                Toast.makeText(AudienceActivity.this,"播放完成",Toast.LENGTH_SHORT).show();
                showFinishLayout(true, 2);
                videoPlayer.resetVideo();
            }
        });

    }

    protected void findViews() {
        super.findViews();

        liveQuit = (ImageView) findViewById(R.id.live_quit);
      //  clickPraise = (ImageView) findViewById(R.id.live_telecast_like);
        quit = (ImageView) findViewById(R.id.live_telecast_quit);
        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        challengeHint = (TextView) findViewById(R.id.audience_hint_emcee_accept);
        challengeRaiseLayout = (LinearLayout) findViewById(R.id.audience_challenge_raise_layout);
        challengeRaiseLayout.setOnClickListener(buttonClickListener);
        liveSettingLayout.setVisibility(View.VISIBLE);
        liveSettingLayout.setClickable(true);
        ImageView settingBtn = (ImageView) findViewById(R.id.live_telecast_setting);
        settingBtn.setVisibility(View.GONE);

        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);
        audienceOver = findViewById(R.id.audience_over);

        backHome = (LinearLayout) findViewById(R.id.master_info_back_home);
        blurImageView = (BlurImageView) audienceOver.findViewById(R.id.audience_over_bg);

      /*  liveMasterHome = (TextView) audienceOver.findViewById(R.id.live_master_home_over);*/
        headIv = (ImageView) audienceOver.findViewById(R.id.master_info_head_iv);
        headIv.setOnClickListener(buttonClickListener);
        overNick = (TextView) audienceOver.findViewById(R.id.master_info_nick_tv);
        overSignature = (TextView)audienceOver.findViewById(R.id.master_info_signature);
        overAttention = (TextView)audienceOver.findViewById(R.id.live_attention);
        overFans = (TextView)audienceOver.findViewById(R.id.live_fans);

        masterAttentino = (TextView) audienceOver.findViewById(R.id.master_dialog_attention_audience);
        masterAttentino.setOnClickListener(buttonClickListener);
      //  liveMasterHome.setOnClickListener(buttonClickListener);
        backHome.setOnClickListener(buttonClickListener);
        keyboardLayout.setAlpha(0);
        keyboardLayout.setFocusable(false);
        keyboardLayout.setClickable(false);
        liveGiftImg.setOnClickListener(buttonClickListener);


      //  clickPraise.setOnClickListener(buttonClickListener);
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

    @Override
    protected void masterOnLineStatus(boolean b) {
        if(b){
            masterComeBack=true;
            //主播回来了
            LiveMessage liveMessage=new LiveMessage();
            liveMessage.setStyle(MessageType.masterIn);
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "回来了", roomId, creatorAccount, liveMessage);
            sendMessage(imMessage, MessageType.masterIn);
            showFinishLayout(false, 2);
        }else {
            masterComeBack=false;
            masterLeaveTime();
            //主播离开了
            LiveMessage liveMessage=new LiveMessage();
            liveMessage.setStyle(MessageType.masterEixt);
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account , "离开了", roomId,creatorAccount , liveMessage);
            sendMessage(imMessage, MessageType.masterEixt);
        }

    }

    @Override
    protected void receiveChallengeMessage(ChatRoomMessage message) {
        ChallengeDetailsResp.ResultBean messageToChallengeBean = MessageToBean.getMessageToChallengeBean(message);
        if(messageToChallengeBean.getStatus()==1){
            challengeHint.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    challengeHint.setVisibility(View.GONE);
                }
            },1500);

            challengeRaiseLayout.setVisibility(View.VISIBLE);
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };



    boolean giftStoreFirstClick=true;
    boolean isNeedRequestServerShowGiftStorePage=false;
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {


              /*  case R.id.live_telecast_like:

                    boolean logined = UserHelper.isLogined(AudienceActivity.this);
                    if (!logined) {
                        showLoginViewDialog();
                    } else {
                        periscopeLayout.addHeart();
                        sendLike();
                    }
                    break;*/
                case R.id.live_telecast_quit:
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_CLOSE);
                    finishLive();
                    break;
                case R.id.live_quit:
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_CLOSE);
                    DialogUtil.clear();
                    finishLive();
                    break;
                case R.id.live_telecast_input_text:

                    boolean logineds = UserHelper.isLogined(AudienceActivity.this);
                    if (!logineds) {
                        showLoginViewDialog();
                    } else if(DemoCache.getLoginChatRoomStatus()){
                        keyboardLayout.setAlpha(1.0f);
                        keyboardLayout.setClickable(true);
                        liveSettingLayout.setVisibility(View.GONE);
                        if(inputPanel!=null){
                            inputPanel.switchToTextLayout(true);
                        }
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
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_GIFT);
                    if(giftStoreFirstClick){
                        giftStoreFirstClick=false;
                    boolean loginedq = UserHelper.isLogined(AudienceActivity.this);
                    if (!loginedq) {
                        showLoginViewDialog();
                        giftStoreFirstClick=true;
                    }else if(!DemoCache.getLoginChatRoomStatus()||!DemoCache.getLoginStatus()) {
                       Toast.makeText(AudienceActivity.this,getString(R.string.live_unlogin_channel_room),Toast.LENGTH_SHORT).show();
                        giftStoreFirstClick=true;
                    } else {
                        if(!"1".equals(playType)) {
                            if(LiveConstant.IS_FIRST_CLICK_PAGE){
                                LiveConstant.IS_FIRST_CLICK_PAGE=false;
                                isNeedRequestServerShowGiftStorePage=true;
                                contentLoaderAudience.getMyWallet();
                            }else {
                                isNeedRequestServerShowGiftStorePage=false;
                                if(hasWindowFocus()){
                                    showGiftPage(myGold);
                                }
                            }

                        }
                    }



                    }
                    break;
                case R.id.audience_challenge_raise_layout://显示任务众筹卡片
                    if(isUnDestory){
                        CustomChallengeRaiseDialog  customChallengeRaiseDialog=new CustomChallengeRaiseDialog(AudienceActivity.this);
                        customChallengeRaiseDialog.show();
                    }

                    break;
             /*   case R.id.live_master_home_over:

                    Intent intent2 = new Intent(AudienceActivity.this, LiveHomePageActivity.class);
                    intent2.putExtra("userId", userId);
                    startActivity(intent2);
                    break;*/
                case R.id.live_layout:
                    periscopeLayout.addHeart();
                    sendLike();
                    break;
                case R.id.master_info_head_iv:
                    Intent intent3 = new Intent(AudienceActivity.this, LiveHomePageActivity.class);
                    intent3.putExtra("userId", userId);
                    startActivity(intent3);
                    break;
                case R.id.master_dialog_attention_audience:

                    if(UserHelper.isLogined(AudienceActivity.this)){
                        if(overAttentionStatus==0){
                            masterAttentino.setText("已关注");
                            contentLoaderAudience.getAddAttention(userId);
                            ++fansNumMaster;
                            overFans.setText(String.valueOf(fansNumMaster));
                            overAttentionStatus=1;
                        }else {
                            overAttentionStatus=0;
                            contentLoaderAudience.getCancelAttention(userId);
                            --fansNumMaster;
                            masterAttentino.setText("关注");

                            overFans.setText(String.valueOf(fansNumMaster));
                        }
                    }else {
                        showLoginViewDialog();
                    }
                //    contentLoaderAudience.getLiveUserInfo(userId);

                    break;

            }
        }
    };

    boolean isFirstShowPlane = true;
    private WalletContent content;
   //显示礼物布局
    private void showGiftPage(int gold) {
        liveSettingLayout.setVisibility(View.GONE);
        if(isUnDestory){
            giftStorePopuWindow = new GiftStorePopuWindow(this, giftSresult);
            giftStorePopuWindow.showGiftStorePopuWindow(gold);
            giftStorePopuWindow.showAtLocation(this.findViewById(R.id.live_layout),
                    Gravity.BOTTOM, 0, 0);
            giftStorePopuWindow.setOnSendClickListener(this);
            giftStorePopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    liveSettingLayout.setVisibility(View.VISIBLE);
                    giftStoreFirstClick=true;
                }
            });
        }

    }

    private int sendTotal;
    private int payBalance;
    private  GiftDataResultBean giftDataResultBean;
    @Override
    public void sendGiftMessage(GiftDataResultBean giftDataResultBean, int sendTotal, int payBalance) {
        final  int id =giftDataResultBean.getId();
        this.giftDataResultBean=giftDataResultBean;
        this.sendTotal=sendTotal;
        this.payBalance=payBalance;
        if(payBalance>myGold){
            //余额不足，充值dialog
            showRechargeDialog(content);
        }else {
            if(sendTotal>0){
                contentLoaderAudience.liveSendGifts(channelId, userId, nickname, id, String.valueOf(sendTotal));
            }

        }
    }

    private void startSendGiftsAnimation(GiftDataResultBean giftDataResultBean, int sendTotal) {
        final String code =giftDataResultBean.getCode();
        AppLog.i("TAG", "startSendGiftsAnimation:" + code);
        String messageContent= "给主播送了" + ("001".equals(code) ? "鲜花" : ("002".equals(code) ? "行李箱" : ("003".equals(code)?"飞机":"神秘礼物")));
        LiveMessage liveMessage=new LiveMessage();
        GiftBean giftBean=new GiftBean();
        giftBean.setUserName(UserHelper.getUserName(AudienceActivity.this));
        giftBean.setUserId(String.valueOf(UserHelper.getUserId(AudienceActivity.this)));
        giftBean.setCode(giftDataResultBean.getCode());
        giftBean.setGiftCount(sendTotal);
        giftBean.setGiftImage(giftDataResultBean.getPhoto());
        giftBean.setGiftName(giftDataResultBean.getName());
       giftBean.setHeadImage(UserHelper.getUserAvatar(AudienceActivity .this));
        liveMessage.setGiftModel(giftBean);
        liveMessage.setChannelId(channelId);
        liveMessage.setStyle(MessageType.gift);
        IMMessage giftMessage = SendMessageUtil.sendMessage(container.account, messageContent, roomId, AuthPreferences.getUserAccount(), liveMessage);
        if(giftDataResultBean==null||code==null||giftDataResultBean.getName()==null||sendTotal==0){
            return;
        }
        if ("003".equals(code)) {
            giftPlaneAnimation.showPlaneAnimation((ChatRoomMessage) giftMessage);

        } else if(code!=null){
            giftAnimation.showGiftAnimation((ChatRoomMessage) giftMessage);
        }
        sendMessage(giftMessage, MessageType.gift);
        giftStorePopuWindow.dismiss();

    }

    int status = -1;
    boolean isManager = false;
    boolean isMuteds = false;

    protected void showMasterInfoPopuwindow(final LiveUserInfoResultBean result, boolean isMuted, final String meberAccount, int id, int managerId, List<LiveManagerListBean> managerList) {
        AppLog.i("TAG","用户端走了showMasterInfoPopuwindow");
        isMuteds = isMuted;
        if (managerId != 0) {
            isManager = true;

        } else {
            isManager = false;
        }
        if(isUnDestory){
            customLiveUserInfoDialog = new CustomLiveUserInfoDialog(AudienceActivity.this, result, isManager, isMuted);
            customLiveUserInfoDialog.setCancelable(false);
            customLiveUserInfoDialog.setCancelBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_CANCEL);
                }
            });
            Object statusa = result.getAttentionVO().getStatus();
            if (statusa != null) {
                double parseDouble = Double.parseDouble(String.valueOf(statusa));
                status = (int) parseDouble;

            }

            customLiveUserInfoDialog.setUserHomeBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_AVATAR);
                    if("1".equals(playType)){
                        return;
                    }
                    Intent intent = new Intent(AudienceActivity.this, LiveHomePageActivity.class);
                    intent.putExtra("userId", String.valueOf(id));
                    startActivity(intent);
                }
            });
            customLiveUserInfoDialog.setReport(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_REPORT);
                    Toast.makeText(AudienceActivity.this,"点击了举报",Toast.LENGTH_SHORT).show();
                }
            });

            if (LiveConstant.IDENTITY == LiveConstant.IS_ONESELF || LiveConstant.IDENTITY == LiveConstant.IS_LIVEER) {

                customLiveUserInfoDialog.setSurceBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                    @Override
                    public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                        if("1".equals(playType)){
                            return;
                        }
                        Intent intent = new Intent(AudienceActivity.this, LiveHomePageActivity.class);
                        intent.putExtra("userId", String.valueOf(id));
                        startActivity(intent);
                    }
                });
            } else {
                if (managerList != null && managerList.size() > 0) {
                    for (LiveManagerListBean bean : managerList) {
                        if (bean.getId() == UserHelper.getUserId(AudienceActivity.this)) {
                            if(userId.equals(String.valueOf(id))){
                                LiveConstant.IDENTITY = LiveConstant.ME_CHECK_OTHER;
                            }else{
                                LiveConstant.IDENTITY = LiveConstant.MANAGER_IS_ME;
                                customLiveUserInfoDialog.setBanBtn(isMuteds == true ? getString(R.string.live_relieve_ban) : getString(R.string.live_ban), new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                                    @Override
                                    public void onCustomLiveUserInfoDialogListener(String id, final TextView textView, ImageView managerMark) {
                                        MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_ANCHOR_PROHIBITION);
                                        if (isMuteds) {
                                            String messageContent="解除了"+result.getNickName()+"的禁言";
                                            LiveMessage liveMessage=new LiveMessage();
                                            liveMessage.setStyle(MessageType.relieveBan);
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
                                            textView.setText(getString(R.string.live_ban));
                                            isMuteds = false;
                                        } else {

                                            String messageContent="禁言了"+result.getNickName();
                                            LiveMessage liveMessage=new LiveMessage();
                                            liveMessage.setStyle(MessageType.ban);
                                            liveMessage.setDisableSendMsgNickName(result.getNickName());
                                            liveMessage.setDisableSendMsgUserId(String.valueOf(result.getId()));
                                            liveMessage.setUserId(userId);
                                            liveMessage.setCreatorAccount(creatorAccount);
                                            liveMessage.setChannelId(channelId);
                                            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, messageContent, roomId, meberAccount, liveMessage);
                                            banListLive.add(meberAccount);
                                            sendMessage(imMessage, MessageType.ban);
                                            textView.setText(getString(R.string.live_relieve_ban));
                                            isMuteds = true;
                                        }
                                    }
                                });
                                break;
                            }

                        } else {
                            LiveConstant.IDENTITY = LiveConstant.ME_CHECK_OTHER;
                        }
                    }
                } else {
                    LiveConstant.IDENTITY = LiveConstant.ME_CHECK_OTHER;
                }

            }
            customLiveUserInfoDialog.setAttention(status == 0 ? getString(R.string.live_attention):getString(R.string.live_attention_ok), new CustomLiveUserInfoDialog.CustomLiveFansOrAttentionListener() {
                int fansCounts = -2;

                @Override
                public void onCustomLiveFansOrAttentionListener(String id, TextView fansView, TextView attentionView, int fansCount, int attentionCount, TextView attentionStatus) {
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_ATTENTION);
                    if (fansCounts == -2) {
                        fansCounts = fansCount;
                    }
                    if (status == 0) {
                        attentionStatus.setText(getString(R.string.live_attention_ok));
                        attentionStatus.setAlpha(0.4f);
                        ++fansCounts;
                        fansView.setText(String.valueOf(fansCounts));
                        contentLoaderAudience.getAddAttention(id);
                        status = 1;
                    } else {
                        attentionStatus.setText(getString(R.string.live_attention));
                        attentionStatus.setAlpha(1);
                        --fansCounts;
                        fansView.setText(String.valueOf(fansCounts));
                        contentLoaderAudience.getCancelAttention(id);
                        status = 0;
                    }
                }

            });

            customLiveUserInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    LiveConstant.USER_INFO_FIRST_CLICK=true;
                }
            });
            customLiveUserInfoDialog.show();
        }

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
        if (!isFastClick()&&container!=null&&container.account!=null&&creatorAccount!=null) {
            AppLog.i("TAG","用户端给主播店点赞那就多喝点"+creatorAccount);
            LiveMessage liveMessage=new LiveMessage();
            liveMessage.setStyle(MessageType.like);
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "给主播点了个赞", roomId, AuthPreferences.getUserAccount(), liveMessage);
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
        AppLog.i("TAG","用户端播放错误弹框");
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
            if(userId!=null){
                contentLoaderAudience.getLiveUserInfo(userId);
            }
        }
        if (!liveEnd && !isAudienceOver) {
            AppLog.i("TAG","主播回来了隐藏主播信息界面");
            isAudienceOver = true;
            loadingPage.setVisibility(View.GONE);
            audienceOver.setVisibility(View.GONE);
            if ("1".equals(playType)) {
                palyerLayout.addView(videoView);
            }else {
                doRenderRemoteUi(uid);
            }
        }
    }

    //显示充值dialog
    private void showRechargeDialog(final WalletContent content) {
        if(isUnDestory){
            final CustomChatDialog rechargeDialog = new CustomChatDialog(AudienceActivity.this);
            rechargeDialog.setTitle(getString(R.string.live_hint));
            rechargeDialog.setContent(getString(R.string.live_recharge_hint));
            rechargeDialog.setCancelable(false);
            rechargeDialog.setCancelBtn(getString(R.string.live_canncel), null);
            rechargeDialog.setSurceBtn("充值", new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    LiveConstant.IS_FIRST_CLICK_PAGE=true;
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
        AppLog.i("TAG","用户端onFirstRemoteVideoDecoded");
        this.uid=uid;
        doRenderRemoteUi(uid);
    }
    int uid;
    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
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
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));//设置远端视频属性
                rtcEngine().setRemoteVideoStreamType(uid,0);
              /*  if (config().mUid == uid) {
                        rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));//设置本地视频属性
                } else {
                    rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));//设置远端视频属性

                }*/
                if(messageListPanel!=null){
                    messageListPanel.setHeaderViewVisible();
                }
                masterComeBack=true;
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
        DialogUtil.clear();
        if(giftStorePopuWindow!=null){
            giftStorePopuWindow.dismiss();
        }
        if(giftsRankPopuWindow!=null){
            giftsRankPopuWindow.dismiss();
        }
        LiveConstant.IS_FIRST_CLICK_PAGE=true;
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        AppLog.i("TAG","用户端走了:onJoinChannelSuccess");
        masterComeBack=false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rtcEngine().setEnableSpeakerphone(true);
                rtcEngine().setPreferHeadset(true);
                masterLeaveTime();
            }
        });

    }

    @Override
    public void onUserOffline(int uid, int reason) {

        AppLog.i("TAG","用户端：onUserOffline"+uid+"  reason:"+reason);
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        AppLog.i("TAG","用户端：onUserJoined"+uid+"  reason:"+elapsed);
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
        AppLog.i("TAG","用户端视频播停止回调:");
    }

    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        AppLog.i("TAG","用户离开直播间回调:"+stats.toString());

    }

    @Override
    public void onUserEnableVideo(int uid, boolean enabled) {
        AppLog.i("TAG","其他用户启用/关闭视频 :"+uid+"         "+enabled);
    }

}
