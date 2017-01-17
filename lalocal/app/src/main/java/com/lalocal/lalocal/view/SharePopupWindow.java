package com.lalocal.lalocal.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.ui.BaseDialog;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CheckWeixinAndWeibo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wcj on 2016/6/30.
 * com.lalocal.lalocal.wxapi.WXEntryActivity
 */
public class SharePopupWindow extends BaseDialog{
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

    private ContentLoader contentLoader;
    private SpecialShareVOBean shareVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
        showShareWindow(shareVO);
    }

    @Override
    public int getLayoutId() {
        return R.layout.liveing_share_dialog;
    }
    Context mContext;
    public SharePopupWindow(Context mContext,SpecialShareVOBean shareVO) {
        super(mContext, R.style.share_dialog);
        this.shareVO=shareVO;
        this.mContext=mContext;

    }

    public void showShareWindow(SpecialShareVOBean shareVO) {
        this.shareVO = shareVO;
        boolean isInstallMm1 = CheckWeixinAndWeibo.checkAPPInstall(getContext(), "com.tencent.mm");
        boolean isInstallWeibo = CheckWeixinAndWeibo.checkAPPInstall(getContext(), "com.sina.weibo");
        if (!isInstallMm1 && !isInstallWeibo) {
            Toast.makeText(mContext, "您尚未安装微博和微信，无法分享!", Toast.LENGTH_SHORT).show();
            dismiss();
        }

        if (!isInstallMm1&&liveingDialogShareFriends!=null&&liveingDialogShareWechat!=null) {
            liveingDialogShareFriends.setVisibility(View.GONE);
            liveingDialogShareWechat.setVisibility(View.GONE);
        }
        if (!isInstallWeibo&&liveingDialogShareWeibo!=null) {
            liveingDialogShareWeibo.setVisibility(View.GONE);
        }
        contentLoader = new ContentLoader(mContext);
        contentLoader.setCallBack(new ICallBack() {
            @Override
            public void onShareStatistics(String json) {
                super.onShareStatistics(json);
            }
        });

    }

    @OnClick({R.id.liveing_dialog_share_friends, R.id.liveing_dialog_share_wechat,R.id.liveing_dialog_share_weibo,R.id.liveing_dialog_share_qq,R.id.liveing_dialog_share_qzone,R.id.liveing_dialog_cancel})
    public void clickBtn(View view) {
        switch (view.getId()) {
            case R.id.liveing_dialog_share_friends:
                sharepPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.liveing_dialog_share_wechat:
                sharepPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.liveing_dialog_share_weibo:
                sharepPlatform(SHARE_MEDIA.SINA);
                break;
            case R.id.liveing_dialog_share_qq:
                break;
            case R.id.liveing_dialog_share_qzone:
                break;
            case R.id.liveing_dialog_cancel:
                dismiss();
                break;
        }
        dismiss();
    }

    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    private void sharepPlatform(SHARE_MEDIA share_media) {
        ShareAction sp = new ShareAction(scanForActivity(getContext()));
        sp.setPlatform(share_media);
        sp.setCallback(new MyUMListener());
        if (shareVO.getBitmap() != null) {
            Bitmap bitmap = shareVO.getBitmap();
            UMImage image = new UMImage(scanForActivity(getContext()), bitmap);
            sp.withMedia(image);
            sp.share();
            return;
        }

        if (shareVO.getImg() != null) {
            UMImage image = new UMImage(scanForActivity(getContext()), shareVO.getImg());
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
            contentLoader.getShareStatistics(String.valueOf(shareVO.getTargetType()), String.valueOf(shareVO.getTargetId()), share_media.equals(SHARE_MEDIA.SINA) ? "2" : (share_media.equals(SHARE_MEDIA.WEIXIN) ? "1" : "0"));
            if (onSuccessShare != null) {
                onSuccessShare.shareSuccess(share_media);
            }
            if (share_media.equals(SHARE_MEDIA.SINA)) {
                Toast.makeText(getContext(), "微博分享成功!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                Toast.makeText(getContext(), "微信分享成功!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                Toast.makeText(getContext(), "微信朋友圈分享成功!", Toast.LENGTH_SHORT).show();
            }

        }

        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if (onSuccessShare != null) {
                onSuccessShare.shareSuccess(share_media);
            }
            if (share_media.equals(SHARE_MEDIA.SINA)) {
                Toast.makeText(getContext(), "微博分享失败!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                Toast.makeText(getContext(), "微信分享失败!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                Toast.makeText(getContext(), "微信朋友圈分享失败!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

            if (share_media.equals(SHARE_MEDIA.SINA)) {
                Toast.makeText(getContext(), "已取消微博分享!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                Toast.makeText(getContext(), "已取消微信分享!", Toast.LENGTH_SHORT).show();
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                Toast.makeText(getContext(), "已取消微信朋友圈分享!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private OnSuccessShareListener onSuccessShare;

    public interface OnSuccessShareListener {
        void shareSuccess(SHARE_MEDIA share_media);
    }

    public void setOnSuccessShare(OnSuccessShareListener onSuccessShare) {
        this.onSuccessShare = onSuccessShare;
    }

}