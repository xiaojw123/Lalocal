package com.lalocal.lalocal.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.DestinationFragment;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.activity.fragment.NewsFragment;
import com.lalocal.lalocal.activity.fragment.RecommendNewFragment;
import com.lalocal.lalocal.model.VersionResult;
import com.lalocal.lalocal.thread.UpdateTask;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.dialog.CustomDialog;

public class HomeActivity extends BaseActivity implements MeFragment.OnMeFragmentListener {
    public static final String VERSION_RESULT = "version_result";
    LinearLayout home_recommend_tab, home_destination_tab, home_news_tab, home_me_tab;
    LinearLayout home_tab_container;
    ViewGroup lastSelectedTab;
    FragmentManager fm;
    Fragment meFragment, newsFragment, distinationFragment, recommendFragment;

    private int selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.print("HomeActivity__oncreate__");
        setContentView(R.layout.home_layout);
        initView();
        checkUpdate();
    }

    private void checkUpdate() {
        VersionResult result = getIntent().getParcelableExtra(VERSION_RESULT);
        boolean flag = result.isForceFlag();
        boolean checkUpdate = result.isCheckUpdate();
        String downLoadUrl = result.getDownloadUrl();
//        flag = false;
//        checkUpdate = true;
//        downLoadUrl = "http://media.lalocal.cn/app/lalocal_2_1_2.apk";
        if (checkUpdate && !TextUtils.isEmpty(downLoadUrl)) {
            if (flag) {
                update(downLoadUrl);
            } else {
                showUpdateDialog(downLoadUrl);
            }
        }


    }

    private void initView() {
        home_tab_container = (LinearLayout) findViewById(R.id.home_tab_containner);
        home_recommend_tab = (LinearLayout) findViewById(R.id.home_tab_recommend);
        home_destination_tab = (LinearLayout) findViewById(R.id.home_tab_destination);
        home_news_tab = (LinearLayout) findViewById(R.id.home_tab_liveplay);
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
                selected = FRAGMENT_RECOMMEND;
                AppLog.print("recommend__" + recommendFragment);
                if (recommendFragment == null) {
                    AppLog.print("___add");
                    recommendFragment = new RecommendNewFragment();
                    ft.add(R.id.home_fragment_container, recommendFragment);
                } else {
                    AppLog.print("___show");
                    ft.show(recommendFragment);
                }
                break;
            case R.id.home_tab_destination:
                selected = FRAGMENT_DESTINATION;
                if (distinationFragment == null) {
                    distinationFragment = new DestinationFragment();
                    ft.add(R.id.home_fragment_container, distinationFragment);
                } else {
                    ft.show(distinationFragment);
                }
                break;
            case R.id.home_tab_liveplay:
                selected = FRAGMENT_NEWS;
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    ft.add(R.id.home_fragment_container, newsFragment);
                } else {
                    ft.show(newsFragment);
                }
                break;
            case R.id.home_tab_me:
                selected = FRAGMENT_ME;
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

    public static final int FRAGMENT_RECOMMEND = 0;
    public static final int FRAGMENT_DESTINATION = 1;
    public static final int FRAGMENT_NEWS = 2;
    public static final int FRAGMENT_ME = 3;

    public void goToFragment(int position) {
        if (selected != position) {
            switch (position) {
                case FRAGMENT_RECOMMEND:
                    showFragment(home_recommend_tab);
                    break;
                case FRAGMENT_DESTINATION:
                    showFragment(home_destination_tab);
                    break;
                case FRAGMENT_NEWS:
                    Log.d("hahaha", "showFragment");
                    showFragment(home_news_tab);
                    break;
                case FRAGMENT_ME:
                    showFragment(home_me_tab);
                    break;
            }
        }
    }


    private void showUpdateDialog(final String dowloadUrl) {
        CustomDialog dialog=new CustomDialog(this,R.style.custom_dialog,CustomDialog.Style.STYLE_IOS);
        dialog.setTitle("请更新至最新版本");
        dialog.setMessage("更新内容：\n1.直播首页改版\n2.个人页面改版\n3.直播录制功能优化");
        dialog.setSurceBtn("立即更新", new CustomDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    update(dowloadUrl);
                } else {
                    Toast.makeText(HomeActivity.this, "无可用存储空间",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setCancelBtn("稍后更新",null);
        dialog.show();

//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("请更新至最新版本");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (Environment.getExternalStorageState().equals(
//                        Environment.MEDIA_MOUNTED)) {
//                    update(dowloadUrl);
//                } else {
//                    Toast.makeText(HomeActivity.this, "无可用存储空间",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//
//        });
//        builder.create().show();
    }

    private void update(String downLoadUrl) {
        UpdateTask task = new UpdateTask(this);
        task.execute(downLoadUrl);
    }

}
