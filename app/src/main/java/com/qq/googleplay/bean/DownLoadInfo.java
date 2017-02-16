package com.qq.googleplay.bean;

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
public class DownLoadInfo {
	public String	savePath;
	public String	downloadUrl;
	public int		state	= DownloadManager.STATE_UNDOWNLOAD; // 默认状态就是未下载
	public String	packageName;								// 包名
	public long		max;
	public long		curProgress;
	public Runnable	task;
}
