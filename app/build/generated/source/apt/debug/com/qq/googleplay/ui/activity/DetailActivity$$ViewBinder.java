// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DetailActivity$$ViewBinder<T extends com.qq.googleplay.ui.activity.DetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624159, "field 'mContainerBottom'");
    target.mContainerBottom = finder.castView(view, 2131624159, "field 'mContainerBottom'");
    view = finder.findRequiredView(source, 2131624163, "field 'mContainerDes'");
    target.mContainerDes = finder.castView(view, 2131624163, "field 'mContainerDes'");
    view = finder.findRequiredView(source, 2131624160, "field 'mContainerInfo'");
    target.mContainerInfo = finder.castView(view, 2131624160, "field 'mContainerInfo'");
    view = finder.findRequiredView(source, 2131624162, "field 'mContainerPic'");
    target.mContainerPic = finder.castView(view, 2131624162, "field 'mContainerPic'");
    view = finder.findRequiredView(source, 2131624161, "field 'mContainerSafe'");
    target.mContainerSafe = finder.castView(view, 2131624161, "field 'mContainerSafe'");
  }

  @Override public void unbind(T target) {
    target.mContainerBottom = null;
    target.mContainerDes = null;
    target.mContainerInfo = null;
    target.mContainerPic = null;
    target.mContainerSafe = null;
  }
}
