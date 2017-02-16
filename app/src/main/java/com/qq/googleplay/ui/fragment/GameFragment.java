package com.qq.googleplay.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseAdapter;
import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.base.LoadingPager;
import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.net.Protocol.GameProtocol;
import com.qq.googleplay.ui.activity.DetailActivity;
import com.qq.googleplay.ui.adapter.recyclerview.OnItemClickListener;
import com.qq.googleplay.utils.CommonUtil;

import java.util.List;

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
public class GameFragment extends BaseFragment<AppInfoBean> {

    private GameProtocol mGameProtocol;
    private RecyclerView mRecyclerView;
    private GameAdapter mGameAdapter;
    private final int viewType = Integer.MAX_VALUE / 2 + 2;

    @Override
    protected View initSuccessView() {

        mGameAdapter = new GameAdapter(mActivity, R.layout.item_gamelist, mData);
        //mAdapter = new RecyclerViewAdapter(AdapterUtils.getScaleInAdapter(mGameAdapter,mRecyclerView));
        //int[] padding = new int[]{0, UIUtils.dip2Px(5), 0, 0};
        mRecyclerView = setupRecyclerView(mGameAdapter, Color.parseColor("#eeeeee"), null);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(viewType,10);
        initListener();

        return mRecyclerView;
    }

    private void initListener() {
        mGameAdapter.setOnItemClickListener(new OnItemClickListener<AppInfoBean>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, AppInfoBean appInfoBean, int position) {
                goToDetailActivity(mData.get(position).packageName);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, AppInfoBean appInfoBean, int position) {
                return false;
            }
        });
    }

    private void goToDetailActivity(String packageName) {
        Intent intent = new Intent(CommonUtil.getContext(), DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", packageName);
        CommonUtil.getContext().startActivity(intent);
    }

    @Override
    protected LoadingPager.LoadedResult initData() {
        mGameProtocol = new GameProtocol();
        try {
            mData = mGameProtocol.loadData(0);
            return checkState(mData);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    private class GameAdapter extends BaseAdapter {

        public GameAdapter(Context context, int layoutId, List<AppInfoBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public int getItemViewType(int position) {
            return 2;
        }
    }

    @Override
    public List<AppInfoBean> onLoadMore() throws Throwable {
        return mGameProtocol.loadData(mData.size());
    }
}
