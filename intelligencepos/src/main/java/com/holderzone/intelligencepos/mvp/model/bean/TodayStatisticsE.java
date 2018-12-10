package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tcw on 2017/3/24.
 */

public class TodayStatisticsE implements Parcelable{
    /**
     * 统计项目ID
     */
    private int ItemID;
    /**
     * 统计项目名称
     */
    private String Name;
    /**
     * 统计值
     */
    private String Value;

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ItemID);
        dest.writeString(this.Name);
        dest.writeString(this.Value);
    }

    public TodayStatisticsE() {
    }

    protected TodayStatisticsE(Parcel in) {
        this.ItemID = in.readInt();
        this.Name = in.readString();
        this.Value = in.readString();
    }

    public static final Creator<TodayStatisticsE> CREATOR = new Creator<TodayStatisticsE>() {
        @Override
        public TodayStatisticsE createFromParcel(Parcel source) {
            return new TodayStatisticsE(source);
        }

        @Override
        public TodayStatisticsE[] newArray(int size) {
            return new TodayStatisticsE[size];
        }
    };
}
