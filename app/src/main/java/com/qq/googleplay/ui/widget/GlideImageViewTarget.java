package com.qq.googleplay.ui.widget;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.qq.googleplay.R;

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
