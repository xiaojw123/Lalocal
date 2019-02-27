package com.lalocal.lalocal.live.entertainment.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.MyApplication;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.LiveCache;
import com.lalocal.lalocal.live.base.util.DialogUtil;
import com.lalocal.lalocal.live.base.util.log.LogUtil;
import com.lalocal.lalocal.live.entertainment.agora.Constant;
import com.lalocal.lalocal.live.entertainment.agora.openlive.WorkerThread;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.fragment.AgoraLiveFragment;
import com.lalocal.lalocal.live.entertainment.fragment.CreateLiveFragment;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomNewUserInforDialog;
import com.lalocal.lalocal.live.entertainment.ui.MasterMoreSettingPopuWindow;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * type 0:其他    2:声网
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LiveActivity extends LivePlayerBaseActivity implements View.OnLayoutChangeListener {
    private static final String TAG = "LiveActivity";
    private int screenHeight;
    private int keyHeight;
    protected String channelId;
    private String roomName;//直播室名字
    boolean isEnterRoom = false;
    private boolean disconnected = false; // 是否断网（断网重连用）
    private boolean isStartLive = false; // 是否开始直播推流
    boolean isClickStartLiveBtn = false; //设置直播title的popuwindow
    public static final String CREATE_ROOMID = "createRoomId";
    public static final int AGAINLOGINIM = 0x01;
    public static final int SHARE_CODE = 0x00;
    boolean isCloseLive = false;//结束直播状态
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
    private RelativeLayout nofitifationLayout;
    private ImageView netWorkHint;
    private TextView startLiveCountDownTV;
    private ImageView closeLiveIv;
    private LinearLayout startLiveBegin;
    private UploadManager uploadManager;
    private int liveNumber;
    private String logTime;
    private ImgTokenResult imgToken;
    private CreateLiveFragment createLiveFragment;
    private CustomChatDialog customChatDialog;
    private CustomNewUserInforDialog customNewUserInforDialog;
    private MasterMoreSettingPopuWindow settingPopuWindow;
   // private YaseaFragment yaseaFragment;
    private AgoraLiveFragment agoraLiveFragment;

    @Override
    protected int getActivityLayout() {
        return R.layout.live_player_activity;
    }

    @Override
    protected int getLayoutId() {
        return R.id.live_layout;
    }



    public Activity getActivity() {
        return LiveActivity.this;
    }


    /**
     * 判断软键盘的显示与隐藏
     * @param v
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param oldLeft
     * @param oldTop
     * @param oldRight
     * @param oldBottom
     */
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
        //七牛云api
        uploadManager = new UploadManager();
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        setListener();
        LiveConstant.ROLE = 1;
        if (CommonUtil.REMIND_BACK != 1) {//判断是否需要此案时创建直播间ui
            showCreateLiveFragment();
        } else {
            if (TextUtils.isEmpty(roomName)) {
                roomName = UserHelper.getUserName(this);
            }
            liveContentLoader.alterLive(roomName, null, CommonUtil.LOCATION_RESULT);
        }
    }
    private byte[] bytesImg;

    /**
     * 显示创建直播间的fragment
     */
    private void showCreateLiveFragment() {
        FragmentManager fm = getFragmentManager();
        createLiveFragment = new CreateLiveFragment();
        createLiveFragment.setOnStartLiveClickListener(new CreateLiveFragment.StartLiveClickListener() {
            @Override
            public void startLive(int tag ,byte[] img) {
                getImageTokenTag=tag;
                bytesImg=img;
                isClickStartLiveBtn = true;
                startTime = System.currentTimeMillis();
                if(getImageTokenTag==-1){
                    liveContentLoader.alterLive(roomName, null, CommonUtil.LOCATION_RESULT);
                }else if(getImageTokenTag == 1){
                    liveContentLoader.getImgToken();
                }
            }
        });

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.create_live_fragment_container, createLiveFragment);
        ft.commit();
    }
    protected void findViews() {
        super.findViews();
        backBtn = findViewById(R.id.BackBtn);
        viewById = findViewById(R.id.live_layout);
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
        liveFinishLayout = (ViewGroup) findViewById(R.id.live_finish_layout);
        closeLiveIv = (ImageView) findViewById(R.id.live_over_close);
        closeLiveIv.setOnClickListener(buttonClickListener);
        aucienceCount = (TextView) findViewById(R.id.live_over_audience_count);
        overTime = (TextView) findViewById(R.id.live_over_time_tv);
        viewById.setOnClickListener(buttonClickListener);
        //结束直播
        overMoney = (TextView) findViewById(R.id.live_over_money);
    }
    /**
     * 直播间状态异常的dialog
     */

    protected void showStatusUnUsual() {
        try {
            if (LiveConstant.isUnDestory) {
                if(customChatDialog==null){
                    customChatDialog = new CustomChatDialog(LiveActivity.this);
                }
                customChatDialog.setContent(getString(R.string.live_status_inusual));
                customChatDialog.setCancelable(false);
                customChatDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                    }
                });
                customChatDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                customChatDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 显示用户信息dialog
     * @param userId  用户id
     * @param channelId  直播间id
     * @param isMaster  是否是主播
     */

    protected void showUserInfoDialog(String userId, final String channelId, boolean isMaster) {
        if (LiveConstant.isUnDestory) {
            if(customNewUserInforDialog==null){
                customNewUserInforDialog = new CustomNewUserInforDialog(this, container, userId, channelId, LiveConstant.ROLE, isMaster, creatorAccount, roomId);
            }
            customNewUserInforDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    customNewUserInforDialog=null;
                }
            });
            customNewUserInforDialog.setDialogStatusListener(this);
            customNewUserInforDialog.show();

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

    /**
     * 显示设置dialog
     */
    @Override
    protected void liveCommonSetting() {
        MobHelper.sendEevent(this, MobEvent.LIVE_ANCHOR_SHARE);
        if(settingPopuWindow==null){
            settingPopuWindow = new MasterMoreSettingPopuWindow(this);
        }
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
                          /*  if(yaseaFragment!=null){
                                yaseaFragment.closeBeauty();
                            }*/
                        } else {
                            Constant.PRP_ENABLED = false;
                            Constant.CAMERABACK = true;
                        /*    if(yaseaFragment!=null){
                                yaseaFragment.openBeauty();
                            }*/
                        }

                        if(agoraLiveFragment!=null) {
                            worker().enablePreProcessor();//美颜开闭切换
                            worker().getRtcEngine().switchCamera();
                        }else{
                          //  yaseaFragment.setSwitchCamera();
                        }
                        MobHelper.sendEevent(LiveActivity.this, MobEvent.LIVE_ANCHOR_CAMERA);
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
                      /*  if(yaseaFragment!=null){
                           if(Constant.PRP_ENABLED){
                               yaseaFragment.openBeauty();
                           }else{
                               yaseaFragment.closeBeauty();
                           }
                        }else {*/
                            worker().enablePreProcessor();//美颜开闭切换
                      //  }
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
            if (LiveConstant.isUnDestory) {
                if(customChatDialog==null){
                    customChatDialog = new CustomChatDialog(LiveActivity.this);
                }
                customChatDialog.setContent(message.getContent());
                customChatDialog.setCancelable(false);
                customChatDialog.setOkBtn(getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                    }
                });
                customChatDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                customChatDialog.show();
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

                case R.id.live_telecast_input_text:
                    showInputTextView();
                    break;
                case R.id.live_over_close:
                    finish();
                    break;
                case  R.id.live_layout:
                    onImHiden();
                    break;
            }
        }
    };
    @Override
    public boolean sendBarrageMessage(IMMessage msg) {
        return false;
    }
    public class LiveCallBack extends ICallBack {
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
                registerObservers(true);
                int type = liveRowsBean.getType();
                if(type==2){//声网直播
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    agoraLiveFragment = new AgoraLiveFragment();
                    MyAgoraCallBackListener myAgoraCallBackListener=new MyAgoraCallBackListener();
                    agoraLiveFragment.setAgoraCallBackListener(myAgoraCallBackListener);
                    Bundle bundle=new Bundle();
                    bundle.putString(KeyParams.CNAME,liveRowsBean.getCname());
                    bundle.putString(KeyParams.LONGTIME,logTime);
                    agoraLiveFragment.setArguments(bundle);
                    ft.add(R.id.player_layout, agoraLiveFragment);
                    ft.commit();
                }else  if(type==0){//备用直播推流
                    AppLog.i("TAG","yasea :备用直播推流:"+liveRowsBean.getPushUrl());
                   /* FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    yaseaFragment = new YaseaFragment();
                    MyYaseaCallBackListener myYaseaCallBackListener = new MyYaseaCallBackListener();
                    yaseaFragment.setAgoraCallBackListener(myYaseaCallBackListener);
                    Bundle bundle=new Bundle();
                    bundle.putString(KeyParams.PUSHURL,liveRowsBean.getPushUrl());
                    yaseaFragment.setArguments(bundle);
                    ft.add(R.id.player_layout, yaseaFragment);
                    ft.commit();*/

                }
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
    }
    /**
     * 上传封面到服务器
     * @param bytesImg 要上传的图片
     * @param filename 图片名
     * @param token
     */
    private void upLoadQiNiu(final byte[] bytesImg, final String filename, final String token) {
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
    /**
     * 删除日志文件
     * @param filePath 文件名
     * @return
     */
    public boolean deleteFile(String filePath) {

        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
    /**
     * 结束直播dialog
     */
    private void logoutChatRoom() {
        if (LiveConstant.isUnDestory) {
            if(customChatDialog==null){
                customChatDialog = new CustomChatDialog(getActivity());
            }
            customChatDialog.setContent(getString(R.string.finish_confirm));
            customChatDialog.setCancelable(false);
            customChatDialog.setCancelBtn(getString(R.string.live_over), new CustomChatDialog.CustomDialogListener() {
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
            customChatDialog.setSurceBtn(getString(R.string.live_continue), null);
            customChatDialog.show();
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
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, getString(R.string.confirm), roomId, AuthPreferences.getUserAccount(), liveMessage);
            sendMessage(imMessage, MessageType.leaveLive);
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
    /**
     * 网络类型变化弹窗
     * @param netType
     * @param reminder
     */
    @Override
    protected void checkNetInfo(String netType, int reminder) {

        if ("rests".equals(netType) && reminder == 0) {
            LiveConstant.NET_CHECK = 1;
            if (LiveConstant.isUnDestory) {
                if(customChatDialog==null){
                    customChatDialog = new CustomChatDialog(LiveActivity.this);
                }
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


    boolean firstYaseaConnect=true;
    /*public class MyYaseaCallBackListener implements YaseaFragment.YaseaCallBackListener {

        @Override
        public void onConnectionInterrupted() {

        }

        @Override
        public void onJoinChannelSuccess() {
            if(firstYaseaConnect){
                firstYaseaConnect=false;
                joinChannelSuccess();
            }

        }
    }*/

    public  class MyAgoraCallBackListener implements AgoraLiveFragment.AgoraCallBackListener {
        @Override
        public void onConnectionInterrupted() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    againLoginIm();
                }
            });
        }
        @Override
        public void onJoinChannelSuccess() {
            joinChannelSuccess();
        }
    }

    private void joinChannelSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isStartLive = true;
                isEnterRoom = true;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if (createLiveFragment != null && !createLiveFragment.isHidden()) {
                    ft.hide(createLiveFragment);
                }
                ft.commit();
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });


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


    private void againLoginIm() {
        AppLog.i("TAG", "直播連接中斷连接中断回调");
        String userToken = AuthPreferences.getUserToken();
        final String userAccount = AuthPreferences.getUserAccount();

        NIMClient.getService(AuthService.class).login(new LoginInfo(userAccount, userToken)).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                LiveCache.setAccount(userAccount);
                LiveCache.getRegUserInfo();
                LiveCache.setLoginStatus(true);
            }
            @Override
            public void onFailed(int i) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(AGAINLOGINIM);
                    }
                }, 1000);

            }
            @Override
            public void onException(Throwable throwable) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(AGAINLOGINIM);
                    }
                }, 1000);
            }
        });
    }

    /**
     * 创建日志文件名
     */
    public void logFile() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        logTime = formatter.format(curDate) + "_" + channelId + "_" + liveNumber + "_" + UserHelper.getUserId(this) + ".log";
        LogFileUtils.makeAgoraFilePath(logTime);
    }

    /**
     * 结束直播
     */
    private void finishLive() {
        if (isStartLive) {
            logoutChatRoom();
        } else {
            if (LiveCache.getLoginChatRoomStatus()) {
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

    protected final WorkerThread worker() {
        return ((MyApplication)getApplication()).getWorkerThread();
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewById.addOnLayoutChangeListener(this);

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
    }
    @Override
    protected void onDestroy() {

        if (LiveCache.getLoginChatRoomStatus()) {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        }

        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishLive();
    }

}