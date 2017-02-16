package com.qq.googleplay.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseAdapter;
import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.base.LoadingPager;
import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.bean.DownLoadInfo;
import com.qq.googleplay.bean.HomeBean;
import com.qq.googleplay.factory.ThreadPoolFactory;
import com.qq.googleplay.manager.DownloadManager;
import com.qq.googleplay.net.Protocol.HomeProtocol;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.ui.activity.DetailActivity;
import com.qq.googleplay.ui.adapter.recyclerview.CommonAdapter;
import com.qq.googleplay.ui.adapter.recyclerview.OnItemClickListener;
import com.qq.googleplay.ui.holder.AppItemHolder;
import com.qq.googleplay.ui.holder.PictureHolder;
import com.qq.googleplay.ui.holder.ViewHolder;
import com.qq.googleplay.ui.recyclerview.RecyclerViewUtils;
import com.qq.googleplay.ui.widget.CircleProgressView;
import com.qq.googleplay.ui.widget.CustomSwipeToRefresh;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.StringUtil;
import com.qq.googleplay.utils.bitmap.BitmapHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
public class HomeFragment extends BaseFragment<AppInfoBean> {

    private HomeProtocol mHomeProtocol;
    private List<String> mPicture;//轮播图的数据
    private List<String> mDesc;//轮播图的title

    private CustomSwipeToRefresh mRefreshLayout;
    public  PictureHolder        mPictureHolder;
    private RecyclerView         mRecyclerView;
    //private HomeAdapter          mHomeAdapter;
    private BaseAdapter          mAdapter;
    private final int viewType = Integer.MAX_VALUE / 2;
    private HomeBean mHomeBean;

