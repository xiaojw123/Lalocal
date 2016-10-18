package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.adapter.MyFavoriteRecyclerAdpater;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MyFavoriteActivity extends BaseActivity implements XRecyclerView.LoadingListener {
    public static final int UPDATE_MY_DATA = 0x12;
    XRecyclerView mXRecyclerView;
    View mNoFavoriteView;
    TextView mNoFavoriteContent;
    int userId;
    int pageNumb = 1;
    int toalPages;
    String token;
    boolean isRefresh, isLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        initView();
        setLoaderCallBack(new FavoriteCallBack());
        userId = UserHelper.getUserId(this);
        token = UserHelper.getToken(this);
        mXRecyclerView.setRefreshing(true);
    }


    private void initView() {
        mNoFavoriteView = findViewById(R.id.my_item_noting);
        mNoFavoriteContent = (TextView) findViewById(R.id.home_me_nothing_tv);
        mXRecyclerView = (XRecyclerView) findViewById(R.id.my_favorite_xrlv);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingMoreEnabled(false);
        mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mXRecyclerView.setLoadingListener(this);
    }

    @Override
    public void onRefresh() {
        AppLog.print("refreshing。。。。。。");
        isRefresh = true;
        mContentloader.getMyFavor(userId, token, 1);

    }

    @Override
    public void onLoadMore() {
        if (pageNumb < toalPages) {
            ++pageNumb;
            isLoadMore = true;
            mContentloader.getMyFavor(userId, token, pageNumb);
        } else {
            loadMoreComplete();
        }

    }

    private void refreshComplete() {
        AppLog.print("refreshComplete。。。。。。");
        isRefresh = false;
        mXRecyclerView.refreshComplete();
    }

    private void loadMoreComplete() {
        AppLog.print("loadMoreComplete。。。。。。");
        isLoadMore = false;
        mXRecyclerView.setNoMore(true);
    }


    class FavoriteCallBack extends ICallBack implements OnItemClickListener {
        MyFavoriteRecyclerAdpater adapter;
        List<FavoriteItem> toalItems;


        public FavoriteCallBack() {
            toalItems = new ArrayList<>();
        }

        @Override
        public void onGetFavoriteItem(List<FavoriteItem> items, int pageNumber, int totalPages, int toalRows) {
            pageNumb = pageNumber;
            toalPages = totalPages;
            if (pageNumb == 1 && items == null || items.size() < 1) {
                AppLog.print("favorite items_____gone");
                mXRecyclerView.setVisibility(View.GONE);
                mNoFavoriteView.setVisibility(View.VISIBLE);
                mNoFavoriteContent.setText(getResources().getString(R.string.no_favorite));
            } else {
                AppLog.print("favorite items____visibile");
                if (mXRecyclerView.getVisibility() != View.VISIBLE) {
                    mXRecyclerView.setVisibility(View.VISIBLE);
                }
                if (mNoFavoriteView.getVisibility() == View.VISIBLE) {
                    mNoFavoriteView.setVisibility(View.GONE);
                }
                AppLog.print(" ISresfres__" + isRefresh);
                if (isRefresh) {
                    refreshComplete();
                }
                if (isLoadMore) {
                    loadMoreComplete();
                } else {
                    toalItems.clear();
                }
                toalItems.addAll(items);
                if (adapter == null) {
                    if (toalPages > 1) {
                        mXRecyclerView.setLoadingMoreEnabled(true);
                    }
                    adapter = new MyFavoriteRecyclerAdpater(items);
                    adapter.setOnItemClickListener(this);
                    mXRecyclerView.setAdapter(adapter);
                } else {
                    adapter.updateListView(toalItems);
                }
            }
        }

        @Override
        public void onResponseFailed() {
            AppLog.print("onResponseFailed______");
            if (isRefresh) {
                refreshComplete();
            }
            if (isLoadMore) {
                loadMoreComplete();
            }
        }

        @Override
        public void onError(VolleyError volleyError) {
            AppLog.print("onError______");
            if (isRefresh) {
                refreshComplete();
            }
            if (isLoadMore) {
                loadMoreComplete();
            }
        }


        @Override
        public void onItemClickListener(View view, int position) {
            FavoriteItem item = (FavoriteItem) view.getTag();
            if (item != null) {
                switch (item.getTargetType()) {//2产品 9线路 10专题 13资讯
                    case 1://文章
                    case 13://资讯
                        gotoArticleDetail(item);
                        break;
                    case 2://产品
                        gotoProductDetail(item);
                        break;
                    case 9://线路
                        gotoRouteDetail(item);
                        break;
                    case 10://专题
                        gotoSpecialDetail(item);
                        break;
                }
            }


        }

    }

    private void gotoProductDetail(FavoriteItem item) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        SpecialToH5Bean bean = new SpecialToH5Bean();
        bean.setTargetId(item.getTargetId());
        bean.setTargetType(item.getTargetType());
        intent.putExtra("productdetails", bean);
        startActivityForResult(intent, 100);
    }

    public void gotoSpecialDetail(FavoriteItem item) {
        Intent intent = new Intent(this, SpecialDetailsActivity.class);
        intent.putExtra("rowId", String.valueOf(item.getTargetId()));
        startActivityForResult(intent, 100);
    }

    private void gotoArticleDetail(FavoriteItem item) {
        AppLog.print("targetId___" + item.getTargetId());
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("targetID", String.valueOf(item.getTargetId()));
        startActivityForResult(intent, 100);
    }

    private void gotoRouteDetail(FavoriteItem item) {
        Intent intent = new Intent(this, RouteDetailActivity.class);
        intent.putExtra(RouteDetailActivity.DETAILS_ID, item.getTargetId());
        startActivityForResult(intent, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppLog.print("onActivityResult  rescode______"+resultCode);
        if (resultCode == UPDATE_MY_DATA) {
            mContentloader.getMyFavor(userId, token, 1);
        }

    }
}
