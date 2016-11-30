package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.easemob.Constant;
import com.lalocal.lalocal.easemob.ui.ChatActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.RouteDetail;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.CustomViewPager;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.lalocal.lalocal.view.adapter.DayRouteApater;
import com.lalocal.lalocal.view.adapter.DayRouteItemAdpater;
import com.lalocal.lalocal.view.listener.OnItemClickListener;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouteDetailActivity extends BaseActivity implements AMap.OnMapLoadedListener, View.OnClickListener, CustomTitleView.onBackBtnClickListener {
    private static final String MINIMAP="com.autonavi.minimap";
    public static final String DETAILS_ID = "detail_id";
    private static final String FORMART_BETWEEN = "Day  %1$s - %2$s";
    RouteDetail mRouteDetail;
    @BindView(R.id.day_item_detail_rlv)
    RecyclerView dayItemDetailRlv;
    @BindView(R.id.day_item_detail_container)
    LinearLayout dayItemDetailContainer;
    @BindView(R.id.day_item_detail_title_container)
    RelativeLayout dayItemDetailTitle;
    @BindView(R.id.day_item_detail_title)
    TextView dayItemDetailName;
    @BindView(R.id.day_item_detail_subtitle)
    TextView dayItemDetailSubtitle;
    @BindView(R.id.day_item_detail_loc_tv)
    TextView dayItemDetailLocTv;
    @BindView(R.id.route_detail_buy_between)
    TextView routeDetailBuyBetween;
    @BindView(R.id.day_item_detail_loc)
    ImageView dayItemDetailLoc;
    @BindView(R.id.day_item_detail_buy_btn)
    Button dayItemDetailBuyBtn;
    private boolean praiseFlag;
    private Object praiseId;
    @BindView(R.id.route_detail_service)
    TextView routeDetailService;
    @BindView(R.id.route_detail_mapview)
    MapView mapView;
    AMap aMap;
    @BindView(R.id.route_detail_routedate_llt)
    LinearLayout routeDetailRoutedateLlt;
    List<LatLng> lats = new ArrayList<>();
    @BindView(R.id.route_detail_hsv)
    HorizontalScrollView routeDetailHsv;
    @BindView(R.id.route_detail_btn_share)
    ImageView routeDetailBtnShare;
    @BindView(R.id.route_detail_collect_sbtn)
    ShineButton likeBtn;
    @BindView(R.id.route_detail_buy)
    FrameLayout routeDetailBuy;
    @BindView(R.id.route_detail_viewpager_route)
    CustomViewPager routeDetailViewpagerRoute;
    int dayItemLeft, firstDayLeft;
    @BindView(R.id.route_detail_day_point)
    ImageView dayPointImg;
    @BindView(R.id.route_detail_title)
    CustomTitleView routeDetailTitle;
    Bundle savedInstanceState;
    DayRouteItemAdpater mItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_detail_layout);
        showLoadingAnimation();
        ButterKnife.bind(this);
        routeDetailTitle.setOnBackClickListener(this);
        this.savedInstanceState = savedInstanceState;
        dayItemLeft = (int) getResources().getDimension(R.dimen.dimen_size_36_dp);
        initLoader();

    }

    private void initViewPager(List<RouteDetail.RouteDatesBean> routeDates) {
        AppLog.print("initViewPager____");
        List<RecyclerView> views = new ArrayList<>();
        for (RouteDetail.RouteDatesBean bean : routeDates) {
            RecyclerView recyclerView = new RecyclerView(this);
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            params.width = ViewPager.LayoutParams.MATCH_PARENT;
            params.height = ViewPager.LayoutParams.MATCH_PARENT;
            recyclerView.setLayoutParams(params);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DayRouteApater apater = new DayRouteApater(this, bean.getRouteItems());
            apater.setOnItemClickListener(dayRouteClickListener);
            recyclerView.setAdapter(apater);
            views.add(recyclerView);
        }
        routeDetailViewpagerRoute.setCallBack(pagerCallBack);
        RoutePagerAdpater pagerAdpater = new RoutePagerAdpater(views);
        routeDetailViewpagerRoute.setAdapter(pagerAdpater);
    }

    @OnClick(R.id.route_detail_service)
    public void chatToLalocalService() {
        // 进入主页面
        MobHelper.sendEevent(this, MobEvent.DESTINATION_ROUTE_SERVICE);
        int messageToIndex = Constant.MESSAGE_TO_DEFAULT;
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(
                Constant.MESSAGE_TO_INTENT_EXTRA, messageToIndex);
        intent.putExtra(Constant.ITEM_POST_URL, mRouteDetail.getPhoto());
        intent.putExtra(Constant.ITEM_TITLE, mRouteDetail.getTitle());
        startActivity(intent);
    }

    @OnClick(R.id.day_item_detail_title_container)
    public void closeDayItemDetail() {
        dayItemDetailContainer.setVisibility(View.INVISIBLE);
    }

    //判断是否安装目标应用
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName)
                .exists();
    }

    @OnClick(R.id.day_item_detail_loc)
    public void planFromMap() {
        Object tagObj = dayItemDetailLoc.getTag();
        if (tagObj != null) {
            if (isInstallByread(MINIMAP)) {
                RouteDetail.RouteDatesBean.RouteItemsBean routeItemsBean = (RouteDetail.RouteDatesBean.RouteItemsBean) tagObj;
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("androidamap://navi?sourceApplication=" + getResources().getString(R.string.app_name) + "&poiname=" + routeItemsBean.getTitle() + "&lat=" + routeItemsBean.getLatitude() + "&lon=" + routeItemsBean.getLongitude() + "&dev=1&style=2"));
                intent.setPackage(MINIMAP);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "目标地址不存在", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick({R.id.day_item_detail_buy_btn, R.id.route_detail_buy})
    public void openProductDetail(View view) {
        MobHelper.sendEevent(this, MobEvent.DESTINATION_ROUTE_BUY);
        Object tag = view.getTag();
        if (tag != null) {

            Intent intent = new Intent(this, ProductDetailsActivity.class);
            SpecialToH5Bean bean = new SpecialToH5Bean();
            if (tag instanceof RouteDetail.RouteDatesBean.RouteItemsBean) {

                bean.setTargetId(Integer.parseInt(((RouteDetail.RouteDatesBean.RouteItemsBean) tag).getProduId()));

            } else if (tag instanceof RouteDetail.ComboProduListBean) {
                bean.setTargetId(((RouteDetail.ComboProduListBean) tag).getProduId());
            } else {
                return;
            }
            intent.putExtra("productdetails", bean);
            startActivity(intent);
        }


    }

    private OnItemClickListener dayRouteClickListener = new OnItemClickListener() {
        @Override
        public void onItemClickListener(View view, int position) {
            dayItemDetailContainer.setVisibility(View.VISIBLE);
            RouteDetail.RouteDatesBean.RouteItemsBean routeItem = (RouteDetail.RouteDatesBean.RouteItemsBean) view.getTag();
            if (routeItem != null) {
                if (!TextUtils.isEmpty(routeItem.getProduId())) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dayItemDetailRlv.getLayoutParams();
                    params.bottomMargin = (int) getResources().getDimension(R.dimen.dimen_size_60_dp);
                    if (dayItemDetailBuyBtn.getVisibility() != View.VISIBLE) {
                        dayItemDetailBuyBtn.setVisibility(View.VISIBLE);
                    }
                    dayItemDetailBuyBtn.setTag(routeItem);
                } else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dayItemDetailRlv.getLayoutParams();
                    params.bottomMargin = 0;
                    if (dayItemDetailBuyBtn.getVisibility() == View.VISIBLE) {
                        dayItemDetailBuyBtn.setVisibility(View.GONE);
                    }
                }
                dayItemDetailLocTv.setText(String.valueOf(position + 1));
                dayItemDetailName.setText(routeItem.getTitle());
                dayItemDetailLoc.setTag(routeItem);
                dayItemDetailSubtitle.setText(routeItem.getSubTitle());
                SparseArray<String> items = new SparseArray<>();
                String ticket = routeItem.getTicket();
                String openTime = routeItem.getOpenTime();
                String duration = routeItem.getDuration();
                String intro = routeItem.getIntroduction();
                String trafic = routeItem.getTraffic();
                String address = routeItem.getAddress();
                if (!TextUtils.isEmpty(ticket)) {
                    items.put(DayRouteItemAdpater.TICKET, ticket);
                }
                if (!TextUtils.isEmpty(openTime)) {
                    items.put(DayRouteItemAdpater.OPEN_TIME, openTime);
                }
                if (!TextUtils.isEmpty(duration)) {
                    items.put(DayRouteItemAdpater.DURATION, duration);
                }
                if (!TextUtils.isEmpty(intro)) {
                    items.put(DayRouteItemAdpater.INTRODUCTION, intro);

                }
                if (!TextUtils.isEmpty(trafic)) {

                    items.put(DayRouteItemAdpater.TRAFFIC, trafic);
                }
                if (!TextUtils.isEmpty(address)) {
                    items.put(DayRouteItemAdpater.ADDRESS, address);
                }
                if (mItemAdapter == null) {
                    mItemAdapter = new DayRouteItemAdpater(RouteDetailActivity.this, items);
                    dayItemDetailRlv.setLayoutManager(new LinearLayoutManager(RouteDetailActivity.this));
                    dayItemDetailRlv.setAdapter(mItemAdapter);
                } else {
                    mItemAdapter.updateItems(items);
                }

            }


        }
    };


    CustomViewPager.PagerCallBack pagerCallBack = new CustomViewPager.PagerCallBack() {
        private boolean mLeft, mRight;

        @Override
        public void getCurrentPosition(int pos) {
            int selectedId = pos + 1;
            startScorll(pos);
            setSelectItem(routeDetailRoutedateLlt, selectedId);
        }

        private void startScorll(int pos) {
            int count = getChildSize(routeDetailRoutedateLlt);
            if (mLeft) {
                int index = pos + 4;
                if (index < count) {
                    scroll(index);
                }
            } else if (mRight) {
                int index = pos - 4;
                if (index >= 0) {
                    scroll(index);
                }

            }
        }

        private void scroll(int index) {
            View v = getChildView(routeDetailRoutedateLlt, index);
            if (v != null) {
                int distacne = v.getMeasuredWidth() + dayItemLeft;
                int x = mLeft ? 0 - distacne : distacne;
                routeDetailHsv.scrollBy(x, 0);
            }
        }

        @Override
        public void changeView(boolean left, boolean right) {
            mLeft = left;
            mRight = right;
        }

        public View getChildView(ViewGroup container, int pos) {
            for (int i = 0; i < container.getChildCount(); i++) {
                View view = container.getChildAt(i);
                if (view instanceof ViewGroup) {
                    return getChildView((ViewGroup) view, pos);
                } else {
                    if (view.getId() == (pos + 1)) {
                        return view;
                    }
                }

            }
            return null;
        }

        public int getChildSize(ViewGroup contianer) {
            int len = 0;
            for (int i = 0; i < contianer.getChildCount(); i++) {
                View view = contianer.getChildAt(i);
                if (view instanceof ViewGroup) {
                    len += ((ViewGroup) view).getChildCount();
                } else {
                    len += 1;
                }
            }
            return len;
        }


    };


    private void initLoader() {
        AppLog.print("initLoader____");
        setLoaderCallBack(new RouteDetailCallBack());
        mContentloader.getRouteDetails(getDetailId());
    }

    private int getDetailId() {

        return getIntent().getIntExtra(DETAILS_ID, -1);

    }

    //显示分享图标页面
    @OnClick(R.id.route_detail_btn_share)
    public void showShare() {
        if (mRouteDetail == null) {
            Toast.makeText(this, "没有可分享的内容", Toast.LENGTH_SHORT).show();
            return;
        }
        SpecialShareVOBean shareVO = new SpecialShareVOBean();
        RouteDetail.ShareVOBean routeShareVO = mRouteDetail.getShareVO();
        shareVO.setType(routeShareVO.getType());
        shareVO.setImg(routeShareVO.getImg());
        shareVO.setDesc(routeShareVO.getDesc());
        shareVO.setTitle(routeShareVO.getTitle());
        shareVO.setUrl(routeShareVO.getUrl());
        SharePopupWindow sharePopupWindow = new SharePopupWindow(this, shareVO);
        sharePopupWindow.showShareWindow();
        sharePopupWindow.showAtLocation(getWindow().getDecorView(),
                Gravity.BOTTOM, 0, 0);

    }

    @OnClick(R.id.route_detail_collect_sbtn)
    public void collect() {
        MobHelper.sendEevent(this, MobEvent.DESTINATION_ROUTE_LIKE);
        if (mRouteDetail != null) {
            //取消收藏
            int id = mRouteDetail.getId();
            if (praiseFlag) {
                mContentloader.cancelParises(praiseId, id);
            } else {//添加收藏
                mContentloader.specialPraise(id, 2);//点赞
            }
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        setSelectItem(routeDetailRoutedateLlt, id);
        routeDetailViewpagerRoute.setCurrentItem(id - 1);
    }


    public void setSelectItem(ViewGroup viewGroup, int selectedId) {
        AppLog.print("setSelectItem___start_");
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                setSelectItem((ViewGroup) view, selectedId);
            } else {
                if (view.getId() == selectedId) {
                    if (!view.isSelected()) {
                        if (dayPointImg.getVisibility() != View.VISIBLE) {
                            dayPointImg.setVisibility(View.VISIBLE);
                        }
                        int width = view.getMeasuredWidth() / 2;
                        int left = view.getLeft();
                        RelativeLayout.LayoutParams pointParams = (RelativeLayout.LayoutParams) dayPointImg.getLayoutParams();
                        pointParams.leftMargin = pointParams.leftMargin == 0 ? firstDayLeft : left + width;
                        AppLog.print("day left margin__" + pointParams.leftMargin + "__width___" + width);
                        dayPointImg.setLayoutParams(pointParams);
                        view.setSelected(true);
                        List<RouteDetail.RouteDatesBean> dates = mRouteDetail.getRouteDates();
                        if (dates != null && dates.size() > 0) {
                            RouteDetail.RouteDatesBean date = dates.get(i);
                            if (date != null) {
                                updateMap(date.getRouteItems());
                                moveCamera();
                            }
                        }

                    }
                } else {
                    view.setSelected(false);
                }
            }
        }
    }

    @Override
    public void onBackClick() {
        setResult(MyFavoriteActivity.UPDATE_MY_DATA);
    }

    @Override
    public void onBackPressed() {
        setResult(MyFavoriteActivity.UPDATE_MY_DATA);
        super.onBackPressed();
    }

    class RouteDetailCallBack extends ICallBack {

        @Override
        public void onGetRouteDetail(RouteDetail routeDetail) {
            AppLog.print("onGetRouteDetail______");
            hidenLoadingAnimation();
            mRouteDetail = routeDetail;
            praiseId = routeDetail.getPraiseId();
            praiseFlag = routeDetail.isPraiseFlag();
            if (praiseFlag) {
                likeBtn.setChecked(true);
            } else {
                likeBtn.setChecked(false);

            }
            updateDateListView(routeDetail);
            initViewPager(routeDetail.getRouteDates());
            setSelectItem(routeDetailRoutedateLlt, 1);
        }

        @Override
        public void onPariseResult(PariseResult pariseResult) {
            if (pariseResult != null && pariseResult.getReturnCode() == 0) {
                likeBtn.setChecked(false);
                praiseFlag = false;
            }
        }

        @Override
        public void onInputPariseResult(PariseResult pariseResult) {
            if (pariseResult.getReturnCode() == 0) {

                likeBtn.setChecked(true);
                praiseId = pariseResult.getResult();
                praiseFlag = true;
            }
        }
    }

    private void updateDateListView(RouteDetail routeDetai) {
        AppLog.print("updateDateListView___");
        List<RouteDetail.ComboProduListBean> comboProdus = routeDetai.getComboProduList();
        List<RouteDetail.RouteDatesBean> routeDates = routeDetai.getRouteDates();
        List<Integer> dates = null;
        LinearLayout comboContainer = null;
        if (comboProdus != null && comboProdus.size() > 0) {
            RouteDetail.ComboProduListBean comboProdu = comboProdus.get(0);
            if (comboProdu != null) {
                routeDetailBuy.setTag(comboProdu);
                dates = comboProdu.getDateList();
                if (dates != null && dates.size() > 0) {
                    comboContainer = new LinearLayout(this);
                    comboContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    comboContainer.setBackground(getResources().getDrawable(R.drawable.route_detail_comb_llt_bg));
                    comboContainer.setOrientation(LinearLayout.HORIZONTAL);
                    comboContainer.setGravity(Gravity.CENTER_VERTICAL);
                    int left = (int) getResources().getDimension(R.dimen.dimen_size_20_dp);
                    comboContainer.setPadding(left, 0, left, 0);
                    routeDetailRoutedateLlt.addView(comboContainer);
                }
            }
        }
        int firstId = 0;
        out1:
        for (int i = 0; i < routeDates.size(); i++) {
            RouteDetail.RouteDatesBean routeDate = routeDates.get(i);
            int dateId = routeDate.getId();
            if (i == 0) {
                firstId = dateId;
            }
            if (dates == null || dates.size() < 1 || !dates.contains(dateId)) {
                //无组合订单
                routeDetailRoutedateLlt.addView(getDateItem(i + 1, false, routeDate));
                continue;
            }
            for (Integer id : dates) {
                if (id == routeDate.getId()) {
                    int pos = dates.indexOf(id);
                    if (pos == 0) {
                        if (routeDetailBuy.getVisibility() != View.VISIBLE) {
                            routeDetailBuy.setVisibility(View.VISIBLE);
                        }
                        routeDetailBuyBetween.setText(String.format(FORMART_BETWEEN, i + 1, i + dates.size()));
                    }
                    if (comboContainer != null) {
                        comboContainer.addView(getDateItem(i + 1, true, routeDate));
                        continue out1;
                    }
                }

            }


        }
        if (dates != null && dates.contains(firstId)) {
            firstDayLeft = (int) getResources().getDimension(R.dimen.first_day_left1);
        } else {
            firstDayLeft = (int) getResources().getDimension(R.dimen.first_day_left2);
        }
    }


    public TextView getDateItem(int dateNum, boolean isFlag, RouteDetail.RouteDatesBean routeDate) {
        TextView view = new TextView(this);
        ColorStateList stateList = getResources().getColorStateList(R.color.date_item_textcolor);
        view.setTextColor(stateList);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_22_sp));
        SpannableString spannableStr = new SpannableString("Day " + dateNum);
        spannableStr.setSpan(new AbsoluteSizeSpan(12, true), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStr.setSpan(new AbsoluteSizeSpan(21, true), 3, spannableStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannableStr);
        view.setOnClickListener(this);
        view.setTag(R.id.routeDateItem, routeDate);
        view.setId(dateNum);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dateNum == 1 && isFlag) {
            params.leftMargin = 0;
            updateMap(routeDate.getRouteItems());
        } else {
            params.leftMargin = dayItemLeft;
        }
        view.setLayoutParams(params);
        return view;
    }


    private void updateMap(List<RouteDetail.RouteDatesBean.RouteItemsBean> routeItems) {
        if (aMap == null) {
            mapView.onCreate(savedInstanceState);
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        }
        if (lats.size() > 0) {
            lats.clear();
            aMap.clear();
        }
        for (RouteDetail.RouteDatesBean.RouteItemsBean item : routeItems) {
            MarkerOptions options = new MarkerOptions();
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            int res = R.drawable.index_article_tipes_icon_location_2;
            int pos = routeItems.indexOf(item);
            if (pos == 0) {
                textView.setPadding(0, 4, 0, 0);
                textView.setTextColor(getResources().getColor(R.color.color_ffaa2a));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_13_sp));
                res = R.drawable.index_article_tipes_icon_location_1;
            } else {
                textView.setTextColor(getResources().getColor(R.color.color_66));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_15_sp));
            }
            textView.setText(String.valueOf(pos + 1));
            textView.setBackground(getResources().getDrawable(res));
            options.icon(BitmapDescriptorFactory.fromView(textView));

            LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
            lats.add(latLng);
            options.position(latLng);
            aMap.addMarker(options);
        }
        AppLog.print("updateMap_____end__");

    }


    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
        moveCamera();

    }

    private void moveCamera() {
        LatLngBounds.Builder bulder = new LatLngBounds.Builder();
        for (LatLng latLng : lats) {
            bulder.include(latLng);
        }
        LatLngBounds bounds = bulder.build();
        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        if (mapView != null) {
            mapView.onResume();
        }
        super.onResume();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    class RoutePagerAdpater extends PagerAdapter {
        List<RecyclerView> mViews;

        public RoutePagerAdpater(List<RecyclerView> views) {
            mViews = views;
            AppLog.print("RoutePagerAdpater___");
        }

        @Override
        public int getCount() {
            return mViews != null && mViews.size() > 0 ? mViews.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            AppLog.print("destroyItem____");
            container.removeView(mViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            AppLog.print("instantiateItem____");
            RecyclerView view = mViews.get(position);
            container.addView(view);
            return view;
        }
    }

}
