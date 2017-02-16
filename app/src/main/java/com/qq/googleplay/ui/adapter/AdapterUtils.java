package com.qq.googleplay.ui.adapter;

import android.support.v7.widget.RecyclerView;

import com.qq.googleplay.ui.animation.recyclerviewanim.animator.AlphaAnimatorAdapter;
import com.qq.googleplay.ui.animation.recyclerviewanim.animator.ScaleInAnimatorAdapter;
import com.qq.googleplay.ui.animation.recyclerviewanim.animator.SlideInBottomAnimatorAdapter;
import com.qq.googleplay.ui.animation.recyclerviewanim.animator.SlideInLeftAnimatorAdapter;
import com.qq.googleplay.ui.animation.recyclerviewanim.animator.SwingBottomInAnimationAdapter;

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
public class AdapterUtils {

    public static SlideInBottomAnimatorAdapter getSlideInBottomAdapter(
            RecyclerView.Adapter adapter, RecyclerView recyclerView){
        return new SlideInBottomAnimatorAdapter(adapter,recyclerView);
    }

    public static AlphaAnimatorAdapter getAlphaAdapter(
            RecyclerView.Adapter adapter, RecyclerView recyclerView){
        return new AlphaAnimatorAdapter(adapter,recyclerView);
    }

    public static ScaleInAnimatorAdapter getScaleInAdapter(
            RecyclerView.Adapter adapter, RecyclerView recyclerView){
        return new ScaleInAnimatorAdapter(adapter,recyclerView);
    }

    public static SlideInLeftAnimatorAdapter getSlideInLeftAdapter(
            RecyclerView.Adapter adapter, RecyclerView recyclerView){
        return new SlideInLeftAnimatorAdapter(adapter,recyclerView);
    }
    public static SwingBottomInAnimationAdapter getSwingBottomInAdapter(
            RecyclerView.Adapter adapter, RecyclerView recyclerView){
        return new SwingBottomInAnimationAdapter(adapter,recyclerView);
    }
}
