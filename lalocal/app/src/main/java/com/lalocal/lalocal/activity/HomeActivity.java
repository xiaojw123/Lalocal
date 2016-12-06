package com.lalocal.lalocal.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.FindFragment;
import com.lalocal.lalocal.activity.fragment.LiveFragment;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.model.VersionResult;
import com.lalocal.lalocal.thread.UpdateTask;
import com.lalocal.lalocal.util.AppLog;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.NormalAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements MeFragment.OnMeFragmentListener {

    @BindView(R.id.home_tab_live)
    ImageView mImgTabLive;
    @BindView(R.id.home_tab_find)
    ImageView mImgTabFind;
    @BindView(R.id.home_tab_search)
    ImageView mImgTabSearch;
    @BindView(R.id.home_tab_me)
    ImageView mImgTabMe;
    @BindView(R.id.home_tab_live_selected)
    View mTabLiveSelected;
    @BindView(R.id.home_tab_find_selected)
    View mTabFindSelected;

    public static final String VERSION_RESULT = "version_result";
    ViewGroup lastSelectedTab;
    FragmentManager fm;
    //    Fragment meFragment, newsFragment, distinationFragment, recommendFragment;
    private Fragment mFragLive;
    private Fragment mFragFind;
    private Fragment mFragMe;
    // 记录第一次点击back的时间
    private long clickTime = 0;

    int mPageType;

    public static final int FRAGMENT_LIVE = 0x01;
    public static final int FRAGMENT_FIND = 0x02;
    public static final int FRAGMENT_ME = 0x03;
    public int mCurFragment = FRAGMENT_LIVE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        // 使用ButterKnife框架
        ButterKnife.bind(this);
        // 显示直播fragment
        showFragment(FRAGMENT_LIVE);
        // 检查更新
        checkUpdate();
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        VersionResult result = getIntent().getParcelableExtra(VERSION_RESULT);
        if (result != null) {
            boolean forceFlag = result.isForceFlag();
            boolean checkUpdate = result.isCheckUpdate();
            String downLoadUrl = result.getDownloadUrl();
            String contentText = getUpdateContent(result.getMsg());
            if (checkUpdate && !TextUtils.isEmpty(downLoadUrl)) {
                if (forceFlag) {
                    showForceUpdateDialog(downLoadUrl, contentText);
                } else {
                    showUpdateDialog(downLoadUrl, contentText);
                }
            }
        }
    }

    @OnClick({R.id.home_tab_live, R.id.home_tab_find, R.id.home_tab_me, R.id.home_tab_search})
    void clickButton(View view) {
        switch (view.getId()) {
            case R.id.home_tab_live:
                showFragment(FRAGMENT_LIVE);
                break;
            case R.id.home_tab_find:
                showFragment(FRAGMENT_FIND);
                break;
            case R.id.home_tab_me:
                showFragment(FRAGMENT_ME);
                break;
            case R.id.home_tab_search:
                Intent intent = new Intent(this, GlobalSearchActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 显示某一个fragment
     *
     * @param tab
     */
    private void showFragment(int tab) {
        // 初始化碎片管理器
        FragmentManager fm = getFragmentManager();
        // 初始化碎片事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 隐藏所有Fragments
        hideFragments(transaction);
        // 标记当前所在的fragment
        mCurFragment = tab;
        // 选中的tab修改样式
        switch (tab) {
            case FRAGMENT_LIVE:
                // 显示直播fragment
                if (mFragLive == null) {
                    mFragLive = new LiveFragment();
                    transaction.add(R.id.home_fragment_container, mFragLive);
                } else {
                    transaction.show(mFragLive);
                }
                // 直播字样不透明
                mImgTabLive.setAlpha(1.0f);
                // 选中bar显示
                mTabLiveSelected.setVisibility(View.VISIBLE);
                break;
            case FRAGMENT_FIND:
                // 显示发现fragment
                if (mFragFind == null) {
                    mFragFind = new FindFragment();
                    transaction.add(R.id.home_fragment_container, mFragFind);
                } else {
                    transaction.show(mFragFind);
                }
                // 发现字样不透明
                mImgTabFind.setAlpha(1.0f);
                // 选中bar显示
                mTabFindSelected.setVisibility(View.VISIBLE);
                break;
            case FRAGMENT_ME:
                // 显示我fragment
                if (mFragMe == null) {
                    mFragMe = new MeFragment();
                    transaction.add(R.id.home_fragment_container, mFragMe);
                } else {
                    transaction.show(mFragMe);
                }
                // 我 图标设置为选中样式
                mImgTabMe.setImageResource(R.drawable.home_myself_sel);
                break;
        }
        // 事务提交
        transaction.commit();
    }

    /**
     * 隐藏fragment
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        // 如果transaction为空
        if (transaction == null) {
            return;
        }

        // 隐藏LiveFragment
        if (mFragLive != null) {
            transaction.hide(mFragLive);
        }

        // 隐藏FindFragment
        if (mFragFind != null) {
            transaction.hide(mFragFind);
        }

        // 隐藏MeFragment
        if (mFragMe != null) {
            transaction.hide(mFragMe);
        }

        // 直播字样设置为半透明
        mImgTabLive.setAlpha(0.5f);
        // 发现字样设置为半透明
        mImgTabFind.setAlpha(0.5f);
        // 我 图片设置为未选中状态
        mImgTabMe.setImageResource(R.drawable.home_myself_ic);

        // 直播选中bar 不显示
        mTabLiveSelected.setVisibility(View.INVISIBLE);
        // 发现选中bar 不显示
        mTabFindSelected.setVisibility(View.INVISIBLE);

    }

//    private void showFragment(ViewGroup container) {
//        FragmentTransaction ft = fm.beginTransaction();
//        hideFragment(ft);
//        setSelectedTab(container);
//        switch (container.getId()) {
//            case R.id.home_tab_recommend:
//                selected = FRAGMENT_RECOMMEND;
//                AppLog.print("recommend__" + recommendFragment);
//                if (recommendFragment == null) {
//                    AppLog.print("___add");
//                    recommendFragment = new FindFragment();
//                    ft.add(R.id.home_fragment_container, recommendFragment);
//                } else {
//                    AppLog.print("___show");
//                    ft.show(recommendFragment);
//                }
//                break;
//            case R.id.home_tab_destination:
//                selected = FRAGMENT_DESTINATION;
//                if (distinationFragment == null) {
//                    distinationFragment = new DestinationFragment();
//                    ft.add(R.id.home_fragment_container, distinationFragment);
//                } else {
//                    ft.show(distinationFragment);
//                }
//                break;
//            case R.id.home_tab_liveplay:
//                selected = FRAGMENT_NEWS;
//                if (newsFragment == null) {
////                    newsFragment = new NewsFragment();
//                    // TODO: 2.2.0版本fragment更换
//                    newsFragment = new LiveFragment();
//                    ft.add(R.id.home_fragment_container, newsFragment);
//                } else {
//                    ft.show(newsFragment);
//                }
//                break;
//            case R.id.home_tab_me:
//                selected = FRAGMENT_ME;
//                if (meFragment == null) {
//                    meFragment = new MeFragment();
//                    ft.add(R.id.home_fragment_container, meFragment);
//                } else {
//                    ft.show(meFragment);
//                }
//                break;
//        }
//        ft.commit();
//    }
//    private void hideFragment(FragmentTransaction ft) {
//
//        if (recommendFragment != null) {
//            ft.hide(recommendFragment);
//        }
//        if (distinationFragment != null) {
//            ft.hide(distinationFragment);
//        }
//        if (newsFragment != null) {
//            ft.hide(newsFragment);
//        }
//        if (meFragment != null) {
//            ft.hide(meFragment);
//        }
//
//
//    }

    //    private void setSelectedTab(ViewGroup container) {
//        if (lastSelectedTab != null) {
//            setTabStatus(lastSelectedTab, false);
//        }
//        setTabStatus(container, true);
//        lastSelectedTab = container;
//    }
//
//
//    private void setTabStatus(ViewGroup container, boolean isSelected) {
//        for (int i = 0; i < container.getChildCount(); i++) {
//            View v = container.getChildAt(i);
//            AppLog.i("tab", "tba is " + i);
//            v.setSelected(isSelected);
//        }
//    }
    @Override
    public void onShowRecommendFragment() {
        showFragment(FRAGMENT_FIND);
    }

    /**
     * 跳转到某一个Fragment
     *
     * @param position
     */
    public void goToFragment(int position) {
        if (mCurFragment != position) {
            showFragment(position);
        }
    }

    private void showForceUpdateDialog(final String dowloadUrl, final String contentText) {
        NormalAlertDialog.Builder builder = new NormalAlertDialog.Builder(this);
        final NormalAlertDialog forceDailog = builder.setHeight(0.23f).setCancelable(false)  //屏幕高度*0.23
                .setWidth(0.8f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("版本更新")
                .setTitleTextColor(R.color.black_light)
                .setContentText(contentText)
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("退出")
                .setLeftButtonTextColor(R.color.color_8c)
                .setRightButtonText("立即更新")
                .setRightButtonTextColor(R.color.color_ffaa2a).build();
        builder.setOnclickListener(new DialogOnClickListener() {
            @Override
            public void clickLeftButton(View view) {
                forceDailog.dismiss();
                finish();
//                if (!MyApplication.isDebug) {
//                    MobclickAgent.onKillProcess(HomeActivity.this);
//                }
//                System.exit(0);
            }

            @Override
            public void clickRightButton(View view) {
                forceDailog.dismiss();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    update(dowloadUrl);
                } else {
                    Toast.makeText(HomeActivity.this, "无可用存储空间",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        forceDailog.show();
    }

    public String getUpdateContent(List<String> msgList) {
        StringBuffer sb = new StringBuffer();
        int len = msgList.size();
        for (int i = 0; i < len; i++) {
            String msg = msgList.get(i);
            if (i == len - 1) {
                sb.append(msg);
            } else {
                sb.append(msg + "\n");
            }
        }
        return sb.toString();
    }


    private void showUpdateDialog(final String dowloadUrl, String contentText) {
        NormalAlertDialog.Builder builder = new NormalAlertDialog.Builder(this);
        final NormalAlertDialog normalDialog = builder.setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.8f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("版本更新")
                .setTitleTextColor(R.color.black_light)
                .setContentText(contentText)
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("稍候更新")
                .setLeftButtonTextColor(R.color.color_8c)
                .setRightButtonText("立即更新")
                .setRightButtonTextColor(R.color.color_ffaa2a).build();
        builder.setOnclickListener(new DialogOnClickListener() {
            @Override
            public void clickLeftButton(View view) {
                normalDialog.dismiss();
            }

            @Override
            public void clickRightButton(View view) {

                normalDialog.dismiss();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    update(dowloadUrl);
                } else {
                    Toast.makeText(HomeActivity.this, "无可用存储空间",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        normalDialog.show();
    }

    private void update(String downLoadUrl) {
        UpdateTask task = new UpdateTask(this);
        task.execute(downLoadUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 2秒内连续点击back键，退出应用
     */
    private void exit() {
        // 退出提示
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(HomeActivity.this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
            // 获取点击时间
            clickTime = System.currentTimeMillis();
        } else {
            // 退出应用
            this.finish();
        }
    }


    /*   next only for test update
    * */
//    private void testCheckUpdate(boolean focrce) {
//        boolean checkUpdate =true;
//        String downLoadUrl ="http://media.lalocal.cn/app/lalocal_2_1_2.apk";
//        List<String> updateItems=new ArrayList<>();
//        updateItems.add("直播首页改版");
//        updateItems.add("礼物功能优化");
//        updateItems.add("直播回放支持");
//        String contentText = getUpdateContent(updateItems);
//        if (checkUpdate && !TextUtils.isEmpty(downLoadUrl)) {
//            if (focrce) {
//                showForceUpdateDialog(downLoadUrl, contentText);
//            } else {
//                showUpdateDialog(downLoadUrl, contentText);
//            }
//        }
//
//
//    }
//
//
//    public void forceUpdate(View view){
//        testCheckUpdate(true);
//
//    }
//    public void normalUpdate(View view){
//        testCheckUpdate(false);
//    }


    protected static final int REQUEST_CODE = 1;


    @Override
    protected void onRestart() {
        super.onRestart();
        mPageType = getPageType();
        AppLog.print("onRestart___mPageType_" + mPageType);
        if (mPageType == PageType.PAGE_ME_FRAGMENT) {
            showFragment(FRAGMENT_ME);
        } else if (mPageType == PageType.PAGE_HOME_FRAMENT) {
            showFragment(FRAGMENT_LIVE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppLog.print("onStart___");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLog.print("onPause___");

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLog.print("onResume___");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLog.print("onStop____");
        setIntent(new Intent());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //立即登录----home  其他---me
    public static void start(Context context, boolean imLogin) {
        Intent intent = new Intent(context, HomeActivity.class);
        AppLog.print("start imlOGIN__" + imLogin);
        if (imLogin) {
            intent.putExtra(KeyParams.PAGE_TYPE, PageType.PAGE_HOME_FRAMENT);
        } else {
            intent.putExtra(KeyParams.PAGE_TYPE, PageType.PAGE_ME_FRAGMENT);
        }
        context.startActivity(intent);
    }

    //确保intent可以传递数据
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        AppLog.print("onNewIntent____");
        setIntent(intent);
    }
}
