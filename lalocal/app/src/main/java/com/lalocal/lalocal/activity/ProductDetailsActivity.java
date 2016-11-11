package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.easemob.Constant;
import com.lalocal.lalocal.easemob.ui.ChatActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.LoginUser;
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

    private static final String BOOK_URL_FORMART = "%1$s&USER_ID=%2$s&TOKEN=%3$s&APP_VERSION=%4$s&DEVICE=%5$s&DEVICE_ID=%6$s";
    private MyScrollView mScrollView;
    private RelativeLayout reLayout;

    private ImageView detailsPhoto1;
    private TextView titleTv;
    private TextView productPrice;
    private LinearLayout checkDetails;
    private ShineButton btnLike;
    private ImageView btnShare;
    private ShapeTextView productReserve;
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
    private View loadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_layout);
        initView();
        initData();


    }

    private void initView() {
        loadingView = findViewById(R.id.page_base_loading);
        backTitleView = (CustomTitleView) findViewById(R.id.product_title_back_ctv);
        detailsPhoto1 = (ImageView) findViewById(R.id.product_details_photo);
        titleTv = (TextView) findViewById(R.id.product_title_tv);
        productPrice = (TextView) findViewById(R.id.product_price);
        checkDetails = (LinearLayout) findViewById(R.id.product_check_detail);
        btnLike = (ShineButton) findViewById(R.id.product_btn_like);
        btnShare = (ImageView) findViewById(R.id.product_btn_share);
        productReserve = (ShapeTextView) findViewById(R.id.product_details_reserve);
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
                startOnlineService();
                //去客服页面
                break;
            case R.id.product_btn_like:
                //TODO 收藏
                MobHelper.sendEevent(this,MobEvent.DESTINATION_PRODUCT_LIKE);
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
                MobHelper.sendEevent(this,MobEvent.DESTINATION_PRODUCT_SHARE);
                if (result != null) {
                    SpecialShareVOBean shareVO = result.shareVO;
                    showShare(shareVO);
                }
                break;
            case R.id.product_details_reserve:
                //TODO 预定
                MobHelper.sendEevent(this,MobEvent.PRODUCT_BOOKING);
                if (UserHelper.isLogined(this)) {
                    contentService.getUserProfile(UserHelper.getUserId(this), UserHelper.getToken(this));
                } else {
                    LLoginActivity.start(this);
                }
                break;
            case R.id.product_check_detail:
                //TODO 查看详情
                if (result != null && result.url != null) {
                    String url = result.url;
                    Intent intent = new Intent(ProductDetailsActivity.this, ProductCheckDetailActivity.class);
                    intent.putExtra("checkdetail", url);
                    startActivity(intent);
                }

                break;
            case R.id.product_service_ll:
                startOnlineService();

                break;
        }
    }

    private void preOrderProduct() {
        //TODO :reset
        if (result != null) {
            String orderUrl = result.orderUrl;
            if (!TextUtils.isEmpty(orderUrl)) {
                int userId = UserHelper.getUserId(this);
                String token = UserHelper.getToken(this);
                String device = CommonUtil.getDevice();
                String devcieId = CommonUtil.getUUID(this);
                String version = AppConfig.getVersionName(this);
                Intent intent = new Intent();
                intent.setClass(this, BookActivity.class);
                intent.putExtra(BookActivity.BOOK_URL, String.format(BOOK_URL_FORMART, orderUrl, userId, token, version, device, devcieId));
                startActivity(intent);
            }
        }
    }

    public void startOnlineService() {
        Intent intent = new Intent(this, ChatActivity.class);
        if (result != null) {
            intent.putExtra(Constant.ITEM_TITLE, result.title);
            intent.putExtra(Constant.ITEM_DES, result.description);
            intent.putExtra(Constant.ITEM_POST_URL, result.photo);
            intent.putExtra(Constant.ITEM_PRICE, String.valueOf((int) result.price));
        }
        startActivity(intent);
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

        @Override
        public void onProductDetails(ProductDetailsDataResp detailsDataResp) {
            loadingView.setVisibility(View.GONE);
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
                        if (i > 6) {
                            break;
                        }
                        RecommendAdResultBean recommendAdResultBean = new RecommendAdResultBean();
                        recommendAdResultBean.photo = photoVOs.get(i).fileName;
                        list.add(recommendAdResultBean);
                    }
                    showphotos(list);
                } else if (photoList.size() > 0) {
                    detailsPhoto1.setVisibility(View.GONE);

                    for (int i = 0; i < photoList.size(); i++) {
                        if(i>7){
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

        View inflate = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.product_viewpager, null);

        CycleViewPager cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.lunbotu_content);

        if (list.size() > 0) {

            ViewFactory.initialize(ProductDetailsActivity.this, inflate, cycleViewPager, list);
        }

        phones.addView(inflate);
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

        titleTv.setText(result.title);
//        productPrice.setText("¥ " + result.price);
        productPrice.setText(CommonUtil.formartOrderPrice(result.price));
        List<SparseArray<String>> items = new ArrayList<>();
        SparseArray<String> item1 = new SparseArray<>();
        SparseArray<String> item2 = new SparseArray<>();
        SparseArray<String> item3 = new SparseArray<>();
        SparseArray<String> item4 = new SparseArray<>();
        SparseArray<String> item5 = new SparseArray<>();
        String departureTime = result.departureTime;
        String departurePoint = result.departurePoint;
        String departureRemark = result.departureRemark;
        String duration = result.duration;
        String returnDetails = result.returnDetails;
        if (!TextUtils.isEmpty(departureTime)) {
            item1.append(0, "出发时间");
            item1.append(1, departureTime);
            items.add(item1);
        }
        if (!TextUtils.isEmpty(departurePoint)) {
            item2.append(0, "出发地点");
            item2.append(1, departurePoint);
            items.add(item2);
        }
        if (!TextUtils.isEmpty(departureRemark)) {
            item3.append(0, "出发须知");
            item3.append(1, departureRemark);
            items.add(item3);
        }
        if (!TextUtils.isEmpty(duration)) {
            item4.append(0, "持续时间");
            item4.append(1, duration);
            items.add(item4);
        }
        if (!TextUtils.isEmpty(returnDetails)) {
            item5.append(0, "返回信息");
            item5.append(1, returnDetails);
            items.add(item5);
        }
        serviceAddtionIntroduce(items);
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

    public void serviceAddtionIntroduce(List<SparseArray<String>> items) {
        for (int i = 0; i < items.size(); i++) {
            SparseArray<String> item = items.get(i);
            TextView tv = new TextView(ProductDetailsActivity.this);
            tv.setText("· " + item.get(0) + "");
            tv.setTextColor(Color.parseColor("#ffaa2a"));
            tv.setPadding(left, left, 0, left);
            serviceLayout.addView(tv);
            TextView tvContent = new TextView(ProductDetailsActivity.this);
            tvContent.setText(item.get(1));
            tvContent.setPadding(left, 0, 0, 0);
            serviceLayout.addView(tvContent);
            View nullView = new View(ProductDetailsActivity.this);
            LinearLayout.LayoutParams nullParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60);
            serviceLayout.addView(nullView, nullParams);
            if (i != items.size() - 1) {
                View view = new View(ProductDetailsActivity.this);
                view.setBackgroundColor(Color.parseColor("#aaaaaa"));
                view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                view.setBackgroundResource(R.drawable.imag_line);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 2));

                params.leftMargin = DensityUtil.dip2px(mContext, 15);
                params.rightMargin = DensityUtil.dip2px(mContext, 15);
                serviceLayout.addView(view, params);
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
                view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                view.setBackgroundResource(R.drawable.imag_line);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 2));

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
        SharePopupWindow sharePopupWindow = new SharePopupWindow(mContext, shareVO,String.valueOf(specialToH5Bean.getTargetId()));
        sharePopupWindow.showShareWindow();
        sharePopupWindow.showAtLocation(ProductDetailsActivity.this.findViewById(R.id.product),
                Gravity.BOTTOM, 0, 0);

    }


}
