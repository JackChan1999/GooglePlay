package com.qq.googleplay.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseAdapter;
import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.base.LoadingPager;
import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.manager.DownloadManager;
import com.qq.googleplay.net.HttpUtils;
import com.qq.googleplay.net.Protocol.AppProtocol;
import com.qq.googleplay.ui.activity.DetailActivity;
import com.qq.googleplay.ui.adapter.recyclerview.OnItemClickListener;
import com.qq.googleplay.ui.holder.AppItemHolder;
import com.qq.googleplay.utils.CommonUtil;

import java.util.List;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/20 19:38
 * 描 述 ：应用页面
 * 修订历史 ：
 * ============================================================
 **/
public class AppFragment extends BaseFragment<AppInfoBean> {

    private AppProtocol  mAppProtocol;
    private AppAdapter   mAppAdapter;
    private RecyclerView mRecyclerView;
    private final int viewType = Integer.MAX_VALUE / 2 + 1;

    @Override
    protected View initSuccessView() {
        mAppAdapter = new AppAdapter(mActivity, R.layout.item_applist, mData);
        //mAdapter = new RecyclerViewAdapter(AdapterUtils.getAlphaAdapter(mAppAdapter,mRecyclerView));
        int padding[] = new int[]{0, CommonUtil.dip2Px(4), 0, 0};
        mRecyclerView = setupRecyclerView(mAppAdapter, Color.parseColor("#EFF4F7"), padding);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(viewType, 10);
        initListener();
        return mRecyclerView;
    }

    @Override
    protected LoadingPager.LoadedResult initData() {

        if (HttpUtils.isNetworkAvailable(mActivity)) {
            mAppProtocol = new AppProtocol();
            try {
                mData = mAppProtocol.loadData(0);
                return checkState(mData);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return LoadingPager.LoadedResult.ERROR;
            }
        } else {
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    private void initListener() {
        mAppAdapter.setOnItemClickListener(new OnItemClickListener<AppInfoBean>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, AppInfoBean appInfoBean, int position) {
                goToDetailActivity(mData.get(position).packageName);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, AppInfoBean appInfoBean, int position) {
                return false;
            }
        });
    }

    private void goToDetailActivity(String packageName) {
        Intent intent = new Intent(CommonUtil.getContext(), DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", packageName);
        CommonUtil.getContext().startActivity(intent);
    }

    @Override
    public List<AppInfoBean> onLoadMore() throws Throwable {
        return mAppProtocol.loadData(mData.size());
    }

    @Override
    public void onResume() {
        // 重新添加监听
        if (mAppAdapter != null) {
            List<AppItemHolder> appItemHolders = mAppAdapter.getAppItemHolders();
            for (AppItemHolder appItemHolder : appItemHolders) {
                DownloadManager.getInstance().addObserver(appItemHolder);//重新添加
            }
            // 手动刷新-->重新获取状态,然后更新ui
            mAppAdapter.notifyDataSetChanged();
        }

        super.onResume();
    }

    @Override
    public void onPause() {

        // 移除监听
        if (mAppAdapter != null) {
            List<AppItemHolder> appItemHolders = mAppAdapter.getAppItemHolders();
            for (AppItemHolder appItemHolder : appItemHolders) {
                DownloadManager.getInstance().deleteObserver(appItemHolder);//删除
            }
        }
        super.onPause();
    }

    private class AppAdapter extends BaseAdapter {

        public AppAdapter(Context context, int layoutId, List<AppInfoBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }
    }

}
