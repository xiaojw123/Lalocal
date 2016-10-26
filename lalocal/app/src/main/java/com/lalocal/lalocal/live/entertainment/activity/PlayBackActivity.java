package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tedcoder.wkvideoplayer.util.VideoPlayCallbackImpl;
import com.android.tedcoder.wkvideoplayer.view.PlayBackPlayer;
import com.android.tedcoder.wkvideoplayer.view.TextureVideoPlayer;
import com.cunoraz.gifview.library.GifView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.ui.CustomLiveUserInfoDialog;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.SharePopupWindow;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/10/12.
 */
public class PlayBackActivity extends BaseActivity {


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
    @BindView(R.id.video_view_player)
    PlayBackPlayer videoViewPlayer;
    private LiveRowsBean liveRowsBean;
    private String videoUrl;
    private SpecialShareVOBean shareVO;
    private int direction;
    private List<LiveRowsBean.VideoListBean> videoList;
    private ContentLoader contentLoader;
    private MyCallBack myCallBack;
    private LiveUserInfoResultBean result;
    private LiveUserBean user;
    private int position1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_activity);
        ButterKnife.bind(this);
        contentLoader = new ContentLoader(this);
        myCallBack = new MyCallBack();
        contentLoader.setCallBack(myCallBack);
        parseIntent();
        initData();

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
        playbackOnlineCount.setText(String.valueOf(liveRowsBean.getOnlineNumber()));
    }

    private boolean isPlayStatus = true;//视频播放状态
    private int position = 0;

    private void initData() {
        user = liveRowsBean.getUser();

        loadingPageBg.setBlurImageURL(user.getAvatar());
        loadingPageBg.setScaleRatio(20);
        loadingPageBg.setBlurRadius(1);

        DrawableUtils.displayImg(this, playbackEmceeHead, user.getAvatar());
        playbackOnlineCount.setText(String.valueOf(liveRowsBean.getOnlineNumber()));
        playbackMasterInfoLayout.setOnClickListener(clickListener);
        AppLog.i("TAG","视频回放:视频方向："+direction+"    视频地址："+videoList.get(0).getUrl());
        if (direction == 0) {//横屏
            videoViewPlayer.setVisibility(View.GONE);
            videoPlayer.setRotation(90f);
            videoPlayer.setVideoPlayCallback(mVideoPlayCallback);
            videoPlayer.setAutoHideController(true);
            if (videoList != null && videoList.size() > 0) {
                Uri uri = Uri.parse(videoList.get(position).getUrl());
                videoPlayer.loadAndPlay(uri, 0);
             //   Toast.makeText(this, "共：" + videoList.size() + "段视频", Toast.LENGTH_SHORT).show();
            }
        } else {
            videoPlayer.setVisibility(View.GONE);

            videoViewPlayer.setVideoPlayCallback(mVideoPlayCallback);
            videoViewPlayer.setAutoHideController(true);
            if (videoList != null && videoList.size() > 0) {
                Uri uri = Uri.parse(videoList.get(position).getUrl());
                videoViewPlayer.loadAndPlay(uri, 0);

            }
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playback_master_info_layout:
                    contentLoader.getLiveUserInfo(String.valueOf(user.getId()));
                    break;
            }
        }
    };


    private VideoPlayCallbackImpl mVideoPlayCallback = new VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            videoPlayer.close();//关闭VideoView
        }

        @Override
        public void onSwitchPageType() {
        }

        /**
         * 播放完成回调
         */
        @Override
        public void onPlayFinish() {
            if (videoList == null || videoList.size() == 0) {
                return;
            }

            if(videoList.size()==1){
                Toast.makeText(PlayBackActivity.this,"视频播放完成!",Toast.LENGTH_SHORT).show();
                return;
            }else if(videoList.size() == position+1){
                Toast.makeText(PlayBackActivity.this,"视频全部播放完毕!",Toast.LENGTH_SHORT).show();
                return;
            }else {
                Toast.makeText(PlayBackActivity.this,"视频播放完毕，正加载下一段视频...",Toast.LENGTH_SHORT).show();
                videoPlayer.close();
                ++position;
                Uri uri = Uri.parse(videoList.get(position).getUrl());
                videoPlayer.loadAndPlay(uri, 0);
            }
        }

        @Override
        public void onPlayStatus(boolean isPlay) {
            isPlayStatus = isPlay;
            Log.i("TAG", " 播放状态:" + (isPlay == true ? "开始播放" : "没有播放"));
            playbackLoadingPage.setVisibility(View.GONE);
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

        @Override
        public void onClickBefore(ImageView view) {
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            if(position==0){
                Toast.makeText(PlayBackActivity.this,"已经是第一段视频了!",Toast.LENGTH_SHORT).show();
                return;
            }
            if(videoList.size()==1){
                Toast.makeText(PlayBackActivity.this,"没有上一段视频!",Toast.LENGTH_SHORT).show();
                return;
            }
            --position;
            if (videoList.size() == position || position < 0) {
                Toast.makeText(PlayBackActivity.this, "播放第一段视频", Toast.LENGTH_SHORT).show();
                position = 0;
            }else{
                Toast.makeText(PlayBackActivity.this,"正在加载上一段视频....",Toast.LENGTH_SHORT).show();
            }

            videoPlayer.close();
            Uri uri = Uri.parse(videoList.get(position).getUrl());
            videoPlayer.loadAndPlay(uri, 0);
        }

        @Override
        public void onClickNext(ImageView view) {
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            if(videoList.size()==1){
                Toast.makeText(PlayBackActivity.this,"没有下一段视频!",Toast.LENGTH_SHORT).show();
                return;
            }
            ++position;
            if (videoList.size() == position) {
                Toast.makeText(PlayBackActivity.this, "播放第一段视频", Toast.LENGTH_SHORT).show();
                position = 0;
            }else{
                Toast.makeText(PlayBackActivity.this,"正在加载下一段视频....",Toast.LENGTH_SHORT).show();
            }

            videoPlayer.close();
            Uri uri = Uri.parse(videoList.get(position).getUrl());
            videoPlayer.loadAndPlay(uri, 0);
        }


    };


    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
    }

    private int status;

    public class MyCallBack extends ICallBack {
        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);

            if (liveUserInfosDataResp.getReturnCode() == 0) {
                result = liveUserInfosDataResp.getResult();
                LiveConstant.IDENTITY = LiveConstant.IS_LIVEER;
                Object statusa = result.getAttentionVO().getStatus();
                if (statusa != null) {
                    double parseDouble = Double.parseDouble(String.valueOf(statusa));
                    status = (int) parseDouble;

                }
                CustomLiveUserInfoDialog dialog = new CustomLiveUserInfoDialog(PlayBackActivity.this, result, false, false);
                dialog.setUserHomeBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                    @Override
                    public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                        Intent intent = new Intent(PlayBackActivity.this, LiveHomePageActivity.class);
                        intent.putExtra("userId", String.valueOf(id));
                        startActivity(intent);
                    }
                });
                dialog.setSurceBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                    @Override
                    public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                        Intent intent = new Intent(PlayBackActivity.this, LiveHomePageActivity.class);
                        intent.putExtra("userId", String.valueOf(id));
                        startActivity(intent);
                    }
                });
                dialog.setAttention(status == 0 ? getString(R.string.live_attention) : getString(R.string.live_attention_ok), new CustomLiveUserInfoDialog.CustomLiveFansOrAttentionListener() {
                    int fansCounts = -2;
                    @Override
                    public void onCustomLiveFansOrAttentionListener(String id, TextView fansView, TextView attentionView, int fansCount, int attentionCount, TextView attentionStatus) {

                        if (fansCounts == -2) {
                            fansCounts = fansCount;
                        }
                        if (status == 0) {
                            attentionStatus.setText(getString(R.string.live_attention_ok));
                            attentionStatus.setAlpha(0.4f);
                            ++fansCounts;
                            fansView.setText(String.valueOf(fansCounts));
                            contentLoader.getAddAttention(id);
                            status = 1;
                        } else {
                            attentionStatus.setText(getString(R.string.live_attention));
                            attentionStatus.setAlpha(1);
                            --fansCounts;
                            fansView.setText(String.valueOf(fansCounts));
                            contentLoader.getCancelAttention(id);
                            status = 0;
                        }
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        LiveConstant.USER_INFO_FIRST_CLICK = true;
                    }
                });
                dialog.show();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 恢复播放
        if (videoPlayer != null) {
            Log.i("TAF","播放器activityhhehh ");
            Uri uri = Uri.parse(videoList.get(position).getUrl());
            videoPlayer.loadAndPlay(uri,position1);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停播放
        if (videoPlayer != null) {
            Log.i("TAF","播放器activityhhehh  onPause ");
            position1 = videoPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        if (videoPlayer != null) {
            videoPlayer.close();
        }
        super.onDestroy();
    }
}
