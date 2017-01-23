package com.qq.googleplay.ui.adapter.abslistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.qq.googleplay.ui.holder.ViewHolder;

import java.util.List;
/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/20 17:01
 * 描 述 ：ListView通用adapter，支持多个itemtype
 * 修订历史 ：
 * ============================================================
 **/

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T>
{

    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public MultiItemCommonAdapter(Context context, List<T> datas,
                                  MultiItemTypeSupport<T> multiItemTypeSupport)
    {
        super(context, -1, datas);
        mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getViewTypeCount()
    {
        if (mMultiItemTypeSupport != null)
            return mMultiItemTypeSupport.getViewTypeCount();
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (mMultiItemTypeSupport != null)
            return mMultiItemTypeSupport.getItemViewType(position,
                    mDatas.get(position));
        return super.getItemViewType(position);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (mMultiItemTypeSupport == null)
            return super.getView(position, convertView, parent);

        int layoutId = mMultiItemTypeSupport.getLayoutId(position,
                getItem(position));
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

}
