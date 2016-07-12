package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsResp;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.HashMap;
import java.util.zip.Inflater;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;


/**
 * Created by lenovo on 2016/6/21.
 */
public class ArticleActivity extends BaseActivity implements View.OnClickListener,PlatformActionListener,Callback {
    private WebView articleWebview;
    private ShineButton btnLike;
    private ImageView btnComment;
    private ImageView btnShare;
    private TextView readTv;
    private TextView collectTv;
    private ContentService contentService;
    private Context mContext=ArticleActivity.this;
    private LinearLayout back;
    private View placeHolder;
    private WebSettings settings;
    private boolean praiseFlag;//是否点赞
    private ArticleDetailsResultBean articleDetailsRespResult;
    private String targetID;
    private Object praiseId;//点赞id
    private ImageView unLike;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_test);
        ShareSDK.initSDK(this);
        Intent intent = getIntent();
        targetID = intent.getStringExtra("targetID");
        initView();
        initData(targetID);


    }



    private void initView() {
        articleWebview = (WebView) findViewById(R.id.webview);
        btnLike = (ShineButton) findViewById(R.id.article_btn_like);
        btnComment = (ImageView) findViewById(R.id.article_btn_comment);
        btnShare = (ImageView) findViewById(R.id.article_btn_share);
        readTv = (TextView) findViewById(R.id.article_read_tv);
        collectTv = (TextView) findViewById(R.id.article_collect_tv);
        back = (LinearLayout) findViewById(R.id.article_back_btn);
        btnLike.setImageResource(R.drawable.index_article_btn_like);

        placeHolder = findViewById(R.id.place_holder);

        //点击事件
        btnLike.setOnClickListener(this);
        btnComment.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        back.setOnClickListener(this);


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.article_btn_comment:
                //评论
                Toast.makeText(mContext,"评论功能尚未开启，敬请期待。。。", Toast.LENGTH_SHORT).show();
                break;
            case R.id.article_btn_share:
                //分享
                if(articleDetailsRespResult!=null){
                    showShare(articleDetailsRespResult.getShareVO());
                }

                break;
            case R.id.article_btn_like:
                //点赞

                if(praiseFlag&&praiseId!=null){
                    contentService.cancelParises(praiseId);//取消赞
                }else {
                    contentService.specialPraise(Integer.parseInt(targetID), 1);
                }
                break;

        }
    }

    private void initData( String targetID) {
        contentService=new ContentService(this);
        contentService.setCallBack(new MyCallBack());
        contentService.articleDetails(targetID);

    }



    public class MyCallBack extends ICallBack {
        @Override
        public void onArticleResult(ArticleDetailsResp articleDetailsResp) {
            super.onArticleResult(articleDetailsResp);
            articleDetailsRespResult = articleDetailsResp.getResult();
            if(articleDetailsRespResult !=null){
                praiseId = articleDetailsRespResult.getPraiseId();
                String url = articleDetailsRespResult.getUrl();
                praiseFlag = articleDetailsRespResult.isPraiseFlag();
                if(praiseFlag){

                    btnLike.setChecked(true);

                }else {
                    btnLike.setChecked(false);

                }
                if(!TextUtils.isEmpty(url)){
                    initWebview(url);//显示h5
                }

                showReadAndCollect(articleDetailsRespResult);//阅读和收藏数

            }

        }

        @Override
        public void onPariseResult(PariseResult pariseResult) {//取消赞
            super.onPariseResult(pariseResult);
            if(pariseResult.getReturnCode()==0){
                praiseFlag=false;
                btnLike.setChecked(false);

            }

        }

        @Override
        public void onInputPariseResult(PariseResult pariseResult) {//点赞
            super.onInputPariseResult(pariseResult);
            if(pariseResult.getReturnCode()==0){
                praiseId= pariseResult.getResult();
                praiseFlag=true;
                btnLike.setChecked(true);


            }
        }
    }



    private void showReadAndCollect(ArticleDetailsResultBean articleDetailsRespResult) {
        readTv.setText("阅读 " + articleDetailsRespResult.getReadNum());
        collectTv.setText("  收藏 " + articleDetailsRespResult.getPraiseNum());
    }


    private void initWebview(String url) {
        settings = articleWebview.getSettings();
        if(Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        if(Build.VERSION.SDK_INT >= 21){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptEnabled(true);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        articleWebview.setBackgroundColor(Color.parseColor("#000000"));
        articleWebview.loadUrl(url);
        articleWebview.setWebViewClient(new MyWebViewClient());

    }

    boolean isLoading=true;
    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
         //   lalocal://codeImageClick?{"name":"Lalocal","wechatNo":"%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB%E5%98%BB",
            // "imageUrl":"http://7xpid3.com1.z0.glb.clouddn.com/20160615142707308988042388"}
           // String[] split = url.split("\\?");
            if(url.matches(".*codeImageClick.*")){
                //TODO 作者二维码
                showPopupWindow();
                return true;
            }else if (url.matches(".*app.*")){
                String[] split = url.split("\\?");
                String json = split[1];
                SpecialToH5Bean specialToH5Bean = new Gson().fromJson(json, SpecialToH5Bean.class);
                if(specialToH5Bean.getTargetType()==2){
                    //TODO 去商品详情
                    Intent intent2 = new Intent(mContext, ProductDetailsActivity.class);
                    intent2.putExtra("productdetails", specialToH5Bean);
                    startActivity(intent2);
                    return  false;
                }else {
                    return  true;
                }

            }
            AppLog.i("TAG",url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(!articleWebview.getSettings().getLoadsImagesAutomatically()) {
                articleWebview.getSettings().setLoadsImagesAutomatically(true);
            }
        }
    }

    private void showPopupWindow() {
        Toast.makeText(this,"ahhaaa",Toast.LENGTH_SHORT).show();
        PopupWindow pw=new PopupWindow(this);
        View view =View.inflate(this,R.layout.author_pop_layout,null);
        pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pw.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pw.setContentView(view);
        pw.setFocusable(true);
        pw.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        pw.setBackgroundDrawable(dw);

        pw.showAtLocation(this.findViewById(R.id.article_relayout),
                Gravity.CENTER, 0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && articleWebview.canGoBack()) {
            articleWebview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private int praisesNum;




    //分享
    private void showShare(SpecialShareVOBean shareVO) {

        SharePopupWindow  shareActivity = new SharePopupWindow(mContext, shareVO);
        shareActivity.setPlatformActionListener(this);
        shareActivity.showShareWindow();
        shareActivity.showAtLocation(ArticleActivity.this.findViewById(R.id.article_relayout),
                Gravity.CENTER, 0, 0);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
    }
}
