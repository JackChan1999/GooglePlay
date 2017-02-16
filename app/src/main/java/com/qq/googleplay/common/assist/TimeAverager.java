package com.qq.googleplay.common.assist;
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
 * 时间均值计算器,只能用于单线程计时。
 *
 * @author MaTianyu
 *         2013-12-12上午1:23:19
 */
public class TimeAverager {
    /**
     * 计时器
     */
    private TimeCounter tc = new TimeCounter();
    /**
     * 均值器
     */
    private Averager av = new Averager();

    /**
     * 一个计时开始
     */
    public long start() {
        return tc.start();
    }

    /**
     * 一个计时结束
     */
    public long end() {
        long time = tc.duration();
        av.add(time);
        return time;
    }

    /**
     * 一个计时结束,并且启动下次计时。
     */
    public long endAndRestart() {
        long time = tc.durationRestart();
        av.add(time);
        return time;
    }

    /**
     * 求全部计时均值
     */
    public Number average() {
        return av.getAverage();
    }

    /**
     * 打印全部时间值
     */
    public void print() {
        av.print();
    }

    /**
     * 清楚数据
     */
    public void clear() {
        av.clear();
    }
}
