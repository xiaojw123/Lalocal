package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.ProductDetailsResultBean;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.RecommendListBean;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.view.viewholder.find.ADCategoryViewHolder;
import com.lalocal.lalocal.view.viewholder.find.ArticleViewHolder;
import com.lalocal.lalocal.view.viewholder.find.ChannelViewHolder;
import com.lalocal.lalocal.view.viewholder.find.ProductViewHolder;
import com.lalocal.lalocal.view.viewholder.find.ThemeViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wangjie at 2016 16/9/9 下午2:54
 *
 * 发现页的适配器类：
 * 多布局列表填充，对布局的类别判断详见:
 *
 * @see #getItemViewType(int)
 */
public class HomeRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    // 广告列表
    private List<RecommendAdResultBean> mAdList = new ArrayList<>();
    // 专题列表
    private List<RecommendRowsBean> mThemeList = new ArrayList<>();
    // 商品列表
    private List<ProductDetailsResultBean> mProduList = new ArrayList<>();
    // 文章列表
    private List<ArticleDetailsResultBean> mArticleList = new ArrayList<>();

    // 分类
    private static final int ADVERTISEMENT_CATEGORY = 0;
    // 热门直播
    private static final int LIVE = 1;
    // 迷人又可爱的商品们
    private static final int PRODUCT = 2;
    // 精彩专题
    private static final int THEME = 3;
    // 旅行笔记
    private static final int ARTICLE = 4;

    // -标题
    private String mTitleProductCN;
    private String mTitleProductEN;
    private String mTitleThemeCN;
    private String mTitleThemeEN;
    private String mTitleArticleCN;
    private String mTitleArticleEN;

    public HomeRecommendAdapter(Context context, List<RecommendAdResultBean> adList, RecommendListBean recommendListBean,
                                List<ArticleDetailsResultBean> articleList) {
        this.mContext = context;
        // 获取广告
        if (adList != null) {
            mAdList = adList;
        }

        // 获取专题、商品
        if (recommendListBean != null) {
            // 获取商品列表
            if (recommendListBean.getProduList() != null) {
                this.mProduList = recommendListBean.getProduList();
            } else {
                this.mProduList = new ArrayList<>();
            }
            // 获取专题列表
            if (recommendListBean.getThemeList() != null) {
                this.mThemeList = recommendListBean.getThemeList();
            } else {
                this.mThemeList = new ArrayList<>();
            }

            // -获取标题
            mTitleProductCN = recommendListBean.getProduCN();
            mTitleProductEN = recommendListBean.getProduEN();
            mTitleThemeCN = recommendListBean.getThemeCN();
            mTitleThemeEN = recommendListBean.getThemeEN();
            mTitleArticleCN = recommendListBean.getTravelCN();
            mTitleArticleEN = recommendListBean.getTravelEN();
        }

        // 获取文章列表
        if (articleList != null) {
            mArticleList = articleList;
        }

    }

    /**
     * 刷新广告
     *
     * @param adList
     */
    public void refreshAD(List<RecommendAdResultBean> adList) {
        if (adList == null || adList.size() == 0) {
            this.mAdList = new ArrayList<>();
        } else {
            this.mAdList = adList;
        }
        this.notifyDataSetChanged();
    }

    /**
     * 刷新文章
     *
     * @param articleList
     */
    public void refreshArticle(List<ArticleDetailsResultBean> articleList) {
        if (articleList == null || articleList.size() == 0) {
            mArticleList = new ArrayList<>();
        } else {
            this.mArticleList = articleList;
        }
        this.notifyDataSetChanged();
    }

    /**
     * 刷新所有
     * @param adList
     * @param recommendListBean
     * @param articleList
     */
    public void refreshAll(List<RecommendAdResultBean> adList, RecommendListBean recommendListBean,
                           List<ArticleDetailsResultBean> articleList) {
        // 刷新广告栏
        if (adList == null || adList.size() == 0) {
            this.mAdList = new ArrayList<>();
        } else {
            this.mAdList = adList;
        }

        // 刷新商品、专题
        if (recommendListBean != null) {
            // 获取商品列表
            if (recommendListBean.getProduList() != null) {
                this.mProduList = recommendListBean.getProduList();
            } else {
                this.mProduList = new ArrayList<>();
            }
            // 获取专题列表
            if (recommendListBean.getThemeList() != null) {
                this.mThemeList = recommendListBean.getThemeList();
            } else {
                this.mThemeList = new ArrayList<>();
            }
        }

        // 刷新文章列表
        if (articleList == null || articleList.size() == 0) {
            mArticleList = new ArrayList<>();
        } else {
            this.mArticleList = articleList;
        }

        // 刷新
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        View view = null;
        switch (viewType) {
            case ADVERTISEMENT_CATEGORY: // 广告、分类
                view = LayoutInflater.from(mContext).inflate(R.layout.home_recommend_category_item, parent, false);
                holder = new ADCategoryViewHolder(mContext, view, (RecyclerView) parent);
                break;
            case LIVE: // 热门直播
                view = View.inflate(mContext, R.layout.home_recommend_hotlive_item, null);
                holder = new ChannelViewHolder(mContext, view);
                break;
            case PRODUCT: // 迷人又可爱的商品们
                view = View.inflate(mContext, R.layout.home_recommend_product_item, null);
                holder = new ProductViewHolder(mContext, view);
                break;
            case THEME: // 精彩专题
                view = View.inflate(mContext, R.layout.home_recommend_theme_item, null);
                holder = new ThemeViewHolder(mContext, view);
                break;
            case ARTICLE: // 旅行笔记
                view = View.inflate(mContext, R.layout.home_recommend_article_list_item, null);
                holder = new ArticleViewHolder(mContext, view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ADVERTISEMENT_CATEGORY:
                ((ADCategoryViewHolder) holder).initData(mAdList);
                break;
            case PRODUCT:
                ((ProductViewHolder) holder).initData(mProduList, mTitleProductCN, mTitleProductEN);
                break;
            case THEME:
                ((ThemeViewHolder) holder).initData(mThemeList, mTitleThemeCN, mTitleThemeEN);
                break;
            case ARTICLE:
                // 获取第一篇文章的位置
                int firstIndex = getFirstArticlePositoin();
                // 当前下标
                int index = position - firstIndex;
                // 获取文章bean
                ArticleDetailsResultBean article = mArticleList.get(index);
                // 标题是否显示
                boolean showHeader = false;
                // 根据下标判断是否显示header
                if (position == firstIndex)
                    showHeader = true;
                // 初始化数据
                ((ArticleViewHolder) holder).initData(article, mTitleArticleCN, mTitleArticleEN, showHeader);
                break;
        }
    }

    /**
     * 获取第一篇文章对应列表的下标
     *
     * 为实现文章子项在XRecyclerView的位置与文章List的下表对应起来，
     * 需要找到下标为0的文章在XRecyclerView中对应的位置
     * @return
     */
    public int getFirstArticlePositoin() {
        // 列表item总数
        int size = getItemCount();
        // 文章列表第一个item所在的下标
        int firstIndex = size - mArticleList.size();
        // 返回下标
        return firstIndex;
    }


    @Override
    public int getItemCount() {
        // 广告、分类
        int count = 1;
        // 商品
        if (mProduList.size() > 0)
            count++;
        // 专题
        if (mThemeList.size() > 0)
            count++;
        // 文章
        if (mArticleList.size() > 0)
            count += mArticleList.size();

        return count;
    }

    /**
     * 根据position的不同判断哪个position对应哪个布局
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ADVERTISEMENT_CATEGORY;
        } else if (position == 1) {
            if (mProduList.size() > 0) {
                return PRODUCT;
            } else if (mThemeList.size() > 0) {
                return THEME;
            } else {
                return ARTICLE;
            }
        } else if (position == 2) {
            if (mThemeList.size() > 0) {
                return THEME;
            } else {
                return ARTICLE;
            }
        }
        return ARTICLE;
    }
}

