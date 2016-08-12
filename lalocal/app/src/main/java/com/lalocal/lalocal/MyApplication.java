package com.lalocal.lalocal;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.easemob.chat.EMChat;
import com.lalocal.lalocal.easemob.DemoHelper;
import com.lalocal.lalocal.easemob.utils.HelpDeskPreferenceUtils;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.thread.AreaParseTask;
import com.lalocal.lalocal.util.AppLog;
import com.pingplusplus.android.PingppLog;
import com.qihoo.updatesdk.lib.UpdateHelper;
import com.umeng.analytics.MobclickAgent;
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
 * 线上版本 友盟日志关闭
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        com.umeng.socialize.utils.Log.LOG = true;
        Log.LOG=true;
        PingppLog.DEBUG = true;
        Config.IsToastTip = true;
        AppLog.print("MyApplication onCreate___");
        //360更新
        UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#0A93DB"));
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
        startFabric();
        startUmeng();
        //数据库
        intCountryDB();
        //TODO:bugtags online delete
//        Bugtags.start("f0e34b0e2c605ee7f54158da0c3c08c9", this, Bugtags.BTGInvocationEventBubble);

        //代码中设置环信IM的Appkey
        String appkey = HelpDeskPreferenceUtils.getInstance(this).getSettingCustomerAppkey();
        EMChat.getInstance().setAppkey(appkey);
        // init demo helper
        DemoHelper.getInstance().init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void intCountryDB() {
        LitePalApplication.initialize(this);
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
    //fabric分析
    private void startFabric() {
        Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
    }
    //umeng分析
    public void startUmeng() {
        //友盟
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //设置是否对日志信息进行加密, 默认false(不加密)
        MobclickAgent.enableEncrypt(false);//6.0.0版本及以后
        MobclickAgent.setDebugMode(true);
    }


}