package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.VersionInfo;
import com.lalocal.lalocal.model.VersionResult;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
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
        contentService.versionUpdate(AppConfig.getVersionName(this));

    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onVersionResult(VersionInfo versionInfo) {
            VersionResult result = versionInfo.getResult();
            String apiUrl = result.getApiUrl();
           // AppConfig.setBaseUrl(apiUrl);
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
