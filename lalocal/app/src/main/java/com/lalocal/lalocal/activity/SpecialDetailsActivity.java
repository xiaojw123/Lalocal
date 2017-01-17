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
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.TargetPage;
import com.lalocal.lalocal.help.TargetType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackDetailActivity;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.ArticleDetailsBean;
import com.lalocal.lalocal.model.BigPictureBean;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.SpecialBannerBean;
import com.lalocal.lalocal.model.SpecialGroupsBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.MyScrollView;
import com.lalocal.lalocal.view.SecretTextView;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;




/**
 * Created by lenovo on 2016/6/17.
 * 专题详情页
 */
public class SpecialDetailsActivity extends BaseActivity implements View.OnClickListener, MyScrollView.ScrollViewListener, CustomTitleView.onBackBtnClickListener,UMShareListener {
    private ShineButton detailsLike;
    private ImageView detailsShare;
    private WebView specialWebView;
    private ContentLoader contentService;
    private WebSettings settings;
    private ContentLoader contentService1;
    private LinearLayout mainUi;
    private Context mContext = SpecialDetailsActivity.this;
    private SpecialShareVOBean shareVO;
    private Object praiseId1;
    private ArticleDetailsBean articleDetailsBean;
    private int targetType1;
    private SuperVideoPlayer videoView;
    private MyScrollView mScrollview;
    private ImageView loadingImg;
    private RelativeLayout photoLayout;
    private LinearLayout imgLayout;
    private RelativeLayout videoLayoout;
    private String description;
    //  private SharePopupWindow shareActivity;
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


