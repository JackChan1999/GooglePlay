package com.qq.googleplay.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

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
public abstract class BaseHolder<T> {

    public View mHolderView;
    protected LayoutInflater mInflater;
    private T mData;
    protected Context mContext;

    public BaseHolder(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mHolderView = initHolderView();//初始化根布局
        mHolderView.setTag(this);
    }

    public View getHolderView() {
        return mHolderView;
    }

    public void setDataAndRefreshHolderView(T data) {
        mData = data;//保存数据
        refreshHolderView(data);//刷新显示
    }

    /**
     * @return
     * @des 初始化holderview根视图
     * @call baseholder初始化时调用
     */
    public abstract View initHolderView();


    /**
     * @param data
     * @des 刷新holder视图
     * @call setDataAndRefreshHolderView()方法被调用的时候就被调用
     */
    public abstract void refreshHolderView(T data);

    /**
     * @des 用于回收
     */
    public void recycle() {

    }
}
