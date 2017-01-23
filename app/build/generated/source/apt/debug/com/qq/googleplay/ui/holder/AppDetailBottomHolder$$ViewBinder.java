// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.holder;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AppDetailBottomHolder$$ViewBinder<T extends com.qq.googleplay.ui.holder.AppDetailBottomHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624295, "field 'mBtnShare'");
    target.mBtnShare = finder.castView(view, 2131624295, "field 'mBtnShare'");
    view = finder.findRequiredView(source, 2131624294, "field 'mBtnFavo'");
    target.mBtnFavo = finder.castView(view, 2131624294, "field 'mBtnFavo'");
    view = finder.findRequiredView(source, 2131624296, "field 'mProgressButton'");
    target.mProgressButton = finder.castView(view, 2131624296, "field 'mProgressButton'");
  }

  @Override public void unbind(T target) {
    target.mBtnShare = null;
    target.mBtnFavo = null;
    target.mProgressButton = null;
  }
}
