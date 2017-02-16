package com.qq.googleplay.manager;

import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.bean.DownLoadInfo;
import com.qq.googleplay.common.io.IOUtils;
import com.qq.googleplay.common.utils.AppUtil;
import com.qq.googleplay.factory.ThreadPoolFactory;
import com.qq.googleplay.net.HttpUtils;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
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
public class DownloadManager {

    public static final int STATE_UNDOWNLOAD      = 0;// 未下载
    public static final int STATE_DOWNLOADING     = 1;// 下载中
    public static final int STATE_PAUSEDOWNLOAD   = 2;// 暂停下载
    public static final int STATE_WAITINGDOWNLOAD = 3;// 等待下载
    public static final int STATE_DOWNLOADFAILED  = 4;// 下载失败
    public static final int STATE_DOWNLOADED      = 5;// 下载完成
    public static final int STATE_INSTALLED       = 6;// 已安装

    public static DownloadManager instance;
    // 记录正在下载的一些downLoadInfo
    public Map<String, DownLoadInfo> mDownLoadInfoMaps = new HashMap<String, DownLoadInfo>();

    private DownloadManager() {

    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    /**
     * 用户点击了下载按钮
     */
    public void downLoad(DownLoadInfo info) {
        mDownLoadInfoMaps.put(info.packageName, info);

        //当前状态: 未下载
        info.state = STATE_UNDOWNLOAD;
        notifyObservers(info);

        //当前状态: 等待状态
        info.state = STATE_WAITINGDOWNLOAD;
        notifyObservers(info);

        // 得到线程池,执行任务
        DownLoadTask task = new DownLoadTask(info);
        info.task = task;// downInfo身上的task赋值
        ThreadPoolFactory.getDownLoadPool().execute(task);
    }

    class DownLoadTask implements Runnable {
        DownLoadInfo mInfo;

        public DownLoadTask(DownLoadInfo info) {
            super();
            mInfo = info;
        }

        @Override
        public void run() {
            // 正在发起网络请求下载apk
            // http://localhost:8080/GooglePlayServer/download?name=app/com.itheima.www/com.itheima.www.apk&range=0
            try {
                //当前状态: 下载中
                mInfo.state = STATE_DOWNLOADING;
                notifyObservers(mInfo);

                long initRange = 0;
                File saveApk = new File(mInfo.savePath);
                if (saveApk.exists()) {
                    initRange = saveApk.length();// 未下载完成的apk已有的长度
                }
                mInfo.curProgress = initRange;// 处理初始进度

                Map<String, String> params = new HashMap<>();
                params.put("name", mInfo.downloadUrl);
                params.put("range", initRange + "");

                Response response = HttpUtils.download(RequestConstants.URLS.DOWNLOADBASEURL, params);

                if (response.isSuccessful()) {
                    InputStream in = null;
                    //FileChannel channelOut = null;
                    RandomAccessFile out = null;
                    boolean isPause = false;
                    try {
                        in = response.body().byteStream();
                        File saveFile = new File(mInfo.savePath);
                        out = new RandomAccessFile(saveFile, "rwd");
                        out.seek(initRange);
                        /*channelOut = out.getChannel();
                        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode
                        .READ_WRITE, initRange, body.contentLength());*/

                        byte[] buffer = new byte[1024];
                        int len = -1;

                        long startTime = System.currentTimeMillis();
                        while ((len = in.read(buffer)) != -1) {
                            if (mInfo.state == STATE_PAUSEDOWNLOAD) {
                                isPause = true;
                                break;
                            }
                            //mappedBuffer.put(buffer, 0, len);
                            out.write(buffer, 0, len);
                            mInfo.curProgress += len;

                            if ((System.currentTimeMillis() - startTime) > 500){
                                startTime = System.currentTimeMillis();
                                //当前状态: 下载中
                                mInfo.state = STATE_DOWNLOADING;
                                notifyObservers(mInfo);
                            }
                        }

                        if (isPause) {// 用户暂停了下载走到这里来了
                            //当前状态: 暂停
                            mInfo.state = STATE_PAUSEDOWNLOAD;
                            notifyObservers(mInfo);
                        } else {// 下载完成走到这里来
                            //当前状态: 下载完成
                            mInfo.state = STATE_DOWNLOADED;
                            notifyObservers(mInfo);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        mInfo.state = STATE_DOWNLOADED;
                        notifyObservers(mInfo);
                    } finally {
                        //IOUtils.closeQuietly(channelOut);
                        IOUtils.closeQuietly(out);
                        IOUtils.closeQuietly(in);
                    }
                } else {
                    //当前状态: 下载失败
                    mInfo.state = STATE_DOWNLOADFAILED;
                    notifyObservers(mInfo);
                    throw new IOException("Unexpected code " + response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                //当前状态: 下载失败
                mInfo.state = STATE_DOWNLOADFAILED;
                notifyObservers(mInfo);
            }
        }
    }

    /**
     * @des 暴露当前状态, 也就是需要提供downLoadInfo
     * @call 外界需要知道最新的state的时候
     */
    public DownLoadInfo getDownLoadInfo(AppInfoBean data) {
        // 已安装
        if (AppUtil.isInstalled(CommonUtil.getContext(), data.packageName)) {
            DownLoadInfo info = generateDownLoadInfo(data);
            info.state = STATE_INSTALLED;// 已安装
            return info;
        }
        // 下载完成
        DownLoadInfo info = generateDownLoadInfo(data);
        File saveApk = new File(info.savePath);
        if (saveApk.exists()) {// 如果存在我们的下载目录里面
            if (saveApk.length() == data.size) {
                info.state = STATE_DOWNLOADED;// 下载完成
                return info;
            }
        }
        /**
         *下载中
         *暂停下载
         *等待下载
         *下载失败
         */
        DownLoadInfo downLoadInfo = mDownLoadInfoMaps.get(data.packageName);
        if (downLoadInfo != null) {
            if (downLoadInfo.state == STATE_DOWNLOADED && !saveApk.exists()){
                downLoadInfo.state = STATE_UNDOWNLOAD;
            }
            return downLoadInfo;
        }

        // 未下载
        DownLoadInfo tempInfo = generateDownLoadInfo(data);
        tempInfo.state = STATE_UNDOWNLOAD;// 未下载
        return tempInfo;
    }

    /**
     * 根据AppInfoBean生成一个DownLoadInfo,进行一些常规的赋值,
     * 也就是对一些常规属性赋值(除了state之外的属性)
     */
    public DownLoadInfo generateDownLoadInfo(AppInfoBean data) {
        String dir = FileUtil.getDir("download");// sdcard/android/data/包名/download
        File file = new File(dir, data.name + ".apk");
        // sdcard/android/data/包名/download/com.itheima.www.apk
        // 保存路径
        String savePath = file.getAbsolutePath();// sdcard/android/data/包名/download/com.itheima.apk

        // 初始化一个downLoadInfo
        DownLoadInfo info = new DownLoadInfo();
        // 相关赋值
        info.savePath = savePath;
        info.downloadUrl = data.downloadUrl;
        info.packageName = data.packageName;
        info.max = data.size;
        info.curProgress = 0;
        return info;
    }

    /**
     * 暂停下载
     */
    public void pause(DownLoadInfo info) {
        //当前状态: 暂停
        info.state = STATE_PAUSEDOWNLOAD;
        notifyObservers(info);
    }

    /**
     * 取消下载
     */
    public void cancel(DownLoadInfo info) {
        Runnable task = info.task;
        // 找到线程池,移除任务
        ThreadPoolFactory.getDownLoadPool().removeTask(task);

        //当前状态: 未下载
        info.state = STATE_UNDOWNLOAD;
        notifyObservers(info);
    }

    /**
     * 自定义观察者设计模式
     */
    public interface DownLoadObserver {
        void onDownLoadInfoChange(DownLoadInfo info);
    }

    List<DownLoadObserver> downLoadObservers = new LinkedList<DownLoadObserver>();

    /**
     * 添加观察者
     */
    public void addObserver(DownLoadObserver observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!downLoadObservers.contains(observer))
                downLoadObservers.add(observer);
        }
    }

    /**
     * 删除观察者
     */
    public synchronized void deleteObserver(DownLoadObserver observer) {
        downLoadObservers.remove(observer);
    }

    /**
     * 通知观察者数据改变
     */
    public void notifyObservers(DownLoadInfo info) {
        for (DownLoadObserver observer : downLoadObservers) {
            observer.onDownLoadInfoChange(info);
        }
    }

    /*FileDownloadListener mListener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            System.out.println("pending" + soFarBytes);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            System.out.println("progress" + soFarBytes);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            System.out.println("completed");
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {

        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    };*/

}
