package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsBean;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by lenovo on 2016/6/21.
 */
public class ArticleActivity extends BaseActivity implements View.OnClickListener{
    private WebView articleWebview;
    private ArticleDetailsBean articleDetailsBean;
    private ImageView btnLike;
    private ImageView btnComment;
    private ImageView btnShare;
    private TextView readTv;
    private TextView collectTv;
    private ImageView backBtn;
    private ContentService contentService;
    private int targetId;
    private int targetType;
    private int praises;
    private int result;
    private Context mContext=ArticleActivity.this;
    private LinearLayout back;
    private View placeHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ShareSDK.initSDK(this);
        Intent intent = getIntent();
        articleDetailsBean = intent.getParcelableExtra("articleDetailsBean");
        initView();
        initWebview();
        initData();


    }



    private void initView() {
        articleWebview = (WebView) findViewById(R.id.article_webview);
        btnLike = (ImageView) findViewById(R.id.article_btn_like);
        btnComment = (ImageView) findViewById(R.id.article_btn_comment);
        btnShare = (ImageView) findViewById(R.id.article_btn_share);
        readTv = (TextView) findViewById(R.id.article_read_tv);
        collectTv = (TextView) findViewById(R.id.article_collect_tv);
        backBtn = (ImageView) findViewById(R.id.common_back_btn);
        back = (LinearLayout) findViewById(R.id.article_back_btn);
        placeHolder = findViewById(R.id.place_holder);

        //点击事件
        btnLike.setOnClickListener(this);
        btnComment.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    private void initData() {
        readTv.setText("阅读 " + articleDetailsBean.getReadNum());
        collectTv.setText("  收藏 " + articleDetailsBean.getPraiseNum());
        targetId = articleDetailsBean.getTargetId();
        targetType = articleDetailsBean.getTargetType();
        praises = articleDetailsBean.getPraises();
        contentService = new ContentService(this);
        contentService.setCallBack(new MyCallBack());

    }
    private void initWebview() {

        articleWebview.getSettings().setJavaScriptEnabled(true);
        articleWebview.loadUrl("https://dev.lalocal.cn/wechat/app_article?id=" + articleDetailsBean.getTargetId());
        MyWebViewClient webViewClient=new MyWebViewClient();
        articleWebview.setWebViewClient(webViewClient);

    }




boolean isPraises=true;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.article_back_btn:
                finish();
                break;
            case R.id.article_btn_comment:
                //评论
                contentService.cancelParises(result);//取消赞
                break;
            case R.id.article_btn_share:
                //分享
                showShare();
                break;
            case R.id.article_btn_like:
                //点赞
                contentService.specialPraise(targetId, targetType);
              /*  if(isPraises){
                    contentService.cancelParises(praises);//取消赞
                }else {
                    contentService.specialPraise(targetId, targetType);
                }*/
                break;
        }
    }



    boolean isLoading=true;
    class  MyWebViewClient extends  WebViewClient{
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //lalocal://codeImageClick?{"name":"Lalocal","wechatNo":"%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB","imageUrl":"http://7xpid3.com1.z0.glb.clouddn.com/20160615142707308988042388"}
              /*  String[] split = url.split("codeImageClick\\?");
                String json = split[1];*/
                AppLog.i("000000000000000000000",url);
                return  true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                AppLog.i("1111111111111111111111111111", url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                AppLog.i("1111111111111111111111111111",url);
                placeHolder.setVisibility(View.GONE);
                if(isLoading){
                    isLoading=false;
                    articleWebview.loadUrl("https://dev.lalocal.cn/wechat/app_article?id=" + articleDetailsBean.getTargetId());
                }
            }
        }

    private int praisesNum;
    public class MyCallBack extends ICallBack {
        //点赞
        @Override
        public void onInputPariseResult(PariseResult pariseResult) {
            super.onInputPariseResult(pariseResult);

            btnLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
            result = pariseResult.getResult();
            praisesNum = articleDetailsBean.getPraiseNum() + 1;
            collectTv.setText("  收藏 " +praisesNum);

        }
        //取消赞
        @Override
        public void onPariseResult(PariseResult pariseResult) {
            super.onPariseResult(pariseResult);
            btnLike.setImageResource(R.drawable.index_icon_like);
            int i =praisesNum-1;
            collectTv.setText("  收藏 " + i);

        }
    }

    //分享
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(articleDetailsBean.getTargetName());
        oks.setText(articleDetailsBean.getTargetName());

           oks.setImageUrl("http://7xpid3.com1.z0.glb.clouddn.com/2016051320515814900627825168?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200");

        oks.setUrl("https://dev.lalocal.cn/wechat/app_article?id=" + articleDetailsBean.getTargetId());
        oks.setSiteUrl("https://dev.lalocal.cn/wechat/app_article?id=" + articleDetailsBean.getTargetId());
// 启动分享GUI
        oks.show(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
    }
}
