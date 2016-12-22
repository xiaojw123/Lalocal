package com.lalocal.lalocal.activity.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.CategoryBean;
import com.lalocal.lalocal.model.ChannelIndexTotalResult;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.RecommendationsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.RecommendLayout;
import com.lalocal.lalocal.view.adapter.CategoryAdapter;
import com.lalocal.lalocal.view.adapter.HomeLiveAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends BaseFragment {

    @BindView(R.id.xrv_live)
    XRecyclerView mXrvLive;
    @BindView(R.id.btn_takelive)
    FloatingActionButton mBtnTakeLive;
    @BindView(R.id.recommend_page)
    RecommendLayout mRecommendPage;
    @BindView(R.id.rv_top_category)
    RecyclerView mRvTopCategory;

    // 适配器
    private HomeLiveAdapter mAdapter;
    // 顶部目录适配器
    private CategoryAdapter mCateAdapter;
    // 声明内容加载器
    private ContentLoader mContentLoader;
    // 我的关注
    private LiveUserBean mAttention;
    // 广告列表
    private List<RecommendAdResultBean> mAdList = new ArrayList<>();
    // 分类列表
    private List<CategoryBean> mCategoryList = new ArrayList<>();
    // 正在直播列表
    private List<LiveRowsBean> mLivingList = new ArrayList<>();
    // 直播回放列表
    private List<LiveRowsBean> mPlaybackList = new ArrayList<>();

    // -刷新类型
    private static final int INITIAL = 0X00;
    private static final int REFRESH_MY_ATTENTION = 0x01;
    private static final int REFRESH_LIVING_LIST = 0x02;
    private static final int REFRESH_PLAYBACK_LIST = 0x03;
    private static final int REFRESH_LIVE = 0x04;
    private static final int REFRESH_ALL = 0x05;

    // 推荐页直播录播标记
    public static final int LIVING = 0;
    public static final int PLAYBACK = 1;

    // 第一次进入该页面
    private boolean isFirst = true;

    // 判断历史回放是否在刷新
    private boolean isRefresh = false;
    // 判断是否加载更多
    private boolean isLoadingMore = false;
    // 判断是否刷新我的关注
    private boolean isSyncAttention = false;
    // 当前分页页码
    private int mCurPageNum = 1;

    // -标记
    private static final int INIT = 0x01;
    private static final int PREPARE_LVIE = 0x02;

    // 是否第一次进入页面
    private boolean isFirstEnter = true;

    // 选中的分类项
    private int mSelCategory = 0;
    // 当前分类id
    private int mCategoryId = Constants.CATEGORY_HOT_LIVE;

    // 标记分类栏所在的列表下标
    private int mCategoryIndex = 2;

    private View mRootView;

    // -权限控制
    // SD卡读写权限
    private final int LIVE_PERMISSION_RW_EXTERNAL_STORAGE_CODE = 100;
    private static final String[] RW_SD_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    // 照相机权限
    private final int LIVE_PERMISSION_CAMERA_CODE = 101;
    private static final String[] CAMERA_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    public LiveFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_live, container, false);

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 使用ButterKnife框架
        ButterKnife.bind(this, mRootView);

        if (Build.VERSION.SDK_INT >= 22) {
            // 请求用户权限
            reminderUserPermission(INIT);
        } else {
            // 初始化
            init();
        }
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

        // 选中的分类重置
        mSelCategory = 0;
        // 设置分类的id为热门直播
        mCategoryId = Constants.CATEGORY_HOT_LIVE;
        // 当前请求页为第一页
        mCurPageNum = 1;
        // 数据请求
        getChannelIndexTotal(mCurPageNum, mCategoryId);
        // 获取每日推荐数据
        mContentLoader.getDailyRecommendations(Constants.OPEN_APP_TO_SCAN);
    }

    /**
     * 获取直播首页数据
     */
    private void getChannelIndexTotal(int curPage, int categoryId) {
        // 分类编号
        String catId = String.valueOf(categoryId);
        // 如果是热门直播，则不传值
        if (categoryId == Constants.CATEGORY_HOT_LIVE) {
            catId = "";
        }
        // 获取用户id
        int userId = UserHelper.getUserId(getActivity());
        // 时间戳键值对的key基
        String baseKey = "live_index_timestamp_scan_";
        // 时间戳
        String dateTime = "";
        // 如果用户在登录状态
        if (userId != -1) {
            dateTime = SPCUtils.getString(getActivity(), (baseKey + String.valueOf(userId)));
        }
        // 获取直播首页数据
        mContentLoader.getChannelIndexTotal(curPage, 10, catId, dateTime);
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

//                mXrvLive.setNoMore(false);

                if (isRefresh == false) {
                    mSelCategory = 0;
                    mCategoryId = Constants.CATEGORY_HOT_LIVE;
                    isRefresh = true;
                    mCurPageNum = 1;
                    getChannelIndexTotal(mCurPageNum, mCategoryId);
                }
            }

            @Override
            public void onLoadMore() {

                if (isLoadingMore == false) {
                    isLoadingMore = true;
                    // 页码+1
                    mCurPageNum++;
                    getChannelIndexTotal(mCurPageNum, mCategoryId);
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

        /**
         * 滚动事件，达到分类栏悬停的效果
         */
        mXrvLive.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) mXrvLive.getLayoutManager();

                int position = layoutManager.findFirstVisibleItemPosition();
                AppLog.i("psn", "position i s " + position);
                AppLog.i("psn", "category index is " + mCategoryIndex);
                if (mCategoryIndex > 0) {
                    if (position > mCategoryIndex) {
                        mRvTopCategory.setVisibility(View.VISIBLE);
                    } else {
                        mRvTopCategory.setVisibility(View.INVISIBLE);
                    }
                } else {
                    int scrollY = getScrollYDistance(mXrvLive);
                    AppLog.i("psn", "scroll y = " + scrollY);
                    if (scrollY <= DensityUtil.dip2px(getActivity(), 100)) {
                        mRvTopCategory.setVisibility(View.INVISIBLE);
                    } else {
                        mRvTopCategory.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });

    }

    private int getScrollYDistance(XRecyclerView xRecyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) xRecyclerView.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(firstVisibleItemPosition);
        if (firstVisiableChildView != null) {
            int itemHeight = firstVisiableChildView.getHeight();
            return (firstVisibleItemPosition) * itemHeight - firstVisiableChildView.getTop();
        } else {
            return 0;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void reminderUserPermission(int type) {
        switch (type) {
            case INIT:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(RW_SD_PERMISSIONS, LIVE_PERMISSION_RW_EXTERNAL_STORAGE_CODE);
                } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(RW_SD_PERMISSIONS, LIVE_PERMISSION_RW_EXTERNAL_STORAGE_CODE);
                } else {
                    // 初始化
                    init();
                }
                break;
            case PREPARE_LVIE:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(CAMERA_PERMISSIONS, LIVE_PERMISSION_CAMERA_CODE);
                } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(CAMERA_PERMISSIONS, LIVE_PERMISSION_CAMERA_CODE);
                } else {
                    prepareLive();
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case LIVE_PERMISSION_RW_EXTERNAL_STORAGE_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 初始化
                    init();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.live_read_write_external_storage), Toast.LENGTH_SHORT).show();
                }
                break;
            case LIVE_PERMISSION_CAMERA_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prepareLive();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.live_camera_jurisdiction), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && isFirst) {
            // 隐藏推荐页
            mRecommendPage.hide();
            mRecommendPage.setFocusable(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstEnter) {
            isFirstEnter = false;
            mXrvLive.setRefreshing(true);
        } else {
            isSyncAttention = true;
            // 刷新我的关注
            getChannelIndexTotal(mCurPageNum, mCategoryId);
        }
    }

    @OnClick({R.id.btn_takelive})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_takelive:
                MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BUTTON);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    prepareLive();
                } else {
                    // 提示用户开启直播权限
                    reminderUserPermission(PREPARE_LVIE);
                }
                break;
        }
    }

    /**
     * 准备直播
     */
    private void prepareLive() {
        boolean isLogin = UserHelper.isLogined(getActivity());
        if (isLogin) {
            startActivity(new Intent(getActivity(), LiveActivity.class));
        } else {
            showLoginDialog();
        }
    }

    /**
     * 显示登录提示对话框
     */
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
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);


            if (isLoadingMore) {
                isLoadingMore = false;
                mXrvLive.loadMoreComplete();
            }
            if (isRefresh) {
                isRefresh = false;
                mXrvLive.refreshComplete();
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
                Glide.with(getActivity())
                        .load(photo)
                        .placeholder(R.drawable.androidloading)
                        .into(imgPhoto);
            }
            if (!TextUtils.isEmpty(avatar)) {
                ImageView imgAvatar = (ImageView) mRecommendPage.getView(R.id.img_recommendations_avatar);
                Glide.with(getActivity())
                        .load(avatar)
                        .placeholder(R.drawable.androidloading)
                        .into(imgAvatar);
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

        @Override
        public void onGetChannelIndexTotal(ChannelIndexTotalResult result, long dateTime) {
            super.onGetChannelIndexTotal(result, dateTime);

            AppLog.i("sls", "enter onGetChannelIndexTotal");
            if (isSyncAttention) {
                isSyncAttention = false;
                // 获取我的关注
                mAttention = result.getLastDynamicUser();
                // 刷新我的关注
                setAdapter(REFRESH_MY_ATTENTION);
//            } else if (isSwitchCate) {
//
//                isSwitchCate = false;
//                // -刷新的时候去掉之前加载
//                isRefresh = false;
//                isLoadingMore = false;
//                mXrvLive.refreshComplete();
//                mXrvLive.loadMoreComplete();
//                // 加载页标记为第1页
//                mCurPageNum = 1;
//
//                // 获取正在直播的列表
//                mLivingList.clear();
//                mLivingList.addAll(result.getChannelList());
//
//                // 获取回放列表
//                ChannelIndexTotalResult.HistoryListBean historyListBean = result.getHistoryList();
//                mPlaybackList.clear();
//                if (historyListBean != null) {
//                    mPlaybackList.addAll(historyListBean.getRows());
//                }
//                setAdapter(REFRESH_LIVE);
//            } else if (isRefresh) {
//                // -刷新的时候去掉之前加载
//                isRefresh = false;
//                isLoadingMore = false;
//                mXrvLive.refreshComplete();
//                mXrvLive.loadMoreComplete();
//                // 加载页标记为第1页
//                mCurPageNum = 1;
//
//                // 获取我的关注
//                mAttention = result.getLastDynamicUser();
//
//                // 获取广告列表
//                mAdList.clear();
//                mAdList.addAll(result.getAdvertisingList());
//
//                // 获取分类列表，“热门分类”手动添加
//                mCategoryList.clear();
//                CategoryBean categoryBean = new CategoryBean();
//                categoryBean.setId(Constants.CATEGORY_HOT_LIVE);
//                mCategoryList.add(categoryBean);
//                mCategoryList.addAll(result.getCategoryList());
//
//                // 获取正在直播的列表
//                mLivingList.clear();
//                mLivingList.addAll(result.getChannelList());
//
//                // 获取回放列表
//                ChannelIndexTotalResult.HistoryListBean historyListBean = result.getHistoryList();
//                mPlaybackList.clear();
//                if (historyListBean != null) {
//                    mPlaybackList.addAll(historyListBean.getRows());
//                }
//                setAdapter(REFRESH_ALL);
            } else if (isLoadingMore) {
                // 加载的时候去掉刷新
                isLoadingMore = false;
                isRefresh = false;

                // 获取回放列表
                ChannelIndexTotalResult.HistoryListBean historyListBean = result.getHistoryList();
                // 如果无数据
                if (historyListBean == null || historyListBean.getRows() == null || historyListBean.getRows().size() == 0) {
                    // 显示没有更多
                    mXrvLive.setNoMore(true);
                } else { // 如果有数据
                    // 结束加载
                    mXrvLive.loadMoreComplete();
                    // 添加数据
                    mPlaybackList.addAll(historyListBean.getRows());
                    // 刷新回放
                    setAdapter(REFRESH_PLAYBACK_LIST);
                }
            } else {

//                // 加载完毕
//                isLoadingMore = false;
//                // 刷新完毕
//                isRefresh = false;
//                // 刷新结束
//                mXrvLive.refreshComplete();

                AppLog.i("fsa", "else");
                AppLog.i("dsp", "else ");
                // -刷新的时候去掉之前加载
                isRefresh = false;
                isLoadingMore = false;
                mXrvLive.refreshComplete();
                mXrvLive.loadMoreComplete();
                // 加载页标记为第1页
                mCurPageNum = 1;

                // 获取我的关注
                mAttention = result.getLastDynamicUser();

                // 获取广告列表
                mAdList.clear();
                mAdList.addAll(result.getAdvertisingList());

                // 获取分类列表，“热门分类”手动添加
                mCategoryList.clear();
                CategoryBean categoryBean = new CategoryBean();
                categoryBean.setId(Constants.CATEGORY_HOT_LIVE);
                mCategoryList.add(categoryBean);
                mCategoryList.addAll(result.getCategoryList());
                AppLog.i("fsa", "ad size is " + mCategoryList.size());

                // 获取正在直播的列表
                mLivingList.clear();
                mLivingList.addAll(result.getChannelList());

                // 获取回放列表
                ChannelIndexTotalResult.HistoryListBean historyListBean = result.getHistoryList();
                mPlaybackList.clear();
                if (historyListBean != null) {
                    mPlaybackList.addAll(historyListBean.getRows());
                }
                AppLog.i("sls", "onGetChannelIndexTotal aflter data got");
                setAdapter(REFRESH_ALL);
            }
        }
    }

    /**
     * 配置适配器
     *
     * @param refreshType
     */
    public void setAdapter(int refreshType) {

        // 同步分类栏的下标
        syncCategoryIndex();

        if (mAdapter == null) {
            OnCategoryItemClickListener listener = new OnCategoryItemClickListener();
            // 初始化适配器，此时mLivingList和mPlaybackList数据为空
            mAdapter = new HomeLiveAdapter(getActivity(), mAdList, mAttention, mCategoryList, mSelCategory, mLivingList,
                    mPlaybackList, listener, mRvTopCategory);
            // 刷新数据
            mXrvLive.setAdapter(mAdapter);
            // 分类栏滚动到顶部再滚动回来
//            initCategory();
        } else {
            switch (refreshType) {
                case REFRESH_MY_ATTENTION:
                    // 刷新我的关注
                    mAdapter.refreshMyAttention(mAttention);
                    break;
                case REFRESH_LIVING_LIST:
                    // 刷新正在直播列表
                    mAdapter.refreshLivingList(mLivingList);
                    break;
                case REFRESH_PLAYBACK_LIST:
                    // 刷新回放列表
                    mAdapter.refreshPlaybackList(mPlaybackList);
                    break;
                case INITIAL:
                    // 重新setAdapter
                    mAdapter = null;
                    setAdapter(INITIAL);
                    break;
                case REFRESH_LIVE:
                    AppLog.i("sls", "REFRESH_LIVE");
                    // 刷新直播和回放列表
                    mAdapter.refreshLive(mLivingList, mPlaybackList);
                    AppLog.i("sls", "after refresh live");
                    break;
                case REFRESH_ALL:
                    AppLog.i("dsp", "REFRESH_ALL");
                    // 刷新直播回放列表
                    mAdapter.refreshAll(mAdList, mAttention, mCategoryList, mSelCategory, mLivingList,
                            mPlaybackList);
                    // 配置顶部目录适配器
                    setCategoryAdapter();
                    break;
            }
        }
    }

    /**
     * 设置目录适配器
     */
    private void setCategoryAdapter() {

        if (mCateAdapter == null) {
            AppLog.i("dsp", "mCateAdapter null");
            OnCategoryItemClickListener itemClickListener = new OnCategoryItemClickListener();
            // 初始化分类适配器
            mCateAdapter = new CategoryAdapter(getActivity(), mCategoryList, itemClickListener, mRvTopCategory, mSelCategory);
            // 初始化布局管理器
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            // 设置为横向
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            // 设置布局管理器
            mRvTopCategory.setLayoutManager(layoutManager);
            // 设置适配器
            mRvTopCategory.setAdapter(mCateAdapter);
        } else {
            AppLog.i("dsp", "mCateAdapter not null");
            AppLog.i("sls", "else setSelected");
            mCateAdapter.setSelected(mSelCategory);
        }
    }

    /**
     * 同步分类栏的下标
     */
    private void syncCategoryIndex() {
        mCategoryIndex = 2;
        int id = UserHelper.getUserId(getActivity());
        if (id == -1) {
            mCategoryIndex--;
        }
        if (mAdList == null || mAdList.size() == 0) {
            mCategoryIndex--;
        }
    }

    private class OnCategoryItemClickListener implements CategoryAdapter.MyOnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            if (mCategoryList.size() > 0) { //  && position != mSelCategory

                AppLog.i("dsp", "position is " + position);
                // 获取选中的分类
                CategoryBean categoryBean = mCategoryList.get(position);
                // 获取分类id
                mCategoryId = categoryBean.getId();
                // 得到选中的分类的下标
                mSelCategory = position;
                AppLog.i("slt", "positoin is " + position);
                // 加载分页重置为1
                mCurPageNum = 1;
                // 获取数据
                getChannelIndexTotal(mCurPageNum, mCategoryId);
                // 设置选中的分类栏
                if (mAdapter != null) {
                    mAdapter.setSelected(mSelCategory);
                }

                LinearLayoutManager layoutManager = (LinearLayoutManager) mXrvLive.getLayoutManager();
                layoutManager.scrollToPosition(0);
            }
        }
    }
}
