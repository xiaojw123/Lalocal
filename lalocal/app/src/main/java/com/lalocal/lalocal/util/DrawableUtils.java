package com.lalocal.lalocal.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.concurrent.Executors;

public class DrawableUtils {
    public static final int MAX_AGE = 24 * 3600 * 1000;
    public static final int DISK_MAX_SIZE = 50 * 1024 * 1024;
    public static final int MEMORY_MAX_SIZE = 5 * 1024 * 1024;
    private static final int DRAWABLE_NULL = R.drawable.androidloading;
    private static ImageLoader loader;
    //改变drwable颜色
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static void displayImg(Context context, ImageView img, String url) {
        displayImg(context, img, url, 0, DRAWABLE_NULL, null);
    }

    public static void displayImg(Context context, ImageView img, String url, int drawable, ImageLoadingListener listener) {
        displayImg(context, img, url, 0, drawable, listener);
    }


    public static void displayImg(Context context, ImageView img, String url, int drawable) {
        displayImg(context, img, url, 0, drawable, null);
    }

    public static void displayImg(Context context, ImageView img, String url, int radius, int drawable) {
        displayImg(context, img, url, radius, drawable, null);
    }


    public static void displayImg(Context context, ImageView img, String url, int radius, int drawable, ImageLoadingListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (loader == null) {
            loader = ImageLoader.getInstance();
        }
        if (!loader.isInited()) {
            init(context,img);
        }

        File imgFile = loader.getDiskCache().get(url);

        if (imgFile == null || !imgFile.exists()) {
            loader.displayImage(url, img, getImageOptions(radius, drawable), listener);

        } else {
            String fileUri = "file://" + imgFile.getAbsolutePath();
            loader.displayImage(fileUri, img, listener);

        }
    }

    public static void displayRadiusImg(Context context, ImageView img, String url, int radius, int drawable) {
        if (loader == null) {
            loader = ImageLoader.getInstance();
        }
        if (!loader.isInited()) {
            init(context,img);
        }

        File imgFile = loader.getDiskCache().get(url);
        loader.displayImage(url, img, getImageOptions(radius, drawable));
    }

    private static void init(Context context,ImageView img) {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.diskCache(new LimitedAgeDiskCache(getFileDir(), MAX_AGE));
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        builder.threadPoolSize(3);
        builder.diskCacheSize(DISK_MAX_SIZE);
        builder.memoryCacheSize(MEMORY_MAX_SIZE);
        builder.tasksProcessingOrder(QueueProcessingType.FIFO);
        builder.taskExecutor(Executors.newCachedThreadPool());
        builder.threadPriority(Thread.MAX_PRIORITY);
        builder.diskCacheExtraOptions(img.getWidth(),img.getHeight(),null);
        builder.memoryCacheExtraOptions(img.getWidth(),img.getHeight());
        builder.writeDebugLogs();
        loader.init(builder.build());
    }

    private static DisplayImageOptions getImageOptions(int radius, int resID) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();

        if (resID != -1) {
            builder.showImageForEmptyUri(resID);
            builder.showImageOnLoading(resID);
            builder.showImageOnFail(resID);
        }
        if (radius > 0) {
            builder.displayer(new RoundedBitmapDisplayer(radius));
        }
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.imageScaleType(ImageScaleType.EXACTLY);

        return builder.build();
    }

    public static File getFileDir() {
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path + "/Lalocal/Img");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getCacheImgFile() {
        if (loader != null) {
            DiskCache diskCache = loader.getDiskCache();
            if (diskCache != null) {
                return diskCache.getDirectory();
            }
        }
        return null;
    }


    ;


}
