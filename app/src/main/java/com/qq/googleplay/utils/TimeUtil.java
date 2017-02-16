package com.qq.googleplay.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
public class TimeUtil {
    public static int getDay() {
        long ts = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth() {
        long ts = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getYear() {
        long ts = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts);
        return calendar.get(Calendar.YEAR);
    }

    public static int getYesterdayDay() {
        long ts = System.currentTimeMillis() - 86400000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getYesterdayMonth() {
        long ts = System.currentTimeMillis() - 86400000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getYesterdayYear() {
        long ts = System.currentTimeMillis() - 86400000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts);
        return calendar.get(Calendar.YEAR);
    }

    public static String parseTime(int ms) {
        StringBuilder sb = new StringBuilder();
        int hour = (ms / 1000) / 3600;
        if (hour < 10) {
            sb.append("0");
        }
        sb.append(hour);
        sb.append(":");
        int minute = ((ms / 1000) % 3600) / 60;
        if (minute < 10) {
            sb.append("0");
        }
        sb.append(minute);
        sb.append(":");
        int second = ((ms / 1000) % 3600) % 60;
        if (second < 10) {
            sb.append("0");
        }
        sb.append(second);
        return sb.toString();
    }

    public static String parseShortTime(int ms) {
        if (ms == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int hour = (ms / 1000) / 3600;
        if (hour > 0) {
            if (hour < 10) {
                sb.append("0");
            }
            sb.append(hour);
            sb.append(":");
        }
        int minute = ((ms / 1000) % 3600) / 60;
        if (minute < 10) {
            sb.append("0");
        }
        sb.append(minute);
        sb.append(":");
        int second = ((ms / 1000) % 3600) % 60;
        if (second < 10) {
            sb.append("0");
        }
        sb.append(second);
        return sb.toString();
    }

    public static String msTime2Date(long msTime) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE)
                .format(new Date(msTime));
    }

    public static boolean isToday(String dateStr) {
        String str = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE)
                .format(new Date(System.currentTimeMillis()));
        if (StringUtil.isEmpty(dateStr) || !dateStr.equals(str)) {
            return false;
        }
        return true;
    }

    public static boolean isYesterday(String dateStr) {
        String str = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE)
                .format(new Date(System.currentTimeMillis() - 86400000));
        if (StringUtil.isEmpty(dateStr) || !dateStr.equals(str)) {
            return false;
        }
        return true;
    }
}
