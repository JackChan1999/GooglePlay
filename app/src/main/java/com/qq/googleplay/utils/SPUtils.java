package com.qq.googleplay.utils;

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
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtils {
	private static final String SP_NAME = "config";
	private static SharedPreferences sp;
	//PreferenceManager.getDefaultSharedPreferences();
	public static final String FILE_NAME = "share_data";

	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		private SharedPreferencesCompat() {
		}

		private static Method findApplyMethod() {
			try {
				return SharedPreferences.Editor.class.getMethod("apply", new Class[0]);
			} catch (NoSuchMethodException e) {
				return null;
			}
		}

		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor, new Object[0]);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e2) {
			} catch (InvocationTargetException e3) {
			}
			editor.commit();
		}
	}

	public static void put(Context context, String fileName, String key, Object object) {
		put(false, fileName, key, object);
	}

	public static void put(Context context, String key, Object object) {
		put(false, FILE_NAME, key, object);
	}

	public static void putSyn(Context context, String key, Object object) {
		put(true, FILE_NAME, key, object);
	}

	public static void put(boolean isSyn, String fileName, String key, Object value) {
		if (value != null) {
			SharedPreferences.Editor editor = CommonUtil.getContext().getSharedPreferences(fileName, 4).edit();
			if (value instanceof String) {
				editor.putString(key, (String) value);
			} else if (value instanceof Integer) {
				editor.putInt(key, ((Integer) value).intValue());
			} else if (value instanceof Boolean) {
				editor.putBoolean(key, ((Boolean) value).booleanValue());
			} else if (value instanceof Float) {
				editor.putFloat(key, ((Float) value).floatValue());
			} else if (value instanceof Long) {
				editor.putLong(key, ((Long) value).longValue());
			} else {
				editor.putString(key, value.toString());
			}
			if (isSyn) {
				editor.commit();
			} else {
				SharedPreferencesCompat.apply(editor);
			}
		}
	}

	public static Object get(Context context, String key, Object defaultObject) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 4);
		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		}
		if (defaultObject instanceof Integer) {
			return Integer.valueOf(sp.getInt(key, ((Integer) defaultObject).intValue()));
		}
		if (defaultObject instanceof Boolean) {
			return Boolean.valueOf(sp.getBoolean(key, ((Boolean) defaultObject).booleanValue()));
		}
		if (defaultObject instanceof Float) {
			return Float.valueOf(sp.getFloat(key, ((Float) defaultObject).floatValue()));
		}
		if (defaultObject instanceof Long) {
			return Long.valueOf(sp.getLong(key, ((Long) defaultObject).longValue()));
		}
		return null;
	}

	public static Object get(String fileName, String key, Object defaultObject) {
		SharedPreferences sp = CommonUtil.getContext().getSharedPreferences(fileName, 4);
		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		}
		if (defaultObject instanceof Integer) {
			return Integer.valueOf(sp.getInt(key, ((Integer) defaultObject).intValue()));
		}
		if (defaultObject instanceof Boolean) {
			return Boolean.valueOf(sp.getBoolean(key, ((Boolean) defaultObject).booleanValue()));
		}
		if (defaultObject instanceof Float) {
			return Float.valueOf(sp.getFloat(key, ((Float) defaultObject).floatValue()));
		}
		if (defaultObject instanceof Long) {
			return Long.valueOf(sp.getLong(key, ((Long) defaultObject).longValue()));
		}
		return null;
	}

	public static void remove(Context context, String key) {
		SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, 0).edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	public static void remove(Context context, String fileName, String key) {
		SharedPreferences.Editor editor = context.getSharedPreferences(fileName, 0).edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	public static void clear(Context context) {
		SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, 0).edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	public static void clear(Context context, String fileName) {
		SharedPreferences.Editor editor = context.getSharedPreferences(fileName, 0).edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	public static boolean contains(Context context, String key) {
		return context.getSharedPreferences(FILE_NAME, 0).contains(key);
	}

	public static Map<String, ?> getAll(Context context) {
		return context.getSharedPreferences(FILE_NAME, 0).getAll();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}

		return sp.getBoolean(key, defValue);
	}

	public static int getInt(Context context, String key, int defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}

		return sp.getInt(key, defValue);
	}

	public static void putInt(Context context, String key, int value) {

		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}

		sp.edit().putInt(key, value).commit();

	}

	public static void saveBoolean(Context context, String key, boolean value) {

		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}

		sp.edit().putBoolean(key, value).commit();

	}

	// 保存string 类型的数据
	public static void saveString(Context context, String key, String value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putString(key, value).commit();
	}

	// 获取string 类型的数据
	public static String getString(Context context, String key, String defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getString(key, defValue);
	}
}
