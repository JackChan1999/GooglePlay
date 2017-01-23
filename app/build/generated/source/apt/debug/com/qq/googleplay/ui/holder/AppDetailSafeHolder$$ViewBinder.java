// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.holder;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AppDetailSafeHolder$$ViewBinder<T extends com.qq.googleplay.ui.holder.AppDetailSafeHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624310, "field 'mContainerPic'");
    target.mContainerPic = finder.castView(view, 2131624310, "field 'mContainerPic'");
    view = finder.findRequiredView(source, 2131624311, "field 'mContainerDes'");
    target.mContainerDes = finder.castView(view, 2131624311, "field 'mContainerDes'");
    view = finder.findRequiredView(source, 2131624309, "field 'mIvArrow'");
    target.mIvArrow = finder.castView(view, 2131624309, "field 'mIvArrow'");
  }

  @Override public void unbind(T target) {
    target.mContainerPic = null;
    target.mContainerDes = null;
    target.mIvArrow = null;
  }
}
