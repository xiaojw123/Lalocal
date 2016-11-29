package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/11/16.
 */
public class LiveingSharePopuwindow extends PopupWindow {

    @BindView(R.id.liveing_dialog_bgiv)
    ImageView liveingDialogBgiv;
    @BindView(R.id.liveing_dialog_share_friends)
    TextView liveingDialogShareFriends;
    @BindView(R.id.liveing_dialog_share_wechat)
    TextView liveingDialogShareWechat;
    @BindView(R.id.liveing_dialog_share_weibo)
    TextView liveingDialogShareWeibo;
    @BindView(R.id.liveing_dialog_share_qq)
    TextView liveingDialogShareQq;
    @BindView(R.id.liveing_dialog_share_qzone)
    TextView liveingDialogShareQzone;
    @BindView(R.id.liveing_dialog_cancel)
    ImageView liveingDialogCancel;
    private Context context;

    public LiveingSharePopuwindow(Context context) {
        this.context = context;

    }

    public void showSharePopu() {
        View view = View.inflate(context, R.layout.liveing_share_dialog, null);
        ButterKnife.bind(this, view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);

    }

    @OnClick({R.id.liveing_dialog_share_friends, R.id.liveing_dialog_share_wechat,R.id.liveing_dialog_share_weibo,R.id.liveing_dialog_share_qq,R.id.liveing_dialog_share_qzone})
    public void clickBtn(View view) {
        switch (view.getId()) {
            case R.id.liveing_dialog_share_friends:
                if(livingShareListener!=null){
                    livingShareListener.sharePlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
                break;
            case R.id.liveing_dialog_share_wechat:
                if(livingShareListener!=null){
                    livingShareListener.sharePlatform(SHARE_MEDIA.WEIXIN);
                }
                break;
            case R.id.liveing_dialog_share_weibo:
                if(livingShareListener!=null){
                    livingShareListener.sharePlatform(SHARE_MEDIA.SINA);
                }
                break;
            case R.id.liveing_dialog_share_qq:
                if(livingShareListener!=null){
                    livingShareListener.sharePlatform(SHARE_MEDIA.QQ);
                }
                break;
            case R.id.liveing_dialog_share_qzone:
                if(livingShareListener!=null){
                    livingShareListener.sharePlatform(SHARE_MEDIA.QZONE);
                }
                break;
            case R.id.liveing_dialog_cancel:
                dismiss();
                break;
        }
        }

    private  LivingShareListener livingShareListener;

    public interface  LivingShareListener{
        void sharePlatform(SHARE_MEDIA share_media);
    }

    public  void setOnLivingShareListener(LivingShareListener livingShareListener){
        this.livingShareListener=livingShareListener;
    }
        }
