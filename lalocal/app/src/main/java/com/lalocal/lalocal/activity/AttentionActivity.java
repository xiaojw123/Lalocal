package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.live.entertainment.model.LiveHomeListResp;
import com.lalocal.lalocal.live.entertainment.model.LivePlayBackListResp;
import com.lalocal.lalocal.live.entertainment.ui.CustomLinearLayoutManager;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.CustomXRecyclerView;
import com.lalocal.lalocal.view.adapter.LiveMainAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Boolean.parseBoolean;

public class AttentionActivity extends BaseActivity {

    @BindView(R.id.xrecyclerview)
    CustomXRecyclerView xRecyclerView;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_search)
    ImageView imgSearch;

    // 列表第一个可视位置
    private int firstVisibleItemPosition;
    // 是否刷新
    boolean isRefresh = false;
    // 是否第一次获取数据
    boolean isFirstGetData = true;
    // recyclerView滚动的距离
    int startScollYDistance = 0;
    // 是否点击
    boolean isClick = true;
    // 内容加载器
    private ContentLoader contentService;
    // 声明适配器
    private LiveMainAdapter attenAdapter;
    // 数据列表
    private List<LiveRowsBean> allRows = new ArrayList<LiveRowsBean>();
    // 所有关注列表
    private List<LiveRowsBean> allAttenRows = new ArrayList<LiveRowsBean>();
    // 是否是最后一页
    private boolean lastPage = false;
    // 当前页码
    private int pageNumber = 1;
    // 直播间id
    private int roomId = 0;
    // 创建直播间标记
    public static final String CREATE_ROOMID = "createRoomId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);

        ButterKnife.bind(this);

        // 初始化ContentLoader
        initLoader();
        // 初始化RecyclerView
        initRecyclerView();
    }

    /**
     * 初始化ContentLoader
     */
    private void initLoader() {
        contentService = new ContentLoader(AttentionActivity.this);
        contentService.setCallBack(new MyCallBack());
//        contentService.getLivelist("", "");
    }

    /**
     * 初始化RecyclerView
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
        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                AppLog.print("onScrollStateChanged  newState_____" + newState);
                int scollYDistance = getScollYDistance();
                if (scollYDistance > 0 && isFirstGetData) {
                    startScollYDistance = scollYDistance;
                    isFirstGetData = false;
                }
                if (!isClick) {
                    isClick = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int scollYDistance = getScollYDistance();
                int scollDy = 50 - DensityUtil.px2dip(AttentionActivity.this, (scollYDistance - startScollYDistance));
                AppLog.print("onScrolled firstVisibleItemPosition____" + firstVisibleItemPosition + "____scollDy____" + scollDy);
            }
        });
    }

    private int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) xRecyclerView.getLayoutManager();
        firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(firstVisibleItemPosition);
        if (firstVisiableChildView != null) {
            int itemHeight = firstVisiableChildView.getHeight();
            return (firstVisibleItemPosition) * itemHeight - firstVisiableChildView.getTop();
        } else {
            return 0;
        }
    }

    public class XRecyclerviewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            isRefresh = true;
            contentService.getLivelist("", "true");
        }

        @Override
        public void onLoadMore() {
            AppLog.i("xrv", "onLoadMore()-" + allRows.size());
            isRefresh = false;
            if (lastPage) {
                xRecyclerView.setNoMore(true);
            } else {
                AppLog.print("关注laodmore");
                contentService.getPlayBackLiveList("", pageNumber, "true");
            }

        }

    }


    @OnClick({R.id.img_search, R.id.img_back})
    void clickBtn(View v) {
        switch (v.getId()) {
            case R.id.img_search:
                startActivity(new Intent(AttentionActivity.this, GlobalSearchActivity.class));
                break;
            case R.id.img_back:
                // 销毁activity
                AttentionActivity.this.finish();
                break;
        }
    }

    public class MyCallBack extends ICallBack {
        String reminfBack = "0";

        @Override
        public void onError(VolleyError volleyError) {
//            resetAdapterData();
        }

        @Override
        public void onResponseFailed(int code, String message) {
//            resetAdapterData();
        }

        @Override
        public void onLiveHomeList(LiveHomeListResp liveListDataResp, String attentionFlag) {
            try {
                if (liveListDataResp.getReturnCode() == 0) {
                    List<LiveRowsBean> rows = liveListDataResp.getResult();
                    if (rows == null) {
                        return;
                    }
                    if (isRefresh) {
                        allRows.clear();
                        allAttenRows.clear();
                    }
                    if (allRows.size() == 0) {
                        allRows.addAll(0, rows);
                    } else {
                        allRows.addAll(allRows.size(), rows);
                    }
                    if (allAttenRows.size() == 0) {
                        allAttenRows.addAll(0, rows);
                    } else {
                        allAttenRows.addAll(allAttenRows.size(), rows);
                    }
                    Collections.sort(allRows);//排序
                    Collections.sort(allAttenRows);//排序
                    boolean isAttentionFlag = parseBoolean(attentionFlag);
                    if (isAttentionFlag) {
                        if (attenAdapter == null) {
                            attenAdapter = new LiveMainAdapter(AttentionActivity.this, allAttenRows);
                            attenAdapter.setOnLiveItemClickListener(liveItemClickListener);
                        } else {
                            attenAdapter.refresh(allAttenRows);
                        }
                        attenAdapter.setHightPostion(true, allAttenRows.size());
                        xRecyclerView.setAdapter(attenAdapter);
                    }
                    contentService.getPlayBackLiveList("", 1, attentionFlag);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPlayBackList(String json, String attentionFlag) {
            LivePlayBackListResp livePlayBackListResp = new Gson().fromJson(json, LivePlayBackListResp.class);
            if (livePlayBackListResp.getReturnCode() == 0) {
                LivePlayBackListResp.ResultBean result = livePlayBackListResp.getResult();
                pageNumber = result.getPageNumber() + 1;
                lastPage = result.isLastPage();
                List<LiveRowsBean> rows = result.getRows();
                AppLog.print("onPlayBackList size____" + rows.size());
                if (rows == null) {
                    return;
                }
                allRows.addAll(allRows.size(), rows);
                allAttenRows.addAll(allAttenRows.size(), rows);
                boolean isAttention = Boolean.parseBoolean(attentionFlag);
                if (isAttention) {
                    attenAdapter.refresh(allAttenRows);
                }
                if (isRefresh) {
                    xRecyclerView.refreshComplete();
                } else if (!lastPage) {
                    xRecyclerView.setNoMore(true);
                } else {
                    xRecyclerView.loadMoreComplete();
                }
            }

        }

        @Override
        public void onLoginSucess(User user) {
            super.onLoginSucess(user);
        }
    }

    private LiveMainAdapter.OnLiveItemClickListener liveItemClickListener = new LiveMainAdapter.OnLiveItemClickListener() {
        @Override
        public void goLiveRoom(LiveRowsBean liveRowsBean) {
            if (liveRowsBean.getEndAt() != null && liveRowsBean.getStartAt() != null) {
                Intent intent = new Intent(AttentionActivity.this, PlayBackActivity.class);
                intent.putExtra("id", String.valueOf(liveRowsBean.getId()));
                startActivity(intent);
            } else {
                roomId = liveRowsBean.getRoomId();
                String createRoom = SPCUtils.getString(AttentionActivity.this, CREATE_ROOMID);
                String s = String.valueOf(roomId);
                if (createRoom != null && createRoom.equals(s)) {
                    CommonUtil.REMIND_BACK = 1;
//                    prepareLive();
                    return;
                }
                Intent intent = new Intent(AttentionActivity.this, AudienceActivity.class);
                intent.putExtra("id", String.valueOf(liveRowsBean.getId()));
                startActivity(intent);
            }
        }
    };
}