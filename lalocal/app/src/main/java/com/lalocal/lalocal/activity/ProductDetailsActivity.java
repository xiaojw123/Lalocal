package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.PhotosVosBean;
import com.lalocal.lalocal.model.ProductContentBean;
import com.lalocal.lalocal.model.ProductDetailsBean;
import com.lalocal.lalocal.model.ProductDetailsDataResp;
import com.lalocal.lalocal.model.ProductDetailsResultBean;
import com.lalocal.lalocal.model.ProductValueBean;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.ViewFactory;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.MyScrollView;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.lalocal.lalocal.view.viewpager.CycleViewPager;

import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Created by lenovo on 2016/6/22.
 */

public class ProductDetailsActivity extends AppCompatActivity implements MyScrollView.ScrollViewListener,MyScrollView.ScrollByListener ,
        View.OnClickListener,CustomTitleView.onBackBtnClickListener{



    private MyScrollView mScrollView;
    private RelativeLayout reLayout;

    private ImageView detailsPhoto1;
    private TextView titleTv;
    private TextView productPrice;
    private LinearLayout checkDetails;
    private ShineButton btnLike;
    private ImageView btnShare;
    private TextView productReserve;
    private ImageView customer;
    private TextView productService;
    private LinearLayout featureLayout;
    private LinearLayout serviceLayout;
    private LinearLayout purchaseNotes;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_layout);
    //    ShareSDK.initSDK(this);
        initView();
        initData();


    }

    private void initView() {
        backTitleView= (CustomTitleView) findViewById(R.id.product_title_back_ctv);
        detailsPhoto1 = (ImageView) findViewById(R.id.product_details_photo);
        titleTv = (TextView) findViewById(R.id.product_title_tv);
        productPrice = (TextView) findViewById(R.id.product_price);
        checkDetails = (LinearLayout) findViewById(R.id.product_check_detail);
        btnLike = (ShineButton) findViewById(R.id.product_btn_like);
        btnShare = (ImageView) findViewById(R.id.product_btn_share);
        productReserve = (TextView) findViewById(R.id.product_details_reserve);
        customer = (ImageView) findViewById(R.id.product_customer_service);
        phones = (RelativeLayout) findViewById(R.id.product_details_phones);
        productService = (TextView) findViewById(R.id.product_service);
        featureLayout = (LinearLayout) findViewById(R.id.product_content_value);
        serviceLayout = (LinearLayout) findViewById(R.id.product_service_layout);
        purchaseNotes = (LinearLayout) findViewById(R.id.product_purchase_notes);
        titleBack = (ImageView) findViewById(R.id.product_title_back_btn);
        titleRelayout = (RelativeLayout) findViewById(R.id.product_service_relayout);
        mScrollView = (MyScrollView) findViewById(R.id.product_scrollview);
        reLayout = (RelativeLayout) findViewById(R.id.product_title_relayout);
        detailsPhoto1 = (ImageView) findViewById(R.id.product_details_photo);
        titleLine = findViewById(R.id.product_title_line);
        serviceLL = (LinearLayout) findViewById(R.id.product_service_ll);

        //点击监听
        backTitleView.setOnBackClickListener(this);
        checkDetails.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        customer.setOnClickListener(this);
        productReserve.setOnClickListener(this);

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
                //去客服页面
                break;
            case R.id.product_btn_like:
                //TODO 收藏
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
                //TODO 分享
                if (result != null) {
                    SpecialShareVOBean shareVO = result.shareVO;
                    showShare(shareVO);
                }
                break;
            case R.id.product_details_reserve:
                //TODO 预定
                break;
            case R.id.product_check_detail:
                //TODO 查看详情
                String url = result.url;
                Intent intent = new Intent(ProductDetailsActivity.this, ProductCheckDetailActivity.class);
                intent.putExtra("checkdetail", url);
                startActivity(intent);

                break;
        }
    }


        @Override
    public void onScrollBy(int deltaX, int deltaY, int scrollX, int scrollY){
            AppLog.i("TAG", "deltaY:" + deltaY + "scrollY:" + scrollY);
        }


    public void onBackClick() {
        setResult(MeFragment.UPDATE_MY_DATA);

    }


    public class MyCallBack extends ICallBack {
        @Override
        public void onProductDetails(ProductDetailsDataResp detailsDataResp) {
            super.onProductDetails(detailsDataResp);
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
                if (photoVOs.size() > 0) {
                    //显示轮播图
                    detailsPhoto1.setVisibility(View.GONE);
                    for (int i = 0; i < photoVOs.size(); i++) {
                        RecommendAdResultBean recommendAdResultBean = new RecommendAdResultBean();
                        recommendAdResultBean.photo = photoVOs.get(i).fileName;
                        list.add(recommendAdResultBean);
                    }
                    showphotos(list);
                } else if (photoList.size() > 0) {
                    detailsPhoto1.setVisibility(View.GONE);
                    for (int i = 0; i < photoList.size(); i++) {
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

        View inflate = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.product_viewpager, null);

        CycleViewPager cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.lunbotu_content);

        if (list.size() > 0) {

            ViewFactory.initialize(ProductDetailsActivity.this, inflate, cycleViewPager, list);
        }

        phones.addView(inflate);
    }

    //产品详情介绍
    private void productDetail(ProductDetailsResultBean result) {
        titleTv.setText(result.title);
        productPrice.setText("¥ " + result.price);
        List<ProductDetailsBean> details = result.details;
        for (int i = 0; i < details.size(); i++) {
            if (i == 0) {
                //亮点介绍
                featureIntroduce(details.get(i).content.get(i).value);
            } else if (i == 1) {
                //服务介绍
                serviceIntroduce(details.get(i).content);
                productService.setText("· " + details.get(i).title + " ·");
                productService.setTextColor(Color.BLACK);
            } else if (i == 2) {
                //  purchaseNotes(details.get(i).content);
            }
        }

    }

    //购买须知
    private void purchaseNotes(List<ProductContentBean> content) {
        for (int i = 0; i < content.size(); i++) {
            for (int j = 0; j < content.get(i).value.size(); j++) {
                TextView tv = new TextView(ProductDetailsActivity.this);
                tv.setText(content.get(i).value.get(j).text);
                purchaseNotes.addView(tv);
            }
        }
    }

    //服务介绍
    private void serviceIntroduce(List<ProductContentBean> content) {
        for (int i = 0; i < content.size(); i++) {
            TextView tv = new TextView(ProductDetailsActivity.this);
            tv.setText("· " + content.get(i).menu + "");
            tv.setTextColor(Color.parseColor("#ffaa2a"));
            tv.setPadding(left, left, 0, left);
            serviceLayout.addView(tv);
            List<ProductValueBean> value = content.get(i).value;
            List<ProductValueBean> value1 = content.get(i).value;
            photoList = new ArrayList<>();

            for (int k = 0; k < value1.size(); k++) {
                String photo = value1.get(k).photo;
                if (!photo.equals("")) {
                    photoList.add(photo);
                }

            }
            for (int j = 0; j < value.size(); j++) {
                TextView tvContent = new TextView(ProductDetailsActivity.this);
                tvContent.setText(value.get(j).text);
                tvContent.setPadding(left, 0, 0, 0);
                serviceLayout.addView(tvContent);
            }
            View nullView = new View(ProductDetailsActivity.this);

            LinearLayout.LayoutParams nullParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60);
            serviceLayout.addView(nullView, nullParams);
            if (i != content.size() - 1) {
                View view = new View(ProductDetailsActivity.this);
                view.setBackgroundColor(Color.parseColor("#aaaaaa"));
                view.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
                view.setBackgroundResource(R.drawable.imag_line);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  DensityUtil.dip2px(mContext, 2));

                params.leftMargin = DensityUtil.dip2px(mContext, 15);
                params.rightMargin = DensityUtil.dip2px(mContext, 15);
                serviceLayout.addView(view, params);
            }


        }
    }

    //亮点介绍
    private void featureIntroduce(List<ProductValueBean> value) {
        for (int i = 0; i < value.size(); i++) {
            TextView tv = new TextView(ProductDetailsActivity.this);
            tv.setText(value.get(i).text);
            tv.setPadding(left, top, 0, 0);
            featureLayout.addView(tv);
        }

    }


    //显示分享图标页面
    private void showShare(SpecialShareVOBean shareVO) {
        SharePopupWindow   sharePopupWindow = new SharePopupWindow(mContext, shareVO);
        sharePopupWindow.showShareWindow();
        sharePopupWindow.showAtLocation(ProductDetailsActivity.this.findViewById(R.id.product),
                Gravity.CENTER, 0, 0);

    }
/*
    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        if (what == 1) {
            Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
        }
        if (sharePopupWindow != null) {
            sharePopupWindow.dismiss();
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
        ShareSDK.stopSDK();
    }*/
}
