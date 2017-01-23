package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qq.googleplay.R;

public class CustomViewBehind extends ViewGroup {
    private static final int INVALID_POINTER = -1;
    private static final int MARGIN_THRESHOLD = 50;
    private static final String TAG = "CustomViewBehind";
    private static int mGestureAreaWidth = 100;
    private int mActivePointerId;
    private boolean mChildrenEnabled;
    private View mContent;
    private float mFadeDegree;
    private boolean mFadeEnabled;
    private final Paint mFadePaint;
    private float mInitialMotionX;
    private boolean mIsBeingDragged;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mMarginThreshold;
    private int mMaximumVelocity;
    private int mMenuWidth;
    private int mMinimumVelocity;
    private int mMode;
    private float mScrollScale;
    private View mSecondaryContent;
    private Drawable mSecondaryShadowDrawable;
    private View mSelectedView;
    private Bitmap mSelectorDrawable;
    private boolean mSelectorEnabled;
    private Drawable mShadowDrawable;
    private int mShadowWidth;
    private int mTouchMode;
    private int mTouchSlop;
    private SlidingMenu.CanvasTransformer mTransformer;
    private VelocityTracker mVelocityTracker;
    private CustomViewAbove mViewAbove;
    private int[] mViewBehindLocation;
    private boolean mVisibleAlways;
    private boolean mWidthChanged;

    public CustomViewBehind(Context context) {
        this(context, null);
    }

    public CustomViewBehind(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTouchMode = 0;
        this.mVisibleAlways = false;
        this.mActivePointerId = -1;
        this.mViewBehindLocation = new int[2];
        this.mFadePaint = new Paint();
        this.mSelectorEnabled = true;
        this.mMarginThreshold = (int) TypedValue.applyDimension(1, 50.0f, getResources().getDisplayMetrics());
        setGestureAreaWidth(this.mMarginThreshold);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        setBackgroundColor(getResources().getColor(R.color.slidingmenu_background_light));
    }

    public void setCustomViewAbove(CustomViewAbove customViewAbove) {
        this.mViewAbove = customViewAbove;
    }

    public void setCanvasTransformer(SlidingMenu.CanvasTransformer canvasTransformer) {
        this.mTransformer = canvasTransformer;
    }

    public void setMenuWidth(int i) {
        this.mWidthChanged = this.mMenuWidth != i;
        this.mMenuWidth = i;
        requestLayout();
    }

    public int getBehindWidth() {
        return this.mContent.getWidth();
    }

    public void setContent(View view) {
        if (this.mContent != null) {
            removeView(this.mContent);
        }
        this.mContent = view;
        addView(this.mContent);
    }

    public View getContent() {
        return this.mContent;
    }

    public void setSecondaryContent(View view) {
        if (this.mSecondaryContent != null) {
            removeView(this.mSecondaryContent);
        }
        this.mSecondaryContent = view;
        addView(this.mSecondaryContent);
    }

    public View getSecondaryContent() {
        return this.mSecondaryContent;
    }

    public void setChildrenEnabled(boolean z) {
        this.mChildrenEnabled = z;
    }

