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

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.SplashActivity;
import com.lalocal.lalocal.live.im.ui.periscope.PeriscopeLayout;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.view.RecommendLayout;
import com.lalocal.lalocal.view.adapter.HomeLiveAdapter;
import com.melnykov.fab.FloatingActionButton;

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

    private static PeriscopeLayout mPeriscope;

    private HomeLiveAdapter mAdapter;

    // 任务
    private static TimerTask mTask = null;
    // 定时器
    private static Timer mTimer = null;

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
            AppLog.i("TAG", "点击直播按钮，版本小于，权限判断");
            // 初始化推荐页
            initRecommendPage();
            // 初始化列表
            initXRecyclerView();
        }

        return view;
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
            // 初始化推荐页
            initRecommendPage();
            // 初始化列表
            initXRecyclerView();
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
                // 初始化推荐页
                initRecommendPage();
                // 初始化列表
                initXRecyclerView();
            } else {
                AppLog.i("misss", "requestCode != LIVE_RECOMMEND_PEMISSION_CODE");
                Toast.makeText(getActivity(), getString(R.string.live_camera_jurisdiction), Toast.LENGTH_SHORT).show();
            }

        }
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

    /**
     * 初始化列表
     */
    private void initXRecyclerView() {
        mAdapter = new HomeLiveAdapter(getActivity());
        mXrvLive.setLayoutManager(new LinearLayoutManager(getActivity()));
        mXrvLive.setHasFixedSize(true);
        mXrvLive.setAdapter(mAdapter);
        mXrvLive.setPullRefreshEnabled(true);
        mXrvLive.setLoadingMoreEnabled(true);
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
                        AppLog.i("scro", "delta--" + delta);
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

}
