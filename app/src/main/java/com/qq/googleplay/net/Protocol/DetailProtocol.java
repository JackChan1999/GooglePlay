package com.qq.googleplay.net.Protocol;

import com.qq.googleplay.base.BaseProtocol;
import com.qq.googleplay.bean.AppInfoBean;

import java.util.HashMap;
import java.util.Map;
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
public class DetailProtocol extends BaseProtocol<AppInfoBean> {
	private String	mPackageName;

	public DetailProtocol(String packageName) {
		super();
		mPackageName = packageName;
	}

	@Override
	public String getInterfaceKey() {
		return "detail";
	}

	/*@Override
	public AppInfoBean parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, AppInfoBean.class);
	}*/

	@Override
	public Map<String, String> getExtraParmas() {
		Map<String, String> extraParmas = new HashMap<String, String>();
		extraParmas.put("packageName", mPackageName);
		return extraParmas;
	}

}
