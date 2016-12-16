package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomXRecyclerView;

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
