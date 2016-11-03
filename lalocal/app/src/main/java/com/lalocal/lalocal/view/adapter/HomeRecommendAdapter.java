package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.ProductDetailsResultBean;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.RecommendListBean;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomXRecyclerView;
import com.lalocal.lalocal.view.MyPtrClassicFrameLayout;
import com.lalocal.lalocal.view.viewholder.ArticleViewHolder;
import com.lalocal.lalocal.view.viewholder.CategoryViewHolder;
import com.lalocal.lalocal.view.viewholder.ChannelViewHolder;
import com.lalocal.lalocal.view.viewholder.ProductViewHolder;
import com.lalocal.lalocal.view.viewholder.ThemeViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：lalocal
 * 模块名称：
 * 包名：com.lalocal.lalocal.view.adapter
 * 类功能：
 * 创建人：wangjie
 * 创建时间：2016 16/9/9 下午2:54
 * 联系邮箱: wjnovember@icloud.com
 */
public class HomeRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<RecommendAdResultBean> mAdList;
    private RecommendListBean mRecommendListBean;
    private List<ArticleDetailsResultBean> mArticleList;

    private List<Integer> mItems = new ArrayList<Integer>();

    // 广告
//    private static final int ADVERTISEMENT = 0;
    // 分类
    private static final int CATEGORY = 0;
    // 热门直播
    private static final int LIVE = 1;
    // 迷人又可爱的商品们
    private static final int PRODUCT = 2;
    // 精彩专题
    private static final int THEME = 3;
    // 旅行笔记
    private static final int ARTICLE = 4;

    private boolean adEmpty;
    private boolean liveEmpty;
    private boolean productEmpty;
    private boolean themeEmpty;
    private boolean articleEmpty;

