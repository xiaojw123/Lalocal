package com.lalocal.lalocal.live.entertainment.ui.assist;

import com.lalocal.lalocal.live.entertainment.ui.ApngImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PngImageLoader extends ImageLoader {
    private static PngImageLoader singleton;

    public static PngImageLoader getInstance() {
        if (singleton == null) {
            synchronized (ApngImageLoader.class) {
                if (singleton == null) {
                    singleton = new PngImageLoader();
                }
            }
        }
        return singleton;
    }

    protected PngImageLoader() { /*Singleton*/ }
}
