package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.PhotosVosBean;
import com.lalocal.lalocal.model.ProductDetailsDataResp;
import com.lalocal.lalocal.model.ProductDetailsResultBean;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.ViewFactory;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.MyScrollView;
import com.lalocal.lalocal.view.ShapeTextView;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.lalocal.lalocal.view.viewpager.CycleViewPager;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lenovo on 2016/6/22.
 */
public class ProductDetailsActivity extends BaseActivity implements MyScrollView.ScrollViewListener, MyScrollView.ScrollByListener,
        View.OnClickListener, CustomTitleView.onBackBtnClickListener {
    private MyScrollView mScrollView;
    private RelativeLayout reLayout;
    private ImageView detailsPhoto1;
    private ShineButton btnLike;
    private ImageView btnShare;
    private ShapeTextView productReserve;
    private ImageView customer;
    //    private LinearLayout serviceLayout;
    private int left;
    private int top;
    private RelativeLayout phones;
    private Context mContext = ProductDetailsActivity.this;
    private SpecialToH5Bean specialToH5Bean;
    private ContentLoader contentService;
    private Object praiseId;
    // private SharePopupWindow sharePopupWindow;
    private ImageView titleBack;
    private RelativeLayout titleRelayout;
    private ProductDetailsResultBean result;
    private CustomTitleView backTitleView;
    private boolean praiseFlag;
    private int parise;
    private List<String> photoList;
    private View titleLine;
    private LinearLayout serviceLL;
    private int statusHeight;
    WebView detailWv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_layout);
        showLoadingAnimation();
        initView();
        initData();


    }

    private void initView() {
        detailWv = (WebView) findViewById(R.id.product_details_wv);
        backTitleView = (CustomTitleView) findViewById(R.id.product_title_back_ctv);
        detailsPhoto1 = (ImageView) findViewById(R.id.product_details_photo);
        btnLike = (ShineButton) findViewById(R.id.product_btn_like);
        btnShare = (ImageView) findViewById(R.id.product_btn_share);
        productReserve = (ShapeTextView) findViewById(R.id.product_details_reserve);
        customer = (ImageView) findViewById(R.id.product_customer_service);
        phones = (RelativeLayout) findViewById(R.id.product_details_phones);
        titleBack = (ImageView) findViewById(R.id.product_title_back_btn);
        titleRelayout = (RelativeLayout) findViewById(R.id.product_service_relayout);
        mScrollView = (MyScrollView) findViewById(R.id.product_scrollview);
        reLayout = (RelativeLayout) findViewById(R.id.product_title_relayout);
        detailsPhoto1 = (ImageView) findViewById(R.id.product_details_photo);
        titleLine = findViewById(R.id.product_title_line);
        serviceLL = (LinearLayout) findViewById(R.id.product_service_ll);
        CommonUtil.setWebView(detailWv,true);
        //点击监听
        backTitleView.setOnBackClickListener(this);
        btnLike.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        customer.setOnClickListener(this);
        productReserve.setOnClickListener(this);
        serviceLL.setOnClickListener(this);

        // final int statusHeight = getStatusHeight(this);//获取状态栏高度


        mScrollView.setScrollViewListener(ProductDetailsActivity.this);
        reLayout.setBackgroundColor(Color.argb(0, 250, 250, 250));

        ViewTreeObserver vto = detailsPhoto1.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                detailsPhoto1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                statusHeight = detailsPhoto1.getHeight();
                detailsPhoto1.getWidth();

            }
        });

    }

    private void initData() {
        Intent intent = getIntent();
        specialToH5Bean = intent.getParcelableExtra("productdetails");
        contentService = new ContentLoader(this);
        contentService.setCallBack(new MyCallBack());
        contentService.productDetails(specialToH5Bean.getTargetId() + "");
        left = DensityUtil.dip2px(ProductDetailsActivity.this, 15);
        top = DensityUtil.dip2px(ProductDetailsActivity.this, 3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (result!=null){
            detailWv.loadUrl(AppConfig.getH5Url(this,result.h5Url));
        }
    }

    @Override

    public void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy) {//title透明渐变
        boolean isOpactity = true;

        // reLayout.setBackgroundColor(Color.argb((int) alpha, 250, 250, 250));
        if (y <= 0) {
            reLayout.setAlpha(0.0f);
            titleBack.setVisibility(View.VISIBLE);
            titleLine.setVisibility(View.GONE);
            serviceLL.setVisibility(View.GONE);
            reLayout.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));//AGB由相关工具获得，或者美工提供
        } else if (y > 0 && y <= statusHeight - 200) {
            float scale = (float) y / (statusHeight - 200);
            float alpha = (255 * scale);
            titleLine.setVisibility(View.GONE);
            reLayout.setAlpha(scale);
            reLayout.setBackgroundColor(Color.argb((int) alpha, 250, 250, 250));
        } else {
            titleBack.setVisibility(View.GONE);
            titleLine.setVisibility(View.VISIBLE);
            serviceLL.setVisibility(View.VISIBLE);
            reLayout.setAlpha(1.0f);
            reLayout.setBackgroundColor(Color.argb((int) 255, 250, 250, 250));
        }
    }

    //获取状态栏高度
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_title_back_btn:
                finish();
                break;
            case R.id.product_service_relayout:
                //去客服页面
                break;
            case R.id.product_customer_service:
                MobHelper.sendEevent(this, MobEvent.DESTINATION_PRODUCT_SERVICE);
                CommonUtil.startCustomService(this);
                //去客服页面
                break;
            case R.id.product_btn_like:
                // 收藏
                MobHelper.sendEevent(this, MobEvent.DESTINATION_PRODUCT_LIKE);
                if (result != null) {
                    //取消收藏
                    int targetId = specialToH5Bean.getTargetId();
                    if (praiseFlag) {
                        contentService.cancelParises(praiseId, targetId);
                    } else {//添加收藏
                        contentService.specialPraise(targetId, 2);//点赞
                    }
                }
                break;
            case R.id.product_btn_share:
                // 分享
                MobHelper.sendEevent(this, MobEvent.DESTINATION_PRODUCT_SHARE);
                if (result != null) {
                    SpecialShareVOBean shareVO = result.shareVO;
                    showShare(shareVO);
                }
                break;
            case R.id.product_details_reserve:
                // 预定
                MobHelper.sendEevent(this, MobEvent.PRODUCT_BOOKING);
                if (UserHelper.isLogined(this)) {
                    contentService.getUserProfile(UserHelper.getUserId(this), UserHelper.getToken(this), v);
                } else {
                    LLoginActivity.start(this);
                }
                break;

            case R.id.product_service_ll:
                MobHelper.sendEevent(this, MobEvent.DESTINATION_PRODUCT_SERVICE);
                CommonUtil.startCustomService(this);
                break;
        }
    }

    private void preOrderProduct() {
        //TODO :reset
        if (result != null) {
            String orderUrl = result.orderUrl;
            if (!TextUtils.isEmpty(orderUrl)) {
                Intent intent = new Intent();
                intent.setClass(this, BookActivity.class);
                intent.putExtra(BookActivity.BOOK_URL, orderUrl);
                startActivity(intent);
            }
        }
    }


    @Override
    public void onScrollBy(int deltaX, int deltaY, int scrollX, int scrollY) {
        AppLog.i("TAG", "deltaY:" + deltaY + "scrollY:" + scrollY);
    }


    public void onBackClick() {
        setResult(MyFavoriteActivity.UPDATE_MY_DATA);

    }

    @Override
    public void onBackPressed() {
        setResult(MyFavoriteActivity.UPDATE_MY_DATA);
        super.onBackPressed();
    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onGetUserProfile(final LoginUser user) {
            if (user != null) {
                if (user.getStatus() == 0) {
                    preOrderProduct();
                } else {
                    String email = user.getEmail();
                    if (TextUtils.isEmpty(email)) {
                        CommonUtil.showPromptDialog(ProductDetailsActivity.this, "邮箱未绑定,请前去绑定", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                Intent intent = new Intent(ProductDetailsActivity.this, EmailBoundActivity.class);
                                intent.putExtra(KeyParams.USERID, user.getId());
                                intent.putExtra(KeyParams.TOKEN, UserHelper.getToken(ProductDetailsActivity.this));
                                startActivity(intent);
                            }
                        });

                    } else {
                        CommonUtil.showPromptDialog(ProductDetailsActivity.this, "邮箱未验证，请前去验证!", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                Intent intent = new Intent(ProductDetailsActivity.this, AccountEidt2Activity.class);
                                intent.putExtra(AccountEidt2Activity.ACTION_TYPE, AccountEidt2Activity.ACTION_EMAIL_MODIFY);
                                intent.putExtra("emailtext", user.getEmail() + "(未验证)");
                                intent.putExtra(KeyParams.USERID, user.getId());
                                intent.putExtra(KeyParams.TOKEN, UserHelper.getToken(ProductDetailsActivity.this));
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

        }

        @Override
        public void onProductDetails(ProductDetailsDataResp detailsDataResp) {
            hidenLoadingAnimation();
            if (detailsDataResp.returnCode == 0) {
                result = detailsDataResp.result;
                praiseId = result.praiseId;

                String photo = result.photo;
                praiseFlag = result.praiseFlag;
                if (praiseFlag) {
                    btnLike.setChecked(true);
                } else {
                    btnLike.setChecked(false);
                }
                //产品详情介绍
                productDetail(result);
                List<PhotosVosBean> photoVOs = result.photoVOs;
                List<RecommendAdResultBean> list = new ArrayList<>();
                if (photoVOs != null && photoVOs.size() > 0) {
                    //显示轮播图
                    detailsPhoto1.setVisibility(View.GONE);
                    for (int i = 0; i < photoVOs.size(); i++) {
                        if (i > 6) {
                            break;
                        }
                        RecommendAdResultBean recommendAdResultBean = new RecommendAdResultBean();
                        recommendAdResultBean.photo = photoVOs.get(i).fileName;
                        list.add(recommendAdResultBean);
                    }
                    showphotos(list);
                } else if (photoList != null && photoList.size() > 0) {
                    detailsPhoto1.setVisibility(View.GONE);

                    for (int i = 0; i < photoList.size(); i++) {
                        if (i > 7) {
                            break;
                        }
                        RecommendAdResultBean recommendAdResultBean = new RecommendAdResultBean();
                        recommendAdResultBean.photo = photoList.get(i);
                        list.add(recommendAdResultBean);
                    }
                    showphotos(list);

                } else {
                    DrawableUtils.displayImg(ProductDetailsActivity.this, detailsPhoto1, photo);
                }

            }

        }

        @Override
        public void onPariseResult(PariseResult pariseResult) {
            if (pariseResult != null && pariseResult.getReturnCode() == 0) {

                btnLike.setChecked(false);
                praiseFlag = false;
            }
        }

        @Override
        public void onInputPariseResult(PariseResult pariseResult) {
            super.onInputPariseResult(pariseResult);
            if (pariseResult.getReturnCode() == 0) {

                btnLike.setChecked(true);
                praiseId = pariseResult.getResult();
                praiseFlag = true;
            }
        }
    }

    //轮播图
    private void showphotos(List<RecommendAdResultBean> list) {
        try {
            View inflate = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.product_viewpager, null);
            CycleViewPager cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.lunbotu_content);
            if (list.size() > 0) {
                ViewFactory.initialize(ProductDetailsActivity.this, inflate, cycleViewPager, list);
            }
            phones.addView(inflate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //产品详情介绍
    private void productDetail(ProductDetailsResultBean result) {
        switch (result.status) {
            case 0:
                productReserve.setText("预定");
                productReserve.setEnabled(true);
                productReserve.setSolidColor(getResources().getColor(R.color.color_ffaa2a));
//                productReserve.setBackgroundColor();
                break;
            case 1:
                productReserve.setText("已售罄");
                productReserve.setEnabled(false);
                productReserve.setSolidColor(getResources().getColor(R.color.color_b3));
//                productReserve.setBackgroundColor(getResources().getColor(R.color.color_b3));
                break;
            case 2:
                productReserve.setText("已过期");
                productReserve.setEnabled(false);
                productReserve.setSolidColor(getResources().getColor(R.color.color_b3));
//                productReserve.setBackgroundColor(getResources().getColor(R.color.color_b3));
                break;
            case -1:
                productReserve.setText("已删除");
                productReserve.setEnabled(false);
                productReserve.setSolidColor(getResources().getColor(R.color.color_b3));
//                productReserve.setBackgroundColor(getResources().getColor(R.color.color_b3));
                break;


        }
        detailWv.loadUrl(AppConfig.getH5Url(this,result.h5Url));

    }




    //显示分享图标页面
    private void showShare(SpecialShareVOBean shareVO) {
        SharePopupWindow sharePopupWindow = new SharePopupWindow(mContext, shareVO);
        sharePopupWindow.show();
    }


}
