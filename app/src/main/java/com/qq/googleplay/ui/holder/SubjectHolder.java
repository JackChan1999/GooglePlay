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
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/26 23:14
 * 描 述 ：
 * 修订历史 ：
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