    @Override
    protected LoadingPager.LoadedResult initData() {

        mHomeProtocol = new HomeProtocol();
        try {
            mHomeBean = mHomeProtocol.loadData(0);
            LoadingPager.LoadedResult state = checkState(mHomeBean);

            // 如果不成功,就直接返回,走到这里说明homeBean是ok
            if (state != LoadingPager.LoadedResult.SUCCESS) {
                return state;
            }
            // 如果不成功,就直接返回,走到这里说明homeBean.list是ok
            state = checkState(mHomeBean.list);
            if (state != LoadingPager.LoadedResult.SUCCESS) {
                return state;
            }
            mData = mHomeBean.list;
            mPicture = mHomeBean.picture;
            mDesc = mHomeBean.des;
            return LoadingPager.LoadedResult.SUCCESS;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {

        mRefreshLayout = new CustomSwipeToRefresh(mActivity);
        mRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED);

        mPictureHolder = new PictureHolder(mActivity);
        mPictureHolder.setDataAndRefreshHolderView(mPicture);
        mPictureHolder.setDescData(mDesc);
        View headerView = mPictureHolder.getHolderView();

        //mHomeAdapter = new HomeAdapter(mActivity, R.layout.item_homelist,mData);

        mAdapter = new BaseAdapter(mActivity,R.layout.item_homelist,mData);
        //mAdapter = new RecyclerViewAdapter(AdapterUtils.getSlideInBottomAdapter(mHomeAdapter,mRecyclerView));
        mRecyclerView = setupRecyclerView(mAdapter, Color.parseColor("#eaeaea"), null);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(viewType,10);
        RecyclerViewUtils.setHeaderView(mRecyclerView, headerView);
        mRefreshLayout.addView(mRecyclerView);
        initListener();

        return mRefreshLayout;
    }

    private void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ThreadPoolFactory.getNormalPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mHomeBean = mHomeProtocol.loadData(0);
                            mData.addAll(0,mHomeBean.list);
                            mDesc = mHomeBean.des;
                            mPicture = mHomeBean.picture;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        CommonUtil.postTaskSafely(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                mRefreshLayout.setRefreshing(false);
                                mPictureHolder.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener<AppInfoBean>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, AppInfoBean appInfoBean, int position) {
                goToDetailActivity(mData.get(position - 1).packageName);
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

    private class HomeAdapter extends CommonAdapter<AppInfoBean> {

        public HomeAdapter(Context context, int layoutId, List<AppInfoBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(final ViewHolder holder, final AppInfoBean data) {
            holder.setText(R.id.item_appinfo_tv_des, data.des);
            holder.setText(R.id.item_appinfo_tv_size, StringUtil.formatFileSize(data.size));
            holder.setText(R.id.item_appinfo_tv_title, data.name);

            holder.setRating(R.id.item_appinfo_rb_stars, data.stars);

            BitmapHelper.instance.init(mActivity).display((ImageView) holder.getView(R.id.item_appinfo_iv_icon),
                    RequestConstants.URLS.IMAGEBASEURL+data.iconUrl);

            Observable.create(new Observable.OnSubscribe<DownLoadInfo>() {
                @Override
                public void call(Subscriber<? super DownLoadInfo> subscriber) {
                    subscriber.onNext(DownloadManager.getInstance().getDownLoadInfo(data));
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<DownLoadInfo>() {
                @Override
                public void call(DownLoadInfo downLoadInfo) {
                    refreshCircleProgressViewUI(downLoadInfo,
                            (CircleProgressView) holder.getView(R.id.circleprogressview));
                }
            });

            /*ThreadPoolFactory.getNormalPool().execute(new Runnable() {
                @Override
                public void run() {
                    final DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(data);
                    CommonUtil.postTaskSafely(new Runnable() {
                        @Override
                        public void run() {
                            refreshCircleProgressViewUI(info, (CircleProgressView) holder.getView(R.id.circleprogressview));
                        }
                    });
                }
            });*/
        }

        public void refreshCircleProgressViewUI(DownLoadInfo info, CircleProgressView circleprogressview) {
            switch (info.state) {
                /**
                 状态(编程记录)  	|  给用户的提示(ui展现)
                 ----------------|----------------------
                 未下载			|下载
                 下载中			|显示进度条
                 暂停下载			|继续下载
                 等待下载			|等待中...
                 下载失败 			|重试
                 下载完成 			|安装
                 已安装 			|打开
                 */
                case DownloadManager.STATE_UNDOWNLOAD:// 未下载
                    circleprogressview.setNote("下载");
                    circleprogressview.setIcon(R.mipmap.ic_download);
                    break;
                case DownloadManager.STATE_DOWNLOADING:// 下载中
                    circleprogressview.setProgressEnable(true);
                    circleprogressview.setMax(info.max);
                    circleprogressview.setProgress(info.curProgress);
                    int progress = (int) (info.curProgress * 100.f / info.max + .5f);
                    circleprogressview.setNote(progress + "%");
                    circleprogressview.setIcon(R.mipmap.ic_pause);
                    break;
                case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                    circleprogressview.setNote("继续下载");
                    circleprogressview.setIcon(R.mipmap.ic_resume);
                    break;
                case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                    circleprogressview.setNote("等待中...");
                    circleprogressview.setIcon(R.mipmap.ic_pause);
                    break;
                case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                    circleprogressview.setNote("重试");
                    circleprogressview.setIcon(R.mipmap.ic_redownload);
                    break;
                case DownloadManager.STATE_DOWNLOADED:// 下载完成
                    circleprogressview.setProgressEnable(false);
                    circleprogressview.setNote("安装");
                    circleprogressview.setIcon(R.mipmap.ic_install);
                    break;
                case DownloadManager.STATE_INSTALLED:// 已安装
                    circleprogressview.setNote("打开");
                    circleprogressview.setIcon(R.mipmap.ic_install);
                    break;

                default:
                    break;
            }
        }
    }


    @Override
    public List<AppInfoBean> onLoadMore() throws Throwable {
        return loadMore(mData.size());
    }

    private List<AppInfoBean> loadMore(int index) throws Throwable {

        HomeBean homeBean = mHomeProtocol.loadData(index);
        if (homeBean == null) {
            return null;
        }

        if (homeBean.list == null || homeBean.list.size() == 0) {
            return null;
        }

        return homeBean.list;
    }

    @Override
    public void onResume() {
        // 重新添加监听
        if (mAdapter != null) {
            List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
            for (AppItemHolder appItemHolder : appItemHolders) {
                DownloadManager.getInstance().addObserver(appItemHolder);//重新添加
            }
            // 手动刷新-->重新获取状态,然后更新ui
            mAdapter.notifyDataSetChanged();
        }

        super.onResume();
    }

    @Override
    public void onPause() {

        // 移除监听
        if (mAdapter != null) {
            List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
            for (AppItemHolder appItemHolder : appItemHolders) {
                DownloadManager.getInstance().deleteObserver(appItemHolder);//删除
            }
        }
        super.onPause();
    }

}
