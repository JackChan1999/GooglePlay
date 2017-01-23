package com.qq.googleplay.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.v4.view.MotionEventCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseApplication;

import java.io.File;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/29 16:28
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class CommonUtil {
    private static Signature[] sSystemSignature;
    private static volatile String sSN;
    private static volatile String sIMEI;
    private static volatile String sMACAddress;
    private static String deviceId;

    public static Context getContext() {
        return BaseApplication.getContext();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static long getMainThreadid() {
        return BaseApplication.getMainTreadId();
    }

    public static Handler getMainThreadHandler() {
        return BaseApplication.getHandler();
    }

    public static void postTaskSafely(Runnable task) {
        int curThreadId = android.os.Process.myTid();
        if (curThreadId == getMainThreadid()) {
            task.run();
        } else {
            getMainThreadHandler().post(task);
        }
    }

    public static int dip2Px(int dip) {
        float density = getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    public static int px2Dip(int px) {
        float density = getResources().getDisplayMetrics().density;
        int dip = (int) (px / density + 0.5f);
        return dip;
    }

    public static int dipToPixel(Context context, float dip) {
        return (int) (((double) (context.getResources().getDisplayMetrics().density * dip)) + 0.5f);
    }

    public static int pixelToDip(Context context, float pixel) {
        return (int) ((pixel / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static float px2sp(float pxVal)
    {
        return (pxVal / getResources().getDisplayMetrics().scaledDensity);
    }

    public static int sp2px(float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());
    }

    public static int getScreenHeight(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.y;
    }

    public static int getScreenWidth(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.x;
    }

    public static Point getScreenWidthAndHeight(Context context){
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point;
    }

    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    public static Drawable getDrawable(int drawableId) {
        return getResources().getDrawable(drawableId);
    }

    public static ColorStateList getColorStateList(int id) {
        return getResources().getColorStateList(id);
    }

    public static void showInputMethodWindow(Activity activity, View view) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void closeInputMethodWindow(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static String getMACAddress(Context context) {
        String address = null;
        if (!StringUtil.isEmpty(sMACAddress)) {
            return sMACAddress;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            address = wifiInfo.getMacAddress();
        }
        if (!StringUtil.isEmpty(address)) {
            sMACAddress = address;
        }
        return sMACAddress;
    }

    public static String getIMEI(Context context) {
        if (!StringUtil.isEmpty(sIMEI)) {
            return sIMEI;
        }
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return "";
            }
            String imei = tm.getDeviceId();
            if (!StringUtil.isEmpty(imei)) {
                sIMEI = imei;
            }
            return sIMEI;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSN() {
        if (!StringUtil.isEmpty(sSN)) {
            return sSN;
        }
        String sn = getProperty("ro.serialno");
        if (StringUtil.isEmpty(sn)) {
            return sn;
        }
        sSN = sn;
        return sn;
    }

    public static String getCountry(Context context) {
        if (context == null || context.getResources().getConfiguration() == null
                || context.getResources().getConfiguration().locale == null) {
            return "";
        }
        return context.getResources().getConfiguration().locale.getCountry();
    }

    /**判断是否是系统应用*/
    public static boolean isSystemPackage(PackageManager pm, PackageInfo pkg) {
        if (sSystemSignature == null) {
            sSystemSignature = new Signature[]{getSystemSignature(pm)};
        }
        if (sSystemSignature[0] == null || !sSystemSignature[0].equals(getFirstSignature(pkg))) {
            return false;
        }
        return true;
    }

    private static Signature getSystemSignature(PackageManager pm) {
        try {
            return getFirstSignature(pm.getPackageInfo("android", PackageManager.GET_SIGNATURES));
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private static Signature getFirstSignature(PackageInfo pkg) {
        if (pkg == null || pkg.signatures == null || pkg.signatures.length <= 0) {
            return null;
        }
        return pkg.signatures[0];
    }

    /**获取电量百分比*/
    public static String getBatteryPercentage(Intent batteryChangedIntent) {
        return formatPercentage(getBatteryLevel(batteryChangedIntent));
    }

    public static String formatPercentage(int percentage) {
        return formatPercentage(((double) percentage) / 100.0d);
    }

    private static String formatPercentage(double percentage) {
        return NumberFormat.getPercentInstance().format(percentage);
    }

    public static int getBatteryLevel(Intent batteryChangedIntent) {
        int level = batteryChangedIntent.getIntExtra("level", 0);
        return (level * 100) / batteryChangedIntent.getIntExtra("scale", 100);
    }

    /**获取充电状态*/
    public static String getBatteryStatus(Resources res, Intent batteryChangedIntent) {
        Intent intent = batteryChangedIntent;
        int plugType = intent.getIntExtra("plugged", 0);
        int status = intent.getIntExtra("status", 1);
        if (status == 2) {
            int resId;
            if (plugType == 1) {
                resId = R.string.battery_info_status_charging_ac;
            } else if (plugType == 2) {
                resId = R.string.battery_info_status_charging_usb;
            } else if (plugType == 4) {
                resId = R.string.battery_info_status_charging_wireless;
            } else {
                resId = R.string.battery_info_status_charging;
            }
            return res.getString(resId);
        } else if (status == 3) {
            return res.getString(R.string.battery_info_status_discharging);
        } else {
            if (status == 4) {
                return res.getString(R.string.battery_info_status_not_charging);
            }
            if (status == 5) {
                return res.getString(R.string.battery_info_status_full);
            }
            return res.getString(R.string.battery_info_status_unknown);
        }
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }

    public static boolean isPackageExist(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPackageExistAndHasAction(Context context, String pkgName, String action) {
        if (isPackageExist(context, pkgName) && context.getPackageManager()
                .queryIntentActivities(new Intent(action),
                PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            return true;
        }
        return false;
    }

    public static void forceShowInputMethod(Activity activity) {
        Window window = activity.getWindow();
        window.setSoftInputMode(window.getAttributes().softInputMode
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public static void createShortcut(Context context, String shortcutName,
                                      int drawableResId, Intent shortcutIntent) {
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutintent.putExtra("duplicate", false);
        shortcutintent.putExtra("android.intent.extra.shortcut.NAME", shortcutName);
        shortcutintent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent
                .ShortcutIconResource.fromContext(context, drawableResId));
        shortcutintent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
        context.sendBroadcast(shortcutintent);
    }

    /**获取语言和国家*/
    public static String getLanguageAndCountry() {
        String res = "";
        if (!TextUtils.isEmpty(Locale.getDefault().getLanguage())) {
            res = res + Locale.getDefault().getLanguage().toLowerCase() + "-";
        }
        if (TextUtils.isEmpty(Locale.getDefault().getCountry().toUpperCase())) {
            return res;
        }
        return res + Locale.getDefault().getCountry().toUpperCase();
    }

   /**获取存储大小*/
    public static String getStorageSize(Context context) {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return Formatter.formatFileSize(context, ((long) stat.getBlockSize()) * ((long) stat.getBlockCount()));
    }

   /**获取sim卡信息*/
    public static String getSimOperatorName(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperatorName();
    }

    public static String getReleaseVersion() {
        return Build.VERSION.RELEASE;
    }

    /**获取版本名*/
    public static final String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**获取版本号*/
    public static final int getVersionCode() {
        int i = 0;
        try {
            return getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }

    /**获取设备id，即IMEI码*/
    public static String getDeviceId(Context context) {
        String str;
        synchronized (CommonUtil.class) {
            try {
                if (TextUtils.isEmpty(deviceId)) {
                    deviceId = (String) ReflectUtil.invoke("android.telephony.MzTelephonyManager",
                            "getDeviceId", null, null);
                    if (TextUtils.isEmpty(deviceId)) {
                        deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                                .getDeviceId();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th) {
                th.printStackTrace();
            }
            str = deviceId;
        }
        return str;
    }

    private static String SN;

    /**获取SN号*/
    public static String getSN(Context context) {
        synchronized (CommonUtil.class) {
            String str;
            try {
                if (SN == null) {
                    SN = ReflectUtil.getSystemProperties(context, "ro.serialno");
                }
                str = "Get Mz Phone SN " + SN + "XXX";
                Log.e("PhoneUtils", str);
                return SN;
            } finally {

            }
        }
    }

    public static <T> T[] concatArray(T[] array1, T[] array2) {
        if (array1 == null || array1.length == 0) {
            return array2;
        }
        if (array2 == null || array2.length == 0) {
            return array1;
        }
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static String getSystemProperties(String property) {
        try {
            Method getStringMethod = Build.class.getDeclaredMethod("getString", String.class);
            getStringMethod.setAccessible(true);
            return (String) getStringMethod.invoke(null, property);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getProperty(String property) {
        return (String) System.getProperties().get(property);
    }

    public static String getOperater(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null || 5 != tm.getSimState()) {
            return "";
        }
        return tm.getSimOperator();
    }

    /**判断系统是否root*/
    public static String isRoot() {
        int isRoot = 0;
        try {
            if (new File("/system/bin/su").exists() || new File("/system/xbin/su").exists()) {
                isRoot = 1;
            } else {
                isRoot = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(isRoot);
    }

    public static String getUid(Context context, String type) {
        Account account = getAccount(context, type);
        return account == null ? null : account.name;
    }

    public static Account getAccount(Context context, String type) {
        Account[] accountsByType = AccountManager.get(context.getApplicationContext())
                .getAccountsByType(type);
        return (accountsByType == null || accountsByType.length <= 0) ? null : accountsByType[0];
    }

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String str = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
            while (str.length() < 2) {
                str = "0" + str;
            }
            hexString.append(str);
        }
        return hexString.toString();
    }

    public static String getImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        String imei = tm.getDeviceId();
        if (StringUtil.isEmpty(imei)) {
            return "";
        }
        return imei;
    }

    /**2.2以上版本*/
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**2.3以上版本*/
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**3.0以上版本*/
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**3.1以上版本*/
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**4.1以上版本*/
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**4.4以上版本*/
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 判断微信客户端是否可用
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

}
