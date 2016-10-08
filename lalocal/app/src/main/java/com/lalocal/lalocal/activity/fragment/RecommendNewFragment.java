package com.lalocal.lalocal.activity.fragment;


import android.app.Fragment;
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
import com.lalocal.lalocal.R;
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
public class RecommendNewFragment extends Fragment {

    private View mContentView;

    @BindView(R.id.recycleview_home_recomend)
    MyRecyclerView mRecyclerView;

    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    private HomeRecommendAdapter mRecommendAdapter;

    private List<RecommendAdResultBean> mAdResultList = new ArrayList<>();
    private RecommendListBean mRecommendListBeen;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;
    private List<ArticleDetailsResultBean> mArticleList;

    private ContentLoader mContentLoader;

    private int mArticlePageSize = 10;
    private int mArticlePageNum = 1;

    private boolean mScrolled = false;

    // 是否在刷新
    private boolean isRefreshing = false;
    // 是否在加载
    private boolean isLoadingMore = false;

    private static final int DATA_GOT_FINISHED = 3;

    private static final int GET_ADVERTISEMENT = 0x01;
    private static final int GET_LIVE_COMODITY_SPECIAL = 0x02;
    private static final int GET_ARTICLE_LIST = 0x03;
    private static final int NO_MORE_ARTICLE = 0x04;
    private final Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ADVERTISEMENT:
                    mAdResultList = (List<RecommendAdResultBean>) msg.obj;
                    mContentLoader.indexRecommentList();
//                    mRecommendAdapter.setAdData(mAdResultList);
                    break;
                case GET_LIVE_COMODITY_SPECIAL:
                    if (isRefreshing) {
                        mArticlePageNum = 1;
                    }
                    mContentLoader.articleList(mArticlePageSize, mArticlePageNum);
//                    mRecommendAdapter.setListData(mRecommendListBeen);
                    break;
                case GET_ARTICLE_LIST:
                    if (isRefreshing) {
                        isLoadingMore = false;
                        isRefreshing = false;
                        mPtrLayout.refreshComplete();
//                        mRecommendAdapter.notifyDataSetChanged();
                        setAdapter();
//                        mPtrLayout.loadMoreComplete(false);
                        break;
                    }
                    if (isLoadingMore) {
                        isRefreshing = false;
                        isLoadingMore = false;
                        mRecommendAdapter.setArticleData(mArticleList);
                        mPtrLayout.loadMoreComplete(true);
                        break;
                    }
                    // 第一次刷新时，初始化RecyclerView
                    setAdapter();
//                    mRecommendAdapter.setArticleData(mArticleList);
                    break;
                case NO_MORE_ARTICLE:
                    isRefreshing = false;
                    isLoadingMore = false;
                    mPtrLayout.loadMoreComplete(true);
                    mPtrLayout.setLoadMoreEnable(false);
                    View bottomView = LayoutInflater.from(getActivity()).inflate(R.layout.load_more_layout, null);
                    mRecyclerView.addView(bottomView);
                    Toast.makeText(getActivity(), "没有更多文章咯~", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 设置适配器
     */
    private void setAdapter() {
        // 初始化适配器
        mRecommendAdapter = new HomeRecommendAdapter(getActivity(), mAdResultList, mRecommendListBeen, mArticleList, mPtrLayout);
        mAdapter = new RecyclerAdapterWithHF(mRecommendAdapter);
        mRecyclerView.setAdapter(mAdapter);

        // 加载数据以后显示加载更多
        mPtrLayout.setLoadMoreEnable(true);
        // 设置适配器
//        mRecyclerView.setAdapter(mRecommendAdapter);
    }

    public RecommendNewFragment() {

    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        if (hidden) {
//            isRefreshing = false;
//            mPtrLayout.refreshComplete();
//            isLoadingMore = false;
//            mPtrLayout.loadMoreComplete(true);
//        }
//        super.onHiddenChanged(hidden);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.home_recommend_new_layout, container, false);
        ButterKnife.bind(this, mContentView);
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化视图
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        // -这里的调用顺序不能改变，否则会出现空指针错误
        // 初始化header
        initPtrLoadMore();
        // 初始化RecyclerView
        initRecyclerView();
        // 初始化ContentLoader
        initLoader();
        // 滑动不加载图片
//        scrollNotLoadPic();
    }

    /**
     * 初始化header
     */
    private void initPtrLoadMore() {
        // -下拉刷新
        // 实例化PTRHeader
        PtrHeader ptrHeader = new PtrHeader(getActivity());
        // 给PTR控件添加头视图
        mPtrLayout.setHeaderView(ptrHeader);
        // 下拉刷新支持时间
//        mPtrLayout.setLastUpdateTimeRelateObject(this);

        mPtrLayout.setResistance(1.7f);
        mPtrLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrLayout.setDurationToClose(200);
        mPtrLayout.setLoadingMinTime(1000);
        mPtrLayout.setKeepHeaderWhenRefresh(true);

        // -延时自动刷新数据
//        mPtrLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrLayout.autoRefresh(false);
//            }
//        }, 0);

        // 下拉刷新
        mPtrLayout.setPtrHandler(new com.chanven.lib.cptr.PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(com.chanven.lib.cptr.PtrFrameLayout frame) {
                isRefreshing = true;
                mPtrLayout.setLoadMoreEnable(true);
                mContentLoader.recommendAd();


            }
        });

        // 上拉加载更多
        mPtrLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                isLoadingMore = true;
                mArticlePageNum++;
                mContentLoader.articleList(mArticlePageSize, mArticlePageNum);
            }
        });

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        // 避免出现RecyclerView has no LayoutManager的错误
        mRecyclerView.setHasFixedSize(true);
        // 计算RecyclerView的大小，可以显示其内容
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * 初始化ContentLoader
     */
    private void initLoader() {
        mContentLoader = new ContentLoader(getActivity());
        mContentLoader.setCallBack(new MyCallBack());
        mContentLoader.recommendAd();
//        mContentLoader.indexRecommentList();
//        mContentLoader.articleList(10, 0);
    }

    /**
     * 滑动不加载图片
     */
