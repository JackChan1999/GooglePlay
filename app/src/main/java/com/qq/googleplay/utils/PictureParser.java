package com.qq.googleplay.utils;

import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.text.TextUtils;

import com.qq.googleplay.bean.Picture;
import com.qq.googleplay.utils.bitmap.BitmapUtils;

import java.io.File;
import java.io.IOException;

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
