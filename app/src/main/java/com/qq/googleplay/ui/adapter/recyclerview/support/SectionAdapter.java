package com.qq.googleplay.ui.adapter.recyclerview.support;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.qq.googleplay.ui.adapter.recyclerview.MultiItemCommonAdapter;
import com.qq.googleplay.ui.adapter.recyclerview.MultiItemTypeSupport;
import com.qq.googleplay.ui.holder.ViewHolder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public abstract class SectionAdapter<T> extends MultiItemCommonAdapter<T>
{
    private SectionSupport mSectionSupport;
    private static final int TYPE_SECTION = 0;
    private LinkedHashMap<String, Integer> mSections;

    private MultiItemTypeSupport<T> headerItemTypeSupport;

    @Override
    public int getItemViewType(int position)
    {
        return mMultiItemTypeSupport.getItemViewType(position, null);
    }

    final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            findSections();
        }
    };

    public SectionAdapter(Context context, int layoutId, List<T> datas, SectionSupport sectionSupport)
    {
        this(context, layoutId, null, datas, sectionSupport);
    }

    public SectionAdapter(Context context, MultiItemTypeSupport multiItemTypeSupport, List<T> datas, SectionSupport sectionSupport)
    {
        this(context, -1, multiItemTypeSupport, datas, sectionSupport);
    }

    public SectionAdapter(Context context, int layoutId, MultiItemTypeSupport multiItemTypeSupport, List<T> datas, SectionSupport sectionSupport)
    {
        super(context, datas, null);
        mLayoutId = layoutId;
        initMulitiItemTypeSupport(layoutId, multiItemTypeSupport);
        mMultiItemTypeSupport = headerItemTypeSupport;
        mSectionSupport = sectionSupport;
        mSections = new LinkedHashMap<>();
        findSections();
        registerAdapterDataObserver(observer);
    }

    private void initMulitiItemTypeSupport(int layoutId, final MultiItemTypeSupport multiItemTypeSupport)
    {
        if (layoutId != -1)
        {
            headerItemTypeSupport = new MultiItemTypeSupport<T>()
            {
                @Override
                public int getLayoutId(int itemType)
                {
                    if (itemType == TYPE_SECTION)
                        return mSectionSupport.sectionHeaderLayoutId();
                    else
                        return mLayoutId;
                }

                @Override
                public int getItemViewType(int position, T o)
                {
                    int positionVal = getIndexForPosition(position);
                    return mSections.values().contains(position) ?
                            TYPE_SECTION :
                            1;
                }
            };
        } else if (multiItemTypeSupport != null)
        {
            headerItemTypeSupport = new MultiItemTypeSupport<T>()
            {
                @Override
                public int getLayoutId(int itemType)
                {
                    if (itemType == TYPE_SECTION)
                        return mSectionSupport.sectionHeaderLayoutId();
                    else
                        return multiItemTypeSupport.getLayoutId(itemType);
                }

                @Override
                public int getItemViewType(int position, T o)
                {
                    int positionVal = getIndexForPosition(position);
                    return mSections.values().contains(position) ?
                            TYPE_SECTION :
                            multiItemTypeSupport.getItemViewType(positionVal, o);
                }
            };
        } else
        {
            throw new RuntimeException("layoutId or MultiItemTypeSupport must set one.");
        }

    }

    @Override
    protected boolean isEnabled(int viewType)
    {
        if (viewType == TYPE_SECTION)
            return false;
        return super.isEnabled(viewType);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView)
    {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(observer);
    }

    public void findSections()
    {
        int n = mDatas.size();
        int nSections = 0;
        mSections.clear();

        for (int i = 0; i < n; i++)
        {
            String sectionName = mSectionSupport.getTitle(mDatas.get(i));

            if (!mSections.containsKey(sectionName))
            {
                mSections.put(sectionName, i + nSections);
                nSections++;
            }
        }

    }


    @Override
    public int getItemCount()
    {
        return super.getItemCount() + mSections.size();
    }

    public int getIndexForPosition(int position)
    {
        int nSections = 0;

        Set<Map.Entry<String, Integer>> entrySet = mSections.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet)
        {
            if (entry.getValue() < position)
            {
                nSections++;
            }
        }
        return position - nSections;
    }

    @Override
    protected int getPosition(RecyclerView.ViewHolder viewHolder)
    {
        return getIndexForPosition(viewHolder.getAdapterPosition());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        position = getIndexForPosition(position);
        if (holder.getItemViewType() == TYPE_SECTION)
        {
            holder.setText(mSectionSupport.sectionTitleTextViewId(), mSectionSupport.getTitle(mDatas.get(position)));
            return;
        }
        super.onBindViewHolder(holder, position);
    }


}
