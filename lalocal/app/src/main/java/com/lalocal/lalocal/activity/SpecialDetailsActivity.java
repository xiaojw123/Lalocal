package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsBean;
import com.lalocal.lalocal.model.BigPictureBean;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.RelationListBean;
import com.lalocal.lalocal.model.SpecialBannerBean;
import com.lalocal.lalocal.model.SpecialGroupsBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.MyScrollView;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.mob.tools.utils.UIHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


/**
 * Created by lenovo on 2016/6/17.
 * 专题详情页
 */
public class SpecialDetailsActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener, Callback, MyScrollView.ScrollViewListener {
    private ImageView back;
    private ImageView detailsLike;
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
    private VideoView videoView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.special_details_layout);
        initView();
        initData();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.common_back_btn);
        detailsLike = (ImageView) findViewById(R.id.special_details_like_iv);
        detailsShare = (ImageView) findViewById(R.id.special_details_share_iv);
        loadingPage = (LinearLayout) findViewById(R.id.loading_page);
        mainUi = (LinearLayout) findViewById(R.id.special_main_ui);
        mScrollview = (MyScrollView) findViewById(R.id.special_scrollview);
        loadingImg = (ImageView) findViewById(R.id.special_details_loading);
        photoLayout = (RelativeLayout) findViewById(R.id.photo_to_text);
        imgLayout = (LinearLayout) findViewById(R.id.special_details_img);
        videoLayoout = (RelativeLayout) findViewById(R.id.layout_video);
        main = (LinearLayout) findViewById(R.id.mian);
        videoView = (VideoView) findViewById(R.id.video);
        banerContent = (RelativeLayout) findViewById(R.id.special_title_content);


        back.setOnClickListener(this);
        detailsLike.setOnClickListener(this);
        detailsShare.setOnClickListener(this);
        mScrollview.setScrollViewListener(this);
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
            case R.id.common_back_btn:
                finish();
                break;
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
        }
    }

    public class MyCallBack extends ICallBack {
        @Override
        public void onRecommendSpecial(SpectialDetailsResp spectialDetailsResp) {
            super.onRecommendSpecial(spectialDetailsResp);
            if (spectialDetailsResp.returnCode == 0) {
                getArticDetailsData(spectialDetailsResp);
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
                Toast.makeText(SpecialDetailsActivity.this, "praiseId:" + spectialDetailsResp.result.praiseId, Toast.LENGTH_SHORT).show();
                if (praiseFlag) {
                    detailsLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
                } else {
                    detailsLike.setImageResource(R.drawable.index_article_btn_like);
                }
                bannerBean = spectialDetailsResp.result.banner;
                if (bannerBean != null) {
                    int type = bannerBean.type;
                    //type : 画报类型 0 为图⽚  1为视频
                    if (type == 0) {
                        // 显示图片和文字
                        videoLayoout.setVisibility(View.GONE);
                        photoLayout.setVisibility(View.VISIBLE);
                        imgLayout.setVisibility(View.GONE);
                        showArtworkAndText(bannerBean);
                    } else if (type == 1) {
                        // 播放视频
                        String videoUrl = bannerBean.videoUrl;
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
                Toast.makeText(SpecialDetailsActivity.this, "Special，returnCode=1", Toast.LENGTH_SHORT).show();

            }
            //显示h5页面
            showWebview(h5Url);

        }

        //点赞
        @Override
        public void onInputPariseResult(PariseResult pariseResult) {
            super.onInputPariseResult(pariseResult);
            detailsLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
            praiseId1 = pariseResult.getResult();
            praiseFlag = true;
            Toast.makeText(SpecialDetailsActivity.this, "红" + "pariseResult:" + pariseResult.getResult(), Toast.LENGTH_SHORT).show();

        }

        //取消
        @Override
        public void onPariseResult(PariseResult pariseResult) {
            super.onPariseResult(pariseResult);
            Toast.makeText(SpecialDetailsActivity.this, "白", Toast.LENGTH_SHORT).show();
            detailsLike.setImageResource(R.drawable.index_article_btn_like);
            praiseFlag = false;
        }
    }

    //获取显示h5页面所需数据
    private void getArticDetailsData(SpectialDetailsResp spectialDetailsResp) {
        articleDetailsBeanList = new ArrayList<>();
        List<SpecialGroupsBean> groups = spectialDetailsResp.result.groups;
        for (int i = 0; i < groups.size(); i++) {
            SpecialGroupsBean specialGroupsBean = groups.get(i);
            List<RelationListBean> relationList = specialGroupsBean.relationList;
            for (int j = 0; j < relationList.size(); j++) {
                articleDetailsBean = new ArticleDetailsBean();
                articleDetailsBean.setPhone(relationList.get(j).photo);
                articleDetailsBean.setPraiseNum(relationList.get(j).praiseNum);
                articleDetailsBean.setReadNum(relationList.get(j).readNum);
                articleDetailsBean.setTargetName(relationList.get(j).targetName);
                articleDetailsBean.setTargetId(relationList.get(j).targetId);
                articleDetailsBean.setPraises(relationList.get(j).id);
                articleDetailsBean.setTargetType(targetType1);
                articleDetailsBeanList.add(articleDetailsBean);
            }
        }
    }


    //显示h5页面
    private void showWebview(String h5Url) {
        specialWebView = (WebView) findViewById(R.id.special_details_webview);
        settings = specialWebView.getSettings();
        specialWebView.loadUrl(h5Url);
        settings.setJavaScriptEnabled(true);
        specialWebView.setWebViewClient(new MyWebViewClient());

    }

    //显示图片和文字
    private void showArtworkAndText(final SpecialBannerBean bannerBean) {
        mScrollview.scrollTo(0, 0);
        final BigPictureBean bean = new BigPictureBean();
        bean.setContent(bannerBean.content);
        bean.setName(bannerBean.authorName);
        bean.setImgUrl(bannerBean.videoScreenShot);
        String videoScreenShot = bannerBean.videoScreenShot;

        TextView textContent = (TextView) findViewById(R.id.photo_to_text_content);
        ImageView photoIv = (ImageView) findViewById(R.id.photo_to_text_iv);
        TextView textName = (TextView) findViewById(R.id.photo_to_text_name);
        DrawableUtils.displayImg(mContext, photoIv, videoScreenShot);
        textContent.setText(bannerBean.content);
        textName.setText("- -" + bannerBean.authorName);
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
        loadingImg.setScaleType(ImageView.ScaleType.FIT_XY);
        final BigPictureBean bean = new BigPictureBean();
        bean.setImgUrl(photourl);
        DrawableUtils.displayImg(mContext, loadingImg, photourl);
        Toast.makeText(mContext, photourl, Toast.LENGTH_SHORT).show();
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


        // 检查包能不能使用
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }
        videoView.setVideoPath(videoUrl);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                videoView.start();
            }
        });
        FrameLayout ll = (FrameLayout) findViewById(R.id.ll);
        MediaController mediaController = new MediaController(this, true, ll);
        mediaController.setVisibility(View.GONE);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

    }

    //滚动监听，超出视屏区域，暂停播放视屏
    @Override
    public void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy) {
        int i = DensityUtil.dip2px(mContext, 200);
        if (y > 600) {
            videoView.pause();
        } else {
            videoView.start();
        }

    }


    boolean isLoading = true;

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Toast.makeText(SpecialDetailsActivity.this, url, Toast.LENGTH_SHORT).show();
            String[] split = url.split("\\?");
            String json = split[1];
            SpecialToH5Bean specialToH5Bean = new Gson().fromJson(json, SpecialToH5Bean.class);

            if (specialToH5Bean != null) {
                switch (specialToH5Bean.getTargetType()) {
                    case 1:
                        //文章，调h5接口
                        if (articleDetailsBeanList != null) {
                            for (int i = 0; i < articleDetailsBeanList.size(); i++) {
                                if (specialToH5Bean.getTargetId() == articleDetailsBeanList.get(i).getTargetId()) {
                                    Intent intent1 = new Intent(mContext, ArticleActivity.class);
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

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (isLoading) {
                specialWebView.reload();
                isLoading = false;
            }
            loadingPage.setVisibility(View.GONE);

        }
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