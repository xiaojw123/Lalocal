package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomXRecyclerView;
import com.lalocal.lalocal.view.adapter.ArticleCommentListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleCommentActivity extends BaseActivity {

    @BindView(R.id.img_write_comment)
    ImageView imgWriteComment;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.xrv_article_comments)
    CustomXRecyclerView xrvArticleComments;

    // 内容加载器
    private ContentLoader mContentLoader;
    // 适配器
    private ArticleCommentListAdapter mAdapter;

    // 文章id
    private String mArticleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);

        // 使用ButterKnife框架
        ButterKnife.bind(this);

        // 解析Intent
        parseIntent();
        // 初始化ContentLoader
        initLoader();
        // 初始化XRecyclerView
        initXRecyclerView();
    }

    /**
     * 解析intent
     */
    private void parseIntent() {
        // 获取intent
        Intent intent = getIntent();
        // 如果有intent传入
        if (intent != null) {
            // 获取文章id
            mArticleId = intent.getStringExtra(Constants.KEY_ARTICLE_ID);
        }
    }

    /**
     * 初始化ContentLoader
     */
    private void initLoader() {
        // 实例化ContentLoader
        mContentLoader = new ContentLoader(this);
        // 设置回调
        mContentLoader.setCallBack(new MyCallBack());
        // 请求
        mContentLoader.getArticleComments(mArticleId, 1);
    }

    /**
     * 初始化XRecyclerView
     */
    private void initXRecyclerView() {
        // 初始化适配器
        mAdapter = new ArticleCommentListAdapter(this);
        // 实例化布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 添加布局管理器
        xrvArticleComments.setLayoutManager(layoutManager);
        // 设置可下拉刷新
        xrvArticleComments.setPullRefreshEnabled(true);
        // 设置可加载更多
        xrvArticleComments.setLoadingMoreEnabled(true);
        // 设置适配器
        xrvArticleComments.setAdapter(mAdapter);
        // 设置监听事件
        xrvArticleComments.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                xrvArticleComments.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                xrvArticleComments.loadMoreComplete();
            }
        });
    }

    private class MyCallBack extends ICallBack {

        @Override
        public void onGetArticleComments(String json) {
            super.onGetArticleComments(json);
            AppLog.i("articleComments", "the json got is " + json);
        }

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);
        }
    }

    @OnClick({R.id.img_back, R.id.img_write_comment})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                ArticleCommentActivity.this.finish();
                break;
            case R.id.img_write_comment:
                startActivity(new Intent(ArticleCommentActivity.this, RePlyActivity.class));
                break;
        }
    }
}
