package com.lalocal.lalocal.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.PayActivity;
import com.lalocal.lalocal.help.KeyParams;


public class TestActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_widget);
    }

    public void payment(View view){
        Intent intent=new Intent(this,PayActivity.class);
        intent.putExtra(PayActivity.ORDER_ID,1287);
        intent.putExtra(KeyParams.ACTION_TYPE,KeyParams.ACTION_BOOK);
        startActivity(intent);
        finish();
    }

}
