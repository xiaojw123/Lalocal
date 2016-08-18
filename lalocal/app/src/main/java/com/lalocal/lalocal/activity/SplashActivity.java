package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.easemob.Constant;
import com.lalocal.lalocal.easemob.DemoHelper;
import com.lalocal.lalocal.easemob.utils.CommonUtils;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.VersionInfo;
import com.lalocal.lalocal.model.VersionResult;
import com.lalocal.lalocal.model.WelcomeImg;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.liveroomview.DemoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * Created by android on 2016/7/14.
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener {
    private static final int UPDATE_TIME = 0x001;
    ImageView startImg;
    ImageView welImg;
    TextView timeTv;
    int totalTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        welImg = (ImageView) findViewById(R.id.wel_img);
        timeTv = (TextView) findViewById(R.id.wel_time_tv);
        startImg = (ImageView) findViewById(R.id.wel_start_img);
        timeTv.setOnClickListener(this);
        setLoaderCallBack(new MyCallBack());
        mContentloader.versionUpdate(AppConfig.getVersionName(this));
        registerObservers(true);

    }

    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            if (statusCode.wontAutoLogin()) {
                AppLog.i("TAG","NewsFragment:密码错误等");
            }else if(statusCode.shouldReLogin()) {

                final String imccId = UserHelper.getImccId(SplashActivity.this);
                String imToken = UserHelper.getImToken(SplashActivity.this);
                if (imccId != null || imToken != null) {
                    AppLog.i("TAG","SplashActivity没有登录并走了这里1："+"imccId:"+imccId+"   imToken:"+imToken);
                    NIMClient.getService(AuthService.class).login(new LoginInfo(imccId, imToken)).setCallback(new RequestCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            AppLog.i("TAG", "SplashActivity,手动登录成功");
                            DemoCache.setAccount(imccId);
                        }

                        @Override
                        public void onFailed(int i) {
                            AppLog.i("TAG", "SplashActivity,手动登录失败" + i);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            AppLog.i("TAG", "SplashActivity,手动登录失败2");
                        }
                    });
                }
            }

        }
    };

    @Override
    public void onClick(View v) {
        if (mHandler.hasMessages(UPDATE_TIME)) {
            mHandler.removeMessages(UPDATE_TIME);
        }
        loginChatService();
    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onVersionResult(VersionInfo versionInfo) {
            VersionResult result = versionInfo.getResult();
            String apiUrl = result.getApiUrl();
            AppConfig.setBaseUrl(apiUrl);
            mContentloader.getWelcommenImgs();
        }

        @Override
        public void onGetWelcomeImgs(WelcomeImg welcomeImg) {
            AppLog.print("welcommeImg_photo__" + welcomeImg.getPhoto());
            String photo = welcomeImg.getPhoto();
            if (TextUtils.isEmpty(photo)) {
                loginChatService();
            } else {
                DrawableUtils.displayImg(SplashActivity.this, welImg, welcomeImg.getPhoto());
                totalTime = welcomeImg.getSecond();
                startImg.setVisibility(View.GONE);
                mHandler.sendEmptyMessage(UPDATE_TIME);
            }
        }
    }

    private void startHomePage() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    public void loginChatService() {
        if (EMChat.getInstance().isLoggedIn()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        //加载本地数据库中的消息到内存中
                        EMChatManager.getInstance().loadAllConversations();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startHomePage();
                }
            }).start();
        } else {
            //根据deviceId创建一个用户并登录环信服务器
            createRandomAccountAndLoginChatServer();
        }

    }

    private void createRandomAccountAndLoginChatServer() {
        // 自动生成账号
        final String randomAccount = CommonUtils.getRandomAccount(this);
        final String userPwd = Constant.DEFAULT_ACCOUNT_PWD;
        createAccountToServer(randomAccount, userPwd, new EMCallBack() {

            @Override
            public void onSuccess() {
                //登录环信服务器
                loginHuanxinServer(randomAccount, userPwd);
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int errorCode, final String message) {
                if (errorCode == EMError.USER_ALREADY_EXISTS) {
                    AppLog.print("用户已存在！！！");
                    loginHuanxinServer(randomAccount, userPwd);
                } else {
                    if (errorCode == EMError.NONETWORK_ERROR) {
                        AppLog.print("网络不可用！！！");
                    } else if (errorCode == EMError.UNAUTHORIZED) {
                        AppLog.print("无开放注册权限！！！");
                    } else if (errorCode == EMError.ILLEGAL_USER_NAME) {
                        AppLog.print("用户名非法！！！");
                    } else {
                        AppLog.print("注册失败！！！");
                    }
                    startHomePage();
                }
            }
        });
    }

    //注册用户
    private void createAccountToServer(final String uname, final String pwd, final EMCallBack callback) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    EMChatManager.getInstance().createAccountOnServer(uname, pwd);
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } catch (EaseMobException e) {
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.getMessage());
                    }
                }
            }
        });
        thread.start();
    }

    public void loginHuanxinServer(final String uname, final String upwd) {
        // login huanxin server
        EMChatManager.getInstance().login(uname, upwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                AppLog.print("环信账号登录成功。。。");
                DemoHelper.getInstance().setCurrentUserName(uname);
                DemoHelper.getInstance().setCurrentPassword(upwd);
                try {
                    EMChatManager.getInstance().loadAllConversations();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                startHomePage();
            }

            @Override
            public void onProgress(int progress, String status) {
                AppLog.print("环信账号登录过程中。。。");
            }

            @Override
            public void onError(final int code, final String message) {
                AppLog.print("环信聊天移动客服服务登录失败,errorMsg:" + message);
                startHomePage();
            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_TIME) {
                if (totalTime > 0) {
                    if (timeTv.getVisibility() != View.VISIBLE) {
                        timeTv.setVisibility(View.VISIBLE);
                    }
                    timeTv.setText(Html.fromHtml("跳过" + totalTime));
                    --totalTime;
                    sendEmptyMessageDelayed(UPDATE_TIME, 1000);
                } else {
                    if (hasMessages(UPDATE_TIME)) {
                        removeMessages(UPDATE_TIME);
                    }
                    loginChatService();
                }

            }


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }
}
