package com.qq.googleplay.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;
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
public class ResolutionUtil {
    static final String TAG = ResolutionUtil.class.getSimpleName();

    public static int getHeight(Context context, String height, String width) {
        return getHeight(context, Integer.parseInt(width), Integer.parseInt(height));
    }

    public static int getHeight(Context context, int width, int height) {
        return getHeight(context, getScreenWidth(context), width, height);
    }

    public static int getHeight(Context context, int originWidth, int width, int height) {
        return (height * originWidth) / width;
    }

    public static int getWidthBasedHeight(int width, int height, int baseHieght) {
        return (width / height) * baseHieght;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        float width = (float) bgimage.getWidth();
        float height = (float) bgimage.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) newWidth) / width, ((float) newHeight) / height);
        return Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
    }

    public static Bitmap getViewBitmap(View v) {
        View view = v.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static Bitmap cutBitmap(Bitmap bmp, int cut, int pos) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        if (pos == 0) {
            return Bitmap.createBitmap(bmp, 0, cut, width, height - cut, new Matrix(), true);
        }
        return Bitmap.createBitmap(bmp, 0, 0, width, height - cut, new Matrix(), true);
    }
}
