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
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/4/1 13:35
 * 描 述 ：
 * 修订历史 ：
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
