package com.lalocal.lalocal.view.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.HomeActivity;
import com.lalocal.lalocal.activity.ThemeActivity;

/**
 * Created by wangjie on 2016/10/25.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private LinearLayout mLayoutLive;
    private LinearLayout mLayoutTheme;
    private LinearLayout mLayoutArticle;
    private LinearLayout mLayoutShop;

    private RecyclerView mRvRecommendList;

    // 旅行笔记
    private static final int ARTICLE = 4;

    /**
     * 分类
     *
     * @param itemView
     */
    public CategoryViewHolder(final Context context, View itemView, RecyclerView recyclerView) {
        super(itemView);

        this.mContext = context;
        // 关联控件
        this.mLayoutLive = (LinearLayout) itemView.findViewById(R.id.layout_live);
        this.mLayoutTheme = (LinearLayout) itemView.findViewById(R.id.layout_theme);
        this.mLayoutArticle = (LinearLayout) itemView.findViewById(R.id.layout_article);
        this.mLayoutShop = (LinearLayout) itemView.findViewById(R.id.layout_shop);

        this.mRvRecommendList = recyclerView;

        this.mLayoutLive.setFocusable(false);
        this.mLayoutTheme.setFocusable(false);
        this.mLayoutArticle.setFocusable(false);
        this.mLayoutShop.setFocusable(false);

        // 监听事件回调
        mLayoutLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转直播界面
                ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_NEWS);
            }
        });

        mLayoutTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转专题界面
                mContext.startActivity(new Intent(mContext, ThemeActivity.class));
            }
        });

        mLayoutArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastPosition = mRvRecommendList.getAdapter().getItemCount();
                ((LinearLayoutManager) mRvRecommendList.getLayoutManager()).scrollToPositionWithOffset(lastPosition, 0);
            }
        });

        mLayoutShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转目的地界面
                ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_DESTINATION);
            }
        });
    }
}
