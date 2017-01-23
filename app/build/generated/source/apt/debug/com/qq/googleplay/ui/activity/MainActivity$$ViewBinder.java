// Generated code from Butter Knife. Do not modify!
package com.qq.googleplay.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.qq.googleplay.ui.activity.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131623990, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131623990, "field 'mToolbar'");
    view = finder.findRequiredView(source, 2131623944, "field 'mDrawerLayout'");
    target.mDrawerLayout = finder.castView(view, 2131623944, "field 'mDrawerLayout'");
    view = finder.findRequiredView(source, 2131624006, "field 'mViewPager'");
    target.mViewPager = finder.castView(view, 2131624006, "field 'mViewPager'");
    view = finder.findRequiredView(source, 2131623986, "field 'mTabLayout'");
    target.mTabLayout = finder.castView(view, 2131623986, "field 'mTabLayout'");
    view = finder.findRequiredView(source, 2131623978, "field 'mNavigationView'");
    target.mNavigationView = finder.castView(view, 2131623978, "field 'mNavigationView'");
  }

  @Override public void unbind(T target) {
    target.mToolbar = null;
    target.mDrawerLayout = null;
    target.mViewPager = null;
    target.mTabLayout = null;
    target.mNavigationView = null;
  }
}
