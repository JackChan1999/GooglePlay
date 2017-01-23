package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomProgressButton extends Button {
	private boolean		mProgressEnable;
	private long		mMax	= 100;
	private long		mProgress;
	private Drawable	mProgressDrawable;

	public CustomProgressButton(Context context) {
		super(context);
	}

	public CustomProgressButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// onmeasure onlayout ondraw
	@Override
	protected void onDraw(Canvas canvas) {
		if (mProgressEnable) {
			Drawable drawable = new ColorDrawable(Color.BLUE);
			int left = 0;
			int top = 0;
			int right = (int) (mProgress * 1.0f / mMax * getMeasuredWidth() + .5f);
			int bottom = getBottom();
			drawable.setBounds(left, top, right, bottom);// 必须的.告知绘制的范围
			drawable.draw(canvas);
		}

		super.onDraw(canvas);// 绘制文本,还会绘制背景
	}

	/**设置是否允许进度*/
	public void setProgressEnable(boolean progressEnable) {
		mProgressEnable = progressEnable;
	}

	/**设置进度的最大值*/
	public void setMax(long max) {
		mMax = max;
	}

	/**设置当前的进度,并且进行重绘操作*/
	public void setProgress(long progress) {
		mProgress = progress;
		invalidate();
	}

	/**设置progressButton的进度图片*/
	public void setProgressDrawable(Drawable progressDrawable) {
		mProgressDrawable = progressDrawable;
	}
}
