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
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.ui.TActivity;
import com.lalocal.lalocal.live.entertainment.adapter.GiftAdapter;
import com.lalocal.lalocal.live.entertainment.adapter.TouristAdapter;
import com.lalocal.lalocal.live.entertainment.constant.CustomDialogStyle;
import com.lalocal.lalocal.live.entertainment.constant.GiftType;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomNotificationHelper;
import com.lalocal.lalocal.live.entertainment.helper.GiftAnimations;
import com.lalocal.lalocal.live.entertainment.helper.SimpleCallback;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResp;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResultBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.module.ChatRoomMsgListPanel;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.im.session.Container;
import com.lalocal.lalocal.live.im.session.ModuleProxy;
import com.lalocal.lalocal.live.im.session.actions.BaseAction;
import com.lalocal.lalocal.live.im.session.input.InputConfig;
import com.lalocal.lalocal.live.im.session.input.InputPanel;
import com.lalocal.lalocal.live.im.ui.barrage.BarrageConfig;
import com.lalocal.lalocal.live.im.ui.barrage.BarrageView;
import com.lalocal.lalocal.live.im.ui.dialog.DialogMaker;
import com.lalocal.lalocal.live.im.ui.periscope.PeriscopeLayout;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.TouristInfoResp;
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
import com.netease.nimlib.sdk.ResponseCode;
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
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 直播端和观众端的基类
 */
public abstract class LivePlayerBaseActivity extends TActivity implements ModuleProxy {
    private static final String TAG = LivePlayerBaseActivity.class.getSimpleName();
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    private final static String AVATAR = "AVATAR";
    private final static String USER_ID = "USER_ID";
    public static final String LIVE_USER_ID = "LIVE_USER_ID";
    public static final String ANNOUCEMENT = "ANNOUCEMENT";
    public static final String CHANNELID="CHANNELID";
    private final static int FETCH_ONLINE_PEOPLE_COUNTS_DELTA =5000;
    public static final int LIVE_BASE_RESQUEST_CODE = 701;
    private static final int LIMIT = 100;
    public static final int REFRESH=101;
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
    private TextView onlineCountText; // 在线人数view
    protected GridView giftView; // 礼物列表
    protected ImageButton giftBtn; // 礼物按钮
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
    protected ViewGroup giftLayout; // 礼物布局
    protected ViewGroup controlLayout; // 右下角几个image button布局
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
    private TouristAdapter tourisAdapter;
    private ChatRoomMember master;
    ChatRoomMember member1;
    private List<ChatRoomMember> items = null;
    private List<ChatRoomMember> allItems = new ArrayList<>();
    private SpecialShareVOBean shareVO;

    public int onlineCounts;
    protected int maxOnLineCount = 0;
    public String avatar;
    public String annoucement;//公告
    protected String userId;
    private String userIdItem;
    private String enterroomgetnick;
    private String avatarIntetn;
    protected String masterAvatar;
    protected String creatorAccount;//聊天室创建者账号
    private String barrageContent;
    private String fromNickBarrage;

    private boolean isFirstLoadding = true;
    boolean isFirstClick = true;//防止快速点击，出现两个popuwindow
    protected boolean barrageSelectorStatus = false;
    private boolean isNormalEmpty = false; // 固定成员是否拉取完
    private long updateTime = 0; // 非游客的updateTime
    private long enterTime = 0;

    protected ContentLoader contentLoader;
    protected List<GiftDataResultBean> giftSresult;
    protected ImageView giftPlane;
    private String code;
    private AnimationDrawable rocketAnimation;
    private LiveUserInfoResultBean result;
    private boolean isMuted;//是否禁言
    private String meberAccount;//聊天室成员账号
    private int meberType;//聊天室成员类型
    private String channelId;
    private int dialogId;
    private String disableSendMsgUserId;
    List<String> banListAudience=new ArrayList<>();
    List<String> banListLive=new ArrayList<>();

