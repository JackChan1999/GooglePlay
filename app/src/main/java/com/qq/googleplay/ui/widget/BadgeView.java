package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TabWidget;
import android.widget.TextView;
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
public class BadgeView extends TextView {
    private static final int DEFAULT_BADGE_COLOR = Color.parseColor("#FFFF4C4B");
    private static final int DEFAULT_CORNER_RADIUS_DIP = 8;
    private static final int DEFAULT_LR_PADDING_DIP = 5;
    private static final int DEFAULT_MARGIN_DIP = 5;
    private static final int DEFAULT_POSITION = 2;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    public static final int POSITION_TOP_LEFT = 1;
    public static final int POSITION_TOP_RIGHT = 2;
    public static final int POSITION_BOTTOM_LEFT = 3;
    public static final int POSITION_BOTTOM_RIGHT = 4;
    public static final int POSITION_CENTER = 5;
    public static final int POSITION_CENTER_RIGHT = 6;

    private static Animation fadeIn;
    private static Animation fadeOut;
    private ShapeDrawable badgeBg;
    private int badgeColor;
    private int badgeMarginH;
    private int badgeMarginV;
    private int badgePosition;
    private int targetTabIndex;
    private Context context;
    private View target;
    private boolean isShown;

    public BadgeView(Context context) {
        this(context, (AttributeSet) null, android.R.attr.textViewStyle);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BadgeView(Context context, View target) {
        this(context, null, android.R.attr.textViewStyle, target, 0);
    }

    public BadgeView(Context context, TabWidget target, int index) {
        this(context, null, android.R.attr.textViewStyle, target, index);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, null, 0);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyle, View target, int tabIndex) {
        super(context, attrs, defStyle);
        init(context, target, tabIndex);
    }

    private void init(Context context, View target, int tabIndex) {
        context = context;
        target = target;
        targetTabIndex = tabIndex;
        badgePosition = 2;
        badgeMarginH = dipToPixels(5);
        badgeMarginV = badgeMarginH;
        badgeColor = DEFAULT_BADGE_COLOR;

        setTypeface(Typeface.DEFAULT_BOLD);
        int paddingPixels = dipToPixels(5);
        setPadding(paddingPixels, 0, paddingPixels, 0);
        setTextColor(DEFAULT_TEXT_COLOR);

        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(200);

        fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(200);
        isShown = false;

        if (target != null) {
            applyTo(target);
        } else {
            show();
        }
    }

    public void applyTo(View target) {
        LayoutParams lp = target.getLayoutParams();
        ViewParent parent = target.getParent();
        FrameLayout container = new FrameLayout(context);
        if (target instanceof TabWidget) {
            target = ((TabWidget) target).getChildTabViewAt(targetTabIndex);
            target = target;
            ((ViewGroup) target).addView(container, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            setVisibility(View.GONE);
            container.addView(this);
            return;
        }
        ViewGroup group = (ViewGroup) parent;
        int index = group.indexOfChild(target);
        group.removeView(target);
        group.addView(container, index, lp);
        container.addView(target);
        setVisibility(View.GONE);
        container.addView(this);
        group.invalidate();
    }

    public void show() {
        show(false, null);
    }

    public void show(boolean animate) {
        show(animate, fadeIn);
    }

    public void show(Animation anim) {
        show(true, anim);
    }

    public void hide() {
        hide(false, null);
    }

    public void hide(boolean animate) {
        hide(animate, fadeOut);
    }

    public void hide(Animation anim) {
        hide(true, anim);
    }

    public void toggle() {
        toggle(false, null, null);
    }

    public void toggle(boolean animate) {
        toggle(animate, fadeIn, fadeOut);
    }

    public void toggle(Animation animIn, Animation animOut) {
        toggle(true, animIn, animOut);
    }

    private void show(boolean animate, Animation anim) {
        if (getBackground() == null) {
            if (badgeBg == null) {
                badgeBg = getDefaultBackground();
            }
            setBackgroundDrawable(badgeBg);
        }
        applyLayoutParams();
        if (animate) {
            startAnimation(anim);
        }
        setVisibility(View.VISIBLE);
        isShown = true;
    }

    private void hide(boolean animate, Animation anim) {
        setVisibility(View.GONE);
        if (animate) {
            startAnimation(anim);
        }
        isShown = false;
    }

    private void toggle(boolean animate, Animation animIn, Animation animOut) {
        boolean isAnimate = true;
        if (isShown) {
            if (!animate || animOut == null) {
                isAnimate = false;
            }
            hide(isAnimate, animOut);
            return;
        }
        if (!animate || animIn == null) {
            isAnimate = false;
        }
        show(isAnimate, animIn);
    }

    public int increment(int offset) {
        int text;
        CharSequence txt = getText();
        if (txt != null) {
            try {
                text = Integer.parseInt(txt.toString());
            } catch (NumberFormatException e) {
                text = 0;
            }
        } else {
            text = 0;
        }
        text += offset;
        setText(String.valueOf(text));
        return text;
    }

    public int decrement(int offset) {
        return increment(-offset);
    }

    private ShapeDrawable getDefaultBackground() {
        int radius = dipToPixels(8);
        ShapeDrawable drawable = new ShapeDrawable(new RoundRectShape(new float[]{(float) radius, (float) radius,
                (float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius}, null, null));
        drawable.getPaint().setColor(badgeColor);
        return drawable;
    }

    private void applyLayoutParams() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        switch (badgePosition) {
            case POSITION_TOP_LEFT:
                lp.gravity = 51;
                lp.setMargins(badgeMarginH, badgeMarginV, 0, 0);
                break;
            case POSITION_TOP_RIGHT:
                lp.gravity = 53;
                lp.setMargins(0, badgeMarginV, badgeMarginH, 0);
                break;
            case POSITION_BOTTOM_LEFT:
                lp.gravity = 83;
                lp.setMargins(badgeMarginH, 0, 0, badgeMarginV);
                break;
            case POSITION_BOTTOM_RIGHT:
                lp.gravity = 85;
                lp.setMargins(0, 0, badgeMarginH, badgeMarginV);
                break;
            case POSITION_CENTER:
                lp.gravity = 17;
                lp.setMargins(0, 0, 0, 0);
                break;
            case POSITION_CENTER_RIGHT:
                lp.gravity = 21;
                lp.setMargins(0, 0, 0, 0);
                break;
        }
        setLayoutParams(lp);
    }

    public View getTarget() {
        return target;
    }

    public boolean isShown() {
        return isShown;
    }

    public int getBadgePosition() {
        return badgePosition;
    }

    public void setBadgePosition(int layoutPosition) {
        badgePosition = layoutPosition;
    }

    public int getHorizontalBadgeMargin() {
        return badgeMarginH;
    }

    public int getVerticalBadgeMargin() {
        return badgeMarginV;
    }

    public void setBadgeMargin(int badgeMargin) {
        badgeMarginH = dipToPixels(badgeMargin);
        badgeMarginV = dipToPixels(badgeMargin);
    }

    public void setBadgeMargin(int horizontal, int vertical) {
        badgeMarginH = horizontal;
        badgeMarginV = vertical;
    }

    public int getBadgeBackgroundColor() {
        return badgeColor;
    }

    public void setBadgeBackgroundColor(int badgeColor) {
        badgeColor = badgeColor;
        badgeBg = getDefaultBackground();
    }

    private int dipToPixels(int dip) {
        return (int) TypedValue.applyDimension(1, (float) dip, getResources().getDisplayMetrics());
    }
}
