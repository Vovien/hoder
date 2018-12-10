package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.List;

/**
 * 订单点菜记录
 * Created by Administrator on 2017/1/10.
 */

public class SalesOrderBatchRecord {

    /**
     * 加菜批次菜品主键
     */
    private String SalesOrderBatchDishesGUID;
    /**
     * 菜品名称
     */
    private String DishesName;
    /**
     * 下单数量 正数是加菜，负数是退菜
     */
    private Double OrderCount;

    /**
     * 制作数量
     */
    private Double MakeCount;

    /**
     * 上菜数量
     */
    private Double ServingCount;

    /**
     * 退菜数量
     */
    private Double BackCount;

    /**
     * 赠品标识 0=非赠送 1=赠送
     */
    private int Gift;

    /**
     * 价单
     */
    private Double Price;

    private String tableInfo;
    /**
     * 点单人信息
     */
    private String User;
    private String time;
    private double amount;



    /**
     * 此菜品若为套餐项目，则此字段为套餐子项集合
     */
    private List<SalesOrderBatchDishes> ArrayOfSalesOrderBatchDishes;


    /**
     * 是否为套餐菜品
     * 0：普通菜品
     * 1：套餐
     */
    private Integer IsPackageDishes;


    public String getSalesOrderBatchDishesGUID() {
        return SalesOrderBatchDishesGUID;
    }

    public void setSalesOrderBatchDishesGUID(String salesOrderBatchDishesGUID) {
        SalesOrderBatchDishesGUID = salesOrderBatchDishesGUID;
    }

    public String getDishesName() {
        return DishesName;
    }

    public void setDishesName(String dishesName) {
        DishesName = dishesName;
    }

    public Double getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(Double orderCount) {
        OrderCount = orderCount;
    }

    public Double getMakeCount() {
        return MakeCount;
    }

    public void setMakeCount(Double makeCount) {
        MakeCount = makeCount;
    }

    public Double getServingCount() {
        return ServingCount;
    }

    public void setServingCount(Double servingCount) {
        ServingCount = servingCount;
    }

    public Double getBackCount() {
        return BackCount;
    }

    public void setBackCount(Double backCount) {
        BackCount = backCount;
    }

    public int getGift() {
        return Gift;
    }

    public void setGift(int gift) {
        Gift = gift;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(String tableInfo) {
        this.tableInfo = tableInfo;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<SalesOrderBatchDishes> getArrayOfSalesOrderBatchDishes() {
        return ArrayOfSalesOrderBatchDishes;
    }

    public void setArrayOfSalesOrderBatchDishes(List<SalesOrderBatchDishes> arrayOfSalesOrderBatchDishes) {
        ArrayOfSalesOrderBatchDishes = arrayOfSalesOrderBatchDishes;
    }

    public Integer getIsPackageDishes() {
        return IsPackageDishes;
    }

    public void setIsPackageDishes(Integer isPackageDishes) {
        IsPackageDishes = isPackageDishes;
    }
}
