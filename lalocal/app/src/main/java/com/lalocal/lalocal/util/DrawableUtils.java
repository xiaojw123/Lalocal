package com.lalocal.lalocal.util;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.concurrent.Executors;

public class DrawableUtils {
    public static final int MAX_AGE = 24 * 3600 * 1000;
    private static  final  int DRAWABLE_NULL=R.drawable.company_logo;
    private static ImageLoader loader;

    public static void displayImg(Context context, ImageView img, String url) {
        if (loader == null) {
            loader = ImageLoader.getInstance();
        }
        if (!loader.isInited()) {
            init(context);
        }

        File imgFile = loader.getDiskCache().get(url);

        if (imgFile == null || !imgFile.exists()) {
            loader.displayImage(url, img, getImageOptions(0,DRAWABLE_NULL));
        } else {
            String fileUri = "file://" + imgFile.getAbsolutePath();
            loader.displayImage(fileUri, img);
        }
    }

    private static void init(Context context) {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.diskCache(new LimitedAgeDiskCache(getFileDir(), MAX_AGE));
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        builder.threadPoolSize(3);
        builder.tasksProcessingOrder(QueueProcessingType.FIFO);
        builder.taskExecutor(Executors.newCachedThreadPool());
        builder.threadPriority(Thread.MAX_PRIORITY);
        builder.writeDebugLogs();
        loader.init(builder.build());
    }

    private static DisplayImageOptions getImageOptions(int radius,int resID) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        if (resID!=DRAWABLE_NULL){
            builder.showImageForEmptyUri(resID);
            builder.showImageOnLoading(resID);
            builder.showImageOnFail(resID);
        }
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.displayer(new RoundedBitmapDisplayer(radius));
//		builder.displayer(new CircleBitmapDisplayer())
        builder.imageScaleType(ImageScaleType.EXACTLY);
        return builder.build();
    }

    private static File getFileDir() {
        String path = Environment.getExternalStorageDirectory().getPath();
        AppLog.print("sdcard path___" + path);
        File file = new File(path + "/SeeingVideo/Img");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

}
