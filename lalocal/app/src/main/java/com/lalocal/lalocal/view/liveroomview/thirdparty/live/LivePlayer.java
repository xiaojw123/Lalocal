package com.lalocal.lalocal.view.liveroomview.thirdparty.live;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager;

import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.liveroomview.base.util.log.LogUtil;
import com.netease.LSMediaCapture.lsMediaCapture;
import com.netease.LSMediaCapture.lsMessageHandler;
import com.netease.livestreamingFilter.filter.Filters;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;


/**
 * Created by huangjun on 2016/3/28.
 */
public class LivePlayer implements lsMessageHandler {

    private final String TAG = "NELivePlayer";
    private long startTime;
    private long endTime;
    private long liveTime;
    private Timer timer;

    public interface ActivityProxy {
        Activity getActivity();
    }

    private ActivityProxy activityProxy;
    private LiveSurfaceView liveView;
    private Intent mAlertServiceIntent;
    private boolean live = false; // 是否已经开始推流（断网重连用），推流没有暂停

    // 视频采集器
    private lsMediaCapture mLSMediaCapture; // 直播实例
    private lsMediaCapture.LSLiveStreamingParaCtx mLSLiveStreamingParaCtx;


    // 基本配置
    private String mliveStreamingURL; // 推流地址
    private int mVideoEncodeWidth, mVideoEncodeHeight; // 推流分辨率

    // 音视频
    public static final int HAVE_AUDIO = 0;
    public static final int HAVE_VIDEO = 1;
    public static final int HAVE_AV = 2;

    // 协议
    public static final int FLV = 0;
    public static final int RTMP = 1;

    // 前后置摄像头
    public static final int CAMERA_POSITION_BACK = 0;
    public static final int CAMERA_POSITION_FRONT = 1;

    // 横竖屏
    public static final int CAMERA_ORIENTATION_PORTRAIT = 0;
    public static final int CAMERA_ORIENTATION_LANDSCAPE = 1;

    // 语音编码
    public static final int LS_AUDIO_CODEC_AAC = 0;
    public static final int LS_AUDIO_CODEC_SPEEX = 1;
    public static final int LS_AUDIO_CODEC_MP3 = 2;
    public static final int LS_AUDIO_CODEC_G711A = 3;
    public static final int LS_AUDIO_CODEC_G711U = 4;

    // 视频编码
    public static final int LS_VIDEO_CODEC_AVC = 0;
    public static final int LS_VIDEO_CODEC_VP9 = 1;
    public static final int LS_VIDEO_CODEC_H265 = 2;

    // 状态控制
    private boolean m_liveStreamingInitFinished = false;
    private boolean m_liveStreamingOn = false;
    private boolean m_liveStreamingPause = false;
    private boolean m_tryToStopLiveStreaming = false;

    public LivePlayer(LiveSurfaceView liveView, String url, ActivityProxy proxy) {

        this.liveView = liveView;
        this.mliveStreamingURL = url;
        this.activityProxy = proxy;
    }

    /**
     * ******************************** Activity 生命周期直播状态控制 ********************************
     */
    public void onActivityResume() {
        if (mLSMediaCapture != null) {
            //关闭推流固定图像
            mLSMediaCapture.stopVideoEncode();

            //关闭推流静音帧
            mLSMediaCapture.stopAudioEncode();
        }
    }

    public void onActivityPause() {
        if (mLSMediaCapture != null) {
            //关闭视频Preview
            mLSMediaCapture.stopVideoPreview();

            if (m_tryToStopLiveStreaming) {
                m_liveStreamingOn = false;
            } else {
                //继续视频推流，推固定图像
                mLSMediaCapture.resumeVideoEncode();

                //释放音频采集资源
                mLSMediaCapture.stopAudioRecord();
            }
        }
    }

