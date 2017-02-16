package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
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
public class ViewPagerFixed extends ViewPager {
  
    public ViewPagerFixed(Context context) {
        super(context);  
    }  
  
    public ViewPagerFixed(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent ev) {
        try {  
            return super.onTouchEvent(ev);  
        } catch (IllegalArgumentException ex) {  
            ex.printStackTrace();  
        }  
        return false;  
    }  
  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        try {  
            return super.onInterceptTouchEvent(ev);  
        } catch (IllegalArgumentException ex) {  
            ex.printStackTrace();  
        }  
        return false;  
    }  
}  