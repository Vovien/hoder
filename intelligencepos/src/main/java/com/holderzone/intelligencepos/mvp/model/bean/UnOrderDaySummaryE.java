package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LT on 2018-04-02.
 */

public class UnOrderDaySummaryE implements Parcelable {
    /**
     * 标识
     * 1=全部平台
     * 2=微信
     * 3=饿了么
     * 4=美团
     */
    private Integer ItemID;

    /**
     * 项目名称
     */
    private String Caption;

    /**
     * 数据项目01
     */
    private String Data01;

    /**
     * 数据项目01标签
     */
    private String Data01Caption;

    /**
     * 数据项目02
     */
    private String Data02;

    /**
     * 数据项目02标签
     */
    private String Data02Caption;

    public Integer getItemID() {
        return ItemID;
    }

    public void setItemID(Integer itemID) {
        ItemID = itemID;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getData01() {
        return Data01;
    }

    public void setData01(String data01) {
        Data01 = data01;
    }

    public String getData01Caption() {
        return Data01Caption;
    }

    public void setData01Caption(String data01Caption) {
        Data01Caption = data01Caption;
    }

    public String getData02() {
        return Data02;
    }

    public void setData02(String data02) {
        Data02 = data02;
    }

    public String getData02Caption() {
        return Data02Caption;
    }

    public void setData02Caption(String data02Caption) {
        Data02Caption = data02Caption;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.ItemID);
        dest.writeString(this.Caption);
        dest.writeString(this.Data01);
        dest.writeString(this.Data01Caption);
        dest.writeString(this.Data02);
        dest.writeString(this.Data02Caption);
    }

    public UnOrderDaySummaryE() {
    }

    protected UnOrderDaySummaryE(Parcel in) {
        this.ItemID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Caption = in.readString();
        this.Data01 = in.readString();
        this.Data01Caption = in.readString();
        this.Data02 = in.readString();
        this.Data02Caption = in.readString();
    }

    public static final Parcelable.Creator<UnOrderDaySummaryE> CREATOR = new Parcelable.Creator<UnOrderDaySummaryE>() {
        @Override
        public UnOrderDaySummaryE createFromParcel(Parcel source) {
            return new UnOrderDaySummaryE(source);
        }

        @Override
        public UnOrderDaySummaryE[] newArray(int size) {
            return new UnOrderDaySummaryE[size];
        }
    };
}
