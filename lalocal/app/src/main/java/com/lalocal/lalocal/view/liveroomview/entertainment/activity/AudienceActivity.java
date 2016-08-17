package com.lalocal.lalocal.view.liveroomview.entertainment.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.liveroomview.entertainment.adapter.GiftAdapter;
import com.lalocal.lalocal.view.liveroomview.entertainment.constant.GiftType;
import com.lalocal.lalocal.view.liveroomview.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.view.liveroomview.entertainment.module.GiftAttachment;
import com.lalocal.lalocal.view.liveroomview.im.ui.dialog.EasyAlertDialogHelper;
import com.lalocal.lalocal.view.liveroomview.permission.MPermission;
import com.lalocal.lalocal.view.liveroomview.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.view.liveroomview.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.view.liveroomview.thirdparty.video.NEVideoView;
import com.lalocal.lalocal.view.liveroomview.thirdparty.video.VideoPlayer;
import com.lalocal.lalocal.view.liveroomview.thirdparty.video.constant.VideoConstant;
import com.lalocal.lalocal.view.xlistview.PowerImageView;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 观众端
 * Created by hzxuwen on 2016/3/18.
 */
public class AudienceActivity extends LivePlayerBaseActivity implements VideoPlayer.VideoPlayerProxy ,View.OnLayoutChangeListener{
    private static final String TAG = AudienceActivity.class.getSimpleName();
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    private final static String EXTRA_URL = "EXTRA_URL";
    public static final String  AVATAR_AUDIENCE ="AVATAR";
    public static final String NICK_NAME_AUDIENCE="NICK_NAME";
    public static final String USER = "user";
    public static final String LIVE_USER_ID="LIVE_USER_ID";
    public static final String PLAYER_TYPE="PLAYER_TYPE";
    public static final String ANNOUCEMENT="ANNOUCEMENT";

    // view

    private ImageButton shareBtn;
    private ImageButton likeBtn;

    private Button sendGiftBtn;

    // 播放器
    private VideoPlayer videoPlayer;
    // 发送礼物频率控制使用
    private long lastClickTime = 0;
    // 选中的礼物
    private int giftPosition = -1;

    // state
    private boolean isStartLive = false; // 推流是否开始
    private ImageView clickPraise;
    private ImageView quit;


    private View liveSettingLayout;
    private LinearLayout keyboardLayout;

    protected View viewById;
    private int screenHeight;
    private int keyHeight;

    private String nickname;
    private String playType;
    private View loadingPage;
    private RelativeLayout mBlurDrawableRelativeLayout1;
    private TextView send;
    private PowerImageView loadingPageLayout;
    private TextView andiuence;
    public String annoucement;//公告


    public static void start(Context context, String roomId, String url, String avatar, String nickName, String userId, SpecialShareVOBean shareVO, String type,String annoucement) {
        Intent intent = new Intent();
        intent.setClass(context, AudienceActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(AVATAR_AUDIENCE,avatar);
        intent.putExtra(NICK_NAME_AUDIENCE,nickName);
        intent.putExtra(ANNOUCEMENT,annoucement);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(PLAYER_TYPE,type);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("shareVO",shareVO);
        intent.putExtra(LIVE_USER_ID,userId);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAudienceParam();
        parseIntent();
        initView();
        enterRoom();
        registerObservers(true);
    }

    protected  void parseIntent(){
        super.parseIntent();
        nickname = getIntent().getStringExtra(NICK_NAME_AUDIENCE);
        avatar = getIntent().getStringExtra(AVATAR_AUDIENCE);
        playType = getIntent().getStringExtra(PLAYER_TYPE);
        annoucement = getIntent().getStringExtra(ANNOUCEMENT);
    }

    private void initView() {
        viewById = findViewById(R.id.live_layout);
        loadingPage = findViewById(R.id.live_loading_page);
        andiuence = (TextView) loadingPage.findViewById(R.id.audience_over_layout);
        loadingPageLayout = (PowerImageView) loadingPage.findViewById(R.id.xlistview_header_anim);
        ImageView imageView= (ImageView) findViewById(R.id.loading_page_bg);

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight /3;
        DrawableUtils.displayImg(AudienceActivity.this,imageView,avatar);
        mBlurDrawableRelativeLayout1 = (RelativeLayout) this.findViewById(R.id.blur_drawable_container);
        mBlurDrawableRelativeLayout1.setBackgroundColor(Color.parseColor("#a5ffffff"));
    }
    protected  void enterRoom(){
        super.enterRoom();
    }
    protected  void registerObservers(boolean register){
        super.registerObservers(register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
    }
    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {

        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            IMMessage message = messages.get(0);

            if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
                // 通知类消息
                ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();

                switch (notificationAttachment.getType()) {
                    case ChatRoomClose:
                        //直播间被关闭；
                        showFinishLayout();
                        AppLog.i("TAG","直播间被关闭");
                        break;
                }
            }
        }
    };
    @Override
    protected int getActivityLayout() {
        return R.layout.audience_activity;
    }

