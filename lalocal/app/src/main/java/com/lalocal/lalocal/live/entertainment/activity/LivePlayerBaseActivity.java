package com.lalocal.lalocal.live.entertainment.activity;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.ui.TActivity;
import com.lalocal.lalocal.live.base.util.DialogUtil;
import com.lalocal.lalocal.live.entertainment.adapter.GiftAdapter;
import com.lalocal.lalocal.live.entertainment.adapter.TouristAdapter;
import com.lalocal.lalocal.live.entertainment.agora.openlive.AGEventHandler;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.GiftAnimations;
import com.lalocal.lalocal.live.entertainment.helper.GiftPlaneAnimation;
import com.lalocal.lalocal.live.entertainment.model.LiveGiftRanksResp;
import com.lalocal.lalocal.live.entertainment.model.LiveRoomAvatarSortResp;
import com.lalocal.lalocal.live.entertainment.model.OnLineUser;
import com.lalocal.lalocal.live.entertainment.model.RankUserBean;
import com.lalocal.lalocal.live.entertainment.model.RoomNotifyExt;
import com.lalocal.lalocal.live.entertainment.model.TotalRanksBean;
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
import com.lalocal.lalocal.util.DensityUtil;
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
import com.netease.nimlib.sdk.chatroom.constant.MemberType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomStatusChangeData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 直播端和观众端的基类
 */
public abstract class LivePlayerBaseActivity extends TActivity implements ModuleProxy, AGEventHandler {
    public static final int LIVE_BASE_RESQUEST_CODE = 701;
    public boolean isUnDestory = true;
    boolean isStartLiveShare = true;
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
    private ChatRoomMember master;
    ChatRoomMember member1;
    private List<ChatRoomMember> items = null;
    private List<ChatRoomMember> allItems = new ArrayList<>();
    private SpecialShareVOBean shareVO;
    public int onlineCounts;
    public String avatar;
    protected String userId;
    protected String creatorAccount;//聊天室创建者账号
    private String barrageContent;
    private boolean isFirstLoadding = true;
    boolean isFirstClick = true;//防止快速点击，出现两个popuwindow
    private ContentLoader contentLoader;
    protected ImageView giftPlaneUp;
    protected RelativeLayout giftPlaneBg;
    protected ImageView userHeadImg, anchorHeadImg;
    private String code;
    private String channelId;
    private String disableSendMsgUserId;
    List<String> banListAudience = new ArrayList<>();
    protected RelativeLayout scoreLayout;
    protected GiftPlaneAnimation giftPlaneAnimation;
    protected RelativeLayout palyerLayout;
    protected ImageView liveGiftImg;
    private InputConfig inputConfig;
    private MyCallBack myCallBack;
    protected GiftsRankPopuWindow giftsRankPopuWindow;
    protected ImageView settingLiveImg;
    private TextView sendPlaneName;
    private EnterChatRoomRequestCallback enterChatRoomRequestCallback;
    boolean audienceOnLineCountsChange = true;
    protected MarqueeView marqueeView;
    private Queue<IMMessage> marquee = new LinkedList<>();
    private ImageView overLiveShareFriends;
    private ImageView overLiveShareWeibo;
    private ImageView overLiveShareWeixin;
    protected ImageView quit;
    protected View topView;


    protected abstract void checkNetInfo(String netType, int reminder);

    protected abstract void onConnected(); // 网络连上

    protected abstract void onDisconnected(); // 网络断开

    protected abstract int getActivityLayout(); // activity布局文件

    protected abstract int getLayoutId(); // 根布局资源id

    protected abstract void initParam(); // 初始化推流/拉流参数，具体在子类中实现


    private static Map<MemberType, Integer> compMap = new HashMap<>();


    @Override
    protected void showKeyboardDelayed(View focus) {
        super.showKeyboardDelayed(focus);
    }


    @Override
    protected void initUIandEvent() {
        event().addEventHandler(this);
    }

