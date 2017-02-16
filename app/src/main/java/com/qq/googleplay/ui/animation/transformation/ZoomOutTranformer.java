package com.qq.googleplay.ui.animation.transformation;

import android.view.View;
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
public class ZoomOutTranformer extends ABaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		final float scale = 1f + Math.abs(position);
		view.setScaleX(scale);
		view.setScaleY(scale);
		view.setPivotX(view.getWidth() * 0.5f);
		view.setPivotY(view.getHeight() * 0.5f);
		view.setAlpha(position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
		if(position == -1){
			view.setTranslationX(view.getWidth() * -1);
		}
	}

}
