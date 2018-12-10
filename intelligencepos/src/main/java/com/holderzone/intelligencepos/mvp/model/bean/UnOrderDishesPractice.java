package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by LT on 2018-04-02.
 */

public class UnOrderDishesPractice {
    /**数据标识*/
    private String UnOrderDishesPracticeGUID ;

    /**订单明细GUID*/
    private String UnOrderDishesGUID ;

    /**菜品做法GUID*/
    private String DishesPracticeGUID ;

    /**制作费用*/
    private Double Fees ;

    /**费用数量*/
    private Integer FeesCount ;

    /**小计*/
    private Double SubTotal ;

    public String getUnOrderDishesPracticeGUID() {
        return UnOrderDishesPracticeGUID;
    }

    public void setUnOrderDishesPracticeGUID(String unOrderDishesPracticeGUID) {
        UnOrderDishesPracticeGUID = unOrderDishesPracticeGUID;
    }

    public String getUnOrderDishesGUID() {
        return UnOrderDishesGUID;
    }

    public void setUnOrderDishesGUID(String unOrderDishesGUID) {
        UnOrderDishesGUID = unOrderDishesGUID;
    }

    public String getDishesPracticeGUID() {
        return DishesPracticeGUID;
    }

    public void setDishesPracticeGUID(String dishesPracticeGUID) {
        DishesPracticeGUID = dishesPracticeGUID;
    }

    public Double getFees() {
        return Fees;
    }

    public void setFees(Double fees) {
        Fees = fees;
    }

    public Integer getFeesCount() {
        return FeesCount;
    }

    public void setFeesCount(Integer feesCount) {
        FeesCount = feesCount;
    }

    public Double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(Double subTotal) {
        SubTotal = subTotal;
    }
}
