package com.qq.googleplay.net.okhttp.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
public class Platform
{
    private static final Platform PLATFORM = findPlatform();

    public static Platform get()
    {
       L.e(PLATFORM.getClass().toString());
        return PLATFORM;
    }

    private static Platform findPlatform()
    {
        try
        {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0)
            {
                return new Android();
            }
        } catch (ClassNotFoundException ignored)
        {
        }
        return new Platform();
    }

    public Executor defaultCallbackExecutor()
    {
        return Executors.newCachedThreadPool();
    }

    public void execute(Runnable runnable)
    {
        defaultCallbackExecutor().execute(runnable);
    }


    static class Android extends Platform
    {
        @Override
        public Executor defaultCallbackExecutor()
        {
            return new MainThreadExecutor();
        }

        static class MainThreadExecutor implements Executor
        {
            private final Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(Runnable r)
            {
                handler.post(r);
            }
        }
    }


}
