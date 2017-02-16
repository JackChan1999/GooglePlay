package com.qq.googleplay.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public class ThreadPoolProxy {

    private ThreadPoolExecutor mExecutor;//只创建一次

    private int mCorePoolSize;//核心线程数

    private int mMaximumPoolSize;//最大线程数

    private long mKeepAliveTime;//保持时间

    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        super();
        mKeepAliveTime = keepAliveTime;
        mMaximumPoolSize = maximumPoolSize;
        mCorePoolSize = corePoolSize;
    }

    //双重检查加锁
    private ThreadPoolExecutor initThreadPoolExecutor() {
        if (mExecutor == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null) {
                    TimeUnit timeUnit = TimeUnit.MILLISECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();//无界队列
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
                    //丢弃任务并抛出RejectedExecutionException异常

                    mExecutor = new ThreadPoolExecutor(
                            mCorePoolSize, //核心线程数
                            mMaximumPoolSize, //最大的线程数
                            mKeepAliveTime,//保持时间
                            timeUnit, //保持时间对应的单位
                            workQueue,//缓存队列/阻塞队列
                            threadFactory,//线程工厂
                            handler//异常捕获器
                    );
                }
            }
        }

        return mExecutor;
    }

    /*执行任务*/
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }

    /* 提交任务*/
    public Future<?> submit(Runnable task) {
        initThreadPoolExecutor();
        return mExecutor.submit(task);
    }

    /*移除任务*/
    public void removeTask(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.remove(task);
    }

}
