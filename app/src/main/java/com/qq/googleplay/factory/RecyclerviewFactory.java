package com.qq.googleplay.factory;

import android.support.v7.widget.RecyclerView;

import com.qq.googleplay.utils.CommonUtil;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/26 10:15
 * 描 述 ：RecyclerView工厂
 * 修订历史 ：
 * ============================================================
 **/
public class RecyclerviewFactory {

    public  static RecyclerView createRecyclerView(){
        RecyclerView rv = new RecyclerView(CommonUtil.getContext());
        //rv.setBackgroundColor(Color.parseColor("#EFF4F7"));
        rv.setHasFixedSize(true);
        return rv;
    }
}
