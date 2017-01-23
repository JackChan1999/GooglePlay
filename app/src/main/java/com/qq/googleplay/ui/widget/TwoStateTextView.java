package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.utils.StringUtil;

public class TwoStateTextView extends TextView {
    private int mCurrentSelectedCount;
    private int mTotalCount;
    private String mSelectAll;
    private String mCancelSelectALl;
    private State mState;
    private boolean mForceUpdate;

    private enum State {
        COMPLETED,
        PROGRESS
    }

    public TwoStateTextView(Context context) {
        this(context, null);
    }

    public TwoStateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTotalCount = 0;
        mCurrentSelectedCount = Integer.MAX_VALUE;
        mForceUpdate = false;
        init();
    }

    private void init() {
        mSelectAll = StringUtil.getString(R.string.select_all);
        mCancelSelectALl = StringUtil.getString(R.string.select_all_cancel);
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
        mForceUpdate = true;
    }

    public void setSelectedCount(int selectedCount) {
        mCurrentSelectedCount = selectedCount;
        updateState();
    }

    private void updateState() {
        if (mForceUpdate) {
            mForceUpdate = false;
            if (mCurrentSelectedCount >= mTotalCount) {
                mState = State.COMPLETED;
                setText(mCancelSelectALl);
                return;
            }
            mState = State.PROGRESS;
            setText(mSelectAll);
        } else if (mState == State.PROGRESS && mCurrentSelectedCount >= mTotalCount) {
            setText(mCancelSelectALl);
            mState = State.COMPLETED;
        } else if (mState == State.COMPLETED && mCurrentSelectedCount < mTotalCount) {
            setText(mSelectAll);
            mState = State.PROGRESS;
        }
    }
}
