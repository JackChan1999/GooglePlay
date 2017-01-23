package com.qq.googleplay.utils;

import com.qq.googleplay.bean.DownLoadInfo;
import com.qq.googleplay.manager.DownloadManager;


public class PrintDownLoadInfo {
	public static void printDownLoadInfo(DownLoadInfo info) {
		String result = "";
		switch (info.state) {
		case DownloadManager.STATE_UNDOWNLOAD:// 未下载
			result = "未下载";
			break;
		case DownloadManager.STATE_DOWNLOADING:// 下载中
			result = "下载中";
			break;
		case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
			result = "暂停下载";
			break;
		case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
			result = "等待下载";
			break;
		case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
			result = "等待下载";
			break;
		case DownloadManager.STATE_DOWNLOADED:// 下载完成
			result = "下载完成";
			break;
		case DownloadManager.STATE_INSTALLED:// 已安装
			result = "已安装";
			break;

		default:
			break;
		}
		LogUtil.sf(result);
	}
}
