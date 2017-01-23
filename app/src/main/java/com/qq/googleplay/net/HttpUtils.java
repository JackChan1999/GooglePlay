package com.qq.googleplay.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.qq.googleplay.net.okhttp.OkHttpUtils;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.StringUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Response;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/25 18:13
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class HttpUtils {

    /*public static String requestGet(String url, String[] params) {
        return request(url, 0, params);
    }*/

   /* public String requestPost(String url, RequeseParams[] params) {
        return request(url, 1, params);
    }*/

    /* public static String request(String url, int method, RequestParams[] params){

    }*/

    public static String request(String url, int index) throws Throwable{

        String jsonString = OkHttpUtils.get().url(url).addParams("index", index + "").build()
                .execute().body().string();
        return jsonString;
    }

    public static String request(String url, Map<String, String> params) throws Throwable{

        String jsonString = OkHttpUtils.get().url(url).params(params).build()
                .execute().body().string();
        return jsonString;
    }

    public static Response download(String url, Map<String, String> params) throws IOException {
        return OkHttpUtils.get().url(url).params(params).build().execute();
    }

    protected static String appendParams(String url, Map<String, String> params)
    {
        if (url == null || params == null || params.isEmpty())
        {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext())
        {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

    /**判断网络是否可用*/
    public static boolean isNetworkAvailable(Context paramContext)
    {
        if (paramContext != null)
        {
            NetworkInfo[] arrayOfNetworkInfo = ((ConnectivityManager)paramContext.
                    getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworkInfo();
            if (arrayOfNetworkInfo != null)
                for (int i = 0; i < arrayOfNetworkInfo.length; i++)
                    if (arrayOfNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
        }
        return false;
    }

    /**判断wifi是否连接*/
    public static boolean isWifiConnection(Context paramContext)
    {
        NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.
                getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(1);
        if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()))
        {
            Log.d("net", "wifi is connected!");
            return true;
        }
        return false;
    }

    public static String getNetworkType() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) CommonUtil.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info == null || !info.isAvailable()) {
                    return "off";
                }
                if (info.getType() == 1) {
                    return "wifi";
                }
                TelephonyManager tm = (TelephonyManager) CommonUtil.getContext()
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tm.getNetworkType() == 1 || tm.getNetworkType() == 2) {
                    return "2g";
                }
                if (tm.getNetworkType() == 13) {
                    return "4g";
                }
                return "3g";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    /**获取当前连接的网络类型*/
    public static String getNetWorkType(Context context) {
        String mNetWorkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return "none";
        }
        String type = networkInfo.getTypeName();
        if (type.equalsIgnoreCase("WIFI")) {
            return "wifi";
        }
        if (!type.equalsIgnoreCase("MOBILE")) {
            return mNetWorkType;
        }
        if (!TextUtils.isEmpty(Proxy.getDefaultHost())) {
            return "wap";
        }
        int mobile = getNetType(context);
        if (mobile == 2) {
            return "2g";
        }
        if (mobile == 3) {
            return "3g";
        }
        return "4g";
    }

    private static int getNetType(Context context) {
        switch (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()) {
            case 3:
                return 3;
            case 5:
                return 3;
            case 6:
                return 3;
            case 8:
                return 3;
            case 9:
                return 3;
            case 10:
                return 3;
            case 12:
                return 3;
            case 13:
                return 4;
            case 14:
                return 3;
            case 15:
                return 3;
            default:
                return 2;
        }
    }

    public static String getAgent() {
        return System.getProperty("http.agent");
    }

   /**获取mac地址*/
    public static String getMacAddress(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo()
                .getMacAddress();
    }

    public static void disconnectSafely(HttpURLConnection connect) {
        if (connect != null) {
            try {
                connect.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static volatile String sMACAddress;

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

}
