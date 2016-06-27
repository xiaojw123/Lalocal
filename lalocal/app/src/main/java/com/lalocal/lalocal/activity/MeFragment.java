package com.lalocal.lalocal.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.adapter.MyCouponAdapter;
import com.lalocal.lalocal.view.adapter.MyFavoriteAdapter;
import com.lalocal.lalocal.view.adapter.MyOrderAdapter;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2016/6/3.
 */
public class MeFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    public static final String USER = "user";
    public static final String LOGIN_STATUS = "loginstatus";
    TextView username_tv, verified_tv;
    TextView favoriteNum_tv, orderNum_tv, couponNum_tv;
    ImageView headImg;
    LinearLayout favorite_tab, order_tab, coupon_tab;
    ViewGroup lastSelectedView;
    Button settingBtn;
    RecyclerView myfavorite_rlv, myorder_rlv, coupon_rlv;
    ContentService contentService;
    boolean isLogined, isRefresh;
    int favoriteTotalPages, favoritePage;
    User user;
    XListView xListView;
    MyFavoriteAdapter favoriteAdapter;
    MyCouponAdapter couponAdapter;
    MyOrderAdapter orderAdapter;
    List<FavoriteItem> allItems = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            xListView.stopRefresh();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment_layout, container, false);
        xListView = (XListView) view.findViewById(R.id.home_me_xlistview);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(this);
        View headerView = inflater.inflate(R.layout.home_me_layout, null);
        xListView.addHeaderView(headerView);
        initView(headerView);
        initAdapter();
        initContentService();
        return view;
    }

    private void initAdapter() {
        favoriteAdapter = new MyFavoriteAdapter(getActivity(), null);
        couponAdapter = new MyCouponAdapter(getActivity(), null);
        orderAdapter = new MyOrderAdapter(getActivity(),null);
        xListView.setAdapter(favoriteAdapter);
    }

    private void initContentService() {
        favoritePage = 1;
        contentService = new ContentService(getActivity());
        contentService.setCallBack(new MeCallBack());
        contentService.getMyFavorite(-1, null, 1, 10);
    }

    private void initView(View view) {
        settingBtn = (Button) view.findViewById(R.id.home_me_set_btn);
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


    private View.OnClickListener meFragmentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == favorite_tab || v == order_tab || v == coupon_tab) {
                setSelectedTab((ViewGroup) v);
                if (v == favorite_tab) {
                    xListView.setPullLoadEnable(true);
                    if (favoriteAdapter != null) {
                        xListView.setAdapter(favoriteAdapter);
                    }
                } else if (v == coupon_tab) {
                    xListView.setPullLoadEnable(false);
                    if (couponAdapter != null) {
                        xListView.setAdapter(couponAdapter);
                    }
                } else if (v == order_tab) {
                    xListView.setPullLoadEnable(false);
                    if (orderAdapter != null) {
                        xListView.setAdapter(orderAdapter);
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
        AppLog.print("onActivityResult resCode__" + resultCode);
        if (resultCode == LoginActivity.REGISTER_OK) {
            String email = data.getStringExtra(LoginActivity.EMAIL);
            String psw = data.getStringExtra(LoginActivity.PSW);
            contentService.login(email, psw);
        } else if (resultCode == LoginActivity.LOGIN_OK) {
            User user = data.getParcelableExtra(USER);
            updateFragmentView(true, user);
        } else if (resultCode == SettingActivity.UN_LOGIN_OK) {
            updateFragmentView(false, null);
        } else if (resultCode == AccountEidt1Activity.UPDATE_ME_DATA) {
            int status = data.getIntExtra(KeyParams.STATUS, -1);
            String nickname = data.getStringExtra(KeyParams.NICKNAME);
            String avatar = data.getStringExtra(KeyParams.AVATAR);
            AppLog.print("status__" + status + ", nickanem__" + nickname + ", avatar__" + avatar);
            user.setStatus(status);
            user.setNickName(nickname);
            user.setAvatar(avatar);
            updateFragmentView(isLogined, user);
        }
        ;


    }

    //isLogined=false时注意擦除登陆数据
    private void updateFragmentView(boolean isLogined, User user) {
        this.isLogined = isLogined;
        this.user = user;
        int userid = -1;
        String token = null;
        if (isLogined && user != null) {
            userid = user.getId();
            token = user.getToken();
            String nickname = user.getNickName();
            if (!TextUtils.isEmpty(nickname)) {
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
            contentService.getMyFavorite(userid, token, 1, 10);
            contentService.getMyCoupon(userid, token);
            contentService.getMyOrder(userid, token);
        } else {
            username_tv.setText(getResources().getString(R.string.please_login));
            verified_tv.setVisibility(View.GONE);
            headImg.setImageResource(R.drawable.home_me_personheadnormal);
            favoriteAdapter = new MyFavoriteAdapter(getActivity(), null);
            couponAdapter = new MyCouponAdapter(getActivity(), null);
            orderAdapter = new MyOrderAdapter(getActivity(), null);
            favoriteNum_tv.setText("0");
            orderNum_tv.setText("0");
            couponNum_tv.setText("0");
            if (favorite_tab.isSelected()) {
                xListView.setAdapter(favoriteAdapter);
            }
            if (coupon_tab.isSelected()) {
                xListView.setAdapter(couponAdapter);
            }
            if (order_tab.isSelected()) {
                xListView.setAdapter(orderAdapter);
            }
            contentService.getMyFavorite(-1, null, 1, 10);


        }


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
            Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
        }
        xListView.stopLoadMore();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Object objValue= view.getTag(R.id.orderDetailId);
        if (objValue!=null){
            //订单详情

        }



//        int targetId = (int) view.getTag(R.id.targetId);
//        Intent intent = new Intent();
//        switch (targetId) {
//            case 1:
//                intent.setClass(getActivity(), ArticleActivity.class);
//                break;
//            case 2:
//                break;
//            case 9:
//                break;
//            case 10:
//                intent.setClass(getActivity(), SpecialDetailsActivity.class);
//                break;
//            case 13:
//                break;
//        }
//        intent.putExtra("productdetails", String.valueOf(targetId));
//        startActivity(intent);
    }

    class MeCallBack extends ICallBack {
        @Override
        public void onLoginSucess(String code, String message, User user) {
            if ("0".equals(code)) {
                updateFragmentView(true, user);
            }
        }

        @Override
        public void onGetFavoriteItem(List<FavoriteItem> items, int totalPages, int totalRows) {
            favoriteTotalPages = totalPages;
            if (!isRefresh) {
                allItems.clear();
            }
            if (allItems.size() == 0) {
                allItems.addAll(items);
                favoriteAdapter = new MyFavoriteAdapter(getActivity(), allItems);
                if (favorite_tab.isSelected()) {
                    xListView.setAdapter(favoriteAdapter);
                }
            } else {
                if (allItems.contains(items)) {
                    return;
                }
                int len = allItems.size();
                allItems.addAll(len, items);
                favoriteAdapter.updateListView(allItems);
            }
            favoriteNum_tv.setText(String.valueOf(allItems.size()));
        }

        @Override
        public void onGetCounponItem(List<Coupon> items) {
            couponNum_tv.setText(String.valueOf(items.size()));
            couponAdapter = new MyCouponAdapter(getActivity(), items);
            if (coupon_tab.isSelected()) {
                xListView.setAdapter(couponAdapter);
            }
        }

        @Override
        public void onGetOrderItem(List<OrderItem> items) {
            if (items != null) {
                orderNum_tv.setText(String.valueOf(items.size()));
                orderAdapter = new MyOrderAdapter(getActivity(), items);
            }
            if (order_tab.isSelected()){
                xListView.setAdapter(orderAdapter);
            }

        }
    }


}
