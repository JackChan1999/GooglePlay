package com.qq.googleplay.net.Protocol;

import com.qq.googleplay.base.BaseProtocol;

import java.util.List;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/27 10:32
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class HotProtocol extends BaseProtocol<List<String>> {
    @Override
    public String getInterfaceKey() {
        return "hot";
    }

   /* @Override
    public List<String> parsejson(String jsonString) {
        return new Gson().fromJson(jsonString,new TypeToken<List<String>>(){}.getType());
    }*/
}
