package com.lalocal.lalocal.activity.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.CarouselFigureActivity;
import com.lalocal.lalocal.activity.LiveSearchActivity;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.activity.RouteDetailActivity;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.util.DialogUtil;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.live.entertainment.adapter.LiveClassifyGridViewAdapter;
import com.lalocal.lalocal.live.entertainment.model.LiveHomeAreaResp;
import com.lalocal.lalocal.live.entertainment.model.LiveHomeListResp;
import com.lalocal.lalocal.live.entertainment.model.LivePlayBackListResp;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomLinearLayoutManager;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.CustomXRecyclerView;
import com.lalocal.lalocal.view.adapter.LiveMainAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lalocal.lalocal.R.id.live_search_bar;
import static java.lang.Boolean.parseBoolean;

/**
 * Created by xiaojw on 2016/6/3.
 */
public class NewsFragment extends BaseFragment implements View.OnClickListener {
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    public static final int RESQUEST_COD = 701;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static final String CREATE_ROOMID = "createRoomId";
    private ContentLoader contentService;
    private ListView liveRecyclearView;
    //    private BlurImageView layoutBg;
    //   private LiveMainListAdapter liveMainListAdapter;
    private List<LiveRowsBean> allRows = new ArrayList<LiveRowsBean>();
    private List<LiveRowsBean> allAttenRows = new ArrayList<LiveRowsBean>();
    private boolean isFirstLoad = true;//刷新列表
    boolean closeRegister = true;
    private int roomId = 0;
    Handler handler = new Handler();
    private int createRoomId;
    private String mliveStreamingURL;
    private String pullUrl;
    private int userCreateId;
    private String createAvatar;
    private SpecialShareVOBean shareVOCreate;
    private FrameLayout liveSeachFl;
    private CustomXRecyclerView xRecyclerView;
    private LiveMainAdapter liveMainAdapter, attenAdapter;

    private int totalPages;
    private TextView titleHot;
    private LinearLayout hotContent;
    private GridView gridView;
    private LiveClassifyGridViewAdapter liveClassifyGridViewAdapter;
    private LinearLayout searchLayout;
    private RelativeLayout.LayoutParams lp;
    private View viewCover;
    private View inflate, searchinfate;
    private SliderLayout sliderLayout;
    private TextView titleAttention;
    private TextPaint paint2;
    private TextPaint paint1;
    private LinearLayout dotContainer;
    private int firstVisibleItemPosition;
    private ImageView searchBar;
    private int lastScrollDy;
    private TextView attenLoginText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentService = new ContentLoader(getActivity());
        contentService.setCallBack(new MyCallBack());
        contentService.getLiveArea();
        contentService.recommendAd();
        requestBasicPermission(); // 申请APP基本权限
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_news_layout, container, false);
        LinearLayout createLiveRoom = (LinearLayout) view.findViewById(R.id.live_create_room);
        createLiveRoom.setOnClickListener(this);
        searchBar = (ImageView) view.findViewById(live_search_bar);
        searchBar.getDrawable().setAlpha(60);
        searchBar.setOnClickListener(this);
        titleAttention = (TextView) view.findViewById(R.id.live_fragment_title_attention);
        titleAttention.setOnClickListener(this);
        titleHot = (TextView) view.findViewById(R.id.live_fragment_title_hot);
        titleHot.setOnClickListener(this);
      /*  paint2 = titleAttention.getPaint();
        paint1 = titleHot.getPaint();*/
        xRecyclerView = (CustomXRecyclerView) view.findViewById(R.id.xrecyclerview);
        //TODO:直播搜索 add by xiaojw
        attenLoginText = (TextView) view.findViewById(R.id.live_no_login_atten);
//        FrameLayout headerContainer = (FrameLayout) view.findViewById(R.id.live_header_container);
//        headerContainer.bringToFront();
        hotContent = (LinearLayout) view.findViewById(R.id.hot_content);
        gridView = (GridView) view.findViewById(R.id.live_classify);
