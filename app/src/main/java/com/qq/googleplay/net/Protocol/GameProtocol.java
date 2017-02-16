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
