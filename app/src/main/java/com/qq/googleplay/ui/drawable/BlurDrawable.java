package com.qq.googleplay.ui.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.ViewDebug.ExportedProperty;

import java.lang.reflect.Method;

public class BlurDrawable extends Drawable {
    public static final int DEFAULT_BLUR_COLOR = Color.parseColor("#26000001");
    public static final Mode DEFAULT_BLUR_COLOR_MODE = Mode.SRC_OVER;
    public static final float DEFAULT_BLUR_LEVEL = 0.9f;
    public static final Method sDrawBlurRectMethod = getDrawBlurRectMethod();
    private boolean mMutated;
    @ExportedProperty(deepExport = true, prefix = "state_")
    private BlurState mState;

    static final class BlurState extends ConstantState {
        int mAlpha = 255;
        int mBaseColor = BlurDrawable.DEFAULT_BLUR_COLOR;
        @ExportedProperty
        int mChangingConfigurations;
        float mLevel = BlurDrawable.DEFAULT_BLUR_LEVEL;
        Paint mPaint = new Paint();
        int mUseColor = BlurDrawable.getUseColor(BlurDrawable.DEFAULT_BLUR_COLOR, 255,
                BlurDrawable.DEFAULT_BLUR_LEVEL);

        BlurState(BlurState blurState) {
            if (blurState != null) {
                mLevel = blurState.mLevel;
                mPaint = new Paint(blurState.mPaint);
                mChangingConfigurations = blurState.mChangingConfigurations;
            } else if (BlurDrawable.sDrawBlurRectMethod == null) {
                mPaint.setColor(mUseColor);
            }
        }

        public Drawable newDrawable() {
            return new BlurDrawable();
        }

        public Drawable newDrawable(Resources resources) {
            return new BlurDrawable();
        }

        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }
    }

    public BlurDrawable() {
        this(null);
    }

    public BlurDrawable(float level) {
        this(null);
        setBlurLevel(level);
    }

    private BlurDrawable(BlurState state) {
        this.mState = new BlurState(state);
        if (state == null) {
            setColorFilter(DEFAULT_BLUR_COLOR, DEFAULT_BLUR_COLOR_MODE);
        }
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mState.mChangingConfigurations;
    }

    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mState = new BlurState(mState);
            mMutated = true;
        }
        return this;
    }

    @Override
    public void draw(Canvas canvas) {
        if (sDrawBlurRectMethod != null) {
            try {
                sDrawBlurRectMethod.invoke(canvas, getBounds(), Float.valueOf(mState.mLevel),
                        mState.mPaint);
                return;
            } catch (Exception e) {
                canvas.drawRect(getBounds(), mState.mPaint);
                return;
            }
        }
        canvas.drawRect(getBounds(), mState.mPaint);
    }

    public float getBlurLevel() {
        return mState.mLevel;
    }

    public void setBlurLevel(float level) {
        if (mState.mLevel != level) {
            mState.mLevel = level;
            if (updateUseColor()) {
                invalidateSelf();
            }
        }
    }

    public int getAlpha() {
        return mState.mPaint.getAlpha();
    }

    public void setAlpha(int alpha) {
        if (mState.mAlpha != alpha) {
            mState.mAlpha = alpha;
            if (updateUseColor()) {
                if (sDrawBlurRectMethod != null) {
                    mState.mPaint.setAlpha(alpha);
                }
                invalidateSelf();
            }
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        mState.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public void setColorFilter(int color, Mode mode) {
        if (sDrawBlurRectMethod != null) {
            super.setColorFilter(color, mode);
        } else if (mState.mBaseColor != color) {
            mState.mBaseColor = color;
            if (updateUseColor()) {
                invalidateSelf();
            }
        }
    }

    public void setXfermode(Xfermode xfermode) {
        mState.mPaint.setXfermode(xfermode);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        if (sDrawBlurRectMethod == null) {
            switch (mState.mPaint.getAlpha()) {
                case 0:
                    return -2;
                case 255:
                    return -1;
            }
        }
        return -3;
    }

    public ConstantState getConstantState() {
        mState.mChangingConfigurations = getChangingConfigurations();
        return mState;
    }

    private static Method getDrawBlurRectMethod() {
        try {
            return Canvas.class.getMethod("drawBlurRect", Rect.class, Float.TYPE, Paint.class);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean updateUseColor() {
        if (sDrawBlurRectMethod != null) {
            return true;
        }
        int useColor = getUseColor(mState.mBaseColor, mState.mAlpha, mState.mLevel);
        if (mState.mUseColor == useColor) {
            return false;
        }
        mState.mUseColor = useColor;
        mState.mPaint.setColor(useColor);
        return true;
    }

    private static int getUseColor(int color, int alpha, float level) {
        float a = (float) ((ViewCompat.MEASURED_STATE_MASK & color) >>> 24);
        return ((((int) (((a + ((255.0f - a) * level)) * ((float) alpha)) / 255.0f)) & 255) << 24)
                | (ViewCompat.MEASURED_SIZE_MASK & color);
    }
}
