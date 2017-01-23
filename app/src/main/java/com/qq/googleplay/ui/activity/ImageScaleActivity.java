package com.qq.googleplay.ui.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseActivity;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.ui.widget.ViewPagerFixed;
import com.qq.googleplay.ui.widget.photoview.PhotoView;
import com.qq.googleplay.utils.bitmap.BitmapHelper;

import java.util.List;


public class ImageScaleActivity extends BaseActivity{

	private List<String>   mDatas;
	private ViewPagerFixed pager;
	private int            mPostion;

	@Override
	protected void onBeforeSetContentLayout() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
	}

	@Override
	public void initView() {
		setContentView(R.layout.image_scale_activity);
		pager = (ViewPagerFixed) findViewById(R.id.pager);
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		if(null != intent){
			mDatas = intent.getStringArrayListExtra("imageUrls");
			mPostion = intent.getIntExtra("position", 0);
		}
		ImageScaleAdapter adapter = new ImageScaleAdapter();
		pager.setAdapter(adapter);
		pager.setCurrentItem(mPostion,true);
	}
	
	private class ImageScaleAdapter extends PagerAdapter{

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			PhotoView photoView = new PhotoView(ImageScaleActivity.this);
			photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			BitmapHelper.instance.init(ImageScaleActivity.this).display(photoView,
					RequestConstants.URLS.IMAGEBASEURL + mDatas.get(position));
			container.addView(photoView);
			
			return photoView;
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
	}

}
