package com.lalocal.lalocal.live.entertainment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.log.LogUtil;
import com.lalocal.lalocal.live.entertainment.constant.CustomDialogStyle;
import com.lalocal.lalocal.live.entertainment.constant.GiftConstant;
import com.lalocal.lalocal.live.entertainment.constant.GiftType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.GiftCache;
import com.lalocal.lalocal.live.entertainment.model.Gift;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListResp;
import com.lalocal.lalocal.live.entertainment.module.CustomRemoteExtensionStyle;
import com.lalocal.lalocal.live.entertainment.ui.CreateLiveRoomPopuwindow;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomLiveUserInfoDialog;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.thirdparty.live.LivePlayer;
import com.lalocal.lalocal.live.thirdparty.live.LiveSurfaceView;
import com.lalocal.lalocal.model.CloseLiveBean;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.model.ImgTokenResult;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.ReversalBitmapUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.MemberOption;
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
    private final static String CHANNELID = "CHANNELID";
    private final static String AVATAR = "AVATAR";
    public static final String LIVE_USER_ID = "LIVE_USER_ID";
    public static final String ANNOUCEMENT = "ANNOUCEMENT";
    public static final String CREATE_NICK_NAME = "CREATE_NICK_NAME";
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;
    public static final String  IS_SECOND="IS_SECOND";

    private int screenHeight;
    private int keyHeight;

    private long lastClickTime = 0;  // 发送礼物频率控制使用
    private String userOnLineCountParameter;
    protected String channelId;
    private String createNickName;
    public String annoucement;//公告
    private String roomName;//直播室名字


    boolean isshowPopu = false;
    boolean isEnterRoom = false;
    private boolean disconnected = false; // 是否断网（断网重连用）
    private boolean isStartLive = false; // 是否开始直播推流
    boolean isClickStartLiveBtn = false; //设置直播title的popuwindow
    boolean isCancelCreama = true;//监听屏幕焦点改变，控制软键盘显示与隐藏

    // view
    private LiveSurfaceView liveView;
    private View backBtn;

    private TextView noGiftText;

    private View liveSettingLayout;//直播间底部设置栏
    protected View viewById;

    private TextView onlineCount;//在线人数
    private TextView liveOverBack;
    private TextView aucienceCount;

    private TextView overTime;
    private TextView inputLiveRoom;

    private ImageView overLiveShareFriends;
    private ImageView overLiveShareWeibo;
    private ImageView liveQuit;
    private ImageView quit;
    private ImageView playLike;
    private ImageView overLiveShareWeixin;
    private ImageView switchBtn;
    private ImageView clickPraise;//点赞
    private LivePlayer livePlayer;


    private LinearLayout modelLayout;//用户信息layout
    private LinearLayout keyboardLayout;//自定义键盘输入

    private ViewGroup liveFinishLayout;
    // data
    private List<Gift> giftList = new ArrayList<>(); // 礼物列表数据
    private ContentLoader contentLoader;
    private SpecialShareVOBean shareVO;
    private UploadManager uploadManager;
    private Timer timer;

    private String isSecond;

    boolean isShowPopuTo=false;
    private String roomId;
    private List<LiveManagerListBean> managerList;
    private CreateLiveRoomPopuwindow createLiveRoomPopuwindow;

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && !isshowPopu && isCancelCreama&&CommonUtil.REMIND_BACK!=1) {
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

    public static void start(Context context, String roomId, String url,
                             String channelId, String avatag, String liveUserId, SpecialShareVOBean shareVO, String annoucement, String createNickName,String isSecond) {
        Intent intent = new Intent();
        intent.setClass(context, LiveActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(CHANNELID, channelId);
        intent.putExtra(AVATAR, avatag);
        intent.putExtra(ANNOUCEMENT, annoucement);
        intent.putExtra(IS_SECOND,isSecond);
        intent.putExtra(CREATE_NICK_NAME, createNickName);
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

        if(CommonUtil.REMIND_BACK==1){
            contentLoader.alterLive(createNickName, channelId, null, null, null, null);
            CommonUtil.REMIND_BACK=0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewById.addOnLayoutChangeListener(this);
        AppLog.i("TAG", "LiveActivity:onResume");
        // 恢复直播
        if (livePlayer != null) {
            livePlayer.onActivityResume();
        }
    }

    protected void onPause() {
        super.onPause();
        AppLog.i("TAG", "LiveActivity:onPause");
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
        channelId = getIntent().getStringExtra(CHANNELID);
        createNickName = getIntent().getStringExtra(CREATE_NICK_NAME);
        shareVO = getIntent().getParcelableExtra("shareVO");
        String s = new Gson().toJson(shareVO);
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
        isSecond = getIntent().getStringExtra(IS_SECOND);
        roomId = getIntent().getStringExtra(EXTRA_ROOM_ID);
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
        AppLog.i("TAG", "LiveActivity:onStart");
    }

    @Override
    public void onStop() {
        super.onStop();

        AppLog.i("TAG", "LiveActivity:onStop");
    }

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }
    boolean isCloseLive=false;//结束直播状态

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
                        isShowPopuTo=true;
                        sendLiveStatusMessgae(CustomRemoteExtensionStyle.START_LIVE_STYLE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(liveSettingLayout.getWindowToken(), 0);
                        isEnterRoom = true;
                        if(createLiveRoomPopuwindow!=null){
                            createLiveRoomPopuwindow.dismiss();
                        }
                        enterRoom();
                        registerObservers(true);
                        modelLayout.setVisibility(View.VISIBLE);
                        liveSettingLayout.setVisibility(View.VISIBLE);
                        liveSettingLayout.setClickable(true);
                        livePlayer.screenShot();

                        contentLoader.getImgToken();//获取上传图片的token
                        userOnLineCountParameter = channelId + "/onlineUsers";
                        //上传在线人数
                        contentLoader.getUserOnLine(userOnLineCountParameter, onlineCounts);

                        if (timer == null) {
                            timer = new Timer(true);
                        }
                        timer.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                if(!isCloseLive){
                                    AppLog.i("TAG", "上传在线人数：" + onlineCounts);
                                    contentLoader.getUserOnLine(userOnLineCountParameter, onlineCounts);
                                }
                            }
                        }, 1000, 5 * 1000);
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
               isCloseLive=true;
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

                            AppLog.i("TAG", "图片翻转失败");
                            return;
                        }
                        //上传图片
                        uploadManager.put(reversal, filename, token, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                contentLoader.alterLiveCover(roomName, channelId, key, null, null, null);
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

        @Override
        public void onLiveManager(LiveManagerBean liveManagerBean) {
            super.onLiveManager(liveManagerBean);
        }




    }



    private void showCreateLiveRoomPopuwindow() {

        createLiveRoomPopuwindow = new CreateLiveRoomPopuwindow(this);
        createLiveRoomPopuwindow.showCreateLiveRoomPopuwindow();
        createLiveRoomPopuwindow.showAtLocation(this.findViewById(R.id.live_layout),
                Gravity.CENTER, 0, 0);
        createLiveRoomPopuwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isShowPopuTo){
                    releaseResource();//释放资源
                    contentLoader.cancelLiveRoom(channelId);
                    finish();
                }
            }
        });

        createLiveRoomPopuwindow.setOnSendClickListener(new CreateLiveRoomPopuwindow.OnCreateLiveListener() {
            @Override
            public void startLiveBtn(String rooName, int isShareSelector) {
                liveSettingLayout.setVisibility(View.GONE);
                if (TextUtils.isEmpty(rooName)) {
                    roomName = createNickName;
                }else{
                    roomName=rooName;
                }
                //TODO 进入直播间
                isClickStartLiveBtn = true;

                if (isShareSelector == 0) {
                    liveShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                } else if (isShareSelector == 1) {
                    liveShare(SHARE_MEDIA.SINA);
                } else if (isShareSelector == 2) {
                    liveShare(SHARE_MEDIA.WEIXIN);
                }
                if (channelId != null) {
                    contentLoader.alterLive(roomName, channelId, null, null, null, null);
                } else {

                    finish();
                }
            }

            @Override
            public void closeLiveBtn() {
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
                contentLoader.cancelLiveRoom(channelId);
                createLiveRoomPopuwindow.dismiss();
            }
        });
    };

    // 退出聊天室
    private void logoutChatRoom() {
        CustomChatDialog customDialog = new CustomChatDialog(getActivity());
        customDialog.setContent(getString(R.string.finish_confirm));
        customDialog.setCancelable(false);
        customDialog.setCancelBtn("结束直播", new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                endLive();
            }
        });
        customDialog.setSurceBtn("继续直播", null);
        customDialog.show();
    }

    private void endLive() {
        if (livePlayer != null) {
            livePlayer.resetLive();
        }
        if(timer!=null){
            timer.cancel();
        }
        contentLoader.cancelLiveRoom(channelId);
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        if (isClickStartLiveBtn) {
            inputPanel.collapse(true);// 收起软键盘
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

                }
            });
        } else {
            finish();
        }


    }

    private void sendLiveStatusMessgae(String status) {

        ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(roomId, "结束或开始直播");
        Map<String, Object> ext = new HashMap<>();
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, AuthPreferences.getUserAccount());
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            ext.put("type", chatRoomMember.getMemberType().getValue());
            ext.put("style", status);
            message.setRemoteExtension(ext);
        }
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false);

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
                    isCancelCreama = false;
                    if (isStartLive && livePlayer != null) {
                        livePlayer.tryStop();
                        livePlayer.resetLive();
                    }
                    CommonUtil.RESULT_DIALOG = 2;
                    finish();
                }

                @Override
                public void getAudioOpenStatus() {
                    if (isFirstAudio) {
                        isFirstAudio = false;
                        isCancelCreama = false;
                        if (isStartLive && livePlayer != null) {
                            livePlayer.tryStop();
                            livePlayer.resetLive();
                        }
                        CommonUtil.RESULT_DIALOG = 3;
                        finish();
                    }

                }
            });
        }


    }
    boolean adimnStatus=false;
    boolean isMuteds=false;
    int status=-1;

    @Override
    protected void showMasterInfoPopuwindow(LiveUserInfoResultBean result, final boolean isMuted, final String meberAccount, int meberType,int id,int managerId) {
        isMuteds=isMuted;
        Object statusa = result.getAttentionVO().getStatus();

        if (statusa!=null){
            double parseDouble = Double.parseDouble(String.valueOf(statusa));
            status = (int) parseDouble;

        }
        CustomDialogStyle.CUSTOM_DIALOG_LIVE=1;
        if(meberType==2){
            adimnStatus=true;
        }else{
            adimnStatus=false;
        }
        CustomLiveUserInfoDialog customLiveUserInfoDialog = new CustomLiveUserInfoDialog(LiveActivity.this, result);
        customLiveUserInfoDialog.setCancelable(false);
        customLiveUserInfoDialog.setCancelBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
            @Override
            public void onCustomLiveUserInfoDialogListener(String id,TextView textView) {

            }
        });
        if(CustomDialogStyle.CUSTOM_DIALOG_STYLE==1){

            customLiveUserInfoDialog.setSurceBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id,TextView textView) {
                    Intent intent = new Intent(LiveActivity.this, LiveHomePageActivity.class);
                    intent.putExtra("userId", String.valueOf(id));
                    startActivity(intent);
                }
            });
        }else if(CustomDialogStyle.CUSTOM_DIALOG_STYLE==2){
            customLiveUserInfoDialog.setAttention(status==0?"关注":"正在关注", new CustomLiveUserInfoDialog.CustomLiveFansOrAttentionListener() {
                int fansCounts=-2;
                @Override
                public void onCustomLiveFansOrAttentionListener(String id,TextView fansView,TextView attentionView,int fansCount,int attentionCount,TextView textView) {
                    if(fansCounts==-2){
                        fansCounts=fansCount;
                    }
                    if(status==0){
                        textView.setText("正在关注");
                        textView.setAlpha(0.4f);
                        ++fansCounts;
                        fansView.setText(String.valueOf(fansCounts));
                        contentLoader.getAddAttention(id);
                        status=1;
                    }else {
                        textView.setText("关注");
                        textView.setAlpha(1);
                        --fansCounts;
                        fansView.setText(String.valueOf(fansCounts));
                        contentLoader.getCancelAttention(id);
                        status=0;
                    }
                    //
                }
            });
            customLiveUserInfoDialog.setReport(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id,TextView textView) {

                }
            });
            customLiveUserInfoDialog.setBanBtn(isMuteds==true?"解除禁言":"禁言", new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, final TextView textView) {
                    contentLoader.checkUserIdentity(channelId,String.valueOf(id));
                    contentLoader.setCallBack(new ICallBack() {
                        @Override
                        public void onCheckManager(LiveManagerBean liveManagerBean) {
                            super.onCheckManager(liveManagerBean);
                            if(liveManagerBean.getResult()!=0){
                                Toast.makeText(LiveActivity.this,"对方为管理员!",Toast.LENGTH_SHORT).show();
                            }else {
                                NIMClient.getService(ChatRoomService.class).markChatRoomMutedList(!isMuteds,new MemberOption(roomId,meberAccount)).setCallback(new RequestCallback<ChatRoomMember>() {
                                    @Override
                                    public void onSuccess(ChatRoomMember chatRoomMember) {
                                        CustomDialogStyle.IS_MUTED= chatRoomMember.isMuted();
                                        if(CustomDialogStyle.IS_MUTED){
                                            textView.setText("解除禁言");
                                            isMuteds=true;
                                        }else{
                                            textView.setText("禁言");
                                            isMuteds=false;
                                        }
                                    }
                                    @Override
                                    public void onFailed(int i) {
                                    }
                                    @Override
                                    public void onException(Throwable throwable) {
                                    }
                                });
                            }
                        }
                    });

                }
            });
            customLiveUserInfoDialog.setManagerBtn(adimnStatus==true?"取消管理员":"设为管理员", new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(final String id, final TextView textView) {
                    contentLoader.getLiveManagerList(channelId);
                    contentLoader.setCallBack(new ICallBack() {
                        @Override
                        public void onManagerList(LiveManagerListResp liveManagerListResp) {
                            super.onManagerList(liveManagerListResp);
                            managerList = liveManagerListResp.getResult();
                            if(managerList!=null&&managerList.size()>=3&&!adimnStatus){
                                Toast.makeText(LiveActivity.this,"设置失败，每个直播间最多设置3个管理员",Toast.LENGTH_SHORT).show();
                            } else {
                                NIMClient.getService(ChatRoomService.class).markChatRoomManager(!adimnStatus, new MemberOption(roomId,meberAccount)).setCallback(new RequestCallback<ChatRoomMember>() {
                                    @Override
                                    public void onSuccess(ChatRoomMember chatRoomMember) {
                                        int value = chatRoomMember.getMemberType().getValue();
                                        if(value==2){
                                            textView.setText("取消管理员");
                                            textView.setAlpha(0.4f);
                                            adimnStatus=true;
                                            contentLoader.liveAccreditManager(channelId,String.valueOf(id));

                                        }else {
                                            textView.setText("设为管理员");
                                            textView.setAlpha(1);
                                            adimnStatus=false;
                                            contentLoader.checkUserIdentity(channelId,String.valueOf(id));
                                        }

                                    }
                                    @Override
                                    public void onFailed(int i) {
                                    }
                                    @Override
                                    public void onException(Throwable throwable) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCheckManager(LiveManagerBean liveManagerBean) {
                            super.onCheckManager(liveManagerBean);
                            int result = liveManagerBean.getResult();
                            AppLog.i("TAG","查看是否为管理员："+result);
                            if(result!=0){
                                contentLoader.cancelManagerAccredit(String.valueOf(result));
                            }
                        }
                    });
                }
            });
        }



        customLiveUserInfoDialog.show();
        CustomDialogStyle.CUSTOM_DIALOG_STYLE=0;
        CustomDialogStyle.CUSTOM_DIALOG_LIVE=0;

    }

    boolean isFirstAudio = true;

    protected void enterRoom() {
        super.enterRoom();
    }

    protected void registerObservers(boolean register) {
        super.registerObservers(register);
    }

    @Override
    protected void checkNetInfo(String netType, int reminder) {
        if ("rests".equals(netType) && reminder == 0) {
            CustomChatDialog customChatDialog = new CustomChatDialog(LiveActivity.this);
            customChatDialog.setTitle("提示");
            customChatDialog.setContent("当前网络为移动网络，是否继续直播？");
            customChatDialog.setCancelable(false);
            customChatDialog.setCancelBtn("继续直播", null);
            customChatDialog.setSurceBtn("结束直播", new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    endLive();
                }
            });
            customChatDialog.show();
        }
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

    boolean isFirstFirendsClick=true;
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
                        livePlayer.switchCamera();//切换摄像头

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
        if (isStartLive && livePlayer != null) {
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
    //释放直播资源
    public void releaseResource(){
        if (livePlayer != null) {
            livePlayer.tryStop();
        }
        if (isStartLive) {
            if (livePlayer != null) {
                livePlayer.resetLive();
            }
        }
    }

}