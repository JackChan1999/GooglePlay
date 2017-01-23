package com.qq.googleplay.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.qq.googleplay.common.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class FileUtil {
	private static final String TAG = FileUtil.class.getSimpleName();
	public static final String ROOT_DIR = "Android/data/" + CommonUtil.getPackageName();
	public static final String DOWNLOAD_DIR = "download";
	public static final String CACHE_DIR = "cache";
	public static final String ICON_DIR = "icon";
	public static final String DELIMITER = "/";
	private static final long MEGA_BYTES = 1048576;//1M大小
	private static final long GIGA_BYTES = 1073741824;//1G大小

	/**获取磁盘缓存路径*/
	public static File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath =
				Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
						!isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
						context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	/**判断Sd卡是否删除*/
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static boolean isExternalStorageRemovable() {
		if (CommonUtil.hasGingerbread()) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**获取SD卡缓存路径*/
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static File getExternalCacheDir(Context context) {
		if (CommonUtil.hasFroyo()) {
			return context.getExternalCacheDir();
		}

		final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}

	/**获取指定路径可用大小*/
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static long getUsableSpace(File path) {
		if (CommonUtil.hasGingerbread()) {
			return path.getUsableSpace();
		}
		final StatFs stats = new StatFs(path.getPath());
		return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	}

	/** 判断SD卡是否挂载 */
	public static boolean isSDCardAvailable() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean hasExternalXDir() {
		return Build.VERSION.SDK_INT >= 8;
	}

	public static boolean isExternalStorageAvailable() {
		return "mounted".equals(Environment.getExternalStorageState());
	}


	/** 获取下载目录 */
	public static String getDownloadDir() {
		return getDir(DOWNLOAD_DIR);
	}

	/** 获取缓存目录 */
	public static String getCacheDir() {
		return getDir(CACHE_DIR);
	}

	/** 获取icon目录 */
	public static String getIconDir() {
		return getDir(ICON_DIR);
	}

	/** 获取应用目录*/
	public static String getDir(String name) {
		StringBuilder sb = new StringBuilder();
		if (isSDCardAvailable()) {
			sb.append(getExternalStoragePath());
		} else {
			sb.append(getCachePath());
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		if (createDirs(path)) {
			return path;
		} else {
			return null;
		}
	}

	/** 获取SD下的应用目录 */
	public static String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取应用的cache目录 */
	public static String getCachePath() {
		File f = CommonUtil.getContext().getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 创建文件夹 */
	public static boolean createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}

	/** 复制文件，可以选择是否删除源文件 */
	public static boolean copyFile(String srcPath, String destPath,
			boolean deleteSrc) {
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		return copyFile(srcFile, destFile, deleteSrc);
	}

	/** 复制文件，可以选择是否删除源文件 */
	public static boolean copyFile(File srcFile, File destFile,
			boolean deleteSrc) {
		if (!srcFile.exists() || !srcFile.isFile()) {
			return false;
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = in.read(buffer)) > 0) {
				out.write(buffer, 0, i);
				out.flush();
			}
			if (deleteSrc) {
				srcFile.delete();
			}
		} catch (Exception e) {
			LogUtil.e(e);
			return false;
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
		return true;
	}

	/** 判断文件是否可写 */
	public static boolean isWriteable(String path) {
		try {
			if (StringUtil.isEmpty(path)) {
				return false;
			}
			File f = new File(path);
			return f.exists() && f.canWrite();
		} catch (Exception e) {
			LogUtil.e(e);
			return false;
		}
	}

	/** 修改文件的权限,例如"777"等 */
	public static void chmod(String path, String mode) {
		try {
			String command = "chmod " + mode + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (Exception e) {
			LogUtil.e(e);
		}
	}

	/**把数据写入文件*/
	public static boolean writeFile(InputStream is, String path,
			boolean recreate) {
		boolean res = false;
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			if (recreate && f.exists()) {
				f.delete();
			}
			if (!f.exists() && null != is) {
				File parentFile = new File(f.getParent());
				parentFile.mkdirs();
				int count = -1;
				byte[] buffer = new byte[1024];
				fos = new FileOutputStream(f);
				while ((count = is.read(buffer)) != -1) {
					fos.write(buffer, 0, count);
				}
				res = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(is);
		}
		return res;
	}

	/**把字符串数据写入文件*/
	public static boolean writeFile(byte[] content, String path, boolean append) {
		boolean res = false;
		File f = new File(path);
		RandomAccessFile raf = null;
		try {
			if (f.exists()) {
				if (!append) {
					f.delete();
					f.createNewFile();
				}
			} else {
				f.createNewFile();
			}
			if (f.canWrite()) {
				raf = new RandomAccessFile(f, "rw");
				raf.seek(raf.length());
				raf.write(content);
				res = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			IOUtils.closeQuietly(raf);
		}
		return res;
	}

	/**把字符串数据写入文件*/
	public static boolean writeFile(String content, String path, boolean append) {
		return writeFile(content.getBytes(), path, append);
	}

	/**把键值对写入文件*/
	public static void writeProperties(String filePath, String key,
			String value, String comment) {
		if (StringUtil.isEmpty(key) || StringUtil.isEmpty(filePath)) {
			return;
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);// 先读取文件，再把键值对追加到后面
			p.setProperty(key, value);
			fos = new FileOutputStream(f);
			p.store(fos, comment);
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fos);
		}
	}

	/**删除文件*/
	public static final boolean deleteFile(String path) {
		makeParentPath(path);
		File file = new File(path);
		if (file.isDirectory()) {
			return false;
		}
		return file.delete();
	}

	public static final void makeParentPath(String path) {
		File parentFile = new File(path).getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs();
		}
	}

	/**删除文件夹*/
	public static final void deleteDirectory(String path) {
		File file = new File(path);
		if (file.exists() && !file.delete()) {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				if (!listFiles[i].isDirectory()) {
					listFiles[i].delete();
				} else if (!listFiles[i].delete()) {
					deleteDirectory(listFiles[i].getAbsolutePath());
				}
			}
			file.delete();
		}
	}

	/** 根据值读取 */
	public static String readProperties(String filePath, String key,
			String defaultValue) {
		if (StringUtil.isEmpty(key) || StringUtil.isEmpty(filePath)) {
			return null;
		}
		String value = null;
		FileInputStream fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			value = p.getProperty(key, defaultValue);
		} catch (IOException e) {
			LogUtil.e(e);
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return value;
	}

	/** 把字符串键值对的map写入文件 */
	public static void writeMap(String filePath, Map<String, String> map,
			boolean append, String comment) {
		if (map == null || map.size() == 0 || StringUtil.isEmpty(filePath)) {
			return;
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			Properties p = new Properties();
			if (append) {
				fis = new FileInputStream(f);
				p.load(fis);// 先读取文件，再把键值对追加到后面
			}
			p.putAll(map);
			fos = new FileOutputStream(f);
			p.store(fos, comment);
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fos);
		}
	}

	/** 把字符串键值对的文件读入map */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, String> readMap(String filePath,
			String defaultValue) {
		if (StringUtil.isEmpty(filePath)) {
			return null;
		}
		Map<String, String> map = null;
		FileInputStream fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			map = new HashMap<String, String>((Map) p);// 因为properties继承了map，所以直接通过p来构造一个map
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return map;
	}

	/** 改名 */
	public static boolean copy(String src, String des, boolean delete) {
		File file = new File(src);
		if (!file.exists()) {
			return false;
		}
		File desFile = new File(des);
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = new FileOutputStream(desFile);
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
				out.flush();
			}
		} catch (Exception e) {
			LogUtil.e(e);
			return false;
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		if (delete) {
			file.delete();
		}
		return true;
	}

	public static boolean fileExists(Context context, String mediaUrl) {
		if (!StringUtil.isEmpty(mediaUrl)) {
			String filename;
			Uri uri = Uri.parse(mediaUrl);
			if (StringUtil.isEmpty(uri.getScheme()) || !uri.getScheme().equals("content")) {
				filename = mediaUrl.replace("file://", "");
			} else {
				filename = getRealFilePathFromContentUri(context, uri);
			}
			if (!StringUtil.isEmpty(filename)) {
				return new File(filename).exists();
			}
		}
		return false;
	}

	private static String getRealFilePathFromContentUri(Context context, Uri contentUri) {
		try {
			Cursor cursor = context.getContentResolver().query(contentUri,
					new String[]{"_data"}, null, null, null);
			if (cursor == null) {
				return null;
			}
			int index = cursor.getColumnIndexOrThrow("_data");
			cursor.moveToFirst();
			String result = cursor.getString(index);
			cursor.close();
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public static void delDir(File dir) {
		if (dir != null) {
			try {
				delAllFiles(dir);
				dir.delete();
			} catch (Exception e) {
			}
		}
	}

	public static void delDir(String dirFullName) {
		delDir(new File(dirFullName));
	}

	public static void delAllFiles(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			String dirFullName = dir.getAbsolutePath();
			String[] fileList = dir.list();
			for (int i = 0; i < fileList.length; i++) {
				File tempFile;
				if (dirFullName.endsWith(File.separator)) {
					tempFile = new File(dirFullName + fileList[i]);
				} else {
					tempFile = new File(dirFullName + File.separator + fileList[i]);
				}
				if (tempFile.isFile()) {
					tempFile.delete();
				}
				if (tempFile.isDirectory()) {
					delAllFiles(dirFullName + DELIMITER + fileList[i]);
					delDir(dirFullName + DELIMITER + fileList[i]);
				}
			}
		}
	}

	public static void delAllFiles(String dirFullName) {
		delAllFiles(new File(dirFullName));
	}

	public static long getSDAvailaleSize() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long blockSize = (long) stat.getBlockSize();
		long availableBlocks = (long) stat.getAvailableBlocks();
		Log.d("test", "getSDAvailaleSize " + (availableBlocks * blockSize));
		return availableBlocks * blockSize;
	}

	public static long getSDAllSize() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long blockSize = (long) stat.getBlockSize();
		long availableBlocks = (long) stat.getBlockCount();
		Log.d("test", "getSDAllSize " + (availableBlocks * blockSize));
		return availableBlocks * blockSize;
	}

	public static String convertToFormateSize(long size) {
		if (size <= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
			return size + "B";
		}
		long sizeInKB = size / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
		if (sizeInKB > PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
			long sizeInMB = sizeInKB / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
			if (sizeInMB > PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
				long sizeInGB = sizeInMB / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
				sizeInMB = (long) (((double) (sizeInMB % PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 102.4d);
				return sizeInGB + (sizeInMB == 0 ? "" : "." + sizeInMB) + "GB";
			}
			sizeInKB = (long) (((double) (sizeInKB % PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 102.4d);
			return sizeInMB + (sizeInKB == 0 ? "" : "." + sizeInKB) + "MB";
		}
		size = (long) (((double) (size % PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 102.4d);
		return sizeInKB + (size == 0 ? "" : "." + size) + "KB";
	}

	public static String formatSize(long size) {
		if (size <= 0) {
			try {
				return "0 B";
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e(TAG, "size: " + size);
				return "0 B";
			}
		} else if (size < MEGA_BYTES) {
			return String.format(Locale.US, "%.1f K", (((float) size) + 0.0f) / 1024.0f);
		} else if (size < GIGA_BYTES) {
			return String.format(Locale.US, "%.1f M", (((float) size) + 0.0f) / 1048576.0f);
		} else {
			return String.format(Locale.US, "%.1f G", (((float) size) + 0.0f) / 1.07374182E9f);
		}
	}
}
