package com.qq.googleplay.common.assist;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;

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
 * <!-- 解锁 -->
 * require <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
 *
 */
public class KeyguardLock {
    KeyguardManager              keyguardManager;
    KeyguardManager.KeyguardLock keyguardLock;

    public KeyguardLock(Context context, String tag) {
        //获取系统服务
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        //初始化键盘锁，可以锁定或解开键盘锁
        keyguardLock = keyguardManager.newKeyguardLock(tag);
    }

    /**
     * Call requires API level 16
     */
    public boolean isKeyguardLocked() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Log.e("Log : ", "can not call isKeyguardLocked if SDK_INT < 16 ");
            return false;
        } else {
            return keyguardManager.isKeyguardLocked();
        }

    }

    /**
     * Call requires API level 16
     */
    public boolean isKeyguardSecure() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Log.e("Log : ", "can not call isKeyguardSecure if SDK_INT < 16 ");
            return false;
        } else {
            return keyguardManager.isKeyguardSecure();
        }
    }

    public boolean inKeyguardRestrictedInputMode() {
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

    public void disableKeyguard() {
        keyguardLock.disableKeyguard();
    }

    public void reenableKeyguard() {
        keyguardLock.reenableKeyguard();
    }

    public void release() {
        if (keyguardLock != null) {
            //禁用显示键盘锁定
            keyguardLock.reenableKeyguard();
        }
    }

    public KeyguardManager getKeyguardManager() {
        return keyguardManager;
    }

    public void setKeyguardManager(KeyguardManager keyguardManager) {
        this.keyguardManager = keyguardManager;
    }

    public KeyguardManager.KeyguardLock getKeyguardLock() {
        return keyguardLock;
    }

    public void setKeyguardLock(KeyguardManager.KeyguardLock keyguardLock) {
        this.keyguardLock = keyguardLock;
    }
}
