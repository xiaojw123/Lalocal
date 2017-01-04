package com.lalocal.lalocal.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.model.PraiseComment;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.PraiseCommentAdapter;
import com.lalocal.lalocal.view.decoration.LinearItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PraiseCommentActivity extends BaseActivity implements XRecyclerView.LoadingListener {
    int pageNum, toalPages, lastPageNum;
    boolean isRefresh, isLoadMore;
    PraiseCommentAdapter praiseCommentAdapter;
    List<PraiseComment.RowsBean> rowList = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, PraiseCommentActivity.class);
        context.startActivity(intent);
    }


    XRecyclerView mXRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise_comment);
        mXRecyclerView = (XRecyclerView) findViewById(R.id.praise_comment_xrl);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingMoreEnabled(true);
        praiseCommentAdapter = new PraiseCommentAdapter();
        mXRecyclerView.setAdapter(praiseCommentAdapter);
        mXRecyclerView.setLoadingListener(this);
        mXRecyclerView.addItemDecoration(new LinearItemDecoration(this));
        setLoaderCallBack(new PraiseCommentCallBack());

    }


    @Override
    protected void onStart() {
        super.onStart();
        mXRecyclerView.setRefreshing(true);
    }


    @Override
    public void onRefresh() {
        isRefresh = true;
        mContentloader.getPraiseComment(1);
    }

    @Override
    public void onLoadMore() {
        if (pageNum < toalPages) {
            isLoadMore = true;
            lastPageNum = pageNum;
            pageNum++;
            mContentloader.getPraiseComment(pageNum);
        } else {
            mXRecyclerView.setNoMore(true);
        }
    }

    class PraiseCommentCallBack extends ICallBack {

        @Override
        public void onError(VolleyError volleyError) {
            reset();
        }

        private void reset() {
            pageNum = lastPageNum;
            if (isRefresh) {
                isRefresh = false;
                mXRecyclerView.refreshComplete();
            }
            if (isLoadMore) {
                isLoadMore = false;
                mXRecyclerView.loadMoreComplete();
            }
        }

        @Override
        public void onResponseFailed(int returnCode, String message) {
            reset();
        }

        @Override
        public void onGetPraiseComment(PraiseComment praiseComment) {
            pageNum = praiseComment.getPageNumber();
            toalPages = praiseComment.getTotalPages();
            if (isRefresh) {
                isRefresh = false;
                mXRecyclerView.refreshComplete();
            }
            if (isLoadMore) {
                isLoadMore = false;
                mXRecyclerView.loadMoreComplete();
            } else {
                rowList.clear();
            }
            rowList.addAll(praiseComment.getRows());
            praiseCommentAdapter.updatItems(rowList);
        }
    }
}
