package com.lalocal.lalocal.live.entertainment.fragment;


import com.lalocal.lalocal.activity.fragment.BaseFragment;

/**
 * Created by ${WCJ} on 2017/1/9.
 */

public class YaseaFragment extends BaseFragment {

}
/*
public class YaseaFragment extends BaseFragment implements RtmpHandler.RtmpListener, SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener{
    @BindView(R.id.glsurfaceview_camera)
    SrsCameraView glsurfaceviewCamera;
    @BindView(R.id.yasea_layout)
    RelativeLayout yaseaLayout;
    private String pushUrl;
    private SrsPublisher mPublisher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yasea_layout, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pushUrl = bundle.getString(KeyParams.PUSHURL);
        }
    }

    private void initView() {
        mPublisher = new SrsPublisher(glsurfaceviewCamera);
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        mPublisher.setPreviewResolution(640, 480);
        mPublisher.setOutputResolution(720, 1280);
        mPublisher.setVideoHDMode();
        mPublisher.startPublish(pushUrl);
        mPublisher.setVideoSmoothMode();

       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               handler.sendEmptyMessage(400);
           }
       }, 2000);
    }

    Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==400){
                openBeauty();
            }
        }
    };

    YaseaCallBackListener yaseaCallBackListener;
    public void setAgoraCallBackListener(YaseaCallBackListener yaseaCallBackListener) {
        this.yaseaCallBackListener = yaseaCallBackListener;
    }

    public interface YaseaCallBackListener {
        void onConnectionInterrupted();
        void onJoinChannelSuccess();
    }


    public void setSwitchCamera(){
        if (Camera.getNumberOfCameras() > 0) {
            mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
        }
    }

    public void openBeauty(){
        mPublisher.switchCameraFilter(MagicFilterType.BEAUTY);
    }
    public void closeBeauty(){
        mPublisher.switchCameraFilter(MagicFilterType.NONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPublisher.resumeRecord();
    }
    @Override
    public void onPause() {
        super.onPause();
        mPublisher.pauseRecord();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }

    @Override
    public void onRtmpConnecting(String msg) {

    }

    @Override
    public void onRtmpConnected(String msg) {
        AppLog.d("TAG","  yasea:   onRtmpConnected: "+msg);
        if(yaseaCallBackListener!=null){
            yaseaCallBackListener.onJoinChannelSuccess();
        }
    }

    @Override
    public void onRtmpVideoStreaming() {
        AppLog.d("TAG","  yasea:   onRtmpVideoStreaming: ");
    }

    @Override
    public void onRtmpAudioStreaming() {
        AppLog.d("TAG","  yasea:   onRtmpAudioStreaming: ");
    }

    @Override
    public void onRtmpStopped() {

    }

    @Override
    public void onRtmpDisconnected() {

    }

    @Override
    public void onRtmpVideoFpsChanged(double fps) {
        AppLog.i("TAG", "yasea:  onRtmpVideoFpsChanged: "+String.format("Output Fps: %f", fps));
    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {

        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            AppLog.i("TAG", "yasea:onRtmpVideoBitrateChanged:   "+String.format("Video bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i("TAG", "yasea: onRtmpVideoBitrateChanged:    "+String.format("Video bitrate: %d bps", rate));
        }

    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            AppLog.i("TAG", "yasea: onRtmpVideoBitrateChanged:  "+String.format("Audio bitrate: %f kbps", bitrate / 1000));
        } else {
            AppLog.i("TAG", "yasea: onRtmpVideoBitrateChanged:  "+String.format("Audio bitrate: %d bps", rate));
        }
    }

    @Override
    public void onRtmpSocketException(SocketException e) {

    }

    @Override
    public void onRtmpIOException(IOException e) {

    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {

    }

    @Override
    public void onNetworkWeak() {

    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRecordPause() {

    }

    @Override
    public void onRecordResume() {

    }

    @Override
    public void onRecordStarted(String msg) {

    }

    @Override
    public void onRecordFinished(String msg) {

    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRecordIOException(IOException e) {

    }



}
*/
