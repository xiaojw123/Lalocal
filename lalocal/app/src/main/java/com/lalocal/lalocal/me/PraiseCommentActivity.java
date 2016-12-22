package com.lalocal.lalocal.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.model.PraiseComment;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.PraiseCommentAdapter;

import java.util.ArrayList;
import java.util.List;

public class PraiseCommentActivity extends BaseActivity implements XRecyclerView.LoadingListener {
    int pageNum, toalPages;
    boolean isRefresh, isLoadMore;
    PraiseCommentAdapter praiseCommentAdapter;


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
        praiseCommentAdapter=new PraiseCommentAdapter();
        mXRecyclerView.setAdapter(praiseCommentAdapter);
        mXRecyclerView.setLoadingListener(this);
        setLoaderCallBack(new PraiseCommentCallBack());
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
            isLoadMore=true;
            pageNum++;
            mContentloader.getPraiseComment(pageNum);
        }else{
            mXRecyclerView.setNoMore(true);
        }
    }

    class PraiseCommentCallBack extends ICallBack {
        List<PraiseComment.RowsBean> rowList=new ArrayList<>();;
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
            }else{
                rowList.clear();
            }
            rowList.addAll(praiseComment.getRows());
            praiseCommentAdapter.updatItems(rowList);
        }
    }
}
