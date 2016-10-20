package com.lalocal.lalocal.live.entertainment.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 2016/8/3.
 */
public class TestActivity extends BaseActivity {
    @BindView(R.id.video_view)
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_test_activity);
        ButterKnife.bind(this);
        Uri uri = Uri.parse("http://vid-11812.vod.chinanetcenter.broadcastapp.agoraio.cn/live-dev-33-69--20161013102710.mp4");
        videoView.setVideoURI(uri);
        videoView.start();


    }


}
