package com.lalocal.lalocal.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.CheckWeixinAndWeibo;
import com.lalocal.lalocal.view.liveroomview.im.ui.blur.BlurImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
/**
 * Created by lenovo on 2016/6/30.
 * com.lalocal.lalocal.wxapi.WXEntryActivity
 */
public class SharePopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private LinearLayout shareFriends;
    private LinearLayout shareWechat;
    private LinearLayout shareWeibo;
    private SpecialShareVOBean shareVO;
    private TextView cancel;
    private static final int REQUEST_PERM = 151;
    private BlurImageView shareBlur;
    private View view;
    public SharePopupWindow(Context cx, SpecialShareVOBean shareVO) {
        this.context = cx;
        this.shareVO = shareVO;
    }

    public void showShareWindow() {
        view = LayoutInflater.from(context).inflate(R.layout.share_layout, null);
        shareBlur = (BlurImageView) view.findViewById(R.id.share_blur);
        shareBlur.setBlurImageRes(R.drawable.citybg,11,20);
        shareFriends = (LinearLayout) view.findViewById(R.id.share_friends);
        shareWechat = (LinearLayout) view.findViewById(R.id.share_wechat);
        shareWeibo = (LinearLayout) view.findViewById(R.id.share_weibo);
        cancel = (TextView) view.findViewById(R.id.cancel_share);
        shareFriends.setOnClickListener(this);
        shareWechat.setOnClickListener(this);
        shareWeibo.setOnClickListener(this);
        cancel.setOnClickListener(this);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_friends:
                if(shareVO!=null){
                    boolean isInstallMm =CheckWeixinAndWeibo.checkAPPInstall(context,"com.tencent.mm");
                    if(isInstallMm){
                        shareFriends();

                    }else{
                        Toast.makeText(context,"没有安装微信客户端",Toast.LENGTH_SHORT).show();
                    }


                }

                break;
            case R.id.share_wechat:
                if(shareVO!=null){
                    boolean isInstallMm =CheckWeixinAndWeibo.checkAPPInstall(context,"com.tencent.mm");
                    if(isInstallMm){
                        shareWechat();

                    }else{
                        Toast.makeText(context,"没有安装微信客户端",Toast.LENGTH_SHORT).show();
                    }

                }

                break;
            case R.id.share_weibo:
                if(shareVO!=null){
                    boolean isInstallWeibo = CheckWeixinAndWeibo.checkAPPInstall(context, "com.sina.weibo");
                    if(isInstallWeibo){
                        shareWeibo();

                    }else {
                        Toast.makeText(context,"没有安装微博客户端",Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.cancel_share:
                dismiss();
                break;
        }
        dismiss();
    }

    private void shareWechat() {

        ShareAction sp = new ShareAction((Activity) context);
        sp.setPlatform(SHARE_MEDIA.WEIXIN);
        sp.setCallback(new MyUMListener());
        if (shareVO.getBitmap() != null) {
            UMImage image = new UMImage((Activity) context, shareVO.getBitmap());
            sp.withMedia(image);
        }
        if (shareVO.getImg() != null) {
            UMImage image = new UMImage((Activity) context, shareVO.getImg());
            sp.withMedia(image);
        }
        if (shareVO.getTitle() != null) {
            sp.withTitle(shareVO.getTitle());
        }
        if (shareVO.getDesc() != null) {
            sp.withText(shareVO.getDesc());
        }
        if (shareVO.getUrl() != null) {
            sp.withTargetUrl(shareVO.getUrl());
        }
        sp.share();
    }

    private void shareFriends() {
        ShareAction sp = new ShareAction((Activity) context);
        sp.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        sp.setCallback(new MyUMListener());
        if (shareVO.getBitmap() != null) {
            UMImage image = new UMImage((Activity) context, shareVO.getBitmap());
            sp.withMedia(image);
            sp.share();
            return;
        }
        if (shareVO.getImg() != null) {
            UMImage image = new UMImage((Activity) context, shareVO.getImg());
            sp.withMedia(image);
        }
        if (shareVO.getTitle() != null) {
            sp.withTitle(shareVO.getTitle());
        }
        if (shareVO.getDesc() != null) {
            sp.withText(shareVO.getDesc());
        }
        if (shareVO.getUrl() != null) {
            sp.withTargetUrl(shareVO.getUrl());
        }
        sp.share();

    }

    private void shareWeibo() {

        ShareAction sp = new ShareAction((Activity) context);
        sp.setPlatform(SHARE_MEDIA.SINA);
        sp.setCallback(new MyUMListener());
        if (shareVO.getBitmap() != null) {

            Bitmap bitmap = shareVO.getBitmap();
            UMImage image = new UMImage((Activity) context, bitmap);
            sp.withMedia(image);
            sp.share();
            return;
        }

        if (shareVO.getImg() != null) {
            UMImage image = new UMImage((Activity) context, shareVO.getImg());
            sp.withMedia(image);
        }
        if (shareVO.getTitle() != null) {
            sp.withTitle(shareVO.getTitle());
        }
        if (shareVO.getDesc() != null) {
            sp.withText(shareVO.getDesc());
        }
        if (shareVO.getUrl() != null) {
            sp.withTargetUrl(shareVO.getUrl());
        }

        sp.share();

    }



    private UMShareListener callBackListener;

    public void setCallBackListener(UMShareListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    class MyUMListener implements UMShareListener {

        @Override
        public void onResult(SHARE_MEDIA share_media) {

           if(share_media.equals(SHARE_MEDIA.SINA)){
               Toast.makeText(context,"微博分享成功!",Toast.LENGTH_SHORT).show();
           }else if(share_media.equals(SHARE_MEDIA.WEIXIN)){
               Toast.makeText(context,"微信分享成功!",Toast.LENGTH_SHORT).show();
           }else if(share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
               Toast.makeText(context,"微信朋友圈分享成功!",Toast.LENGTH_SHORT).show();
           }

        }
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if(share_media.equals(SHARE_MEDIA.SINA)){
                Toast.makeText(context,"微博分享失败!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN)){
                Toast.makeText(context,"微信分享失败!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
                Toast.makeText(context,"微信朋友圈分享失败!",Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            if(share_media.equals(SHARE_MEDIA.SINA)){
                Toast.makeText(context,"已取消微博分享!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN)){
                Toast.makeText(context,"已取消微信分享!",Toast.LENGTH_SHORT).show();
            }else if(share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
                Toast.makeText(context,"已取消微信朋友圈分享!",Toast.LENGTH_SHORT).show();
            }
        }
    }


}