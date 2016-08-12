package com.lalocal.lalocal.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.DestinationFragment;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.activity.fragment.NewsFragment;
import com.lalocal.lalocal.activity.fragment.RecommendFragment;
import com.lalocal.lalocal.util.AppLog;
import com.qihoo.updatesdk.lib.UpdateHelper;

public class HomeActivity extends BaseActivity implements MeFragment.OnMeFragmentListener {
    LinearLayout home_recommend_tab, home_destination_tab, home_news_tab, home_me_tab;
    TextView title_recommed, title_destination, title_liveplay, title_me;
    LinearLayout home_tab_container;
    ViewGroup lastSelectedTab;
    FragmentManager fm;
    Fragment meFragment, newsFragment, distinationFragment, recommendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.print("HomeActivity__oncreate__");
        setContentView(R.layout.home_layout);
        AppLog.print("memory size___" + Runtime.getRuntime().freeMemory());

        initView();
        UpdateHelper.getInstance().autoUpdate("com.lalocal.lalocal");
    }

    private void initView() {
        title_recommed = (TextView) findViewById(R.id.home_tab_title_recommend);
        title_liveplay = (TextView) findViewById(R.id.home_tab_title_liveplay);
        title_destination = (TextView) findViewById(R.id.home_tab_title_destination);
        title_me = (TextView) findViewById(R.id.home_tab_title_me);
        home_tab_container = (LinearLayout) findViewById(R.id.home_tab_containner);
        home_recommend_tab = (LinearLayout) findViewById(R.id.home_tab_recommend);
        home_destination_tab = (LinearLayout) findViewById(R.id.home_tab_destination);
        home_news_tab = (LinearLayout) findViewById(R.id.home_tab_news);
        home_me_tab = (LinearLayout) findViewById(R.id.home_tab_me);
        home_recommend_tab.setOnClickListener(tabClickListener);
        home_destination_tab.setOnClickListener(tabClickListener);
        home_news_tab.setOnClickListener(tabClickListener);
        home_me_tab.setOnClickListener(tabClickListener);
        fm = getFragmentManager();
        showFragment(home_recommend_tab);
    }


    private View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewGroup container = (ViewGroup) v;
            showFragment(container);

        }
    };

    private void showFragment(ViewGroup container) {
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        setSelectedTab(container);
        switch (container.getId()) {
            case R.id.home_tab_recommend:
                title_recommed.setVisibility(View.VISIBLE);
                title_destination.setVisibility(View.GONE);
                title_liveplay.setVisibility(View.GONE);
                title_me.setVisibility(View.GONE);
                if (recommendFragment == null) {
                    recommendFragment = new RecommendFragment();
                    ft.add(R.id.home_fragment_container, recommendFragment);
                } else {
                    ft.show(recommendFragment);
                }
                break;
            case R.id.home_tab_destination:
                title_recommed.setVisibility(View.GONE);
                title_destination.setVisibility(View.VISIBLE);
                title_liveplay.setVisibility(View.GONE);
                title_me.setVisibility(View.GONE);
                if (distinationFragment == null) {
                    distinationFragment = new DestinationFragment();
                    ft.add(R.id.home_fragment_container, distinationFragment);
                } else {
                    ft.show(distinationFragment);
                }
                break;
            case R.id.home_tab_news:
                title_recommed.setVisibility(View.GONE);
                title_destination.setVisibility(View.GONE);
                title_liveplay.setVisibility(View.VISIBLE);
                title_me.setVisibility(View.GONE);
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    ft.add(R.id.home_fragment_container, newsFragment);
                } else {
                    ft.show(newsFragment);
                }
                break;
            case R.id.home_tab_me:
                title_recommed.setVisibility(View.VISIBLE);
                title_destination.setVisibility(View.GONE);
                title_liveplay.setVisibility(View.GONE);
                title_me.setVisibility(View.VISIBLE);
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    ft.add(R.id.home_fragment_container, meFragment);
                } else {
                    ft.show(meFragment);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {

        if (recommendFragment != null) {
            ft.hide(recommendFragment);
        }
        if (distinationFragment != null) {
            ft.hide(distinationFragment);
        }
        if (newsFragment != null) {
            ft.hide(newsFragment);
        }
        if (meFragment != null) {
            ft.hide(meFragment);
        }


    }

    private void setSelectedTab(ViewGroup container) {
        if (lastSelectedTab != null) {
            setTabStatus(lastSelectedTab, false);
        }
        setTabStatus(container, true);
        lastSelectedTab = container;
    }


    private void setTabStatus(ViewGroup container, boolean isSelected) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            v.setSelected(isSelected);
        }
    }


    @Override
    public void onShowRecommendFragment() {
        AppLog.print("Activity  onShowRecommendFragment");
        showFragment(home_recommend_tab);
    }
}