    private boolean isLocation = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[] locations = new int[2];
        if (quit != null && periscopeLayout != null && !isLocation) {//计算点赞动画的位置
            isLocation = true;
            quit.getLocationOnScreen(locations);
            int x = locations[0];

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) periscopeLayout.getLayoutParams();
            int i = DensityUtil.dip2px(LivePlayerBaseActivity.this, 70);
            layoutParams.leftMargin = x - (i / 4);
            periscopeLayout.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  //应用运行时，保持屏幕高亮，不锁屏
        contentLoader = new ContentLoader(this);
        myCallBack = new MyCallBack();
        contentLoader.setCallBack(myCallBack);
        LiveConstant.USER_INFO_FIRST_CLICK = true;
        findViews();
        checkSharePlatform();//检测手机安装分享平台
        registerNetStatus();
    }

    private void checkSharePlatform() {
       boolean isInstallMm1 = CheckWeixinAndWeibo.checkAPPInstall(this, "com.tencent.mm");
      boolean  isInstallWeibo = CheckWeixinAndWeibo.checkAPPInstall(this, "com.sina.weibo");
        if(!isInstallMm1){
            overLiveShareFriends.setVisibility(View.GONE);
            overLiveShareWeixin.setVisibility(View.GONE);
        }
        if (!isInstallWeibo) {
           overLiveShareWeibo.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLog.i("TAG", "直播基类登录回调：" + resultCode);
        if (requestCode == LIVE_BASE_RESQUEST_CODE && resultCode == MeFragment.LOGIN_OK) {
            if (data != null) {
                AppLog.i("TAG", "直播基类登录回调rere：" + resultCode);
                ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
                NIMClient.getService(AuthService.class).logout();
            }
        }
    }

    private String playType;

    public void getParameter(LiveRowsBean liveRowsBean) {
        roomId = String.valueOf(liveRowsBean.getRoomId());
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
        playType = String.valueOf(liveRowsBean.getType());
        avatar = liveRowsBean.getUser().getAvatar();
        container = new Container(this, roomId, SessionTypeEnum.ChatRoom, this);
        if (messageListPanel == null) {
            messageListPanel = new ChatRoomMsgListPanel(container, view, annoucement, LivePlayerBaseActivity.this);
        }
        // 礼物动画展示
        findGiftLayout();
    }

    protected void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineStatus, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    //监听云信账号登录状态
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            AppLog.i("TAG", "LivePlayerBaseActivity監聽用戶登錄狀態愛;" + statusCode);
            if (statusCode != StatusCode.LOGINED) {
                DemoCache.setLoginStatus(false);
               if (statusCode == StatusCode.UNLOGIN||statusCode==StatusCode.NET_BROKEN) {

                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String userAccount = AuthPreferences.getUserAccount();
                            String userToken = AuthPreferences.getUserToken();
                            AppLog.i("TAG", "LivePlayerBaseActivity監聽用戶登錄狀態愛：" + userAccount + "    userToken:" + userToken);
                            if (userAccount != null && userToken != null) {
                                loginIMServer(userAccount, userToken);
                            } else {
                                contentLoader.getTouristInfo();
                            }
                        }
                    }, 1500);
                }
            } else if (statusCode == StatusCode.LOGINED) {
                DemoCache.setLoginStatus(true);
                AppLog.i("TAG", "用户登录成功的监听:" + AuthPreferences.getUserAccount());
                DemoCache.setAccount(AuthPreferences.getUserAccount());
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enterRoom();
                    }
                }, 2000);

            }

        }
    };

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

    //检测聊天室登录状态
    Observer<ChatRoomStatusChangeData> onlineStatus = new Observer<ChatRoomStatusChangeData>() {
        @Override
        public void onEvent(ChatRoomStatusChangeData chatRoomStatusChangeData) {
            if (chatRoomStatusChangeData.status == StatusCode.CONNECTING) {
                DemoCache.setLoginChatRoomStatus(false);
                AppLog.i("TAG", "聊天室正在连接中");
                if (marqueeView != null) {
                    marqueeView.start("正在连接聊天室...");
                }

            } else if (chatRoomStatusChangeData.status == StatusCode.UNLOGIN) {
                int enterErrorCode = NIMClient.getService(ChatRoomService.class).getEnterErrorCode(roomId);
                AppLog.i("TAG", "进入聊天室失败，获取enterErrorCode：" + enterErrorCode);
                onOnlineStatusChanged(false);
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINING) {
                AppLog.i("TAG", "聊天室登录中。。。");
                DemoCache.setLoginChatRoomStatus(false);
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINED) {
                onOnlineStatusChanged(true);

            } else if (chatRoomStatusChangeData.status.wontAutoLogin()) {
                AppLog.i("TAG", "聊天室wontAutoLogin");
            } else if (chatRoomStatusChangeData.status == StatusCode.NET_BROKEN) {
                onOnlineStatusChanged(false);
                if (marqueeView != null) {
                    marqueeView.start("聊天室登录失败，正在重新连接...");
                }

            }
            AppLog.i("TAG", "Chat Room Online Status:" + chatRoomStatusChangeData.status.name());
        }
    };

    //踢出聊天室
    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
        @Override
        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
            AppLog.i("TAG","监听到被踢出聊天室");
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
    private boolean mIsTouchUP = true;
    private boolean isScrollStop = true;

    protected void findViews() {
        topView = findViewById(R.id.top_view);
        palyerLayout = (RelativeLayout) findViewById(R.id.player_layout);
        barrageView = (BarrageView) findViewById(R.id.barrageView_test);
        masterName = (TextView) findViewById(R.id.live_emcee_name);
        maseterHead = (CircleImageView) findViewById(R.id.live_emcee_head);
        touristList = (RecyclerView) findViewById(R.id.live_visitors_list_recy);
        liveGiftImg = (ImageView) findViewById(R.id.live_gift_img);
        quit = (ImageView) findViewById(R.id.live_telecast_quit);
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(LivePlayerBaseActivity.this, CustomLinearLayoutManager.HORIZONTAL, false);
        touristList.setLayoutManager(linearLayoutManager);
        onlineCountText = findView(R.id.live_online_count);
        scoreLayout = (RelativeLayout) findViewById(R.id.audience_score_layout);
        giftPlaneUp = (ImageView) findViewById(R.id.gift_plane_up);
        giftPlaneBg = (RelativeLayout) findViewById(R.id.audient_gift_plane_bg);
        anchorHeadImg = (ImageView) findViewById(R.id.audience_anchor_headportrait);
        userHeadImg = (ImageView) findViewById(R.id.audience_user_headportrait);
        sendPlaneName = (TextView) findViewById(R.id.audience_gift_send_plane);
        marqueeView = (MarqueeView) findViewById(R.id.live_notifitation_marquee_view);


        //直播結束，分享
        overLiveShareFriends = (ImageView) findViewById(R.id.over_page_friends);
        overLiveShareWeibo = (ImageView) findViewById(R.id.over_page_weibo);
        overLiveShareWeixin = (ImageView) findViewById(R.id.over_page_weixin);
        overLiveShareFriends.setOnClickListener(clickListener);
        overLiveShareWeibo.setOnClickListener(clickListener);
        overLiveShareWeixin.setOnClickListener(clickListener);



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

        touristList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isScrollStop = true;
                } else {
                    isScrollStop = false;
                }
            }
        });

        touristList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsTouchUP = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mIsTouchUP = false;
                        break;
                    default:
                        mIsTouchUP = true;
                        break;
                }
                return false;

            }
        });


        scoreLayout.setOnClickListener(clickListener);

        masterInfoLayout = (LinearLayout) findViewById(R.id.live_master_info_layout);
        masterInfoLayout.setOnClickListener(clickListener);

        //设置更多
        settingLiveImg = (ImageView) findViewById(R.id.live_telecast_top_setting);
        settingLiveImg.setOnClickListener(clickListener);

        drawerLayout = (DrawerLayout) findViewById(R.id.live_drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // TODO Auto-generated method stub
                super.onDrawerSlide(drawerView, slideOffset);
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                palyerLayout.requestLayout();

            }
        });
    }
    protected abstract void showStatusUnUsual();
    public class MyCallBack extends ICallBack {

        @Override
        public void onResponseFailed(String message, int requestCode) {
            super.onResponseFailed(message, requestCode);
            if(243==requestCode){
                showStatusUnUsual();
            }
        }
        @Override
        public void onLiveRoomAvatar(LiveRoomAvatarSortResp.ResultBean result) {
            super.onLiveRoomAvatar(result);
            int number = result.getNumber();
            onlineCounts=number;
            onlineCountText.setText(String.valueOf(number) + "人");
            List<LiveRoomAvatarSortResp.ResultBean.UserAvatarsBean> userAvatars = result.getUserAvatars();
            if (isFirstLoadding) {

                tourisAdapter = new TouristAdapter(LivePlayerBaseActivity.this, userAvatars);
                touristList.setAdapter(tourisAdapter);
                isFirstLoadding = false;
                tourisAdapter.setOnTouristItemClickListener(new TouristAdapter.OnTouristItemClickListener() {
                    @Override
                    public void showTouristInfo(LiveRoomAvatarSortResp.ResultBean.UserAvatarsBean member, boolean isMasterAccount) {

                        if (LiveConstant.USER_INFO_FIRST_CLICK) {
                            LiveConstant.USER_INFO_FIRST_CLICK = false;
                            boolean isLogin = UserHelper.isLogined(LivePlayerBaseActivity.this);
                            if (!isLogin) {
                                showLoginViewDialog();
                            } else {

                                String userIdItem =String.valueOf(member.getId());
                                inputPanel.hideInputMethod();
                                if (!isMasterAccount) {
                                    if ("-1".equals(userIdItem) || userIdItem == null || userIdItem.length() == 0) {
                                        LiveConstant.USER_INFO_FIRST_CLICK = true;
                                        Toast.makeText(LivePlayerBaseActivity.this, "该用户为游客!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    showUserInfoDialog(userIdItem, channelId ,false);
                                }
                            }

                        }
                    }
                });

            } else if (userAvatars.size() > 0) {
                tourisAdapter.refresh(userAvatars);
            }

        }

        @Override
        public void onGiftRanks(LiveGiftRanksResp liveGiftRanksResp) {
            super.onGiftRanks(liveGiftRanksResp);
            if (liveGiftRanksResp.getReturnCode() == 0) {
                firstClick = false;
                List<TotalRanksBean> currentRanks = liveGiftRanksResp.getResult().getCurrentRanks();
                if (currentRanks != null && currentRanks.size() > 0) {
                    for (int i = 0; i < currentRanks.size(); i++) {
                        totalGold = totalGold + currentRanks.get(i).getGold();
                    }
                }
                if (isUnDestory) {
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
                    DemoCache.clear();
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
                DemoCache.setAccount(imccId);
                DemoCache.getRegUserInfo();
                DemoCache.setLoginStatus(true);
            }

            @Override
            public void onFailed(int i) {
                AppLog.i("TAG", "NewsFragment,手动登录失败" + i);
                DemoCache.setLoginStatus(false);
            }

            @Override
            public void onException(Throwable throwable) {
                AppLog.i("TAG", "NewsFragment,手动登录失败2");
                DemoCache.setLoginStatus(false);
            }

        });
    }

    /***************************
     * 监听
     ****************************/
    boolean firstClick = true;//排行榜
    boolean userInfoFirstclick = true;

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.live_master_info_layout:
                    if(LivePlayerBaseActivity.this instanceof AudienceActivity){
                        if(userId!=null&&channelId!=null){
                            showUserInfoDialog(userId, channelId, true);
                        }
                    }else {
                        boolean logineds = UserHelper.isLogined(LivePlayerBaseActivity.this);
                        if (logineds) {
                            contentLoader.liveGiftRanks(channelId);
                            contentLoader.setCallBack(myCallBack);
                        } else {
                            showLoginViewDialog();
                        }
                    }


                    break;
                case R.id.live_telecast_top_setting:
                    liveCommonSetting();
                    break;

                case R.id.audience_score_layout:
                    MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_ANCHOR_LIST);
                    if (firstClick&&LivePlayerBaseActivity.this instanceof AudienceActivity) {
                        boolean logineds = UserHelper.isLogined(LivePlayerBaseActivity.this);
                        if (logineds) {
                            contentLoader.liveGiftRanks(channelId);
                            contentLoader.setCallBack(myCallBack);
                        } else {
                            showLoginViewDialog();
                        }
                    }
                    break;
                case R.id.over_page_friends:
                    Toast.makeText(LivePlayerBaseActivity.this,"點解",Toast.LENGTH_SHORT).show();
                    overLiveShareFriends.setSelected(true);
                    overLiveShareWeibo.setSelected(false);
                    overLiveShareWeixin.setSelected(false);
                    liveShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    isStartLiveShare = false;
                    break;
                case R.id.over_page_weibo:
                    overLiveShareFriends.setSelected(false);
                    overLiveShareWeibo.setSelected(true);
                    overLiveShareWeixin.setSelected(false);
                    liveShare(SHARE_MEDIA.SINA);
                    isStartLiveShare = false;
                    break;
                case R.id.over_page_weixin:
                    overLiveShareFriends.setSelected(false);
                    overLiveShareWeibo.setSelected(false);
                    overLiveShareWeixin.setSelected(true);
                    liveShare(SHARE_MEDIA.WEIXIN);
                    isStartLiveShare = false;
                    break;

            }
        }
    };

    protected abstract void liveShare(SHARE_MEDIA share_media);

    protected abstract void showUserInfoDialog(String userId, String channelId,boolean isMaster);

    protected abstract void liveCommonSetting();


    Queue<ChatRoomMessage> cache = new LinkedList<>();
    boolean isFirstPlane = true;
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {

        private String onlineNum;
        private String messageUserId;

        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            int styles = 0;
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

                    if ("barrag".equals(key)) {
                        barrageContent = value.toString();
                    }
                    if ("style".equals(key)) {
                        styles = Integer.parseInt(value.toString());
                    }
                    if ("userId".equals(key)) {
                        messageUserId = value.toString();
                    }
                    if("creatorAccount".equals(key)){
                        String  creatorAccount = value.toString();
                        if(creatorAccount==null||"null".equals(creatorAccount)){
                            return;
                        }
                    }
                    if ("disableSendMsgUserId".equals(key)) {
                        disableSendMsgUserId = value.toString();
                    }
                    if ("onlineNum".equals(key)) {
                        onlineNum = value.toString();
                    }
                    if ("giftModel".equals(key)) {
                        String text = value.toString();
                        if (text != null && !"null".equals(text)) {
                            Map<String, Object> map = (Map<String, Object>) value;
                            Iterator<Map.Entry<String, Object>> mapItem = map.entrySet().iterator();
                            while (mapItem.hasNext()) {
                                Map.Entry<String, Object> next1 = mapItem.next();
                                String key1 = next1.getKey();
                                Object value1 = next1.getValue();
                                if ("code".equals(key1)) {
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
                        barrageView.init(new BarrageConfig());
                        if (content != null) {
                            BarrageViewBean barrageBean = new BarrageViewBean();
                            barrageBean.setUserId(messageUserId);
                            barrageBean.setContent(content);
                            barrageBean.setAvator(senderAvatar);
                            barrageBean.setSenderName(senderNick);
                            barrageView.addTextBarrage(barrageBean);
                        }
                    }

                    barrageView.setOnBarrageClickListener(new BarrageView.OnBarrageClickListener() {
                        @Override
                        public void getUserId(String userId) {
                            AppLog.i("TAG","弹幕被点击了");
                            showUserInfoDialog(userId, channelId ,false);
                        }
                    });
                    break;
                case MessageType.like:

                    periscopeLayout.addHeart();
                    marqueeView.start(((ChatRoomMessage) message).getChatRoomMessageExtension().getSenderNick() + "  给主播点了个赞");
                    break;
                case MessageType.gift:
                    if ("003".equals(code)) {
                        giftPlaneAnimation.showPlaneAnimation((ChatRoomMessage) message);
                    } else if (code != null) {
                        giftAnimation.showGiftAnimation((ChatRoomMessage) message);
                    }
                    messageListPanel.onIncomingMessage(messages);
                    break;
                case MessageType.ban://禁言
                    if (banListAudience.size() > 0) {
                        for (int i = 0; i < banListAudience.size(); i++) {
                            if (disableSendMsgUserId.equals(banListAudience.get(i))) {
                                return;
                            }
                        }
                    }
                    banListAudience.add(disableSendMsgUserId);
                    messageListPanel.onIncomingMessage(messages);
                    break;
                case MessageType.relieveBan://解除禁言
                    messageListPanel.onIncomingMessage(messages);
                    if (banListAudience.size() > 0) {
                        for (int i = 0; i < banListAudience.size(); i++) {
                            if (disableSendMsgUserId.equals(banListAudience.get(i))) {
                                banListAudience.remove(i);
                                return;
                            }
                        }
                    }
                    break;
                case MessageType.managerLive:
                    messageListPanel.onIncomingMessage(messages);
                    break;
                case MessageType.cancel:
                    messageListPanel.onIncomingMessage(messages);
                    break;
                case MessageType.closeLive:
                    AppLog.i("TAG","收到关闭直播间消息");
                    closeLiveNotifi();
                    break;

                case MessageType.leaveLive:
                    showFinishLayout(true, 2);
                    break;
                case MessageType.kickOut:
                    if(messageUserId!=null&&messageUserId.equals(String.valueOf(UserHelper.getUserId(LivePlayerBaseActivity.this)))){
                        superManagerKickOutUser();
                    }
                    messageListPanel.onIncomingMessage(messages);

            }

            if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();
                switch (notificationAttachment.getType()) {
                    case ChatRoomMemberIn:
                        //发送进入直播间的通知
                        isOnLineUsersCountChange = true;
                        audienceOnLineCountsChange = true;
                        String fromAccountIn = message.getFromAccount();
                        showNotifacatin(message);
                     //   sendMessage(message, MessageType.text);
                        if (fromAccountIn != null && creatorAccount != null) {
                            if (creatorAccount.equals(fromAccountIn)) {
                                //主播回来了
                                masterOnLineStatus(true);
                            }
                        }
                        break;
                    case ChatRoomMemberExit:
                        isOnLineUsersCountChange = true;
                        audienceOnLineCountsChange = true;
                        String fromAccountExit = message.getFromAccount();
                        if (creatorAccount != null && creatorAccount.equals(fromAccountExit)) {
                            //主播离开了
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

    protected abstract void superManagerKickOutUser();


    protected abstract void closeLiveNotifi();
    LinkedList<RoomNotifyExt> roomNotifyExtList = new LinkedList<>();
    private void showNotifacatin(IMMessage message) {
        ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();

        Map<String, String> targetNicks = getTargetNicks(notificationAttachment);
        Set keys = targetNicks.keySet();
        if(keys != null) {
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                Object value = targetNicks.get(key);
                marqueeView.start("欢迎" + key.toString() + "进入直播间");

            }
        }

    }


    private static Map<String,String> getTargetNicks(final ChatRoomNotificationAttachment attachment) {
        Map<String,String> map=new HashMap<>();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        List<String> accounts = attachment.getTargets();
        List<String> targets = attachment.getTargetNicks();
        if (attachment.getTargetNicks() != null) {
            for (int i = 0; i < targets.size(); i++) {
                if (DemoCache.getAccount() != null) {
                    sb.append(DemoCache.getAccount().equals(accounts.get(i)) ? "你" : targets.get(i));
                    sb.append(",");
                } else {
                    sb.append(targets.get(i));
                    sb.append(",");
                }

            }
            sb.deleteCharAt(sb.length() - 1);
        }

        if(attachment.getTargets()!=null){
            for(int j=0;j<accounts.size();j++){
                AppLog.i("TAG","通知的用户账号："+accounts.get(j));
                sb1.append(accounts.get(j));
                sb1.append(",");
            }
            sb1.deleteCharAt(sb1.length() - 1);
        }
        map.put(sb.toString(),sb1.toString());
        return map;
    }

    private void rollView() {
        IMMessage poll = marquee.poll();
        if (poll == null) {
            return;
        }

    }

    protected abstract void masterOnLineStatus(boolean b);

    protected abstract void showFinishLayout(boolean b, int i);

    protected abstract void receiveChallengeMessage(ChatRoomMessage message);


    /**************************
     * 断网重连
     ****************************/
    protected void onOnlineStatusChanged(boolean isOnline) {
        if (isOnline) {
            onConnected();
            DemoCache.setLoginChatRoomStatus(true);
        } else {

            NIMClient.getService(AuthService.class).logout();
            DemoCache.setLoginChatRoomStatus(false);
            onDisconnected();
            //    MobHelper.sendEevent(getActivity(), MobEvent.MY_WALLET);
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
        if (DemoCache.getLoginStatus()) {
            AppLog.i("TAG", "走了登录聊天室的方法:" + roomId);
            EnterChatRoomData data = new EnterChatRoomData(roomId);
            data.setRoomId(roomId);
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> ext1 = new HashMap<>();
            ext1.put("userId", String.valueOf(UserHelper.getUserId(LivePlayerBaseActivity.this)));
            ext1.put("avatar", UserHelper.getUserAvatar(LivePlayerBaseActivity.this));
            ext1.put("sortValue", String.valueOf(UserHelper.getSortValue(LivePlayerBaseActivity.this)));
            data.setNotifyExtension(ext1);
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
            AppLog.i("TAG", "登陆聊天室成功：" + code+"   "+member1.getMemberType());
            LiveConstant.enterRoom = true;
            initInputPanel(creatorAccount, channelId);
        }

        @Override
        public void onFailed(int i) {
            AppLog.i("TAG", "登录聊天室失败：" + code);
            DemoCache.setLoginChatRoomStatus(false);
            onLoginDone();
        }

        @Override
        public void onException(Throwable throwable) {
            DemoCache.setLoginChatRoomStatus(false);
            onLoginDone();
            finish();
        }
    }
    boolean isLiveMaster;
    OnLinesRunnable   onLInesRunnable=null;
    private int liveNumber;
    private void getRoomUsersCount() {
        if(LivePlayerBaseActivity.this instanceof AudienceActivity){
            isLiveMaster=false;

        }else if(LivePlayerBaseActivity.this instanceof LiveActivity){
            isLiveMaster=true;
        }
        if(onLInesRunnable==null){
            onLInesRunnable = new OnLinesRunnable();
        }
        handler.postDelayed(onLInesRunnable,2000);

    }
    public  void endHandler(){
        if(handler!=null){
            handler.removeCallbacks(onLInesRunnable);
            isUnDestory=false;
        }
    }
    public  int  produceRandom(){
        java.util.Random random=new java.util.Random();
        int result=random.nextInt(3);
        return result+1;
    }

    private class OnLinesRunnable implements Runnable {
        @Override
        public void run() {
            handler.removeCallbacks(this);
            if(isUnDestory&&isOnLineUsersCountChange){
                isOnLineUsersCountChange=false;
                if(roomId!=null&&liveNumber!=-1){
                    contentLoader.getLiveAvatar(channelId,liveNumber,isLiveMaster);
                }
                AppLog.i("TAG","调用获取直播间头像接口");
            }else{
                onlineCountText.setText(String.valueOf(produceRandom()+onlineCounts) + "人");
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

    boolean isOnLineUsersCountChange = true;


    private void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }
    private Handler handler = new Handler();
    // 初始化礼物布局
    protected void findGiftLayout() {
        giftAnimationViewDown = findView(R.id.gift_animation_view);
        giftAnimationViewUp = findView(R.id.gift_animation_view_up);
        giftAnimation = new GiftAnimations(giftPlaneUp, giftAnimationViewDown, giftAnimationViewUp, this);
        giftPlaneAnimation = new GiftPlaneAnimation(anchorHeadImg, userHeadImg, giftPlaneUp, giftPlaneBg, this, avatar);

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (inputPanel != null) {
            inputPanel.hideInputMethod();
            inputPanel.collapse(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


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
        DemoCache.setLoginChatRoomStatus(false);
        unregisterReceiver(myNetReceiver);
        AppLog.i("TAG", "直播基类走了onDestroy");

        adapter = null;
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
                SharePopupWindow shareActivity = new SharePopupWindow(LivePlayerBaseActivity.this, shareVO);
                shareActivity.showShareWindow();
                shareActivity.showAtLocation(LivePlayerBaseActivity.this.findViewById(R.id.live_drawer_layout),
                        Gravity.BOTTOM, 0, 0);

            }

            @Override
            public void ranListItemClick(RankUserBean rankUser) {
                String userId = String.valueOf(rankUser.getId());
                if(userId!=null){
                    contentLoader.getLiveUserInfo(userId);
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

    @Override
    public boolean sendMessage(IMMessage msg, int type) {
        String messageUserId = null;
        String fromAccount = null;
        ChatRoomMessage message = (ChatRoomMessage) msg;
        if (msg != null) {
            Map<String, Object> remoteExtension1 = msg.getRemoteExtension();
            if (remoteExtension1 != null) {
                Iterator<Map.Entry<String, Object>> iterator = remoteExtension1.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> next = iterator.next();
                    String key = next.getKey();
                    Object value = next.getValue();
                    if ("disableSendMsgUserId".equals(key)) {
                        fromAccount = value.toString();
                    }
                }
            }
            if (banListAudience.size() > 0) {
                for (String account : banListAudience) {
                    if (account.equals(fromAccount)) {
                        Toast.makeText(LivePlayerBaseActivity.this, "你已被管理员禁言", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }


            NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                    .setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {

                        }

                        @Override
                        public void onFailed(int code) {
                            if(code==13004){
                                Toast.makeText(LivePlayerBaseActivity.this,"你已被管理员禁言!",Toast.LENGTH_SHORT).show();
                            }
                            return;

                        }

                        @Override
                        public void onException(Throwable exception) {
                        }
                    });


            switch (type) {
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
                        barrageViewBean.setSenderName("我");
                        barrageViewBean.setAvator(UserHelper.getUserAvatar(this));
                        barrageView.addTextBarrage(barrageViewBean);
                    }
                    barrageView.setOnBarrageClickListener(new BarrageView.OnBarrageClickListener() {
                        @Override
                        public void getUserId(String userId) {
                            showUserInfoDialog(userId, channelId ,false);
                        }
                    });
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
                    messageListPanel.onMsgSend(message);
                    break;
                case MessageType.managerLive:
                    messageListPanel.onMsgSend(message);
                    break;
                case MessageType.leaveLive:
                    messageListPanel.onMsgSend(message);
                    break;
                case MessageType.challenge:
                    messageListPanel.onMsgSend(message);
                    break;
                case MessageType.text:
                    messageListPanel.onMsgSend(message);
                    break;
                case MessageType.closeLive:
                    messageListPanel.onMsgSend(message);
                    break;
                case MessageType.kickOut:

                    messageListPanel.onMsgSend(message);
                    break;
            }
        }

        return true;
    }

    //登录界面
    public void showLoginViewDialog() {
        if (isUnDestory) {
            final CustomChatDialog customDialog = new CustomChatDialog(this);
            customDialog.setContent(getString(R.string.live_login_hint));
            customDialog.setCancelable(false);
            customDialog.setCancelBtn(getString(R.string.live_canncel), null);
            customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    DemoCache.setLoginStatus(false);
                    ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
                    LLoginActivity.startForResult(LivePlayerBaseActivity.this, LIVE_BASE_RESQUEST_CODE);
                }
            });
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    LiveConstant.USER_INFO_FIRST_CLICK = true;
                }
            });
            customDialog.show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        isUnDestory = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isUnDestory = true;
    }
}