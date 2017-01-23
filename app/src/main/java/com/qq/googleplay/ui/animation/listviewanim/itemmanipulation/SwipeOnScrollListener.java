package com.qq.googleplay.ui.animation.listviewanim.itemmanipulation;

import android.widget.AbsListView;

public class SwipeOnScrollListener implements AbsListView.OnScrollListener {
	
	SwipeDismissListViewTouchListener mTouchListener;
	
	public void setTouchListener(SwipeDismissListViewTouchListener touchListener) {
		mTouchListener = touchListener;
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			mTouchListener.disallowSwipe();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}
}
