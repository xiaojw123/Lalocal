package com.lalocal.lalocal.util;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by wangjie on 2016/9/28.
 */
public class CustomCachingGlideModule implements GlideModule{

    /**
     * 设置glide内存缓存大小为默认缓存的20%,磁盘缓存为100M
     * @param context
     * @param glideBuilder
     */
    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCache = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        int cacheSize100MegaBytes = 104857600; // 100M大小

        // 指定缓存目录
        String downloadDirectoryPath = Environment.getDownloadCacheDirectory().getPath();

        glideBuilder.setMemoryCache(new LruResourceCache(customMemoryCache));
        glideBuilder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        glideBuilder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
//        glideBuilder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize100MegaBytes));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
