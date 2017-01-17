package com.lalocal.lalocal.live.entertainment.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.activity.fragment.PersonalMessageFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.im.ChatFragment;
import com.lalocal.lalocal.live.LiveCache;
import com.lalocal.lalocal.live.base.util.DialogUtil;
import com.lalocal.lalocal.live.entertainment.adapter.GiftAdapter;
import com.lalocal.lalocal.live.entertainment.adapter.TouristAdapter;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.LiveParams;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.GiftAnimations;
import com.lalocal.lalocal.live.entertainment.helper.GiftPlaneAnimation;
import com.lalocal.lalocal.live.entertainment.helper.MessageUpdateListener;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.LiveGiftRanksResp;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListResp;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.model.LiveRoomAvatarSortResp;
import com.lalocal.lalocal.live.entertainment.model.OnLineUser;
import com.lalocal.lalocal.live.entertainment.model.RankUserBean;
import com.lalocal.lalocal.live.entertainment.model.TotalRanksBean;
import com.lalocal.lalocal.live.entertainment.model.UserAvatarsBean;
import com.lalocal.lalocal.live.entertainment.module.ChatRoomMsgListPanel;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomLinearLayoutManager;
import com.lalocal.lalocal.live.entertainment.ui.GiftsRankPopuWindow;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.im.session.BarrageViewBean;
import com.lalocal.lalocal.live.im.session.Container;
import com.lalocal.lalocal.live.im.session.ModuleProxy;
import com.lalocal.lalocal.live.im.session.actions.BaseAction;
import com.lalocal.lalocal.live.im.session.input.InputConfig;
import com.lalocal.lalocal.live.im.session.input.InputPanel;
import com.lalocal.lalocal.live.im.ui.barrage.BarrageConfig;
import com.lalocal.lalocal.live.im.ui.barrage.BarrageView;
import com.lalocal.lalocal.live.im.ui.dialog.DialogMaker;
import com.lalocal.lalocal.live.im.ui.periscope.PeriscopeLayout;
import com.lalocal.lalocal.live.im.ui.widget.MarqueeView;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.TouristInfoResp;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CheckWeixinAndWeibo;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomStatusChangeData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 直播端和观众端的基类
 */
public abstract class LivePlayerBaseActivity extends BaseActivity implements ModuleProxy, MessageUpdateListener {
    public static final int LIVE_BASE_RESQUEST_CODE = 701;
    public static  String CHANNELID_ID="";
    // 聊天室信息
    protected String roomId;
    protected String url; // 推流/拉流地址
    protected InputPanel inputPanel;
    protected ChatRoomMsgListPanel messageListPanel;
    // view
    protected DrawerLayout drawerLayout;
    private View view;
    protected TextView onlineCountText; // 在线人数view
    protected TextView masterName;
    protected CircleImageView maseterHead;
    protected TextView inputChar;
    private RelativeLayout giftAnimationViewDown; // 礼物动画布局1
    private RelativeLayout giftAnimationViewUp; // 礼物动画布局2
    protected PeriscopeLayout periscopeLayout; // 点赞爱心布局
    private RecyclerView touristList;
    protected EditText editTextInput;
    private LinearLayout masterInfoLayout;
    private BarrageView barrageView;
    protected Container container;
    // data
    protected GiftAdapter adapter;
    protected GiftAnimations giftAnimation; // 礼物动画
    private AbortableFuture<EnterChatRoomResultData> enterRequest;
    private NetworkInfo netInfo;
    protected TouristAdapter tourisAdapter;
    ChatRoomMember member1;
    private SpecialShareVOBean shareVO;
    public int onlineCounts;
    public String avatar;
    protected String userId;
    protected String creatorAccount;//聊天室创建者账号
    private ContentLoader contentLoader;
    protected ImageView giftPlaneUp;
    protected RelativeLayout giftPlaneBg;
    protected ImageView userHeadImg, anchorHeadImg;
    private String code;
    private String channelId;
    protected RelativeLayout scoreLayout;
    protected GiftPlaneAnimation giftPlaneAnimation;
    protected FrameLayout palyerLayout;
    protected ImageView liveGiftImg;
    private InputConfig inputConfig;
    private MyCallBack myCallBack;
    protected GiftsRankPopuWindow giftsRankPopuWindow;
    protected ImageView settingLiveImg;
    private EnterChatRoomRequestCallback enterChatRoomRequestCallback;
    protected MarqueeView marqueeView;
    private ImageView overLiveShareFriends;
    private ImageView overLiveShareWeibo;
    private ImageView overLiveShareWeixin;
    private ImageView msgImg;
    protected ImageView quit;
    protected View topView;
    private TextView newMessageRemind;
    private boolean isFirstEnterRoom = true;
    boolean isLiveMaster;
    OnLinesRunnable onLInesRunnable = null;
    boolean isOnLineUsersCountChange = true;
    private int liveNumber;
    boolean firstClick = true;//排行榜
    private Handler handler = new Handler();
    private TextView unReadTv;
    private CustomChatDialog customChatDialog;
    private ImageView liveQuit;

    protected abstract void checkNetInfo(String netType, int reminder);

    protected abstract void onConnected(); // 网络连上

    protected abstract void onDisconnected(); // 网络断开

    protected abstract int getActivityLayout(); // activity布局文件

    protected abstract int getLayoutId(); // 根布局资源id



    protected abstract void masterOnLineStatus(boolean b);

    protected abstract void showFinishLayout(boolean b, int i);

    protected abstract void showUserInfoDialog(String userId, String channelId, boolean isMaster);//用户信息dialog

    protected abstract void liveCommonSetting();

    protected abstract void superManagerKickOutUser();

