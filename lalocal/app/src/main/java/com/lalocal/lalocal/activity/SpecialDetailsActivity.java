package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.SpecialBannerBean;
import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.util.APPcofig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;

/**
 * Created by lenovo on 2016/6/17.
 * 专题详情页
 */
public class SpecialDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private RequestQueue mQueue;
    private ImageView back;
    private ImageView detailsLike;
    private ImageView detailsShare;
    private ContentService contentService;
    private WebView specialWebView;
    private VideoView videoView;
    private ImageView pictorial;
    private TextView detailsTvContent;
    private TextView detailsTvAuthorName;
    private RelativeLayout detailsRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_details_layout);
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        initView();
        initData();

    }
    private void initView() {
        back = (ImageView) findViewById(R.id.common_back_btn);
        videoView = (VideoView) findViewById(R.id.special_details_pictorial_video);
        detailsLike = (ImageView) findViewById(R.id.special_details_like_iv);
        detailsShare = (ImageView) findViewById(R.id.special_details_share_iv);
        specialWebView = (WebView) findViewById(R.id.special_details_webview);
        pictorial = (ImageView) findViewById(R.id.special_details_pictorial_iv);
        detailsTvContent = (TextView) findViewById(R.id.spcial_details_tv_content);
        detailsTvAuthorName = (TextView) findViewById(R.id.spcial_details_tv_authorName);
        detailsRl = (RelativeLayout) findViewById(R.id.special_details_rl);
        specialWebView.getSettings().setJavaScriptEnabled(true);
        back.setOnClickListener(this);
        detailsLike.setOnClickListener(this);
        detailsShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_back_btn:
                finish();
                break;
            case R.id.special_details_like_iv:

                break;
            case R.id.special_details_share_iv:

                break;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        String rowId = intent.getStringExtra("rowId");
         String url= APPcofig.SPECIAL_DETAILS_URL+rowId;
        if(rowId!=null){
           StringRequest mRequest= new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    SpectialDetailsResp spectialDetailsResp = new Gson().fromJson(s, SpectialDetailsResp.class);
                    AppLog.i("spectialDetailsResp", s);
                    if(spectialDetailsResp.returnCode==0){
                        String h5Url = spectialDetailsResp.result.url;
                        AppLog.i("h5url",h5Url);
                        specialWebView.loadUrl(h5Url);
                        specialWebView.setDownloadListener(new MyWebViewDownLoadListener());
                        SpecialBannerBean bannerBean = spectialDetailsResp.result.banner;
                        if(bannerBean!=null){
                            int type = bannerBean.type;
                            //type : 画报类型 0 为图⽚  1为视频
                            if(type==0){
                                //TODO 显示图片
                                videoView.setVisibility(View.GONE);
                                pictorial.setVisibility(View.VISIBLE);
                                String videoScreenShot = bannerBean.videoScreenShot;
                                detailsTvContent.setText(bannerBean.content);
                                detailsTvAuthorName.setText("- - "+bannerBean.authorName);
                                DrawableUtils.displayImg(SpecialDetailsActivity.this, pictorial,bannerBean.photo);

                            }else if(type==1){
                                //TODO 播放视频
                                String videoUrl = bannerBean.videoUrl;

                                videoView.setVisibility(View.VISIBLE);
                                detailsRl.setVisibility(View.GONE);
                                videoView.setVideoPath(videoUrl);
                                videoView.start();
                                Toast.makeText(SpecialDetailsActivity.this,"视频",Toast.LENGTH_SHORT).show();
                                AppLog.i("videoUrl", videoUrl);
                            }
                        }else{
                            //没有内容，显示当前图片
                            pictorial.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.GONE);
                            detailsTvAuthorName.setVisibility(View.GONE);
                            detailsTvContent.setVisibility(View.GONE);
                            DrawableUtils.displayImg(SpecialDetailsActivity.this, pictorial, spectialDetailsResp.result.photo);
                            Toast.makeText(SpecialDetailsActivity.this, "为空！！！", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //TODO  待处理
                        Toast.makeText(SpecialDetailsActivity.this, "Special，returnCode=1", Toast.LENGTH_SHORT).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SpecialDetailsActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
            mQueue.add(mRequest);
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        //添加监听事件即可
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype,long contentLength)          {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
           // Toast.makeText(SpecialDetailsActivity.this, "点击了", Toast.LENGTH_SHORT).show();
        }
    }
}
