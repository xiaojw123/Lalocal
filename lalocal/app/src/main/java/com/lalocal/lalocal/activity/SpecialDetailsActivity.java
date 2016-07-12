package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsBean;
import com.lalocal.lalocal.model.BigPictureBean;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.SpecialBannerBean;
import com.lalocal.lalocal.model.SpecialGroupsBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.MyScrollView;
import com.lalocal.lalocal.view.SecretTextView;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.mob.tools.utils.UIHandler;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;




/**
 * Created by lenovo on 2016/6/17.
 * 专题详情页
 */
public class SpecialDetailsActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener, Callback, MyScrollView.ScrollViewListener {
    private ShineButton detailsLike;
    private ImageView detailsShare;
    private WebView specialWebView;
    private ContentService contentService;
    private WebSettings settings;
    private ContentService contentService1;
    private LinearLayout mainUi;
    private Context mContext = SpecialDetailsActivity.this;
    private SpecialShareVOBean shareVO;
    private Object praiseId1;
    private ArticleDetailsBean articleDetailsBean;
    private List<ArticleDetailsBean> articleDetailsBeanList;
    private int targetType1;
    private SuperVideoPlayer videoView;
    private MyScrollView mScrollview;
    private ImageView loadingImg;
    private RelativeLayout photoLayout;
    private LinearLayout imgLayout;
    private RelativeLayout videoLayoout;
    private String description;
    private SharePopupWindow shareActivity;
    private LinearLayout main;
    private RelativeLayout banerContent;
    private LinearLayout loadingPage;
    private boolean praiseFlag;//是否已赞
    SpecialBannerBean bannerBean;
    private String photourl;
    private int targetType;
    private int targetId;
    private int praiseId;
    private String h5Url;
    private View mPlayBtnView;
    private RelativeLayout relativeLayout;
    private View line;
    private boolean isPause;
    private SecretTextView textContent;
    private SecretTextView textName;
    private int bannerType=5;
    public static final int RESULT_PLAY=0;
    private String videoUrl;//视频播放地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.special_details_layout);
        initView();
        initData();
    }
    private void initView() {
        detailsLike = (ShineButton) findViewById(R.id.special_details_like_iv);
        detailsShare = (ImageView) findViewById(R.id.special_details_share_iv);
        loadingPage = (LinearLayout) findViewById(R.id.loading_page);
        mainUi = (LinearLayout) findViewById(R.id.special_main_ui);
        mScrollview = (MyScrollView) findViewById(R.id.special_scrollview);
        loadingImg = (ImageView) findViewById(R.id.special_details_loading);
        photoLayout = (RelativeLayout) findViewById(R.id.photo_to_text);
        imgLayout = (LinearLayout) findViewById(R.id.special_details_img);
        videoLayoout = (RelativeLayout) findViewById(R.id.layout_video);
        main = (LinearLayout) findViewById(R.id.mian);
        videoView = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        banerContent = (RelativeLayout) findViewById(R.id.special_title_content);
        line = findViewById(R.id.special_line);
        relativeLayout = (RelativeLayout) findViewById(R.id.reLayout);
        mPlayBtnView = findViewById(R.id.play_btn);
        mPlayBtnView.setOnClickListener(this);
        detailsLike.setOnClickListener(this);
        detailsShare.setOnClickListener(this);
        mScrollview.setScrollViewListener(this);
        videoView.setVideoPlayCallback(mVideoPlayCallback);


    }

    private void initData() {
        final Intent intent = getIntent();
        String rowId = intent.getStringExtra("rowId");
        String url = AppConfig.SPECIAL_DETAILS_URL + rowId;

        if (rowId != null) {
            contentService1 = new ContentService(this);
            contentService1.setCallBack(new MyCallBack());
            contentService1.specialDetail(rowId);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.special_details_like_iv:
                if (praiseFlag) {
                    contentService1.cancelParises(praiseId1);//取消赞
                } else {
                    contentService1.specialPraise(targetId, 10);//点赞
                }
                break;
            case R.id.special_details_share_iv:
                if (shareVO != null) {
                    showShare(shareVO);
                }
                break;
            case R.id.play_btn:
                mPlayBtnView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                videoView.setAutoHideController(false);
                Uri uri = Uri.parse("http://media.lalocal.cn/video/mov/balidao.mov");
                videoView.loadAndPlay(uri, 0);
                break;
            case R.id.fullscreen_back_btn:
                hideFullScreen();
                break;

        }
    }

    Handler handler=new Handler(){
        @Override
        public String getMessageName(Message message) {

            switch (message.what){
                case RESULT_PLAY:

                    break;
            }
            return super.getMessageName(message);
        }
    };



    public class MyCallBack extends ICallBack {
        @Override
        public void onRecommendSpecial(SpectialDetailsResp spectialDetailsResp) {
            super.onRecommendSpecial(spectialDetailsResp);

            if (spectialDetailsResp.returnCode == 0) {

                h5Url = spectialDetailsResp.result.url;
                shareVO = spectialDetailsResp.result.shareVO;
                praiseId1 = spectialDetailsResp.result.praiseId;
                praiseFlag = spectialDetailsResp.result.praiseFlag;
                description = spectialDetailsResp.result.description;
                targetId = spectialDetailsResp.result.id;
                List<SpecialGroupsBean> groups = spectialDetailsResp.result.groups;
                for (int i = 0; i < groups.size(); i++) {
                    targetType1 = groups.get(i).targetType;
                }

                if (praiseFlag) {
                    //   detailsLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
                    detailsLike.setChecked(true);
                } else {
                    //  detailsLike.setImageResource(R.drawable.index_article_btn_like);
                    detailsLike.setChecked(false);
                }
                bannerBean = spectialDetailsResp.result.banner;

                if (bannerBean != null) {
                    bannerType = bannerBean.type;
                    //type : 画报类型 0 为图⽚  1为视频
                    if (bannerType == 0) {
                        // 显示图片和文字
                        videoLayoout.setVisibility(View.GONE);
                        photoLayout.setVisibility(View.VISIBLE);
                        imgLayout.setVisibility(View.GONE);
                        showArtworkAndText(bannerBean);
                    } else if (bannerType == 1) {
                        // 播放视频
                        videoUrl = bannerBean.videoUrl;
                        photoLayout.setVisibility(View.GONE);
                        imgLayout.setVisibility(View.GONE);
                        videoLayoout.setVisibility(View.VISIBLE);
                        playVideo(videoUrl);
                    }
                } else {
                    //没有内容，显示当前图片
                    photourl = spectialDetailsResp.result.photo;
                    videoLayoout.setVisibility(View.GONE);
                    photoLayout.setVisibility(View.GONE);
                    imgLayout.setVisibility(View.VISIBLE);

                    if (photourl != null) {
                        showArtwork(photourl);
                    }
                }
            } else {
                //TODO  待处理

            }
            //显示h5页面
            showWebview(h5Url);

        }

        //点赞
        @Override
        public void onInputPariseResult(PariseResult pariseResult) {
            super.onInputPariseResult(pariseResult);
            // detailsLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
            detailsLike.setChecked(true);
            praiseId1 = pariseResult.getResult();
            praiseFlag = true;


        }

        //取消
        @Override
        public void onPariseResult(PariseResult pariseResult) {
            super.onPariseResult(pariseResult);

            //  detailsLike.setImageResource(R.drawable.index_article_btn_like);
            detailsLike.setChecked(false);
            praiseFlag = false;
        }
    }



    //显示图片和文字
    private void showArtworkAndText(final SpecialBannerBean bannerBean) {
        final BigPictureBean bean = new BigPictureBean();
        bean.setContent(bannerBean.content);
        bean.setName(bannerBean.authorName);
        bean.setImgUrl(bannerBean.videoScreenShot);
        bean.setShare(true);
        String videoScreenShot = bannerBean.videoScreenShot;
        textContent = (SecretTextView) findViewById(R.id.photo_to_text_content);
        ImageView photoIv = (ImageView) findViewById(R.id.photo_to_text_iv);
        textName = (SecretTextView) findViewById(R.id.photo_to_text_name);
        DrawableUtils.displayImg(mContext, photoIv, videoScreenShot);
        textContent.setText(bannerBean.content);
        if(!"".equals(bannerBean.authorName)){
            textName.setText("- -" + bannerBean.authorName);
        }
        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, BigPictureActivity.class);
                intent1.putExtra("bigbean", bean);
                startActivity(intent1);
                overridePendingTransition(R.anim.head_in, R.anim.head_out);
            }
        });
    }


    //显示图片
    private void showArtwork(final String photourl) {
        loadingImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final BigPictureBean bean = new BigPictureBean();
        bean.setImgUrl(photourl);
        DrawableUtils.displayImg(mContext, loadingImg, photourl);

        //点击图片放大
        loadingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, BigPictureActivity.class);
                intent1.putExtra("bigbean", bean);
                startActivity(intent1);
                overridePendingTransition(R.anim.head_in, R.anim.head_out);

            }
        });
    }

    //播放视频
    private void playVideo(final String videoUrl) {
        mPlayBtnView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.setAutoHideController(true);
        Uri uri = Uri.parse(videoUrl);
        videoView.loadAndPlay(uri, 0);
        videoView.pausePlay(true);
    }

    /**
     * 播放器的回调函数
     */
    private boolean isFullScreen=true;//判断是否全屏
    LinearLayout heightLayout;
    private boolean isPlayStatus=true;//视频播放状态
    private boolean isPageFinish=false;//h5页面加载状态
    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            videoView.close();//关闭VideoView
            mPlayBtnView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            resetPageToPortrait();
        }

        @Override
        public void onSwitchPageType() {
            if(isFullScreen){
               showFullScreen();
            }else {
              hideFullScreen();
            }
        }

        /**
         * 播放完成回调
         */
        @Override
        public void onPlayFinish() {
        }

        @Override
        public void onPlayStatus(boolean isPlay) {
              isPlayStatus=isPlay;
        }


    };
    //隐藏全屏
    private void hideFullScreen() {
        isFullScreen=true;
        specialWebView.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        main.setBackgroundColor(Color.WHITE);
        line.setVisibility(View.VISIBLE);
        heightLayout.setVisibility(View.GONE);
        heightLayout.removeAllViews();
    }

    //显示全屏
    private void showFullScreen() {
        isFullScreen=false;
        specialWebView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);
        main.setBackgroundColor(Color.BLACK);
        line.setVisibility(View.GONE);
        heightLayout= (LinearLayout) findViewById(R.id.height_layout);
        View inflate = View.inflate(mContext, R.layout.fullsreen_back_layout, null);
        ImageView playerBack= (ImageView) inflate.findViewById(R.id.fullscreen_back_btn);
        heightLayout.addView(inflate);
        heightLayout.setVisibility(View.VISIBLE);
        View view=new View(mContext);
        LinearLayout.LayoutParams sp_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int height = getWindowManager().getDefaultDisplay().getHeight();
        sp_params.height=(height-900)/2;
        heightLayout.addView(view,sp_params);
      playerBack.setOnClickListener(this);
    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            videoView.setPageType(MediaController.PageType.SHRINK);
        }
    }
    //显示h5页面
    private void showWebview(String h5Url) {
        specialWebView = (WebView) findViewById(R.id.special_details_webview);
        if(Build.VERSION.SDK_INT >= 19) {
            specialWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            specialWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        settings = specialWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        specialWebView.loadUrl(h5Url);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if(Build.VERSION.SDK_INT >= 21){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        specialWebView.setWebViewClient(new MyWebViewClient());

    }

    //滚动监听，超出视屏区域，暂停播放视屏
    @Override
    public void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy) {
        int i = DensityUtil.dip2px(mContext, 200);
        if (y > i) {
            isPause = videoView.pausePlay(false);
            isPause=true;
        } else if(isPause){
            isPause=false;
            if(!isPlayStatus){
                videoView.pausePlay(false);
            }else {
                videoView.goOnPlay();
            }

        }
    }

    boolean isLoading = true;
    boolean isNextLoading=true;
    int loadingCount=0;

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            AppLog.i("TAG","shouldOverrideUrlLoading:"+url);
            String[] split = url.split("\\?");
            String json = split[1];
            // targetType=1&targetId=230;
           /* String[] split2=json.split("&");
            String split3 = split2[0];
            String[] split4 = split3.split("Type=");
            String targetTy = split4[1];
            String[] split1 = json.split("Id=");


            String targetID = split1[1];
            int targetIDD=Integer.parseInt(targetID);
            int targetTY = Integer.parseInt(targetTy);*/
           // SpecialToH5Bean specialToH5Bean=new SpecialToH5Bean();

             SpecialToH5Bean specialToH5Bean = new Gson().fromJson(json, SpecialToH5Bean.class);

           /* specialToH5Bean.setTargetId(targetIDD);
            specialToH5Bean.setTargetType(targetTY);*/
            if (specialToH5Bean != null) {
                switch (specialToH5Bean.getTargetType()) {
                    case 1:
                        //文章，调h5接口
                        if (articleDetailsBeanList != null) {
                            for (int i = 0; i < articleDetailsBeanList.size(); i++) {
                                if (specialToH5Bean.getTargetId() == articleDetailsBeanList.get(i).getTargetId()) {
                                    Intent intent1 = new Intent(mContext, ArticleTestAct.class);
                                    intent1.putExtra("articleDetailsBean", articleDetailsBeanList.get(i));
                                    startActivity(intent1);
                                }
                            }
                        }
                        break;
                    case 2:
                        //产品,去产品详情页
                        Intent intent2 = new Intent(mContext, ProductDetailsActivity.class);
                        intent2.putExtra("productdetails", specialToH5Bean);
                        startActivity(intent2);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
            }

            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }


        boolean isShowAnim=true;//画报文字动画

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(!specialWebView.getSettings().getLoadsImagesAutomatically()) {
                specialWebView.getSettings().setLoadsImagesAutomatically(true);
            }
            ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            String typeName = networkInfo.getTypeName();


            //文字动画
            if(bannerType==0&&isShowAnim){
                isShowAnim=false;
                textContent.toggle();
                textContent.setIsVisible(true);
                textContent.setDuration(3000);
                textName.toggle();
                textName.setIsVisible(true);
                textName.setDuration(1500);

            }
            if(typeName.equalsIgnoreCase("WIFI")&&isLoading){
                isLoading=false;
                isNextLoading=false;
                loadingCount=loadingCount+1;
                specialWebView.reload();
                isPageFinish=true;
                Message message = new Message();
                message.what =RESULT_PLAY;
                handler.sendMessage(message);
            }
            if(typeName.equalsIgnoreCase("WIFI")&&loadingCount==1){
                loadingPage.setVisibility(View.GONE);
            }
            if(!typeName.equalsIgnoreCase("WIFI")){
                loadingPage.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (specialWebView!=null&&(keyCode == KeyEvent.KEYCODE_BACK) && specialWebView.canGoBack()) {
            specialWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //分享
    private void showShare(SpecialShareVOBean shareVO) {
        shareActivity = new SharePopupWindow(mContext, shareVO);
        shareActivity.setPlatformActionListener(this);
        shareActivity.showShareWindow();
        shareActivity.showAtLocation(SpecialDetailsActivity.this.findViewById(R.id.mian),
                Gravity.CENTER, 0, 0);

    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        if (what == 1) {
            Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
        }
        if (shareActivity != null) {
            shareActivity.dismiss();
        }
        return false;
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Message msg = new Message();
        msg.what = 1;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = 0;
        UIHandler.sendMessage(msg, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);

    }
}