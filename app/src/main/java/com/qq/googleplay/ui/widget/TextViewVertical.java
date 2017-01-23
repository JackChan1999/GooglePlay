package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewVertical extends TextView {
    public TextViewVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewVertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextViewVertical(Context context) {
        super(context);
    }

    public void setText(CharSequence text, BufferType type) {
        if (!"".equals(text) && text != null && text.length() != 0) {
            int m = text.length();
            StringBuffer sb = new StringBuffer();
            int i = 0;
            while (i < m) {
                sb.append(text.toString().subSequence(i, i + 1) + (i < m + -1 ? "\n" : ""));
                i++;
            }
            super.setText(sb, type);
        }
    }
}
