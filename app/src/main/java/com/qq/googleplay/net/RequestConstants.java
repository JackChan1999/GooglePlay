package com.qq.googleplay.net;

import com.qq.googleplay.utils.LogUtil;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/20 17:01
 * 描 述 ：全局变量
 * 修订历史 ：
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
