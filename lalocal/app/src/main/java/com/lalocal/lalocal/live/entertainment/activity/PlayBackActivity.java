package com.lalocal.lalocal.live.entertainment.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.base.util.ActivityManager;
import com.lalocal.lalocal.live.base.util.DialogUtil;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomLiveUserInfoDialog;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.me.LLoginActivity;
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
    public static final int RESQUEST_COD = 101;


    @BindView(R.id.video_player)
    TextureVideoPlayer videoPlayer;
    @BindView(R.id.video_view_player)
    PlayBackPlayer videoViewPlayer;
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
    @BindView(R.id.play_loading_page_bg)
    BlurImageView playLoadingPageBg;
    @BindView(R.id.loading_live_imag)
    GifView loadingLiveImag;
    @BindView(R.id.xlistview_header_anim)
    LinearLayout xlistviewHeaderAnim;
    @BindView(R.id.playback_loading_page)
    RelativeLayout playbackLoadingPage;
    private SpecialShareVOBean shareVO;
    private int direction;
    private List<LiveRowsBean.VideoListBean> videoList;
    private ContentLoader contentLoader;
    private MyCallBack myCallBack;
    private LiveUserInfoResultBean result;
    private int position1;
    private LiveUserBean user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        ButterKnife.bind(this);
        ActivityManager.removePlayBackCurrent();
        ActivityManager.addPlayBackActivity(this);
        contentLoader = new ContentLoader(this);
        myCallBack = new MyCallBack();
        contentLoader.setCallBack(myCallBack);
        String id = getIntent().getStringExtra("id");
        contentLoader.getPlayBackLiveDetails(Integer.parseInt(id));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void parseIntent(LiveRowsBean liveRowsBean) {
        videoList = liveRowsBean.getVideoList();
        user = liveRowsBean.getUser();
        shareVO = liveRowsBean.getShareVO();
        direction = liveRowsBean.getDirection();
        playbackOnlineCount.setText(String.valueOf(liveRowsBean.getOnlineNumber()));
        initData(liveRowsBean);
    }

    private boolean isPlayStatus = true;//视频播放状态
    private int position = 0;

    private void initData(LiveRowsBean liveRowsBean) {
        playLoadingPageBg.setBlurImageURL(user.getAvatar());
        playLoadingPageBg.setScaleRatio(20);
        playLoadingPageBg.setBlurRadius(1);
        DrawableUtils.displayImg(this, playbackEmceeHead, user.getAvatar());
        playbackOnlineCount.setText(String.valueOf(liveRowsBean.getOnlineNumber()));
        playbackMasterInfoLayout.setOnClickListener(clickListener);
        AppLog.i("TAG", "视频回放:视频方向：" + direction + "    视频地址：" + videoList.get(0).getUrl());
        // 视频回放:视频方向：1    视频地址：http://vid-11812.vod.chinanetcenter.broadcastapp.agoraio.cn/live-dev-388-11--20161027115838.mp4
        if (direction == 0) {//横屏
            positionChangeListener(0);
            videoViewPlayer.setVisibility(View.GONE);
            videoPlayer.setRotation(90f);
            videoPlayer.setVideoPlayCallback(mVideoPlayCallback);
            videoPlayer.setAutoHideController(true);
            if (videoList != null && videoList.size() > 0) {
                Uri uri = Uri.parse(videoList.get(position).getUrl());
                videoPlayer.loadAndPlay(uri, 0);
            }
        } else {
            videoPlayer.setVisibility(View.GONE);
            positionChangeListener(0);
            videoViewPlayer.setVideoPlayCallback(mVideoPlayCallback);
            videoViewPlayer.setAutoHideController(true);
            if (videoList != null && videoList.size() > 0) {
                Uri uri = Uri.parse(videoList.get(position).getUrl());
                videoViewPlayer.loadAndPlay(uri, 0);
            }
        }
    }


    private void positionChangeListener(int position){

        if((videoPlayer.getVisibility())==0){
            if (videoList.size() == 1) {
                videoPlayer.setBefore(0.4f, false);
                videoPlayer.setNext(0.4f, false);
            } else {
                if (videoList.size() == position + 1) {
                    videoPlayer.setBefore(1.0f, true);
                    videoPlayer.setNext(0.4f, false);
                } else if (position == 0) {
                    videoPlayer.setNext(1.0f, true);
                    videoPlayer.setBefore(0.4f, false);
                } else {
                    videoPlayer.setNext(1.0f, true);
                    videoPlayer.setBefore(1.0f, true);
                }

            }
        }else if((videoViewPlayer.getVisibility())==0){
            if (videoList.size() == 1) {
                videoViewPlayer.setBefore(0.4f, false);
                videoViewPlayer.setNext(0.4f, false);
            } else {
                if (videoList.size() == position + 1) {
                    videoViewPlayer.setBefore(1.0f, true);
                    videoViewPlayer.setNext(0.4f, false);
                } else if (position == 0) {
                    videoViewPlayer.setNext(1.0f, true);
                    videoViewPlayer.setBefore(0.4f, false);
                } else {
                    videoViewPlayer.setNext(1.0f, true);
                    videoViewPlayer.setBefore(1.0f, true);
                }

            }

        }






    }



    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playback_master_info_layout:
                    if(UserHelper.isLogined(PlayBackActivity.this)){
                        contentLoader.getLiveUserInfo(String.valueOf(user.getId()));
                    }else{
                        CustomChatDialog customDialog = new CustomChatDialog(PlayBackActivity.this);
                        customDialog.setContent(getString(R.string.live_login_hint));
                        customDialog.setCancelable(false);
                        customDialog.setCancelable(false);
                        customDialog.setCancelBtn(getString(R.string.live_canncel), null);
                        customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                LLoginActivity.startForResult(PlayBackActivity.this,RESQUEST_COD);

                            }
                        });
                        customDialog.show();

                        DialogUtil.addDialog(customDialog);
                    }

                    break;
            }
        }
    };


    private VideoPlayCallbackImpl mVideoPlayCallback = new VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            if((videoPlayer.getVisibility())==0){
                videoPlayer.close();//关闭VideoView
            }else if((videoViewPlayer.getVisibility())==0){
                videoViewPlayer.close();
            }

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

            if (videoList.size() == 1) {
                Toast.makeText(PlayBackActivity.this, "视频播放完成!", Toast.LENGTH_SHORT).show();
                return;
            } else if (videoList.size() == position + 1) {
                Toast.makeText(PlayBackActivity.this, "视频全部播放完毕!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(PlayBackActivity.this, "视频播放完毕，正加载下一段视频...", Toast.LENGTH_SHORT).show();

                ++position;
                positionChangeListener(position);
                Uri uri = Uri.parse(videoList.get(position).getUrl());
                if((videoPlayer.getVisibility())==0){
                    videoPlayer.close();
                    videoPlayer.loadAndPlay(uri, 0);
                }else if((videoViewPlayer.getVisibility())==0){
                    videoViewPlayer.close();
                    videoViewPlayer.loadAndPlay(uri, 0);
                }

            }
        }

        @Override
        public void onPlayStatus(boolean isPlay) {
            isPlayStatus = isPlay;
            Log.i("TAG", " 播放状态:" + (isPlay == true ? "开始播放" : "没有播放"));

        }

        @Override
        public void onClickQuit() {
            if((videoPlayer.getVisibility())==0){
                videoPlayer.close();
            }else if((videoViewPlayer.getVisibility())==0){
                videoViewPlayer.close();
            }

            finish();
        }

        @Override
        public void onClickShare() {
            if (shareVO != null) {
                SharePopupWindow shareActivity = new SharePopupWindow(PlayBackActivity.this, shareVO);
                shareActivity.showShareWindow();

                shareActivity.showAtLocation(PlayBackActivity.this.findViewById(R.id.play_layout),
                        Gravity.BOTTOM, 0, 0);
            } else {
                Toast.makeText(PlayBackActivity.this, "此视频暂不可分享!!", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onClickBefore(ImageView view) {
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            if (position == 0) {
                Toast.makeText(PlayBackActivity.this, "已经是第一段视频了!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (videoList.size() == 1) {
                Toast.makeText(PlayBackActivity.this, "没有上一段视频!", Toast.LENGTH_SHORT).show();
                return;
            }
            --position;
            if (videoList.size() == position || position < 0) {
                Toast.makeText(PlayBackActivity.this, "播放第一段视频", Toast.LENGTH_SHORT).show();
                position = 0;
            } else {
                Toast.makeText(PlayBackActivity.this, "正在加载上一段视频....", Toast.LENGTH_SHORT).show();
            }
            positionChangeListener(position);

            Uri uri = Uri.parse(videoList.get(position).getUrl());
            if((videoPlayer.getVisibility())==0){
                videoPlayer.close();
                videoPlayer.loadAndPlay(uri, 0);
            }else if((videoViewPlayer.getVisibility())==0) {
                videoViewPlayer.close();
                videoViewPlayer.loadAndPlay(uri, 0);
            }

        }

        @Override
        public void onClickNext(ImageView view) {
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            if (videoList.size() == 1) {
                Toast.makeText(PlayBackActivity.this, "没有下一段视频!", Toast.LENGTH_SHORT).show();
                return;
            }
            ++position;

            if (videoList.size() == position) {
                Toast.makeText(PlayBackActivity.this, "播放第一段视频", Toast.LENGTH_SHORT).show();
                position = 0;
            } else {
                Toast.makeText(PlayBackActivity.this, "正在加载下一段视频....", Toast.LENGTH_SHORT).show();
            }
            positionChangeListener(position);
            Uri uri = Uri.parse(videoList.get(position).getUrl());
            if((videoPlayer.getVisibility())==0){
                videoPlayer.close();
                videoPlayer.loadAndPlay(uri, 0);
            }else if((videoViewPlayer.getVisibility())==0){
                videoViewPlayer.close();
                videoViewPlayer.loadAndPlay(uri, 0);
            }




        }

        @Override
        public void showLoadingPage(boolean isShow) {
            playbackLoadingPage.setVisibility(View.GONE);
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
        public void onPlayBackDetails(LiveRowsBean liveRowsBean) {
            super.onPlayBackDetails(liveRowsBean);
            if(liveRowsBean!=null){
                parseIntent(liveRowsBean);
            }
        }

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);

            if (liveUserInfosDataResp.getReturnCode() == 0) {
                result = liveUserInfosDataResp.getResult();
                LiveConstant.IDENTITY = LiveConstant.ME_CHECK_OTHER;
                Object statusa = result.getAttentionVO().getStatus();
                if (statusa != null) {
                    double parseDouble = Double.parseDouble(String.valueOf(statusa));
                    status = (int) parseDouble;

                }
                final CustomLiveUserInfoDialog dialog = new CustomLiveUserInfoDialog(PlayBackActivity.this, result, false, false);
                dialog.setUserHomeBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                    @Override
                    public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                        Intent intent = new Intent(PlayBackActivity.this, LiveHomePageActivity.class);
                        intent.putExtra("userId", String.valueOf(id));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.setSurceBtn(new CustomLiveUserInfoDialog.CustomLiveUserInfoDialogListener() {
                    @Override
                    public void onCustomLiveUserInfoDialogListener(String id, TextView textView, ImageView managerMark) {
                        Intent intent = new Intent(PlayBackActivity.this, LiveHomePageActivity.class);
                        intent.putExtra("userId", String.valueOf(id));
                        startActivity(intent);
                        dialog.dismiss();
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
        if (videoList != null && videoList.size() > 0) {
            Log.i("TAF", "播放器activityhhehh ");
            Uri uri = Uri.parse(videoList.get(position).getUrl());
            if((videoPlayer.getVisibility())==0){
                videoPlayer.loadAndPlay(uri, position1);
            }else if((videoViewPlayer.getVisibility())==0){
                videoViewPlayer.loadAndPlay(uri, position1);
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停播放
        if ((videoPlayer.getVisibility())==0) {
            Log.i("TAF", "播放器activityhhehh  onPause ");
            position1 = videoPlayer.pause();
        }else if((videoViewPlayer.getVisibility())==0){
            position1 = videoViewPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        if ((videoPlayer.getVisibility())==0) {
            videoPlayer.close();
        }else{
            if((videoViewPlayer.getVisibility())==0){
                videoViewPlayer.close();
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DialogUtil.clear();
    }
}
