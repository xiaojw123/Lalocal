package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bugtags.library.Bugtags;
import com.lalocal.lalocal.MyApplication;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.LiveCache;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
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
    boolean isOnResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        isOnResume = false;
        registerObservers(false);
    }

    //监听IM账号登录状态
    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            try {
                if (statusCode == StatusCode.UNLOGIN) {
                    String userAccount = AuthPreferences.getUserAccount();
                    String userToken = AuthPreferences.getUserToken();
                    if (userAccount != null && userToken != null && isOnResume) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        loginIMServer(userAccount, userToken);
                    }
                }
                if (statusCode == StatusCode.KICKOUT) {
                    //TODO 账号被踢出
                    AppLog.print("baseActivity kiktout____");
                    if (UserHelper.isLogined(BaseActivity.this)) {
                        UserHelper.updateSignOutInfo(BaseActivity.this);
                        Toast.makeText(BaseActivity.this, "您的账号在其他设备上登录,请重新登录", Toast.LENGTH_SHORT).show();
                        LLoginActivity.start(BaseActivity.this);
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    private void loginIMServer(final String imccId, String imToken) {
        NIMClient.getService(AuthService.class).login(new LoginInfo(imccId, imToken)).setCallback(new RequestCallback() {

            @Override
            public void onSuccess(Object o) {
                AppLog.i("TAG", "BaseActivity,登录云信成功");
                LiveCache.setAccount(imccId);
                LiveCache.getRegUserInfo();
                LiveCache.setLoginStatus(true);
            }

            @Override
            public void onFailed(int i) {
                AppLog.i("TAG", "BaseActivity,登录云信失败" + i);
                LiveCache.setLoginStatus(false);
            }

            @Override
            public void onException(Throwable throwable) {
                AppLog.i("TAG", "BaseActivity,登录云信异常");
                LiveCache.setLoginStatus(false);
            }
        });
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
        isOnResume = true;
        registerObservers(true);
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
        return getIntent().getIntExtra(KeyParams.PAGE_TYPE, PageType.PAGE_DEFAULT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            if (mLoginBack && resultCode == MeFragment.LOGIN_OK) {
                AppLog.print("onActivityResult loginOk result_code___");
                setResult(resultCode, data);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
