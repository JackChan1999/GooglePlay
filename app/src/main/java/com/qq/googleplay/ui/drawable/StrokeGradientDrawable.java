package com.qq.googleplay.ui.drawable;

import android.graphics.drawable.GradientDrawable;
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
public class StrokeGradientDrawable {
    private GradientDrawable mGradientDrawable;
    private int mStrokeColor;
    private int mStrokeWidth;

    public StrokeGradientDrawable(GradientDrawable gradientDrawable) {
        this.mGradientDrawable = gradientDrawable;
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int i) {
        mStrokeWidth = i;
        mGradientDrawable.setStroke(i, getStrokeColor());
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int i) {
        mStrokeColor = i;
        mGradientDrawable.setStroke(getStrokeWidth(), i);
    }

    public GradientDrawable getGradientDrawable() {
        return mGradientDrawable;
    }
}
