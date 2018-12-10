package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CashCouponModel implements Parcelable {
    /**
     * 券类型 （1 折扣卷; 2 代金券; 3 兑换券）
     */
    private String type;
    /**
     * 券名称
     */
    private String title;
    private String startTime;
    private String endTime;
    /**
     * 二维码
     */
    private String qrCode;

    private String barCode;
    /**
     * 状态 （0可使用、-1已失效 1已使用）
     */
    private String status;
    /**
     * 抵用金额
     */
    private Double vouchers;
    /**
     * 折扣
     */
    private Double discount;
    /**
     * 是否选中
     */
    private Boolean selected;
    /**
     *  是否优惠共享
     *  true 可以优惠共享
     */
    private Boolean isShare;

    public Boolean getShare() {
        return isShare;
    }

    public void setShare(Boolean share) {
        isShare = share;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getVouchers() {
        return vouchers;
    }

    public void setVouchers(Double vouchers) {
        this.vouchers = vouchers;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CashCouponModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.title);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.qrCode);
        dest.writeString(this.barCode);
        dest.writeString(this.status);
        dest.writeValue(this.vouchers);
        dest.writeValue(this.discount);
        dest.writeValue(this.selected);
        dest.writeValue(this.isShare);
    }

    protected CashCouponModel(Parcel in) {
        this.type = in.readString();
        this.title = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.qrCode = in.readString();
        this.barCode = in.readString();
        this.status = in.readString();
        this.vouchers = (Double) in.readValue(Double.class.getClassLoader());
        this.discount = (Double) in.readValue(Double.class.getClassLoader());
        this.selected = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isShare = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<CashCouponModel> CREATOR = new Creator<CashCouponModel>() {
        @Override
        public CashCouponModel createFromParcel(Parcel source) {
            return new CashCouponModel(source);
        }

        @Override
        public CashCouponModel[] newArray(int size) {
            return new CashCouponModel[size];
        }
    };
}