    protected abstract void checkNetInfo(String netType,int reminder);

    protected abstract void onConnected(); // 网络连上

    protected abstract void onDisconnected(); // 网络断开

    protected abstract int getActivityLayout(); // activity布局文件

    protected abstract int getLayoutId(); // 根布局资源id

    protected abstract void initParam(); // 初始化推流/拉流参数，具体在子类中实现

    protected abstract void showMasterInfoPopuwindow(LiveUserInfoResultBean result,boolean isMuted,String meberAccount,int id,int managerId);

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

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==110){
                rocketAnimation.stop();
                giftPlane.setVisibility(View.GONE);
                startPlaneAnimation();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
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


    void parseIntent() {
        roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);
        url = getIntent().getStringExtra(EXTRA_URL);
        avatarIntetn = getIntent().getStringExtra(AVATAR);
        userId = getIntent().getStringExtra(LIVE_USER_ID);
        shareVO = getIntent().getParcelableExtra("shareVO");
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
        channelId = getIntent().getStringExtra(CHANNELID);

    }

    protected void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineStatus, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    //检测网络类型
    private void registerNetStatus() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
    }

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
                        checkNetInfo("wifi",0);
                    } else {
                        checkNetInfo("rests",0);
                    }
                }
            }
        }
    };

    /****************************
     * 布局初始化
     **************************/
    protected void findViews() {
        barrageView = (BarrageView) findViewById(R.id.barrageView_test);
        masterName = (TextView) findViewById(R.id.live_emcee_name);
        maseterHead = (CircleImageView) findViewById(R.id.live_emcee_head);
        touristList = (RecyclerView) findViewById(R.id.live_visitors_list_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LivePlayerBaseActivity.this, LinearLayoutManager.HORIZONTAL, false);
        touristList.setLayoutManager(linearLayoutManager);
        onlineCountText = findView(R.id.live_online_count);
        giftBtn = (ImageButton) findViewById(R.id.gift_btn);
        giftPlane = (ImageView) findViewById(R.id.gift_plane);
        // 礼物列表
          findGiftLayout();
        // 点赞的爱心布局
        periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);
        View liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.GONE);
        container = new Container(this, roomId, SessionTypeEnum.ChatRoom, this);
        view = findViewById(getLayoutId());
        InputConfig inputConfig = new InputConfig();
        inputConfig.isTextAudioSwitchShow = false;
        inputConfig.isMoreFunctionShow = false;
        inputConfig.isEmojiButtonShow = false;
        if (inputPanel == null) {
            inputPanel = new InputPanel(this, container, view, getActionList(), inputConfig);
        } else {
            inputPanel.reload(container, inputConfig);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldCollapseInputPanel();
            }
        });

        //弹幕开关状态
        inputPanel.setOnBarrageViewCheckStatusListener(new InputPanel.OnBarrageViewCheckStatusListener() {
            @Override
            public void getBarrageViewCheckStatus(boolean isCheck, String content) {
                barrageView.init(new BarrageConfig());
                if (content != null) {
                    barrageView.addTextBarrage(content);
                }
            }
        });
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
            int userId = UserHelper.getUserId(LivePlayerBaseActivity.this);
            AppLog.i("TAG","onLiveUserInfo:"+userId+"resultId:"+result.getId());
            boolean logined = UserHelper.isLogined(LivePlayerBaseActivity.this);
            if (!logined) {
                showLoginViewDialog();
            } else {
               contentLoader.checkUserIdentity(channelId,String.valueOf(userId));
            }
        }
        @Override
        public void onCheckManager(LiveManagerBean liveManagerBean) {
            super.onCheckManager(liveManagerBean);
            int results = liveManagerBean.getResult();
            AppLog.i("TAG","获取禁言列表："+results);
            if(banListLive.size()>0){
                for(String name:banListLive){
                    if(name.equals(meberAccount)){
                        isMuted=true;
                    }else{
                        isMuted=false;
                    }
                }
            }else {
                isMuted=false;
            }
            showMasterInfoPopuwindow(result,isMuted,meberAccount,dialogId,results);
        }
        @Override
        public void onLoginSucess(User user) {
            super.onLoginSucess(user);
        }

        @Override
        public void onTouristInfo(String json) {
            super.onTouristInfo(json);
            TouristInfoResp touristInfoResp = new Gson().fromJson(json, TouristInfoResp.class);
            if (touristInfoResp.getReturnCode() == 0) {
                TouristInfoResp.ResultBean result = touristInfoResp.getResult();
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

        @Override
        public void onGiftsStore(GiftDataResp giftDataResp) {
            if(giftDataResp.getReturnCode()==0){
                AppLog.i("TAG","请求成功");
                giftSresult = giftDataResp.getResult();
            }

        }
    }


    protected void loginIMServer(final String imccId, String imToken) {
        AppLog.i("TAG","获取云信账号："+"imccId:"+imccId+"imToken:"+imToken);
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

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.live_master_info_layout:
                    inputPanel.hideInputMethod();
                    if (isFirstClick) {
                        isFirstClick = false;
                        contentLoader.getLiveUserInfo(userId);
                        CustomDialogStyle.CUSTOM_DIALOG_STYLE=1;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                isFirstClick = true;
                            }
                        }, 500);
                    }
                    userIdItem = userId;
                    break;

                case R.id.live_telecast_share:
                    if (shareVO != null) {
                        String s = new Gson().toJson(shareVO);
                        SharePopupWindow shareActivity = new SharePopupWindow(LivePlayerBaseActivity.this, shareVO);
                        shareActivity.showShareWindow();
                        shareActivity.showAtLocation(LivePlayerBaseActivity.this.findViewById(R.id.live_layout),
                                Gravity.CENTER, 0, 0);

                    }
                    break;
                case R.id.master_info_back_home:
                    finish();
                    break;

            }
        }
    };
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            AppLog.i("TAG", "LivePlayerBaseActivity監聽用戶登錄狀態愛;" + statusCode);

        }
    };


    Queue<ChatRoomMessage> cache=new LinkedList<>();
    boolean isFirstPlane=true;
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {

        @Override
        public void onEvent(List<ChatRoomMessage> messages) {

            String styles = null;
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
                    if ("barrag".equals(key)) {
                        barrageContent = value.toString();
                    }
                    if ("style".equals(key)) {
                        styles = value.toString();
                    }
                    if("code".equals(key)){
                        code = value.toString();
                    }
                    if("disableSendMsgUserId".equals(key)){
                        disableSendMsgUserId = value.toString();
                    }

                }
            }

            if (styles != null) {
                switch (styles) {
                    case "0"://聊天
                      
                        messageListPanel.onIncomingMessage(messages);
                        break;
                    case "1"://点赞
                        ChatRoomMessage barrageMessage = (ChatRoomMessage) message;
                        String senderNick = barrageMessage.getChatRoomMessageExtension().getSenderNick();
                        String content = barrageMessage.getContent();
                        if (senderNick != null) {
                            barrageView.init(new BarrageConfig());
                            if (content != null) {
                                barrageView.addTextBarrage(senderNick + ":" + content);
                            }
                        }
                        break;
                    case "2"://弹幕

                        periscopeLayout.addHeart();
                        messageListPanel.onIncomingMessage(messages);

                        break;
                    case "10":
                        if("003".equals(code)){
                            cache.add((ChatRoomMessage)message);
                            startPlaneAnimation();
                        }else {
                            giftAnimation.showGiftAnimation((ChatRoomMessage) message);
                        }

                        break;
                    case "6"://禁言

                        banListAudience.add(disableSendMsgUserId);
                        for(String name:banListAudience){
                            AppLog.i("TAG","打印banlist："+name);
                        }
                        messageListPanel.onIncomingMessage(messages);
                        break;
                    case "7"://解除禁言

                        AppLog.i("TAG","解除了禁言。。。。。。。。。。。。。。。。");
                        if(banListAudience.size()>0){
                            for(int i=0;i<banListAudience.size();i++){
                                if(disableSendMsgUserId.equals(banListAudience.get(i))){
                                    banListAudience.remove(i);
                                    return;
                                }
                            }
                        }
                        messageListPanel.onIncomingMessage(messages);
                        break;
                }
            }

            if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();

                switch (notificationAttachment.getType()) {
                    case ChatRoomMemberIn:
                        //发送进入直播间的通知
                        sendMessage(message, MessageType.text);
                        break;

                    case ChatRoomMemberExit:
                        //退出直播间，解除禁言
                        if(banListLive.size()>0){
                            for(int i=0;i<banListLive.size();i++){
                                if(message.getFromAccount().equals(banListLive.get(i))){
                                    banListLive.remove(i);
                                }
                            }
                        }
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

    protected void  startPlaneAnimation() {
        ChatRoomMessage message = cache.poll();
        if(message == null) {
            return;
        }
        giftPlane.setVisibility(View.VISIBLE);
        giftPlane.setBackgroundResource(R.drawable.plane_rocket);
        rocketAnimation = (AnimationDrawable) giftPlane.getBackground();
        rocketAnimation.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(110);
            }
        },4400);
    }
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



    /**************************
     * 断网重连
     ****************************/
    protected void onOnlineStatusChanged(boolean isOnline) {
        if (isOnline) {
            onConnected();
        } else {
            onDisconnected();
            enterRoom();
        }
    }

    // 进入聊天室
    protected void enterRoom() {
        if (messageListPanel == null) {
            messageListPanel = new ChatRoomMsgListPanel(container, view, annoucement);
        }

        EnterChatRoomData data = new EnterChatRoomData(roomId);
        data.setRoomId(roomId);
        Map<String, Object> map = new HashMap<>();
        int userId = UserHelper.getUserId(LivePlayerBaseActivity.this);
        map.put("userId", String.valueOf(userId));
        map.put("roomExt", String.valueOf(userId));
        data.setExtension(map);
        data.setAvatar(UserHelper.getUserAvatar(LivePlayerBaseActivity.this));
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
                masterName.setText(enterroomgetnick);
                updateUI(enterroomgetnick);
                DemoCache.setLoginChatRoomStatus(true);
                chatRoomStatusRemind("登陆聊天室成功...");
            }

            @Override
            public void onFailed(int code) {
                AppLog.i("TAG", "登录聊天室失败：" + code);
                DemoCache.setLoginChatRoomStatus(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
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
                //      Toast.makeText(LivePlayerBaseActivity.this, "enter chat room exception, e=" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //查询在线成员列表
        fetchData();
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
        } else {

            tourisAdapter.refresh(allItems);

        }

        tourisAdapter.setOnTouristItemClickListener(new TouristAdapter.OnTouristItemClickListener() {
            @Override
            public void showTouristInfo(ChatRoomMember member, boolean isMasterAccount) {
                meberAccount = member.getAccount();
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
                            if ("-1".equals(userIdItem)) {
                                Toast.makeText(LivePlayerBaseActivity.this, "该用户为游客!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            contentLoader.getLiveUserInfo(userIdItem);
                            CustomDialogStyle.CUSTOM_DIALOG_STYLE=2;
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

    protected void updateUI(String nick) {
        masterName.setText(nick);
        onlineCountText.setText(String.format("%s人", String.valueOf(roomInfo.getOnlineUserCount())));
        fetchOnlineCount();
    }


    private void fetchOnlineCount() {

        if (timer == null) {
            timer = new Timer();
        }

        //开始一个定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                NIMClient.getService(ChatRoomService.class).fetchRoomInfo(roomId).setCallback(new RequestCallback<ChatRoomInfo>() {
                    @Override
                    public void onSuccess(final ChatRoomInfo param) {
                        onlineCounts = param.getOnlineUserCount();
                        onlineCountText.setText(String.format("%s人", String.valueOf(onlineCounts)));
                        clearCache();
                        fetchData();
                        int onlineUserCount = param.getOnlineUserCount();
                        if (onlineUserCount > maxOnLineCount) {//计算最大在线人数
                            maxOnLineCount = onlineUserCount;
                        }
                    }

                    @Override
                    public void onFailed(int code) {

                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }
        }, 100, FETCH_ONLINE_PEOPLE_COUNTS_DELTA);
    }

    /**************************
     * Module proxy
     ***************************/

    @Override
    public boolean sendMessage(IMMessage msg, int type) {

        if (msg != null) {
            fromNickBarrage = msg.getFromNick();
            ChatRoomMessage message = (ChatRoomMessage) msg;

            String fromAccount = message.getFromAccount();
            if(banListAudience.size()>0){
                AppLog.i("TAG","查看禁言消息账号"+banListAudience.get(0).toString()+"fromAccountfff:"+fromAccount+"   msg:"+msg.getFromAccount());
                for(String account:banListAudience){
                    if(account.equals(msg.getFromAccount())){
                        Toast.makeText(LivePlayerBaseActivity.this,"你已被管理员禁言",Toast.LENGTH_SHORT).show();
                        return  false;
                    }
                }
            }

            switch (type){
                case MessageType.text:
                        messageListPanel.onMsgSend(message);
                    break;
                case MessageType.barrage:
                    String content = msg.getContent();
                    barrageView.init(new BarrageConfig());
                    if (content != null) {
                        String name = DemoCache.getUserInfo().getName();
                        barrageView.addTextBarrage("我" + ":" + content);
                    }
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
            }

            NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                    .setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            AppLog.i("TAG", "消息发送成功");
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == ResponseCode.RES_CHATROOM_MUTED) {
                                //  Toast.makeText(DemoCache.getContext(), "用户被禁言", Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(DemoCache.getContext(), "消息发送失败：code:" + code, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onException(Throwable exception) {
                            //    Toast.makeText(DemoCache.getContext(), "消息发送失败！", Toast.LENGTH_SHORT).show();
                        }
                    });




        }

        return true;
    }

    public void showLoginViewDialog() {
        CustomChatDialog customDialog = new CustomChatDialog(this);
        customDialog.setContent("还没登录，快去登录吧!");
        customDialog.setCancelable(false);
        customDialog.setCancelBtn("取消", null);
        customDialog.setSurceBtn("确定", new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                DemoCache.setLoginStatus(false);
                ChatRoomMemberCache.getInstance().clearRoomCache(roomId);

                startActivityForResult(new Intent(LivePlayerBaseActivity.this, LoginActivity.class), LIVE_BASE_RESQUEST_CODE);
            }
        });
        customDialog.show();
    }




    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
        @Override
        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
            Toast.makeText(LivePlayerBaseActivity.this, "被踢出聊天室，原因:" + chatRoomKickOutEvent.getReason(), Toast.LENGTH_SHORT).show();
            clearChatRoom();
        }
    };

    private void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }

    // 初始化礼物布局
    protected void findGiftLayout() {
        giftLayout = findView(R.id.gift_layout);
        giftView = findView(R.id.gift_grid_view);
        giftAnimationViewDown = findView(R.id.gift_animation_view);
        giftAnimationViewUp = findView(R.id.gift_animation_view_up);

        giftAnimation = new GiftAnimations(giftPlane,giftAnimationViewDown, giftAnimationViewUp,this);
    }

    // 更新礼物列表，由子类定义
    protected void updateGiftList(GiftType type) {

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
        //   isFirstClick = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        inputPanel.hideInputMethod();
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
        // controlLayout.setVisibility(View.GONE);
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
        // controlLayout.setVisibility(View.VISIBLE);
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

}