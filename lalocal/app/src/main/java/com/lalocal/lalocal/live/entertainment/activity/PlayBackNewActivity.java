package com.lalocal.lalocal.live.entertainment.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.gifview.library.GifView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.im.ChatFragment;
import com.lalocal.lalocal.live.base.util.ActivityManager;
import com.lalocal.lalocal.live.base.util.DialogUtil;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.model.PlayBackMsgResultBean;
import com.lalocal.lalocal.live.entertainment.model.PlayBackResultBean;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomNewUserInforDialog;
import com.lalocal.lalocal.live.entertainment.ui.PlayBackMsgAdapter;
import com.lalocal.lalocal.live.im.ui.blur.BlurImageView;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.SharePopupWindow;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import openlive.agora.io.pldroidplayer.utils.Utils;
import openlive.agora.io.pldroidplayer.utils.widget.MediaController;

import static com.lalocal.lalocal.R.id.playback_fragment_container;

/**
 * Created by android on 2016/12/21.
 */
public class PlayBackNewActivity extends BaseActivity implements View.OnTouchListener {
    public static final int RESQUEST_COD = 101;
    private static final int MESSAGE_ID_RECONNECTING = 0x01;
    @BindView(R.id.VideoView)
    PLVideoTextureView mVideoView;
    @BindView(R.id.playback_emcee_head)
    CircleImageView playbackEmceeHead;
    @BindView(R.id.playback_emcee_name)
    TextView playbackEmceeName;
    @BindView(R.id.playback_online_count)
    TextView playbackOnlineCount;
    @BindView(R.id.playback_master_info_layout)
    LinearLayout playbackMasterInfoLayout;
    @BindView(R.id.playback_quit)
    ImageView playbackQuit;
    @BindView(R.id.playback_top_share)
    ImageView playbackTopShare;
    @BindView(R.id.msgListview)
    ListView msgListview;
    @BindView(R.id.play_loading_page_bg)
    BlurImageView playLoadingPageBg;
    @BindView(R.id.loading_live_imag)
    GifView loadingLiveImag;
    @BindView(R.id.xlistview_header_anim)
    LinearLayout xlistviewHeaderAnim;
    @BindView(R.id.playback_loading_page)
    RelativeLayout playbackLoadingPage;
    @BindView(R.id.play_layout)
    FrameLayout playLayout;
    @BindView(R.id.playback_fragment_container)
    FrameLayout frameCotainer;
    private SpecialShareVOBean shareVO;
    private int direction;
    int status;
    private LiveUserInfoResultBean result;
    private LiveUserBean user;
    private int targetId;
    private Object praiseId;
    private boolean praiseFlag;
    private List<PlayBackResultBean.VideoListBean> videoList;
    private LinkedList<PlayBackMsgResultBean.ResultBean> items = new LinkedList<>();
    private List<PlayBackMsgResultBean.ResultBean> msgListResult;
    private long startAtLong;
    private PlayBackMsgResultBean.ResultBean thisPoll;
    private long thisSendAt;
    private PlayBackMsgAdapter playBackMsgAdapter;
    private List<PlayBackMsgResultBean.ResultBean> msgList = new ArrayList<>();
    private int userId;
    private MediaController mMediaController;
    private ContentLoader contentLoader;
    private MyCallBack myCallBack;
    private String intentId;
    private View loadingView;
    private String accid;
    private String nickName;
    FrameLayout framentCotainer;
    int resId = 123;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        ActivityManager.removePlayBackCurrent();
        ActivityManager.addPlayBackActivity(this);
        contentLoader = new ContentLoader(this);
        myCallBack = new MyCallBack();
        contentLoader.setCallBack(myCallBack);
        //1659
        intentId = getIntent().getStringExtra("id");
        contentLoader.getPlayBackLiveDetails(Integer.parseInt(intentId));
        contentLoader.getPlayBackMsg(intentId);
        framentCotainer = (FrameLayout) findViewById(playback_fragment_container);
        loadingView = findViewById(R.id.loading_view);
        mVideoView.setBufferingIndicator(loadingView);
        mMediaController = new MediaController(this, true, false);
        mMediaController.setInstantSeeking(true);
        mMediaController.setMediaControl(mMediaControlImpl);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setOnPreparedListener(mOnPreparedListener);
        mVideoView.setOnTouchListener(this);

