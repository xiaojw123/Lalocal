package com.lalocal.lalocal.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.TargetPage;
import com.lalocal.lalocal.help.UserHelper;
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
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

import java.util.List;


/**
 * Created by android on 2016/7/14.
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener {
    private static final int MSG_UPDATE_TIME = 0x001;
    private static final int MSG_DISPAY_IMG = 0x002;
    public static final int MSG_VERSION_UPDATE = 0x005;
    ImageView welImg;
    TextView timeTv;
    int totalTime = 0;
    SplashHandler mHandler;
    VersionResult result;
    private boolean isNotJumpt = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private boolean isAdJumpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        AppLog.print("splash create");
        welImg = (ImageView) findViewById(R.id.wel_img);
        timeTv = (TextView) findViewById(R.id.wel_time_tv);
        welImg.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        mHandler = new SplashHandler();
        requestUserPermission(Manifest.permission.READ_PHONE_STATE);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void updateVersion() {
        mHandler.sendEmptyMessage(MSG_VERSION_UPDATE);
    }

    @OnMPermissionGranted(PERMISSION_STGAT_CODE)
    public void onPermissionGranted() {
        AppLog.print("onPermissionGranted___");
        ICallBack callBack = new MyCallBack();
        setLoaderCallBack(callBack);
        updateVersion();
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
    protected void onRestart() {
        super.onRestart();
        if (isAdJumpt) {
            isAdJumpt = false;
            startHomePage();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wel_img:
                Object tagObj = welImg.getTag();
                if (tagObj != null) {
                    WelcomeImg welImg = (WelcomeImg) tagObj;
                    int targetType=welImg.getTargetType();
                        switch (targetType){
                            case -1://链接
                                removeUpdateTime();
                                TargetPage.gotoWebDetail(this, welImg.getTargetUrl(),welImg.getTargetName(),false);
                                break;
                            case 0://用户
                                removeUpdateTime();
                                TargetPage.gotoUser(this, String.valueOf(welImg.getTargetId()),false);
                                break;
                            case 1://文章
                            case 13://资讯
                                removeUpdateTime();
                                TargetPage.gotoArticleDetail(this, String.valueOf(welImg.getTargetId()),false);
                                break;
                            case 2://产品
                                removeUpdateTime();
                                TargetPage.gotoProductDetail(this, String.valueOf(welImg.getTargetId()), targetType,false);
                                break;
                            case 9://线路
                                removeUpdateTime();
                                TargetPage.gotoRouteDetail(this, welImg.getTargetId(),false);
                                break;
                            case 10://专题
                                removeUpdateTime();
                                TargetPage.gotoSpecialDetail(this, String.valueOf(welImg.getTargetId()),false);
                                break;
                            case 15://直播-视频
                                removeUpdateTime();
                                TargetPage.gotoLive(this, String.valueOf(welImg.getTargetId()),false);
                                break;
                            case 20://回放
                                removeUpdateTime();
                                TargetPage.gotoPlayBack(this, String.valueOf(welImg.getTargetId()),false);
                                break;
                        }
                }

                break;
            case R.id.wel_time_tv:
                timeTv.setEnabled(false);
                if (mHandler.hasMessages(MSG_UPDATE_TIME)) {
                    mHandler.removeMessages(MSG_UPDATE_TIME);
                }
                if (isNotJumpt) {
                    startHomePage();
                }
                break;
        }
    }

    private void removeUpdateTime() {
        isAdJumpt = true;
        if (mHandler.hasMessages(MSG_UPDATE_TIME)) {
            mHandler.removeMessages(MSG_UPDATE_TIME);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Splash Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class MyCallBack extends ICallBack {
        //添加tag 添加alias
        @Override
        public void onResponseGetTags(final List<String> tags) {
            if (tags != null && tags.size() > 0) {
                final PushAgent mPushAgent = PushAgent.getInstance(SplashActivity.this);
                mPushAgent.getTagManager().list(new TagManager.TagListCallBack() {
                    @Override
                    public void onMessage(boolean isSuccess, List<String> result) {
                        AppLog.print("获取 taglist");
                        if (result != null && result.size() > 0) {
                            for (String tag : tags) {// 1  5  6      4
                                if (!result.contains(tag)) {
                                    //add
                                    mPushAgent.getTagManager().add(new TagManager.TCallBack() {

                                        @Override
                                        public void onMessage(boolean b, ITagManager.Result result) {
                                            AppLog.print("添加 tag成功");
                                        }
                                    }, tag);
                                }
                            }

                        } else {
                            String tagArray[] = new String[tags.size()];
                            mPushAgent.getTagManager().add(new TagManager.TCallBack() {
                                @Override
                                public void onMessage(boolean b, ITagManager.Result result) {
                                    AppLog.print("添加 tags成功");
                                }
                            }, tags.toArray(tagArray));
                        }


                    }
                });
            }
            mContentloader.getSystemConfigs();
        }

        @Override
        public void onVersionResult(VersionInfo versionInfo) {
            AppLog.print("onVersionResult______");
            result = versionInfo.getResult();
            if (result != null) {
                String apiUrl = result.getApiUrl();
                AppConfig.setBaseUrl(apiUrl);
                if (UserHelper.isLogined(SplashActivity.this)) {
                    mContentloader.getPushTags();
                } else {
                    mContentloader.getSystemConfigs();
                }
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
                        LiveConstant.DEFAULT_DIRECTION = Integer.parseInt(defaultDirection);
                        break;
                }

            }
            mContentloader.getWelcommenImgs();
        }

        @Override
        public void onGetWelcomeImgs(WelcomeImg welcomeImg) {
            welImg.setTag(welcomeImg);
            if (welcomeImg != null) {
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
            } else {
                if (isNotJumpt) {
                    startHomePage();
                }
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
