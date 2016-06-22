package com.lalocal.lalocal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.xlistview.XListView;

/**
 * Created by lenovo on 2016/6/20.
 */
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_layout);
      WebView wb= (WebView) findViewById(R.id.wb);
        wb.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        String h5url = intent.getStringExtra("h5url");
        wb.loadUrl(h5url);
    }
}
