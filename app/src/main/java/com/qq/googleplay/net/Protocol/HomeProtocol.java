package com.qq.googleplay.net.Protocol;

import com.qq.googleplay.base.BaseProtocol;
import com.qq.googleplay.bean.HomeBean;

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
public class HomeProtocol extends BaseProtocol<HomeBean> {


    @Override
    public String getInterfaceKey() {
        return "home";
    }

   /* @Override
    public HomeBean parsejson(String jsonString) {
        //GsonUtil.changeGsonToBean(jsonString,HomeBean.class)
        return new Gson().fromJson(jsonString, HomeBean.class);
    }*/

}
