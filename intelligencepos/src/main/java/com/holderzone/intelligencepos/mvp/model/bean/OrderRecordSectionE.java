package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 预订时段
 * Created by zhaoping on 2017/5/8.
 */

public class OrderRecordSectionE implements Parcelable{
    private String OrderRecordSectionGUID;
    private String EnterpriseInfoGUID;
    /**
     * 当前是否可用
     * 1=可用  0=不可用
     * 由服务端根据当前时间计算是否可用
     */
    private Integer CanUseNow;
    /**预订日期*/
    private String BusinessDay;
    private String StoreGUID;
    private String DiningTableGUID;
    /**
     * 时段名称
     */
    private String Name;
    /**
     * 时间点格式：1800,1830
     */
    private String SectionTime;

    /**
     * 是否返回时段明细
     */
    private Integer IsResultSectionTime;
    /**筛选启用状态
     */
    private Integer IsEnable;
    /***/
    private String KeepMinute;
    /**
     * 下属时间点明细
     */
    private List<OrderRecordSectionTimeE> ArrayOfOrderRecordSectionTimeE;

    public String getOrderRecordSectionGUID() {
        return OrderRecordSectionGUID;
    }

    public void setOrderRecordSectionGUID(String orderRecordSectionGUID) {
        OrderRecordSectionGUID = orderRecordSectionGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSectionTime() {
        return SectionTime;
    }

    public void setSectionTime(String sectionTime) {
        SectionTime = sectionTime;
    }

    public String getKeepMinute() {
        return KeepMinute;
    }

    public void setKeepMinute(String keepMinute) {
        KeepMinute = keepMinute;
    }

    public List<OrderRecordSectionTimeE> getArrayOfOrderRecordSectionTimeE() {
        return ArrayOfOrderRecordSectionTimeE;
    }

    public void setArrayOfOrderRecordSectionTimeE(List<OrderRecordSectionTimeE> arrayOfOrderRecordSectionTimeE) {
        ArrayOfOrderRecordSectionTimeE = arrayOfOrderRecordSectionTimeE;
    }

    public Integer getIsResultSectionTime() {
        return IsResultSectionTime;
    }

    public void setIsResultSectionTime(Integer isResultSectionTime) {
        IsResultSectionTime = isResultSectionTime;
    }

    public Integer getIsEnable() {
        return IsEnable;
    }

    public void setIsEnable(Integer isEnable) {
        IsEnable = isEnable;
    }

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public Integer getCanUseNow() {
        return CanUseNow;
    }

    public void setCanUseNow(Integer canUseNow) {
        CanUseNow = canUseNow;
    }

    public String getBusinessDay() {
        return BusinessDay;
    }

    public void setBusinessDay(String businessDay) {
        BusinessDay = businessDay;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public String getDiningTableGUID() {
        return DiningTableGUID;
    }

    public void setDiningTableGUID(String diningTableGUID) {
        DiningTableGUID = diningTableGUID;
    }

    public OrderRecordSectionE() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.OrderRecordSectionGUID);
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.BusinessDay);
        dest.writeString(this.StoreGUID);
        dest.writeString(this.DiningTableGUID);
        dest.writeString(this.Name);
        dest.writeString(this.SectionTime);
        dest.writeValue(this.IsResultSectionTime);
        dest.writeValue(this.IsEnable);
        dest.writeString(this.KeepMinute);
        dest.writeList(this.ArrayOfOrderRecordSectionTimeE);
    }

    protected OrderRecordSectionE(Parcel in) {
        this.OrderRecordSectionGUID = in.readString();
        this.EnterpriseInfoGUID = in.readString();
        this.BusinessDay = in.readString();
        this.StoreGUID = in.readString();
        this.DiningTableGUID = in.readString();
        this.Name = in.readString();
        this.SectionTime = in.readString();
        this.IsResultSectionTime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.IsEnable = (Integer) in.readValue(Integer.class.getClassLoader());
        this.KeepMinute = in.readString();
        this.ArrayOfOrderRecordSectionTimeE = new ArrayList<OrderRecordSectionTimeE>();
        in.readList(this.ArrayOfOrderRecordSectionTimeE, OrderRecordSectionTimeE.class.getClassLoader());
    }

    public static final Creator<OrderRecordSectionE> CREATOR = new Creator<OrderRecordSectionE>() {
        @Override
        public OrderRecordSectionE createFromParcel(Parcel source) {
            return new OrderRecordSectionE(source);
        }

        @Override
        public OrderRecordSectionE[] newArray(int size) {
            return new OrderRecordSectionE[size];
        }
    };
}
