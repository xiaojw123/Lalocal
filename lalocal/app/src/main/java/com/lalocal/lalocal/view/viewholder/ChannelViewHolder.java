package com.lalocal.lalocal.view.viewholder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.HomeActivity;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.ScaleAlphaPageTransformer;
import com.lalocal.lalocal.view.DisallowParentTouchViewPager;
import com.lalocal.lalocal.view.MyPtrClassicFrameLayout;
import com.lalocal.lalocal.view.VerticalTextView;
import com.lalocal.lalocal.view.adapter.HomeRecoLiveAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 */
public class ChannelViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private TextView mTitleView;
    private TextView mSubTitleView;
    private DisallowParentTouchViewPager mVpHotLives;
    private LinearLayout mVtvSeeMore;
    private RelativeLayout mLayoutMore;
    private LinearLayout mDotContainer;
    private FrameLayout mLiveContainer;
    private int mSelected = 0;
    private List<Button> mDotBtns = new ArrayList<>();
    private boolean mLiveEmpty = false;

    private MyPtrClassicFrameLayout mPtrLayout;

    public ChannelViewHolder(Context context, View itemView, boolean mLiveEmpty) {
        super(itemView);
        this.mContext = context;
        this.mLiveEmpty = mLiveEmpty;

        mLiveContainer = (FrameLayout) itemView.findViewById(R.id.live_container);
        mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
        mSubTitleView = (TextView) itemView.findViewById(R.id.tv_subtitle);
        mLayoutMore = (RelativeLayout) itemView.findViewById(R.id.layout_more);
        mVpHotLives = (DisallowParentTouchViewPager) itemView.findViewById(R.id.vp_hot_lives);
        mDotContainer = (LinearLayout) itemView.findViewById(R.id.dot_container);
        mVtvSeeMore = (LinearLayout) itemView.findViewById(R.id.vertical_see_more);

        // 传入父容器
        mVpHotLives.setNestParent(mPtrLayout);
        // 设置一个屏幕最多显示视频的个数
        mVpHotLives.setOffscreenPageLimit(4);
        // 设置热播视频间距
        mVpHotLives.setPageMargin(mContext.getResources().getDimensionPixelSize(
                R.dimen.home_recommend_viewpager_page_margin));

    }

    /**
     * 初始化数据
     */
    public void initData(List<LiveRowsBean> list, String title, String subtitle) {
        if (TextUtils.isEmpty(title)) {
            mTitleView.setText("");
        } else {
            mTitleView.setText(title);
        }
        if (TextUtils.isEmpty(subtitle)) {
            mSubTitleView.setText("");
        } else {
            mSubTitleView.setText(subtitle);
        }

        final List<LiveRowsBean> hotLiveList = list;
        final int size = (list == null ? 0 : list.size());

        if (mLiveEmpty) {
            mLiveContainer.setVisibility(View.GONE);
            return;
        }

        // 改变大小透明度的工具类
        ScaleAlphaPageTransformer mScaleAlphaPageTransformer = new ScaleAlphaPageTransformer();
        // 填充数据
        HomeRecoLiveAdapter hotLiveAdapter = new HomeRecoLiveAdapter(mContext, hotLiveList);
        // 配置适配器
        mVpHotLives.setAdapter(hotLiveAdapter);
        // 设置值改变透明度，不改变
        // 大小
        mScaleAlphaPageTransformer.setType(true, false);
        // 对ViewPager进行设置改变透明度
        mVpHotLives.setPageTransformer(true, mScaleAlphaPageTransformer);
        // 如果当前子项个数大于等于3，则将ViewPager定位到第2项
        if (hotLiveAdapter.getCount() >= 3) {
            // 将当前ViewPager设置到第2项
            mVpHotLives.setCurrentItem(1);
        }

        mSelected = mVpHotLives.getCurrentItem();
        AppLog.i("slidder", "mSelected is " + mSelected);

        // 初始化小圆点
        mDotBtns = CommonUtil.initDot(mContext, mVpHotLives, mDotContainer, size, mSelected, CommonUtil.DARK_DOT);

        // ViewPager添加滑动事件
        mVpHotLives.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CommonUtil.selectDotBtn(mDotBtns, position, CommonUtil.DARK_DOT);
                // 如果滑动至最后一页
                if (position == size - 1) {
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

        mLayoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转直播界面
                ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_NEWS);
            }
        });

//        // 设置查看更多
//        mVtvSeeMore.setText("查看更多");
//        // 设置可点击
//        mVtvSeeMore.setClickable(true);
        // 设置点击监听
        mVtvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转直播界面
                ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_NEWS);
            }
        });
    }
}
