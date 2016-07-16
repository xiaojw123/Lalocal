package com.lalocal.lalocal;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;


import com.crashlytics.android.Crashlytics;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.thread.AreaParseTask;
import com.lalocal.lalocal.util.AppLog;
import com.qihoo.updatesdk.lib.UpdateHelper;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.utils.Log;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by xiaojw on 2016/6/30.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        com.umeng.socialize.utils.Log.LOG = true;
        Log.LOG=true;
        Config.IsToastTip = true;
        AppLog.print("MyApplication onCreate___");
        LitePalApplication.initialize(this);
        UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#0A93DB"));
        Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        List<Country> countries = null;
        try {
            countries = DataSupport.findAll(Country.class);
            if (countries == null || countries.size() < 1) {
                Connector.getDatabase();
                AreaParseTask task = new AreaParseTask(this);
                task.start();
            }
        } catch (Exception e) {
            AppLog.print("未找到数据库");
        }
        //TODO:bugtags online delete
//        Bugtags.start("f0e34b0e2c605ee7f54158da0c3c08c9", this, Bugtags.BTGInvocationEventBubble);
        Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
        PlatformConfig.setWeixin("wx6117251010e95624", "9154c280dd8a7a9a6b5f57d08dae2930");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("2849578775","3b3bce66ae4671ae755fa11c2ba0ad5d");
        //新浪微博 appkey appsecret

    }



}