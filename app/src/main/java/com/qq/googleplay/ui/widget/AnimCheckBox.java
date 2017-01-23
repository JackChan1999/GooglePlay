package com.qq.googleplay.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.CheckBox;

public class AnimCheckBox extends CheckBox {
    int mInitVisible;
    private CheckBoxAnimHelper checkBoxHelper;
    private UpdateListener mUpdateListener;

    private static class CheckBoxAnimHelper {
        private boolean DEBUG = false;
        private boolean mHasInit = false;
        private boolean mIsAnimation = true;
        private boolean targetActivatedState;
        private boolean targetChecekedState;
        private ObjectAnimator mAnimator1;
        private ObjectAnimator mAnimator2;
        private ValueAnimator mAnimator3;
        private AnimatorSet mAnimatorSet;
        private TimeInterpolator mInterpolator1;
        private TimeInterpolator mInterpolator2;
        private TimeInterpolator mInterpolator3;
        private TimeInterpolator mInterpolator4;
        private AnimCheckBox mTarget;

        public CheckBoxAnimHelper(AnimCheckBox checkBox) {
            mTarget = checkBox;
            mHasInit = true;
            init();
        }

        private void init() {
            if (VERSION.SDK_INT >= 21) {
                mInterpolator1 = new PathInterpolator(0.33f, 0.0f, 1.0f, 1.0f);
                mInterpolator2 = new PathInterpolator(0.0f, 0.0f, 0.01f, 1.0f);
                mInterpolator3 = new PathInterpolator(0.4f, 0.0f, 0.01f, 1.0f);
                mInterpolator4 = new PathInterpolator(0.0f, 0.0f, 0.1f, 1.0f);
            } else {
                TimeInterpolator decelerateInterpolator = new DecelerateInterpolator();
                mInterpolator4 = decelerateInterpolator;
                mInterpolator3 = decelerateInterpolator;
                mInterpolator2 = decelerateInterpolator;
                mInterpolator1 = decelerateInterpolator;
            }
            PropertyValuesHolder scaleYPVH = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f);
            mAnimator1 = ObjectAnimator.ofPropertyValuesHolder(mTarget, scaleYPVH);
            mAnimator1.setInterpolator(mInterpolator1);
            mAnimator1.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    CheckBoxAnimHelper.this.mTarget.superSetChecked(CheckBoxAnimHelper.this.targetChecekedState);
                    CheckBoxAnimHelper.this.mTarget.superSetActivated(CheckBoxAnimHelper.this.targetActivatedState);
                    if (CheckBoxAnimHelper.this.mTarget.mInitVisible != 0) {
                        if (CheckBoxAnimHelper.this.targetChecekedState) {
                            CheckBoxAnimHelper.this.mTarget.setVisibility(View.VISIBLE);
                        } else {
                            CheckBoxAnimHelper.this.mTarget.setVisibility(CheckBoxAnimHelper.this.mTarget.mInitVisible);
                        }
                    }
                    CheckBoxAnimHelper.this.mAnimator2.start();
                }
            });
            PropertyValuesHolder scaleYPVH2 = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f);
            mAnimator2 = ObjectAnimator.ofPropertyValuesHolder(mTarget, scaleYPVH2);
            mAnimator2.setInterpolator(mInterpolator2);
            mAnimator3 = ValueAnimator.ofFloat(0.0f, 1.0f);
            mAnimator3.setInterpolator(mInterpolator3);
            mAnimator3.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (CheckBoxAnimHelper.this.mTarget.mUpdateListener != null) {
                        float ff = (Float) animation.getAnimatedValue();
                        if (!CheckBoxAnimHelper.this.targetChecekedState) {
                            ff = 1.0f - ff;
                        }
                        CheckBoxAnimHelper.this.mTarget.mUpdateListener.getUpdateTransition(ff);
                    }
                }
            });
            mAnimator3.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (CheckBoxAnimHelper.this.mAnimator2.isRunning()) {
                        CheckBoxAnimHelper.this.mAnimator2.end();
                    }
                }
            });
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(mAnimator1, mAnimator3);
        }

        public void setChecked(boolean checked) {
            if (mHasInit && mIsAnimation) {
                if (DEBUG) {
                    Log.i("xx", "setChecked checked = " + checked + " targetChecekedState = " + targetChecekedState
                            + " " + " " + mAnimatorSet.isRunning() + " " + mAnimator2.isRunning());
                }
                if (checked != targetChecekedState) {
                    targetChecekedState = checked;
                    if (checked) {
                        if (mAnimatorSet.isRunning() || mAnimator2.isRunning()) {
                            mAnimatorSet.end();
                            mAnimator2.end();
                            targetChecekedState = false;
                            setChecked(checked);
                            return;
                        }
                        mAnimator1.setDuration(150);
                        mAnimator2.setDuration(230);
                        mAnimator3.setDuration(380);
                        mAnimatorSet.start();
                        return;
                    } else if (mAnimatorSet.isRunning() || mAnimator2.isRunning()) {
                        mTarget.superSetChecked(checked);
                        mAnimatorSet.end();
                        mAnimator2.end();
                        return;
                    } else {
                        mAnimator1.setDuration(0);
                        mAnimator2.setDuration(476);
                        mAnimator3.setDuration(476);
                        mAnimatorSet.start();
                        return;
                    }
                }
                return;
            }
            mTarget.superSetChecked(checked);
            targetChecekedState = checked;
        }

        public void setActivated(boolean activated) {
            targetActivatedState = activated;
            if (mHasInit && mIsAnimation) {
                if (DEBUG) {
                    Log.i("xx", "setActivated activated = " + activated + " " + mTarget.isActivated() + " "
                            + targetActivatedState + " targetChecekedState = " + targetChecekedState + " "
                            + mTarget.isChecked() + " " + mAnimatorSet.isRunning() + " " + mAnimator2.isRunning());
                }
                if (activated == mTarget.isActivated()) {
                    return;
                }
                if (!activated && !targetChecekedState && mTarget.isChecked()) {
                    return;
                }
                if (mTarget.isChecked() && targetChecekedState) {
                    mTarget.superSetActivated(activated);
                    if (!mAnimatorSet.isRunning() && !mAnimator2.isRunning()) {
                        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.0f, 1.0f);
                        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.0f, 1.0f);
                        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mTarget, scaleX, scaleY);
                        animator.setDuration(40).setInterpolator(mInterpolator4);
                        animator.start();
                        return;
                    }
                    return;
                } else if (!activated) {
                    mAnimatorSet.end();
                    mAnimator2.end();
                    mTarget.superSetActivated(activated);
                    return;
                } else {
                    return;
                }
            }
            mTarget.superSetActivated(activated);
        }

        public void setIsAnimation(boolean isAnimation) {
            mIsAnimation = isAnimation;
        }
    }

    public interface UpdateListener {
        void getUpdateTransition(float f);
    }

    public AnimCheckBox(Context context) {
        this(context, null);
    }

    public AnimCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInitVisible = getVisibility();
        setIsAnimation(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mInitVisible = getVisibility();
        setIsAnimation(true);
    }

    public void setInitVisible(int visible) {
        if (visible == View.VISIBLE || visible == View.INVISIBLE || visible == View.GONE) {
            mInitVisible = visible;
        }
    }

    public void setChecked(boolean checked) {
        if (checkBoxHelper == null) {
            super.setChecked(checked);
        } else {
            checkBoxHelper.setChecked(checked);
        }
    }

    public void setActivated(boolean activated) {
        if (checkBoxHelper == null) {
            super.setActivated(activated);
        } else {
            checkBoxHelper.setActivated(activated);
        }
    }

    public void setIsAnimation(boolean isAnimation) {
        if (checkBoxHelper == null) {
            checkBoxHelper = new CheckBoxAnimHelper(this);
        }
        checkBoxHelper.setIsAnimation(isAnimation);
    }

    public void superSetChecked(boolean checked) {
        super.setChecked(checked);
    }

    public void superSetActivated(boolean activated) {
        super.setActivated(activated);
    }

    public void setUpdateListner(UpdateListener listener) {
        mUpdateListener = listener;
    }
}
