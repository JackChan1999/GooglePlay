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
