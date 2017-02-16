package com.qq.googleplay.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
public class SampleBean implements Parcelable {

    public String key;
    public int value;
    public boolean isHide;


    protected SampleBean(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.key = in.readString();
        this.value = in.readInt();
        this.isHide = in.readInt() == 1 ? true : false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeInt(value);
        dest.writeInt(isHide ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SampleBean> CREATOR = new Creator<SampleBean>() {
        @Override
        public SampleBean createFromParcel(Parcel in) {
            return new SampleBean(in);
        }

        @Override
        public SampleBean[] newArray(int size) {
            return new SampleBean[size];
        }
    };
}
