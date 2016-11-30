package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.ui.CustomLinearLayoutManager;
import com.lalocal.lalocal.model.RecommendDataResp;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.ThemeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeActivity extends BaseActivity {


    @BindView(R.id.rv_theme)
    XRecyclerView xRecyclerView;

    private RecyclerAdapterWithHF mAdapter;
    private ThemeAdapter mThemeAdapter;
    private ContentLoader mContentLoader;

    private List<RecommendRowsBean> mThemeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        // 使用ButterKnife
        ButterKnife.bind(this);
        initLoader();
        initRecyclerView();
    }
    private void initLoader() {
        mContentLoader = new ContentLoader(this);
        mContentLoader.setCallBack(new MyCallBack());
    }
    private void initRecyclerView() {
        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        XRecyclerviewLoadingListener xRecyclerviewLoadingListener = new XRecyclerviewLoadingListener();
        xRecyclerView.setLoadingListener(xRecyclerviewLoadingListener);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshing(true);

    }
    private int mPageSize = 10;
    int pageNum=1;
    boolean isRefresh = false;
    public class XRecyclerviewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            isRefresh = true;
            pageNum=1;
            mContentLoader.recommentList(mPageSize, pageNum);
        }
        @Override
        public void onLoadMore() {
            isRefresh = false;
            ++pageNum;
            mContentLoader.recommentList(mPageSize, pageNum);
        }

    }

    List<RecommendRowsBean> rowsBeanList=new ArrayList<>();
    private class MyCallBack extends ICallBack {

        /**
         * 专题列表，因旧版本该模块在首页推荐，因此名字为onRecommend
         * @param recommendDataResp
         */
        @Override
        public void onRecommend(RecommendDataResp recommendDataResp) {
            super.onRecommend(recommendDataResp);
            if(recommendDataResp.getReturnCode()==0){
                List<RecommendRowsBean> rows = recommendDataResp.getResult().getRows();
                if(rows==null){
                    return;
                }
                if(isRefresh){
                    rowsBeanList.clear();
                }
                if(rowsBeanList.size()==0){
                    rowsBeanList.addAll(0,rows);
                } else {
                    rowsBeanList.addAll(rowsBeanList.size(), rows);
                }
                if(mThemeAdapter==null){
                    mThemeAdapter=new ThemeAdapter(ThemeActivity.this,rowsBeanList);
                    xRecyclerView.setAdapter(mThemeAdapter);


                }else {
                    mThemeAdapter.setResh(rowsBeanList);
                }
                if (isRefresh) {
                    xRecyclerView.refreshComplete();
                }  else {
                    if(rows.size()<10){
                        xRecyclerView.setNoMore(true);
                    }else{
                        xRecyclerView.loadMoreComplete();
                    }
                }
            }
        }
    }
}
