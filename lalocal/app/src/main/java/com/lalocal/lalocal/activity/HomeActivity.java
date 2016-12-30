package com.lalocal.lalocal.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.FindFragment;
import com.lalocal.lalocal.activity.fragment.LiveFragment;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.model.VersionResult;
import com.lalocal.lalocal.thread.UpdateTask;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.NormalAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class HomeActivity extends BaseActivity implements MeFragment.OnMeFragmentListener{
    private static final int READ_WRITE_SDCARD_CODE = 0x126;

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
    @BindView(R.id.home_unread_tv)
    public TextView unReadTv;

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
    String mUpgradeUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        // 使用ButterKnife框架
        ButterKnife.bind(this);
        // 显示直播fragment
        showFragment(FRAGMENT_LIVE);
        updateUnReadMsg();
        //关闭通知栏提醒
        NIMClient.toggleNotification(false);
        // 检查更新
        checkUpdate();
        //初始化定位
        if (Build.VERSION.SDK_INT >= 22) {
            // 请求用户权限
            reminderUserPermission(INIT);
        } else {
            // 初始化
            initLocation();
        }

    }

    private static final int INIT = 0x01;
    private static final String[] RW_SD_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private final int LIVE_PERMISSION_RW_EXTERNAL_STORAGE_CODE = 100;
    @TargetApi(Build.VERSION_CODES.M)
    private void reminderUserPermission(int init) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(RW_SD_PERMISSIONS, LIVE_PERMISSION_RW_EXTERNAL_STORAGE_CODE);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(RW_SD_PERMISSIONS, LIVE_PERMISSION_RW_EXTERNAL_STORAGE_CODE);
        } else {
            // 初始化
            initLocation();
        }

    }

    private void doNext(int requestCode, int[] grantResults) {
        if(LIVE_PERMISSION_RW_EXTERNAL_STORAGE_CODE==requestCode){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 初始化
                initLocation();
            } else {
                Toast.makeText(this, "没有地理位置权限", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        startLocation();
    }


    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);

    }


    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation loc) {
            try {
                String errorInfo = loc.getErrorInfo();
                if("success".equals(errorInfo)){
                    CommonUtil.LATITUDE=String.valueOf(loc.getLatitude());
                    CommonUtil.LONGITUDE=String.valueOf(loc.getLongitude());
                    String result = loc.getCountry() + "·" + loc.getProvince() + "·" + loc.getCity();
                    CommonUtil.LOCATION_RESULT=result;
                    CommonUtil.LOCATION_Y =true;
                }else{
                    CommonUtil.LOCATION_Y = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setNeedAddress(true);
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        return mOption;
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

    @OnClick({R.id.layout_tab_live, R.id.layout_tab_find, R.id.home_tab_me, R.id.home_tab_search})
    void clickButton(View view) {
        switch (view.getId()) {
            case R.id.layout_tab_live:
                showFragment(FRAGMENT_LIVE);
                break;
            case R.id.layout_tab_find:
                showFragment(FRAGMENT_FIND);
                break;
            case R.id.home_tab_me:
                showFragment(FRAGMENT_ME);
                break;
            case R.id.home_tab_search:
                gotoSearch();
                break;
        }
    }

    private void gotoSearch() {
        Intent intent = new Intent(this, GlobalSearchActivity.class);
        startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mFragLive != null) {
            mFragLive.onActivityResult(requestCode, resultCode, data);
        }
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
                requestSDCardPermission(dowloadUrl);
            }
        });
        forceDailog.show();
    }

    private void requestSDCardPermission(String url) {
        mUpgradeUrl = url;
        MPermission.with(HomeActivity.this)
                .addRequestCode(READ_WRITE_SDCARD_CODE)
                .permissions(READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    @OnMPermissionGranted(READ_WRITE_SDCARD_CODE)
    public void onPermissionGranted() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            update(mUpgradeUrl);
        } else {
            Toast.makeText(HomeActivity.this, "无可用存储空间",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnMPermissionDenied(READ_WRITE_SDCARD_CODE)
    public void onPermissionDenied() {
        Toast.makeText(this, getResources().getString(R.string.live_read_write_external_storage), Toast.LENGTH_SHORT).show();
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
                requestSDCardPermission(dowloadUrl);
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
        registerMsgServicesObser(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLog.print("onResume___");
        registerMsgServicesObser(true);
        updateUnReadMsg();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLog.print("onStop____");
        setIntent(new Intent());
    }


    @Override
    protected void onDestroy() {
        NIMClient.toggleNotification(true);
        destroyLocation();
        super.onDestroy();
    }

    private void destroyLocation() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;

        }
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



    //  创建观察者对象
    Observer<List<RecentContact>> messageObserver =
            new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(List<RecentContact> messages) {
                    AppLog.print("messageObserver____onEvent___");
                    updateUnReadMsg();
                }
            };

    public void registerMsgServicesObser(boolean flag) {
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, flag);
    }

    public void updateUnReadMsg() {
        int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        if (unreadNum > 0) {
            unReadTv.setVisibility(View.VISIBLE);
        } else {
            unReadTv.setVisibility(View.INVISIBLE);
        }

    }
}
