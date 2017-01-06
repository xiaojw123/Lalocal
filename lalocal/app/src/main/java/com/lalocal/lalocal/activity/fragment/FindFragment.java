package com.lalocal.lalocal.activity.fragment;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
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
import com.lalocal.lalocal.view.CustomXRecyclerView;
import com.lalocal.lalocal.view.adapter.HomeRecommendAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by Wangjie
 * <p>
 * 【发现页】
 * 包括：广告位、分类栏、商品列表、专题列表、文章列表
 * <p>
 * *广告位* {@link com.lalocal.lalocal.view.viewholder.find.ADCategoryViewHolder}
 * 使用AndroidImageSlider框架，实现无限轮播图的效果，Github网址：https://github.com/daimajia/AndroidImageSlider
 * <p>
 * *分类栏* {@link com.lalocal.lalocal.view.viewholder.find.ChannelViewHolder}
 * 点击“旅行专题”，跳转专题列表页面 {@link com.lalocal.lalocal.activity.ThemeActivity}
 * 点击“旅游笔记”，跳转文章列表 {@link com.lalocal.lalocal.view.viewholder.find.ArticleViewHolder}
 * 点击“旅游商城”，跳转目的地页面 {@link com.lalocal.lalocal.activity.DestinationActivity}
 * <p>
 * *商品列表* {@link com.lalocal.lalocal.view.viewholder.find.ProductViewHolder}
 * 包含4个商品预览和More {@link com.lalocal.lalocal.activity.DestinationActivity}
 * 使用GridView实现，对GridView重新计算高度
 * <p>
 * *专题列表* {@link com.lalocal.lalocal.view.viewholder.find.ThemeViewHolder}
 * 最多显示10页专题Item，向右滑动至底部，出现查看更多 {@link com.lalocal.lalocal.activity.ThemeActivity}
 * 使用ViewPager+View来实现
 * <p>
 * *文章列表* {@link com.lalocal.lalocal.view.viewholder.find.ArticleViewHolder}
 * 文章列表的每一个Item都是XRecyclerView的Item
 * <p>
 * -------------------------------------------------------------------------------------------------
 * 布局搭建策略：
 * 本页面使用XRecyclerView多布局填充实现，各布局作为XRecyclerView的子项填充进列表
 *
 * @see com.lalocal.lalocal.view.adapter.HomeRecommendAdapter#getItemViewType(int)
 * <p>
 * -------------------------------------------------------------------------------------------------
 * 卡顿优化策略：
 * Glide+XRecyclerView实现
 * 注意：
 * 1. Glide适合刷新频率比较低，图片数量比较多的情况使用
 * 2. Glide不适合于项目中的RoundedImageView共用
 * @see com.makeramen.roundedimageview.RoundedImageView
 * <p>
 * -------------------------------------------------------------------------------------------------
 * 网络数据请求策略：
 * 网络数据请求采用异步加载策略，本页面相关接口有三个，每获取一个接口数据，则刷新一次页面
 * @see com.lalocal.lalocal.net.ContentLoader#recommendAd()
 * @see com.lalocal.lalocal.net.ContentLoader#indexRecommentList()
 * @see com.lalocal.lalocal.net.ContentLoader#articleList(int, int)
 */
public class FindFragment extends BaseFragment {

    @BindView(R.id.xrv_recommend)
    CustomXRecyclerView mXrvRecommend;

    // 多布局列表适配器
    private HomeRecommendAdapter mRecommendAdapter;

    // 广告列表
    private List<RecommendAdResultBean> mAdResultList = new ArrayList<>();
    // 包含商品列表、专题列表的bean类
    private RecommendListBean mRecommendListBeen;
    // 文章列表
    private List<ArticleDetailsResultBean> mArticleList = new ArrayList<>();

    // 网络数据请求响应的类
    private ContentLoader mContentLoader;

    // 文章列表每页加载的数量
    private int mArticlePageSize = 10;
    // 文章列表当前加载的页码
    private int mArticlePageNum = 1;

    // 刷新区域的标记
    private static final int INIT = 0x00;
    private static final int REFRESH_AD = 0x01;
    private static final int REFRESH_ARTICLE = 0x02;

    // 是否在刷新
    private boolean isRefreshing = false;
    // 是否在加载
    private boolean isLoadingMore = false;

    // 刷新完成的标记，因三个接口，所以为3，表示所有的数据都获取完毕
    private static final int REFRESH_COMPLETE = 3;
    // 当前已经获取完毕的数据接口数量
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
        // 为防止发现页重现时，列表自动上下打滑
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
    }

    /**
     * XRecyclerView监听事件
     */
    private class XRecyclerViewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            // 之前出现列表控件不存在但列表刷新出现的空指针BUG，加上判断，防止异常发生
            if (mXrvRecommend != null) {
                isRefreshing = true;
                mXrvRecommend.setLoadingMoreEnabled(true);

                // 获取三个不同接口的数据
                mContentLoader.recommendAd();
                mContentLoader.indexRecommentList();
                mContentLoader.articleList(10, 0);
            } else {
                // 这里有点思路有点问题，但是最好不要动代码，防止出现BUG
                mXrvRecommend.refreshComplete();
            }
            // 重置当前文章列表的页码，从1开始加载
            mArticlePageNum = 1;
        }

        @Override
        public void onLoadMore() {
            isLoadingMore = true;
            mArticlePageNum++;
            // 获取文章接口数据
            mContentLoader.articleList(mArticlePageSize, mArticlePageNum);
        }
    }

    /**
     * 初始化ContentLoader
     */
    private void initLoader() {
        // 网络接口数据获取做准备
        mContentLoader = new ContentLoader(getActivity());
        // 设置回调监听
        mContentLoader.setCallBack(new MyCallBack());
    }

    /**
     * 回调监听类
     */
    public class MyCallBack extends ICallBack {

        /**
         * 获取不到数据时，停止刷新或加载
         * @param volleyError
         */
        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);

            if (isRefreshing) {
                isRefreshing = false;
                mCountRefresh = 0;
                mXrvRecommend.refreshComplete();
            }

            if (isLoadingMore) {
                isLoadingMore = false;
                mXrvRecommend.loadMoreComplete();
            }
        }

        /**
         * 获取广告位数据
         * @param recommendAdResp
         */
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
         * 首页推荐列表，包括：专题、商品、直播列表(直播列表取消)
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
                    // 这里的INIT即表示初始化适配器，也可以表示刷新全局，
                    // 因存在notifyDataSetChanged无效的情况（涉及list.clear()+list.addAll()崩溃的问题，
                    // 所以不用这个策略而使用重新初始化适配器的策略）
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
                        // 局部刷新：刷新文章列表
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
     * 设置适配器，根据传入参数的不同实现不同范围的刷新
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
                    break;
                case REFRESH_ARTICLE:
                    mRecommendAdapter.refreshArticle(mArticleList);
                    break;
                case INIT:
                    mRecommendAdapter = null;
                    updateAdapter(INIT);
                    break;
            }
        }
    }
}
