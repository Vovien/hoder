package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by NEW on 2017/4/11.
 */

public class ViewCardBusinessOrderE {
    /**
     * 门店名称
     */
    private String StoreName;
    /**
     * 发生时间
     */
    private String AddTime;
    /**
     * 充值金额   充值业务数据使用
     */
    private double RechargeAmount;
    /**
     * 充值赠送金额   充值业务数据使用
     */
    private double RechargeGiveAmount;
    /**
     * 刷卡应收金额   刷卡业务数据使用
     */
    private double ReceiveAmount;
    /**
     * 实际刷卡金额   刷卡业务数据使用
     */
    private double ActualAmount;
    /**
     * 刷卡折扣金额   刷卡业务数据使用
     */
    private double DiscountAmount;
    /**
     * 卡余额   本次发生业务后的余额
     */
    private double CardsBalance;
    /**
     * 记录类型   1=充值  -1=刷卡
     */
    private int PayAmountType;
    /**
     * 合计(赠送+售价)
     */
    private Double TotalAmount;

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public double getRechargeAmount() {
        return RechargeAmount;
    }

    public void setRechargeAmount(double rechargeAmount) {
        RechargeAmount = rechargeAmount;
    }

    public double getRechargeGiveAmount() {
        return RechargeGiveAmount;
    }

    public void setRechargeGiveAmount(double rechargeGiveAmount) {
        RechargeGiveAmount = rechargeGiveAmount;
    }

    public double getReceiveAmount() {
        return ReceiveAmount;
    }

    public void setReceiveAmount(double receiveAmount) {
        ReceiveAmount = receiveAmount;
    }

    public double getActualAmount() {
        return ActualAmount;
    }

    public void setActualAmount(double actualAmount) {
        ActualAmount = actualAmount;
    }

    public double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        DiscountAmount = discountAmount;
    }

    public double getCardsBalance() {
        return CardsBalance;
    }

    public void setCardsBalance(double cardsBalance) {
        CardsBalance = cardsBalance;
    }

    public int getPayAmountType() {
        return PayAmountType;
    }

    public void setPayAmountType(int payAmountType) {
        PayAmountType = payAmountType;
    }
}
