package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 附加费
 * Created by tcw on 2017/3/16.
 */

public class SalesOrderAdditionalFeesE implements Parcelable {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**附加费名称*/
    private String Name;
    /**附加费单价*/
    private Double Price ;
    /**附加费小计*/
    private Double SubTotal  ;
    /**
     * 附加费标识
     */
    private String SalesOrderAdditionalFeesGUID;
    private String salesOrderGUID;
    /**
     * 附加费项目标识
     */
    private String AdditionalFeesGUID;
    /**
     * 附加费数量
     */
    private Integer AdditionalFeesCount;
    /**
     * 操作员标识
     */
    private String AddUsersGUID;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getSalesOrderAdditionalFeesGUID() {
        return SalesOrderAdditionalFeesGUID;
    }

    public void setSalesOrderAdditionalFeesGUID(String salesOrderAdditionalFeesGUID) {
        SalesOrderAdditionalFeesGUID = salesOrderAdditionalFeesGUID;
    }

    public String getAdditionalFeesGUID() {
        return AdditionalFeesGUID;
    }

    public void setAdditionalFeesGUID(String additionalFeesGUID) {
        AdditionalFeesGUID = additionalFeesGUID;
    }

    public Integer getAdditionalFeesCount() {
        return AdditionalFeesCount;
    }

    public void setAdditionalFeesCount(Integer additionalFeesCount) {
        AdditionalFeesCount = additionalFeesCount;
    }

    public String getAddUsersGUID() {
        return AddUsersGUID;
    }

    public void setAddUsersGUID(String addUsersGUID) {
        AddUsersGUID = addUsersGUID;
    }

    public String getSalesOrderGUID() {
        return salesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        this.salesOrderGUID = salesOrderGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(Double subTotal) {
        SubTotal = subTotal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.Name);
        dest.writeValue(this.Price);
        dest.writeValue(this.SubTotal);
        dest.writeString(this.SalesOrderAdditionalFeesGUID);
        dest.writeString(this.salesOrderGUID);
        dest.writeString(this.AdditionalFeesGUID);
        dest.writeValue(this.AdditionalFeesCount);
        dest.writeString(this.AddUsersGUID);
    }

    public SalesOrderAdditionalFeesE() {
    }

    protected SalesOrderAdditionalFeesE(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.Name = in.readString();
        this.Price = (Double) in.readValue(Double.class.getClassLoader());
        this.SubTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.SalesOrderAdditionalFeesGUID = in.readString();
        this.salesOrderGUID = in.readString();
        this.AdditionalFeesGUID = in.readString();
        this.AdditionalFeesCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.AddUsersGUID = in.readString();
    }

    public static final Creator<SalesOrderAdditionalFeesE> CREATOR = new Creator<SalesOrderAdditionalFeesE>() {
        @Override
        public SalesOrderAdditionalFeesE createFromParcel(Parcel source) {
            return new SalesOrderAdditionalFeesE(source);
        }

        @Override
        public SalesOrderAdditionalFeesE[] newArray(int size) {
            return new SalesOrderAdditionalFeesE[size];
        }
    };
}
