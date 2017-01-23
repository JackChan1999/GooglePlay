package com.qq.googleplay.net.Protocol;

import com.qq.googleplay.base.BaseProtocol;
import com.qq.googleplay.bean.AppInfoBean;

import java.util.List;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/26 09:35
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class AppProtocol extends BaseProtocol<List<AppInfoBean>> {

    @Override
    public String getInterfaceKey() {
        return "app";
    }

   /* @Override
    public List<AppInfoBean> parsejson(String jsonString) {
        //泛型解析
        return new Gson().fromJson(jsonString, new TypeToken<List<AppInfoBean>>() {}.getType());
    }*/
}