//    private CustomXRecyclerView mXRecyclerView;

    public HomeRecommendAdapter(Context context) {
        this.mContext = context;
    }

    public HomeRecommendAdapter(Context context, List<RecommendAdResultBean> adList, RecommendListBean recommendListBean,
                                List<ArticleDetailsResultBean> articleList, CustomXRecyclerView recyclerView) {
        this.mContext = context;
        this.mAdList = adList;
        this.mRecommendListBean = recommendListBean;
        this.mArticleList = articleList;
//        this.mXRecyclerView = recyclerView;

//        if (adList == null || adList.size() == 0) {
//            AppLog.i("hehe", "ad null");
//            adEmpty = true;
//        }

        if (recommendListBean == null) {
            AppLog.e("hehe", "recommendListBean is null");
            liveEmpty = true;
            productEmpty = true;
            themeEmpty = true;

        } else {
            if (recommendListBean.getChannelList() == null || recommendListBean.getChannelList().size() == 0) {
                AppLog.e("hehe", "channel list null");
                liveEmpty = true;

            }
            if (recommendListBean.getProduList() == null || recommendListBean.getProduList().size() == 0) {
                AppLog.e("hehe", "product list null");
                productEmpty = true;

            }
            if (recommendListBean.getThemeList() == null || recommendListBean.getThemeList().size() == 0) {
                AppLog.e("hehe", "theme list null");
                themeEmpty = true;

            }
        }

        if (articleList == null || articleList.size() == 0) {
            AppLog.i("hehe", "article list null");
            articleEmpty = true;
        }

        AppLog.i("recc", liveEmpty + "-" + productEmpty + "-" + themeEmpty + "-" + articleEmpty);

        syncItems();
    }

    public void setArticleData(List<ArticleDetailsResultBean> articleList) {
        this.mArticleList = articleList;
        if (articleList == null || articleList.size() == 0) {
            AppLog.i("hehe", "article list null");

            // 如果原来存在文章列表
            if (articleEmpty == false) {
                articleEmpty = true;
            }
        } else {
            // 如果原来没有文章列表
            if (articleEmpty == true) {
                articleEmpty = false;
            }
        }

        syncItems();
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppLog.i("recc", "viewType " + viewType);
        RecyclerView.ViewHolder holder = null;

        AppLog.i("recc", "onCreateViewHolder viewwtype " + viewType);
         View view = null;
        switch (viewType) {
            case CATEGORY: // 分类
                view = LayoutInflater.from(mContext).inflate(R.layout.home_recommend_category_item, parent, false);
                holder = new CategoryViewHolder(mContext, view, (RecyclerView)parent);
                break;
            case LIVE: // 热门直播
                view = View.inflate(mContext, R.layout.home_recommend_hotlive_item, null);
                holder = new ChannelViewHolder(mContext, view, liveEmpty);
                break;
            case PRODUCT: // 迷人又可爱的商品们
                view = View.inflate(mContext, R.layout.home_recommend_product_item, null);
                holder = new ProductViewHolder(mContext, view, productEmpty);
                break;
            case THEME: // 精彩专题
                view = View.inflate(mContext, R.layout.home_recommend_theme_item, null);
                holder = new ThemeViewHolder(mContext, view, themeEmpty);
                break;
            case ARTICLE: // 旅行笔记
                view = View.inflate(mContext, R.layout.home_recommend_article_item, null);
                holder = new ArticleViewHolder(mContext, view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppLog.i("recc", "onBindViewHolder viewwtype " + getItemViewType(position));
        switch (getItemViewType(position)) {
            case LIVE:
                if (liveEmpty) {
                    ((ChannelViewHolder) holder).initData(null, null, null);
                    return;
                }
                List<LiveRowsBean> liveBeanList = mRecommendListBean.getChannelList();
                String liveTitle = mRecommendListBean.getChannleCN();
                String liveSubTitle = mRecommendListBean.getChannleEN();
                ((ChannelViewHolder) holder).initData(liveBeanList, liveTitle, liveSubTitle);
                break;
            case PRODUCT:
                if (productEmpty) {
                    ((ProductViewHolder) holder).initData(null, null, null);
                    return;
                }
                List<ProductDetailsResultBean> productBeanList = mRecommendListBean.getProduList();
                String comTitle = mRecommendListBean.getProduCN();
                String comSubTitle = mRecommendListBean.getProduEN();
                ((ProductViewHolder) holder).initData(productBeanList, comTitle, comSubTitle);
                break;
            case THEME:
                if (themeEmpty) {
                    ((ThemeViewHolder) holder).initData(null, null, null);
                    return;
                }
                List<RecommendRowsBean> themeBeanList = mRecommendListBean.getThemeList();
                String themeTitle = mRecommendListBean.getThemeCN();
                String themeSubTitle = mRecommendListBean.getThemeEN();
                ((ThemeViewHolder) holder).initData(themeBeanList, themeTitle, themeSubTitle);
                break;
            case ARTICLE:
                if (articleEmpty) {
                    ((ArticleViewHolder) holder).initData(null, null, null);
                    return;
                }
                String diaryTitle = mRecommendListBean.getTravelCN();
                String diarySubTitle = mRecommendListBean.getTravelEN();
                ((ArticleViewHolder) holder).initData(mArticleList, diaryTitle, diarySubTitle);
                break;
        }
    }

    @Override
    public int getItemCount() {
        AppLog.i("recc", "1 the size is " + mItems.size());
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < mItems.size()) {
            int type = mItems.get(position);
            return type;
        }
        return position;
    }

    private void syncItems() {

        if (mItems.size() > 0) {
            mItems.clear();
        }

        liveEmpty = true;

        mItems.add(CATEGORY);
        if (!liveEmpty) {
            mItems.add(LIVE);
        }
        if (!productEmpty) {
            mItems.add(PRODUCT);
        }
        if (!themeEmpty) {
            mItems.add(THEME);
        }
        if (!articleEmpty) {
            mItems.add(ARTICLE);
        }
        AppLog.i("recc", "the size is " + mItems.size());
    }

}

