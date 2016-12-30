package com.lalocal.lalocal.live.entertainment.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
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
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.ui.CustomChallengeListviewDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomNewUserInforDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomToast;
import com.lalocal.lalocal.live.entertainment.ui.MasterMoreSettingPopuWindow;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
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
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.LogFileUtils;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.dialog.PhotoSelectDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LiveActivity extends LivePlayerBaseActivity implements LivePlayer.ActivityProxy, View.OnLayoutChangeListener, PhotoSelectDialog.OnDialogClickListener, CustomTitleView.onBackBtnClickListener {
    private static final String TAG = "LiveActivity";
    public static final int LOCATION_REQUEST_CODE=500;
    private int screenHeight;
    private int keyHeight;
    protected String channelId;

    private String roomName;//直播室名字
    boolean isEnterRoom = false;
    private boolean disconnected = false; // 是否断网（断网重连用）
    private boolean isStartLive = false; // 是否开始直播推流
    boolean isClickStartLiveBtn = false; //设置直播title的popuwindow
    private View backBtn;
    private int getImageTokenTag = -1;

    private View liveSettingLayout;//直播间底部设置栏
    protected View viewById;
    private TextView aucienceCount;
    private TextView overTime;
    private ImageView liveQuit;
    private ImageView switchBtn;
    private RelativeLayout modelLayout;//用户信息layout
    private LinearLayout keyboardLayout;//自定义键盘输入
    private ViewGroup liveFinishLayout;

    // data
    private ContentLoader liveContentLoader;
    private SpecialShareVOBean shareVO;

    private String roomId;
    private String cname;
    private long startTime;
    private long endTime;
    private TextView overMoney;

    private LiveCallBack liveCallBack;
    private ImageView challengeNewTask;

    private RelativeLayout nofitifationLayout;
    private ImageView netWorkHint;

    private RelativeLayout createLiveLayout;
    private ImageView liveCreateRoomCloseIv;
    private TextView inputStartLive;

    private EditText liveRoomName;
    private ImageView createLiveHeadIv;
    private TextView createLiveLocationTv;


    private TextView liveTextTitleCount;
    private TextView startLiveCountDownTV;

    private ImageView closeLiveIv;
    private int roomNameLength;
    private LinearLayout startLiveBegin;
    private UploadManager uploadManager;
    private int liveNumber;
    private String logTime;
    private ImgTokenResult imgToken;
    private TextView definitionChoose;
    private LinearLayout chooseLayout;
    private TextView chooseUp;
    private TextView chooseDown;

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

    private byte[] bytesImg;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LOCATION_REQUEST_CODE&&resultCode== KeyParams.LOCATION_RESULTCODE){
           // String locationInfo = data.getStringExtra(KeyParams.POST_GET_LOCATION);
            AppLog.i("TAG","獲取返回地理位置:"+CommonUtil.LOCATION_RESULT+"    "+(CommonUtil.LOCATION_Y==true?"有":"无"));
            if(createLiveLocationTv!=null){
                createLiveLocationTv.setText(CommonUtil.LOCATION_RESULT);
                if (roomNameLength > 0 && CommonUtil.LOCATION_Y == true) {
                    inputStartLive.setTextColor(getResources().getColor(R.color.live_start_tv));
                } else {
                    inputStartLive.setTextColor(getResources().getColor(R.color.live_start_nomal_tv));
                }
            }

        }

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                crop(Uri.fromFile(tempFile));
            } else {
                CommonUtil.showToast(this, "未找到存储卡，无法存储照片", Toast.LENGTH_SHORT);
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                try {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap != null) {
                        createLiveHeadIv.setImageBitmap(bitmap);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        bytesImg = bos.toByteArray();
                        bos.flush();
                        bos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //判断软键盘显示与隐藏
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//隐藏
            if (keyboardLayout != null && isEnterRoom) {
                keyboardLayout.setVisibility(View.GONE);
                if (nofitifationLayout != null) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    lp.height = DensityUtil.dip2px(this, 30);
                    nofitifationLayout.setLayoutParams(lp);
                }
                topView.setVisibility(View.VISIBLE);

            }
        } else if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {//显示
            topView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liveContentLoader = new ContentLoader(this);
        liveCallBack = new LiveCallBack();
        liveContentLoader.setCallBack(liveCallBack);
        viewById = findViewById(R.id.live_layout);

        //七牛云api
        uploadManager = new UploadManager();
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        setListener();
        LiveConstant.ROLE = 1;
        if (CommonUtil.REMIND_BACK != 1) {
            initCreateLiveLayout();
        } else {
            if (TextUtils.isEmpty(roomName)) {
                roomName = UserHelper.getUserName(this);
            }
            liveContentLoader.alterLive(roomName, null, CommonUtil.LOCATION_RESULT);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @OnMPermissionGranted(PERMISSION_STGAT_CODE)
    public void onPermissionGranted() {
        photoGraph();
    }

    @OnMPermissionDenied(PERMISSION_STGAT_CODE)
    public void onPermissionDenied() {
        Toast.makeText(this, "权限被拒绝，无法继续往下执行", Toast.LENGTH_SHORT).show();

    }
    private File tempFile;
    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;

    private void photoGraph() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            tempFile = new File(Environment
                    .getExternalStorageDirectory(),
                    "header.jpg");
            Uri uri = Uri.fromFile(tempFile);
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent,
                    PHOTO_REQUEST_CAREMA);
        } else {
            CommonUtil.showToast(this, "未找到存储卡，无法存储照片",
                    Toast.LENGTH_SHORT);
        }
    }


    protected void findViews() {
        super.findViews();
        backBtn = findView(R.id.BackBtn);
        modelLayout = (RelativeLayout) findViewById(R.id.live_view_top_layout);
        startLiveBegin = (LinearLayout) findViewById(R.id.start_live_begin);
        keyboardLayout = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);
        switchBtn = (ImageView) findViewById(R.id.live_telecast_setting);
        liveGiftImg.setVisibility(View.GONE);
        liveSettingLayout = findViewById(R.id.setting_bottom_layout);
        liveSettingLayout.setVisibility(View.GONE);
        liveQuit = (ImageView) findViewById(R.id.live_quit);

        backBtn.setVisibility(View.GONE);
        modelLayout.setVisibility(View.GONE);
        keyboardLayout.setVisibility(View.GONE);

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
            if (LiveConstant.isUnDestory && firstWarning) {
                firstWarning = false;
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
        createLiveLayout = (RelativeLayout) findViewById(R.id.create_live_layout);
        createLiveLayout.setVisibility(View.VISIBLE);
        liveCreateRoomCloseIv = (ImageView) createLiveLayout.findViewById(R.id.live_create_room_close_iv);
        inputStartLive = (TextView) createLiveLayout.findViewById(R.id.input_start_live);
        liveRoomName = (EditText) createLiveLayout.findViewById(R.id.live_room_name);
        createLiveLocationTv = (TextView) createLiveLayout.findViewById(R.id.create_live_location_tv);
        createLiveHeadIv = (ImageView) createLiveLayout.findViewById(R.id.create_live_head_iv);
        definitionChoose = (TextView) createLiveLayout.findViewById(R.id.live_definition_choose);
        chooseLayout = (LinearLayout) createLiveLayout.findViewById(R.id.live_definition_choose_layout);
        chooseUp = (TextView) createLiveLayout.findViewById(R.id.live_definition_choose_up);
        chooseDown = (TextView) createLiveLayout.findViewById(R.id.live_definition_choose_down);
        liveTextTitleCount = (TextView) createLiveLayout.findViewById(R.id.live_text_title_count);
        View layoutBg = createLiveLayout.findViewById(R.id.create_layout_bg);
        createLiveLocationTv.setText(CommonUtil.LOCATION_RESULT);
        createLiveLocationTv.setOnClickListener(buttonClickListener);
        liveCreateRoomCloseIv.setOnClickListener(buttonClickListener);
        inputStartLive.setOnClickListener(buttonClickListener);
        createLiveHeadIv.setOnClickListener(buttonClickListener);
        definitionChoose.setOnClickListener(buttonClickListener);
        chooseUp.setOnClickListener(buttonClickListener);
        chooseDown.setOnClickListener(buttonClickListener);
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

        layoutBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(chooseLayout!=null&&chooseLayout.getVisibility()==View.VISIBLE){
                    chooseLayout.setVisibility(View.GONE);
                    liveRoomName.setVisibility(View.VISIBLE);
                }
                return false;
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
            String liveRoomNameCount = liveRoomName.getText().toString().trim();
            roomNameLength = liveRoomNameCount.length();
            AppLog.i("TAG","是否获取地理位置:"+(CommonUtil.LOCATION_Y==true?"是":"foe"));
            if (roomNameLength > 0 && CommonUtil.LOCATION_Y == true) {
                inputStartLive.setTextColor(getResources().getColor(R.color.live_start_tv));
            } else {
                inputStartLive.setTextColor(getResources().getColor(R.color.live_start_nomal_tv));
            }
            liveTextTitleCount.setText(roomNameLength + "/20");
        }
    };

    @Override
    protected void showUserInfoDialog(String userId, final String channelId, boolean isMaster) {
        if (LiveConstant.isUnDestory) {
            CustomNewUserInforDialog  dialog= new CustomNewUserInforDialog(this, container, userId, channelId, LiveConstant.ROLE, isMaster, creatorAccount, roomId);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    LiveConstant.USER_INFO_FIRST_CLICK = true;
                }
            });
            dialog.setDialogStatusListener(this);
            dialog.show();

        }
    }

    protected void showInputTextView() {
        keyboardLayout.setVisibility(View.VISIBLE);
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
                        MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_ANCHOR_CAMERA);
                        worker().enablePreProcessor();//美颜开闭切换
                        worker().getRtcEngine().switchCamera();
                        break;
                    case R.id.live_beauty_switch:
                        if (Constant.PRP_ENABLED) {
                            Constant.PRP_ENABLED = false;
                            ((TextView) view).setText(R.string.live_beauty_off);
                            MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_ANCHOR_BEAUTY_ON);
                        } else {
                            Constant.PRP_ENABLED = true;
                            ((TextView) view).setText(R.string.live_beauty_on);
                            MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_ANCHOR_BEAUTY_OFF);
                            ((TextView) view).setTextColor(getResources().getColor(R.color.live_beauty_on));
                        }

                        worker().enablePreProcessor();//美颜开闭切换

                        break;
                    case R.id.live_share_pop:
                        MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_USER_SHARE);
                        showSharePopuwindow(shareVO, getString(R.string.share_live));
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


    //烧鸡管理员关闭直播间
    @Override
    protected void closeLiveNotifi(IMMessage message) {
        endLive();
        try {
            if (LiveConstant.isUnDestory&&firstWarning) {
                firstWarning = false;
                final CustomChatDialog customDialog = new CustomChatDialog(LiveActivity.this);
                customDialog.setContent(message.getContent());
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


    @Override
    protected void masterOnLineStatus(boolean b) {

    }

    @Override
    protected void showFinishLayout(boolean b, int i) {
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
                    MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_LOCACTION_BUTTON);
                    Intent intent = new Intent(LiveActivity.this, LiveLocationActivity.class);
                    startActivityForResult(intent,LOCATION_REQUEST_CODE);
                    break;
                case R.id.live_create_room_close_iv:
                    MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_CANCEL);
                    if (isStartLive) {
                        finish();
                    } else {
                        if (DemoCache.getLoginChatRoomStatus()) {
                            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
                        }
                        clearChatRoom();
                    }
                    if (channelId != null) {
                        liveContentLoader.cancelLiveRoom(channelId);
                    }
                    break;
                case R.id.input_start_live:
                    startLiveing();
                    break;
                case R.id.live_over_close:
                    finish();
                    break;
                case R.id.create_live_head_iv:
                    PhotoSelectDialog dialog = new PhotoSelectDialog(LiveActivity.this);
                    dialog.setButtonClickListener(LiveActivity.this);
                    dialog.show();
                    break;
                case R.id.live_definition_choose:
                    chooseLayout.setVisibility(View.VISIBLE);
                    liveRoomName.setVisibility(View.GONE);
                    break;
                case R.id.live_definition_choose_up:
                    definitionChoose.setText("网络: 高清");
                    LiveConstant.LIVE_DEFINITION=1;
                    chooseLayout.setVisibility(View.GONE);
                    liveRoomName.setVisibility(View.VISIBLE);
                    break;
                case R.id.live_definition_choose_down:
                    Toast.makeText(LiveActivity.this,"点击了流畅",Toast.LENGTH_SHORT).show();
                    definitionChoose.setText("网络: 流畅");
                    chooseLayout.setVisibility(View.GONE);
                    liveRoomName.setVisibility(View.VISIBLE);
                    LiveConstant.LIVE_DEFINITION=2;
                    break;
            }
        }
    };

    private void startLiveing() {
        MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_START_START);
        if (isFirstClick) {
            isFirstClick = false;
            roomName = liveRoomName.getText().toString().trim();
            if (TextUtils.isEmpty(roomName)) {
                isFirstClick = true;
                Toast.makeText(LiveActivity.this, getString(R.string.live_room_title_no_null), Toast.LENGTH_SHORT).show();
                return;
            }
            if (CommonUtil.LOCATION_Y==false) {
                isFirstClick = true;
                Toast.makeText(LiveActivity.this, getString(R.string.not_get_location_please_input), Toast.LENGTH_SHORT).show();
                return;
            }
            inputStartLive.setText(getString(R.string.prepare_start_live));
            isClickStartLiveBtn = true;
            startTime = System.currentTimeMillis();
            if(getImageTokenTag==-1){
                liveContentLoader.alterLive(roomName, null, CommonUtil.LOCATION_RESULT);
            }else if(getImageTokenTag == 1){
                liveContentLoader.getImgToken();
            }
        }
    }

    public static final String CREATE_ROOMID = "createRoomId";
    boolean firstWarning = true;



    @Override
    public void onButtonClickListner(Dialog dialog, View view) {
        dialog.dismiss();
        int id = view.getId();
        switch (id) {
            case R.id.photoalbum_btn://相册
                getImageTokenTag=1;
                searchPhotoBum();
                break;
            case R.id.photograph_btn:
                getImageTokenTag=1;
                requestUserPermission(Manifest.permission.CAMERA);
                break;
            case R.id.cancel_selectephoto_btn:
                getImageTokenTag=-1;
                break;
        }


    }

    private void searchPhotoBum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,
                PHOTO_REQUEST_GALLERY);

    }

    @Override
    public void onBackClick() {

    }

    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    public class LiveCallBack extends ICallBack {

        @Override
        public void onResponseLog(String json) {
            super.onResponseLog(json);
            AppLog.i("TAG", "获取日志信息：" + json);
        }


        @Override
        public void onAlterLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {//修改直播间
            super.onAlterLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                LiveRowsBean liveRowsBean = createLiveRoomDataResp.getResult();
                roomId = String.valueOf(liveRowsBean.getRoomId());
                LiveConstant.ROOM_ID = roomId;
                SPCUtils.put(getActivity(), CREATE_ROOMID, String.valueOf(roomId));
                avatar = liveRowsBean.getUser().getAvatar();
                roomId = String.valueOf(liveRowsBean.getRoomId());
                channelId = String.valueOf(liveRowsBean.getId());
                cname = liveRowsBean.getCname();
                shareVO = liveRowsBean.getShareVO();

                liveNumber = createLiveRoomDataResp.getResult().getNumber();
                userId = String.valueOf(liveRowsBean.getUser().getId());
                getParameter(liveRowsBean);
                CommonUtil.REMIND_BACK = 0;
                logFile();
                startLive();
                AppLog.i("TAG", "创建直播成功:" + cname);
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
                imgToken = imgTokenBean.getResult();
                String token = imgToken.getToken();
                String filename = imgToken.getFilename();
                if (getImageTokenTag == 1) {
                    upLoadQiNiu(bytesImg, filename, token);
                } else if (getImageTokenTag == 2) {
                    uploadManager.put(Environment.getExternalStorageDirectory() + "/" + LogFileUtils.fileAgoraPath + logTime, LogFileUtils.fileAgoraPath + logTime, token, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            boolean ok = info.isOK();
                            //删除本地日志
                            deleteFile(Environment.getExternalStorageDirectory() + "/" + LogFileUtils.fileAgoraPath + logTime);

                        }
                    }, null);
                }
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
                Toast.makeText(LiveActivity.this, getString(R.string.him_is_manager), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onChallengeList(String json) {
            super.onChallengeDetails(json);
            final ChallengeDetailsResp challengeDetailsResp = new Gson().fromJson(json, ChallengeDetailsResp.class);
            if (challengeDetailsResp.getReturnCode() == 0) {
                //TODO  进入挑战列表
                List<ChallengeDetailsResp.ResultBean> result = challengeDetailsResp.getResult();
                if (LiveConstant.isUnDestory) {
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

    private void upLoadQiNiu(final byte[] bytesImg, final String filename, final String token) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(bytesImg, filename, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.statusCode == 200) {
                            liveContentLoader.alterLive(roomName, filename, CommonUtil.LOCATION_RESULT);
                        } else {
                            liveContentLoader.alterLive(roomName, null,CommonUtil.LOCATION_RESULT);
                        }
                    }
                }, null);

    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
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
        if (LiveConstant.isUnDestory) {
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
        if (LiveConstant.isUnDestory) {
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
                    getImageTokenTag = 2;//上传日志
                    liveContentLoader.getImgToken();
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
        if(channelId!=null){
            liveContentLoader.cancelLiveRoom(channelId);
        }
        LiveMessage liveMessage = new LiveMessage();
        liveMessage.setStyle(MessageType.leaveLive);
        liveMessage.setCreatorAccount(creatorAccount);
        liveMessage.setUserId(userId);
        if (container != null && container.account != null) {
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, "结束直播", roomId, AuthPreferences.getUserAccount(), liveMessage);
            sendMessage(imMessage, MessageType.leaveLive);
        }
        if (isLeaveChannel) {
            deInitUIandEvent();
            isLeaveChannel = false;
        }
        if (isClickStartLiveBtn) {
            isStartLive = false;
            drawerLayout.closeDrawer(Gravity.RIGHT);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            if (inputPanel != null) {
                inputPanel.collapse(true);// 收起软键盘
            }
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            String hms = formatter.format(endTime - startTime);
            overTime.setText(hms);
            aucienceCount.setText(String.valueOf(onlineCounts));
            liveFinishLayout.setVisibility(View.VISIBLE);

        } else {
            finish();
        }
    }

    @Override
    protected void checkNetInfo(String netType, int reminder) {
        if ("rests".equals(netType) && reminder == 0) {
            LiveConstant.NET_CHECK = 1;
            if (LiveConstant.isUnDestory) {
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


    public static final int SHARE_CODE = 0x00;

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isStartLive = true;
                if (liveRoomName != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(liveRoomName.getWindowToken(), 0);
                }
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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(SHARE_CODE);
                    }
                }, 60000);
            }
        });

    }

    int startTimes = 2;

    private void startLiveCountDown() {
        final CountDownTimer timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startLiveCountDownTV.setText(String.valueOf(startTimes));
                --startTimes;
            }

            @Override
            public void onFinish() {
                startLiveBegin.setVisibility(View.GONE);
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


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHARE_CODE:
                    if (LiveConstant.isUnDestory && liveFinishLayout.getVisibility() != View.VISIBLE) {
                        showSharePopuwindow(shareVO, getString(R.string.share_live));
                    }
                    break;
                case AGAINLOGINIM:
                    againLoginIm();
                    break;


            }
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

    public static final int AGAINLOGINIM = 0x01;

    private void againLoginIm() {
        AppLog.i("TAG", "直播連接中斷连接中断回调");

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
                        handler.sendEmptyMessage(AGAINLOGINIM);
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
                        handler.sendEmptyMessage(AGAINLOGINIM);
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
                    if (LiveConstant.isUnDestory) {
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
                    if (LiveConstant.isUnDestory) {
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
        AppLog.i("TAG", "主播端视频功能停止回调");
    }

    @Override
    public void onLeaveChannel(final IRtcEngineEventHandler.RtcStats stats) {//离开频道的回调

    }

    @Override
    public void onUserEnableVideo(int uid, boolean enabled) {
    }

    @Override
    public void onLastmileQuality(final int quality) {//网络质量检测
        AppLog.i("TAG", "声网sdk网络质量检测" + quality);
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
                        //   netWorkHint.setImageResource(R.drawable.signal_non);
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
            int i = rtcEngine().setLogFile(Environment.getExternalStorageDirectory() + "/" + LogFileUtils.fileAgoraPath + logTime);
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

    public void logFile() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        logTime = formatter.format(curDate) + "_" + channelId + "_" + liveNumber + "_" + UserHelper.getUserId(this) + ".log";
        LogFileUtils.makeAgoraFilePath(logTime);
    }

    private void finishLive() {
        if (isStartLive) {
            logoutChatRoom();
        } else {
            if (DemoCache.getLoginChatRoomStatus()) {
                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            }
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
        //    rtcEngine().enableLastmileTest();//启动网络质量检测
        if (isStartLive) {
            //主播回来了
            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setStyle(MessageType.text);
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, getString(R.string.come_back), roomId, creatorAccount, liveMessage);
            sendMessage(imMessage, MessageType.text);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        DialogUtil.clear();
        rtcEngine().disableLastmileTest();//关闭网络质量检测
        AppLog.i("TAG", "LiveActivity:onStop");
        if (isStartLive) {
            //主播离开了
            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setStyle(MessageType.text);
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, getString(R.string.leave_master), roomId, creatorAccount, liveMessage);
            sendMessage(imMessage, MessageType.text);
        }

    }

    protected void onPause() {
        super.onPause();
        AppLog.i("TAG", "LiveActivity:onPause");
    }

    @Override
    protected void onDestroy() {
        AppLog.i("TAG", "直播端走了onDestroy");


        if (DemoCache.getLoginChatRoomStatus()) {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        }

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