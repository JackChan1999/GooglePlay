package com.qq.googleplay.common.utils;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
public class AlarmUtil {
    /**
     * 开启定时器
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void startAlarmIntent(Context context, int triggerAtMillis, PendingIntent pendingIntent) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP,triggerAtMillis, pendingIntent);
    }

    /**
     * 关闭定时器
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void stopAlarmIntent(Context context, PendingIntent pendingIntent) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    /**
     * 开启轮询服务
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void startAlarmService(Context context, int triggerAtMillis, Class<?> cls, String action) {
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        startAlarmIntent(context, triggerAtMillis,pendingIntent);
    }

    /**
     * 停止启轮询服务
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void stopAlarmService(Context context, Class<?> cls, String action) {
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        stopAlarmIntent(context, pendingIntent);
    }

}
