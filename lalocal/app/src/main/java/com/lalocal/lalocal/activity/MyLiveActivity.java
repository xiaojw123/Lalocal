package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.PostShortVideoActivity;
import com.lalocal.lalocal.live.entertainment.model.PostShortVideoParameterBean;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.UserLiveItem;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.adapter.MyLiveAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MyLiveActivity extends BaseActivity implements XRecyclerView.LoadingListener {
    XRecyclerView mXRecyclerView;
    TextView hintTv;
    int pageNum, pageSize;
    boolean isRefresh, isLoadMore;
    CustomTitleView titleView;
    public static final int VIDEO_PREVIEW_REQUESTCODE = 501;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_PREVIEW_REQUESTCODE) {
            if (resultCode == KeyParams.UPLOAD_SHORT_VIEW_RESULTCODE) {
                if (adapter != null) {
                    adapter.updateDataBaseItmes(DataSupport.findAll(PostShortVideoParameterBean.class));
                }
            } else if (resultCode == KeyParams.UPLOAD_SHORT_VIEW_SUCCESS_RESULTCODE_) {
                isRefresh = true;
                mContentloader.getUserLive(UserHelper.getUserId(this), 1);
                if (adapter != null) {
                    adapter.updateDataBaseItmes(DataSupport.findAll(PostShortVideoParameterBean.class));
                }
            }
        }
        if (resultCode == KeyParams.UPLOAD_SHORT_VIEW_RESULTCODE) {
            if (adapter != null) {
                adapter.updateDataBaseItmes(DataSupport.findAll(PostShortVideoParameterBean.class));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_live);
        initView();
        setLoaderCallBack(new LiveCallBack());
        mXRecyclerView.setRefreshing(true);
    }


    private void initView() {
        titleView = (CustomTitleView) findViewById(R.id.my_live_ctv);
        hintTv = (TextView) findViewById(R.id.my_live_items_hint);
        mXRecyclerView = (XRecyclerView) findViewById(R.id.my_live_items_xrlv);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingListener(this);
        addHeaderView();
    }

    private void addHeaderView() {
        View inflate = View.inflate(this, R.layout.add_short_video_layout, null);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyLiveActivity.this, PostShortVideoActivity.class));
            }
        });
        mXRecyclerView.addHeaderView(inflate);
    }
<<<<<<< HEAD
    //获取我的直播列表
=======


>>>>>>> dev
    @Override
    public void onRefresh() {
        isRefresh = true;
        if (adapter != null) {
            adapter.updateDataBaseItmes(DataSupport.findAll(PostShortVideoParameterBean.class));
        }
        mContentloader.getUserLive(UserHelper.getUserId(this), 1);
    }
    //加载更多items
    @Override
    public void onLoadMore() {
        if (pageNum < pageSize) {
            isLoadMore = true;
            ++pageNum;
            mContentloader.getUserLive(UserHelper.getUserId(this), pageNum);
        } else {
            isLoadMore = false;
            mXRecyclerView.setNoMore(true);
        }


    }

    MyLiveAdapter adapter;

    class LiveCallBack extends ICallBack {
        List<LiveRowsBean> rowsList = new ArrayList<>();


        @Override
        public void onDeleteLiveHistory() {
            Toast.makeText(MyLiveActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onGetUserLive(UserLiveItem item) {
            if (item != null) {
                int toalRows = item.getTotalRows();
                if (toalRows > 0) {
                    pageNum = item.getPageNumber();
                    pageSize = item.getTotalPages();
                    if (isRefresh) {
                        isRefresh = false;
                        mXRecyclerView.refreshComplete();
                    }
                    if (isLoadMore) {
                        isLoadMore = false;
                        mXRecyclerView.setNoMore(true);
                    } else {
                        rowsList.clear();
                    }
                    rowsList.addAll(item.getRows());
                    if (adapter == null) {
                        adapter = new MyLiveAdapter(mXRecyclerView, mContentloader, rowsList, DataSupport.findAll(PostShortVideoParameterBean.class));
                        mXRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateItems(rowsList);
                    }
                } else {
                    //    mXRecyclerView.setVisibility(View.GONE);
                    //    hintTv.setVisibility(View.VISIBLE);
                }

            } else {
                //  mXRecyclerView.setVisibility(View.GONE);
                //   hintTv.setVisibility(View.VISIBLE);
            }
        }

        public void resetLoad() {
            if (isRefresh) {
                isRefresh = false;
                mXRecyclerView.refreshComplete();
            }
            if (isLoadMore) {
                isLoadMore = false;
                mXRecyclerView.setNoMore(true);
            }
        }


        @Override
        public void onError(VolleyError volleyError) {
            resetLoad();
        }

        @Override
        public void onResponseFailed(int code, String message) {
            resetLoad();
        }

    }
}
