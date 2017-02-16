package com.qq.googleplay.ui.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseViewHolder;
import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.bean.DownLoadInfo;
import com.qq.googleplay.common.utils.AppUtil;
import com.qq.googleplay.manager.DownloadManager;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.ui.widget.CircleProgressView;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.StringUtil;
import com.qq.googleplay.utils.bitmap.BitmapHelper;

import java.io.File;

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
public class AppHolder extends BaseViewHolder<AppInfoBean> implements
        View.OnClickListener, DownloadManager.DownLoadObserver {

    private CircleProgressView circleprogressview;

    public AppHolder(Context context, View itemView, ViewGroup parent, int position) {
        super(context, itemView, parent, position);
    }

    public static AppHolder get(Context context, View convertView,
                                    ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            AppHolder holder = new AppHolder(context, itemView, parent, position);
            holder.mLayoutId = layoutId;
            return holder;
        } else {
            AppHolder holder = (AppHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    @Override
    public void refreshHolderView(final AppInfoBean data) {
        circleprogressview = getView(R.id.circleprogressview);
        circleprogressview.setOnClickListener(this);

        // 清除复用convertView之后的progress效果
        circleprogressview.setProgress(0);

        setText(R.id.item_appinfo_tv_des, data.des);
        setText(R.id.item_appinfo_tv_size, StringUtil.formatFileSize(data.size));
        setText(R.id.item_appinfo_tv_title, data.name);
        setRating(R.id.item_appinfo_rb_stars, data.stars);

        BitmapHelper.instance.init(mContext).display((ImageView) getView(R.id.item_appinfo_iv_icon),
                RequestConstants.URLS.IMAGEBASEURL + data.iconUrl);

        /*=============== 根据不同的状态给用户提示 ===============*/

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
                        refreshCircleProgressViewUI(downLoadInfo);
                    }
                });
    }

    public void refreshCircleProgressViewUI(DownLoadInfo info) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circleprogressview:

                DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(mData);

                switch (info.state) {
                    /**
                     状态(编程记录)     | 用户行为(触发操作)
                     ----------------| -----------------
                     未下载			| 去下载
                     下载中			| 暂停下载
                     暂停下载			| 断点继续下载
                     等待下载			| 取消下载
                     下载失败 			| 重试下载
                     下载完成 			| 安装应用
                     已安装 			| 打开应用
                     */
                    case DownloadManager.STATE_UNDOWNLOAD:// 未下载
                        doDownLoad(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADING:// 下载中
                        pauseDownLoad(info);
                        break;
                    case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                        doDownLoad(info);
                        break;
                    case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                        cancelDownLoad(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                        doDownLoad(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADED:// 下载完成
                        installApk(info);
                        break;
                    case DownloadManager.STATE_INSTALLED:// 已安装
                        openApk(info);
                        break;

                    default:
                        break;
                }

                break;

            default:
                break;
        }
    }

    /**
     * 打开应用
     *
     * @param info
     */
    private void openApk(DownLoadInfo info) {
        AppUtil.openApp(CommonUtil.getContext(), info.packageName);
    }

    /**
     * 安装应用
     *
     * @param info
     */
    private void installApk(DownLoadInfo info) {
        File apkFile = new File(info.savePath);
        AppUtil.installApp(CommonUtil.getContext(), apkFile);
    }

    /**
     * 取消下载
     *
     * @param info
     */
    private void cancelDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().cancel(info);
    }

    /**
     * 暂停下载
     *
     * @param info
     */
    private void pauseDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().pause(info);
    }

    /**
     * 开始下载
     *
     * @param info
     */
    private void doDownLoad(DownLoadInfo info) {
        /*=============== 根据不同的状态触发不同的操作 ===============*/
        /*// 下载apk放置的目录
        String dir = FileUtils.getDir("download");// sdcard/android/data/包名/download
		File file = new File(dir, mData.packageName + ".apk");//
		sdcard/android/data/包名/download/com.itheima.www.apk
		// 保存路径
		String savePath = file.getAbsolutePath();// sdcard/android/data/包名/download/com.itheima
		.www.apk

		DownLoadInfo info = new DownLoadInfo();
		info.savePath = savePath;
		info.downloadUrl = mData.downloadUrl;
		info.packageName = mData.packageName;*/

        DownloadManager.getInstance().downLoad(info);
    }

    /**
     * 收到数据改变,更新UI
     */
    @Override
    public void onDownLoadInfoChange(final DownLoadInfo info) {
        // 过滤DownLoadInfo
        if (!info.packageName.equals(mData.packageName)) {
            return;
        }
        //PrintDownLoadInfo.printDownLoadInfo(info);
        CommonUtil.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                refreshCircleProgressViewUI(info);
            }
        });
    }

}
