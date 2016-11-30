package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.LiveHomePageActivity;
import com.lalocal.lalocal.live.entertainment.activity.ReportActivity;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListResp;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.im.session.Container;
import com.lalocal.lalocal.model.LiveAttentionStatusBean;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/10/31.
 */
public class CustomUserInfoDialog extends BaseDialog {
    @BindView(R.id.custom_dialog_close_iv)
    ImageView customDialogCloseIv;
    @BindView(R.id.custom_dialog_report)
    TextView customDialogReport;
    @BindView(R.id.custom_dialog_live_header_layout)
    RelativeLayout customDialogLiveHeaderLayout;
    @BindView(R.id.custom_dialog_close_iv_1)
    ImageView customDialogCloseIv1;
    @BindView(R.id.custom_dialog_audience_close_layout)
    RelativeLayout customDialogAudienceCloseLayout;
    @BindView(R.id.userinfo_header_layout)
    FrameLayout userinfoHeaderLayout;
    @BindView(R.id.userinfo_head_iv)
    CircleImageView userinfoHeadIv;
    @BindView(R.id.live_manager_mark)
    ImageView liveManagerMark;
    @BindView(R.id.userinfo_nick_tv)
    TextView userinfoNickTv;
    @BindView(R.id.master_info_signature)
    TextView masterInfoSignature;
    @BindView(R.id.live_attention)
    TextView liveAttention;
    @BindView(R.id.live_fans)
    TextView liveFans;
    @BindView(R.id.userinfo_bottom_left)
    TextView userinfoBottomLeft;
    @BindView(R.id.userinfo_bottom_center)
    TextView userinfoBottomCenter;
    @BindView(R.id.userinfo_bottom_right)
    TextView userinfoBottomRight;
    @BindView(R.id.user_info_layout_bottom)
    LinearLayout userInfoLayoutBottom;
    private String userId;
    private Context mContext;
   boolean isMuted;//是否禁言
    private final ContentLoader contentLoader;
    private int userStatus;
    private int role;//身份，主播，用户
    private  String channelId;
    private List<LiveManagerListBean> managerListResult;
    private Intent intent;
    boolean isManager=false;//是否为管理员
    private int attentionNum;
    private int fansNum;
    private  boolean isMaster;
    private LiveUserInfoResultBean result;
    Container container;
    String creatorAccount;
    String roomId;
    private String accId;
    private String nickName;

