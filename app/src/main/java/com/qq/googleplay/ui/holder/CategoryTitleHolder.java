package com.qq.googleplay.ui.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.bean.CategoryInfoBean;
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
public class CategoryTitleHolder extends BaseHolder<CategoryInfoBean> {

    private TextView mTextView;

    public CategoryTitleHolder(Context context) {
        super(context);
    }

    @Override
    public View initHolderView() {

        mTextView = new TextView(CommonUtil.getContext());
        mTextView.setTextColor(Color.WHITE);
        mTextView.setTextSize(16);
        mTextView.setBackgroundColor(Color.DKGRAY);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTextView.setLayoutParams(params);

        return mTextView;
    }

    @Override
    public void refreshHolderView(CategoryInfoBean data) {
        mTextView.setText(data.title);
    }
}
