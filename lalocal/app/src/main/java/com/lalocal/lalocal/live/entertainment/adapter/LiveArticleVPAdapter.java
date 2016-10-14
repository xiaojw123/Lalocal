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
import com.lalocal.lalocal.util.AppLog;

/**
 * Created by wangjie on 2016/10/12.
 */
public class LiveArticleVPAdapter extends PagerAdapter {

    private static final int LIVE = 0;
    private static final int ARTICLE = 1;

    private Context mContext;

    public LiveArticleVPAdapter(Context context) {
        this.mContext = context;
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
                // 初始化直播适配器
                adapter = new HomepageLiveAdapter(mContext);
                AppLog.i("ttt", "LIVE_finish initing");
                // 让recyclerview显示
                holder.xrvList.setLayoutManager(new LinearLayoutManager(mContext));
                // 配置适配器
                holder.xrvList.setAdapter(adapter);
                break;
            case ARTICLE:
                AppLog.i("ttt", "ARTICLE__");
                // 初始化文章适配器
                adapter = new HomepageArticleAdapter(mContext);
                AppLog.i("ttt", "ARTICLE_finish initing");
                // 让recyclerview显示
                holder.xrvList.setLayoutManager(new LinearLayoutManager(mContext));
                // 配置适配器
                holder.xrvList.setAdapter(adapter);
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
