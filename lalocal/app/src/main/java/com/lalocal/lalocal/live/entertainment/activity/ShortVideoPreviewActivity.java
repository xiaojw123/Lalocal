package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.util.AppLog;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import openlive.agora.io.videocompressorlib.CompressListener;
import openlive.agora.io.videocompressorlib.Compressor;
import openlive.agora.io.videocompressorlib.InitListener;

/**
 * Created by ${WCJ} on 2017/1/13.
 */
public class ShortVideoPreviewActivity extends BaseActivity {
    @BindView(R.id.video_preview)
    PLVideoTextureView videoPreview;
    @BindView(R.id.upload_progress)
    ProgressBar uploadProgress;
    @BindView(R.id.video_compress_progress_layout)
    LinearLayout videoCompressProgressLayout;
    @BindView(R.id.video_cancel)
    TextView videoCancel;
    @BindView(R.id.video_pause)
    ImageView videoPause;
    @BindView(R.id.video_ok)
    TextView videoOk;
    @BindView(R.id.video_bottom_layout)
    LinearLayout videoBottomLayout;
    private String videPath;
    private String rotation;
    String videoDirection="0";
    private String currentInputVideoPath="";
    private String currentOutputVideoPath = "/mnt/sdcard/videokit/out.mp4";
    String cmd ="";
    private Compressor mCompressor;
    private String duration;
    private File mFile;
    private MyCompressListener myCompressListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_preview_layout);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            videPath = extras.getString(KeyParams.VIDEO_PREVIEW_PATH);
            videoPreview.setVideoPath(videPath);
            AppLog.d("TAG","获取视频地址你发女女女女:"+videPath);
            currentInputVideoPath=videPath;
            cmd="-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                    "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 48k -s 840x480 -aspect 16:9 " + currentOutputVideoPath;
           // videoPreview.pause();
            MediaMetadataRetriever retr = new MediaMetadataRetriever();
            retr.setDataSource(videPath);
          String  rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            duration = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(rotation.equals("90")){
                videoPreview.setDisplayOrientation(270);
                videoDirection="0";
            }else {
                videoPreview.setDisplayOrientation(0);
                videoDirection="1";
            }
            videoPreview.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
            initCompressorVideo();
        }
    }




    int playStatus=0;
    @OnClick({R.id.video_pause,R.id.video_ok,R.id.video_cancel})
    public  void btnClick(View v){
        switch (v.getId()){
            case R.id.video_cancel:
                finish();
                break;
            case R.id.video_ok:
                videoCompressProgressLayout.setVisibility(View.VISIBLE);
                execCommand(cmd);
                break;
            case R.id.video_pause:
                if(playStatus==0){
                    playStatus=1;
                    videoPreview.start();
                    videoPause.setImageResource(R.drawable.livingreplay_unplay);
                }else {
                    int i = playStatus % 2;
                    if(i==0){
                        videoPreview.start();
                        videoPause.setImageResource(R.drawable.livingreplay_unplay);
                    }else{
                        videoPreview.pause();
                        videoPause.setImageResource(R.drawable.livingreplay_play);
                    }
                    playStatus++;
                }
                break;
        }
    }

    boolean isCompressorSuccess=false;

    private void initCompressorVideo() {
        File file = new File(currentOutputVideoPath);
        if (file.exists()) {
            file.delete();
        }
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                Log.v("TAG", "load library succeed");
            }

            @Override
            public void onLoadFail(String reason) {
                Log.i("TAG", "load library fail:" + reason);
            }

        });
    }
    private void execCommand(String cmd) {
        mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }
        myCompressListener = new MyCompressListener();
        mCompressor.execCommand(cmd, myCompressListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isCompressorSuccess&&mCompressor!=null){
            myCompressListener=null;
            mCompressor.execCommand(null,null);
           mCompressor.killProgress();
        }
    }


    public  class  MyCompressListener implements CompressListener{
        @Override
        public void onExecSuccess(String message) {
            isCompressorSuccess=true;
            Log.i("TAG", "success " + message);
            Toast.makeText(getApplicationContext(), "视频压缩完成! ", Toast.LENGTH_SHORT).show();
            videoPreview.stopPlayback();
            Intent intent=new Intent();
            intent.putExtra("saveFilePath",currentOutputVideoPath);
            intent.putExtra("videoDirection",videoDirection);
            setResult(RESULT_OK,intent);
            finish();
        }

        @Override
        public void onExecFail(String reason) {
            Log.i("TAG", "fail " + reason);
        }
        @Override
        public void onExecProgress(String message) {
            Log.i("TAG", "progress " + message);
            if(message.contains("time=")&&message.contains("bitrate")){
                String time = message.substring(message.indexOf("time=") + 5, message.indexOf("bitrate"));
                String substring = time.substring(0, 8);
                String[] split = substring.split(":");
                int compressorDuration=(Integer.parseInt(split[0])*60*1000+Integer.parseInt(split[1])*1000+Integer.parseInt(split[2]))*1000;
                int videoDuration = Integer.parseInt(duration);
                int i = (int)((((compressorDuration*1.0) /videoDuration)) * 100);
                AppLog.d("TAG","压缩进度："+compressorDuration+"  视频长度："+videoDuration+"   i:"+i);
                uploadProgress.setProgress(i);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPreview.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPreview.stopPlayback();
    }


}