//        hotContent.bringToFront();
        titleAttention.setSelected(false);
        titleHot.setSelected(true);
        initRecyclerView();
        return view;
    }

    private void initGridView(final List<LiveHomeAreaResp.ResultBean> result) {
        LiveHomeAreaResp.ResultBean resultBean = new LiveHomeAreaResp.ResultBean();
        resultBean.setName("热门");
        result.add(0, resultBean);
        int size = result.size();
        classflyHeight = ((size % 4) == 0 ? ((size / 4) * 40) : ((size / 4) + 1) * 40);
        liveClassifyGridViewAdapter = new LiveClassifyGridViewAdapter(getActivity(), result);
        gridView.setAdapter(liveClassifyGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView classifyItem = (TextView) view.findViewById(R.id.live_classify_item_tv);
                LiveHomeAreaResp.ResultBean resultBean1 = result.get(position);
                int id1 = resultBean1.getId();
                titleHot.setText(resultBean1.getName());
                liveClassifyGridViewAdapter.setSelectedPosition(position);
                liveClassifyGridViewAdapter.notifyDataSetChanged();
                if (allRows != null) {
                    allRows.clear();
                    pageNumber = 1;
                }
                if (position == 0) {
                    contentService.getLivelist("", "");
                } else {
                    contentService.getLivelist(String.valueOf(id1), "");
                }
            }
        });
    }

    int startScollYDistance = 0;
    boolean isFirstGetData = true;
    int endScollYDistance = 0;
    boolean isVisible = true;
    boolean isStopAutoCycle = false;

    private void initRecyclerView() {
        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        initHeaderView();
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
                    showClassifyView(0, isClick);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int top = xRecyclerView.getChildAt(0).getTop();
                int scollYDistance = getScollYDistance();
                int i = DensityUtil.dip2px(getActivity(), 10);
                int scollDy = 50 - DensityUtil.px2dip(getActivity(), (scollYDistance - startScollYDistance));
                AppLog.print("onScrolled firstVisibleItemPosition____" + firstVisibleItemPosition + "____scollDy____" + scollDy);
                if ((scollDy < 10 || firstVisibleItemPosition > 1)) {
                    if (isVisible && scollDy != 0) {
                        lastScrollDy = scollDy;
                        searchBar.setVisibility(View.VISIBLE);
                        isVisible = false;
                    }
                } else {
                    if (!isVisible) {
                        lastScrollDy = scollDy;
                        isVisible = true;
                        AppLog.print("onScrolled—————scrollbar——invisible———");
                        searchBar.setVisibility(View.INVISIBLE);
                    }
                }

                //设置只有轮播图显示时才滚动
                if (firstVisibleItemPosition > 1) {
                    if (!isStopAutoCycle) {
                        isStopAutoCycle = true;
                        sliderLayout.stopAutoCycle();
                        sliderLayout.setFocusable(false);
                        xRecyclerView.setFocusable(true);
                        xRecyclerView.requestFocus();

                    }

                } else {
                    if (isStopAutoCycle) {
                        isStopAutoCycle = false;
                        sliderLayout.startAutoCycle();
                        sliderLayout.setFocusable(true);
                    }
                }
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

    private int prePosition = -1;

    private void initHeaderView() {
        AppLog.i("TAG", "给recycler添加头部");
        searchinfate = View.inflate(getActivity(), R.layout.header_search_layout, null);
        inflate = View.inflate(getActivity(), R.layout.live_recommend_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inflate.setLayoutParams(params);
        searchinfate.setLayoutParams(params);
        liveSeachFl = (FrameLayout) searchinfate.findViewById(R.id.live_search_fl);
        TextView liveSearchTv = (TextView) searchinfate.findViewById(R.id.live_search_textview);
        liveSearchTv.getCompoundDrawables()[0].setAlpha(60);
//        liveSearchTv.setCompoundDrawables(getTextColorDrawable(liveSearchTv), null, null, null);
        liveSeachFl.setOnClickListener(this);
        dotContainer = (LinearLayout) inflate.findViewById(R.id.live_dot_container);
        sliderLayout = (SliderLayout) inflate.findViewById(R.id.live_ad_slider);

        sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        // 轮播图页面改变
        sliderLayout.addOnPageChangeListener(new ViewPagerEx.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (dotContainer.getChildAt(0) != null && dotContainer.getChildAt(position) != null) {
                    dotContainer.getChildAt(position).setBackgroundResource(
                            R.drawable.icon_dark_dot_selected);
                    if (prePosition != -1) {
                        dotContainer.getChildAt(prePosition).setBackgroundResource(
                                R.drawable.icon_dark_dot_normal);
                    }
                    prePosition = position;
                }
            }
        });
        xRecyclerView.addHeaderView(searchinfate);
        xRecyclerView.addHeaderView(inflate);

    }


