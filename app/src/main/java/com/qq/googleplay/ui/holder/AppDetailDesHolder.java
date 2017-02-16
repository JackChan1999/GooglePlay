package com.qq.googleplay.ui.holder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.bean.AppInfoBean;

import butterknife.Bind;
import butterknife.ButterKnife;
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

public class AppDetailDesHolder extends BaseHolder<AppInfoBean> implements OnClickListener {

    @Bind(R.id.app_detail_des_iv_arrow)
    ImageView mIvArrow;

    @Bind(R.id.app_detail_des_tv_des)
    TextView mTvDes;

    private boolean isOpen = true;
    private int mTvDesMeasuredHeight;

   /* @Bind(R.id.expand_text_view)
    ExpandableTextView expTv;*/

    @Bind(R.id.app_detail_des_tv_author)
    TextView mTvAuthor;

    private AppInfoBean mData;

    public AppDetailDesHolder(Context context) {
        super(context);
    }

    @Override
    public View initHolderView() {
        View view = View.inflate(mContext, R.layout.item_app_detail_des, null);
        ButterKnife.bind(this, view);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshHolderView(AppInfoBean data) {
        mData = data;

        mTvDes.setText(data.des);
        mTvAuthor.setText("作者：" + data.author);
        mTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mTvDesMeasuredHeight = mTvDes.getMeasuredHeight();
                // 默认折叠
                toggle(false);
                // 如果不移除,一会高度变成7行的时候.mTvDesMeasuredHeight就会变
                mTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    @Override
    public void onClick(View v) {
        toggle(true);
    }

    private void toggle(boolean isAnimation) {
        if (isOpen) {// 折叠
            int start = mTvDesMeasuredHeight;
            int end = getShortHeight(7, mData);
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mTvDes.setHeight(end);
            }
        } else {// 展开
            int start = getShortHeight(7, mData);
            int end = mTvDesMeasuredHeight;
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mTvDes.setHeight(end);
            }
        }

        if (isAnimation) {// mTvDes正在折叠或者展开
            if (isOpen) {
                ObjectAnimator.ofFloat(mIvArrow, "rotation", 180, 0).start();
            } else {
                ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();
            }
        }
        isOpen = !isOpen;

    }

    public void doAnimation(int start, int end) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mTvDes, "height", start, end);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {// 动画开始

            }

            @Override
            public void onAnimationRepeat(Animator arg0) {// 动画重复

            }

            @Override
            public void onAnimationEnd(Animator arg0) {// 动画结束
                ViewParent parent = mTvDes.getParent();
                while (true) {
                    parent = parent.getParent();
                    if (parent == null) {// 已经没有父亲
                        break;
                    }
                    if (parent instanceof ScrollView) {// 已经找到
                        ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
                        break;
                    }

                }
            }

            @Override
            public void onAnimationCancel(Animator arg0) {// 动画取消

            }
        });
    }

    /**
     * @param i    指定行高
     * @param data 指定textView的内容
     * @return
     */
    private int getShortHeight(int i, AppInfoBean data) {
        //临时textView,只做测绘用
        TextView tempTextView = new TextView(mContext);
        tempTextView.setLines(i);
        tempTextView.setText(data.des);
        tempTextView.setTextSize(15);
        tempTextView.measure(0, 0);

        int measuredHeight = tempTextView.getMeasuredHeight();

        return measuredHeight;
    }

}
