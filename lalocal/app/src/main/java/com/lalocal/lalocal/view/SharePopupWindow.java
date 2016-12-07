package com.lalocal.lalocal.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CheckWeixinAndWeibo;
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
    private TextView shareFriends;
    private TextView shareWechat;
    private TextView shareWeibo;
    private SpecialShareVOBean shareVO;
    private ImageView cancel;
    private static final int REQUEST_PERM = 151;
  //  private BlurImageView shareBlur;
    private View view;
    private View cancelLayout;
    private  String  targetId;
    ImageView cancelShareBtn;


    private ContentLoader contentLoader;



    public SharePopupWindow(Context cx, SpecialShareVOBean shareVO) {
        this.context = cx;
        this.shareVO = shareVO;
    }

    public  SharePopupWindow(Context cx,SpecialShareVOBean shareVO,String targetId){
        this.context=cx;
        this.shareVO=shareVO;
        this.targetId=targetId;
    }

    public void showShareWindow() {
        boolean isInstallMm1 =CheckWeixinAndWeibo.checkAPPInstall(context,"com.tencent.mm");
        boolean isInstallWeibo = CheckWeixinAndWeibo.checkAPPInstall(context, "com.sina.weibo");
        if(!isInstallMm1&&!isInstallWeibo){
            Toast.makeText(context,"您尚未安装微博和微信，无法分享!",Toast.LENGTH_SHORT).show();
            dismiss();
        }
        view = LayoutInflater.from(context).inflate(R.layout.share_layout, null);
        shareFriends = (TextView) view.findViewById(R.id.share_friends);
        shareWechat = (TextView) view.findViewById(R.id.share_wechat);
        shareWeibo = (TextView) view.findViewById(R.id.share_weibo);
   cancelShareBtn= (ImageView) view.findViewById(R.id.cancel_share);

        shareFriends.setOnClickListener(this);
        shareWechat.setOnClickListener(this);
        shareWeibo.setOnClickListener(this);
        cancelShareBtn.setOnClickListener(this);


        if(!isInstallMm1){
            shareFriends.setVisibility(View.GONE);
            shareWechat.setVisibility(View.GONE);
        }
        if(!isInstallWeibo){
            shareWeibo.setVisibility(View.GONE);
        }

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);
        contentLoader = new ContentLoader(context);
        contentLoader.setCallBack(new ICallBack() {
            @Override
            public void onShareStatistics(String json) {
                super.onShareStatistics(json);
            }
        });

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
            contentLoader.getShareStatistics(String.valueOf(shareVO.getTargetType()), String.valueOf(shareVO.getTargetId()),share_media.equals(SHARE_MEDIA.SINA)?"2":(share_media.equals(SHARE_MEDIA.WEIXIN)?"1":"0"));
            if(onSuccessShare!=null){
                onSuccessShare.shareSuccess(share_media);
            }
           if(share_media.equals(SHARE_MEDIA.SINA)){
               Toast.makeText(context,"微博分享成功!",Toast.LENGTH_SHORT).show();
           }else if(share_media.equals(SHARE_MEDIA.WEIXIN)){
               Toast.makeText(context,"微信分享成功!",Toast.LENGTH_SHORT).show();
           }else if(share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
               Toast.makeText(context,"微信朋友圈分享成功!",Toast.LENGTH_SHORT).show();
           }

        }
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if(onSuccessShare!=null){
                onSuccessShare.shareSuccess(share_media);
            }
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

    private  OnSuccessShareListener onSuccessShare;

    public  interface OnSuccessShareListener{
        void shareSuccess(SHARE_MEDIA share_media);
    }
    public void setOnSuccessShare(OnSuccessShareListener onSuccessShare) {
        this.onSuccessShare = onSuccessShare;
    }

}