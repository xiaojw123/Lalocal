package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.easemob.Constant;
import com.lalocal.lalocal.easemob.DemoHelper;
import com.lalocal.lalocal.easemob.utils.CommonUtils;
import com.lalocal.lalocal.model.VersionInfo;
import com.lalocal.lalocal.model.VersionResult;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;

/**
 * Created by android on 2016/7/14.
 */
public class WelcomeActivity extends BaseActivity {
    private ContentLoader contentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        contentService = new ContentLoader(this);
        contentService.setCallBack(new MyCallBack());
        contentService.versionUpdate(AppConfig.getVersionName(this));

    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onVersionResult(VersionInfo versionInfo) {
            VersionResult result = versionInfo.getResult();
            String apiUrl = result.getApiUrl();
           // AppConfig.setBaseUrl(apiUrl);
            loginChatService();
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void startHomePage() {
        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
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
}
