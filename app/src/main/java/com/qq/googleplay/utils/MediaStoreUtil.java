package com.qq.googleplay.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.qq.googleplay.common.io.IOUtils;
import com.qq.googleplay.utils.bitmap.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
public class MediaStoreUtil {
    static final String TAG = MediaStoreUtil.class.getSimpleName();

    public static String getRealPathFromUri(Context context, Uri uri) {
        if (uri != null) {
            Log.d(TAG, uri.toString());
        }
        String realPath = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow("_data");
                cursor.moveToFirst();
                realPath = cursor.getString(column_index);
            }
            Log.d(TAG + "real path", realPath);
            return realPath;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri, Options options) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Throwable th) {
           th.printStackTrace();
        }finally {
            IOUtils.closeQuietly(is);
        }
        return bitmap;
    }

    public static String getLatestPath(Context context) {
        String imageLocation = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id",
                    "_data", "bucket_display_name", "datetaken", "mime_type"}, null, null, "_id DESC");
            if (cursor != null && cursor.moveToFirst()) {
                imageLocation = cursor.getString(cursor.getColumnIndex("_data"));
            }
            if (cursor != null) {
                cursor.close();
            }
            return imageLocation;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            return imageLocation;
        }
    }

    public static Uri getCompressedUri(Context context, String imagePath) {
        if (context == null || imagePath == null) {
            return null;
        }
        Bitmap bmp = BitmapFactory.decodeFile(imagePath);
        try {
            String fileName = context.getExternalCacheDir().toString()
                    + File.separatorChar + System.currentTimeMillis() + ".png";
            BitmapUtils.saveBitmap(fileName,bmp);
            return Uri.fromFile(new File(fileName));
        } catch (Exception e) {
            return null;
        }
    }
}
