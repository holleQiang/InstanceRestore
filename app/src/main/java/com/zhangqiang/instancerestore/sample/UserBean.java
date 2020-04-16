package com.zhangqiang.instancerestore.sample;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class UserBean implements Serializable, Parcelable {

    private String name;

    public String getName() {
        return name;
    }

    public UserBean setName(String name) {
        this.name = name;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
