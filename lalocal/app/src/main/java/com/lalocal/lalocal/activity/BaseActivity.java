package com.lalocal.lalocal.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.umeng.analytics.MobclickAgent;

import butterknife.Unbinder;
/*
*
* Activity基类
*集成了友盟 ，bugtags sdk
*线上版本日志加密开启
* */

public class BaseActivity extends AppCompatActivity {
    public static final int PERMISSION_STGAT_CODE = 1123;
    Unbinder unbinder;
    ContentLoader mContentloader;
    View mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }

    //页面全屏加载loading显示
    public void showLoadingAnimation() {
        if (mLoadingView == null) {
            FrameLayout container = (FrameLayout) getWindow().getDecorView();
            mLoadingView = LayoutInflater.from(this).inflate(R.layout.page_base_loading, container, false);
            container.addView(mLoadingView);
        } else {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    //页面全屏加载loading隐藏
    public void hidenLoadingAnimation() {
        if (mLoadingView != null&&mLoadingView.getVisibility()==View.VISIBLE) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    public void setLoaderCallBack(ICallBack callBack) {
        if (mContentloader == null) {
            mContentloader = new ContentLoader(this);
        }
        mContentloader.setCallBack(callBack);
    }


    public void requestUserPermission(String... permissions) {
        AppLog.print("requestUserPermission___");
        MPermission.with(this)
                .addRequestCode(PERMISSION_STGAT_CODE)
                .permissions(permissions)
                .request();
    }


    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    //TODO:bugtags online delete
    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
     //   Bugtags.onResume(this);
        MobclickAgent.onResume(this);
    }

    //TODO:bugtags online delete
    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
     //   Bugtags.onPause(this);
        MobclickAgent.onPause(this);
    }

    //TODO:bugtags online delete
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
     //   Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    public int getPageType() {
        return getIntent().getIntExtra(KeyParams.PAGE_TYPE, 0);
    }

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
