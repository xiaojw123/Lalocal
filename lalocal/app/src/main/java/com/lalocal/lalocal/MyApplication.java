package com.lalocal.lalocal;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.bugtags.library.Bugtags;
import com.crashlytics.android.Crashlytics;
import com.lalocal.lalocal.help.TargetPage;
import com.lalocal.lalocal.help.TargetType;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.util.ScreenUtil;
import com.lalocal.lalocal.live.base.util.crash.AppCrashHandler;
import com.lalocal.lalocal.live.base.util.sys.SystemUtil;
import com.lalocal.lalocal.live.entertainment.agora.openlive.WorkerThread;
import com.lalocal.lalocal.live.entertainment.ui.ApngImageLoader;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.im.config.UserPreferences;
import com.lalocal.lalocal.live.im.util.storage.StorageType;
import com.lalocal.lalocal.live.im.util.storage.StorageUtil;
import com.lalocal.lalocal.live.inject.FlavorDependent;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.thread.AreaParseTask;
import com.lalocal.lalocal.util.AppLog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.pingplusplus.android.PingppLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.utils.Log;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

;


/**
 * Created by xiaojw on 2016/6/30.
 * 【APP上线注意事项】
 * <p>
 * 1.为区分线上/线下版本，版本号定义如下
 * 线下版本：版本号=版本名称数字
 * 线上版本：版本号=版本名称数字+1
 * eg:版本名称：2.1.3
 * 线下版本号：213
 * 线上版本号：214
 * 此约定从2.1.3版本开始生效
 * <p>
 * 2.设置isDebug=false
 * 日志关闭
 * bugTag关闭
 * 友盟统计开启
 * fabric开启
 * <p>
 * 3.baseUrl改为生产环境
 * 解决app崩溃后退回开发环境问题
 * <p>
 * 4.云信key(线上)
 * 5.
 * 5-1 第三方加固
 * meta-data选项选择UMENG_CHANNEL
 * 根据不同市场设置对应value，生成
 * 相应渠道包
 * 5-2 重新签名
 * 此约定从2.1.3版本开始生效
 */
public class MyApplication extends Application {
    public static final boolean isDebug =true;
    private WorkerThread mWorkerThread;
    @Override
    public void onCreate() {
        super.onCreate();
        AppLog.print("Application create start");
        initLogManager();
        Config.IsToastTip = true;
        AppCrashHandler.getInstance(this);
        if (isDebug) {
            Bugtags.start("165db764bcad149e50e319635e6a614c", this, Bugtags.BTGInvocationEventBubble);
        } else {
            startFabric();
            startUmeng();
        }
        initUPsuh();
        AppLog.print("initUPush");

        //数据库
        intCountryDB();
        AppLog.print("initDB");
        //初始化Apng
    //    initApngImageLoader();
        DemoCache.setContext(this);
        AppLog.print("init Live Cache");
        NIMClient.init(this, getLoginInfo(), getOptions());
        AppLog.print("init NIMClient");
        if (inMainProcess()) {
            // 注册自定义消息附件解析器
            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(FlavorDependent.getInstance().getMsgAttachmentParser());
            // init tools
            StorageUtil.init(this, null);
            ScreenUtil.init(this);
            DemoCache.initImageLoaderKit();
            initLog();
            FlavorDependent.getInstance().onApplicationCreate();
        }
        AppLog.print("Application create end");
    }

    private void initApngImageLoader() {
        ApngImageLoader apngImageLoader = ApngImageLoader.getInstance();
        apngImageLoader.setEnableDebugLog(false);
        apngImageLoader.setEnableVerboseLog(false);
        apngImageLoader.init(this);

    }


