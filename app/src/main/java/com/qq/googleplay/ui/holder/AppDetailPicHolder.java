package com.qq.googleplay.ui.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.bean.AppInfoBean;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.ui.activity.ImageScaleActivity;
import com.qq.googleplay.ui.widget.RatioLayout;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.bitmap.BitmapHelper;

import java.util.ArrayList;
import java.util.List;

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
public class AppDetailPicHolder extends BaseHolder<AppInfoBean> implements View.OnClickListener {

	@Bind(R.id.app_detail_pic_iv_container)
	LinearLayout	mContainerPic;

	List<String> picUrls;

	public AppDetailPicHolder(Context context) {
		super(context);
	}

	@Override
	public View initHolderView() {
		View view = mInflater.inflate(R.layout.item_app_detail_pic, null,false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		picUrls = data.screen;

		RatioLayout.LayoutParams picParams = new RatioLayout.LayoutParams(
				RatioLayout.LayoutParams.MATCH_PARENT,RatioLayout.LayoutParams.MATCH_PARENT);

		for (int i = 0; i < picUrls.size(); i++) {
			String url = picUrls.get(i);
			ImageView ivPic = new ImageView(mContext);
			ivPic.setLayoutParams(picParams);
			ivPic.setOnClickListener(this);
			ivPic.setId(i);

			BitmapHelper.instance.init(mContext).display(ivPic,
					RequestConstants.URLS.IMAGEBASEURL + url);

			// 控件宽度等于屏幕的1/3
			int widthPixels = CommonUtil.getResources().getDisplayMetrics().widthPixels;
			widthPixels = widthPixels - mContainerPic.getPaddingLeft() - mContainerPic.getPaddingRight();

			int childWidth = widthPixels / 3;
			// 已知控件的宽度,和图片的宽高比,去动态的计算控件的高度
			RatioLayout rl = new RatioLayout(mContext);
			rl.setRatio((float) (150*1.0 / 250));// 图片的宽高比
			//rl.setRelative(RatioLayout.RELATIVE_WIDTH);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(childWidth,
					LayoutParams.WRAP_CONTENT);

			rl.addView(ivPic);

			if (i != 0) {// 不处理第一张图片
				params.leftMargin = CommonUtil.dip2Px(5);
			}

			mContainerPic.addView(rl, params);
			
		}
	}

	@Override
	public void onClick(View v) {
		enterImageScaleActivity(v.getId());
	}

	/**
	 * 进入到图片缩放的页面
	 * @param position 位置
	 */
	private void enterImageScaleActivity(int position) {
		Intent intent = new Intent(mContext,ImageScaleActivity.class);
		intent.putExtra("position", position);
		//传入一个集合
		intent.putStringArrayListExtra("imageUrls", (ArrayList<String>) picUrls);
		mContext.startActivity(intent);
	}

}
