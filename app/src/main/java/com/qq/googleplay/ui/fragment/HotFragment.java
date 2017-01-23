package com.qq.googleplay.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qq.googleplay.net.Protocol.HotProtocol;
import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.base.LoadingPager;
import com.qq.googleplay.ui.widget.FlowLayout;
import com.qq.googleplay.utils.CommonUtil;

import java.util.List;
import java.util.Random;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/20 19:41
 * 描 述 ： 排行页面
 * 修订历史 ：
 * ============================================================
 **/
public class HotFragment extends BaseFragment {


    private HotProtocol mHotProtocol;
    private List<String> mData;
    private ScrollView mScrollView;

   // @ViewInject(R.id.flowlayout)
    private FlowLayout mFlowLayout;

    //@ViewInject(R.id.scrollview)
    //private ReboundScrollView mScrollView;

    @Override
    protected View initSuccessView() {
      /* View view = UIUtils.inflate(R.layout_cb.layout_hot);
        x.view().inject(this,view);*/
        // 返回成功的视图
        mScrollView = new ScrollView(CommonUtil.getContext());

       // mScrollView = new ReboundScrollView(UIUtils.getContext());

       mScrollView.setBackgroundColor(Color.parseColor("#eaeaea"));

       /* FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mScrollView.setLayoutParams(layoutParams);*/

       mFlowLayout = new FlowLayout(CommonUtil.getContext());
        int padding = CommonUtil.dip2Px(10);
        mFlowLayout.setPadding(padding, padding, padding, padding);
        mFlowLayout.setSpace(CommonUtil.dip2Px(10), CommonUtil.dip2Px(15));
        for (final String data : mData) {

            TextView textView = new TextView(CommonUtil.getContext());
            int tvPadding = CommonUtil.dip2Px(10);
            textView.setPadding(CommonUtil.dip2Px(15), tvPadding, CommonUtil.dip2Px(15), tvPadding);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(16);
            textView.setText(data);
            textView.setTextColor(Color.WHITE);

            Random random = new Random();//Math.random()
            int alpha = 255;
            int green = random.nextInt(190) + 30;
            int red = random.nextInt(190) + 30;
            int blue = random.nextInt(190) + 30;
            int argb = Color.argb(alpha, red, green, blue);

            //设置shape
            GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setCornerRadius(CommonUtil.dip2Px(6));
            normalDrawable.setColor(argb);

            GradientDrawable pressedDrawable = new GradientDrawable();
            pressedDrawable.setColor(Color.DKGRAY);
            pressedDrawable.setCornerRadius(CommonUtil.dip2Px(5));


            //设置选择器selector
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            stateListDrawable.addState(new int[]{}, normalDrawable);

            textView.setBackgroundDrawable(stateListDrawable);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast(data);
                }
            });
            mFlowLayout.addView(textView);
        }

        mScrollView.addView(mFlowLayout);
        return mScrollView;

    }

    @Override
    protected LoadingPager.LoadedResult initData() {
        mHotProtocol = new HotProtocol();
        try {
            mData = mHotProtocol.loadData(0);
            return checkState(mData);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }

    }
}
