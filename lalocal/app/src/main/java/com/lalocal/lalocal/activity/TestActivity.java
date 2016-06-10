package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.APPcofig;
import com.lalocal.lalocal.util.AppLog;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AppLog.print("android uuid _____"+ APPcofig.getVersionName(this));
    }
}
