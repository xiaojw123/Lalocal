package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.VersionInfo;
import com.lalocal.lalocal.model.VersionResult;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.qihoo.updatesdk.lib.UpdateHelper;

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
        contentService.versionUpdate("1.0.2");

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public class MyCallBack extends ICallBack {
        @Override
        public void onRequestFailed() {
            super.onRequestFailed();
        }

        @Override
        public void onVersionResult(VersionInfo versionInfo) {

            VersionResult result = versionInfo.getResult();
            String apiUrl = result.getApiUrl();
            AppConfig.getInstance().BASE_URL=apiUrl;
            AppLog.i("TAG","apiUrl:"+apiUrl+"BASE_URL:"+ AppConfig.getInstance().BASE_URL+"/hhhhhhhh:"+AppConfig.getInstance().CHECK_EMAIL_URL);
           // UpdateHelper.getInstance().manualUpdate("com.lalocal.lalocal");
            Intent intent=new Intent(WelcomeActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
