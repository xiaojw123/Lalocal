package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveHomePageActivity;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.SendMessageUtil;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
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
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/12/13.
 */
public class CustomNewUserInforDialog extends BaseDialog {
    @BindView(R.id.custom_dialog_close_iv)
    ImageView customDialogCloseIv;
    @BindView(R.id.custom_dialog_report)
    TextView customDialogReport;
    @BindView(R.id.custom_dialog_live_header_layout)
    RelativeLayout customDialogLiveHeaderLayout;
    @BindView(R.id.custom_dialog_close_iv_1)
    ImageView customDialogCloseIv1;
    @BindView(R.id.master_info_bg_top)
    ImageView masterInfoBgTop;
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
    private String channelId;
    private List<LiveManagerListBean> managerListResult;
    private Intent intent;
    boolean isManager = false;//是否为管理员
    private int attentionNum;
    private int fansNum;
    private boolean isMaster;
    private LiveUserInfoResultBean result;
    Container container;
    String creatorAccount;
    String roomId;
    private String accId;
    private String nickName;

    public CustomNewUserInforDialog(Context context, Container container, String userId, String channelId, int role, boolean isMaster, String creatorAccount, String roomId) {
        super(context, R.style.live_dialog);
        this.userId = userId;
        this.mContext = context;
        this.channelId = channelId;
        this.role = role;
        this.isMaster = isMaster;
        this.container = container;
        this.roomId = roomId;
        this.creatorAccount = creatorAccount;
        contentLoader = new ContentLoader(context);
        contentLoader.setCallBack(new MyCallBack());
        contentLoader.getLiveUserInfo(String.valueOf(userId));//获取用户基本信息
        contentLoader.checkUserIdentity(channelId, userId);//检查是否为管理员
        AppLog.i("TAG", "获取用户ID:" + userId);
    }

    private int managerResult;

    class MyCallBack extends ICallBack {


