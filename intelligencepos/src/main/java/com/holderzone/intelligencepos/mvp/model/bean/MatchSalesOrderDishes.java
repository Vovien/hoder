package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * 对单实体
 * Created by Administrator on 2017/4/7.
 */

public class MatchSalesOrderDishes {
    private String dishesName;
    private double price;
    private String time;
    private String title;
    /**
     * 赠品标识 0=非赠送 1=赠送
     */
    private Integer gift;

    /**
     * 所属批次标识
     */
    private String SalesOrderBatchGUID;
    private String salesOrderBatchDishesGUID;
    private String packageDishesString;
    private String cookMethodString;
    private Double checkCount;
    private Double ConfirmCount;
    /**对单数量*/
    private Double ReviewCheckCount;
    /**
     * 会员价
     */
    private Double MemberPrice;

    /**
     * 用来保存默认的赠送状态 0=非赠送 1=赠送
     */
    private Integer defaultGift;

    public Integer getDefaultGift() {
        return defaultGift;
    }

    public String getSalesOrderBatchGUID() {
        return SalesOrderBatchGUID;
    }

    public void setSalesOrderBatchGUID(String salesOrderBatchGUID) {
        SalesOrderBatchGUID = salesOrderBatchGUID;
    }

    public void setDefaultGift(Integer defaultGift) {
        this.defaultGift = defaultGift;
    }

    public Double getReviewCheckCount() {
        return ReviewCheckCount;
    }

    public void setReviewCheckCount(Double reviewCheckCount) {
        ReviewCheckCount = reviewCheckCount;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Double checkCount) {
        this.checkCount = checkCount;
    }

    public Double getConfirmCount() {
        return ConfirmCount;
    }

    public void setConfirmCount(Double confirmCount) {
        ConfirmCount = confirmCount;
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

    public Integer getGift() {
        return gift;
    }

    public void setGift(Integer gift) {
        this.gift = gift;
    }

    public Double getMemberPrice() {
        return MemberPrice;
    }

    public void setMemberPrice(Double memberPrice) {
        MemberPrice = memberPrice;
    }
}
