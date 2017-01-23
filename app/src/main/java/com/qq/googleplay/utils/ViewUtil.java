package com.qq.googleplay.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.qq.googleplay.R;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/20 17:27
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class ViewUtil {

    public static void setTextViewLineFlag(TextView tv, int flags) {
        tv.getPaint().setFlags(flags);
        tv.invalidate();
    }

    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * 请求View树重新布局，用于解决中层View有布局状态而导致上层View状态断裂
     */
    public static void requestLayoutParent(View view, boolean isAll) {
        ViewParent parent = view.getParent();
        while (parent != null && parent instanceof View) {
            if (!parent.isLayoutRequested()) {
                parent.requestLayout();
                if (!isAll) {
                    break;
                }
            }
            parent = parent.getParent();
        }
    }

    /**
     * 判断触点是否落在该View上
     */
    public static boolean isTouchInView(MotionEvent ev, View v) {
        int[] vLoc = new int[2];
        v.getLocationOnScreen(vLoc);
        float motionX = ev.getRawX();
        float motionY = ev.getRawY();
        return motionX >= vLoc[0] && motionX <= (vLoc[0] + v.getWidth()) && motionY >= vLoc[1] &&
                motionY <= (vLoc[1] + v.getHeight());
    }

    /**FindViewById的泛型封装，减少强转代码*/
    public static <T extends View> T findViewById(View layout, int id) {
        return (T) layout.findViewById(id);
    }

    public static void setViewAdjustToActionBar(Context context, View view, int extraTopPadding) {
        int top = context.getResources().getDimensionPixelSize(R.dimen
                .actionbar_and_statusbar_height)
                + extraTopPadding;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(view.getLeft(), top,
                    view.getRight(), view.getBottom());
        }
    }

    public static void setAppcompatActionBarBlur(Context context) {
        ActionBar actionBar = null;
        if (context instanceof AppCompatActivity) {
            actionBar = ((AppCompatActivity) context).getSupportActionBar();
        }
        actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable
                .actionbar_bg));
        actionBar.setSplitBackgroundDrawable(context.getResources().getDrawable(R.drawable
                .smartbar_background));
    }

    public static void setDecorViewBackground(Activity activity) {
        activity.getWindow().getDecorView().setBackgroundColor(activity.getResources()
                .getColor(R.color.settings_dashboard_bg));
    }

    public static void showView(View view) {
        if (view != null && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideView(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static Rect getViewBoundRect(View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + view.getWidth(), iArr[1] + view.getHeight());
    }

    public static int getViewHeight(View view) {
        if (view == null) {
            return 0;
        }
        int measuredHeight = view.getMeasuredHeight();
        if (measuredHeight != 0) {
            return measuredHeight;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.height > 0) {
            return layoutParams.height;
        }
        view.measure(0, 0);
        return view.getMeasuredHeight();
    }

    public static int getViewWidth(View view) {
        if (view == null) {
            return 0;
        }
        int measuredWidth = view.getMeasuredWidth();
        if (measuredWidth != 0) {
            return measuredWidth;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.width > 0) {
            return layoutParams.width;
        }
        view.measure(0, 0);
        return view.getMeasuredWidth();
    }

    public static void startShakeAnimation(View v) {
        ObjectAnimator.ofFloat(v, "translationX", 0.6f, 2.9f, 8.4f, 14.9f, 20.4f, 22.7f, 20.0f,
                13.4f, 4.8f, -3.8f, -10.5f, -13.1f, -11.5f, -7.5f, -2.5f, 2.8f, 6.8f,
                8.4f, 7.9f, 6.8f, 5.3f, 3.7f, 2.2f, 1.0f, 0.0f).setDuration(417).start();
    }

    public static void startShakeAnim(View view) {
        ObjectAnimator.ofFloat(view, "translationX", 0.0f, 5.6f, 17.9f, 30.2f, 35.8f,
                26.4f, 5.7f, -14.9f, -24.3f, -19.6f, -8.4f, 4.9f, 16.1f, 20.8f, 17.0f,
                8.1f, -2.6f, -11.5f, -15.3f, -13.4f, -8.7f, -2.6f, 3.4f, 8.1f, 10.0f,
                9.6f, 8.4f, 6.8f, 5.0f, 3.2f, 1.6f, 0.4f, 0.0f).setDuration(550).start();
    }

}