    private boolean isPause;
    private SecretTextView textContent;
    private SecretTextView textName;
    private int bannerType = 5;
    public static final int RESULT_PLAY = 0;
    private String videoUrl;//视频播放地址
    private ImageView backTitleView;
    private TextView readTv;
    private TextView collectTv;
    private LinearLayout bottomLayout;
    private int readNum;
    private int praiseNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_details_layout);
        initView();
        initData();
    }
    private void initView() {
        backTitleView = (ImageView) findViewById(R.id.special_details_ctv);
        detailsLike = (ShineButton) findViewById(R.id.article_btn_like);
        detailsShare = (ImageView) findViewById(R.id.article_btn_share);
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
        bottomLayout = (LinearLayout) findViewById(R.id.article_bottom_layout);
        readTv = (TextView) findViewById(R.id.article_read_tv);
        collectTv = (TextView) findViewById(R.id.article_collect_tv);
        mPlayBtnView = findViewById(R.id.play_btn);
        findViewById(R.id.article_btn_comment).setVisibility(View.GONE);
        backTitleView.setOnClickListener(this);
        mPlayBtnView.setOnClickListener(this);
        detailsLike.setOnClickListener(this);
        detailsShare.setOnClickListener(this);
        mScrollview.setScrollViewListener(this);
        videoView.setVideoPlayCallback(mVideoPlayCallback);
    }

    private void initData() {
        final Intent intent = getIntent();
        String rowId = intent.getStringExtra("rowId");
        AppLog.i("TAG","专题详情:"+rowId);
        String url = AppConfig.getSepcailDetailUrl() + rowId;
        if (rowId != null) {
            contentService1 = new ContentLoader(this);
            contentService1.setCallBack(new MyCallBack());
            contentService1.specialDetail(rowId);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.article_btn_like:
                if (praiseFlag) {
                    contentService1.cancelParises(praiseId1, targetId);//取消赞
                } else {
                    contentService1.specialPraise(targetId, 10);//点赞
                }
                break;
            case R.id.article_btn_share:
                if (shareVO != null) {
                    SharePopupWindow shareActivity = new SharePopupWindow(mContext,shareVO);
                    shareActivity.show();
                }
                break;
            case R.id.play_btn:

                break;
            case R.id.fullscreen_back_btn:
                hideFullScreen();
                break;
            case R.id.special_details_ctv:
                finish();
                break;

        }
    }

    Handler handler = new Handler() {
        @Override
        public String getMessageName(Message message) {
            switch (message.what) {
                case RESULT_PLAY:
                    break;
            }
            return super.getMessageName(message);
        }
    };

    @Override
    public void onBackClick() {
        setResult(MyFavoriteActivity.UPDATE_MY_DATA);
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
    }
    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }


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
                readNum = spectialDetailsResp.result.readNum;
                praiseNum = spectialDetailsResp.result.praiseNum;
                readTv.setText("阅读 "+readNum);
                collectTv.setText("· 收藏 "+praiseNum);
                List<SpecialGroupsBean> groups = spectialDetailsResp.result.groups;
                for (int i = 0; i < groups.size(); i++) {
                    targetType1 = groups.get(i).targetType;
                }

                if (praiseFlag) {

                    detailsLike.setChecked(true);
                } else {

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
            praiseNum=praiseNum+1;
            collectTv.setText("· 收藏 "+praiseNum);

        }

        //取消
        @Override
        public void onPariseResult(PariseResult pariseResult) {
            super.onPariseResult(pariseResult);
            detailsLike.setChecked(false);
            praiseFlag = false;
            praiseNum=praiseNum-1;
            collectTv.setText("· 收藏 "+praiseNum);
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
        if (!"".equals(bannerBean.authorName)) {
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
        bean.setShare(false);
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

        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        String typeName = networkInfo.getTypeName();

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
    private boolean isFullScreen = true;//判断是否全屏
    LinearLayout heightLayout;
    private boolean isPlayStatus = true;//视频播放状态
    private boolean isPageFinish = false;//h5页面加载状态
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
            if (isFullScreen) {
                showFullScreen();
            } else {
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
            isPlayStatus = isPlay;
        }

    };

    //隐藏全屏
    private void hideFullScreen() {
        isFullScreen = true;
        specialWebView.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
        main.setBackgroundColor(Color.WHITE);
        heightLayout.setVisibility(View.GONE);
        heightLayout.removeAllViews();
        videoView.setPageType(MediaController.PageType.SHRINK);
    }

    //显示全屏
    private void showFullScreen() {
        isFullScreen = false;
        videoView.setPageType(MediaController.PageType.EXPAND);
        isFullScreen=false;
        specialWebView.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
        main.setBackgroundColor(Color.BLACK);

        heightLayout = (LinearLayout) findViewById(R.id.height_layout);
        View inflate = View.inflate(mContext, R.layout.fullsreen_back_layout, null);
        ImageView playerBack = (ImageView) inflate.findViewById(R.id.fullscreen_back_btn);
        heightLayout.addView(inflate);
        heightLayout.setVisibility(View.VISIBLE);
        View view = new View(mContext);
        LinearLayout.LayoutParams sp_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int height = getWindowManager().getDefaultDisplay().getHeight();
        sp_params.height = (height - 900) / 2;
        heightLayout.addView(view, sp_params);
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
        if (Build.VERSION.SDK_INT >= 19) {
            specialWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            specialWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        settings = specialWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        specialWebView.loadUrl(h5Url);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 21) {
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
            isPause = true;
        } else if (isPause) {
            isPause = false;
            if (!isPlayStatus) {
                videoView.pausePlay(false);
            } else {
                videoView.goOnPlay();
            }
        }
    }

    boolean isLoading = true;
    boolean isNextLoading = true;
    int loadingCount = 0;

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("lalocal:")) {
                int startIndex = url.indexOf("?") + 1;
                String jsonData = url.substring(startIndex, url.length());
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String targetType = jsonObject.optString("targetType");
                    String targetId = jsonObject.optString("targetId");
                    String targetUrl = jsonObject.optString("targetUrl");
                    if (!TextUtils.isEmpty(targetType)) {
                        //优惠券跳转
                        if (TargetType.COUPON.equals(targetType)) {
                            if (UserHelper.isLogined(mContext)) {
                                Intent intent = new Intent(mContext, MyCouponActivity.class);
                                intent.putExtra(KeyParams.PAGE_TYPE, KeyParams.PAGE_TYPE_WALLET);
                                mContext.startActivity(intent);
                            } else {
                                LLoginActivity.start(mContext);
                            }
                            return true;
                        }
                        //直播视频跳转
                        if (TargetType.LIVE_VIDEO.equals(targetType)) {
                            Intent intent = new Intent(mContext, AudienceActivity.class);
                            intent.putExtra("id", targetId);
                            mContext.startActivity(intent);
                            return true;
                        }
                        //直播回放跳转
                        if (TargetType.LIVE_PALY_BACK.equals(targetType)) {
                            Intent intent = new Intent(mContext, PlayBackDetailActivity.class);
                            intent.putExtra("id", targetId);
                            mContext.startActivity(intent);
                            return true;
                        }
                        if (TargetType.URL.equals(targetType)) {
                            TargetPage.gotoWebDetail(mContext, targetUrl, null, false);
                            return true;
                        }

                        if (TargetType.USER.equals(targetType) || TargetType.AUTHOR.equals(targetType)) {
                            TargetPage.gotoUser(mContext, targetId, false);
                            return true;
                        }
                        if (TargetType.ARTICLE.equals(targetType) || TargetType.INFORMATION.equals(targetType)) {
                            TargetPage.gotoArticleDetail(mContext, targetId, false);
                            return true;
                        }
                        if (TargetType.PRODUCT.equals(targetType)) {
                            TargetPage.gotoProductDetail(mContext, targetId, targetType, false);
                            return true;
                        }
                        if (TargetType.ROUTE.equals(targetType)) {
                            TargetPage.gotoRouteDetail(mContext, targetId, false);
                            return true;
                        }
                        if (TargetType.SPECIAL.equals(targetType)) {
                            TargetPage.gotoSpecialDetail(mContext, targetId, false);
                            return true;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }


        boolean isShowAnim = true;//画报文字动画

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!specialWebView.getSettings().getLoadsImagesAutomatically()) {
                specialWebView.getSettings().setLoadsImagesAutomatically(true);
            }
            ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            String typeName = networkInfo.getTypeName();


            //文字动画
            if (bannerType == 0 && isShowAnim) {
                isShowAnim = false;
                textContent.toggle();
                textContent.setIsVisible(true);
                textContent.setDuration(3000);
                textName.toggle();
                textName.setIsVisible(true);
                textName.setDuration(1500);

            }
            if (typeName.equalsIgnoreCase("WIFI") && isLoading) {
                isLoading = false;
                isNextLoading = false;
                loadingCount = loadingCount + 1;
                specialWebView.reload();
                isPageFinish = true;
                Message message = new Message();
                message.what = RESULT_PLAY;
                handler.sendMessage(message);
            }
            if (typeName.equalsIgnoreCase("WIFI") && loadingCount == 1) {
                loadingPage.setVisibility(View.GONE);
                videoView.goOnPlay();
            }
            if (!typeName.equalsIgnoreCase("WIFI")) {
                loadingPage.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (specialWebView != null && (keyCode == KeyEvent.KEYCODE_BACK) && specialWebView.canGoBack()) {
            specialWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        AppLog.print("onBackPressed_____special__");
        setResult(MyFavoriteActivity.UPDATE_MY_DATA);
        super.onBackPressed();
    }

}