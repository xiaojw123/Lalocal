package com.lalocal.lalocal.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.AccountEidt1Activity;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.activity.SettingActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.ArticleDetailsBean;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.adapter.MyCouponAdapter;
import com.lalocal.lalocal.view.adapter.MyFavoriteAdapter;
import com.lalocal.lalocal.view.adapter.MyOrderAdapter;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2016/6/3.
 * note: isRefresh更新
 */
public class MeFragment extends Fragment implements XListView.IXListViewListener {
    public static final String USER = "user";
    public static final String LOGIN_STATUS = "loginstatus";
    TextView username_tv, verified_tv;
    TextView favoriteNum_tv, orderNum_tv, couponNum_tv;
    ImageView headImg;
    LinearLayout favorite_tab, order_tab, coupon_tab;
    ViewGroup lastSelectedView;
    ImageButton settingBtn;
    ContentService contentService;
    public boolean isLogined, isRefresh, isImLogin;
    int favoriteTotalPages, favoritePage = 1;
    User user;
    XListView mListView;
    MyFavoriteAdapter favoriteAdapter, emptFavoriteAdpater;
    MyCouponAdapter couponAdapter, emptCouponAapter;
    MyOrderAdapter orderAdapter, emptOrderAdpater;
    List<FavoriteItem> allItems = new ArrayList<>();
    OnMeFragmentListener fragmentListener;
    Intent imLoginData;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mListView.stopRefresh();
        }
    };
    //收藏数据是否需要  list

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
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(false);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(xlvItemClicklistener);
        View headerView = inflater.inflate(R.layout.home_me_layout, null);
        mListView.addHeaderView(headerView);
        initView(headerView);
        initAdapter();
        initContentService();
        return view;
    }

    private void initView(View view) {
        settingBtn = (ImageButton) view.findViewById(R.id.home_me_set_btn);
        headImg = (ImageView) view.findViewById(R.id.home_me_headportrait_img);
        verified_tv = (TextView) view.findViewById(R.id.home_me_verified);
        username_tv = (TextView) view.findViewById(R.id.home_me_username);
        favorite_tab = (LinearLayout) view.findViewById(R.id.home_me_favorite_tab);
        order_tab = (LinearLayout) view.findViewById(R.id.home_me_order_tab);
        coupon_tab = (LinearLayout) view.findViewById(R.id.home_me_coupon_tab);
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
        setSelectedTab(favorite_tab);
    }


    private void initAdapter() {
        emptCouponAapter = new MyCouponAdapter(getActivity(), null, this);
        emptOrderAdpater = new MyOrderAdapter(getActivity(), null);
        emptFavoriteAdpater = new MyFavoriteAdapter(getActivity(), null);
        mListView.setAdapter(emptFavoriteAdpater);
    }


    private void initContentService() {
        contentService = new ContentService(getActivity());
        contentService.setCallBack(new MeCallBack());
        if (UserHelper.isLogined(getActivity())) {
            //恢复上一次登录的状态
            String email = UserHelper.getUserEmail(getActivity());
            String psw = UserHelper.getPassword(getActivity());
            contentService.login(email, psw);
        } else {
            setLoginStatus(-1, null);
        }
    }

    //从其他页面切换到我的页面
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppLog.print("onHiddenChanged____hidden___" + hidden);
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
                if (isLogined) {
                    if (user != null) {
                        int id = user.getId();
                        String token = user.getToken();
                        contentService.getUserProfile(id, token);
                        if (favorite_tab.isSelected()) {
                            contentService.getMyFavorite(id, token, 1, 10);
                        }
                        if (order_tab.isSelected()) {
                            contentService.getMyOrder(id, token);
                        }
                        if (coupon_tab.isSelected()) {
                            contentService.getMyCoupon(id, token);
                        }
                    }
                } else {
                    if (favorite_tab.isSelected()) {
                        contentService.getMyFavorite(-1, null, 1, 10);
                    }
                    if (order_tab.isSelected()) {
                        mListView.setAdapter(emptOrderAdpater);
                    }
                    if (coupon_tab.isSelected()) {
                        mListView.setAdapter(emptCouponAapter);
                    }
                }
            }
        }


    }

    private View.OnClickListener meFragmentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == favorite_tab || v == order_tab || v == coupon_tab) {
                setSelectedTab((ViewGroup) v);
                if (v == favorite_tab) {
                    mListView.setPullLoadEnable(true);
                    if (user != null) {
                        if (favoriteAdapter != null) {
                            mListView.setAdapter(favoriteAdapter);
                        } else {
                            mListView.setAdapter(emptFavoriteAdpater);
                        }
                        contentService.getMyFavorite(user.getId(), user.getToken(), 1, 10);
                    } else {
                        if (favoriteAdapter != null) {
                            mListView.setAdapter(favoriteAdapter);
                        } else {
                            mListView.setAdapter(emptFavoriteAdpater);
                        }
                        contentService.getMyFavorite(-1, null, 1, 10);
                    }
                } else if (v == coupon_tab) {
                    mListView.setPullLoadEnable(false);
                    if (user != null) {
                        if (couponAdapter != null) {
                            mListView.setAdapter(couponAdapter);
                        } else {
                            mListView.setAdapter(emptCouponAapter);
                        }
                        contentService.getMyCoupon(user.getId(), user.getToken());
                    } else {
                        mListView.setAdapter(emptCouponAapter);
                    }
                } else if (v == order_tab) {
                    mListView.setPullLoadEnable(false);
                    if (user != null) {
                        if (orderAdapter != null) {
                            mListView.setAdapter(orderAdapter);
                        } else {
                            mListView.setAdapter(emptOrderAdpater);
                        }
                        contentService.getMyOrder(user.getId(), user.getToken());
                    } else {
                        mListView.setAdapter(emptOrderAdpater);
                    }
                }
            } else if (v == headImg || v == username_tv) {
                if (isLogined) {
                    Intent intent = new Intent(getActivity(), AccountEidt1Activity.class);
                    intent.putExtra(KeyParams.USERID, user.getId());
                    intent.putExtra(KeyParams.TOKEN, user.getToken());
                    startActivityForResult(intent, 100);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 100);
                }
            } else if (v == settingBtn) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra(LOGIN_STATUS, isLogined);
                startActivityForResult(intent, 101);
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
        if (resultCode == LoginActivity.REGISTER_OK) {
            String email = data.getStringExtra(LoginActivity.EMAIL);
            String psw = data.getStringExtra(LoginActivity.PSW);
            contentService.login(email, psw);
        } else if (resultCode == LoginActivity.LOGIN_OK) {
            User user = data.getParcelableExtra(USER);
            updateFragmentView(true, user);
        } else if (resultCode == SettingActivity.UN_LOGIN_OK) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(KeyParams.IS_LOGIN, false);
            UserHelper.saveLoginInfo(getActivity(), bundle);
            updateFragmentView(false, null);
        } else if (resultCode == AccountEidt1Activity.UPDATE_ME_DATA) {
            String nickname = data.getStringExtra(KeyParams.NICKNAME);
            String avatar = data.getStringExtra(KeyParams.AVATAR);
            if (user != null) {
                user.setNickName(nickname);
                user.setAvatar(avatar);
                updateFragmentView(isLogined, user);
                contentService.getUserProfile(user.getId(), user.getToken());
            }
        } else if (resultCode == SettingActivity.IM_LOGIN) {
            isImLogin = true;
            imLoginData = data;
            if (fragmentListener != null) {
                fragmentListener.onShowRecommendFragment();
            }
        }


    }

    private void updateFragmentView(boolean isLogined, User user) {
        this.isLogined = isLogined;
        this.user = user;
        int userid = -1;
        String token = null;
        if (isLogined && user != null) {
            token = user.getToken();
            userid = user.getId();
            CommonUtil.setUserParams(userid, token);
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
            setLoginStatus(userid, token);
        } else {
            CommonUtil.setUserParams(-1, null);
            username_tv.setActivated(false);
            username_tv.setText(getResources().getString(R.string.please_login));
            verified_tv.setVisibility(View.GONE);
            headImg.setImageResource(R.drawable.home_me_personheadnormal);
            favoriteAdapter = new MyFavoriteAdapter(getActivity(), null);
            couponAdapter = new MyCouponAdapter(getActivity(), null, this);
            orderAdapter = new MyOrderAdapter(getActivity(), null);
            favoriteNum_tv.setText("0");
            orderNum_tv.setText("0");
            couponNum_tv.setText("0");
            setLoginStatus(-1, null);
        }


    }


    private void setLoginStatus(int userid, String token) {
        if (!favorite_tab.isSelected()) {
            setSelectedTab(favorite_tab);
        }
        contentService.getMyFavorite(userid, token, 1, 10);
    }


    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh = false;
                handler.sendEmptyMessage(0);


            }
        }, 1000);

    }

    @Override
    public void onLoadMore() {
        isRefresh = true;
        favoritePage = favoritePage + 1;
        if (favoritePage <= favoriteTotalPages) {
            if (user != null) {
                contentService.getMyFavorite(user.getId(), user.getToken(), 10, favoritePage);
            }

        } else {
            isRefresh = false;
            CommonUtil.showToast(getActivity(), "没有更多数据", Toast.LENGTH_SHORT);
        }
        mListView.stopLoadMore();
    }


    private void gotoProductDetail(FavoriteItem item) {
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        SpecialToH5Bean bean = new SpecialToH5Bean();
        bean.setTargetId(item.getTargetId());
        bean.setTargetType(item.getTargetType());
        intent.putExtra("productdetails", bean);
        startActivity(intent);

    }

    private void gotoArticleDetail(FavoriteItem item) {
        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        ArticleDetailsBean bean = new ArticleDetailsBean();
        bean.setTargetId(item.getTargetId());
        intent.putExtra("articleDetailsBean", bean);
        startActivity(intent);
    }

    class MeCallBack extends ICallBack {
        @Override
        public void onRequestFailed() {
            isRefresh = false;
        }

        @Override
        public void onLoginSucess(User user) {
            updateFragmentView(true, user);
        }

        @Override
        public void onGetFavoriteItem(List<FavoriteItem> items, int totalPages, int totalRows) {
            updateFavorite(items, totalPages);
        }

        @Override
        public void onGetCounponItem(List<Coupon> items) {
            AppLog.print("me fragment __onGetCounponItem__size__" + items.size());
            couponNum_tv.setText(String.valueOf(items.size()));
            setCouponAdapter(items);
        }


        @Override
        public void onGetOrderItem(List<OrderItem> items) {
            orderNum_tv.setText(String.valueOf(items.size()));
            setOrderAdpater(items);
        }


        private void updateFavorite(List<FavoriteItem> items, int totalPages) {
            favoriteTotalPages = totalPages;
            if (!isRefresh) {
                AppLog.print("清理allItems___");
                allItems.clear();
            } else {
                isRefresh = false;
            }
            if (allItems.size() == 0) {
                AppLog.print("初次加载————————");
                allItems.addAll(items);
                setFavoriteAdapter();
            } else {
                //上拉加载更多
                AppLog.print("上拉加载更多————————");
                int len = allItems.size();
                allItems.addAll(len, items);
                setFavoriteAdapter();
            }
            favoriteNum_tv.setText(String.valueOf(allItems.size()));
        }


        //只刷新验证状态————
        @Override
        public void onGetUserProfile(LoginUser user) {
            if (user != null) {
                if (user.getStatus() == 0) {
                    verified_tv.setActivated(true);

                    verified_tv.setText(getResources().getString(R.string.verified));
                } else {
                    verified_tv.setActivated(false);
                    verified_tv.setText(getResources().getString(R.string.no_verified));
                }
            }

        }
    }

    private void setCouponAdapter(List<Coupon> items) {
        if (items.size() > 0) {
            if (couponAdapter == null) {
                couponAdapter = new MyCouponAdapter(getActivity(), items, MeFragment.this);
            }
            if (coupon_tab.isSelected()) {
                mListView.setAdapter(couponAdapter);
            }
        } else {
            mListView.setAdapter(emptCouponAapter);
        }
    }

    private void setOrderAdpater(List<OrderItem> items) {
        if (items.size() > 0) {
            if (orderAdapter == null) {
                orderAdapter = new MyOrderAdapter(getActivity(), items);
            }
            if (order_tab.isSelected()) {
                mListView.setAdapter(orderAdapter);
            }
        } else {
            mListView.setAdapter(emptOrderAdpater);
        }
    }

    private void setFavoriteAdapter() {
        if (allItems.size() > 0) {
            if (favoriteAdapter == null) {
                favoriteAdapter = new MyFavoriteAdapter(getActivity(), allItems);
            } else {
                favoriteAdapter.updateListView(allItems);
            }
            if (favorite_tab.isSelected()) {
                mListView.setAdapter(favoriteAdapter);
            }
        } else {
            if (favorite_tab.isSelected()) {
                mListView.setAdapter(emptFavoriteAdpater);
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
            switch (item.getTargetType()) {//2产品 9线路 10专题 13资讯
                case 1://文章
                    gotoArticleDetail(item);
                    break;
                case 2://产品
                    gotoProductDetail(item);

                    break;
                case 9://线路
                    break;
                case 10://专题
                    break;
                case 13://资讯
                    Intent intent = new Intent(getActivity(), ArticleActivity.class);
                    ArticleDetailsBean bean = new ArticleDetailsBean();
                    //   bean.setCollected(true);
                    bean.setTargetId(item.getTargetId());
                    intent.putExtra("articleDetailsBean", bean);
                    startActivity(intent);
                    break;
            }

        }
    };


}
