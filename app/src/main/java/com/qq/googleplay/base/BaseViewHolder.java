package com.qq.googleplay.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.qq.googleplay.ui.holder.ViewHolder;

/**
 * ============================================================
 * Copyright：${TODO}有限公司版权所有 (c) 2016
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * <p/>
 * Project_Name：GooglePlay
 * Package_Name：com.qq.googleplay.base
 * Version：1.0
 * time：2016/10/19 15:02
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

