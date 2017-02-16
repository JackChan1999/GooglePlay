package com.qq.googleplay.factory;

import android.support.v7.widget.RecyclerView;

import com.qq.googleplay.utils.CommonUtil;

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
public class RecyclerviewFactory {

    public  static RecyclerView createRecyclerView(){
        RecyclerView rv = new RecyclerView(CommonUtil.getContext());
        //rv.setBackgroundColor(Color.parseColor("#EFF4F7"));
        rv.setHasFixedSize(true);
        return rv;
    }
}
