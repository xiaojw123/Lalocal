package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.bugtags.library.Bugtags;
import com.lalocal.lalocal.MyApplication;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
/*
*
* Activity基类
*集成了友盟 ，bugtags sdk
*线上版本日志加密开启
* */

public class BaseActivity extends AppCompatActivity {
    public static final int PERMISSION_STGAT_CODE = 1123;
    public ContentLoader mContentloader;
    View mLoadingView;
    boolean mLoginBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

    //mLoginBack/true登录页面登录成功时，依次清空回退栈并将数据回传，规避resultcode重复情况
    public void setLoginBackResult(boolean loginBack) {
        mLoginBack = loginBack;
    }

    //页面全屏加载loading隐藏
    public void hidenLoadingAnimation() {
        if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
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
    protected void onResume() {
        super.onResume();
        //注：回调 1
        if (MyApplication.isDebug) {
            Bugtags.onResume(this);
        } else {
            MobclickAgent.onPageStart(getClass().getName());
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        if (MyApplication.isDebug) {
            Bugtags.onPause(this);
        } else {
            MobclickAgent.onPageEnd(getClass().getName());
            MobclickAgent.onPause(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        if (MyApplication.isDebug) {
            Bugtags.onDispatchTouchEvent(this, event);
        }
        return super.dispatchTouchEvent(event);
    }

    public int getPageType() {
        return getIntent().getIntExtra(KeyParams.PAGE_TYPE, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            if (mLoginBack && resultCode == LoginActivity.LOGIN_OK) {
                setResult(resultCode, data);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
