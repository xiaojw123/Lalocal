package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.ReportActivity;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListResp;
import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.lalocal.lalocal.live.im.session.Container;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by android on 2016/12/13.
 */
public class CustomOtherFunctionDialog extends BaseDialog {

    @BindView(R.id.live_other_function_content)
    LinearLayout liveOtherFunctionContent;
    @BindView(R.id.live_other_function_cancel)
    TextView liveOtherFunctionCancel;
    @BindView(R.id.live_other_function_report)
    TextView liveOtherFunctionReport;
    @BindView(R.id.live_other_function_common)
    TextView liveOtherFunctionCommon;
    @BindView(R.id.live_other_function_bottom)
    TextView liveOtherFunctionBottom;
    @BindView(R.id.live_other_function_bottom_layout)
    LinearLayout liveOtherFunctionBottomLayout;
    private ContentLoader contentLoader;
    String channelId;
    private List<LiveManagerListBean> managerListResult;
    Context mContext;
    int role;
    String roomId;
    String userId;
    boolean isManager = false;//是否为管理员
    boolean isMaster;
    LiveUserInfoResultBean result;
    String creatorAccount;
    Container container;
    public CustomOtherFunctionDialog(Context context, Container container, int role, String channelId, String roomId, String userId, boolean isMaster, LiveUserInfoResultBean result, String creatorAccount) {
        super(context, R.style.share_dialog);
        this.role = role;
        this.mContext = context;
        this.channelId = channelId;
        this.roomId = roomId;
        this.userId = userId;
       this.result=result;
        this.isMaster = isMaster;
        this.creatorAccount=creatorAccount;
        this.container=container;
        contentLoader = new ContentLoader(context);
        contentLoader.setCallBack(new MyCallBack());
        if (role == 0) {//用户端
            contentLoader.checkUserIdentity(channelId, String.valueOf(UserHelper.getUserId(mContext)));//查看我是否为管理员
        }
        if (channelId != null) {
            contentLoader.getLiveManagerList(channelId);//查看管理员列表
        }

    }

    private int managerResult;
    boolean isCancelManager = false;

    class MyCallBack extends ICallBack {
        @Override
        public void onManagerList(LiveManagerListResp liveManagerListResp) {
            super.onManagerList(liveManagerListResp);
            if (liveManagerListResp.getReturnCode() == 0) {
                managerListResult = liveManagerListResp.getResult();
            }
        }
        @Override
        public void onLiveManager(LiveManagerBean liveManagerBean) {
            super.onLiveManager(liveManagerBean);
            if(liveManagerBean.getReturnCode()==0){
                isManager=true;
                isCancelManager=false;
                liveManagerBean.getResult();
                liveOtherFunctionBottom.setText(mContext.getString(R.string.live_cancel_manager_cancel));
                String messageContent = getContext().getString(R.string.sou_quan) + result.getNickName() + mContext.getString(R.string.wei_manager);
                sendMessage(messageContent, MessageType.managerLive);
                LiveManagerListBean listBean=new LiveManagerListBean();
                listBean.setAvatar(result.getAvatar());
                listBean.setId(Integer.parseInt(userId));
                LiveConstant.result.add(listBean);
                if(onSettingManagerListener!=null){
                    onSettingManagerListener.settingManager(true);
                }
            }
        }

        @Override
        public void onCancelManager(String json) {
            super.onCancelManager(json);
            liveOtherFunctionBottom.setText(mContext.getString(R.string.lvie_setting_manager));
            isManager = false;
            String messageContent = mContext.getString(R.string.cancel_le) + result.getNickName() + mContext.getString(R.string.manager_quanxian);
            sendMessage(messageContent,MessageType.cancel);
            LiveManagerListBean listBean=new LiveManagerListBean();
            listBean.setAvatar(result.getAvatar());
            listBean.setId(Integer.parseInt(userId));
            LiveConstant.result.remove(listBean);
            if(onSettingManagerListener!=null){
                onSettingManagerListener.settingManager(false);
            }
        }
        @Override
        public void onPerpetualMute(int code) {
            super.onPerpetualMute(code);
            if (code != 0) {
                sendMessage(mContext.getString(R.string.jiang)+result.getNickName()+mContext.getString(R.string.super_manager_ban_long),MessageType.kickOut);
            }
        }

        @Override
        public void onCheckManager(LiveManagerBean liveManagerBean) {
            super.onCheckManager(liveManagerBean);
            if (liveManagerBean.getReturnCode() == 0) {
                managerResult = liveManagerBean.getResult();
                if(!isCancelManager){
                    if (role == 0) {
                        if (managerResult != 0) {//我是管理员
                            checkMute(result.getAccId());
                        }
                    } else if (role == 1) {//主播端
                        checkMute(result.getAccId());
                        liveOtherFunctionBottomLayout.setVisibility(View.VISIBLE);
                        liveOtherFunctionBottom.setTextColor(mContext.getResources().getColor(R.color.color_0076ff));
                        if (managerResult != 0) {//他是管理员
                            isManager = true;
                            liveOtherFunctionBottom.setText("取消管理员");
                        } else {
                            isManager = false;
                            liveOtherFunctionBottom.setText("设置管理员");
                        }
                    }


                }else{
                    contentLoader.cancelManagerAccredit(String.valueOf(managerResult));//取消管理员
                }

            }
        }
    }

