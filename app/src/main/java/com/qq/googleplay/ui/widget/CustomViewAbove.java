package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;
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
public class CustomViewAbove extends ViewGroup {
    private static final boolean DEBUG = false;
    private static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 300;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final String TAG = "CustomViewAbove";
    private static final boolean USE_CACHE = false;
    private static int mCriticalVelocity = 650;
    private static final DecelerateInterpolator sInterpolator = new DecelerateInterpolator();
    private int mActivePointerId;
    private SlidingMenu.OnClosedListener mClosedListener;
    private View mContent;
    private int mCurItem;
    private boolean mEnabled;
    private int mFlingDistance;
    private List<View> mIgnoredViews;
    private int mInitialAbsVelocity;
    private float mInitialMotionX;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private OnPageChangeListener mOnPageChangeListener;
    private SlidingMenu.OnOpenedListener mOpenedListener;
    private int mPlusVelocitys;
    private boolean mQuickReturn;
    private float mScrollX;
    private Scroller mScroller;
    private boolean mScrolling;
    private boolean mScrollingCacheEnabled;
    protected int mTouchMode;
    private int mTouchSlop;
    private int mVelocityCount;
    protected VelocityTracker mVelocityTracker;
    private CustomViewBehind mViewBehind;
    private int mViewOffsetLeft;

    public interface OnPageChangeListener {
        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
        }

