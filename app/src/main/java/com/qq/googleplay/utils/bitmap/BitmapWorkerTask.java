package com.qq.googleplay.utils.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.qq.googleplay.R;

import java.lang.ref.WeakReference;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/30 11:44
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

    private Context mContext;
    private final WeakReference imageViewReference;
    private DiskLruCache mDiskLruCache;
    private final Object mDiskLruCacheLock = new Object();
    private boolean mDiskLruCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB 磁盘缓存
    private static final String DISK_CACHE_SUBDIR = "thumbnails";
    public Bitmap mLoadingBitmap;
    protected Resources mResources;
    private int data = 0;
    private int reqWidth;
    private int reqHeight;

    public BitmapWorkerTask(ImageView imageView, Context context) {
        // 双向弱引用关联解决异步加载图片乱序问题
        imageViewReference = new WeakReference(imageView);
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_default);
        mContext = context;
        mResources = context.getResources();
        //File cacheDir = getDiskCacheDir(this, DISK_CACHE_SUBDIR);
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        reqWidth = params[1];
        reqHeight = params[2];
        Bitmap bitmap = BitmapUtils.decodeResource(mResources, data, reqWidth, reqHeight);
        return bitmap;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = (ImageView) imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /*public Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {}
            }
            if (mDiskLruCache != null) {
                return mDiskLruCache.get(key);
            }
        }
        return null;
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        // Add to memory cache as before
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }

        // Also add to disk cache
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
                mDiskLruCache.put(key, bitmap);
            }
        }*/

    public void setLoadingImage(Bitmap bitmap) {
        mLoadingBitmap = bitmap;
    }

    public void setLoadingImage(int resId) {
        mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId);
    }

    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }
}
