package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ProductDetailsDataResp;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.view.MyScrollView;

/**
 * Created by lenovo on 2016/6/22.
 */
public class ProductDetailsActivity extends AppCompatActivity implements MyScrollView.ScrollViewListener {

    private MyScrollView mScrollView;
    private RelativeLayout reLayout;
    private int reLayoutHeight;
    private ImageView detailsPhoto;
    private int height;
    private ImageView detailsPhoto1;
    private TextView titleTv;
    private TextView productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_layout);
        initView();
        initData();


    }
   boolean isFirst;
    private void initView() {

        detailsPhoto1 = (ImageView) findViewById(R.id.product_details_photo);
        titleTv = (TextView) findViewById(R.id.product_title_tv);
        productPrice = (TextView) findViewById(R.id.product_price);




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
               // height=  DensityUtil.px2dip(ProductDetailsActivity.this,detailsPhoto.getHeight());
                detailsPhoto1.getWidth();

            }
        });

    }

    @Override
    public void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy) {
        AppLog.i("scrollViewHeightToReLayoutHeight","scrollView:"+y+"//oldy:"+oldy+"/height:"+height);
        if(y<=height) {
            float scale = (float)y/ height;
            float alpha = (255 * scale);
            //layout全部透明    0.16--0.95
            reLayout.setAlpha(scale);
            //只是layout背景透明
           reLayout.setBackgroundColor(Color.argb((int) alpha, 250, 250, 250));
        }
        }

    private void initData() {
        Intent intent = getIntent();
        String productdetails = intent.getStringExtra("productdetails");
        ContentService contentService = new ContentService(this);
        contentService.setCallBack(new MyCallBack());
        contentService.productDetails(productdetails);

    }



    public class MyCallBack extends ICallBack {
        @Override
        public void onProductDetails(ProductDetailsDataResp detailsDataResp) {
            super.onProductDetails(detailsDataResp);


        }
    }
}