        public void onPageScrollStateChanged(int i) {
        }
    }

    public CustomViewAbove(Context context) {
        this(context, null);
    }

    public CustomViewAbove(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mActivePointerId = -1;
        this.mEnabled = true;
        this.mIgnoredViews = new ArrayList();
        this.mViewOffsetLeft = 0;
        this.mTouchMode = 1;
        this.mQuickReturn = false;
        this.mInitialAbsVelocity = -1;
        this.mPlusVelocitys = 0;
        this.mVelocityCount = 0;
        this.mScrollX = 0.0f;
        initCustomViewAbove();
    }

    void initCustomViewAbove() {
        setWillNotDraw(false);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        setInternalPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int i) {
                if (CustomViewAbove.this.mViewBehind != null) {
                    switch (i) {
                        case 0:
                        case 2:
                            CustomViewAbove.this.mViewBehind.setChildrenEnabled(true);
                            return;
                        case 1:
                            CustomViewAbove.this.mViewBehind.setChildrenEnabled(true);
                            return;
                        default:
                            return;
                    }
                }
            }
        });
        this.mFlingDistance = (int) (context.getResources().getDisplayMetrics().density * 25.0f);
    }

    private int getWindowBackgroud() {
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{16842836});
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    private void setDefaultBackgroud(View view, int i) {
        int i2;
        int i3 = 1;
        if (view.getBackground() == null) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        if (view == null) {
            i3 = 0;
        }
        if ((i3 & i2) != 0) {
            view.setBackgroundResource(i);
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(CustomViewAbove.class.getName());
    }

    public void setCurrentItem(int i) {
        setCurrentItemInternal(i, true, false);
    }

    public void setCurrentItem(int i, boolean z) {
        setCurrentItemInternal(i, z, false);
    }

    public void setCurrentItem(int i, boolean z, boolean z2) {
        setCurrentItemInternal(i, z, z2);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int i, boolean z, boolean z2) {
        setCurrentItemInternal(i, z, z2, 0);
    }

    void setCurrentItemInternal(int i, boolean z, boolean z2, int i2) {
        if (z2 || this.mCurItem != i) {
            int menuPage = this.mViewBehind.getMenuPage(i);
            boolean z3 = this.mCurItem != menuPage;
            this.mCurItem = menuPage;
            int destScrollX = getDestScrollX(this.mCurItem);
            if (z3 && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(menuPage);
            }
            if (z3 && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(menuPage);
            }
            if (z) {
                smoothScrollTo(destScrollX, 0, i2);
                return;
            }
            completeScroll();
            scrollTo(destScrollX, 0);
            return;
        }
        setScrollingCacheEnabled(false);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setOnOpenedListener(SlidingMenu.OnOpenedListener onOpenedListener) {
        this.mOpenedListener = onOpenedListener;
    }

    public void setOnClosedListener(SlidingMenu.OnClosedListener onClosedListener) {
        this.mClosedListener = onClosedListener;
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener onPageChangeListener) {
        OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = onPageChangeListener;
        return onPageChangeListener2;
    }

    public void addIgnoredView(View view) {
        if (!this.mIgnoredViews.contains(view)) {
            this.mIgnoredViews.add(view);
        }
    }

    public void removeIgnoredView(View view) {
        this.mIgnoredViews.remove(view);
    }

    public void clearIgnoredViews() {
        this.mIgnoredViews.clear();
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((float) (((double) (f - 0.5f)) * 0.4712389167638204d));
    }

    public int getDestScrollX(int i) {
        switch (i) {
            case 0:
            case 2:
                return this.mViewBehind.getMenuLeft(this.mContent, i);
            case 1:
                return this.mContent.getLeft();
            default:
                return 0;
        }
    }

    public int getLeftBound() {
        return this.mViewBehind.getAbsLeftBound(this.mContent);
    }

    public int getRightBound() {
        return this.mViewBehind.getAbsRightBound(this.mContent);
    }

    public int getContentLeft() {
        return this.mContent.getLeft() + this.mContent.getPaddingLeft();
    }

    public boolean isMenuOpen() {
        return this.mCurItem == 0 || this.mCurItem == 2;
    }

    private boolean isInIgnoredView(MotionEvent motionEvent) {
        Rect rect = new Rect();
        for (View hitRect : this.mIgnoredViews) {
            hitRect.getHitRect(rect);
            if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                return true;
            }
        }
        return false;
    }

    private boolean isInternalContentView(MotionEvent motionEvent) {
        Rect rect = new Rect();
        this.mContent.getHitRect(rect);
        if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            return true;
        }
        return false;
    }

    public int getBehindWidth() {
        if (this.mViewBehind == null) {
            return 0;
        }
        return this.mViewBehind.getBehindWidth();
    }

    public int getChildWidth(int i) {
        switch (i) {
            case 0:
                return getBehindWidth();
            case 1:
                return this.mContent.getWidth();
            default:
                return 0;
        }
    }

    public boolean isSlidingEnabled() {
        return this.mEnabled;
    }

    public void setSlidingEnabled(boolean z) {
        this.mEnabled = z;
    }

    void smoothScrollTo(int i, int i2) {
        smoothScrollTo(i, i2, 0);
    }

    void smoothScrollTo(int i, int i2, int i3) {
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int i4 = i - scrollX;
        int i5 = i2 - scrollY;
        if (i4 == 0 && i5 == 0) {
            completeScroll();
            if (isMenuOpen()) {
                if (this.mOpenedListener != null) {
                    this.mOpenedListener.onOpened();
                    return;
                }
                return;
            } else if (this.mClosedListener != null) {
                this.mClosedListener.onClosed();
                return;
            } else {
                return;
            }
        }
        setScrollingCacheEnabled(true);
        this.mScrolling = true;
        int min = Math.min((int) (((((float) Math.abs(i4)) / ((float) getBehindWidth())) + 2.0f) * 100.0f), 300);
        if (this.mViewBehind.getVisibility() != View.VISIBLE) {
            this.mViewBehind.setVisibility(View.VISIBLE);
        }
        this.mScroller.startScroll(scrollX, scrollY, i4, i5, min);
        invalidate();
    }

    public void setContent(View view) {
        if (this.mContent != null) {
            removeView(this.mContent);
        }
        this.mContent = view;
        addView(this.mContent, -1, -1);
        setDefaultBackgroud(this.mContent, getWindowBackgroud());
    }

    public View getContent() {
        return this.mContent;
    }

    public void setCustomViewBehind(CustomViewBehind customViewBehind) {
        this.mViewBehind = customViewBehind;
    }

    protected void onMeasure(int i, int i2) {
        int defaultSize = getDefaultSize(0, i);
        int defaultSize2 = getDefaultSize(0, i2);
        setMeasuredDimension(defaultSize, defaultSize2);
        this.mContent.measure(getChildMeasureSpec(i, 0, defaultSize), getChildMeasureSpec(i2, 0, defaultSize2));
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3) {
            completeScroll();
            scrollTo(getDestScrollX(this.mCurItem), getScrollY());
        }
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mContent.layout(0, 0, this.mContent.getMeasuredWidth(), this.mContent.getMeasuredHeight());
    }

    public void setAboveOffsetLeft(int i) {
        this.mViewBehind.setVisibleAlways(i > 0);
        this.mViewOffsetLeft = i;
        requestLayout();
    }

    public int getAboveOffsetLeft() {
        return this.mViewOffsetLeft;
    }

    public void computeScroll() {
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll();
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int currX = this.mScroller.getCurrX();
        int currY = this.mScroller.getCurrY();
        if (!(scrollX == currX && scrollY == currY)) {
            scrollTo(currX, currY);
            pageScrolled(currX);
        }
        invalidate();
    }

    public void pageScrolled(int i) {
        int width = getWidth();
        int i2 = i % width;
        onPageScrolled(i / width, ((float) i2) / ((float) width), i2);
    }

    protected void onPageScrolled(int i, float f, int i2) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(i, f, i2);
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(i, f, i2);
        }
    }

    private void completeScroll() {
        if (this.mScrolling) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (!(scrollX == currX && scrollY == currY)) {
                scrollTo(currX, currY);
            }
            if (isMenuOpen()) {
                if (this.mOpenedListener != null) {
                    this.mOpenedListener.onOpened();
                }
            } else if (this.mClosedListener != null) {
                this.mClosedListener.onClosed();
            }
        }
        this.mScrolling = false;
    }

    public void setTouchMode(int i) {
        this.mTouchMode = i;
    }

    public int getTouchMode() {
        return this.mTouchMode;
    }

    private boolean thisTouchAllowed(MotionEvent motionEvent) {
        int x = (int) (motionEvent.getX() + this.mScrollX);
        if (isMenuOpen()) {
            return this.mViewBehind.menuOpenTouchAllowed(this.mContent, this.mCurItem, (float) x);
        }
        switch (this.mTouchMode) {
            case 0:
                return this.mViewBehind.marginTouchAllowed(this.mContent, x);
            case 1:
                if (isInIgnoredView(motionEvent) || !isInternalContentView(motionEvent)) {
                    return false;
                }
                return true;
            case 2:
                return false;
            default:
                return false;
        }
    }

    public boolean thisSlideAllowed(float f) {
        if (isMenuOpen()) {
            return this.mViewBehind.menuOpenSlideAllowed(f);
        }
        return this.mViewBehind.menuClosedSlideAllowed(f);
    }

    private int getPointerIndex(MotionEvent motionEvent, int i) {
        int findPointerIndex = motionEvent.findPointerIndex(i);
        if (findPointerIndex == -1) {
            this.mActivePointerId = -1;
        }
        return findPointerIndex;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mEnabled) {
            if (this.mViewBehind.IsBeingDragged()) {
                this.mIsBeingDragged = false;
                return true;
            }
            int action = motionEvent.getAction() & 255;
            if (action == 3 || action == 1 || (action != 0 && this.mIsUnableToDrag)) {
                endDrag();
                return false;
            }
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            float x;
            switch (action) {
                case 0:
                    action = motionEvent.getAction();
                    if (VERSION.SDK_INT >= 8) {
                        this.mActivePointerId = action & MotionEventCompat.ACTION_POINTER_INDEX_MASK;
                        x = motionEvent.getX(this.mActivePointerId);
                        this.mInitialMotionX = x;
                        this.mLastMotionX = x;
                        this.mLastMotionY = motionEvent.getY(this.mActivePointerId);
                    } else {
                        this.mActivePointerId = action & MotionEventCompat.ACTION_POINTER_INDEX_MASK;
                        x = motionEvent.getX(this.mActivePointerId);
                        this.mInitialMotionX = x;
                        this.mLastMotionX = x;
                        this.mLastMotionY = motionEvent.getY(this.mActivePointerId);
                    }
                    if (thisTouchAllowed(motionEvent)) {
                        this.mIsBeingDragged = false;
                        this.mIsUnableToDrag = false;
                        if (isMenuOpen() && this.mViewBehind.menuTouchInQuickReturn(this.mContent, this.mCurItem, motionEvent.getX() + this.mScrollX)) {
                            this.mQuickReturn = true;
                        }
                    } else {
                        this.mIsUnableToDrag = true;
                    }
                    this.mPlusVelocitys = 0;
                    this.mVelocityCount = 0;
                    break;
                case 2:
                    if (this.mTouchMode == 0 && !isMenuOpen()) {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                        action = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                        if (action > 0) {
                            this.mPlusVelocitys = action + this.mPlusVelocitys;
                            this.mVelocityCount++;
                        }
                    }
                    action = this.mActivePointerId;
                    if (action != -1) {
                        action = getPointerIndex(motionEvent, action);
                        if (action != -1) {
                            float x2 = motionEvent.getX(action);
                            float f = x2 - this.mLastMotionX;
                            float abs = Math.abs(f);
                            x = Math.abs(motionEvent.getY(action) - this.mLastMotionY);
                            if (abs <= ((float) this.mTouchSlop) || abs <= x || !thisSlideAllowed(f)) {
                                if (x > ((float) this.mTouchSlop)) {
                                    this.mIsUnableToDrag = true;
                                    break;
                                }
                            } else if (this.mTouchMode != 0 || this.mVelocityCount <= 0 || this.mPlusVelocitys / this.mVelocityCount <= mCriticalVelocity) {
                                startDrag();
                                this.mLastMotionX = x2;
                                setScrollingCacheEnabled(true);
                                break;
                            } else {
                                return false;
                            }
                        }
                    }
                    break;
                case 6:
                    this.mPlusVelocitys = 0;
                    this.mVelocityCount = 0;
                    onSecondaryPointerUp(motionEvent);
                    break;
            }
            if (this.mIsBeingDragged || this.mQuickReturn) {
                return true;
            }
            return false;
        } else if (isMenuOpen()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mEnabled) {
            if (!this.mIsBeingDragged && !thisTouchAllowed(motionEvent)) {
                return false;
            }
            if (this.mViewBehind.IsBeingDragged()) {
                this.mIsBeingDragged = false;
                return false;
            }
            int action = motionEvent.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            float x;
            int xVelocity;
            switch (action & 255) {
                case 0:
                    completeScroll();
                    x = motionEvent.getX();
                    this.mInitialMotionX = x;
                    this.mLastMotionX = x;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.mPlusVelocitys = 0;
                    this.mVelocityCount = 0;
                    break;
                case 1:
                    this.mPlusVelocitys = 0;
                    this.mVelocityCount = 0;
                    if (!this.mIsBeingDragged) {
                        if (this.mQuickReturn && this.mViewBehind.menuTouchInQuickReturn(this.mContent, this.mCurItem, motionEvent.getX() + this.mScrollX)) {
                            setCurrentItem(1);
                            endDrag();
                            break;
                        }
                    }
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    xVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                    x = ((float) (getScrollX() - getDestScrollX(this.mCurItem))) / ((float) getBehindWidth());
                    int pointerIndex = getPointerIndex(motionEvent, this.mActivePointerId);
                    if (this.mActivePointerId != -1) {
                        setCurrentItemInternal(determineTargetPage(x, xVelocity, (int) (motionEvent.getX(pointerIndex) - this.mInitialMotionX)), true, true, xVelocity);
                    } else {
                        setCurrentItemInternal(this.mCurItem, true, true, xVelocity);
                    }
                    this.mActivePointerId = -1;
                    endDrag();
                    break;
                case 2:
                    float x2;
                    if (!this.mIsBeingDragged) {
                        if (this.mActivePointerId != -1) {
                            action = getPointerIndex(motionEvent, this.mActivePointerId);
                            if (action != -1) {
                                x2 = motionEvent.getX(action);
                                float f = x2 - this.mLastMotionX;
                                float abs = Math.abs(f);
                                x = Math.abs(motionEvent.getY(action) - this.mLastMotionY);
                                if ((abs <= ((float) this.mTouchSlop) && (!this.mQuickReturn || abs <= ((float) (this.mTouchSlop / 4)))) || abs <= x || !thisSlideAllowed(f)) {
                                    return false;
                                }
                                startDrag();
                                this.mLastMotionX = x2;
                                setScrollingCacheEnabled(true);
                            }
                        }
                    }
                    if (this.mIsBeingDragged) {
                        xVelocity = getPointerIndex(motionEvent, this.mActivePointerId);
                        if (this.mActivePointerId != -1) {
                            float x3 = motionEvent.getX(xVelocity);
                            x = this.mLastMotionX - x3;
                            this.mLastMotionX = x3;
                            x2 = ((float) getScrollX()) + x;
                            x3 = (float) getLeftBound();
                            x = (float) getRightBound();
                            if (x2 >= x3) {
                                if (x2 > x) {
                                    x3 = x;
                                } else {
                                    x3 = x2;
                                }
                            }
                            this.mLastMotionX += x3 - ((float) ((int) x3));
                            scrollTo((int) x3, getScrollY());
                            pageScrolled((int) x3);
                            break;
                        }
                    }
                    break;
                case 3:
                    if (this.mIsBeingDragged) {
                        setCurrentItemInternal(this.mCurItem, true, true);
                        this.mActivePointerId = -1;
                        endDrag();
                        break;
                    }
                    break;
                case 5:
                    xVelocity = motionEvent.getActionIndex();
                    this.mLastMotionX = motionEvent.getX(xVelocity);
                    this.mActivePointerId = motionEvent.getPointerId(xVelocity);
                    break;
                case 6:
                    onSecondaryPointerUp(motionEvent);
                    xVelocity = getPointerIndex(motionEvent, this.mActivePointerId);
                    if (this.mActivePointerId != -1) {
                        this.mLastMotionX = motionEvent.getX(xVelocity);
                        break;
                    }
                    break;
            }
            return true;
        } else if (thisTouchAllowed(motionEvent)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean IsBeingDragged() {
        return this.mIsBeingDragged;
    }

    public void scrollTo(int i, int i2) {
        super.scrollTo(i, i2);
        this.mScrollX = (float) i;
        this.mViewBehind.scrollBehindTo(this.mContent, i, i2);
    }

    int determineTargetPage(float f, int i, int i2) {
        int i3 = this.mCurItem;
        if (Math.abs(i2) <= this.mFlingDistance || Math.abs(i) <= this.mMinimumVelocity) {
            return Math.round(((float) this.mCurItem) + f);
        }
        if (i > 0 && i2 > 0) {
            return i3 - 1;
        }
        if (i >= 0 || i2 >= 0) {
            return i3;
        }
        return i3 + 1;
    }

    protected float getPercentOpen() {
        return Math.abs(this.mScrollX - ((float) this.mContent.getLeft())) / ((float) getBehindWidth());
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.mViewBehind.drawShadow(this.mContent, canvas);
        this.mViewBehind.drawFade(this.mContent, canvas, getPercentOpen());
        this.mViewBehind.drawSelector(this.mContent, canvas, getPercentOpen());
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            actionIndex = actionIndex == 0 ? 1 : 0;
            this.mLastMotionX = motionEvent.getX(actionIndex);
            this.mActivePointerId = motionEvent.getPointerId(actionIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private void startDrag() {
        this.mIsBeingDragged = true;
        this.mQuickReturn = false;
    }

    private void endDrag() {
        this.mQuickReturn = false;
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        this.mActivePointerId = -1;
        if (this.mVelocityTracker != null) {
            try {
                this.mVelocityTracker.recycle();
            } catch (IllegalStateException e) {
            }
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean z) {
        if (this.mScrollingCacheEnabled != z) {
            this.mScrollingCacheEnabled = z;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || executeKeyEvent(keyEvent);
    }

    public boolean executeKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0) {
            return false;
        }
        switch (keyEvent.getKeyCode()) {
            case 21:
                return arrowScroll(17);
            case 22:
                return arrowScroll(66);
            case 61:
                if (VERSION.SDK_INT < 11) {
                    return false;
                }
                if (keyEvent.hasNoModifiers()) {
                    return arrowScroll(2);
                }
                if (keyEvent.hasModifiers(1)) {
                    return arrowScroll(1);
                }
                return false;
            default:
                return false;
        }
    }

    public boolean arrowScroll(int i) {
        boolean pageLeft;
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        }
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i);
        if (findNextFocus == null || findNextFocus == findFocus) {
            if (i == 17 || i == 1) {
                pageLeft = pageLeft();
            } else {
                if (i == 66 || i == 2) {
                    pageLeft = pageRight();
                }
                pageLeft = false;
            }
        } else if (i == 17) {
            pageLeft = findNextFocus.requestFocus();
        } else {
            if (i == 66) {
                pageLeft = (findFocus == null || findNextFocus.getLeft() > findFocus.getLeft()) ? findNextFocus.requestFocus() : pageRight();
            }
            pageLeft = false;
        }
        if (pageLeft) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i));
        }
        return pageLeft;
    }

    boolean pageLeft() {
        if (this.mCurItem <= 0) {
            return false;
        }
        setCurrentItem(this.mCurItem - 1, true);
        return true;
    }

    boolean pageRight() {
        if (this.mCurItem >= 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    public void setCriticalVelocity(int i) {
        mCriticalVelocity = i;
    }
}