        playbackQuit.setClickable(true);
    }

    public class MyCallBack extends ICallBack {
        @Override
        public void onPlayBackDetails(PlayBackResultBean liveRowsBean) {
            super.onPlayBackDetails(liveRowsBean);
            if (liveRowsBean != null) {
                parseIntent(liveRowsBean);
            }
        }

        @Override
        public void onPlayBackMsgDetails(PlayBackMsgResultBean msgResultBean) {
            super.onPlayBackMsgDetails(msgResultBean);
            if (msgResultBean.getReturnCode() == 0) {
                msgListResult = msgResultBean.getResult();
                if (msgListResult != null && msgListResult.size() > 0) {
                    thisSendAt = msgListResult.get(0).getSendAt();
                    items.addAll(msgListResult);
                    // 消息记录:1481876276240
                    AppLog.i("TAG", "消息记录:" + thisSendAt + "" + "发送人:" + msgListResult.get(0).getFromNick() + "   聊天内容:" + msgListResult.get(0).getContent());
                }

            }
        }

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            if (liveUserInfosDataResp.getReturnCode() == 0) {
                result = liveUserInfosDataResp.getResult();
                int id = result.getId();
                LiveConstant.IDENTITY = LiveConstant.ME_CHECK_OTHER;
                Object statusa = result.getAttentionVO().getStatus();
                if (statusa != null) {
                    double parseDouble = Double.parseDouble(String.valueOf(statusa));
                    status = (int) parseDouble;
                }
                if (isUnDestroy) {
                    CustomNewUserInforDialog dialog = new CustomNewUserInforDialog(PlayBackNewActivity.this, null, String.valueOf(id), intentId, 0, false, null, null);
                    dialog.show();
                }

            }
        }

        @Override
        public void onPariseResult(PariseResult pariseResult) {//取消收藏
            super.onPariseResult(pariseResult);
            if (pariseResult != null && pariseResult.getReturnCode() == 0) {
                praiseFlag = false;
                playBackCollectStatus(praiseFlag);
            }

        }

        @Override
        public void onInputPariseResult(PariseResult pariseResult) {//收藏
            super.onInputPariseResult(pariseResult);
            if (pariseResult.getReturnCode() == 0) {
                praiseId = pariseResult.getResult();
                praiseFlag = true;
                playBackCollectStatus(praiseFlag);
            }
        }
    }

    private void playBackCollectStatus(boolean praiseFlag) {
        if (mMediaController != null) {
            mMediaController.setCollect(praiseFlag);
        }
    }

    private void parseIntent(PlayBackResultBean liveRowsBean) {
        accid = liveRowsBean.getCreaterAccId();
        videoList = liveRowsBean.getVideoList();
        user = liveRowsBean.getUser();
        shareVO = liveRowsBean.getShareVO();
        direction = liveRowsBean.getDirection();
        targetId = liveRowsBean.getId();
        praiseId = liveRowsBean.getPraiseId();
        praiseFlag = liveRowsBean.isPraiseFlag();
        String startAt = liveRowsBean.getStartAt();
        String endAt = liveRowsBean.getEndAt();
        userId = user.getId();
        nickName = user.getNickName();
        startAtLong = stringToDate(startAt, "yyyy-MM-dd HH:mm:ss");
        long endAtLong = stringToDate(endAt, "yyyy-MM-dd HH:mm:ss");
        // 回放视频开始和结束的时间:1481876214000      end:1481877415000
        //                  消息记录:1481876276240
        AppLog.i("TAG", "回放视频开始和结束的时间:" + startAtLong + "      end:" + endAtLong);
        playbackOnlineCount.setText(String.valueOf(liveRowsBean.getOnlineNumber()));
        initListview();
        initData(liveRowsBean);
    }

    private void initListview() {
        playBackMsgAdapter = new PlayBackMsgAdapter(this, null, userId);
        msgListview.setAdapter(playBackMsgAdapter);
    }

    private void initData(PlayBackResultBean liveRowsBean) {
        playLoadingPageBg.setBlurImageURL(user.getAvatar());
        playLoadingPageBg.setScaleRatio(20);
        playLoadingPageBg.setBlurRadius(1);
        DrawableUtils.displayImg(this, playbackEmceeHead, user.getAvatar());
        playbackOnlineCount.setText(String.valueOf(liveRowsBean.getOnlineNumber()));
        initPlayer(direction);

    }

    private void initPlayer(int direction) {

        if (videoList != null && videoList.size() > 0) {
            mVideoView.setVideoPath(videoList.get(0).getUrl());
        }


        if (direction == 0) {//横屏
            mVideoView.setDisplayOrientation(450);
        } else {//竖屏
            mVideoView.setDisplayOrientation(0);
        }
        mVideoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        mVideoView.start();
    }

    @OnClick({R.id.playback_master_info_layout, R.id.playback_quit, R.id.playback_top_share})
    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.playback_master_info_layout:
                if (user == null) {
                    break;
                }
                if (UserHelper.isLogined(PlayBackNewActivity.this)) {
                    contentLoader.getLiveUserInfo(String.valueOf(user.getId()));
                } else {
                    if (isUnDestroy) {
                        CustomChatDialog customDialog = new CustomChatDialog(PlayBackNewActivity.this);
                        customDialog.setContent(getString(R.string.live_login_hint));
                        customDialog.setCancelable(false);
                        customDialog.setCancelable(false);
                        customDialog.setCancelBtn(getString(R.string.live_canncel), null);
                        customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                LLoginActivity.startForResult(PlayBackNewActivity.this, RESQUEST_COD);
                            }
                        });
                        customDialog.show();

                        DialogUtil.addDialog(customDialog);
                    }

                }
                break;
            case R.id.playback_quit://退出回放
                if (mVideoView != null) {
                    mVideoView.stopPlayback();
                }
                setResult(KeyParams.PLAYER_OVER_FIRST_RESULTCODE,new Intent());
                finish();
                break;
            case R.id.playback_top_share:
                if (shareVO != null && isUnDestroy) {
                    SharePopupWindow shareActivity = new SharePopupWindow(PlayBackNewActivity.this, shareVO);
                    shareActivity.show();
                } else {
                    showToastTips("此视频暂不可分享!!");
                }
                break;
        }

    }


    long playDuration;
    private MediaController.MediaControlImpl mMediaControlImpl = new MediaController.MediaControlImpl() {

        @Override
        public void onClickCollect(ImageView imageView) {
            clickCollectBtn();
        }

        @Override
        public void onClickBefore(ImageView imageView) {
            clickBefore();
        }

        @Override
        public void onClickNext(ImageView imageView) {
            clickNext();
        }

        @Override
        public void getprogressDuration(long duration) {
            playDuration = duration;
            long msgAt = startAtLong + duration;
            AppLog.i("TAG", "播放时间。。。。：" + msgAt);
            getMsgInfo(msgAt);
        }

        @Override
        public void inputPrivate() {
            //TODO 私信主播
            attatchChatFragment();
        }
    };

    public void attatchChatFragment() {
        //TODO 私信主播
        frameCotainer.requestFocus();
        mMediaController.setHideContro();
        FragmentManager fm = getFragmentManager();
        ft = fm.beginTransaction();
        if (chatFragment == null) {
            chatFragment = new ChatFragment();
            Bundle bundle = new Bundle();
            bundle.putString(KeyParams.ACCID, CommonUtil.getAccId(userId));
            bundle.putString(KeyParams.NICKNAME, nickName);
            bundle.putInt(KeyParams.PAGE_TYPE, PageType.PAGE_PLAY_BACK);
            chatFragment.setArguments(bundle);
            ft.add(R.id.playback_fragment_container, chatFragment);

            chatFragment.setOnCloseFragmentClickListener(new ChatFragment.CloseFragmentClickListener() {
                @Override
                public void closeClick() {
                    mMediaController.showContro();
                }
            });
        } else {
            ft.show(chatFragment);
        }
        ft.commit();
    }

    private ChatFragment chatFragment;



    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(PLMediaPlayer plMediaPlayer) {
            plMediaPlayer.setOnInfoListener(new PLMediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        if (videoList != null && videoList.size() == 1) {
                            mMediaController.setPlayerCountStatus(false);
                        }
                        playbackLoadingPage.setVisibility(View.GONE);
                        mMediaController.setCollect(praiseFlag);
                        return true;
                    }
                    return false;
                }
            });
        }
    };

    private int position = 0;
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            //TODO 播放完成
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            if (videoList.size() == 1) {
                showToastTips("视频播放完成!");
                return;
            } else if (videoList.size() == position + 1) {
                showToastTips("视频全部播放完毕!");
                return;
            } else {
                showToastTips("视频播放完毕，正加载下一段视频...");
                ++position;
                positionChangeListener(position);
                mVideoView.setVideoPath(videoList.get(position).getUrl());
                mVideoView.start();
            }

        }
    };


    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
            boolean isNeedReconnect = false;
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    AppLog.i("TAG", "视频播放错误码:Invalid URL !:ERROR_CODE_INVALID_URI");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:

                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_404_NOT_FOUND:404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:

                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_CONNECTION_REFUSED:Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    isNeedReconnect = true;
                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_CONNECTION_TIMEOUT:Connection timeout !");
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    showToastTips("Empty playlist !");
                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_EMPTY_PLAYLIST:Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    showToastTips("Stream disconnected !");
                    isNeedReconnect = true;
                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_STREAM_DISCONNECTED:Stream disconnected !");
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    isNeedReconnect = true;
                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_IO_ERROR:Network IO Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_UNAUTHORIZED:Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    isNeedReconnect = true;
                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_PREPARE_TIMEOUT:Prepare timeout !");
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    isNeedReconnect = true;
                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_READ_FRAME_TIMEOUT:Read frame timeout !");
                    break;
                case PLMediaPlayer.ERROR_CODE_HW_DECODE_FAILURE:
                    isNeedReconnect = true;
                    AppLog.i("TAG", "视频播放错误码:ERROR_CODE_HW_DECODE_FAILURE");
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    AppLog.i("TAG", "视频播放错误码:MEDIA_ERROR_UNKNOWN");
                    break;
                default:
                    AppLog.i("TAG", "视频播放错误码:default:unknown error !");
                    break;
            }

            if (isNeedReconnect) {
                sendReconnectMessage();
            } else {
                finish();
            }
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }

    };


    private void getMsgInfo(long msgAt) {
        if (thisSendAt != 0) {
            if (msgAt > thisSendAt) {
                if (thisPoll != null) {
                    msgList.add(thisPoll);
                    playBackMsgAdapter.addItemMsg(msgList);
                }
                thisPoll = items.poll();
                if (thisPoll != null) {
                    thisSendAt = thisPoll.getSendAt();
                    getMsgInfo(msgAt);
                } else {

                }
            }
        }

    }

    private void clickBefore() {
        if (videoList == null || videoList.size() == 0) {
            return;
        }
        if (position == 0) {
            showToastTips("已经是第一段视频了!");
            return;
        }
        if (videoList.size() == 1) {
            showToastTips("没有上一段视频!");
            return;
        }
        --position;
        if (videoList.size() == position || position < 0) {
            showToastTips("播放第一段视频");

            position = 0;
        } else {
            showToastTips("正在加载上一段视频....");
        }

        positionChangeListener(position);
        mVideoView.setVideoPath(videoList.get(position).getUrl());
        mVideoView.start();
    }

    private void clickNext() {
        if (videoList == null || videoList.size() == 0) {
            return;
        }
        if (videoList.size() == 1) {
            showToastTips("没有下一段视频!");
            return;
        }
        ++position;

        if (videoList.size() == position) {
            showToastTips("播放第一段视频!");
            Toast.makeText(PlayBackNewActivity.this, "播放第一段视频", Toast.LENGTH_SHORT).show();
            position = 0;
        } else {
            showToastTips("正在加载下一段视频....");
        }
        positionChangeListener(position);
        mVideoView.setVideoPath(videoList.get(position).getUrl());
        mVideoView.start();

    }


    private void clickCollectBtn() {
        if (UserHelper.isLogined(PlayBackNewActivity.this)) {
            if (praiseFlag) {
                contentLoader.cancelParises(praiseId, targetId);
            } else {
                contentLoader.specialPraise(targetId, 20);//收藏
            }
        } else {
            if (isUnDestroy) {
                CustomChatDialog customDialog = new CustomChatDialog(PlayBackNewActivity.this);
                customDialog.setContent(getString(R.string.live_login_hint));
                customDialog.setCancelable(false);
                customDialog.setCancelable(false);
                customDialog.setCancelBtn(getString(R.string.live_canncel), null);
                customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        LLoginActivity.startForResult(PlayBackNewActivity.this, RESQUEST_COD);
                    }
                });
                customDialog.show();
            }
        }
    }


    private void positionChangeListener(int position) {
        if (videoList.size() > 1) {
            if (videoList.size() == position + 1) {
                mMediaController.setBefore(1.0f, true);
                mMediaController.setNext(0.4f, false);
            } else if (position == 0) {
                mMediaController.setNext(1.0f, true);
                mMediaController.setBefore(0.4f, false);
            } else {
                mMediaController.setNext(1.0f, true);
                mMediaController.setBefore(1.0f, true);
            }
        }

    }

    private void sendReconnectMessage() {
        loadingView.setVisibility(View.VISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 3000);
    }

    private void showToastTips(final String tips) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayBackNewActivity.this, tips, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean mIsActivityPaused = true;
    private int mIsLiveStreaming = 0;

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING) {
                return;
            }
            if (mIsActivityPaused || !Utils.isLiveStreamingAvailable()) {
                finish();
                return;
            }
            if (!Utils.isNetworkAvailable(PlayBackNewActivity.this)) {
                sendReconnectMessage();
                return;
            }
            loadingView.setVisibility(View.GONE);
            mVideoView.setVideoPath(videoList.get(0).getUrl());
            mVideoView.start();
            mMediaController.setSeekTo(playDuration);

        }
    };

    public static long stringToDate(String strTime, String formatType) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            Date date = formatter.parse(strTime);
            long time = date.getTime();
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    protected void onPause() {
        super.onPause();

        mVideoView.pause();
        mIsActivityPaused = true;
    }

    boolean isUnDestroy = true;

    @Override
    protected void onResume() {
        super.onResume();
        isUnDestroy = true;
        mIsActivityPaused = false;
        mVideoView.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isUnDestroy = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (chatFragment!=null&&!chatFragment.isHidden()){
            mMediaController.showContro();
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.hide(chatFragment);
            ft.commit();
        }
        return false;
    }


}
