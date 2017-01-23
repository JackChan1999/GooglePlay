package com.qq.googleplay.ui.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.bean.CategoryInfoBean;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.utils.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/4/1 13:35
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class CategoryItemHolder extends BaseHolder<CategoryInfoBean> {
    @Bind(R.id.item_category_item_1)
    LinearLayout mContainerItem1;

    @Bind(R.id.item_category_item_2)
    LinearLayout mContainerItem2;

    @Bind(R.id.item_category_item_3)
    LinearLayout mContainerItem3;

    @Bind(R.id.item_category_icon_1)
    ImageView mIvIcon1;

    @Bind(R.id.item_category_icon_2)
    ImageView mIvIcon2;

    @Bind(R.id.item_category_icon_3)
    ImageView mIvIcon3;

    @Bind(R.id.item_category_name_1)
    TextView mTvName1;

    @Bind(R.id.item_category_name_2)
    TextView mTvName2;

    @Bind(R.id.item_category_name_3)
    TextView mTvName3;

    public CategoryItemHolder(Context context) {
        super(context);
    }

    @Override
    public View initHolderView() {
        View view = mInflater.inflate(R.layout.item_category_info,null,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void refreshHolderView(CategoryInfoBean data) {

        setData(data.name1, data.url1, mTvName1, mIvIcon1);
        setData(data.name2, data.url2, mTvName1, mIvIcon2);
        setData(data.name3, data.url3, mTvName3, mIvIcon3);
    }

    private void setData(final String name, String url, TextView tvName, ImageView ivIcon) {
        if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(url)) {
            tvName.setText(name);
            ivIcon.setImageResource(R.mipmap.ic_default);
            Glide.with(CommonUtil.getContext()).load(RequestConstants.URLS.IMAGEBASEURL + url).into(ivIcon);
            ((ViewGroup) tvName.getParent()).setVisibility(View.VISIBLE);
            ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CommonUtil.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ((ViewGroup) tvName.getParent()).setVisibility(View.INVISIBLE);
        }
    }
}
