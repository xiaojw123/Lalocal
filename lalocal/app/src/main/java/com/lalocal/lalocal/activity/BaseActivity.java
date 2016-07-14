package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
/*
*
* Activity基类 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }
//TODO:bugtags online delete
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //注：回调 1
//        Bugtags.onResume(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //注：回调 2
//        Bugtags.onPause(this);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        //注：回调 3
//        Bugtags.onDispatchTouchEvent(this, event);
//        return super.dispatchTouchEvent(event);
//    }
}
