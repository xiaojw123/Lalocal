package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.LiveAttentionStatusBean;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.DrawableUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/11/16.
 */
public class LiveAttentionPopuwindow extends BaseDialog {
    @BindView(R.id.live_attention_dialog_bgiv)
    ImageView liveAttentionDialogBgiv;
    @BindView(R.id.live_attention_dialog_head)
    CircleImageView liveAttentionDialogHead;
    @BindView(R.id.live_attention_dialog_name)
    TextView liveAttentionDialogName;
    @BindView(R.id.live_attention_dialog_btn)
    TextView liveAttentionDialogBtn;
    @BindView(R.id.live_attention_dialog_cance)
    ImageView liveAttentionDialogCancel;

    private  ContentLoader contentLoader;
    private String userId;

    public LiveAttentionPopuwindow(Context mContext) {
        super(mContext, R.style.share_dialog);
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.live_attention_dialog;
    }


    public void showAttentionPopu(LiveRowsBean liveRowsBean) {
        userId = String.valueOf(liveRowsBean.getUser().getId());
        DrawableUtils.displayImg(getContext(),liveAttentionDialogHead,liveRowsBean.getUser().getAvatar());
        liveAttentionDialogName.setText(liveRowsBean.getUser().getNickName());
        contentLoader = new ContentLoader(getContext());
        AttentionCallBack attentionCallBack=new AttentionCallBack();
        contentLoader.setCallBack(attentionCallBack);
    }
    @OnClick({R.id.live_attention_dialog_cance,R.id.live_attention_dialog_btn})
    public  void clickBtn(View view){
        switch (view.getId()){
            case R.id.live_attention_dialog_cance:
                dismiss();
                break;
            case R.id.live_attention_dialog_btn:
                if(isAttention){
                    if(UserHelper.isLogined(getContext())){
                        contentLoader.getAddAttention(userId);
                    }else{
                        //去弹登录dialog
                        if(onUserAttentionListener!=null){
                            onUserAttentionListener.goLoginActivity();
                        }
                    }

                }else {
                    contentLoader.getCancelAttention(userId);
                }
                break;
        }
    }

    boolean isAttention=true;


    class  AttentionCallBack extends ICallBack{
        @Override
        public void onLiveAttentionStatus(LiveAttentionStatusBean liveAttentionStatusBean) {
            super.onLiveAttentionStatus(liveAttentionStatusBean);
            if (liveAttentionStatusBean.getReturnCode() == 0) {
                int status = liveAttentionStatusBean.getResult().getStatus();
                isAttention=false;
                if(status==1){
                    liveAttentionDialogBtn.setText("已关注");
                    liveAttentionDialogBtn.setAlpha(0.6f);
                }else if(status==2){
                    liveAttentionDialogBtn.setText("相互关注");
                    liveAttentionDialogBtn.setAlpha(0.6f);
                }
                Drawable drawable1 = getContext().getResources().getDrawable(R.drawable.followsb_ic_sel);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                liveAttentionDialogBtn.setCompoundDrawables(drawable1, null, null, null);
                if(onUserAttentionListener!=null){
                    onUserAttentionListener.getAttention();
                }
            }

        }

        @Override
        public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
            super.onLiveCancelAttention(liveCancelAttention);
            if (liveCancelAttention.getReturnCode() == 0) {
                isAttention=true;
                liveAttentionDialogBtn.setText("关注");
                liveAttentionDialogBtn.setAlpha(1.0f);
                Drawable drawable1 = getContext().getResources().getDrawable(R.drawable.followsb_ic_unsel);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                liveAttentionDialogBtn.setCompoundDrawables(drawable1, null, null, null);
            }
        }
    }


    public  interface  OnUserAttentionListener {
        void getAttention();
        void goLoginActivity();
    }
    OnUserAttentionListener onUserAttentionListener;

    public  void setOnUserAttentionListener (OnUserAttentionListener onUserAttentionListener){
        this.onUserAttentionListener=onUserAttentionListener;
    }
}
