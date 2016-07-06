package com.lalocal.lalocal.view;
import android.content.Context;
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
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
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

    public SharePopupWindow(Context cx, SpecialShareVOBean shareVO) {
        this.context = cx;
        this.shareVO=shareVO;
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






    private void shareWeibo() {
        SinaWeibo.ShareParams sp  = new SinaWeibo.ShareParams();
        Platform plat= ShareSDK.getPlatform(SinaWeibo.NAME);
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(shareVO.getTitle());
        sp.setText(shareVO.getUrl());

        if (shareVO.getImg() != null) {
            sp.setImageUrl(shareVO.getImg());
        }
        sp.setUrl(shareVO.getUrl());
        sp.setSiteUrl(shareVO.getUrl());
        if (platformActionListener != null) {
            plat.setPlatformActionListener(platformActionListener);
        }
        plat.share(sp);
    }

    private void shareFriends() {
        WechatMoments.ShareParams sp  = new WechatMoments.ShareParams();
        Platform plat= ShareSDK.getPlatform(WechatMoments.NAME);
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(shareVO.getTitle());
        sp.setText(shareVO.getUrl());
        if (shareVO.getImg() != null) {
            sp.setImageUrl(shareVO.getImg());
        }
        sp.setUrl(shareVO.getUrl());
        sp.setSiteUrl(shareVO.getUrl());
        if (platformActionListener != null) {
            plat.setPlatformActionListener(platformActionListener);
        }
        plat.share(sp);
    }
    private void shareWechat() {
        Wechat.ShareParams sp  = new Wechat.ShareParams();
        Platform plat= ShareSDK.getPlatform(Wechat.NAME);
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(shareVO.getTitle());
        sp.setText(shareVO.getUrl());
        if (shareVO.getImg() != null) {
            sp.setImageUrl(shareVO.getImg());
        }
        sp.setUrl(shareVO.getUrl());
        sp.setSiteUrl(shareVO.getUrl());
        if (platformActionListener != null) {
            plat.setPlatformActionListener(platformActionListener);
        }
        plat.share(sp);

    }


    private PlatformActionListener platformActionListener;
    public PlatformActionListener getPlatformActionListener() {
        return platformActionListener;
    }

    public void setPlatformActionListener(
            PlatformActionListener platformActionListener) {
        this.platformActionListener = platformActionListener;
    }
}
