package com.lalocal.lalocal.view.liveroomview.entertainment.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.liveroomview.entertainment.activity.LiveHomePageActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/8/24.
 */
public class LiveUserInfoPopuwindow extends PopupWindow implements View.OnClickListener{
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

    public LiveUserInfoPopuwindow(Context mContext, LiveUserInfoResultBean result, boolean isLiveOver){
        this.context=mContext;
        this.result=result;
        this.isLiveOver=isLiveOver;
    }
    public void showLiveUserInfoPopuwindow(){

        masterInfoLayoutPw = View.inflate(context, R.layout.master_info_layout, null);
        masterInfoCloseIv = (ImageView) masterInfoLayoutPw.findViewById(R.id.master_info_close_iv);
        masterInfoHeadIv = (CircleImageView) masterInfoLayoutPw.findViewById(R.id.master_info_head_iv);
        masterInfoNickTv = (TextView) masterInfoLayoutPw.findViewById(R.id.master_info_nick_tv);
        masterInfoSignature = (TextView) masterInfoLayoutPw.findViewById(R.id.master_info_signature);
        liveAttention = (TextView) masterInfoLayoutPw.findViewById(R.id.live_attention);
        masterInfoBack = (LinearLayout) masterInfoLayoutPw.findViewById(R.id.master_info_back_home);
        liveFans = (TextView) masterInfoLayoutPw.findViewById(R.id.live_fans);
        liveContribute = (TextView) masterInfoLayoutPw.findViewById(R.id.live_contribute);
        liveMasterHome = (TextView) masterInfoLayoutPw.findViewById(R.id.live_master_home);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(masterInfoLayoutPw);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);

        liveMasterHome.setOnClickListener(this);


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
        liveMasterHome.setOnClickListener(this);
        if (isLiveOver) {
            masterInfoBack.setVisibility(View.VISIBLE);
            masterInfoCloseIv.setVisibility(View.INVISIBLE);
            masterInfoBack.setOnClickListener(this);
            masterInfoCloseIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity)context).finish();
                }
            });
        } else {
            masterInfoCloseIv.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.live_master_home:
                int id = result.getId();
                Intent intent = new Intent(context, LiveHomePageActivity.class);
                intent.putExtra("userId", String.valueOf(id));
                context.startActivity(intent);
                dismiss();
                break;
            case R.id.master_info_back_home:
                ((Activity)context).finish();
                break;
            case R.id.master_info_close_iv:
                dismiss();
                break;
        }
    }
}
