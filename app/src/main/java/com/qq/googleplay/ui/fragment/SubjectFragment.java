package com.qq.googleplay.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.base.LoadingPager;
import com.qq.googleplay.base.SuperBaseAdapter;
import com.qq.googleplay.bean.SubjectInfoBean;
import com.qq.googleplay.factory.ListViewFactory;
import com.qq.googleplay.net.Protocol.SubjectProtocol;
import com.qq.googleplay.ui.animation.listviewanim.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.qq.googleplay.ui.holder.SubjectHolder;
import com.qq.googleplay.utils.bitmap.BitmapHelper;

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
public class SubjectFragment extends BaseFragment<SubjectInfoBean>{

    private SubjectProtocol mSubjectProtocol;
    private SubjectAdapter mSubjectAdapter;
    private SubjectHolder mHolder;
    //private RecyclerView mRecyclerView;
    //private final int viewType = Integer.MAX_VALUE / 2 + 3;

    @Override
    protected View initSuccessView() {

       /* mSubjectAdapter = new SubjectAdapter(mActivity,R.layout.item_subject,mData);
        //mAdapter = new RecyclerViewAdapter(AdapterUtils.getSwingBottomInAdapter(mSubjectAdapter,mRecyclerView));
        mRecyclerView = setupRecyclerView(mSubjectAdapter, Color.parseColor("#eaeaea"), null);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(viewType,10);*/

        ListView listView = ListViewFactory.createListView(mActivity);
        listView.setBackgroundColor(Color.parseColor("#eaeaea"));
        //listView.setOnScrollListener(new LvOnScrollListener());

        mSubjectAdapter = new SubjectAdapter(listView, mData);
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mSubjectAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);

        return listView;
    }

    @Override
    protected LoadingPager.LoadedResult initData() {
        mSubjectProtocol = new SubjectProtocol();
        try {
            mData = mSubjectProtocol.loadData(0);
            return checkState(mData);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    private class SubjectAdapter extends SuperBaseAdapter<SubjectInfoBean> {
        public SubjectAdapter(AbsListView listView, List<SubjectInfoBean> dataSource) {
            super(listView, dataSource);
        }

        @Override
        public BaseHolder<SubjectInfoBean> getHolder(int position) {
            mHolder = new SubjectHolder(mActivity);
            return mHolder;
        }

        @Override
        public List<SubjectInfoBean> onLoadMore() throws Throwable {
            return mSubjectProtocol.loadData(mData.size());
        }
    }

    private class LvOnScrollListener implements AbsListView.OnScrollListener {

        private int startIndex;
        private int endIndex;
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            handleRequest(scrollState, startIndex, endIndex);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            startIndex = firstVisibleItem;
            endIndex = firstVisibleItem + visibleItemCount;
            if (endIndex >= totalItemCount){
                endIndex = totalItemCount - 1;
            }
        }
    }

    private void handleRequest(int scrollState, int startIndex, int endIndex) {
        switch (scrollState){
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                BitmapHelper.instance.manager.resumeRequests();
                //loadImage(startIndex,endIndex);
                break;

            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                BitmapHelper.instance.manager.resumeRequests();
                break;

            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                BitmapHelper.instance.manager.pauseRequests();
                break;
        }

    }

    public void loadImage(int startIndex, int endIndex){
        for (; startIndex < endIndex; startIndex++) {
            SubjectInfoBean data = (SubjectInfoBean) mSubjectAdapter.getItem(startIndex);
            if (data.url != null) {
                BitmapHelper.instance.display(mHolder.mIvIcon,data.url);
            }
        }
    }

    /*private class SubjectAdapter extends CommonAdapter<SubjectInfoBean> {

        public SubjectAdapter(Context context, int layoutId, List<SubjectInfoBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, SubjectInfoBean data) {
            holder.setText(R.id.item_subject_tv_title, data.des);
            BitmapHelper.instance.init(mActivity).display((ImageView) holder.getView(R.id.item_subject_iv_icon),
                    RequestConstants.URLS.IMAGEBASEURL+data.url);
        }

        @Override
        public int getItemViewType(int position) {
            return 3;
        }
    }

    @Override
    public List<SubjectInfoBean> onLoadMore() throws Throwable {
        return mSubjectProtocol.loadData(mData.size());
    }*/
}
