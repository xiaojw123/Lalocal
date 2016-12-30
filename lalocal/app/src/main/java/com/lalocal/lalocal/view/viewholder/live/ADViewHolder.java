package com.lalocal.lalocal.view.viewholder.live;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.CarouselFigureActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.activity.RouteDetailActivity;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveHomePageActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.QiniuUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/12/14.
 */

public class ADViewHolder extends RecyclerView.ViewHolder {

    // 上下文
    private Context mContext;
    // 广告列表
    private List<RecommendAdResultBean> mAdList = new ArrayList<>();
    // 轮播图控件
    private SliderLayout mSliderAd;
    // 列表控件
    private XRecyclerView mXRecyclerView;

    public ADViewHolder(Context context, View itemView, XRecyclerView xRecyclerView) {
        super(itemView);

        mContext = context;
        mXRecyclerView = xRecyclerView;
        itemView.setFocusable(false);
        // 关联控件
        mSliderAd = (SliderLayout) itemView.findViewById(R.id.ad_slider);
    }

    public void initData(List<RecommendAdResultBean> adList) {
        if (adList == null || adList.size() == 0) {
            mSliderAd.setVisibility(View.GONE);
            return;
        }
        mAdList.clear();
        mAdList.addAll(adList);

        // 获取广告栏的宽高
        int width = DensityUtil.getWindowWidth((Activity) mContext);
        int height = DensityUtil.dip2px(mContext, 200);

        // 获取列表大小
        int size = mAdList.size();
        // 移除所有slider
        mSliderAd.removeAllSliders();
        for (int i = 0; i < size; i++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(mContext);
            RecommendAdResultBean ad = mAdList.get(i);
            String photoUrl = ad.photo;
            photoUrl = QiniuUtils.centerCrop(photoUrl, width, height);
            AppLog.i("photoU", "ad photo is " + photoUrl);
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
            // TODO: 暂时不考虑数据埋点
//            switch (position) {
//                case 0:
//                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_01);
//                    break;
//                case 1:
//                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_02);
//                    break;
//                case 2:
//                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_03);
//                    break;
//                case 3:
//                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_04);
//                    break;
//                case 4:
//                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_05);
//                    break;
//                case 5:
//                    MobHelper.sendEevent(mContext, MobEvent.LIVE_BANNER_06);
//                    break;
//            }
            // 点击跳转
            RecommendAdResultBean recommendAdResultBean = mAdList.get(position);
            int targetType = recommendAdResultBean.targetType;
            int targetId = recommendAdResultBean.targetId;

            Intent intent = null;
            switch (targetType) {
                case Constants.PLAY_BACK_TYPE_URL: // 回放
                    intent = new Intent(mContext, PlayBackActivity.class);
                    intent.putExtra("id", String.valueOf(targetId));
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_URL: // 链接
                    String url = recommendAdResultBean.url;
                    if (TextUtils.isEmpty(url)) {
                        Toast.makeText(mContext, "加载链接失败", Toast.LENGTH_SHORT).show();
                    } else {
                        AppLog.i("addd", "链接");
                        intent = new Intent(mContext, CarouselFigureActivity.class);
                        intent.putExtra("carousefigure", recommendAdResultBean);
                        mContext.startActivity(intent);
                    }
                    break;
                case Constants.TARGET_TYPE_ARTICLE: // 文章
                    AppLog.i("addd", "文章");
                    intent = new Intent(mContext, ArticleActivity.class);
                    intent.putExtra("targetID", String.valueOf(targetId));
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_PRODUCTION: // 产品
                    AppLog.i("addd", "产品--" + targetId);
                    // 跳转到商品详情界面
                    SpecialToH5Bean specialToH5Bean = new SpecialToH5Bean();
                    specialToH5Bean.setTargetId(targetId);

                    intent = new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("productdetails", specialToH5Bean);
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_ROUTE: // 路线
                    AppLog.i("addd", "路线");
                    intent = new Intent(mContext, RouteDetailActivity.class);
                    intent.putExtra("detail_id", targetId);
                    mContext.startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_THEME: // 专题
                    AppLog.i("addd", "专题");
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
