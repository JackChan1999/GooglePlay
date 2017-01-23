package com.qq.googleplay.factory;

import android.util.SparseArray;

import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.ui.fragment.AppFragment;
import com.qq.googleplay.ui.fragment.CategoryFragment;
import com.qq.googleplay.ui.fragment.GameFragment;
import com.qq.googleplay.ui.fragment.HomeFragment;
import com.qq.googleplay.ui.fragment.HotFragment;
import com.qq.googleplay.ui.fragment.RecommendFragment;
import com.qq.googleplay.ui.fragment.SubjectFragment;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/24 22:55
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class FragmentFactory {

    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_APP = 1;
    public static final int FRAGMENT_GAME = 2;
    public static final int FRAGMENT_SUBJECT = 3;
    public static final int FRAGMENT_RECOMMEND = 4;
    public static final int FRAGMENT_CATEGORY = 5;
    public static final int FRAGMENT_HOT = 6;

    private static SparseArray<BaseFragment> cachesFragment = new SparseArray<>();

    public static BaseFragment getFragment(int position) {

        BaseFragment fragment = null;
        // 如果缓存里面有对应的fragment,就直接取出返回
        BaseFragment tempFragment = cachesFragment.get(position);
        if (tempFragment != null) {
            fragment = tempFragment;
            return fragment;
        }

        switch (position) {

            case FRAGMENT_HOME://主页
                fragment = new HomeFragment();
                break;
            case FRAGMENT_APP://应用
                fragment = new AppFragment();
                break;
            case FRAGMENT_GAME://游戏
                fragment = new GameFragment();
                break;
            case FRAGMENT_SUBJECT://专题
                fragment = new SubjectFragment();
                break;
            case FRAGMENT_RECOMMEND://推荐
                fragment = new RecommendFragment();
                break;
            case FRAGMENT_CATEGORY://分类
                fragment = new CategoryFragment();
                break;
            case FRAGMENT_HOT://排行
                fragment = new HotFragment();
                break;
        }

        cachesFragment.put(position, fragment);
        return fragment;
    }
}
