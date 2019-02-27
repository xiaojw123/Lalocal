package com.lalocal.lalocal.live.entertainment.activity;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.lalocal.lalocal.live.LiveCache;
import com.lalocal.lalocal.live.base.util.ActivityManager;
import com.lalocal.lalocal.live.base.util.DialogUtil;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.LiveParams;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.fragment.AgoraAudienceFragment;
import com.lalocal.lalocal.live.entertainment.fragment.AgoraAudienceFragment.AgoraAudienceCallBackListener;
import com.lalocal.lalocal.live.entertainment.fragment.IJKPlayerFragment;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResp;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResultBean;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.model.SendGiftResp;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomNewUserInforDialog;
import com.lalocal.lalocal.live.entertainment.ui.GiftStorePopuWindow;
import com.lalocal.lalocal.live.entertainment.ui.LiveAttentionPopuwindow;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
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
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 观众端
 * Created by hzxuwen on 2016/3/18.
 * 日志聚合系统kids
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public  class AudienceActivity extends LivePlayerBaseActivity implements View.OnLayoutChangeListener, GiftStorePopuWindow.OnSendClickListener {
    // 发送礼物频率控制使用
    private long lastClickTime = 0;
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
    protected String channelId;
    private GiftStorePopuWindow giftStorePopuWindow;

    private String cname;
    private String liveStatus;
    private int myGold;
    private AudienceCallBack audienceCallBack;
    private ContentLoader contentLoaderAudience;
    protected List<GiftDataResultBean> giftSresult;
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
    private CustomNewUserInforDialog userInfoDialog;
    private CustomChatDialog customChatDialog;

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
        LiveConstant.ROLE = 0;
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
            if (liveAttentionStatusBean.getReturnCode() == 0) {
                LiveAttentionStatusBean.ResultBean result = liveAttentionStatusBean.getResult();
                int userStatus = result.getStatus();
                overAttentionStatus = userStatus;
                if (userStatus == 0) {
                    overAttention.setText(getString(R.string.live_master_attention));
                } else if (userStatus == 1) {
                    overAttention.setText(getString(R.string.live_attention_ok));

                } else if (userStatus == 2) {
                    overAttention.setText(getString(R.string.live_attention_mutual));
                }
            }
        }
        @Override
        public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
            super.onLiveCancelAttention(liveCancelAttention);
            if (liveCancelAttention.getReturnCode() == 0) {
                overAttentionStatus = 0;
                overAttention.setText(getString(R.string.live_master_attention));
            }
        }
        @Override
        public void onLiveDetails(LiveDetailsDataResp liveDetailsDataResp) {
            super.onLiveDetails(liveDetailsDataResp);
            if (liveDetailsDataResp.getReturnCode() == 0) {
                liveRowsBean = liveDetailsDataResp.getResult();
                liveStatus = String.valueOf(liveRowsBean.getStatus());
                int type = liveRowsBean.getType();
                if ("0".equals(liveStatus)) {
                    showFinishLayout(true, 2);
                }else{
                    initData(liveRowsBean);
                }
                AppLog.d("TAG","用户端拉流type:"+type);
                if(type==2){//声网
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    AgoraAudienceFragment agoraAudienceFragment=new AgoraAudienceFragment();
                    MyAgoraAudienceCallBackListener myAgoraAudienceCallBackListener=new MyAgoraAudienceCallBackListener();
                    agoraAudienceFragment.setAgoraAudienceCallBackListener(myAgoraAudienceCallBackListener);
                    Bundle bundle=new Bundle();
                    bundle.putString(KeyParams.CNAME,liveRowsBean.getCname());
                    agoraAudienceFragment.setArguments(bundle);
                    ft.add(R.id.player_layout, agoraAudienceFragment);
                    ft.commit();
                }else if(type==0){//备用拉流
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    IJKPlayerFragment ijkPlayerFragment=new IJKPlayerFragment();
                    MyIJKPlayerCallBackListener myIJKPlayerCallBackListener = new MyIJKPlayerCallBackListener();
                    ijkPlayerFragment.setAgoraAudienceCallBackListener(myIJKPlayerCallBackListener);
                    Bundle bundle=new Bundle();
                    bundle.putString(KeyParams.PULLURL,liveRowsBean.getPullUrl());
                    ijkPlayerFragment.setArguments(bundle);
                    ft.add(R.id.player_layout, ijkPlayerFragment);
                    ft.commit();
                }

            }
        }

        @Override
        public void onGiftsStore(GiftDataResp giftDataResp) {
            if (giftDataResp.getReturnCode() == 0) {
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
                        if (overAttentionStatus == 0) {
                            overAttention.setText(getString(R.string.live_master_attention));
                        } else {
                            overAttention.setText(R.string.live_attention_ok);
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
    private void initData(LiveRowsBean liveRowsBean) {
        nickname = liveRowsBean.getUser().getNickName();
        avatar = liveRowsBean.getUser().getAvatar();
        playType = String.valueOf(liveRowsBean.getType());
        liveTitle = liveRowsBean.getTitle();
        channelId = String.valueOf(liveRowsBean.getId());
        cname = liveRowsBean.getCname();
        shareVO = liveRowsBean.getShareVO();
        roomId = String.valueOf(liveRowsBean.getRoomId());
        LiveConstant.ROOM_ID = roomId;
        LiveConstant.liveTitle = liveRowsBean.getTitle();
        LiveConstant.liveLocation=liveRowsBean.getAddress();
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        url = liveRowsBean.getPullUrl();
        LiveConstant.creatorAccid = liveRowsBean.getCreaterAccId();
        userId = String.valueOf(liveRowsBean.getUser().getId());
        if (avatar != null) {
            blurView.setBlurImageURL(avatar);
            blurView.setScaleRatio(20);
            blurView.setBlurRadius(1);
        }
        contentLoaderAudience.getLiveUserInfo(String.valueOf(UserHelper.getUserId(AudienceActivity.this)));
        getParameter(liveRowsBean);
        registerObservers(true);

    }

    public class MyAgoraAudienceCallBackListener implements AgoraAudienceCallBackListener {
        @Override
        public void onFirstRemoteVideoDecoded() {
            AppLog.i("TAG","用户端接受第一帧视频");
            masterComeBack = false;
            renderRemoteUi();
        }

        @Override
        public void onJoinChannelSuccess() {
            playerConnectFail();
        }
    }

    public class MyIJKPlayerCallBackListener implements IJKPlayerFragment.IJKPlayerCallBackListener {

        @Override
        public void onPreparedListener() {
            masterComeBack = false;
            AppLog.d("TAG","观看。。。直播开始了");
            renderRemoteUi();
        }
    }


    @Override
    protected int getActivityLayout() {
        return R.layout.audience_activity;
    }
    @Override
    protected int getLayoutId() {
        return R.id.live_layout;
    }

    /**
     * 初始化控件
     */
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

    boolean isFirstCheckNet = true;//弹一次提示网络的dialog
    /**
     * 网络类型变化监听
     * @param netType
     * @param reminder
     */
    @Override
    protected void checkNetInfo(String netType, int reminder) {

        if (NET_TYPE_RESTS.equals(netType)) {
            if (reminder == 1&&customChatDialog!=null) {
                customChatDialog.dismiss();
            }
            if (reminder == 0 && isFirstCheckNet && isAudienceOver) {
                LiveConstant.NET_CHECK = 1;
                isFirstCheckNet = false;
                if (LiveConstant.isUnDestory) {
                    if(customChatDialog==null){
                        customChatDialog = new CustomChatDialog(AudienceActivity.this);

                    }
                    customChatDialog.setTitle(getString(R.string.live_hint));
                    customChatDialog.setContent(getString(R.string.live_net_type_cmcc));
                    customChatDialog.setCancelable(false);
                    customChatDialog.setCancelBtn(getString(R.string.live_continue_look), new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            LiveConstant.NET_CHECK = 0;
                        }
                    });
                    customChatDialog.setSurceBtn(getString(R.string.live_over), new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            LiveConstant.NET_CHECK = 0;
                            if (LiveCache.getLoginChatRoomStatus()) {
                                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                            }

                            clearChatRoom();
                        }
                    });
                    customChatDialog.show();
                }

            }

        }
    }

   public static  boolean masterComeBack = true;
    //计算主播离开时间，若果超过30秒还未回来，就显示主播离开界面，masterComeBack：标记主播离开回来状态
    public static final int MASTER_LEAVEL = 0x01;

    /**
     * 计算主播主播离开的时间
     */
    public void masterLeaveTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!masterComeBack) {
                    handler.sendEmptyMessage(MASTER_LEAVEL);
                }
            }
        }, 30000);
    }

    //视频连接超时
    public static final int VIDEO_CONNECT_FAIUL = 0x02;

    public void playerConnectFail() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(VIDEO_CONNECT_FAIUL);
            }
        }, 30000);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLog.print("audienActivity onPasue____");

    }

    @Override
    protected void onDestroy() {
        AppLog.print("audienActivity onDestroy____");
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
            if(customChatDialog==null){
                customChatDialog = new CustomChatDialog(this);
            }
            customChatDialog.setContent(getString(R.string.finish_confirm));
            customChatDialog.setCancelable(false);
            customChatDialog.setCancelBtn(getString(R.string.cancel), null);
            customChatDialog.setSurceBtn(getString(R.string.confirm), new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    if (LiveCache.getLoginChatRoomStatus()) {
                        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    }
                    clearChatRoom();
                }
            });
            customChatDialog.show();
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


    }

    @Override
    protected void showStatusUnUsual() {
        try {
            if (LiveConstant.isUnDestory) {
                if(customChatDialog==null){
                    customChatDialog = new CustomChatDialog(AudienceActivity.this);

                }
                customChatDialog.setContent(getString(R.string.live_status_inusual));
                customChatDialog.setCancelable(false);
                customChatDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        finishLive();
                        firstWarning = true;
                    }
                });
                customChatDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                customChatDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void showUserInfoDialog(final String userIds, final String channelIds, boolean isMaster) {
        if(userInfoDialog==null){
            userInfoDialog = new CustomNewUserInforDialog(this, container, userIds, channelIds, LiveConstant.ROLE, isMaster, creatorAccount, roomId);
        }
        userInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                userInfoDialog=null;
            }
        });
        userInfoDialog.setDialogStatusListener(this);
        userInfoDialog.show();

    }
    protected void showInputTextView() {
        boolean logineds = UserHelper.isLogined(AudienceActivity.this);
        if (!logineds) {
            showLoginViewDialog();
        } else if (LiveCache.getLoginChatRoomStatus()) {
            keyboardLayout.setAlpha(1.0f);
            keyboardLayout.setClickable(true);
            liveSettingLayout.setVisibility(View.GONE);
            if (inputPanel != null) {
                inputPanel.switchToTextLayout(true);
            }
        } else {
            Toast.makeText(AudienceActivity.this, getString(R.string.unlogin_chatroom_again_login), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void liveCommonSetting() {
        MobHelper.sendEevent(this, MobEvent.LIVE_USER_SHARE);
        showSharePopuwindow(shareVO, getString(R.string.share_live));
    }
    @Override
    protected void superManagerKickOutUser() {
        Toast.makeText(this, getString(R.string.tickout_live_room), Toast.LENGTH_SHORT).show();
        finishLive();
    }
    //超级管理员关闭直播间
    @Override
    protected void closeLiveNotifi(IMMessage message) {
        if(userInfoDialog!=null){
            userInfoDialog.dismiss();
        }
    }

    /**
     * 监听主播离开，回来
     * @param b
     */
    @Override
    protected void masterOnLineStatus(boolean b) {
        if (b) {
            masterComeBack = true;
            showFinishLayout(false, 2);
        } else {
            masterComeBack = false;
            masterLeaveTime();
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ATTENTION_CODE:
                    showAttentionDialog();
                    break;
                case MASTER_LEAVEL:
                    showFinishLayout(true, 2);
                    break;
                case VIDEO_CONNECT_FAIUL:
                    if(audienceOver.getVisibility()!=View.VISIBLE){
                        videoConnectDialog();
                    }
                    break;
            }
        }
    };

    public void videoConnectDialog() {
        try {
            if (!masterComeBack && LiveConstant.isUnDestory) {
                if(customChatDialog==null){
                    customChatDialog = new CustomChatDialog(AudienceActivity.this);
                }
                customChatDialog.setContent(getString(R.string.live_video_conncet_fail));
                customChatDialog.setCancelable(false);
                customChatDialog.setCancelBtn(getString(R.string.live_quit_room), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        if (LiveCache.getLoginChatRoomStatus()) {
                            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                        }
                        clearChatRoom();
                    }
                });
                customChatDialog.setSurceBtn(getString(R.string.live_again_conncet), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        loadingPageLayout.setVisibility(View.VISIBLE);
                        isAudienceOver = true;
                        loadingPage.setVisibility(View.GONE);
                        audienceOver.setVisibility(View.GONE);
                          renderRemoteUi();
                    }
                });
                customChatDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示关注主播的窗口
     */
    private void showAttentionDialog() {
        try {
            if (!LiveConstant.isAttention&&overAttentionStatus == 0 && LiveConstant.isUnDestory && audienceOver.getVisibility() != View.VISIBLE) {
                LiveAttentionPopuwindow popuwindow = new LiveAttentionPopuwindow(AudienceActivity.this, liveRowsBean);
                popuwindow.setOnUserAttentionListener(new LiveAttentionPopuwindow.OnUserAttentionListener() {
                    @Override
                    public void getAttention() {
                        if (container != null && container.account != null && creatorAccount != null) {
                            LiveMessage liveMessage = new LiveMessage();
                            liveMessage.setStyle(MessageType.text);
                            liveMessage.setUserId(String.valueOf(UserHelper.getUserId(AudienceActivity.this)));
                            liveMessage.setCreatorAccount(creatorAccount);
                            liveMessage.setChannelId(channelId);
                            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, getString(R.string.attention_live_e), roomId, AuthPreferences.getUserAccount(), liveMessage);
                            AudienceActivity.this.sendMessage(imMessage, MessageType.text);
                        }
                    }

                    @Override
                    public void goLoginActivity() {
                        showLoginViewDialog();
                    }
                });
                popuwindow.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    boolean isClickSettingLayout = false;
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
                    //TODO 点击送礼物
                    MobHelper.sendEevent(AudienceActivity.this, MobEvent.LIVE_USER_GIFT);
                        boolean loginedq = UserHelper.isLogined(AudienceActivity.this);
                        if (!loginedq) {
                            showLoginViewDialog();
                        } else if (!LiveCache.getLoginChatRoomStatus() || !LiveCache.getLoginStatus()) {
                            Toast.makeText(AudienceActivity.this, getString(R.string.live_unlogin_channel_room), Toast.LENGTH_SHORT).show();
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

                    break;


                case R.id.live_layout:
                    if (UserHelper.isLogined(AudienceActivity.this) && LiveCache.getLoginStatus()) {
                        periscopeLayout.addHeart();
                        sendLike();
                    }
                    onImHiden();
                    break;
                case R.id.master_info_head_iv:
                    Intent intent3 = new Intent(AudienceActivity.this, LiveHomePageActivity.class);
                    intent3.putExtra("userId", userId);
                    startActivity(intent3);
                    break;

                case R.id.live_telecast_input_text:
                    isClickSettingLayout = true;
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

    private WalletContent content;

    //显示礼物布局
    private void showGiftPage(int gold) {
        liveSettingLayout.setVisibility(View.GONE);
        if (LiveConstant.isUnDestory) {
            if(giftStorePopuWindow==null){
                giftStorePopuWindow = new GiftStorePopuWindow(this, giftSresult);
            }
            giftStorePopuWindow.showGiftStorePopuWindow(gold);
            giftStorePopuWindow.showAtLocation(this.findViewById(R.id.live_layout),
                    Gravity.BOTTOM, 0, 0);
            giftStorePopuWindow.setOnSendClickListener(this);
            giftStorePopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    liveSettingLayout.setVisibility(View.VISIBLE);
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
        String messageContent = "给主播送了" + sendTotal+"个"+senGiftContent(giftDataResultBean.getId());
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
        liveMessage.setUserId(String.valueOf(UserHelper.getUserId(AudienceActivity.this)));
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

    private  String  senGiftContent(int giftId){
        switch (giftId){
            case LiveParams.ROSE_CODE:
                return getString(R.string.gift_rose);
            case LiveParams.TRAVELLINGCASE_CODE:
                return getString(R.string.gift_travellingcase);
            case LiveParams.PLAN_CODE:
                return getString(R.string.gift_plan);
            case LiveParams.FACE_CODE:
                return getString(R.string.gift_face);
            case LiveParams.GLASSES_CODE:
                return getString(R.string.gift_glasses);
            case LiveParams.UMBRELLA_CODE:
                return getString(R.string.gift_umbrella);
            case LiveParams.STAR_CODE:
                return getString(R.string.gift_star);
            case LiveParams.FRUIT_CODE:
                return getString(R.string.gift_fruit);
            case LiveParams.WATER_CODE:
                return getString(R.string.gift_water);
            case LiveParams.CHRISTMAS_TREES_CODE:
                return getString(R.string.gift_christmas_trees);
           default:
               return getString(R.string.gift_mystery_gift);
        }
    }

    private void finishLive() {
        if (isStartLive) {
            logoutChatRoom();
        } else {
            if (LiveCache.getLoginChatRoomStatus()) {
                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            }
            clearChatRoom();
        }
    }

    /*************************
     * 点赞爱心
     ********************************/
    boolean isSendLike = true;
    public void sendLike() {
        if (!isFastClick() && container != null && container.account != null && creatorAccount != null) {
            IMMessage imMessage = null;
            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setStyle(MessageType.like);
            liveMessage.setUserId(String.valueOf(UserHelper.getUserId(AudienceActivity.this)));
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            liveMessage.setDisableSendMsgUserId(String.valueOf(UserHelper.getUserId(AudienceActivity.this)));
            if (isSendLike) {
                isSendLike = false;
                imMessage = SendMessageUtil.sendMessage(container.account, getString(R.string.like_master), roomId, AuthPreferences.getUserAccount(), liveMessage);
            } else {
                imMessage = SendMessageUtil.sendMessage(container.account, getString(R.string.live_master_1), roomId, AuthPreferences.getUserAccount(), liveMessage);
            }
            sendMessage(imMessage, MessageType.like);

        }
    }

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

    // 显示和隐藏直播已结束布局
    boolean isAudienceOver = true;
    protected void showFinishLayout(boolean liveEnd, int reminder) {
        if ((reminder == 0||reminder==1)&&customChatDialog!=null) {
            customChatDialog.dismiss();
        }
        if (isAudienceOver && liveEnd) {
            isAudienceOver = false;
            if(userInfoDialog!=null){
                userInfoDialog.dismiss();
            }
            palyerLayout.removeAllViews();
            drawerLayout.closeDrawer(Gravity.RIGHT);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
             renderRemoteUi();
        }
    }

    private void showOverInfo() {
        DrawableUtils.displayImg(this, headIv, avatar);
        masterName.setText(nickname);
    }

    /**
     * 显示充值dialog
     * @param content
     */
    //显示充值dialog
    private void showRechargeDialog(final WalletContent content) {
        if (LiveConstant.isUnDestory) {
            if(customChatDialog==null){
                customChatDialog = new CustomChatDialog(AudienceActivity.this);
            }
            customChatDialog.setTitle(getString(R.string.live_hint));
            customChatDialog.setContent(getString(R.string.live_recharge_hint));
            customChatDialog.setCancelable(false);
            customChatDialog.setCancelBtn(getString(R.string.live_canncel), null);
            customChatDialog.setSurceBtn(getString(R.string.recharge), new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    LiveConstant.IS_FIRST_CLICK_PAGE = true;
                    Intent intent = new Intent(AudienceActivity.this, RechargeActivity.class);
                    intent.putExtra(KeyParams.WALLET_CONTENT, content);
                    startActivity(intent);
                    giftStorePopuWindow.dismiss();
                }
            });
            customChatDialog.show();
        }
    }
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (isClickSettingLayout) {
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {//显示
                if (liveSettingLayout != null && keyboardLayout != null) {
                    keyboardLayout.setAlpha(1);
                    keyboardLayout.setClickable(true);
                    keyboardLayout.setVisibility(View.VISIBLE);
                    liveSettingLayout.setVisibility(View.INVISIBLE);
                    liveSettingLayout.setClickable(false);
                    topView.setVisibility(View.GONE);
                    AppLog.i("TAG", "用户端软键盘显示");
                }
            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//隐藏
                if (keyboardLayout != null) {
                    keyboardLayout.setAlpha(0);
                    keyboardLayout.setClickable(false);
                    keyboardLayout.setVisibility(View.INVISIBLE);
                    liveSettingLayout.setVisibility(View.VISIBLE);
                    liveSettingLayout.setClickable(true);
                    topView.setVisibility(View.VISIBLE);
                    AppLog.i("TAG", "用户端软键盘隐藏");
                }
            }
        }

    }
    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }

    boolean isShowAttentionDialog=true;
    protected void renderRemoteUi(){
        if (messageListPanel != null) {
            messageListPanel.setHeaderViewVisible();
        }
        masterComeBack = true;
        loadingPage.setVisibility(View.GONE);
        contentLoaderAudience.getLiveUserInfo(userId);
        if (showAttentionRunnable == null) {
            showAttentionRunnable = new ShowAttentionRunnable();
        }
        if(isShowAttentionDialog){
            isShowAttentionDialog=false;
            handler.postDelayed(showAttentionRunnable,60000);
        }
        if (audienceOver != null) {
            isAudienceOver = true;
            audienceOver.setVisibility(View.GONE);
        }
    }



    public static final int ATTENTION_CODE = 0x00;
    ShowAttentionRunnable showAttentionRunnable = null;

    private class ShowAttentionRunnable implements Runnable {
        @Override
        public void run() {
            handler.sendEmptyMessage(ATTENTION_CODE);
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

    /**
     * 按音量键增减播放器音量
     * @param keyCode
     * @param event
     * @return
     */
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
