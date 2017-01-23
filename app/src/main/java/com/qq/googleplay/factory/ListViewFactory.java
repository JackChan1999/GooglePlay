package com.qq.googleplay.factory;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/26 10:15
 * 描 述 ：ListView工厂
 * 修订历史 ：
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
