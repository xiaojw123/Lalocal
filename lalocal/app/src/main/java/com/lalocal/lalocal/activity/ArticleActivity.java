package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.model.ArticleDetailsResp;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.SpecialAuthorBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.WebViewClientUrlSkipUtils;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by lenovo on 2016/6/21.
 */
public class ArticleActivity extends BaseActivity implements View.OnClickListener {
    private WebView articleWebview;
    private ShineButton btnLike;
    private ImageView btnComment;
    private ImageView btnShare;
    private TextView readTv;
    private TextView collectTv;
    private ContentLoader contentService;
    private Context mContext = ArticleActivity.this;
    private LinearLayout back;
    private WebSettings settings;
    private boolean praiseFlag;//是否点赞
    private ArticleDetailsResultBean articleDetailsRespResult;
    private String targetID;
    private Object praiseId;//点赞id
    private ImageView unLike;
    private SpecialAuthorBean authorVO;//作者二维码
    private ImageView backBtn;
    private PopupWindow pw;
    private ImageView authorCodeImg;
    private Bitmap image;
    private int praiseNum;
    private int readNum;
    private BlurImageView blurImageView;
    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_test);

        Intent intent = getIntent();
        targetID = intent.getStringExtra("targetID");
        initView();
        initData(targetID);

    }

    private void initView() {
        backBtn = (ImageView) findViewById(R.id.article_common_back_btn);
        blurImageView = (BlurImageView) findViewById(R.id.article_bg);
        articleWebview = (WebView) findViewById(R.id.webview);
        btnLike = (ShineButton) findViewById(R.id.article_btn_like);
        btnComment = (ImageView) findViewById(R.id.article_btn_comment);
        btnShare = (ImageView) findViewById(R.id.article_btn_share);
        readTv = (TextView) findViewById(R.id.article_read_tv);
        collectTv = (TextView) findViewById(R.id.article_collect_tv);
        back = (LinearLayout) findViewById(R.id.article_back_btn);
        btnLike.setImageResource(R.drawable.index_article_btn_like);

        //点击事件
        backBtn.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        btnComment.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.article_common_back_btn:
                AppLog.print("onclick___");
                setResult(MyFavoriteActivity.UPDATE_MY_DATA);
                finish();
                break;

            case R.id.article_btn_comment:
                //评论


                Intent commentIntent = new Intent(ArticleActivity.this, ArticleCommentActivity.class);
                commentIntent.putExtra(Constants.KEY_ARTICLE_ID, Integer.parseInt(targetID));
                ArticleActivity.this.startActivity(commentIntent);
                break;
            case R.id.article_btn_share:
                //分享
                if (articleDetailsRespResult != null) {
                    showShare(articleDetailsRespResult.getShareVO());
                }

                break;
            case R.id.article_btn_like:
                //点赞

                if (praiseFlag && praiseId != null) {
                    contentService.cancelParises(praiseId, Integer.parseInt(targetID));//取消赞
                } else {
                    contentService.specialPraise(Integer.parseInt(targetID), articleDetailsRespResult.getTargetType());
                }
                break;
            case R.id.author_code_close:
                pw.dismiss();
                break;
            case R.id.author_code_download:

                if (authorCodeImg != null) {

                    image = ((BitmapDrawable) authorCodeImg.getDrawable()).getBitmap();

                    if (fileIsExists()) {
                        Toast.makeText(mContext, "已下载过啦！", Toast.LENGTH_SHORT).show();
                    } else {
                        saveImageToGallery(mContext, image);//保存图片到相册
                    }


                }

                break;

        }
    }

    private void showShare(SpecialShareVOBean shareVO) {
        SharePopupWindow shareActivity = new SharePopupWindow(mContext,shareVO);
        shareActivity.show();
    }

    //判断二维码是否下载过
    public boolean fileIsExists() {

        try {

            File appDir = new File(Environment.getExternalStorageDirectory(), "ywq");
            String fileName = authorVO.authorId + ".jpg";
            File file = new File(appDir, fileName);
            if (!file.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    private void initData(String targetID) {
        contentService = new ContentLoader(this);
        contentService.setCallBack(new MyCallBack());
        contentService.articleDetails(targetID);
    }


    public class MyCallBack extends ICallBack {


        @Override
        public void onArticleResult(ArticleDetailsResp articleDetailsResp) {
            super.onArticleResult(articleDetailsResp);

            articleDetailsRespResult = articleDetailsResp.getResult();
            if (articleDetailsRespResult != null) {
                praiseId = articleDetailsRespResult.getPraiseId();
                authorVO = articleDetailsRespResult.getAuthorVO();
                String url = articleDetailsRespResult.getUrl();
                praiseFlag = articleDetailsRespResult.isPraiseFlag();
                String photo = articleDetailsRespResult.getPhoto();
                userId = articleDetailsResp.getResult().getId();
                if (photo != null) {
                    blurImageView.setBlurImageURL(photo);
                    blurImageView.setBlurRadius(1);
                    blurImageView.setScaleRatio(20);
                }

                if (praiseFlag) {

                    btnLike.setChecked(true);

                } else {
                    btnLike.setChecked(false);

                }
                if (!TextUtils.isEmpty(url)) {
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
            if (pariseResult.getReturnCode() == 0) {
                praiseFlag = false;
                btnLike.setChecked(false);
                praiseNum = praiseNum - 1;
                collectTv.setText(" · 收藏 " + praiseNum);

            }

        }

        @Override
        public void onInputPariseResult(PariseResult pariseResult) {//点赞
            super.onInputPariseResult(pariseResult);
            if (pariseResult.getReturnCode() == 0) {
                praiseId = pariseResult.getResult();
                praiseFlag = true;
                btnLike.setChecked(true);
                praiseNum = praiseNum + 1;
                collectTv.setText(" · 收藏 " + praiseNum);

            }
        }
    }


    private void showReadAndCollect(int praiseNum, int readNum) {
        readTv.setText("阅读 " + readNum);
        collectTv.setText(" · 收藏 " + praiseNum);
    }

    private void initWebview(String url) {
        settings = articleWebview.getSettings();
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptEnabled(true);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        articleWebview.setBackgroundColor(0);
        articleWebview.loadUrl(url);
        articleWebview.setWebViewClient(new MyWebViewClient());

    }

    boolean isLoading = true;

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            AppLog.i("TAG", "shouldOverrideUrlLoading" + url);
            if (url.matches(".*codeImageClick.*")) {
                // 作者二维码
                if (authorVO != null) {
                    showPopupWindow(authorVO);
                }
            }
            if(url.matches(".*imageClick.*")){
                String[] split = url.split("imageClick:");
                String s = split[1];
                String[] split1 = s.split(",");
                AppLog.i("TAG","文章评论图片点击:"+split1[1]);
            }
            if (url.matches(".*app.*")) {
               return WebViewClientUrlSkipUtils.getUrlPrasent(url,ArticleActivity.this);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }

    private void showPopupWindow(SpecialAuthorBean authorVO) {

        pw = new PopupWindow(this);
        View view = View.inflate(this, R.layout.author_pop_layout, null);
        LinearLayout codeDownload = (LinearLayout) view.findViewById(R.id.author_code_download);
        LinearLayout popLayout = (LinearLayout) view.findViewById(R.id.pop_layout);
        authorCodeImg = (ImageView) view.findViewById(R.id.author_code_iv);
        TextView authorName = (TextView) view.findViewById(R.id.author_name_tv);
        ImageView codeClose = (ImageView) view.findViewById(R.id.author_code_close);
        TextView authorContent = (TextView) view.findViewById(R.id.author_content);
        AppLog.i("TAG", authorVO.qrCode.toString());
        DrawableUtils.displayImg(mContext, authorCodeImg, authorVO.qrCode.toString());
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.15f;
        getWindow().setAttributes(lp);
        authorName.setText(authorVO.authorName);
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
         /*   if ((keyCode == KeyEvent.KEYCODE_BACK) && articleWebview.canGoBack()) {
                articleWebview.goBack(); // goBack()表示返回WebView的上一页面
                return true;
            }*/
        return super.onKeyDown(keyCode, event);
    }


    //保存二维码图片到相册
    public void saveImageToGallery(Context context, Bitmap bmp) {
        if (bmp == null) {
            Toast.makeText(context, "保存出错", Toast.LENGTH_SHORT).show();
            return;
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "ywq");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = authorVO.authorId + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "保存出错", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(context, "保存出错", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(context, "保存出错", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // 最后通知图库更新
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
            pw.dismiss();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);


    }

    private int praisesNum;

    @Override
    public void onBackPressed() {
        setResult(MyFavoriteActivity.UPDATE_MY_DATA);
        super.onBackPressed();
    }

}

