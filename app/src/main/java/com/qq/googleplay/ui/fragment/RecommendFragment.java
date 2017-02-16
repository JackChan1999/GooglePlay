package com.qq.googleplay.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.base.LoadingPager;
import com.qq.googleplay.net.Protocol.RecommendProtocol;
import com.qq.googleplay.ui.widget.flyoutin.ShakeListener;
import com.qq.googleplay.ui.widget.flyoutin.StellarMap;
import com.qq.googleplay.utils.CommonUtil;

import java.util.List;
import java.util.Random;

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
public class RecommendFragment extends BaseFragment {

    private List<String>  mDatas;
    private ShakeListener mShakeListener;

    @Override
    protected View initSuccessView() {

        // 返回成功的视图
        final StellarMap stellarMap = new StellarMap(CommonUtil.getContext());

        // 设置adapter
        final RecommendAdapter adapter = new RecommendAdapter();
        stellarMap.setAdapter(adapter);

        // 设置第一页的时候显示
        stellarMap.setGroup(0, true);
        // 设置把屏幕拆分成多少个格子
        stellarMap.setRegularity(15, 20);// 总的就有300个格子

        mShakeListener = new ShakeListener(CommonUtil.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake() {
                int groupIndex = stellarMap.getCurrentGroup();
                if (groupIndex == adapter.getGroupCount() - 1) {
                    groupIndex = 0;
                } else {
                    groupIndex++;
                }
                stellarMap.setGroup(groupIndex, true);
            }
        });
        return stellarMap;
    }

    @Override
    protected LoadingPager.LoadedResult initData() {
        RecommendProtocol protocol = new RecommendProtocol();
        try {
            mDatas = protocol.loadData(0);
            return checkState(mDatas);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    @Override
    public void onResume() {
        if (mShakeListener != null) {
            mShakeListener.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mShakeListener != null) {
            mShakeListener.pause();
        }
        super.onPause();
    }

    class RecommendAdapter implements StellarMap.Adapter {
        private static final int	PAGER_SIZE	= 15;	// 每页显示多少条数据

        @Override
        public int getGroupCount() {// 有多少组
            int groupCount = mDatas.size() / PAGER_SIZE;
            // 如果不能整除,还有余数的情况
            if (mDatas.size() % PAGER_SIZE > 0) {// 有余数
                groupCount++;
            }
            return groupCount;
        }

        @Override
        public int getCount(int group) {// 每组有多少条数据
            if (group == getGroupCount() - 1) {// 来到了最后一组
                // 是否有余数
                if (mDatas.size() % PAGER_SIZE > 0) {// 有余数
                    return mDatas.size() % PAGER_SIZE;// 返回具体的余数值就可以
                }
            }
            return PAGER_SIZE;// 0-15
        }

        @Override
        public View getView(int group, int position, View convertView) {// 返回具体的view
            TextView tv = new TextView(CommonUtil.getContext());
            // group:代表第几组
            // position:几组中的第几个位置
            int index = group * PAGER_SIZE + position;
            tv.setText(mDatas.get(index));

            // random对象
            Random random = new Random();
            // 随机大小
            tv.setTextSize(random.nextInt(6) + 15);// 15-21
            // 随机的颜色
            int alpha = 255;//
            int red = random.nextInt(180) + 30;// 30-210
            int green = random.nextInt(180) + 30;
            int blue = random.nextInt(180) + 30;
            int argb = Color.argb(alpha, red, green, blue);
            tv.setTextColor(argb);

            int padding = CommonUtil.dip2Px(5);
            tv.setPadding(padding, padding, padding, padding);

            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }

    }
}
