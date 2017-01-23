package com.qq.googleplay.ui.drawable;

import android.graphics.drawable.GradientDrawable;

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
