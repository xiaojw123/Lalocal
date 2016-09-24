package com.lalocal.lalocal.live.entertainment.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.live.im.ui.barrage.BarrageConfig;
import com.lalocal.lalocal.live.im.ui.barrage.BarrageViewTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2016/8/3.
 */
public class TestActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_test_activity);

    }
    boolean isFirst=true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isFirst){
            isFirst=false;
            BarrageConfig barrageConfig = new BarrageConfig();
            barrageConfig.setMaxTextSizeSp(18);
            barrageConfig.setDuration(5000);
            List<Integer> list=new ArrayList<>();
            list.add(Color.CYAN);
            list.add(Color.GREEN);
            barrageConfig.setColors(list);
            BarrageViewTest barrageView = (BarrageViewTest) findViewById(R.id.test_barrageView);
            barrageView.init(barrageConfig);
            barrageView.addTextBarrage("哈弗家哈金凤凰");



        }

    }
}
