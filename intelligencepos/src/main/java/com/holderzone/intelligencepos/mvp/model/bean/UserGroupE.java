package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/9/27.
 */

public class UserGroupE implements Parcelable {
    /**
     * 组别权限启用停用 0：停用  1：启用
     */
    private Integer GroupRightEnable;

    public Integer getGroupRightEnable() {
        return GroupRightEnable;
    }

    public void setGroupRightEnable(Integer groupRightEnable) {
        GroupRightEnable = groupRightEnable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.GroupRightEnable);
    }

    public UserGroupE() {
    }

    protected UserGroupE(Parcel in) {
        this.GroupRightEnable = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<UserGroupE> CREATOR = new Creator<UserGroupE>() {
        @Override
        public UserGroupE createFromParcel(Parcel source) {
            return new UserGroupE(source);
        }

        @Override
        public UserGroupE[] newArray(int size) {
            return new UserGroupE[size];
        }
    };
}
