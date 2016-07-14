package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsResp;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.SpecialAuthorBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

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
    private ContentLoader contentService;
    private Context mContext=ArticleActivity.this;
    private LinearLayout back;
    private View placeHolder;
    private WebSettings settings;
    private boolean praiseFlag;//是否点赞
    private ArticleDetailsResultBean articleDetailsRespResult;
    private String targetID;
    private Object praiseId;//点赞id
    private ImageView unLike;
    private SpecialAuthorBean authorVO;//作者二维码
    private PopupWindow pw;
    private ImageView authorCodeImg;
    private Bitmap image;
    private int praiseNum;
    private int readNum;

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
                Toast.makeText(mContext,"评论功能尚未开启，敬请期待...", Toast.LENGTH_SHORT).show();
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
            case R.id.author_code_close:
                pw.dismiss();//关闭popuwindow

                break;
            case R.id.author_code_download:

                if(authorCodeImg!=null){
                    image = ((BitmapDrawable) authorCodeImg.getDrawable()).getBitmap();
                    saveImageToGallery(mContext, image);//保存图片到相册
                }

                break;

        }
    }



    private void initData( String targetID) {
        contentService=new ContentLoader(this);
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
                authorVO = articleDetailsRespResult.getAuthorVO();
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
                praiseNum = articleDetailsRespResult.getPraiseNum();
                readNum = articleDetailsRespResult.getReadNum();
                showReadAndCollect(praiseNum, readNum);//阅读和收藏数

            }

        }

        @Override
        public void onPariseResult(PariseResult pariseResult) {//取消赞
            super.onPariseResult(pariseResult);
            if(pariseResult.getReturnCode()==0){
                praiseFlag=false;
                btnLike.setChecked(false);
                praiseNum= praiseNum - 1;
                collectTv.setText(" · 收藏 " + praiseNum);

            }

        }

        @Override
        public void onInputPariseResult(PariseResult pariseResult) {//点赞
            super.onInputPariseResult(pariseResult);
            if(pariseResult.getReturnCode()==0){
                praiseId= pariseResult.getResult();
                praiseFlag=true;
                btnLike.setChecked(true);
                praiseNum=praiseNum + 1;
                collectTv.setText(" · 收藏 " + praiseNum);

            }
        }
    }



    private void showReadAndCollect(int praiseNum, int readNum) {
        readTv.setText("阅读 " +readNum );
        collectTv.setText(" · 收藏 " + praiseNum);
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
           // String[] split = url.split("\\?");
            AppLog.i("TAG",url);
            if(url.matches(".*codeImageClick.*")){
                //TODO 作者二维码
                if(authorVO!=null){
                    showPopupWindow(authorVO);
                }

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

    private void showPopupWindow(SpecialAuthorBean authorVO) {



        pw = new PopupWindow(this);
        View view =View.inflate(this,R.layout.author_pop_layout,null);
        LinearLayout codeDownload= (LinearLayout) view.findViewById(R.id.author_code_download);
        LinearLayout popLayout= (LinearLayout) view.findViewById(R.id.pop_layout);
        authorCodeImg = (ImageView) view.findViewById(R.id.author_code_iv);
        TextView authorName= (TextView) view.findViewById(R.id.author_name_tv);
        ImageView codeClose= (ImageView) view.findViewById(R.id.author_code_close);
        TextView authorContent= (TextView) view.findViewById(R.id.author_content);
        AppLog.i("TAG",authorVO.qrCode.toString());
        DrawableUtils.displayImg(mContext, authorCodeImg,authorVO.qrCode.toString());
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        authorName.setText(authorVO.publicTitle.toString());
        authorContent.setText(authorVO.publicDescription.toString());
        codeClose.setOnClickListener(this);
        codeDownload.setOnClickListener(this);
        pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pw.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pw.setContentView(view);
        pw.setFocusable(true);
        pw.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        pw.setBackgroundDrawable(dw);

        pw.showAtLocation(this.findViewById(R.id.article_relayout),
                Gravity.CENTER, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
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

    //保存二维码图片到相册
    public  void saveImageToGallery(Context context, Bitmap bmp) {
        if (bmp == null){
            Toast.makeText(context,"保存出错",Toast.LENGTH_SHORT).show();
            return;
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "ywq");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context,"保存出错",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(context,"保存出错",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch (Exception e){
            Toast.makeText(context,"保存出错",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // 最后通知图库更新
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
            pw.dismiss();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);


    }




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
