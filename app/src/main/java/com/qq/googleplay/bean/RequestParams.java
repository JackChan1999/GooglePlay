package com.qq.googleplay.bean;

import java.io.Serializable;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/29 19:11
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class RequestParams implements Serializable, Comparable<RequestParams>{

    private static final long serialVersionUID = -8708108746980739212L;

    private String name;
    private String value;

    public RequestParams(String name, int value){
        this.name = name;
        this.value = String.valueOf(value);
    }

    public RequestParams(String name, String value){
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RequestParams that = (RequestParams) o;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(RequestParams another) {
        return compare(another);
    }

    private int compare(RequestParams another) {
        int result = name.compareTo(another.name);
        return result == 0 ? value.compareTo(another.value) : result;
    }
}
