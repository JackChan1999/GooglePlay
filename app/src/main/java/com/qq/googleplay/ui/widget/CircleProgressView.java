package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.utils.CommonUtil;
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
public class CircleProgressView extends LinearLayout {

	private ImageView	mIcon;
	private TextView	mNote;

	private boolean		mProgressEnable;
	private long		mMax	= 100;
	private long		mProgress;

	/**是否允许进度*/
	public void setProgressEnable(boolean progressEnable) {
		mProgressEnable = progressEnable;
	}

	/**进度最大值*/
	public void setMax(long max) {
		mMax = max;
	}

	/**进度当前进度*/
	public void setProgress(long progress) {
		mProgress = progress;
		invalidate();
	}

	/**设置图标*/
	public void setIcon(int resId) {
		mIcon.setImageResource(resId);
	}

	/**设置文字*/
	public void setNote(String note) {
		mNote.setText(note);
	}

	public CircleProgressView(Context context) {
		this(context, null);
	}

	public CircleProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.inflate_circleprogressview, this);
		mIcon = (ImageView) view.findViewById(R.id.circleProgressView_iv_icon);
		mNote = (TextView) view.findViewById(R.id.circleProgressView_tv_note);
	}

	// ondraw onmeasure onlayout
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);//绘制背景,透明图片
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);//绘制具体的内容(图片和文字)

		if (mProgressEnable) {
			RectF oval = new RectF(mIcon.getLeft(), mIcon.getTop(),
					mIcon.getRight(), mIcon.getBottom());
			float startAngle = -90;

			float sweepAngle = mProgress * 360.f / mMax;

			boolean useCenter = false;// 是否保留两条边
			Paint paint = new Paint();
			paint.setColor(Color.BLUE);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(CommonUtil.dip2Px(3));
			paint.setAntiAlias(true);// 消除锯齿
			canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
		}
	}
}
