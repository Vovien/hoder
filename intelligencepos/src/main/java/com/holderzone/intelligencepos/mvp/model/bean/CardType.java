package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 卡类型
 * Created by NEW on 2017/4/19.
 */

public class CardType {
    /**
     * 编号
     */
    private String CardTypeUID;
    /**
     * 卡主键
     */
    private String CardTypeGUID;
    /**
     * 卡名称
     */
    private String CardTypeName;
    /**
     * 是否为记名卡（是否需要绑定会员账户）
     * 0：不记名卡
     * 1：记名卡
     */
    private Integer NeedBind;
    /**
     * 卡批次,外键
     */
    private String CardCreateRecordGUID;
    /**
     * 是否为实物卡
     * 0：电子卡
     * 1：实物卡
     */
    private Integer IsPhysical;
    /**
     * 是否启用
     */
    private  Integer IsEnable;
    /**
     * 是否过期
     * 0:过期
     * 1:未过期
     */
    private Integer IsAvailable;
    /**
     * 面值
     */
    private Double FaceValue;
    /**
     * 价格
     */
    private Double Price;
    /**
     * 支付折扣
     */
    private Double PaymentCiscount;
    /**
     * 权益折扣
     */
    private Double RightsCiscount;
    /**
     * 充值类型
     * 0：非充值卡
     * 1：任意额充值
     * 2：定额充值
     */
    private Integer RechargeType;
    /**
     * 是否启用支付折扣 1 启用  0 不启用
     */
    private Integer IsPaymentCiscount;

    public String getCardTypeUID() {
        return CardTypeUID;
    }

    public void setCardTypeUID(String cardTypeUID) {
        CardTypeUID = cardTypeUID;
    }

    public String getCardTypeGUID() {
        return CardTypeGUID;
    }

    public void setCardTypeGUID(String cardTypeGUID) {
        CardTypeGUID = cardTypeGUID;
    }

    public String getCardTypeName() {
        return CardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        CardTypeName = cardTypeName;
    }

    public Integer getNeedBind() {
        return NeedBind;
    }

    public void setNeedBind(Integer needBind) {
        NeedBind = needBind;
    }

    public String getCardCreateRecordGUID() {
        return CardCreateRecordGUID;
    }

    public void setCardCreateRecordGUID(String cardCreateRecordGUID) {
        CardCreateRecordGUID = cardCreateRecordGUID;
    }

    public Integer getIsPhysical() {
        return IsPhysical;
    }

    public void setIsPhysical(Integer isPhysical) {
        IsPhysical = isPhysical;
    }

    public Integer getIsEnable() {
        return IsEnable;
    }

    public void setIsEnable(Integer isEnable) {
        IsEnable = isEnable;
    }

    public Integer getIsAvailable() {
        return IsAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        IsAvailable = isAvailable;
    }

    public Double getFaceValue() {
        return FaceValue;
    }

    public void setFaceValue(Double faceValue) {
        FaceValue = faceValue;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getPaymentCiscount() {
        return PaymentCiscount;
    }

    public void setPaymentCiscount(Double paymentCiscount) {
        PaymentCiscount = paymentCiscount;
    }

    public Double getRightsCiscount() {
        return RightsCiscount;
    }

    public void setRightsCiscount(Double rightsCiscount) {
        RightsCiscount = rightsCiscount;
    }

    public Integer getRechargeType() {
        return RechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        RechargeType = rechargeType;
    }

    public Integer getIsPaymentCiscount() {
        return IsPaymentCiscount;
    }

    public void setIsPaymentCiscount(Integer isPaymentCiscount) {
        IsPaymentCiscount = isPaymentCiscount;
    }
}
