package com.lalocal.lalocal.activity;

import android.os.Bundle;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;

import butterknife.ButterKnife;

public class ArticleCommentActivity extends BaseActivity {

    // 内容加载器
    private ContentLoader mContentLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);

        // 使用ButterKnife框架
        ButterKnife.bind(this);

        // 初始化ContentLoader
        initLoader();
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
//        mContentLoader.getArticleComments();
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
}
