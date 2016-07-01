package com.lalocal.lalocal;

import android.app.Application;

import com.bugtags.library.Bugtags;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by xiaojw on 2016/6/30.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        //TODO:bugtags online delete
        Bugtags.start("f0e34b0e2c605ee7f54158da0c3c08c9", this, Bugtags.BTGInvocationEventBubble);
    }
}
