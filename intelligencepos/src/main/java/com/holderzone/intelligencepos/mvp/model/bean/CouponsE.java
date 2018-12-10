package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 券扩展实体
 * Created by zhaoping on 2017/4/24.
 */

public class CouponsE {
    private boolean selected;
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 门店标识
     */
    private String StoreGUID;
    /**
     * 所属会员标识
     */
    private String MemberInfoGUID;

    /**
     * 券标识
     */
    private String CouponsGUID;
    /**
     * 券编码
     */
    private String CouponsNumber;
    /***
     * 是否使用
     *0=未使用 1=使用
     */
    private Integer IsUse;
    /**是否进行使用校验  0=不进行校验操作  1=需要进行校验*/
    private Integer UseCheck;
    /**是否返回已使用的券 0=不返回  1=返回*/
    private Integer ResultBeUse;
    /***
     * 启用状态
     */
    private Integer IsEnable;
    /**不满足条件时，具体的使用时间异常说明	-1=已过期  0=可使用  1=未生效*/
    private Integer UseCondition_UseTime;
    /**券是否满足当前使用条件  UseCheck=1：进行校验后返回此参数  -1=未校验  0=不符合条件  1=满足
     */
    private Integer IsAvaliableUseCondition;
    /***
     * 券名称
     */
    private String CouponTypeName;
    /**
     * 生效时间
     */
    private String EffectTime;
    /**
     * 失效时间
     */
    private String InvalidTime;
    /**
     * 账单标识
     */
    private String SalesOrderGUID;
    /**
     * 券类型
     */
    private CouponTypeE CouponTypeE;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public Integer getUseCondition_UseTime() {
        return UseCondition_UseTime;
    }

    public void setUseCondition_UseTime(Integer useCondition_UseTime) {
        UseCondition_UseTime = useCondition_UseTime;
    }

    public String getStoreGUID() {
        return StoreGUID;
    }

    public void setStoreGUID(String storeGUID) {
        StoreGUID = storeGUID;
    }

    public Integer getUseCheck() {
        return UseCheck;
    }

    public void setUseCheck(Integer useCheck) {
        UseCheck = useCheck;
    }

    public String getMemberInfoGUID() {
        return MemberInfoGUID;
    }

    public void setMemberInfoGUID(String memberInfoGUID) {
        MemberInfoGUID = memberInfoGUID;
    }

    public boolean isSelected() {

        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCouponsGUID() {
        return CouponsGUID;
    }

    public void setCouponsGUID(String couponsGUID) {
        CouponsGUID = couponsGUID;
    }

    public String getCouponsNumber() {
        return CouponsNumber;
    }

    public void setCouponsNumber(String couponsNumber) {
        CouponsNumber = couponsNumber;
    }

    public Integer getResultBeUse() {
        return ResultBeUse;
    }

    public void setResultBeUse(Integer resultBeUse) {
        ResultBeUse = resultBeUse;
    }

    public Integer getIsUse() {
        return IsUse;
    }

    public void setIsUse(Integer isUse) {
        IsUse = isUse;
    }

    public Integer getIsEnable() {
        return IsEnable;
    }

    public void setIsEnable(Integer isEnable) {
        IsEnable = isEnable;
    }

    public Integer getIsAvaliableUseCondition() {
        return IsAvaliableUseCondition;
    }

    public void setIsAvaliableUseCondition(Integer isAvaliableUseCondition) {
        IsAvaliableUseCondition = isAvaliableUseCondition;
    }

    public String getCouponTypeName() {
        return CouponTypeName;
    }

    public void setCouponTypeName(String couponTypeName) {
        CouponTypeName = couponTypeName;
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

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public CouponTypeE getCouponTypeE() {
        return CouponTypeE;
    }

    public void setCouponTypeE(CouponTypeE couponTypeE) {
        CouponTypeE = couponTypeE;
    }
}
