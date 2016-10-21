package com.lalocal.lalocal.live.entertainment.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.lalocal.lalocal.activity.LoginActivity;
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
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomNotificationHelper;
import com.lalocal.lalocal.live.entertainment.helper.GiftAnimations;
import com.lalocal.lalocal.live.entertainment.helper.GiftPlaneAnimation;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.helper.SimpleCallback;
import com.lalocal.lalocal.live.entertainment.model.LiveGiftRanksResp;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListResp;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.model.OnLineUser;
import com.lalocal.lalocal.live.entertainment.model.RankUserBean;
import com.lalocal.lalocal.live.entertainment.model.TotalRanksBean;
import com.lalocal.lalocal.live.entertainment.module.ChatRoomMsgListPanel;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
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
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
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
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.constant.MemberType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomStatusChangeData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.NotificationType;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 直播端和观众端的基类
 */
public abstract class LivePlayerBaseActivity extends TActivity implements ModuleProxy, AGEventHandler {
    private static final String TAG = LivePlayerBaseActivity.class.getSimpleName();
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    private final static String AVATAR = "AVATAR";
    private final static String USER_ID = "USER_ID";
    public static final String LIVE_USER_ID = "LIVE_USER_ID";
    public static final String ANNOUCEMENT = "ANNOUCEMENT";
    public static final String CHANNELID = "CHANNELID";
    private final static int FETCH_ONLINE_PEOPLE_COUNTS_DELTA = 3000;
    public static final int LIVE_BASE_RESQUEST_CODE = 701;
    private static final int LIMIT = 30;
    public static final int REFRESH = 101;
    public static final String NIM_CHAT_MESSAGE_INFO = "nimlivesenfmessage";
    private Timer timer;
    // 聊天室信息
    protected String roomId;
    protected ChatRoomInfo roomInfo;
    protected String url; // 推流/拉流地址
    protected InputPanel inputPanel;
    protected ChatRoomMsgListPanel messageListPanel;

    // view
    protected DrawerLayout drawerLayout;
    private View view;
    private ImageView inputText;
    protected TextView onlineCountText; // 在线人数view

    protected TextView masterName;
    protected CircleImageView maseterHead;
    protected ImageView inputChar;
    protected View masterInfoLayoutPw;
    protected PopupWindow masterInfoPopuwindow;
    protected ImageView masterInfoCloseIv;
    protected CircleImageView masterInfoHeadIv;
    protected TextView masterInfoNickTv;
    protected TextView masterInfoSignature;
    protected TextView liveAttention;
    protected TextView liveFans;
    protected TextView liveContribute;
    protected TextView liveMasterHome;
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
    public String annoucement;//公告
    protected String userId;
    private String userIdItem;
    private String enterroomgetnick;
    private String avatarIntetn;
    protected String masterAvatar;
    protected String creatorAccount;//聊天室创建者账号
    private String barrageContent;


    private boolean isFirstLoadding = true;
    boolean isFirstClick = true;//防止快速点击，出现两个popuwindow
    protected boolean barrageSelectorStatus = false;
    private boolean isNormalEmpty = false; // 固定成员是否拉取完
    private long updateTime = 0; // 非游客的updateTime
    private long enterTime = 0;

    private ContentLoader contentLoader;

    protected ImageView giftPlaneUp;
    protected RelativeLayout giftPlaneBg;
    protected ImageView userHeadImg, anchorHeadImg;
    private String code;
    private AnimationDrawable rocketAnimation;
    private LiveUserInfoResultBean result;
    private boolean isMuted;//是否禁言

    private int meberType;//聊天室成员类型
    private String channelId;
    private int dialogId;
    private String disableSendMsgUserId;
    List<String> banListAudience = new ArrayList<>();
    List<String> banListLive = new ArrayList<>();
    private int identity = 0;
    private int manageResult;
    protected RelativeLayout scoreLayout;
    private TextView scoreTv;
    private String meberAccount;
    private RelativeLayout planeLayout;
    //   private ImageView giftPlaneDown;
    protected GiftPlaneAnimation giftPlaneAnimation;
    protected RelativeLayout palyerLayout;
    protected ImageView liveGiftImg;
    private InputConfig inputConfig;
    private MyCallBack myCallBack;
    protected GiftsRankPopuWindow giftsRankPopuWindow;
    protected ImageView chanllenge;

