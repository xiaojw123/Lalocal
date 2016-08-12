package com.lalocal.lalocal.service;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * Created by android on 2016/7/19.
 */
public class NetInfoService extends Service {
    Dialog mDialog;
    WindowManager.LayoutParams wmParams;
    LayoutInflater inflater;

    public static Boolean isShown = false;
    private TextView videoFrameRateTV, videoBitRateTV, audioBitRateTV, totalRealBitRateTV, ResolutionTV;
    private MsgReceiver msgReceiver;

    private int mVideoFrameRate = 0;
    private int mVideoBitrate = 0;
    private int mAudioBitrate = 0;
    private int mTotalRealBitrate = 0;

    private int mResolution = 0;

    private Intent mIntent = new Intent("com.netease.livestreamingcapture");

    @Override
    public IBinder onBind(Intent intent) {
        // TODO 自动生成的方法存根
        return null;
    }

    @Override
    public void onCreate() {
        // TODO 自动生成的方法存根
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.netease.netInfo");
        registerReceiver(msgReceiver, intentFilter);

        initWindow();
        return START_NOT_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDialog != null){
            mDialog.dismiss();
        }
        unregisterReceiver(msgReceiver);
        msgReceiver = null;
    }


    /**
     * 初始化
     */

    private void initWindow() {
        mDialog = new Dialog(NetInfoService.this);
        mDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));

        inflater = LayoutInflater.from(getApplication());
        View view = inflater.inflate(R.layout.net_info_layout, null);

        videoFrameRateTV = (TextView)view.findViewById(R.id.VideoFrameRateTV);
        videoBitRateTV = (TextView)view.findViewById(R.id.VideoBitRateTV);
        audioBitRateTV = (TextView)view.findViewById(R.id.AudioBitRateTV);
        totalRealBitRateTV = (TextView)view.findViewById(R.id.TotalRealBitRateTV);
        ResolutionTV = (TextView)view.findViewById(R.id.ResolutionTV);

        videoFrameRateTV.setText(String.valueOf(mVideoFrameRate) + " fps");
        videoBitRateTV.setText(String.valueOf(mVideoBitrate) + " kbps");
        audioBitRateTV.setText(String.valueOf(mAudioBitrate) + " kbps");
        totalRealBitRateTV.setText(String.valueOf(mTotalRealBitrate) + " kbps");
        if(mResolution == 1)
        {
            ResolutionTV.setText("高清");
        }
        else if(mResolution == 2)
        {
            ResolutionTV.setText("标清");
        }
        else if(mResolution == 3)
        {
            ResolutionTV.setText("流畅");
        }

        mDialog.setContentView(view);
        mDialog.setTitle("网络信息");

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    /**
     * 广播接收器
     * @author len
     *
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int videoFrameRate = intent.getIntExtra("videoFrameRate", 0);
            int videoBitRate = intent.getIntExtra("videoBitRate", 0);
            int audioBitRate = intent.getIntExtra("audioBitRate", 0);
            int totalRealBitRate = intent.getIntExtra("totalRealBitrate", 0);
            int resolution = intent.getIntExtra("resolution", 0);

            mVideoFrameRate = videoFrameRate;
            mVideoBitrate = videoBitRate;
            mAudioBitrate = audioBitRate;
            mTotalRealBitrate = totalRealBitRate;
            mResolution = resolution;

            videoFrameRateTV.setText(String.valueOf(videoFrameRate) + " fps");
            videoBitRateTV.setText(String.valueOf(videoBitRate) + " kbps");
            audioBitRateTV.setText(String.valueOf(audioBitRate) + " kbps");
            totalRealBitRateTV.setText(String.valueOf(totalRealBitRate) + " kbps");

            if(mResolution == 1)
            {
                ResolutionTV.setText("高清");
            }
            else if(mResolution == 2)
            {
                ResolutionTV.setText("标清");
            }
            else if(mResolution == 3)
            {
                ResolutionTV.setText("流畅");
            }
        }
    }

}
