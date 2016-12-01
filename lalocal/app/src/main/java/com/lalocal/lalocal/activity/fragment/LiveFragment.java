package com.lalocal.lalocal.activity.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.SplashActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.live.entertainment.model.LiveHomeListResp;
import com.lalocal.lalocal.live.entertainment.model.LivePlayBackListResp;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.im.ui.periscope.PeriscopeLayout;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendationsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.RecommendLayout;
import com.lalocal.lalocal.view.adapter.HomeLiveAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    // 适配器
    private HomeLiveAdapter mAdapter;

    // 声明内容加载器
    private ContentLoader mContentLoader;
    // 我的关注
    private LiveRowsBean mMyAttention;
    // 正在直播列表
    private List<LiveRowsBean> mLivingList = new ArrayList<>();
    // 直播回放列表
    private List<LiveRowsBean> mPlaybackList = new ArrayList<>();

    // 模块数
    private static final int PART_SIZE = 3;

    // -刷新类型
    private static final int INITIAL = 0X00;
    private static final int REFRESH_MY_ATTENTION = 0x01;
    private static final int REFRESH_LIVING_LIST = 0x02;
    private static final int REFRESH_PLAYBACK_LIST = 0x03;

    // 推荐页直播录播标记
    public static final int LIVING = 0;
    public static final int PLAYBACK = 1;

    // 创建直播间id的key
    public static final String CREATE_ROOMID = "createRoomId";

    // 第一次进入该页面
    private boolean isFirst = true;

    // 刷新计数
    private int mCountRefresh = 0;

    // 判断历史回放是否在刷新
    private boolean isRefresh = false;
    // 判断是否加载更多
    private boolean isLoadingMore = false;
    // 当前分页页码
    private int mCurPageNum = 1;

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

        // 使用ButterKnife框架
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
        // 初始化推荐页
        initRecommendPage();
        // 初始化ContentLoader
        initLoader();
        // 初始化列表
        initXRecyclerView();
    }

    /**
     * 初始化推荐页
     */
    private void initRecommendPage() {
        // 设置不可见
        mRecommendPage.setVisibility(View.GONE);

    }

    /**
     * 初始化ContentLoader
     */
    private void initLoader() {
        // 实例化ContentLoader
        mContentLoader = new ContentLoader(getActivity());
        // 设置回调接口
        mContentLoader.setCallBack(new MyCallBack());

        mContentLoader.getHomeAttention();
        mContentLoader.getLivelist("", "");
        mContentLoader.getPlayBackLiveList("", 1, "");
        mContentLoader.getDailyRecommendations(Constants.OPEN_APP_TO_SCAN);
    }

    /**
     * 初始化列表
     */
    private void initXRecyclerView() {
        mXrvLive.setLayoutManager(new LinearLayoutManager(getActivity()));
        mXrvLive.setHasFixedSize(true);
        mXrvLive.setPullRefreshEnabled(true);
        mXrvLive.setLoadingMoreEnabled(true);
        mXrvLive.setDefaultHeaderText("轻轻下拉刷新，用力下拉进入推荐");

        mBtnTakeLive.attachToRecyclerView(mXrvLive);

        mXrvLive.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                mXrvLive.setNoMore(false);

                if (isRefresh == false) {
                    isRefresh = true;
                    mContentLoader.getHomeAttention();
                    mContentLoader.getLivelist("", "");
                    mContentLoader.getPlayBackLiveList("", 1, "");
                }
            }

            @Override
            public void onLoadMore() {

                if (isLoadingMore == false) {
                    isLoadingMore = true;
                    // 页码+1
                    mCurPageNum++;
                    // 获取相应页码的回放列表
                    mContentLoader.getPlayBackLiveList("", mCurPageNum, "");
                }
            }
        });

        mXrvLive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float[] downY = {0};
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 获取刷新头可见范围的高度
                        int visibleHeight = mXrvLive.getHeaderVisibleHeight();
                        // 如果可见高度大于133dp
                        if (visibleHeight >= DensityUtil.dip2px(getActivity(), 133)) {
                            // 客户端主动下拉获取网络数据
                            mContentLoader.getDailyRecommendations(Constants.PULL_TO_SCAN);
                        }
                        break;
                }
                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void reminderUserPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LIVE_PERMISSIONS, LIVE_RECOMMEND_PEMISSION_CODE);
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LIVE_PERMISSIONS, LIVE_RECOMMEND_PEMISSION_CODE);
        } else {
            // 初始化
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == LIVE_RECOMMEND_PEMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 初始化
                init();
            } else {
                Toast.makeText(getActivity(), getString(R.string.live_camera_jurisdiction), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && isFirst) {
            // 隐藏推荐页
            mRecommendPage.hide();
            mRecommendPage.setFocusable(false);
        } else {
            mXrvLive.setRefreshing(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mXrvLive.setRefreshing(true);
    }

    @OnClick({R.id.btn_takelive})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_takelive:
                MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BUTTON);
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                prepareLive();
//                } else {
//                    reminderUserPermission();
//                }
                break;
        }
    }

    private void prepareLive() {
        boolean isLogin = UserHelper.isLogined(getActivity());
        if (isLogin) {
            startActivity(new Intent(getActivity(), LiveActivity.class));
        } else {
            showLoginDialog();
        }
    }

    private void showLoginDialog() {
        CustomChatDialog customDialog = new CustomChatDialog(getActivity());
        customDialog.setContent(getString(R.string.live_login_hint));
        customDialog.setCancelable(false);
        customDialog.setCancelable(false);
        customDialog.setCancelBtn(getString(R.string.live_canncel), null);
        customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                LLoginActivity.start(getActivity());
            }
        });
        customDialog.show();
    }

    private class MyCallBack extends ICallBack {

        @Override
        public void onGetHomeAttention(LiveRowsBean bean) {
            super.onGetHomeAttention(bean);

            // 处理刷新状态
            dealRefresh();

            // 恢复页码标记
            mCurPageNum = 1;
            // 获取数据
            mMyAttention = bean;
            if (mMyAttention == null) {
            }
            // 配置适配器
            setAdapter(REFRESH_MY_ATTENTION);
        }

        @Override
        public void onLiveHomeList(LiveHomeListResp liveListDataResp, String attenFlag) {
            super.onLiveHomeList(liveListDataResp, attenFlag);

            // 处理刷新状态
            dealRefresh();

            // 恢复页码标记
            mCurPageNum = 1;
            // 如果接口状态正常
            if (liveListDataResp.getReturnCode() == 0) {
                // 先清空列表
                mLivingList.clear();
                // 获取数据
                mLivingList.addAll(liveListDataResp.getResult());
                // 配置适配器
                setAdapter(REFRESH_LIVING_LIST);
            }
        }

        @Override
        public void onPlayBackList(String json, String attentionFlag) {
            super.onPlayBackList(json, attentionFlag);

            // 获取接口数据
            LivePlayBackListResp liveHomeListResp = new Gson().fromJson(json, LivePlayBackListResp.class);

            // 如果接口状态正常
            if (liveHomeListResp.getReturnCode() == 0) {

                AppLog.i("rfe", "getReturnCode");
                LivePlayBackListResp.ResultBean bean = liveHomeListResp.getResult();

                if (bean != null) {
                    AppLog.i("rfe", "bean not null");
                    // 如果是刷新
                    if (isRefresh) {
                        AppLog.i("rfe", "isRefresh 1");
                        // 处理刷新状态
                        dealRefresh();
                        // 恢复页码标记
                        mCurPageNum = 1;
                        // 先清空列表
                        mPlaybackList.clear();
                        // 获取数据
                        mPlaybackList.addAll(bean.getRows());
                        // 配置适配器
                        setAdapter(REFRESH_PLAYBACK_LIST);
                    } else if (isLoadingMore) {
                        AppLog.i("rfe", "isLoadingMore 1");
                        // 如果返回数据为空，则禁止加载更多
                        List<LiveRowsBean> rows = bean.getRows();
                        if (rows == null || rows.size() == 0) {
                            AppLog.i("rfe", "size 0");
                            mXrvLive.setNoMore(true);

                            isLoadingMore = false;
                            return;
                        }
                        // 加载完毕
                        mXrvLive.loadMoreComplete();
                        // 获取数据
                        mPlaybackList.addAll(bean.getRows());
                        // 配置适配器
                        setAdapter(REFRESH_PLAYBACK_LIST);
                    }
                } else {
                    AppLog.i("rfe", "bean null");
                    // 如果是刷新
                    if (isRefresh) {
                        AppLog.i("rfe", "isRefresh 2");
                        // 处理刷新状态
                        dealRefresh();
                        // 恢复页码标记
                        mCurPageNum = 1;
                        // 清空列表
                        mPlaybackList.clear();
                        // 配置适配器
                        setAdapter(REFRESH_PLAYBACK_LIST);

                    } else if (isLoadingMore) { // 如果是加载更多，则表示没有更多数据
                        AppLog.i("rfe", "isLoadingMore 2");
                        Toast.makeText(getActivity(), "没有更多数据咯~", Toast.LENGTH_SHORT).show();
                        mXrvLive.setNoMore(true);
                    }
                }
                AppLog.i("rfe", "finish");
                isLoadingMore = false;
            }

        }

        @Override
        public void onGetDailyRecommend(RecommendationsBean bean) {
            super.onGetDailyRecommend(bean);

            // 如果数据为空
            if (bean == null) {
                // 如果不是第一次调用，则显示“暂无推荐”
                if (isFirst == false) {
                    Toast.makeText(getActivity(), "暂无推荐", Toast.LENGTH_SHORT).show();
                }
                // 改变状态标记
                isFirst = false;
                // 隐藏推荐页
                mRecommendPage.hide();
                // 推荐页不可见
                mRecommendPage.setVisibility(View.GONE);
                mRecommendPage.setFocusable(false);
                return;
            }

            // 修改第一次进入标记
            isFirst = false;

            // 显示推荐页
            mRecommendPage.setVisibility(View.VISIBLE);
            mRecommendPage.show();
            mRecommendPage.setFocusable(true);

            final int type = bean.getType();
            final int targetId = bean.getTargetId();
            String title = bean.getTitle();
            String address = bean.getAddress();
            String photo = bean.getPhoto();
            LiveUserBean user = bean.getUser();
            String nickname = user.getNickName();
            String avatar = user.getAvatar();

            if (TextUtils.isEmpty(title)) {
                title = "木有标题哦~";
            }
            mRecommendPage.setText(R.id.tv_recommendations_title, title);

            if (TextUtils.isEmpty(address)) {
                address = "地点找不到啦";
            }
            mRecommendPage.setText(R.id.tv_recommendations_address, address);

            if (!TextUtils.isEmpty(photo)) {
                ImageView imgPhoto = (ImageView) mRecommendPage.getView(R.id.img_recommendations);
                Glide.with(getActivity()).load(photo).into(imgPhoto);
            }

            if (!TextUtils.isEmpty(avatar)) {
                ImageView imgAvatar = (ImageView) mRecommendPage.getView(R.id.img_recommendations_avatar);
                Glide.with(getActivity()).load(avatar).into(imgAvatar);
            }

            if (TextUtils.isEmpty(nickname)) {
                nickname = "一位不愿意透露姓名的网友";
            }
            mRecommendPage.setText(R.id.tv_recommendations_nickname, nickname);

            if (type == LIVING) {
                mRecommendPage.setText(R.id.tv_recommendations_type, "正在直播");
            } else if (type == PLAYBACK) {
                mRecommendPage.setText(R.id.tv_recommendations_type, "精彩回放");
            } else {
                mRecommendPage.setText(R.id.tv_recommendations_type, "精彩推荐");
            }

            // 设置点击事件
            mRecommendPage.setOnClick(R.id.view_click, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppLog.i("rec", "type is " + type + "; id is " + targetId);
                    // 对回放还是直播进行判断
                    if (type == LIVING) {
                        Intent intent1 = new Intent(getActivity(), AudienceActivity.class);
                        intent1.putExtra("id", String.valueOf(targetId));
                        getActivity().startActivity(intent1);
                    } else if (type == PLAYBACK) {
                        Intent intent = new Intent(getActivity(), PlayBackActivity.class);
                        intent.putExtra("id", String.valueOf(targetId));
                        getActivity().startActivity(intent);
                    }
                }
            });
            mRecommendPage.setPlayTypeId(type, targetId);
        }
    }

    /**
     * 处理刷新状态
     */
    private void dealRefresh() {
        if (isRefresh) {
            mCountRefresh++;
            if (mCountRefresh == PART_SIZE) {
                mCountRefresh = 0;
                isRefresh = false;
                isLoadingMore = false;
                mXrvLive.refreshComplete();
                mXrvLive.loadMoreComplete();
            }
        }
    }


    /**
     * 配置适配器
     *
     * @param refreshType
     */
    public void setAdapter(int refreshType) {

        if (mAdapter == null) {
            // 初始化适配器，此时mLivingList和mPlaybackList数据为空
            mAdapter = new HomeLiveAdapter(getActivity(), mMyAttention, mLivingList, mPlaybackList);
            // 刷新数据
            mXrvLive.setAdapter(mAdapter);
        } else {
            switch (refreshType) {
                case REFRESH_MY_ATTENTION:
                    // 刷新我的关注
                    mAdapter.refreshMyAttention(mMyAttention);
                    break;
                case REFRESH_LIVING_LIST:
                    // 刷新正在直播列表
                    mAdapter.refreshLivingList(mLivingList);
                    break;
                case REFRESH_PLAYBACK_LIST:
                    // 刷新直播回放列表
                    mAdapter.refreshPlaybackList(mPlaybackList);
                    break;
                case INITIAL:
                    // 重新setAdapter
                    mAdapter = null;
                    setAdapter(INITIAL);
                    break;
            }
        }
    }
}
