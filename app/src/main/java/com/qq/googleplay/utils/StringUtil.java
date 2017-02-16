package com.qq.googleplay.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
public class StringUtil {
	private static final String TAG = StringUtil.class.getSimpleName();
	public final static String encoding = "utf-8";

	public static String getString(int resId) {
		return CommonUtil.getResources().getString(resId);
	}

	/**得到String.xml中的字符串,带占位符*/
	public static String getString(int id, Object... formatArgs) {
		return CommonUtil.getResources().getString(id, formatArgs);
	}

	public static String[] getStringArr(int resId) {
		return CommonUtil.getResources().getStringArray(resId);
	}

	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static String formatStringPunctuation(Context c, String originalStr) {
		if (c == null || TextUtils.isEmpty(originalStr)) {
			return originalStr;
		}
		String period;
		boolean rtl;
		String comma;
		if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
			period = "。";
			comma = "，";
		} else {
			period = ".";
			comma = ",";
		}
		if (c.getResources().getConfiguration().getLayoutDirection() == 1) {
			rtl = true;
		} else {
			rtl = false;
		}
		boolean lastIsPeriod = rtl ? originalStr.indexOf(period) == 0 : originalStr.lastIndexOf(period)
				== originalStr.length() + -1;
		if (lastIsPeriod) {
			originalStr = rtl ? originalStr.substring(1) : originalStr.substring(0, originalStr.length() - 1);
		}
		return originalStr;
	}

	/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**返回一个高亮spannable*/
	public static CharSequence getHighLightText(String content, int color, int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**获取链接样式的字符串，即字符串下面有下划线*/
	public static Spanned getHtmlStyleString(int resId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<StorageVolume href=\"\"><u><b>").append(getString(resId)).append(" </b></u></StorageVolume>");
		return Html.fromHtml(sb.toString());
	}

	/** 格式化文件大小，不保留末尾的0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** 格式化文件大小，保留末尾的0，达到长度一致 */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)，保留一位小数
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)，个位四舍五入
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)，保留两位小数
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)，保留一位小数
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)，个位四舍五入
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
		}
		return size;
	}

	/**MD5加密url等字符串*/
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	/**字节数组转成16进制字符串*/
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static String avoidNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	public static String formatString(String format, Object... args) {
		try {
			return String.format(format, args);
		} catch (Exception e) {
			return "";
		}
	}

	private static int indexNumber(String text, StringBuilder builder) {
		int numberStart = -1;
		boolean allZeros = true;
		if (text != null) {
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if (c >= '0' && c <= '9') {
					if (numberStart == -1) {
						numberStart = i;
					}
					if (!(c == '0' && allZeros)) {
						builder.append(c);
					}
					if (c != '0') {
						allZeros = false;
					}
				} else if (numberStart >= 0) {
					if (builder.length() == 0) {
						builder.append('0');
					}
					return numberStart;
				}
			}
		}
		if (numberStart >= 0 && builder.length() == 0) {
			builder.append('0');
		}
		return numberStart;
	}

	private static char getHeadChar(String text) {
		try {
			if (isContainChinese(text)) {
				text = HanziToPinyin.getPinYin(text);
			}
			return text.charAt(0);
		} catch (Exception e) {
			return ' ';
		}
	}

	public static boolean isContainChinese(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (isChinese(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(CharSequence string) {
		return string == null || string.length() == 0;
	}

	public static String formatFloat(float t) {
		try {
			return String.format(Locale.US, "%.1f", new Object[]{Float.valueOf(t)});
		} catch (Exception e) {
			e.printStackTrace();
			return "0.0";
		}
	}

	public static List<String> string2List(String str, String splitter) {
		List<String> list = new ArrayList();
		string2List(str, splitter, list);
		return list;
	}

	public static void string2List(String str, String splitter, List<String> list) {
		if (!str.equals("")) {
			String[] arr = str.split(splitter);
			list = Arrays.asList(arr);
		}
	}

	public static String list2String(List<String> list, String splitter) {
		StringBuilder sb = new StringBuilder();
		int len = list.size();
		for (int i = 0; i < len; i++) {
			sb.append((String) list.get(i));
			if (i != len - 1) {
				sb.append(splitter);
			}
		}
		return sb.toString();
	}

	public static String formatScore(double score) {
		boolean isNegative = false;
		if (score < 0.0d) {
			isNegative = true;
			score = -score;
		}
		int n = (int) ((float) Math.round(((float) Math.round(100.0d * score)) / 10.0f));
		String number = n + "";
		if (n == 0) {
			return "0.0";
		}
		StringBuilder result = new StringBuilder();
		if (isNegative) {
			result.append("-");
		}
		if (number.length() == 1) {
			result.append("0.");
			result.append(number);
		} else {
			result.append(number.substring(0, number.length() - 1));
			result.append(".");
			result.append(number.charAt(number.length() - 1));
		}
		return result.toString();
	}

	public static final List<String> seperateValues(String strValues, String seperator) {
		if (strValues == null || strValues.length() <= 0) {
			return new ArrayList();
		}
		return Arrays.asList(strValues.split(seperator));
	}

	public static String concat(String connector, String... args) {
		StringBuilder sb = new StringBuilder();
		if (args != null) {
			for (String v : args) {
				if (v != null) {
					sb.append(v);
				}
				if (connector != null) {
					sb.append(connector);
				}
			}
		}
		if (connector != null) {
			int len = sb.length();
			sb.delete(len - connector.length(), len);
		}
		return sb.toString();
	}

	public static String formatPercent(long completed, long total) {
		if (completed <= 0 || total <= 0) {
			return "0.0%";
		}
		try {
			return String.format(Locale.US, "%.1f%%", Float.valueOf(((float) (completed / total)) * 100.0f));
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "completed: " + completed + ", total: " + total);
			return "0.0%";
		}
	}
}
