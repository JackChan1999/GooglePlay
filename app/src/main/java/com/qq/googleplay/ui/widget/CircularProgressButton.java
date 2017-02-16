package com.qq.googleplay.ui.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.ui.drawable.CircularAnimatedDrawable;
import com.qq.googleplay.ui.drawable.CircularProgressDrawable;
import com.qq.googleplay.ui.drawable.StrokeGradientDrawable;
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
public class CircularProgressButton extends Button {
    public static final int ERROR_STATE_PROGRESS = -1;
    public static final int IDLE_STATE_PROGRESS = 0;
    private StrokeGradientDrawable background;
    private CircularAnimatedDrawable mAnimatedDrawable;
    private int mColorIndicator;
    private int mColorIndicatorBackground;
    private int mColorProgress;
    private ColorStateList mCompleteColorState;
    private StateListDrawable mCompleteStateDrawable;
    private OnAnimationEndListener mCompleteStateListener = new OnAnimationEndListener() {
        public void onAnimationEnd() {
            if (CircularProgressButton.this.mIconComplete != 0) {
                CircularProgressButton.this.setText(null);
                CircularProgressButton.this.setIcon(CircularProgressButton.this.mIconComplete);
            } else {
                CircularProgressButton.this.setText(CircularProgressButton.this.mCompleteText);
            }
            CircularProgressButton.this.mMorphingInProgress = false;
            CircularProgressButton.this.setClickable(true);
            CircularProgressButton.this.setTextColor(CircularProgressButton.this
                    .mTextColorComplete);
        }
    };
    private String mCompleteText;
    private boolean mConfigurationChanged;
    private float mCornerRadius;
    private StateListDrawable mCurrentStateDrawable;
    private ColorStateList mErrorColorState;
    private StateListDrawable mErrorStateDrawable;
    private OnAnimationEndListener mErrorStateListener = new OnAnimationEndListener() {
        public void onAnimationEnd() {
            if (CircularProgressButton.this.mIconError != 0) {
                CircularProgressButton.this.setText(null);
                CircularProgressButton.this.setIcon(CircularProgressButton.this.mIconError);
            } else {
                CircularProgressButton.this.setText(CircularProgressButton.this.mErrorText);
            }
            CircularProgressButton.this.mMorphingInProgress = false;
            CircularProgressButton.this.setClickable(true);
            CircularProgressButton.this.setTextColor(CircularProgressButton.this.mTextColorError);
        }
    };
    private String mErrorText;
    private int mIconComplete;
    private int mIconError;
    private ColorStateList mIdleColorState;
    private StateListDrawable mIdleStateDrawable;
    private OnAnimationEndListener mIdleStateListener = new OnAnimationEndListener() {
        public void onAnimationEnd() {
            CircularProgressButton.this.removeIcon();
            CircularProgressButton.this.setText(CircularProgressButton.this.mIdleText);
            CircularProgressButton.this.mMorphingInProgress = false;
            CircularProgressButton.this.setClickable(true);
            CircularProgressButton.this.setTextColor(CircularProgressButton.this.mTextColorIdle);
        }
    };
    private String mIdleText;
    private boolean mIndeterminateProgressMode;
    private boolean mIsUseTransitionAnim = true;
    private int mMaxProgress;
    private MorphingAnimation mMorphingAnimation;
    private boolean mMorphingInProgress;
    private boolean mNeedInvalidateCenterIcon;
    private int mPaddingProgress;
    private int mProgress = 0;
    private Drawable mProgressCenterIcon;
    private CircularProgressDrawable mProgressDrawable;
    private StateListDrawable mProgressStateDrawable;
    private OnAnimationEndListener mProgressStateListener = new OnAnimationEndListener() {
        public void onAnimationEnd() {
            CircularProgressButton.this.mMorphingInProgress = false;
            CircularProgressButton.this.setClickable(true);
            CircularProgressButton.this.setText(null);
            CircularProgressButton.this.requestLayout();
        }
    };
    private String mProgressText;
    private boolean mShouldShowCenterIcon = false;
    private boolean mShouldUpdateBounds = false;
    private State mState;
    private ColorStateList mStrokeColorComplete;
    private ColorStateList mStrokeColorError;
    private ColorStateList mStrokeColorIdle;
    private int mStrokeWidth;
    private ColorStateList mTextColorComplete;
    private ColorStateList mTextColorError;
    private ColorStateList mTextColorIdle;

    interface OnAnimationEndListener {
        void onAnimationEnd();
    }

    class MorphingAnimation {
        public static final int DURATION_INSTANT = 1;
        public static final int DURATION_NORMAL = 300;
        private AnimatorSet mAnimSet;
        private StrokeGradientDrawable mDrawable;
        private int mDuration;
        private int mFromColor;
        private float mFromCornerRadius;
        private int mFromStrokeColor;
        private int mFromWidth;
        private OnAnimationEndListener mListener;
        private float mPadding;
        private int mToColor;
        private float mToCornerRadius;
        private int mToStrokeColor;
        private int mToWidth;
        private TextView mView;

        public MorphingAnimation(TextView textView, StrokeGradientDrawable strokeGradientDrawable) {
            mView = textView;
            mDrawable = strokeGradientDrawable;
        }

        public void setDuration(int duration) {
            mDuration = duration;
        }

