package com.lalocal.lalocal.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.AccountEidt1Activity;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.EmptActivity;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.activity.MyWalletActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.activity.RouteDetailActivity;
import com.lalocal.lalocal.activity.SettingActivity;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.help.ErrorMessage;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.LiveAttentionOrFansActivity;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTabLayout;
import com.lalocal.lalocal.view.adapter.MyCouponAdapter;
import com.lalocal.lalocal.view.adapter.MyFavoriteAdapter;
import com.lalocal.lalocal.view.adapter.MyOrderAdapter;
import com.lalocal.lalocal.view.xlistview.XListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2016/6/3.
 * note: isRefresh更新
 */
public class MeFragment extends Fragment implements XListView.IXListViewListener {
    public static final String ACTION_UPDATE_DATA = "action_update_data";
    private static final String PAGE_NAME = "MeFragment";
    public static final int UPDATE_MY_DATA = 0x12;
    public static final int UPDATE_MY_ORDER = 0x13;
    public static final String USER = "user";
    public static final String LOGIN_STATUS = "loginstatus";
    TextView username_tv, verified_tv;
    TextView favoriteNum_tv, orderNum_tv, couponNum_tv, fansNum_tv, followNum_tv;
    ImageView headImg;
    CustomTabLayout favorite_tab, order_tab, coupon_tab, fans_tab, follow_tab,wallet_tab;
    ViewGroup lastSelectedView;
    ImageButton settingBtn;
    ContentLoader contentService;
    public boolean isRefresh, isImLogin, isDownRefresh;
    public boolean isFirstFavorite = true, isFristOrder = true, isFirstCoupon = true;
    int favoriteTotalPages, favoritePage = 1;
    int defaultPageNumb = 1, defaultPageSize = 10;
    User user;
    XListView mListView;
    LinearLayout fansContainer;
    MyFavoriteAdapter favoriteAdapter, emptfAdpater;
    MyCouponAdapter couponAdapter, emptcAdpater;
    MyOrderAdapter orderAdapter, emptoAdpater;
    List<FavoriteItem> allItems = new ArrayList<>();
    OnMeFragmentListener fragmentListener;
    Intent imLoginData;
    int mUserid = -1;
    String mToken;
    List<String> lastFavorites = new ArrayList<>();
    List<String> lastOrders = new ArrayList<>();
    List<String> lastCopons = new ArrayList<>();
    UpdateReciver updateReciver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(ACTION_UPDATE_DATA);
        if (updateReciver == null) {
            updateReciver = new UpdateReciver();
        }
        getActivity().registerReceiver(updateReciver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateReciver != null) {
            getActivity().unregisterReceiver(updateReciver);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AppLog.print("onAttach(Activity)___");
        fragmentListener = (OnMeFragmentListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment_layout, container, false);
        mListView = (XListView) view.findViewById(R.id.home_me_xlistview);
//        mListView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(xlvItemClicklistener);
        View headerView = inflater.inflate(R.layout.home_me_layout, null);
        mListView.addHeaderView(headerView);
        initView(headerView);
        initAdapter();
        initContentService();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }

    private void initView(View view) {
        fansContainer = (LinearLayout) view.findViewById(R.id.home_me_fans_container);
        settingBtn = (ImageButton) view.findViewById(R.id.home_me_set_btn);
        headImg = (ImageView) view.findViewById(R.id.home_me_headportrait_img);
        verified_tv = (TextView) view.findViewById(R.id.home_me_verified);
        username_tv = (TextView) view.findViewById(R.id.home_me_username);
        wallet_tab= (CustomTabLayout) view.findViewById(R.id.home_me_wallet_tab);
        favorite_tab = (CustomTabLayout) view.findViewById(R.id.home_me_favorite_tab);
        order_tab = (CustomTabLayout) view.findViewById(R.id.home_me_order_tab);
        coupon_tab = (CustomTabLayout) view.findViewById(R.id.home_me_coupon_tab);
        fans_tab = (CustomTabLayout) view.findViewById(R.id.home_me_favorite_fans_tab);
        follow_tab = (CustomTabLayout) view.findViewById(R.id.home_me_favorite_follow_tab);
        fansNum_tv = (TextView) view.findViewById(R.id.home_me_fans_num);
        followNum_tv = (TextView) view.findViewById(R.id.home_me_follow_num);
        favoriteNum_tv = (TextView) view.findViewById(R.id.home_me_favorite_num);
        orderNum_tv = (TextView) view.findViewById(R.id.home_me_order_num);
        couponNum_tv = (TextView) view.findViewById(R.id.home_me_coupons_num);
        verified_tv.setOnClickListener(meFragmentClickListener);
        settingBtn.setOnClickListener(meFragmentClickListener);
        headImg.setOnClickListener(meFragmentClickListener);
        username_tv.setOnClickListener(meFragmentClickListener);
        favorite_tab.setOnClickListener(meFragmentClickListener);
        order_tab.setOnClickListener(meFragmentClickListener);
        coupon_tab.setOnClickListener(meFragmentClickListener);
        fans_tab.setOnClickListener(meFragmentClickListener);
        follow_tab.setOnClickListener(meFragmentClickListener);
        wallet_tab.setOnClickListener(meFragmentClickListener);
        setSelectedTab(favorite_tab);
    }


