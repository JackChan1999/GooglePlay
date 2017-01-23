package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class SearchEditText extends EditText {
    private static final int HIDE = 2;
    private static final int SHOW = 1;
    private SearchHandler mHandler;

    static class SearchHandler extends Handler {
        WeakReference<SearchEditText> mWeakReference = null;

        public SearchHandler(WeakReference<SearchEditText> weakReference) {
            this.mWeakReference = weakReference;
        }

        public void handleMessage(Message message) {
            SearchEditText searchEditText = (SearchEditText) mWeakReference.get();
            switch (message.what) {
                case SHOW:
                    searchEditText.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) searchEditText
                            .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.restartInput(searchEditText);
                    inputMethodManager.showSoftInput(searchEditText, 1);
                    return;
                case HIDE:
                    searchEditText.clearFocus();
                    ((InputMethodManager) searchEditText.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    return;
                default:
                    return;
            }
        }
    }

    public SearchEditText(Context context) {
        super(context);
        init();
    }

    public SearchEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public SearchEditText(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    public void init() {
        this.mHandler = new SearchHandler(new WeakReference(this));
    }

    public void showIme(boolean isShow) {
        int what = 1;
        mHandler.removeMessages(SHOW);
        mHandler.removeMessages(HIDE);
        SearchHandler searchHandler = mHandler;
        if (!isShow) {
            what = 2;
        }
        searchHandler.sendEmptyMessageDelayed(what, 100);
    }

    public boolean isInputComplete() {
        return BaseInputConnection.getComposingSpanStart(getEditableText()) == -1;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(SearchEditText.class.getName());
    }
}
