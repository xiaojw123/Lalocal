package com.lalocal.lalocal.live.entertainment.ui;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/8/28.
 */
public class CustomLiveUserInfoDialog extends BaseDialog implements View.OnClickListener{
    private Context context;
    private LiveUserInfoResultBean result;
    private boolean isLiveOver;
    protected View masterInfoLayoutPw;
    private ImageView masterInfoCloseIv;
    private CircleImageView masterInfoHeadIv;
    private TextView masterInfoNickTv;
    private TextView masterInfoSignature;
    private TextView liveAttention;

    private TextView liveFans;

    private TextView liveMasterHome;
    CustomLiveUserInfoDialogListener sureDialogListener,cancelDialogListener,reportListener,settingManagerListener,banListener,userHomeListener;
    CustomLiveFansOrAttentionListener attentionListener;

    private TextView headerReport;
    private TextView bottomReport;
    private TextView liveManagerBan;
    private TextView liveManagerSetting;
    private TextView attentionStatus;
    String ban,managerSetting,dialogAttention;
    private ImageView masterInfoCloseIv1;
    private TextView dialogLiveAttention;
    private int accountId;
    private TextView audienceReport;
    private TextView audienceAttention;
    private TextView homeAudience;
    private TextView audienceManagerReport;
    private TextView audienceManagerban;
    private TextView audienceManagerAttention;
    private int fansNum;
    private int attentionNum;
    private  boolean isManager;
    private  boolean isAudience;
    private ImageView managerMark;
    private boolean isMuted;

    public CustomLiveUserInfoDialog(Context context, LiveUserInfoResultBean result, boolean isManager, boolean isMuted) {
        super(context);
        this.context = context;
        this.result=result;
        this.isManager=isManager;
        this.isMuted=isMuted;
        AppLog.i("TAG","获取用户身份信息:"+result.getRole());
    }

    @Override
    public void initView() {
        showLiveUserInfoPopuwindow();
    }

    @Override
    public int getLayoutId() {
        return R.layout.master_info_layout;
    }