    private void initAdapter() {
        emptcAdpater = new MyCouponAdapter(getActivity(), null, this);
        emptoAdpater = new MyOrderAdapter(getActivity(), this, null, null);
        emptfAdpater = new MyFavoriteAdapter(getActivity(), null);
        mListView.setAdapter(null);
    }


    private void initContentService() {
        AppLog.print("initContentService___");
        lastOrders.addAll(UserHelper.orders);
        lastCopons.addAll(UserHelper.coupons);
        contentService = new ContentLoader(getActivity());
        contentService.setCallBack(new MeCallBack());
        emptoAdpater.setLoader(contentService);
        if (orderAdapter != null) {
            orderAdapter.setLoader(contentService);
        }
        if (UserHelper.isLogined(getActivity())) {
            //恢复上一次登录的状态
            String email = UserHelper.getUserEmail(getActivity());
            String psw = UserHelper.getPassword(getActivity());
            contentService.login(email, psw);
        } else {
            isFirstFavorite = false;
            contentService.getMyFavorite(mUserid, mToken, defaultPageNumb, defaultPageSize);
        }
    }

    //从其他页面切换到我的页面
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppLog.print("onHiddenChanged____" + hidden);
        if (isImLogin) {
            //立即登录
            isImLogin = false;
            if (imLoginData != null) {
                User user = imLoginData.getParcelableExtra(USER);
                if (user != null) {
                    updateFragmentView(true, user);
                } else {
                    String email = imLoginData.getStringExtra(LoginActivity.EMAIL);
                    String psw = imLoginData.getStringExtra(LoginActivity.PSW);
                    contentService.login(email, psw);
                }
            }
        } else {
            //正常登录方式  刷新邮箱验证状态 刷新我的收藏状态(我的收藏没被选中时更新我的收藏适配器)
            if (!hidden) {
                AppLog.print("onHiddenChanged__showFrament____");
                if (UserHelper.isLogined(getActivity())) {
                    mUserid = UserHelper.getUserId(getActivity());
                    mToken = UserHelper.getToken(getActivity());
                    contentService.getUserProfile(mUserid, mToken);
//                    && !lastOrders.equals(UserHelper.orders)
                    if (order_tab.isSelected()) {
                        contentService.getMyOrder(mUserid, mToken);
                    }
                    if (coupon_tab.isSelected() && !lastCopons.equals(UserHelper.coupons)) {
                        contentService.getMyCoupon(mUserid, mToken);
                    }
                } else {
                    mUserid = -1;
                    mToken = null;
                }
                if (favorite_tab.isSelected() && !lastFavorites.equals(UserHelper.favorites)) {
                    contentService.getMyFavorite(mUserid, mToken, defaultPageNumb, defaultPageSize);
                }
            }
        }


    }

    private View.OnClickListener meFragmentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.home_me_wallet_tab:
                    Intent walletIntent=new Intent(getActivity(), MyWalletActivity.class);
                    startActivity(walletIntent);
                    break;
                case R.id.home_me_favorite_fans_tab:
                    Intent fansIntent = new Intent(getActivity(), LiveAttentionOrFansActivity.class);
                    fansIntent.putExtra("liveType", "1");
                    fansIntent.putExtra("userId", String.valueOf(UserHelper.getUserId(getActivity())));
                    startActivity(fansIntent);
                    break;
                case R.id.home_me_favorite_follow_tab:
                    Intent followIntent = new Intent(getActivity(), LiveAttentionOrFansActivity.class);
                    followIntent.putExtra("liveType", "0");
                    followIntent.putExtra("userId", String.valueOf(UserHelper.getUserId(getActivity())));
                    startActivity(followIntent);
                    break;
                case R.id.home_me_favorite_tab:
                    if (v.isSelected()) {
                        return;
                    }
                    setSelectedTab((ViewGroup) v);
                    mListView.setPullLoadEnable(true);
                    mListView.setAdapter(favoriteAdapter);
                    if (UserHelper.isLogined(getActivity())) {
                        mUserid = UserHelper.getUserId(getActivity());
                        mToken = UserHelper.getToken(getActivity());
                    } else {
                        mUserid = -1;
                        mToken = null;
                    }
                    if (isFirstFavorite) {
                        isFirstFavorite = false;
                        contentService.getMyFavorite(mUserid, mToken, defaultPageNumb, defaultPageSize);
                    } else {
                        if (!lastFavorites.equals(UserHelper.favorites)) {
                            contentService.getMyFavorite(mUserid, mToken, defaultPageNumb, defaultPageSize);
                        }
                    }
                    break;
                case R.id.home_me_order_tab:
                    //TODO:需调整
                    if (v.isSelected()) {
                        return;
                    }
                    setSelectedTab((ViewGroup) v);
                    mListView.setPullLoadEnable(false);
                    mListView.setAdapter(orderAdapter);
                    if (UserHelper.isLogined(getActivity())) {
//                        if (isFristOrder) {
//                            isFristOrder = false;
                        contentService.getMyOrder(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
//                        } else {
//                            if (!lastOrders.equals(UserHelper.orders)) {
//                                contentService.getMyOrder(UserHelper.getUserId(getActivity()),UserHelper.getToken(getActivity()));
//                            }
//                        }

                    } else {
                        orderAdapter = emptoAdpater;
                        orderAdapter.updateListView(null);
                        mListView.setAdapter(orderAdapter);
                    }
                    break;
                case R.id.home_me_coupon_tab:
                    //TODO:需调整
                    if (v.isSelected()) {
                        return;
                    }
                    setSelectedTab((ViewGroup) v);
                    mListView.setPullLoadEnable(false);
                    mListView.setAdapter(couponAdapter);
                    if (UserHelper.isLogined(getActivity())) {
                        if (isFirstCoupon) {
                            isFirstCoupon = false;
                            contentService.getMyCoupon(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
                        } else {
                            if (!lastCopons.equals(UserHelper.coupons)) {
                                contentService.getMyCoupon(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
                            }
                        }
                    } else {
                        couponAdapter = emptcAdpater;
                        couponAdapter.updateItems(null);
                        mListView.setAdapter(couponAdapter);
                    }
                    break;
                case R.id.home_me_headportrait_img:
                case R.id.home_me_username:
                    if (UserHelper.isLogined(getActivity())) {
                        Intent intent = new Intent(getActivity(), AccountEidt1Activity.class);
                        intent.putExtra(KeyParams.USERID, UserHelper.getUserId(getActivity()));
                        intent.putExtra(KeyParams.TOKEN, UserHelper.getToken(getActivity()));
                        startActivityForResult(intent, 100);
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivityForResult(intent, 100);
                    }

                    break;
                case R.id.home_me_set_btn:
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    startActivityForResult(intent, 101);
                    break;

            }
        }
    };


    private void setSelectedTab(ViewGroup v) {
        if (lastSelectedView != null) {
            setContentTabSelected(lastSelectedView, false);
        }
        ViewGroup viewGroup = v;
        setContentTabSelected(viewGroup, true);
        lastSelectedView = viewGroup;
    }

    public void setContentTabSelected(ViewGroup container, boolean isSelected) {
        container.setSelected(isSelected);
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            view.setSelected(isSelected);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppLog.print("onActivityResult  resCode___" + resultCode);
        if (resultCode == LoginActivity.REGISTER_OK) {
            String email = data.getStringExtra(LoginActivity.EMAIL);
            String psw = data.getStringExtra(LoginActivity.PSW);
            contentService.login(email, psw);
        } else if (resultCode == LoginActivity.LOGIN_OK) {
            User user = data.getParcelableExtra(USER);
            updateFragmentView(true, user);
        } else if (resultCode == SettingActivity.UN_LOGIN_OK) {
            signOut();
        } else if (resultCode == AccountEidt1Activity.UPDATE_ME_DATA) {
            String nickname = data.getStringExtra(KeyParams.NICKNAME);
            String avatar = data.getStringExtra(KeyParams.AVATAR);
            if (user != null) {
                user.setNickName(nickname);
                user.setAvatar(avatar);
//                updateFragmentView(UserHelper.isLogined(getActivity()), user);
                contentService.getUserProfile(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
            }
        } else if (resultCode == SettingActivity.IM_LOGIN) {
            isImLogin = true;
            imLoginData = data;
            if (fragmentListener != null) {
                fragmentListener.onShowRecommendFragment();
            }
            String ccid = imLoginData.getStringExtra(KeyParams.IM_CCID);
            String imtken = imLoginData.getStringExtra(KeyParams.IM_TOKEN);
            AppLog.i("TAG", "ccid:" + ccid + "token:" + imtken);
        } else if (resultCode == UPDATE_MY_DATA) {
            if (UserHelper.isLogined(getActivity()) && user != null) {
                mUserid = user.getId();
                mToken = user.getToken();
                if (coupon_tab.isSelected() && !lastCopons.equals(UserHelper.coupons)) {
                    contentService.getMyCoupon(mUserid, mToken);
                }
                if (order_tab.isSelected() && !lastOrders.equals(UserHelper.orders)) {
                    contentService.getMyOrder(mUserid, mToken);
                }
            } else {
                mUserid = -1;
                mToken = null;
            }
            contentService.getMyFavorite(mUserid, mToken, defaultPageNumb, defaultPageSize);
        } else if (resultCode == UPDATE_MY_ORDER) {
            AppLog.print("更新订单————————" + UPDATE_MY_ORDER);
            contentService.getMyOrder(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
        }
    }

    private void signOut() {
        UserHelper.updateSignOutInfo(getActivity());
        updateFragmentView(false, null);
    }

    private void updateFragmentView(boolean isLogined, User user) {
        this.user = user;
        if (isLogined && user != null) {
            AppLog.print("账号登录————————");
            if (fansContainer.getVisibility() != View.VISIBLE) {
                fansContainer.setVisibility(View.VISIBLE);
            }
//            mToken = user.getToken();
//            mUserid = user.getId();
            mToken = UserHelper.getToken(getActivity());
            mUserid = UserHelper.getUserId(getActivity());
            String nickname = user.getNickName();
            if (!TextUtils.isEmpty(nickname)) {
                username_tv.setActivated(true);
                username_tv.setText(nickname);
            }
            if (verified_tv.getVisibility() != View.VISIBLE) {
                verified_tv.setVisibility(View.VISIBLE);
            }
            if (user.getStatus() == 0) {
                verified_tv.setActivated(true);
                verified_tv.setText(getResources().getString(R.string.verified));
            } else {
                verified_tv.setActivated(false);
                verified_tv.setText(getResources().getString(R.string.no_verified));
            }
            String avatar = user.getAvatar();
            if (!TextUtils.isEmpty(avatar)) {
                DrawableUtils.displayImg(getActivity(), headImg, avatar);

            }
            contentService.getLiveUserInfo(String.valueOf(mUserid));
            contentService.getMyOrder(mUserid, mToken);
            contentService.getMyCoupon(mUserid, mToken);

        } else {
            AppLog.print("账号退出————————");
            if (fansContainer.getVisibility() == View.VISIBLE) {
                fansContainer.setVisibility(View.INVISIBLE);
            }
            username_tv.setActivated(false);
            username_tv.setText(getResources().getString(R.string.please_login));
            verified_tv.setVisibility(View.GONE);
            headImg.setImageResource(R.drawable.home_me_personheadnormal);
            orderNum_tv.setText("0");
            couponNum_tv.setText("0");
            mUserid = -1;
            mToken = null;
            couponAdapter = emptcAdpater;
            orderAdapter = emptoAdpater;
            if (order_tab.isSelected()) {
                orderAdapter.updateListView(null);
                mListView.setAdapter(orderAdapter);
            }
            if (coupon_tab.isSelected()) {
                couponAdapter.updateItems(null);
                mListView.setAdapter(couponAdapter);
            }
        }
        UserHelper.favorites.clear();
        lastFavorites.clear();
        if (favorite_tab.isSelected()) {
            isFirstFavorite = false;
            contentService.getMyFavorite(mUserid, mToken, defaultPageNumb, defaultPageSize);
        } else {
            isFirstFavorite = true;
        }


    }


    @Override
    public void onRefresh() {
        isRefresh = false;
        isDownRefresh = true;
        if (UserHelper.isLogined(getActivity())) {
            contentService.getUserProfile(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
        } else {
            isDownRefresh = false;
            mListView.stopRefresh();
        }

    }

    @Override
    public void onLoadMore() {
        isRefresh = true;
        favoritePage = favoritePage + 1;
        AppLog.print("favoriete___" + favoritePage + ", toalPgae___" + favoriteTotalPages);
        if (favoritePage <= favoriteTotalPages) {
            AppLog.print("加载更多——————");
            if (UserHelper.isLogined(getActivity())) {
                mUserid = UserHelper.getUserId(getActivity());
                mToken = UserHelper.getToken(getActivity());
            } else {
                mUserid = -1;
                mToken = null;
            }
            contentService.getMyFavorite(mUserid, mToken, favoritePage, defaultPageSize);

        } else {
            AppLog.print("没有更多了——————");
            isRefresh = false;
//            CommonUtil.showToast(getActivity(), "没有更多数据", Toast.LENGTH_SHORT);
            if (!allItems.contains(null)) {
                mListView.closeLoadMore();
                allItems.add(null);
                favoriteAdapter.updateListView(allItems);
            }

        }
    }


    private void gotoProductDetail(FavoriteItem item) {
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        SpecialToH5Bean bean = new SpecialToH5Bean();
        bean.setTargetId(item.getTargetId());
        bean.setTargetType(item.getTargetType());
        intent.putExtra("productdetails", bean);
        startActivityForResult(intent, 100);
    }

    public void gotoSpecialDetail(FavoriteItem item) {
        Intent intent = new Intent(getActivity(), SpecialDetailsActivity.class);
        intent.putExtra("rowId", String.valueOf(item.getTargetId()));
        startActivityForResult(intent, 100);
    }

    private void gotoArticleDetail(FavoriteItem item) {
        AppLog.print("targetId___" + item.getTargetId());
        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        intent.putExtra("targetID", String.valueOf(item.getTargetId()));
        startActivityForResult(intent, 100);
    }

    private void gotoRouteDetail(FavoriteItem item) {
        Intent intent = new Intent(getActivity(), RouteDetailActivity.class);
        intent.putExtra(RouteDetailActivity.DETAILS_ID, item.getTargetId());
        startActivityForResult(intent, 100);
    }

    private void gotoEmptDetail() {
        Intent intent = new Intent(getActivity(), EmptActivity.class);
        startActivity(intent);
    }

    class MeCallBack extends ICallBack {
        @Override
        public void onCancelSuccess() {
            AppLog.print("MeCallBack onCancelSuccess____");
            contentService.getMyOrder(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
        }

        @Override
        public void onResponseFailed() {
            isRefresh = false;
            if (isDownRefresh) {
                isDownRefresh = false;
                mListView.stopRefresh();
            }
        }

        @Override
        public void onError(VolleyError volleyError) {
            isRefresh = false;
            if (isDownRefresh) {
                isDownRefresh = false;
                mListView.stopRefresh();
                if (ErrorMessage.AUTHOR_FIALED.equals(volleyError.toString())) {
                    signOut();
                }
            }

        }


        @Override
        public void onLoginSucess(User user) {
            updateFragmentView(true, user);
        }

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            LiveUserInfoResultBean result = liveUserInfosDataResp.getResult();
            followNum_tv.setText(String.valueOf(result.getAttentionNum()));
            fansNum_tv.setText(String.valueOf(result.getFansNum()));
        }

        @Override
        public void onGetFavoriteItem(List<FavoriteItem> items, int pageNumber, int totalPages, int totalRows) {
            favoriteTotalPages = totalPages;
            favoritePage = pageNumber;
            AppLog.print("onGetFavoriteItem pageNumber__" + pageNumber + ", toalpage__" + totalPages);
            favoriteNum_tv.setText(String.valueOf(totalRows));
            updateFavorite(items, pageNumber);
        }

        @Override
        public void onGetCounponItem(List<Coupon> items) {
            couponNum_tv.setText(String.valueOf(items.size()));
            if (coupon_tab.isSelected()) {
                setCouponAdapter(items);
            }
        }


        @Override
        public void onGetOrderItem(List<OrderItem> items) {
            orderNum_tv.setText(String.valueOf(items.size()));
            if (order_tab.isSelected()) {
                setOrderAdpater(items);
            }
        }


        private void updateFavorite(List<FavoriteItem> items, int pageNumber) {
            AppLog.print("totalPages___" + favoriteTotalPages + ", items size__" + items.size());
            saveFavoriteHistory(items);
            if (!isRefresh) {
                allItems.clear();
            } else {
                isRefresh = false;
            }
            if (pageNumber <= 1) {
                allItems.clear();
            }
            int len = allItems.size();
            if (len == 0) {
                favoritePage = 1;
                allItems.addAll(items);
                updateFavoriteAdapter();
            } else {
                //上拉加载更多
                allItems.addAll(len, items);
                updateFavoriteAdapter();
            }
        }

        private void saveFavoriteHistory(List<FavoriteItem> items) {
            for (FavoriteItem item : items) {
                //缓存收藏信息
                String id = String.valueOf(item.getTargetId());
                if (!UserHelper.favorites.contains(id)) {
                    UserHelper.favorites.add(id);
                }
            }
            lastFavorites.clear();
            lastFavorites.addAll(UserHelper.favorites);
        }


        //只刷新验证状态————
        @Override
        public void onGetUserProfile(LoginUser user) {
            AppLog.print("onGetUserProfiles____获取用户信息————");
            if (isDownRefresh) {
                isDownRefresh = false;
                mListView.stopRefresh();
            }
            updateFragmentView(true, user);

//            if (user != null) {
//                if (user.getStatus() == 0) {
//                    verified_tv.setActivated(true);
//
//                    verified_tv.setText(getResources().getString(R.string.verified));
//                } else {
//                    verified_tv.setActivated(false);
//                    verified_tv.setText(getResources().getString(R.string.no_verified));
//                }
//            }


        }
    }

    private void setCouponAdapter(List<Coupon> items) {
        if (items.size() > 0) {
            if (couponAdapter == null) {
                couponAdapter = new MyCouponAdapter(getActivity(), items, MeFragment.this);
            } else {
                couponAdapter.updateItems(items);
            }
        } else {
            couponAdapter = emptcAdpater;
            couponAdapter.updateItems(null);
        }
        if (coupon_tab.isSelected()) {
            mListView.setAdapter(couponAdapter);
        }
    }

    private void setOrderAdpater(List<OrderItem> items) {
        if (items.size() > 0) {
            if (orderAdapter == null) {
                orderAdapter = new MyOrderAdapter(getActivity(), this, items, contentService);
            } else {
                orderAdapter.updateListView(items);
            }
        } else {
            orderAdapter = emptoAdpater;
            orderAdapter.updateListView(null);
        }
        if (order_tab.isSelected()) {
            mListView.setAdapter(orderAdapter);
        }
    }

    private void updateFavoriteAdapter() {
        AppLog.print("allItems size___" + allItems.size());
        if (allItems.size() > 0) {
            mListView.openLoadMore();
            if (allItems.contains(null)) {
                allItems.remove(null);
            }
            //有数据
            if (favoriteAdapter == null) {
                favoriteAdapter = new MyFavoriteAdapter(getActivity(), allItems);
                if (favorite_tab.isSelected()) {
                    mListView.setAdapter(favoriteAdapter);
                }
            } else {
                favoriteAdapter.updateListView(allItems);
            }


        } else {
            //没有数据
            favoriteAdapter = emptfAdpater;
            if (favorite_tab.isSelected()) {
                mListView.setAdapter(favoriteAdapter);
            }
        }

    }


    public static interface OnMeFragmentListener {
        void onShowRecommendFragment();

    }

    private AdapterView.OnItemClickListener xlvItemClicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            FavoriteItem item = (FavoriteItem) view.getTag(R.id.targetId);
            if (item == null) {
                return;
            }
            switch (item.getTargetType()) {//2产品 9线路 10专题 13资讯
                case 1://文章
                    gotoArticleDetail(item);
                    break;
                case 2://产品
                    gotoProductDetail(item);
                    break;
                case 9://线路
                    gotoRouteDetail(item);
                    break;
                case 10://专题
                    gotoSpecialDetail(item);
                    break;
                case 13://资讯
                    gotoEmptDetail();
                    break;
            }

        }
    };

    class UpdateReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UserHelper.isLogined(getActivity())) {
                contentService.getMyOrder(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
            }
        }
    }

}
