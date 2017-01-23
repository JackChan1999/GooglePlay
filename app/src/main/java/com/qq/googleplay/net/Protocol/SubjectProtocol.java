package com.qq.googleplay.net.Protocol;

import com.qq.googleplay.base.BaseProtocol;
import com.qq.googleplay.bean.SubjectInfoBean;

import java.util.List;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/26 22:59
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class SubjectProtocol extends BaseProtocol<List<SubjectInfoBean>> {
    @Override
    public String getInterfaceKey() {
        return "subject";
    }

    /*@Override
    public List<SubjectInfoBean> parsejson(String jsonString) {
        return new Gson().fromJson(jsonString,new TypeToken<List<SubjectInfoBean>>(){}.getType());
    }*/
}