    /**
     * ******************************** 初始化 ********************************
     */
    //查询Android摄像头支持的采样分辨率相关方法（1）
    private Thread mCameraThread;
    private Looper mCameraLooper;
    private Camera mCamera;
    public void openCamera(final int cameraID) {
        final Semaphore lock = new Semaphore(0);
        final RuntimeException[] exception = new RuntimeException[1];
        mCameraThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mCameraLooper = Looper.myLooper();
                try {
                    mCamera = Camera.open(cameraID);
                } catch (RuntimeException e) {
                    exception[0] = e;
                    AppLog.i("TAG","没有开启摄像头");

                } finally {
                    lock.release();
                    Looper.loop();
                }
            }
        });
        mCameraThread.start();
        lock.acquireUninterruptibly();
    }

    //查询Android摄像头支持的采样分辨率相关方法（2）
    public void lockCamera() {
        try {
            mCamera.reconnect();
        } catch (Exception e) {
            AppLog.i("TAG","视频采集异常");
        }
    }

    //查询Android摄像头支持的采样分辨率相关方法（3）
    public void releaseCamera() {
        if (mCamera != null) {
            lockCamera();
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    AppLog.i("TAG","摄像头开启了3");
                    if(data!=null){
                        AppLog.i("TAG","摄像头开启了3,且data不为空");
                    }else{
                        AppLog.i("TAG","摄像头开启了3,data为空");
                    }
                }
            });
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
    //查询Android摄像头支持的采样分辨率相关方法（4）
    public List<Camera.Size> getCameraSupportSize(int cameraID) {
        openCamera(cameraID);
        if(mCamera != null) {
            Camera.Parameters param = mCamera.getParameters();
            List<Camera.Size> previewSizes = param.getSupportedPreviewSizes();
            releaseCamera();
            return previewSizes;
        }
        return null;
    }


    // 设置推流参数
    private void initLiveParam() {
        AppLog.i("TAG","初始化推流");
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        List<Camera.Size> backCameraSupportSize = getCameraSupportSize(CAMERA_POSITION_BACK);
        List<Camera.Size> frontCameraSupportSize = getCameraSupportSize(CAMERA_POSITION_FRONT);

        if (backCameraSupportSize != null && backCameraSupportSize.size() != 0&&frontCameraSupportSize != null && frontCameraSupportSize.size() != 0 ){
            for (Camera.Size backSize : backCameraSupportSize) {
                for (Camera.Size frontSize : frontCameraSupportSize) {
                   if(backSize.width==frontSize.width&&backSize.height==frontSize.height){
                       mVideoEncodeWidth=backSize.width;
                       mVideoEncodeHeight=backSize.height;
                     /*  mVideoEncodeWidth=320;
                       mVideoEncodeHeight=240;*/
                       AppLog.i("TAG","摄像头分辨率："+"width:"+backSize.width+"  height:"+backSize.height);
                       break;
                   }
                    break;
                }

            }
        }
        m_liveStreamingOn = false;
        m_liveStreamingPause = false;
        m_tryToStopLivestreaming = false;
        //创建直播实例
        mLSMediaCapture = new lsMediaCapture(this, getActivity(), mVideoEncodeWidth, mVideoEncodeHeight);
        mLSMediaCapture.setCameraFocus();

        liveView.setPreviewSize(mVideoEncodeWidth, mVideoEncodeHeight);

        //创建参数实例
        mLSLiveStreamingParaCtx = mLSMediaCapture.new LSLiveStreamingParaCtx();
        mLSLiveStreamingParaCtx.eHaraWareEncType = mLSLiveStreamingParaCtx.new HardWareEncEnable();
        mLSLiveStreamingParaCtx.eOutFormatType = mLSLiveStreamingParaCtx.new OutputFormatType();
        mLSLiveStreamingParaCtx.eOutStreamType = mLSLiveStreamingParaCtx.new OutputStreamType();
        mLSLiveStreamingParaCtx.sLSAudioParaCtx = mLSLiveStreamingParaCtx.new LSAudioParaCtx();
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.codec = mLSLiveStreamingParaCtx.sLSAudioParaCtx.new LSAudioCodecType();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx = mLSLiveStreamingParaCtx.new LSVideoParaCtx();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new LSVideoCodecType();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new CameraPosition();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.interfaceOrientation = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new CameraOrientation();

        //配置音视频和camera参数
        configLiveStream();
    }

    private void configLiveStream() {
        //输出格式：视频、音频和音视频
        mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType = HAVE_AV;

        //输出封装格式
        mLSLiveStreamingParaCtx.eOutFormatType.outputFormatType = RTMP;

        //摄像头参数配置
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition.cameraPosition = CAMERA_POSITION_FRONT;//前置摄像头
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.interfaceOrientation.interfaceOrientation = CAMERA_ORIENTATION_PORTRAIT;//竖屏

        //音频编码参数配置
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.samplerate = 44100;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.bitrate = 64000;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.frameSize = 2048;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.channelConfig = AudioFormat.CHANNEL_IN_MONO;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.codec.audioCODECType = LS_AUDIO_CODEC_AAC;

        //硬件编码参数设置
        mLSLiveStreamingParaCtx.eHaraWareEncType.hardWareEncEnable = false;

        //视频编码参数配置
        if(mVideoEncodeWidth>=1280){
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 20;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 1500000;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 1280;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 720;
            AppLog.i("TAG","编码分辨率：1280");
        }else if(mVideoEncodeWidth==960&&mVideoEncodeHeight==720){
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 20;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 1000000;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 960;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 720;
            AppLog.i("TAG","编码分辨率：960");
        }else if(mVideoEncodeWidth==960&&mVideoEncodeHeight==540){
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 20;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 800000;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 960;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 540;
            AppLog.i("TAG","编码分辨率：960x540");
        }else if(mVideoEncodeWidth==640&&mVideoEncodeHeight==480){
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 20;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 600000;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 640;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 480;
            AppLog.i("TAG","编码分辨率：640");
        }else {
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 15;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 250000;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 320;
            mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 240;
            AppLog.i("TAG","编码分辨率：320x240");
        }
//        }
        if (mLSMediaCapture != null) {
            //开始本地视频预览
            mLSMediaCapture.startVideoPreview(liveView, mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition.cameraPosition);
            mLSMediaCapture.setFilterType(Filters.FILTER_WHITEN);
            AppLog.i("TAG","开启本得视频预览了啊啊啊");
        }
    }


    /**
     * ******************************** 直播控制 ********************************
     */

    /**
     * 返回是否成功开启
     *
     * @return
     */
    public boolean startStopLive() {
        if (!m_liveStreamingOn) {
            if (!m_liveStreamingPause) {
                if (mliveStreamingURL.isEmpty()) {
                    return false;
                }
                initLiveParam();
                live = true;
                return true;
            }
        }

        return true;
    }


    //开始直播
    public void startAV() {
        if (mLSMediaCapture != null) {
            mLSMediaCapture.startLiveStreaming();
            m_liveStreamingOn = true;
            m_liveStreamingPause = false;
         //   Toast.makeText(getActivity(), "开始直播", Toast.LENGTH_SHORT).show();
        }
    }

    //切换前后摄像头
    public void switchCamera() {
        if (mLSMediaCapture != null) {
            mLSMediaCapture.switchCamera();
        }
    }
    //初始化直播推流
    public void initLiveStream() {


        //初始化直播推流
        m_liveStreamingInitFinished = mLSMediaCapture.initLiveStream(mliveStreamingURL, mLSLiveStreamingParaCtx);
        m_liveStreamingInit = true;
        startAV();

    }


    /**
     * 重启直播（例如：断网重连）
     *
     * @return 是否开始重启
     */
    public boolean restartLive() {
        if (live) {
            // 必须是已经开始推流 才需要处理断网重新开始直播
            LogUtil.i(TAG, "restart live on connected");
            if (mLSMediaCapture != null) {
                mLSMediaCapture.resumeVideoPreview();
                mLSMediaCapture.initLiveStream(mliveStreamingURL, mLSLiveStreamingParaCtx);
                mLSMediaCapture.startLiveStreaming();
                m_tryToStopLivestreaming = false;
                return true;
            }
        }

        return false;
    }

    /**
     * 停止直播（例如：断网了）
     */
    public void stopLive() {
        m_tryToStopLivestreaming = true;
        if (mLSMediaCapture != null) {
            mLSMediaCapture.stopLiveStreaming();
            mLSMediaCapture.stopVideoPreview();
        }
    }

    //截图
    boolean isFirstScreennShot = true;
    public void screenShot() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mLSMediaCapture != null) {
                    mLSMediaCapture.enableScreenShot();
                }

            }
        }, 40 * 1000, 10*60 * 1000);
        isFirstScreennShot = false;
    }

    public void tryStop() {
        m_tryToStopLiveStreaming = true;
    }

    public void resetLive() {
        if (mLSMediaCapture != null && m_liveStreamingInitFinished) {
            mLSMediaCapture.stopLiveStreaming();
            mLSMediaCapture.stopVideoPreview();
            mLSMediaCapture.destroyVideoPreview();
            mLSMediaCapture = null;

            m_liveStreamingInitFinished = false;
            m_liveStreamingOn = false;
            m_liveStreamingPause = false;
            m_tryToStopLiveStreaming = false;
        } else if (!m_liveStreamingInitFinished) {
        }

        if (m_liveStreamingInit) {
            m_liveStreamingInit = false;
        }

        if (mAlertServiceOn) {
            mAlertServiceIntent = new Intent(getActivity(), AlertService.class);
            getActivity().stopService(mAlertServiceIntent);
            mAlertServiceOn = false;
        }
    }

    private void onStopStartLive(boolean isRestart) {
        if (isRestart) {
            stopLive();
        } else {
            restartLive();
        }
    }

    /**
     * ******************************** lsMessageHandler ********************************
     */

    private boolean m_liveStreamingInit = false;
    private boolean m_tryToStopLivestreaming = false;
    private boolean mAlertServiceOn = false;
    private long mLastVideoProcessErrorAlertTime = 0;
    private long mLastAudioProcessErrorAlertTime = 0;

    private lsMediaCapture.Statistics mStatistics = null;

    //处理SDK抛上来的异常和事件
    @Override
    public void handleMessage(int msg, Object object) {
        final Context context = getActivity();
        switch (msg) {
            case MSG_SCREENSHOT_FINISHED: {
                AppLog.i("TAG", "MSG_SCREENSHOT_FINISHED");
                if (onStartLiveListener != null) {
                    onStartLiveListener.screenShot((byte[]) object);
                }
            }
            break;
            case MSG_INIT_LIVESTREAMING_OUTFILE_ERROR:
                AppLog.i("TAG", "MSG_INIT_LIVESTREAMING_OUTFILE_ERROR");

                break;
            case MSG_INIT_LIVESTREAMING_VIDEO_ERROR:
                AppLog.i("TAG", "MSG_INIT_LIVESTREAMING_VIDEO_ERROR");
                break;

            case MSG_INIT_LIVESTREAMING_AUDIO_ERROR: {
                AppLog.i("TAG", "MSG_INIT_LIVESTREAMING_AUDIO_ERROR");
                if (context == null) {
                    return;
                }
                if (m_liveStreamingInit) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_INIT_LIVESTREAMING_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                }

            }
            break;
            case MSG_CAMERA_PREVIEW_SIZE_NOT_SUPPORT_ERROR://camera采集分辨率不支持
            {
                AppLog.i("TAG", "camera采集分辨率不支持");
                break;
            }
            case MSG_START_LIVESTREAMING_ERROR:
                AppLog.i("TAG", "MSG_START_LIVESTREAMING_ERROR");
                break;
            case MSG_GET_STATICS_INFO://获取统计信息的反馈消息
            {
                //Log.i(TAG, "test: in handleMessage, MSG_GET_STATICS_INFO");
                Message message = new Message();
                mStatistics = (lsMediaCapture.Statistics) object;
             /*   int videoSendFrameRate = mStatistics.videoSendFrameRate;
                int videoSendBitRate = mStatistics.videoSendBitRate;
                int audioSendBitRate = mStatistics.audioSendBitRate;
                int totalRealSendBitRate = mStatistics.totalRealSendBitRate;
                AppLog.i("TAG","videoSendFrameRate:"+String.valueOf(videoSendFrameRate) +"fps");
                AppLog.i("TAG","videoSendBitRate:"+String.valueOf(videoSendBitRate) +"fps");
                AppLog.i("TAG","audioSendBitRate:"+String.valueOf(audioSendBitRate) +"fps");
                AppLog.i("TAG","totalRealSendBitRate:"+String.valueOf(totalRealSendBitRate) +"fps");*/
                break;
            }
            case MSG_AUDIO_PROCESS_ERROR: {
                AppLog.i("TAG", "MSG_AUDIO_PROCESS_ERROR");
                if (context == null) {
                    return;
                }
                if (m_liveStreamingOn && System.currentTimeMillis() - mLastAudioProcessErrorAlertTime >= 10000) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_AUDIO_PROCESS_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                    mLastAudioProcessErrorAlertTime = System.currentTimeMillis();
                }

            }

            break;
            case MSG_VIDEO_PROCESS_ERROR: {
                AppLog.i("TAG", "test: in handleMessage, MSG_VIDEO_PROCESS_ERROR");
                if (context == null) {
                    return;
                }
                if (m_liveStreamingOn && System.currentTimeMillis() - mLastVideoProcessErrorAlertTime >= 10000) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_VIDEO_PROCESS_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                    mLastVideoProcessErrorAlertTime = System.currentTimeMillis();
                }
            }
            break;
            case MSG_SEND_STATICS_LOG_ERROR: {
                AppLog.i("TAG", "test: in handleMessage, MSG_SEND_STATICS_LOG_ERROR");
            }
            break;
            case MSG_AUDIO_SAMPLE_RATE_NOT_SUPPORT_ERROR: {
                AppLog.i("TAG", "test: in handleMessage, MSG_AUDIO_SAMPLE_RATE_NOT_SUPPORT_ERROR");
            }

            break;
            case MSG_AUDIO_PARAMETER_NOT_SUPPORT_BY_HARDWARE_ERROR: {
                AppLog.i("TAG", "test: in handleMessage, MSG_AUDIO_PARAMETER_NOT_SUPPORT_BY_HARDWARE_ERROR");
            }
            case MSG_NEW_AUDIORECORD_INSTANCE_ERROR: {
                AppLog.i("TAG", "test: in handleMessage, MSG_NEW_AUDIORECORD_INSTANCE_ERROR");
            }
            case MSG_AUDIO_START_RECORDING_ERROR: {
                AppLog.i("TAG", "test: in handleMessage, MSG_AUDIO_START_RECORDING_ERROR");
            }

            case MSG_STOP_LIVESTREAMING_ERROR: {
                AppLog.i("TAG", "test: in handleMessage, MSG_STOP_LIVESTREAMING_ERROR");
                if (context == null) {
                    return;
                }
                if (m_liveStreamingOn) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_STOP_LIVESTREAMING_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                }

                break;
            }

            case MSG_RTMP_URL_ERROR: {
                  /*
                  if(m_liveStreamingOn && System.currentTimeMillis() - mLastRtmpUrlErrorAlertTime >= 10000)
		    	  {
	      		      Bundle bundle = new Bundle();
	                  bundle.putString("alert", "MSG_RTMP_URL_ERROR");
	          	      Intent intent = new Intent(MediaPreviewActivity.this, AlertService.class);
	          	      intent.putExtras(bundle);
	      		      startService(intent);
	      		      mAlertServiceOn = true;
	      		      mLastRtmpUrlErrorAlertTime = System.currentTimeMillis();
		    	  }
		    	  */
                AppLog.i("TAG", "test: in handleMessage, MSG_RTMP_URL_ERROR");
                break;
            }
            case MSG_URL_NOT_AUTH: {
                AppLog.i("TAG", "test: in handleMessage, MSG_URL_NOT_AUTH");
                if (context == null) {
                    return;
                }
                if (m_liveStreamingInit) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_URL_NOT_AUTH");
                    Intent intent = new Intent(getActivity(), AlertService.class);
                    intent.putExtras(bundle);
                    getActivity().startService(intent);
                    mAlertServiceOn = true;
                }

                break;
            }
            case MSG_QOS_TO_STOP_LIVESTREAMING: {
                AppLog.i("TAG", "test: in handleMessage, MSG_QOS_TO_STOP_LIVESTREAMING");

                break;
            }
            case MSG_HW_VIDEO_PACKET_ERROR: {
                AppLog.i("TAG", "test: in handleMessage, MSG_HW_VIDEO_PACKET_ERROR");
                if (context == null) {
                    return;
                }
                if (m_liveStreamingOn) {
                    Bundle bundle = new Bundle();
                    bundle.putString("alert", "MSG_HW_VIDEO_PACKET_ERROR");
                    Intent intent = new Intent(context, AlertService.class);
                    intent.putExtras(bundle);
                    context.startService(intent);
                    mAlertServiceOn = true;
                }

                break;
            }
            case MSG_START_PREVIEW_FINISHED: {
                AppLog.i("TAG", "test: in handleMessage, MSG_START_PREVIEW_FINISHED：开始预览完成heh");
                break;
            }
            case MSG_START_LIVESTREAMING_FINISHED: {
                AppLog.i("TAG", "test: in handleMessage, MSG_START_LIVESTREAMING_FINISHED:开始直播");
                startTime = System.currentTimeMillis();
                if (onStartLiveListener != null) {
                    onStartLiveListener.getStartLiveStatus(true);
                }
                break;
            }
            case MSG_STOP_LIVESTREAMING_FINISHED: {
                AppLog.i("TAG", "test: in handleMessage, MSG_STOP_LIVESTREAMING_FINISHED：直播结束");
                endTime = System.currentTimeMillis();
                liveTime = endTime - startTime;
                if (onQuitLiveListener != null) {
                    onQuitLiveListener.getLiveTime(liveTime);
                }

                AppLog.i("TAG", "直播时间：" + liveTime);
                onStopStartLive(false);
                break;
            }
            case MSG_STOP_VIDEO_CAPTURE_FINISHED: {
                AppLog.i("TAG", "test: in handleMessage: MSG_STOP_VIDEO_CAPTURE_FINISHED");
                if (!m_tryToStopLivestreaming && mLSMediaCapture != null) {
                    //继续视频推流，推最后一帧图像
                    mLSMediaCapture.resumeVideoEncode();
                }
                break;
            }
            case MSG_STOP_RESUME_VIDEO_CAPTURE_FINISHED: {
                AppLog.i("TAG", "test: in handleMessage: MSG_STOP_RESUME_VIDEO_CAPTURE_FINISHED");
                if (mLSMediaCapture != null) {
                    mLSMediaCapture.resumeVideoPreview();
                    m_liveStreamingOn = true;
                    //开启视频推流，推正常帧
                    mLSMediaCapture.startVideoLiveStream();
                }
                break;
            }
            case MSG_STOP_AUDIO_CAPTURE_FINISHED: {
                AppLog.i("TAG", "test: in handleMessage: MSG_STOP_AUDIO_CAPTURE_FINISHED");
                if (!m_tryToStopLivestreaming && mLSMediaCapture != null) {
                    //继续音频推流，推静音帧
                    mLSMediaCapture.resumeAudioEncode();
                }
                break;
            }
            case MSG_STOP_RESUME_AUDIO_CAPTURE_FINISHED: {
                AppLog.i("TAG", "test: in handleMessage: MSG_STOP_RESUME_AUDIO_CAPTURE_FINISHED");
                //开启音频推流，推正常帧
                if (mLSMediaCapture != null) {
                    mLSMediaCapture.startAudioLiveStream();
                }
                break;
            }
            case MSG_SWITCH_CAMERA_FINISHED: {
                AppLog.i("TAG", "test: in handleMessage: MSG_SWITCH_CAMERA_FINISHED：切换摄像头完成");
                int cameraId = (Integer) object;//切换之后的camera id
                break;
            }
            case MSG_SEND_STATICS_LOG_FINISHED: {
                AppLog.i("TAG", "test: in handleMessage, MSG_SEND_STATICS_LOG_FINISHED");
                break;
            }
            case MSG_START_PREVIEW_ERROR:
                AppLog.i("TAG", "视频预览出错！！！！！！！！！！！！！！！！！");
                if(onCheckCameraListener!=null){
                    AppLog.i("TAG","没有开启嘎嘎嘎摄像头");
                    onCheckCameraListener.getCameraOpenStatus();

                }
                break;

            case MSG_AUDIO_RECORD_ERROR:
                AppLog.i("TAG", "音频权限开启失败！！！！！！！！！！！！！！！！！");
                if(onCheckCameraListener!=null){
                    onCheckCameraListener.getAudioOpenStatus();
                }
                break;
        }
    }

    private OnQuitLiveListener onQuitLiveListener;

    public interface OnQuitLiveListener {
        void getLiveTime(long liveTime);
    }

    public void setOnQuitLiveListener(OnQuitLiveListener onQuitLiveListener) {
        this.onQuitLiveListener = onQuitLiveListener;
    }

    private OnStartLiveListener onStartLiveListener;

    public interface OnStartLiveListener {
        void getStartLiveStatus(boolean onStart);

        void screenShot(byte[] bytes);
    }

    public void setOnStartLiveListener(OnStartLiveListener onStartLiveListener) {
        this.onStartLiveListener = onStartLiveListener;
    }

    private  OnCheckCameraListener onCheckCameraListener;
    public interface OnCheckCameraListener{
        void getCameraOpenStatus();
        void getAudioOpenStatus();
    }
    public void setOnCheckCameraListener(OnCheckCameraListener onCheckCameraListener){
        this.onCheckCameraListener=onCheckCameraListener;
    }





    private Activity getActivity() {
        return activityProxy.getActivity();
    }
}
