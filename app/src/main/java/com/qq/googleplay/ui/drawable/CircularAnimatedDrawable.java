package com.qq.googleplay.ui.drawable;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import com.qq.googleplay.ui.widget.CircleProgressBar;


public class CircularAnimatedDrawable extends Drawable implements Animatable {
    public static final String START_ANGLE_PROPERTY = "startAngle";
    public static final String SWEEP_ANGLE_PROPERTY = "sweepAngle";
    private final long LOADING_ANIM_DURATION = 1760;
    private final RectF fBounds = new RectF();
    private Animator mLoadingAnimator = null;
    private Paint mPaint;
    private boolean mRunning;
    private boolean mAllowLoading = true;
    private float mStartAngle;
    private float mSweepAngle;
    private float mBorderWidth;

    public static final float HUE_ROSE = 330.0f;

    public CircularAnimatedDrawable(int color, float borderWidth) {
        mBorderWidth = borderWidth;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(color);
        mPaint.setStrokeCap(Cap.ROUND);
        mLoadingAnimator = createLoadingAnimator();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(fBounds, mStartAngle, mSweepAngle, false, mPaint);
    }

    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return -2;
    }

    @Override
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        fBounds.left = (((float) rect.left) + (mBorderWidth / CircleProgressBar.BAR_WIDTH_DEF_DIP)) + 0.5f;
        fBounds.right = (((float) rect.right) - (mBorderWidth / CircleProgressBar.BAR_WIDTH_DEF_DIP)) - 0.5f;
        fBounds.top = (((float) rect.top) + (mBorderWidth / CircleProgressBar.BAR_WIDTH_DEF_DIP)) + 0.5f;
        fBounds.bottom = (((float) rect.bottom) - (mBorderWidth / CircleProgressBar.BAR_WIDTH_DEF_DIP)) - 0.5f;
    }

    public void start() {
        if (!isRunning()) {
            mRunning = true;
            mLoadingAnimator.start();
            invalidateSelf();
        }
    }

    public void stop() {
        if (isRunning()) {
            mRunning = false;
            mLoadingAnimator.cancel();
            invalidateSelf();
        }
    }

    public boolean isRunning() {
        return mRunning;
    }

    private Animator createLoadingAnimator() {
        Keyframe key1  = Keyframe.ofFloat(0.0f, -90.0f);
        Keyframe key2  = Keyframe.ofFloat(0.5f, HUE_ROSE);
        Keyframe key3  = Keyframe.ofFloat(1.0f, 630.0f);
        PropertyValuesHolder pvhStart = PropertyValuesHolder.ofKeyframe("startAngle", key1, key2, key3);
        PropertyValuesHolder pvhSweep = PropertyValuesHolder.ofFloat("sweepAngle", 0.0f, -120.0f, 0.0f);
        ObjectAnimator loadingAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhStart, pvhSweep);
        loadingAnim.setDuration(1760);
        loadingAnim.setInterpolator(new LinearInterpolator());
        loadingAnim.setRepeatCount(ValueAnimator.INFINITE);
        return loadingAnim;
    }

    public float getSweepAngle() {
        return mSweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        mSweepAngle = sweepAngle;
        if (mAllowLoading) {
            invalidateSelf();
        }
    }

    public float getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(float startAngle) {
        mStartAngle = startAngle;
        if (mAllowLoading) {
            invalidateSelf();
        }
    }

    public void setAllowLoading(boolean allow) {
        mAllowLoading = allow;
    }
}
