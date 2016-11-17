package com.lalocal.lalocal.activity.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.SplashActivity;
import com.lalocal.lalocal.live.entertainment.model.LiveHomeListResp;
import com.lalocal.lalocal.live.im.ui.periscope.PeriscopeLayout;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.view.RecommendLayout;
import com.lalocal.lalocal.view.adapter.HomeLiveAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {

    @BindView(R.id.xrv_live)
    XRecyclerView mXrvLive;
    @BindView(R.id.btn_takelive)
    FloatingActionButton mBtnTakeLive;
    @BindView(R.id.recommend_page)
    RecommendLayout mRecommendPage;

    // 动画控件
    private static PeriscopeLayout mPeriscope;
    // 适配器
    private HomeLiveAdapter mAdapter;

    // 任务
    private static TimerTask mTask = null;
    // 定时器
    private static Timer mTimer = null;

    // 声明内容加载器
    private ContentLoader mContentLoader;
    // 我的关注
    private LiveRowsBean mMyAttention;
    // 正在直播列表
    private List<LiveRowsBean> mLivingList = new ArrayList<>();
    // 直播回放列表
    private List<LiveRowsBean> mPlaybackList = new ArrayList<>();

    // 判断是否在刷新
    private boolean isRefresh = false;
    // 判断是否加载更多
    private boolean isLoadingMore = false;

    // 声明Handler
    private static final int SHOW_PERISCOPE_LAYOUT = 0x01;
    private static final int HIDE_PERISCOPE_LAYOUT = 0x02;
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PERISCOPE_LAYOUT:
                    mPeriscope.addHeart();
                    break;
                case HIDE_PERISCOPE_LAYOUT:

                    break;
            }
        }
    };

    // 权限控制
    private final int LIVE_RECOMMEND_PEMISSION_CODE = 100;
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public LiveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= 22) {
            // 请求用户权限
            reminderUserPermission();
        } else {
            // 初始化
            init();
        }

        return view;
    }

    /**
     * 初始化
     */
    private void init() {
        // 初始化ContentLoader
        initLoader();
        // 初始化推荐页
        initRecommendPage();
        // 初始化列表
        initXRecyclerView();
    }

    /**
     * 初始化ContentLoader
     */
    private void initLoader() {
        // 实例化ContentLoader
        mContentLoader = new ContentLoader(getActivity());
        // 设置回调接口
        mContentLoader.setCallBack(new MyCallBack());

    }

    /**
     * 初始化推荐页
     */
    private void initRecommendPage() {
        AppLog.i("misss", "initRecommendPage");
        AppLog.i("hahae", "initRecommendPage");
        if (mPeriscope == null) {
            mPeriscope = (PeriscopeLayout) mRecommendPage.getView(R.id.periscope);
            AppLog.i("hahae", "initmPeriscope");
        }
        // 开始动画
        startPeriscope();
        AppLog.i("hahae", "startPeriscope");
    }

    /**
     * 初始化列表
     */
    private void initXRecyclerView() {
        mXrvLive.setLayoutManager(new LinearLayoutManager(getActivity()));
        mXrvLive.setHasFixedSize(true);
        mXrvLive.setPullRefreshEnabled(true);
        mXrvLive.setLoadingMoreEnabled(true);
        mXrvLive.setRefreshing(true);
        mXrvLive.setDefaultHeaderText("轻轻下拉刷新，用力下拉进入推荐");

        mBtnTakeLive.attachToRecyclerView(mXrvLive);

        mXrvLive.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "刷新完成", Toast.LENGTH_SHORT).show();
                        mXrvLive.refreshComplete();
                    }
                }, 500);

                // TODO: 获取接口数据
                mContentLoader.getHomeAttention();
                mContentLoader.getLivelist("", "");
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                        mXrvLive.loadMoreComplete();
                    }
                }, 500);
            }
        });

        final float[] downY = {0};
        mXrvLive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY[0] = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float curY = event.getY();
                        float delta = curY - downY[0];
                        int screen = DensityUtil.getWindowHeight(getActivity());

                        if (delta > screen - 400) {
                            mXrvLive.refreshComplete();
                            mRecommendPage.show();
                        }
                        break;
                }
                return false;
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void reminderUserPermission() {
        AppLog.i("misss", "reminderUserPermission");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            AppLog.i("misss", "READ_EXTERNAL_STORAGE");
            requestPermissions(LIVE_PERMISSIONS, LIVE_RECOMMEND_PEMISSION_CODE);
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            AppLog.i("misss", "WRITE_EXTERNAL_STORAGE");
            requestPermissions(LIVE_PERMISSIONS, LIVE_RECOMMEND_PEMISSION_CODE);
        } else {
            AppLog.i("misss", "reminderUserPermission else");
            // 初始化
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AppLog.i("misss", "onRequestPermissionsResult");
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        AppLog.i("misss", "doNext");
        if (requestCode == LIVE_RECOMMEND_PEMISSION_CODE) {
            AppLog.i("misss", "requestCode == LIVE_RECOMMEND_PEMISSION_CODE");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AppLog.i("misss", "grantResults[0] == PackageManager.PERMISSION_GRANTED");
                // 初始化
                init();
            } else {
                AppLog.i("misss", "requestCode != LIVE_RECOMMEND_PEMISSION_CODE");
                Toast.makeText(getActivity(), getString(R.string.live_camera_jurisdiction), Toast.LENGTH_SHORT).show();
            }

        }
    }


    /**
     * 开始动画效果
     */
    public static void startPeriscope() {
        AppLog.i("misss", "startPeriscope");
        AppLog.i("hahae", "startPeriscope");

        // 初始化定时器任务
        if (mTask == null) {
            AppLog.i("hahae", "initmTask");
            mTask = new TimerTask() {
                @Override
                public void run() {
                    if (mPeriscope != null) {
                        mHandler.sendEmptyMessage(SHOW_PERISCOPE_LAYOUT);
                    }
                }
            };
        }

        // 初始化定时器
        if (mTimer == null) {
            AppLog.i("hahae", "initmTimer");
            mTimer = new Timer();
            AppLog.i("hahae", "schedule");
        }
        mTimer.schedule(mTask, 0, 400);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 隐藏推荐页
            mRecommendPage.hide();
        }
    }

    /**
     * 停止动画效果
     */
    public static void stopPeriscope() {
        AppLog.i("misss", "stopPeriscope");
        AppLog.i("hahae", "stopPeriscope");
        // 取消任务
        if (mTask != null) {
            AppLog.i("hahae", "cancelTask");
            mTask.cancel();
            mTask = null;
        }

        // 取消定时器
        if (mTimer != null) {
            AppLog.i("hahae", "cancelTimer");
            mTimer.cancel();
            mTimer = null;
        }

    }

    private class MyCallBack extends ICallBack {

        @Override
        public void onGetHomeAttention(LiveRowsBean bean) {
            super.onGetHomeAttention(bean);

            mMyAttention = bean;

            if (mAdapter == null) {
                mAdapter = new HomeLiveAdapter(getActivity(), mMyAttention, mLivingList, mPlaybackList);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onLiveHomeList(LiveHomeListResp liveListDataResp, String attenFlag) {
            super.onLiveHomeList(liveListDataResp, attenFlag);

            // 如果接口状态正常
            if (liveListDataResp.getReturnCode() == 0) {
                // 先清空列表
                mLivingList.clear();
                // 获取数据
                mLivingList.addAll(liveListDataResp.getResult());
            }
        }

        @Override
        public void onPlayBackList(String json, String attentionFlag) {
            super.onPlayBackList(json, attentionFlag);
            // 获取接口数据
            LiveHomeListResp liveHomeListResp = new Gson().fromJson(json, LiveHomeListResp.class);

            // 如果接口状态正常
            if (liveHomeListResp.getReturnCode() == 0) {
                // 先清空列表
                mPlaybackList.clear();
                // 获取数据
                mPlaybackList.addAll(liveHomeListResp.getResult());
            }

        }
    }
}
