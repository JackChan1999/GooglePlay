package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
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
 * des ：带有删除功能的EditText
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/

public class CleanEditText extends EditText {

	private final String TAG = "editText";
	
	private Drawable dRight;
	private Rect     rBound;

	public CleanEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initEditText();
	}

	public CleanEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initEditText();
	}

	public CleanEditText(Context context) {
		super(context);
		initEditText();
	}

	private void initEditText() {
		setEditTextDrawable();
		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				CleanEditText.this.setEditTextDrawable();
			}
		});

	}

	public void setEditTextDrawable() {
		if (getText().toString().length() == 0) {
			setCompoundDrawables(null, null, null, null);
		} else {
			setCompoundDrawables(null, null, this.dRight, null);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.dRight = null;
		this.rBound = null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((this.dRight != null) && (event.getAction() == 1)) {
			this.rBound = this.dRight.getBounds();
			int i = (int) event.getRawX();
			if (i > getRight() - 3 * this.rBound.width()) {
				// 点击的位置聚焦
				requestFocus();
				setText("");
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if (focused) {
			CleanEditText.this.setEditTextDrawable();
		} else {
			setCompoundDrawables(null, null, null, null);
		}
		
		super.onFocusChanged(focused, direction, previouslyFocusedRect);

	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top,
			Drawable right, Drawable bottom) {
		if (right != null) {
			this.dRight = right;
		}
		super.setCompoundDrawables(left, top, right, bottom);
	}

	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(500);
		return translateAnimation;
	}

}