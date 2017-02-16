package com.qq.googleplay.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseActivity;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.ui.recyclerview.cardgallery.CardAdapterHelper;
import com.qq.googleplay.ui.recyclerview.cardgallery.CardScaleHelper;
import com.qq.googleplay.ui.widget.photoview.PhotoView;
import com.qq.googleplay.utils.ViewSwitchUtils;
import com.qq.googleplay.utils.bitmap.BitmapHelper;
import com.qq.googleplay.utils.bitmap.BitmapUtils;

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
public class CardGalleryActivity extends BaseActivity{

	private List<String>   mDatas;
	//private ViewPagerFixed pager;
	private int            mPostion;
	private int mLastPos = -1;

	private RecyclerView mRecyclerView;
	private ImageView    mBlurView;
	private CardScaleHelper mCardScaleHelper = null;

	private Runnable mBlurRunnable;

	@Override
	protected void onBeforeSetContentLayout() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
	}

	@Override
	public void initView() {
		setContentView(R.layout.layout_app_detail);
		//pager = (ViewPagerFixed) findViewById(pager);

		mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		mRecyclerView.setLayoutManager(linearLayoutManager);
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		if(intent != null){
			mDatas = intent.getStringArrayListExtra("imageUrls");
			mPostion = intent.getIntExtra("position", 0);
		}
		/*ImageScaleAdapter adapter = new ImageScaleAdapter();
		pager.setAdapter(adapter);
		pager.setCurrentItem(mPostion,true);*/

		mRecyclerView.setAdapter(new CardAdapter());
		// mRecyclerView绑定scale效果
		mCardScaleHelper = new CardScaleHelper();
		mCardScaleHelper.setCurrentItemPos(2);
		mCardScaleHelper.attachToRecyclerView(mRecyclerView);

		initBlurBackground();
	}
	private void initBlurBackground() {
		mBlurView = (ImageView) findViewById(R.id.blurView);
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					notifyBackgroundChange();
				}
			}
		});

		notifyBackgroundChange();
	}

	private void notifyBackgroundChange() {
		if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
		mLastPos = mCardScaleHelper.getCurrentItemPos();
		//final int resId = mDatas.get(mCardScaleHelper.getCurrentItemPos());
		final String url = RequestConstants.URLS.IMAGEBASEURL + mDatas.get(mCardScaleHelper.getCurrentItemPos());

		mBlurView.removeCallbacks(mBlurRunnable);
		mBlurRunnable = new Runnable() {
			@Override
			public void run() {
				//Glide.with(CardGalleryActivity.this).load(url).asBitmap().into(mTarget);
				//Bitmap bitmap = Glide.with(CardGalleryActivity.this).load(url).asBitmap().into(width, height).get();
				//ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap, 15));
			}
		};
		mBlurView.postDelayed(mBlurRunnable, 500);
	}

	private SimpleTarget mTarget = new SimpleTarget<Bitmap>(){

		@Override
		public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
			ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BitmapUtils.blur(CardGalleryActivity.this,resource));
		}
	};

	private class ImageScaleAdapter extends PagerAdapter{

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			PhotoView photoView = new PhotoView(CardGalleryActivity.this);
			photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			BitmapHelper.instance.init(CardGalleryActivity.this).display(photoView,
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

	class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
		//private List<Integer> mList = new ArrayList<>();
		private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

		/*public CardAdapter(List<Integer> mList) {
			this.mList = mList;
		}*/

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
			mCardAdapterHelper.onCreateViewHolder(parent, itemView);
			return new ViewHolder(itemView);
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, final int position) {
			mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
			BitmapHelper.instance.init(CardGalleryActivity.this).display(holder.mImageView,
					RequestConstants.URLS.IMAGEBASEURL + mDatas.get(position));
			/*holder.mImageView.setImageResource(mList.get(position));
			holder.mImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ToastUtils.show(holder.mImageView.getContext(), "" + position);
				}
			});*/
		}

		@Override
		public int getItemCount() {
			return mDatas.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public final ImageView mImageView;

			public ViewHolder(final View itemView) {
				super(itemView);
				mImageView = (ImageView) itemView.findViewById(R.id.imageView);
			}

		}

	}

}
