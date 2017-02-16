package com.qq.googleplay.ui.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

public class ExStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private final String TAG = getClass().getSimpleName();

    GridLayoutManager.SpanSizeLookup mSpanSizeLookup;

    public ExStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    /**
     * Returns the current used by the GridLayoutManager.
     *
     * @return The current used by the GridLayoutManager.
     */
    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return mSpanSizeLookup;
    }

    /**
     * 设置某个位置的item的跨列程度，这里和GridLayoutManager有点不一样，
     * 如果你设置某个位置的item的span>1了，那么这个item会占据所有列
     *
     * @param spanSizeLookup instance to be used to query number of spans
     *                       occupied by each item
     */
    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        //Log.d(TAG, "item count = " + getItemCount());
        for (int i = 0; i < getItemCount(); i++) {

            if (mSpanSizeLookup.getSpanSize(i) > 1) {
                //Log.d(TAG, "lookup > 1 = " + i);
                try {
                    //fix 动态添加时报IndexOutOfBoundsException
                    View view = recycler.getViewForPosition(i);
                    if (view != null) {
                        /**
                         *占用所有的列
                         * @see https://plus.google.com/+EtienneLawlor/posts/c5T7fu9ujqi
                         */
                        LayoutParams lp = (LayoutParams) view.getLayoutParams();
                        lp.setFullSpan(true);
                    }
                    // recycler.recycleView(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    }
}