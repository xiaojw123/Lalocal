package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.VideoView;

import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.lalocal.lalocal.R;


/**
 * Created by lenovo on 2016/6/26.
 */
public class VideoActivity extends BaseActivity {

    private SuperVideoPlayer viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        viewById = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        Intent intent = getIntent();
        String video = intent.getStringExtra("video");
        // mPlayBtnView.setVisibility(View.GONE);
        viewById.setVisibility(View.VISIBLE);
        viewById.setAutoHideController(true);
        MediaController mMediaController = new MediaController(this);
        mMediaController.setPageType(MediaController.PageType.SHRINK);
        Uri uri = Uri.parse(video);
        viewById.loadAndPlay(uri, 0);
    }
    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {

        @Override
        public void onCloseVideo() {
            viewById.close();//关闭VideoView
            viewById.setVisibility(View.GONE);

        }

        @Override
        public void onSwitchPageType() {

        }


        /**
         * 播放完成回调
         */
        @Override
        public void onPlayFinish() {

        }

        @Override
        public void onPlayStatus(boolean isPlay) {

        }
    };
}