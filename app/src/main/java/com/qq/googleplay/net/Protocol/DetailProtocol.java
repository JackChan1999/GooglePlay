package com.qq.googleplay.net.Protocol;

import com.qq.googleplay.base.BaseProtocol;
import com.qq.googleplay.bean.AppInfoBean;

import java.util.HashMap;
import java.util.Map;

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
