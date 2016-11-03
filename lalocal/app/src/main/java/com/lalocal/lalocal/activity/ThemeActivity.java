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
import com.jcodecraeer.xrecyclerview.XRecyclerView;
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

//    @BindView(R.id.ptr_frame_layout)
//    PtrClassicFrameLayout mPtrFrame;

    @BindView(R.id.rv_theme)
    XRecyclerView mXrvTheme;

//    private RecyclerAdapterWithHF mAdapter;
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
                        setAdapter(mThemeList);
                        mXrvTheme.refreshComplete();
                        break;
                    }
                    if (isLoadingMore) {
                        AppLog.i("cannotload", "b");
                        isLoadingMore = false;
                        isRefreshing = false;
                        mThemeAdapter.notifyDataSetChanged();
                        ((LinearLayoutManager)mXrvTheme.getLayoutManager()).scrollToPositionWithOffset(mThemeList.size() - 1, 0);
                        mXrvTheme.loadMoreComplete();
                    }
                    // 如果是第一次加载，则设置适配器
                    setAdapter(mThemeList);
                    break;
                case NO_MORE_DATA:
                    isRefreshing = false;
                    isLoadingMore = false;
                    Toast.makeText(ThemeActivity.this, "没有更多专题咯~", Toast.LENGTH_SHORT).show();
                    mXrvTheme.setNoMore(true);
                    break;
                case GET_DATA_ERROR:
                    isRefreshing = false;
                    isLoadingMore = false;
                    Toast.makeText(ThemeActivity.this, "加载数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    mXrvTheme.refreshComplete();
                    break;
            }
        }
    };

    /**
     * 设置适配器
     * @param mThemeList
     */
    private void setAdapter(List<RecommendRowsBean> mThemeList) {
        mThemeAdapter = new ThemeAdapter(this, mThemeList);
        mXrvTheme.setAdapter(mThemeAdapter);
        // 加载数据以后显示加载更多
        mXrvTheme.setLoadingMoreEnabled(true);
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
        // 初始化数据
        initLoader();
        // 初始化XRecyclerView
        initXRecyclerView();
    }

    /**
     * 初始化初始化XRecyclerView
     */
    private void initXRecyclerView() {
        // 初始化布局参数
        LinearLayoutManager layoutManager = new LinearLayoutManager(ThemeActivity.this);
        // 设置竖直排列
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局参数
        mXrvTheme.setLayoutManager(layoutManager);
        // 初始化加载监听事件
        XRecyclerViewLoadingListener listener = new XRecyclerViewLoadingListener();
        // 设置监听事件
        mXrvTheme.setLoadingListener(listener);
        // 激活下拉刷新
        mXrvTheme.setPullRefreshEnabled(true);
        // 设置刷新
        mXrvTheme.setRefreshing(true);
    }

    /**
     * XRecyclerView监听事件
     */
    private class XRecyclerViewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            if (mXrvTheme != null) {
                isRefreshing = true;
                mPageNum = 1;
                mContentLoader.recommentList(mPageSize, mPageNum);
            }
        }

        @Override
        public void onLoadMore() {
            isLoadingMore = true;
            mPageNum++;
            mContentLoader.recommentList(mPageSize, mPageNum);
        }
    }

    /**
     * 初始化数据
     */
    private void initLoader() {
        mContentLoader = new ContentLoader(this);
        mContentLoader.setCallBack(new MyCallBack());
//        mContentLoader.recommentList(mPageSize, mPageNum);
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
//                mHandler.sendEmptyMessage(GET_DATA_ERROR);

                isRefreshing = false;
                isLoadingMore = false;
                Toast.makeText(ThemeActivity.this, "加载数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                mXrvTheme.refreshComplete();
                return;
            }

            List<RecommendRowsBean> themeList = result.getRows();


            if (recommendDataResp.getReturnCode() == 0) {
                // 如果是加载更多
                if (isLoadingMore) {
                    if (themeList == null || themeList.size() == 0) {
//                        mHandler.sendEmptyMessage(NO_MORE_DATA);

                        isRefreshing = false;
                        isLoadingMore = false;
                        Toast.makeText(ThemeActivity.this, "没有更多专题咯~", Toast.LENGTH_SHORT).show();
                        mXrvTheme.setNoMore(true);
                        return;
                    } else {
                        mThemeList.addAll(themeList);
                    }
//                    mHandler.sendEmptyMessage(GET_THEME_SUCCESS);

                    isLoadingMore = false;
                    isRefreshing = false;
                    mThemeAdapter.notifyDataSetChanged();
                    ((LinearLayoutManager)mXrvTheme.getLayoutManager()).scrollToPositionWithOffset(mThemeList.size() - 1, 0);
                    mXrvTheme.loadMoreComplete();
                    return;
                }

                // 如果是刷新
                if(isRefreshing){
                    mThemeList = themeList;
//                    mHandler.sendEmptyMessage(GET_THEME_SUCCESS);

                    isRefreshing = false;
                    isLoadingMore = false;
                    setAdapter(mThemeList);
                    mXrvTheme.refreshComplete();
                }
            }
        }
    }
}
