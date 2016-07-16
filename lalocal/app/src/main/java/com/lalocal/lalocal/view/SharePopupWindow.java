package com.lalocal.lalocal.view;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.sina.weibo.sdk.net.openapi.ShareWeiboApi;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
/**
 * Created by lenovo on 2016/6/30.
 */
public class SharePopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private LinearLayout shareFriends;
    private LinearLayout shareWechat;
    private LinearLayout shareWeibo;
    private SpecialShareVOBean shareVO;
    private TextView cancel;
    private static final int REQUEST_PERM = 151;
    public SharePopupWindow(Context cx, SpecialShareVOBean shareVO) {
        this.context = cx;
        this.shareVO=shareVO;
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions((Activity) context,mPermissionList,REQUEST_PERM);

    }

    public void showShareWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.share_layout, null);
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
        switch (v.getId()){
            case R.id.share_friends:
                shareFriends();
                break;
            case R.id.share_wechat:
                shareWechat();
                break;
            case R.id.share_weibo:
                shareWeibo();
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
        sp.setCallback(callBackListener);
        if(shareVO.getBitmap()!=null){
            UMImage image = new UMImage((Activity)context,shareVO.getBitmap());
            sp.withMedia(image);
        }
        if(shareVO.getImg()!=null){
            UMImage image = new UMImage((Activity)context,shareVO.getImg());
            sp.withMedia(image);
        }
        sp .withTitle(shareVO.getTitle());
        sp .withText(shareVO.getDesc());
        sp .withTargetUrl(shareVO.getUrl());
        sp.share();

    }
    private void shareFriends() {
        ShareAction sp = new ShareAction((Activity) context);
        sp.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        sp .setCallback(new MyUMListener());

        if(shareVO.getBitmap()!=null){
            UMImage image = new UMImage((Activity)context,shareVO.getBitmap());
            // UMImage image = new UMImage((Activity)context,R.drawable.a);
            sp.withMedia(image);
        }
        if(shareVO.getImg()!=null){
            UMImage image = new UMImage((Activity)context,shareVO.getImg());
            sp.withMedia(image);
        }
        sp .withTitle(shareVO.getTitle());
        sp .withText(shareVO.getDesc());
        sp .withTargetUrl(shareVO.getUrl());
        sp.share();


    }
    private void shareWeibo() {
        ShareAction sp = new ShareAction((Activity) context);
        sp.setPlatform(SHARE_MEDIA.SINA);

        sp .setCallback(new MyUMListener());
        if(shareVO.getBitmap()!=null){
            ShareContent shareContent=new ShareContent();

            Bitmap bitmap = shareVO.getBitmap();
            UMImage image = new UMImage((Activity)context,bitmap);
            sp.withMedia(image);

        }
       /* if(shareVO.getImg()!=null){
            UMImage image = new UMImage((Activity)context,shareVO.getImg());
            sp.withMedia(image);
        }
        sp .withTitle(shareVO.getTitle());
        sp .withText(shareVO.getDesc());
        sp .withTargetUrl(shareVO.getUrl());*/
        sp.share();

    }

    private  UMShareListener callBackListener;

    public void  setCallBackListener(UMShareListener callBackListener){
        this.callBackListener=callBackListener;
    }



    class  MyUMListener implements  UMShareListener{

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            AppLog.i("TAG","onResult"+share_media.toString());

        }


        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(context,throwable.toString(),Toast.LENGTH_SHORT).show();
            AppLog.i("TAG","onError"+throwable.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            AppLog.i("TAG","onError"+share_media.toString());
        }
    }



}