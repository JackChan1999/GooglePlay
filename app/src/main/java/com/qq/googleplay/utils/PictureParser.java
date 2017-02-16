package com.qq.googleplay.utils;

import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.text.TextUtils;

import com.qq.googleplay.bean.Picture;
import com.qq.googleplay.utils.bitmap.BitmapUtils;

import java.io.File;
import java.io.IOException;
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
public class PictureParser {
    public static Picture parsePictureFromPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        int imageWidth = -1;
        int imageLength = -1;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            imageWidth = exifInterface.getAttributeInt("ImageWidth", -1);
            imageLength = exifInterface.getAttributeInt("ImageLength", imageLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Picture picture = new Picture();
        if (imageLength <= 0 || imageWidth <= 0) {
            Options options = BitmapUtils.getBitmapOptions(path);
            if (options == null) {
                return null;
            }
            picture.width = options.outWidth;
            picture.height = options.outHeight;
        } else {
            picture.width = imageWidth;
            picture.height = imageLength;
        }
        picture.originalSize = file.length();
        picture.path = path;
        return picture;
    }
}
