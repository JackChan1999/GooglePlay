package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qq.googleplay.R;

public class CircleProgressBar extends View {
    public static final int BAR_COLOR_DEF = Color.parseColor("#1A000001");
    public static final int CENTER_TEXT_COLOR_DEF = Color.parseColor("#dd000000");
    public static final int RIM_COLOR_DEF = Color.parseColor("#34FFFFFF");
    public static final float BAR_WIDTH_DEF_DIP = 2.0f;
    public static final int CENTER_TEXT_SIZE_DEF = 14;

    private Paint mBarPaint;
    private Paint mTextPaint;
    private Paint mRimPaint;

    private int mBarColor;
    private int mRimColor;
    private int mTextColor;

    private int mBarPostition;
    private int mMax;
    private int mPercentage;
    private int mProgress;
    private int mTextSize;
    private float mBarWidth;

    private boolean mIsShowProgress = true;
    private boolean mShouldUpdateBound = false;
    private String mText;
    private RectF mCircleBound;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        mCircleBound = new RectF();
        mText = "0%";
        TypedArray a = context.obtainStyledAttributes(attributeSet,
                R.styleable.CircleProgressBar, defStyle, 0);
        mBarColor = a.getColor(R.styleable.CircleProgressBar_mcCircleBarColor, BAR_COLOR_DEF);
        mRimColor = a.getColor(R.styleable.CircleProgressBar_mcCircleBarRimColor, RIM_COLOR_DEF);

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        mBarWidth = (float) a.getDimensionPixelSize(R.styleable.CircleProgressBar_mcCircleBarWidth,
                (int) (dm.density * BAR_WIDTH_DEF_DIP));
        mTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_mcCenterTextSize,
                (int) (dm.density * 14.0f));

        mTextColor = a.getColor(R.styleable.CircleProgressBar_mcCenterTextColor, -1);
        setMax(a.getInt(R.styleable.CircleProgressBar_mcCircleBarMax, 0));
        setProgress(a.getInt(R.styleable.CircleProgressBar_mcCircleBarProgress, 0));
        mIsShowProgress = a.getBoolean(R.styleable.CircleProgressBar_mcCircleIsShowProgress, mIsShowProgress);
        a.recycle();
        init();
    }

    private void init() {
        setBound();
        setPaint();
        mBarPostition = getPosByProgress(mProgress, true);
        mPercentage = getPosByProgress(mProgress, false);
        mText = String.valueOf(mPercentage) + "%";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShouldUpdateBound) {
            setBound();
            mShouldUpdateBound = false;
        }
        canvas.drawArc(mCircleBound, 360.0f, 360.0f, false, mRimPaint);
        canvas.drawArc(mCircleBound, -90.0f, (float) mBarPostition, false, mBarPaint);
        float descent = ((mTextPaint.descent() - mTextPaint.ascent()) / BAR_WIDTH_DEF_DIP) - mTextPaint.descent();
        float measureText = mTextPaint.measureText(mText) / BAR_WIDTH_DEF_DIP;
        if (mIsShowProgress) {
            canvas.drawText(mText, ((float) (getWidth() / 2)) - measureText, descent + ((float) (getHeight() / 2)),
                    mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mShouldUpdateBound = true;
    }

    private void setBound() {
        if (mCircleBound == null) {
            mCircleBound = new RectF();
        }
        mCircleBound.left = ((float) getPaddingLeft()) + mBarWidth;
        mCircleBound.top = ((float) getPaddingTop()) + mBarWidth;
        mCircleBound.right = ((float) (getWidth() - getPaddingRight())) - mBarWidth;
        mCircleBound.bottom = ((float) (getHeight() - getPaddingBottom())) - mBarWidth;
    }

    private void setPaint() {
        if (mBarPaint == null) {
            mBarPaint = new Paint();
        }
        mBarPaint.setColor(mBarColor);
        mBarPaint.setAntiAlias(true);
        mBarPaint.setStyle(Style.STROKE);
        mBarPaint.setStrokeWidth(mBarWidth);
        mBarPaint.setStrokeJoin(Join.ROUND);
        if (mRimPaint == null) {
            mRimPaint = new Paint();
        }
        mRimPaint.setColor(mRimColor);
        mRimPaint.setAntiAlias(true);
        mRimPaint.setStyle(Style.STROKE);
        mRimPaint.setStrokeWidth(mBarWidth);
        if (mTextPaint == null) {
            mTextPaint = new Paint();
        }
        mTextPaint.setTextSize((float) mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
    }

    private int getPosByProgress(int progress, boolean isCircle) {
        int reference = isCircle ? 360 : 100;
        if (mMax <= 0) {
            return 0;
        }
        if (progress >= mMax) {
            return reference;
        }
        return (int) (((float) reference) * (((float) progress) / ((float) mMax)));
    }

    public void setProgressStatus(boolean isShowProgress) {
        mIsShowProgress = isShowProgress;
    }

    public void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != mMax) {
            mMax = max;
            if (mProgress > max) {
                mProgress = max;
            }
            postInvalidate();
        }
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > mMax) {
            progress = mMax;
        }
        if (progress != mProgress) {
            mProgress = progress;
            mBarPostition = getPosByProgress(mProgress, true);
            mPercentage = getPosByProgress(mProgress, false);
            mText = String.valueOf(mPercentage) + "%";
            postInvalidate();
        }
    }

    public void setCircleBarColor(int color) {
        if (mBarColor != color) {
            mBarColor = color;
            mBarPaint.setColor(mBarColor);
            postInvalidate();
        }
    }

    public void setCircleRimColor(int color) {
        if (mRimColor != color) {
            mRimColor = color;
            mRimPaint.setColor(mRimColor);
            postInvalidate();
        }
    }

    public void setCircleBarWidth(float width) {
        if (((double) Math.abs(mBarWidth - width)) >= 1.0E-6d) {
            if (width < 0.0f) {
                mBarWidth = 0.0f;
            } else {
                mBarWidth = width;
            }
            mBarPaint.setStrokeWidth(mBarWidth);
            mRimPaint.setStrokeWidth(mBarWidth);
            mShouldUpdateBound = true;
            postInvalidate();
        }
    }

    public int getMax() {
        return mMax < 0 ? 0 : mMax;
    }

    public int getProgress() {
        return mProgress < 0 ? 0 : mProgress;
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(CircleProgressBar.class.getName());
    }
}