    private void initUPsuh() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 自定义消息的回调方法
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                AppLog.print("dealWithCustomMessage____");
                super.dealWithCustomMessage(context, msg);
            }

            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);


        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            //打开链接
            @Override
            public void openUrl(Context context, UMessage uMessage) {
                AppLog.print("UMessage____openUrl__text__" + uMessage.text);
                super.openUrl(context, uMessage);
            }

            //打开应用
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                AppLog.print("UMessage____launchApp");
                if (uMessage != null) {
                    Map<String, String> data = uMessage.extra;
                    for (Map.Entry entry:data.entrySet()){
                        AppLog.print("key:"+entry.getKey()+", value:"+entry.getValue()+"\n");
                    }
                    if (data != null && data.size() > 0) {
                        String targetType = data.get("targetType");
                        String targetId = data.get("targetId");
                        String targetUrl = data.get("targetUrl");
                        String targetName = data.get("targetName");
                        AppLog.print("推送custom params@如下  \ntargetType：" + targetType + "\ntargetId: " + targetId + "\ntargetName: " + targetName);
                        if (targetType != null) {
                            switch (targetType) {
                                case TargetType.URL://链接
                                    TargetPage.gotoWebDetail(context, targetUrl, targetName, true);
                                    break;
                                case TargetType.USER://用户
                                    TargetPage.gotoUser(context, targetId, true);
                                    break;
                                case TargetType.ARTICLE://文章
                                case TargetType.INFORMATION://资讯
                                    TargetPage.gotoArticleDetail(context, targetId, true);
                                    break;
                                case TargetType.PRODUCT://产品
                                    TargetPage.gotoProductDetail(context, targetId, Integer.parseInt(targetType), true);
                                    break;
                                case TargetType.ROUTE://线路
                                    TargetPage.gotoRouteDetail(context, Integer.parseInt(targetId), true);
                                    break;
                                case TargetType.SPECIAL://专题
                                    TargetPage.gotoSpecialDetail(context, targetId, true);
                                    break;
                                case TargetType.LIVE_VIDEO://直播视频
                                    TargetPage.gotoLive(context, targetId, true);
                                    break;
                                case TargetType.LIVE_PALY_BACK://回放
                                    TargetPage.gotoPlayBack(context, targetId, true);
                                    break;

                            }
                        }

                    }
                }
            }

            //打开指定页面
            @Override
            public void openActivity(Context context, UMessage uMessage) {
                AppLog.print("UMessage____openActivity");
                super.openActivity(context, uMessage);
            }

            //自定义行为
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                AppLog.print("UMessage____dealWithCustomAction");
                super.dealWithCustomAction(context, msg);
//                for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
//                    String key = entry.getKey();
//                    String value = entry.getValue();
//                    AppLog.print("Application__dealWithCustomAction@ key:"+key+", value:"+value);
//                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
//注册推送服务，每次调用register方法都会回调该接口c
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                AppLog.print("uPush deviceToken____" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });


    }

    private static void initLogManager() {
        Log.LOG = isDebug;
        PingppLog.DEBUG = isDebug;
        if (isDebug) {
            AppLog.debug_level = 0;
        } else {
            AppLog.debug_level = 8;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();
        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
        if (config == null) {
            config = new StatusBarNotificationConfig();
        }

        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;
        UserPreferences.setStatusConfig(config);

        // 配置保存图片，文件，log等数据的目录
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim/";
        options.sdkStorageRootPath = sdkPath;
        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = false;

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = (int) (0.5 * ScreenUtil.screenWidth);

        // 用户信息提供者
        options.userInfoProvider = null;

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = null;

        return options;
    }

    private boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private void initLog() {
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
    }

    private LoginInfo getLoginInfo() {
        String imccId = AuthPreferences.getUserAccount();
        String imToken = AuthPreferences.getUserToken();
        AppLog.i("TAG", "MyApplication：account:" + imccId + "token:" + imToken);
        if (!TextUtils.isEmpty(imccId) && !TextUtils.isEmpty(imToken)) {
            DemoCache.setAccount(imccId.toLowerCase());
            return new LoginInfo(imccId, imToken);
        } else {
            return null;
        }

    }

    private void intCountryDB() {
        LitePalApplication.initialize(this);
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

        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        PlatformConfig.setWeixin("wx6117251010e95624", "9154c280dd8a7a9a6b5f57d08dae2930");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("2849578775", "3b3bce66ae4671ae755fa11c2ba0ad5d");
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone("1105529194", "XONwXa348plDFJGf");


    }

    //fabric分析
    private void startFabric() {
        Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
    }

    //umeng分析
    public void startUmeng() {
        //友盟
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //设置是否对日志信息进行加密, 默认false(不加密)
        MobclickAgent.enableEncrypt(false);//6.0.0版本及以后
//        MobclickAgent.setDebugMode(true);
    }


    //声网
    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();
            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }
}