package com.qq.googleplay.ui.adapter;

import android.support.v7.widget.RecyclerView;

import com.qq.googleplay.ui.animation.recyclerviewanim.animator.AlphaAnimatorAdapter;
import com.qq.googleplay.ui.animation.recyclerviewanim.animator.ScaleInAnimatorAdapter;
import com.qq.googleplay.ui.animation.recyclerviewanim.animator.SlideInBottomAnimatorAdapter;
import com.qq.googleplay.ui.animation.recyclerviewanim.animator.SlideInLeftAnimatorAdapter;
import com.qq.googleplay.ui.animation.recyclerviewanim.animator.SwingBottomInAnimationAdapter;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/26 17:38
 * 描 述 ：
 * 修订历史 ：
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
