package com.qq.googleplay.ui.animation.listviewanim.swinginadapters;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * An implementation of AnimationAdapter which applies a single Animator to
 * views.
 */
public abstract class SingleAnimationAdapter extends AnimationAdapter {

	public SingleAnimationAdapter(BaseAdapter baseAdapter) {
		super(baseAdapter);
	}

	@Override
	public Animator[] getAnimators(ViewGroup parent, View view) {
		Animator animator = getAnimator(parent, view);
		return new Animator[] { animator };
	}

	/**
	 * Get the {@link Animator} to apply to the {@link View}.
	 * 
	 * @param parent
	 *            the {@link ViewGroup} which is the parent of the View.
	 * @param view
	 *            the View that will be animated, as retrieved by
	 *            {@link #getView(int, View, ViewGroup)}.
	 */
	protected abstract Animator getAnimator(ViewGroup parent, View view);

}
