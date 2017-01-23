package com.qq.googleplay.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.PathInterpolator;

import com.qq.googleplay.R;

public class ApplyingAnimationView extends View {
    private int bAlpha, gAlpha, oAlpha, rAlpha;
    private float baseX, baseY;
    private float cbPosition, cgPosition, coPosition, crPosition;
    private float cbRadius, cgRadius, coRadius, crRadius;
    private float halfMaxRadius;
    private float mAnimScale;
    private float maxRadius;
    private float po1, po2, po3, po4;
    private boolean mIsAnimRun;
    private boolean mStopFromUser;
    private boolean bDraw, gDraw, oDraw, rDraw;
    private final String[] positionProperty;
    private final String[] radiusProperty;
    private final String[] alphaProperty;
    private AnimatorSet animator;
    private Paint mBluePaint, mGreenPaint, mYellowPaint, mRedPaint;
    

    public ApplyingAnimationView(Context context) {
        this(context, null, 0);
    }

    public ApplyingAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplyingAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rAlpha = 255;
        bAlpha = 255;
        gAlpha = 255;
        oAlpha = 255;
        positionProperty = new String[]{"crPosition", "cbPosition", "cgPosition", "coPosition"};
        alphaProperty = new String[]{"rAlpha", "bAlpha", "gAlpha", "oAlpha"};
        radiusProperty = new String[]{"crRadius", "cbRadius", "cgRadius", "coRadius"};
        mAnimScale = 1.0f;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ApplyingAnimationView);
        mAnimScale = a.getFloat(0, mAnimScale);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        initData(context);
        mRedPaint = createCommonPaint();
        mRedPaint.setColor(Color.RED);
        mBluePaint = createCommonPaint();
        mBluePaint.setColor(Color.BLUE);
        mGreenPaint = createCommonPaint();
        mGreenPaint.setColor(Color.GREEN);
        mYellowPaint = createCommonPaint();
        mYellowPaint.setColor(Color.YELLOW);
    }

    private void initData(Context context) {
        float ratio = getDensity(context) * mAnimScale;
        maxRadius = 6.0f * ratio;
        halfMaxRadius = maxRadius * 0.5f;
        po1 = 0.0f;
        po2 = 12.3f * ratio;
        po3 = 24.0f * ratio;
        po4 = 11.0f * ratio;
        baseX = (getX() + halfMaxRadius) + (mAnimScale * CircleProgressBar.BAR_WIDTH_DEF_DIP);
        baseY = getY();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (crRadius < halfMaxRadius) {
            canvas.drawCircle(baseX + crPosition, baseY + maxRadius, crRadius, mRedPaint);
            rDraw = true;
        }
        if (cbRadius < halfMaxRadius) {
            canvas.drawCircle(baseX + cbPosition, baseY + maxRadius, cbRadius, mBluePaint);
            bDraw = true;
        }
        if (cgRadius < halfMaxRadius) {
            canvas.drawCircle(baseX + cgPosition, baseY + maxRadius, cgRadius, mGreenPaint);
            gDraw = true;
        }
        if (coRadius < halfMaxRadius) {
            canvas.drawCircle(baseX + coPosition, baseY + maxRadius, coRadius, mYellowPaint);
            oDraw = true;
        }
        if (!rDraw) {
            canvas.drawCircle(baseX + crPosition, baseY + maxRadius, crRadius, mRedPaint);
        }
        if (!bDraw) {
            canvas.drawCircle(baseX + cbPosition, baseY + maxRadius, cbRadius, mBluePaint);
        }
        if (!gDraw) {
            canvas.drawCircle(baseX + cgPosition, baseY + maxRadius, cgRadius, mGreenPaint);
        }
        if (!oDraw) {
            canvas.drawCircle(baseX + coPosition, baseY + maxRadius, coRadius, mYellowPaint);
        }
        rDraw = false;
        bDraw = false;
        gDraw = false;
        oDraw = false;
    }

    private Paint createCommonPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        return paint;
    }

    private void startAnimator() {
        if (!mIsAnimRun) {
            Animator rPosition = createPositionAnimator(0);
            Animator bPosition = createPositionAnimator(1);
            Animator gPosition = createPositionAnimator(2);
            Animator oPosition = createPositionAnimator(3);
            AnimatorSet positionSet = new AnimatorSet();
            positionSet.playTogether(rPosition, bPosition, gPosition, oPosition);

            Animator rRadiusAnim = createRadiusAnimator(0);
            Animator bRadiusAnim = createRadiusAnimator(1);
            Animator gRadiusAnim = createRadiusAnimator(2);
            Animator oRadiusAnim = createRadiusAnimator(3);
            AnimatorSet radiusSet = new AnimatorSet();
            radiusSet.playTogether(rRadiusAnim, bRadiusAnim, gRadiusAnim, oRadiusAnim);

            Animator rAlphaAnim = createAlphaAnimator(0);
            Animator bAlphaAnim = createAlphaAnimator(1);
            Animator gAlphaAnim = createAlphaAnimator(2);
            Animator oAlphaAnim = createAlphaAnimator(3);
            AnimatorSet alphaSet = new AnimatorSet();
            alphaSet.playTogether(rAlphaAnim, bAlphaAnim, gAlphaAnim, oAlphaAnim);

            animator = new AnimatorSet();
            animator.playTogether(positionSet, radiusSet, alphaSet);
            animator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (!ApplyingAnimationView.this.mStopFromUser && ApplyingAnimationView.this.animator != null) {
                        ApplyingAnimationView.this.animator.start();
                    } else if (ApplyingAnimationView.this.animator != null) {
                        ApplyingAnimationView.this.mStopFromUser = false;
                        ApplyingAnimationView.this.mIsAnimRun = false;
                    }
                }
            });
            mIsAnimRun = true;
            animator.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator createPositionAnimator(int begin) {
        ObjectAnimator[] array = new ObjectAnimator[4];
        array[0] = ObjectAnimator.ofFloat(this, positionProperty[begin], 0.0f, po2);
        array[0].setDuration(704);
        array[0].setInterpolator(new PathInterpolator(0.21f, 0.0f, 0.35f, 0.471f));
        array[1] = ObjectAnimator.ofFloat(this, positionProperty[begin], po2, po3);
        array[1].setDuration(704);
        array[1].setInterpolator(new PathInterpolator(0.24f, 0.341f, 0.41f, 1.0f));
        array[2] = ObjectAnimator.ofFloat(this, positionProperty[begin],po3, po4);
        array[2].setDuration(672);
        array[2].setInterpolator(new PathInterpolator(0.26f, 0.0f, 0.87f, 0.758f));
        array[3] = ObjectAnimator.ofFloat(this, positionProperty[begin], po4, po1);
        array[3].setDuration(736);
        array[3].setInterpolator(new PathInterpolator(0.18f, 0.434f, 0.59f, 1.0f));
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(array[begin % 4], array[(begin + 1) % 4], array[(begin + 2) % 4], array[(begin + 3) % 4]);
        return set;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator createRadiusAnimator(int begin) {
        ObjectAnimator[] array = new ObjectAnimator[4];
        array[0] = ObjectAnimator.ofFloat(this, radiusProperty[begin], halfMaxRadius, maxRadius);
        array[0].setInterpolator(new PathInterpolator(0.24f, 0.209f, 0.25f, 1.0f));
        array[0].setDuration(720);
        array[1] = ObjectAnimator.ofFloat(this, radiusProperty[begin], maxRadius, halfMaxRadius);
        array[1].setInterpolator(new PathInterpolator(0.29f, 0.0f, 0.32f, 0.631f));
        array[1].setDuration(704);
        array[2] = ObjectAnimator.ofFloat(this, radiusProperty[begin], halfMaxRadius, maxRadius * 0.25f);
        array[2].setInterpolator(new PathInterpolator(0.2f, 0.337f, 0.17f, 1.0f));
        array[2].setDuration(704);
        array[3] = ObjectAnimator.ofFloat(this, radiusProperty[begin], maxRadius * 0.25f, halfMaxRadius);
        array[3].setInterpolator(new PathInterpolator(0.19f, 0.0f, 0.37f, 0.31f));
        array[3].setDuration(688);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(array[begin % 4], array[(begin + 1) % 4], array[(begin + 2) % 4], array[(begin + 3) % 4]);
        return set;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator createAlphaAnimator(int begin) {
        ObjectAnimator[] array = new ObjectAnimator[4];
        array[0] = ObjectAnimator.ofInt(this, alphaProperty[begin], 255, 255);
        array[0].setDuration(720);
        array[1] = ObjectAnimator.ofInt(this, alphaProperty[begin], 255, 255);
        array[1].setDuration(704);
        array[2] = ObjectAnimator.ofInt(this, alphaProperty[begin], 255, 0, 0, 0);
        array[2].setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.4f, 1.0f));
        array[2].setDuration(704);
        array[3] = ObjectAnimator.ofInt(this, alphaProperty[begin], 0, 255, 255);
        array[3].setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.4f, 1.0f));
        array[3].setDuration(688);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(array[begin % 4], array[(begin + 1) % 4], array[(begin + 2) % 4], array[(begin + 3) % 4]);
        return set;
    }

    private void setRAlpha(int rAlpha) {
        rAlpha = Math.round((float) rAlpha);
        mRedPaint.setAlpha(rAlpha);
    }

    private void setBAlpha(int bAlpha) {
        bAlpha = Math.round((float) bAlpha);
        mBluePaint.setAlpha(bAlpha);
    }

    private void setGAlpha(int gAlpha) {
        gAlpha = Math.round((float) gAlpha);
        mGreenPaint.setAlpha(gAlpha);
    }

    private void setOAlpha(int oAlpha) {
        oAlpha = Math.round((float) oAlpha);
        mYellowPaint.setAlpha(oAlpha);
    }

    private void setCrRadius(float crRadius) {
        crRadius = crRadius;
    }

    private void setCbRadius(float cbRadius) {
        cbRadius = cbRadius;
    }

    private void setCgRadius(float cgRadius) {
        cgRadius = cgRadius;
    }

    private void setCoRadius(float coRadius) {
        coRadius = coRadius;
    }

    private void setCrPosition(float crPosition) {
        crPosition = crPosition;
    }

    private void setCbPosition(float cbPosition) {
        cbPosition = cbPosition;
    }

    private void setCgPosition(float cgPosition) {
        cgPosition = cgPosition;
    }

    private void setCoPosition(float coPosition) {
        coPosition = coPosition;
        invalidate();
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE) {
            stopRunAnimator();
        } else if (isShown()) {
            startAnimator();
            mStopFromUser = false;
        }
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility != View.VISIBLE) {
            stopRunAnimator();
        } else if (isShown()) {
            startAnimator();
            mStopFromUser = false;
        }
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startAnimator();
            mStopFromUser = false;
        } else if (visibility == View.INVISIBLE || visibility == View.GONE) {
            stopRunAnimator();
        }
    }

    private void stopRunAnimator() {
        if (animator != null) {
            animator.cancel();
            mStopFromUser = true;
            mIsAnimRun = false;
            animator = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = Math.round((((po3 - po1) + maxRadius) + (mAnimScale * 4.0f)) + 0.5f);
        setMeasuredDimension(resolveSizeAndState(dw + (getPaddingLeft() + getPaddingRight()),
                widthMeasureSpec, 0), resolveSizeAndState(Math.round((maxRadius * CircleProgressBar.BAR_WIDTH_DEF_DIP)
                + 0.5f) + (getPaddingTop() + getPaddingBottom()), heightMeasureSpec, 0));
    }

    public float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
