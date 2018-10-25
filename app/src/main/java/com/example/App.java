package com.example;

import android.app.Application;
import android.util.Log;

import com.example.topgridlibrary.topgrid.app.AppApplication;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class App extends AppApplication {
    @Override
    public void onCreate() {
        super.onCreate();
//        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "/fanzhuopu110");
//        Log.i("缓存路径>>>>>>>..", cacheDir + "");
//        ImageLoaderConfiguration imageLoaderConfiguration = ImageLoaderConfiguration.createDefault(this);
//                Builder(getApplicationContext()).
//                diskCache(new UnlimitedDiskCache(cacheDir)).
//                diskCacheSize(50 * 1024 * 1024).
//                diskCacheFileCount(100).
//                defaultDisplayImageOptions(displayOption()).
//                build();
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.
                Builder(this).
                diskCacheSize(1024 * 10).
                diskCache(new UnlimitedDiskCache(new File(getCacheDir().getAbsolutePath()))).
                memoryCacheSize(1024 * 10).
                memoryCache(new LruMemoryCache(1024 * 10)).
                build();
        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }

//    private DisplayImageOptions displayOption() {
//        DisplayImageOptions option = new DisplayImageOptions.
//                Builder().
//                displayer(new RoundedBitmapDisplayer(50)).
//                cacheOnDisk(true).
//                build();
//        return option;
//    }
}
