package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tedcoder.wkvideoplayer.view.TextureVideoPlayer;
import com.cunoraz.gifview.library.GifView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.SharePopupWindow;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/10/12.
 */
public class PlayBackActivity extends AppCompatActivity {


    @BindView(R.id.video_player)
    TextureVideoPlayer videoPlayer;
    @BindView(R.id.playback_emcee_head)
    CircleImageView playbackEmceeHead;
    @BindView(R.id.playback_emcee_name)
    TextView playbackEmceeName;
    @BindView(R.id.playback_online_count)
    TextView playbackOnlineCount;
    @BindView(R.id.playback_master_info_layout)
    LinearLayout playbackMasterInfoLayout;
    @BindView(R.id.play_layout)
    LinearLayout playLayout;
    @BindView(R.id.loading_page_bg)
    BlurImageView loadingPageBg;
    @BindView(R.id.loading_live_imag)
    GifView loadingLiveImag;
    @BindView(R.id.xlistview_header_anim)
    LinearLayout xlistviewHeaderAnim;
    @BindView(R.id.playback_loading_page)
    RelativeLayout playbackLoadingPage;
    private LiveRowsBean liveRowsBean;
    private String videoUrl;
    private SpecialShareVOBean shareVO;
    private int direction;
    private List<LiveRowsBean.VideoListBean> videoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_activity);
        ButterKnife.bind(this);
        parseIntent();
        initData();
        startPlayer();
    }


    public static void start(Context context, LiveRowsBean liveRowsBean) {
        Intent intent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("LiveRowsBean", liveRowsBean);
        intent.putExtras(mBundle);
        intent.setClass(context, PlayBackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);

    }

    private void parseIntent() {
        liveRowsBean = getIntent().getParcelableExtra("LiveRowsBean");
        videoList = liveRowsBean.getVideoList();
        shareVO = liveRowsBean.getShareVO();
        direction = liveRowsBean.getDirection();
    }
    private boolean isPlayStatus = true;//视频播放状态
    private void startPlayer() {

        videoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        videoPlayer.setAutoHideController(true);
        Uri uri = Uri.parse(videoList.get(0).getUrl());
        videoPlayer.loadAndPlay(uri, 0);

    }

    private void initData() {
        LiveUserBean user = liveRowsBean.getUser();
        loadingPageBg.setBlurImageURL(user.getAvatar());
        loadingPageBg.setScaleRatio(20);
        loadingPageBg.setBlurRadius(1);

        DrawableUtils.displayImg(this, playbackEmceeHead, user.getAvatar());
        playbackOnlineCount.setText(String.valueOf(liveRowsBean.getOnlineUser()));
        playbackMasterInfoLayout.setOnClickListener(clickListener);
        if (direction == 0) {//横屏
            videoPlayer.setRotation(90f);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.live_master_info_layout:
                    //    CustomLiveUserInfoDialog dialog=new CustomLiveUserInfoDialog(this,liveRowsBean.getUser(),false,false);
                    break;
            }
        }
    };


    private TextureVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new TextureVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            videoPlayer.close();//关闭VideoView
            resetPageToPortrait();
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
            isPlayStatus = isPlay;
            if(isPlay){
                playbackLoadingPage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClickQuit() {
            videoPlayer.close();
            finish();

        }

        @Override
        public void onClickShare() {
            if (shareVO != null) {
                SharePopupWindow shareActivity = new SharePopupWindow(PlayBackActivity.this, shareVO);
                shareActivity.showShareWindow();
                shareActivity.showAtLocation(PlayBackActivity.this.findViewById(R.id.play_layout),
                        Gravity.CENTER, 0, 0);
            } else {
                Toast.makeText(PlayBackActivity.this, "此视频暂不可分享!!", Toast.LENGTH_SHORT).show();
            }

        }


    };

    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
    }

}
