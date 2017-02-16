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
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：GooglePlay
 * Package_Name：com.qq.googleplay
 * Version：1.0
 * time：2016/2/16 13:33
 * des ：${TODO}
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
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
