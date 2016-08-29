package com.lalocal.lalocal.view.liveroomview.entertainment.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
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
    CustomLiveUserInfoDialogListener sureDialogListener,cancelDialogListener;
    private LinearLayout goMainLayout;


    public CustomLiveUserInfoDialog(Context context,LiveUserInfoResultBean result) {
        super(context, R.style.prompt_dialog);
        this.context = context;
        this.result=result;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_info_layout);
        showLiveUserInfoPopuwindow();

    }
    public void showLiveUserInfoPopuwindow(){
        masterInfoCloseIv = (ImageView) findViewById(R.id.master_info_close_iv);
        goMainLayout = (LinearLayout) findViewById(R.id.go_main_layout);
        masterInfoHeadIv = (CircleImageView) findViewById(R.id.master_info_head_iv);
        masterInfoNickTv = (TextView)findViewById(R.id.master_info_nick_tv);
        masterInfoSignature = (TextView)findViewById(R.id.master_info_signature);
        liveAttention = (TextView)findViewById(R.id.live_attention);
        masterInfoBack = (LinearLayout)findViewById(R.id.master_info_back_home);
        liveFans = (TextView)findViewById(R.id.live_fans);
        liveContribute = (TextView)findViewById(R.id.live_contribute);
        liveMasterHome = (TextView) findViewById(R.id.live_master_home);
        masterInfoCloseIv.setOnClickListener(this);
        goMainLayout.setOnClickListener(this);
        String avatar = result.getAvatar();
        String nickName = result.getNickName();
        int fansNum = result.getFansNum();
        int attentionNum = result.getAttentionNum();
        String description = result.getDescription();
        liveFans.setText(String.valueOf(fansNum));
        liveAttention.setText(String.valueOf(attentionNum));
        if (!TextUtils.isEmpty(description)) {
            masterInfoSignature.setText(description);
        }
        DrawableUtils.displayImg(context, masterInfoHeadIv, avatar);
        masterInfoNickTv.setText(nickName);
      //  liveMasterHome.setOnClickListener(this);
    }
    public void setCancelBtn( CustomLiveUserInfoDialogListener listener) {
        cancelDialogListener = listener;
    }

    public void setSurceBtn( CustomLiveUserInfoDialogListener listener) {

        sureDialogListener = listener;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.master_info_close_iv:

                if(cancelDialogListener!=null){
                    cancelDialogListener.onCustomLiveUserInfoDialogListener(null);
                }
                dismiss();
                break;
            case  R.id.go_main_layout:

                int id = result.getId();
                if(sureDialogListener!=null){
                    sureDialogListener.onCustomLiveUserInfoDialogListener(String.valueOf(id));
                }
                dismiss();
                break;

        }
    }

    public static interface CustomLiveUserInfoDialogListener {
        void onCustomLiveUserInfoDialogListener(String id);
    }
}
