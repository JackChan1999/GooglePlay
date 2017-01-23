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
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/25 10:00
 * 描 述 ：加载更多holder
 * 修订历史 ：
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
