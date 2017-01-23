package com.qq.googleplay.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.PowerManager;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class UIUtils {

    public static View inflate(Context context, int resource, ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.from(context).inflate(resource, root, attachToRoot);
    }

    public static View inflate(Context context, int resource, ViewGroup root) {
        return LayoutInflater.from(context).inflate(resource, root);
    }

    public static int getScreenWidth() {
        return ((WindowManager) CommonUtil.getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight() {
        return ((WindowManager) CommonUtil.getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getHeight();
    }

    public static int getMinScreen() {
        return Math.min(getScreenWidth(), getScreenHeight());
    }

    public static int getMaxScreen() {
        return Math.max(getScreenWidth(), getScreenHeight());
    }

    public static int getScreenHeightWithoutStatusBar(Activity activity) {
        return getScreenHeight() - getStatusBarHeight(activity);
    }

    public static int zoomWidth(int w) {
        return Math.round((((float) (w * Math.min(getScreenWidth(), getScreenHeight()))) / 320.0f) + 0.5f);
    }

    public static int zoomHeight(int h) {
        return (int) ((((float) (h * getScreenHeight())) / 480.0f) + 0.5f);
    }

    public static void zoomViewWidth(int w, View view) {
        if (view != null) {
            LayoutParams params = view.getLayoutParams();
            if (params != null) {
                params.width = zoomWidth(w);
            }
        }
    }

    public static void zoomView(int w, int h, View view) {
        if (view != null) {
            LayoutParams params = view.getLayoutParams();
            if (params != null) {
                params.width = zoomWidth(w);
                params.height = zoomWidth(h);
            }
        }
    }


    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private static boolean isTablet(Context c) {
        return (c.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static boolean hasNavigationBar(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id) && !Build.MANUFACTURER.equals("Meizu");
    }

    public static boolean isLandscape(Context activity) {
        if (activity != null && activity.getResources().getConfiguration().orientation == 2) {
            return true;
        }
        return false;
    }

    public static void fullScreen(Activity activity) {
        activity.getWindow().setFlags(1024, 1024);
    }

    public static void cancelFullScreen(Activity activity) {
        activity.getWindow().clearFlags(1024);
    }

    public static void setScreenLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void setScreenPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static int[] measure(View v) {
        v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        return new int[]{v.getMeasuredWidth(), v.getMeasuredHeight()};
    }

    public static void zoomViewHeight(int h, View view) {
        if (view != null) {
            LayoutParams params = view.getLayoutParams();
            if (params != null) {
                params.height = zoomWidth(h);
            }
        }
    }

    public static void hideInputMethod(Activity mActivity) {
        if (mActivity != null && mActivity.getCurrentFocus() != null) {
            InputMethodManager mInputMethodManager = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mInputMethodManager != null) {
                mInputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 2);
            }
        }
    }

    public static int[] measureSize(View v) {
        v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int width = v.getMeasuredWidth();
        int height = v.getMeasuredHeight();
        return new int[]{width, height};
    }

    public static int zoomRealHeight(int h) {
        return (int) ((((float) (h * Math.max(getScreenWidth(), getScreenHeight()))) / 480.0f) + 0.5f);
    }

    public static boolean isScreenOn(Context context) {
        return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }

    public static int[] getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    public static int[] getLocationInWindow(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    @TargetApi(17)
    public static Bitmap picBlur(Context context, Bitmap bitmap, Float radius) {
        Bitmap outPutBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context);
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outPutBitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        blur.setInput(allIn);
        blur.setRadius(20.0f);
        blur.forEach(allOut);
        allIn.copyTo(outPutBitmap);
        rs.destroy();
        return outPutBitmap;
    }

    public static ColorStateList createColorStateList(int normal, int pressed) {
        int[] colors = new int[]{pressed, pressed, pressed, pressed, normal, -1};
        int[][] states = new int[6][];
        states[4] = new int[]{android.R.attr.state_enabled};
        states[5] = new int[0];
        return new ColorStateList(states, colors);
    }

    public static StateListDrawable createStateDrawable(Drawable normal, Drawable pressed, Drawable focus, Drawable checked) {
        StateListDrawable sd = new StateListDrawable();
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_checked}, checked);
        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_selected}, focus);
        sd.addState(new int[0], normal);
        return sd;
    }

    public static GradientDrawable createGradientDrawable(int radious, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius((float) radious);
        return drawable;
    }

    public static void hideSoftkeyboard(Activity mActivity) {
        if (mActivity != null && mActivity.getCurrentFocus() != null) {
            InputMethodManager mInputMethodManager = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mInputMethodManager != null) {
                mInputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 2);
            }
        }
    }

    /** 获取drawable */
    public static Drawable getDrawable(int resId) {
        return CommonUtil.getResources().getDrawable(resId);
    }

    /** 获取颜色 */
    public static int getColor(int resId) {
        return CommonUtil.getResources().getColor(resId);
    }

    /** 获取颜色选择器 */
    public static ColorStateList getColorStateList(int resId) {
        return CommonUtil.getResources().getColorStateList(resId);
    }
    //判断当前的线程是不是在主线程
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == CommonUtil.getMainThreadid();
    }

}
