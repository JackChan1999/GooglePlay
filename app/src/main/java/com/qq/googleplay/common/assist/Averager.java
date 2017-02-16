package com.qq.googleplay.common.assist;

import com.qq.googleplay.android.log.Log;

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
 * des ：统计平均数
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public class Averager {
    private static final String            TAG     = "Averager";
    private              ArrayList<Number> numList = new ArrayList<Number>();

    /**
     * 添加一个数字
     *
     * @param num
     */
    public synchronized void add(Number num) {
        numList.add(num);
    }

    /**
     * 清除全部
     */
    public void clear() {
        numList.clear();
    }

    /**
     * 返回参与均值计算的数字个数
     *
     * @return
     */
    public Number size() {
        return numList.size();
    }

    /**
     * 获取平均数
     *
     * @return
     */
    public Number getAverage() {
        if (numList.size() == 0) {
            return 0;
        } else {
            Float sum = 0f;
            for (int i = 0, size = numList.size(); i < size; i++) {
                sum = sum.floatValue() + numList.get(i).floatValue();
            }
            return sum / numList.size();
        }
    }

    /**
     * 打印数字列
     *
     * @return
     */
    public String print() {
        String str = "PrintList(" + size() + "): " + numList;
        Log.i(TAG, str);
        return str;
    }

}
