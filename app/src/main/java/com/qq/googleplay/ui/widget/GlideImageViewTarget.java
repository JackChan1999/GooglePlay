package com.qq.googleplay.ui.widget;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.qq.googleplay.R;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/30 19:32
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class GlideImageViewTarget extends ImageViewTarget<GlideDrawable> {

    public GlideImageViewTarget(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(GlideDrawable resource) {
        view.setImageDrawable(resource);
    }

    @Override
    public void setRequest(Request request) {
        super.setRequest(request);
       /* view.setTag(i);
        view.setTag(R.id.image_tag,request);*/
    }

    @Override
    public Request getRequest() {
        return (Request) view.getTag(R.id.image_tag);
    }
}
