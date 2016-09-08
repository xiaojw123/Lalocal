package com.lalocal.lalocal.live.entertainment.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.constant.CustomDialogStyle;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.util.DrawableUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/8/28.
 */
public class CustomLiveUserInfoDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private LiveUserInfoResultBean result;
    private boolean isLiveOver;
    protected View masterInfoLayoutPw;
    private ImageView masterInfoCloseIv;
    private CircleImageView masterInfoHeadIv;
    private TextView masterInfoNickTv;
    private TextView masterInfoSignature;
    private TextView liveAttention;
    private LinearLayout masterInfoBack;
    private TextView liveFans;
    private TextView liveContribute;
    private TextView liveMasterHome;
    CustomLiveUserInfoDialogListener sureDialogListener,cancelDialogListener,reportListener,settingManagerListener,banListener;
    CustomLiveFansOrAttentionListener attentionListener;
    private FrameLayout goMainLayout;
    private FrameLayout headerLayout;
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


    public CustomLiveUserInfoDialog(Context context,LiveUserInfoResultBean result) {
        super(context, R.style.prompt_dialog);
        this.context = context;
        this.result=result;
    }

    public CustomLiveUserInfoDialog(Context context,LiveUserInfoResultBean result,boolean isManager,boolean isAudience) {
        super(context, R.style.prompt_dialog);
        this.context = context;
        this.result=result;
        this.isManager=isManager;
        this.isAudience=isAudience;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_info_layout);
        showLiveUserInfoPopuwindow();
    }

    public void showLiveUserInfoPopuwindow(){
        headerLayout = (FrameLayout) findViewById(R.id.custom_info_header_layout);
        goMainLayout = (FrameLayout) findViewById(R.id.go_main_layout);
        masterInfoHeadIv = (CircleImageView) findViewById(R.id.master_info_head_iv);
        masterInfoNickTv = (TextView)findViewById(R.id.master_info_nick_tv);
        masterInfoSignature = (TextView)findViewById(R.id.master_info_signature);
        liveAttention = (TextView)findViewById(R.id.live_attention);
        masterInfoBack = (LinearLayout)findViewById(R.id.master_info_back_home);
        liveFans = (TextView)findViewById(R.id.live_fans);
        liveContribute = (TextView)findViewById(R.id.live_contribute);

        bottomReport = (TextView)findViewById(R.id.custom_dialog_report);
        attentionStatus = (TextView)findViewById(R.id.custom_dialog_attention);
        masterInfoCloseIv = (ImageView)findViewById(R.id.custom_dialog_close_iv);
        masterInfoCloseIv1 = (ImageView)findViewById(R.id.custom_dialog_close_iv_1);
        headerReport = (TextView)findViewById(R.id.custom_dialog_report);
        liveManagerBan = (TextView)findViewById(R.id.live_manager_ban);
        liveManagerSetting = (TextView)findViewById(R.id.live_manager_setting);
        attentionStatus = (TextView)findViewById(R.id.custom_dialog_attention);
        headerReport = (TextView)findViewById(R.id.custom_dialog_report);
        liveManagerBan = (TextView)findViewById(R.id.live_manager_ban);
        liveManagerSetting = (TextView)findViewById(R.id.live_manager_setting);
        attentionStatus = (TextView)findViewById(R.id.custom_dialog_attention);
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

        Object statusa = result.getAttentionVO().getStatus();
        if (statusa!=null){
            double parseDouble = Double.parseDouble(String.valueOf(statusa));
            int status = (int) parseDouble;
            if(status==1){
                attentionStatus.setText("正在关注");
                dialogLiveAttention.setText("正在关注");
                audienceAttention.setText("正在关注");
                audienceManagerAttention.setText("正在关注");
                audienceManagerAttention.setAlpha(0.4f);
                audienceAttention.setAlpha(0.4f);
                attentionStatus.setAlpha(0.4f);
                dialogLiveAttention.setAlpha(0.4f);
            }else if(status==2){
                attentionStatus.setText("相互关注");
                dialogLiveAttention.setText("相互关注");
                audienceAttention.setText("相互关注");
                audienceManagerAttention.setText("相互关注");
                audienceManagerAttention.setAlpha(0.4f);
                audienceAttention.setAlpha(0.4f);
                attentionStatus.setAlpha(0.4f);
                dialogLiveAttention.setAlpha(0.4f);

            }else{
                attentionStatus.setText("关注");
                dialogLiveAttention.setText("关注");
                audienceAttention.setText("关注");
                audienceManagerAttention.setText("关注");
                audienceManagerAttention.setAlpha(1f);
                audienceAttention.setAlpha(1);
                attentionStatus.setAlpha(1);
                dialogLiveAttention.setAlpha(1);
            }
        }
        if (!TextUtils.isEmpty(description)) {
            masterInfoSignature.setText(description);
        }

        if(!TextUtils.isEmpty(dialogAttention)){
            attentionStatus.setText(dialogAttention);
        }
        if(!TextUtils.isEmpty(ban)&&!isAudience){
            liveManagerBan.setText(ban);
            homeLayoutBtn.setVisibility(View.GONE);
            claseLayout.setVisibility(View.GONE);
            headerLayout.setVisibility(View.VISIBLE);
            liveBottomLayout.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(managerSetting)){
            liveManagerSetting.setText(managerSetting);
        }
        if(!TextUtils.isEmpty(dialogAttention)&&TextUtils.isEmpty(ban)&&CustomDialogStyle.CUSTOM_DIALOG_LIVE==1){//判断直播端，主播信息
            attentionStatus.setText(dialogAttention);
            homeLayoutBtn.setVisibility(View.GONE);
            headerLayout.setVisibility(View.VISIBLE);
            audienceBottomLayout.setVisibility(View.VISIBLE);
        }else if(!TextUtils.isEmpty(dialogAttention)&&CustomDialogStyle.CUSTOM_DIALOG_AUDIENCE==1&&CustomDialogStyle.CUSTOM_DIALOG_STYLE==2){
            //判断用户端，用户信息
            attentionStatus.setText(dialogAttention);
            if(isManager){
                audienceManagerban.setText(ban);
                audienceManagerLayout.setVisibility(View.VISIBLE);
                audienceBottomLayout.setVisibility(View.GONE);
            }else {
                audienceBottomLayout.setVisibility(View.VISIBLE);
                audienceManagerLayout.setVisibility(View.GONE);
            }
            headerLayout.setVisibility(View.GONE);
            claseLayout.setVisibility(View.VISIBLE);
            homeLayoutBtn.setVisibility(View.GONE);
            CustomDialogStyle.CUSTOM_DIALOG_STYLE=0;
        }
        if(TextUtils.isEmpty(managerSetting)&& CustomDialogStyle.CUSTOM_DIALOG_STYLE==1&&CustomDialogStyle.CUSTOM_DIALOG_LIVE==1){
            //判断主播端，用户信息
            homeLayoutBtn.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.GONE);
            audienceToLiveToBottom.setVisibility(View.GONE);
        }else if(TextUtils.isEmpty(managerSetting)&& CustomDialogStyle.CUSTOM_DIALOG_STYLE==1&&CustomDialogStyle.CUSTOM_DIALOG_AUDIENCE==1){
            //用户端主播信息
            homeLayoutBtn.setVisibility(View.GONE);
            headerLayout.setVisibility(View.VISIBLE);
            claseLayout.setVisibility(View.GONE);
            audienceToLiveToBottom.setVisibility(View.VISIBLE);


        }

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
                    cancelDialogListener.onCustomLiveUserInfoDialogListener(null,null);
                }
                dismiss();
                break;
            case R.id.custom_dialog_close_iv_1:
                if(cancelDialogListener!=null){
                    cancelDialogListener.onCustomLiveUserInfoDialogListener(null,null);
                }
                dismiss();
                break;
            case  R.id.live_master_home://主播端主页

                if(sureDialogListener!=null){
                   sureDialogListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),liveMasterHome);
                }
                dismiss();
                break;
            case R.id.live_master_home_audience:
                if(sureDialogListener!=null){
                    sureDialogListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),homeAudience);
                }
                break;
            case R.id.custom_dialog_report_audience://观众端举报
                if(reportListener!=null){
                    reportListener.onCustomLiveUserInfoDialogListener(null,audienceReport);
                }
                break;
            case R.id.custom_dialog_report://主播端举报
                if(reportListener!=null){
                   reportListener.onCustomLiveUserInfoDialogListener(null,headerReport);
                }
                break;
            case R.id.custom_dialog_attention://主播端关注
                if(attentionListener!=null){
                  //  attentionListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),attentionStatus);
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
                    banListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),liveManagerBan);
                }
                break;
            case R.id.live_manager_setting://设为管理员
                if(settingManagerListener!=null){

                    settingManagerListener.onCustomLiveUserInfoDialogListener(String.valueOf(accountId),liveManagerSetting);
                }
                break;
            case R.id.audience_manager_report:

                if(reportListener!=null){
                    reportListener.onCustomLiveUserInfoDialogListener(null,audienceManagerReport);
                }
                break;
            case R.id.audience_manager_ban:
                if(banListener!=null){
                    banListener.onCustomLiveUserInfoDialogListener(null,audienceManagerban);
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
        void onCustomLiveUserInfoDialogListener(String id,TextView textView);

    }
    public  static interface CustomLiveFansOrAttentionListener{
        void onCustomLiveFansOrAttentionListener(String id,TextView fansView,TextView attentionView,int fansCount,int attentionCount,TextView attentionStatus);
    }
}
