package com.github.commons.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;

import com.github.commons.utils.CommonUtil;


/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/7/2 11:41
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class ImageUtils {

    /**获取每个像素占用的字节数*/
    public static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    /**是否可以复用内存块*/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean canUseForInBitmap(
            Bitmap candidate, BitmapFactory.Options targetOptions) {
        if (!CommonUtil.hasKitKat()) {
            return candidate.getWidth() == targetOptions.outWidth
                    && candidate.getHeight() == targetOptions.outHeight
                    && targetOptions.inSampleSize == 1;
        }

        int width = targetOptions.outWidth / targetOptions.inSampleSize;
        int height = targetOptions.outHeight / targetOptions.inSampleSize;
        int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
        return byteCount <= candidate.getAllocationByteCount();
    }

    /**获取bitmap字节大小*/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int getBitmapSize(BitmapDrawable value) {
        Bitmap bitmap = value.getBitmap();

        if (CommonUtil.hasKitKat()) {
            return bitmap.getAllocationByteCount();
        }

        if (CommonUtil.hasHoneycombMR1()) {
            return bitmap.getByteCount();
        }

        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
