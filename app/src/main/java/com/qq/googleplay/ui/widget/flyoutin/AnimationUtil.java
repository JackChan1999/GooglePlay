package com.qq.googleplay.ui.widget.flyoutin;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
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
public class AnimationUtil {

	private static final long	MEDIUM	= 500;

	/** 创建一个淡入放大的动画 */
	public static Animation createZoomInNearAnim() {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// 创建一个淡入的动画
		anim = new AlphaAnimation(0f, 1f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new LinearInterpolator());
		ret.addAnimation(anim);
		// 创建一个放大的动画
		anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		return ret;
	}

	/** 创建一个淡出放大的动画 */
	public static Animation createZoomInAwayAnim() {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// 创建一个淡出的动画
		anim = new AlphaAnimation(1f, 0f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		// 创建一个放大的动画
		anim = new ScaleAnimation(1, 3, 1, 3, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		return ret;
	}

	/** 创建一个淡入缩小的动画 */
	public static Animation createZoomOutNearAnim() {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// 创建一个淡入的动画
		anim = new AlphaAnimation(0f, 1f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new LinearInterpolator());
		ret.addAnimation(anim);
		// 创建一个缩小的动画
		anim = new ScaleAnimation(3, 1, 3, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		return ret;
	}

	/** 创建一个淡出缩小的动画 */
	public static Animation createZoomOutAwayAnim() {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// 创建一个淡出的动画
		anim = new AlphaAnimation(1f, 0f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		// 创建一个缩小的动画
		anim = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		return ret;
	}

	/** 创建一个淡入放大的动画 */
	public static Animation createPanInAnim(float degree) {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// 创建一个淡入动画
		anim = new AlphaAnimation(0f, 1f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new LinearInterpolator());
		ret.addAnimation(anim);
		// 创建一个放大动画
		final float pivotX = (float) (1 - Math.cos(degree)) / 2;
		final float pivotY = (float) (1 + Math.sin(degree)) / 2;

		anim = new ScaleAnimation(0.8f, 1, 0.8f, 1, Animation.RELATIVE_TO_SELF,
				pivotX, Animation.RELATIVE_TO_SELF,
				pivotY);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);

		return ret;
	}

	/** 创建一个淡出缩小的动画 */
	public static Animation createPanOutAnim(float degree) {
		AnimationSet ret;
		Animation anim;
		ret = new AnimationSet(false);
		// 创建一个淡出动画
		anim = new AlphaAnimation(1f, 0f);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);
		// 创建一个缩小动画
		final float pivotX = (float) (1 + Math.cos(degree)) / 2;
		final float pivotY = (float) (1 - Math.sin(degree)) / 2;
		anim = new ScaleAnimation(1, 0.8f, 1, 0.8f, Animation.RELATIVE_TO_SELF,
				pivotX, Animation.RELATIVE_TO_SELF,
				pivotY);
		anim.setDuration(MEDIUM);
		anim.setInterpolator(new DecelerateInterpolator());
		ret.addAnimation(anim);

		return ret;
	}
}
