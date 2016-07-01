package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
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
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.ParisesNetwork;
import com.lalocal.lalocal.util.ViewFactory;
import com.lalocal.lalocal.view.MyScrollView;
import com.lalocal.lalocal.view.viewpager.CycleViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by lenovo on 2016/6/22.
 */
public class ProductDetailsActivity extends BaseActivity implements MyScrollView.ScrollViewListener, View.OnClickListener {

    private MyScrollView mScrollView;
    private RelativeLayout reLayout;

    private int height;
    private ImageView detailsPhoto1;
    private TextView titleTv;
    private TextView productPrice;
    private LinearLayout checkDetails;
    private ImageView btnLike;
    private ImageView btnShare;
    private TextView productReserve;
    private ImageView customer;
    private ImageView back;
    private TextView productService;
    private LinearLayout featureLayout;
    private LinearLayout serviceLayout;
    private LinearLayout purchaseNotes;
    private int left;
    private int top;
    private RelativeLayout phones;
    private Context mContext = ProductDetailsActivity.this;
    private SpecialToH5Bean specialToH5Bean;
    private ContentService contentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_layout);
        ShareSDK.initSDK(this);
        initView();
        initData();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
    }

    private void initView() {
        detailsPhoto1 = (ImageView) findViewById(R.id.product_details_photo);
        titleTv = (TextView) findViewById(R.id.product_title_tv);
        productPrice = (TextView) findViewById(R.id.product_price);
        checkDetails = (LinearLayout) findViewById(R.id.product_check_detail);
        btnLike = (ImageView) findViewById(R.id.product_btn_like);
        btnShare = (ImageView) findViewById(R.id.product_btn_share);
        productReserve = (TextView) findViewById(R.id.product_details_reserve);
        customer = (ImageView) findViewById(R.id.product_customer_service);
        phones = (RelativeLayout) findViewById(R.id.product_details_phones);
        back = (ImageView) findViewById(R.id.common_back_btn);
        productService = (TextView) findViewById(R.id.product_service);
        featureLayout = (LinearLayout) findViewById(R.id.product_content_value);
        serviceLayout = (LinearLayout) findViewById(R.id.product_service_layout);
        purchaseNotes = (LinearLayout) findViewById(R.id.product_purchase_notes);
        //点击监听
        checkDetails.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        customer.setOnClickListener(this);
        back.setOnClickListener(this);
        productReserve.setOnClickListener(this);


        mScrollView = (MyScrollView) findViewById(R.id.product_scrollview);
        reLayout = (RelativeLayout) findViewById(R.id.product_title_relayout);
        detailsPhoto1 = (ImageView) findViewById(R.id.product_details_photo);
        mScrollView.setScrollViewListener(ProductDetailsActivity.this);
        reLayout.setBackgroundColor(Color.argb(0, 250, 250, 250));
        ViewTreeObserver vto = detailsPhoto1.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                detailsPhoto1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = detailsPhoto1.getHeight();
                detailsPhoto1.getWidth();
            }
        });

    }

    @Override
    public void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy) {
        AppLog.i("scrollViewHeightToReLayoutHeight", "scrollView:" + y + "//oldy:" + oldy + "/height:" + height);
        if (y <= height) {
            float scale = (float) y / height;
            float alpha = (255 * scale);
            //layout全部透明    0.16--0.95
            reLayout.setAlpha(scale);
            //只是layout背景透明
            reLayout.setBackgroundColor(Color.argb((int) alpha, 250, 250, 250));
        }
    }

    private void initData() {
        Intent intent = getIntent();
        specialToH5Bean = intent.getParcelableExtra("productdetails");
        contentService = new ContentService(this);
        contentService.setCallBack(new MyCallBack());
        contentService.productDetails(specialToH5Bean.getTargetId() + "");

        left = DensityUtil.dip2px(ProductDetailsActivity.this, 15);
        top = DensityUtil.dip2px(ProductDetailsActivity.this, 3);

    }
    private ProductDetailsResultBean result;

    public class MyCallBack extends ICallBack {
        @Override
        public void onProductDetails(ProductDetailsDataResp detailsDataResp) {
            super.onProductDetails(detailsDataResp);
            result = detailsDataResp.result;
            Object praiseId = result.praiseId;
            Toast.makeText(mContext,"onProductDetails+praiseId:"+praiseId,Toast.LENGTH_SHORT).show();
            String photo = result.photo;
           // praiseFlag = result.praiseFlag;
          /*  if (praiseFlag) {
                btnLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
            } else {
                btnLike.setImageResource(R.drawable.index_article_btn_like);

            }*/
            List<PhotosVosBean> photoVOs = result.photoVOs;
            if (photoVOs.size() > 0) {
                //显示轮播图
                detailsPhoto1.setVisibility(View.GONE);
                List<PhotosVosBean> photoVOs1 = result.photoVOs;
                showphotos(photoVOs1);
            } else {
                DrawableUtils.displayImg(ProductDetailsActivity.this, detailsPhoto1, photo);
            }
            //产品详情介绍
            productDetail(result);

        }

        @Override
        public void onPariseResult(PariseResult pariseResult) {
            if(pariseResult!=null&&pariseResult.getReturnCode()==0){
                btnLike.setImageResource(R.drawable.index_article_btn_like);

               Toast.makeText(mContext,"取消成功",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private int parise;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_back_btn:
                finish();
                break;
            case R.id.product_customer_service:
                //去客服页面
            case R.id.product_btn_like:
                //TODO 收藏
                if (result != null) {
                   //取消收藏

                        if(parise!=0){
                            Toast.makeText(mContext,"praiseId:"+parise,Toast.LENGTH_SHORT).show();
                            contentService.cancelParises(parise);
                        }
                     else {//添加收藏
                        addParises();
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
                finish();
                break;
        }
    }

    //添加收藏
    private void addParises() {
        btnLike.setImageResource(R.drawable.index_huabao_btn_like_nor);
        int targetId = specialToH5Bean.getTargetId();
        int targetType = specialToH5Bean.getTargetType();
        final ParisesNetwork parisesNetwork = new ParisesNetwork(mContext, targetId, targetType);
        parisesNetwork.networkResult(new ParisesNetwork.OnNetworkResponse() {
            @Override
            public void networkResult(Object o) {
                PariseResult pariseResult = new Gson().fromJson(o.toString(), PariseResult.class);
                if (pariseResult.getReturnCode()==0){
                    parise=pariseResult.getResult();
                    Toast.makeText(mContext,"收藏成功："+result,Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    //显示分享图标页面
    private void showShare(SpecialShareVOBean shareVO) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(shareVO.title);
        oks.setText(shareVO.title);
        if (shareVO.img != null) {
            oks.setImageUrl(shareVO.img);
        }
        oks.setUrl(shareVO.url);
        oks.setSiteUrl(shareVO.url);
    // 启动分享GUI
        oks.show(this);

    }



    //轮播图
    private void showphotos(List<PhotosVosBean> photoVOs1) {

        View inflate = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.viewpager, null);
        CycleViewPager fragmentById = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        List<RecommendAdResultBean> list = new ArrayList<>();
        for (int i = 0; i < photoVOs1.size(); i++) {
            RecommendAdResultBean recommendAdResultBean = new RecommendAdResultBean();
            recommendAdResultBean.photo = photoVOs1.get(i).fileName;
            list.add(recommendAdResultBean);
        }

        ViewFactory.initialize(ProductDetailsActivity.this, inflate, fragmentById, list);
        phones.addView(inflate);
    }

    //产品详情介绍
    private void productDetail(ProductDetailsResultBean result) {
        titleTv.setText(result.title);
        productPrice.setText("¥" + result.price);
        List<ProductDetailsBean> details = result.details;
        for (int i = 0; i < details.size(); i++) {
            if (i == 0) {
                //亮点介绍
                featureIntroduce(details.get(i).content.get(i).value);
            } else if (i == 1) {
                //服务介绍
                serviceIntroduce(details.get(i).content);
                productService.setText(details.get(i).title);
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
            tv.setText(content.get(i).menu + "");
            tv.setTextColor(Color.parseColor("#ffaa2a"));
            tv.setPadding(left, left, 0, left);
            serviceLayout.addView(tv);
            List<ProductValueBean> value = content.get(i).value;
            for (int j = 0; j < value.size(); j++) {
                TextView tvContent = new TextView(ProductDetailsActivity.this);
                tvContent.setText(value.get(j).text);
                tvContent.setPadding(left, 0, 0, 0);
                serviceLayout.addView(tvContent);
            }
            View view = new View(ProductDetailsActivity.this);
            view.setBackgroundColor(Color.parseColor("#666666"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
            serviceLayout.addView(view, params);
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
}
