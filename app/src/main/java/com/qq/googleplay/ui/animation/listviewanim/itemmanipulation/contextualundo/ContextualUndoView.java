package com.qq.googleplay.ui.animation.listviewanim.itemmanipulation.contextualundo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class ContextualUndoView extends FrameLayout {

	private View mUndoView;
	private View mContentView;
	private TextView mCountDownTV;

	private long mItemId;

	public ContextualUndoView(Context context, int undoLayoutResId, int countDownTextViewResId) {
		super(context);
		initUndo(undoLayoutResId, countDownTextViewResId);
	}

	private void initUndo(int undoLayoutResId, final int countDownTextViewResId) {
		mUndoView = View.inflate(getContext(), undoLayoutResId, null);
		addView(mUndoView);

		if (countDownTextViewResId != -1) {
			mCountDownTV = (TextView) mUndoView.findViewById(countDownTextViewResId);
		}
	}

	public void updateCountDownTimer(String timerText) {
		if (mCountDownTV != null) {
			mCountDownTV.setText(timerText);
		}
	}

	public void updateContentView(View contentView) {
		if (mContentView == null) {
			addView(contentView);
		}
		mContentView = contentView;
	}

	public View getContentView() {
		return mContentView;
	}

	public void setItemId(long itemId) {
		this.mItemId = itemId;
	}

	public long getItemId() {
		return mItemId;
	}

	public boolean isContentDisplayed() {
		return mContentView.getVisibility() == View.VISIBLE;
	}

	public void displayUndo() {
		updateCountDownTimer("");
		mContentView.setVisibility(View.GONE);
		mUndoView.setVisibility(View.VISIBLE);
	}

	public void displayContentView() {
		mContentView.setVisibility(View.VISIBLE);
		mUndoView.setVisibility(View.GONE);
	}
}