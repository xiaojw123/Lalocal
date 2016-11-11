package com.lalocal.lalocal.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.DestinationFragment;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.activity.fragment.NewsFragment;
import com.lalocal.lalocal.activity.fragment.RecommendNewFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.model.VersionResult;
import com.lalocal.lalocal.thread.UpdateTask;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.NormalAlertDialog;

import java.util.List;

public class HomeActivity extends BaseActivity implements MeFragment.OnMeFragmentListener {
    public static final String VERSION_RESULT = "version_result";
    LinearLayout home_recommend_tab, home_destination_tab, home_news_tab, home_me_tab;
    LinearLayout home_tab_container;
    ViewGroup lastSelectedTab;
    FragmentManager fm;
    Fragment meFragment, newsFragment, distinationFragment, recommendFragment;
    private int selected = 0;
    // 记录第一次点击back的时间
    private long clickTime = 0;

    private LocationManager locationManager;
    private String locationProvider;

    private String provider;
    private Location location;
    protected MyLocationListener locationListener = new MyLocationListener();//定位监听器

    int mPageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.print("HomeActivity__oncreate__");
        setContentView(R.layout.home_layout);
        getLocation();
        initView();
        checkUpdate();
    }



    private void checkUpdate() {
        VersionResult result = getIntent().getParcelableExtra(VERSION_RESULT);
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
        showFragment(home_news_tab);
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
//        showFragment(home_recommend_tab);
        showFragment(home_news_tab);
    }

    public static final int FRAGMENT_NEWS = 0;
    public static final int FRAGMENT_DESTINATION = 1;
    public static final int FRAGMENT_RECOMMEND = 2;
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

                    showFragment(home_news_tab);
                    break;
                case FRAGMENT_ME:
                    showFragment(home_me_tab);
                    break;
            }
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
private String judgeProvider(LocationManager locationManager) {
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    criteria.setAltitudeRequired(false);
    criteria.setBearingRequired(false);
    criteria.setCostAllowed(true);
    criteria.setPowerRequirement(Criteria.POWER_LOW);
    String provider =this.locationManager.getBestProvider(criteria, false);

    return provider;
}
    protected static final int REQUEST_CODE = 1;
    protected static int denyCount = 0; //记录拒绝次数  
    private void getLocation() {
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                }else{

                   /* if (denyCount >= 2){
                        Log.d("MQL", "亲爱的用户,请到您的权限管理中打开");
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", "com.analysys.locationdemo", null);
                        intent.setData(uri);
                       startActivity(intent);
                    }*/
                }

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

            } else {

               AppLog.i("MQL","系统版小于23");
            }

        } else {
            AppLog.i("MQL","系统版小于23，哈哈哈哈哈");
            this.locationManager.requestLocationUpdates("network", 1000*10*60, 3000,locationListener);
        }

    }

    private String getBestProvider() {
        //获取最合适的provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider =locationManager.getBestProvider(criteria, false);

        //检查位置提供者是否启用
        //在设置里，一些位置提供者比如GPS可以被关闭。良好的做法就是通过检测你想要的位置提供者是否打开。
        //如果位置提供者被关闭了，你可以在设置里通过启动Intent来让用户打开。
     /*   if (locationManager.isProviderEnabled(provider) == false) {

            return null;
        }*/


        return provider;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (REQUEST_CODE == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
              /*  String provider = this.getBestProvider();
                if (provider != null){
                    this.locationManager.requestLocationUpdates("network", 1000*60*30, 5000,locationListener);
                }*/
                this.locationManager.requestLocationUpdates("network", 1000*60*10, 5000,locationListener);

            }else{
                denyCount++;
            }
        }

    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            String longitude = location.getLongitude() + "";
            String latitude = location.getLatitude() + "";
            CommonUtil.LATITUDE=latitude;
            CommonUtil.LONGITUDE=longitude;


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {


        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }





    @Override
    protected void onRestart() {
        super.onRestart();
        mPageType = getPageType();
        AppLog.print("onRestart___mPageType_" + mPageType);
        if (mPageType == PageType.PAGE_ME_FRAGMENT) {
            showFragment(home_me_tab);
        } else if (mPageType == PageType.PAGE_HOME_FRAMENT) {
            showFragment(home_news_tab);
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
        AppLog.print("start imlOGIN__"+imLogin);
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
