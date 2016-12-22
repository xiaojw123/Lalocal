package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.view.CommonWebClient;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.SharePopupWindow;

import static com.lalocal.lalocal.R.id.carous_figure_webview;

/**
 * Created by android on 2016/7/7.
 */
public class CarouselFigureActivity extends BaseActivity implements View.OnClickListener, CustomTitleView.onBackBtnClickListener {

    private WebView carousFigure;
    private ImageView figgure;
    private TextView figureTv;
    private RecommendAdResultBean recommendAdResultBean;
    private SpecialShareVOBean shareVO;
    private CustomTitleView carousFigureCtv;
    private int targetId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carous_figure_layout);
        init();
        Intent intent = getIntent();
        recommendAdResultBean = intent.getParcelableExtra("carousefigure");
        String url = recommendAdResultBean.url;
        shareVO = recommendAdResultBean.shareVO;
        targetId = recommendAdResultBean.targetId;
        carousFigure.getSettings().setJavaScriptEnabled(true);
        carousFigure.loadUrl(AppConfig.getH5Url(this,url));
        carousFigure.setWebViewClient(new CommonWebClient(this));
        figureTv.setText(recommendAdResultBean.title);
    }

    private void init() {
        carousFigureCtv = (CustomTitleView) findViewById(R.id.carous_figure_ctv);
        carousFigure = (WebView) findViewById(carous_figure_webview);
        figgure = (ImageView) findViewById(R.id.carous_figure_share);
        figureTv = (TextView) findViewById(R.id.carous_figure_tv);
        figgure.setOnClickListener(this);
        carousFigureCtv.setFisishEanble(false);
        carousFigureCtv.setOnBackClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carous_figure_share:
                showShare(shareVO);
                break;
        }

    }

    private void showShare(SpecialShareVOBean shareVO) {
        SharePopupWindow shareActivity = new SharePopupWindow(CarouselFigureActivity.this);
        shareActivity.showShareWindow(shareVO);
        shareActivity.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && carousFigure.canGoBack()) {
            carousFigure.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cloaseAudio();
    }

    private void cloaseAudio() {
        String js = "var _audio = document.getElementsByTagName('audio')[0];\nif(_audio.played){\n_audio.pause();}";
        carousFigure.loadUrl("javascript:" + js);//video
    }

    @Override
    public void onBackClick() {
        if (carousFigure.canGoBack()) {
            carousFigure.goBack();
        } else {
            finish();
        }
    }

//    class CarouseLWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            AppLog.print("webviewclient url____" + url);
////            webviewclient url____lalocal://app?{"targetType": "19","targetId": "","targetUrl": ""}
//            if (url.startsWith("lalocal:")) {
//                int startIndex = url.indexOf("?") + 1;
//                String jsonData = url.substring(startIndex, url.length());
//                try {
//                    JSONObject jsonObject = new JSONObject(jsonData);
//                    String targetType = jsonObject.optString("targetType");
//                    String targetId = jsonObject.optString("targetId");
//                    if (!TextUtils.isEmpty(targetType)) {
//                        if ("19".equals(targetType)) {
//                            if (UserHelper.isLogined(CarouselFigureActivity.this)) {
//                                Intent intent = new Intent(CarouselFigureActivity.this, MyCouponActivity.class);
//                                intent.putExtra(KeyParams.PAGE_TYPE, KeyParams.PAGE_TYPE_WALLET);
//                                startActivity(intent);
//                            } else {
//                                LLoginActivity.start(CarouselFigureActivity.this);
//                            }
//                            return true;
//                        } else if ("15".equals(targetType)) {
//                            Intent intent = new Intent(CarouselFigureActivity.this, AudienceActivity.class);
//                            intent.putExtra("id", targetId);
//                            startActivity(intent);
//                            return true;
//                        } else if ("20".equals(targetType)) {
//                            Intent intent = new Intent(CarouselFigureActivity.this, PlayBackActivity.class);
//                            intent.putExtra("id", targetId);
//                            startActivity(intent);
//                            return true;
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//            return super.shouldOverrideUrlLoading(view, url);
//        }
//    }



}
