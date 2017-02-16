package com.qq.googleplay.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Observable;
import android.os.StatFs;
import android.os.storage.StorageManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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
public class SDCardHelper extends BroadcastReceiver {
    private static SDCardHelper sInstance = null;
    private Context mContext;
    private boolean mIsMounted = true;
    private List<MountPoint> mMountPathList = Collections.synchronizedList(new ArrayList());
    private final SDCardStateObservable mStateObservable = new SDCardStateObservable();
    private Method sDescription;
    private Method sIsRemovable;
    private Method sPath;
    private Method sVolumeState;

    public class MountPoint {
        private String mDescription;
        private boolean mIsExternal;
        private String mMountedState;
        private String mPath;

        public String getDescription() {
            return this.mDescription;
        }

        private void setDescription(String str) {
            this.mDescription = str;
        }

        public String getPath() {
            return this.mPath;
        }

        private void setPath(String str) {
            this.mPath = str;
        }

        public String getMountedState() {
            return this.mMountedState;
        }

        private void setMountedState(String str) {
            this.mMountedState = str;
        }

        public boolean isMounted() {
            return this.mMountedState.equals("mounted");
        }

        public boolean isExternal() {
            return this.mIsExternal;
        }

        private void setExternal(boolean z) {
            this.mIsExternal = z;
        }

        @SuppressLint({"NewApi"})
        public long getTotalBlocks() {
            if (!isMounted()) {
                return 0;
            }
            StatFs statFs = new StatFs(this.mPath);
            return statFs.getBlockCountLong() * statFs.getBlockSizeLong();
        }

        @SuppressLint({"NewApi"})
        public long availableSpace() {
            if (!isMounted()) {
                return 0;
            }
            StatFs statFs = new StatFs(this.mPath);
            return statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
        }
    }

    static class SDCardStateObservable extends Observable<SDCardStateObserver> {
        private SDCardStateObservable() {
        }

        public void notifyStateChanged(Intent intent, boolean z) {
            synchronized (this.mObservers) {
                for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                    ((SDCardStateObserver) this.mObservers.get(size)).onChanged(intent, z);
                }
            }
        }
    }

    public interface SDCardStateObserver {
        void onChanged(Intent intent, boolean z);
    }

    public static void createInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SDCardHelper(context);
        }
    }

    public static SDCardHelper getInstance() {
        return sInstance;
    }

    private SDCardHelper(Context context) {
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_EJECT");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addDataScheme("file");
        this.mContext.registerReceiver(this, intentFilter);
        getMountPointList(context);
        this.mIsMounted = isSDCardMounted();
    }

    public void onDestory() {
        this.mContext.unregisterReceiver(this);
        sInstance = null;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        getMountPointList(context);
        if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
            this.mIsMounted = true;
            notifyStateChanged(intent, true);
        } else if ("android.intent.action.MEDIA_EJECT".equals(action)) {
            this.mIsMounted = false;
            notifyStateChanged(intent, false);
        } else if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
            this.mIsMounted = false;
            notifyStateChanged(intent, false);
        }
    }

    public boolean isMounted() {
        return this.mIsMounted;
    }

    public void registerStateObserver(SDCardStateObserver sDCardStateObserver) {
        this.mStateObservable.registerObserver(sDCardStateObserver);
    }

    public void unregisterStateObserver(SDCardStateObserver sDCardStateObserver) {
        this.mStateObservable.unregisterObserver(sDCardStateObserver);
    }

    public void notifyStateChanged(Intent intent, boolean z) {
        this.mStateObservable.notifyStateChanged(intent, z);
    }

    public boolean isSDCardMounted() {
        if (getSDCardMountPoint() == null) {
            return false;
        }
        return getSDCardMountPoint().isMounted();
    }

    public boolean isOtgMounted() {
        if (getOtgMountPoint() == null) {
            return false;
        }
        return getOtgMountPoint().isMounted();
    }

    public MountPoint getOtgMountPoint() {
        for (MountPoint mountPoint : this.mMountPathList) {
            if (mountPoint.mIsExternal && mountPoint.mPath.contains("otg")) {
                return mountPoint;
            }
        }
        return null;
    }

    public MountPoint getStorageMountPoint() {
        for (MountPoint mountPoint : this.mMountPathList) {
            if (!mountPoint.mIsExternal) {
                return mountPoint;
            }
        }
        return null;
    }

    public MountPoint getSDCardMountPoint() {
        for (MountPoint mountPoint : this.mMountPathList) {
            if (mountPoint.mIsExternal && mountPoint.mPath.contains("sdcard")) {
                return mountPoint;
            }
        }
        return null;
    }

    public List<MountPoint> getMountPointList(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        this.mMountPathList.clear();
        try {
            Object[] objArr = (Object[]) storageManager.getClass().getMethod("getVolumeList", new Class[0])
                    .invoke(storageManager, new Object[0]);
            if (objArr != null) {
                for (Object obj : objArr) {
                    MountPoint mountPoint = new MountPoint();
                    if (sDescription == null || sPath == null || this.sIsRemovable == null || this.sVolumeState == null) {
                        sDescription = obj.getClass().getDeclaredMethod("getDescription", new Class[]{Context.class});
                        sPath = obj.getClass().getDeclaredMethod("getPath", new Class[0]);
                        sIsRemovable = obj.getClass().getDeclaredMethod("isRemovable", new Class[0]);
                        sVolumeState = storageManager.getClass().getMethod("getVolumeState", new Class[]{String.class});
                    }
                    String str = (String) this.sPath.invoke(obj, new Object[0]);
                    mountPoint.setDescription((String) this.sDescription.invoke(obj, new Object[]{context}));
                    mountPoint.setPath(str);
                    mountPoint.setMountedState((String) this.sVolumeState.invoke(storageManager, new Object[]{str}));
                    mountPoint.setExternal(((Boolean) this.sIsRemovable.invoke(obj, new Object[0])).booleanValue());
                    this.mMountPathList.add(mountPoint);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }
        return this.mMountPathList;
    }
}
