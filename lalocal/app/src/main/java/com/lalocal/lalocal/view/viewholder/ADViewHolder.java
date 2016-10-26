package com.lalocal.lalocal.view.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.CarouselFigureActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.activity.RouteDetailActivity;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.DisallowParentTouchSliderLayout;
import com.lalocal.lalocal.view.MyPtrClassicFrameLayout;

import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 */
public class ADViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout mAdContainer;
    private LinearLayout mDotContainer;
    private DisallowParentTouchSliderLayout mSliderLayout;
    private Context mContext;
    private List<RecommendAdResultBean> mAdResultList;
    private List<Button> mDotBtns;

    private MyPtrClassicFrameLayout mPtrLayout;

    public ADViewHolder(Context context, View itemView, MyPtrClassicFrameLayout ptr) {
        super(itemView);

        this.mContext = context;
        this.mPtrLayout = ptr;

        mAdContainer = (LinearLayout) itemView.findViewById(R.id.ad_container);
        mDotContainer = (LinearLayout) itemView.findViewById(R.id.dot_container);
        mSliderLayout = (DisallowParentTouchSliderLayout) itemView.findViewById(R.id.ad_slider);
        // 隐藏指示器
        mSliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        // 传入父容器
        mSliderLayout.setNestParent(mPtrLayout);
    }

    /**
     * 初始化数据
     *
     * @param ads
     */
    public void initData(final List<RecommendAdResultBean> ads) {
        // 如果不存在数据，则不显示相应控件
        if (ads == null || ads.size() == 0) {
            mAdContainer.setVisibility(View.GONE);
            return;
        }
        int size = ads.size();
        mAdResultList = ads;

        mSliderLayout.removeAllSliders();
        for (int i = 0; i < mAdResultList.size(); i++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(mContext);
            RecommendAdResultBean ad = mAdResultList.get(i);
            defaultSliderView.image(ad.photo);
            defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    int position = mSliderLayout.getCurrentPosition();
                    // 点击跳转
                    RecommendAdResultBean recommendAdResultBean = ads.get(position);
                    String url = recommendAdResultBean.url;
                    int targetType = recommendAdResultBean.targetType;
                    int targetId = recommendAdResultBean.targetId;

                    Intent intent = null;
                    switch (targetType) {
                        case Constants.TARGET_TYPE_URL:
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
            mSliderLayout.addSlider(defaultSliderView);
        }

        // 自定义指示器
        mDotBtns = CommonUtil.initDot(mContext, mSliderLayout, mDotContainer, size, mSliderLayout.getCurrentPosition(), CommonUtil.AD_DOT);

        // 轮播图页面改变
        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                AppLog.i("TAG","首页轮播图野蛮改变监听："+position);
                CommonUtil.selectDotBtn(mDotBtns, position, CommonUtil.AD_DOT);
            }
        });
    }

    /**
     * 移除所有的TextSliderView
     *
     * @param mSliderLayout
     */
    private void removeSlider(SliderLayout mSliderLayout) {
        int size = mSliderLayout.getChildCount();
        for (int i = size - 1; i >= 0; i++) {
            mSliderLayout.removeSliderAt(i);
        }
    }

}
