package com.qq.googleplay.net;

import com.qq.googleplay.utils.LogUtil;

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
public class RequestConstants {

	// LEVEL_ALL,显示所有的日子,OFF:关闭日志的显示
	public static final int	DEBUGLEVEL		= LogUtil.LEVEL_ALL;
	public static final int	PAGESIZE		= 20;
	public static final int	PROTOCOLTIMEOUT	= 30*60*1000;// 30分钟

	public static final class URLS {
		public static final String	BASEURL			= "http://127.0.0.1:8090/";
		// http://localhost:8080/GooglePlayServer/image?name=
		public static final String	IMAGEBASEURL	= BASEURL + "image?name=";
		public static final String	DOWNLOADBASEURL	= BASEURL + "download";
	}

	public static final class PAY {

	}

	public static final class REQ {

	}

	public static final class RES {

	}

}
