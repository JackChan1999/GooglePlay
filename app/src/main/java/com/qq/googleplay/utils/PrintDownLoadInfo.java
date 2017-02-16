package com.qq.googleplay.utils;

import com.qq.googleplay.bean.DownLoadInfo;
import com.qq.googleplay.manager.DownloadManager;
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
