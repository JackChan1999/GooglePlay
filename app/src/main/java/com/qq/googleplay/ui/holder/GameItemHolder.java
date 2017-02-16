package com.qq.googleplay.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.utils.bitmap.BitmapHelper;
import com.qq.googleplay.utils.StringUtil;

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
public class GameItemHolder extends BaseHolder<AppInfoBean> {
    @Bind(R.id.item_appinfo_iv_icon)
    ImageView mIvIcon;

    @Bind(R.id.item_appinfo_rb_stars)
    RatingBar mRbStars;

    @Bind(R.id.item_appinfo_tv_des)
    TextView mTvDes;

    @Bind(R.id.item_appinfo_tv_size)
    TextView mTvSize;

    @Bind(R.id.item_appinfo_tv_title)
    TextView mTvTitle;

    public GameItemHolder(Context context) {
        super(context);
    }

    @Override
    public View initHolderView() {
        View view = mInflater.inflate(R.layout.item_gamelist, null, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void refreshHolderView(AppInfoBean data) {

        mTvDes.setText(data.des);
        mTvSize.setText(StringUtil.formatFileSize(data.size));
        mTvTitle.setText(data.name);

        mIvIcon.setImageResource(R.mipmap.ic_default);// 默认图片

        String url = RequestConstants.URLS.IMAGEBASEURL + data.iconUrl;
        BitmapHelper.instance.init(mContext).display(mIvIcon, url);
        mRbStars.setRating(data.stars);

    }

}
