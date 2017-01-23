package com.qq.googleplay.ui.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.LruCache;

public class StrokeDrawableUtils {
    private static final int DEF_STROKE_RADIUS = 1;
    private static final int EFFECTIVE_COLOR = Color.GRAY;
    private static final int MAX_LENGTH = 1000;
    private static final int STROKE_ALPHA_VALUE = 78;
    private static final int STROKE_RECT_ALPHA_VALUE = 26;
    private static final Object syncStroke = new Object();
    private static final Object syncStrokeRect = new Object();

    private static class StrokeLruCache {
        private static BlurMaskFilter mBlurMaskFilter;
        private static final int mMaxMemory = ((int) (Runtime.getRuntime().maxMemory() / 1024));
        private static final int mCacheSize = (mMaxMemory / 8);
        private static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        private static Canvas mStrokeCanvas;
        private static Paint mStrokePaint;

        private StrokeLruCache() {
        }

        public static Bitmap getExtraAlphaBitmap(int width, int height, int hashCode, Bitmap src) {
            String imageKey = String.valueOf(hashCode) + String.valueOf(width) + String.valueOf(height);
            Bitmap bitmap = (Bitmap) mMemoryCache.get(imageKey);
            if (bitmap != null) {
                return bitmap;
            }
            BlurMaskFilter maskFilter = obtainBlurMaskFilter();
            Paint filterPaint = obtainStokePaint();
            filterPaint.setMaskFilter(maskFilter);
            Bitmap newBitmap = src.extractAlpha(filterPaint, new int[2]);
            mMemoryCache.put(imageKey, newBitmap);
            return newBitmap;
        }

        public static Paint obtainStokePaint() {
            if (mStrokePaint == null) {
                mStrokePaint = new Paint();
            }
            return mStrokePaint;
        }

        public static Canvas obtainStrokeCanvas() {
            if (mStrokeCanvas == null) {
                mStrokeCanvas = new Canvas();
            }
            return mStrokeCanvas;
        }

        public static BlurMaskFilter obtainBlurMaskFilter() {
            if (mBlurMaskFilter == null) {
                mBlurMaskFilter = new BlurMaskFilter(1.0f, Blur.OUTER);
            }
            return mBlurMaskFilter;
        }
    }

    public static Drawable createStrokeDrawable(Drawable originalDrawable, Resources resources, Boolean recycle) {
        synchronized (syncStroke) {
            Drawable newDrawable = null;
            try {
                Bitmap mapBitmap = drawable2Bitmap(originalDrawable);
                if (mapBitmap != null) {
                    int createHeight = originalDrawable.getIntrinsicHeight() + 2;
                    int createWidth = originalDrawable.getIntrinsicWidth() + 2;
                    Bitmap strokeBitmap = Bitmap.createBitmap(createWidth, createHeight, Config.ARGB_8888);
                    if (strokeBitmap != null) {
                        int hashCode = originalDrawable.hashCode();
                        strokeBitmap.eraseColor(0);
                        Canvas canvas = StrokeLruCache.obtainStrokeCanvas();
                        canvas.setBitmap(strokeBitmap);
                        Paint filterPaint = StrokeLruCache.obtainStokePaint();
                        Bitmap alphaBitmap = StrokeLruCache.getExtraAlphaBitmap(createWidth, createHeight, hashCode, mapBitmap);
                        filterPaint.reset();
                        filterPaint.setAlpha(78);
                        canvas.drawBitmap(alphaBitmap, (float) ((createWidth - alphaBitmap.getWidth()) >> 1), (float) ((createHeight - alphaBitmap.getHeight()) >> 1), filterPaint);
                        canvas.drawBitmap(mapBitmap, (float) ((createWidth - mapBitmap.getWidth()) >> 1), (float) ((createHeight - mapBitmap.getHeight()) >> 1), null);
                        Drawable newDrawable2 = new BitmapDrawable(resources, strokeBitmap);
                        try {
                            newDrawable2.setBounds(0, 0, createWidth, createHeight);
                            newDrawable = newDrawable2;
                        } catch (Throwable th) {
                            newDrawable = newDrawable2;
                            th.printStackTrace();
                        }
                    }
                }
                if (newDrawable != null) {
                    originalDrawable = newDrawable;
                }
                return originalDrawable;
            } catch (Throwable th) {
               th.printStackTrace();
                return newDrawable;
            }
        }
    }

    @Deprecated
    public static Drawable createStrokeDrawable(Drawable originalDrawable, Resources resources) {
        return createStrokeDrawable(originalDrawable, resources, Boolean.valueOf(true));
    }

    private static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable.getIntrinsicHeight() > MAX_LENGTH || drawable.getIntrinsicWidth() > MAX_LENGTH) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        if ((drawable instanceof NinePatchDrawable) || (drawable instanceof StateListDrawable) || (drawable instanceof GradientDrawable) || (drawable instanceof InsetDrawable) || (drawable instanceof LayerDrawable) || (drawable instanceof LevelListDrawable) || (drawable instanceof PaintDrawable) || (drawable instanceof PictureDrawable) || (drawable instanceof RotateDrawable) || (drawable instanceof ScaleDrawable) || (drawable instanceof ShapeDrawable) || (drawable instanceof ClipDrawable)) {
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                return null;
            }
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
            if (bitmap != null) {
                Canvas canvas = StrokeLruCache.obtainStrokeCanvas();
                canvas.setBitmap(bitmap);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        }
        return null;
    }

    public static Drawable createRectStrokeDrawable(Drawable originalDrawable, Resources res) {
        synchronized (syncStrokeRect) {
            Drawable newDrawable = null;
            int createWidth = originalDrawable.getIntrinsicWidth();
            int createHeight = originalDrawable.getIntrinsicHeight();
            Bitmap strokeBitmap = Bitmap.createBitmap(createWidth, createHeight, Config.ARGB_8888);
            if (strokeBitmap != null) {
                strokeBitmap.eraseColor(0);
                Canvas canvas = StrokeLruCache.obtainStrokeCanvas();
                canvas.setBitmap(strokeBitmap);
                originalDrawable.setBounds(0, 0, createWidth, createHeight);
                originalDrawable.draw(canvas);
                canvas.save();
                Paint strokePaint = StrokeLruCache.obtainStokePaint();
                strokePaint.setStrokeWidth(1.0f);
                strokePaint.setColor(EFFECTIVE_COLOR);
                strokePaint.setAlpha(26);
                strokePaint.setStyle(Style.STROKE);
                canvas.drawRect(1.0f, 1.0f, (float) (createWidth - 1), (float) (createHeight - 1), strokePaint);
                newDrawable = new BitmapDrawable(res, strokeBitmap);
            }
            if (newDrawable != null) {
                originalDrawable = newDrawable;
            }
        }
        return originalDrawable;
    }
}
