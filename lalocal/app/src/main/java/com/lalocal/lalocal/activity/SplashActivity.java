package com.lalocal.lalocal.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.easemob.Constant;
import com.lalocal.lalocal.easemob.DemoHelper;
import com.lalocal.lalocal.easemob.utils.CommonUtils;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.model.SysConfigItem;
import com.lalocal.lalocal.model.VersionInfo;
import com.lalocal.lalocal.model.VersionResult;
import com.lalocal.lalocal.model.WelcomeImg;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;


/**
 * Created by android on 2016/7/14.
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener {
    private static final int MSG_UPDATE_TIME = 0x001;
    private static final int MSG_DISPAY_IMG = 0x002;
    private static final int MSG_LOGIN_HUANXIN = 0x003;
    public static final int MSG_VERSION_UPDATE = 0x005;
    ImageView welImg;
    TextView timeTv;
    int totalTime = 0;
    SplashHandler mHandler;
    VersionResult result;

    private boolean isNotJumpt = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        welImg = (ImageView) findViewById(R.id.wel_img);
        timeTv = (TextView) findViewById(R.id.wel_time_tv);
        timeTv.setOnClickListener(this);
        mHandler = new SplashHandler();
        requestUserPermission(Manifest.permission.READ_PHONE_STATE);
    }

    public void updateVersion() {
        mHandler.sendEmptyMessage(MSG_VERSION_UPDATE);
    }

    @OnMPermissionGranted(PERMISSION_STGAT_CODE)
    public void onPermissionGranted() {
        AppLog.print("onPermissionGranted___");
        ICallBack callBack = new MyCallBack();
        setLoaderCallBack(callBack);
        loginChatService();
    }

    @OnMPermissionDenied(PERMISSION_STGAT_CODE)
    public void onPermissionDenied() {
        Toast.makeText(this, "权限被拒绝，无法继续往下执行", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        AppLog.print("onRequestPermissionsResult____");
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onClick(View v) {
        timeTv.setEnabled(false);
        if (mHandler.hasMessages(MSG_UPDATE_TIME)) {
            mHandler.removeMessages(MSG_UPDATE_TIME);
        }
        if (isNotJumpt) {
            startHomePage();
        }
    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onVersionResult(VersionInfo versionInfo) {
            AppLog.print("onVersionResult______");
            result = versionInfo.getResult();
            if (result != null) {
                String apiUrl = result.getApiUrl();
                AppConfig.setBaseUrl(apiUrl);
                mContentloader.getSystemConfigs();
            } else {
                Toast.makeText(SplashActivity.this, "系统服务异常", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(VolleyError volleyError) {
            startHomePage();
        }
        @Override
        public void onGetSysConfigs(List<SysConfigItem> items) {
            AppLog.print("onGetSysConfigs____");
            for (SysConfigItem item : items) {
                int id = item.getId();
                switch (id) {
                    case 1://通告
                    case 2://滚动速度
                    case 4://分期支付开关
                    case 5://分期支付提示语
                    case 6://直播分享文案
                    case 7://视频默认的清晰度
                    case 8://h5下开关程开发
                    case 9://直播违规警告
                    case 10://直播违规报错
                    case 11://首次充值奖励
                        break;
                    case 3://用户协议页面
                        AppConfig.setUserRuleUrl(item.getEnumValue());
                        break;
                    case 21:
                        String enumValue = item.getEnumValue();
                        LiveConstant.LIVE_DEFINITION = Integer.parseInt(enumValue);
                        break;
                    case 23://视频方向
                        String defaultDirection = item.getEnumValue();
                        LiveConstant.DEFAULT_DIRECTION=Integer.parseInt(defaultDirection);
                        break;
                }

            }
            mContentloader.getWelcommenImgs();
        }

        @Override
        public void onGetWelcomeImgs(WelcomeImg welcomeImg) {
            AppLog.print("welcommeImg_photo__" + welcomeImg.getPhoto());
            String photo = welcomeImg.getPhoto();
            if (TextUtils.isEmpty(photo)) {
                if (isNotJumpt) {
                    startHomePage();
                }
            } else {
                totalTime = welcomeImg.getSecond();
                Message message = mHandler.obtainMessage();
                message.what = MSG_DISPAY_IMG;
                message.obj = photo;
                mHandler.sendMessage(message);
            }
        }
    }


    private void startHomePage() {
        isNotJumpt = false;
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.putExtra(HomeActivity.VERSION_RESULT, result);
        startActivity(intent);
        finish();
    }


    private void loginChatService() {
        // 自动生成账号
        final String randomAccount = CommonUtils.getRandomAccount(this);
        final String userPwd = Constant.DEFAULT_ACCOUNT_PWD;
        createAccountToServer(randomAccount, userPwd, new EMCallBack() {

            @Override
            public void onSuccess() {
                //登录环信服务器
                AppLog.print("创建账号成功！！");
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
                    updateVersion();
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
                    try {
                        EMChatManager.getInstance().createAccountOnServer(uname, pwd);
                    } catch (NullPointerException e) {
                        updateVersion();
                    }
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
                try {
                    DemoHelper.getInstance().setCurrentUserName(uname);
                    DemoHelper.getInstance().setCurrentPassword(upwd);
                    EMChatManager.getInstance().loadAllConversations();
                    AppLog.print("环信账号登录成功。。。");
                } catch (Exception e) {
                    AppLog.print("环信账号登录异常");
                }
                updateVersion();
            }

            @Override
            public void onProgress(int progress, String status) {
                AppLog.print("环信账号登录过程中。。。");
            }

            @Override
            public void onError(final int code, final String message) {
                AppLog.print("环信聊天移动客服服务登录失败,errorMsg:" + message);
                updateVersion();
            }
        });
    }

    public class SplashHandler extends Handler implements ImageLoadingListener {
        @Override
        public void handleMessage(Message msg) {
            int code = msg.what;
            switch (code) {
                case MSG_UPDATE_TIME:
                    if (totalTime > 0) {
                        if (timeTv.getVisibility() != View.VISIBLE) {
                            timeTv.setVisibility(View.VISIBLE);
                        }
                        timeTv.setText(Html.fromHtml("跳过" + totalTime));
                        --totalTime;
                        sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000);
                    } else {
                        if (hasMessages(MSG_UPDATE_TIME)) {
                            removeMessages(MSG_UPDATE_TIME);
                        }
                        if (isNotJumpt) {
                            startHomePage();
                        }
                    }
                    break;
                case MSG_DISPAY_IMG:
                    DrawableUtils.displayImg(SplashActivity.this, welImg, ((String) msg.obj), -1, this);
                    break;
                case MSG_LOGIN_HUANXIN:
                    loginChatService();
                    break;
                case MSG_VERSION_UPDATE:
                    mContentloader.versionUpdate(AppConfig.getVersionName(SplashActivity.this));
                    break;
            }
        }


        @Override
        public void onLoadingStarted(String imageUri, View view) {
            AppLog.print("onLoadingStarted_____");

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            AppLog.print("onLoadingFailed____");
            if (isNotJumpt) {
                startHomePage();
            }

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            AppLog.print("onLoadingComplete____");
            sendEmptyMessage(MSG_UPDATE_TIME);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            AppLog.print("onLoadingCancelled_____");
            if (isNotJumpt) {
                startHomePage();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  registerObservers(false);
    }

}
