package com.lalocal.lalocal.view.viewholder.find;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.CarouselFigureActivity;
import com.lalocal.lalocal.activity.DestinationActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.activity.RouteDetailActivity;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.activity.ThemeActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveHomePageActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackDetailActivity;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.QiniuUtils;
import com.lalocal.lalocal.view.adapter.HomeRecommendAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 *
 * 发现页广告、分类区域试图容器
 *
 *
 */
public class ADCategoryViewHolder extends RecyclerView.ViewHolder {

    // 上下文
    private Context mContext;

    // 控件声明
    private LinearLayout mLayoutTheme;
    private LinearLayout mLayoutArticle;
    private LinearLayout mLayoutShop;
    private SliderLayout mSliderAd;

    // 列表控件
    private RecyclerView mRvRecommendList;
    // 小圆点容器
    private LinearLayout mDotContainer;

    // 广告列表
    private List<RecommendAdResultBean> mAdList = new ArrayList<>();

    // 小圆点按钮列表
    private List<Button> mDotBtns = new ArrayList<>();

    // 上一个小圆点位置
    private int mPrePosition = -1;
    /**
     * 分类
     *
     * @param itemView
     */
    public ADCategoryViewHolder(final Context context, View itemView, RecyclerView recyclerView) {
        super(itemView);

        this.mContext = context;
        // 关联控件
        this.mDotContainer = (LinearLayout) itemView.findViewById(R.id.dot_container);
        this.mLayoutTheme = (LinearLayout) itemView.findViewById(R.id.layout_theme);
        this.mLayoutArticle = (LinearLayout) itemView.findViewById(R.id.layout_article);
        this.mLayoutShop = (LinearLayout) itemView.findViewById(R.id.layout_shop);
        this.mSliderAd = (SliderLayout) itemView.findViewById(R.id.ad_slider);



        // 取消焦点
        this.mLayoutTheme.setFocusable(false);
        this.mLayoutArticle.setFocusable(false);
        this.mLayoutShop.setFocusable(false);
        this.mRvRecommendList = recyclerView;

        // 设置不可获取焦点，防止发现页重现时列表上下打滑的问题
        this.mLayoutTheme.setFocusable(false);
        this.mLayoutArticle.setFocusable(false);
        this.mLayoutShop.setFocusable(false);

        // 监听事件回调
        // "旅行专题"点击监听事件
        this.mLayoutTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转专题界面
                mContext.startActivity(new Intent(mContext, ThemeActivity.class));
            }
        });

        // "旅游笔记"点击监听事件
        this.mLayoutArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int firstArticlePosition = ((HomeRecommendAdapter)mRvRecommendList.getAdapter()).getFirstArticlePositoin() + 1;
                ((LinearLayoutManager) mRvRecommendList.getLayoutManager()).scrollToPositionWithOffset(firstArticlePosition, 0);
            }
        });
        // "旅游商店"点击监听事件
        this.mLayoutShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转目的地界面
                Intent intent = new Intent(mContext, DestinationActivity.class);
                mContext.startActivity(intent);
            }
        });

        // 列表滚动监听事件，当列表向下滑动，广告位轮播图不可见时，轮播图不轮播；可见时继续轮播
        mRvRecommendList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRvRecommendList.getLayoutManager();
                // 通过判断列表中第一个可见的Item是不是轮播图来判断是否进行轮播
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition > 1) {
                    mSliderAd.stopAutoCycle();
                } else {
                    mSliderAd.startAutoCycle();
                    mSliderAd.setFocusable(true);
                    mDotContainer.setFocusable(true);
                }
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
        mAdList.clear();
        mAdList.addAll(ads);

        mPrePosition = -1;

        // 获取广告栏的宽高
        int width = DensityUtil.getWindowWidth((Activity) mContext);
        int height = DensityUtil.dip2px(mContext, 200);

        // 获取列表大小
        int size = mAdList.size();
        // 移除所有slider
        mSliderAd.removeAllSliders();
        // 广告位的广告页填充
        for (int i = 0; i < size; i++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(mContext);
            RecommendAdResultBean ad = mAdList.get(i);
            String photoUrl = ad.photo;
            photoUrl = QiniuUtils.centerCrop(photoUrl, width, height);
            defaultSliderView.image(photoUrl);
            defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            defaultSliderView.setOnSliderClickListener(onSliderClickListener);
            mSliderAd.addSlider(defaultSliderView);
        }
    }

    private BaseSliderView.OnSliderClickListener onSliderClickListener = new BaseSliderView.OnSliderClickListener() {
        @Override
        public void onSliderClick(BaseSliderView slider) {
            int position = mSliderAd.getCurrentPosition();
            int size = mAdList.size();
            // 友盟数据埋点
            switch (position) {
                case 0:
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_01);
                    break;
                case 1:
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_02);
                    break;
                case 2:
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_03);
                    break;
                case 3:
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_04);
                    break;
                case 4:
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_05);
                    break;
                case 5:
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_06);
                    break;
            }
            // 点击跳转
            RecommendAdResultBean recommendAdResultBean = mAdList.get(position);
            String url = recommendAdResultBean.url;
            int targetType = recommendAdResultBean.targetType;
            int targetId = recommendAdResultBean.targetId;

            // 广告位不同跳转的分类
            Intent intent = null;
            switch (targetType) {
                case Constants.PLAY_BACK_TYPE_URL: // 回放
                    intent = new Intent(mContext, PlayBackDetailActivity.class);
                    intent.putExtra("id", String.valueOf(targetId));
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_URL: // 链接
                    intent = new Intent(mContext, CarouselFigureActivity.class);
                    intent.putExtra("carousefigure", recommendAdResultBean);
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_ARTICLE: // 文章
                    intent = new Intent(mContext, ArticleActivity.class);
                    intent.putExtra("targetID", String.valueOf(targetId));
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_PRODUCTION: // 产品
                    // 跳转到商品详情界面
                    SpecialToH5Bean specialToH5Bean = new SpecialToH5Bean();
                    specialToH5Bean.setTargetId(targetId);

                    intent = new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("productdetails", specialToH5Bean);
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_ROUTE: // 路线
                    intent = new Intent(mContext, RouteDetailActivity.class);
                    intent.putExtra("detail_id", targetId);
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_THEME: // 专题
                    intent = new Intent(mContext, SpecialDetailsActivity.class);
                    intent.putExtra("rowId", targetId + "");
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_CHANNEL: // 视频直播频道
                    Intent intent1 = new Intent(mContext, AudienceActivity.class);
                    intent1.putExtra("id", String.valueOf(targetId));
                    mContext.startActivity(intent1);
                    break;
                case Constants.TARGET_TYPE_USER: // 用户
                    Intent intentUser = new Intent(mContext, LiveHomePageActivity.class);
                    intentUser.putExtra("userId", String.valueOf(targetId));
                    mContext.startActivity(intentUser);
                    break;
            }

        }
    };
}
