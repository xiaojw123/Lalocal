package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
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

/**
 * 专题列表Activity
 */
public class ThemeActivity extends BaseActivity {


    @BindView(R.id.rv_theme)
    XRecyclerView xRecyclerView;

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

    /**
     * 网络数据获取准备
     */
    private void initLoader() {
        mContentLoader = new ContentLoader(this);
        mContentLoader.setCallBack(new MyCallBack());
    }

    /**
     * 初始化XRecyclerView
     */
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

    /**
     * 加载监听事件
     */
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

    /**
     * 网络接口获取结果
     */
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

                    rowsBeanList.addAll(rowsBeanList.size(), rows);

                if(mThemeAdapter==null){
                    mThemeAdapter=new ThemeAdapter(ThemeActivity.this,rowsBeanList);
                    xRecyclerView.setAdapter(mThemeAdapter);
                }else {
                    mThemeAdapter.setRefresh(rowsBeanList);
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

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);

            isRefresh = false;
            pageNum = 1;
            xRecyclerView.refreshComplete();
            xRecyclerView.loadMoreComplete();
        }
    }
}
