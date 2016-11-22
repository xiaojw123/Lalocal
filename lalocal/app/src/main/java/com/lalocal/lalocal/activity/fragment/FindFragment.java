package com.lalocal.lalocal.activity.fragment;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
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
import com.lalocal.lalocal.view.MyPtrClassicFrameLayout;
import com.lalocal.lalocal.view.MyRecyclerView;
import com.lalocal.lalocal.view.adapter.HomeRecommendAdapter;
import com.lalocal.lalocal.view.ptr.PtrHeader;

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
    private List<ArticleDetailsResultBean> mArticleList;

    private ContentLoader mContentLoader;

    private int mArticlePageSize = 10;
    private int mArticlePageNum = 1;


//    private int mLastPosition = 0;
//    private int mOffset = 0;

    // 是否在刷新
    private boolean isRefreshing = false;
    // 是否在加载
    private boolean isLoadingMore = false;

    private static final int REFRESH_COMPLETE = 3;

    private int mCountRefresh = 0;

    /**
     * 设置适配器
     */
    private void setAdapter() {
        // 初始化适配器
        mRecommendAdapter = new HomeRecommendAdapter(getActivity(), mAdResultList, mRecommendListBeen, mArticleList);
        // 设置适配器
        mXrvRecommend.setAdapter(mRecommendAdapter);
        // 加载数据以后显示加载更多
        mXrvRecommend.setLoadingMoreEnabled(true);
    }

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
        AppLog.i("fd", "initXRecyclerView");
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
        // 一开始就刷新
        mXrvRecommend.setRefreshing(true);
        // 滚动事件监听，保证滑动到底部的时候才加载更多而不是最后一个item出现的时候就加载
        mXrvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mXrvRecommend.canScrollVertically(1) == true) {
                    mXrvRecommend.loadMoreComplete();
                } else if (mXrvRecommend.canScrollVertically(1) == false) {
                    mXrvRecommend.setLoadingMoreEnabled(true);
                }
            }
        });
        // 滚动事件监听，保证滑动到底部的时候才加载更多而不是最后一个item出现的时候就加载
        mXrvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mXrvRecommend.canScrollVertically(1) == true) {
                    mXrvRecommend.loadMoreComplete();
                } else if (mXrvRecommend.canScrollVertically(1) == false) {
                    mXrvRecommend.setLoadingMoreEnabled(true);
                }
            }
        });
    }

    /**
     * XRecyclerView监听事件
     */
    private class XRecyclerViewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            AppLog.i("fd", "onRefresh");
            if (mXrvRecommend != null) {
                isRefreshing = true;
                mXrvRecommend.setLoadingMoreEnabled(true);

                mContentLoader.recommendAd();
                mContentLoader.indexRecommentList();
                mContentLoader.articleList(10, 0);
            } else {
                mXrvRecommend.refreshComplete();
            }
        }

        @Override
        public void onLoadMore() {
            AppLog.i("fd", "onLoadMore");
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
        AppLog.i("fd", "initLoader");
        mContentLoader = new ContentLoader(getActivity());
        mContentLoader.setCallBack(new MyCallBack());
        mContentLoader.recommendAd();
        mContentLoader.indexRecommentList();
        mContentLoader.articleList(10, 0);
    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onRecommendAd(final RecommendAdResp recommendAdResp) {
            super.onRecommendAd(recommendAdResp);
            AppLog.i("fd", "onRecommendAd");
            try {
                if (recommendAdResp.getReturnCode() == 0) {
                    mCountRefresh++;
                    if (mCountRefresh == REFRESH_COMPLETE) {
                        mCountRefresh = 0;
                        isRefreshing = false;
                        mXrvRecommend.refreshComplete();
                        mXrvRecommend.loadMoreComplete();
                    }

                    // 获取广告数据
                    List<RecommendAdResultBean> adResultList = recommendAdResp.getResult();

                    mAdResultList.clear();
                    mAdResultList.addAll(adResultList);
                }
            } catch (Exception e) {
                e.printStackTrace();
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

            AppLog.i("fd", "onRecommendList");
            mCountRefresh++;
            if (mCountRefresh == REFRESH_COMPLETE) {
                AppLog.i("fd", "onRecommendList 1");
                mCountRefresh = 0;
                AppLog.i("fd", "onRecommendList 2");
                isRefreshing = false;
                AppLog.i("fd", "onRecommendList 3");
                isLoadingMore = false;
                AppLog.i("fd", "onRecommendList 4");
                mXrvRecommend.refreshComplete();
                AppLog.i("fd", "onRecommendList 5");
                mXrvRecommend.loadMoreComplete();
                AppLog.i("fd", "onRecommendList 6");
            }

            try {
                if (recommendListDataResp.getReturnCode() == 0) {
                    AppLog.i("fd", "onRecommendList 7");
                    // 获取首页推荐列表
                    mRecommendListBeen = recommendListDataResp.getResult();
                    AppLog.i("fd", "onRecommendList 8");
//                    mHanlder.sendEmptyMessage(GET_LIVE_COMODITY_SPECIAL);

                    if (isRefreshing) {
                        AppLog.i("fd", "onRecommendList 9");
                        mArticlePageNum = 1;
                        AppLog.i("fd", "onRecommendList 10");
                    }
//                    mContentLoader.articleList(mArticlePageSize, mArticlePageNum);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onArticleListResult(final ArticlesResp articlesResp) {
            super.onArticleListResult(articlesResp);

            AppLog.i("fd", "onArticleListResult");

            try {
                if (articlesResp.getReturnCode() == 0) {
                    AppLog.i("fd", "onArticleListResult 5");
                    // 获取首页推荐文章列表
                    ArticlesResultBean articlesResultBean = articlesResp.getResult();
                    AppLog.i("fd", "onArticleListResult 6");
                    List<ArticleDetailsResultBean> articleList = articlesResultBean == null ? null : articlesResultBean.getRows();
                    AppLog.i("fd", "onArticleListResult 7");
                    if (isLoadingMore) {
                        AppLog.i("fd", "onArticleListResult 8");
                        if (articleList == null || articleList.size() == 0) {
                            AppLog.i("fd", "onArticleListResult 9");

                            isLoadingMore = false;
                            AppLog.i("fd", "onArticleListResult 10");
                            // setNoMore与loadMoreComplete不能共存，否则不能显示FooterView
                            mXrvRecommend.setNoMore(true);
                            AppLog.i("fd", "onArticleListResult 11");
                            Toast.makeText(getActivity(), "没有更多文章咯~", Toast.LENGTH_SHORT).show();
                            AppLog.i("fd", "onArticleListResult 12");
                            return;
                        } else {
                            mArticleList.addAll(articleList);
                        }

                        isLoadingMore = false;
                        AppLog.i("fd", "onArticleListResult 13");
                        mXrvRecommend.loadMoreComplete();
                        AppLog.i("fd", "onArticleListResult 14");
                        mRecommendAdapter.refreshArticle(mArticleList);
                        AppLog.i("fd", "onArticleListResult 15");
                        return;
                    }

                    if (isRefreshing) {
                        AppLog.i("fd", "onArticleListResult 16");
                        mArticleList = articleList;
                        AppLog.i("fd", "onArticleListResult 17");
                        setAdapter();
                        AppLog.i("fd", "onArticleListResult 18");
                        return;
                    }
                }

                if (isRefreshing) {
                    mCountRefresh++;
                    AppLog.i("fd", "onArticleListResult 1");
                    if (mCountRefresh == REFRESH_COMPLETE) {
                        AppLog.i("fd", "onArticleListResult 2");
                        mCountRefresh = 0;
                        isLoadingMore = false;
                        isRefreshing = false;
                        AppLog.i("fd", "onArticleListResult 3");
                        mXrvRecommend.refreshComplete();
                        AppLog.i("fd", "onArticleListResult 4");
                        mXrvRecommend.loadMoreComplete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
