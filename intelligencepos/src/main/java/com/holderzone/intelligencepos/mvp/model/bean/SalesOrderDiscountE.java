package com.holderzone.intelligencepos.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 折扣表
 * Created by Administrator on 2017/3/10.
 */
public class SalesOrderDiscountE implements Parcelable {
    /**
     * 折扣项目，中文名称
     */
    private String DiscountItem;
    /**
     * 折扣金额
     */
    private Double DiscountAmount;

    /**
     * 折扣类型
     * 0     系统省零
     * 10     整单折扣
     * 11     会员权益整单折扣
     * 20     优惠活动
     * 30     优惠券
     * 40     菜品赠送
     * 50     省零（人工）
     * 60     外卖平台折扣     商家承担的部分
     * 70     会员卡支付折扣
     * 80     单品折扣
     * 90     美团劵折扣
     */
    private Integer DiscountType;

    public String getDiscountItem() {
        return DiscountItem;
    }

    public void setDiscountItem(String discountItem) {
        DiscountItem = discountItem;
    }

    public Double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        DiscountAmount = discountAmount;
    }

    public Integer getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(Integer discountType) {
        DiscountType = discountType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DiscountItem);
        dest.writeValue(this.DiscountAmount);
        dest.writeValue(this.DiscountType);
    }

    public SalesOrderDiscountE() {
    }

    protected SalesOrderDiscountE(Parcel in) {
        this.DiscountItem = in.readString();
        this.DiscountAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.DiscountType = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<SalesOrderDiscountE> CREATOR = new Creator<SalesOrderDiscountE>() {
        @Override
        public SalesOrderDiscountE createFromParcel(Parcel source) {
            return new SalesOrderDiscountE(source);
        }

        @Override
        public SalesOrderDiscountE[] newArray(int size) {
            return new SalesOrderDiscountE[size];
        }
    };
}
