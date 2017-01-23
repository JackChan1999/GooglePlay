package com.qq.googleplay.net.Protocol;

import com.qq.googleplay.base.BaseProtocol;
import com.qq.googleplay.bean.HomeBean;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/24 16:02
 * 描 述 ：
 * 修订历史 ：
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