    public void showLiveUserInfoPopuwindow(){

        masterInfoHeadIv = (CircleImageView) findViewById(R.id.master_info_head_iv);
        masterInfoNickTv = (TextView)findViewById(R.id.master_info_nick_tv);
        masterInfoSignature = (TextView)findViewById(R.id.master_info_signature);
        liveAttention = (TextView)findViewById(R.id.live_attention);

        liveFans = (TextView)findViewById(R.id.live_fans);

        managerMark = (ImageView) findViewById(R.id.live_manager_mark);
        bottomReport = (TextView)findViewById(R.id.custom_dialog_report);
        attentionStatus = (TextView)findViewById(R.id.custom_dialog_attention);
        masterInfoCloseIv = (ImageView)findViewById(R.id.custom_dialog_close_iv);
        masterInfoCloseIv1 = (ImageView)findViewById(R.id.custom_dialog_close_iv_1);
        headerReport = (TextView)findViewById(R.id.custom_dialog_report);
        liveManagerBan = (TextView)findViewById(R.id.live_manager_ban);
        liveManagerSetting = (TextView)findViewById(R.id.live_manager_setting);

        headerReport = (TextView)findViewById(R.id.custom_dialog_report);
        liveManagerBan = (TextView)findViewById(R.id.live_manager_ban);
        liveManagerSetting = (TextView)findViewById(R.id.live_manager_setting);

        liveMasterHome= (TextView) findViewById(R.id.live_master_home);
        audienceReport = (TextView) findViewById(R.id.custom_dialog_report_audience);
        audienceAttention = (TextView) findViewById(R.id.custom_dialog_attention_audience);
        homeAudience = (TextView) findViewById(R.id.live_master_home_audience);
        dialogLiveAttention = (TextView) findViewById(R.id.custom_dialog_attention_live);

        audienceManagerReport = (TextView) findViewById(R.id.audience_manager_report);
        audienceManagerban = (TextView) findViewById(R.id.audience_manager_ban);
        audienceManagerAttention = (TextView) findViewById(R.id.audience_manager_attention);

        LinearLayout homeLayoutBtn = (LinearLayout) findViewById(R.id.custom_dialog_home_layout);
        LinearLayout liveBottomLayout= (LinearLayout) findViewById(R.id.custom_dialog_live_bottom_layout);
        LinearLayout audienceBottomLayout= (LinearLayout) findViewById(R.id.custom_dialog_audience_bottom_layout);
        RelativeLayout headerLayout= (RelativeLayout) findViewById(R.id.custom_dialog_live_header_layout);
        LinearLayout audienceToLiveToBottom= (LinearLayout) findViewById(R.id.custom_dialog_audience_to_live_bottom_l);
        RelativeLayout claseLayout= (RelativeLayout) findViewById(R.id.custom_dialog_audience_close_layout);
        LinearLayout audienceManagerLayout= (LinearLayout) findViewById(R.id.custom_dialog_audience_to_user_bottom);

        headerReport.setOnClickListener(this);
        bottomReport.setOnClickListener(this);
        liveManagerBan.setOnClickListener(this);
        liveManagerSetting.setOnClickListener(this);
        attentionStatus.setOnClickListener(this);
        liveMasterHome.setOnClickListener(this);
        masterInfoCloseIv.setOnClickListener(this);
        masterInfoCloseIv1.setOnClickListener(this);
        audienceReport.setOnClickListener(this);
        audienceAttention.setOnClickListener(this);
        homeAudience.setOnClickListener(this);
        dialogLiveAttention.setOnClickListener(this);
        audienceManagerReport.setOnClickListener(this);
        audienceManagerban.setOnClickListener(this);
        audienceManagerAttention.setOnClickListener(this);
        masterInfoHeadIv.setOnClickListener(this);

        String avatar = result.getAvatar();
        String nickName= result.getNickName();
        fansNum = result.getFansNum();
        attentionNum = result.getAttentionNum();
        String description = result.getDescription();
        liveFans.setText(String.valueOf(fansNum));
        liveAttention.setText(String.valueOf(attentionNum));
        DrawableUtils.displayImg(context, masterInfoHeadIv, avatar);
        masterInfoNickTv.setText(nickName);
        accountId = result.getId();
        if(isManager){
            managerMark.setVisibility(View.VISIBLE);
        }else {
            managerMark.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(description)) {
            masterInfoSignature.setText(description);
        }


        if(!TextUtils.isEmpty(managerSetting)){
            if(isManager){
                liveManagerSetting.setText("取消管理员");
                liveManagerSetting.setAlpha(0.4f);
            }else{
                liveManagerSetting.setText("设为管理员");
                liveManagerSetting.setAlpha(1);
            }

        }



        if(LiveConstant.IDENTITY== LiveConstant.IS_LIVEER){//主播
            setAttentionStatus(attentionStatus);
            headerLayout.setVisibility(View.VISIBLE);
            audienceToLiveToBottom.setVisibility(View.VISIBLE);
            LiveConstant.IDENTITY=-1;
        }else if(LiveConstant.IDENTITY== LiveConstant.IS_ONESELF){//自己
            homeLayoutBtn.setVisibility(View.VISIBLE);
            claseLayout.setVisibility(View.VISIBLE);
            LiveConstant.IDENTITY=-1;
        }else if(LiveConstant.IDENTITY== LiveConstant.MANAGER_IS_ME){//我是管理员
            if(!TextUtils.isEmpty(ban)){
                if(isMuted){
                    audienceManagerban.setText("解除禁言");
                    audienceManagerban.setAlpha(0.4f);
                }else{
                    audienceManagerban.setText("禁言");
                    audienceManagerban.setAlpha(1);
                }

            }
            setAttentionStatus(audienceManagerAttention);
            claseLayout.setVisibility(View.VISIBLE);
            audienceManagerLayout.setVisibility(View.VISIBLE);
            LiveConstant.IDENTITY=-1;

        }else if(LiveConstant.IDENTITY== LiveConstant.LIVEER_CHECK_ADMIN){//主播设置管理员
            if(!TextUtils.isEmpty(ban)){
                if(isMuted){
                    liveManagerBan.setText("解除禁言");
                    liveManagerBan.setAlpha(0.4f);
                }else{
                    liveManagerBan.setText("禁言");
                    liveManagerBan.setAlpha(1);
                }

            }
            setAttentionStatus(dialogLiveAttention);
            headerLayout.setVisibility(View.VISIBLE);
            liveBottomLayout.setVisibility(View.VISIBLE);
            LiveConstant.IDENTITY=-1;

        }else if(LiveConstant.IDENTITY== LiveConstant.ME_CHECK_OTHER){//我看别人
            setAttentionStatus(audienceAttention);
            claseLayout.setVisibility(View.VISIBLE);
            audienceBottomLayout.setVisibility(View.VISIBLE);
            LiveConstant.IDENTITY=-1;
        }

        setCanceledOnTouchOutside(true);

    }

    private void setAttentionStatus(TextView attentionStatus) {
        Object statusa = result.getAttentionVO().getStatus();
        if (statusa!=null){
            double parseDouble = Double.parseDouble(String.valueOf(statusa));
            int status = (int) parseDouble;
            if(status==1){
                attentionStatus.setText(context.getString(R.string.live_attention_ok));
                attentionStatus.setAlpha(0.4f);

            }else if(status==2){
                attentionStatus.setText(context.getString(R.string.live_attention_mutual));
                attentionStatus.setAlpha(0.4f);
            }else{
                attentionStatus.setText(context.getString(R.string.live_attention));
                attentionStatus.setAlpha(1);
            }
        }

    }

