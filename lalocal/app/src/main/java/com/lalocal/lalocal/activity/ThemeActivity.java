package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RecommendDataResp;
import com.lalocal.lalocal.model.RecommendResultBean;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.adapter.ThemeAdapter;
import com.lalocal.lalocal.view.ptr.PtrHeader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class ThemeActivity extends AppCompatActivity {

    @BindView(R.id.ptr_frame_layout)
    PtrClassicFrameLayout mPtrFrame;

    @BindView(R.id.rv_theme)
    RecyclerView mRvTheme;

    private RecyclerAdapterWithHF mAdapter;
    private ThemeAdapter mThemeAdapter;

    private ContentLoader mContentLoader;

    private List<RecommendRowsBean> mThemeList;

    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;

    private int mPageNum = 0;
    private int mPageSize = 10;

    private static final int GET_THEME_SUCCESS = 0x01;
    private static final int NO_MORE_DATA = 0x02;
    private static final int GET_DATA_ERROR = 0x03;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_THEME_SUCCESS:
                    if (isRefreshing) {
                        AppLog.i("cannotload", "a");
                        isRefreshing = false;
                        isLoadingMore = false;
                        mThemeAdapter.notifyDataSetChanged();
                        mPtrFrame.refreshComplete();
//                        mPtrFrame.loadMoreComplete(false);
                        break;
                    }
                    if (isLoadingMore) {
                        AppLog.i("cannotload", "b");
                        isLoadingMore = false;
                        isRefreshing = false;
                        mThemeAdapter.notifyDataSetChanged();
                        ((LinearLayoutManager)mRvTheme.getLayoutManager()).scrollToPositionWithOffset(mThemeList.size() - 1, 0);
                        mPtrFrame.loadMoreComplete(true);
                    }
                    // 如果是第一次加载，则设置适配器
                    setAdapter(mThemeList);
                    break;
                case NO_MORE_DATA:
                    isRefreshing = false;
                    isLoadingMore = false;
                    Toast.makeText(ThemeActivity.this, "没有更多专题咯~", Toast.LENGTH_SHORT).show();
                    mPtrFrame.setLoadMoreEnable(false);
                    break;
                case GET_DATA_ERROR:
                    isRefreshing = false;
                    isLoadingMore = false;
                    Toast.makeText(ThemeActivity.this, "加载数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    mPtrFrame.refreshComplete();
                    break;
            }
        }
    };

    /**
     * 设置适配器
     * @param mThemeList
     */
    private void setAdapter(List<RecommendRowsBean> mThemeList) {
        AppLog.i("hehe", "set adapter");
        mThemeAdapter = new ThemeAdapter(this, mThemeList);
        mAdapter = new RecyclerAdapterWithHF(mThemeAdapter);
        AppLog.i("hehe", "the adatper is " + mThemeAdapter.toString());
        mRvTheme.setAdapter(mAdapter);
        // 加载数据以后显示加载更多
        mPtrFrame.setLoadMoreEnable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        // 使用ButterKnife
        ButterKnife.bind(this);
        // 初始化试图
        initView();

    }

    /**
     * 初始化试图
     */
    private void initView() {
        // 初始化header
        initPtrLoadMore();
        // 初始化RecyclerView
        initRecyclerView();
        // 初始化数据
        initLoader();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        // 避免出现RecyclerView has no LayoutManager的错误
        mRvTheme.setHasFixedSize(true);
        // 计算RecyclerView的大小，可以显示器内容
        mRvTheme.setLayoutManager(new LinearLayoutManager(this));
        // 点击事件封装在adapter里面
    }

    /**
     * 初始化header
     */
    private void initPtrLoadMore() {
        // 实例化PTRHeader
        PtrHeader ptrHeader = new PtrHeader(this);
        // 给PTR控件添加头视图
        mPtrFrame.setHeaderView(ptrHeader);
        
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setLoadingMinTime(1000);
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        // 下拉刷新
        mPtrFrame.setPtrHandler(new com.chanven.lib.cptr.PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(com.chanven.lib.cptr.PtrFrameLayout frame) {
                isRefreshing = true;
//                mPtrFrame.setLoadMoreEnable(true);
                mPageNum = 1;
                mContentLoader.recommentList(mPageSize, mPageNum);
            }
        });

        // 上拉加载更多
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                isLoadingMore = true;
                mPageNum++;
                mContentLoader.recommentList(mPageSize, mPageNum);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initLoader() {
        mContentLoader = new ContentLoader(this);
        mContentLoader.setCallBack(new MyCallBack());
        mContentLoader.recommentList(mPageSize, mPageNum);
    }

    private class MyCallBack extends ICallBack {

        /**
         * 专题列表，因旧版本该模块在首页推荐，因此名字为onRecommend
         * @param recommendDataResp
         */
        @Override
        public void onRecommend(RecommendDataResp recommendDataResp) {
            super.onRecommend(recommendDataResp);

            RecommendResultBean result = recommendDataResp == null ? null :
                    recommendDataResp.getResult();

            AppLog.i("hehe", "recommendresult bean is " + recommendDataResp);

            if (result == null) {
                AppLog.i("cannotload", "result is null");
                mHandler.sendEmptyMessage(GET_DATA_ERROR);
                return;
            }

            List<RecommendRowsBean> themeList = result.getRows();


            if (recommendDataResp.getReturnCode() == 0) {
                if (isLoadingMore) {
                    if (themeList == null || themeList.size() == 0) {
                        AppLog.i("cannotload", "no more data");
                        mHandler.sendEmptyMessage(NO_MORE_DATA);
                        return;
                    } else {
                        mThemeList.addAll(themeList);
                    }
                } else {
                    mThemeList = themeList;
                }
                AppLog.i("hehe", "link success");
                AppLog.i("cannotload", "get_theme_success");
                mHandler.sendEmptyMessage(GET_THEME_SUCCESS);
            }
        }
    }
}
