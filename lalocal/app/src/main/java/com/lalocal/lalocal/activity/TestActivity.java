package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.xlistview.XListView;

public class TestActivity extends AppCompatActivity implements XListView.IXListViewListener, AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        XListView mlistview= (XListView) findViewById(R.id.test_xlistview);
        View headView= LayoutInflater.from(this).inflate(R.layout.home_me_layout,null);
        mlistview.setPullLoadEnable(true);
        mlistview.setPullRefreshEnable(true);
        mlistview.setXListViewListener(this);
        mlistview.addHeaderView(headView);
        mlistview.setOnItemClickListener(this);
//        List<String> list=new ArrayList<>();
//        list.add("1__");
//        list.add("2__");
//        list.add("3__");
//        list.add("4__");
//        list.add("5__");
//        mlistview.setAdapter(new MyFavoriteAdapter(this,list));






    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