        public void setListener(OnAnimationEndListener onAnimationEndListener) {
            mListener = onAnimationEndListener;
        }

        public void setFromWidth(int fromWidth) {
            mFromWidth = fromWidth;
        }

        public void setToWidth(int toWidth) {
            mToWidth = toWidth;
        }

        public void setFromColor(int fromColor) {
            mFromColor = fromColor;
        }

        public void setToColor(int toColor) {
            mToColor = toColor;
        }

        public void setFromStrokeColor(int fromStrokeColor) {
            mFromStrokeColor = fromStrokeColor;
        }

        public void setToStrokeColor(int toStrokeColor) {
            mToStrokeColor = toStrokeColor;
        }

        public void setFromCornerRadius(float fromCornerRadius) {
            mFromCornerRadius = fromCornerRadius;
        }

        public void setToCornerRadius(float toCornerRadius) {
            mToCornerRadius = toCornerRadius;
        }

        public void setPadding(float padding) {
            mPadding = padding;
        }

        public void start() {
            ValueAnimator animator = ValueAnimator.ofInt(mFromWidth, mToWidth);
            final GradientDrawable gradientDrawable = mDrawable.getGradientDrawable();
            animator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int leftOffset;
                    int rightOffset;
                    int padding;
                    Integer value = (Integer) valueAnimator.getAnimatedValue();
                    if (MorphingAnimation.this.mFromWidth > MorphingAnimation.this.mToWidth) {
                        leftOffset = (MorphingAnimation.this.mFromWidth - value.intValue()) / 2;
                        rightOffset = MorphingAnimation.this.mFromWidth - leftOffset;
                        padding = (int) (MorphingAnimation.this.mPadding * valueAnimator
                                .getAnimatedFraction());
                    } else {
                        leftOffset = (MorphingAnimation.this.mToWidth - value.intValue()) / 2;
                        rightOffset = MorphingAnimation.this.mToWidth - leftOffset;
                        padding = (int) (MorphingAnimation.this.mPadding - (MorphingAnimation.this
                                .mPadding * valueAnimator.getAnimatedFraction()));
                    }
                    gradientDrawable.setBounds(leftOffset + padding, padding, rightOffset - padding,
                            MorphingAnimation.this.mView.getHeight() - padding);
                    CircularProgressButton.this.invalidate();
                }
            });
            ObjectAnimator.ofInt(gradientDrawable, "color", mFromColor, mToColor)
                    .setEvaluator(new ArgbEvaluator());
            ObjectAnimator.ofInt(mDrawable, "strokeColor", mFromStrokeColor, mToStrokeColor)
                    .setEvaluator(new ArgbEvaluator());
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(gradientDrawable, "cornerRadius",
                    mFromCornerRadius, mToCornerRadius);
            mAnimSet = new AnimatorSet();
            mAnimSet.setInterpolator(CircularProgressButton.this.getInterpolator());
            mAnimSet.setDuration((long) mDuration);
            mAnimSet.playTogether(animator, ofFloat);
            mAnimSet.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    if (MorphingAnimation.this.mListener != null) {
                        MorphingAnimation.this.mListener.onAnimationEnd();
                    }
                }

                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }
            });
            mAnimSet.start();
        }

        public void cancelAllAnim() {
            mAnimSet.end();
            mAnimSet.removeAllListeners();
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private boolean mConfigurationChanged;
        private boolean mIndeterminateProgressMode;
        private int mProgress;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            boolean indeterminateProgressMode;
            boolean configurationChanged = true;
            mProgress = parcel.readInt();
            if (parcel.readInt() == 1) {
                indeterminateProgressMode = true;
            } else {
                indeterminateProgressMode = false;
            }
            mIndeterminateProgressMode = indeterminateProgressMode;
            if (parcel.readInt() != 1) {
                configurationChanged = false;
            }
            mConfigurationChanged = configurationChanged;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            super.writeToParcel(parcel, flags);
            int indeterminateProgressMode;
            int configurationChanged = 1;
            parcel.writeInt(mProgress);
            if (mIndeterminateProgressMode) {
                indeterminateProgressMode = 1;
            } else {
                indeterminateProgressMode = 0;
            }
            parcel.writeInt(indeterminateProgressMode);
            if (!mConfigurationChanged) {
                configurationChanged = 0;
            }
            parcel.writeInt(configurationChanged);
        }
    }

    public enum State {
        PROGRESS,
        IDLE,
        COMPLETE,
        ERROR
    }

    class StateManager {
        private boolean mIsEnabled;
        private int mProgress;

        public StateManager(CircularProgressButton circularProgressButton) {
            mIsEnabled = circularProgressButton.isEnabled();
            mProgress = circularProgressButton.getProgress();
        }

        public void saveProgress(CircularProgressButton circularProgressButton) {
            mProgress = circularProgressButton.getProgress();
        }

        public boolean isEnabled() {
            return mIsEnabled;
        }

        public int getProgress() {
            return mProgress;
        }

        public void checkState(CircularProgressButton circularProgressButton) {
            if (circularProgressButton.getProgress() != getProgress()) {
                circularProgressButton.setProgress(circularProgressButton.getProgress());
            } else if (circularProgressButton.isEnabled() != isEnabled()) {
                circularProgressButton.setEnabled(circularProgressButton.isEnabled());
            }
        }
    }

    public CircularProgressButton(Context context) {
        super(context);
        init(context, null);
    }

    public CircularProgressButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public CircularProgressButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        initAttributes(context, attributeSet);
        mMaxProgress = 100;
        mState = State.IDLE;
        setText(mIdleText);
        initIdleStateDrawable();
        initCompleteStateDrawable();
        initProgressStateDrawable();
        initErrorStateDrawable();
        mCurrentStateDrawable = mIdleStateDrawable;
        setBackgroundCompat(null);
    }

    private void initErrorStateDrawable() {
        StrokeGradientDrawable drawablePressed = createDrawable(getPressedColor(mErrorColorState),
                getPressedColor(mStrokeColorError));
        if (mErrorStateDrawable == null) {
            mErrorStateDrawable = new StateListDrawable();
            mErrorStateDrawable.setCallback(this);
        }
        mErrorStateDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed.getGradientDrawable());
        mErrorStateDrawable.addState(StateSet.WILD_CARD, background.getGradientDrawable());
        mErrorStateDrawable.setBounds(0, 0, getRight() - getLeft(), getBottom() - getTop());
    }

    private void initCompleteStateDrawable() {
        StrokeGradientDrawable drawablePressed = createDrawable(getPressedColor(mCompleteColorState),
                getPressedColor(mStrokeColorComplete));
        if (mCompleteStateDrawable == null) {
            mCompleteStateDrawable = new StateListDrawable();
            mCompleteStateDrawable.setCallback(this);
        }
        mCompleteStateDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed
                .getGradientDrawable());
        mCompleteStateDrawable.addState(StateSet.WILD_CARD, background
                .getGradientDrawable());
        mCompleteStateDrawable.setBounds(0, 0, getRight() - getLeft(), getBottom() - getTop());
    }

    private void initIdleStateDrawable() {
        int colorNormal = getNormalColor(mIdleColorState);
        int colorPressed = getPressedColor(mIdleColorState);
        int colorFocused = getFocusedColor(mIdleColorState);
        int colorDisabled = getDisabledColor(mIdleColorState);

        int strokeColorNormal = getNormalColor(mStrokeColorIdle);
        int strokeColorPressed = getPressedColor(mStrokeColorIdle);
        int strokeColorFocused = getFocusedColor(mStrokeColorIdle);
        int strokeColorDisabled = getDisabledColor(mStrokeColorIdle);

        if (background == null) {
            background = createDrawable(colorNormal, strokeColorNormal);
        }
        StrokeGradientDrawable disabledDrawable = createDrawable(colorDisabled, strokeColorDisabled);
        StrokeGradientDrawable focusedDrawable = createDrawable(colorFocused, strokeColorFocused);
        StrokeGradientDrawable pressedDrawable = createDrawable(colorPressed, strokeColorPressed);
        if (mIdleStateDrawable == null) {
            mIdleStateDrawable = new StateListDrawable();
            mIdleStateDrawable.setCallback(this);
        }
        mIdleStateDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable.getGradientDrawable());
        mIdleStateDrawable.addState(new int[]{android.R.attr.state_focused}, focusedDrawable.getGradientDrawable());
        mIdleStateDrawable.addState(new int[]{android.R.attr.state_enabled}, disabledDrawable.getGradientDrawable());
        mIdleStateDrawable.addState(StateSet.WILD_CARD, background.getGradientDrawable());
        mIdleStateDrawable.setBounds(0, 0, getRight() - getLeft(), getBottom() - getTop());
    }

    private void initProgressStateDrawable() {
        if (mProgressStateDrawable == null) {
            mProgressStateDrawable = new StateListDrawable();
            mProgressStateDrawable.setCallback(this);
        }
        mProgressStateDrawable.addState(StateSet.WILD_CARD, background.getGradientDrawable());
        int abs = (Math.abs(getWidth() - getHeight()) / 2) + mPaddingProgress;
        mProgressStateDrawable.setBounds(abs, mPaddingProgress, (getWidth() - abs) -
                mPaddingProgress, getHeight() - mPaddingProgress);
    }

    private int getNormalColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, 0);
    }

    private int getPressedColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{android.R.attr.state_pressed}, 0);
    }

    private int getFocusedColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{android.R.attr.state_focused}, 0);
    }

    private int getDisabledColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{-android.R.attr.state_enabled}, 0);
    }

    private StrokeGradientDrawable createDrawable(int color, int strokeColor) {
        GradientDrawable gradientDrawable = (GradientDrawable) getResources()
                .getDrawable(R.drawable.cir_pro_btn_background).mutate();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(mCornerRadius);
        StrokeGradientDrawable strokeGradientDrawable = new StrokeGradientDrawable(gradientDrawable);
        strokeGradientDrawable.setStrokeColor(strokeColor);
        strokeGradientDrawable.setStrokeWidth(mStrokeWidth);
        return strokeGradientDrawable;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Rect recordBackgroundBoundIfNeed = recordBackgroundBoundIfNeed();
        setBackgroundState(mIdleStateDrawable, getDrawableState());
        setBackgroundState(mCompleteStateDrawable, getDrawableState());
        setBackgroundState(mErrorStateDrawable, getDrawableState());
        setBackgroundState(mProgressStateDrawable, getDrawableState());
        restoreBackgroundBoundIfNeed(recordBackgroundBoundIfNeed);
    }

    private Rect recordBackgroundBoundIfNeed() {
        if (!mMorphingInProgress) {
            return null;
        }
        Rect rect = new Rect();
        rect.set(background.getGradientDrawable().getBounds());
        return rect;
    }

    private void restoreBackgroundBoundIfNeed(Rect rect) {
        if (mMorphingInProgress && rect != null) {
            background.getGradientDrawable().setBounds(rect);
        }
    }

    private void setBackgroundState(Drawable drawable, int[] iArr) {
        if (drawable != null) {
            drawable.setState(iArr);
        }
    }

    public void setPressed(boolean ispress) {
        if (!ispress || !mMorphingInProgress) {
            super.setPressed(ispress);
        }
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = getTypedArray(context, attributeSet, R.styleable.CircularProgressButton);
        if (typedArray != null) {
            mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable
                            .CircularProgressButton_strokeWidth, (int) getContext().getResources().getDimension(R.dimen
                            .mc_cir_progress_button_stroke_width));
            mIdleText = typedArray.getString(R.styleable.CircularProgressButton_textIdle);
            mCompleteText = typedArray.getString(R.styleable.CircularProgressButton_textComplete);
            mErrorText = typedArray.getString(R.styleable.CircularProgressButton_textError);
            mProgressText = typedArray.getString(R.styleable.CircularProgressButton_textProgress);
            mIconComplete = typedArray.getResourceId(R.styleable.CircularProgressButton_iconComplete, 0);
            mIconError = typedArray.getResourceId(R.styleable.CircularProgressButton_iconError, 0);
            mCornerRadius = typedArray.getDimension(R.styleable.CircularProgressButton_cornerRadius, 0.0f);
            mPaddingProgress = typedArray.getDimensionPixelSize(R.styleable.CircularProgressButton_paddingProgress, 0);
            int blue = getColor(R.color.mc_cir_progress_button_blue);
            int white = getColor(R.color.mc_cir_progress_button_white);
            int grey = getColor(R.color.mc_cir_progress_button_grey);
            int idleStateSelector = typedArray.getResourceId(R.styleable.CircularProgressButton_selectorIdle,
                    R.color.mc_cir_progress_button_blue);
            mIdleColorState = getResources().getColorStateList(idleStateSelector);
            mStrokeColorIdle = getResources().getColorStateList(typedArray.getResourceId(
                    R.styleable.CircularProgressButton_strokeColorIdle, idleStateSelector));
            int completeStateSelector = typedArray.getResourceId(R.styleable.CircularProgressButton_selectorComplete,
                    R.color.mc_cir_progress_button_green);
            mCompleteColorState = getResources().getColorStateList(completeStateSelector);
            mStrokeColorComplete = getResources().getColorStateList(typedArray.getResourceId(
                    R.styleable.CircularProgressButton_strokeColorComplete, completeStateSelector));
            int errorStateSelector = typedArray.getResourceId(R.styleable.CircularProgressButton_selectorError,
                    R.color.mc_cir_progress_button_red);
            mErrorColorState = getResources().getColorStateList(errorStateSelector);
            mStrokeColorError = getResources().getColorStateList(typedArray.getResourceId(
                    R.styleable.CircularProgressButton_strokeColorError, errorStateSelector));
            mColorProgress = typedArray.getColor(R.styleable.CircularProgressButton_colorProgress, white);
            mColorIndicator = typedArray.getColor(R.styleable.CircularProgressButton_colorIndicator, blue);
            mColorIndicatorBackground = typedArray.getColor(R.styleable.CircularProgressButton_colorIndicatorBackground, grey);
            mTextColorError = typedArray.getColorStateList(R.styleable.CircularProgressButton_textColor_error);
            if (mTextColorError == null) {
                mTextColorError = getTextColors();
            }
            mTextColorIdle = typedArray.getColorStateList(R.styleable
                    .CircularProgressButton_textColorIdle);
            if (mTextColorIdle == null) {
                mTextColorIdle = getTextColors();
            }
            mTextColorComplete = typedArray.getColorStateList(R.styleable
                    .CircularProgressButton_textColorComplete);
            if (mTextColorComplete == null) {
                mTextColorComplete = getTextColors();
            }
            typedArray.recycle();
        }
    }

    protected int getColor(int id) {
        return getResources().getColor(id);
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] iArr) {
        return context.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState != State.PROGRESS || mMorphingInProgress) {
            if (mAnimatedDrawable != null) {
                mAnimatedDrawable.setAllowLoading(false);
            }
        } else if (mIndeterminateProgressMode) {
            drawIndeterminateProgress(canvas);
        } else {
            drawProgress(canvas);
        }
    }

    private void drawIndeterminateProgress(Canvas canvas) {
        if (mAnimatedDrawable == null) {
            int width = (getWidth() - getHeight()) / 2;
            mAnimatedDrawable = new CircularAnimatedDrawable(mColorIndicator, (float) this
                    .mStrokeWidth);
            int i = mPaddingProgress + width;
            width = (getWidth() - width) - mPaddingProgress;
            int height = getHeight() - mPaddingProgress;
            mAnimatedDrawable.setBounds(i, mPaddingProgress, width, height);
            mAnimatedDrawable.setCallback(this);
            mAnimatedDrawable.start();
            return;
        }
        mAnimatedDrawable.setAllowLoading(true);
        mAnimatedDrawable.draw(canvas);
    }

    private void drawProgress(Canvas canvas) {
        if (mProgressDrawable == null) {
            int width = (getWidth() - getHeight()) / 2;
            mProgressDrawable = new CircularProgressDrawable(getHeight() - (this
                    .mPaddingProgress * 2), mStrokeWidth, mColorIndicator);
            width += mPaddingProgress;
            mProgressDrawable.setBounds(width, mPaddingProgress, width, this
                    .mPaddingProgress);
        }
        if (mNeedInvalidateCenterIcon) {
            mNeedInvalidateCenterIcon = false;
            mProgressDrawable.setCenterIcon(mProgressCenterIcon);
            if (mProgressCenterIcon == null) {
                mProgressDrawable.setShowCenterIcon(mShouldShowCenterIcon);
            }
        }
        mProgressDrawable.setSweepAngle((360.0f / ((float) mMaxProgress)) * ((float)
                mProgress));
        mProgressDrawable.draw(canvas);
    }

    public boolean isIndeterminateProgressMode() {
        return mIndeterminateProgressMode;
    }

    public void setIndeterminateProgressMode(boolean indeterminateProgressMode) {
        mIndeterminateProgressMode = indeterminateProgressMode;
    }

    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == mAnimatedDrawable || drawable == mProgressStateDrawable ||
                drawable == mIdleStateDrawable || drawable == mErrorStateDrawable ||
                drawable == mCompleteStateDrawable || super.verifyDrawable(drawable);
    }

    private MorphingAnimation createMorphing() {
        mMorphingInProgress = true;
        setClickable(false);
        mMorphingAnimation = new MorphingAnimation(this, background);
        mMorphingAnimation.setFromCornerRadius(mCornerRadius);
        mMorphingAnimation.setToCornerRadius(mCornerRadius);
        mMorphingAnimation.setFromWidth(getWidth());
        mMorphingAnimation.setToWidth(getWidth());
        if (mConfigurationChanged || !mIsUseTransitionAnim) {
            mMorphingAnimation.setDuration(1);
        } else {
            mMorphingAnimation.setDuration(300);
        }
        mConfigurationChanged = false;
        return mMorphingAnimation;
    }

    private MorphingAnimation createProgressMorphing(float fromCornerRadius, float toCornerRadius, int fromWidth, int toWidth) {
        mMorphingInProgress = true;
        setClickable(false);
        mMorphingAnimation = new MorphingAnimation(this, background);
        mMorphingAnimation.setFromCornerRadius(fromCornerRadius);
        mMorphingAnimation.setToCornerRadius(toCornerRadius);
        mMorphingAnimation.setPadding((float) mPaddingProgress);
        mMorphingAnimation.setFromWidth(fromWidth);
        mMorphingAnimation.setToWidth(toWidth);
        if (mConfigurationChanged || !mIsUseTransitionAnim) {
            mMorphingAnimation.setDuration(1);
        } else {
            mMorphingAnimation.setDuration(300);
        }
        mConfigurationChanged = false;
        return mMorphingAnimation;
    }

    private void morphToProgress() {
        setWidth(getWidth());
        setText(mProgressText);
        MorphingAnimation createProgressMorphing = createProgressMorphing(mCornerRadius,
                (float) getHeight(), getWidth(), getHeight());
        createProgressMorphing.setFromColor(getNormalColor(mIdleColorState));
        createProgressMorphing.setToColor(mColorProgress);
        createProgressMorphing.setFromStrokeColor(getNormalColor(mStrokeColorIdle));
        createProgressMorphing.setToStrokeColor(mColorIndicatorBackground);
        createProgressMorphing.setListener(mProgressStateListener);
        setState(State.PROGRESS);
        mCurrentStateDrawable = mProgressStateDrawable;
        createProgressMorphing.start();
    }

    private void morphProgressToComplete() {
        MorphingAnimation createProgressMorphing = createProgressMorphing((float) getHeight(),
                mCornerRadius, getHeight(), getWidth());
        createProgressMorphing.setFromColor(mColorProgress);
        createProgressMorphing.setFromStrokeColor(mColorIndicator);
        createProgressMorphing.setToStrokeColor(getNormalColor(mStrokeColorComplete));
        createProgressMorphing.setToColor(getNormalColor(mCompleteColorState));
        createProgressMorphing.setListener(mCompleteStateListener);
        setState(State.COMPLETE);
        mCurrentStateDrawable = mCompleteStateDrawable;
        createProgressMorphing.start();
    }

    private void morphIdleToComplete() {
        MorphingAnimation createMorphing = createMorphing();
        createMorphing.setFromColor(getNormalColor(mIdleColorState));
        createMorphing.setFromStrokeColor(getNormalColor(mStrokeColorIdle));
        createMorphing.setToColor(getNormalColor(mCompleteColorState));
        createMorphing.setToStrokeColor(getNormalColor(mStrokeColorComplete));
        createMorphing.setListener(mCompleteStateListener);
        setState(State.COMPLETE);
        mCurrentStateDrawable = mCompleteStateDrawable;
        createMorphing.start();
    }

    private void morphCompleteToIdle() {
        MorphingAnimation createMorphing = createMorphing();
        createMorphing.setFromColor(getNormalColor(mCompleteColorState));
        createMorphing.setToColor(getNormalColor(mIdleColorState));
        createMorphing.setFromStrokeColor(getNormalColor(mStrokeColorComplete));
        createMorphing.setToStrokeColor(getNormalColor(mStrokeColorIdle));
        createMorphing.setListener(mIdleStateListener);
        setState(State.IDLE);
        mCurrentStateDrawable = mIdleStateDrawable;
        createMorphing.start();
    }

    private void morphErrorToIdle() {
        MorphingAnimation createMorphing = createMorphing();
        createMorphing.setFromColor(getNormalColor(mErrorColorState));
        createMorphing.setToColor(getNormalColor(mIdleColorState));
        createMorphing.setFromStrokeColor(getNormalColor(mStrokeColorError));
        createMorphing.setToStrokeColor(getNormalColor(mStrokeColorIdle));
        createMorphing.setListener(mIdleStateListener);
        setState(State.IDLE);
        mCurrentStateDrawable = mIdleStateDrawable;
        createMorphing.start();
    }

    private void morphIdleToError() {
        MorphingAnimation createMorphing = createMorphing();
        createMorphing.setFromColor(getNormalColor(mIdleColorState));
        createMorphing.setToColor(getNormalColor(mErrorColorState));
        createMorphing.setFromStrokeColor(getNormalColor(mStrokeColorIdle));
        createMorphing.setToStrokeColor(getNormalColor(mStrokeColorError));
        createMorphing.setListener(mErrorStateListener);
        setState(State.ERROR);
        mCurrentStateDrawable = mErrorStateDrawable;
        createMorphing.start();
    }

    private void morphProgressToError() {
        MorphingAnimation createProgressMorphing = createProgressMorphing((float) getHeight(),
                mCornerRadius, getHeight(), getWidth());
        createProgressMorphing.setFromColor(mColorProgress);
        createProgressMorphing.setToColor(getNormalColor(mErrorColorState));
        createProgressMorphing.setFromStrokeColor(mColorIndicator);
        createProgressMorphing.setToStrokeColor(getNormalColor(mStrokeColorError));
        createProgressMorphing.setListener(mErrorStateListener);
        setState(State.ERROR);
        mCurrentStateDrawable = mErrorStateDrawable;
        createProgressMorphing.start();
    }

    private void morphProgressToIdle() {
        MorphingAnimation createProgressMorphing = createProgressMorphing((float) getHeight(),
                mCornerRadius, getHeight(), getWidth());
        createProgressMorphing.setFromColor(mColorProgress);
        createProgressMorphing.setToColor(getNormalColor(mIdleColorState));
        createProgressMorphing.setFromStrokeColor(mColorIndicator);
        createProgressMorphing.setToStrokeColor(getNormalColor(mStrokeColorIdle));
        createProgressMorphing.setListener(new OnAnimationEndListener() {
            public void onAnimationEnd() {
                CircularProgressButton.this.removeIcon();
                CircularProgressButton.this.setText(CircularProgressButton.this.mIdleText);
                CircularProgressButton.this.mMorphingInProgress = false;
                CircularProgressButton.this.setClickable(true);
            }
        });
        setState(State.IDLE);
        mCurrentStateDrawable = mIdleStateDrawable;
        createProgressMorphing.start();
    }

    private void setIcon(int icon) {
        Drawable drawable = getResources().getDrawable(icon);
        if (drawable != null) {
            int width = (getWidth() / 2) - (drawable.getIntrinsicWidth() / 2);
            setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
            setPadding(width, 0, 0, 0);
        }
    }

    protected void removeIcon() {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        setPadding(0, 0, 0, 0);
    }

    @SuppressLint({"NewApi"})
    public void setBackgroundCompat(Drawable drawable) {
        if (VERSION.SDK_INT >= 16) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    public void setProgress(int progress, boolean useAnim) {
        mProgress = progress;
        mIsUseTransitionAnim = useAnim;
        if (!mMorphingInProgress && getWidth() != 0) {
            if (mProgress >= mMaxProgress) {
                if (mState == State.PROGRESS) {
                    morphProgressToComplete();
                } else if (mState == State.IDLE) {
                    morphIdleToComplete();
                }
            } else if (mProgress > 0) {
                if (mState == State.IDLE || mState == State.ERROR) {
                    morphToProgress();
                } else if (mState == State.PROGRESS) {
                    invalidate();
                }
            } else if (mProgress == -1) {
                if (mState == State.PROGRESS) {
                    morphProgressToError();
                } else if (mState == State.IDLE) {
                    morphIdleToError();
                }
            } else if (mProgress != 0) {
            } else {
                if (mState == State.COMPLETE) {
                    morphCompleteToIdle();
                } else if (mState == State.PROGRESS) {
                    morphProgressToIdle();
                } else if (mState == State.ERROR) {
                    morphErrorToIdle();
                }
            }
        }
    }

    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    public int getProgress() {
        return mProgress;
    }

    public void setBackgroundColor(int color) {
        background.getGradientDrawable().setColor(color);
    }

    public void setStrokeColor(int strokeColor) {
        background.setStrokeColor(strokeColor);
    }

    public String getIdleText() {
        return mIdleText;
    }

    public String getCompleteText() {
        return mCompleteText;
    }

    public String getErrorText() {
        return mErrorText;
    }

    public void setIdleText(String idleText) {
        mIdleText = idleText;
    }

    public void setCompleteText(String completeText) {
        mCompleteText = completeText;
    }

    public void setErrorText(String errorText) {
        mErrorText = errorText;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            setState(mState, false, false);
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mProgress = mProgress;
        savedState.mIndeterminateProgressMode = mIndeterminateProgressMode;
        savedState.mConfigurationChanged = true;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(parcelable);
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            mProgress = savedState.mProgress;
            mIndeterminateProgressMode = savedState.mIndeterminateProgressMode;
            mConfigurationChanged = savedState.mConfigurationChanged;
            super.onRestoreInstanceState(savedState.getSuperState());
            setProgress(mProgress);
            return;
        }
    }

    public void setProgressCenterIcon(Drawable drawable) {
        mProgressCenterIcon = drawable;
        mNeedInvalidateCenterIcon = true;
    }

    public void setShowCenterIcon(boolean showCenterIcon) {
        mShouldShowCenterIcon = showCenterIcon;
        mNeedInvalidateCenterIcon = true;
    }

    private void ensureBackgroundBounds() {
        setBackgroundBound(State.IDLE, mIdleStateDrawable);
        setBackgroundBound(State.COMPLETE, mCompleteStateDrawable);
        setBackgroundBound(State.ERROR, mErrorStateDrawable);
        setBackgroundBound(mState, background.getGradientDrawable());
    }

    private void setBackgroundBound(State state, Drawable drawable) {
        if (drawable != null) {
            if (state == State.PROGRESS) {
                int abs = (Math.abs(getWidth() - getHeight()) / 2) + mPaddingProgress;
                drawable.setBounds(abs, mPaddingProgress, (getWidth() - abs)
                        - mPaddingProgress, getHeight() - mPaddingProgress);
                return;
            }
            drawable.setBounds(0, 0, getRight() - getLeft(), getBottom() - getTop());
        }
    }

    public void setState(State state, boolean useAnim, boolean fromUser) {
        if (state != mState) {
            mIsUseTransitionAnim = useAnim;
            if (!useAnim) {
                changeBackground(state, false);
            } else if (!mMorphingInProgress && getWidth() != 0) {
                switch (state) {
                    case COMPLETE:
                        switch (mState) {
                            case PROGRESS:
                                morphProgressToComplete();
                                return;
                            case IDLE:
                                morphIdleToComplete();
                                return;
                            default:
                                return;
                        }
                    case ERROR:
                        switch (mState) {
                            case PROGRESS:
                                morphProgressToError();
                                return;
                            case IDLE:
                                morphIdleToError();
                                return;
                            default:
                                return;
                        }
                    case PROGRESS:
                        if (mState != State.PROGRESS) {
                            morphToProgress();
                            return;
                        }
                        return;
                    case IDLE:
                        switch (mState) {
                            case COMPLETE:
                                morphCompleteToIdle();
                                return;
                            case ERROR:
                                morphErrorToIdle();
                                return;
                            case PROGRESS:
                                morphProgressToIdle();
                                return;
                            default:
                                return;
                        }
                    default:
                        return;
                }
            }
        }
    }

    private void changeBackground(State state, boolean forceUpdate) {
        if (forceUpdate || state != mState) {
            cancelAllAnimation();
            CharSequence charSequence = "";
            int backgroundColor = getNormalColor(mIdleColorState);
            int strokeColor = getNormalColor(mIdleColorState);
            ColorStateList textColor = getTextColors();
            switch (state) {
                case COMPLETE:
                    backgroundColor = getNormalColor(mCompleteColorState);
                    strokeColor = getNormalColor(mStrokeColorComplete);
                    charSequence = mCompleteText;
                    setState(State.COMPLETE);
                    textColor = mTextColorComplete;
                    mCurrentStateDrawable = mCompleteStateDrawable;
                    break;
                case ERROR:
                    backgroundColor = getNormalColor(mErrorColorState);
                    strokeColor = getNormalColor(mStrokeColorError);
                    charSequence = mErrorText;
                    setState(State.ERROR);
                    textColor = mTextColorError;
                    mCurrentStateDrawable = mErrorStateDrawable;
                    break;
                case PROGRESS:
                    backgroundColor = mColorProgress;
                    strokeColor = mColorIndicatorBackground;
                    setState(State.PROGRESS);
                    mCurrentStateDrawable = mProgressStateDrawable;
                    break;
                case IDLE:
                    backgroundColor = getNormalColor(mIdleColorState);
                    strokeColor = getNormalColor(mStrokeColorIdle);
                    charSequence = mIdleText;
                    setState(State.IDLE);
                    textColor = mTextColorIdle;
                    mCurrentStateDrawable = mIdleStateDrawable;
                    break;
            }
            GradientDrawable gradientDrawable = background.getGradientDrawable();
            if (state == State.PROGRESS) {
                int abs = (Math.abs(getWidth() - getHeight()) / 2) + mPaddingProgress;
                gradientDrawable.setBounds(abs, mPaddingProgress, (getWidth() - abs) - this
                        .mPaddingProgress, getHeight() - mPaddingProgress);
            } else {
                gradientDrawable.setBounds(0, 0, getRight() - getLeft(), getBottom() - getTop());
            }
            gradientDrawable.setColor(backgroundColor);
            background.setStrokeWidth(mStrokeWidth);
            background.setStrokeColor(strokeColor);
            setText(charSequence);
            setTextColor(textColor);
            invalidate();
        }
    }

    public void setProgressForState(int progress) {
        if (mState == State.PROGRESS) {
            mProgress = progress;
            invalidate();
        }
    }

    public State getState() {
        return mState;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAllAnimation();
    }

    public void cancelAllAnimation() {
        if (mMorphingAnimation != null) {
            mMorphingAnimation.cancelAllAnim();
        }
    }

    public void setStateColorSelector(State state, ColorStateList backgroundColorStateList, ColorStateList
            strokeColorStateList) {
        if (backgroundColorStateList != null && strokeColorStateList != null) {
            switch (state) {
                case COMPLETE:
                    mCompleteColorState = backgroundColorStateList;
                    mStrokeColorComplete = strokeColorStateList;
                    break;
                case ERROR:
                    mErrorColorState = backgroundColorStateList;
                    mStrokeColorError = strokeColorStateList;
                    break;
                case IDLE:
                    mIdleColorState = backgroundColorStateList;
                    mStrokeColorIdle = strokeColorStateList;
                    break;
            }
            background = null;
            mIdleStateDrawable = null;
            mProgressStateDrawable = null;
            mCompleteStateDrawable = null;
            mErrorStateDrawable = null;
            initIdleStateDrawable();
            initProgressStateDrawable();
            initErrorStateDrawable();
            initCompleteStateDrawable();
            if (mState == state) {
                setBackgroundFromState(state);
            }
            changeBackground(mState, true);
            drawableStateChanged();
        }
    }

    private void setBackgroundFromState(State state) {
        switch (state) {
            case COMPLETE:
                mCurrentStateDrawable = mCompleteStateDrawable;
                return;
            case ERROR:
                mCurrentStateDrawable = mErrorStateDrawable;
                return;
            case PROGRESS:
                mCurrentStateDrawable = mProgressStateDrawable;
                return;
            case IDLE:
                mCurrentStateDrawable = mIdleStateDrawable;
                return;
            default:
                return;
        }
    }

    private void setState(State state) {
        if (mState != state) {
            mState = state;
        }
    }

    public void setStateTextColor(State state, ColorStateList colorStateList) {
        switch (state) {
            case COMPLETE:
                mTextColorComplete = colorStateList;
                break;
            case ERROR:
                mTextColorError = colorStateList;
                break;
            case IDLE:
                mTextColorIdle = colorStateList;
                break;
        }
        if (mState == state) {
            invalidate();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mShouldUpdateBounds || !mMorphingInProgress) {
            mShouldUpdateBounds = false;
            ensureBackgroundBounds();
        }
        if (mMorphingInProgress && isPressed()) {
            super.draw(canvas);
            return;
        }
        if (mCurrentStateDrawable != null) {
            if ((getScrollX() | getScrollY()) == 0) {
                switch (mState) {
                    case COMPLETE:
                        drawStateDrawable(mCompleteStateDrawable, canvas);
                        break;
                    case ERROR:
                        drawStateDrawable(mErrorStateDrawable, canvas);
                        break;
                    case PROGRESS:
                        drawStateDrawable(mProgressStateDrawable, canvas);
                        break;
                    case IDLE:
                        drawStateDrawable(mIdleStateDrawable, canvas);
                        break;
                }
            }
            canvas.translate((float) getScrollX(), (float) getScrollY());
            mCurrentStateDrawable.draw(canvas);
            canvas.translate((float) (-getScrollX()), (float) (-getScrollY()));
        }
        super.draw(canvas);
    }

    private void drawStateDrawable(Drawable drawable, Canvas canvas) {
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    public void setStateText(State state, String text) {
        switch (state) {
            case COMPLETE:
                mCompleteText = text;
                break;
            case ERROR:
                mErrorText = text;
                break;
            case IDLE:
                mIdleText = text;
                break;
        }
        if (mState == state && !mMorphingInProgress) {
            setTextForState(state);
        }
    }

    private void setTextForState(State state) {
        switch (state) {
            case COMPLETE:
                setText(mCompleteText);
                return;
            case ERROR:
                setText(mErrorText);
                return;
            case IDLE:
                setText(mIdleText);
                return;
            default:
                return;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mAnimatedDrawable = null;
        mProgressDrawable = null;
        mNeedInvalidateCenterIcon = true;
        mShouldUpdateBounds = true;
    }

    private Interpolator getInterpolator() {
        if (VERSION.SDK_INT >= 21) {
            return new PathInterpolator(0.33f, 0.0f, 0.1f, 1.0f);
        }
        return PathInterpolatorCompat.create(0.33f, 0.0f, 0.1f, 1.0f);
    }

    public void setProgressIndicatorColor(int progressIndicatorColor) {
        mColorIndicator = progressIndicatorColor;
        mAnimatedDrawable = null;
        mProgressDrawable = null;
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(CircularProgressButton.class.getName());
    }
}
