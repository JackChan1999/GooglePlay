package com.qq.googleplay.net.Protocol;

import com.qq.googleplay.base.BaseProtocol;

import java.util.List;

public class RecommendProtocol extends BaseProtocol<List<String>> {

	@Override
	public String getInterfaceKey() {
		return "recommend";
	}

	/*@Override
	protected List<String> parsejson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<String>>() {}.getType());
	}*/
}
