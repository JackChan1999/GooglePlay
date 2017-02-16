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
public class InnerViewPager extends ViewPager {

	private float	mDownX;
	private float	mDownY;
	private float	mMoveX;
	private float	mMoveY;

	public InnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InnerViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 左右滑动-->自己处理
		// 上下滑动-->父亲处理
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getRawX();
			mDownY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			mMoveX = ev.getRawX();
			mMoveY = ev.getRawY();

			int diffX = (int) (mMoveX - mDownX);
			int diffY = (int) (mMoveY - mDownY);
			// 左右滚动的绝对值 > 上下滚动的绝对值
			if (Math.abs(diffX) > Math.abs(diffY)) {// 左右
				// 左右滑动-->自己处理
				getParent().requestDisallowInterceptTouchEvent(true);
			} else {// 上下
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_UP:

			break;
		case MotionEvent.ACTION_CANCEL:

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/*@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return  false ;
	}*/

	/*@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {


		// 左右滑动-->自己处理
		// 上下滑动-->父亲处理
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 不要拦截，保证ACTION_MOVE调用
				getParent().requestDisallowInterceptTouchEvent(true);
				mDownX = ev.getRawX();
				mDownY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				mMoveX = ev.getRawX();
				mMoveY = ev.getRawY();

				int diffX = (int) (mMoveX - mDownX);
				int diffY = (int) (mMoveY - mDownY);
				// 左右滚动的绝对值 > 上下滚动的绝对值
				if (Math.abs(diffX) > Math.abs(diffY)) {// 左右
					// 左右滑动-->自己处理
					// getParent()(父亲).request(请求)Disallow(不)Intercept(拦截)TouchEvent(touch事件)(true(同意));
					// 请求父亲不拦截事件
					getParent().requestDisallowInterceptTouchEvent(true);
				} else {// 上下
					getParent().requestDisallowInterceptTouchEvent(false);
				}
				break;
			case MotionEvent.ACTION_UP:

				break;
			case MotionEvent.ACTION_CANCEL:

				break;

		}
		return super.onTouchEvent(ev);
	}*/
}