    @Override
    protected int getLayoutId() {
        return R.id.live_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewById.addOnLayoutChangeListener(this);
        // 恢复播放
        if(videoPlayer != null) {
            videoPlayer.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 暂停播放
        if(videoPlayer != null) {
            videoPlayer.onActivityPause();
        }
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        if (videoPlayer != null) {
            videoPlayer.resetVideo();
            AppLog.i("TAG","释放播放器资源");
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishLive();
    }



    // 离开聊天室
    private void logoutChatRoom() {
        EasyAlertDialogHelper.createOkCancelDiolag(this, null, getString(R.string.finish_confirm),
                getString(R.string.confirm), getString(R.string.cancel), true,
                new EasyAlertDialogHelper.OnDialogActionListener() {
                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                        clearChatRoom();
                    }
                }).show();
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

    /******************************** 初始化 *******************************/

    // 初始化播放器参数
    protected void initParam() {
        requestBasicPermission(); // 申请APP基本权限
    }


    int bufferStrategy=-1;
    String mediaType;
    private void initAudienceParam() {
        NEVideoView videoView = findView(R.id.video_view);
        LinearLayout videoLayout= (LinearLayout) findViewById(R.id.video_layout);
        if("1".equals(playType)){
            bufferStrategy=1;
            videoView.setBufferStrategy(bufferStrategy);
            mediaType="videoondemand";
            videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                    bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FIT,mediaType);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(videoView.getLayoutParams());
            lp.gravity= Gravity.CENTER;
            videoLayout.setBackgroundColor(Color.BLACK);
            videoView.setLayoutParams(lp);
        }else {
            bufferStrategy=0;
            mediaType="livestream";
            videoView.setBufferStrategy(bufferStrategy);
            videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                    bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FILL_SCALE,mediaType);
        }
        videoPlayer.openVideo();

        videoView.setOnErrorListener(new NELivePlayer.OnErrorListener() {
            @Override
            public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
                AppLog.i("TAG","NEVideoView:ErrorListener"+i+"   extra :"+i1);
                Toast.makeText(AudienceActivity.this,"无法播放此视频",Toast.LENGTH_SHORT).show();
                finishLive();
                return false;
            }
        });
        videoView.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer neLivePlayer) {
                loadingPage.setVisibility(View.GONE);
                AppLog.i("TAG","NEVideoView:PreparedListener视频预处理完成后");
            }
        });

    }
    protected void findViews() {
        super.findViews();
        controlLayout = findView(R.id.control_layout);
        shareBtn = findView(R.id.share_btn);
        shareBtn.setVisibility(View.GONE);
        likeBtn = findView(R.id.like_btn);
        clickPraise = (ImageView) findViewById(R.id.live_telecast_like);
        quit = (ImageView) findViewById(R.id.live_telecast_quit);
        likeBtn.setVisibility(View.GONE);
        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.VISIBLE);
        liveSettingLayout.setClickable(true);
        ImageView settingBtn= (ImageView) findViewById(R.id.live_telecast_setting);
        settingBtn.setVisibility(View.GONE);
        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);

        keyboardLayout.setAlpha(0);
        keyboardLayout.setFocusable(false);
        keyboardLayout.setClickable(false);
        shareBtn.setOnClickListener(buttonClickListener);
        giftBtn.setOnClickListener(buttonClickListener);
        likeBtn.setOnClickListener(buttonClickListener);
        clickPraise.setOnClickListener(buttonClickListener);
        quit.setOnClickListener(buttonClickListener);
        inputChar.setOnClickListener(buttonClickListener);

    }

    // 初始化礼物布局
    protected void findGiftLayout() {
        super.findGiftLayout();
        sendGiftBtn = findView(R.id.send_gift_btn);
        sendGiftBtn.setOnClickListener(buttonClickListener);

        adapter = new GiftAdapter(this);
        giftView.setAdapter(adapter);

        giftLayout.setOnClickListener(buttonClickListener);
        giftView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                giftPosition = position;
            }
        });

    }

    protected  void updateUI(String nick){
        super.updateUI( nick);
        DrawableUtils.displayImg(this, maseterHead,avatar);
        masterName.setText(nickname);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.share_btn:
                    break;
                case R.id.gift_btn:
                    showGiftLayout();
                    break;
                case R.id.like_btn:

                    break;
                case R.id.gift_layout:
                    giftLayout.setVisibility(View.GONE);
                    giftPosition = -1;
                    break;
                case R.id.send_gift_btn:
                    sendGift();
                    break;
                case R.id.live_telecast_like:
                    int[] locations = new int[2];
                    v.getLocationOnScreen(locations);
                    int x = locations[0];//获取组件当前位置的横坐标
                    int y = locations[1];
                    int i = DensityUtil.px2dip(AudienceActivity.this, x);
                    AppLog.i("TAG","likeLocation:"+i+"    Y:"+y);

                    periscopeLayout.addHeart();
                    sendLike();
                    break;
                case R.id.live_telecast_quit:
                    finishLive();
                    break;
                case R.id.live_telecast_input_text:
                    keyboardLayout.setAlpha(1.0f);
                    keyboardLayout.setClickable(true);
                    liveSettingLayout.setVisibility(View.GONE);
                    inputPanel.switchToTextLayout(true);
                    break;
            }
        }
    };

    private void finishLive() {
        if (isStartLive) {
            logoutChatRoom();
        } else {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }
    }

    /**
     * 基本权限管理
     */
    private void requestBasicPermission() {
        MPermission.with(AudienceActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.READ_PHONE_STATE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess(){
        //    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        initAudienceParam();

    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed(){
        //    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
        finish();

    }

    /************************* 点赞爱心 ********************************/

    // 发送点赞爱心
    public void sendLike() {
        if (!isFastClick()) {

            IMMessage likeMessage= ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, "给主播点了个赞");
            sendMessage(likeMessage,"2");

        }
    }


    // 发送爱心频率控制
    private boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        long time = currentTime - lastClickTime;
        if (time > 0 && time < 10000) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }

    /*********************** 收发礼物 ******************************/

    // 显示礼物列表
    private void showGiftLayout() {
        giftLayout.setVisibility(View.VISIBLE);
        inputPanel.collapse(true);
    }

    // 发送礼物
    private void sendGift() {
        if (giftPosition == -1) {
            //  Toast.makeText(AudienceActivity.this, "请选择礼物", Toast.LENGTH_SHORT).show();
            return;
        }
        giftLayout.setVisibility(View.GONE);
        GiftAttachment attachment = new GiftAttachment(GiftType.typeOfValue(giftPosition), 1);
        ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(roomId, attachment);
        // setMemberType(message,);
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false);

        giftAnimation.showGiftAnimation(message);
        giftPosition = -1; // 发送完毕，置空
    }

    private void setMemberType(ChatRoomMessage message, String type) {
        Map<String, Object> ext = new HashMap<>();
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, UserHelper.getImccId(AudienceActivity.this));
        if (chatRoomMember != null) {
            // ext.put("type", type);
            ext.put("type", chatRoomMember.getMemberType().getValue());
            message.setRemoteExtension(ext);
        }
    }

    @Override
    public boolean isDisconnected() {
        return false;
    }

    /**************************** 播放器状态回调 *****************************/

    @Override
    public void onError() {

        showFinishLayout();
    }

    @Override
    public void onCompletion() {
        isStartLive = false;
        showFinishLayout();

    }

    @Override
    public void onPrepared() {
        isStartLive = true;

    }

    // 显示直播已结束布局
    private void showFinishLayout() {
        loadingPageLayout.setVisibility(View.GONE);
        loadingPage.setVisibility(View.VISIBLE);
        andiuence.setVisibility(View.VISIBLE);
        inputPanel.collapse(true);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            if(keyboardLayout!=null){
                keyboardLayout.setAlpha(0);
                keyboardLayout.setClickable(false);
                liveSettingLayout.setVisibility(View.VISIBLE);
                liveSettingLayout.setClickable(true);
            }

        }
    }

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }
}