package com.qq.googleplay.ui.holder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.bitmap.BitmapHelper;

import java.util.List;

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

public class AppDetailSafeHolder extends BaseHolder<AppInfoBean> implements OnClickListener {
    @Bind(R.id.app_detail_safe_pic_container)
    LinearLayout mContainerPic;

    @Bind(R.id.app_detail_safe_des_container)
    LinearLayout mContainerDes;

    @Bind(R.id.app_detail_safe_iv_arrow)
    ImageView mIvArrow;

    private boolean isOpen = true;

    public AppDetailSafeHolder(Context context) {
        super(context);
    }

    @Override
    public View initHolderView() {
        View view = mInflater.inflate(R.layout.item_app_detail_safe, null,false);
        ButterKnife.bind(this, view);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshHolderView(AppInfoBean data) {
        List<AppInfoBean.AppInfoSafeBean> safeBeans = data.safe;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.dip2Px
                (16), CommonUtil.dip2Px(16));
        params.rightMargin = CommonUtil.dip2Px(3);
        params.gravity = Gravity.CENTER_VERTICAL;

        LinearLayout.LayoutParams iconparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtil.dip2Px(20));
        int margin = CommonUtil.dip2Px(5);
        iconparams.setMargins(margin,0,margin,0);

        for (AppInfoBean.AppInfoSafeBean appInfoSafeBean : safeBeans) {
            ImageView ivIcon = new ImageView(mContext);
            ivIcon.setLayoutParams(iconparams);
            BitmapHelper.instance.init(mContext).display(ivIcon,
                    RequestConstants.URLS.IMAGEBASEURL+appInfoSafeBean.safeUrl);
            mContainerPic.addView(ivIcon);

            LinearLayout ll = new LinearLayout(mContext);

            // 描述图标
            ImageView ivDes = new ImageView(mContext);
            ivDes.setLayoutParams(params);
            BitmapHelper.instance.init(mContext).display(ivDes,
                    RequestConstants.URLS.IMAGEBASEURL + appInfoSafeBean.safeDesUrl);

            // 描述内容
            TextView tvDes = new TextView(mContext);
            tvDes.setText(appInfoSafeBean.safeDes);
            if (appInfoSafeBean.safeDesColor == 0) {
                tvDes.setTextColor(CommonUtil.getColor(R.color.app_detail_safe_normal));
            } else {
                tvDes.setTextColor(CommonUtil.getColor(R.color.app_detail_safe_warning));
            }

            tvDes.setGravity(Gravity.CENTER);
            // 加点间距
            int padding = CommonUtil.dip2Px(5);
            ll.setPadding(padding, 0, padding, padding);

            ll.addView(ivDes);
            ll.addView(tvDes);

            mContainerDes.addView(ll);

        }
        // 默认折叠
        toggle(false);
    }

    @Override
    public void onClick(View v) {
        toggle(true);
    }

    private void toggle(boolean isAnimation) {
        if (isOpen) {// 折叠
            mContainerDes.measure(0, 0);
            int measuredHeight = mContainerDes.getMeasuredHeight();
            int start = measuredHeight;// 动画的开始高度
            int end = 0;// 动画的结束高度
            if (isAnimation) {
                doAnimation(start, end);
            } else {// 直接修改高度
                LayoutParams params = mContainerDes.getLayoutParams();
                params.height = end;
                mContainerDes.setLayoutParams(params);
            }
        } else {// 展开
            mContainerDes.measure(0, 0);
            int measuredHeight = mContainerDes.getMeasuredHeight();
            int end = measuredHeight;// 动画的开始高度
            int start = 0;// 动画的结束高度
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                LayoutParams params = mContainerDes.getLayoutParams();
                params.height = end;
                mContainerDes.setLayoutParams(params);
            }
        }
        // 箭头的旋转动画
        if (isAnimation) {// 有折叠动画的时候
            if (isOpen) {
                ObjectAnimator.ofFloat(mIvArrow, "rotation", 180, 0).start();
            } else {
                ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();
            }
        }

        isOpen = !isOpen;
    }

    public void doAnimation(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.start();// 开始动画
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator value) {
                int height = (Integer) value.getAnimatedValue();
                // 通过layoutParams,修改高度
                LayoutParams params = mContainerDes.getLayoutParams();
                params.height = height;
                mContainerDes.setLayoutParams(params);
            }
        });
    }

}
