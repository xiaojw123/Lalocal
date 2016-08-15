package com.lalocal.lalocal.view.liveroomview.entertainment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.lalocal.lalocal.view.liveroomview.DemoCache;
import com.lalocal.lalocal.view.liveroomview.base.ui.TActivity;
import com.lalocal.lalocal.view.liveroomview.base.util.log.LogUtil;
import com.lalocal.lalocal.view.liveroomview.entertainment.adapter.GiftAdapter;
import com.lalocal.lalocal.view.liveroomview.entertainment.adapter.TouristAdapter;
import com.lalocal.lalocal.view.liveroomview.entertainment.constant.GiftType;
import com.lalocal.lalocal.view.liveroomview.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.view.liveroomview.entertainment.helper.GiftAnimation;
import com.lalocal.lalocal.view.liveroomview.entertainment.helper.SimpleCallback;
import com.lalocal.lalocal.view.liveroomview.entertainment.module.BarrageAttachment;
import com.lalocal.lalocal.view.liveroomview.entertainment.module.ChatRoomMsgListPanel;
import com.lalocal.lalocal.view.liveroomview.entertainment.module.GiftAttachment;
import com.lalocal.lalocal.view.liveroomview.entertainment.module.LikeAttachment;
import com.lalocal.lalocal.view.liveroomview.im.config.AuthPreferences;
import com.lalocal.lalocal.view.liveroomview.im.session.Container;
import com.lalocal.lalocal.view.liveroomview.im.session.ModuleProxy;
import com.lalocal.lalocal.view.liveroomview.im.session.actions.BaseAction;
import com.lalocal.lalocal.view.liveroomview.im.session.input.InputConfig;
import com.lalocal.lalocal.view.liveroomview.im.session.input.InputPanel;
import com.lalocal.lalocal.view.liveroomview.im.ui.barrage.BarrageConfig;
import com.lalocal.lalocal.view.liveroomview.im.ui.barrage.BarrageView;
import com.lalocal.lalocal.view.liveroomview.im.ui.dialog.DialogMaker;
import com.lalocal.lalocal.view.liveroomview.im.ui.periscope.PeriscopeLayout;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
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
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 直播端和观众端的基类
 *
 */
public abstract class LivePlayerBaseActivity extends TActivity implements ModuleProxy{
    private static final String TAG = LivePlayerBaseActivity.class.getSimpleName();

    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    private final static String AVATAR="AVATAR";
    private final static String USER_ID = "USER_ID";
    public static final String LIVE_USER_ID="LIVE_USER_ID";
    private final static int FETCH_ONLINE_PEOPLE_COUNTS_DELTA = 60 * 1000;
    private Timer timer;
    // 聊天室信息
    protected String roomId;
    protected ChatRoomInfo roomInfo;
    protected String url; // 推流/拉流地址
    // modules
    protected InputPanel inputPanel;
    protected ChatRoomMsgListPanel messageListPanel;

    // view
    private TextView onlineCountText; // 在线人数view
    protected GridView giftView; // 礼物列表
    private RelativeLayout giftAnimationViewDown; // 礼物动画布局1
    private RelativeLayout giftAnimationViewUp; // 礼物动画布局2
    protected PeriscopeLayout periscopeLayout; // 点赞爱心布局
    protected ImageButton giftBtn; // 礼物按钮
    protected ViewGroup giftLayout; // 礼物布局
    protected ViewGroup controlLayout; // 右下角几个image button布局

    // data
    protected GiftAdapter adapter;
    protected GiftAnimation giftAnimation; // 礼物动画

    private AbortableFuture<EnterChatRoomResultData> enterRequest;

    private ImageView inputText;



    protected TextView masterName;
    protected CircleImageView maseterHead;
    private RecyclerView touristList;



