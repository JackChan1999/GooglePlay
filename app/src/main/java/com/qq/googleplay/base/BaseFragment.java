package com.qq.googleplay.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qq.googleplay.factory.RecyclerviewFactory;
import com.qq.googleplay.factory.ThreadPoolFactory;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.ui.recyclerview.EndlessRecyclerOnScrollListener;
import com.qq.googleplay.ui.recyclerview.RecyclerViewAdapter;
import com.qq.googleplay.ui.recyclerview.RecyclerViewStateUtils;
import com.qq.googleplay.ui.widget.LoadingFooter;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.ToastUtil;
import com.qq.googleplay.utils.ViewUtil;
import com.qq.googleplay.utils.bitmap.BitmapHelper;

import java.util.List;
import java.util.Map;

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
public abstract class BaseFragment<T> extends Fragment {

    protected Activity mActivity;
    private LoadingPager mLoadingPager;
    protected List<T> mData;
    public RecyclerView.RecycledViewPool mPool;

    public LoadingPager getLoadingPager() {
        return mLoadingPager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        //会出现重复添加的问题
        if (mLoadingPager == null) {
            mLoadingPager = new LoadingPager(mActivity) {
                @Override
                protected LoadedResult initData() {
                    return BaseFragment.this.initData();
                }

                @Override
                public View initSuccessView() {
                    return BaseFragment.this.initSuccessView();
                }
            };
        } else {
            ViewUtil.removeSelfFromParent(mLoadingPager);
           // ((ViewGroup) mLoadingPager.getParent()).removeView(mLoadingPager);

        }
        return mLoadingPager;
    }

    /**
     * @return
     * @desc 返回成功视图
     * @call 正在加载数据完成后，并且数据加载成功
     */
    protected abstract View initSuccessView();

    /**
     * @desc 真正加载数据
     * @call loadData()方法调用的时候
     */
    protected abstract LoadingPager.LoadedResult initData();

    /**
     * @param obj 网络数据json化之后的对象
     * @return
     */
    public LoadingPager.LoadedResult checkState(Object obj) {
        if (obj == null) {
            return LoadingPager.LoadedResult.EMPTY;
        }

        if (obj instanceof List) {
            if (((List) obj).size() == 0) {
                return LoadingPager.LoadedResult.EMPTY;
            }
        }

        if (obj instanceof Map) {
            if (((Map) obj).size() == 0) {
                return LoadingPager.LoadedResult.EMPTY;
            }
        }

        return LoadingPager.LoadedResult.SUCCESS;
    }

    public void toast(String content){
        ToastUtil.toast(content);
    }

    public void toast(Activity activity, String content, int duration){
        ToastUtil.toast(activity,content,duration);
    }

    protected EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            loadNextPage(view);
        }

        @Override
        public void onStateChange(int newState, boolean isDown) {
           handleRequest(newState, isDown);
        }
    };

    private void loadNextPage(View view){
        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState((RecyclerView) view);
        if(state == LoadingFooter.State.Loading) {
            Log.d("@Allen", "the state is Loading, just wait..");
            return;
        }

        if (hasLoadMore()) {
            // loading more
            RecyclerViewStateUtils.setFooterViewState(mActivity, (RecyclerView) view, 20,
                    LoadingFooter.State.Loading, null);
            loadMore((RecyclerView) view);
        } else {
            //the end
            RecyclerViewStateUtils.setFooterViewState(mActivity, (RecyclerView) view, 20,
                    LoadingFooter.State.TheEnd, null);
        }
    }

    private void handleRequest(int newState, boolean isDown) {
        switch (newState){
            case RecyclerView.SCROLL_STATE_IDLE:
                BitmapHelper.instance.manager.resumeRequests();
                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:
                BitmapHelper.instance.manager.resumeRequests();
                break;

            case RecyclerView.SCROLL_STATE_SETTLING:
                if (isDown){
                    BitmapHelper.instance.manager.pauseRequests();
                }
                break;
        }
    }

    /**加载下一页数据*/
    private void loadMore(final RecyclerView view) {
        ThreadPoolFactory.getNormalPool().execute(new Runnable() {
            @Override
            public void run() {
                List<T> loadMoreDatas = null;
                LoadingFooter.State state = LoadingFooter.State.Loading;
                try {
                    loadMoreDatas = onLoadMore();
                    if (loadMoreDatas == null) {
                        state = LoadingFooter.State.TheEnd;
                    } else {
                        if (loadMoreDatas.size() < RequestConstants.PAGESIZE) {
                            state = LoadingFooter.State.NetWorkError;
                        } else {
                            state = LoadingFooter.State.Normal;
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    state = LoadingFooter.State.NetWorkError;
                }

                final List<T> tempLoadMoreDatas = loadMoreDatas;
                final LoadingFooter.State tempstate = state;
                CommonUtil.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerViewStateUtils.setFooterViewState(mActivity, view, 20, tempstate, mFooterClick);
                        if (tempLoadMoreDatas != null) {
                            mData.addAll(tempLoadMoreDatas);
                            view.getAdapter().notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    /**点击重新加载*/
    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(mActivity, (RecyclerView) v.getParent(), 20,
                    LoadingFooter.State.Loading, null);
            loadMore((RecyclerView) v.getParent());
        }
    };

    protected RecyclerView setupRecyclerView(RecyclerView.Adapter adapter, int color, int[] padding){

        RecyclerView recyclerView = RecyclerviewFactory.createRecyclerView();
        if (padding != null && padding.length != 0){
            recyclerView.setPadding(padding[0],padding[1],padding[2],padding[3]);
        }

        if (color != 0){
            recyclerView.setBackgroundColor(color);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setRecycleChildrenOnDetach(true);
        recyclerView.setLayoutManager(layoutManager);
        if (mPool != null) {
            recyclerView.setRecycledViewPool(mPool);
        }
        //recyclerView.setItemViewCacheSize(10);

        RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(adapter);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.addOnScrollListener(mOnScrollListener);

        return recyclerView;
    }

    protected boolean hasLoadMore() {
        return true;
    }


    public List<T> onLoadMore() throws Throwable {
        return null;
    }
}
