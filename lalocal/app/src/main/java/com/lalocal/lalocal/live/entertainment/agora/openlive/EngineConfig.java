package com.lalocal.lalocal.live.entertainment.agora.openlive;

public class EngineConfig {
    public int mClientRole;

    public int mVideoProfile;

    public int mUid;

    public String mChannel;

    public void reset() {
        mChannel = null;
    }

    EngineConfig() {
    }
}
