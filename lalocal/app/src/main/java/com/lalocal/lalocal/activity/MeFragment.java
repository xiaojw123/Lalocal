package com.lalocal.lalocal.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.adapter.MyFavoriteAdpater;

/**
 * Created by xiaojw on 2016/6/3.
 */
public class MeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String USER = "user";
    public static final String LOGIN_STATUS = "loginstatus";
    TextView username_tv, verified_tv;
    ImageView headImg;
    SwipeRefreshLayout refreshLayout;
    LinearLayout favorite_tab, order_tab, coupon_tab;
    FrameLayout tab_content_container;
    ViewGroup lastSelectedView;
    Button settingBtn;
    RecyclerView myfavorite_rlv, myorder_rlv, coupon_rlv;
    ContentService contentService;
    boolean isLogined;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            refreshLayout.setRefreshing(false);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_me_layout, container, false);
        initContentService();
        initView(view);
        return view;
    }

    private void initContentService() {
        contentService = new ContentService(getActivity());
        contentService.setCallBack(new MeCallBack());
    }

    private void initView(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_me_refresh_container);
        settingBtn = (Button) view.findViewById(R.id.home_me_set_btn);
        headImg = (ImageView) view.findViewById(R.id.home_me_headportrait_img);
        verified_tv = (TextView) view.findViewById(R.id.home_me_verified);
        username_tv = (TextView) view.findViewById(R.id.home_me_username);
        favorite_tab = (LinearLayout) view.findViewById(R.id.home_me_favorite_tab);
        order_tab = (LinearLayout) view.findViewById(R.id.home_me_order_tab);
        coupon_tab = (LinearLayout) view.findViewById(R.id.home_me_coupon_tab);
        tab_content_container = (FrameLayout) view.findViewById(R.id.home_tab_content_container);
        initTabView(tab_content_container);
        refreshLayout.setColorSchemeResources(R.color.color_8fe6ff, R.color.thin_blue, R.color.color_de);
        refreshLayout.setOnRefreshListener(this);
        verified_tv.setOnClickListener(meFragmentClickListener);
        settingBtn.setOnClickListener(meFragmentClickListener);
        headImg.setOnClickListener(meFragmentClickListener);
        username_tv.setOnClickListener(meFragmentClickListener);
        favorite_tab.setOnClickListener(meFragmentClickListener);
        order_tab.setOnClickListener(meFragmentClickListener);
        coupon_tab.setOnClickListener(meFragmentClickListener);
    }

    private void initTabView(FrameLayout tab_content_container) {
        myfavorite_rlv = new RecyclerView(getActivity());
        MyFavoriteAdpater adpater = new MyFavoriteAdpater(getActivity());
        myfavorite_rlv.setAdapter(adpater);


    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(0, 2000);
    }


    private View.OnClickListener meFragmentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == favorite_tab || v == order_tab || v == coupon_tab) {
                if (lastSelectedView != null) {
                    setContentTabSelected(lastSelectedView, false);
                }
                ViewGroup viewGroup = (ViewGroup) v;
                setContentTabSelected(viewGroup, true);
                lastSelectedView = viewGroup;
            } else if (v == headImg || v == username_tv) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, 100);
            } else if (v == settingBtn) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra(LOGIN_STATUS, isLogined);
                startActivityForResult(intent, 101);
            }
        }
    };

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
            updateFragmentView(false, null);
        }


    }

    //isLogined=false时注意擦除登陆数据
    private void updateFragmentView(boolean isLogined, User user) {
        this.isLogined = isLogined;
        if (isLogined) {
            username_tv.setText(user.getNickName());
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
            DrawableUtils.displayImg(getActivity(), headImg, user.getAvatar());
        } else {
            username_tv.setText(getResources().getString(R.string.please_login));
            verified_tv.setVisibility(View.GONE);
            headImg.setImageResource(R.drawable.home_me_personheadnormal);
        }
    }

    class MeCallBack extends ICallBack {
        @Override
        public void onLoginSucess(String code, String message, User user) {
            if ("0".equals(code)) {
                updateFragmentView(true, user);
            }
        }

    }


}
