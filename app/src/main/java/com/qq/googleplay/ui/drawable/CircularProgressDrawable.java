package com.qq.googleplay.ui.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.qq.googleplay.ui.widget.CircleProgressBar;
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
public class CircularProgressDrawable extends Drawable {
    private Drawable mCenterIcon;
    private Paint mIconPaint;
    private Paint mPaint;
    private Path mPath;
    private RectF mRectF;
    private boolean mShouldIcon = false;
    private float mStartAngle;
    private float mSweepAngle;
    private int mStrokeColor;
    private int mStrokeWidth;
    private int mSize;

    public CircularProgressDrawable(int size, int strokeWidth, int strokeColor) {
        mSize = size;
        mStrokeWidth = strokeWidth;
        mStrokeColor = strokeColor;
        mStartAngle = -90.0f;
    }

    public void setSweepAngle(float sweepAngle) {
        mSweepAngle = sweepAngle;
    }

    public int getSize() {
        return mSize;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (mPath == null) {
            mPath = new Path();
        }
        mPath.reset();
        mPath.addArc(getRect(), mStartAngle, mSweepAngle);
        mPath.offset((float) bounds.left, (float) bounds.top);
        canvas.drawPath(mPath, createPaint());
        if (mCenterIcon != null) {
            canvas.save();
            mCenterIcon.setBounds(0, 0, mCenterIcon.getIntrinsicWidth(), mCenterIcon.getIntrinsicHeight());
            canvas.translate((float) ((bounds.left + (getSize() / 2)) - (mCenterIcon.getIntrinsicWidth() / 2)),
                    (float) ((bounds.top + (getSize() / 2)) - (mCenterIcon.getIntrinsicHeight() / 2)));
            mCenterIcon.draw(canvas);
            canvas.restore();
        } else if (mShouldIcon) {
            if (mIconPaint == null) {
                mIconPaint = new Paint();
                mIconPaint.setStrokeCap(Cap.ROUND);
                mIconPaint.setColor(mStrokeColor);
            }
            int height = getSize();
            int width = getSize();
            int lineWidth = mStrokeWidth;
            int lineHeight = (int) ((((float) height) / 5.0f) * CircleProgressBar.BAR_WIDTH_DEF_DIP);
            int lineGap = (int) (0.15d * ((double) width));
            mIconPaint.setStrokeWidth((float) lineWidth);
            canvas.drawLine((float) (((bounds.left + (width / 2)) - (lineGap / 2)) - (lineWidth / 2)),
                    (float) ((bounds.top + (height / 2)) - (lineHeight / 2)), (float) (((bounds.left + (width / 2))
                            - (lineGap / 2)) - (lineWidth / 2)), (float) ((bounds.top + (height / 2)) + (lineHeight / 2)), mIconPaint);
            canvas.drawLine((float) (((bounds.left + (width / 2)) + (lineGap / 2)) + (lineWidth / 2)),
                    (float) ((bounds.top + (height / 2)) - (lineHeight / 2)), (float) (((bounds.left + (width / 2))
                            + (lineGap / 2)) + (lineWidth / 2)), (float) ((bounds.top + (height / 2)) + (lineHeight / 2)), mIconPaint);
        }
    }

    public void setAlpha(int alpha) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return 1;
    }

    private RectF getRect() {
        if (mRectF == null) {
            int index = mStrokeWidth / 2;
            mRectF = new RectF((float) index, (float) index, (float) (getSize() - index), (float) (getSize() - index));
        }
        return mRectF;
    }

    private Paint createPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Style.STROKE);
            mPaint.setStrokeWidth((float) mStrokeWidth);
            mPaint.setColor(mStrokeColor);
        }
        return mPaint;
    }

    public void setCenterIcon(Drawable centerIcon) {
        mCenterIcon = centerIcon;
    }

    public void setShowCenterIcon(boolean showCenterIcon) {
        mShouldIcon = showCenterIcon;
    }
}
