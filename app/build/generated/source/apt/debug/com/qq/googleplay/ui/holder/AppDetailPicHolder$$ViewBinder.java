// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.holder;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AppDetailPicHolder$$ViewBinder<T extends com.qq.googleplay.ui.holder.AppDetailPicHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624308, "field 'mContainerPic'");
    target.mContainerPic = finder.castView(view, 2131624308, "field 'mContainerPic'");
  }

  @Override public void unbind(T target) {
    target.mContainerPic = null;
  }
}
