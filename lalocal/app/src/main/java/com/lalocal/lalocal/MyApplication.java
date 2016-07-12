package com.lalocal.lalocal;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.thread.AreaParseTask;
import com.lalocal.lalocal.util.AppLog;

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
        AppLog.print("MyApplication onCreate___");
        LitePalApplication.initialize(this);
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

    }

}
