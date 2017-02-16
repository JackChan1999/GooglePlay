package com.qq.googleplay.utils;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
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
public abstract class AutoSwitchHandler extends Handler {
    private static final int DEFAULT_AUTO_SWITCH_DELAY_TIME = 5000;
    private static final int SWITCH = 1;
    public static final AutoSwitchObservable autoScrollObservable = new AutoSwitchObservable();
    private final int mAutoSwitchDelay;
    private boolean mIsEnabled;

    public static class AutoSwitchObservable {
        private List<AutoSwitchHandler> list = new ArrayList();

        void addHandler(AutoSwitchHandler handler) {
            this.list.add(handler);
        }

        void deleteHandler(AutoSwitchHandler handler) {
            this.list.remove(handler);
        }

        public void stopAll() {
            for (AutoSwitchHandler handler : this.list) {
                handler.onPause();
            }
        }

        public void clearAll() {
            stopAll();
            this.list.clear();
        }
    }

    public abstract void next();

    public AutoSwitchHandler() {
        this(5000);
    }

    public AutoSwitchHandler(int delay) {
        this.mAutoSwitchDelay = delay;
        autoScrollObservable.addHandler(this);
        this.mIsEnabled = true;
    }

    public void handleMessage(Message msg) {
        if (msg.what == 1 && this.mIsEnabled) {
            next();
            onResume();
        }
    }

    public void onResume() {
        removeCallbacksAndMessages(null);
        this.mIsEnabled = true;
        sendEmptyMessageDelayed(1, (long) this.mAutoSwitchDelay);
    }

    public void onPause() {
        removeCallbacksAndMessages(null);
        this.mIsEnabled = false;
    }

    public void onDestory() {
        onPause();
        autoScrollObservable.deleteHandler(this);
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }
}
