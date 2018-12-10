package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tcw on 2017/4/17.
 */

public class SalesMsgE implements Parcelable {

    /**
     * SalesMsgUID : 2017041717582369
     * SalesMsgGUID : d6b7774d-57a7-4d31-9b97-19353c6eb62b
     * StoreGUID : caa8c797-b1ae-47b7-a7c8-81d5a2e4199e
     * MsgType : 8
     * Content : 桌台[桌016]已点菜，操作员[坷垃]
     * AddTime : 2017-04-17 17:58:23
     * AddTimeStamp : 1492423103166
     */

    /**
     *
     */
    private String SalesMsgUID;

    /**
     * 消息标识
     */
    private String SalesMsgGUID;

    /**
     *
     */
    private String StoreGUID;

    /**
     * 消息类型
     */
    private Integer MsgType;

    /**
     * 消息内容
     */
    private String Content;

    /**
     * 生成时间
     */
    private String AddTime;

    /**
     *
     */
    private Long AddTimeStamp;

    public String getSalesMsgUID() {
        return SalesMsgUID;
    }

    public void setSalesMsgUID(String SalesMsgUID) {
        this.SalesMsgUID = SalesMsgUID;
    }

    public String getSalesMsgGUID() {
        return SalesMsgGUID;
    }

    public void setSalesMsgGUID(String SalesMsgGUID) {
        this.SalesMsgGUID = SalesMsgGUID;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String StoreGUID) {
        this.StoreGUID = StoreGUID;
    }

    public Integer getMsgType() {
        return MsgType;
    }

    public void setMsgType(Integer MsgType) {
        this.MsgType = MsgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String AddTime) {
        this.AddTime = AddTime;
    }

    public Long getAddTimeStamp() {
        return AddTimeStamp;
    }

    public void setAddTimeStamp(Long AddTimeStamp) {
        this.AddTimeStamp = AddTimeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SalesMsgUID);
        dest.writeString(this.SalesMsgGUID);
        dest.writeString(this.StoreGUID);
        dest.writeValue(this.MsgType);
        dest.writeString(this.Content);
        dest.writeString(this.AddTime);
        dest.writeValue(this.AddTimeStamp);
    }

    public SalesMsgE() {
    }

    protected SalesMsgE(Parcel in) {
        this.SalesMsgUID = in.readString();
        this.SalesMsgGUID = in.readString();
        this.StoreGUID = in.readString();
        this.MsgType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Content = in.readString();
        this.AddTime = in.readString();
        this.AddTimeStamp = (Long) in.readValue(Long.class.getClassLoader());
    }

    @Generated(hash = 1552703712)
    public SalesMsgE(String SalesMsgUID, String SalesMsgGUID, String StoreGUID,
                     Integer MsgType, String Content, String AddTime, Long AddTimeStamp) {
        this.SalesMsgUID = SalesMsgUID;
        this.SalesMsgGUID = SalesMsgGUID;
        this.StoreGUID = StoreGUID;
        this.MsgType = MsgType;
        this.Content = Content;
        this.AddTime = AddTime;
        this.AddTimeStamp = AddTimeStamp;
    }

    public static final Creator<SalesMsgE> CREATOR = new Creator<SalesMsgE>() {
        @Override
        public SalesMsgE createFromParcel(Parcel source) {
            return new SalesMsgE(source);
        }

        @Override
        public SalesMsgE[] newArray(int size) {
            return new SalesMsgE[size];
        }
    };
}
