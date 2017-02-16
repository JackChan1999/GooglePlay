package com.qq.googleplay.factory;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

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
public class ListViewFactory {

    public  static ListView createListView(Context context){
        ListView listView = new ListView(context);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setDivider(null);
        //listView.setFastScrollEnabled(true);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        return listView ;
    }
}
