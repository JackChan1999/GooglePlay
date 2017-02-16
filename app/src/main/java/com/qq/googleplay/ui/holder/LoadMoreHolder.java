package com.qq.googleplay.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.base.SuperBaseAdapter;
import com.qq.googleplay.ui.animation.listviewanim.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.qq.googleplay.ui.widget.LoadingFooter;

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
public class LoadMoreHolder extends BaseHolder<LoadingFooter.State> {

    private LoadingFooter mFooter;

    public LoadMoreHolder(Context context) {
        super(context);
    }


    @Override
    public View initHolderView() {
        mFooter = new LoadingFooter(mContext);
        return mFooter;
    }

    @Override
    public void refreshHolderView(LoadingFooter.State state) {
        mFooter.setState(state);
        if (state == LoadingFooter.State.NetWorkError) {
            mFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFooter.setState(LoadingFooter.State.Loading);
                    ListView lv = (ListView) mFooter.getParent();
                    SwingBottomInAnimationAdapter adapter = (SwingBottomInAnimationAdapter)lv.getAdapter();
                    SuperBaseAdapter baseAdapter = (SuperBaseAdapter) adapter.getDecoratedBaseAdapter();
                    baseAdapter.perFormLoadMore();
                }
            });
        }
    }
}
