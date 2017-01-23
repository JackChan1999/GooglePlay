package com.qq.googleplay.ui.animation.listviewanim.swinginadapters;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * An implementation of AnimationAdapter which bases the animations on
 * resources.
 */
public abstract class ResourceAnimationAdapter<T> extends AnimationAdapter {

	private Context mContext;

	public ResourceAnimationAdapter(BaseAdapter baseAdapter, Context context) {
		super(baseAdapter);
		mContext = context;
	}

	@Override
	public Animator[] getAnimators(ViewGroup parent, View view) {
		return new Animator[] { AnimatorInflater.loadAnimator(mContext, getAnimationResourceId()) };
	}

	/**
	 * Get the resource id of the animation to apply to the views.
	 */
	protected abstract int getAnimationResourceId();

}
