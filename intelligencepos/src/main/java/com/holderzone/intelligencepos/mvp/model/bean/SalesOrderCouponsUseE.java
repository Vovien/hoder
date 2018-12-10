package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by LiTao on 2017-5-11.
 */

public class SalesOrderCouponsUseE {
    /**
     * 优惠券名称
     */
    private String CouponTypeName;
    /**
     * 面值
     */
    private Double Amount;
    /**
     * 数量
     */
    private Integer CouponsCount;
    /**
     * 小计
     */
    private Double Total;

    public String getCouponTypeName() {
        return CouponTypeName;
    }

    public void setCouponTypeName(String couponTypeName) {
        CouponTypeName = couponTypeName;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public Integer getCouponsCount() {
        return CouponsCount;
    }

    public void setCouponsCount(Integer couponsCount) {
        CouponsCount = couponsCount;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }
}
