package com.lalocal.lalocal.view.liveroomview.entertainment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.CloseLiveBean;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.model.ImgTokenResult;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.ReversalBitmapUtil;
import com.lalocal.lalocal.view.liveroomview.base.util.log.LogUtil;
import com.lalocal.lalocal.view.liveroomview.entertainment.constant.GiftConstant;
import com.lalocal.lalocal.view.liveroomview.entertainment.constant.GiftType;
import com.lalocal.lalocal.view.liveroomview.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.view.liveroomview.entertainment.helper.GiftCache;
import com.lalocal.lalocal.view.liveroomview.entertainment.model.Gift;
import com.lalocal.lalocal.view.liveroomview.entertainment.module.CustomRemoteExtensionStyle;
import com.lalocal.lalocal.view.liveroomview.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.view.liveroomview.im.config.AuthPreferences;
import com.lalocal.lalocal.view.liveroomview.thirdparty.live.LivePlayer;
import com.lalocal.lalocal.view.liveroomview.thirdparty.live.LiveSurfaceView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class LiveActivity extends LivePlayerBaseActivity implements LivePlayer.ActivityProxy, View.OnLayoutChangeListener {
    private static final String TAG = "LiveActivity";
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    private final static String USER_ID = "USER_ID";
    private final static String AVATAR = "AVATAR";
    public static final String LIVE_USER_ID = "LIVE_USER_ID";
    public static final String ANNOUCEMENT = "ANNOUCEMENT";
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;



    // view
    private LiveSurfaceView liveView;
    private View backBtn;
    private ImageView switchBtn;
    private TextView noGiftText;
    private ViewGroup liveFinishLayout;
    // state
    private boolean disconnected = false; // 是否断网（断网重连用）
    private boolean isStartLive = false; // 是否开始直播推流
    // 推流参数
    private LivePlayer livePlayer;
    // data
    private List<Gift> giftList = new ArrayList<>(); // 礼物列表数据
    private ContentLoader contentLoader;
    private LinearLayout modelLayout;//用户信息layout
    private LinearLayout keyboardLayout;//自定义键盘输入
    private View liveSettingLayout;//直播间底部设置栏
    private RelativeLayout startLiveLayout;
    private ImageView clickPraise;//点赞
    private TextView onlineCount;//在线人数
    private int screenHeight;
    private int keyHeight;
    protected View viewById;
    private TextView inputLiveRoom;
    private ImageView quit;
    private ImageView playLike;

    private TextView liveOverBack;
    private TextView aucienceCount;
    private TextView overTime;
    private UploadManager uploadManager;
    private String userId;
    boolean isshowPopu = false;
    boolean isEnterRoom = false;
    private PopupWindow createLivePw;
    private ImageView cancelCreateRoom;
    private TextView inputRoom;
    private EditText liveRoomName;
    private String avatag;
    private ImageView shareFriends;
    private ImageView shareWeibo;
    private ImageView shareWeixin;
    private SpecialShareVOBean shareVO;
    private ImageView overLiveShareFriends;
    private ImageView overLiveShareWeibo;
    private ImageView overLiveShareWeixin;
    private ScrollView startLiveScrollview;
    private TextView textCount;
    private Timer timer;
    private String userOnLineCountParameter;
    private ImageView liveQuit;
    public String annoucement;//公告
    private String roomName;//直播室名字
    int isShareSelector = -1;//创建直播分享
    private long lastClickTime = 0;  // 发送礼物频率控制使用

    @Override
    protected int getActivityLayout() {
        return R.layout.live_player_activity;
    }

    @Override
    protected int getLayoutId() {
        return R.id.live_layout;
    }

    @Override
    public Activity getActivity() {
        return LiveActivity.this;
    }



    //监听屏幕焦点改变，控制软键盘显示与隐藏
    boolean isCancelCreama=true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !isshowPopu&&isCancelCreama) {
            isshowPopu = true;
            showCreateLiveRoomPopuwindow();
        }
    }

    //判断软键盘显示与隐藏
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
      if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            if (inputLiveRoom != null && inputLiveRoom.getVisibility() == View.VISIBLE) {
                liveSettingLayout.setVisibility(View.GONE);
                liveSettingLayout.setClickable(false);
            }
            if (keyboardLayout != null && isEnterRoom) {
                keyboardLayout.setAlpha(0);
                keyboardLayout.setClickable(false);
                liveSettingLayout.setVisibility(View.VISIBLE);

                liveSettingLayout.setClickable(true);
            }
        }
    }

    public static void start(Context context, String roomId, String url, String userId, String avatag, String liveUserId, SpecialShareVOBean shareVO, String annoucement) {
        Intent intent = new Intent();
        intent.setClass(context, LiveActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(USER_ID, userId);
        intent.putExtra(AVATAR, avatag);
        intent.putExtra(ANNOUCEMENT, annoucement);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("shareVO", shareVO);
        intent.putExtras(mBundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(LIVE_USER_ID, liveUserId);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        viewById = findViewById(R.id.live_layout);
        uploadManager = new UploadManager();//七牛云api
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        gainIntent();
        loadGift();
        setListener();

    }



    @Override
    protected void onResume() {
        super.onResume();
        viewById.addOnLayoutChangeListener(this);

        AppLog.i("TAG","LiveActivity:onResume");
        // 恢复直播
        if (livePlayer != null) {
            livePlayer.onActivityResume();
        }
    }

    protected void onPause() {
        super.onPause();
        AppLog.i("TAG","LiveActivity:onPause");
        // 暂停直播
        if (livePlayer != null) {
            livePlayer.onActivityPause();
        }
    }

    @Override
    protected void onDestroy() {

        // 释放资源
        if (livePlayer != null) {
            livePlayer.resetLive();
        }
        giftList.clear();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (livePlayer != null) {
            livePlayer.tryStop();
        }
        if (isStartLive) {
            logoutChatRoom();
        } else {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }
    }

    private void gainIntent() {
        userId = getIntent().getStringExtra(USER_ID);
        shareVO = getIntent().getParcelableExtra("shareVO");
        String s = new Gson().toJson(shareVO);
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
        AppLog.i("TAG","开启直播分享内容:"+s);
    }

    protected void findViews() {
        super.findViews();
        liveView = (LiveSurfaceView) findViewById(R.id.live_view);
        backBtn = findView(R.id.BackBtn);
        modelLayout = (LinearLayout) findViewById(R.id.live_telecast_model_layout);
        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);
        switchBtn = (ImageView) findViewById(R.id.live_telecast_setting);
        noGiftText = findView(R.id.no_gift_tip);
        controlLayout = findView(R.id.control_layout);
        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveQuit = (ImageView) findViewById(R.id.live_quit);

        onlineCount = (TextView) findViewById(R.id.live_online_count);
        backBtn.setVisibility(View.GONE);

        noGiftText.setVisibility(View.GONE);

        modelLayout.setVisibility(View.GONE);
        keyboardLayout.setAlpha(0);
        keyboardLayout.setClickable(false);
        liveSettingLayout.setClickable(true);
        // 直播结束
        liveFinishLayout = findView(R.id.live_finish_layout);
        aucienceCount = (TextView) findViewById(R.id.live_over_audience_count);
        overTime = (TextView) findViewById(R.id.live_over_time_tv);

        quit = (ImageView) findViewById(R.id.live_telecast_quit);
        playLike = (ImageView) findViewById(R.id.live_telecast_like);
        playLike.setVisibility(View.INVISIBLE);
        //结束直播
        liveOverBack = (TextView) findViewById(R.id.live_over_back_home);
        liveOverBack.setOnClickListener(buttonClickListener);
        overLiveShareFriends = (ImageView) findViewById(R.id.live_over_share_friends);
        overLiveShareWeibo = (ImageView) findViewById(R.id.live_over_share_weibo);
        overLiveShareWeixin = (ImageView) findViewById(R.id.live_over_share_weixin);
        overLiveShareFriends.setOnClickListener(buttonClickListener);
        overLiveShareWeibo.setOnClickListener(buttonClickListener);
        overLiveShareWeixin.setOnClickListener(buttonClickListener);

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

    private void setListener() {
        backBtn.setOnClickListener(buttonClickListener);
        switchBtn.setOnClickListener(buttonClickListener);
     //   giftLayout.setOnClickListener(buttonClickListener);
        inputChar.setOnClickListener(buttonClickListener);
        quit.setOnClickListener(buttonClickListener);
        playLike.setOnClickListener(buttonClickListener);
        liveQuit.setOnClickListener(buttonClickListener);
    }
    @Override
    public void onStart() {
        super.onStart();
        AppLog.i("TAG","LiveActivity:onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        AppLog.i("TAG","LiveActivity:onStop");
    }

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }

    public class MyCallBack extends ICallBack {
        @Override
        public void onAlterLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {//修改直播间
            super.onAlterLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                avatar = createLiveRoomDataResp.getResult().getUser().getAvatar();
                LiveRowsBean result = createLiveRoomDataResp.getResult();
                livePlayer.initLiveStream();

                livePlayer.setOnStartLiveListener(new LivePlayer.OnStartLiveListener() {//监听直播开始，开启截屏
                    @Override
                    public void getStartLiveStatus(boolean onStart) {

                        sendLiveStatusMessgae(CustomRemoteExtensionStyle.START_LIVE_STYLE);
                        isEnterRoom = true;
                        createLivePw.dismiss();
                        enterRoom();
                        registerObservers(true);
                        modelLayout.setVisibility(View.VISIBLE);
                        liveSettingLayout.setVisibility(View.VISIBLE);
                        liveSettingLayout.setClickable(true);
                        livePlayer.screenShot();
                        AppLog.i("TAG", "getStartLiveStatus");
                        contentLoader.getImgToken();//获取上传图片的token
                        userOnLineCountParameter = userId + "/onlineUsers";
                        //上传在线人数
                        contentLoader.getUserOnLine(userOnLineCountParameter, onlineCounts);
                        if (timer == null) {
                            timer = new Timer();
                        }
                        timer.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                contentLoader.getUserOnLine(userOnLineCountParameter, onlineCounts);
                            }
                        }, 1000, 5 * 60 * 6000);
                    }

                    @Override
                    public void screenShot(byte[] bytes) {
                    }
                });

            }
        }


        @Override
        public void onCloseLive(CloseLiveBean closeLiveBean) {
            super.onCloseLive(closeLiveBean);
            if (closeLiveBean.getReturnCode() == 0) {
                AppLog.i("TAG", "关闭直播LiveActivity");
            }
        }


        @Override
        public void onImgToken(ImgTokenBean imgTokenBean) {
            super.onImgToken(imgTokenBean);
            if (imgTokenBean.getReturnCode() == 0) {
                ImgTokenResult imgToken = imgTokenBean.getResult();
                final String filename = imgToken.getFilename();
                final String token = imgToken.getToken();
                livePlayer.setOnStartLiveListener(new LivePlayer.OnStartLiveListener() {
                    @Override
                    public void getStartLiveStatus(boolean onStart) {
                    }

                    @Override
                    public void screenShot(byte[] bytes) {
                        //翻转图片
                        byte[] reversal = ReversalBitmapUtil.reversal(bytes);
                        if (reversal == null) {
                            reversal = bytes;
                            AppLog.i("TAG", "图片翻转失败");
                        }
                        //上传图片
                        uploadManager.put(reversal, filename, token, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                contentLoader.alterLiveCover(roomName, userId, key, null, null, null);
                                contentLoader.getImgToken();
                            }
                        }, null);
                    }
                });



            }
        }

        @Override
        public void onAlterLiveCover(CreateLiveRoomDataResp createLiveRoomDataResp) {
            super.onAlterLiveCover(createLiveRoomDataResp);

        }
    }

    //设置直播title的popuwindow
    boolean isClickStartLiveBtn = false;

    private void showCreateLiveRoomPopuwindow() {
        createLivePw = new PopupWindow(this);
        View view = View.inflate(this, R.layout.live_create_room_pop_layout, null);
        cancelCreateRoom = (ImageView) view.findViewById(R.id.live_create_room_close_iv);
        inputLiveRoom = (TextView) view.findViewById(R.id.input_start_live);
        startLiveScrollview = (ScrollView) view.findViewById(R.id.start_live_bottom_layout);

        //分享
        shareFriends = (ImageView) view.findViewById(R.id.live_create_share_friends);
        shareWeibo = (ImageView) view.findViewById(R.id.live_create_share_weibo);
        shareWeixin = (ImageView) view.findViewById(R.id.live_create_share_weixin);
        shareFriends.setSelected(true);
        isShareSelector=0;
        shareFriends.setOnClickListener(buttonClickListener);
        shareWeibo.setOnClickListener(buttonClickListener);
        shareWeixin.setOnClickListener(buttonClickListener);
        inputLiveRoom.setOnClickListener(buttonClickListener);



        //自动调用软键盘
        liveRoomName = (EditText) view.findViewById(R.id.live_room_name);
        textCount = (TextView) view.findViewById(R.id.live_text_title_count);
        liveRoomName.setFocusable(true);
        liveRoomName.setFocusableInTouchMode(true);
        liveRoomName.requestFocus();
        liveRoomName.addTextChangedListener(watcher);//edittext字数改变监听

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) liveRoomName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(liveRoomName, 0);
                           }
                       },
                300);
        createLivePw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        createLivePw.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        createLivePw.setContentView(view);
        createLivePw.setFocusable(true);
        createLivePw.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        createLivePw.setBackgroundDrawable(dw);
        createLivePw.showAtLocation(this.findViewById(R.id.live_layout),
                Gravity.CENTER, 0, 0);

        cancelCreateRoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inputLiveRoom.setVisibility(View.GONE);
                if (livePlayer != null) {
                    livePlayer.tryStop();
                }
                if (isStartLive) {
                    if (livePlayer != null) {
                        livePlayer.resetLive();
                    }
                    finish();
                } else {
                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    clearChatRoom();
                }
                contentLoader.cancelLiveRoom(userId);
                createLivePw.dismiss();
            }
        });

    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String liveRoomNameCount = liveRoomName.getText().toString();
            int length = liveRoomNameCount.length();
            textCount.setText(length + "/20");
        }
    };

    // 退出聊天室
    private void logoutChatRoom() {
        CustomChatDialog customDialog = new CustomChatDialog(getActivity());
        customDialog.setTitle( getString(R.string.finish_confirm));
        customDialog.setCancelable(false);
        customDialog.setCancelBtn("继续直播", null);
        customDialog.setSurceBtn(getString(R.string.confirm), new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                if (livePlayer != null) {
                    livePlayer.resetLive();
                }
                if (isClickStartLiveBtn) {
                    inputPanel.collapse(true);// 收起软键盘
                    contentLoader.cancelLiveRoom(userId);
                    isStartLive = false;
                    livePlayer.setOnQuitLiveListener(new LivePlayer.OnQuitLiveListener() {
                        @Override
                        public void getLiveTime(long liveTime) {
                            drawerLayout.closeDrawer(Gravity.RIGHT);
                            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");//初始化Formatter的转换格式。
                            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                            String hms = formatter.format(liveTime);
                            overTime.setText(hms);
                            aucienceCount.setText(String.valueOf(maxOnLineCount));
                            liveFinishLayout.setVisibility(View.VISIBLE);
                            //发送结束直播的通知。
                            sendLiveStatusMessgae(CustomRemoteExtensionStyle.END_LIVE_STYLE);

                        }
                    });
                } else {
                    finish();
                }
            }
        });
        customDialog.show();

    }

    private void sendLiveStatusMessgae(String status) {

        ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage( roomId,"结束或开始直播");
        Map<String, Object> ext = new HashMap<>();
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, AuthPreferences.getUserAccount());
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            ext.put("type", chatRoomMember.getMemberType().getValue());
            ext.put("style", status);
            message.setRemoteExtension(ext);
        }
        NIMClient.getService(ChatRoomService.class).sendMessage(message,false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AppLog.i("TAG","结束直播消息发送了");
            }

            @Override
            public void onFailed(int i) {
                AppLog.i("TAG","结束直播消息失败了"+i);
            }

            @Override
            public void onException(Throwable throwable) {
                AppLog.i("TAG","结束直播消息了");
            }
        });

    }


    // 清空聊天室缓存
    private void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }

    /*****************************
     * 初始化
     *****************************/
    protected void initParam() {
        // 初始化推流
        livePlayer = new LivePlayer(liveView, url, this);
        if (livePlayer.startStopLive()) {
            isStartLive = true;
            livePlayer.setOnCheckCameraListener(new LivePlayer.OnCheckCameraListener() {
                @Override
                public void getCameraOpenStatus() {
                    isCancelCreama=false;
                    if (isStartLive && livePlayer != null) {
                        livePlayer.tryStop();
                        livePlayer.resetLive();
                    }
                    CommonUtil.RESULT_DIALOG=2;
                    finish();
                }

                @Override
                public void getAudioOpenStatus() {
                    AppLog.i("TAG", "音频权限败！！！！！！！！！！！！！！！！！");
                    if(isFirstAudio){
                        isFirstAudio=false;
                        isCancelCreama=false;
                        if (isStartLive && livePlayer != null) {
                            livePlayer.tryStop();
                            livePlayer.resetLive();
                        }
                        CommonUtil.RESULT_DIALOG=3;
                        finish();
                    }

                }
            });
        }


    }
   boolean  isFirstAudio=true;
    protected void enterRoom() {
        super.enterRoom();
    }

    protected void registerObservers(boolean register) {
        super.registerObservers(register);
    }

