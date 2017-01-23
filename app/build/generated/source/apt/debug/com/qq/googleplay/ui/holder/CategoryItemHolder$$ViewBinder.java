// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.holder;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CategoryItemHolder$$ViewBinder<T extends com.qq.googleplay.ui.holder.CategoryItemHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131623962, "field 'mContainerItem1'");
    target.mContainerItem1 = finder.castView(view, 2131623962, "field 'mContainerItem1'");
    view = finder.findRequiredView(source, 2131623963, "field 'mContainerItem2'");
    target.mContainerItem2 = finder.castView(view, 2131623963, "field 'mContainerItem2'");
    view = finder.findRequiredView(source, 2131623964, "field 'mContainerItem3'");
    target.mContainerItem3 = finder.castView(view, 2131623964, "field 'mContainerItem3'");
    view = finder.findRequiredView(source, 2131623959, "field 'mIvIcon1'");
    target.mIvIcon1 = finder.castView(view, 2131623959, "field 'mIvIcon1'");
    view = finder.findRequiredView(source, 2131623960, "field 'mIvIcon2'");
    target.mIvIcon2 = finder.castView(view, 2131623960, "field 'mIvIcon2'");
    view = finder.findRequiredView(source, 2131623961, "field 'mIvIcon3'");
    target.mIvIcon3 = finder.castView(view, 2131623961, "field 'mIvIcon3'");
    view = finder.findRequiredView(source, 2131623965, "field 'mTvName1'");
    target.mTvName1 = finder.castView(view, 2131623965, "field 'mTvName1'");
    view = finder.findRequiredView(source, 2131623966, "field 'mTvName2'");
    target.mTvName2 = finder.castView(view, 2131623966, "field 'mTvName2'");
    view = finder.findRequiredView(source, 2131623967, "field 'mTvName3'");
    target.mTvName3 = finder.castView(view, 2131623967, "field 'mTvName3'");
  }

  @Override public void unbind(T target) {
    target.mContainerItem1 = null;
    target.mContainerItem2 = null;
    target.mContainerItem3 = null;
    target.mIvIcon1 = null;
    target.mIvIcon2 = null;
    target.mIvIcon3 = null;
    target.mTvName1 = null;
    target.mTvName2 = null;
    target.mTvName3 = null;
  }
}
