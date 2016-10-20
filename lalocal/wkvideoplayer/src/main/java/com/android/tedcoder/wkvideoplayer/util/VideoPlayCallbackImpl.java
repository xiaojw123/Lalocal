package com.android.tedcoder.wkvideoplayer.util;

import android.widget.ImageView;

/**
 * Created by android on 2016/10/19.
 */
public interface VideoPlayCallbackImpl {
    void onCloseVideo();

    void onSwitchPageType();

    void onPlayFinish();

    void onPlayStatus(boolean isPlay);

    void onClickQuit();
    void onClickShare();
    void  onClickBefore(ImageView view);
    void onClickNext(ImageView view);
}
