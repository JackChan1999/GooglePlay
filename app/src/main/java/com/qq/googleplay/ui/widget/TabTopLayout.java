package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

public class TabTopLayout extends TabLayout {
    private static final String TAG = TabTopLayout.class.getSimpleName();
    private int mDefaultMargin = 20;
    private int mDividerHeight = 1;
    private int mHeaderHeight = 0;
    private int mLRMargin = 0;
    private int mScale = 2;
    private boolean mIsAttachTop = false;
    private View mDividerView;
    private View mHeaderView;
    private View mParentLayoutView;
    private OnHeaderScrollListener mOnHeaderScrollListener;

    public interface OnHeaderScrollListener {
        void onHeaderScroll(int left, int top);
    }

    public TabTopLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mDividerView = new View(context);
        mDividerView.setBackgroundColor(Color.BLUE);
        addView(mDividerView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDividerView();
    }

    public void setHeaderHeight(int headerHeight) {
        mHeaderHeight = headerHeight;
        if (mHeaderHeight == 0) {
            Log.w(TAG, "mHeaderHeight is 0");
        }
    }

    public void setOnHeaderScrollListener(OnHeaderScrollListener onHeaderScrollListener) {
        mOnHeaderScrollListener = onHeaderScrollListener;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
    }

    public void setParentLayoutView(View view) {
        mParentLayoutView = view;
    }

    protected void setHeaderView(int resId) {
        mHeaderView = findViewById(resId);
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerHeight = dividerHeight;
        initDividerView();
    }

    public void setDividerParam(int margin, int height, int scale, int color) {
        mDefaultMargin = margin;
        if (height > 0) {
            mDividerHeight = height;
        }
        if (scale > 0) {
            mScale = scale;
        }
        if (color > 0) {
            mDividerView.setBackgroundColor(color);
        }
        mLRMargin = mDefaultMargin * mScale;
        updateViewParam(mLRMargin);
    }

    public void setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
    }

    public void onScroll(int left, int top, int oldleft, int oldtop) {
        int headerHeight = getHeaderHeight();
        if (top - oldtop <= 0) {
            if (top < headerHeight - mLRMargin) {
                updateViewParam(mLRMargin);
                scrollToView(0, 0);
            }
            dragView(top);
        } else if (top >= 0) {
            if (top >= 0 && top > headerHeight) {
                updateViewParam(0);
                scrollToView(0, headerHeight);
            }
            pushView(top);
        }
    }

    private void scrollToView(int x, int y) {
        if (mOnHeaderScrollListener != null) {
            mOnHeaderScrollListener.onHeaderScroll(x, y);
        }
        if (mParentLayoutView != null) {
            mParentLayoutView.scrollTo(x, y);
        } else {
            scrollTo(x, y);
        }
    }

    private void initDividerView() {
        if (mDividerView != null) {
            removeView(mDividerView);
            LayoutParams layoutParams = new LayoutParams(-1, mDividerHeight);
            layoutParams.setMargins(mLRMargin / mScale, getHeight() - mDividerHeight, mLRMargin / mScale, 0);
            addView(mDividerView, layoutParams);
            Log.v(TAG, "drawDivider");
        }
    }

    private int getHeaderHeight() {
        if (mHeaderView != null) {
            return mHeaderView.getHeight();
        }
        return mHeaderHeight;
    }

    private void pushView(int t) {
        int headerHeight = getHeaderHeight();
        int top = Math.abs(headerHeight - t);
        if (t < 0 || t >= headerHeight) {
            if (t - headerHeight >= mLRMargin) {
                mIsAttachTop = true;
            }
            if (mIsAttachTop && top < mLRMargin) {
                updateViewParam(mLRMargin - top);
                return;
            }
            return;
        }
        scrollToView(0, t);
        if (top < mLRMargin && !mIsAttachTop) {
            updateViewParam(top);
        }
    }

    private void dragView(int t) {
        int headerHeight = getHeaderHeight();
        int top = Math.abs(headerHeight - t);
        if (t >= 0 && t < headerHeight) {
            scrollToView(0, t);
            if (top < mLRMargin && !mIsAttachTop) {
                updateViewParam(top);
            }
            if (headerHeight - t >= mLRMargin) {
                mIsAttachTop = false;
            }
        } else if (mIsAttachTop && top < mLRMargin) {
            updateViewParam(mLRMargin - top);
        }
    }

    public void updateViewParam(int margin) {
        margin /= mScale;
        LayoutParams layoutParams = (LayoutParams) mDividerView.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = mDividerHeight;
            layoutParams.setMargins(margin, layoutParams.topMargin, margin, layoutParams.bottomMargin);
            mDividerView.setLayoutParams(layoutParams);
            mDividerView.invalidate();
        }
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(TabTopLayout.class.getName());
    }
}
