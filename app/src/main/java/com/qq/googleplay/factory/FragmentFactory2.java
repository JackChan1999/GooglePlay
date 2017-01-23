package com.qq.googleplay.factory;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.ui.fragment.AppFragment;
import com.qq.googleplay.ui.fragment.CategoryFragment;
import com.qq.googleplay.ui.fragment.GameFragment;
import com.qq.googleplay.ui.fragment.HomeFragment;
import com.qq.googleplay.ui.fragment.SubjectFragment;
import com.qq.googleplay.ui.fragment.HotFragment;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/20 17:33
 * 描 述 ： fragment工厂
 * 修订历史 ：
 * ============================================================
 **/
public class FragmentFactory2 {


    private static SparseArray<BaseFragment> mFragments = new SparseArray<>();

    public static Fragment createFragment(int position){

        BaseFragment fragment = mFragments.get(position);
        //如果集合中取出来的是空，重新创建
        if (fragment == null ){
            if (position == 0){
                fragment = new HomeFragment();

            }else if (position == 1){
                fragment = new AppFragment();
            }else if (position == 2){
                fragment = new GameFragment();
            }
            else if (position == 3){
                fragment = new SubjectFragment();
            }
            else if (position == 4){
                fragment = new CategoryFragment();
            }
            else if (position == 5){
                fragment = new HotFragment();
            }

            if (fragment != null){
                //把创建好的fragment存放到集合中缓存起来
                mFragments.put(position,fragment);
            }

        }

        return fragment ;

    }
}
