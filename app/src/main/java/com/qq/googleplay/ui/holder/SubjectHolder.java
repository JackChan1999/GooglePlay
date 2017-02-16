package com.qq.googleplay.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.bean.SubjectInfoBean;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.utils.bitmap.BitmapHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

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
public class SubjectHolder extends BaseHolder<SubjectInfoBean> {

    @Bind(R.id.item_subject_iv_icon)
    public ImageView mIvIcon;

    @Bind(R.id.item_subject_tv_title)
    public TextView mTvTitle;

    public SubjectHolder(Context context) {
        super(context);
    }

    @Override
    public View initHolderView() {
        View view = mInflater.inflate(R.layout.item_subject,null, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void refreshHolderView(SubjectInfoBean data) {
        mTvTitle.setText(data.des);
        BitmapHelper.instance.init(mContext).display(mIvIcon, RequestConstants.URLS.IMAGEBASEURL+data.url);
    }
}