    public CustomUserInfoDialog(Context context, Container container, String userId, String channelId, int role, boolean isMaster,String creatorAccount,String roomId) {
        super(context);
        this.userId = userId;
        this.mContext=context;
        this.channelId=channelId;
        this.role=role;
        this.isMaster=isMaster;
        this.container=container;
        this.roomId=roomId;
        this.creatorAccount=creatorAccount;
        contentLoader = new ContentLoader(context);
        contentLoader.setCallBack(new MyCallBack());
        contentLoader.getLiveUserInfo(String.valueOf(userId));//获取用户基本信息

        if(channelId==null){
            userinfoBottomCenter.setVisibility(View.GONE);
            userinfoBottomLeft.setText(mContext.getString(R.string.live_report));
        }else{
            contentLoader.getLiveManagerList(channelId);//查看管理员列表
        }

        AppLog.i("TAG","获取用户ID:"+userId);
    }
    @Override
    public void initView() {
       setCanceledOnTouchOutside(false);
        if(userId.equals(String.valueOf(UserHelper.getUserId(mContext)))){//我自己
            customDialogLiveHeaderLayout.setVisibility(View.GONE);
            customDialogAudienceCloseLayout.setVisibility(View.VISIBLE);
            userInfoLayoutBottom.setVisibility(View.GONE);
        }else {
            if(role==0){
                customDialogLiveHeaderLayout.setVisibility(View.GONE);
                customDialogAudienceCloseLayout.setVisibility(View.VISIBLE);
            }else{
                customDialogLiveHeaderLayout.setVisibility(View.VISIBLE);
                customDialogAudienceCloseLayout.setVisibility(View.GONE);
            }
            if(role==2){//我是超级管理员
                if(isMaster){
                    customDialogReport.setText(mContext.getString(R.string.super_manager_close_liveroom));
                    customDialogReport.setTextColor(Color.parseColor("#ff6f6f"));
                    userinfoBottomCenter.setVisibility(View.GONE);
                }else {
                    customDialogReport.setText(mContext.getString(R.string.super_manager_ban_long));
                    customDialogReport.setTextColor(Color.parseColor("#ff6f6f"));
                    userinfoBottomCenter.setText(mContext.getString(R.string.live_ban));
                }
                userinfoBottomLeft.setText(mContext.getString(R.string.live_report));
                userinfoBottomRight.setText(mContext.getString(R.string.live_master_attention));
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.user_info_layout;
    }

    class MyCallBack extends ICallBack {
        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            try {
               if(liveUserInfosDataResp.getReturnCode()==0){
                   result = liveUserInfosDataResp.getResult();
                   nickName = result.getNickName();
                   String avatar = result.getAvatar();
                   attentionNum = result.getAttentionNum();
                   accId = result.getAccId();
                   fansNum = result.getFansNum();
                   AppLog.i("TAG","查看用户省份："+result.getRole());
                   String description = result.getDescription();
                   Object status = result.getAttentionVO().getStatus();

                   if (status != null) {
                       double parseDouble = Double.parseDouble(String.valueOf(status));
                       userStatus = (int) parseDouble;//关注状态
                       if(userStatus==0){
                           userinfoBottomRight.setText(mContext.getString(R.string.live_master_attention));
                       }else if(userStatus==1){
                           userinfoBottomRight.setText(mContext.getString(R.string.live_attention_ok));
                       }else if(userStatus==2){
                           userinfoBottomRight.setText(mContext.getString(R.string.live_attention_mutual));
                       }
                   }
                   DrawableUtils.displayImg(mContext,userinfoHeadIv,avatar);
                   userinfoNickTv.setText(nickName);
                   if (!TextUtils.isEmpty(description)) {
                       masterInfoSignature.setText(description);
                   }
                   liveAttention.setText(String.valueOf(attentionNum));
                   liveFans.setText(String.valueOf(fansNum));
                   if(role==0){//用户端
                       contentLoader.checkUserIdentity(channelId, String.valueOf(UserHelper.getUserId(mContext)));//查看我是否为管理员
                   }else if(role!=2){//主播端
                       contentLoader.checkUserIdentity(channelId, userId);//查看用户是否为管理员
                   }
                   checkMute(accId);
               }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onCheckManager(LiveManagerBean liveManagerBean) {
            super.onCheckManager(liveManagerBean);
            if(liveManagerBean.getReturnCode()==0){
                int managerResult = liveManagerBean.getResult();
                if(!isCancelManager){
                    if(role==0){//用户端
                        if(!userId.equals(String.valueOf(UserHelper.getUserId(mContext)))){
                            if(managerResult!=0){
                                //我是管理员
                                userinfoBottomCenter.setVisibility(View.VISIBLE);
                                if(isMuted){
                                    userinfoBottomCenter.setText(mContext.getString(R.string.live_relieve_ban));
                                }else{
                                    userinfoBottomCenter.setText(mContext.getString(R.string.live_ban));
                                }
                                userinfoBottomLeft.setText(mContext.getString(R.string.live_report));

                            }else{
                                userinfoBottomCenter.setVisibility(View.GONE);
                                userinfoBottomLeft.setText(mContext.getString(R.string.live_report));
                            }
                        }else {


                            //我自己

                        }

                    }else{//主播端
                        if(userId.equals(String.valueOf(UserHelper.getUserId(mContext)))){
                            //我自己

                        }else{
                            if(managerResult!=0){
                                //他是管理员
                                isManager=true;
                                userinfoBottomCenter.setText(mContext.getString(R.string.live_cancel_manager_cancel));

                            }else{
                                isManager=false;
                                userinfoBottomCenter.setText(mContext.getString(R.string.lvie_setting_manager));
                            }
                            if(isMuted){
                                userinfoBottomLeft.setText(mContext.getString(R.string.live_relieve_ban));
                            }else{
                                userinfoBottomLeft.setText(mContext.getString(R.string.live_ban));
                            }
                        }
                    }
                }else{
                    contentLoader.cancelManagerAccredit(String.valueOf(managerResult));//取消管理员
                }
            }
        }
        @Override
        public void onManagerList(LiveManagerListResp liveManagerListResp) {
            super.onManagerList(liveManagerListResp);
            if(liveManagerListResp.getReturnCode()==0){
                managerListResult = liveManagerListResp.getResult();
            }

        }

        @Override
        public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
            super.onLiveCancelAttention(liveCancelAttention);
            if(liveCancelAttention.getReturnCode()==0){
                AppLog.i("TAG","取消");
                userStatus=0;
                userinfoBottomRight.setText(mContext.getString(R.string.live_master_attention));
                --fansNum;
                liveFans.setText(String.valueOf(fansNum));
            }
        }

        @Override
        public void onLiveAttentionStatus(LiveAttentionStatusBean liveAttentionStatusBean) {
            super.onLiveAttentionStatus(liveAttentionStatusBean);
            if(liveAttentionStatusBean.getReturnCode()==0){
                LiveAttentionStatusBean.ResultBean result = liveAttentionStatusBean.getResult();
                userStatus = result.getStatus();
                if(userStatus==0){
                    userinfoBottomRight.setText(mContext.getString(R.string.live_master_attention));
                }else if(userStatus==1){
                    ++fansNum;
                    userinfoBottomRight.setText(mContext.getString(R.string.live_attention_ok));
                    liveFans.setText(String.valueOf(fansNum));
                }else if(userStatus==2){
                    ++fansNum;
                    userinfoBottomRight.setText(mContext.getString(R.string.live_attention_mutual));
                    liveFans.setText(String.valueOf(fansNum));
                }
            }
        }

        @Override
        public void onLiveManager(LiveManagerBean liveManagerBean) {
            super.onLiveManager(liveManagerBean);
            if(liveManagerBean.getReturnCode()==0){
                isManager=true;
                isCancelManager=false;
                liveManagerBean.getResult();
                userinfoBottomCenter.setText(mContext.getString(R.string.live_cancel_manager_cancel));
                String messageContent = "授权" + result.getNickName() + "为管理员";
                sendMessage(messageContent,MessageType.managerLive);

            }

        }

        @Override
        public void onCancelManager(String json) {
            super.onCancelManager(json);
            userinfoBottomCenter.setText(mContext.getString(R.string.lvie_setting_manager));
            isManager = false;
            String messageContent = "取消了" + result.getNickName() + "管理员权限";
            sendMessage(messageContent,MessageType.cancel);

        }

        @Override
        public void onPerpetualMute(int code) {
            super.onPerpetualMute(code);
            if(code!=0){
                sendMessage("将"+nickName+"踢出直播间",MessageType.kickOut);
            }
        }
    }

    private void checkMute(String accId) {
        List<String> userIdList=new ArrayList<>();
        userIdList.add(accId);
        NIMClient.getService(ChatRoomService.class)
                .fetchRoomMembersByIds(roomId,userIdList )
                .setCallback(new RequestCallbackWrapper<List<ChatRoomMember>>() {
                    @Override
                    public void onResult(int i, List<ChatRoomMember> chatRoomMembers, Throwable throwable) {
                        if(chatRoomMembers!=null&&chatRoomMembers.size()>0){
                            for(ChatRoomMember chatRoom:chatRoomMembers ){
                                isMuted= chatRoom.isTempMuted();
                                boolean muted = chatRoom.isMuted();
                                if(isMuted){



                                }else {



                                }

                                AppLog.i("TAG","检测用户是否禁言1："+(muted==true?"禁言":"未禁言"));
                                AppLog.i("TAG","检测用户是否禁言："+(isMuted==true?"禁言":"未禁言"));
                            }
                        }
                    }
                });
    }

    boolean isCancelManager=false;
    @OnClick({R.id.custom_dialog_close_iv,R.id.custom_dialog_report,R.id.custom_dialog_close_iv_1,R.id.userinfo_head_iv,R.id.userinfo_bottom_left,R.id.userinfo_bottom_center,R.id.userinfo_bottom_right})
    public  void clickBtn(View view){
        switch (view.getId()){
            case R.id.custom_dialog_close_iv:
                dismiss();
                break;
            case R.id.custom_dialog_report:
                // 举报
                if(role==1){//主播端，举报
                  toReportActivity();

                }else if(role==2){
                    if(isMaster){//关闭直播间
                        Toast.makeText(mContext,"关闭直播间",Toast.LENGTH_SHORT).show();
                        CustomChatDialog dialogNet = new CustomChatDialog(mContext);
                        dialogNet.setTitle(mContext.getString(R.string.super_manager_close_liveroom));
                        dialogNet.setContent(mContext.getString(R.string.super_manager_close_liveroom_content));
                        dialogNet.setCancelable(false);
                        dialogNet.setCancelBtn(mContext.getString(R.string.super_manager_cancel_liveroom), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {

                            }
                        });
                        dialogNet.setSurceBtn(mContext.getString(R.string.super_manager_close_liveroom_ok), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                if(onSuperManagerCloseLiveRoomListener!=null){
                                    onSuperManagerCloseLiveRoomListener.closeLiveRoom();
                                }

                            }
                        });
                        dialogNet.show();

                    }else{//永久禁言
                        if(userId!=null){
                            AppLog.i("TAG","获取用户id："+userId+"     accid:"+accId);
                            contentLoader.getPerpetualMute(userId);
                        }
                        Toast.makeText(mContext,"永久禁言",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.custom_dialog_close_iv_1:
                dismiss();
                break;
            case R.id.userinfo_head_iv://进入用户主页
                intent = new Intent(mContext, LiveHomePageActivity.class);
                intent.putExtra("userId", String.valueOf(userId));
                mContext.startActivity(intent);
                break;
            case R.id.userinfo_bottom_left:
                if(role==0||role==2){//用户端
                    // 举报
                    toReportActivity();
                }else {//主播端 禁言
                    if(isMuted){
                        //解除禁言
                        userinfoBottomLeft.setText(mContext.getString(R.string.live_ban));
                        isMuted=false;
                        contentLoader.getUserMute(channelId,roomId,String.valueOf(UserHelper.getUserId(mContext)),userId,1);
                        sendMessage("解除了"+result.getNickName()+"的禁言",MessageType.relieveBan);
                    }else {
                        //禁言
                        contentLoader.getUserMute(channelId,roomId,String.valueOf(UserHelper.getUserId(mContext)),userId,0);
                        userinfoBottomLeft.setText(mContext.getString(R.string.live_relieve_ban));
                        isMuted=true;
                        sendMessage("禁言了"+result.getNickName(),MessageType.ban);
                    }
                }
                dismiss();
                break;
            case R.id.userinfo_bottom_center:
                if(role==0||role==2){//禁言
                    if(isMuted){
                        //解除禁言
                        userinfoBottomCenter.setText(mContext.getString(R.string.live_ban));
                        contentLoader.getUserMute(channelId,roomId,String.valueOf(UserHelper.getUserId(mContext)),userId,1);
                        isMuted=false;
                        sendMessage("解除了"+result.getNickName()+"的禁言",MessageType.relieveBan);

                    }else {
                        //禁言
                        userinfoBottomCenter.setText(mContext.getString(R.string.live_relieve_ban));
                        isMuted=true;
                        contentLoader.getUserMute(channelId,roomId,String.valueOf(UserHelper.getUserId(mContext)),userId,0);
                        sendMessage("禁言了"+result.getNickName(),MessageType.ban);
                    }
                }else {//管理员设置
                    if(isManager){
                        //取消管理员
                        CustomChatDialog customDialog = new CustomChatDialog(mContext);
                        customDialog.setContent(mContext.getString(R.string.live_setting_manager_confirm));
                        customDialog.setCancelable(false);
                        customDialog.setCancelBtn(mContext.getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                contentLoader.checkUserIdentity(channelId, String.valueOf(userId));
                                isCancelManager=true;
                            }
                        });
                        customDialog.setSurceBtn(mContext.getString(R.string.live_not_cancel), null);
                        customDialog.show();

                    }else{
                        //设置管理员
                        if(managerListResult.size()>3){
                            final CustomChatDialog customDialog = new CustomChatDialog(mContext);
                            customDialog.setContent(mContext.getString(R.string.live_manager_count));
                            customDialog.setCancelable(false);
                            customDialog.setOkBtn(mContext.getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                                @Override
                                public void onDialogClickListener() {
                                    customDialog.dismiss();
                                }
                            });
                            customDialog.show();
                        }else {
                            contentLoader.liveAccreditManager(channelId, String.valueOf(userId));
                        }
                    }
                }
                break;
            case R.id.userinfo_bottom_right://关注
                if(userStatus==0){//加关注
                    contentLoader.getAddAttention(String.valueOf(userId));
                }else{//取消关注
                    contentLoader.getCancelAttention(String.valueOf(userId));
                }
                break;
        }
    }

    public void sendMessage(String content,int type){
        try {
            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setStyle(type);
            liveMessage.setAdminSendMsgImUserId(String.valueOf(UserHelper.getImccId(mContext)));
            liveMessage.setAdminSendMsgNickName(UserHelper.getUserName(mContext));
            liveMessage.setAdminSendMsgUserId(String.valueOf(UserHelper.getUserId(mContext)));
            liveMessage.setDisableSendMsgNickName(UserHelper.getUserName(mContext));
            liveMessage.setDisableSendMsgUserId(String.valueOf(UserHelper.getImccId(mContext)));
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            AppLog.i("TAG","用户信息dialog:"+new Gson().toJson(result).toString());
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, content, roomId, String.valueOf(UserHelper.getImccId(mContext)), liveMessage);
            container.proxy.sendMessage(imMessage, type);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //去举报页面
    public  void toReportActivity(){
        MobHelper.sendEevent(mContext, MobEvent.LIVE_ANCHOR_REPORT);
        Intent intent = new Intent(mContext, ReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(com.lalocal.lalocal.model.Constants.KEY_CHANNEL_ID, channelId);
        bundle.putString(com.lalocal.lalocal.model.Constants.KEY_USER_ID, userId);
        bundle.putString(com.lalocal.lalocal.model.Constants.KEY_MASTER_NAME,nickName );
        intent.putExtras(bundle);
        mContext.startActivity(intent);

    }

    public  interface  OnSuperManagerCloseLiveRoomListener {
        void closeLiveRoom();

    }
    OnSuperManagerCloseLiveRoomListener onSuperManagerCloseLiveRoomListener;

    public  void setOnSuperManagerCloseLiveRoomListener ( OnSuperManagerCloseLiveRoomListener onSuperManagerCloseLiveRoomListener){
        this.onSuperManagerCloseLiveRoomListener=onSuperManagerCloseLiveRoomListener;
    }
    public  interface  OnLiveRoomContentListener {
        void getClickContent(String content);

    }
    OnLiveRoomContentListener onLiveRoomContentListener;

    public  void setOnLiveRoomContentListener ( OnLiveRoomContentListener onLiveRoomContentListener){
        this.onLiveRoomContentListener=onLiveRoomContentListener;
    }

}
