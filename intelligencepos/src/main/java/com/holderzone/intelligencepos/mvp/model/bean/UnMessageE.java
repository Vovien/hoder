package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LT on 2018-04-08.
 */

public class UnMessageE implements Parcelable {
    /**
     * 企业GUID
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店GUID
     */
    private String StoreGUID;

    /**
     * 消息类型
     * 1010=新订单
     * 1020=催单
     * 1030=退单
     */
    private String MsgType;

    /**
     * 备注
     */
    private String MsgRemark;

    /**
     * 消息时间戳
     */
    private Long MsgTimeStamp;

    /**
     * 消息时间
     */
    private String MsgTime;

    /**
     * 是否自动接单
     * 0 == 失败
     * 1 == 成功
     */
    private Integer AutoOrder;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getMsgRemark() {
        return MsgRemark;
    }

    public void setMsgRemark(String msgRemark) {
        MsgRemark = msgRemark;
    }

    public Long getMsgTimeStamp() {
        return MsgTimeStamp;
    }

    public void setMsgTimeStamp(Long msgTimeStamp) {
        MsgTimeStamp = msgTimeStamp;
    }

    public String getMsgTime() {
        return MsgTime;
    }

    public void setMsgTime(String msgTime) {
        MsgTime = msgTime;
    }

    public Integer getAutoOrder() {
        return AutoOrder;
    }

    public void setAutoOrder(Integer autoOrder) {
        AutoOrder = autoOrder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.MsgType);
        dest.writeString(this.MsgRemark);
        dest.writeValue(this.MsgTimeStamp);
        dest.writeString(this.MsgTime);
        dest.writeValue(this.AutoOrder);
    }

    public UnMessageE() {
    }

    protected UnMessageE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGUID = in.readString();
        this.MsgType = in.readString();
        this.MsgRemark = in.readString();
        this.MsgTimeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.MsgTime = in.readString();
        this.AutoOrder = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<UnMessageE> CREATOR = new Parcelable.Creator<UnMessageE>() {
        @Override
        public UnMessageE createFromParcel(Parcel source) {
            return new UnMessageE(source);
        }

        @Override
        public UnMessageE[] newArray(int size) {
            return new UnMessageE[size];
        }
    };
}
