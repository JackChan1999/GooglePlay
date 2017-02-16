package com.qq.googleplay.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.googleplay.R;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
public class BaseApplication extends Application {

    private static BaseApplication mApplication;
    private static Context         mContext;
    private static Handler         mHandler;
    private static long            mMainTreadId;

    {
        PlatformConfig.setWeixin("wx91accd9v54410795", "dbb502q500592b11cd6446239ae10ded");
        PlatformConfig.setSinaWeibo("1065325295", "e3a7a0b12fa7aa0bdc3265b029ece142");
        PlatformConfig.setQQZone("100909225", "cbfe6bbc344d684a24bb68da757e97b4");

    }

    @Override
    public void onCreate() {
        super.onCreate();

       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/

        mApplication = this;
        mHandler = new Handler();
        mMainTreadId = android.os.Process.myTid();
        mContext = getApplicationContext();

        ToastMabager.builder.init(mContext);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        /*OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(client);*/

        //FileDownloader.init(getApplicationContext());

        UMShareAPI.get(this);//初始化sdk
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
    }

    public static Context getApplication() {
        return mApplication;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static long getMainTreadId() {
        return mMainTreadId;
    }

    public enum ToastMabager {
        builder;

        private View     view;
        private TextView tv;
        private Toast    toast;

        /**
         * 初始化toast
         */
        public void init(Context context) {
            view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            tv = (TextView) view.findViewById(R.id.tv_toast);
            toast = new Toast(context);
            toast.setView(view);
        }

        /**
         * 显示toast
         * @param content  显示的内容
         * @param duration 持续时间
         */
        public void display(CharSequence content, int duration) {
            if (content.length() != 0) {
                tv.setText(content);
                toast.setDuration(duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    class ExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            try {
                PrintWriter printWriter = new PrintWriter(
                        Environment.getExternalStorageDirectory() + "/googleplay.log");
                ex.printStackTrace(printWriter);
                printWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Process.killProcess(Process.myPid());
        }
    }

}

