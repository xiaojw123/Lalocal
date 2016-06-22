package com.lalocal.lalocal.activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.SpecialBannerBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.service.ContentService;

import com.lalocal.lalocal.service.callback.ICallBack;

import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;

/**
 * Created by lenovo on 2016/6/17.
 * 专题详情页
 */
public class SpecialDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView back;
    private ImageView detailsLike;
    private ImageView detailsShare;
    private WebView specialWebView;
    private VideoView videoView;
    private ImageView pictorial;
    private TextView detailsTvContent;
    private TextView detailsTvAuthorName;
    private RelativeLayout detailsRl;
    private  ContentService contentService;
    private WebSettings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_details_layout);
        initView();
        initData();

    }
    private void initView() {
        back = (ImageView) findViewById(R.id.common_back_btn);
        videoView = (VideoView) findViewById(R.id.special_details_pictorial_video);
        detailsLike = (ImageView) findViewById(R.id.special_details_like_iv);
        detailsShare = (ImageView) findViewById(R.id.special_details_share_iv);
        pictorial = (ImageView) findViewById(R.id.special_details_pictorial_iv);
        detailsTvContent = (TextView) findViewById(R.id.spcial_details_tv_content);
        detailsTvAuthorName = (TextView) findViewById(R.id.spcial_details_tv_authorName);
        detailsRl = (RelativeLayout) findViewById(R.id.special_details_rl);

        back.setOnClickListener(this);
        detailsLike.setOnClickListener(this);
        detailsShare.setOnClickListener(this);
        pictorial.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_back_btn:
                finish();
                break;
            case R.id.special_details_like_iv:
                if(praiseFlag){
                    praiseFlag=false;

                    detailsLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
                }else {
                    praiseFlag=true;
                    detailsLike.setImageResource(R.drawable.index_article_btn_like);
                }

                break;
            case R.id.special_details_share_iv:

                break;

            case R.id.special_details_pictorial_iv:
                Toast.makeText(SpecialDetailsActivity.this, "gggg", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(SpecialDetailsActivity.this, BigPictureActivity.class);
                if(bannerBean!=null){
                    intent1.putExtra("imageurl", bannerBean.photo);
                    startActivity(intent1);
                }else if(photourl!=null){
                    intent1.putExtra("imageurl", photourl);
                    startActivity(intent1);
                }
                break;
        }
    }
    private boolean praiseFlag;//是否已赞
    SpecialBannerBean bannerBean;
    private String photourl;
    private void initData() {
        final Intent intent = getIntent();
        String rowId = intent.getStringExtra("rowId");

         String url= AppConfig.SPECIAL_DETAILS_URL+rowId;

        if(rowId!=null){
            ContentService contentService = new ContentService(this);
            contentService.setCallBack(new MyCallBack());
            contentService.specialDetail(rowId);
        }
    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onRecommendSpecial(SpectialDetailsResp spectialDetailsResp) {
            super.onRecommendSpecial(spectialDetailsResp);

            if(spectialDetailsResp.returnCode==0){
                String h5Url = spectialDetailsResp.result.url;
                praiseFlag = spectialDetailsResp.result.praiseFlag;
                Object praiseId = spectialDetailsResp.result.praiseId;
                if(praiseFlag){
                    detailsLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
                }else{
                    detailsLike.setImageResource(R.drawable.index_article_btn_like);
                }
                AppLog.i("h5url", h5Url);
                specialWebView = (WebView) findViewById(R.id.special_details_webview);

               settings=specialWebView.getSettings();
                specialWebView.loadUrl(h5Url);
                settings.setJavaScriptEnabled(true); //如果访问的页面中有Javascript，则WebView必须设置支持Javascript
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                settings.setSupportZoom(true); //支持缩放
                settings.setBuiltInZoomControls(true); //支持手势缩放
                settings.setDisplayZoomControls(false); //是否显示缩放按钮

                // >= 19(SDK4.4)启动硬件加速，否则启动软件加速
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    specialWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    settings.setLoadsImagesAutomatically(true); //支持自动加载图片
                } else {
                    specialWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    settings.setLoadsImagesAutomatically(false);
                }

                settings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
                settings.setLoadWithOverviewMode(true); //自适应屏幕
                settings.setDomStorageEnabled(true);
                settings.setSaveFormData(true);
                settings.setSupportMultipleWindows(true);
                settings.setAppCacheEnabled(true);
                settings.setCacheMode(WebSettings.LOAD_DEFAULT); //优先使用缓存
                specialWebView.setHorizontalScrollbarOverlay(true);
                specialWebView.setHorizontalScrollBarEnabled(false);
                specialWebView.setOverScrollMode(View.OVER_SCROLL_NEVER); // 取消WebView中滚动或拖动到顶部、底部时的阴影
                specialWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 取消滚动条白边效果
                specialWebView.requestFocus();
                specialWebView.setWebViewClient(new MyWebViewClient());
                    bannerBean=spectialDetailsResp.result.banner;
                    if(bannerBean!=null)

                    {
                        int type = bannerBean.type;
                        //type : 画报类型 0 为图⽚  1为视频
                        if (type == 0) {
                            //TODO 显示图片
                            videoView.setVisibility(View.GONE);
                            pictorial.setVisibility(View.VISIBLE);
                            String videoScreenShot = bannerBean.videoScreenShot;
                            detailsTvContent.setText(bannerBean.content);
                            detailsTvAuthorName.setText("- - " + bannerBean.authorName);
                            DrawableUtils.displayImg(SpecialDetailsActivity.this, pictorial, bannerBean.photo);
                        } else if (type == 1) {
                            //TODO 播放视频
                            String videoUrl = bannerBean.videoUrl;
                            videoView.setVisibility(View.VISIBLE);
                            detailsRl.setVisibility(View.GONE);
                            videoView.setVideoPath(videoUrl);
                            videoView.start();
                            Toast.makeText(SpecialDetailsActivity.this, "视频", Toast.LENGTH_SHORT).show();
                            AppLog.i("videoUrl", videoUrl);
                        }
                    }

                    else

                    {
                        //没有内容，显示当前图片
                        pictorial.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.GONE);
                        detailsTvAuthorName.setVisibility(View.GONE);
                        detailsTvContent.setVisibility(View.GONE);
                        photourl = spectialDetailsResp.result.photo;
                        DrawableUtils.displayImg(SpecialDetailsActivity.this, pictorial, photourl);
                        Toast.makeText(SpecialDetailsActivity.this, "为空！！！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                //TODO  待处理
                Toast.makeText(SpecialDetailsActivity.this, "Special，returnCode=1", Toast.LENGTH_SHORT).show();

            }


        }
    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Toast.makeText(SpecialDetailsActivity.this,url,Toast.LENGTH_SHORT).show();
            String[] split = url.split("\\?");
            String json = split[1];
            SpecialToH5Bean specialToH5Bean = new Gson().fromJson(json, SpecialToH5Bean.class);
            if(specialToH5Bean!=null){
          switch (specialToH5Bean.getTargetType()){
              case "1":
                  //文章，调h5接口
                        Intent intent1 = new Intent(SpecialDetailsActivity.this, ArticleActivity.class);
                      intent1.putExtra("articl",specialToH5Bean.getTargetId() + "");
                      startActivity(intent1);
                  break;
              case "2":
                  //产品,去产品详情页
                  Intent intent2=new Intent(SpecialDetailsActivity.this,ProductDetailsActivity.class);
                  intent2.putExtra("productdetails",specialToH5Bean.getTargetId()+"");
                  startActivity(intent2);
                  break;
              case "3":

                  break;
              case "4":

                  break;
              case "5":

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

        }
    }
}