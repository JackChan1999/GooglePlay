package com.qq.googleplay.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.base.LoadingPager;
import com.qq.googleplay.net.Protocol.CategoryProtocol;
import com.qq.googleplay.ui.widget.FlexboxLayout;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/20 19:39
 * 描 述 ： 分类页面
 * 修订历史 ：
 * ============================================================
 **/
public class CategoryFragment extends BaseFragment {

    private CategoryProtocol mCategoryProtocol;

    @Override
    protected View initSuccessView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_category,null,false);
        FlexboxLayout flexbox = (FlexboxLayout) view.findViewById(R.id.fbl);
        return view;
    }

    @Override
    protected LoadingPager.LoadedResult initData() {
        mCategoryProtocol = new CategoryProtocol();
        try {
            mData = mCategoryProtocol.loadData(0);
            return checkState(mData);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

   /* class CategoryAdapter extends SuperBaseAdapter<CategoryInfoBean> {

        public CategoryAdapter(AbsListView listView, List<CategoryInfoBean> dataSource) {
            super(listView, dataSource);
        }

        @Override
        public BaseHolder<CategoryInfoBean> getHolder(int position) {

            CategoryInfoBean infoBean = mData.get(position);
            if (infoBean.isTitle) {
                return new CategoryTitleHolder(mActivity);
            } else {
                return new CategoryItemHolder(mActivity);
            }
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getNormalViewType(int position) {

            CategoryInfoBean infoBean = mData.get(position);
            if (infoBean.isTitle) {
                return super.getNormalViewType(position) + 1;
            } else {
                return super.getNormalViewType(position);
            }
        }
    }*/

}
