package com.qq.googleplay.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseActivity;
import com.qq.googleplay.base.LoadingPager;
import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.manager.DownloadManager;
import com.qq.googleplay.net.Protocol.DetailProtocol;
import com.qq.googleplay.ui.holder.AppDetailBottomHolder;
import com.qq.googleplay.ui.holder.AppDetailDesHolder;
import com.qq.googleplay.ui.holder.AppDetailInfoHolder;
import com.qq.googleplay.ui.holder.AppDetailPicHolder;
import com.qq.googleplay.ui.holder.AppDetailSafeHolder;
import com.qq.googleplay.utils.CommonUtil;
import com.umeng.socialize.UMShareAPI;

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
public class DetailActivity extends BaseActivity {

    private String      mPackageName;
    private AppInfoBean mData;

    @Bind(R.id.app_detail_bottom)
    FrameLayout mContainerBottom;

    @Bind(R.id.app_detail_des)
    FrameLayout mContainerDes;

    @Bind(R.id.app_detail_info)
    FrameLayout mContainerInfo;

    @Bind(R.id.app_detail_pic)
    FrameLayout mContainerPic;

    @Bind(R.id.app_detail_safe)
    FrameLayout mContainerSafe;

    private LoadingPager mLoadingPager;

    private AppDetailBottomHolder	mAppDetailBottomHolder;

    @Override
    public void initView() {
        mPackageName = getIntent().getStringExtra("packageName");

        mLoadingPager = new LoadingPager(CommonUtil.getContext()) {

            @Override
            public View initSuccessView() {
                return onLoadSuccessView();
            }

            @Override
            public LoadedResult initData() {
                return onInitData();
            }

        };
        setContentView(mLoadingPager);
        if (Build.VERSION.SDK_INT > 21){
            getWindow().setStatusBarColor(Color.parseColor("#616161"));
        }
        mActionBar.setElevation(0);
        mActionBar.setTitle("应用详情");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public void initData() {
        // 触发加载数据
        mLoadingPager.loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private LoadingPager.LoadedResult onInitData() {
        // 发起网络请求
        DetailProtocol protocol = new DetailProtocol(mPackageName);
        try {
            mData = protocol.loadData(0);
            if (mData == null) {
                return LoadingPager.LoadedResult.ERROR;
            }
            return LoadingPager.LoadedResult.SUCCESS;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    private View onLoadSuccessView() {
        View view = View.inflate(this, R.layout.activity_detail, null);
        ButterKnife.bind(this, view);

        // 填充内容
        // 1.信息部分
        AppDetailInfoHolder appDetailInfoHolder = new AppDetailInfoHolder(this);
        mContainerInfo.addView(appDetailInfoHolder.getHolderView());
        appDetailInfoHolder.setDataAndRefreshHolderView(mData);

        // 2.安全部分
        AppDetailSafeHolder appDetailSafeHolder = new AppDetailSafeHolder(this);
        mContainerSafe.addView(appDetailSafeHolder.getHolderView());
        appDetailSafeHolder.setDataAndRefreshHolderView(mData);

        // 3.截图部分
        AppDetailPicHolder appDetailPicHolder = new AppDetailPicHolder(this);
        mContainerPic.addView(appDetailPicHolder.getHolderView());
        appDetailPicHolder.setDataAndRefreshHolderView(mData);

        // 4.描述部分
        AppDetailDesHolder appDetailDesHolder = new AppDetailDesHolder(this);
        mContainerDes.addView(appDetailDesHolder.getHolderView());
        appDetailDesHolder.setDataAndRefreshHolderView(mData);

        // 5.下载部分
        mAppDetailBottomHolder = new AppDetailBottomHolder(this);
        mContainerBottom.addView(mAppDetailBottomHolder.getHolderView());
        mAppDetailBottomHolder.setDataAndRefreshHolderView(mData);

        DownloadManager.getInstance().addObserver(mAppDetailBottomHolder);

        return view;
    }

    @Override
    protected void onPause() {// 界面不可见的时候移除观察者
        if (mAppDetailBottomHolder != null) {
            DownloadManager.getInstance().deleteObserver(mAppDetailBottomHolder);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {// 界面可见的时候重新添加观察者
        if (mAppDetailBottomHolder != null) {
            // 开启监听的时候,手动的去获取一下最新的状态
            mAppDetailBottomHolder.addObserverAndRerefresh();
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
