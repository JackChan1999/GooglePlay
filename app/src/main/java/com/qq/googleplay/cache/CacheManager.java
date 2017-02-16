package com.qq.googleplay.cache;

import android.content.Context;

import com.qq.googleplay.common.io.IOUtils;
import com.qq.googleplay.net.HttpUtils;
import com.qq.googleplay.utils.CommonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class CacheManager {

    // wifi缓存时间为5分钟
    private static long wifi_cache_time = 5 * 60 * 1000;
    // 其他网络环境为1小时
    private static long other_cache_time = 60 * 60 * 1000;


    public static boolean saveObject(Serializable ser, String filename) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = CommonUtil.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(fos);
        }
    }

    public static Serializable readObject(String filename) {
        if (!isExistDataCache(filename))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = CommonUtil.getContext().openFileInput(filename);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = CommonUtil.getContext().getFileStreamPath(filename);
                data.delete();
            }
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }
        return null;
    }

    public static boolean isExistDataCache(String cachefile) {
        if (CommonUtil.getContext() == null)
            return false;
        boolean exist = false;
        File data = CommonUtil.getContext().getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 判断缓存是否已经失效
     */
    public static boolean isCacheDataFailure(String filename) {
        File data = CommonUtil.getContext().getFileStreamPath(filename);
        if (!data.exists()) {
            return false;
        }
        long existTime = System.currentTimeMillis() - data.lastModified();
        boolean failure = false;
        if (HttpUtils.getNetworkType() == "wifi") {
            failure = existTime > wifi_cache_time;
        } else {
            failure = existTime > other_cache_time;
        }
        return failure;
    }
}
