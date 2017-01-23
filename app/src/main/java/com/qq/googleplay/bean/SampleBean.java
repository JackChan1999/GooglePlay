package com.qq.googleplay.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/29 21:24
 * 描 述 ：
 * 修订历史 ：
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
