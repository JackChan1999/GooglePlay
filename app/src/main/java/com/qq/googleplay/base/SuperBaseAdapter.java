package com.qq.googleplay.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.qq.googleplay.factory.ThreadPoolFactory;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.ui.holder.LoadMoreHolder;
import com.qq.googleplay.ui.widget.LoadingFooter.State;
import com.qq.googleplay.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

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
public abstract class SuperBaseAdapter<T> extends BaseAdapter implements AbsListView
        .RecyclerListener, AdapterView.OnItemClickListener {

    public static final int VIEWTYPE_LOADMORE = 0;
    public static final int VIEWTYPE_NORMAL = 1;

    private LoadMoreHolder mLoadMoreHolder;
    private LoadMoreTask mLoadMoreTask;

    private List<T> mDataSource = new ArrayList<>();

    private List<BaseHolder> mDisplayedHolders; //用于记录所有显示的holder

    protected AbsListView mListView;//和该adapter关联的listView

    public SuperBaseAdapter(AbsListView listView, List<T> dataSource) {
        mListView = listView;
        mDisplayedHolders = new ArrayList<>();
        if (listView != null) {
            listView.setOnItemClickListener(this);
            listView.setRecyclerListener(this);
        }
        mDataSource = dataSource;
    }

    /**
     * @param view
     * @des listview回收
     */
    @Override
    public void onMovedToScrapHeap(View view) {
        if (view != null) {
            Object tag = view.getTag();
            if (tag instanceof BaseHolder) {
                BaseHolder holder = (BaseHolder) tag;
                synchronized (mDisplayedHolders) {
                    mDisplayedHolders.remove(holder);
                }
                holder.recycle();
            }
        }
    }

    public List<BaseHolder> getDisplayedHolders() {
        synchronized (mDisplayedHolders) {
            return new ArrayList<BaseHolder>(mDisplayedHolders);
        }
    }

    @Override
    public int getCount() {
        if (mDataSource != null) {
            return mDataSource.size() + 1;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDataSource != null) {
            mDataSource.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //初始化布局
        BaseHolder<T> holder = null;
        if (convertView == null) {
            if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
                holder = (BaseHolder<T>) getLoadMoreHolder(parent.getContext());
            } else {
                holder = getHolder(position);
            }
        } else {
            holder = (BaseHolder<T>) convertView.getTag();
        }

        //数据展示

        if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
            if (hasLoadMore()) {
                perFormLoadMore();
            } else {
                mLoadMoreHolder.setDataAndRefreshHolderView(State.Normal);
            }
        } else {
            holder.setDataAndRefreshHolderView(mDataSource.get(position));
        }

        mDisplayedHolders.add(holder);
        return holder.getHolderView();
    }

    public void perFormLoadMore() {
        if (mLoadMoreTask == null) {
            State state = State.Loading;
            mLoadMoreHolder.setDataAndRefreshHolderView(state);
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolFactory.getNormalPool().execute(mLoadMoreTask);
        }

    }

    /**
     * @return
     * @des 决定有没有加载更多, 默认是返回true, 但是子类可以覆写此方法, 如果子类返回的是flase, 就不会去加载更多
     * @call getView中滑动底的时候会调用
     */
    protected boolean hasLoadMore() {
        return true;
    }

    /**
     * @return
     * @des 返回具体的baseholder的子类
     * @call getview方法中的convertview为空时创建
     */
    public abstract BaseHolder<T> getHolder(int position);

    private LoadMoreHolder getLoadMoreHolder(Context context) {
        if (mLoadMoreHolder == null) {
            mLoadMoreHolder = new LoadMoreHolder(context);
        }
        return mLoadMoreHolder;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            return VIEWTYPE_LOADMORE;//滑到底部，加载更多
        }
        return getNormalViewType(position);
    }

    /**
     * @des 返回普通view的一个viewType
     * @call 子类其实可以复写该方法.添加更多的viewType
     */
    public int getNormalViewType(int position) {
        return VIEWTYPE_NORMAL;
    }


    class LoadMoreTask implements Runnable {

        @Override
        public void run() {

            //请求网络加载更多数据
            List<T> loadMoreDatas = null;
            State state = State.Loading;
            try {
                loadMoreDatas = onLoadMore();
                //处理返回的结果
                if (loadMoreDatas == null || loadMoreDatas.size() == 0) {
                    state = State.TheEnd;
                } else {
                    if (loadMoreDatas.size() < RequestConstants.PAGESIZE) {
                        state = State.TheEnd;
                    } else {
                        state = State.Loading;
                    }
                }

            } catch (Throwable throwable) {
                throwable.printStackTrace();
                state = State.NetWorkError;
            }

            final State tempsate = state; //定义一个中转的临时变量
            final List<T> tempLoadMoreDatas = loadMoreDatas;

            CommonUtil.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    //刷新loadmore视图
                    mLoadMoreHolder.setDataAndRefreshHolderView(tempsate);
                    //刷新listview视图
                    if (tempLoadMoreDatas != null && tempLoadMoreDatas.size() != 0) {
                        mDataSource.addAll(tempLoadMoreDatas);
                        notifyDataSetChanged();
                    }
                }
            });
            mLoadMoreTask = null;
        }
    }

    /**
     * @des 可有可无的方法, 定义成一个public方法, 如果子类有加载更多.再去覆写就行了
     * @des 真正开始加载更多数据的地方
     * @call 滑到底的时候
     */
    public List<T> onLoadMore() throws Throwable {
        return null;
    }


    //处理item的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListView instanceof ListView) {
            position = position - ((ListView) mListView).getHeaderViewsCount();
        }

        if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
            perFormLoadMore();
        } else {
            onNormalItemClick(parent, view, position, id);
        }
    }

    /**
     * @des 点击普通条目对应的事件处理
     * @call 如果子类需要处理item的点击事件, 就直接覆写此方法
     */
    public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
