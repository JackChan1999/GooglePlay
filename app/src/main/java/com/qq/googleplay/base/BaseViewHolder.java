package com.qq.googleplay.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.qq.googleplay.ui.holder.ViewHolder;

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

public abstract class BaseViewHolder<T> extends ViewHolder {
    protected T mData;

    public BaseViewHolder(Context context, View itemView, ViewGroup parent, int position) {
        super(context, itemView, parent, position);
    }

    public void setDataAndRefreshHolderView(T data) {
        mData = data;//保存数据
        refreshHolderView(data);//刷新显示
    }

    public abstract void refreshHolderView(T data);
}

