package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 预订备注项实体定义
 * Created by zhaoping on 2017/5/8.
 */

public class OrderRecordRemarkItemE implements Parcelable {
    /***/
    private String OrderRecordRemarkItemGUID;
    private String EnterpriseInfoGUID;
    private Integer IsEnable;
    /**
     * 备注
     */
    private String Name;
    /**
     * 序号
     */
    private String Sort;

    public String getOrderRecordRemarkItemGUID() {
        return OrderRecordRemarkItemGUID != null ? OrderRecordRemarkItemGUID.toLowerCase() : null;
    }

    public void setOrderRecordRemarkItemGUID(String orderRecordRemarkItemGUID) {
        OrderRecordRemarkItemGUID = orderRecordRemarkItemGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSort() {
        return Sort;
    }

    public void setSort(String sort) {
        Sort = sort;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public OrderRecordRemarkItemE() {
    }

    public Integer getIsEnable() {
        return IsEnable;
    }

    public void setIsEnable(Integer isEnable) {
        IsEnable = isEnable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.OrderRecordRemarkItemGUID);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeValue(this.IsEnable);
        dest.writeString(this.Name);
        dest.writeString(this.Sort);
    }

    protected OrderRecordRemarkItemE(Parcel in) {
        this.OrderRecordRemarkItemGUID = in.readString();
        this.EnterpriseInfoGUID = in.readString();
        this.IsEnable = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Name = in.readString();
        this.Sort = in.readString();
    }

    public static final Creator<OrderRecordRemarkItemE> CREATOR = new Creator<OrderRecordRemarkItemE>() {
        @Override
        public OrderRecordRemarkItemE createFromParcel(Parcel source) {
            return new OrderRecordRemarkItemE(source);
        }

        @Override
        public OrderRecordRemarkItemE[] newArray(int size) {
            return new OrderRecordRemarkItemE[size];
        }
    };
}
