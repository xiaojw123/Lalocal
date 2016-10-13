package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.UserLiveItem;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.MyLiveAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyLiveActivity extends BaseActivity implements XRecyclerView.LoadingListener {
    XRecyclerView mXRecyclerView;
    TextView totalNumTv;
    int pageNum, pageSize;
    boolean isRefresh, isLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_live);
        initView();
        setLoaderCallBack(new LiveCallBack());
        mXRecyclerView.setRefreshing(true);
    }

    private void initView() {
        totalNumTv = (TextView) findViewById(R.id.my_live_toalnum_tv);
        mXRecyclerView = (XRecyclerView) findViewById(R.id.my_live_items_xrlv);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingListener(this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mContentloader.getUserLive(9368, 1);
//        mContentloader.getUserLive(UserHelper.getUserId(this), 1);
    }

    @Override
    public void onLoadMore() {
        if (pageNum<pageSize){
            isLoadMore=true;
            ++pageNum;
//            mContentloader.getUserLive(UserHelper.getUserId(this),pageNum);
            mContentloader.getUserLive(9368,pageNum);
        }else{
            isLoadMore=false;
            mXRecyclerView.setNoMore(true);
        }


    }

    class LiveCallBack extends ICallBack {
        List<UserLiveItem.RowsBean> rowsList = new ArrayList<>();
        MyLiveAdapter adapter;

        @Override
        public void onGetUserLive(UserLiveItem item) {
            if (item != null) {
                int toalRows = item.getTotalRows();
                if (toalRows > 0) {
                    pageNum = item.getPageNumber();
                    pageSize = item.getPageSize();
                    totalNumTv.setText("共"+item.getTotalRows()+"篇");
                    if (isRefresh){
                        isRefresh=false;
                        mXRecyclerView.refreshComplete();
                    }
                    if (isLoadMore) {
                        isLoadMore=false;
                        mXRecyclerView.setNoMore(true);
                    } else {
                        rowsList.clear();
                    }
                    rowsList.addAll(item.getRows());
                    if (adapter==null){
                        adapter=new MyLiveAdapter(rowsList);
                        mXRecyclerView.setAdapter(adapter);
                    }else{
                        adapter.updateItems(rowsList);
                    }
                } else {
                    mXRecyclerView.setVisibility(View.GONE);
                }

            }
        }

        public void resetLoad(){
            if (isRefresh){
                isRefresh=false;
                mXRecyclerView.refreshComplete();
            }
            if (isLoadMore) {
                isLoadMore=false;
                mXRecyclerView.setNoMore(true);
            }
        }


        @Override
        public void onError(VolleyError volleyError) {
            resetLoad();
        }

        @Override
        public void onResponseFailed() {
            resetLoad();
        }
    }
}
