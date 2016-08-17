package com.lalocal.lalocal;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.easemob.chat.EMChat;
import com.lalocal.lalocal.easemob.DemoHelper;
import com.lalocal.lalocal.easemob.utils.HelpDeskPreferenceUtils;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.thread.AreaParseTask;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.liveroomview.DemoCache;
import com.lalocal.lalocal.view.liveroomview.base.util.ScreenUtil;
import com.lalocal.lalocal.view.liveroomview.base.util.crash.AppCrashHandler;
import com.lalocal.lalocal.view.liveroomview.base.util.sys.SystemUtil;
import com.lalocal.lalocal.view.liveroomview.im.config.AuthPreferences;
import com.lalocal.lalocal.view.liveroomview.im.config.UserPreferences;
import com.lalocal.lalocal.view.liveroomview.im.util.storage.StorageType;
import com.lalocal.lalocal.view.liveroomview.im.util.storage.StorageUtil;
import com.lalocal.lalocal.view.liveroomview.inject.FlavorDependent;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.pingplusplus.android.PingppLog;
import com.qihoo.updatesdk.lib.UpdateHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.utils.Log;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by xiaojw on 2016/6/30.
 * 线上版本 友盟日志关闭
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        com.umeng.socialize.utils.Log.LOG = true;
        Log.LOG=true;
        PingppLog.DEBUG = true;
        Config.IsToastTip = true;
        AppLog.print("MyApplication onCreate___");
        AppCrashHandler.getInstance(this);
        //360更新
        UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#0A93DB"));
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
        startFabric();
        startUmeng();
        //数据库
        intCountryDB();
        //代码中设置环信IM的Appkey
        String appkey = HelpDeskPreferenceUtils.getInstance(this).getSettingCustomerAppkey();
        EMChat.getInstance().setAppkey(appkey);
        // init demo helper
        DemoHelper.getInstance().init(this);
        //TODO:bugtags online delete
     //   Bugtags.start("f0e34b0e2c605ee7f54158da0c3c08c9", this, Bugtags.BTGInvocationEventBubble);
        DemoCache.setContext(this);
        NIMClient.init(this, getLoginInfo(), getOptions());
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
        android.util.Log.i("demo", FlavorDependent.getInstance().getFlavorName() + " demo nim sdk log path=" + sdkPath);

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

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

        String account = AuthPreferences.getUserAccount();
        String token = AuthPreferences.getUserToken();
        AppLog.i("TAG","MyApplication：account:"+account+"token:"+token);
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
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

        //TODO:bugtags online delete
//        Bugtags.start("f0e34b0e2c605ee7f54158da0c3c08c9", this, Bugtags.BTGInvocationEventBubble);
        Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
        PlatformConfig.setWeixin("wx6117251010e95624", "9154c280dd8a7a9a6b5f57d08dae2930");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("2849578775","3b3bce66ae4671ae755fa11c2ba0ad5d");
        //新浪微博 appkey appsecret


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
        MobclickAgent.setDebugMode(true);
    }


}