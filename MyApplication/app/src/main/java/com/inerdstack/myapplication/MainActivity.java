package com.inerdstack.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.btn_jump)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, Main2Activity.class), 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("smzq", "onActivityResult");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("smzq", "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("smzq", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("smzq", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("smzq", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("smzq", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("smzq", "onDestroy");
    }

}