//    private void scrollNotLoadPic() {
//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                switch (newState) {
//                    case RecyclerView.SCROLL_STATE_IDLE: // 当前未滑动
//                        if (mRecommendAdapter.getScrolling() && mScrolled) {
//                            // 对于滚动不加载图片的尝试
//                            mRecommendAdapter.setScrolling(false);
//                            mAdapter.notifyDataSetChangedHF();
//                        }
//                        mScrolled = false;
//                        break;
//                    case RecyclerView.SCROLL_STATE_DRAGGING: // 开始拖拽
//
//                        break;
//                    case RecyclerView.SCROLL_STATE_SETTLING: // 滑动到某个位置
//
//                        break;
//                }
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });
//
//    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onRecommendAd(final RecommendAdResp recommendAdResp) {
            super.onRecommendAd(recommendAdResp);
            try {
                if (recommendAdResp.getReturnCode() == 0) {
                    // 获取广告数据
                    List<RecommendAdResultBean> adResultList = recommendAdResp.getResult();
                    Message msg = new Message();
                    msg.what = GET_ADVERTISEMENT;
                    msg.obj = adResultList;
                    mHanlder.sendMessage(msg);
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
                    mHanlder.sendEmptyMessage(GET_LIVE_COMODITY_SPECIAL);
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
                            mHanlder.sendEmptyMessage(NO_MORE_ARTICLE);
                            return;
                        } else {
                            mArticleList.addAll(articleList);
                        }
                    } else {
//                        mArticleList.clear();
//                        AppLog.i("refreshs", "1");
//                        emptyArticleList();
//                        AppLog.i("refreshs", "2");
//                        mArticleList.addAll(articleList);
//                        AppLog.i("refreshs", "3");
                        mArticleList = articleList;
                    }
                    mHanlder.sendEmptyMessage(GET_ARTICLE_LIST);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清空列表
     */
    private void emptyArticleList() {
        AppLog.i("refreshs", "emptyArticleList");
        AppLog.i("refresh", "list size is " );
        for (int i = mArticleList.size() - 1; i >= 0; i++) {
            AppLog.i("refreshs", "size = " + mArticleList.size() + ", position is " + i);
            mArticleList.remove(i);
        }
    }
}
