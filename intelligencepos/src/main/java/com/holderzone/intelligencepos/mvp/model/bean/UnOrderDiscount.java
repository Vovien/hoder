package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LT on 2018-04-02.
 */

public class UnOrderDiscount implements Parcelable {
    /**
     * 自增ID
     */
    private Integer UnOrderDiscountID;

    /**
     * 编号
     */
    private String UnOrderDiscountUID;

    /**
     * 标识GUID 主键
     */
    private String UnOrderDiscountGUID;

    /**
     * 订单GUID 外键
     */
    private String UnOrderGUID;

    /**
     * 优惠活动名称
     */
    private String DiscountName;

    /**
     * 优惠总金额（EnterpriseDiscount + PlatformTotal + OtherTotal）
     */
    private Double Total;

    /**
     * 商家承担金额
     */
    private Double EnterpriseDiscount;

    /**
     * 外卖平台承担金额
     */
    private Double PlatformTotal;

    /**
     * 第三方承担金额
     */
    private Double OtherTotal;

    /**
     * 备注说明
     */
    private String Remark;

    public Integer getUnOrderDiscountID() {
        return UnOrderDiscountID;
    }

    public void setUnOrderDiscountID(Integer unOrderDiscountID) {
        UnOrderDiscountID = unOrderDiscountID;
    }

    public String getUnOrderDiscountUID() {
        return UnOrderDiscountUID;
    }

    public void setUnOrderDiscountUID(String unOrderDiscountUID) {
        UnOrderDiscountUID = unOrderDiscountUID;
    }

    public String getUnOrderDiscountGUID() {
        return UnOrderDiscountGUID;
    }

    public void setUnOrderDiscountGUID(String unOrderDiscountGUID) {
        UnOrderDiscountGUID = unOrderDiscountGUID;
    }

    public String getUnOrderGUID() {
        return UnOrderGUID;
    }

    public void setUnOrderGUID(String unOrderGUID) {
        UnOrderGUID = unOrderGUID;
    }

    public String getDiscountName() {
        return DiscountName;
    }

    public void setDiscountName(String discountName) {
        DiscountName = discountName;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public Double getEnterpriseDiscount() {
        return EnterpriseDiscount;
    }

    public void setEnterpriseDiscount(Double enterpriseDiscount) {
        EnterpriseDiscount = enterpriseDiscount;
    }

    public Double getPlatformTotal() {
        return PlatformTotal;
    }

    public void setPlatformTotal(Double platformTotal) {
        PlatformTotal = platformTotal;
    }

    public Double getOtherTotal() {
        return OtherTotal;
    }

    public void setOtherTotal(Double otherTotal) {
        OtherTotal = otherTotal;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.UnOrderDiscountID);
        dest.writeString(this.UnOrderDiscountUID);
        dest.writeString(this.UnOrderDiscountGUID);
        dest.writeString(this.UnOrderGUID);
        dest.writeString(this.DiscountName);
        dest.writeValue(this.Total);
        dest.writeValue(this.EnterpriseDiscount);
        dest.writeValue(this.PlatformTotal);
        dest.writeValue(this.OtherTotal);
        dest.writeString(this.Remark);
    }

    public UnOrderDiscount() {
    }

    protected UnOrderDiscount(Parcel in) {
        this.UnOrderDiscountID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UnOrderDiscountUID = in.readString();
        this.UnOrderDiscountGUID = in.readString();
        this.UnOrderGUID = in.readString();
        this.DiscountName = in.readString();
        this.Total = (Double) in.readValue(Double.class.getClassLoader());
        this.EnterpriseDiscount = (Double) in.readValue(Double.class.getClassLoader());
        this.PlatformTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.OtherTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.Remark = in.readString();
    }

    public static final Parcelable.Creator<UnOrderDiscount> CREATOR = new Parcelable.Creator<UnOrderDiscount>() {
        @Override
        public UnOrderDiscount createFromParcel(Parcel source) {
            return new UnOrderDiscount(source);
        }

        @Override
        public UnOrderDiscount[] newArray(int size) {
            return new UnOrderDiscount[size];
        }
    };
}
