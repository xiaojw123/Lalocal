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
public class RecommendNewFragment extends BaseFragment {

    @BindView(R.id.xrv_recommend)
    CustomXRecyclerView mXrvRecommend;

    private HomeRecommendAdapter mRecommendAdapter;

    private List<RecommendAdResultBean> mAdResultList = new ArrayList<>();
    private RecommendListBean mRecommendListBeen;
    private List<ArticleDetailsResultBean> mArticleList;

    private ContentLoader mContentLoader;

    private int mArticlePageSize = 10;
    private int mArticlePageNum = 1;

    private int mScrolledYDistance;

    // 是否在刷新
    private boolean isRefreshing = false;
    // 是否在加载
    private boolean isLoadingMore = false;

    private static final int DATA_GOT_FINISHED = 3;

//    private static final int GET_ADVERTISEMENT = 0x01;
//    private static final int GET_LIVE_COMODITY_SPECIAL = 0x02;
//    private static final int GET_ARTICLE_LIST = 0x03;
//    private static final int NO_MORE_ARTICLE = 0x04;
//    private final Handler mHanlder = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case GET_ADVERTISEMENT:
//                    mAdResultList = (List<RecommendAdResultBean>) msg.obj;
//                    mContentLoader.indexRecommentList();
////                    mRecommendAdapter.setAdData(mAdResultList);
//                    break;
//                case GET_LIVE_COMODITY_SPECIAL:
//                    if (isRefreshing) {
//                        mArticlePageNum = 1;
//                    }
//                    mContentLoader.articleList(mArticlePageSize, mArticlePageNum);
////                    mRecommendAdapter.setListData(mRecommendListBeen);
//                    break;
//                case GET_ARTICLE_LIST:
//                    if (isRefreshing) {
//                        AppLog.i("xrv", "refreshing--" + mArticleList.size());
//                        isLoadingMore = false;
//                        isRefreshing = false;
//                        mXrvRecommend.refreshComplete();
////                        mRecommendAdapter.notifyDataSetChanged();
//                        setAdapter();
////                        mPtrLayout.loadMoreComplete(false);
//                        break;
//                    }
//                    if (isLoadingMore) {
//                        AppLog.i("xrv", "loadingMore--" + mArticleList.size());
//                        isRefreshing = false;
//                        isLoadingMore = false;
//                        mRecommendAdapter.setArticleData(mArticleList);
//                        mXrvRecommend.loadMoreComplete();
//                        break;
//                    }
//                    // 第一次刷新时，初始化RecyclerView
//                    setAdapter();
////                    mRecommendAdapter.setArticleData(mArticleList);
//                    break;
//                case NO_MORE_ARTICLE:
//                    isRefreshing = false;
//                    isLoadingMore = false;
//                    // setNoMore与loadMoreComplete不能共存，否则不能显示FooterView
//                    mXrvRecommend.setNoMore(true);
//                    Toast.makeText(getActivity(), "没有更多文章咯~", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };

    /**
     * 设置适配器
     */
    private void setAdapter() {
        // 初始化适配器
        mRecommendAdapter = new HomeRecommendAdapter(getActivity(), mAdResultList, mRecommendListBeen, mArticleList, mXrvRecommend);
        // 设置适配器
        mXrvRecommend.setAdapter(mRecommendAdapter);
        // 加载数据以后显示加载更多
        mXrvRecommend.setLoadingMoreEnabled(true);
    }

    public RecommendNewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_recommend_new_layout, container, false);
        ButterKnife.bind(this, view);

        mXrvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                AppLog.i("scro", "state -- " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                AppLog.i("scro", "dx = " + dx + "; dy = " + dy);
            }
        });
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
                // 隐藏首页推荐广告栏
//                    mContentLoader.recommendAd();
                mContentLoader.indexRecommentList();
            } else {
                mXrvRecommend.refreshComplete();
            }
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
        // 首页推荐隐藏广告栏
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
//                    Message msg = new Message();
//                    msg.what = GET_ADVERTISEMENT;
//                    msg.obj = adResultList;
//                    mHanlder.sendMessage(msg);

                    mAdResultList.clear();
                    mAdResultList.addAll(adResultList);
                    mContentLoader.indexRecommentList();
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
            try {
                if (recommendListDataResp.getReturnCode() == 0) {
                    // 获取首页推荐列表
                    mRecommendListBeen = recommendListDataResp.getResult();
//                    mHanlder.sendEmptyMessage(GET_LIVE_COMODITY_SPECIAL);

                    if (isRefreshing) {
                        mArticlePageNum = 1;
                    }
                    mContentLoader.articleList(mArticlePageSize, mArticlePageNum);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                        if (articleList == null || articleList.size() == 0) {
//                            mHanlder.sendEmptyMessage(NO_MORE_ARTICLE);

                            isRefreshing = false;
                            isLoadingMore = false;
                            // setNoMore与loadMoreComplete不能共存，否则不能显示FooterView
                            mXrvRecommend.setNoMore(true);
                            Toast.makeText(getActivity(), "没有更多文章咯~", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            mArticleList.addAll(articleList);
                        }
//                        mHanlder.sendEmptyMessage(GET_ARTICLE_LIST);

                        isRefreshing = false;
                        isLoadingMore = false;
                        mRecommendAdapter.setArticleData(mArticleList);
                        mXrvRecommend.loadMoreComplete();
                        return;
                    }

                    if (isRefreshing){
                        mArticleList = articleList;
//                        mHanlder.sendEmptyMessage(GET_ARTICLE_LIST);

                        isLoadingMore = false;
                        isRefreshing = false;
                        mXrvRecommend.refreshComplete();
//                        mRecommendAdapter.notifyDataSetChanged();
                        setAdapter();
//                        mPtrLayout.loadMoreComplete(false);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
