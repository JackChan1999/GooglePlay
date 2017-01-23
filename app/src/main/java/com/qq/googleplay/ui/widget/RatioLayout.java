package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.qq.googleplay.R;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/26 23:16
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class RatioLayout extends FrameLayout {

    /*private float mPicRatio = 0f;                // 图片的宽高比 2.43
    public static final int RELATIVE_WIDTH = 0;  // 控件宽度固定,已知图片的宽高比,求控件的高度
    public static final int RELATIVE_HEIGHT = 1; // 控件高度固定,已知图片的宽高比,求控件的宽度
    private int mRelative = RELATIVE_WIDTH;

    public void setPicRatio(float picRatio) {
        mPicRatio = picRatio;
    }

    public void setRelative(int relative) {
        mRelative = relative;
    }

    public RatioLayout(Context context) {
        this(context, null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);

        mPicRatio = typedArray.getFloat(R.styleable.RatioLayout_picRatio, 0.0f);

        mRelative = typedArray.getInt(R.styleable.RatioLayout_relative, RELATIVE_WIDTH);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 控件宽度固定,已知图片的宽高比,求控件的高度
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);

        // 控件高度固定,已知图片的宽高比,求控件的宽度
        int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (parentWidthMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative ==
                RELATIVE_WIDTH) {// 控件宽度固定,已知图片的宽高比,求控件的高度
            // 得到父容器的宽度
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            // 得到孩子的宽度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            // 控件的宽度/控件的高度 = mPicRatio;

            // 计算孩子的高度
            int childHeight = (int) (childWidth / mPicRatio + .5f);

            // 计算父容器的高度
            int parentHeight = childHeight + getPaddingBottom() + getPaddingTop();

            // 主动测绘孩子.固定孩子的大小
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec
                    .EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec
                    .EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

            // 设置自己的测试结果
            setMeasuredDimension(parentWidth, parentHeight);

        } else if (parentHeightMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative ==
                RELATIVE_HEIGHT) {
            // 控件高度固定,已知图片的宽高比,求控件的宽度
            // 得到父亲的高度
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

            // 得到孩子的高度
            int childHeight = parentHeight - getPaddingBottom() - getPaddingTop();

            // 控件的宽度/控件的高度 = mPicRatio;
            // 计算控件宽度
            int childWidth = (int) (childHeight * mPicRatio + .5f);

            // 得到父亲的宽度
            int parentWidth = childWidth + getPaddingRight() + getPaddingLeft();

            // 主动测绘孩子.固定孩子的大小
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec
                    .EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec
                    .EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

            // 设置自己的测试结果
            setMeasuredDimension(parentWidth, parentHeight);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }

    }*/

    // 宽和高的比例
    private float ratio = 0.0f;

    public RatioLayout(Context context) {
        this(context, null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        ratio = a.getFloat(R.styleable.RatioLayout_picRatio, 0.0f);
        a.recycle();
    }

    public void setRatio(float f) {
        ratio = f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(),
                    MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {
            width = (int) (height * ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(),
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
