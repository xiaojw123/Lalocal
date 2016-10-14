package com.lalocal.lalocal.activity.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.daimajia.slider.library.Indicators.PagerIndicator;
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
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.DisallowParentTouchSliderLayout;
import com.lalocal.lalocal.view.adapter.LiveMainAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiaojw on 2016/6/3.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NewsFragment extends Fragment implements View.OnClickListener {

    private static final String PAGE_NAME = "NewsFragment";
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    public static final int RESQUEST_COD = 701;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static final String CREATE_ROOMID = "createRoomId";

    private ContentLoader contentService;
    private ListView liveRecyclearView;
//    private BlurImageView layoutBg;
 //   private LiveMainListAdapter liveMainListAdapter;
    private List<LiveRowsBean> allRows = new ArrayList<LiveRowsBean>();
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
    private XRecyclerView xRecyclerView;
    private LiveMainAdapter liveMainAdapter;

    private int totalPages;
    private TextView titleHot;
    private LinearLayout hotContent;
    private GridView gridView;
    private LiveClassifyGridViewAdapter liveClassifyGridViewAdapter;
    private LinearLayout searchLayout;
    private RelativeLayout.LayoutParams lp;
    private View viewCover;
    private View inflate;
    private DisallowParentTouchSliderLayout sliderLayout;
    private TextView titleAttention;
    private TextPaint paint2;
    private TextPaint paint1;
    private LinearLayout dotContainer;


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


        //TODO:直播搜索 add by xiaojw
        TextView liveSearchTv = (TextView) view.findViewById(R.id.live_search_textview);
        liveSearchTv.setCompoundDrawables(getTextColorDrawable(liveSearchTv), null, null, null);
        liveSeachFl = (FrameLayout) view.findViewById(R.id.live_search_fl);
        liveSeachFl.setOnClickListener(this);

        titleAttention = (TextView) view.findViewById(R.id.live_fragment_title_attention);
        titleAttention.setOnClickListener(this);
        titleHot = (TextView) view.findViewById(R.id.live_fragment_title_hot);
        titleHot.setOnClickListener(this);
        paint2 = titleAttention.getPaint();
        paint1 = titleHot.getPaint();

        xRecyclerView = (XRecyclerView) view.findViewById(R.id.xrecyclerview);

        FrameLayout headerContainer= (FrameLayout) view.findViewById(R.id.live_header_container);
        headerContainer.bringToFront();

        searchLayout = (LinearLayout) view.findViewById(R.id.live_search_layout);

        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int i = DensityUtil.dip2px(getActivity(), 50);
        lp.setMargins(0,i,0,0);
        searchLayout.setLayoutParams(lp);


        hotContent = (LinearLayout) view.findViewById(R.id.hot_content);
        gridView = (GridView) view.findViewById(R.id.live_classify);

        hotContent.bringToFront();


        initRecyclerView();

        return view;
    }
    private void initGridView(final List<LiveHomeAreaResp.ResultBean> result) {
        LiveHomeAreaResp.ResultBean resultBean = new LiveHomeAreaResp.ResultBean();
        resultBean.setName("热门");
        result.add(0,resultBean);
        liveClassifyGridViewAdapter = new LiveClassifyGridViewAdapter(getActivity(),result );
        gridView.setAdapter(liveClassifyGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView classifyItem= (TextView) view.findViewById(R.id.live_classify_item_tv);
                LiveHomeAreaResp.ResultBean resultBean1 = result.get(position);
                int id1 = resultBean1.getId();
                titleHot.setText(resultBean1.getName());
                paint1.setFakeBoldText(true);
                liveClassifyGridViewAdapter.setSelectedPosition(position);
                liveClassifyGridViewAdapter.notifyDataSetChanged();
                if(allRows!=null){
                    allRows.clear();
                    pageNumber=1;
                }
                if(position==0){
                    contentService.getLivelist(null);
                }else {
                    contentService.getLivelist(String.valueOf(id1));
                }

            }
        });

    }

    int startScollYDistance=0;
    boolean isFirstGetData=true;
    int endScollYDistance=0;
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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
              int scollYDistance = getScollYDistance();
              if(scollYDistance>0&&isFirstGetData){
                  startScollYDistance=scollYDistance;
                  isFirstGetData=false;
              }
              AppLog.i("TAG","scollYDistance:"+scollYDistance);
              if(!isClick){
                  isClick=true;
                  showClassifyView(0,isClick);
              }
          }

          @Override
          public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              super.onScrolled(recyclerView, dx, dy);
              int top = xRecyclerView.getChildAt(0).getTop();
              int scollYDistance = getScollYDistance();
              int i = DensityUtil.dip2px(getActivity(), 10);
              int scollDy = 50 - DensityUtil.px2dip(getActivity(), (scollYDistance - startScollYDistance));
              AppLog.i("TAG","recyclerviw滑动距离监听："+top+"  scollYDistance:"+scollYDistance+"  scollDy:"+scollDy);
              if(scollDy>0){
                /*  lp.setMargins(0,DensityUtil.dip2px(getActivity(),scollDy),0,0);
                  searchLayout.setLayoutParams(lp);*/
              }

             /* if(Math.abs(top)>i){
                  int abs = Math.abs(top);
                  lp.setMargins(0,DensityUtil.px2dip(getActivity(),Math.abs(top)),0,0);
                  searchLayout.setLayoutParams(lp);
              }*/

          }

      });

    }

    private int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) xRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();

    }


    private int[] imgs={R.drawable.a,R.drawable.loading_10,R.drawable.loading_01};
    List<Button> dotBtns;
    private static final int RECT = 0x01;
    private static final int SQUARE = 0x02;
    private int prePosition = -1;
    private void initHeaderView() {
        AppLog.i("TAG","给recycler添加头部");
        inflate = View.inflate(getActivity(), R.layout.live_recommend_layout,null);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        inflate.setLayoutParams(params);
        LinearLayout adContainer = (LinearLayout) inflate.findViewById(R.id.live_ad_container);
        dotContainer = (LinearLayout) inflate.findViewById(R.id.live_dot_container);
        sliderLayout = (DisallowParentTouchSliderLayout) inflate.findViewById(R.id.live_ad_slider);
        sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        // 轮播图页面改变
        sliderLayout.addOnPageChangeListener(new ViewPagerEx.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                AppLog.i("TAG","轮播图显示page:"+position);
                if(dotContainer.getChildAt(0)!=null){
                    dotContainer.getChildAt(position).setBackgroundResource(
                            R.drawable.icon_dark_dot_selected);
                    if(prePosition!=-1){
                        dotContainer.getChildAt(prePosition ).setBackgroundResource(
                                R.drawable.icon_dark_dot_normal);
                    }

                    prePosition = position;
                }

            }
        });
        xRecyclerView.addHeaderView(inflate);


    }


    @NonNull
    private Drawable getTextColorDrawable(TextView liveSearchTv) {
        Drawable drawable = getResources().getDrawable(R.drawable.searchbar_searchicon);
        Drawable colorDrawable = DrawableUtils.tintDrawable(drawable, liveSearchTv.getTextColors());
        colorDrawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return colorDrawable;
    }

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

   boolean firstLoadData=true;
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

    boolean isClick=true;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_create_room:
                if (Build.VERSION.SDK_INT >= 23) {
                    reminderUserPermission();//创建直播间，判断权限
                } else {
                    prepareLive();
                }
                break;
            case R.id.live_search_fl:
                Intent intent = new Intent(getActivity(), LiveSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.live_fragment_title_hot:
                if(isClick){
                    isClick=false;
                    showClassifyView(160,isClick);
                }else{
                    isClick=true;
                    showClassifyView(0,isClick);
                }
                break;
            case R.id.live_fragment_title_attention:
                isClick=true;
                showClassifyView(0,isClick);

                paint2.setFakeBoldText(true);

                titleHot.setCompoundDrawables(null,null,null,null);
                paint1.setFakeBoldText(false);
                Drawable drawable1 = getActivity().getResources().getDrawable(R.drawable.tab_morefanction_unsel);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                Drawable drawable2 = getActivity().getResources().getDrawable(R.drawable.tabselect_line);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                titleAttention.setCompoundDrawables(null,null,drawable1,drawable2);
                break;
        }
    }

   public void  showClassifyView(int height,boolean isClick){
       ObjectAnimator objectAnimator=ObjectAnimator.ofInt(new WrapView(hotContent),"height",DensityUtil.dip2px(getActivity(),height));
       objectAnimator.setDuration(300);
       objectAnimator.start();
      if(isClick){
          Drawable drawable1 = getActivity().getResources().getDrawable(R.drawable.tab_morefanction_unsel);
          drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
          Drawable drawable2 = getActivity().getResources().getDrawable(R.drawable.tabselect_line);
          drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
          titleHot.setCompoundDrawables(null,null,drawable1,drawable2);

      }else{
          Drawable drawable1 = getActivity().getResources().getDrawable(R.drawable.tab_morefanction_sel );
          drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
          Drawable drawable2 = getActivity().getResources().getDrawable(R.drawable.tabselect_line);
          drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
          titleHot.setCompoundDrawables(null,null,drawable1,drawable2);
      }
       titleAttention.setCompoundDrawables(null,null,null,null);
       paint2.setFakeBoldText(false);
       paint1.setFakeBoldText(true);
   }

    class  WrapView{
        private  View view;
        private  int width;
        private  int height;
        public  WrapView(View view){
            this.view=view;
        }
        public  int getWidth(){
            return  view.getLayoutParams().width;
        }
        public  void setWidth(int width){
            this.width=width;
            view.getLayoutParams().width=width;
            view.requestLayout();
        }
        public  int getHeight(){
            return  view.getLayoutParams().height;
        }
        public  void setHeight(int height){
            this.height=height;
            view.getLayoutParams().height=height;
            view.requestLayout();
        }
    }


    private int liveUserId;
    private String createNickName;
    String createAnn = null;
    boolean isRefresh=false;
    int pageNumber=1;
    boolean lastPage=false;
    private List<RecommendAdResultBean> adResultList;
    public class MyCallBack extends ICallBack {
        String reminfBack = "0";
        @Override
        public void onCreateLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {
            super.onCreateLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                LiveRowsBean result = createLiveRoomDataResp.getResult();
                createRoomId= result.getRoomId();
                SPCUtils.put(getActivity(), CREATE_ROOMID, String.valueOf(createRoomId));
                Object annoucement = createLiveRoomDataResp.getResult().getAnnoucement();
                if (annoucement != null) {
                    createAnn = annoucement.toString();
                } else {
                    createAnn = "这是公告";
                }
                //初始化直播间
                LiveActivity.start(getActivity(),result,createAnn,reminfBack);
            }
        }

        @Override
        public void onLiveHomeList(LiveHomeListResp liveListDataResp) {
            super.onLiveHomeList(liveListDataResp);
            if(liveListDataResp.getReturnCode()==0){

                List<LiveRowsBean> rows = liveListDataResp.getResult();
                if(rows==null){
                    return;
                }
                if(isRefresh){
                    allRows.clear();
                }
                if(allRows.size()==0){
                    allRows.addAll(0, rows);
                }else {
                    allRows.addAll(allRows.size(), rows);
                }

                Collections.sort(allRows);//排序
                if(isFirstLoad){
                    isFirstLoad=false;
                    liveMainAdapter = new LiveMainAdapter(getActivity(), allRows);
                    AppLog.i("TAG","给recycler   liveMainAdapter");
                    xRecyclerView.setAdapter(liveMainAdapter);
                    hotLiveItemClick();
                }else {
                    liveMainAdapter.refresh(allRows);
                }
                contentService.getPlayBackLiveList(null,1);
            }
        }

        @Override
        public void onPlayBackList(String json) {
            super.onPlayBackList(json);
            LivePlayBackListResp livePlayBackListResp = new Gson().fromJson(json, LivePlayBackListResp.class);
            if(livePlayBackListResp.getReturnCode()==0){
                LivePlayBackListResp.ResultBean result = livePlayBackListResp.getResult();
                pageNumber= result.getPageNumber()+1;
                lastPage = result.isLastPage();
                List<LiveRowsBean> rows = result.getRows();
                if(rows==null){
                    return;
                }
                allRows.addAll(allRows.size(), rows);
                liveMainAdapter.refresh(allRows);
                if(isRefresh){
                    xRecyclerView.refreshComplete();
                }else if(!lastPage){
                    xRecyclerView.setNoMore(true);
                }else {
                    xRecyclerView.loadMoreComplete();
                }
            }

        }

        @Override
        public void onLiveHomeArea(LiveHomeAreaResp liveHomeAreaResp) {
            super.onLiveHomeArea(liveHomeAreaResp);
            if(liveHomeAreaResp.getReturnCode()==0){
                List<LiveHomeAreaResp.ResultBean> result = liveHomeAreaResp.getResult();
                initGridView(result);
            }
        }

        @Override
        public void onPlayBackDetails(LiveRowsBean liveRowsBean) {
            super.onPlayBackDetails(liveRowsBean);
            PlayBackActivity.start(getActivity(),liveRowsBean);
        }

        @Override
        public void onRecommendAd(RecommendAdResp recommendAdResp) {
            super.onRecommendAd(recommendAdResp);
            try {
                if (recommendAdResp.getReturnCode() == 0) {
                    // 获取广告数据
                    adResultList = recommendAdResp.getResult();
                    sliderLayout.removeAllSliders();
                    for(int i=0;i<adResultList.size();i++){
                        DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                        defaultSliderView.image(adResultList.get(i).photo);
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
                    dotContainer.getChildAt(0).setBackgroundResource(R.drawable.icon_dark_dot_selected);

                    contentService.getLivelist(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoginSucess(User user) {
            super.onLoginSucess(user);
        }

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);
        }

    }

   private BaseSliderView.OnSliderClickListener onSliderClickListener= new BaseSliderView.OnSliderClickListener(){
       @Override
       public void onSliderClick(BaseSliderView slider) {
           int position = sliderLayout.getCurrentPosition();
           // 点击跳转
           RecommendAdResultBean recommendAdResultBean = adResultList.get(position);
           String url = recommendAdResultBean.url;
           int targetType = recommendAdResultBean.targetType;
           int targetId = recommendAdResultBean.targetId;
           Intent intent = null;
           switch (targetType) {
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
               case Constants.TARGET_TYPE_PRODUCT:
                   AppLog.i("addd", "产品--" + targetId);
                   // 跳转到商品详情界面
                   SpecialToH5Bean specialToH5Bean = new SpecialToH5Bean();
                   specialToH5Bean.setTargetId(71);

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
               case Constants.TARGET_TYPE_LIVE:
                   // 跳转播放界面 TODO: 暂时不用做
//                                AudienceActivity.start(mContext, liveRowsBean, finalAnn1);
                   break;
           }
       }
   };




    private void hotLiveItemClick() {
        liveMainAdapter.setOnLiveItemClickListener(new LiveMainAdapter.OnLiveItemClickListener() {
            @Override
            public void goLiveRoom(LiveRowsBean liveRowsBean) {

                if(liveRowsBean.getEndAt()!=null&&liveRowsBean.getStartAt()!=null){
                    contentService.getPlayBackLiveDetails(liveRowsBean.getId());
                }else {
                    roomId = liveRowsBean.getRoomId();
                    String createRoom = SPCUtils.getString(getActivity(), CREATE_ROOMID);
                    String s = String.valueOf(roomId);
                    if (createRoom != null && createRoom.equals(s)) {
                        CommonUtil.REMIND_BACK = 1;
                        SPCUtils.put(getActivity(), CREATE_ROOMID, "fdfdad");
                        prepareLive();
                        return;
                    }
                    Object annoucement = liveRowsBean.getAnnoucement();
                    String ann = null;
                    if (annoucement != null) {
                        ann = annoucement.toString();
                    } else {
                        ann = "这是公告哈";
                    }

                    SpecialShareVOBean shareVO = liveRowsBean.getShareVO();
                    AudienceActivity.start(getActivity(),liveRowsBean,ann);
                }
            }
        });


    }


    private int pageCount = 2;
    private boolean isLoading = false;
    private int pageSize = 9;


    public  class  XRecyclerviewLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            isRefresh=true;
            contentService.getLivelist(null);
        }

        @Override
        public void onLoadMore() {
            isRefresh=false;
            if(lastPage){
                xRecyclerView.setNoMore(true);
            }else {
                contentService.getPlayBackLiveList(null,pageNumber);
            }


        }

    }


    boolean isLogining = false;

    private void prepareLive() {
        boolean isLogin = UserHelper.isLogined(getActivity());
        boolean loginStatus = DemoCache.getLoginStatus();
        if (isLogin && loginStatus) {
            contentService.createLiveRoom();//直播接口
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
                Toast.makeText(getActivity(), "没有视频权限，无法开启直播", Toast.LENGTH_SHORT).show();
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


    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart(PAGE_NAME);
        if (CommonUtil.RESULT_DIALOG == 2) {
            CommonUtil.RESULT_DIALOG = 0;
            final CustomChatDialog customDialog = new CustomChatDialog(getActivity());
            customDialog.setContent("摄像头启动失败,请尝试在手机应用权限管理中打开权限");
            customDialog.setCancelable(false);
            customDialog.setOkBtn("确定", new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    customDialog.dismiss();
                }
            });

            customDialog.show();
        } else if (CommonUtil.RESULT_DIALOG == 3) {
            CommonUtil.RESULT_DIALOG = 0;
            final CustomChatDialog customDialog = new CustomChatDialog(getActivity());
            customDialog.setContent("音频权限开启失败,请尝试在手机应用权限管理中打开权限");
            customDialog.setCancelable(false);
            customDialog.setOkBtn("确定", new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    customDialog.dismiss();
                }
            });
            customDialog.show();
        }
        AppLog.i("TAG", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
        AppLog.i("TAG", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!isClick){

            isClick=true;
            showClassifyView(0,isClick);
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
