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
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/24 22:06
 * 描 述 ：创建线程池,执行任务,提交任务
 * 修订历史 ：
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
