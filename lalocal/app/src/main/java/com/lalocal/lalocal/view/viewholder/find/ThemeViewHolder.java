package com.lalocal.lalocal.view.viewholder.find;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ThemeActivity;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.util.DotUtils;
import com.lalocal.lalocal.util.ScaleAlphaPageTransformer;
import com.lalocal.lalocal.view.adapter.HomeRecoThemeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 *
 * 发现页的专题列表试图容器类
 *
 */
public class ThemeViewHolder extends RecyclerView.ViewHolder {

    // 上下文
    private Context mContext;

    // 专题列表大小
    private int mSize = -1;
    // 当前显示的列表项所在的下标
    private int mSelected = -1;

    // 控件声明
    private TextView mTitleView;
    private TextView mSubtitleView;
    private FrameLayout mLayoutContainer;
    private RelativeLayout mLayoutMore;
    private ViewPager mVpTheme;
    private LinearLayout mVtvSeeMore;
    private LinearLayout mDotContainer;

    // 专题列表下方的小圆点列表
    private List<Button> mDotBtns = new ArrayList<>();

    /**
     * 构造方法
     * @param context
     * @param itemView
     */
    public ThemeViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;

        // 关联控件
        mLayoutContainer = (FrameLayout) itemView.findViewById(R.id.theme_container);
        mVtvSeeMore = (LinearLayout) itemView.findViewById(R.id.vertical_see_more);
        mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
        mSubtitleView = (TextView) itemView.findViewById(R.id.tv_subtitle);
        mLayoutMore = (RelativeLayout) itemView.findViewById(R.id.layout_more);
        mVpTheme = (ViewPager) itemView.findViewById(R.id.vp_theme);
        mDotContainer = (LinearLayout) itemView.findViewById(R.id.dot_container);

        // 设置一个屏幕最多显示专题的个数
        mVpTheme.setOffscreenPageLimit(4);
        // 设置专题列表子项间距
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
        if (list == null || list.size() == 0) {
            mLayoutContainer.setVisibility(View.GONE);
            return;
        }

        List<RecommendRowsBean> recommendSpecialList = list;

        // 设置专题区域中英文标题
        mTitleView.setText(title);
        mSubtitleView.setText(subtitle);

        // 改变大小透明度的工具类，专题列表左右滑动时，当前子项的透明度会高一点
        ScaleAlphaPageTransformer mScaleAlphaPageTransformer = new ScaleAlphaPageTransformer();
        // 初始化适配器
        final HomeRecoThemeAdapter themeAdapter = new HomeRecoThemeAdapter(mContext, recommendSpecialList);
        // 配置适配器
        mVpTheme.setAdapter(themeAdapter);
        // 设置值改变透明度，不改变大小
        mScaleAlphaPageTransformer.setType(true, false);
        // 对ViewPager进行设置改变透明度
        mVpTheme.setPageTransformer(true, mScaleAlphaPageTransformer);
        // 如果当前子项个数大于等于3，则将ViewPager定位到第2项
        if (themeAdapter.getCount() >= 3) {
            // 将当前ViewPager设置到第2项
            mVpTheme.setCurrentItem(1);
        }

        mSize = list.size();
        mSelected = mVpTheme.getCurrentItem();

        // 初始化小圆点
        mDotBtns = DotUtils.initDot(mContext, mVpTheme, mDotContainer, mSize, mSelected, DotUtils.ROUND_LIGHT_DOT);

        // 如果只有一项，显示查看更多字样
        if (mSize == 1) {
            mVtvSeeMore.setVisibility(View.VISIBLE);
        } else {
            mVtvSeeMore.setVisibility(View.GONE);
        }
        // 滑动监听事件
        mVpTheme.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 滚动到最后一页显示文案“查看更多”
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                DotUtils.selectDotBtn(mDotBtns, position, DotUtils.ROUND_LIGHT_DOT);
                // 如果滑动至最后一页
                if (position == mSize - 1) {
                    // 查看更多字样显示
                    mVtvSeeMore.setVisibility(View.VISIBLE);
                } else { // 如果不是最后一页
                    // 查看更多字样隐藏
                    mVtvSeeMore.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 标题栏的More
        mLayoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转专题界面
                mContext.startActivity(new Intent(mContext, ThemeActivity.class));
            }
        });

        // ViewPager滑到最后一页显示“查看更多”
        mVtvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转专题界面
                mContext.startActivity(new Intent(mContext, ThemeActivity.class));
            }
        });
    }

}
