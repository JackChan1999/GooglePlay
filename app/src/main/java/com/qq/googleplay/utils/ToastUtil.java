package com.qq.googleplay.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseApplication;
import com.qq.googleplay.ui.widget.CustomToast;

/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-2-21 上午10:47:56
 * 
 * 描 述 ：
 * 		土司工具类
 * 
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class ToastUtil {

	private static CustomToast mToast = null;
	private static Context mContext = CommonUtil.getContext();

	public static void notifyShort(String text) {
		if (mContext != null) {
			mToast = new CustomToast(mContext);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.setErr(false);
		mToast.setMessage(text);
		mToast.show();
	}

	public static void notifyShort(int textId) {
		if (mContext != null) {
			mToast = new CustomToast(mContext);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.setErr(false);
		mToast.setMessageById(textId);
		mToast.show();
	}

	public static void showToast(String text) {
		if (mToast != null) {
			mToast.cancel();
		}
		if (mContext != null) {
			mToast = new CustomToast(mContext);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.setMessage(text);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public static void showToast(String text, int position, int xOffset, int yOffset) {
		if (mToast != null) {
			mToast.cancel();
		}
		if (mContext != null) {
			mToast = new CustomToast(mContext);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.setMessage(text);
		mToast.setGravity(position, xOffset, yOffset);
		mToast.show();
	}

	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

	public static void showCentorTextToast(Context context, String text) {
		if (mToast != null) {
			mToast.cancel();
		}
		View layout = LayoutInflater.from(context).inflate(R.layout.toast_center_text,
				(ViewGroup) ((Activity) context).findViewById(R.id.toast_layout_root));
		((TextView) layout.findViewById(R.id.text)).setText(text);
		Toast toast = new Toast(context);
		toast.setGravity(16, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	public static void showNormalToast(int textResId) {
		if (mToast != null) {
			mToast.cancel();
		}
		if (mContext != null) {
			mToast = new CustomToast(mContext);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.setMessageById(textResId);
		mToast.show();
	}

	public static void showToast(int txtId) {
		if (mToast != null) {
			mToast.cancel();
		}
		if (mContext != null) {
			mToast = new CustomToast(mContext);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.setMessageById(txtId);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public static void showToastString(String txt) {
		if (mToast != null) {
			mToast.cancel();
		}
		if (mContext != null) {
			mToast = new CustomToast(mContext);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.setMessage(txt);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public static void notifyShortNormal(Context context, int textId) {
		if (mToast != null) {
			mToast.cancel();
		}
		if (context != null) {
			mToast = new CustomToast(context);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.setMessageById(textId);
		mToast.show();
	}

	public static void notifyShortNormal(Context context, String msg) {
		if (mToast != null) {
			mToast.cancel();
		}
		if (context != null) {
			mToast = new CustomToast(context);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.setMessage(msg);
		mToast.show();
	}

	public static void notifyLong(String text) {
		if (CommonUtil.getContext() != null) {
			mToast = new CustomToast(CommonUtil.getContext());
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.setErr(false);
		mToast.setMessage(text);
		mToast.show();
	}

	public static void notifyLong(int textId) {
		if (CommonUtil.getContext() != null) {
			mToast = new CustomToast(CommonUtil.getContext());
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.setErr(false);
		mToast.setMessageById(textId);
		mToast.show();
	}

	public static void notifyToast(int textId, int time) {
		if (CommonUtil.getContext() != null) {
			mToast = new CustomToast(CommonUtil.getContext());
			mToast.setDuration(time);
		}
		mToast.setErr(false);
		mToast.setMessageById(textId);
		mToast.show();
	}

	public static void show4dToast(Context context, String text) {
		if (mToast != null) {
			mToast.cancel();
		}
		View layout = LayoutInflater.from(context).inflate(R.layout.toast_center_text,
				(ViewGroup) ((Activity) context).findViewById(R.id.toast_layout_root));
		((TextView) layout.findViewById(R.id.text)).setText(text);
		Toast toast = new Toast(context);
		toast.setGravity(48, 0, 100);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	public static void toast(final Activity activity, final String content, final int duration) {
		if (content == null){
			return;
		}else {
			if (Thread.currentThread().getName().equals("main")) {
				BaseApplication.ToastMabager.builder.display(content,duration);
			} else {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						BaseApplication.ToastMabager.builder.display(content,duration);
					}
				});
			}
		}

	}

	public static void toast(String content){
		if (content == null){
			return;
		}else {
			BaseApplication.ToastMabager.builder.display(content, Toast.LENGTH_SHORT);
		}
	}
}
