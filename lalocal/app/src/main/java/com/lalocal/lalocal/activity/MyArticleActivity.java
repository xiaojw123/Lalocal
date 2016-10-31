package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.adapter.HomepageArticleAdapter;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.HomepageUserArticlesResp;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.CustomTitleView;

import java.util.ArrayList;
import java.util.List;

public class MyArticleActivity extends BaseActivity implements XRecyclerView.LoadingListener{


    XRecyclerView mXRecyclerView;
    CustomTitleView mCustomTitleView;
    TextView totalNumTv;
    boolean isRefresh, isLoadMore;
    int pageNumb, toalPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_article);
        initRecycerView();
        setLoaderCallBack(new MyArticelHolder());
        mXRecyclerView.setRefreshing(true);
    }

    private void initRecycerView() {
        mCustomTitleView=(CustomTitleView) findViewById(R.id.my_article_ctv);
        totalNumTv = (TextView) findViewById(R.id.my_article_num);
        mXRecyclerView = (XRecyclerView) findViewById(R.id.my_article_xrlv);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingMoreEnabled(false);
        mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mXRecyclerView.setLoadingListener(this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mContentloader.getUserArticles(UserHelper.getUserId(this), 1);
    }

    @Override
    public void onLoadMore() {
        if (pageNumb < toalPages) {
            ++pageNumb;
            isLoadMore = true;
            mContentloader.getUserArticles(UserHelper.getUserId(this), pageNumb);
        } else {
            isLoadMore = false;
            mXRecyclerView.setNoMore(true);
        }

    }


    public void resetLoad() {
        if (isRefresh) {
            isRefresh = false;
            mXRecyclerView.refreshComplete();
        }
        if (isLoadMore) {
            isLoadMore = false;
            mXRecyclerView.setNoMore(true);
        }
    }



    class MyArticelHolder extends ICallBack {

        HomepageArticleAdapter adapter;
        List<ArticleDetailsResultBean> rowsList=new ArrayList<>();

        public void onError(VolleyError volleyError) {
            resetLoad();
        }

        @Override
        public void onResponseFailed(int code,String message) {
            resetLoad();
        }


        @Override
        public void onGetUserArticles(HomepageUserArticlesResp articlesResp) {
            if (articlesResp != null) {
                HomepageUserArticlesResp.ResultBean rowsBean = articlesResp.getResult();
                if (rowsBean != null) {
                    toalPages = rowsBean.getTotalPages();
                    pageNumb = rowsBean.getPageNumber();
                    totalNumTv.setText("共" + rowsBean.getTotalRows() + "篇");
                    if (isRefresh) {
                        isRefresh = false;
                        mXRecyclerView.refreshComplete();
                    }
                    if (isLoadMore) {
                        isLoadMore = false;
                        mXRecyclerView.setNoMore(true);
                    } else {
                        rowsList.clear();
                    }
                    rowsList.addAll(rowsBean.getRows());
                    if (adapter == null) {
                        adapter = new HomepageArticleAdapter(MyArticleActivity.this, rowsList);
                        mXRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateItems(rowsList);
                    }
                }


            }


        }
    }
}
