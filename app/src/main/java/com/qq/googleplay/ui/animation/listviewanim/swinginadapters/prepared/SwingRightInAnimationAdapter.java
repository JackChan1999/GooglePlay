package com.qq.googleplay.ui.animation.listviewanim.swinginadapters.prepared;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qq.googleplay.ui.animation.listviewanim.swinginadapters.SingleAnimationAdapter;
/**
 * An implementation of the AnimationAdapter class which applies a
 * swing-in-from-the-right-animation to views.
 */
public class SwingRightInAnimationAdapter extends SingleAnimationAdapter {

	private final long mAnimationDelayMillis;
	private final long mAnimationDurationMillis;

	public SwingRightInAnimationAdapter(BaseAdapter baseAdapter) {
		this(baseAdapter, DEFAULTANIMATIONDELAYMILLIS, DEFAULTANIMATIONDURATIONMILLIS);
	}

	public SwingRightInAnimationAdapter(BaseAdapter baseAdapter, long animationDelayMillis) {
		this(baseAdapter, animationDelayMillis, DEFAULTANIMATIONDURATIONMILLIS);
	}

	public SwingRightInAnimationAdapter(BaseAdapter baseAdapter, long animationDelayMillis, long animationDurationMillis) {
		super(baseAdapter);
		mAnimationDelayMillis = animationDelayMillis;
		mAnimationDurationMillis = animationDurationMillis;
	}

	@Override
	protected long getAnimationDelayMillis() {
		return mAnimationDelayMillis;
	}

	@Override
	protected long getAnimationDurationMillis() {
		return mAnimationDurationMillis;
	}

	@Override
	protected Animator getAnimator(ViewGroup parent, View view) {
		return ObjectAnimator.ofFloat(view, "translationX", parent.getWidth(), 0);
	}
}
