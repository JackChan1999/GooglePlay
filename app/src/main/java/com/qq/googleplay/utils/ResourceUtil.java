package com.qq.googleplay.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.qq.googleplay.R;
import com.qq.googleplay.ui.drawable.BlurDrawable;

import java.util.ArrayList;

public class ResourceUtil {
    private static Integer sStatusBarHeight;

    static final class AnonymousClass1 extends AnimatorListenerAdapter {
        final View view;

        AnonymousClass1(View view) {
            this.view = view;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void onAnimationCancel(Animator animator) {
            view.setBackgroundColor(0);
            view.setHasTransientState(false);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void onAnimationEnd(Animator animator) {
            view.setBackgroundColor(0);
            view.setHasTransientState(false);
        }
    }

    static final class AnonymousClass2 implements AnimatorUpdateListener {
        final  ColorMatrix colorMatrix;
        final  Drawable drawable;

        AnonymousClass2(ColorMatrix colorMatrix, Drawable drawable) {
            this.colorMatrix = colorMatrix;
            this.drawable = drawable;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            colorMatrix.set(new float[]{1.0f, 0.0f, 0.0f, 0.0f, floatValue, 0.0f, 1.0f, 0.0f, 0.0f, 
                    floatValue, 0.0f, 0.0f, 1.0f, 0.0f, floatValue, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
            drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
    }

    public static ArrayList<String> getStringArray(Context context, TypedArray a, int index) {
        TypedValue outValue = new TypedValue();
        if (!a.getValue(index, outValue)) {
            return null;
        }
        int resourceId = outValue.resourceId;
        if (resourceId == 0) {
            throw new IllegalStateException("can't find the resourceId");
        }
        TypedArray array = context.getResources().obtainTypedArray(resourceId);
        int count = array.length();
        ArrayList<String> strs = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            strs.add(array.getString(i));
        }
        array.recycle();
        return strs;
    }

    public static Drawable getSmartBarBackground(Context context) {
        if (context == null) {
            return null;
        }
        int divider = context.getResources().getColor(R.color.mc_smartbar_divider);
        int background = context.getResources().getColor(R.color.mc_smartbar_background);
        int smartbarDividerHeight = context.getResources().getDimensionPixelSize(R.dimen.mc_smartbarbar_divider_height);
        int smartbarHeight = context.getResources().getDimensionPixelSize(R.dimen.mz_action_bar_default_height_appcompat_split);
        Rect layerInset = new Rect();
        layerInset.set(0, 0, 0, smartbarHeight - smartbarDividerHeight);
        return createDrawable(background, divider, layerInset);
    }

    public static Drawable getTitleBarBackground(Context context, int dividerColor) {
        return getTitleBarBackground(context, dividerColor, true);
    }

    public static Drawable getTitleBarBackground(Context context, int dividerColor, boolean fullScreen) {
        if (context == null) {
            return null;
        }
        int statusBarHeight;
        int divider = context.getResources().getColor(R.color.mc_titlebar_background);
        Rect layerInset = new Rect();
        int titlebarDividerHeight = context.getResources().getDimensionPixelSize(R.dimen.mc_titlebar_divider_height);
        int actionBarHeight = getActionBarHeight(context);
        if (fullScreen) {
            statusBarHeight = getStatusBarHeight(context);
        } else {
            statusBarHeight = 0;
        }
        layerInset.set(0, (statusBarHeight + actionBarHeight) - titlebarDividerHeight, 0, 0);
        return createDrawable(divider, dividerColor, layerInset);
    }

    private static Drawable createDrawable(int filtColor, int divider, Rect layerInset) {
        ColorDrawable dividerDrawable = new ColorDrawable(divider);
        BlurDrawable blurDrawable = new BlurDrawable();
        blurDrawable.setColorFilter(filtColor, BlurDrawable.DEFAULT_BLUR_COLOR_MODE);
        LayerDrawable ld = new LayerDrawable(new Drawable[]{blurDrawable, dividerDrawable});
        ld.setLayerInset(1, layerInset.left, layerInset.top, layerInset.right, layerInset.bottom);
        return ld;
    }

    public static Drawable createBlurDrawable(Drawable drawable, float level, int color) {
        BlurDrawable blurDrawable = new BlurDrawable();
        if (level >= 0.0f && level <= 1.0f) {
            blurDrawable.setBlurLevel(level);
        }
        return new LayerDrawable(new Drawable[]{blurDrawable, drawable});
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return context.getResources().getDimensionPixelSize(R.dimen.mz_action_bar_default_height_appcompat);
    }

    public static int getStatusBarHeight(Context context) {
        try {
            if (sStatusBarHeight == null) {
                Class cls = Class.forName("com.android.internal.R$dimen");
                sStatusBarHeight = Integer.valueOf(context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString())));
            }
        } catch (Throwable e) {
            Log.e("ResurceUtils", "get status bar height fail", e);
            sStatusBarHeight = Integer.valueOf(context.getResources().getDimensionPixelSize(R.dimen.status_bar_height));
        }
        return sStatusBarHeight.intValue();
    }

    public static int getGradualColor(int startcolor, int endcolor, float offset, int direction) {
        int gradualRed;
        int gradualGreen;
        int gradualBlue;
        int gradualAlpha;
        
        int startRed = Color.red(startcolor);
        int startGreen = Color.green(startcolor);
        int startBlue = Color.blue(startcolor);
        int startAlpha = Color.alpha(startcolor);
        
        int endRed = Color.red(endcolor);
        int endGreen = Color.green(endcolor);
        int endBlue = Color.blue(endcolor);
        int endAlpha = Color.alpha(endcolor);
        
        if (direction < 0) {
            gradualRed = Math.round(((float) endRed) - (((float) (endRed - startRed)) * offset));
            gradualGreen = Math.round(((float) endGreen) - (((float) (endGreen - startGreen)) * offset));
            gradualBlue = Math.round(((float) endBlue) - (((float) (endBlue - startBlue)) * offset));
            gradualAlpha = Math.round(((float) endAlpha) - (((float) (endAlpha - startAlpha)) * offset));
        } else {
            gradualRed = Math.round(((float) startRed) + (((float) (endRed - startRed)) * offset));
            gradualGreen = Math.round(((float) startGreen) + (((float) (endGreen - startGreen)) * offset));
            gradualBlue = Math.round(((float) startBlue) + (((float) (endBlue - startBlue)) * offset));
            gradualAlpha = Math.round(((float) startAlpha) + (((float) (endAlpha - startAlpha)) * offset));
        }
        return Color.argb(gradualAlpha, gradualRed, gradualGreen, gradualBlue);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static ValueAnimator getBackgroundAnimation(View view, int startColor, int fianlColor) {
        int alpha = Color.alpha(startColor);
        int sColor = startColor;
        if (alpha == 255 || alpha == 0) {
            startColor = Color.argb(20, Color.red(startColor), Color.green(startColor), Color.blue(startColor));
        }
        view.setHasTransientState(true);
        view.setBackgroundColor(startColor);
        ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", new int[]{startColor, fianlColor});
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(1000);
        colorAnim.setStartDelay(700);
        colorAnim.addListener(new AnonymousClass1(view));
        return colorAnim;
    }

    public static void startBrightnessAnim(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof ColorDrawable) {
            Log.i("error", "fade animation will not be useful to ColorDrawable because ColorDrawable has not implement method setColorFilter");
            return;
        }
        Rect bounds = drawable.getBounds();
        if (bounds == null || (bounds != null && bounds.isEmpty())) {
            Log.i("error", "you should setBounds for drawable before start brightness anim");
            return;
        }
        ColorMatrix colorMatrix = new ColorMatrix();
        Keyframe keyframe0 = Keyframe.ofFloat(0.0f, 0.0f);
        Keyframe keyframe1 = Keyframe.ofFloat(0.15f, 35.0f);
        Keyframe keyframe2 = Keyframe.ofFloat(1.0f, 0.0f);
        Keyframe[] keyframeArr = new Keyframe[]{keyframe0, keyframe1, keyframe2};
        ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(
                PropertyValuesHolder.ofKeyframe("", keyframeArr));
        ofPropertyValuesHolder.addUpdateListener(new AnonymousClass2(colorMatrix, drawable));
        ofPropertyValuesHolder.setDuration(550);
        ofPropertyValuesHolder.start();
    }

    @SuppressLint({"NewApi"})
    public static void actionBarHomeAsUpOnScrolling(Activity activity, int closeDrawableRes, int openDrawableRes,
                                                    boolean isOpened, int curX, int beginX, int endX) {
        if (activity != null && VERSION.SDK_INT >= 18) {
            Drawable closeDrawable = activity.getResources().getDrawable(closeDrawableRes);
            Drawable openDrawable = activity.getResources().getDrawable(openDrawableRes);
            if (closeDrawable != null && openDrawable != null) {
                if (curX == beginX) {
                    activity.getActionBar().setHomeAsUpIndicator(closeDrawableRes);
                    return;
                }
                Drawable drawable = closeDrawable;
                if (isOpened) {
                    drawable = openDrawable;
                }
                activity.getActionBar().setHomeAsUpIndicator(drawable);
                Rect rect = new Rect();
                openDrawable.getPadding(rect);
                int endLength = openDrawable.getIntrinsicWidth() - rect.right;
                int beginLength = closeDrawable.getIntrinsicWidth();
                float offset = ((float) curX) / ((float) (endX - beginX));
                int drawableWidth = (int) ((((float) (endLength - beginLength)) * offset) + ((float) beginLength));
                if (isOpened) {
                    drawableWidth += rect.right;
                }
                TypedArray a = activity.obtainStyledAttributes(R.styleable.Theme);
                int themeColor = a.getInt(R.styleable.Theme_mzThemeColor, 3319271);
                a.recycle();
                int color = getGradualColor(11053224, themeColor, offset, 1);
                drawable.setBounds(0, 0, endLength, drawable.getIntrinsicHeight());
                drawable.setColorFilter(new LightingColorFilter(0, beginLength));
            }
        }
    }

   public static Integer getThemeColor(Context context) {
        if (context.getResources().getIdentifier("mzThemeColor", "attr", context.getPackageName()) <= 0) {
            return null;
        }
        TypedArray array = context.getTheme().obtainStyledAttributes(R.styleable.Theme);
        int color = array.getColor(0, -1);
        array.recycle();
        if (color == -1) {
            return null;
        }
        return Integer.valueOf(color);
    }
}
