package com.qq.googleplay.bean;

import java.io.Serializable;

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
