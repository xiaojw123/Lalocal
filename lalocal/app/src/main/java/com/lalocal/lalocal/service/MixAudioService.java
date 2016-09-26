package com.lalocal.lalocal.service;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * Created by android on 2016/7/19.
 */
public class MixAudioService extends Service implements SeekBar.OnSeekBarChangeListener {
    Dialog mDialog;
    WindowManager.LayoutParams wmParams;
    LayoutInflater inflater;

    public static Boolean isShown = false;
    private boolean mAudioMixOn = false;
    private boolean mAudioMixPause = false;

    private Button startAudioMixBtn, pauseResumeAudioMixBtn, stopAudioMixBtn;
    private Spinner mixAudioFileSpinner;
    private SeekBar mixAudioVolumeBar;
    private TextView mixAudioVolumeTV;

    private int m_mixAudioVolumeProgress = 20;
    private int m_mixAudioVolume = 0;

    private Intent mIntentAudioMix = new Intent("AudioMix");
    private Intent mIntentAudioMixVolume = new Intent("AudioMixVolume");

    private static final String[] mixAudioFileArray = {
            "yueyunpengchangwuhuanzhige.mp3",
            "najigejianbingzouba.mp3",
    };
    private ArrayAdapter<String> adapter;
    private String mixAudioFile;
    private static final String TAG = "NeteaseLiveStream";

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initWindow();
        return START_NOT_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDialog != null){
            mDialog.dismiss();
        }
    }


    private void initMixAudioFileSpinner(View view)
    {
        mixAudioFileSpinner = (Spinner)view.findViewById(R.id.mixAudioFileSpinner);
        if(Build.VERSION.SDK_INT >=11)
        {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mixAudioFileArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            mixAudioFileSpinner.setAdapter(adapter);
            mixAudioFileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    mixAudioFile = mixAudioFileArray[arg2];
                    mIntentAudioMix.putExtra("AudioMixFilePathMSG", mixAudioFile);
                    sendBroadcast(mIntentAudioMix);
                }

                public void onNothingSelected(AdapterView<?> arg0) {}
            });
            mixAudioFileSpinner.setVisibility(View.VISIBLE);
        }else{
            mixAudioFileSpinner.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化
     */
    private void initWindow() {
        mDialog = new Dialog(MixAudioService.this);
        mDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));

        inflater = LayoutInflater.from(getApplication());
        View view = inflater.inflate(R.layout.mix_audio_layout, null);

        startAudioMixBtn = (Button) view.findViewById(R.id.StartAudioMixBtn);
        if(!mAudioMixOn)
        {
            startAudioMixBtn.setEnabled(true);
        }
        else
        {
            startAudioMixBtn.setEnabled(false);
        }

        startAudioMixBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                mIntentAudioMix.putExtra("AudioMixMSG", 1);
                sendBroadcast(mIntentAudioMix);
                mAudioMixOn = true;
                pauseResumeAudioMixBtn.setText("暂停");
                startAudioMixBtn.setEnabled(false);
                stopAudioMixBtn.setEnabled(true);
                pauseResumeAudioMixBtn.setEnabled(true);
            }

        });

        pauseResumeAudioMixBtn = (Button) view.findViewById(R.id.PauseResumeAudioMixBtn);
        if(mAudioMixOn && !mAudioMixPause)
        {
            pauseResumeAudioMixBtn.setText("暂停");
            pauseResumeAudioMixBtn.setEnabled(true);
        }
        else if(mAudioMixOn && mAudioMixPause)
        {
            pauseResumeAudioMixBtn.setText("继续");
            pauseResumeAudioMixBtn.setEnabled(true);
        }
        else
        {
            pauseResumeAudioMixBtn.setText("继续");
            pauseResumeAudioMixBtn.setEnabled(false);
        }

        pauseResumeAudioMixBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(!mAudioMixPause)
                {
                    mIntentAudioMix.putExtra("AudioMixMSG", 3);
                    sendBroadcast(mIntentAudioMix);
                    mAudioMixPause = true;
                    pauseResumeAudioMixBtn.setText("继续");
                    pauseResumeAudioMixBtn.setEnabled(true);
                }
                else
                {
                    mIntentAudioMix.putExtra("AudioMixMSG", 2);
                    sendBroadcast(mIntentAudioMix);
                    mAudioMixPause = false;
                    pauseResumeAudioMixBtn.setText("暂停");
                    pauseResumeAudioMixBtn.setEnabled(true);
                }
            }

        });

        stopAudioMixBtn = (Button) view.findViewById(R.id.StopAudioMixBtn);
        if(mAudioMixOn)
        {
            stopAudioMixBtn.setEnabled(true);
        }
        else
        {
            stopAudioMixBtn.setEnabled(false);
        }
        stopAudioMixBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                mIntentAudioMix.putExtra("AudioMixMSG", 4);
                sendBroadcast(mIntentAudioMix);
                mAudioMixOn = false;
                mAudioMixPause = false;
                startAudioMixBtn.setEnabled(true);
                pauseResumeAudioMixBtn.setEnabled(false);
                pauseResumeAudioMixBtn.setText("继续");
                stopAudioMixBtn.setEnabled(false);
            }

        });


        initMixAudioFileSpinner(view);

        mixAudioVolumeBar = (SeekBar)view.findViewById(R.id.mixAudioVolumeBar);
        mixAudioVolumeBar.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);

        mixAudioVolumeTV = (TextView)view.findViewById(R.id.mixAudioVolumeTV);
        if(mAudioMixOn)
        {
            mixAudioVolumeTV.setText(String.valueOf(m_mixAudioVolumeProgress) + "%");
            mIntentAudioMixVolume.putExtra("AudioMixVolumeMSG", m_mixAudioVolume);
            mixAudioVolumeBar.setProgress(m_mixAudioVolumeProgress);
        }
        else
        {
            mixAudioVolumeTV.setText("20%");
            mIntentAudioMixVolume.putExtra("AudioMixVolumeMSG", 20/10);
            mixAudioVolumeBar.setProgress(20);
        }

        sendBroadcast(mIntentAudioMixVolume);

        mDialog.setContentView(view);
        mDialog.setTitle("伴音选择");

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // TODO Auto-generated method stub
        m_mixAudioVolumeProgress = progress;
        m_mixAudioVolume = progress/10;//0-9
        mixAudioVolumeTV.setText(String.valueOf(progress) + "%");

        mIntentAudioMixVolume.putExtra("AudioMixVolumeMSG", m_mixAudioVolume);
        sendBroadcast(mIntentAudioMixVolume);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

}
