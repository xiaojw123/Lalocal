package com.lalocal.lalocal.live.entertainment.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.HomepageUserArticlesResp;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.UserLiveItem;
import com.lalocal.lalocal.util.AppLog;

import java.util.List;

/**
 * Created by wangjie on 2016/10/12.
 */
public class LiveArticleVPAdapter extends PagerAdapter {

    private static final int LIVE = 0;
    private static final int ARTICLE = 1;

    private Context mContext;

    // 用户当前直播
    private LiveRowsBean mLiving;
    // 用户历史直播列表
    private List<LiveRowsBean> mUserLiveList;
    // 用户文章列表
    private List<ArticleDetailsResultBean> mUserArticleList;

    public LiveArticleVPAdapter(Context context, LiveRowsBean living, List<LiveRowsBean> userLiveList, List<ArticleDetailsResultBean> userArticleList) {
        // 上下文
        this.mContext = context;
        // 用户当前直播
        this.mLiving = living;
        // 用户历史直播列表
        this.mUserLiveList = userLiveList;
        // 用户文章列表
        this.mUserArticleList = userArticleList;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.homepage_live_article_list, null);

        ViewHolder holder = new ViewHolder();
        holder.tvNoList = (TextView) view.findViewById(R.id.tv_no_list);
        holder.xrvList = (XRecyclerView) view.findViewById(R.id.xrv_list);
        holder.xrvList.setLoadingMoreEnabled(false);
        holder.xrvList.setPullRefreshEnabled(false);

        // TODO: 根据数据的有无判断是否显示列表

        RecyclerView.Adapter adapter = null;
        switch (position) {
            case LIVE:
                AppLog.i("ttt", "LIVE__");
                if (mLiving == null && (mUserLiveList == null || mUserLiveList.size() == 0)) {
                    holder.xrvList.setVisibility(View.GONE);
                    holder.tvNoList.setVisibility(View.VISIBLE);
                    holder.tvNoList.setText("他还没有直播过");
                } else {
                    holder.xrvList.setVisibility(View.VISIBLE);
                    holder.tvNoList.setVisibility(View.GONE);

                    // 初始化直播适配器
                    adapter = new HomepageLiveAdapter(mContext, mLiving, mUserLiveList);
                    AppLog.i("ttt", "LIVE_finish initing");
                    // 让recyclerview显示
                    holder.xrvList.setLayoutManager(new LinearLayoutManager(mContext));
                    // 配置适配器
                    holder.xrvList.setAdapter(adapter);
                }
                break;
            case ARTICLE:
                AppLog.i("ttt", "ARTICLE__");
                if (mUserArticleList == null || mUserArticleList.size() == 0) {
                    holder.xrvList.setVisibility(View.GONE);
                    holder.tvNoList.setVisibility(View.VISIBLE);
                    holder.tvNoList.setText("他还没有写过文章");
                } else {

                    holder.xrvList.setVisibility(View.VISIBLE);
                    holder.tvNoList.setVisibility(View.GONE);

                    // 初始化文章适配器
                    adapter = new HomepageArticleAdapter(mContext, mUserArticleList);
                    AppLog.i("ttt", "ARTICLE_finish initing");
                    // 让recyclerview显示
                    holder.xrvList.setLayoutManager(new LinearLayoutManager(mContext));
                    // 配置适配器
                    holder.xrvList.setAdapter(adapter);
                }
                break;
        }
        // 布局添加到容器
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private class ViewHolder {
        TextView tvNoList;
        XRecyclerView xrvList;
    }
}
