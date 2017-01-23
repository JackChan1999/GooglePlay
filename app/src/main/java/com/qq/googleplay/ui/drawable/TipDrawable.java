package com.qq.googleplay.ui.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.qq.googleplay.R;

import java.io.InputStream;

public class TipDrawable extends BitmapDrawable {
    private Context mContext;
    private int mRadius;
    private Paint mTiPaint;
    private int mTipColor;

    public TipDrawable(Context context, Resources resources, Bitmap bitmap) {
        super(resources, bitmap);
        init(context);
    }

    public TipDrawable(Context context, Resources resources, String str) {
        super(resources, str);
        init(context);
    }

    public TipDrawable(Context context, Resources resources, InputStream inputStream) {
        super(resources, inputStream);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R.styleable.TipDrawable,
                R.attr.TipDrawableStyle, 0);
        mTipColor = obtainStyledAttributes.getColor(R.styleable.TipDrawable_mcTipColor,
                mContext.getResources().getColor(R.color.mc_tip_color));
        mRadius = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TipDrawable_mcTipRadius,
                mContext.getResources().getDimensionPixelSize(R.dimen.mc_drawable_tip_radius));
        obtainStyledAttributes.recycle();
        mTiPaint = new Paint();
        mTiPaint.setAntiAlias(true);
        mTiPaint.setColor(mTipColor);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle((float) (getBounds().right + ((mRadius * 3) / 5)),
                (float) (getBounds().top + mRadius), (float) mRadius, mTiPaint);
    }

    public void setTipColor(int tipColor) {
        if (mTipColor != tipColor) {
            mTipColor = tipColor;
            mTiPaint.setColor(mTipColor);
            invalidateSelf();
        }
    }

    public void setTipRadius(int tipRadius) {
        if (mRadius != tipRadius) {
            mRadius = tipRadius;
            invalidateSelf();
        }
    }
}