//    @NonNull
//    private Drawable getTextColorDrawable(TextView liveSearchTv) {
//        Drawable drawable = getResources().getDrawable(R.drawable.searchbar_searchicon);
//        Drawable colorDrawable = tintDrawable(drawable, liveSearchTv.getTextColors());
//        colorDrawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        return colorDrawable;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESQUEST_COD && (resultCode == 101 || resultCode == 105)) {
            if (data != null) {
                isLogining = true;
                String email = data.getStringExtra(LoginActivity.EMAIL);
                String psw = data.getStringExtra(LoginActivity.PSW);
                contentService.login(email, psw);
            }
        }
    }

    boolean firstLoadData = true;

    @Override
    public void onHiddenChanged(boolean hidden) {//切换fragment刷新fragment
        super.onHiddenChanged(hidden);
        if (!hidden) {
          /*  xRecyclerView.setPullRefreshEnabled(true);
            xRecyclerView.setRefreshing(true);
            isRefresh=true;
            contentService.getLivelist(null);*/
        }
    }


    //监听IM账号登录状态
    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            AppLog.i("TAG", "newsfragment監聽用戶登錄狀態：" + statusCode);
        }
    };

    boolean isClick = true;
    int classflyHeight = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_create_room:
                MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BUTTON);
               /* if (Build.VERSION.SDK_INT >= 23) {
                    AppLog.i("TAG", "点击直播按钮，版本大于23，权限判断");
                    reminderUserPermission();//创建直播间，判断权限
                } else {
                    AppLog.i("TAG", "点击直播按钮，版本小于，权限判断");
                    prepareLive();
                }*/
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    prepareLive();
                } else {
                    reminderUserPermission();
                }
                break;
            case R.id.live_search_fl:
                MobHelper.sendEevent(getActivity(), MobEvent.LIVE_SEARCH);
                Intent intent = new Intent(getActivity(), LiveSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.live_fragment_title_hot:
                if (titleHot.isSelected()) {
                    return;
                }
                titleAttention.setSelected(false);
                titleHot.setSelected(true);
                xRecyclerView.setVisibility(View.VISIBLE);
                attenLoginText.setVisibility(View.INVISIBLE);
                xRecyclerView.addHeaderView(inflate);
                xRecyclerView.setAdapter(liveMainAdapter);
                xRecyclerView.setRefreshing(true);
                showIndictorView(titleHot);
                AppLog.print("click end____searchLayout Visible___" + xRecyclerView.getVisibility());
               /* if (isClick) {
                    isClick = false;
                    showClassifyView(classflyHeight, isClick);
                } else {
                    isClick = true;
                    showClassifyView(0, isClick);
                }*/
              /*  paint2.setFakeBoldText(false);
                paint1.setFakeBoldText(true);*/
                break;
            case R.id.live_fragment_title_attention:
                if (titleAttention.isSelected()) {
                    return;
                }
                titleAttention.setSelected(true);
                titleHot.setSelected(false);
                AppLog.print("click start_____");
                MobHelper.sendEevent(getActivity(), MobEvent.LIVE_ATTENTION);
                showIndictorView(titleAttention);
                if (attenAdapter == null) {
                    attenAdapter = new LiveMainAdapter(getActivity(), null);
                    attenAdapter.setOnLiveItemClickListener(liveItemClickListener);
                }
                attenAdapter.setRecyclerView(xRecyclerView);
                xRecyclerView.setHeaderVisible();
                xRecyclerView.setAdapter(attenAdapter);
                if (UserHelper.isLogined(getActivity())) {
                    xRecyclerView.setVisibility(View.VISIBLE);
                    attenLoginText.setVisibility(View.INVISIBLE);
//                    contentService.getLivelist("", "true");
                    xRecyclerView.setRefreshing(true);
                } else {
                    xRecyclerView.setVisibility(View.INVISIBLE);
                    attenLoginText.setVisibility(View.VISIBLE);
                    attenLoginText.setText(getResources().getString(R.string.you_no_login_no_atten));
                }
                AppLog.print("click end____searchLayout Visible___" + xRecyclerView.getVisibility());
                break;
            case live_search_bar:
                Intent intent1 = new Intent(getActivity(), LiveSearchActivity.class);
                startActivity(intent1);
                break;
        }
    }


    private void showIndictorView(View view) {
//        Drawable drawable1 = getActivity().getResources().getDrawable(R.drawable.tab_morefanction_unsel);
//        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        Drawable drawable2 = getActivity().getResources().getDrawable(R.drawable.tabselect_line);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
        if (view == titleAttention) {
            titleHot.setCompoundDrawables(null, null, null, null);
            titleAttention.setCompoundDrawables(null, null, null, drawable2);
        } else if (view == titleHot) {
            titleAttention.setCompoundDrawables(null, null, null, null);
            titleHot.setCompoundDrawables(null, null, null, drawable2);
        }
    }

    public void showClassifyView(int height, boolean isClick) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(new WrapView(hotContent), "height", DensityUtil.dip2px(getActivity(), height));
        objectAnimator.setDuration(120);
        objectAnimator.start();
        if (isClick) {
            Drawable drawable1 = getActivity().getResources().getDrawable(R.drawable.tab_morefanction_unsel);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            Drawable drawable2 = getActivity().getResources().getDrawable(R.drawable.tabselect_line);
            drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
            titleHot.setCompoundDrawables(null, null, drawable1, drawable2);

        } else {
            Drawable drawable1 = getActivity().getResources().getDrawable(R.drawable.tab_morefanction_sel);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            Drawable drawable2 = getActivity().getResources().getDrawable(R.drawable.tabselect_line);
            drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
            titleHot.setCompoundDrawables(null, null, drawable1, drawable2);
        }
        titleAttention.setCompoundDrawables(null, null, null, null);

    }

    class WrapView {
        private View view;
        private int width;
        private int height;

        public WrapView(View view) {
            this.view = view;
        }

        public int getWidth() {
            return view.getLayoutParams().width;
        }

        public void setWidth(int width) {
            this.width = width;
            view.getLayoutParams().width = width;
            view.requestLayout();
        }

        public int getHeight() {
            return view.getLayoutParams().height;
        }

        public void setHeight(int height) {
            this.height = height;
            view.getLayoutParams().height = height;
            view.requestLayout();
        }
    }

    private int liveUserId;
    private String createNickName;
    String createAnn = null;
    boolean isRefresh = false;
    int pageNumber = 1;
    boolean lastPage = false;
    private List<RecommendAdResultBean> adResultList;

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

        private void resetAdapterData() {
            allRows.clear();
            if (titleHot.isSelected()) {
                if (liveMainAdapter != null) {
                    liveMainAdapter.refresh(allRows);
                }
            }
            if (titleAttention.isSelected()) {
                if (titleAttention.isSelected()) {
                    if (titleAttention != null) {
                        attenAdapter.refresh(allRows);
                    }
                }
            }
        }

        @Override
        public void onCreateLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {
            super.onCreateLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                LiveRowsBean result = createLiveRoomDataResp.getResult();
                createRoomId = result.getRoomId();
                SPCUtils.put(getActivity(), CREATE_ROOMID, String.valueOf(createRoomId));
                Object annoucement = createLiveRoomDataResp.getResult().getAnnoucement();
                if (annoucement != null) {
                    createAnn = annoucement.toString();
                } else {
                    createAnn = "这是公告";
                }
                //初始化直播间

            }
        }

        @Override
        public void onLiveHomeList(LiveHomeListResp liveListDataResp, String attentionFlag) {
            try {
                if (sliderLayout != null) {
                    sliderLayout.startAutoCycle();
                }
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
                            attenAdapter = new LiveMainAdapter(getActivity(), allAttenRows);
                            attenAdapter.setOnLiveItemClickListener(liveItemClickListener);
                        } else {
                            attenAdapter.refresh(allAttenRows);
                        }
                        attenAdapter.setHightPostion(true, allAttenRows.size());
                        xRecyclerView.setAdapter(attenAdapter);
                    } else {
                        if (isFirstLoad) {
                            isFirstLoad = false;
                            if(getActivity()!=null){
                                liveMainAdapter = new LiveMainAdapter(getActivity(), allRows);
                            }
                            AppLog.i("TAG", "给recycler   liveMainAdapter");
                            xRecyclerView.setAdapter(liveMainAdapter);
                            liveMainAdapter.setOnLiveItemClickListener(liveItemClickListener);
                        } else {
                            liveMainAdapter.refresh(allRows);
                        }
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
                } else {
                    liveMainAdapter.refresh(allRows);
                }
                if (isRefresh) {
                    xRecyclerView.refreshComplete();
                } else if (!lastPage) {
                    xRecyclerView.setNoMore(true);
                } else {
                    xRecyclerView.loadMoreComplete();
                }
                if (allAttenRows.size() < 1) {
                    if (isAttention) {
                        attenLoginText.setVisibility(View.VISIBLE);
                        attenLoginText.setText(getResources().getString(R.string.got_to_atten_travel_talent));
                    }
                }
            }

        }

        @Override
        public void onLiveHomeArea(LiveHomeAreaResp liveHomeAreaResp) {
            super.onLiveHomeArea(liveHomeAreaResp);
            if (liveHomeAreaResp.getReturnCode() == 0) {
                List<LiveHomeAreaResp.ResultBean> result = liveHomeAreaResp.getResult();
                initGridView(result);
            }
        }


        @Override
        public void onRecommendAd(RecommendAdResp recommendAdResp) {
            super.onRecommendAd(recommendAdResp);
            try {
                if (recommendAdResp.getReturnCode() == 0) {
                    // 获取广告数据
                    adResultList = recommendAdResp.getResult();
                    sliderLayout.removeAllSliders();
                    dotContainer.removeAllViews();
                    for (int i = 0; i < adResultList.size(); i++) {
                        DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                        // CustomSliderView defaultSliderView=new CustomSliderView(getActivity());
                        defaultSliderView.image(adResultList.get(i).photo);
                        defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
                        defaultSliderView.setOnSliderClickListener(onSliderClickListener);
                        sliderLayout.addSlider(defaultSliderView);
                        View point = new View(getActivity());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                15, 15);
                        params.leftMargin = 20;
                        point.setBackgroundResource(R.drawable.icon_dark_dot_normal);
                        point.setLayoutParams(params);
                        // 为point设置标识,便于将来识别point
                        point.setTag(i);
                        dotContainer.addView(point);
                    }
                    //     dotContainer.getChildAt(0).setBackgroundResource(R.drawable.icon_dark_dot_selected);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoginSucess(User user) {
            super.onLoginSucess(user);
        }


    }

    private BaseSliderView.OnSliderClickListener onSliderClickListener = new BaseSliderView.OnSliderClickListener() {
        @Override
        public void onSliderClick(BaseSliderView slider) {
            int position = sliderLayout.getCurrentPosition();
            switch (position) {
                case 0:
                    MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BANNER_01);
                    break;
                case 1:
                    MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BANNER_02);
                    break;
                case 2:
                    MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BANNER_03);
                    break;
                case 3:
                    MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BANNER_04);
                    break;
                case 4:
                    MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BANNER_05);
                    break;
                case 5:
                    MobHelper.sendEevent(getActivity(), MobEvent.LIVE_BANNER_06);
                    break;
            }
            // 点击跳转
            RecommendAdResultBean recommendAdResultBean = adResultList.get(position);
            String url = recommendAdResultBean.url;
            int targetType = recommendAdResultBean.targetType;
            int targetId = recommendAdResultBean.targetId;
            Intent intent = null;
            switch (targetType) {
                case Constants.PLAY_BACK_TYPE_URL:
                    intent = new Intent(getActivity(), PlayBackActivity.class);
                    intent.putExtra("id", String.valueOf(targetId));
                    startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_URL:
                    AppLog.i("addd", "链接");
                    intent = new Intent(getActivity(), CarouselFigureActivity.class);
                    intent.putExtra("carousefigure", recommendAdResultBean);
                    getActivity().startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_ARTICLE:
                    AppLog.i("addd", "文章");
                    intent = new Intent(getActivity(), ArticleActivity.class);
                    intent.putExtra("targetID", String.valueOf(targetId));
                    getActivity().startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_PRODUCTION:
                    AppLog.i("addd", "产品--" + targetId);
                    // 跳转到商品详情界面
                    SpecialToH5Bean specialToH5Bean = new SpecialToH5Bean();
                    specialToH5Bean.setTargetId(targetId);

                    intent = new Intent(getActivity(), ProductDetailsActivity.class);
                    intent.putExtra("productdetails", specialToH5Bean);
                    getActivity().startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_ROUTE:
                    AppLog.i("addd", "路线");
                    intent = new Intent(getActivity(), RouteDetailActivity.class);
                    intent.putExtra("detail_id", targetId);
                    getActivity().startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_THEME:
                    AppLog.i("addd", "专题");
                    intent = new Intent(getActivity(), SpecialDetailsActivity.class);
                    intent.putExtra("rowId", targetId + "");
                    getActivity().startActivity(intent);
                    break;
                case Constants.TARGET_TYPE_CHANNEL:
                    Intent intent1 = new Intent(getActivity(), AudienceActivity.class);
                    intent1.putExtra("id", String.valueOf(targetId));
                    startActivity(intent1);
                    break;
            }
        }
    };


    private LiveMainAdapter.OnLiveItemClickListener liveItemClickListener = new LiveMainAdapter.OnLiveItemClickListener() {
        @Override
        public void goLiveRoom(LiveRowsBean liveRowsBean) {
            if (liveRowsBean.getEndAt() != null && liveRowsBean.getStartAt() != null) {
                Intent intent = new Intent(getActivity(), PlayBackActivity.class);
                intent.putExtra("id", String.valueOf(liveRowsBean.getId()));
                startActivity(intent);
            } else {
                roomId = liveRowsBean.getRoomId();
                String createRoom = SPCUtils.getString(getActivity(), CREATE_ROOMID);
                String s = String.valueOf(roomId);
                if (createRoom != null && createRoom.equals(s)) {
                    CommonUtil.REMIND_BACK = 1;
                    prepareLive();
                    return;
                }
                Intent intent = new Intent(getActivity(), AudienceActivity.class);
                intent.putExtra("id", String.valueOf(liveRowsBean.getId()));
                startActivity(intent);
            }
        }
    };
    private int pageCount = 2;
    private boolean isLoading = false;
    private int pageSize = 9;


    public class XRecyclerviewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            isRefresh = true;
            RecyclerView.Adapter adapter = xRecyclerView.getAdapter();
            if (adapter == liveMainAdapter) {
                contentService.getLivelist("", "");
                AppLog.print("热门refrsh");
            } else if (adapter == attenAdapter) {
                AppLog.print("关注refresh");
                contentService.getLivelist("", "true");
            }
        }

        @Override
        public void onLoadMore() {
            isRefresh = false;
            if (lastPage) {
                xRecyclerView.setNoMore(true);
            } else {
                RecyclerView.Adapter adapter = xRecyclerView.getAdapter();
                if (adapter == liveMainAdapter) {
                    contentService.getPlayBackLiveList("", pageNumber, "");
                    AppLog.print("热门laodmore");
                } else if (adapter == attenAdapter) {
                    AppLog.print("关注laodmore");
                    contentService.getPlayBackLiveList("", pageNumber, "true");
                }
            }


        }

    }


    boolean isLogining = false;

    private void prepareLive() {
        boolean isLogin = UserHelper.isLogined(getActivity());
        boolean loginStatus = DemoCache.getLoginStatus();
        if (isLogin && loginStatus) {
            startActivity(new Intent(getActivity(), LiveActivity.class));
        } else if (isLogin && !loginStatus) {
            String imccId = UserHelper.getImccId(getActivity());
            String imToken = UserHelper.getImToken(getActivity());
            if (imccId != null && imToken != null) {
                loginIMServer(imccId, imToken);
            }
        } else {
            CustomChatDialog customDialog = new CustomChatDialog(getActivity());
            customDialog.setContent(getString(R.string.live_login_hint));
            customDialog.setCancelable(false);
            customDialog.setCancelable(false);
            customDialog.setCancelBtn(getString(R.string.live_canncel), null);
            customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, RESQUEST_COD);

                }
            });
            customDialog.show();

            DialogUtil.addDialog(customDialog);
        }
    }

    private void loginIMServer(final String imccId, final String imToken) {
        NIMClient.getService(AuthService.class).login(new LoginInfo(imccId, imToken)).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                prepareLive();
                DemoCache.setAccount(imccId);
                DemoCache.getRegUserInfo();
                DemoCache.setLoginStatus(true);
            }

            @Override
            public void onFailed(int i) {

                DemoCache.setLoginStatus(false);
            }

            @Override
            public void onException(Throwable throwable) {

                DemoCache.setLoginStatus(false);
            }
        });
    }


    private void claerImLoginInfo() {
        DemoCache.clear();
        AuthPreferences.clearUserInfo();
        NIMClient.getService(AuthService.class).logout();
        DemoCache.setLoginStatus(false);
    }

    // 权限控制
    private void requestBasicPermission() {
        MPermission.with(getActivity())
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();

    }

    // 权限控制
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };
    //开启摄像头权限

    @TargetApi(Build.VERSION_CODES.M)
    private void reminderUserPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LIVE_PERMISSIONS, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LIVE_PERMISSIONS, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            prepareLive();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prepareLive();
            } else {
                Toast.makeText(getActivity(), getString(R.string.live_camera_jurisdiction), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        //    Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        //  Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册监听
        registerObservers(true);
        if (sliderLayout != null) {
            sliderLayout.startAutoCycle();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onStop() {
        super.onStop();
        if (!isClick) {
            isClick = true;
            showClassifyView(0, isClick);
        }
        if (sliderLayout != null) {
            sliderLayout.stopAutoCycle();
        }
        registerObservers(false);
        AppLog.i("TAG", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppLog.i("TAG", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.i("TAG", "onDestroy");

    }
}
