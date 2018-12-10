package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.List;

/**
 * Created by LT on 2018-04-02.
 */

public class UnOrderDishesE extends UnOrderDishes {
    /**
     * 商家标识
     */
    private String EnterpriseInfoGUID;
    /**
     * 做法合计金额
     */
    private Double PracticePrice;
    /**
     * 换菜后总金额
     */
    private Double ChangeSubTotal;
    /**
     * 菜品做法名字（逗号分隔）
     */
    private String DishesPracticeContent;
    /**
     * 最终出餐菜品
     */
    private DishesE DishesE;
    /**
     * 1为微信
     */
    private Integer OrderType;
    List<UnOrderDishesPracticeE> ArrayOfUnOrderDishesPracticeE;

    public String getEnterpriseInfoGUID() {
        return EnterpriseInfoGUID;
    }

    public void setEnterpriseInfoGUID(String enterpriseInfoGUID) {
        EnterpriseInfoGUID = enterpriseInfoGUID;
    }

    public Double getPracticePrice() {
        return PracticePrice;
    }

    public void setPracticePrice(Double practicePrice) {
        PracticePrice = practicePrice;
    }

    public Double getChangeSubTotal() {
        return ChangeSubTotal;
    }

    public void setChangeSubTotal(Double changeSubTotal) {
        ChangeSubTotal = changeSubTotal;
    }

    public String getDishesPracticeContent() {
        return DishesPracticeContent;
    }

    public void setDishesPracticeContent(String dishesPracticeContent) {
        DishesPracticeContent = dishesPracticeContent;
    }

    public com.holderzone.intelligencepos.mvp.model.bean.DishesE getDishesE() {
        return DishesE;
    }

    public void setDishesE(com.holderzone.intelligencepos.mvp.model.bean.DishesE dishesE) {
        DishesE = dishesE;
    }

    public Integer getOrderType() {
        return OrderType;
    }

    public void setOrderType(Integer orderType) {
        OrderType = orderType;
    }

    public List<UnOrderDishesPracticeE> getArrayOfUnOrderDishesPracticeE() {
        return ArrayOfUnOrderDishesPracticeE;
    }

    public void setArrayOfUnOrderDishesPracticeE(List<UnOrderDishesPracticeE> arrayOfUnOrderDishesPracticeE) {
        ArrayOfUnOrderDishesPracticeE = arrayOfUnOrderDishesPracticeE;
    }
}
