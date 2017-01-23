package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;

import com.qq.googleplay.R;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/22 19:26
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class CustomShareActionProvider extends ShareActionProvider {

    private final Context mContext;

    public CustomShareActionProvider(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView() {
        ActivityChooserView chooserView =
                (ActivityChooserView) super.onCreateActionView();

        // Set your drawable here
        Drawable icon = mContext.getResources().getDrawable(R.mipmap.ic_action_search);

        chooserView.setExpandActivityOverflowButtonDrawable(icon);

        return chooserView;
    }
}
