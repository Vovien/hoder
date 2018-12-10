package com.holderzone.intelligencepos.mvp.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by tcw10 on 2017/3/16.
 */
public class DishesEstimateRecordDishes implements Serializable {
    public static final long serialVersionUID = 46567573;
    /**
     * 菜品标识
     */
    private String DishesGUID;
    /**
     * 估清数量
     */
    private Double EstimateCount;

    private String DishesName;
    /**
     * 0=停售  1=正常销售中
     */
    private Integer SalesStatus;
    /**
     * 当前订单点单数量
     */
    private Double currentOrderCount;
    /**
     * 剩余菜品数量
     */
    private Double residueCount;

    /**
     * 警告数量
     */
    private Double WarningCount;

    /**
     * 销售数量
     */
    private Double SaleCount;

    public String getDishesGUID() {
        return DishesGUID;
    }

    public void setDishesGUID(String dishesGUID) {
        DishesGUID = dishesGUID;
    }

    public Double getEstimateCount() {
        return EstimateCount;
    }

    public void setEstimateCount(Double estimateCount) {
        EstimateCount = estimateCount;
    }

    public Double getWarningCount() {
        return WarningCount;
    }

    public void setWarningCount(Double warningCount) {
        WarningCount = warningCount;
    }

    public String getDishesName() {
        return DishesName;
    }

    public void setDishesName(String dishesName) {
        DishesName = dishesName;
    }

    public Integer getSalesStatus() {
        return SalesStatus;
    }

    public void setSalesStatus(Integer salesStatus) {
        SalesStatus = salesStatus;
    }

    public Double getSaleCount() {
        return SaleCount;
    }

    public void setSaleCount(Double saleCount) {
        SaleCount = saleCount;
    }

    public Double getCurrentOrderCount() {
        return currentOrderCount;
    }

    public void setCurrentOrderCount(Double currentOrderCount) {
        this.currentOrderCount = currentOrderCount;
    }

    public Double getResidueCount() {
        return residueCount;
    }

    public void setResidueCount(Double residueCount) {
        this.residueCount = residueCount;
    }
}
