package com.qq.googleplay.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.qq.googleplay.android.log.Log;
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
public class ScreenReceiver extends BroadcastReceiver {
    private String TAG = "ScreenActionReceiver";
    private ScreenListener screenListener;

    public ScreenReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            Log.d(TAG, "屏幕解锁广播...");
            if (screenListener != null) {
                screenListener.screenOn();
            }
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d(TAG, "屏幕加锁广播...");
            if (screenListener != null) {
                screenListener.screenOff();
            }
        }
    }

    public void registerScreenReceiver(Context context, ScreenListener screenListener) {
        try {
            this.screenListener = screenListener;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            Log.d(TAG, "注册屏幕解锁、加锁广播接收者...");
            context.registerReceiver(this, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterScreenReceiver(Context context) {
        try {
            context.unregisterReceiver(this);
            Log.d(TAG, "注销屏幕解锁、加锁广播接收者...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static interface ScreenListener {
        public void screenOn();

        public void screenOff();
    }

}