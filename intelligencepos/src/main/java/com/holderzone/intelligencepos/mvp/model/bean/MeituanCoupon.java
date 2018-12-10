package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by terry on 17-11-2.
 */

public class MeituanCoupon implements Parcelable {

    /**
     * 企业标识
     */
    private String EnterpriseInfoGUID;

    /**
     * 门店标识
     */
    private String StoreGuid;

    /**
     * 付款项标识
     */
    private String SalesOrderPaymentGUID;

    /**
     * 最大可验证张数
     */
    private Integer count;

    /**
     * 券购买价,即用户在购买团购券时所付的实际价格
     */
    private Double couponBuyPrice;

    /**
     * 券码
     */
    private String couponCode;

    /**
     * 是否代金券
     */
    private Boolean isVoucher;

    /**
     * 券码有效期
     */
    private String couponEndTime;

    /**
     * 项目开始时间
     */
    private String dealBeginTime;

    /**
     * 项目ID
     */
    private Integer dealId;

    /**
     * 团购券价格，即商家促销前的券购买价格，如首次购买有更多优惠这类需要在开店宝设置的促销
     */
    private Double dealPrice;

    /**
     * 项目名称
     */
    private String dealTitle;

    /**
     * 券面值，即人们常说的市场价
     */
    private Double dealValue;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 最小消费张数
     */
    private Integer minConsume;

    /**
     * 操作状态
     */
    private Integer result;

    /**
     * 首次使用数量
     */
    private Integer UseCount;

    /**
     * 撤销储量
     */
    private Integer CancelCount;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public String getStoreGuid() {
        return StoreGuid;
    }

    public void setStoreGuid(String storeGuid) {
        StoreGuid = storeGuid;
    }

    public String getSalesOrderPaymentGUID() {
        return SalesOrderPaymentGUID;
    }

    public void setSalesOrderPaymentGUID(String salesOrderPaymentGUID) {
        SalesOrderPaymentGUID = salesOrderPaymentGUID;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getCouponBuyPrice() {
        return couponBuyPrice;
    }

    public void setCouponBuyPrice(Double couponBuyPrice) {
        this.couponBuyPrice = couponBuyPrice;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Boolean getVoucher() {
        return isVoucher;
    }

    public void setVoucher(Boolean voucher) {
        isVoucher = voucher;
    }

    public String getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(String couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public String getDealBeginTime() {
        return dealBeginTime;
    }

    public void setDealBeginTime(String dealBeginTime) {
        this.dealBeginTime = dealBeginTime;
    }

    public Integer getDealId() {
        return dealId;
    }

    public void setDealId(Integer dealId) {
        this.dealId = dealId;
    }

    public Double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(Double dealPrice) {
        this.dealPrice = dealPrice;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public Double getDealValue() {
        return dealValue;
    }

    public void setDealValue(Double dealValue) {
        this.dealValue = dealValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMinConsume() {
        return minConsume;
    }

    public void setMinConsume(Integer minConsume) {
        this.minConsume = minConsume;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getUseCount() {
        return UseCount;
    }

    public void setUseCount(Integer useCount) {
        UseCount = useCount;
    }

    public Integer getCancelCount() {
        return CancelCount;
    }

    public void setCancelCount(Integer cancelCount) {
        CancelCount = cancelCount;
    }

    public MeituanCoupon() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EnterpriseInfoGUID);
        dest.writeString(this.StoreGuid);
        dest.writeString(this.SalesOrderPaymentGUID);
        dest.writeValue(this.count);
        dest.writeValue(this.couponBuyPrice);
        dest.writeString(this.couponCode);
        dest.writeValue(this.isVoucher);
        dest.writeString(this.couponEndTime);
        dest.writeString(this.dealBeginTime);
        dest.writeValue(this.dealId);
        dest.writeValue(this.dealPrice);
        dest.writeString(this.dealTitle);
        dest.writeValue(this.dealValue);
        dest.writeString(this.message);
        dest.writeValue(this.minConsume);
        dest.writeValue(this.result);
        dest.writeValue(this.UseCount);
        dest.writeValue(this.CancelCount);
    }

    protected MeituanCoupon(Parcel in) {
        this.EnterpriseInfoGUID = in.readString();
        this.StoreGuid = in.readString();
        this.SalesOrderPaymentGUID = in.readString();
        this.count = (Integer) in.readValue(Integer.class.getClassLoader());
        this.couponBuyPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.couponCode = in.readString();
        this.isVoucher = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.couponEndTime = in.readString();
        this.dealBeginTime = in.readString();
        this.dealId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dealPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.dealTitle = in.readString();
        this.dealValue = (Double) in.readValue(Double.class.getClassLoader());
        this.message = in.readString();
        this.minConsume = (Integer) in.readValue(Integer.class.getClassLoader());
        this.result = (Integer) in.readValue(Integer.class.getClassLoader());
        this.UseCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CancelCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<MeituanCoupon> CREATOR = new Creator<MeituanCoupon>() {
        @Override
        public MeituanCoupon createFromParcel(Parcel source) {
            return new MeituanCoupon(source);
        }

        @Override
        public MeituanCoupon[] newArray(int size) {
            return new MeituanCoupon[size];
        }
    };
}
