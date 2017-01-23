package com.qq.googleplay.ui.animation.listviewanim.swinginadapters.prepared;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qq.googleplay.ui.animation.listviewanim.swinginadapters.AnimationAdapter;

public class AlphaInAnimationAdapter extends AnimationAdapter {

	public AlphaInAnimationAdapter(BaseAdapter baseAdapter) {
		super(baseAdapter);
	}

	@Override
	protected long getAnimationDelayMillis() {
		return DEFAULTANIMATIONDELAYMILLIS;
	}

	@Override
	protected long getAnimationDurationMillis() {
		return DEFAULTANIMATIONDURATIONMILLIS;
	}

	@Override
	public Animator[] getAnimators(ViewGroup parent, View view) {
		return new Animator[0];
	}
}
