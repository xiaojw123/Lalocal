package com.lalocal.lalocal.activity.fragment;


import android.Manifest;
import android.annotation.TargetApi;
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
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.RecommendLayout;
import com.lalocal.lalocal.view.adapter.CategoryAdapter;
import com.lalocal.lalocal.view.adapter.HomeLiveAdapter;
import com.lalocal.lalocal.view.viewholder.live.CategoryViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 重现情况分类及刷新策略：
 * 1. 点击home键，返回桌面后重进应用（后台运行，非销毁）：
 * 定位刷新
 * onResume调用刷新
 * 2. 点击分类栏item：
 * 分类栏置顶刷新
 * onItemClick方法调用刷新，列表滑动到分类栏置顶的位置
 * 3. 切换Fragment：
 * 定位刷新
 * onHideChanged调用刷新
 * 4：跳转界面再回来：
 * 全局定位刷新
 * 5：点击直播按钮再回来：
 * 定位刷新
 *
 * -------------------------------------------------------------------------------------------------
 * 分类栏顶部悬停策略：
 * 两个布局实现，一个布局A在列表里面，另一个布局B在列表外面
 * A滑动到即将屏幕顶部的时候，B显示，否则B隐藏
 *
 * 对于本身分类栏就在顶部的情况：无广告位，无我的关注
 * 这种情况下，发现刷新列表后，再向下滑动列表，数据显示滑动的距离达到约100dp的时候，列表才真正的动
 * 所以再列表下拉100dp的时候隐藏B
 *
 * -------------------------------------------------------------------------------------------------
 * 两个分类栏滑动同步策略：
 * 在列表的分类栏的ViewHolder中实现两个分类栏滑动的同步，将外部的分类栏XRecyclerView传入列表中的分类栏ViewHolder中实现同步
 *
 * @see CategoryViewHolder#syncScroll(RecyclerView, RecyclerView)
 *
 * -------------------------------------------------------------------------------------------------
 * 两个分类栏点击同步策略：
 * 向分类栏XrecyclerView所在的Adapter中分别传入当前选中的是哪一项，
 * 其中列表里的分类栏，通过一层层Adapter和ViewHolder往里面传入，直到传到CategoryAdapter里面的setSelected
 *
 * @see CategoryAdapter#setSelected(int)
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
    // 当前分页页码
    private int mCurPageNum = 1;

    // 判断是不是需要重现时刷新
    private boolean isNeedRefresh = true;

    // 如果是点击分类栏刷新
    private boolean isCateRefresh = false;

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

        /*
        对手机系统版本进行判断，若是6.0及以上，则动态获取权限
         */
        if (Build.VERSION.SDK_INT >= 22) {
            // 请求用户权限
            reminderUserPermission(INIT);
        } else {
            // 初始化
            init();
        }
    }

    /**
     * 跳转其他界面再返回时，实现全局定位刷新，不改变列表位置和分类栏的选中情况
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            isNeedRefresh = false;
            getChannelIndexTotal(mCurPageNum,mCategoryId);
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
        // 初始化XRecyclerView
        mXrvLive.setLayoutManager(new LinearLayoutManager(getActivity()));
        mXrvLive.setHasFixedSize(true);
        mXrvLive.setPullRefreshEnabled(true);
        mXrvLive.setLoadingMoreEnabled(true);
        mXrvLive.setDefaultHeaderText("轻轻下拉刷新，用力下拉进入推荐");
        mBtnTakeLive.attachToRecyclerView(mXrvLive);

        mXrvLive.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
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

        /**
         * 对XRecyclerView的触摸事件进行监听，
         * 通过对刷新头部的显示部分的高度进行判断，大于133dp的时候，视为用力刷新将每日推荐显示
         */
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
                if (mCategoryIndex > 0) {
                    if (position > mCategoryIndex) {
                        mRvTopCategory.setVisibility(View.VISIBLE);
                    } else {
                        mRvTopCategory.setVisibility(View.INVISIBLE);
                    }
                } else {
                    int scrollY = getScrollYDistance(mXrvLive);
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

    /**
     * 获取XRecyclerView滑动超出屏幕的高度，单位：px
     * @param xRecyclerView
     * @return
     */
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

    /**
     * 6.0系统动态获取权限
     * @param type
     */
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

    /**
     * 权限请求的结果处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    /**
     * 获取SD卡读取权限和相机录音等权限
     * @param requestCode
     * @param grantResults
     */
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

    /**
     * fragment切换的监听事件
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && isFirst) {
            // 隐藏推荐页
            mRecommendPage.hide();
            mRecommendPage.setFocusable(false);
        } else if (!hidden) {
            // 如果仅仅是fragment的tab切换，则刷新页面，不更改列表定位
            refreshWithSolidPosition();
            mBtnTakeLive.show();
        }

    }

    /**
     * 页面重现的监听事件
     */
    @Override
    public void onResume() {
        super.onResume();
        if (isFirstEnter) {
            isFirstEnter = false;
            mXrvLive.setRefreshing(true);
        } else if (isNeedRefresh) {
            refreshWithSolidPosition();
        }
        mBtnTakeLive.show();
        isNeedRefresh = true;
    }


    /**
     * 获取热门直播
     * 刷新页面，不更改列表定位
     */
    private void refreshWithSolidPosition() {
        // 默认为热门直播的id，自定义的，因为热门直播分类的tab是本地的，访问接口的categoryId=""，
        // 所以定义一个标记，实际访问的时候用空字符串
        mCategoryId = Constants.CATEGORY_HOT_LIVE;
        // 当前页为0
        mCurPageNum = 0;
        // 当前选中的分类栏下标为0
        mSelCategory = 0;
        // 设置选中的分类栏
        if (mAdapter != null) {
            mAdapter.setSelected(mSelCategory);
        }
        // 刷新我的关注
        getChannelIndexTotal(mCurPageNum, mCategoryId);
    }

    /**
     * 点击直播按钮的监听事件，先请求权限
     * @param v
     */
    @OnClick({R.id.btn_takelive})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_takelive:
                MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BUTTON);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    // 准备直播
                    prepareLive();
                } else {
                    // 提示用户开启直播权限
                    reminderUserPermission(PREPARE_LVIE);
                }
                break;
        }
    }
    /**
     * 准备直播，先判断是否处于登录状态
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

    /**
     * 网络数据监听回调
     */
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

        /**
         * 每日推荐数据获取结果
         * @param bean
         */
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

            // 解析数据
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
                RoundedImageView imgAvatar = (RoundedImageView) mRecommendPage.getView(R.id.img_recommendations_avatar);
                // 此处不能用Grlid，因为Glid与RoundedImageView
                DrawableUtils.displayImg(getActivity(), imgAvatar, avatar, R.drawable.androidloading);
            }
            if (TextUtils.isEmpty(nickname)) {
                nickname = "一位不愿意透露姓名的网友";
            }
            mRecommendPage.setText(R.id.tv_recommendations_nickname, nickname);

            // 直播类型标签设置
            if (type == LIVING) {
                mRecommendPage.setText(R.id.tv_recommendations_type, "正在直播");
            } else if (type == PLAYBACK) {
                mRecommendPage.setText(R.id.tv_recommendations_type, "回放");
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

        /**
         * 综合数据获取结果
         * @param result
         * @param dateTime
         */
        @Override
        public void onGetChannelIndexTotal(ChannelIndexTotalResult result, long dateTime) {
            super.onGetChannelIndexTotal(result, dateTime);

                if (isLoadingMore) {
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

                // 获取正在直播的列表
                mLivingList.clear();
                mLivingList.addAll(result.getChannelList());

                // 获取回放列表
                ChannelIndexTotalResult.HistoryListBean historyListBean = result.getHistoryList();
                mPlaybackList.clear();
                if (historyListBean != null) {
                    mPlaybackList.addAll(historyListBean.getRows());
                }
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
                    // 刷新直播和回放列表
                    mAdapter.refreshLive(mLivingList, mPlaybackList);
                    break;
                case REFRESH_ALL:
                    // 如果分类不为热门直播
                    if (mCategoryId != Constants.CATEGORY_HOT_LIVE) {
                        mLivingList.clear();
                    }
                    // 刷新直播回放列表
                    mAdapter.refreshAll(mAdList, mAttention, mCategoryList, mSelCategory, mLivingList,
                            mPlaybackList);
                    // 配置顶部目录适配器
                    setCategoryAdapter();

                    if (isCateRefresh) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) mXrvLive.getLayoutManager();
                        layoutManager.scrollToPositionWithOffset(mCategoryIndex + 1, 0);
                        isCateRefresh = false;
                    }
                    break;
            }
        }
    }

    /**
     * 设置目录适配器
     */
    private void setCategoryAdapter() {

        if (mCateAdapter == null) {
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

                // 获取选中的分类
                CategoryBean categoryBean = mCategoryList.get(position);
                // 获取分类id
                mCategoryId = categoryBean.getId();
                // 得到选中的分类的下标
                mSelCategory = position;
                // 加载分页重置为1
                mCurPageNum = 1;
                // 点击分类刷新
                isCateRefresh = true;
                // 获取数据
                getChannelIndexTotal(mCurPageNum, mCategoryId);
                // 设置选中的分类栏
                if (mAdapter != null) {
                    mAdapter.setSelected(mSelCategory);
                }
            }
        }
    }
}
