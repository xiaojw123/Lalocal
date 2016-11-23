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
                    AppLog.i("art", "1");
                    // 获取首页推荐文章列表
                    ArticlesResultBean articlesResultBean = articlesResp.getResult();
                    AppLog.i("art", "2");
                    List<ArticleDetailsResultBean> articleList = articlesResultBean == null ? null : articlesResultBean.getRows();
                    AppLog.i("art", "3");
                    AppLog.i("art", "isLoadingMOre : " + isLoadingMore + "; isRefresing : " + isRefreshing);
                    if (isLoadingMore) {
                        AppLog.i("art", "4");
                        if (articleList == null || articleList.size() == 0) {
                            AppLog.i("art", "5");

                            isLoadingMore = false;
                            // setNoMore与loadMoreComplete不能共存，否则不能显示FooterView
                            mXrvRecommend.setNoMore(true);
                            AppLog.i("art", "6");
                            Toast.makeText(getActivity(), "没有更多文章咯~", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            mArticleList.addAll(articleList);
                            AppLog.i("art", "7");
                        }

                        isLoadingMore = false;
                        mXrvRecommend.loadMoreComplete();
                        AppLog.i("art", "8");
                        updateAdapter(REFRESH_ARTICLE);
                        AppLog.i("art", "9");
                    } else if (isRefreshing) {
                        mArticleList.clear();
                        AppLog.i("art", "10");
                    }
                    AppLog.i("art", "30");
                    mArticleList.addAll(articleList);
                    AppLog.i("art", "11");
                    updateAdapter(REFRESH_ARTICLE);
                    AppLog.i("art", "12");
                }
            } catch (Exception e) {
                e.printStackTrace();
                AppLog.i("art", "error " + e.getMessage());
            } finally {
                // 刷新完毕
                refreshComplete();
                AppLog.i("art", "13");
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
                mXrvRecommend.loadMoreComplete();
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
                    AppLog.i("upd", "mAdresult List is " + mAdResultList.size());
                    mRecommendAdapter.refreshAD(mAdResultList);
                    break;
                case REFRESH_PRODUCT_THEME:
                    AppLog.i("upd", "recommend list is " + mRecommendListBeen.toString());
                    mRecommendAdapter.refreshProductTheme(mRecommendListBeen);
                    break;
                case REFRESH_ARTICLE:
                    AppLog.i("upd", "article list is " + mArticleList.size());
                    mRecommendAdapter.refreshArticle(mArticleList);
                    break;
                case REFRESH_ALL:
                    AppLog.i("upd", "refresh_all");
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