    @OnClick({R.id.live_other_function_cancel, R.id.live_other_function_common, R.id.live_other_function_report,R.id.live_other_function_bottom})
    public void clickBtn(View view) {
        switch (view.getId()) {
            case R.id.live_other_function_cancel:
                dismiss();
                break;
            case R.id.live_other_function_common:
                    if (isMuted) {//解除禁言
                        liveOtherFunctionCommon.setText(mContext.getString(R.string.live_ban));
                        isMuted = false;
                        contentLoader.getUserMute(channelId, roomId, String.valueOf(UserHelper.getUserId(mContext)), userId, 1);
                        sendMessage("解禁",MessageType.relieveBan);
                    } else {//禁言
                        contentLoader.getUserMute(channelId, roomId, String.valueOf(UserHelper.getUserId(mContext)), userId, 0);
                        liveOtherFunctionCommon.setText(mContext.getString(R.string.live_relieve_ban));
                        isMuted = true;
                        sendMessage("禁言",MessageType.ban);
                    }
                dismiss();
                break;
            case R.id.live_other_function_report:
                toReportActivity();
                dismiss();
                break;
            case R.id.live_other_function_bottom:
                if (role == 1) {//主播
                    if (isManager) {//取消管理员
                        try {
                            CustomChatDialog customDialog = new CustomChatDialog(mContext);
                            customDialog.setContent(mContext.getString(R.string.live_setting_manager_confirm));
                            customDialog.setCancelable(false);
                            customDialog.setCancelBtn(mContext.getString(R.string.lvie_sure), new CustomChatDialog.CustomDialogListener() {
                                @Override
                                public void onDialogClickListener() {
                                    contentLoader.checkUserIdentity(channelId, String.valueOf(userId));
                                    isCancelManager = true;
                                }
                            });
                            customDialog.setSurceBtn(mContext.getString(R.string.live_not_cancel), null);
                            customDialog.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {//设置管理员
                        if (managerListResult.size() > 3) {
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
                        } else {
                            contentLoader.liveAccreditManager(channelId, String.valueOf(userId));
                        }
                    }
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_ANCHOR_MANAGEMENT);

                } else if (role == 2) {
                    if (isMaster) {
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
                               sendMessage("关闭直播间",MessageType.closeLive);
                            }
                        });
                        dialogNet.show();
                    } else {
                        if (userId != null) {
                            contentLoader.getPerpetualMute(userId);
                        }
                    }

                }
                dismiss();
                break;
        }

    }

    private void toReportActivity() {
        MobHelper.sendEevent(mContext, MobEvent.LIVE_ANCHOR_REPORT);
        Intent intent = new Intent(mContext, ReportActivity.class);
        Bundle bundle = new Bundle();
        AppLog.i("TAG", "用戶新係：" + channelId);
        bundle.putString(Constants.KEY_CHANNEL_ID, channelId);
        bundle.putString(Constants.KEY_USER_ID, userId);
        bundle.putString(Constants.KEY_MASTER_NAME, result.getNickName());
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    boolean isMuted;

    private void checkMute(final String accId) {
        List<String> userIdList = new ArrayList<>();
        userIdList.add(accId);
        NIMClient.getService(ChatRoomService.class)
                .fetchRoomMembersByIds(roomId, userIdList)
                .setCallback(new RequestCallbackWrapper<List<ChatRoomMember>>() {
                    @Override
                    public void onResult(int i, List<ChatRoomMember> chatRoomMembers, Throwable throwable) {
                        if (chatRoomMembers != null && chatRoomMembers.size() > 0) {
                            for (ChatRoomMember chatRoom : chatRoomMembers) {
                                isMuted = chatRoom.isTempMuted();
                                liveOtherFunctionCommon.setVisibility(View.VISIBLE);
                                liveOtherFunctionCommon.setTextColor(mContext.getResources().getColor(R.color.color_fe3824));
                                if (isMuted) {
                                    liveOtherFunctionCommon.setText("解除禁言");
                                } else {
                                    liveOtherFunctionCommon.setText("禁言");
                                }
                            }
                        } else {
                            liveOtherFunctionCommon.setVisibility(View.VISIBLE);
                            liveOtherFunctionCommon.setTextColor(mContext.getResources().getColor(R.color.color_fe3824));
                            liveOtherFunctionCommon.setText("禁言");
                        }
                    }
                });
    }


    @Override
    public void initView() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        if (role == 1) {//主播端
            contentLoader.checkUserIdentity(channelId, userId);//查看用户是否为管理员
            liveOtherFunctionCommon.setVisibility(View.VISIBLE);
            liveOtherFunctionBottomLayout.setVisibility(View.VISIBLE);
            liveOtherFunctionBottom.setText("设置管理员");
            liveOtherFunctionCommon.setTextColor(mContext.getResources().getColor(R.color.color_fe3824));
        } else if (role == 2) {//超级管理员
            checkMute(result.getAccId());
            liveOtherFunctionBottomLayout.setVisibility(View.VISIBLE);
            if (isMaster) {
                liveOtherFunctionBottom.setText("关闭直播间");
            } else {
                liveOtherFunctionBottom.setText("踢出直播间");
            }
            liveOtherFunctionBottom.setTextColor(mContext.getResources().getColor(R.color.color_fe3824));
        }
    }

    public void sendMessage(String content,int type){
        try {
            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setStyle(type);
            liveMessage.setAdminSendMsgImUserId(result.getAccId());
            liveMessage.setAdminSendMsgNickName(result.getNickName());
            liveMessage.setAdminSendMsgUserId(userId);
            liveMessage.setDisableSendMsgNickName(result.getNickName());
            liveMessage.setDisableSendMsgUserId(userId);
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
    @Override
    public int getLayoutId() {
        return R.layout.custom_live_other_function_layout;
    }

    public  interface  OnSettingManagerListener {
        void settingManager(boolean isManager);

    }
    OnSettingManagerListener onSettingManagerListener;

    public  void setOnSettingManagerListener ( OnSettingManagerListener onSettingManagerListener){
        this.onSettingManagerListener=onSettingManagerListener;
    }


}
