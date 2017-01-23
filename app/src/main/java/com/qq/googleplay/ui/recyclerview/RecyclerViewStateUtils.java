package com.qq.googleplay.ui.recyclerview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qq.googleplay.ui.widget.LoadingFooter;

/**
 * 分页展示数据时，RecyclerView的FooterView State 操作工具类
 *
 * RecyclerView一共有几种State：Normal/Loading/Error/TheEnd
 */
public class RecyclerViewStateUtils {

    /**
     * 设置headerAndFooterAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param pageSize      分页展示时，recyclerView每一页的数量
     * @param state         FooterView State
     * @param errorListener FooterView处于Error状态时的点击事件
     */
    public static void setFooterViewState(Activity instance, RecyclerView recyclerView, int pageSize, LoadingFooter.State state, View.OnClickListener errorListener) {

        if(instance==null || instance.isFinishing()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof RecyclerViewAdapter)) {
            return;
        }

        RecyclerViewAdapter rvAdapter = (RecyclerViewAdapter) outerAdapter;

        //只有一页的时候，就别加什么FooterView了
        if (rvAdapter.getInnerAdapter().getItemCount() < pageSize) {
            return;
        }

        LoadingFooter footerView;

        //已经有footerView了
        if (rvAdapter.getFooterViewsCount() > 0) {
            footerView = (LoadingFooter) rvAdapter.getFooterView();
            footerView.setState(state);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }
            recyclerView.scrollToPosition(rvAdapter.getItemCount() - 1);
        } else {
            footerView = new LoadingFooter(instance);
            footerView.setState(state);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }

            rvAdapter.addFooterView(footerView);
            recyclerView.scrollToPosition(rvAdapter.getItemCount() - 1);
        }
    }

    /**
     * 获取当前RecyclerView.FooterView的状态
     *
     * @param recyclerView
     */
    public static LoadingFooter.State getFooterViewState(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof RecyclerViewAdapter) {
            if (((RecyclerViewAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((RecyclerViewAdapter) outerAdapter).getFooterView();
                return footerView.getState();
            }
        }

        return LoadingFooter.State.Normal;
    }

    /**
     * 设置当前RecyclerView.FooterView的状态
     *
     * @param recyclerView
     * @param state
     */
    public static void setFooterViewState(RecyclerView recyclerView, LoadingFooter.State state) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof RecyclerViewAdapter) {
            if (((RecyclerViewAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((RecyclerViewAdapter) outerAdapter).getFooterView();
                footerView.setState(state);
            }
        }
    }
}
