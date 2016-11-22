package com.lalocal.lalocal.view.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.CarouselFigureActivity;
import com.lalocal.lalocal.activity.HomeActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.activity.RouteDetailActivity;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.activity.ThemeActivity;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.QiniuUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 */
public class ADCategoryViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private CardView mCardView;
    private LinearLayout mLayoutTheme;
    private LinearLayout mLayoutArticle;
    private LinearLayout mLayoutShop;
    private SliderLayout mSliderAd;

    private RecyclerView mRvRecommendList;

    private List<RecommendAdResultBean> mAdList = new ArrayList<>();

    // 旅行笔记
    private static final int ARTICLE = 4;

    /**
     * 分类
     *
     * @param itemView
     */
    public ADCategoryViewHolder(final Context context, View itemView, RecyclerView recyclerView) {
        super(itemView);

        this.mContext = context;
        // 关联控件
        this.mCardView = (CardView) itemView.findViewById(R.id.cv_category);
        this.mLayoutTheme = (LinearLayout) itemView.findViewById(R.id.layout_theme);
        this.mLayoutArticle = (LinearLayout) itemView.findViewById(R.id.layout_article);
        this.mLayoutShop = (LinearLayout) itemView.findViewById(R.id.layout_shop);
        this.mSliderAd = (SliderLayout) itemView.findViewById(R.id.ad_slider);

        // 隐藏指示器
        this.mSliderAd.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

        // 取消焦点
        this.mLayoutTheme.setFocusable(false);
        this.mLayoutArticle.setFocusable(false);
        this.mLayoutShop.setFocusable(false);
        this.mRvRecommendList = recyclerView;

        this.mLayoutTheme.setFocusable(false);
        this.mLayoutArticle.setFocusable(false);
        this.mLayoutShop.setFocusable(false);

        // 监听事件回调
        this.mLayoutTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转专题界面
                mContext.startActivity(new Intent(mContext, ThemeActivity.class));
            }
        });

        this.mLayoutArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastPosition = mRvRecommendList.getAdapter().getItemCount();
                ((LinearLayoutManager) mRvRecommendList.getLayoutManager()).scrollToPositionWithOffset(lastPosition, 0);
            }
        });

        this.mLayoutShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转目的地界面
                ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_DESTINATION);
            }
        });
    }

    /**
     * 初始化数据
     * @param ads
     */
    public void initData(List<RecommendAdResultBean> ads) {
        // 如果不存在数据，则不显示相应控件
        if (ads == null || ads.size() == 0) {
            mSliderAd.setVisibility(View.GONE);
            return;
        }
        mAdList = ads;

        // 获取广告栏的宽高
        int width = DensityUtil.getWindowWidth((Activity) mContext);
        int height = DensityUtil.dip2px(mContext, 200);

        for (int i = 0; i < mAdList.size(); i++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(mContext);
            RecommendAdResultBean ad = mAdList.get(i);
            String photoUrl = ad.photo;
            photoUrl = QiniuUtils.centerCrop(photoUrl, width, height);
            AppLog.i("photoU", "ad photo is " + photoUrl);
            defaultSliderView.image(photoUrl);
            defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    int position = mSliderAd.getCurrentPosition();
                    // 点击跳转
                    RecommendAdResultBean recommendAdResultBean = mAdList.get(position);
                    String url = recommendAdResultBean.url;
                    int targetType = recommendAdResultBean.targetType;
                    int targetId = recommendAdResultBean.targetId;

                    Intent intent = null;
                    switch (targetType) {
                        case Constants.TARGET_TYPE_URL: // 链接
                            AppLog.i("addd", "链接");
                            intent = new Intent(mContext, CarouselFigureActivity.class);
                            intent.putExtra("carousefigure", recommendAdResultBean);
                            mContext.startActivity(intent);
                            break;
                        case Constants.TARGET_TYPE_ARTICLE:
                            AppLog.i("addd", "文章");
                            intent = new Intent(mContext, ArticleActivity.class);
                            intent.putExtra("targetID", String.valueOf(targetId));
                            mContext.startActivity(intent);
                            break;
                        case Constants.TARGET_TYPE_PRODUCTION:
                            AppLog.i("addd", "产品--" + targetId);
                            // 跳转到商品详情界面
                            SpecialToH5Bean specialToH5Bean = new SpecialToH5Bean();
                            specialToH5Bean.setTargetId(targetId);

                            intent = new Intent(mContext, ProductDetailsActivity.class);
                            intent.putExtra("productdetails", specialToH5Bean);
                            mContext.startActivity(intent);
                            break;
                        case Constants.TARGET_TYPE_ROUTE:
                            AppLog.i("addd", "路线");
                            intent = new Intent(mContext, RouteDetailActivity.class);
                            intent.putExtra("detail_id", targetId);
                            mContext.startActivity(intent);
                            break;
                        case Constants.TARGET_TYPE_THEME:
                            AppLog.i("addd", "专题");
                            intent = new Intent(mContext, SpecialDetailsActivity.class);
                            intent.putExtra("rowId", targetId + "");
                            mContext.startActivity(intent);
                            break;
                        case Constants.TARGET_TYPE_CHANNEL:

                            Intent intent1=new Intent(mContext, AudienceActivity.class);
                            intent1.putExtra("id",String.valueOf(targetId));
                            mContext.startActivity(intent1);

                            break;
                    }

                }
            });
            mSliderAd.addSlider(defaultSliderView);
        }
    }
}
