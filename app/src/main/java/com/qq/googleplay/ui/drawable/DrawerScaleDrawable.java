package com.qq.googleplay.ui.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import com.qq.googleplay.R;
import com.qq.googleplay.utils.ResourceUtil;

public class DrawerScaleDrawable extends Drawable {
    private int mDefaultColor;
    private int mHeight;
    private int mPathDefaultLength;
    private int mPathDistance;
    private int mPathMinLength;
    private int mPathThickness;
    private int mThemeColor;
    private int mWidth;
    private float mProgress;
    private final Paint mPaint = new Paint();
    private final Paint mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();

    public DrawerScaleDrawable(Context context) {
        Resources resources = context.getResources();
        TypedArray a = context.obtainStyledAttributes(R.styleable.Theme);
        mThemeColor = a.getColor(R.styleable.Theme_mzThemeColor,
                resources.getColor(R.color.mc_default_theme_color));
        a.recycle();

        mDefaultColor = resources.getColor(R.color.mc_drawerscaledrawable_default_color);
        mPathThickness = resources.getDimensionPixelSize(R.dimen.mc_drawerscaledrawable_path_thickness);
        mPathDistance = resources.getDimensionPixelSize(R.dimen.mc_drawerscaledrawable_path_distance);
        mPathDefaultLength = resources.getDimensionPixelSize(R.dimen.mc_drawerscaledrawable_path_length);
        mPathMinLength = resources.getDimensionPixelSize(R.dimen.mc_drawerscaledrawable_path_min_length);
        if (mPathThickness % 2 != 0) {
            mPathThickness++;
        }
        mHeight = (mPathThickness * 3) + (mPathDistance * 2);
        mWidth = mPathDefaultLength;
        mPaint.setAntiAlias(true);
        mPaint.setColor(mDefaultColor);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeJoin(Join.ROUND);
        mPaint.setStrokeCap(Cap.SQUARE);
        mPaint.setStrokeWidth((float) mPathThickness);
        mPaintFill.setStyle(Style.FILL);
        mPaintFill.setColor(mDefaultColor);
    }

    @Override
    public void draw(Canvas canvas) {
        mPath.rewind();
        int radius = mPathThickness / 2;
        int y = radius;
        int x = radius;
        float drawLength = lerp((float) mPathDefaultLength, (float) mPathMinLength, mProgress)
                - ((float) mPathThickness);
        int drawColor = ResourceUtil.getGradualColor(mDefaultColor, mThemeColor, mProgress, 1);
        mPaintFill.setColor(drawColor);
        for (int i = 0; i < 3; i++) {
            canvas.drawCircle((float) x, (float) y, (float) radius, mPaintFill);
            canvas.drawCircle((float) (mPathThickness + drawLength), (float) y, (float) radius, mPaintFill);
            y += mPathDistance + mPathThickness;
        }
        mPaint.setColor(drawColor);
        mPath.moveTo((float) x, (float) y);
        mPath.rLineTo(drawLength, 0.0f);
        y += mPathDistance + mPathThickness;
        mPath.moveTo((float) x, (float) y);
        mPath.rLineTo(drawLength, 0.0f);
        mPath.moveTo((float) x, (float) (y + (mPathDistance + mPathThickness)));
        mPath.rLineTo(drawLength, 0.0f);
        mPath.moveTo(0.0f, 0.0f);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mPaintFill.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        mPaintFill.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return -3;
    }

    public void setProgress(float progress) {
        mProgress = progress;
        invalidateSelf();
    }

    public void setProgress(int curX, int beginX, int endX) {
        mProgress = ((float) curX) / ((float) (endX - beginX));
        invalidateSelf();
    }

    private static float lerp(float a, float b, float t) {
        return ((b - a) * t) + a;
    }

    public int getIntrinsicHeight() {
        return mHeight;
    }

    public int getIntrinsicWidth() {
        return mWidth;
    }
}
