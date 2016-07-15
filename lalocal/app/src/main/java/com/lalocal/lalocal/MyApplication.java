package com.lalocal.lalocal;

import android.app.Application;
import android.graphics.Color;

import com.bugtags.library.Bugtags;
import com.crashlytics.android.Crashlytics;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.thread.AreaParseTask;
import com.lalocal.lalocal.util.AppLog;
import com.qihoo.updatesdk.lib.UpdateHelper;
import com.umeng.analytics.MobclickAgent;

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
        AppLog.print("MyApplication onCreate___");
        //360更新
        UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#0A93DB"));
        startFabric();
        startUmeng();
        //数据库
        intCountryDB();
        //TODO:bugtags online delete
        Bugtags.start("f0e34b0e2c605ee7f54158da0c3c08c9", this, Bugtags.BTGInvocationEventBubble);

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
