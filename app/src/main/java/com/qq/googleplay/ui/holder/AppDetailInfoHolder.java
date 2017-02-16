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
import com.qq.googleplay.utils.StringUtil;
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
public class AppDetailInfoHolder extends BaseHolder<AppInfoBean> {

    @Bind(R.id.app_detail_info_iv_icon)
    ImageView mIvIcon;

    @Bind(R.id.app_detail_info_rb_star)
    RatingBar mRbStar;

    @Bind(R.id.app_detail_info_tv_downloadnum)
    TextView mTvDownLoadNum;

    @Bind(R.id.app_detail_info_tv_name)
    TextView mTvName;

    @Bind(R.id.app_detail_info_tv_time)
    TextView mTvTime;

    @Bind(R.id.app_detail_info_tv_version)
    TextView mTvVersion;

    @Bind(R.id.app_detail_info_tv_size)
    TextView mTvSize;

    public AppDetailInfoHolder(Context context) {
        super(context);
    }

    @Override
    public View initHolderView() {
        View view = mInflater.inflate(R.layout.item_app_detail_info, null,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void refreshHolderView(AppInfoBean data) {
        String date = StringUtil.getString(R.string.detail_date, data.date);
        String downloadNum = StringUtil.getString(R.string.detail_downloadnum, data.downloadNum);
        String size = StringUtil.getString(R.string.detail_size, StringUtil.formatFileSize(data
                .size));
        String version = StringUtil.getString(R.string.detail_version, data.version);

        mTvName.setText(data.name);
        mTvDownLoadNum.setText(downloadNum);
        mTvTime.setText(date);
        mTvVersion.setText(version);
        mTvSize.setText(size);

        mIvIcon.setImageResource(R.mipmap.ic_default);
        BitmapHelper.instance.init(mContext).display(mIvIcon,
                RequestConstants.URLS.IMAGEBASEURL+data.iconUrl);

        mRbStar.setRating(data.stars);
    }

}
