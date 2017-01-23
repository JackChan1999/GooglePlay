// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.holder;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HomeHolder$$ViewBinder<T extends com.qq.googleplay.ui.holder.HomeHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624312, "field 'mIvIcon'");
    target.mIvIcon = finder.castView(view, 2131624312, "field 'mIvIcon'");
    view = finder.findRequiredView(source, 2131624314, "field 'mRbStars'");
    target.mRbStars = finder.castView(view, 2131624314, "field 'mRbStars'");
    view = finder.findRequiredView(source, 2131624317, "field 'mTvDes'");
    target.mTvDes = finder.castView(view, 2131624317, "field 'mTvDes'");
    view = finder.findRequiredView(source, 2131624315, "field 'mTvSize'");
    target.mTvSize = finder.castView(view, 2131624315, "field 'mTvSize'");
    view = finder.findRequiredView(source, 2131624313, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624313, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624316, "field 'circleprogressview'");
    target.circleprogressview = finder.castView(view, 2131624316, "field 'circleprogressview'");
  }

  @Override public void unbind(T target) {
    target.mIvIcon = null;
    target.mRbStars = null;
    target.mTvDes = null;
    target.mTvSize = null;
    target.mTvTitle = null;
    target.circleprogressview = null;
  }
}
