// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.holder;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AppDetailDesHolder$$ViewBinder<T extends com.qq.googleplay.ui.holder.AppDetailDesHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624300, "field 'mIvArrow'");
    target.mIvArrow = finder.castView(view, 2131624300, "field 'mIvArrow'");
    view = finder.findRequiredView(source, 2131624298, "field 'mTvDes'");
    target.mTvDes = finder.castView(view, 2131624298, "field 'mTvDes'");
    view = finder.findRequiredView(source, 2131624299, "field 'mTvAuthor'");
    target.mTvAuthor = finder.castView(view, 2131624299, "field 'mTvAuthor'");
  }

  @Override public void unbind(T target) {
    target.mIvArrow = null;
    target.mTvDes = null;
    target.mTvAuthor = null;
  }
}