    protected abstract void closeLiveNotifi(IMMessage message);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  //应用运行时，保持屏幕高亮，不锁屏
        contentLoader = new ContentLoader(this);
        myCallBack = new MyCallBack();
        contentLoader.setCallBack(myCallBack);
        findViews();
        updateUnReadMsg();
        checkSharePlatform();//检测手机安装分享平台
        registerNetStatus();
    }


    private void checkManagerList() {
        contentLoader.getLiveManagerList(channelId);//查看管理员列表
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLog.i("TAG", "直播基类登录回调：" + resultCode);
        if (requestCode == LIVE_BASE_RESQUEST_CODE && resultCode == MeFragment.LOGIN_OK) {
            if (data != null) {
                AppLog.i("TAG", "直播基类登录回调rere：" + resultCode);
                ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
                try {
                    NIMClient.getService(AuthService.class).logout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void getParameter(LiveRowsBean liveRowsBean) {
        roomId = String.valueOf(liveRowsBean.getRoomId());
        accId = liveRowsBean.getCreaterAccId();
        nickName = liveRowsBean.getUser().getNickName();
        url = liveRowsBean.getPullUrl();
        Object ann = liveRowsBean.getAnnoucement();
        String annoucement = null;
        if (ann != null) {
            annoucement = ann.toString();
        } else {
            annoucement = "这是公告哈";
        }
        userId = String.valueOf(liveRowsBean.getUser().getId());
        liveNumber = liveRowsBean.getNumber();
        shareVO = liveRowsBean.getShareVO();
        channelId = String.valueOf(liveRowsBean.getId());
        CHANNELID_ID=channelId;
        avatar = liveRowsBean.getUser().getAvatar();
        String title = liveRowsBean.getTitle();
        if (LivePlayerBaseActivity.this instanceof LiveActivity) {
            title = getString(R.string.start_live);
        } else {
            title = getString(R.string.liveing) + title;
        }
        container = new Container(this, roomId, SessionTypeEnum.ChatRoom, this);
        if (messageListPanel == null) {
            messageListPanel = new ChatRoomMsgListPanel(container, view, annoucement, title, LivePlayerBaseActivity.this, newMessageRemind);
        }
        // 礼物动画展示
        findGiftLayout();
        checkManagerList();
    }

    protected void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);//消息接收
        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineStatus, register);  //检测聊天室登录状态
        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register); //监听云信账号登录状态
    }

    //监听云信账号登录状态
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            try {
                if (statusCode != StatusCode.LOGINED) {
                    LiveCache.setLoginStatus(false);
                    if (statusCode == StatusCode.UNLOGIN || statusCode == StatusCode.NET_BROKEN) {
                        getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String userAccount = AuthPreferences.getUserAccount();
                                String userToken = AuthPreferences.getUserToken();
                                if (userAccount != null && userToken != null) {
                                    loginIMServer(userAccount, userToken);
                                } else {
                                    contentLoader.getTouristInfo();
                                }
                            }
                        }, 1500);
                    }
                } else if (statusCode == StatusCode.LOGINED) {
                    LiveCache.setLoginStatus(true);
                    LiveCache.setAccount(AuthPreferences.getUserAccount());
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enterRoom();
                        }
                    }, 2000);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    //检测聊天室登录状态
    Observer<ChatRoomStatusChangeData> onlineStatus = new Observer<ChatRoomStatusChangeData>() {
        @Override
        public void onEvent(ChatRoomStatusChangeData chatRoomStatusChangeData) {
            if (chatRoomStatusChangeData.status == StatusCode.CONNECTING) {
                LiveCache.setLoginChatRoomStatus(false);
                if (marqueeView != null) {
                    marqueeView.start(getString(R.string.connectting_chatroom));
                }

            } else if (chatRoomStatusChangeData.status == StatusCode.UNLOGIN) {
                int enterErrorCode = NIMClient.getService(ChatRoomService.class).getEnterErrorCode(roomId);
                AppLog.i("TAG", "进入聊天室失败，获取enterErrorCode：" + enterErrorCode);
                onOnlineStatusChanged(false);
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINING) {

                LiveCache.setLoginChatRoomStatus(false);
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINED) {
                onOnlineStatusChanged(true);

            } else if (chatRoomStatusChangeData.status.wontAutoLogin()) {

            } else if (chatRoomStatusChangeData.status == StatusCode.NET_BROKEN) {
                onOnlineStatusChanged(false);
                if (marqueeView != null) {
                    marqueeView.start(getString(R.string.connect_chatroom_fuial));
                }
            }
            AppLog.i("TAG", "Chat Room Online Status:" + chatRoomStatusChangeData.status.name());
        }
    };

    //踢出聊天室
    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
        @Override
        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
            AppLog.i("TAG", "监听到被踢出聊天室");
            clearChatRoom();
        }
    };

    //检测网络类型
    private void registerNetStatus() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
    }


    /****************************
     * 布局初始化
     **************************/
    protected void findViews() {
        msgImg = (ImageView) findViewById(R.id.live_telecast_top_message);
        unReadTv = (TextView) findViewById(R.id.audience_immessage_count);
        topView = findViewById(R.id.top_view);
        palyerLayout = (FrameLayout) findViewById(R.id.player_layout);
        barrageView = (BarrageView) findViewById(R.id.barrageView_test);
        masterName = (TextView) findViewById(R.id.live_emcee_name);
        maseterHead = (CircleImageView) findViewById(R.id.live_emcee_head);
        touristList = (RecyclerView) findViewById(R.id.live_visitors_list_recy);
        liveGiftImg = (ImageView) findViewById(R.id.live_gift_img);
        quit = (ImageView) findViewById(R.id.live_telecast_quit);
        newMessageRemind = (TextView) findViewById(R.id.new_message_remind);
        onlineCountText = (TextView) findViewById(R.id.live_online_count);
        scoreLayout = (RelativeLayout) findViewById(R.id.audience_score_layout);
        giftPlaneUp = (ImageView) findViewById(R.id.gift_plane_up);
        giftPlaneBg = (RelativeLayout) findViewById(R.id.audient_gift_plane_bg);
        anchorHeadImg = (ImageView) findViewById(R.id.audience_anchor_headportrait);
        userHeadImg = (ImageView) findViewById(R.id.audience_user_headportrait);
        marqueeView = (MarqueeView) findViewById(R.id.live_notifitation_marquee_view);
        liveQuit = (ImageView) findViewById(R.id.live_quit);

        //直播結束，分享
        overLiveShareFriends = (ImageView) findViewById(R.id.over_page_friends);
        overLiveShareWeibo = (ImageView) findViewById(R.id.over_page_weibo);
        overLiveShareWeixin = (ImageView) findViewById(R.id.over_page_weixin);
        overLiveShareFriends.setOnClickListener(clickListener);
        overLiveShareWeibo.setOnClickListener(clickListener);
        overLiveShareWeixin.setOnClickListener(clickListener);
        msgImg.setOnClickListener(clickListener);


        // 点赞的爱心布局
        periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);
        View liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        //软键盘输入框
        editTextInput = (EditText) findViewById(R.id.editTextMessage);
        inputChar = (TextView) findViewById(R.id.live_telecast_input_text);

        liveSettingLayout.setVisibility(View.GONE);

        view = findViewById(getLayoutId());
        inputConfig = new InputConfig();
        inputConfig.isTextAudioSwitchShow = false;
        inputConfig.isMoreFunctionShow = false;
        inputConfig.isEmojiButtonShow = false;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldCollapseInputPanel();
            }
        });
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(LivePlayerBaseActivity.this, CustomLinearLayoutManager.HORIZONTAL, false);
        touristList.setLayoutManager(linearLayoutManager);
        tourisAdapter = new TouristAdapter(LivePlayerBaseActivity.this, null);
        touristList.setAdapter(tourisAdapter);
        tourisListListener();//直播间头像监听

        scoreLayout.setOnClickListener(clickListener);
        masterInfoLayout = (LinearLayout) findViewById(R.id.live_master_info_layout);
        masterInfoLayout.setOnClickListener(clickListener);

        //设置更多
        settingLiveImg = (ImageView) findViewById(R.id.live_telecast_top_setting);
        settingLiveImg.setOnClickListener(clickListener);

        drawerLayout = (DrawerLayout) findViewById(R.id.live_drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayoutListener();//drawerLayout开关监听
    }

    protected abstract void showStatusUnUsual();

    public class MyCallBack extends ICallBack {

        @Override
        public void onResponseFailed(String message, int requestCode) {
            super.onResponseFailed(message, requestCode);
            if (243 == requestCode) {
                showStatusUnUsual();
            }
        }
        @Override
        public void onSendMessage(int code) {
            super.onSendMessage(code);
            if (code == 0) {
                final ChatRoomMessage message = (ChatRoomMessage) msg;
                if (msg != null) {
                    sendGreenMessage(message);
                }
            }
        }

        @Override
        public void onManagerList(LiveManagerListResp liveManagerListResp) {
            super.onManagerList(liveManagerListResp);
            if (liveManagerListResp.getReturnCode() == 0) {
                LiveConstant.result.clear();
                LiveConstant.result = liveManagerListResp.getResult();
                isOnLineUsersCountChange = true;
                AppLog.i("TAG", "查看直播间管理员列表");
            }
        }

        @Override
        public void onLiveRoomAvatar(LiveRoomAvatarSortResp.ResultBean result) {
            super.onLiveRoomAvatar(result);
            int number = result.getNumber();
            onlineCounts = number;
            onlineCountText.setText(String.valueOf(number) + "人");
            List<UserAvatarsBean> userAvatars = result.getUserAvatars();
            if (userAvatars != null && userAvatars.size() > 0) {
                tourisAdapter.refresh(userAvatars);
            }
        }

        @Override
        public void onGiftRanks(LiveGiftRanksResp liveGiftRanksResp) {
            super.onGiftRanks(liveGiftRanksResp);
            if (liveGiftRanksResp.getReturnCode() == 0) {
                List<TotalRanksBean> currentRanks = liveGiftRanksResp.getResult().getCurrentRanks();
                if (currentRanks != null && currentRanks.size() > 0) {
                    for (int i = 0; i < currentRanks.size(); i++) {
                        totalGold = totalGold + currentRanks.get(i).getGold();
                    }
                }
                if (LiveConstant.isUnDestory) {
                    showGiftRanksPopuWindow(liveGiftRanksResp);
                }

            }
        }

        @Override
        public void onGetAudienceOnLineUserCount(String json) {
            super.onGetAudienceOnLineUserCount(json);
            OnLineUser onLineUser = new Gson().fromJson(json, OnLineUser.class);
            if (onLineUser != null && onLineUser.getResult() > 0) {
                AppLog.i("TAG", "从服务器拉去人数:" + onLineUser.getResult());
                onlineCountText.setText(String.valueOf(onLineUser.getResult()) + "人");
            }
        }

        @Override
        public void onTouristInfo(String json) {
            super.onTouristInfo(json);
            TouristInfoResp touristInfoResp = new Gson().fromJson(json, TouristInfoResp.class);
            if (touristInfoResp.getReturnCode() == 0) {
                TouristInfoResp.ResultBean result = touristInfoResp.getResult();
                if (result == null) {
                    return;
                }
                final String accid = result.getAccid();
                final String token = result.getToken();
                if (accid != null || token != null) {
                    LiveCache.clear();
                    AuthPreferences.saveUserAccount(accid);
                    AuthPreferences.saveUserToken(token);
                    loginIMServer(accid, token);
                }
            }
        }

    }

    int totalGold = 0;

    protected void loginIMServer(final String imccId, String imToken) {

        NIMClient.getService(AuthService.class).login(new LoginInfo(imccId, imToken)).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                AppLog.i("TAG", "LivePlayer,手动登录成功");
                LiveCache.setAccount(imccId);
                LiveCache.getRegUserInfo();
                LiveCache.setLoginStatus(true);
            }

            @Override
            public void onFailed(int i) {
                AppLog.i("TAG", "NewsFragment,手动登录失败" + i);
                LiveCache.setLoginStatus(false);
            }

            @Override
            public void onException(Throwable throwable) {
                AppLog.i("TAG", "NewsFragment,手动登录失败2");
                LiveCache.setLoginStatus(false);
            }

        });
    }

    /***************************
     * 监听
     ****************************/

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.live_telecast_top_message:
                    if (UserHelper.isLogined(LivePlayerBaseActivity.this)) {
                        gotoPersonalMessage(false, accId, nickName);
                    } else {
                        showLoginViewDialog();
                    }
                    break;

                case R.id.live_master_info_layout:
                    if (UserHelper.isLogined(LivePlayerBaseActivity.this)) {
                        if (userId != null && channelId != null) {
                            if (LivePlayerBaseActivity.this instanceof AudienceActivity) {
                                MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_USER_ANCHOR);
                                showUserInfoDialog(userId, channelId, true);
                            }else if(LivePlayerBaseActivity.this instanceof LiveActivity){
                                contentLoader.liveGiftRanks(channelId);
                            }
                        }
                    } else {
                        showLoginViewDialog();
                    }

                    break;
                case R.id.live_telecast_top_setting:
                    liveCommonSetting();
                    break;
                case R.id.audience_score_layout:
                    MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_ANCHOR_LIST);
                    if (firstClick) {
                        boolean logineds = UserHelper.isLogined(LivePlayerBaseActivity.this);
                        if (logineds) {
                            firstClick = false;
                            contentLoader.liveGiftRanks(channelId);
                        }
                    }
                    break;
                case R.id.over_page_friends:
                    overLiveShareFriends.setSelected(true);
                    overLiveShareWeibo.setSelected(false);
                    overLiveShareWeixin.setSelected(false);
                    liveShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;
                case R.id.over_page_weibo:
                    overLiveShareFriends.setSelected(false);
                    overLiveShareWeibo.setSelected(true);
                    overLiveShareWeixin.setSelected(false);
                    liveShare(SHARE_MEDIA.SINA);

                    break;
                case R.id.over_page_weixin:
                    overLiveShareFriends.setSelected(false);
                    overLiveShareWeibo.setSelected(false);
                    overLiveShareWeixin.setSelected(true);
                    liveShare(SHARE_MEDIA.WEIXIN);
                    break;
            }
        }
    };

    //消息接受监听
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        private String messageUserId;
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            int styles = -1;
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
                    if (LiveParams.STYLE.equals(key)) {
                        styles = Integer.parseInt(value.toString());
                    }
                    if (LiveParams.USERID.equals(key)) {
                        messageUserId = value.toString();
                    }
                    if (LiveParams.CREATORACCOUNT.equals(key)) {
                        String creatorAccount = value.toString();
                        if (creatorAccount == null || LiveParams.NULL.equals(creatorAccount)) {
                            return;
                        }
                    }
                    if (LiveParams.GIFTMODEL.equals(key)) {
                        String text = value.toString();
                        if (text != null && !LiveParams.NULL.equals(text)) {
                            Map<String, Object> map = (Map<String, Object>) value;
                            Iterator<Map.Entry<String, Object>> mapItem = map.entrySet().iterator();
                            while (mapItem.hasNext()) {
                                Map.Entry<String, Object> next1 = mapItem.next();
                                String key1 = next1.getKey();
                                Object value1 = next1.getValue();
                                if (LiveParams.CODE.equals(key1)) {
                                    code = value1.toString();
                                }
                            }
                        }
                    }
                }
            }

            switch (styles) {
                case MessageType.text://聊天
                    messageListPanel.onIncomingMessage(messages);
                    break;
                case MessageType.barrage://弹幕
                    ChatRoomMessage barrageMessage = (ChatRoomMessage) message;
                    String senderNick = barrageMessage.getChatRoomMessageExtension().getSenderNick();
                    String senderAvatar = barrageMessage.getChatRoomMessageExtension().getSenderAvatar();
                    AppLog.i("TAG", "获取用户头像:" + senderAvatar);
                    String content = barrageMessage.getContent();
                    if (senderNick != null) {
                        barrageView.setVisibility(View.VISIBLE);
                        barrageView.init(new BarrageConfig());
                        if (content != null) {
                            BarrageViewBean barrageBean = new BarrageViewBean();
                            barrageBean.setUserId(messageUserId);
                            barrageBean.setContent(content);
                            barrageBean.setAvator(senderAvatar);
                            barrageBean.setSenderName(senderNick);
                            barrageView.addTextBarrage(barrageBean);
                        }
                        messageListPanel.onIncomingMessage(messages);
                        barrageView.setOnBarrageClickListener(new BarrageView.OnBarrageClickListener() {
                            @Override
                            public void getUserId(String userId) {
                                showUserInfoDialog(userId, channelId, false);
                            }
                        });
                    }

                    break;
                case MessageType.like:
                    periscopeLayout.addHeart();
                    AppLog.i("TAG", "点赞内容监听:" + message.getContent());
                    if (message.getContent().equals(getString(R.string.like_master))) {
                        messageListPanel.onIncomingMessage(messages);
                    }
                    break;
                case MessageType.gift:
                    if (LiveParams.PLAN.equals(code)) {
                        giftPlaneAnimation.showPlaneAnimation((ChatRoomMessage) message);
                    } else if (code != null) {
                        giftAnimation.showGiftAnimation((ChatRoomMessage) message);
                    }
                    messageListPanel.onIncomingMessage(messages);
                    break;
                case MessageType.ban://禁言
                    messageListPanel.onIncomingMessage(messages);
                    break;
                case MessageType.relieveBan://解除禁言
                    messageListPanel.onIncomingMessage(messages);

                    break;
                case MessageType.managerLive:
                    if (channelId != null) {
                        contentLoader.getLiveManagerList(channelId);
                    }
                    messageListPanel.onIncomingMessage(messages);
                    break;
                case MessageType.cancel:
                    if (channelId != null) {
                        contentLoader.getLiveManagerList(channelId);
                    }
                    messageListPanel.onIncomingMessage(messages);
                    break;
                case MessageType.closeLive:
                    AppLog.i("TAG", "收到关闭直播间消息");
                    isOnLineUsersCountChange=false;
                    if(handler!=null){
                        handler.removeCallbacks(onLInesRunnable);
                    }
                    closeLiveNotifi(message);
                    break;

                case MessageType.leaveLive:
                    showFinishLayout(true, 2);
                    break;
                case MessageType.kickOut:
                    if (messageUserId != null && messageUserId.equals(String.valueOf(UserHelper.getUserId(LivePlayerBaseActivity.this)))) {
                        superManagerKickOutUser();
                    }
                    messageListPanel.onIncomingMessage(messages);
                    break;

            }

            if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();
                switch (notificationAttachment.getType()) {
                    case ChatRoomMemberIn:
                        //发送进入直播间的通知
                        isOnLineUsersCountChange = true;
                        String fromAccountIn = message.getFromAccount();
                        showNotifacatin(message);
                        if (fromAccountIn != null && creatorAccount != null) {
                            if (creatorAccount.equals(fromAccountIn) && !isFirstEnterRoom) {
                                //主播回来了
                                isFirstEnterRoom = true;
                                masterOnLineStatus(true);
                            }
                        }
                        break;
                    case ChatRoomMemberExit:
                        isOnLineUsersCountChange = true;
                        String fromAccountExit = message.getFromAccount();
                        if (creatorAccount != null && creatorAccount.equals(fromAccountExit)) {
                            //主播离开了
                            isFirstEnterRoom = false;
                            masterOnLineStatus(false);
                        }

                        break;
                    case ChatRoomInfoUpdated:
                        //直播间信息修改通知
                        //    sendMessage(message, MessageType.text);
                        break;
                    case ChatRoomClose:
                        break;
                    case ChatRoomManagerAdd:
                        break;

                }
            } else {

            }
        }
    };

    private void showNotifacatin(IMMessage message) {
        ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();
        Map<String, String> targetNicks = getTargetNicks(notificationAttachment);
        Set keys = targetNicks.keySet();
        if (keys != null) {
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                marqueeView.start(getString(R.string.welcome_live) + key.toString() + getString(R.string.enter_room));
            }
        }
    }

    private static Map<String, String> getTargetNicks(final ChatRoomNotificationAttachment attachment) {
        Map<String, String> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        List<String> accounts = attachment.getTargets();
        List<String> targets = attachment.getTargetNicks();
        if (attachment.getTargetNicks() != null) {
            for (int i = 0; i < targets.size(); i++) {
                if (LiveCache.getAccount() != null) {
                    sb.append(LiveCache.getAccount().equals(accounts.get(i)) ? "你" : targets.get(i));
                    sb.append(",");
                } else {
                    sb.append(targets.get(i));
                    sb.append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        if (attachment.getTargets() != null) {
            for (int j = 0; j < accounts.size(); j++) {
                AppLog.i("TAG", "通知的用户账号：" + accounts.get(j));
                sb1.append(accounts.get(j));
                sb1.append(",");
            }
            sb1.deleteCharAt(sb1.length() - 1);
        }
        map.put(sb.toString(), sb1.toString());
        return map;
    }

    /**************************
     * 断网重连
     ****************************/
    protected void onOnlineStatusChanged(boolean isOnline) {
        if (isOnline) {
            onConnected();
            LiveCache.setLoginChatRoomStatus(true);
        } else {
            try {
                NIMClient.getService(AuthService.class).logout();
                LiveCache.setLoginChatRoomStatus(false);
                onDisconnected();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private static Handler enterRoomhandler;

    protected final Handler getHandler() {
        if (enterRoomhandler == null) {
            enterRoomhandler = new Handler(getMainLooper());
        }
        return enterRoomhandler;
    }


    // 进入聊天室
    protected void enterRoom() {
        if (LiveCache.getLoginStatus()) {
            AppLog.i("TAG", "走了登录聊天室的方法:" + roomId);
            EnterChatRoomData data = new EnterChatRoomData(roomId);
            data.setRoomId(roomId);
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> ext1 = new HashMap<>();
            ext1.put("userId", String.valueOf(UserHelper.getUserId(LivePlayerBaseActivity.this)));
            ext1.put("avatar", UserHelper.getUserAvatar(LivePlayerBaseActivity.this));
            ext1.put("sortValue", String.valueOf(UserHelper.getSortValue(LivePlayerBaseActivity.this)));
            data.setNotifyExtension(ext1);
            data.setExtension(ext1);
            data.setAvatar(UserHelper.getUserAvatar(LivePlayerBaseActivity.this));
            data.setNick(UserHelper.getUserName(LivePlayerBaseActivity.this));
            enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
            enterChatRoomRequestCallback = new EnterChatRoomRequestCallback();
            enterRequest.setCallback(enterChatRoomRequestCallback);

        }
    }


    public class EnterChatRoomRequestCallback implements RequestCallback<EnterChatRoomResultData> {

        @Override
        public void onSuccess(EnterChatRoomResultData result) {

            onLoginDone();
            member1 = result.getMember();
            Map<String, Object> extension = result.getRoomInfo().getExtension();
            getRoomUsersCount();
            ChatRoomInfo roomInfo = result.getRoomInfo();
            creatorAccount = roomInfo.getCreator();
            ChatRoomMemberCache.getInstance().saveMyMember(member1);
            DrawableUtils.displayImg(LivePlayerBaseActivity.this, maseterHead, LivePlayerBaseActivity.this.avatar);
            if (marqueeView != null) {
                marqueeView.start("登陆聊天室成功...");
            }
            AppLog.i("TAG", "登陆聊天室成功：" + code + "   " + member1.getMemberType());
            LiveConstant.enterRoom = true;
            initInputPanel(creatorAccount, channelId);
        }

        @Override
        public void onFailed(int i) {
            AppLog.i("TAG", "登录聊天室失败：" + code);
            LiveCache.setLoginChatRoomStatus(false);
            onLoginDone();

        }

        @Override
        public void onException(Throwable throwable) {
            LiveCache.setLoginChatRoomStatus(false);
            onLoginDone();
            finish();
        }
    }

    private void getRoomUsersCount() {
        if (channelId != null) {
            contentLoader.getLiveManagerList(channelId);
        }
        if (LivePlayerBaseActivity.this instanceof AudienceActivity) {
            isLiveMaster = false;

        } else if (LivePlayerBaseActivity.this instanceof LiveActivity) {
            isLiveMaster = true;
        }
        if (onLInesRunnable == null) {
            onLInesRunnable = new OnLinesRunnable();
        }
        handler.postDelayed(onLInesRunnable, 2000);
    }

    public void endHandler() {
        if (handler != null) {
            handler.removeCallbacks(onLInesRunnable);
            LiveConstant.isUnDestory = false;
        }
    }

    public int produceRandom() {
        java.util.Random random = new java.util.Random();
        int result = random.nextInt(3);
        return result + 1;
    }

    private class OnLinesRunnable implements Runnable {
        @Override
        public void run() {
            handler.removeCallbacks(this);
            if (LiveConstant.isUnDestory && isOnLineUsersCountChange) {
                isOnLineUsersCountChange = false;
                if (roomId != null && liveNumber != -1) {
                    contentLoader.getLiveAvatar(channelId, liveNumber, isLiveMaster);
                }

            } else {
                onlineCountText.setText(String.valueOf(produceRandom() + onlineCounts) + "人");
            }
            handler.postDelayed(this, 2000);
        }
    }


    protected void initInputPanel(String creatorAccount, String channelId) {
        if (inputPanel == null) {
            inputPanel = new InputPanel(LivePlayerBaseActivity.this, container, view, getActionList(), inputConfig, creatorAccount, UserHelper.getUserId(LivePlayerBaseActivity.this), channelId);
        } else {
            inputPanel.reload(container, inputConfig);
        }

    }

    private void onLoginDone() {
        enterRequest = null;
        DialogMaker.dismissProgressDialog();
    }
    private void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }

    // 初始化礼物布局
    protected void findGiftLayout() {
        giftAnimationViewDown = (RelativeLayout) findViewById(R.id.gift_animation_view);
        giftAnimationViewUp = (RelativeLayout) findViewById(R.id.gift_animation_view_up);
        giftAnimation = new GiftAnimations(giftPlaneUp, giftAnimationViewDown, giftAnimationViewUp, this);
        giftPlaneAnimation = new GiftPlaneAnimation(anchorHeadImg, userHeadImg, giftPlaneUp, giftPlaneBg, this, avatar);
    }


    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {
        if (inputPanel != null) {
            inputPanel.collapse(false);
        }
    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }

    // 操作面板集合
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        return actions;
    }

    //显示礼物排行榜
    private void showGiftRanksPopuWindow(LiveGiftRanksResp liveGiftRanksResp) {
        giftsRankPopuWindow = new GiftsRankPopuWindow(LivePlayerBaseActivity.this, liveGiftRanksResp);
        giftsRankPopuWindow.showGiftsRankPopuWindow();
        giftsRankPopuWindow.showAtLocation(this.findViewById(R.id.live_drawer_layout),
                Gravity.CENTER, 0, 0);
        drawerLayout.closeDrawer(Gravity.RIGHT);
        giftsRankPopuWindow.setOnSendClickListener(new GiftsRankPopuWindow.OnGiftRanksListener() {
            @Override
            public void closeRankPopuBtn() {
            }

            @Override
            public void shareBtn(SpecialShareVOBean shareVO) {
                showSharePopuwindow(shareVO, getString(R.string.share_rank));
            }

            @Override
            public void ranListItemClick(RankUserBean rankUser) {
                MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_RANK_AVATAR_CLICK);
                String userId = String.valueOf(rankUser.getId());
                if (userId != null) {
                    showUserInfoDialog(userId, channelId, false);
                }
            }
        });
        giftsRankPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                drawerLayout.openDrawer(Gravity.RIGHT);
                firstClick = true;
            }
        });

    }

    /**************************
     * Module proxy
     ***************************/
    String messageUserId = null;
    IMMessage msg;
    int messageType;
    @Override
    public boolean sendMessage(final IMMessage msg, final int type) {
        this.msg = msg;
        this.messageType = type;
        contentLoader.sendMessage(msg.getContent(), LiveParams.MSG_VERIFY);
        return true;
    }

    //登录界面
    public void showLoginViewDialog() {
        if (LiveConstant.isUnDestory) {
            if(customChatDialog==null){
                customChatDialog = new CustomChatDialog(this);
            }
            customChatDialog.setContent(getString(R.string.live_login_hint));
            customChatDialog.setCancelable(false);
            customChatDialog.setCancelBtn(getString(R.string.live_canncel), null);
            customChatDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    LiveCache.setLoginStatus(false);
                    ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
                    LLoginActivity.startForResult(LivePlayerBaseActivity.this, LIVE_BASE_RESQUEST_CODE);
                }
            });
            customChatDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            customChatDialog.show();
        }
    }

    protected void liveShare(SHARE_MEDIA share_media) {
        ShareAction sp = new ShareAction(this);
        sp.setPlatform(share_media);
        sp.withTitle(shareVO.getTitle());
        sp.withTargetUrl(shareVO.getUrl());
        sp.withText(shareVO.getTitle());
        UMImage image = new UMImage(this, shareVO.getImg());
        sp.withMedia(image);
        sp.share();
    }

    protected void showSharePopuwindow(SpecialShareVOBean shareVO, final String shareRemid) {
        try {
            if (shareVO != null && LiveConstant.isUnDestory) {
                SharePopupWindow shareActivity = new SharePopupWindow(this, shareVO);
                shareActivity.setOnSuccessShare(new SharePopupWindow.OnSuccessShareListener() {
                    @Override
                    public void shareSuccess(SHARE_MEDIA share_media) {
                        String content = null;
                        if (share_media.equals(SHARE_MEDIA.SINA)) {
                            content = getString(R.string.share_sina);
                            MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_USER_SHARE_WEIBO);
                        } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                            content = getString(R.string.share_weixin);
                            MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_USER_SHARE_WECHAT1);
                        } else if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                            content = getString(R.string.share_weiclcle);
                            MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_USER_SHARE_WECHAT2);
                        }
                        if (UserHelper.isLogined(LivePlayerBaseActivity.this)) {
                            LiveMessage liveMessage = new LiveMessage();
                            liveMessage.setStyle(MessageType.text);
                            liveMessage.setUserId(String.valueOf(UserHelper.getUserId(LivePlayerBaseActivity.this)));
                            liveMessage.setCreatorAccount(creatorAccount);
                            liveMessage.setChannelId(channelId);
                            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, shareRemid + content, roomId, AuthPreferences.getUserAccount(), liveMessage);
                            sendMessage(imMessage, MessageType.text);
                        }
                    }
                });
                shareActivity.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.i("TAG", "直播间分享错误");
        }
    }
    //通过的消息
    private void sendGreenMessage(final ChatRoomMessage message) {
        try {
            NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                    .setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            switch (messageType) {
                                case MessageType.barrage:
                                    String content = msg.getContent();
                                    Map<String, Object> remoteExtension = msg.getRemoteExtension();
                                    if (remoteExtension != null) {
                                        Iterator<Map.Entry<String, Object>> iterator = remoteExtension.entrySet().iterator();
                                        while (iterator.hasNext()) {
                                            Map.Entry<String, Object> next = iterator.next();
                                            String key = next.getKey();
                                            Object value = next.getValue();
                                            if ("userId".equals(key)) {
                                                messageUserId = value.toString();
                                            }
                                        }
                                    }
                                    barrageView.init(new BarrageConfig());
                                    if (content != null) {
                                        BarrageViewBean barrageViewBean = new BarrageViewBean();
                                        barrageViewBean.setUserId(messageUserId);
                                        barrageViewBean.setContent(content);
                                        barrageViewBean.setSenderName(getString(R.string.me));
                                        barrageViewBean.setAvator(UserHelper.getUserAvatar(LivePlayerBaseActivity.this));
                                        barrageView.addTextBarrage(barrageViewBean);
                                    }
                                    barrageView.setOnBarrageClickListener(new BarrageView.OnBarrageClickListener() {
                                        @Override
                                        public void getUserId(String userId) {
                                            showUserInfoDialog(userId, channelId, false);
                                        }
                                    });
                                    messageListPanel.onMsgSend(message);
                                    if (LivePlayerBaseActivity.this instanceof AudienceActivity) {
                                        MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_USER_BARRAGE);
                                    } else if (LivePlayerBaseActivity.this instanceof LiveActivity) {
                                        MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_ANCHOR_BARRAGE);
                                    }

                                    break;
                                case MessageType.gift:
                                    messageListPanel.onMsgSend(message);
                                    break;
                                case MessageType.ban:
                                    messageListPanel.onMsgSend(message);
                                    break;
                                case MessageType.relieveBan:
                                    messageListPanel.onMsgSend(message);
                                    break;
                                case MessageType.cancel:
                                    if (channelId != null) {
                                        contentLoader.getLiveManagerList(channelId);
                                    }
                                    messageListPanel.onMsgSend(message);
                                    break;
                                case MessageType.managerLive:
                                    if (channelId != null) {
                                        contentLoader.getLiveManagerList(channelId);
                                    }
                                    messageListPanel.onMsgSend(message);
                                    break;
                                case MessageType.leaveLive:
                                    messageListPanel.onMsgSend(message);
                                    break;
                                case MessageType.challenge:
                                    messageListPanel.onMsgSend(message);
                                    break;
                                case MessageType.text:
                                    if (LivePlayerBaseActivity.this instanceof AudienceActivity) {
                                        MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_USER_EDIT);
                                    } else if (LivePlayerBaseActivity.this instanceof LiveActivity) {
                                        MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_ANCHOR_EDIT);
                                    }
                                    messageListPanel.onMsgSend(message);
                                    break;
                                case MessageType.closeLive:
                                    messageListPanel.onMsgSend(message);
                                    break;
                                case MessageType.kickOut:
                                    break;
                                case MessageType.like:
                                    if (message.getContent().equals(getString(R.string.like_master))) {
                                        messageListPanel.onMsgSend(message);
                                    } else {
                                        break;
                                    }
                                    break;
                            }
                        }
                        @Override
                        public void onFailed(int code) {
                            if (code == 13004) {
                                Toast.makeText(LivePlayerBaseActivity.this, getString(R.string.you_ban), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }

                        @Override
                        public void onException(Throwable exception) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawerLayoutListener() {
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                liveQuit.setVisibility(View.GONE);
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                liveQuit.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                palyerLayout.requestLayout();
                liveQuit.setVisibility(View.GONE);
            }
        });
    }

    private void checkSharePlatform() {
        boolean isInstallMm1 = CheckWeixinAndWeibo.checkAPPInstall(this, "com.tencent.mm");
        boolean isInstallWeibo = CheckWeixinAndWeibo.checkAPPInstall(this, "com.sina.weibo");
        if (!isInstallMm1) {
            overLiveShareFriends.setVisibility(View.GONE);
            overLiveShareWeixin.setVisibility(View.GONE);
        }
        if (!isInstallWeibo) {
            overLiveShareWeibo.setVisibility(View.GONE);
        }
    }


    private void tourisListListener() {
        tourisAdapter.setOnTouristItemClickListener(new TouristAdapter.OnTouristItemClickListener() {
            @Override
            public void showTouristInfo(UserAvatarsBean member, boolean isMasterAccount) {
                if (LivePlayerBaseActivity.this instanceof AudienceActivity) {
                    MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_USER_USER);
                } else {
                    MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_ANCHOR_USER);
                }

                boolean isLogin = UserHelper.isLogined(LivePlayerBaseActivity.this);
                if (!isLogin) {
                    showLoginViewDialog();
                } else {
                    String userIdItem = String.valueOf(member.getId());
                    inputPanel.hideInputMethod();
                    if (!isMasterAccount) {
                        if ("-1".equals(userIdItem) || userIdItem == null || userIdItem.length() == 0) {
                            Toast.makeText(LivePlayerBaseActivity.this, getString(R.string.user_tourist), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        showUserInfoDialog(userIdItem, channelId, false);
                    }
                }
            }
        });

    }


    //检测网络类型
    public static final String NET_TYPE_WIFI = "wifi";
    public static final String NET_TYPE_RESTS = "rests";
    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    String name = netInfo.getTypeName();
                    NetworkInfo.State state = netInfo.getState();
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        checkNetInfo(NET_TYPE_WIFI, 0);
                    } else {
                        if (LiveConstant.NET_CHECK == 0) {
                            checkNetInfo(NET_TYPE_RESTS, 0);
                        }

                    }
                }
            }
        }
    };
    @Override
    protected void onStop() {
        super.onStop();

        LiveConstant.isUnDestory = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LiveConstant.isUnDestory = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        AppLog.print("livePlayer onPause___");
        registerMsgServicesObser(false);
        if (inputPanel != null) {
            inputPanel.hideInputMethod();
            inputPanel.collapse(false);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        AppLog.print("livePlayer onResume___");
        registerMsgServicesObser(true);
        if (messageListPanel != null) {
            messageListPanel.onResume();
        }
    }


    @Override
    public void onBackPressed() {
        DialogUtil.clear();
        if (inputPanel != null && inputPanel.collapse(true)) {
        }
        if (messageListPanel != null && messageListPanel.onBackPressed()) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        LiveCache.setLoginChatRoomStatus(false);
        unregisterReceiver(myNetReceiver);
        AppLog.i("TAG", "直播基类走了onDestroy");

        adapter = null;
    }

    public void gotoPersonalMessage(boolean chatVisible, String accId, String nickName) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle chatBundle = new Bundle();
        chatBundle.putString(KeyParams.ACCID, accId);
        chatBundle.putString(KeyParams.NICKNAME, nickName);
        if (chatFragment != null) {
            ft.show(personalMsgFragment);
            if (!chatVisible) {
                ft.hide(chatFragment);
            } else {
                chatFragment.updateView(chatBundle);
                ft.show(chatFragment);
            }
        } else {
            personalMsgFragment = new PersonalMessageFragment();
            chatFragment = new ChatFragment();
            Bundle perMsgBundle = new Bundle();
            perMsgBundle.putBoolean(KeyParams.HAST_TITLE, true);
            personalMsgFragment.setNextFragment(chatFragment);
            personalMsgFragment.setArguments(perMsgBundle);
            chatFragment.setLastFragment(personalMsgFragment);
            chatFragment.setUnReadUpdateListener(this);
            chatBundle.putBoolean(KeyParams.HAST_CANCLE, true);
            chatFragment.setArguments(chatBundle);
            AppLog.print("fragment___" + chatFragment);
            ft.add(R.id.audience_fragment_container, personalMsgFragment);
            ft.add(R.id.audience_fragment_container, chatFragment);
            if (!chatVisible) {
                ft.hide(chatFragment);
            }
        }
        ft.commit();
    }

    @Override
    public void onImHiden() {
        if (chatFragment == null || personalMsgFragment == null) {
            return;
        }
        if (chatFragment.isHidden() && personalMsgFragment.isHidden()) {
            return;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (chatFragment != null && !chatFragment.isHidden()) {
            ft.hide(chatFragment);
        }
        if (personalMsgFragment != null && !personalMsgFragment.isHidden()) {
            ft.hide(personalMsgFragment);
        }
        ft.commit();
    }

    private String accId;
    private String nickName;
    ChatFragment chatFragment;
    PersonalMessageFragment personalMsgFragment;


    public void updateUnReadMsg() {
        int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        AppLog.print("unReadNum____" + unreadNum);
        if (unreadNum > 0) {
            String fomartCount = unreadNum > 99 ? unreadNum + "+" : String.valueOf(unreadNum);
            unReadTv.setVisibility(View.VISIBLE);
            unReadTv.setText(fomartCount);
        } else {
            unReadTv.setVisibility(View.INVISIBLE);
        }
    }

    public void registerMsgServicesObser(boolean flag) {
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, flag);
    }

    //  创建观察者对象
    Observer<List<RecentContact>> messageObserver =
            new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(List<RecentContact> messages) {
                    AppLog.print("messageObserver____onEvent___");
                    updateUnReadMsg();
                }
            };

    @Override
    public void onUnReadUpate() {
        updateUnReadMsg();
    }

}