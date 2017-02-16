package com.qq.googleplay.net.Protocol;

import com.qq.googleplay.base.BaseProtocol;
import com.qq.googleplay.bean.CategoryInfoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class CategoryProtocol extends BaseProtocol<List<CategoryInfoBean>> {
    @Override
    public String getInterfaceKey() {
        return "category";
    }

    @Override
    public List<CategoryInfoBean> parsejson(String jsonString) {

        List<CategoryInfoBean> list = new ArrayList<>();

        try {//原生api解析
            JSONArray rootJsonArray = new JSONArray(jsonString);
            for (int i = 0; i< rootJsonArray.length(); i++){
                JSONObject itemJsonObj = (JSONObject) rootJsonArray.get(i);
                String title = itemJsonObj.getString("title");
                CategoryInfoBean titleBean = new CategoryInfoBean();
                titleBean.title = title ;
                titleBean.isTitle = true ;
                list.add(titleBean);
                JSONArray infoJsonArray = itemJsonObj.getJSONArray("infos");

                for (int j = 0 ; j<infoJsonArray.length();j++){
                    JSONObject  infoJsonObject = (JSONObject) infoJsonArray.get(j);
                    String name1 = infoJsonObject.getString("name1");
                    String name2 = infoJsonObject.getString("name2");
                    String name3 = infoJsonObject.getString("name3");
                    String url1 = infoJsonObject.getString("url1");
                    String url2 = infoJsonObject.getString("url2");
                    String url3 = infoJsonObject.getString("url3");

                    CategoryInfoBean infoBean = new CategoryInfoBean();
                    infoBean.name1 = name1;
                    infoBean.name2 = name2;
                    infoBean.name3 = name3;
                    infoBean.url1 = url1;
                    infoBean.url2 = url2;
                    infoBean.url3 = url3;

                    list.add(infoBean);
                }
            }
            return list ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
