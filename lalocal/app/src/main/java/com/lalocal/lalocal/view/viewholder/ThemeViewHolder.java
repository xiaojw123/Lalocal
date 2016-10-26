package com.lalocal.lalocal.view.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.activity.ThemeActivity;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.ScaleAlphaPageTransformer;
import com.lalocal.lalocal.view.DisallowParentTouchViewPager;
import com.lalocal.lalocal.view.MyPtrClassicFrameLayout;
import com.lalocal.lalocal.view.adapter.HomeRecoThemeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 */
public class ThemeViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private int mSize = -1;
    private int mSelected = -1;

    TextView  mTitleView;
    TextView mSubtitleView;
    FrameLayout mLayoutContainer;
    RelativeLayout mLayoutMore;

    DisallowParentTouchViewPager mVpTheme;
    LinearLayout mDotContainer;

    List<Button> mDotBtns = new ArrayList<>();

    private MyPtrClassicFrameLayout mPtrLayout;

    private boolean isEmpty = false;

    public ThemeViewHolder(Context context, View itemView, MyPtrClassicFrameLayout ptr, boolean isEmpty) {
        super(itemView);
        this.mContext = context;
        this.mPtrLayout = ptr;
        this.isEmpty = isEmpty;

        mLayoutContainer = (FrameLayout) itemView.findViewById(R.id.theme_container);
         mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
        mSubtitleView = (TextView) itemView.findViewById(R.id.tv_subtitle);
        mLayoutMore = (RelativeLayout) itemView.findViewById(R.id.layout_more);
        mVpTheme = (DisallowParentTouchViewPager) itemView.findViewById(R.id.vp_theme);
        mDotContainer = (LinearLayout) itemView.findViewById(R.id.dot_container);

        // 传入父容器
        mVpTheme.setNestParent(mPtrLayout);
        // 设置一个屏幕最多显示视频的个数
        mVpTheme.setOffscreenPageLimit(4);
        // 设置热播视频间距
        mVpTheme.setPageMargin(mContext.getResources().getDimensionPixelSize(
                R.dimen.home_recommend_viewpager_page_margin));
    }

    /**
     * 初始化数据
     *
     * @param list
     * @param title
     * @param subtitle
     */
    public void initData(List<RecommendRowsBean> list, String title, String subtitle) {
        List<RecommendRowsBean> recommendSpecialList = list;

        if (isEmpty) {
            mLayoutContainer.setVisibility(View.GONE);
            return;
        }

         mTitleView.setText(title);
        mSubtitleView.setText(subtitle);

        // 改变大小透明度的工具类
        ScaleAlphaPageTransformer mScaleAlphaPageTransformer = new ScaleAlphaPageTransformer();
        // 初始化适配器
        final HomeRecoThemeAdapter ThemeAdapter = new HomeRecoThemeAdapter(mContext, recommendSpecialList);
        // 配置适配器
        mVpTheme.setAdapter(ThemeAdapter);
        // 设置值改变透明度，不改变大小
        mScaleAlphaPageTransformer.setType(true, false);
        // 对ViewPager进行设置改变透明度
        mVpTheme.setPageTransformer(true, mScaleAlphaPageTransformer);
        // 如果当前子项个数大于等于3，则将ViewPager定位到第2项
        if (ThemeAdapter.getCount() >= 3) {
            // 将当前ViewPager设置到第2项
            mVpTheme.setCurrentItem(1);
        }

        mSize = list.size();
        mSelected = mVpTheme.getCurrentItem();

        // 初始化小圆点
        mDotBtns = CommonUtil.initDot(mContext, mVpTheme, mDotContainer, mSize, mSelected, CommonUtil.DARK_DOT);


        mVpTheme.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CommonUtil.selectDotBtn(mDotBtns, position, CommonUtil.DARK_DOT);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mLayoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转专题界面
                mContext.startActivity(new Intent(mContext, ThemeActivity.class));
            }
        });
    }

}
