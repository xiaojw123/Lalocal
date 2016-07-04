package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lalocal.lalocal.R;


/**
 * Created by lenovo on 2016/6/26.
 */
public class VideoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        VideoView viewById = (VideoView) findViewById(R.id.video_player_item_1);
        Intent intent = getIntent();
        String video = intent.getStringExtra("video");
        MediaController mediaController = new MediaController(this);
        viewById.setMediaController(mediaController);
        mediaController.setMediaPlayer(viewById);
        viewById.setVideoPath(video);
        viewById.start();
    }
}
