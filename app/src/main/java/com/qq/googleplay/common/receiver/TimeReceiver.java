package com.qq.googleplay.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

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
/**
 * 时间广播
 *
 */
public class TimeReceiver extends BroadcastReceiver {

    private static final String TAG = "TimeReceiver";

    private TimeListener timeListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Log.isPrint) {
            Log.i(TAG, "action: " + intent.getAction());
            Log.d(TAG, "intent : ");
            Bundle bundle = intent.getExtras();
            for (String key : bundle.keySet()) {
                Log.d(TAG, key + " : " + bundle.get(key));
            }
        }
        if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            if (timeListener != null) {
                timeListener.onTimeTick();
            }
        } else if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {
            if (timeListener != null) {
                timeListener.onTimeChanged();
            }
        } else if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            if (timeListener != null) {
                timeListener.onTimeZoneChanged();
            }
        }
    }

    public void registerReceiver(Context context, TimeListener timeListener) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            filter.setPriority(Integer.MAX_VALUE);
            context.registerReceiver(this, filter);
            this.timeListener = timeListener;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterReceiver(Context context) {
        try {
            context.unregisterReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static interface TimeListener {
        /**
         * 时区改变
         */
        public void onTimeZoneChanged();

        /**
         * 设置时间
         */
        public void onTimeChanged();

        /**
         * 每分钟调用
         */
        public void onTimeTick();
    }
}
