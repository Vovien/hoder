package com.holderzone.intelligencepos.mvp.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/17.
 */

public class SalesOrderDishesBack {
    /**
     * 数据标识
     */

    public String SalesOrderDishesBackGUID;
    /**
     * 对应下单批次菜品记录明细
     */
    public String SalesOrderBatchDishesGUID;

    /**
     * 对应的下单记录实体
     */
    public SalesOrderBatchDishes SalesOrderBatchDishesEntity;
    /**
     * 销售单主表标识
     */
    public String SalesOrderGUID;
    /**
     * 餐桌标识
     */
    public String DiningTableGUID;
    /**
     * 餐桌
     */
    public DiningTable DiningTable;

    /**
     * 退菜数量
     */
    public Double DishesBackCount;
    /**
     * 退菜备注
     */
    public String Remark;
    /**
     * 退菜时间
     */
    public String BackTime;

    /**
     * 操作员工标识
     */
    public String StaffGUID;

    /**
     * 原始消费单ID
     */
    public String OrgSalesOrderGUID;
    /**
     * 菜品信息
     */
    public Dishes Dishes;

    public List<SalesOrderDishesBack> ArrayOfDishesBack;
}