        @Override
        public void onCheckManager(LiveManagerBean liveManagerBean) {
            super.onCheckManager(liveManagerBean);
            if (liveManagerBean.getReturnCode() == 0) {
                managerResult = liveManagerBean.getResult();
                if (managerResult != 0) {//是管理员
                    liveManagerMark.setVisibility(View.VISIBLE);
                } else {
                    liveManagerMark.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            try {
                if (liveUserInfosDataResp.getReturnCode() == 0) {
                    result = liveUserInfosDataResp.getResult();
                    nickName = result.getNickName();
                    String avatar = result.getAvatar();
                    attentionNum = result.getAttentionNum();
                    accId = result.getAccId();
                    fansNum = result.getFansNum();
                    AppLog.i("TAG", "查看用户省份：" + result.getRole() + "    accId:" + accId);
                    String description = result.getDescription();
                    Object status = result.getAttentionVO().getStatus();
                    if (status != null) {
                        double parseDouble = Double.parseDouble(String.valueOf(status));
                        userStatus = (int) parseDouble;//关注状态
                        if (userStatus == 0) {
                            userinfoBottomRight.setText(mContext.getString(R.string.live_master_attention));
                            userinfoBottomLeft.setText(mContext.getString(R.string.private_letter_no));
                            userinfoBottomLeft.setAlpha(0.5f);
                            userinfoBottomLeft.setEnabled(false);
                        } else if (userStatus == 1) {
                            userinfoBottomRight.setText(mContext.getString(R.string.live_attention_ok));
                            userinfoBottomLeft.setText(mContext.getString(R.string.private_letter));
                            userinfoBottomLeft.setAlpha(1.0f);
                            userinfoBottomLeft.setEnabled(true);
                        } else if (userStatus == 2) {
                            userinfoBottomRight.setText(mContext.getString(R.string.live_attention_mutual));
                            userinfoBottomLeft.setText(mContext.getString(R.string.private_letter));
                            userinfoBottomLeft.setAlpha(1.0f);
                            userinfoBottomLeft.setEnabled(true);
                        }
                    }
                    DrawableUtils.displayImg(mContext, userinfoHeadIv, avatar);
                    userinfoNickTv.setText(nickName);

                    if (accId.equals(LiveConstant.creatorAccid)) {//主播
                        masterInfoSignature.setText(LiveConstant.liveTitle);
                        masterInfoSignature.setBackgroundResource(R.drawable.live_user_info_signature_bg);
                        masterInfoBgTop.setImageResource(R.drawable.live_humancard_triangle_yellow);
                    } else {
                        if (!TextUtils.isEmpty(description)) {
                            masterInfoSignature.setText(description);
                        } else {
                            masterInfoSignature.setText(getContext().getString(R.string.live_default_signture));
                        }
                    }
                    liveAttention.setText(String.valueOf(attentionNum));
                    liveFans.setText(String.valueOf(fansNum));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
            super.onLiveCancelAttention(liveCancelAttention);
            if (liveCancelAttention.getReturnCode() == 0) {
                AppLog.i("TAG", "取消");
                userStatus = 0;
                userinfoBottomRight.setText(mContext.getString(R.string.live_master_attention));
                userinfoBottomLeft.setText(mContext.getString(R.string.private_letter_no));
                userinfoBottomLeft.setAlpha(0.5f);
                userinfoBottomLeft.setEnabled(false);
                --fansNum;
                liveFans.setText(String.valueOf(fansNum));
            }
        }

        @Override
        public void onLiveAttentionStatus(LiveAttentionStatusBean liveAttentionStatusBean) {
            super.onLiveAttentionStatus(liveAttentionStatusBean);
            if (liveAttentionStatusBean.getReturnCode() == 0) {
                LiveAttentionStatusBean.ResultBean resultStatus = liveAttentionStatusBean.getResult();
                userStatus = resultStatus.getStatus();
                if (userStatus == 0) {
                    userinfoBottomRight.setText(mContext.getString(R.string.live_master_attention));
                    userinfoBottomLeft.setText(mContext.getString(R.string.private_letter_no));
                    userinfoBottomLeft.setAlpha(0.5f);
                    userinfoBottomLeft.setEnabled(false);
                } else if (userStatus == 1) {
                    ++fansNum;
                    userinfoBottomRight.setText(mContext.getString(R.string.live_attention_ok));
                    liveFans.setText(String.valueOf(fansNum));
                    userinfoBottomLeft.setText(mContext.getString(R.string.private_letter));
                    userinfoBottomLeft.setAlpha(1.0f);
                    userinfoBottomLeft.setEnabled(true);
                    if (isMaster) {
                        sendMessage(getContext().getString(R.string.attention_live_e), MessageType.text);
                    }
                } else if (userStatus == 2) {
                    ++fansNum;
                    userinfoBottomRight.setText(mContext.getString(R.string.live_attention_mutual));
                    liveFans.setText(String.valueOf(fansNum));
                    userinfoBottomLeft.setText(mContext.getString(R.string.private_letter_no));
                    userinfoBottomLeft.setAlpha(0.5f);
                    userinfoBottomLeft.setEnabled(false);
                    if (isMaster) {
                        sendMessage(getContext().getString(R.string.attention_live_e), MessageType.text);
                    }
                }
            }
        }

    }


    @OnClick({R.id.custom_dialog_report, R.id.custom_dialog_close_iv, R.id.custom_dialog_close_iv_1, R.id.userinfo_head_iv, R.id.userinfo_bottom_left, R.id.userinfo_bottom_right})
    public void clickBtn(View view) {
        switch (view.getId()) {
            case R.id.custom_dialog_close_iv:
                if (role == 1) {
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_ANCHOR_CANCEL);
                } else {
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_USER_CANCEL);
                }
                dismiss();
                break;
            case R.id.custom_dialog_close_iv_1:
                if (role == 1) {
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_ANCHOR_CANCEL);
                } else {
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_USER_CANCEL);
                }
                dismiss();
                break;
            case R.id.userinfo_head_iv://进入用户主页
                if (role == 1) {
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_ANCHOR_AVATAR);
                } else {
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_USER_AVATAR);
                }
                intent = new Intent(mContext, LiveHomePageActivity.class);
                intent.putExtra("userId", String.valueOf(userId));
                mContext.startActivity(intent);
                break;
            case R.id.userinfo_bottom_left:
                //TODO 私信入口
                dismiss();
                if (mContext instanceof AudienceActivity) {
                    ((AudienceActivity) mContext).gotoPersonalMessage(true,accId, nickName);
                }
                break;
            case R.id.userinfo_bottom_right://关注
                if (userStatus == 0) {//加关注
                    contentLoader.getAddAttention(String.valueOf(userId));
                } else {//取消关注
                    contentLoader.getCancelAttention(String.valueOf(userId));
                }
                if (role == 1) {
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_ANCHOR_ATTENTION);
                } else {
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_USER_ATTENTION);
                }
                break;
            case R.id.custom_dialog_report:
                if (result != null) {
                    final CustomOtherFunctionDialog customOtherFunctionDialog = new CustomOtherFunctionDialog(mContext, container, role, channelId, roomId, userId, isMaster, result, creatorAccount);
                    customOtherFunctionDialog.setOnSettingManagerListener(new CustomOtherFunctionDialog.OnSettingManagerListener() {
                        @Override
                        public void settingManager(boolean isManager) {
                            liveManagerMark.setVisibility(isManager == true ? View.VISIBLE : View.GONE);
                        }
                    });
                    customOtherFunctionDialog.show();
                }
                break;

        }
    }


    @Override
    public void initView() {
        if (userId != null && userId.equals(String.valueOf(UserHelper.getUserId(mContext)))) {//我自己
            customDialogLiveHeaderLayout.setVisibility(View.GONE);
            customDialogAudienceCloseLayout.setVisibility(View.VISIBLE);
            userinfoBottomLeft.setVisibility(View.GONE);
            userinfoBottomRight.setVisibility(View.GONE);
            userinfoBottomCenter.setText(getContext().getString(R.string.live_master_home));
            userinfoBottomCenter.setTextColor(Color.WHITE);
            userinfoBottomCenter.setBackgroundColor(mContext.getResources().getColor(R.color.color_ffaa2a));
        } else {
            customDialogLiveHeaderLayout.setVisibility(View.VISIBLE);
            customDialogAudienceCloseLayout.setVisibility(View.GONE);
        }
    }

    public void sendMessage(String content, int type) {
        try {
            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setStyle(type);
            liveMessage.setAdminSendMsgImUserId(accId);
            liveMessage.setAdminSendMsgNickName(nickName);
            liveMessage.setAdminSendMsgUserId(userId);
            liveMessage.setDisableSendMsgNickName(nickName);
            liveMessage.setDisableSendMsgUserId(userId);
            liveMessage.setUserId(userId);
            liveMessage.setCreatorAccount(creatorAccount);
            liveMessage.setChannelId(channelId);
            AppLog.i("TAG", "用户信息dialog:" + new Gson().toJson(result).toString());
            IMMessage imMessage = SendMessageUtil.sendMessage(container.account, content, roomId, String.valueOf(UserHelper.getImccId(mContext)), liveMessage);
            container.proxy.sendMessage(imMessage, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.user_info_layout;
    }
}
