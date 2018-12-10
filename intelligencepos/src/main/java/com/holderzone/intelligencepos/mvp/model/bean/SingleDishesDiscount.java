package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 对单实体
 * Created by Administrator on 2017/4/7.
 */

public class SingleDishesDiscount {
    private String dishesName;
    /**
     * 单价
     */
    private double price;
    /**
     * 做法附加费用
     */
    private Double PracticeSubTotal;
    /**
     * 会员价
     */
    private Double MemberPrice;
    /**
     * 折后单价
     */
    private double newPrice;
    private Double DiscountPrice;
    private String packageDishesString;
    private String cookMethodString;
    /**
     * 赠品标识 0=非赠送 1=赠送
     */
    private Integer gift;
    private String time;
    private String title;
    private String salesOrderBatchDishesGUID;
    private Double checkCount;
    /**
     * 所属批次标识
     */
    private String SalesOrderBatchGUID;

    public String getSalesOrderBatchGUID() {
        return SalesOrderBatchGUID;
    }

    public void setSalesOrderBatchGUID(String salesOrderBatchGUID) {
        SalesOrderBatchGUID = salesOrderBatchGUID;
    }

    public Double getDiscountPrice() {
        return DiscountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        DiscountPrice = discountPrice;
    }

    public String getDishesName() {
        return dishesName;
    }

    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getMemberPrice() {
        return MemberPrice;
    }

    public void setMemberPrice(Double MemberPrice) {
        this.MemberPrice = MemberPrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getCheckCount() {
        return checkCount;
    }

    public Double getPracticeSubTotal() {
        return PracticeSubTotal;
    }

    public void setPracticeSubTotal(Double practiceSubTotal) {
        PracticeSubTotal = practiceSubTotal;
    }

    public void setCheckCount(Double checkCount) {
        this.checkCount = checkCount;
    }

    public String getPackageDishesString() {
        return packageDishesString;
    }

    public void setPackageDishesString(String packageDishesString) {
        this.packageDishesString = packageDishesString;
    }

    public String getCookMethodString() {
        return cookMethodString;
    }

    public void setCookMethodString(String cookMethodString) {
        this.cookMethodString = cookMethodString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalesOrderBatchDishesGUID() {
        return salesOrderBatchDishesGUID;
    }

    public void setSalesOrderBatchDishesGUID(String salesOrderBatchDishesGUID) {
        this.salesOrderBatchDishesGUID = salesOrderBatchDishesGUID;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public Integer getGift() {
        return gift;
    }

    public void setGift(Integer gift) {
        this.gift = gift;
    }
}
