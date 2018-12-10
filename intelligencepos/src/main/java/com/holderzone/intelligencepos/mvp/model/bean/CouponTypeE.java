package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 券类型扩展实体
 * Created by zhaoping on 2017/4/24.
 */

public class CouponTypeE {
    /***/
    private String CouponTypeGUID;
    /**券名称*/
    private String CouponTypeName;
    /**抵扣金额*/
    private Double Amount;
    /**使用条件生效时间*/
    private String EffectTime;
    /**使用条件失效时间*/
    private String InvalidTime;
    /**使用条件券生效时长（小时）	领券之时起*/
    private Integer EffectDuration;
    /**使用条件券失效时长（小时）	生效之时起*/
    private Integer InvalidDuration;
    /**备注*/
    private String Remark;
    /**
     * 0=现金券
     * 1 = 菜品抵扣券
     */
    private Integer DeductType;

    public String getCouponTypeGUID() {
        return CouponTypeGUID;
    }

    public void setCouponTypeGUID(String couponTypeGUID) {
        CouponTypeGUID = couponTypeGUID;
    }

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

    public String getEffectTime() {
        return EffectTime;
    }

    public void setEffectTime(String effectTime) {
        EffectTime = effectTime;
    }

    public String getInvalidTime() {
        return InvalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        InvalidTime = invalidTime;
    }

    public Integer getEffectDuration() {
        return EffectDuration;
    }

    public void setEffectDuration(Integer effectDuration) {
        EffectDuration = effectDuration;
    }

    public Integer getInvalidDuration() {
        return InvalidDuration;
    }

    public void setInvalidDuration(Integer invalidDuration) {
        InvalidDuration = invalidDuration;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public Integer getDeductType() {
        return DeductType;
    }

    public void setDeductType(Integer deductType) {
        DeductType = deductType;
    }
}
