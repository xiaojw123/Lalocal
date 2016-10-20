package com.lalocal.lalocal.live.entertainment.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.util.log.LogUtil;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.ChallengeDetailsResp;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.model.OnLineUser;
import com.lalocal.lalocal.live.entertainment.ui.CreateLiveRoomPopuwindow;
import com.lalocal.lalocal.live.entertainment.ui.CustomChallengeListviewDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomLiveUserInfoDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomToast;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.live.thirdparty.live.LivePlayer;
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
import com.lalocal.lalocal.util.CheckWeixinAndWeibo;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.WrapContentImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.MemberOption;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qiniu.android.storage.UploadManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

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

    private LinearLayout modelLayout;//用户信息layout
    private LinearLayout keyboardLayout;//自定义键盘输入

    private ViewGroup liveFinishLayout;
    // data

    private ContentLoader liveContentLoader;
    private SpecialShareVOBean shareVO;
    private UploadManager uploadManager;
    private Timer timer;

    private String isSecond;

    boolean isShowPopuTo = false;
    private String roomId;
    private List<LiveManagerListBean> managerList;
    private CreateLiveRoomPopuwindow createLiveRoomPopuwindow;
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
    protected void clickChallengeBtn() {

        liveContentLoader.getChallengeList(channelId, -1);

        initGuideDialog();
        challengeNewTask.setVisibility(View.VISIBLE);
    }

    @Override
    public Activity getActivity() {
        return LiveActivity.this;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        AppLog.i("TAG", "onWindowFocusChanged");
        if (hasFocus && !isshowPopu && isCancelCreama && CommonUtil.REMIND_BACK != -1) {
            isshowPopu = true;
            showCreateLiveRoomPopuwindow();

        }
    }

    //判断软键盘显示与隐藏
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//隐藏
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

    public static void start(Context context, LiveRowsBean result, String ann, String isSecond) {
        Intent intent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("LiveRowsBean", result);
        intent.putExtras(mBundle);
        intent.setClass(context, LiveActivity.class);
        intent.putExtra(ANNOUCEMENT, ann);
        intent.putExtra(IS_SECOND, isSecond);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.i("TAG", "主播端走了onCreate");
        liveContentLoader = new ContentLoader(this);

        liveCallBack = new LiveCallBack();
        liveContentLoader.setCallBack(liveCallBack);
        viewById = findViewById(R.id.live_layout);
        uploadManager = new UploadManager();//七牛云api
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        gainIntent();
        setListener();

    }
    private void gainIntent() {
        LiveRowsBean liveRowsBean = getIntent().getParcelableExtra("LiveRowsBean");
        createNickName = liveRowsBean.getUser().getNickName();
        avatar = liveRowsBean.getUser().getAvatar();
        roomId = String.valueOf(liveRowsBean.getRoomId());
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
        channelId = String.valueOf(liveRowsBean.getId());
        cname = liveRowsBean.getCname();
        shareVO = liveRowsBean.getShareVO();
        isSecond = getIntent().getStringExtra(IS_SECOND);
    }

    protected void findViews() {
        super.findViews();
        backBtn = findView(R.id.BackBtn);
        modelLayout = (LinearLayout) findViewById(R.id.live_telecast_model_layout);
        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);
        switchBtn = (ImageView) findViewById(R.id.live_telecast_setting);
        liveGiftImg.setVisibility(View.GONE);

        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveQuit = (ImageView) findViewById(R.id.live_quit);
        onlineCount = (TextView) findViewById(R.id.live_online_count);
        backBtn.setVisibility(View.GONE);

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
        blurImageView = (BlurImageView) findViewById(R.id.live_over_bg);
        TextView shareTv= (TextView) findViewById(R.id.over_share_title);
        playLike.setVisibility(View.INVISIBLE);


        //挑战
        challengeNewTask = (ImageView) findViewById(R.id.challenge_new_task);
        challengeNewTask.setOnClickListener(buttonClickListener);

        //结束直播
        liveOverBack = (TextView) findViewById(R.id.live_over_back_home);
        overMoney = (TextView) findViewById(R.id.live_over_money);
        liveOverBack.setOnClickListener(buttonClickListener);
        overLiveShareFriends = (ImageView) findViewById(R.id.live_over_share_friends);
        overLiveShareWeibo = (ImageView) findViewById(R.id.live_over_share_weibo);
        overLiveShareWeixin = (ImageView) findViewById(R.id.live_over_share_weixin);
        overLiveShareFriends.setOnClickListener(buttonClickListener);
        overLiveShareWeibo.setOnClickListener(buttonClickListener);
        overLiveShareWeixin.setOnClickListener(buttonClickListener);

        boolean isInstallMm1 = CheckWeixinAndWeibo.checkAPPInstall(this,"com.tencent.mm");
        boolean isInstallWeibo = CheckWeixinAndWeibo.checkAPPInstall(this, "com.sina.weibo");

        if(!isInstallMm1){
            overLiveShareFriends.setVisibility(View.GONE);
            overLiveShareWeixin.setVisibility(View.GONE);
        }
        if(!isInstallWeibo){
            overLiveShareWeibo.setVisibility(View.GONE);
        }
        if(!isInstallMm1&&!isInstallWeibo){
            shareTv.setVisibility(View.GONE);
        }

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
    protected void receiveChallengeMessage(ChatRoomMessage message) {

        challengeNewTask.setVisibility(View.VISIBLE);

    }

    /**
     * 初始化新手引导弹窗
     */
    private void initGuideDialog() {
        // 是否跳过新手引导
        boolean isSkipGuide = SPCUtils.getBoolean(this, "skip_guide");
        List<Button> dotBtns = new ArrayList<>();
        // 不跳过
        if (!isSkipGuide) {
            // 显示过新手引导后，不再显示新手引导
//            SPCUtils.put(this, "skip_guide", true);
            // -弹出新手引导窗口
            // 获取弹出窗口的view
            View popView = LayoutInflater.from(this).inflate(R.layout.novice_guide_layout, null);
            View rootView = LayoutInflater.from(this).inflate(R.layout.live_player_activity, null);
            // 初始化弹出窗口
            final PopupWindow pop = new PopupWindow(popView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT, true);
            // 设置可触摸
            pop.setTouchable(true);
            // 设置内容外不可触摸
            pop.setOutsideTouchable(false);
            // 全屏居中显示
            pop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            // 进入时渐变动画
            pop.setAnimationStyle(R.style.GuidePopupStyle);
            // 弹窗消失事件
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });

            // -新手引导ViewPager实现
            // 新手引导ViewPager
            ViewPager vp = (ViewPager) popView.findViewById(R.id.vp_guide);
            // 跳过按钮
            final Button btnSkip = (Button) popView.findViewById(R.id.btn_skip);
            // 小点容器
            LinearLayout dotContainer = (LinearLayout) popView.findViewById(R.id.dot_container);
            // 小点列表
            dotBtns = CommonUtil.initDot(this, vp, dotContainer, GUIDE_PAGE_SIZE, 0, CommonUtil.WHITE_DOT);

            final List<Button> finalDotBtns = dotBtns;
            // 设置适配器
            vp.setAdapter(new GuideAdapter(pop));
            // 设置页边距
            vp.setPageMargin((int) getResources().getDimension(R.dimen.novice_guide_vp_margin));
            // “跳过”按钮下划线
            btnSkip.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            // 抗锯齿
            btnSkip.getPaint().setAntiAlias(true);
            // “跳过”按钮点击事件
            btnSkip.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 弹窗消失
                    pop.dismiss();
                }
            });
            // 页面滑动时间
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    // 根据ViewPager滑动选择对应小圆点
                    CommonUtil.selectDotBtn(finalDotBtns, position, CommonUtil.WHITE_DOT);
                    // 如果是最后一页，则去掉不显示取消按钮
                    if (position == GUIDE_PAGE_SIZE - 1) {
                        btnSkip.setVisibility(View.INVISIBLE);
                    } else {
                        btnSkip.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    /**
     * 新手引导页适配器
     */
    private class GuideAdapter extends PagerAdapter {

        PopupWindow window;

        public GuideAdapter(PopupWindow window) {
            this.window = window;
        }

        @Override
        public int getCount() {
            return GUIDE_PAGE_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            View view = LayoutInflater.from(LiveActivity.this).inflate(R.layout.novice_guide_item, null);
            WrapContentImageView page = (WrapContentImageView) view.findViewById(R.id.img_guide);
            switch (position) {
                case 0:
                    page.setImageResource(R.drawable.card_1);
                    break;
                case 1:
                    page.setImageResource(R.drawable.card_2);
                    break;
                case 2:
                    page.setImageResource(R.drawable.card_3);
                    break;
                case 3:
                    page.setImageResource(R.drawable.card_4);
                    break;
            }
            if (position == GUIDE_PAGE_SIZE - 1) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.dismiss();
                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private void setListener() {
        backBtn.setOnClickListener(buttonClickListener);
        switchBtn.setOnClickListener(buttonClickListener);
        inputChar.setOnClickListener(buttonClickListener);
        quit.setOnClickListener(buttonClickListener);
        playLike.setOnClickListener(buttonClickListener);
        liveQuit.setOnClickListener(buttonClickListener);
    }


    OnClickListener buttonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BackBtn:
                    finishLive();
                    break;
                case R.id.live_telecast_setting:
                    worker().getRtcEngine().switchCamera();
                    break;
                case R.id.live_telecast_input_text:
                    keyboardLayout.setAlpha(1.0f);
                    keyboardLayout.setClickable(true);
                    liveSettingLayout.setVisibility(View.GONE);
                    liveSettingLayout.setClickable(false);
                    inputPanel.switchToTextLayout(true);
                    break;
                case R.id.live_telecast_like:
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
                case R.id.challenge_new_task:
                    //TODO 挑战列表
                    challengeNewTask.setVisibility(View.GONE);
                    liveContentLoader.getChallengeList(channelId, -1);
                    break;
            }
        }
    };

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }

    public class LiveCallBack extends ICallBack {

        private int errorCode;

        @Override
        public void onOnLinesCount(String json) {
            super.onOnLinesCount(json);
            OnLineUser onLineUser = new Gson().fromJson(json, OnLineUser.class);

            int returnCode = onLineUser.getReturnCode();
            AppLog.i("TAG", "获取在线人数" + onLineUser.getResult()+"返回码:"+returnCode);
            if(onLineUser.getErrorCode()!=null){
               errorCode = (int)onLineUser.getErrorCode();
           }
            if(errorCode==13){
                AppLog.i("TAG","收到服务器返回的直播间关闭的消息");
                LiveMessage liveMessage=new LiveMessage();
                liveMessage.setStyle(MessageType.leaveLive);
                liveMessage.setUserId(userId);
                liveMessage.setCreatorAccount(creatorAccount);
                IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "结束直播", roomId, AuthPreferences.getUserAccount(), liveMessage);
                sendMessage(imMessage, MessageType.leaveLive);
                finish();
            }

            int result = onLineUser.getResult();
            if (maxOnLineUserCount < result) {
                maxOnLineUserCount = result;
            }
            if (onLineUser.getReturnCode() == 0 && onLineUser.getResult() != 0 && onLineUser.getResult() > 0) {
                onlineCountText.setText(String.valueOf(onLineUser.getResult()) + "人");
                LiveMessage liveMessage = new LiveMessage();
                liveMessage.setStyle(MessageType.onlineNum);
                liveMessage.setCreatorAccount(creatorAccount);
                liveMessage.setUserId(userId);
                liveMessage.setOnlineNum(String.valueOf(onLineUser.getResult()));
                liveMessage.setChannelId(channelId);
                IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "上传在线人数", roomId, AuthPreferences.getUserAccount(), liveMessage);
                sendMessage(imMessage, MessageType.onlineNum);
            }
        }

        @Override
        public void onAlterLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {//修改直播间
            super.onAlterLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                avatar = createLiveRoomDataResp.getResult().getUser().getAvatar();
                LiveRowsBean result = createLiveRoomDataResp.getResult();
                startLive();
                CommonUtil.REMIND_BACK = 0;
                AppLog.i("TAG","调用了修改直播间接口，并成功返回回调");
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
                final String filename = imgToken.getFilename();
                final String token = imgToken.getToken();
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
                final CustomChallengeListviewDialog customChallengeListviewDialog = new CustomChallengeListviewDialog(LiveActivity.this, result);
                customChallengeListviewDialog.setDialogClickListener(new CustomChallengeListviewDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener(int status,int challengeId) {
                        //主播操作挑战
                        customChallengeListviewDialog.dismiss();
                        showHintDialog(status,challengeId);
                    }
                });
                customChallengeListviewDialog.show();
            }
        }

        @Override
        public void onLiveChallengeStatus(ChallengeDetailsResp.ResultBean resultBean) {
            super.onLiveChallengeStatus(resultBean);
            int status = resultBean.getStatus();
            if(status==1){
                CustomToast customToast=new CustomToast(LiveActivity.this,getString(R.string.challenge_task_accept));
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

    private void showHintDialog(final int status,final int challengeId) {

        String hintTitle=null;
        String hintContent=null;
        String cancel=null;
        String ok=null;
        switch (status) {
            case 1://进行中
                liveContentLoader.getLiveChallengeStatus(status,challengeId);
              return;
            case 2://已完成
                hintTitle=getString(R.string.challenge_complete_task);
                hintContent=getString(R.string.challenge_complete_content);
                cancel=getString(R.string.challenge_dialog_cancel);
                ok=getString(R.string.challenge_dialog_complete);
                break;
            case 3://拒绝
                liveContentLoader.getLiveChallengeStatus(status,challengeId);
               return;
            case 4://放弃任务
                hintTitle=getString(R.string.challenge_waive_task);
                hintContent=getString(R.string.challenge_waive_content);
                cancel=getString(R.string.challenge_dialog_cancel);
                ok=getString(R.string.challenge_dialog_waive_ok);
                break;
        }
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
                                liveContentLoader.getLiveChallengeStatus(status,challengeId);
                            }
                        });
        dialogChallenge.show();


    }

    @Override
    protected void showMasterInfoPopuwindow(final LiveUserInfoResultBean result1, final boolean isMuted, final String meberAccount, int id, int managerId, final List<LiveManagerListBean> managerList) {

        if (userInfoContentLoader == null) {
            userInfoContentLoader = new ContentLoader(LiveActivity.this);
            userInfoContentLoader.setCallBack(new ICallBack() {
            });
        }
        isMuteds = isMuted;
        Object statusa = result1.getAttentionVO().getStatus();


        if (statusa != null) {
            double parseDouble = Double.parseDouble(String.valueOf(statusa));
            status = (int) parseDouble;
        }

            isManager = managerId != 0 ? true : false;
            CustomLiveUserInfoDialog customLiveUserInfoDialog = new CustomLiveUserInfoDialog(LiveActivity.this, result1, isManager, isMuted);
            customLiveUserInfoDialog.setCancelable(false);
            customLiveUserInfoDialog.setCancelBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {

                }
            });

            customLiveUserInfoDialog.setUserHomeBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                @Override
                public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                    Intent intent = new Intent(LiveActivity.this, LiveHomePageActivity.class);
                    intent.putExtra("userId", String.valueOf(id));
                    startActivity(intent);
                }
            });
            if (LiveConstant.IDENTITY == LiveConstant.IS_ONESELF) {
                LiveConstant.IDENTITY = LiveConstant.IS_ONESELF;
                customLiveUserInfoDialog.setSurceBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                    @Override
                    public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                        Intent intent = new Intent(LiveActivity.this, LiveHomePageActivity.class);
                        intent.putExtra("userId", String.valueOf(id));
                        startActivity(intent);
                    }

                });
            } else {
                LiveConstant.IDENTITY = LiveConstant.LIVEER_CHECK_ADMIN;
                customLiveUserInfoDialog.setAttention(status == 0 ? getString(R.string.live_attention) : getString(R.string.live_attention_ok), new CustomLiveUserInfoDialog.CustomLiveFansOrAttentionListener() {
                    int fansCounts = -2;

                    @Override
                    public void onCustomLiveFansOrAttentionListener(String id, TextView fansView, TextView attentionView, int fansCount, int attentionCount, TextView textView) {
                        if (fansCounts == -2) {
                            fansCounts = fansCount;
                        }
                        if (status == 0) {
                            textView.setText(getString(R.string.live_attention_ok));
                            textView.setAlpha(0.4f);
                            ++fansCounts;
                            fansView.setText(String.valueOf(fansCounts));
                            userInfoContentLoader.getAddAttention(id);
                            status = 1;
                        } else {
                            textView.setText(getString(R.string.live_attention));
                            textView.setAlpha(1);
                            --fansCounts;
                            fansView.setText(String.valueOf(fansCounts));
                            userInfoContentLoader.getCancelAttention(id);
                            status = 0;
                        }
                        //
                    }
                });
                customLiveUserInfoDialog.setReport(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                    @Override
                    public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {

                        Toast.makeText(LiveActivity.this, "点击了举报", Toast.LENGTH_SHORT).show();
                        // 进入举报界面
                        Intent intent = new Intent(LiveActivity.this, ReportActivity.class);
                        LiveActivity.this.startActivity(intent);

                    }
                });
                customLiveUserInfoDialog.setBanBtn(isMuteds == true ? getString(R.string.live_relieve_ban) : getString(R.string.live_ban), new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                    @Override
                    public void onCustomLiveUserInfoDialogListener(String id, final TextView textView, ImageView managerMark) {
                        userInfoContentLoader.checkUserIdentity(channelId, String.valueOf(id));
                        userInfoContentLoader.setCallBack(new ICallBack() {
                            @Override
                            public void onCheckManager(LiveManagerBean liveManagerBean) {
                                super.onCheckManager(liveManagerBean);
                                if (liveManagerBean.getResult() != 0) {
                                    Toast.makeText(LiveActivity.this, "对方为管理员!", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (isMuteds) {
                                        String messageContent = "解除了" + result1.getNickName() + "的禁言";
                                        LiveMessage liveMessage = new LiveMessage();
                                        liveMessage.setStyle(MessageType.relieveBan);
                                        liveMessage.setDisableSendMsgNickName(result1.getNickName());
                                        liveMessage.setDisableSendMsgUserId(String.valueOf(result1.getId()));
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
                                        textView.setAlpha(1f);
                                        isMuteds = false;
                                    } else {

                                        String messageContent = "禁言了" + result1.getNickName();
                                        LiveMessage liveMessage = new LiveMessage();
                                        liveMessage.setStyle(MessageType.ban);
                                        liveMessage.setDisableSendMsgNickName(result1.getNickName());
                                        liveMessage.setDisableSendMsgUserId(String.valueOf(result1.getId()));
                                        liveMessage.setUserId(userId);
                                        liveMessage.setChannelId(channelId);
                                        liveMessage.setCreatorAccount(creatorAccount);
                                        IMMessage imMessage = SendMessageUtil.sendMessage(container.account, messageContent, roomId, meberAccount, liveMessage);
                                        banListLive.add(meberAccount);
                                        sendMessage(imMessage, MessageType.ban);
                                        textView.setText(getString(R.string.live_relieve_ban));
                                        textView.setAlpha(0.4f);
                                        isMuteds = true;
                                    }
                                }
                            }
                        });

                    }
                });
                customLiveUserInfoDialog.setManagerBtn(isManager == true ? getString(R.string.live_cancel_manager_cancel) : getString(R.string.live_cancel_manager_cancel), new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                    @Override
                    public void onCustomLiveUserInfoDialogListener(final String id, final TextView textView, final ImageView managerMark) {
                        if (managerList != null && managerList.size() >= 3 && !isManager) {
                            final CustomChatDialog customDialog = new CustomChatDialog(getActivity());
                            customDialog.setContent(getString(R.string.live_manager_count));
                            customDialog.setCancelable(false);
                            customDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                                @Override
                                public void onDialogClickListener() {
                                    customDialog.dismiss();
                                }
                            });
                            customDialog.show();

                        } else {
                            if (!isManager) {
                                userInfoContentLoader.liveAccreditManager(channelId, String.valueOf(id));
                                NIMClient.getService(ChatRoomService.class).markChatRoomManager(true, new MemberOption(roomId, meberAccount));
                                String messageContent = "授权" + result1.getNickName() + "为管理员";
                                LiveMessage liveMessage = new LiveMessage();
                                liveMessage.setStyle(MessageType.managerLive);
                                liveMessage.setAdminSendMsgImUserId(result1.getAccId());
                                liveMessage.setAdminSendMsgNickName(result1.getNickName());
                                liveMessage.setAdminSendMsgUserId(String.valueOf(result1.getId()));
                                liveMessage.setUserId(userId);
                                liveMessage.setCreatorAccount(creatorAccount);
                                liveMessage.setChannelId(channelId);
                                IMMessage imMessage = SendMessageUtil.sendMessage(container.account, messageContent, roomId, meberAccount, liveMessage);

                                sendMessage(imMessage, MessageType.managerLive);
                                textView.setText(getString(R.string.live_cancel_manager_cancel));
                                textView.setAlpha(0.4f);
                                managerMark.setVisibility(View.VISIBLE);
                                Toast.makeText(LiveActivity.this, getString(R.string.live_manager_setting_success), Toast.LENGTH_SHORT).show();
                                isManager = true;
                            } else {
                                CustomChatDialog customDialog = new CustomChatDialog(getActivity());
                                customDialog.setContent(getString(R.string.live_setting_manager_confirm));
                                customDialog.setCancelable(false);
                                customDialog.setCancelBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                                    @Override
                                    public void onDialogClickListener() {
                                        userInfoContentLoader.checkUserIdentity(channelId, String.valueOf(id));
                                        NIMClient.getService(ChatRoomService.class).markChatRoomManager(false, new MemberOption(roomId, meberAccount));

                                        String messageContent = "取消了" + result1.getNickName() + "的管理员权限";
                                        LiveMessage liveMessage = new LiveMessage();
                                        liveMessage.setStyle(MessageType.cancel);
                                        liveMessage.setAdminSendMsgImUserId(result1.getAccId());
                                        liveMessage.setAdminSendMsgNickName(result1.getNickName());
                                        liveMessage.setAdminSendMsgUserId(String.valueOf(result1.getId()));
                                        liveMessage.setUserId(userId);
                                        liveMessage.setCreatorAccount(creatorAccount);
                                        liveMessage.setChannelId(channelId);
                                        IMMessage imMessage = SendMessageUtil.sendMessage(container.account, messageContent, roomId, meberAccount, liveMessage);
                                        sendMessage(imMessage, MessageType.cancel);
                                        textView.setText(getString(R.string.lvie_setting_manager));
                                        textView.setAlpha(1);

                                        managerMark.setVisibility(View.GONE);
                                        isManager = false;
                                    }
                                });
                                customDialog.setSurceBtn(getString(R.string.live_not_cancel), null);
                                customDialog.show();
                            }
                        }

                        userInfoContentLoader.setCallBack(new ICallBack() {
                            @Override
                            public void onCheckManager(LiveManagerBean liveManagerBean) {
                                super.onCheckManager(liveManagerBean);
                                int result = liveManagerBean.getResult();
                                AppLog.i("TAG", "查看是否为管理员：" + result);
                                if (result != 0) {
                                    userInfoContentLoader.cancelManagerAccredit(String.valueOf(result));
                                }
                            }


                        });
                    }
                });
            }
            customLiveUserInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    LiveConstant.IDENTITY = LiveConstant.LIVEER_CHECK_ADMIN;
                    LiveConstant.USER_INFO_FIRST_CLICK = true;
                }
            });
            customLiveUserInfoDialog.show();

    }

    private void showCreateLiveRoomPopuwindow() {
        createLiveRoomPopuwindow = new CreateLiveRoomPopuwindow(this);
        createLiveRoomPopuwindow.showCreateLiveRoomPopuwindow();
        createLiveRoomPopuwindow.showAtLocation(this.findViewById(R.id.live_layout),
                Gravity.CENTER, 0, 0);
        createLiveRoomPopuwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isShowPopuTo) {
                    liveContentLoader.cancelLiveRoom(channelId);
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
                } else {
                    roomName = rooName;
                }
                //TODO 进入直播间
                isClickStartLiveBtn = true;
                //开始直播的时间
                startTime = System.currentTimeMillis();
                if (channelId != null) {
                    liveContentLoader.alterLive(roomName, channelId, null, null, null, null);
                } else {
                    finish();
                }
                if (isShareSelector == 0) {
                    liveShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                } else if (isShareSelector == 1) {
                    liveShare(SHARE_MEDIA.SINA);
                } else if (isShareSelector == 2) {
                    liveShare(SHARE_MEDIA.WEIXIN);
                }
            }

            @Override
            public void closeLiveBtn() {
                if (isStartLive) {
                    finish();
                } else {
                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                    clearChatRoom();
                }
                liveContentLoader.cancelLiveRoom(channelId);
                createLiveRoomPopuwindow.dismiss();
            }
        });
    }


    // 退出聊天室
    private void logoutChatRoom() {
        CustomChatDialog customDialog = new CustomChatDialog(getActivity());
        customDialog.setContent(getString(R.string.finish_confirm));
        customDialog.setCancelable(false);
        customDialog.setCancelBtn(getString(R.string.live_over), new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                //结束直播的时间
                endTime = System.currentTimeMillis();
                isCloseLive = true;
                endLive();

            }
        });
        customDialog.setSurceBtn(getString(R.string.live_continue), null);
        customDialog.show();
    }


    private void endLive() {
        AppLog.i("TAG","主播端走了，endLive");
        liveContentLoader.cancelLiveRoom(channelId);
        LiveMessage liveMessage = new LiveMessage();
        liveMessage.setStyle(MessageType.leaveLive);
        liveMessage.setCreatorAccount(creatorAccount);
        liveMessage.setUserId(userId);
        IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "结束直播了哈哈哈哈哈哈", roomId, AuthPreferences.getUserAccount(), liveMessage);
        sendMessage(imMessage, MessageType.leaveLive);
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
            AppLog.i("TAG", "结束时间：" + hms + "    " + (endTime - startTime));
            overTime.setText(hms);
            aucienceCount.setText(String.valueOf(maxOnLineUserCount));
            liveFinishLayout.setVisibility(View.VISIBLE);
            blurImageView.setBlurImageURL(avatar);
            blurImageView.setScaleRatio(20);
            blurImageView.setBlurRadius(1);

        } else {
            finish();
        }
    }

    protected void registerObservers(boolean register) {
        super.registerObservers(register);
    }

    @Override
    protected void checkNetInfo(String netType, int reminder) {
        if ("rests".equals(netType) && reminder == 0) {
            LiveConstant.NET_CHECK = 1;
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


    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_DUAL_STREAM_BROADCASTER;
    }

    boolean isCloseLive = false;//结束直播状态

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        AppLog.i("TAG", "直播端接受到一帧视频");
    }

    private int lastOnlineCounts=0;
    private  Handler handlerLine=new Handler();
    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            handlerLine.removeCallbacks(this);
            if (!isCloseLive&&DemoCache.getLoginChatRoomStatus()) {
                AppLog.i("TAG", "上传在线人数：" + onlineCounts);
                if (onlineCounts > 0 && liveContentLoader != null&&lastOnlineCounts!=onlineCounts) {
                    lastOnlineCounts=onlineCounts;
                    liveContentLoader.getUserOnLine(userOnLineCountParameter, onlineCounts);
                }
            }
            handlerLine.postDelayed(this, 2000);
        }

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
              //  sendLiveStatusMessgae(CustomRemoteExtensionStyle.START_LIVE_STYLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(liveSettingLayout.getWindowToken(), 0);
                isEnterRoom = true;
                if (createLiveRoomPopuwindow != null) {
                    createLiveRoomPopuwindow.dismiss();
                    if(messageListPanel!=null){
                        messageListPanel.setHeaderViewVisible();
                    }
                }
                registerObservers(true);
                modelLayout.setVisibility(View.VISIBLE);
                scoreLayout.setVisibility(View.VISIBLE);
                liveSettingLayout.setVisibility(View.VISIBLE);
                liveSettingLayout.setClickable(true);
                userOnLineCountParameter = channelId + "/onlineUsers";
                //上传在线人数
                AppLog.i("TAG","调用了上传直播间任务的的接口");
                //handlerLine.postDelayed(new MyRunnable(),2000);
            }
        });

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
    public void onError(int err) {
        AppLog.i("TAG", "直播发生错误:" + err);
        if (err == 1003) {
            CommonUtil.RESULT_DIALOG = 2;
            finish();
        } else if (err == 1018) {
            CommonUtil.RESULT_DIALOG = 3;
            finish();
        }
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
    protected void initUIandEvent() {
        super.initUIandEvent();

        if (CommonUtil.REMIND_BACK == 1) {
            liveContentLoader.alterLive(createNickName, channelId, null, null, null, null);
            CommonUtil.REMIND_BACK = -1;
        }
    }

    private void  startLive() {
        int cRole = Constants.CLIENT_ROLE_DUAL_STREAM_BROADCASTER;
        doConfigEngine(cRole);
        if (isBroadcaster(cRole)) {

            //  主播播放器：1725    oldBottom:894
            final SurfaceView surfaceView = RtcEngine.CreateRendererView(getApplicationContext());

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
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(this.getWindowManager().getDefaultDisplay().getWidth(), this.getWindowManager().getDefaultDisplay().getHeight());
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.bottomMargin = 0;
            palyerLayout.addView(surfaceView, lp);
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
    private void liveShare(SHARE_MEDIA share_media) {
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
            liveContentLoader.getShareStatistics(String.valueOf(shareVO.getTargetType()), String.valueOf(shareVO.getTargetId()),share_media.equals(SHARE_MEDIA.SINA)?"2":(share_media.equals(SHARE_MEDIA.WEIXIN)?"1":"0"));
            if(share_media.equals(SHARE_MEDIA.SINA)){
                Toast.makeText(LiveActivity.this,"微博分享成功!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN)){
                Toast.makeText(LiveActivity.this,"微信分享成功!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
                Toast.makeText(LiveActivity.this,"微信朋友圈分享成功!",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if(share_media.equals(SHARE_MEDIA.SINA)){
                Toast.makeText(LiveActivity.this,"微博分享失败!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN)){
                Toast.makeText(LiveActivity.this,"微信分享失败!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
                Toast.makeText(LiveActivity.this,"微信朋友圈分享失败!",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            if(share_media.equals(SHARE_MEDIA.SINA)){
                Toast.makeText(LiveActivity.this,"已取消微博分享!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN)){
                Toast.makeText(LiveActivity.this,"已取消微信分享!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
                Toast.makeText(LiveActivity.this,"已取消微信朋友圈分享!",Toast.LENGTH_SHORT).show();
            }
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
        AppLog.i("TAG", "LiveActivity:onResume");

    }

    protected void onPause() {
        super.onPause();
        AppLog.i("TAG", "LiveActivity:onPause");

    }

    @Override
    protected void onDestroy() {
        AppLog.i("TAG", "直播端走了onDestroy");
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