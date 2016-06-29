package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
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
import com.lalocal.lalocal.util.DrawableUtils;
import java.util.ArrayList;
import java.util.List;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by lenovo on 2016/6/17.
 * 专题详情页
 */
public class SpecialDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView detailsLike;
    private ImageView detailsShare;
    private WebView specialWebView;
    private ContentService contentService;
    private WebSettings settings;
    private ContentService contentService1;
    private LinearLayout mainUi;
    private ProgressBar pb;


    private LinearLayout titleContent;
    private Context mContext = SpecialDetailsActivity.this;
    private SpecialShareVOBean shareVO;
    private Object praiseId1;
    private ArticleDetailsBean articleDetailsBean;
    private List<ArticleDetailsBean> articleDetailsBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_details_layout);
        ShareSDK.initSDK(this);
        initView();
        initData();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb.setVisibility(View.GONE);
            mainUi.setVisibility(View.VISIBLE);

        }
    };


    private void initView() {
        back = (ImageView) findViewById(R.id.common_back_btn);
        detailsLike = (ImageView) findViewById(R.id.special_details_like_iv);
        detailsShare = (ImageView) findViewById(R.id.special_details_share_iv);
        titleContent = (LinearLayout) findViewById(R.id.special_title_content);
        pb = (ProgressBar) findViewById(R.id.loading);
        mainUi = (LinearLayout) findViewById(R.id.special_main_ui);

        mainUi.setVisibility(View.INVISIBLE);
        back.setOnClickListener(this);
        detailsLike.setOnClickListener(this);
        detailsShare.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_back_btn:
                finish();
                break;
            case R.id.special_details_like_iv:
                if (praiseFlag) {
                    contentService1.cancelParises(praiseId);//取消赞
                } else {
                    contentService1.specialPraise(targetId, 1);//点赞
                }
                break;
            case R.id.special_details_share_iv:
                if(shareVO!=null){
                    showShare(shareVO);
                }

                break;
        }
    }

    //分享
    private void showShare(SpecialShareVOBean shareVO) {
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(shareVO.title);
        oks.setText(shareVO.url);
        if(shareVO.img!=null){
           oks.setImageUrl(shareVO.img);
        }
        oks.setUrl(shareVO.url);
        oks.setSiteUrl(shareVO.url);
// 启动分享GUI
        oks.show(this);
    }




