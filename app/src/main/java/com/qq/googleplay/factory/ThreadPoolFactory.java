package com.qq.googleplay.factory;

import com.qq.googleplay.manager.ThreadPoolProxy;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/24 22:42
 * 描 述 ： 线程池工厂
 * 修订历史 ：
 * ============================================================
 **/
public class ThreadPoolFactory {

    private static ThreadPoolProxy mNormalPool;
    private static ThreadPoolProxy mDownLoadPool;

    /**得到一个普通的线程池*/
    public static ThreadPoolProxy getNormalPool() {

        if (mNormalPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mNormalPool == null) {
                    mNormalPool = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mNormalPool;
    }

    /**
     * @return
     * @des 得到一个下载的线程池
     */
    public static ThreadPoolProxy getDownLoadPool() {
        if (mDownLoadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mDownLoadPool == null) {
                    mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
                }
            }
        }
        return mDownLoadPool;
    }

}
