package com.qq.googleplay.ui.holder;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseActivity;
import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.bean.DownLoadInfo;
import com.qq.googleplay.common.utils.AppUtil;
import com.qq.googleplay.manager.DownloadManager;
import com.qq.googleplay.ui.widget.ProgressButton;
import com.qq.googleplay.utils.CommonUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
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
public class AppDetailBottomHolder extends BaseHolder<AppInfoBean> implements
        OnClickListener, DownloadManager.DownLoadObserver {

    @Bind(R.id.app_detail_download_btn_share)
    Button mBtnShare;

    @Bind(R.id.app_detail_download_btn_favo)
    Button mBtnFavo;

    @Bind(R.id.app_detail_download_btn_download)
    ProgressButton mProgressButton;

    private AppInfoBean mData;

    public AppDetailBottomHolder(Context context) {
        super(context);
    }

    @Override
    public View initHolderView() {
        View view = mInflater.inflate(R.layout.item_app_detail_bottom, null, false);
        ButterKnife.bind(this, view);
        mBtnShare.setOnClickListener(this);
        mBtnFavo.setOnClickListener(this);
        mProgressButton.setOnClickListener(this);
        mProgressButton.setButtonRadius(20f);
        return view;
    }

    @Override
    public void refreshHolderView(AppInfoBean data) {
        mData = data;
        //根据不同的状态给用户提示
        DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(data);
        refreshProgressBtnUI(info);
    }

    public void refreshProgressBtnUI(DownLoadInfo info) {
        float progress = info.curProgress * 100.f / info.max + .5f;
        switch (info.state) {
            /**
             状态(编程记录)  	|  给用户的提示(ui展现)
             ----------------|----------------------
             未下载			|下载
             下载中			|显示进度条
             暂停下载		|继续下载
             等待下载		|等待中...
             下载失败 		|重试
             下载完成 		|安装
             已安装 			|打开
             */
            case DownloadManager.STATE_UNDOWNLOAD:// 未下载
                mProgressButton.setCurrentText("下载");
                break;
            case DownloadManager.STATE_DOWNLOADING:// 下载中
                mProgressButton.setState(ProgressButton.DOWNLOADING);
                if (mProgressButton.getProgress() == 0) {
                    mProgressButton.setProgress(progress);
                }
                mProgressButton.setProgressText("下载中", progress);
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                mProgressButton.setState(ProgressButton.DOWNLOADING);
                mProgressButton.setProgress(progress);
                mProgressButton.setProgressText("继续下载", progress);
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                mProgressButton.setCurrentText("等待中...");
                break;
            case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                mProgressButton.setCurrentText("重试");
                break;
            case DownloadManager.STATE_DOWNLOADED:
                mProgressButton.setState(ProgressButton.NORMAL);
                mProgressButton.setCurrentText("安装");
                break;
            case DownloadManager.STATE_INSTALLED:// 已安装
                mProgressButton.setCurrentText("打开");
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_detail_download_btn_download:

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
            case R.id.app_detail_download_btn_share:
                new ShareAction((Activity) mContext).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA
                        .QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withText("下载地址:" + "https://play.google.com/store/apps/details?id=" + mData.packageName)
                        .setCallback(umShareListener)
                        .open();
                break;
            case R.id.app_detail_download_btn_favo:
                ((BaseActivity)mContext).toast("收藏");
                break;
            default:
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };


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

    /*=============== 收到数据改变,更新ui ===============*/
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
                refreshProgressBtnUI(info);
            }
        });
    }

    public void addObserverAndRerefresh() {
        DownloadManager.getInstance().addObserver(this);
        // 手动刷新
        DownLoadInfo downLoadInfo = DownloadManager.getInstance().getDownLoadInfo(mData);
        DownloadManager.getInstance().notifyObservers(downLoadInfo);// 方式一
        // refreshProgressBtnUI(downLoadInfo);//方式二
    }

}