private boolean praiseFlag=false;//是否已赞
SpecialBannerBean bannerBean;
private String photourl;

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
    private int targetType;
    private int targetId;
    private int praiseId;
    private String h5Url;
    public class MyCallBack extends ICallBack {
    @Override
    public void onRecommendSpecial(SpectialDetailsResp spectialDetailsResp) {
        super.onRecommendSpecial(spectialDetailsResp);
        if (spectialDetailsResp.returnCode == 0) {
            getArticDetailsData(spectialDetailsResp);
          //  int praiseNum = spectialDetailsResp.result.groups.get(0).relationList.get(0).praiseNum;
            h5Url = spectialDetailsResp.result.url;
            shareVO = spectialDetailsResp.result.shareVO;
            praiseId1 = spectialDetailsResp.result.praiseId;
          //  int i = targetId = spectialDetailsResp.result.groups;
            targetType = spectialDetailsResp.result.type;
            Toast.makeText(SpecialDetailsActivity.this, "praiseId:"+spectialDetailsResp.result.praiseId, Toast.LENGTH_SHORT).show();
            if (praiseId1 !=null) {
                detailsLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
                praiseFlag=true;
            } else {
                detailsLike.setImageResource(R.drawable.index_icon_like);
            }
            bannerBean = spectialDetailsResp.result.banner;
            if (bannerBean != null) {
                int type = bannerBean.type;
                //type : 画报类型 0 为图⽚  1为视频
                if (type == 0) {
                    // 显示图片和文字
                    showArtworkAndText(bannerBean);
                } else if (type == 1) {
                    // 播放视频
                    String videoUrl = bannerBean.videoUrl;
                    playVideo(videoUrl);
                }
            } else {
                //没有内容，显示当前图片
                photourl = spectialDetailsResp.result.photo;
                showArtwork(photourl);

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
            praiseFlag = true;
            Toast.makeText(SpecialDetailsActivity.this, "红"+"pariseResult:"+pariseResult.getResult(), Toast.LENGTH_SHORT).show();

        }
        //取消
        @Override
        public void onPariseResult(PariseResult pariseResult) {
            super.onPariseResult(pariseResult);
            praiseFlag = false;
            Toast.makeText(SpecialDetailsActivity.this, "白", Toast.LENGTH_SHORT).show();
            detailsLike.setImageResource(R.drawable.index_icon_like);
        }

    }
    //获取显示h5页面所需数据
    private void getArticDetailsData(SpectialDetailsResp spectialDetailsResp) {
        articleDetailsBeanList = new ArrayList<>();
        List<SpecialGroupsBean> groups = spectialDetailsResp.result.groups;
        for(int i=0;i<groups.size();i++){
            SpecialGroupsBean specialGroupsBean = groups.get(i);
            List<RelationListBean> relationList = specialGroupsBean.relationList;
            for(int j=0 ;j<relationList.size();j++){
                articleDetailsBean = new ArticleDetailsBean();
                articleDetailsBean.setPhone(relationList.get(j).photo);
                articleDetailsBean.setPraiseNum(relationList.get(j).praiseNum);
                articleDetailsBean.setReadNum(relationList.get(j).readNum);
                articleDetailsBean.setTargetName(relationList.get(j).targetName);
                articleDetailsBean.setTargetId(relationList.get(j).targetId);
                articleDetailsBean.setPraises(relationList.get(j).id);
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 1000);
    }

    //显示图片和文字
    private void showArtworkAndText(final SpecialBannerBean bannerBean) {
        final BigPictureBean bean=new BigPictureBean();
        bean.setContent(bannerBean.content);
        bean.setName(bannerBean.authorName);
        bean.setImgUrl(bannerBean.videoScreenShot);
        String videoScreenShot = bannerBean.videoScreenShot;
        View inflate = View.inflate(mContext, R.layout.photo_to_text, null);
        TextView textContent = (TextView) inflate.findViewById(R.id.photo_to_text_content);
        ImageView photoIv= (ImageView) inflate.findViewById(R.id.photo_to_text_iv);
        TextView textName = (TextView) inflate.findViewById(R.id.photo_to_text_name);
        DrawableUtils.displayImg(mContext, photoIv, videoScreenShot);
        textContent.setText(bannerBean.content);
        textName.setText("- -"+bannerBean.authorName);
        titleContent.addView(inflate);
        inflate.setOnClickListener(new View.OnClickListener() {
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
        View inflate = View.inflate(mContext, R.layout.video_layout, null);
        final VideoView viewById = (VideoView) inflate.findViewById(R.id.video);
        // 检查包能不能使用
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }

        titleContent.addView(inflate);
        viewById.setVideoPath(videoUrl);
        viewById.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                viewById.start();
            }
        });
        // 设置控制器
   //  viewById.setMediaController(new io.vov.vitamio.widget.MediaController(this));
       /* titleContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, VideoActivity.class);
                intent1.putExtra("video", videoUrl);
                startActivity(intent1);
            }
        });*/
    }

    //显示图片
    private void showArtwork(final String photourl) {
        ImageView img = new ImageView(mContext);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        titleContent.addView(img);
        DrawableUtils.displayImg(mContext, img, photourl);
        //点击图片放大
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, BigPictureActivity.class);
                intent1.putExtra("imageurl", photourl);
                startActivity(intent1);
                overridePendingTransition(R.anim.head_in,R.anim.head_out);
            }
        });

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
                    if(articleDetailsBeanList!=null){
                        for(int i=0;i<articleDetailsBeanList.size();i++){
                            if(specialToH5Bean.getTargetId()==articleDetailsBeanList.get(i).getTargetId()){
                                Intent intent1 = new Intent(mContext, ArticleActivity.class);
                                articleDetailsBeanList.get(i).setTargetType(1);
                                intent1.putExtra("articleDetailsBean", articleDetailsBeanList.get(i));
                                startActivity(intent1);

                            }
                        }
                    }

                    break;
                case 2:
                    //产品,去产品详情页
                    Intent intent2 = new Intent(mContext, ProductDetailsActivity.class);
                    intent2.putExtra("productdetails",specialToH5Bean);
                    //intent2.putExtra("productdetails", specialToH5Bean.getTargetId() + "");
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


    }

}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);

    }
}