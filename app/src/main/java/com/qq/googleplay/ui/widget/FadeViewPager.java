package com.qq.googleplay.ui.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.Iterator;

public class FadeViewPager extends ViewPager {
    ArrayList<View> mAllChildView;
    View mCurrentView;
    ValueAnimator mAnimation;
    Interpolator mFadeInInterpolator;
    Interpolator mFadeOutInterpolator;
    int mFadeOutTime;
    int mFadeInTime;
    Boolean mIsFade = false;

    public FadeViewPager(Context context) {
        super(context);
        init();
    }

    public FadeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mFadeInTime = 80;
        mFadeOutTime = 80;
        mFadeInInterpolator = new AccelerateDecelerateInterpolator();
        mFadeOutInterpolator = new AccelerateDecelerateInterpolator();
    }

    private void setCurrentView() {
        mCurrentView = findViewById(getCurrentItem());
        mAllChildView = new ArrayList();
        if (mCurrentView != null) {
            int width = getWidth();
            int[] currentViewPosition = new int[2];
            mCurrentView.getLocationOnScreen(currentViewPosition);
            int[] childViewPosition = new int[2];
            for (int i = 0; i < getChildCount(); i++) {
                View childView = getChildAt(i);
                if (childView != null) {
                    childView.getLocationOnScreen(childViewPosition);
                    if (Math.abs(currentViewPosition[0] - childViewPosition[0]) <= (width * 3) / 2) {
                        mAllChildView.add(childView);
                    }
                    if (childView != mCurrentView) {
                        childView.setId(-1);
                    }
                }
            }
        }
    }

    public void setFadeInTime(int time) {
        mFadeInTime = time;
    }

    public void setFadeOutTime(int time) {
        mFadeOutTime = time;
    }

    public void setFadeInInterpolator(Interpolator interpolator) {
        mFadeInInterpolator = interpolator;
    }

    public void setFadeOutInterPolator(Interpolator interPolator) {
        mFadeOutInterpolator = interPolator;
    }

    public void startFadeOut() {
        setCurrentView();
        if (!mIsFade && mAllChildView != null && mAllChildView.size() > 0) {
            cancelAnimation();
            mIsFade = true;
            float currentAlpha = 1.0f;
            Iterator iterator = mAllChildView.iterator();
            while (iterator.hasNext()) {
                View childView = (View) iterator.next();
                if (mCurrentView != childView) {
                    currentAlpha = childView.getAlpha();
                    break;
                }
            }
            if (mCurrentView != null) {
                mCurrentView.setAlpha(1.0f);
            }
            mAnimation = ValueAnimator.ofFloat(currentAlpha, 0.0f);
            mAnimation.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator aValueAnimator) {
                    float alpha = (Float) aValueAnimator.getAnimatedValue();
                    Iterator it = FadeViewPager.this.mAllChildView.iterator();
                    while (it.hasNext()) {
                        View childView = (View) it.next();
                        if (FadeViewPager.this.mCurrentView != childView) {
                            childView.setAlpha(alpha);
                        }
                    }
                }
            });
            mAnimation.setDuration((long) ((int) (((float) mFadeOutTime) * currentAlpha)));
            mAnimation.setInterpolator(mFadeOutInterpolator);
            mAnimation.start();
        }
    }

    public void startFadeIn() {
        if (mIsFade && mAllChildView != null && mAllChildView.size() > 0) {
            cancelAnimation();
            mIsFade = false;
            float currentAlpha = 1.0f;
            Iterator iterator = mAllChildView.iterator();
            while (iterator.hasNext()) {
                View childView = (View) iterator.next();
                if (mCurrentView != childView) {
                    currentAlpha = childView.getAlpha();
                    break;
                }
            }
            if (mCurrentView != null) {
                mCurrentView.setAlpha(1.0f);
            }
            mAnimation = ValueAnimator.ofFloat(currentAlpha, 1.0f);
            mAnimation.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator aValueAnimator) {
                    float alpha = (Float) aValueAnimator.getAnimatedValue();
                    Iterator it = FadeViewPager.this.mAllChildView.iterator();
                    while (it.hasNext()) {
                        View childView = (View) it.next();
                        if (FadeViewPager.this.mCurrentView != childView) {
                            childView.setAlpha(alpha);
                        }
                    }
                }
            });
            mAnimation.setDuration((long) ((int) ((1.0f - currentAlpha) * ((float) mFadeInTime))));
            mAnimation.setInterpolator(mFadeInInterpolator);
            mAnimation.start();
        }
    }

    public void setShowWithOutAnimation() {
        cancelAnimation();
        if (mAllChildView != null) {
            Iterator iterator = mAllChildView.iterator();
            while (iterator.hasNext()) {
                ((View) iterator.next()).setAlpha(1.0f);
            }
        }
    }

    private void cancelAnimation() {
        if (mAnimation == null) {
            return;
        }
        if (mAnimation.isStarted() || mAnimation.isRunning()) {
            mAnimation.cancel();
        }
    }
}
