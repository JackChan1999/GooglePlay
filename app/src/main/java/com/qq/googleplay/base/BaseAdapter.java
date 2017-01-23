package com.qq.googleplay.base;

import android.content.Context;
import android.view.ViewGroup;

import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.manager.DownloadManager;
import com.qq.googleplay.ui.adapter.recyclerview.CommonAdapter;
import com.qq.googleplay.ui.holder.AppItemHolder;
import com.qq.googleplay.ui.holder.ViewHolder;

import java.util.LinkedList;
import java.util.List;

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
 * time：2016/10/19 15:18
 * des ：${TODO}
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public class BaseAdapter extends CommonAdapter<AppInfoBean>{
    private List<AppItemHolder>	mAppItemHolders	= new LinkedList<AppItemHolder>();

    public List<AppItemHolder> getAppItemHolders() {
        return mAppItemHolders;
    }

    public BaseAdapter(Context context, int layoutId, List<AppInfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppItemHolder appItemHolder = AppItemHolder.get(mContext, null, parent, mLayoutId, -1);
        mAppItemHolders.add(appItemHolder);
        // 初始化的时候把AppItemHolder加到观察者集合里面去
        DownloadManager.getInstance().addObserver(appItemHolder);
        setListener(parent, appItemHolder, viewType);
        return appItemHolder;
    }

    @Override
    public void convert(ViewHolder holder, AppInfoBean appinfo) {
        ((AppItemHolder)holder).setDataAndRefreshHolderView(appinfo);
    }

}
