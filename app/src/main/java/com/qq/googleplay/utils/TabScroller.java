package com.qq.googleplay.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import com.qq.googleplay.R;

import java.util.ArrayList;

public class TabScroller {
    private float mCurrentTabOffset;
    private float mOldTabOffset;
    private int mCurrentTabPos;
    private int mTabIndicatorHeight = 20;
    private Drawable mTabIndicator;
    private ViewGroup mTabParentView;
    private ArrayList<Integer> mTabLength = new ArrayList<>();
    private ArrayList<View> mTabViews = new ArrayList<>();

    public TabScroller(Context context, ViewGroup viewGroup) {
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.TabScroller,
                R.attr.TabScrollerStyle, 0);
        mTabIndicator = a.getDrawable(R.styleable.TabScroller_mcTabIndicatorDrawable);
        if (mTabIndicator == null) {
            mTabIndicator = context.getResources().getDrawable(R.drawable.mz_tab_selected);
        }
        a.recycle();
        mTabIndicatorHeight = mTabIndicator.getIntrinsicHeight();
        mTabParentView = viewGroup;
    }

    public void setTabIndicatorDrawable(Drawable drawable) {
        if (drawable != null) {
            mTabIndicator = drawable;
            mTabIndicatorHeight = mTabIndicator.getIntrinsicHeight();
        }
    }

    public void setTabIndicatorHeight(int position) {
        if (position > 0) {
            mTabIndicatorHeight = position;
        }
    }

    public void setCurrentTab(int position) {
        if (mTabParentView != null) {
            mCurrentTabPos = position;
            mCurrentTabOffset = 0.0f;
            mTabParentView.invalidate();
        }
    }

    public void addTabView(View view) {
        if (view != null && !mTabViews.contains(view)) {
            mTabViews.add(view);
        }
    }

    public void addTabView(int index, View view) {
        if (view != null && !mTabViews.contains(view)) {
            mTabViews.add(index, view);
        }
    }

    public void setTabLength(int[] lengths) {
        if (lengths != null) {
            for (int valueOf : lengths) {
                mTabLength.add(Integer.valueOf(valueOf));
            }
        }
    }

    public boolean removeTabView(View view) {
        boolean remove = mTabViews.remove(view);
        if (mTabParentView != null) {
            mTabParentView.invalidate();
        }
        return remove;
    }

    public boolean removeTabView(int index) {
        return removeTabView((View) mTabViews.get(index));
    }

    public void removeAllTabView() {
        mTabViews.clear();
        if (mTabParentView != null) {
            mTabParentView.invalidate();
        }
    }

    public void onTabScrolled(int position, float positionOffset) {
        mCurrentTabPos = position;
        mCurrentTabOffset = positionOffset;
        if (mTabParentView != null) {
            mTabParentView.invalidate();
        }
    }

    public void dispatchDraw(Canvas canvas) {
        int tabSize = mTabViews.size();
        if (tabSize != 0) {
            if (mCurrentTabPos >= tabSize) {
                mCurrentTabPos = tabSize - 1;
            } else if (mCurrentTabPos < 0) {
                mCurrentTabPos = 0;
            }
            boolean setTabLength = mTabLength.size() == tabSize;
            View mCurTab = (View) mTabViews.get(mCurrentTabPos);
            int curTabwidth = setTabLength ? (Integer) mTabLength.get(mCurrentTabPos) : mCurTab.getWidth();
            if (curTabwidth < 0 || curTabwidth > mCurTab.getWidth()) {
                curTabwidth = mCurTab.getWidth();
            }
            int height = mCurTab.getHeight();
            int center = mCurTab.getLeft() + (mCurTab.getWidth() / 2);
            View nextTab = null;
            int nextTabWidth = 0;
            float deltaWidth = 0.0f;
            if (mCurrentTabOffset > mOldTabOffset && mCurrentTabPos < tabSize - 1) {
                nextTab = (View) mTabViews.get(mCurrentTabPos + 1);
                nextTabWidth = setTabLength ? (Integer) mTabLength.get(mCurrentTabPos + 1) : nextTab.getWidth();
                if (nextTabWidth < 0 || nextTabWidth > nextTab.getWidth()) {
                    nextTabWidth = nextTab.getWidth();
                }
            } else if (mCurrentTabOffset < mOldTabOffset && mCurrentTabPos > 0) {
                nextTab = (View) mTabViews.get(mCurrentTabPos - 1);
                nextTabWidth = setTabLength ? (Integer) mTabLength.get(mCurrentTabPos - 1) : nextTab.getWidth();
                if (nextTabWidth < 0 || nextTabWidth > nextTab.getWidth()) {
                    nextTabWidth = nextTab.getWidth();
                }
            }
            if (nextTab != null) {
                deltaWidth = ((float) (nextTabWidth - curTabwidth)) * mCurrentTabOffset;
                center = (int) (((float) center) + (((float) ((nextTab.getLeft() + (nextTab
                        .getWidth() / 2)) - (mCurTab.getLeft() + (mCurTab.getWidth() / 2)))) *
                        mCurrentTabOffset));
            }
            int curWidth = (int) (((float) curTabwidth) + deltaWidth);
            mTabIndicator.setBounds(center - (curWidth / 2), height - mTabIndicatorHeight, (curWidth / 2) + center, height);
            canvas.save();
            mTabIndicator.draw(canvas);
            canvas.restore();
        }
    }
}
