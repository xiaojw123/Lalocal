package com.lalocal.lalocal.view.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.HomeActivity;
import com.lalocal.lalocal.activity.ThemeActivity;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;

/**
 * Created by wangjie on 2016/10/25.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private CardView mCardView;
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
        this.mCardView = (CardView) itemView.findViewById(R.id.cv_category);
        this.mLayoutTheme = (LinearLayout) itemView.findViewById(R.id.layout_theme);
        this.mLayoutArticle = (LinearLayout) itemView.findViewById(R.id.layout_article);
        this.mLayoutShop = (LinearLayout) itemView.findViewById(R.id.layout_shop);

        // 取消焦点
        this.mLayoutTheme.setFocusable(false);
        this.mLayoutArticle.setFocusable(false);
        this.mLayoutShop.setFocusable(false);
        this.mRvRecommendList = recyclerView;

        // 监听事件回调
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
                AppLog.i("recc", "jump the position is " + lastPosition);
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
