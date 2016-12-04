package com.lalocal.lalocal.live.entertainment.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.util.DialogUtil;
import com.lalocal.lalocal.live.base.util.log.LogUtil;
import com.lalocal.lalocal.live.entertainment.agora.Constant;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.ChallengeDetailsResp;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.ui.CustomChallengeListviewDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomToast;
import com.lalocal.lalocal.live.entertainment.ui.CustomUserInfoDialog;
import com.lalocal.lalocal.live.entertainment.ui.LiveingSharePopuwindow;
import com.lalocal.lalocal.live.entertainment.ui.MasterMoreSettingPopuWindow;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.live.thirdparty.live.LivePlayer;
import com.lalocal.lalocal.model.CloseLiveBean;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.model.ImgTokenResult;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CheckWeixinAndWeibo;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.SPCUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qiniu.android.storage.UploadManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
    public static final String IS_SECOND = "IS_SECOND";
    public static final String CNAME = "CNAME";
    private int screenHeight;
    private int keyHeight;
    public static String liveLocation = "LALOCAL神秘之地";

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

    private View backBtn;


    private View liveSettingLayout;//直播间底部设置栏
    protected View viewById;

    private TextView aucienceCount;
    private TextView overTime;
    private ImageView liveQuit;
    private ImageView switchBtn;
    private ImageView clickPraise;//点赞

    private RelativeLayout modelLayout;//用户信息layout
    private LinearLayout keyboardLayout;//自定义键盘输入

    private ViewGroup liveFinishLayout;

    // data
    private ContentLoader liveContentLoader;
    private SpecialShareVOBean shareVO;
    private UploadManager uploadManager;
    private Timer timer;
    boolean isShowPopuTo = false;
    private String roomId;
    private List<LiveManagerListBean> managerList;

    private String cname;
    private long startTime;
    private long endTime;
    private TextView overMoney;

    private LiveCallBack liveCallBack;
    private BlurImageView blurImageView;
    private ContentLoader userInfoContentLoader;
    private ImageView challengeNewTask;

    // 状态栏的高度
    private int statusBarHeight;
    // 软键盘的高度
    private int keyboardHeight;
    // 软键盘的显示状态
    private boolean isShowKeyboard;

    private static final int GUIDE_PAGE_SIZE = 4;


    boolean isMuteds = false;//没有禁言
    int status = -1;
    boolean isManager = false;
    boolean isFirstAudio = true;
    boolean isFirstFirendsClick = true;

    private int maxOnLineUserCount = 0;
    private String joinChannel;
    private int joinUid;
    private int joinElapsed;
    private RelativeLayout nofitifationLayout;
    private ImageView netWorkHint;

    private RelativeLayout createLiveLayout;
    private ImageView liveCreateRoomCloseIv;
    private TextView inputStartLive;
    private LinearLayout startLiveLayout;
    private ScrollView startLiveBottomLayout;
    private EditText liveRoomName;
    private CircleImageView createLiveHeadIv;
    private TextView createLiveLocationTv;
    private ImageView createLiveShareFriends;
    private ImageView createLiveShareWechat;
    private ImageView createLiveShareWeibo;
    private ImageView createLiveShareQq;
    private ImageView createLiveShareQzone;
    private TextView liveTextTitleCount;
    private TextView startLiveCountDownTV;
    private boolean isInstallMm1;
    private boolean isInstallWeibo;
    private ImageView closeLiveIv;

    @Override
    protected int getActivityLayout() {
        return R.layout.live_player_activity;
    }

    @Override
    protected int getLayoutId() {
        return R.id.live_layout;
    }

    @Override
    protected void initParam() {
    }

    @Override
    public Activity getActivity() {
        return LiveActivity.this;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }
    //判断软键盘显示与隐藏
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//隐藏
            if (keyboardLayout != null && isEnterRoom) {
                keyboardLayout.setAlpha(0);
                keyboardLayout.setClickable(false);
                if (nofitifationLayout != null) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    lp.height = DensityUtil.dip2px(this, 30);
                    nofitifationLayout.setLayoutParams(lp);
                }
                topView.setVisibility(View.VISIBLE);

            }
        }else if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)){//显示
            topView.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.i("TAG", "主播端走了onCreate");
        liveContentLoader = new ContentLoader(this);
        liveCallBack = new LiveCallBack();
        liveContentLoader.setCallBack(liveCallBack);
        liveContentLoader.createLiveRoom();//直播接口
        viewById = findViewById(R.id.live_layout);
        uploadManager = new UploadManager();//七牛云api
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        setListener();
        checkSharePlatform();//检测手机安装分享平台
        LiveConstant.ROLE=1;
    }

    private void checkSharePlatform() {
        isInstallMm1 = CheckWeixinAndWeibo.checkAPPInstall(this, "com.tencent.mm");
        isInstallWeibo = CheckWeixinAndWeibo.checkAPPInstall(this, "com.sina.weibo");
        if(!isInstallMm1){
            createLiveShareFriends.setVisibility(View.GONE);
            createLiveShareWechat.setVisibility(View.GONE);
        }
        if (!isInstallWeibo) {
            createLiveShareWeibo.setVisibility(View.GONE);
        }

    }

    AMapLocationClient locationClient = null;

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);

    }


    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            AppLog.i("TAG", "获取定位结果：" + loc.getErrorInfo() + "     " + loc.getErrorCode() + "    " + loc.getCountry() + "·" + loc.getProvince() + "·" + loc.getCity());
            try {
                String errorInfo = loc.getErrorInfo();
                if ("success".equals(errorInfo)) {

                    //解析定位结果
                    String result = loc.getCountry() + "·" + loc.getProvince() + "·" + loc.getCity();
                    if (createLiveLocationTv != null) {
                        createLiveLocationTv.setText(result);
                        LiveActivity.liveLocation = result;
                    }
                } else {
                    if (createLiveLocationTv != null) {
                        createLiveLocationTv.setText("Lalocal神秘之地");
                        LiveActivity.liveLocation = "Lalocal神秘之地";
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    };

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setNeedAddress(true);
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        return mOption;
    }


    private void destroyLocation() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;

        }
    }

    protected void findViews() {
        super.findViews();
        //初始化定位
        initLocation();

        backBtn = findView(R.id.BackBtn);
        modelLayout = (RelativeLayout) findViewById(R.id.live_view_top_layout);
        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);
        switchBtn = (ImageView) findViewById(R.id.live_telecast_setting);
        liveGiftImg.setVisibility(View.GONE);
        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.GONE);
        liveQuit = (ImageView) findViewById(R.id.live_quit);

        backBtn.setVisibility(View.GONE);
        modelLayout.setVisibility(View.GONE);
        keyboardLayout.setAlpha(0);
        keyboardLayout.setClickable(false);
        nofitifationLayout = (RelativeLayout) findViewById(R.id.live_nofitifation_layout);
        netWorkHint = (ImageView) findViewById(R.id.network_hint_iv);
        //开始倒计时
        startLiveCountDownTV = (TextView) findViewById(R.id.start_live_count_down);
        // 直播结束
        liveFinishLayout = findView(R.id.live_finish_layout);
        closeLiveIv = (ImageView) findViewById(R.id.live_over_close);
        closeLiveIv.setOnClickListener(buttonClickListener);
        aucienceCount = (TextView) findViewById(R.id.live_over_audience_count);
        overTime = (TextView) findViewById(R.id.live_over_time_tv);


        //挑战
        challengeNewTask = (ImageView) findViewById(R.id.challenge_new_task);
        challengeNewTask.setOnClickListener(buttonClickListener);

        //结束直播

        overMoney = (TextView) findViewById(R.id.live_over_money);

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
            if (isUnDestory&&firstWarning) {
                firstWarning=false;
                final CustomChatDialog customDialog = new CustomChatDialog(LiveActivity.this);
                customDialog.setContent(getString(R.string.live_status_inusual));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCreateLiveLayout() {
        startLocation();
        createLiveLayout = (RelativeLayout) findViewById(R.id.create_live_layout);
        createLiveLayout.setVisibility(View.VISIBLE);
        liveCreateRoomCloseIv = (ImageView) createLiveLayout.findViewById(R.id.live_create_room_close_iv);
        inputStartLive = (TextView) createLiveLayout.findViewById(R.id.input_start_live);
        startLiveLayout = (LinearLayout) createLiveLayout.findViewById(R.id.start_live_layout);
        startLiveBottomLayout = (ScrollView) createLiveLayout.findViewById(R.id.start_live_bottom_layout);
        liveRoomName = (EditText) createLiveLayout.findViewById(R.id.live_room_name);
        createLiveHeadIv = (CircleImageView) createLiveLayout.findViewById(R.id.create_live_head_iv);
        createLiveLocationTv = (TextView) createLiveLayout.findViewById(R.id.create_live_location_tv);
        createLiveShareFriends = (ImageView) createLiveLayout.findViewById(R.id.create_live_share_friends);
        createLiveShareWechat = (ImageView) createLiveLayout.findViewById(R.id.create_live_share_wechat);
        createLiveShareWeibo = (ImageView) createLiveLayout.findViewById(R.id.create_live_share_weibo);
        if(UserHelper.getUserAvatar(this)!=null){
            DrawableUtils.displayImg(this,createLiveHeadIv,UserHelper.getUserAvatar(this));
        }

        liveTextTitleCount = (TextView) createLiveLayout.findViewById(R.id.live_text_title_count);
        createLiveLocationTv.setOnClickListener(buttonClickListener);
        liveCreateRoomCloseIv.setOnClickListener(buttonClickListener);
        startLiveLayout.setOnClickListener(buttonClickListener);
        createLiveShareFriends.setOnClickListener(buttonClickListener);
        createLiveShareWechat.setOnClickListener(buttonClickListener);
        createLiveShareWeibo.setOnClickListener(buttonClickListener);
        liveRoomName.setFocusable(true);
        liveRoomName.setFocusableInTouchMode(true);
        liveRoomName.requestFocus();
        liveRoomName.addTextChangedListener(watcher);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) liveRoomName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(liveRoomName, 0);
                           }
                       },
                300);
    }

    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }
    private void resetOption() {
        locationOption.setNeedAddress(true);
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);
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
            liveTextTitleCount.setText(length + "/20");
        }
    };

    @Override
    protected void showUserInfoDialog(String userId, final String channelId,boolean isMaster) {
        if (isUnDestory) {
            CustomUserInfoDialog dialog = new CustomUserInfoDialog(this, container,userId, channelId,LiveConstant.ROLE, isMaster,creatorAccount,roomId);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    LiveConstant.USER_INFO_FIRST_CLICK = true;
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


    }


    protected void showInputTextView() {
    //    topView.setVisibility(View.GONE);
        keyboardLayout.setAlpha(1.0f);
        keyboardLayout.setFocusable(true);
        keyboardLayout.setClickable(true);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);//两个参数分别是layout_width,layout_height
        lp.height = DensityUtil.dip2px(this, 30);
        lp.addRule(RelativeLayout.ABOVE, R.id.messageActivityBottomLayout);
        nofitifationLayout.setLayoutParams(lp);
        if (inputPanel != null) {
            inputPanel.switchToTextLayout(true);
        }
    }

    @Override
    protected void liveCommonSetting() {
        MobHelper.sendEevent(this, MobEvent.LIVE_ANCHOR_SHARE);
        MasterMoreSettingPopuWindow settingPopuWindow = new MasterMoreSettingPopuWindow(this);
        settingPopuWindow.showSettingView();
        settingPopuWindow.showAsDropDown(settingLiveImg);
        settingPopuWindow.setClickListener(new MasterMoreSettingPopuWindow.MasterMoreSettingListener() {
            @Override
            public void onPopItemClickListener(int viewId, View view) {
                switch (viewId) {
                    case R.id.live_switchover_camera:
                        if (Constant.CAMERABACK) {
                            Constant.CAMERABACK = false;
                            Constant.PRP_ENABLED = true;
                        } else {
                            Constant.CAMERABACK = true;
                            Constant.PRP_ENABLED = false;
                        }
                        worker().enablePreProcessor();//美颜开闭切换
                        worker().getRtcEngine().switchCamera();
                        break;
                    case R.id.live_beauty_switch:
                        if (Constant.PRP_ENABLED) {
                            Constant.PRP_ENABLED = false;
                            ((TextView) view).setText(R.string.live_beauty_off);
                        } else {
                            Constant.PRP_ENABLED = true;
                            ((TextView) view).setText(R.string.live_beauty_on);
                            ((TextView) view).setTextColor(getResources().getColor(R.color.live_beauty_on));
                        }

                        worker().enablePreProcessor();//美颜开闭切换

                        break;
                    case R.id.live_share_pop:
                        if (shareVO != null && isUnDestory) {
                            shareLivePopu();
                        }

                        break;
                    case R.id.live_send_message_pop:
                        showInputTextView();
                        break;
                }
            }
        });
    }

    @Override
    protected void superManagerKickOutUser() {
    }
    private void shareLivePopu() {
        try{
            LiveingSharePopuwindow sharePop = new LiveingSharePopuwindow(LiveActivity.this);
            sharePop.showSharePopu();
            sharePop.showAtLocation(LiveActivity.this.findViewById(R.id.live_layout),
                    Gravity.BOTTOM, 0, 0);
            sharePop.setOnLivingShareListener(new LiveingSharePopuwindow.LivingShareListener() {
                @Override
                public void sharePlatform(SHARE_MEDIA share_media) {
                    isStartLiveShare = false;
                    liveShare(share_media);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    //烧鸡管理员关闭直播间
    @Override
    protected void closeLiveNotifi() {

        endLive();
        Toast.makeText(this,"直播间被管理员关闭",Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void masterOnLineStatus(boolean b) {

    }

    @Override
    protected void showFinishLayout(boolean b, int i) {
    }

    @Override
    protected void receiveChallengeMessage(ChatRoomMessage message) {

        challengeNewTask.setVisibility(View.VISIBLE);

    }



    private void setListener() {
        backBtn.setOnClickListener(buttonClickListener);
        switchBtn.setOnClickListener(buttonClickListener);
        inputChar.setOnClickListener(buttonClickListener);
        quit.setOnClickListener(buttonClickListener);
        liveQuit.setOnClickListener(buttonClickListener);
    }

    int isShareSelector = 5;
    OnClickListener buttonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BackBtn:
                    finishLive();
                    break;
                case R.id.live_telecast_setting:
                    if (Constant.PRP_ENABLED) {
                        Constant.PRP_ENABLED = false;
                        MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_ANCHOR_BEAUTY);
                    } else {
                        Constant.PRP_ENABLED = true;
                        MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_ANCHOR_CAMERA);
                    }
                    worker().enablePreProcessor();//美颜开闭切换
                    worker().getRtcEngine().switchCamera();
                    break;

                case R.id.live_telecast_quit:
                    finishLive();
                    break;
                case R.id.live_quit:
                    MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_ANCHOR_CLOSE);
                    finishLive();
                    break;

                case R.id.challenge_new_task:
                    //TODO 挑战列表
                    challengeNewTask.setVisibility(View.GONE);
                    liveContentLoader.getChallengeList(channelId, -1);
                    break;

                case R.id.live_telecast_input_text:
                    showInputTextView();
                    break;
                case R.id.create_live_location_tv:
                    Intent intent = new Intent(LiveActivity.this, LiveLocationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.live_create_room_close_iv:
                    MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_CANCEL);
                    if (isStartLive) {
                        finish();
                    } else {
                        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                        clearChatRoom();
                    }
                    liveContentLoader.cancelLiveRoom(channelId);

                    break;
                case R.id.create_live_share_friends:
                    MobHelper.sendEevent(LiveActivity.this,MobEvent.LIVE_START_WECHAT1_SHARE);
                    setSelected(0,!isFirstFirendsClick);
                    break;
                case R.id.create_live_share_wechat:
                    MobHelper.sendEevent(LiveActivity.this,MobEvent.LIVE_START_WECHAT2_SHARE);
                    setSelected(2,isFirstFirendsClick);
                    break;
                case R.id.create_live_share_weibo:
                    MobHelper.sendEevent(LiveActivity.this,MobEvent.LIVE_START_WEIBO_SHARE);
                    if(!isInstallMm1){
                        setSelected(1,!isFirstFirendsClick);
                    }else{
                        setSelected(1,isFirstFirendsClick);
                    }
                    break;
                case R.id.start_live_layout:
                    startLiveing();
                    break;
                case R.id.live_over_close:
                    finish();
                    break;
            }
        }
    };


    private void setSelected(int isShareSelectr, boolean isSelector) {
        switch (isShareSelectr){
            case 0:
                createLiveShareWeibo.setSelected(false);
                createLiveShareFriends.setSelected(isSelector);
                createLiveShareWechat.setSelected(false);
                if(isSelector){
                    isShareSelector=0;
                }else{
                    isShareSelector=-1;
                }
                isFirstFirendsClick=isSelector;
                break;
            case 1:
                createLiveShareWeibo.setSelected(isSelector);
                createLiveShareFriends.setSelected(false);
                createLiveShareWechat.setSelected(false);
                if(isSelector){
                    isShareSelector=1;
                }else{
                    isShareSelector=-1;
                }
                if(!isInstallMm1){
                    isFirstFirendsClick=isSelector;
                }else{
                    isFirstFirendsClick=!isSelector;
                }

                break;
            case 2:
                createLiveShareWeibo.setSelected(false);
                createLiveShareFriends.setSelected(false);
                createLiveShareWechat.setSelected(isSelector);
                if(isSelector){
                    isShareSelector=2;
                }else{
                    isShareSelector=-1;
                }
                isFirstFirendsClick=!isSelector;
                break;
        }

    }



    private void startLiveing() {
        MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_START_START);
        if (isFirstClick) {
            isFirstClick = false;
            roomName = liveRoomName.getText().toString().trim();

            if (TextUtils.isEmpty(roomName)) {
                Toast.makeText(LiveActivity.this, "直播间标题不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            isClickStartLiveBtn = true;
            startTime = System.currentTimeMillis();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(startLiveBottomLayout.getWindowToken(), 0);


            if (isShareSelector == 0) {
                liveShare(SHARE_MEDIA.WEIXIN_CIRCLE);
            } else if (isShareSelector == 1) {
                liveShare(SHARE_MEDIA.SINA);
            } else if (isShareSelector == 2) {
                liveShare(SHARE_MEDIA.WEIXIN);
            } else {
                startShareLive(false);
            }
        }
    }

    public static final String CREATE_ROOMID = "createRoomId";
    boolean firstWarning = true;

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }

    public class LiveCallBack extends ICallBack {
        @Override
        public void onCreateLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {
            super.onCreateLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                LiveRowsBean liveRowsBean = createLiveRoomDataResp.getResult();
                roomId = String.valueOf(liveRowsBean.getRoomId());
                LiveConstant.ROOM_ID=roomId;
                SPCUtils.put(getActivity(), CREATE_ROOMID, String.valueOf(roomId));
                createNickName = liveRowsBean.getUser().getNickName();
                avatar = liveRowsBean.getUser().getAvatar();
                roomId = String.valueOf(liveRowsBean.getRoomId());
                channelId = String.valueOf(liveRowsBean.getId());
                cname = liveRowsBean.getCname();
                shareVO = liveRowsBean.getShareVO();
                userId = String.valueOf(liveRowsBean.getUser().getId());
                String log = TAG + "  / " + Thread.currentThread().getStackTrace()[2].getMethodName() + " REMIND_BACK:" + CommonUtil.REMIND_BACK;
                liveContentLoader.getUploadLogs(log);
                getParameter(liveRowsBean);
                initParam();

                if (CommonUtil.REMIND_BACK != 1) {
                    AppLog.i("TAG", "开启直播走了这1");
                    initCreateLiveLayout();
                } else {
                    AppLog.i("TAG", "开启直播走了这2");
                    if (TextUtils.isEmpty(roomName)) {
                        roomName = liveRowsBean.getUser().getNickName();
                    }

                    liveContentLoader.alterLive(roomName, channelId, null, null, null, null);
                }
            }
        }

        @Override
        public void onResponseLog(String json) {
            super.onResponseLog(json);
            AppLog.i("TAG", "获取日志信息：" + json);
        }


        @Override
        public void onAlterLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {//修改直播间
            super.onAlterLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                avatar = createLiveRoomDataResp.getResult().getUser().getAvatar();
                LiveRowsBean result = createLiveRoomDataResp.getResult();
                startLive();
                CommonUtil.REMIND_BACK = 0;
                AppLog.i("TAG", "调用了修改直播间接口，并成功返回回调");
            }
        }

        @Override
        public void onCloseLive(CloseLiveBean closeLiveBean) {
            super.onCloseLive(closeLiveBean);
            if (closeLiveBean.getReturnCode() == 0) {
                overMoney.setText(String.valueOf(closeLiveBean.getResult().getScore()));
            }
        }

        @Override
        public void onImgToken(ImgTokenBean imgTokenBean) {
            super.onImgToken(imgTokenBean);
            if (imgTokenBean.getReturnCode() == 0) {
                ImgTokenResult imgToken = imgTokenBean.getResult();
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

        @Override
        public void onCheckManager(LiveManagerBean liveManagerBean) {
            super.onCheckManager(liveManagerBean);
            if (liveManagerBean.getResult() != 0) {
                Toast.makeText(LiveActivity.this, "对方为管理员!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onChallengeList(String json) {
            super.onChallengeDetails(json);
            final ChallengeDetailsResp challengeDetailsResp = new Gson().fromJson(json, ChallengeDetailsResp.class);
            if (challengeDetailsResp.getReturnCode() == 0) {
                //TODO  进入挑战列表
                List<ChallengeDetailsResp.ResultBean> result = challengeDetailsResp.getResult();
                if (isUnDestory) {
                    final CustomChallengeListviewDialog customChallengeListviewDialog = new CustomChallengeListviewDialog(LiveActivity.this, result);
                    customChallengeListviewDialog.setDialogClickListener(new CustomChallengeListviewDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener(int status, int challengeId) {
                            //主播操作挑战
                            customChallengeListviewDialog.dismiss();
                            showHintDialog(status, challengeId);
                        }
                    });
                    customChallengeListviewDialog.show();
                }

            }
        }

        @Override
        public void onLiveChallengeStatus(ChallengeDetailsResp.ResultBean resultBean) {
            super.onLiveChallengeStatus(resultBean);
            int status = resultBean.getStatus();
            if (status == 1) {

                CustomToast customToast = new CustomToast(LiveActivity.this, getString(R.string.challenge_task_accept));
            }
            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setStyle(MessageType.challenge);
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChallengeModel(resultBean);
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "挑战", roomId, AuthPreferences.getUserAccount(), liveMessage);
            sendMessage(imMessage, MessageType.challenge);
        }

    }
    private void showHintDialog(final int status, final int challengeId) {

        String hintTitle = null;
        String hintContent = null;
        String cancel = null;
        String ok = null;
        switch (status) {
            case 1://进行中
                liveContentLoader.getLiveChallengeStatus(status, challengeId);
                return;
            case 2://已完成
                hintTitle = getString(R.string.challenge_complete_task);
                hintContent = getString(R.string.challenge_complete_content);
                cancel = getString(R.string.challenge_dialog_cancel);
                ok = getString(R.string.challenge_dialog_complete);
                break;
            case 3://拒绝
                liveContentLoader.getLiveChallengeStatus(status, challengeId);
                return;
            case 4://放弃任务
                hintTitle = getString(R.string.challenge_waive_task);
                hintContent = getString(R.string.challenge_waive_content);
                cancel = getString(R.string.challenge_dialog_cancel);
                ok = getString(R.string.challenge_dialog_waive_ok);
                break;
        }
        if (isUnDestory) {
            CustomChatDialog dialogChallenge = new CustomChatDialog(LiveActivity.this);
            dialogChallenge.setTitle(hintTitle);
            dialogChallenge.setContent(hintContent);
            dialogChallenge.setCancelable(false);
            dialogChallenge.setCancelBtn(cancel, new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {

                }
            });
            dialogChallenge.setSurceBtn(ok, new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    liveContentLoader.getLiveChallengeStatus(status, challengeId);
                }
            });
            dialogChallenge.show();
        }

    }




    // 退出聊天室
    private void logoutChatRoom() {
        if (isUnDestory) {
            CustomChatDialog customDialog = new CustomChatDialog(getActivity());
            customDialog.setContent(getString(R.string.finish_confirm));
            customDialog.setCancelable(false);
            customDialog.setCancelBtn(getString(R.string.live_over), new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    //结束直播的时间
                    endHandler();
                    endTime = System.currentTimeMillis();
                    isCloseLive = true;
                    endLive();
                }
            });
            customDialog.setSurceBtn(getString(R.string.live_continue), null);
            customDialog.show();
        }

    }

    private void endLive() {
        AppLog.i("TAG", "主播端走了，endLive");
        DialogUtil.clear();
        liveContentLoader.cancelLiveRoom(channelId);
        LiveMessage liveMessage = new LiveMessage();
        liveMessage.setStyle(MessageType.leaveLive);
        liveMessage.setCreatorAccount(creatorAccount);
        liveMessage.setUserId(userId);
        if (container != null && container.account != null) {
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "结束直播了哈哈哈哈哈哈", roomId, AuthPreferences.getUserAccount(), liveMessage);
            sendMessage(imMessage, MessageType.leaveLive);
        }
        if (isLeaveChannel) {
            deInitUIandEvent();
            isLeaveChannel = false;
        }
        if (isClickStartLiveBtn) {
            isStartLive = false;
            drawerLayout.closeDrawer(Gravity.RIGHT);
            if (inputPanel != null) {
                inputPanel.collapse(true);// 收起软键盘
            }
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            String hms = formatter.format(endTime - startTime);
            overTime.setText(hms);
            aucienceCount.setText(String.valueOf(maxOnLineUserCount));
            liveFinishLayout.setVisibility(View.VISIBLE);

        } else {
            finish();
        }
    }

    @Override
    protected void checkNetInfo(String netType, int reminder) {
        if ("rests".equals(netType) && reminder == 0) {
            LiveConstant.NET_CHECK = 1;
            if (isUnDestory) {
                CustomChatDialog customChatDialog = new CustomChatDialog(LiveActivity.this);
                customChatDialog.setTitle(getString(R.string.live_hint));
                customChatDialog.setContent(getString(R.string.live_net_type_cmcc));
                customChatDialog.setCancelable(false);
                customChatDialog.setCancelBtn(getString(R.string.live_continue), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        LiveConstant.NET_CHECK = 0;
                    }
                });
                customChatDialog.setSurceBtn(getString(R.string.live_over), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        LiveConstant.NET_CHECK = 0;
                        endLive();
                    }
                });
                customChatDialog.show();
            }

        }
    }

    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_BROADCASTER;
    }

    boolean isCloseLive = false;//结束直播状态

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        AppLog.i("TAG", "直播端接受到一帧视频");
    }



    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

        this.joinChannel = channel;
        this.joinUid = uid;
        this.joinElapsed = elapsed;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isShowPopuTo = true;
                isStartLive = true;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(liveSettingLayout.getWindowToken(), 0);
                isEnterRoom = true;
                if (createLiveLayout != null) {
                    createLiveLayout.setVisibility(View.GONE);
                }
                drawerLayout.setVisibility(View.VISIBLE);
                startLiveCountDown();
                if (messageListPanel != null) {
                    messageListPanel.setHeaderViewVisible();
                }
                modelLayout.setVisibility(View.VISIBLE);
                scoreLayout.setVisibility(View.VISIBLE);
                userOnLineCountParameter = channelId + "/onlineUsers";

                showLiveingShareDialog();
            }
        });

    }
    int startTimes=3 ;
    private void startLiveCountDown() {
        new CountDownTimer(4000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                startLiveCountDownTV.setText(String.valueOf(startTimes));
                --startTimes;
            }
            @Override
            public void onFinish() {
                startLiveCountDownTV.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawerLayout.openDrawer(Gravity.RIGHT);
                            }
                        });

                    }
                }).start();

            }
        }.start();

    }

    private void showLiveingShareDialog() {
        new CountDownTimer(60000, 60000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                AppLog.i("TAG", "显示主播直播1分钟，分享dialog");
                if(isUnDestory&&liveFinishLayout.getVisibility()!=View.VISIBLE){
                    shareLivePopu();
                }
            }
        }.start();


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onUserOffline(int uid, int reason) {
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {

    }

    @Override
    public void onConnectionInterrupted() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                againLoginIm();
            }
        });

    }


    private void againLoginIm() {
        AppLog.i("TAG", "直播連接中斷连接中断回调");
        //  NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        String userToken = AuthPreferences.getUserToken();
        final String userAccount = AuthPreferences.getUserAccount();

        NIMClient.getService(AuthService.class).login(new LoginInfo(userAccount, userToken)).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                AppLog.i("TAG", "LiveActivity,直播中断重登账号成功");
                DemoCache.setAccount(userAccount);
                DemoCache.getRegUserInfo();
                DemoCache.setLoginStatus(true);
            }

            @Override
            public void onFailed(int i) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        againLoginIm();
                    }
                }, 1000);
                AppLog.i("TAG", "LiveActivity,直播中断重登账号失败" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                AppLog.i("TAG", "LiveActivity,直播中断重登账号失败");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        againLoginIm();
                    }
                }, 1000);
            }
        });


    }

    @Override
    public void onConnectionLost() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //    againLoginIm();
            }
        });

        AppLog.i("TAG", "直播連接中斷连接丟失");
    }

    @Override
    public void onError(final int err) {
        AppLog.i("TAG", "直播发生错误:" + err);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (err == 1003) {
                    if (isUnDestory) {
                        final CustomChatDialog customDialog = new CustomChatDialog(getActivity());
                        customDialog.setContent(getString(R.string.live_camera_start_failure));
                        customDialog.setCancelable(false);
                        customDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                finish();
                            }
                        });

                        customDialog.show();
                    }


                } else if (err == 1018) {
                    CommonUtil.RESULT_DIALOG = 0;
                    if (isUnDestory) {
                        final CustomChatDialog customDialog = new CustomChatDialog(getActivity());
                        customDialog.setContent(getString(R.string.live_frequency_start_failure));
                        customDialog.setCancelable(false);
                        customDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                finish();
                            }
                        });
                        customDialog.show();
                    }

                }
            }
        });

    }

    @Override
    public void onVideoStopped() {//停止视频功能

    }

    @Override
    public void onLeaveChannel(final IRtcEngineEventHandler.RtcStats stats) {//离开频道的回调

    }

    @Override
    public void onUserEnableVideo(int uid, boolean enabled) {
    }

    @Override
    public void onLastmileQuality(final int quality) {//网络质量检测
        AppLog.i("TAG", "声网sdk网络质量检测"+quality);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (quality) {
                    case 1:
                        netWorkHint.setImageResource(R.drawable.signal_great);
                        break;
                    case 2:
                        netWorkHint.setImageResource(R.drawable.signal_ok);
                        break;
                    case 3:
                        netWorkHint.setImageResource(R.drawable.signal_bad);
                        break;
                    case 4:
                        netWorkHint.setImageResource(R.drawable.signal_non);
                        break;
                    default:
                        netWorkHint.setImageResource(R.drawable.signal_non);
                        break;
                }
            }
        });
    }

    @Override
    protected void initUIandEvent() {
        super.initUIandEvent();

    }

    private void startLive() {
        int cRole = Constants.CLIENT_ROLE_BROADCASTER;
        doConfigEngine(cRole);
        registerObservers(true);
        if (isBroadcaster(cRole)) {
            //  主播播放器：1725    oldBottom:894
            final SurfaceView surfaceView = RtcEngine.CreateRendererView(getApplicationContext());
            int childCount = palyerLayout.getChildCount();
            if (childCount > 0) {
                palyerLayout.removeAllViews();
            }
            palyerLayout.addView(surfaceView);
            surfaceView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (palyerLayout != null && palyerLayout.getChildAt(0) != null) {
                        if (bottom > oldBottom) {
                            palyerLayout.getChildAt(0).layout(left, top, right, bottom);
                        } else {
                            palyerLayout.getChildAt(0).layout(oldLeft, oldTop, oldRight, oldBottom);
                        }
                    }
                }
            });

            rtcEngine().setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));//VideoCanvas:本地代码显示属性
            surfaceView.setZOrderOnTop(true);
            surfaceView.setZOrderMediaOverlay(true);
            worker().preview(true, surfaceView, UserHelper.getUserId(LiveActivity.this));

        }
        worker().joinChannel(cname, UserHelper.getUserId(LiveActivity.this));
    }

    private void doConfigEngine(int cRole) {

        int vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_720P;

        switch (LiveConstant.LIVE_DEFINITION) {
            case 1:
                vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_720P;
                break;
            case 2:
                vProfile = IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_480P;
                break;
            case 3:
                break;
        }

        worker().configEngine(cRole, vProfile);
    }

    boolean isLeaveChannel = true;

    protected void deInitUIandEvent() {
        doLeaveChannel();
        event().removeEventHandler(this);
    }

    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }

    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
        if (isBroadcaster()) {
            AppLog.i("TAG", "停止视频预览。。。。");
            worker().preview(false, null, 0);
        }
    }

    //分享
    @Override
    protected void liveShare(SHARE_MEDIA share_media) {
        ShareAction sp = new ShareAction(this);
        sp.setPlatform(share_media);
        sp.setCallback(new MyUMListener());
        sp.withTitle(shareVO.getTitle());
        sp.withTargetUrl(shareVO.getUrl());
        sp.withText(shareVO.getTitle());
        UMImage image = new UMImage(this, shareVO.getImg());
        sp.withMedia(image);
        sp.share();
    }

    class MyUMListener implements UMShareListener {

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            liveContentLoader.getShareStatistics(String.valueOf(shareVO.getTargetType()), String.valueOf(shareVO.getTargetId()), share_media.equals(SHARE_MEDIA.SINA) ? "2" : (share_media.equals(SHARE_MEDIA.WEIXIN) ? "1" : "0"));
            if (share_media.equals(SHARE_MEDIA.SINA)) {
                Toast.makeText(LiveActivity.this, "微博分享成功!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                Toast.makeText(LiveActivity.this, "微信分享成功!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                Toast.makeText(LiveActivity.this, "微信朋友圈分享成功!", Toast.LENGTH_SHORT).show();
            }
            startShareLive(true);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if (share_media.equals(SHARE_MEDIA.SINA)) {
                Toast.makeText(LiveActivity.this, "微博分享失败!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                Toast.makeText(LiveActivity.this, "微信分享失败!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                Toast.makeText(LiveActivity.this, "微信朋友圈分享失败!", Toast.LENGTH_SHORT).show();
            }
            startShareLive(true);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            if (share_media.equals(SHARE_MEDIA.SINA)) {
                Toast.makeText(LiveActivity.this, "已取消微博分享!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                Toast.makeText(LiveActivity.this, "已取消微信分享!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                Toast.makeText(LiveActivity.this, "已取消微信朋友圈分享!", Toast.LENGTH_SHORT).show();
            }
            startShareLive(true);
        }


    }

    private void startShareLive(boolean isResult) {
        if (!isStartLiveShare) {
            return;
        }
        if (!isResult) {
            if (channelId != null) {
                liveContentLoader.alterLive(roomName, channelId, null, null, null, null);
            } else {
                finish();
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (channelId != null) {
                        liveContentLoader.alterLive(roomName, channelId, null, null, null, null);
                    } else {
                        finish();
                    }

                }
            }).start();
        }

    }


    private void finishLive() {
        if (isStartLive) {
            logoutChatRoom();
        } else {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }
    }

    // 清空聊天室缓存
    private void clearChatRoom() {
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
        finish();
    }

    // 网络连接成功
    protected void onConnected() {
        if (disconnected == false) {
            return;
        }
        disconnected = false;
    }

    // 网络断开
    protected void onDisconnected() {
        LogUtil.i(TAG, "live on disconnected");
        disconnected = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewById.addOnLayoutChangeListener(this);
        if (createLiveLayout!=null&&createLiveLayout.getVisibility() == View.VISIBLE) {
            createLiveLocationTv.setText(LiveActivity.liveLocation);
        } else {
            if (inputPanel != null) {
                inputPanel.hideInputMethod();
                inputPanel.collapse(false);
            }
        }


        rtcEngine().enableLastmileTest();//启动网络质量检测

    }

    @Override
    protected void onStop() {
        super.onStop();
        DialogUtil.clear();
        rtcEngine().disableLastmileTest();//关闭网络质量检测
        AppLog.i("TAG", "LiveActivity:onStop");
    }

    protected void onPause() {
        super.onPause();
        AppLog.i("TAG", "LiveActivity:onPause");
    }

    @Override
    protected void onDestroy() {
        AppLog.i("TAG", "直播端走了onDestroy");

        destroyLocation();
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        if (isLeaveChannel) {
            deInitUIandEvent();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishLive();
    }

}