    protected abstract void checkNetInfo(String netType, int reminder);

    protected abstract void onConnected(); // 网络连上

    protected abstract void onDisconnected(); // 网络断开

    protected abstract int getActivityLayout(); // activity布局文件

    protected abstract int getLayoutId(); // 根布局资源id

    protected abstract void initParam(); // 初始化推流/拉流参数，具体在子类中实现

    protected abstract void clickChallengeBtn();//挑战按钮

    protected abstract void showMasterInfoPopuwindow(LiveUserInfoResultBean result, boolean isMuted, String meberAccount, int id, int managerId, List<LiveManagerListBean> managerList);

    private static Map<MemberType, Integer> compMap = new HashMap<>();

    static {
        compMap.put(MemberType.CREATOR, 0);
        compMap.put(MemberType.ADMIN, 1);
        compMap.put(MemberType.NORMAL, 2);
        compMap.put(MemberType.LIMITED, 3);
        compMap.put(MemberType.GUEST, 4);
    }

    @Override
    protected void showKeyboardDelayed(View focus) {
        super.showKeyboardDelayed(focus);
    }


    @Override
    protected void initUIandEvent() {
        event().addEventHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        contentLoader = new ContentLoader(this);
        myCallBack = new MyCallBack();
        contentLoader.setCallBack(myCallBack);
        parseIntent();
        findViews();
        initParam();
        registerNetStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIVE_BASE_RESQUEST_CODE && (resultCode == 101 || resultCode == 105)) {
            if (data != null) {
                String email = data.getStringExtra(LoginActivity.EMAIL);
                String psw = data.getStringExtra(LoginActivity.PSW);
                contentLoader.login(email, psw);
            }
        }

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
            } else if (statusCode == StatusCode.LOGINED) {
                DemoCache.setLoginStatus(true);
                enterRoom();
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
                AppLog.i("TAG", "聊天室正在连接中");

            } else if (chatRoomStatusChangeData.status == StatusCode.UNLOGIN) {
                onOnlineStatusChanged(false);
                AppLog.i("TAG", "聊天室没有登录");

            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINING) {
                AppLog.i("TAG", "聊天室登录中。。。");

            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINED) {
                AppLog.i("TAG", "聊天室已经登录");
                onOnlineStatusChanged(true);

            } else if (chatRoomStatusChangeData.status.wontAutoLogin()) {
                AppLog.i("TAG", "聊天室wontAutoLogin");
            } else if (chatRoomStatusChangeData.status == StatusCode.NET_BROKEN) {
                onOnlineStatusChanged(false);

            }
            AppLog.i("TAG", "Chat Room Online Status:" + chatRoomStatusChangeData.status.name());
        }
    };

    //踢出聊天室
    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
        @Override
        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
            clearChatRoom();
        }
    };


    void parseIntent() {
        LiveRowsBean liveRowsBean = getIntent().getParcelableExtra("LiveRowsBean");
        roomId = String.valueOf(liveRowsBean.getRoomId());
        url = liveRowsBean.getPullUrl();
        userId = String.valueOf(liveRowsBean.getUser().getId());
        avatarIntetn = liveRowsBean.getUser().getAvatar();
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
        shareVO = liveRowsBean.getShareVO();
        channelId = String.valueOf(liveRowsBean.getId());

    }


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
        palyerLayout = (RelativeLayout) findViewById(R.id.player_layout);
        barrageView = (BarrageView) findViewById(R.id.barrageView_test);
        masterName = (TextView) findViewById(R.id.live_emcee_name);
        maseterHead = (CircleImageView) findViewById(R.id.live_emcee_head);
        touristList = (RecyclerView) findViewById(R.id.live_visitors_list_recy);
        liveGiftImg = (ImageView) findViewById(R.id.live_gift_img);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LivePlayerBaseActivity.this, LinearLayoutManager.HORIZONTAL, false);
        touristList.setLayoutManager(linearLayoutManager);
        onlineCountText = findView(R.id.live_online_count);
        scoreLayout = (RelativeLayout) findViewById(R.id.audience_score_layout);
        scoreTv = (TextView) findViewById(R.id.audience_score_tv);
        giftPlaneUp = (ImageView) findViewById(R.id.gift_plane_up);
        giftPlaneBg = (RelativeLayout) findViewById(R.id.audient_gift_plane_bg);
        anchorHeadImg = (ImageView) findViewById(R.id.audience_anchor_headportrait);
        userHeadImg = (ImageView) findViewById(R.id.audience_user_headportrait);
      //  chanllenge = (ImageView) findViewById(R.id.live_telecast_challenge);


        // 礼物动画展示
        findGiftLayout();
        // 点赞的爱心布局
        periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);
        View liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.GONE);
        container = new Container(this, roomId, SessionTypeEnum.ChatRoom, this);
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
        if (messageListPanel == null) {
            messageListPanel = new ChatRoomMsgListPanel(container, view, annoucement, LivePlayerBaseActivity.this);
        }


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
     //   chanllenge.setOnClickListener(clickListener);

        //软键盘输入框
        editTextInput = (EditText) findViewById(R.id.editTextMessage);
        inputChar = (ImageView) findViewById(R.id.live_telecast_input_text);
        masterInfoLayout = (LinearLayout) findViewById(R.id.live_master_info_layout);
        masterInfoLayout.setOnClickListener(clickListener);
        //分享
        ImageView shareLiveIng = (ImageView) findViewById(R.id.live_telecast_share);
        shareLiveIng.setOnClickListener(clickListener);

        drawerLayout = (DrawerLayout) findViewById(R.id.live_drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.openDrawer(Gravity.RIGHT);
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

            }
        });
    }

    public class MyCallBack extends ICallBack {
        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            result = liveUserInfosDataResp.getResult();
            dialogId = result.getId();
            meberAccount = result.getAccId();
            int userId = UserHelper.getUserId(LivePlayerBaseActivity.this);
            boolean logined = UserHelper.isLogined(LivePlayerBaseActivity.this);
            if (!logined) {
                showLoginViewDialog();
            } else {
                if (dialogId == userId) {
                    LiveConstant.IDENTITY = LiveConstant.IS_ONESELF;
                } else if (LiveConstant.IDENTITY != LiveConstant.IS_LIVEER) {
                    LiveConstant.IDENTITY = -1;
                }

                contentLoader.checkUserIdentity(channelId, String.valueOf(dialogId));

            }
        }

        @Override
        public void onCheckManager(LiveManagerBean liveManagerBean) {
            super.onCheckManager(liveManagerBean);
            manageResult = liveManagerBean.getResult();

            contentLoader.getLiveManagerList(channelId);


        }

        @Override
        public void onManagerList(LiveManagerListResp liveManagerListResp) {
            super.onManagerList(liveManagerListResp);
            List<LiveManagerListBean> managerList = liveManagerListResp.getResult();
            if (banListLive.size() > 0) {
                for (String name : banListLive) {
                    if (name.equals(meberAccount)) {
                        isMuted = true;
                    } else {
                        isMuted = false;
                    }
                }
            } else {
                isMuted = false;
            }
            showMasterInfoPopuwindow(result, isMuted, meberAccount, dialogId, manageResult, managerList);

        }

        @Override
        public void onLoginSucess(User user) {
            super.onLoginSucess(user);

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
                showGiftRanksPopuWindow(liveGiftRanksResp);
            }
        }

        @Override
        public void onGetAudienceOnLineUserCount(String json) {
            super.onGetAudienceOnLineUserCount(json);
            OnLineUser onLineUser = new Gson().fromJson(json, OnLineUser.class);
            if(onLineUser!=null&&onLineUser.getResult()>0){
                AppLog.i("TAG","从服务器拉去人数:"+onLineUser.getResult());
                onlineCountText.setText(String.valueOf(onLineUser.getResult())+"人");
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
                    if (LiveConstant.USER_INFO_FIRST_CLICK) {
                        LiveConstant.USER_INFO_FIRST_CLICK = false;

                        if (inputPanel != null) {
                            inputPanel.hideInputMethod();
                        }
                        contentLoader.getLiveUserInfo(userId);
                        contentLoader.setCallBack(myCallBack);
                        if (userId.equals(UserHelper.getUserId(LivePlayerBaseActivity.this))) {

                            LiveConstant.IDENTITY = LiveConstant.IS_ONESELF;
                        } else {

                            LiveConstant.IDENTITY = LiveConstant.IS_LIVEER;
                        }
                        userIdItem = userId;
                    }
                    break;

                case R.id.live_telecast_share:
                    if(LivePlayerBaseActivity.this instanceof AudienceActivity){
                        MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_USER_SHARE);
                    }
                    if(LivePlayerBaseActivity.this instanceof LiveActivity){
                        MobHelper.sendEevent(LivePlayerBaseActivity.this, MobEvent.LIVE_ANCHOR_SHARE);
                    }
                    if (shareVO != null) {
                        SharePopupWindow shareActivity = new SharePopupWindow(LivePlayerBaseActivity.this, shareVO);
                        shareActivity.showShareWindow();
                        shareActivity.showAtLocation(LivePlayerBaseActivity.this.findViewById(R.id.live_layout),
                                Gravity.CENTER, 0, 0);

                    }
                    break;
                case R.id.master_info_back_home:
                    finish();
                    break;
                case R.id.audience_score_layout:

                    if (firstClick) {
                        firstClick = false;
                        boolean logineds = UserHelper.isLogined(LivePlayerBaseActivity.this);
                        if (logineds) {
                            contentLoader.liveGiftRanks(channelId);
                            contentLoader.setCallBack(myCallBack);
                        } else {
                            showLoginViewDialog();
                        }
                    }


                    break;
                case R.id.live_telecast_input_text:

                    break;
               /* case R.id.live_telecast_challenge:
                    clickChallengeBtn();
                    break;*/
            }
        }
    };


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
                    String content = barrageMessage.getContent();

                    if (senderNick != null) {
                        barrageView.init(new BarrageConfig());
                        if (content != null) {
                            BarrageViewBean barrageBean = new BarrageViewBean();
                            String s = senderNick + ":" + content;
                            barrageBean.setUserId(messageUserId);
                            barrageBean.setContent(s);
                            barrageView.addTextBarrage(barrageBean);
                        }
                    }

                    barrageView.setOnBarrageClickListener(new BarrageView.OnBarrageClickListener() {
                        @Override
                        public void getUserId(String userId) {
                            //     Toast.makeText(LivePlayerBaseActivity.this, "弹幕点击了" + userId, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case MessageType.like://点赞
                    periscopeLayout.addHeart();
                    messageListPanel.onIncomingMessage(messages);

                    break;
                case MessageType.gift:

                    if ("003".equals(code)) {
                        giftPlaneAnimation.showPlaneAnimation((ChatRoomMessage) message);
                    } else {
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

                case MessageType.onlineNum:
                    //更新在綫人數
                    AppLog.i("TAG", "获取主播上传的人数:" + onlineNum);
                      /*  if(onlineNum!=null&&onlineNum.length()>0){
                         //   onlineCountText.setText(String.format("%s人", String.valueOf(onlineNum)));
                            onlineCountText.setText(String.valueOf(onlineNum)+"人");
                        }*/

                    break;
                case MessageType.leaveLive:
                    //    AppLog.i("TAG","接受主播离开的消息");
                    break;
                case MessageType.challenge:
                 /*   AppLog.i("TAG", "收到挑战信息");
                    receiveChallengeMessage((ChatRoomMessage) message);
                    messageListPanel.onIncomingMessage(messages);*/
                    break;

            }


            if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();

                switch (notificationAttachment.getType()) {
                    case ChatRoomMemberIn:
                        //发送进入直播间的通知
                        isOnLineUsersCountChange = true;
                        sendMessage(message, MessageType.text);
                        break;

                    case ChatRoomMemberExit:
                        isOnLineUsersCountChange = true;
                        //退出直播间，解除禁言
                       /* if (banListLive.size() > 0) {
                            for (int i = 0; i < banListLive.size(); i++) {
                                if (message.getFromAccount().equals(banListLive.get(i))) {
                                    banListLive.remove(i);
                                }
                            }
                        }*/
                        break;
                    case ChatRoomInfoUpdated:
                        sendMessage(message, MessageType.text);
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

    protected abstract void receiveChallengeMessage(ChatRoomMessage message);


    /**************************
     * 断网重连
     ****************************/
    protected void onOnlineStatusChanged(boolean isOnline) {
        if (isOnline) {
            onConnected();
        } else {
            onDisconnected();
            enterRoom();
        //    MobHelper.sendEevent(getActivity(), MobEvent.MY_WALLET);
        }
    }


    // 进入聊天室
    protected void enterRoom() {
        if (DemoCache.getLoginStatus()) {
            EnterChatRoomData data = new EnterChatRoomData(roomId);
            data.setRoomId(roomId);
            Map<String, Object> map = new HashMap<>();
            int userId = UserHelper.getUserId(LivePlayerBaseActivity.this);
            map.put("userId", String.valueOf(userId));
            map.put("roomExt", String.valueOf(userId));
            data.setExtension(map);
            data.setAvatar(UserHelper.getUserAvatar(LivePlayerBaseActivity.this));
            data.setNick(UserHelper.getUserName(LivePlayerBaseActivity.this));
            enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
            enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
                @Override
                public void onSuccess(EnterChatRoomResultData result) {
                    onLoginDone();
                    roomInfo = result.getRoomInfo();
                    member1 = result.getMember();
                    enterroomgetnick = member1.getNick();
                    masterAvatar = member1.getAvatar();
                    creatorAccount = roomInfo.getCreator();
                    DemoCache.setLoginChatRoomStatus(true);
                    member1.setRoomId(roomInfo.getRoomId());
                    ChatRoomMemberCache.getInstance().saveMyMember(member1);
                    DrawableUtils.displayImg(LivePlayerBaseActivity.this, maseterHead, avatar);
                    if (isFirstEnrRoom) {
                        updateUI();
                    }
                    DemoCache.setLoginChatRoomStatus(true);
                    chatRoomStatusRemind("登陆聊天室成功...");
                    LiveConstant.enterRoom=true;
                    initInputPanel(creatorAccount, channelId);
                  //  testMessage();
                }
                @Override
                public void onFailed(int code) {
                    AppLog.i("TAG", "登录聊天室失败：" + code);
                    DemoCache.setLoginChatRoomStatus(false);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            enterRoom();
                        }
                    });
                    onLoginDone();
                }

                @Override
                public void onException(Throwable exception) {
                    DemoCache.setLoginChatRoomStatus(false);
                    onLoginDone();

                    finish();
                }
            });
            //查询在线成员列表
           /* if (isFirstEnrRoom) {
                fetchData();
            }*/

        }
    }
    int count=0;
    private void testMessage() {
        new CountDownTimer(100000000, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                LiveMessage liveMessage=new LiveMessage();
                liveMessage.setStyle(MessageType.text);
                liveMessage.setUserId(userId);
                liveMessage.setCreatorAccount(creatorAccount);
                liveMessage.setChannelId(channelId);
                IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "哈哈哈哈："+count, roomId, AuthPreferences.getUserAccount(), liveMessage);
                sendMessage(imMessage, MessageType.text);
                count++;
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    protected void initInputPanel(String creatorAccount, String channelId) {

        if (inputPanel == null) {
            inputPanel = new InputPanel(LivePlayerBaseActivity.this, container, view, getActionList(), inputConfig, creatorAccount, UserHelper.getUserId(LivePlayerBaseActivity.this), channelId);
        } else {
            inputPanel.reload(container, inputConfig);
        }

    }

    private void chatRoomStatusRemind(String remindContent) {
        ChatRoomNotificationHelper.content = remindContent;
        ChatRoomNotificationAttachment notificationAttachment = new ChatRoomNotificationAttachment();
        notificationAttachment.setType(NotificationType.AVChatRecord);
        IMMessage customMessage = MessageBuilder.createCustomMessage(remindContent, SessionTypeEnum.ChatRoom, notificationAttachment);
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "ext data");
        data.put("key2", 2015);
        customMessage.setRemoteExtension(data);
        messageListPanel.onMsgSend(customMessage);
    }

    protected void fetchData() {
        if (!isNormalEmpty) {
            // 拉取固定在线成员
            getMembers(MemberQueryType.ONLINE_NORMAL, updateTime, 0);
        } else {
            // 拉取非固定成员
            getMembers(MemberQueryType.GUEST, enterTime, 0);
        }
    }

    private void clearCache() {
        updateTime = 0;
        enterTime = 0;
        allItems.clear();
        memberCache.clear();
        isNormalEmpty = false;
    }

    boolean isFirstEnrRoom = true;
    private Map<String, ChatRoomMember> memberCache = new ConcurrentHashMap<>();

    private void getMembers(final MemberQueryType memberQueryType, final long time, int limit) {

        ChatRoomMemberCache.getInstance().fetchRoomMembers(roomId, memberQueryType, time, (LIMIT - limit), new SimpleCallback<List<ChatRoomMember>>() {
            @Override
            public void onResult(boolean success, List<ChatRoomMember> result) {

                if (success) {
                    addMembers(result);
                    if (memberQueryType == MemberQueryType.ONLINE_NORMAL && result.size() < LIMIT) {
                        isNormalEmpty = true; // 固定成员已经拉完
                        getMembers(MemberQueryType.GUEST, enterTime, result.size());
                    }
                } else {

                    AppLog.i("TAG", "拉取在线人数失败");
                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    isFirstEnrRoom=false;
                    enterRoom();

                }
            }

        });
    }


    protected void addMembers(List<ChatRoomMember> members) {
        for (ChatRoomMember member : members) {
            if (!isNormalEmpty) {
                updateTime = member.getUpdateTime();
            } else {
                enterTime = member.getEnterTime();
            }
            if (memberCache.containsKey(member.getAccount())) {
                allItems.remove(memberCache.get(member.getAccount()));
            }
            memberCache.put(member.getAccount(), member);
            if (member.getMemberType() == MemberType.CREATOR) {
                continue;
            }
            allItems.add(member);
        }
        Collections.sort(allItems, comp);

        if (isFirstLoadding) {
            tourisAdapter = new TouristAdapter(LivePlayerBaseActivity.this, allItems);
            touristList.setAdapter(tourisAdapter);
            isFirstLoadding = false;

        } else if (allItems.size() > 0) {
            tourisAdapter.refresh(allItems);
        }

        tourisAdapter.setOnTouristItemClickListener(new TouristAdapter.OnTouristItemClickListener() {
            @Override
            public void showTouristInfo(ChatRoomMember member, boolean isMasterAccount) {

                if (LiveConstant.USER_INFO_FIRST_CLICK) {
                    LiveConstant.USER_INFO_FIRST_CLICK = false;

                    boolean isLogin = UserHelper.isLogined(LivePlayerBaseActivity.this);
                    if (!isLogin) {
                        showLoginViewDialog();
                    } else {
                        Map<String, Object> extension = member.getExtension();
                        inputPanel.hideInputMethod();
                        if (!isMasterAccount) {
                            if (extension != null) {
                                userIdItem = (String) extension.get("userId");
                            } else {
                                int userId = UserHelper.getUserId(LivePlayerBaseActivity.this);
                                userIdItem = String.valueOf(userId);
                            }
                            if ("-1".equals(userIdItem) || userIdItem == null || userIdItem.length() == 0) {
                                Toast.makeText(LivePlayerBaseActivity.this, "该用户为游客!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            contentLoader.getLiveUserInfo(userIdItem);
                            contentLoader.setCallBack(myCallBack);
                        }

                    }

                }
            }
        });
    }


    private static Comparator<ChatRoomMember> comp = new Comparator<ChatRoomMember>() {
        @Override
        public int compare(ChatRoomMember lhs, ChatRoomMember rhs) {
            if (lhs == null) {
                return 1;
            }
            if (rhs == null) {
                return -1;
            }

            return compMap.get(lhs.getMemberType()) - compMap.get(rhs.getMemberType());
        }
    };

    private void onLoginDone() {
        enterRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    protected void updateUI() {
        handler.postDelayed(new MyRunnable(), 2000);
    }

    boolean isOnLineUsersCountChange = true;

    private Handler handler = new Handler();

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            handler.removeCallbacks(this);
            fetchOnlineCount();

            handler.postDelayed(this, 2000);
        }
    }
    private void fetchOnlineCount() {
        if (isOnLineUsersCountChange) {
            isOnLineUsersCountChange = false;
            NIMClient.getService(ChatRoomService.class).fetchRoomInfo(roomId).setCallback(new RequestCallback<ChatRoomInfo>() {
                @Override
                public void onSuccess(final ChatRoomInfo param) {
                    onlineCounts = param.getOnlineUserCount();
                    if (contentLoader!=null) {
                        contentLoader.getAudienceUserOnLine(onlineCounts,channelId);
                    }
                    AppLog.i("TAG", "基类获取在线人数:" + onlineCounts);
                    if (isScrollStop && mIsTouchUP) {
                        clearCache();
                        fetchData();
                    }
                }

                @Override
                public void onFailed(int code) {
                    AppLog.i("TAG", "定时拉去在线人数失败:" + code);
                    isFirstEnrRoom=false;
                  //  enterRoom();
                }

                @Override
                public void onException(Throwable exception) {
                    AppLog.i("TAG", "查询列表定时任务失败：");
                }
            });
        }
    }


    private void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }

    // 初始化礼物布局
    protected void findGiftLayout() {
        giftAnimationViewDown = findView(R.id.gift_animation_view);
        giftAnimationViewUp = findView(R.id.gift_animation_view_up);
        giftAnimation = new GiftAnimations(giftPlaneUp, giftAnimationViewDown, giftAnimationViewUp, this);
        giftPlaneAnimation = new GiftPlaneAnimation(anchorHeadImg, userHeadImg, giftPlaneUp, giftPlaneBg, this, avatar);

    }


    private void claerImLoginInfo() {
        DemoCache.clear();
        AuthPreferences.clearUserInfo();
        NIMClient.getService(AuthService.class).logout();
        DemoCache.setLoginStatus(false);
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
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
        if (inputPanel != null) {
            inputPanel.hideInputMethod();

            inputPanel.collapse(false);

        }

        if (messageListPanel != null) {
            messageListPanel.onResume();
        }
    }

    @Override
    public void onBackPressed() {


        if (inputPanel != null && inputPanel.collapse(true)) {
        }
        if (messageListPanel != null && messageListPanel.onBackPressed()) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        unregisterReceiver(myNetReceiver);

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
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
                        Gravity.CENTER, 0, 0);
            }

            @Override
            public void ranListItemClick(RankUserBean rankUser) {
                contentLoader.getLiveUserInfo(String.valueOf(rankUser.getId()));
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

            switch (type) {
                case MessageType.text:
                    messageListPanel.onMsgSend(message);
                    break;
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
                        String s = "我" + ":" + content;
                        barrageViewBean.setUserId(messageUserId);
                        barrageViewBean.setContent(s);
                        String name = DemoCache.getUserInfo().getName();
                        barrageView.addTextBarrage(barrageViewBean);
                    }
                    barrageView.setOnBarrageClickListener(new BarrageView.OnBarrageClickListener() {
                        @Override
                        public void getUserId(String userId) {
                            Toast.makeText(LivePlayerBaseActivity.this, "弹幕点击了" + userId, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case MessageType.like:
                    messageListPanel.onMsgSend(message);
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
            }

            NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                    .setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                        }

                        @Override
                        public void onFailed(int code) {

                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
        }

        return true;
    }

    //登录界面
    public void showLoginViewDialog() {
        final CustomChatDialog customDialog = new CustomChatDialog(this);
        customDialog.setContent(getString(R.string.live_login_hint));
        customDialog.setCancelable(false);
        customDialog.setCancelBtn(getString(R.string.live_canncel), null);
        customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                DemoCache.setLoginStatus(false);
                ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
                startActivityForResult(new Intent(LivePlayerBaseActivity.this, LoginActivity.class), LIVE_BASE_RESQUEST_CODE);
            }
        });
        customDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DialogUtil.clear();
    }
}