    public  void setUserHomeBtn(CustomLiveUserInfoDialogListener listener){
        userHomeListener = listener;
    }
    public void setCancelBtn( CustomLiveUserInfoDialogListener listener) {
        cancelDialogListener = listener;
    }
    public void setSurceBtn( CustomLiveUserInfoDialogListener listener) {
        sureDialogListener = listener;
    }
    public void setBanBtn(String ban, CustomLiveUserInfoDialogListener listener){
        this.ban=ban;
        banListener=listener;
    }
    public  void setManagerBtn(String managerSetting,CustomLiveUserInfoDialogListener listener){
        this.managerSetting=managerSetting;
        settingManagerListener=listener;
    }
    public void setAttention(String attention,CustomLiveFansOrAttentionListener listener){
        this.dialogAttention=attention;
        attentionListener=listener;

    }
    public  void setReport(CustomLiveUserInfoDialogListener listener){
        reportListener=listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.custom_dialog_close_iv://关闭
                if(cancelDialogListener!=null){
                    cancelDialogListener.onCustomLiveUserInfoDialogListener(null,null,null);
                }
                dismiss();
                break;
            case R.id.custom_dialog_close_iv_1:
                if(cancelDialogListener!=null){
                    cancelDialogListener.onCustomLiveUserInfoDialogListener(null,null,null);
                }
                dismiss();
                break;
            case  R.id.live_master_home://主播端主页

                if(sureDialogListener!=null){
                    sureDialogListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),liveMasterHome,managerMark);
                }
                dismiss();
                break;
            case R.id.master_info_head_iv:
                if(userHomeListener!=null){
                    userHomeListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),liveMasterHome,managerMark);
                }
                dismiss();
                break;
            case R.id.live_master_home_audience:
                if(sureDialogListener!=null){
                    sureDialogListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),homeAudience,managerMark);
                }
                break;
            case R.id.custom_dialog_report_audience://观众端举报
                if(reportListener!=null){
                    reportListener.onCustomLiveUserInfoDialogListener(null,audienceReport,managerMark);
                }
                break;
            case R.id.custom_dialog_report://主播端举报
                if(reportListener!=null){
                    reportListener.onCustomLiveUserInfoDialogListener(null,headerReport,managerMark);
                }
                break;
            case R.id.custom_dialog_attention://主播端关注
                if(attentionListener!=null){

                    attentionListener.onCustomLiveFansOrAttentionListener(String.valueOf(accountId),liveFans,liveAttention,fansNum,attentionNum,attentionStatus);
                }
                break;
            case R.id.custom_dialog_attention_live:
                if(attentionListener!=null){
                    attentionListener.onCustomLiveFansOrAttentionListener(String.valueOf(accountId),liveFans,liveAttention,fansNum,attentionNum,dialogLiveAttention);
                }
                break;
            case R.id.custom_dialog_attention_audience://观众端用户关注
                if(attentionListener!=null){
                    attentionListener.onCustomLiveFansOrAttentionListener(String.valueOf(accountId),liveFans,liveAttention,fansNum,attentionNum,audienceAttention);
                }
                break;
            case R.id.live_manager_ban://主播端禁言
                if(banListener!=null){
                    banListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),liveManagerBan,managerMark);
                }
                break;
            case R.id.live_manager_setting://设为管理员
                if(settingManagerListener!=null){

                    settingManagerListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),liveManagerSetting,managerMark);
                }
                break;
            case R.id.audience_manager_report:

                if(reportListener!=null){
                    reportListener.onCustomLiveUserInfoDialogListener(null,audienceManagerReport,null);
                }
                break;
            case R.id.audience_manager_ban:
                if(banListener!=null){
                    banListener.onCustomLiveUserInfoDialogListener(null,audienceManagerban,null);
                }
                break;
            case R.id.audience_manager_attention:
                if(attentionListener!=null){
                    attentionListener.onCustomLiveFansOrAttentionListener(String.valueOf(accountId),liveFans,liveAttention,fansNum,attentionNum,audienceManagerAttention);
                }
                break;
        }
    }

    public static interface CustomLiveUserInfoDialogListener {
        void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark);
    }
    public  static interface CustomLiveFansOrAttentionListener{
        void onCustomLiveFansOrAttentionListener(String id, TextView fansView, TextView attentionView, int fansCount, int attentionCount, TextView attentionStatus);
    }
}