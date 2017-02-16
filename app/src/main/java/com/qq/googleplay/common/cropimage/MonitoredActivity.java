/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qq.googleplay.common.cropimage;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
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
public class MonitoredActivity extends Activity {

    private final ArrayList<LifeCycleListener> mListeners =
            new ArrayList<LifeCycleListener>();

    public static interface LifeCycleListener {

        public void onActivityCreated(MonitoredActivity activity);

        public void onActivityDestroyed(MonitoredActivity activity);

        public void onActivityPaused(MonitoredActivity activity);

        public void onActivityResumed(MonitoredActivity activity);

        public void onActivityStarted(MonitoredActivity activity);

        public void onActivityStopped(MonitoredActivity activity);
    }

    public static class LifeCycleAdapter implements LifeCycleListener {

        public void onActivityCreated(MonitoredActivity activity) {

        }

        public void onActivityDestroyed(MonitoredActivity activity) {

        }

        public void onActivityPaused(MonitoredActivity activity) {

        }

        public void onActivityResumed(MonitoredActivity activity) {

        }

        public void onActivityStarted(MonitoredActivity activity) {

        }

        public void onActivityStopped(MonitoredActivity activity) {

        }
    }

    public void addLifeCycleListener(LifeCycleListener listener) {

        if (mListeners.contains(listener)) return;
        mListeners.add(listener);
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {

        mListeners.remove(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityCreated(this);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityDestroyed(this);
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityStarted(this);
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityStopped(this);
        }
    }
}
