package com.qq.googleplay.base;

import android.os.SystemClock;

import com.qq.googleplay.common.io.IOUtils;
import com.qq.googleplay.net.HttpUtils;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.utils.FileUtil;
import com.qq.googleplay.utils.GsonUtil;
import com.qq.googleplay.utils.LogUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/24 16:03
 * 描 述 ： Protocol基类
 * 修订历史 ：
 * ============================================================
 **/
public abstract class BaseProtocol<T> {

    public T loadData(int index) throws Throwable {

        SystemClock.sleep(300);// 休息300毫秒，防止加载过快，看不到界面变化效果

        T localData = getDataFromLocal(index);
        if (localData != null) {
            LogUtil.sf("读取本地缓存--" + getCacheFile(index).getAbsolutePath());
            return localData;
        }
        String jsonString = getDataFromNet(index);

        T homeBean = parsejson(jsonString);

        return homeBean;
    }

    /**
     * 读取本地缓存
     * @param index
     * @return
     */
    private T getDataFromLocal(int index) {

        //DiskLruCacheUtil.readObject(jsonString,getInterfaceKey());
        File cacheFile = getCacheFile(index);
        if (cacheFile.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                String timeTimeMillis = reader.readLine();//读入插入时间
                if (System.currentTimeMillis() - Long.parseLong(timeTimeMillis) < RequestConstants
                        .PROTOCOLTIMEOUT) {
                    String jsonString = reader.readLine();//读入缓存内容
                    return parsejson(jsonString);//解析json
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }

        return null;
    }

    /**
     * 从网络获取数据
     * @param index
     * @return
     * @throws Throwable
     */
    public String getDataFromNet(int index) throws Throwable {
        String url = RequestConstants.URLS.BASEURL + getInterfaceKey();
        String jsonString = null;
        Map<String, String> extraParmas = getExtraParmas();
        if (extraParmas == null) {
            jsonString = HttpUtils.request(url,index);
        } else {// 子类覆写了getExtraParmas(),返回了额外的参数
            // 取出额外的参数
            jsonString = HttpUtils.request(url,extraParmas);
        }

        //保存网络数据到本地
        //DiskLruCacheUtil.saveObject(jsonString,getInterfaceKey());
        File cacheFile = getCacheFile(index);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(cacheFile));
            writer.write(System.currentTimeMillis() + "");//第一行写入插入时间
            writer.write("\r\n");//换行
            writer.write(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return jsonString;

    }


    /**获取缓存路径*/
    public File getCacheFile(int index) {

        // 如果是详情页 interfacekey+"."+包名
        Map<String, String> extraParmas = getExtraParmas();
        String name = "";
        if (extraParmas == null) {
            name = getInterfaceKey() + "." + index;// interfacekey+"."+index
        } else {// 详情页
            for (Map.Entry<String, String> info : extraParmas.entrySet()) {
                String key = info.getKey();//"packageName"
                String value = info.getValue();//com.itheima
                name = getInterfaceKey() + "." + value;// interfacekey+"."+com.itheima
            }
        }

        String dir = FileUtil.getDir("json");// sdcard/Android/data/包名/json
        File cacheFile = new File(dir, name);
        return cacheFile;
    }

    /**
     * @des 返回额外的参数
     * @call 默认在基类里面返回是null,但是如果子类需要返回额外的参数的时候,覆写该方法
     */
    public Map<String, String> getExtraParmas() {
        return null;
    }

    /**获取模块的suburl*/
    protected abstract String getInterfaceKey();

    /**泛型解析*/
    protected T parsejson(String jsonString){
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] args = genericSuperclass.getActualTypeArguments();
        Type type = args[0];
        return GsonUtil.changeGsonToBean(jsonString,type);
    }
}
