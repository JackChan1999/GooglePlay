package com.qq.googleplay.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.qq.googleplay.R;
import com.qq.googleplay.factory.ThreadPoolFactory;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.UIUtils;

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
public abstract class LoadingPager extends FrameLayout {

    public final static int STATE_NONE = -1;    //默认情况
    public final static int STATE_LOADING = 0;  //加载中
    public final static int STATE_ERROR = 1;    //加载错误
    public final static int STATE_EMPTY = 2;    //空页面
    public final static int STATE_SUCCESS = 3;  //加载数据成功
    public int mCurState = STATE_NONE;          //当前状态

    //四种状态对应的view
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;

    ImageView mIvLoading;

    private AnimationDrawable mAnimationDrawable;

    public LoadingPager(Context context) {
        super(context);
       initCommonView(context);
    }

    /**
    * @des 初始化常规视图
    * @call loadingpager初始化的时候
    */
    private void initCommonView(Context context) {

        //加载中页面
        mLoadingView = UIUtils.inflate(context,R.layout.loadpage_loading,null,false);
        mIvLoading = ButterKnife.findById(mLoadingView,R.id.iv_loading);
        addView(mLoadingView);

        //mIvLoading.setBackgroundResource(R.drawable.loading_animation);
        mAnimationDrawable = (AnimationDrawable) mIvLoading.getDrawable();

        //加载错误页面
        mErrorView = UIUtils.inflate(context,R.layout.loadpage_error,null,false);
        mErrorView.findViewById(R.id.page_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();//重新触发加载数据
            }
        });
        addView(mErrorView);

        //加载为空页面
        mEmptyView = UIUtils.inflate(context, R.layout.loadpage_empty, null, false);
        addView(mEmptyView);

        refreshUI();

    }

    /**
    * @desc 根据不同的状态显示不同的view
    * @call 1、loadingpager正在初始化的时候
    * @call 2、显示正在加载数据
    * @call 3、正在加载数据完成的时候
    */
    private void refreshUI() {
        CommonUtil.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                //控制loadingview视图的显示隐藏
                if (mCurState == STATE_LOADING || mCurState == STATE_NONE) {
                    mLoadingView.setVisibility(View.VISIBLE);
                    mAnimationDrawable.start();
                } else {
                    mLoadingView.setVisibility(View.GONE);
                    mAnimationDrawable.stop();
                }

       /* mLoadingView.setVisibility((mCurState == STATE_LOADING || mCurState == STATE_NONE) ? View
                .VISIBLE : View.INVISIBLE);*/

                //控制errorview视图的显示隐藏
                mErrorView.setVisibility((mCurState == STATE_ERROR) ? View
                        .VISIBLE : View.GONE);

                //控制emptyview视图的显示隐藏
                mEmptyView.setVisibility((mCurState == STATE_EMPTY) ? View
                        .VISIBLE : View.GONE);

                if (mSuccessView == null && mCurState == STATE_SUCCESS) {
                    mSuccessView = initSuccessView();
                    addView(mSuccessView);
                }

                if (mSuccessView != null) {
                    mSuccessView.setVisibility((mCurState == STATE_SUCCESS) ? View.VISIBLE : View.GONE);
                }
            }
        });

    }

    /**
     * 加载数据的流程
     * 1、触发加载数据：进入页面开始加载或点击某一个按钮的时候开始加载
     * 2、异步加载数据 -->显示加载视图
     * 3、处理加载结果
     * （1）成功，显示成功视图
     * （2）失败
     *      数据为空，显示空视图
     *      数据加载失败，显示加载失败的视图
     *
     * @desc 触发加载数据
     * @call 暴露给外界调用，其实就是外界 触发加载数据
     */
    public void loadData() {

        //如果加载成功，则无需再次加载
        if (mCurState != STATE_SUCCESS && mCurState != STATE_LOADING) {

            //保证每次加载的时候一定是加载中视图，而不是上次加载的mCurState
            mCurState = STATE_LOADING;

            refreshUI();//显示正在加载数据

            ThreadPoolFactory.getNormalPool().execute(new LoadDataTask());
        }

    }

    private class LoadDataTask implements Runnable{

        @Override
        public void run() {
            //异步加载数据
            LoadedResult tempstate = initData();
            //处理加载结果
            mCurState = tempstate.getState();
            CommonUtil.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    refreshUI();//刷新ui，正在加载数据完成的时候
                }
            });
        }
    }

    /**
    * @desc 真正加载数据
    * @call loadData()被调用的时候
    *
    **/
    protected abstract LoadedResult initData();

    /**
     * @return
     * @desc 创建成功视图
     * @call 正在加载数据完成后，并且数据加载成功，创建成功视图
     */
    public abstract View initSuccessView();


    /**
     * 用枚举保存加载数据的3种状态
     */

    public enum LoadedResult {
        SUCCESS(STATE_SUCCESS),
        EMPTY(STATE_EMPTY),
        ERROR(STATE_ERROR);

        int state;

        private LoadedResult(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
