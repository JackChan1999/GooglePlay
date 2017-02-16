package com.qq.googleplay.utils.bitmap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.qq.googleplay.R;

/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：GooglePlay
 * Package_Name：com.qq.googleplay
 * Version：1.0
 * time：2016/2/16 13:33
 * des ：${TODO}
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public enum  BitmapHelper {
    instance;

    public RequestManager manager;

    public BitmapHelper init(Context context){
        manager =  Glide.with(context);
        return this;
    }

    public void  display(ImageView iv ,String url){
        //iv.setTag(R.id.image_tag,url);
        if (manager != null){
            manager.load(url).placeholder(new ColorDrawable(Color.parseColor("#eaeaea")))
                    .error(R.mipmap.ic_default).into(iv);
        }
    }

    public void  display(ImageView iv ,int resId){
        //iv.setTag(R.id.image_tag,url);
        if (manager != null){
            manager.load(resId).error(R.mipmap.ic_default).into(iv);
        }
    }



    /**优先加载图片*/
    public  void loadImageWithHighPriority(ImageView iv ,String url) {
        if (manager != null){
            manager.load(url).priority( Priority.HIGH ).error(R.mipmap.ic_default).into(iv);
        }
    }

    public  void loadImagesWithLowPriority(ImageView iv ,String url) {
        if (manager != null){
            manager.load(url).priority( Priority.LOW ).error(R.mipmap.ic_default).into(iv);
        }
    }

    /*public void loadBitmap(int resId, ImageView imageView, Context context) {
        if (BitmapWorkerTask.cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView,context);
          final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(context.getResources(), task.mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }*/


   /* public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }*/

}
