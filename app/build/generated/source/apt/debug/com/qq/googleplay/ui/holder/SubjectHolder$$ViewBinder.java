// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.holder;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SubjectHolder$$ViewBinder<T extends com.qq.googleplay.ui.holder.SubjectHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131623969, "field 'mIvIcon'");
    target.mIvIcon = finder.castView(view, 2131623969, "field 'mIvIcon'");
    view = finder.findRequiredView(source, 2131623970, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131623970, "field 'mTvTitle'");
  }

  @Override public void unbind(T target) {
    target.mIvIcon = null;
    target.mTvTitle = null;
  }
}
