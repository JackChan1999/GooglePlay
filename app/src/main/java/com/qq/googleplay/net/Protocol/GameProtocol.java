package com.qq.googleplay.net.Protocol;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.qq.googleplay.base.BaseProtocol;
import com.qq.googleplay.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/26 11:22
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class GameProtocol extends BaseProtocol<List<AppInfoBean>> {

    @Override
    public String getInterfaceKey() {
        return "game";
    }

    @Override
    public List<AppInfoBean> parsejson(String jsonString) {
        //结点解析
        List<AppInfoBean> appInfoBeans = new ArrayList<>() ;
        JsonParser parser = new JsonParser();//获取json解析器
        JsonElement rootJsonElement = parser.parse(jsonString);
        JsonArray rootJsonArray = rootJsonElement.getAsJsonArray();
        for (JsonElement itemJsonElement : rootJsonArray) {
            JsonObject itemJsonObject = itemJsonElement.getAsJsonObject();
            JsonPrimitive desPrimitive = itemJsonObject.getAsJsonPrimitive("des");
            String des = desPrimitive.getAsString();

            JsonPrimitive downloadUrlPrimitive = itemJsonObject.getAsJsonPrimitive("downloadUrl");
            String downloadUrl = downloadUrlPrimitive.getAsString();

            String iconUrl = itemJsonObject.get("iconUrl").getAsString();
            long id = itemJsonObject.get("id").getAsLong();
            String name = itemJsonObject.get("name").getAsString();
            String packageName = itemJsonObject.get("packageName").getAsString();
            long size = itemJsonObject.get("size").getAsLong();
            float stars = itemJsonObject.get("stars").getAsFloat();

            AppInfoBean info = new AppInfoBean();
            info.des = des;
            info.downloadUrl = downloadUrl;
            info.iconUrl = iconUrl;
            info.id = id;
            info.name = name;
            info.packageName = packageName;
            info.size = size;
            info.stars = stars;
            // 添加到集合
            appInfoBeans.add(info);

        }
        return  appInfoBeans;
    }
}
