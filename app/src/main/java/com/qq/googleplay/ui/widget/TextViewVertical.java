package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
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
