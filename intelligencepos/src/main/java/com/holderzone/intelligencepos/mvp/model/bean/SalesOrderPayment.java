package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 订单支付表
 * Created by Administrator on 2017/3/13.
 */

public class SalesOrderPayment {
    /**
     * 操作人
     */
    private String CreateStaffGUID;

    /**
     * 订单标识
     */
    private String SalesOrderGUID;

    /**
     * 应付金额
     */
    private Double PayableAmount;

    /**
     * 实付金额
     */
    private Double ActuallyAmount;

    /**
     * 付款方式代码
     */
    private String PaymentItemCode;

    /**
     * 支付状态  -1=作废，1=付款完成，
     */
    private Integer PaymentSTAT;

    public String getCreateStaffGUID() {
        return CreateStaffGUID;
    }

    public void setCreateStaffGUID(String createStaffGUID) {
        CreateStaffGUID = createStaffGUID;
    }

    public String getSalesOrderGUID() {
        return SalesOrderGUID;
    }

    public void setSalesOrderGUID(String salesOrderGUID) {
        SalesOrderGUID = salesOrderGUID;
    }

    public Double getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(Double payableAmount) {
        PayableAmount = payableAmount;
    }

    public Double getActuallyAmount() {
        return ActuallyAmount;
    }

    public void setActuallyAmount(Double actuallyAmount) {
        ActuallyAmount = actuallyAmount;
    }

    public String getPaymentItemCode() {
        return PaymentItemCode;
    }

    public void setPaymentItemCode(String paymentItemCode) {
        PaymentItemCode = paymentItemCode;
    }

    public Integer getPaymentSTAT() {
        return PaymentSTAT;
    }

    public void setPaymentSTAT(Integer paymentSTAT) {
        PaymentSTAT = paymentSTAT;
    }
}
