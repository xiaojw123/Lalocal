package com.lalocal.lalocal.activity.fragment;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.ui.CustomLinearLayoutManager;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.ArticlesResp;
import com.lalocal.lalocal.model.ArticlesResultBean;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.RecommendListBean;
import com.lalocal.lalocal.model.RecommendListDataResp;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomXRecyclerView;
import com.lalocal.lalocal.view.adapter.HomeRecommendAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends BaseFragment {

    @BindView(R.id.xrv_recommend)
    CustomXRecyclerView mXrvRecommend;

    private HomeRecommendAdapter mRecommendAdapter;

    private List<RecommendAdResultBean> mAdResultList = new ArrayList<>();
    private RecommendListBean mRecommendListBeen;
    private List<ArticleDetailsResultBean> mArticleList = new ArrayList<>();

    private ContentLoader mContentLoader;

    private int mArticlePageSize = 10;
    private int mArticlePageNum = 1;

    // 刷新标记
    private static final int INIT = 0x00;
    private static final int REFRESH_AD = 0x01;
    private static final int REFRESH_PRODUCT_THEME = 0x02;
    private static final int REFRESH_ARTICLE = 0x03;
    private static final int REFRESH_ALL = 0x04;

    // 是否在刷新
    private boolean isRefreshing = false;
    // 是否在加载
    private boolean isLoadingMore = false;

    private static final int REFRESH_COMPLETE = 3;

    private int mCountRefresh = 0;

    public FindFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_recommend_new_layout, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化ContentLoader
        initLoader();
        // 初始化XRecyclerView
        initXRecyclerView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        mXrvRecommend.setFocusable(true);
    }

    /**
     * 初始化XRecyclerView
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void initXRecyclerView() {
        // 初始化布局参数
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getActivity());
        // 设置竖直排列
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // xrecyclerview设置布局参数
        mXrvRecommend.setLayoutManager(layoutManager);
        // 初始化加载监听事件
        XRecyclerViewLoadingListener listener = new XRecyclerViewLoadingListener();
        // 设置监听事件
        mXrvRecommend.setLoadingListener(listener);
        // 激活下拉刷新
        mXrvRecommend.setPullRefreshEnabled(true);
        // 激活加载更多
        mXrvRecommend.setLoadingMoreEnabled(true);
        // 一开始就刷新
        mXrvRecommend.setRefreshing(true);
        // 初始化适配器
        updateAdapter(INIT);
        // 滚动事件监听，保证滑动到底部的时候才加载更多而不是最后一个item出现的时候就加载
//        mXrvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (mXrvRecommend.canScrollVertically(1) == true) {
//                    mXrvRecommend.loadMoreComplete();
//                } else if (mXrvRecommend.canScrollVertically(1) == false) {
//                    mXrvRecommend.setLoadingMoreEnabled(true);
//                }
//            }
//        });
    }

    /**
     * XRecyclerView监听事件
     */
    private class XRecyclerViewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            if (mXrvRecommend != null) {
                isRefreshing = true;
                mXrvRecommend.setLoadingMoreEnabled(true);

                mContentLoader.recommendAd();
                mContentLoader.indexRecommentList();
                mContentLoader.articleList(10, 0);
            } else {
                mXrvRecommend.refreshComplete();
            }
            mArticlePageNum = 1;
        }

        @Override
        public void onLoadMore() {
            if (mXrvRecommend.canScrollVertically(1) == false) {
                isLoadingMore = true;
                mArticlePageNum++;
                mContentLoader.articleList(mArticlePageSize, mArticlePageNum);
            }
        }
    }

    /**
     * 初始化ContentLoader
     */
    private void initLoader() {
        mContentLoader = new ContentLoader(getActivity());
        mContentLoader.setCallBack(new MyCallBack());
//        mContentLoader.recommendAd();
//        mContentLoader.indexRecommentList();
//        mContentLoader.articleList(10, 0);
    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onRecommendAd(final RecommendAdResp recommendAdResp) {
            super.onRecommendAd(recommendAdResp);
            try {
                if (recommendAdResp.getReturnCode() == 0) {

                    // 获取广告数据
                    List<RecommendAdResultBean> adResultList = recommendAdResp.getResult();

                    mAdResultList.clear();
                    mAdResultList.addAll(adResultList);

                    updateAdapter(REFRESH_AD);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 刷新完毕
                refreshComplete();
            }
        }

        /**
         * 首页推荐列表，包括：专题、商品、直播列表
         *
         * @param recommendListDataResp
         */
        @Override
        public void onRecommendList(final RecommendListDataResp recommendListDataResp) {
            super.onRecommendList(recommendListDataResp);

            try {
                if (recommendListDataResp.getReturnCode() == 0) {
                    // 获取首页推荐列表
                    mRecommendListBeen = recommendListDataResp.getResult();
                    updateAdapter(INIT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 刷新完毕
                refreshComplete();
            }
        }

        @Override
        public void onArticleListResult(final ArticlesResp articlesResp) {
            super.onArticleListResult(articlesResp);

            try {
                if (articlesResp.getReturnCode() == 0) {
                    // 获取首页推荐文章列表
                    ArticlesResultBean articlesResultBean = articlesResp.getResult();
                    List<ArticleDetailsResultBean> articleList = articlesResultBean == null ? null : articlesResultBean.getRows();
                    if (isLoadingMore) {
                        isLoadingMore = false;
                        if (articleList == null || articleList.size() == 0) {
                            // setNoMore与loadMoreComplete不能共存，否则不能显示FooterView
                            mXrvRecommend.setNoMore(true);
                            Toast.makeText(getActivity(), "没有更多文章咯~", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mXrvRecommend.loadMoreComplete();
                        updateAdapter(REFRESH_ARTICLE);
                    } else if (isRefreshing) {
                        mArticleList.clear();
                    }
                    mArticleList.addAll(articleList);
                    updateAdapter(REFRESH_ARTICLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 刷新完毕
                refreshComplete();
            }
        }

    }

    /**
     * 刷新完毕
     */
    private void refreshComplete() {
        if (isRefreshing) {
            mCountRefresh++;
            if (mCountRefresh == REFRESH_COMPLETE) {
                mCountRefresh = 0;
                isLoadingMore = false;
                isRefreshing = false;
                mXrvRecommend.refreshComplete();
            }
        }
    }

    /**
     * 设置适配器
     */
    private void updateAdapter(int refreshType) {
        if (mRecommendAdapter == null) {
            // 初始化适配器
            mRecommendAdapter = new HomeRecommendAdapter(getActivity(), mAdResultList, mRecommendListBeen, mArticleList);
            // 配置适配器
            mXrvRecommend.setAdapter(mRecommendAdapter);
        } else {
            switch (refreshType) {
                case REFRESH_AD:
                    mRecommendAdapter.refreshAD(mAdResultList);
                    AppLog.i("TAH","刷新。。。。。。。。。轮播图");
                    break;
                case REFRESH_PRODUCT_THEME:
                    mRecommendAdapter.refreshProductTheme(mRecommendListBeen);
                    break;
                case REFRESH_ARTICLE:
                    mRecommendAdapter.refreshArticle(mArticleList);
                    break;
                case REFRESH_ALL:
                    mRecommendAdapter.refreshAll(mAdResultList, mRecommendListBeen, mArticleList);
                    break;
                case INIT:
                    mRecommendAdapter = null;
                    updateAdapter(INIT);
                    break;
            }
        }
    }
}
