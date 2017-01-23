// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.holder;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AppDetailInfoHolder$$ViewBinder<T extends com.qq.googleplay.ui.holder.AppDetailInfoHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624301, "field 'mIvIcon'");
    target.mIvIcon = finder.castView(view, 2131624301, "field 'mIvIcon'");
    view = finder.findRequiredView(source, 2131624303, "field 'mRbStar'");
    target.mRbStar = finder.castView(view, 2131624303, "field 'mRbStar'");
    view = finder.findRequiredView(source, 2131624304, "field 'mTvDownLoadNum'");
    target.mTvDownLoadNum = finder.castView(view, 2131624304, "field 'mTvDownLoadNum'");
    view = finder.findRequiredView(source, 2131624302, "field 'mTvName'");
    target.mTvName = finder.castView(view, 2131624302, "field 'mTvName'");
    view = finder.findRequiredView(source, 2131624306, "field 'mTvTime'");
    target.mTvTime = finder.castView(view, 2131624306, "field 'mTvTime'");
    view = finder.findRequiredView(source, 2131624305, "field 'mTvVersion'");
    target.mTvVersion = finder.castView(view, 2131624305, "field 'mTvVersion'");
    view = finder.findRequiredView(source, 2131624307, "field 'mTvSize'");
    target.mTvSize = finder.castView(view, 2131624307, "field 'mTvSize'");
  }

  @Override public void unbind(T target) {
    target.mIvIcon = null;
    target.mRbStar = null;
    target.mTvDownLoadNum = null;
    target.mTvName = null;
    target.mTvTime = null;
    target.mTvVersion = null;
    target.mTvSize = null;
  }
}
