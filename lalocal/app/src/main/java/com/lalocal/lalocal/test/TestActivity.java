package com.lalocal.lalocal.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.PayActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.ProgressButton;


public class TestActivity extends BaseActivity implements View.OnClickListener {
    ProgressButton pb,pb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_widget);
        pb=(ProgressButton) findViewById(R.id.progressView);
        pb2= (ProgressButton) findViewById(R.id.progressView2);
        pb.setOnClickListener(this);
        pb2.setOnClickListener(this);
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        };
//        timerTask.cancel();
//        Timer timer=new Timer();
//        timer.cancel();

}
    public void payment(View view){
        Intent intent=new Intent(this,PayActivity.class);
        intent.putExtra(PayActivity.ORDER_ID,1287);
        intent.putExtra(KeyParams.ACTION_TYPE,KeyParams.ACTION_BOOK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        AppLog.print("click____");
        mHandler.sendEmptyMessageDelayed(0,1000);
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            pb.stopLoadingAnimation();
            pb2.stopLoadingAnimation();
        }
    };
}
