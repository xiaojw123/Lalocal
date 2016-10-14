package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.adapter.HomepageArticleAdapter;

public class MyArticleActivity extends BaseActivity {


    XRecyclerView mXRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_article);
        mXRecyclerView= (XRecyclerView) findViewById(R.id.my_article_xrlv);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.setAdapter(new HomepageArticleAdapter(this));

    }
}
