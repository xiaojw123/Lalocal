package com.lalocal.lalocal.live.entertainment.agora.openlive;

import android.view.SurfaceView;

import java.lang.ref.SoftReference;

public class VideoStatusData {
    public static final int DEFAULT_STATUS = 0;
    public static final int VIDEO_MUTED = 1;
    public static final int AUDIO_MUTED = VIDEO_MUTED << 1;

    public static final int DEFAULT_VOLUME = 0;

    public VideoStatusData(int uid, SoftReference<SurfaceView> view, int status, int volume) {
        this.mUid = uid;
        this.mView = view;
        this.mStatus = status;
        this.mVolume = volume;
    }

    public int mUid;

    public SoftReference<SurfaceView> mView;

    public int mStatus;

    public int mVolume;

    @Override
    public String toString() {
        return "VideoStatusData{" +
                "mUid=" + mUid +
                ", mView=" + mView +
                ", mStatus=" + mStatus +
                ", mVolume=" + mVolume +
                '}';
    }
}
