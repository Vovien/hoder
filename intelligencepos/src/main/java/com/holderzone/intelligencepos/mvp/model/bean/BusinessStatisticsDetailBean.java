package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by LiTao on 2017-5-11.
 */

public class BusinessStatisticsDetailBean {
    /**
     * 操作员名字
     */
    private String StaffName;
    /**
     * 上菜数量
     */
    private Double CheckCount;
    /**
     * 菜品单价
     */
    private Double Price;
    /**
     * 操作 时间
     */
    private String BatchTime;
    /**
     * 交易流水号
     */
    private String BatchNumber;
    /**
     * 菜品名称
     */
    private String DishName;
    /**
     * 菜品编号
     */
    private String DishCode;
    /**
     * 小计
     */
    private Double ItemTotal;
    /**
     * 总计
     */
    private Double Total;
    /**
     * 退菜数量
     */
    private Double BackCount;
    /**
     * 退菜原因
     */
    private String ReturnReason;
    /**
     * 赠品标识 0=非赠送 1=赠送
     */

    public int Gift;
    private int CodeNumber;

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getDishCode() {
        return DishCode;
    }

    public void setDishCode(String dishCode) {
        DishCode = dishCode;
    }

    public String getStaffName() {
        return StaffName;
    }

    public void setStaffName(String staffName) {
        StaffName = staffName;
    }


    public Double getCheckCount() {
        return CheckCount;
    }

    public void setCheckCount(Double checkCount) {
        CheckCount = checkCount;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getBatchTime() {
        return BatchTime;
    }

    public void setBatchTime(String batchTime) {
        BatchTime = batchTime;
    }

    public String getBatchNumber() {
        return BatchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        BatchNumber = batchNumber;
    }

    public Double getItemTotal() {
        return ItemTotal;
    }

    public void setItemTotal(Double itemTotal) {
        ItemTotal = itemTotal;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public Double getBackCount() {
        return BackCount;
    }

    public void setBackCount(Double backCount) {
        BackCount = backCount;
    }

    public String getReturnReason() {
        return ReturnReason;
    }

    public void setReturnReason(String returnReason) {
        ReturnReason = returnReason;
    }

    public int getGift() {
        return Gift;
    }

    public void setGift(int gift) {
        Gift = gift;
    }

    public int getCodeNumber() {
        return CodeNumber;
    }

    public void setCodeNumber(int codeNumber) {
        CodeNumber = codeNumber;
    }
}