    public void scrollTo(int i, int i2) {
        super.scrollTo(i, i2);
        if (this.mTransformer != null) {
            invalidate();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mViewAbove.isSlidingEnabled()) {
            return false;
        }
        if (this.mViewAbove.IsBeingDragged()) {
            this.mIsBeingDragged = false;
            return true;
        }
        int action = motionEvent.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        int i;
        switch (action & 255) {
            case 0:
                int x = (int) motionEvent.getX();
                x = (int) motionEvent.getY();
                this.mIsBeingDragged = false;
                float x2 = (float) ((int) motionEvent.getX());
                this.mInitialMotionX = x2;
                this.mLastMotionX = x2;
                this.mLastMotionY = (float) x;
                this.mActivePointerId = motionEvent.getPointerId(0);
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(motionEvent);
                break;
            case 1:
            case 3:
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                recycleVelocityTracker();
                break;
            case 2:
                i = this.mActivePointerId;
                if (i != -1) {
                    i = motionEvent.findPointerIndex(i);
                    action = (int) motionEvent.getX(i);
                    i = (int) motionEvent.getY(i);
                    int abs = (int) Math.abs(this.mLastMotionX - ((float) action));
                    int abs2 = (int) Math.abs(this.mLastMotionY - ((float) i));
                    if (abs > this.mTouchSlop && abs - abs2 > 0 && this.mViewAbove.thisSlideAllowed(((float) action) - this.mLastMotionX)) {
                        this.mIsBeingDragged = true;
                        this.mLastMotionX = (float) action;
                        this.mLastMotionY = (float) i;
                        initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement(motionEvent);
                        if (getParent() != null) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                        }
                    }
                }
                break;
            case 5:
                i = motionEvent.getActionIndex();
                this.mLastMotionX = (float) ((int) motionEvent.getX(i));
                this.mLastMotionY = (float) ((int) motionEvent.getY(i));
                this.mActivePointerId = motionEvent.getPointerId(i);
                break;
            case 6:
                onSecondaryPointerUp(motionEvent);
                this.mLastMotionX = (float) ((int) motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)));
                this.mLastMotionY = (float) ((int) motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId)));
                break;
        }
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mViewAbove.isSlidingEnabled()) {
            return super.onTouchEvent(motionEvent);
        }
        if (this.mViewAbove.IsBeingDragged()) {
            this.mIsBeingDragged = false;
            return false;
        }
        initVelocityTrackerIfNotExists();
        this.mVelocityTracker.addMovement(motionEvent);
        float x;
        int xVelocity;
        float scrollX;
        switch (motionEvent.getAction() & 255) {
            case 0:
                if (getChildCount() != 0) {
                    x = (float) ((int) motionEvent.getX());
                    this.mInitialMotionX = x;
                    this.mLastMotionX = x;
                    this.mLastMotionY = (float) ((int) motionEvent.getY());
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    break;
                }
                return false;
            case 1:
            case 3:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    xVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                    scrollX = ((float) (this.mViewAbove.getScrollX() - this.mViewAbove.getDestScrollX(this.mViewAbove.getCurrentItem()))) / ((float) getBehindWidth());
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (this.mActivePointerId != -1) {
                        this.mViewAbove.setCurrentItemInternal(this.mViewAbove.determineTargetPage(scrollX, xVelocity, (int) (motionEvent.getX(findPointerIndex) - this.mInitialMotionX)), true, true, xVelocity);
                    } else {
                        this.mViewAbove.setCurrentItemInternal(this.mViewAbove.getCurrentItem(), true, true, xVelocity);
                    }
                    this.mActivePointerId = -1;
                    this.mIsBeingDragged = false;
                    recycleVelocityTracker();
                    break;
                }
                break;
            case 2:
                int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                xVelocity = (int) motionEvent.getX(findPointerIndex2);
                int y = (int) motionEvent.getY(findPointerIndex2);
                float f = this.mLastMotionX - ((float) xVelocity);
                float f2 = this.mLastMotionY - ((float) y);
                if (!this.mIsBeingDragged && Math.abs(f) > ((float) (this.mTouchSlop / 2)) && Math.abs(f) - Math.abs(f2) > 0.0f && this.mViewAbove.thisSlideAllowed(((float) xVelocity) - this.mLastMotionX)) {
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    this.mIsBeingDragged = true;
                    f = f > 0.0f ? f - ((float) this.mTouchSlop) : f + ((float) this.mTouchSlop);
                }
                if (this.mIsBeingDragged) {
                    this.mLastMotionX = (float) xVelocity;
                    this.mLastMotionY = (float) y;
                    scrollX = ((float) this.mViewAbove.getScrollX()) + f;
                    f = (float) this.mViewAbove.getLeftBound();
                    x = (float) this.mViewAbove.getRightBound();
                    if (scrollX >= f) {
                        if (scrollX > x) {
                            f = x;
                        } else {
                            f = scrollX;
                        }
                    }
                    this.mLastMotionX += f - ((float) ((int) f));
                    this.mViewAbove.scrollTo((int) f, getScrollY());
                    this.mViewAbove.pageScrolled((int) f);
                    break;
                }
                break;
            case 6:
                onSecondaryPointerUp(motionEvent);
                break;
        }
        return true;
    }

    public boolean IsBeingDragged() {
        return this.mIsBeingDragged;
    }

    public void setVisibleAlways(boolean z) {
        this.mVisibleAlways = z;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            actionIndex = actionIndex == 0 ? 1 : 0;
            this.mLastMotionX = motionEvent.getX(actionIndex);
            this.mActivePointerId = motionEvent.getPointerId(actionIndex);
            recycleVelocityTracker();
        }
    }

    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            try {
                this.mVelocityTracker.recycle();
            } catch (IllegalStateException e) {
            }
            this.mVelocityTracker = null;
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        if (this.mTransformer != null) {
            canvas.save();
            this.mTransformer.transformCanvas(canvas, this.mViewAbove.getPercentOpen());
            super.dispatchDraw(canvas);
            canvas.restore();
            return;
        }
        super.dispatchDraw(canvas);
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = i4 - i2;
        int min = Math.min(this.mContent.getMeasuredWidth(), i3 - i);
        i5 = Math.min(this.mContent.getMeasuredHeight(), i5);
        this.mContent.layout(0, 0, min, i5);
        if (this.mSecondaryContent != null) {
            this.mSecondaryContent.layout(0, 0, min, i5);
        }
        if (this.mWidthChanged) {
            switch (this.mViewAbove.getCurrentItem()) {
                case 0:
                    this.mViewAbove.setCurrentItem(0, false, true);
                    break;
                case 2:
                    this.mViewAbove.setCurrentItem(2, false, true);
                    break;
            }
            this.mWidthChanged = false;
        }
    }

    protected void onMeasure(int i, int i2) {
        int defaultSize = getDefaultSize(0, i);
        int defaultSize2 = getDefaultSize(0, i2);
        setMeasuredDimension(defaultSize, defaultSize2);
        defaultSize = getChildMeasureSpec(i, 0, Math.min(defaultSize, this.mMenuWidth));
        defaultSize2 = getChildMeasureSpec(i2, 0, defaultSize2);
        this.mContent.measure(defaultSize, defaultSize2);
        if (this.mSecondaryContent != null) {
            this.mSecondaryContent.measure(defaultSize, defaultSize2);
        }
    }

    public void setMode(int i) {
        if (i == 0 || i == 1) {
            if (this.mContent != null) {
                this.mContent.setVisibility(View.VISIBLE);
            }
            if (this.mSecondaryContent != null) {
                this.mSecondaryContent.setVisibility(View.INVISIBLE);
            }
        }
        this.mMode = i;
    }

    public int getMode() {
        return this.mMode;
    }

    public void setScrollScale(float f) {
        this.mScrollScale = f;
    }

    public float getScrollScale() {
        return this.mScrollScale;
    }

    public void setShadowDrawable(Drawable drawable) {
        this.mShadowDrawable = drawable;
        invalidate();
    }

    public void setSecondaryShadowDrawable(Drawable drawable) {
        this.mSecondaryShadowDrawable = drawable;
        invalidate();
    }

    public void setShadowWidth(int i) {
        this.mShadowWidth = i;
        invalidate();
    }

    public void setFadeEnabled(boolean z) {
        this.mFadeEnabled = z;
    }

    public void setFadeDegree(float f) {
        if (f > 1.0f || f < 0.0f) {
            throw new IllegalStateException("The BehindFadeDegree must be between 0.0f and 1.0f");
        }
        this.mFadeDegree = f;
    }

    public int getMenuPage(int i) {
        if (i > 1) {
            i = 2;
        } else if (i < 1) {
            i = 0;
        }
        if (this.mMode == 0 && i > 1) {
            return 0;
        }
        if (this.mMode != 1 || i >= 1) {
            return i;
        }
        return 2;
    }

    public void scrollBehindTo(View view, int i, int i2) {
        int i3 = View.VISIBLE;
        if (this.mMode == 0) {
            if (i >= view.getLeft() && !this.mVisibleAlways) {
                i3 = View.INVISIBLE;
            }
            scrollTo((int) (((float) (getBehindWidth() + i)) * this.mScrollScale), i2);
        } else if (this.mMode == 1) {
            if (i <= view.getLeft() && !this.mVisibleAlways) {
                i3 = View.INVISIBLE;
            }
            scrollTo((int) (((float) (getBehindWidth() - getWidth())) + (((float) (i - getBehindWidth())) * this.mScrollScale)), i2);
        } else if (this.mMode == 2) {
            int i4;
            this.mContent.setVisibility(i >= view.getLeft() ? View.INVISIBLE : View.VISIBLE);
            View view2 = this.mSecondaryContent;
            if (i > view.getLeft() || this.mVisibleAlways) {
                i4 = View.VISIBLE;
            } else {
                i4 = View.INVISIBLE;
            }
            view2.setVisibility(i4);
            if (i == 0) {
                i3 = View.INVISIBLE;
            }
            if (i <= view.getLeft()) {
                scrollTo((int) (((float) (getBehindWidth() + i)) * this.mScrollScale), i2);
            } else {
                scrollTo((int) (((float) (getBehindWidth() - getWidth())) + (((float) (i - getBehindWidth())) * this.mScrollScale)), i2);
            }
        }
        if (i3 == View.INVISIBLE) {
            Log.v(TAG, "behind INVISIBLE");
        }
        setVisibility(i3);
        if (i3 == View.VISIBLE) {
            invalidate();
        }
    }

    public int getMenuLeft(View view, int i) {
        int i2 = 0;
        if (this.mViewAbove != null) {
            i2 = this.mViewAbove.getAboveOffsetLeft();
        }
        if (this.mMode != 0) {
            if (this.mMode != 1) {
                if (this.mMode == 2) {
                    switch (i) {
                        case 0:
                            return (i2 + view.getLeft()) - getBehindWidth();
                        case 2:
                            return (i2 + view.getLeft()) + getBehindWidth();
                        default:
                            break;
                    }
                }
            }
            switch (i) {
                case 0:
                    return i2 + view.getLeft();
                case 2:
                    return (i2 + view.getLeft()) + getBehindWidth();
                default:
                    break;
            }
        }
        switch (i) {
            case 0:
                return (i2 + view.getLeft()) - getBehindWidth();
            case 2:
                return i2 + view.getLeft();
        }
        return view.getLeft();
    }

    public int getAbsLeftBound(View view) {
        int aboveOffsetLeft;
        if (this.mViewAbove != null) {
            aboveOffsetLeft = this.mViewAbove.getAboveOffsetLeft();
        } else {
            aboveOffsetLeft = 0;
        }
        if (this.mMode == 0 || this.mMode == 2) {
            return (view.getLeft() - getBehindWidth()) + aboveOffsetLeft;
        }
        if (this.mMode == 1) {
            return view.getLeft();
        }
        return 0;
    }

    public int getAbsRightBound(View view) {
        if (this.mMode == 0) {
            return view.getLeft();
        }
        if (this.mMode == 1 || this.mMode == 2) {
            return view.getLeft() + getBehindWidth();
        }
        return 0;
    }

    public boolean marginTouchAllowed(View view, int i) {
        int left = view.getLeft();
        int right = view.getRight();
        if (this.mMode == 0) {
            if (i < left || i > left + mGestureAreaWidth) {
                return false;
            }
            return true;
        } else if (this.mMode == 1) {
            if (i > right || i < right - mGestureAreaWidth) {
                return false;
            }
            return true;
        } else if (this.mMode != 2) {
            return false;
        } else {
            if (i >= left && i <= left + mGestureAreaWidth) {
                return true;
            }
            if (i > right || i < right - mGestureAreaWidth) {
                return false;
            }
            return true;
        }
    }

    public void setTouchMode(int i) {
        this.mTouchMode = i;
    }

    public boolean menuOpenTouchAllowed(View view, int i, float f) {
        switch (this.mTouchMode) {
            case 0:
                return menuTouchInQuickReturn(view, i, f);
            case 1:
                return true;
            default:
                return false;
        }
    }

    public boolean menuTouchInQuickReturn(View view, int i, float f) {
        if (this.mMode == 0 || (this.mMode == 2 && i == 0)) {
            if (f >= ((float) view.getLeft())) {
                return true;
            }
            return false;
        } else if (this.mMode != 1 && (this.mMode != 2 || i != 2)) {
            return false;
        } else {
            if (f > ((float) view.getRight())) {
                return false;
            }
            return true;
        }
    }

    public boolean menuClosedSlideAllowed(float f) {
        if (this.mMode == 0) {
            if (f > 0.0f) {
                return true;
            }
            return false;
        } else if (this.mMode == 1) {
            if (f >= 0.0f) {
                return false;
            }
            return true;
        } else if (this.mMode != 2) {
            return false;
        } else {
            return true;
        }
    }

    public boolean menuOpenSlideAllowed(float f) {
        if (this.mMode == 0) {
            if (f < 0.0f) {
                return true;
            }
            return false;
        } else if (this.mMode == 1) {
            if (f <= 0.0f) {
                return false;
            }
            return true;
        } else if (this.mMode != 2) {
            return false;
        } else {
            return true;
        }
    }

    public void drawShadow(View view, Canvas canvas) {
        if (this.mShadowDrawable != null) {
            int left;
            if (this.mShadowWidth <= 0) {
                this.mShadowWidth = this.mShadowDrawable.getIntrinsicWidth();
            }
            if (this.mMode == 0) {
                view.getLocationInWindow(this.mViewBehindLocation);
                if (this.mViewBehindLocation[0] > 0) {
                    left = view.getLeft() + this.mShadowWidth;
                } else {
                    return;
                }
            } else if (this.mMode == 1) {
                left = view.getRight();
            } else if (this.mMode == 2) {
                if (this.mSecondaryShadowDrawable != null) {
                    left = view.getRight();
                    this.mSecondaryShadowDrawable.setBounds(left - this.mShadowWidth, 0, left, getHeight());
                    this.mSecondaryShadowDrawable.draw(canvas);
                }
                left = view.getLeft() + this.mShadowWidth;
            } else {
                left = 0;
            }
            this.mShadowDrawable.setBounds(left - this.mShadowWidth, 0, left, getHeight());
            this.mShadowDrawable.draw(canvas);
        }
    }

    public void drawFade(View view, Canvas canvas, float f) {
        int i = 0;
        if (this.mFadeEnabled) {
            int left;
            this.mFadePaint.setColor(Color.argb((int) ((this.mFadeDegree * 255.0f) * Math.abs(1.0f - f)), 0, 0, 0));
            if (this.mMode == 0) {
                left = view.getLeft() - getBehindWidth();
                i = view.getLeft();
            } else if (this.mMode == 1) {
                left = view.getRight();
                i = view.getRight() + getBehindWidth();
            } else if (this.mMode == 2) {
                Canvas canvas2 = canvas;
                canvas2.drawRect((float) (view.getLeft() - getBehindWidth()), 0.0f, (float) view.getLeft(), (float) getHeight(), this.mFadePaint);
                left = view.getRight();
                i = view.getRight() + getBehindWidth();
            } else {
                left = 0;
            }
            canvas.drawRect((float) left, 0.0f, (float) i, (float) getHeight(), this.mFadePaint);
        }
    }

    public void drawSelector(View view, Canvas canvas, float f) {
        if (this.mSelectorEnabled && this.mSelectorDrawable != null && this.mSelectedView != null && ((String) this.mSelectedView.getTag()).equals("CustomViewBehindSelectedView")) {
            canvas.save();
            int width = (int) (((float) this.mSelectorDrawable.getWidth()) * f);
            int left;
            if (this.mMode == 0) {
                left = view.getLeft();
                width = left - width;
                canvas.clipRect(width, 0, left, getHeight());
                canvas.drawBitmap(this.mSelectorDrawable, (float) width, (float) getSelectorTop(), null);
            } else if (this.mMode == 1) {
                left = view.getRight();
                width += left;
                canvas.clipRect(left, 0, width, getHeight());
                canvas.drawBitmap(this.mSelectorDrawable, (float) (width - this.mSelectorDrawable.getWidth()), (float) getSelectorTop(), null);
            }
            canvas.restore();
        }
    }

    public void setSelectorEnabled(boolean z) {
        this.mSelectorEnabled = z;
    }

    public void setSelectedView(View view) {
        if (this.mSelectedView != null) {
            this.mSelectedView.setTag("");
            this.mSelectedView = null;
        }
        if (view != null && view.getParent() != null) {
            this.mSelectedView = view;
            this.mSelectedView.setTag("CustomViewBehindSelectedView");
            invalidate();
        }
    }

    private int getSelectorTop() {
        return this.mSelectedView.getTop() + ((this.mSelectedView.getHeight() - this.mSelectorDrawable.getHeight()) / 2);
    }

    public void setSelectorBitmap(Bitmap bitmap) {
        this.mSelectorDrawable = bitmap;
        refreshDrawableState();
    }

    public void setMenuVisibleAlways(boolean z) {
        this.mVisibleAlways = z;
    }

    public void setGestureAreaWidth(int i) {
        mGestureAreaWidth = i;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(CustomViewBehind.class.getName());
    }
}
