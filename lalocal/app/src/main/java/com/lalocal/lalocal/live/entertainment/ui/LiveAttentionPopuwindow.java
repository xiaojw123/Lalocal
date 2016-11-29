package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.LiveAttentionStatusBean;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/11/16.
 */
public class LiveAttentionPopuwindow extends PopupWindow {
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
    private Context context;
    private  LiveRowsBean liveRowsBean;
    private final ContentLoader contentLoader;
    private String userId;

    public LiveAttentionPopuwindow(Context context,LiveRowsBean liveRowsBean) {
        this.context = context;
        this.liveRowsBean=liveRowsBean;
        contentLoader = new ContentLoader(context);
        AttentionCallBack attentionCallBack=new AttentionCallBack();
        contentLoader.setCallBack(attentionCallBack);
    }

    public void showAttentionPopu() {
        View view = View.inflate(context, R.layout.live_attention_dialog, null);
        ButterKnife.bind(this, view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);
        userId = String.valueOf(liveRowsBean.getUser().getId());

    }
    @OnClick({R.id.live_attention_dialog_cance,R.id.live_attention_dialog_btn})
    public  void clickBtn(View view){
        switch (view.getId()){
            case R.id.live_attention_dialog_cance:
                Toast.makeText(context,"点击了live_attention_dialog_cancel",Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.live_attention_dialog_btn:
                if(isAttention){
                    contentLoader.getAddAttention(userId);
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

                Drawable drawable1 = context.getResources().getDrawable(R.drawable.followsb_ic_sel);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());

                liveAttentionDialogBtn.setCompoundDrawables(drawable1, null, null, null);
            }

        }

        @Override
        public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
            super.onLiveCancelAttention(liveCancelAttention);
            if (liveCancelAttention.getReturnCode() == 0) {
                isAttention=true;
                liveAttentionDialogBtn.setText("关注");
                liveAttentionDialogBtn.setAlpha(1.0f);
                Drawable drawable1 = context.getResources().getDrawable(R.drawable.followsb_ic_unsel);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                liveAttentionDialogBtn.setCompoundDrawables(drawable1, null, null, null);
            }

        }
    }


}