    private TouristAdapter tourisAdapter;
    private ChatRoomMember master;
    public String avatar;
    protected boolean barrageSelectorStatus=false;
    protected EditText editTextInput;
    protected ImageView inputChar;
    private String avatarIntetn;
    private LinearLayout masterInfoLayout;
    private String enterroomgetnick;
    private ContentLoader contentLoader;
    private String userId;
    private String userIdItem;
    private SpecialShareVOBean shareVO;
    protected DrawerLayout drawerLayout;
    protected Container container;
    private String barrageContent;
    public int onlineCounts;

    protected abstract int getActivityLayout(); // activity布局文件

    protected abstract int getLayoutId(); // 根布局资源id

    protected abstract void initParam(); // 初始化推流/拉流参数，具体在子类中实现

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        parseIntent();
        findViews();
        initParam();
    }

    public class MyCallBack extends ICallBack {
        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            LiveUserInfoResultBean result = liveUserInfosDataResp.getResult();
            showMasterInfoPopuwindow(result);
        }
    }
    void parseIntent() {
        roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);
        url = getIntent().getStringExtra(EXTRA_URL);
        avatarIntetn = getIntent().getStringExtra(AVATAR);
        userId = getIntent().getStringExtra(LIVE_USER_ID);
        shareVO = getIntent().getParcelableExtra("shareVO");

    }

    @Override
    protected void onResume() {
        super.onResume();
      isFirstClick=true;
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
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        adapter = null;
    }

    /***************************
     * 监听
     ****************************/
    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.master_info_close_iv:
                    isFirstClick=true;
                    masterInfoPopuwindow.dismiss();
                    inputPanel.hideInputMethod();
                    break;
                case R.id.live_master_info_layout:
                    inputPanel.hideInputMethod();
                    if(isFirstClick){
                        isFirstClick=false;
                        contentLoader.getLiveUserInfo(userId);
                    }

                    userIdItem=userId;
                    break;
                case R.id.live_master_home:
                    //直播用户信息主页
                    Intent intent=new Intent(LivePlayerBaseActivity.this,LiveHomePageActivity.class);
                    intent.putExtra("userId",userIdItem);
                    startActivity(intent);
                    masterInfoPopuwindow.dismiss();
                    break;
                case R.id.live_telecast_share:
                    SharePopupWindow shareActivity = new SharePopupWindow(LivePlayerBaseActivity.this, shareVO);
                    shareActivity.showShareWindow();
                    shareActivity.showAtLocation(LivePlayerBaseActivity.this.findViewById(R.id.live_layout),
                            Gravity.CENTER, 0, 0);
                    break;
            }
        }
    };



    protected void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineStatus, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver, register);
    }

    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                AppLog.i("TAG","收到空消息");
                return;
            }
            IMMessage message = messages.get(0);
            AppLog.i("TAG","弹幕content:"+message.getContent()+"    fromNick:"+message.getFromNick());
            Map<String, Object> remoteExtension = message.getRemoteExtension();
            if(remoteExtension!=null){
                Iterator<Map.Entry<String, Object>> iterator = remoteExtension.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, Object> next = iterator.next();
                    String key = next.getKey();
                    Object value = next.getValue();
                    if("barrag".equals(key)){
                        barrageContent = value.toString();
                    }
                    AppLog.i("TAG","key:"+key+"    value:"+value);
                }
                AppLog.i("TAG","content:"+message.getContent()+"nick:"+message.getFromNick());
            }

            if(message!=null&&"点赞".equals(message.getContent())){
                periscopeLayout.addHeart();
                AppLog.i("TAG","收到点赞爱心");
            }
            if (message != null && message.getAttachment() instanceof GiftAttachment) {
                // 收到礼物消息
                GiftType type = ((GiftAttachment) message.getAttachment()).getGiftType();
                updateGiftList(type);
                giftAnimation.showGiftAnimation((ChatRoomMessage) message);
            } else if (message != null && message.getAttachment() instanceof LikeAttachment) {
                // 收到点赞爱心
                periscopeLayout.addHeart();

            }else if(message != null && message.getAttachment() instanceof BarrageAttachment){//弹幕
                AppLog.i("TAG","BarrageAttachment弹幕监听到了啊a");
                barrageView.init(new BarrageConfig());
                if(barrageContent!=null){
                    barrageView.addTextBarrage(barrageContent);
                }


            } else if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();
                switch (notificationAttachment.getType()) {
                    case ChatRoomMemberIn:
                        //发送进入直播间的通知
                        sendMessage(message);
                        break;
                    case ChatRoomMemberExit:
                        //发送离开直播间的通知
                        sendMessage(message);
                        break;
                    case ChatRoomInfoUpdated:
                        sendMessage(message);
                        break;
                }
            } else {
                AppLog.i("TAG","incomingChatRoomMsg"+"其他消息");
                messageListPanel.onIncomingMessage(messages);

            }
        }
    };

    Observer<ChatRoomStatusChangeData> onlineStatus = new Observer<ChatRoomStatusChangeData>() {
        @Override
        public void onEvent(ChatRoomStatusChangeData chatRoomStatusChangeData) {
            if (chatRoomStatusChangeData.status == StatusCode.CONNECTING) {
                AppLog.i("TAG");

            } else if (chatRoomStatusChangeData.status == StatusCode.UNLOGIN) {
                onOnlineStatusChanged(false);

            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINING) {

            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINED) {

                onOnlineStatusChanged(true);
            } else if (chatRoomStatusChangeData.status.wontAutoLogin()) {
            } else if (chatRoomStatusChangeData.status == StatusCode.NET_BROKEN) {
                onOnlineStatusChanged(false);

            }
            AppLog.i("TAG", "Chat Room Online Status:" + chatRoomStatusChangeData.status.name());
        }
    };

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
    protected void showMasterInfoPopuwindow(LiveUserInfoResultBean result) {
        masterInfoPopuwindow = new PopupWindow(this);
        masterInfoLayoutPw = View.inflate(this, R.layout.master_info_layout, null);
        masterInfoCloseIv = (ImageView) masterInfoLayoutPw.findViewById(R.id.master_info_close_iv);
        masterInfoHeadIv = (CircleImageView) masterInfoLayoutPw.findViewById(R.id.master_info_head_iv);
        masterInfoNickTv = (TextView) masterInfoLayoutPw.findViewById(R.id.master_info_nick_tv);
        masterInfoSignature = (TextView) masterInfoLayoutPw.findViewById(R.id.master_info_signature);
        liveAttention = (TextView) masterInfoLayoutPw.findViewById(R.id.live_attention);
        liveFans = (TextView) masterInfoLayoutPw.findViewById(R.id.live_fans);
        liveContribute = (TextView) masterInfoLayoutPw.findViewById(R.id.live_contribute);
        liveMasterHome = (TextView) masterInfoLayoutPw.findViewById(R.id.live_master_home);
        masterInfoPopuwindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        masterInfoPopuwindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        masterInfoPopuwindow.setContentView(masterInfoLayoutPw);
        masterInfoPopuwindow.setFocusable(true);
        masterInfoPopuwindow.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        masterInfoPopuwindow.setBackgroundDrawable(dw);
        masterInfoCloseIv.setOnClickListener(clickListener);
        liveMasterHome.setOnClickListener(clickListener);


        String avatar = result.getAvatar();
        String nickName = result.getNickName();
        int fansNum = result.getFansNum();
        int attentionNum = result.getAttentionNum();

        String description = result.getDescription();
        liveFans.setText(String.valueOf(fansNum));
        liveAttention.setText(String.valueOf(attentionNum));
        if(!TextUtils.isEmpty(description)){
            masterInfoSignature.setText(description);
        }
        DrawableUtils.displayImg(LivePlayerBaseActivity.this, masterInfoHeadIv,avatar);
        masterInfoPopuwindow.showAtLocation(this.findViewById(R.id.live_layout),
                Gravity.CENTER, 0, 0);
        masterInfoNickTv.setText(nickName);
        liveMasterHome.setOnClickListener(clickListener);

    }
    /**************************
     * 断网重连
     ****************************/

    protected void onOnlineStatusChanged(boolean isOnline) {
        if (isOnline) {
            onConnected();
        } else {
            onDisconnected();
        }
    }

    protected abstract void onConnected(); // 网络连上

    protected abstract void onDisconnected(); // 网络断开

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

    /****************************
     * 布局初始化
     **************************/

    private BarrageView barrageView;
    protected void findViews() {

        barrageView = (BarrageView) findViewById(R.id.barrageView_test);
        masterName = (TextView) findViewById(R.id.live_emcee_name);
        maseterHead = (CircleImageView) findViewById(R.id.live_emcee_head);
        touristList = (RecyclerView) findViewById(R.id.live_visitors_list_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LivePlayerBaseActivity.this, LinearLayoutManager.HORIZONTAL, false);
        touristList.setLayoutManager(linearLayoutManager);
        onlineCountText = findView(R.id.live_online_count);
        giftBtn = (ImageButton) findViewById(R.id.gift_btn);
        // 礼物列表
        findGiftLayout();
        // 点赞的爱心布局
        periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);
        View liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.GONE);
        container = new Container(this, roomId, SessionTypeEnum.ChatRoom, this);
        View view = findViewById(getLayoutId());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (messageListPanel == null) {
            messageListPanel = new ChatRoomMsgListPanel(container, view);
        }
        InputConfig inputConfig = new InputConfig();
        inputConfig.isTextAudioSwitchShow = false;
        inputConfig.isMoreFunctionShow = false;
        inputConfig.isEmojiButtonShow = false;
        if (inputPanel == null) {
            inputPanel = new InputPanel(container, view, getActionList(), inputConfig);
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
            public void getBarrageViewCheckStatus(boolean isCheck) {
                barrageSelectorStatus = isCheck;
            }
        });
        //软键盘输入框
        editTextInput = (EditText) findViewById(R.id.editTextMessage);
        inputChar = (ImageView) findViewById(R.id.live_telecast_input_text);

        masterInfoLayout = (LinearLayout) findViewById(R.id.live_master_info_layout);
        masterInfoLayout.setOnClickListener(clickListener);

        //分享
        ImageView shareLiveIng= (ImageView) findViewById(R.id.live_telecast_share);
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
        });
    }

    private void setMemberType(ChatRoomMessage message2) {
        Map<String, Object> ext = new HashMap<>();
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, DemoCache.getAccount());
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            ext.put("type", chatRoomMember.getMemberType().getValue());
            message2.setRemoteExtension(ext);
        }

    }
    // 初始化礼物布局
    protected void findGiftLayout() {
        giftLayout = findView(R.id.gift_layout);
        giftView = findView(R.id.gift_grid_view);
        giftAnimationViewDown = findView(R.id.gift_animation_view);
        giftAnimationViewUp = findView(R.id.gift_animation_view_up);
        giftAnimation = new GiftAnimation(giftAnimationViewDown, giftAnimationViewUp);
    }
    // 更新礼物列表，由子类定义
    protected void updateGiftList(GiftType type) {

    }

    // 进入聊天室
    protected void enterRoom() {

        EnterChatRoomData data = new EnterChatRoomData(roomId);
        data.setRoomId(roomId);
        data.setAvatar(AuthPreferences.getKeyUserAvatar());
        enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
        enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData result) {
                String s = new Gson().toJson(result);
                AppLog.i("TAG","EnterChatRoomResultData:"+s);
                onLoginDone();
                roomInfo = result.getRoomInfo();
                ChatRoomMember member = result.getMember();
                enterroomgetnick = member.getNick();
                String avatar = member.getAvatar();
                member.setRoomId(roomInfo.getRoomId());
                ChatRoomMemberCache.getInstance().saveMyMember(member);
                DrawableUtils.displayImg(LivePlayerBaseActivity.this, maseterHead,avatar);
                masterName.setText(enterroomgetnick);
                updateUI(enterroomgetnick);
            }

            @Override
            public void onFailed(int code) {
                AppLog.i("TAG","进入聊天室失败："+code);
                onLoginDone();

            }

            @Override
            public void onException(Throwable exception) {
                onLoginDone();
          //      Toast.makeText(LivePlayerBaseActivity.this, "enter chat room exception, e=" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //查询在线成员列表
        fetchData();

    }

    private boolean isNormalEmpty = false; // 固定成员是否拉取完
    private long updateTime = 0; // 非游客的updateTime
    private long enterTime = 0;
    private boolean isFirstLoadding = true;
    private List<ChatRoomMember> items = null;
    private List<ChatRoomMember> allItems = new ArrayList<>();

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
        allItems .clear();
        memberCache.clear();
        isNormalEmpty = false;
    }

    private static final int LIMIT = 100;
    private Map<String, ChatRoomMember> memberCache = new ConcurrentHashMap<>();
    private void getMembers(final MemberQueryType memberQueryType, final long time, int limit) {
        ChatRoomMemberCache.getInstance().fetchRoomMembers(roomId, memberQueryType, time, (LIMIT - limit), new SimpleCallback<List<ChatRoomMember>>() {
            @Override
            public void onResult(boolean success, List<ChatRoomMember> result) {
                if (success) {
                    AppLog.i(TAG, "游客列表：" + result.size());
                    addMembers(result);
                    if (memberQueryType == MemberQueryType.ONLINE_NORMAL && result.size() < LIMIT) {
                        isNormalEmpty = true; // 固定成员已经拉完
                        getMembers(MemberQueryType.GUEST, enterTime, result.size());
                    }
                }
            }
        });
    }
    boolean isFirstClick=true;//防止快速点击，出现两个popuwindow
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
                Map<String, Object> extension = member.getExtension();
                inputPanel.hideInputMethod();
                if(isFirstClick){
                    isFirstClick=false;
                    if(!isMasterAccount){
                        if(extension!=null){
                            userIdItem = (String) extension.get("userId");
                            contentLoader.getLiveUserInfo(userIdItem);
                        }else {
                            String nick = member.getNick();
                            String avatar = member.getAvatar();
                            String s = new Gson().toJson(member);
                            AppLog.i("TAG","showTouristInfo:"+s);
                            LiveUserInfoResultBean result=new LiveUserInfoResultBean();
                            result.setAvatar(avatar);
                            result.setNickName(nick);
                            showMasterInfoPopuwindow(result);
                        }
                    }else {
                        contentLoader.getLiveUserInfo(userId);
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

    protected int maxOnLineCount=0;
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
                        if(onlineUserCount>maxOnLineCount){//计算最大在线人数
                            maxOnLineCount=onlineUserCount;
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        LogUtil.d(TAG, "fetch room info failed:" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        LogUtil.d(TAG, "fetch room info exception:" + exception);
                    }
                });
            }
        }, FETCH_ONLINE_PEOPLE_COUNTS_DELTA, FETCH_ONLINE_PEOPLE_COUNTS_DELTA);
    }

    /**************************
     * Module proxy
     ***************************/

    @Override
    public boolean sendMessage(IMMessage msg) {
        ChatRoomMessage message = (ChatRoomMessage) msg;
        String fromNick = msg.getFromNick();
        String content = msg.getContent();
        if(barrageSelectorStatus){
            barrageView.init(new BarrageConfig());
            if(content!=null){
                barrageView.addTextBarrage(fromNick+":"+content);
            }
        }

        Map<String, Object> ext = new HashMap<>();
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, DemoCache.getAccount());
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            ext.put("type", chatRoomMember.getMemberType().getValue());

            message.setRemoteExtension(ext);
        }
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {

                        AppLog.i("TAG","消息发送成功");
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
        messageListPanel.onMsgSend(message);
        return true;
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