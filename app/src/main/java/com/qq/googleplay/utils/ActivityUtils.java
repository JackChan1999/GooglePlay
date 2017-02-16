package com.qq.googleplay.utils;

import android.app.Activity;
import android.text.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;
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
public class ActivityUtils {
    private static ActivityUtils instance = null;
    private Map<String, Activity> mActivityMap = new LinkedHashMap();

    private ActivityUtils() {
    }

    public static ActivityUtils getInstance() {
        if (instance == null) {
            instance = new ActivityUtils();
        }
        return instance;
    }

    public void addActivity(String name, Activity activity) {
        if (!TextUtils.isEmpty(name) && activity != null) {
            removeActivity(name, true);
            this.mActivityMap.put(name, activity);
        }
    }

    public void removeActivity(String name, boolean finish) {
        if (!TextUtils.isEmpty(name) && this.mActivityMap.containsKey(name)) {
            if (finish) {
                Activity activity = (Activity) this.mActivityMap.get(name);
                if (!(activity == null || activity.isFinishing())) {
                    activity.finish();
                }
            }
            this.mActivityMap.remove(name);
        }
    }

    public void removeAll() {
        if (!this.mActivityMap.isEmpty()) {
            for (String key : this.mActivityMap.keySet()) {
                Activity activity = (Activity) this.mActivityMap.get(key);
                if (!(activity == null || activity.isFinishing())) {
                    activity.finish();
                }
            }
        }
        this.mActivityMap.clear();
    }
}