/*
    // 初始化礼物布局
    protected void findGiftLayout() {
        super.findGiftLayout();
        adapter = new GiftAdapter(giftList, this);
        giftView.setAdapter(adapter);
    }*/


    // 取出缓存的礼物
    private void loadGift() {
        Map gifts = GiftCache.getInstance().getGift(roomId);
        if (gifts == null) {
            return;
        }
        Iterator<Map.Entry<Integer, Integer>> it = gifts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            int type = entry.getKey();
            int count = entry.getValue();
            giftList.add(new Gift(GiftType.typeOfValue(type), GiftConstant.titles[type], count, GiftConstant.images[type]));
        }
    }

    boolean isSelsector=true;
    OnClickListener buttonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BackBtn:
                    if (isStartLive && livePlayer != null) {
                        livePlayer.tryStop();
                        logoutChatRoom();
                    } else {
                        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                        clearChatRoom();
                    }
                    break;
                case R.id.live_telecast_setting:
                    if (livePlayer != null) {
                        livePlayer.switchCamera();
                        AppLog.i("TAG", "LiveActivity：" + "切换摄像头啦");
                    }
                    break;
                case R.id.gift_btn:
                   // showGiftLayout();
                    break;
                case R.id.gift_layout:
                //    giftLayout.setVisibility(View.GONE);
                    break;
                case R.id.live_telecast_input_text:

                    keyboardLayout.setAlpha(1.0f);
                    keyboardLayout.setClickable(true);
                    liveSettingLayout.setVisibility(View.GONE);
                    liveSettingLayout.setClickable(false);
                    inputPanel.switchToTextLayout(true);

                    break;
                case R.id.input_start_live:
                    roomName = liveRoomName.getText().toString().trim();
                    liveSettingLayout.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(roomName)) {
                        //TODO 进入直播间
                        isClickStartLiveBtn = true;
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(startLiveScrollview.getWindowToken(), 0);
                        if (isShareSelector == 0) {
                            liveShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                        } else if (isShareSelector == 1) {
                            liveShare(SHARE_MEDIA.SINA);
                        } else if (isShareSelector == 2) {
                            liveShare(SHARE_MEDIA.WEIXIN);
                        }

                        if(userId!=null){
                            contentLoader.alterLive(roomName, userId, null, null, null, null);
                        }else {
                            Toast.makeText(LiveActivity.this,"请重新创建！",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    break;
                case R.id.live_telecast_like:
                   // periscopeLayout.addHeart();
                    // sendLike();
                    break;
                case R.id.live_telecast_quit:
                    finishLive();
                    break;
                case R.id.live_quit:
                    finishLive();
                    break;
                case R.id.live_over_back_home:
                    finish();
                    break;
                case R.id.live_create_share_friends:

                    if(isSelsector){
                        isShareSelector=-1;
                        isSelsector=false;
                        shareFriends.setSelected(false);
                    }else {
                        isSelsector=true;
                        shareFriends.setSelected(true);
                        isShareSelector=0;
                    }

                    shareWeibo.setSelected(false);
                    shareWeixin.setSelected(false);
                    break;
                case R.id.live_create_share_weibo:
                    shareWeibo.setSelected(true);
                    shareFriends.setSelected(false);
                    shareWeixin.setSelected(false);
                    isShareSelector = 1;
                    break;
                case R.id.live_create_share_weixin:
                    shareFriends.setSelected(false);
                    shareWeibo.setSelected(false);
                    shareWeixin.setSelected(true);
                    isShareSelector = 2;
                    break;
                case R.id.live_over_share_friends:
                    overLiveShareFriends.setSelected(true);
                    overLiveShareWeibo.setSelected(false);
                    overLiveShareWeixin.setSelected(false);
                    liveShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;
                case R.id.live_over_share_weibo:
                    overLiveShareFriends.setSelected(false);
                    overLiveShareWeibo.setSelected(true);
                    overLiveShareWeixin.setSelected(false);
                    liveShare(SHARE_MEDIA.SINA);
                    break;
                case R.id.live_over_share_weixin:
                    overLiveShareFriends.setSelected(false);
                    overLiveShareWeibo.setSelected(false);
                    overLiveShareWeixin.setSelected(true);
                    liveShare(SHARE_MEDIA.WEIXIN);
                    break;
            }
        }
    };
    //分享
    private void liveShare(SHARE_MEDIA share_media) {
        ShareAction sp = new ShareAction(this);
        sp.setPlatform(share_media);
        sp.withTitle(shareVO.getTitle());
        sp.withTargetUrl(shareVO.getUrl());
        sp.withText(shareVO.getTitle());
        UMImage image = new UMImage(this, shareVO.getImg());
        sp.withMedia(image);
        sp.share();
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

    private void finishLive() {
        if (isStartLive&&livePlayer!=null) {
            livePlayer.tryStop();
            logoutChatRoom();
        } else {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }
    }

    // 显示礼物布局
    private void showGiftLayout() {
        inputPanel.collapse(true);// 收起软键盘
        // giftLayout.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        if (adapter.getCount() == 0) {
            // 暂无礼物
            noGiftText.setVisibility(View.VISIBLE);
        } else {
            noGiftText.setVisibility(View.GONE);
        }
    }

    protected void updateGiftList(GiftType type) {
        if (!updateGiftCount(type)) {
            giftList.add(new Gift(type, GiftConstant.titles[type.getValue()], 1, GiftConstant.images[type.getValue()]));
        }
        GiftCache.getInstance().saveGift(roomId, type.getValue());
    }

    // 更新收到礼物的数量
    private boolean updateGiftCount(GiftType type) {
        for (Gift gift : giftList) {
            if (type == gift.getGiftType()) {
                gift.setCount(gift.getCount() + 1);
                return true;
            }
        }
        return false;
    }

    /**
     * ********************************** 断网重连处理 **********************************
     */

    // 网络连接成功
    protected void onConnected() {
        if (disconnected == false) {
            return;
        }

        LogUtil.i(TAG, "live on connected");
        if (livePlayer != null) {
            livePlayer.restartLive();
        }

        disconnected = false;
    }

    // 网络断开
    protected void onDisconnected() {
        LogUtil.i(TAG, "live on disconnected");

        if (livePlayer != null) {
            livePlayer.stopLive();
        }
        disconnected = true;
    }


}
