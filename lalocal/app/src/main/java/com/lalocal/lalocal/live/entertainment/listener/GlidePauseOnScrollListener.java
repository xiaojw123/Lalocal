package com.lalocal.lalocal.live.entertainment.listener;

import com.bumptech.glide.Glide;

import cn.finalteam.galleryfinal.PauseOnScrollListener;

/**
 * Created by wangjie on 2016/10/11.
 */
public class GlidePauseOnScrollListener extends PauseOnScrollListener {
    public GlidePauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        super(pauseOnScroll, pauseOnFling);
    }

    @Override
    public void resume() {
        Glide.with(getActivity()).resumeRequests();
    }

    @Override
    public void pause() {
        Glide.with(getActivity()).pauseRequests();
    }
}
