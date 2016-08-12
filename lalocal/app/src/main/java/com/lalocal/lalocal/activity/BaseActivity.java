package com.lalocal.lalocal.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.umeng.analytics.MobclickAgent;

import butterknife.Unbinder;
/*
*
* Activity基类
*集成了友盟 ，bugtags sdk
*线上版本日志加密开启
* */

public class BaseActivity extends AppCompatActivity {
    Unbinder unbinder;
    ContentLoader mContentloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContentloader = new ContentLoader(this);
    }

    public void setLoaderCallBack(ICallBack callBack) {
        mContentloader.setCallBack(callBack);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    //TODO:bugtags online delete
    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
//        Bugtags.onResume(this);
        MobclickAgent.onResume(this);
    }

    //TODO:bugtags online delete
    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
//        Bugtags.onPause(this);
        MobclickAgent.onPause(this);
    }

    //TODO:bugtags online delete
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        //注：回调 3
//        Bugtags.onDispatchTouchEvent(this, event);
//        return super.dispatchTouchEvent(event);
//    }

    /**
     * 通过xml查找相应的ID，通用方法
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }
}
