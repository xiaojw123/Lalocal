package com.lalocal.lalocal.live.entertainment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.live.entertainment.ui.ApngDrawable;
import com.lalocal.lalocal.live.entertainment.ui.ApngImageLoader;
import com.lalocal.lalocal.util.AppLog;

/**
 * Created by android on 2016/8/3.
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_test_activity);
     final  ImageView iv= (ImageView) findViewById(R.id.video_view);
        String uri ="assets://apng/live_loading_anim_.png";
        ApngImageLoader.getInstance().displayImage(uri,iv);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApngDrawable apngDrawable = ApngDrawable.getFromView(iv);
                if (apngDrawable != null) {
                    AppLog.i("TAG","开始播放apng动画");
                    apngDrawable.setNumPlays(100);
                    apngDrawable.start();
                }else{
                    AppLog.i("TAG","apngDrawable为空");
                }
            }
        });

    }


}
